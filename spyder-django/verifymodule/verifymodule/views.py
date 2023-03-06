import base64
import os
import time
import uuid

from django.http import JsonResponse, HttpResponse
import ddddocr
from PIL import Image
from lxml import etree
from playwright.sync_api import Page, expect, sync_playwright


# 只解析 星期一(14-14小节 这样的字符串
def parse_single(s: str) -> list[int]:
    day = s.split('(')[0]
    if day == '星期一':
        day_res = 1
    elif day == '星期二':
        day_res = 2
    elif day == '星期三':
        day_res = 3
    elif day == '星期四':
        day_res = 4
    elif day == '星期五':
        day_res = 5
    elif day == '星期六':
        day_res = 6
    else:
        day_res = 7

    # count 是 08-10
    count = s.split('(')[1].split(')')[0].split('小节')[0]
    lesson_start = int(count.split('-')[0])
    lesson_count = int(count.split('-')[1]) - int(count.split('-')[0]) + 1
    return [day_res, lesson_start, lesson_count]


# 解析类似 星期一(14-14小节) 或 星期二(08-10小节) 或 星期一(14-14小节)星期二(08-10小节) 的字符串
# 返回三个数 [1, 8, 2] 表示周一，第8小节，2节课
def parse_time(s: str) -> list[list[int]]:
    res = []
    if s.count('星期') == 1:
        res.append(parse_single(s))

    # 星期一(14-14小节)星期二(08-10小节)
    elif s.count('星期') == 2:
        one = s.rsplit('星期', maxsplit=1)[0]
        two = s.split(')', maxsplit=1)[1]
        res.append(parse_single(one))
        res.append(parse_single(two))

    return res


# 解析类似 1-16(周) 或 1(周) 的字符串
def parse_week(s: str) -> list[int]:
    # num 是 1-16 或 1 这种形式
    num = s.split('(')[0]
    if '-' in num:
        start_week = int(num.split('-')[0])
        end_week = int(num.split('-')[1])
        return [start_week, end_week]
    else:
        return [int(num), int(num)]


def parse_final_table(my_course: list[list[dict]], courses: dict):
    for name, data in courses.items():
        start_week, end_week = data['week']
        # 星期下标偏移
        for week in range(start_week - 1, end_week):
            # 某一周可能有多节课
            for idx in range(len(data['time'])):
                my_course[week].append({
                    'name': name,
                    'code': data['code'],
                    'order': data['order'],
                    'teacher': data['teacher'],
                    'day': data['time'][idx][0],
                    'lesson_start': data['time'][idx][1],
                    'lesson_count': data['time'][idx][2],
                    'score': data['score'],
                    'room': data['room'][idx] if len(data['room']) != 0 else '',
                    'type': data['type'],
                    'stage': data['stage']
                })


# 优秀->90 良好->80 ...
def score_str2float(score):
    if score == "优秀":
        score = 90
    elif score == "良好":
        score = 80
    elif score == "中等":
        score = 70
    elif score == "及格":
        score = 60
    elif score == "不及格":
        score = 59
    return score


# 根据成绩获取gpa
def get_gpa(score) -> float:
    score = float(score)
    if score <= 59:
        return 0
    if score <= 63:
        return 1
    if score <= 67:
        return 1.5
    if score <= 71:
        return 2
    if score <= 74:
        return 2.3
    if score <= 77:
        return 2.7
    if score <= 81:
        return 3.0
    if score <= 84:
        return 3.3
    if score <= 89:
        return 3.7
    return 4.0


def login_jwc(username: str, password: str):
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        context = browser.new_context()
        page = context.new_page()
        # 休息2s
        time.sleep(2)
        # 有时候无法访问教务处页面
        try:
            page.goto("http://202.119.81.113:8080/")
        except:
            raise Exception('无法访问教务处页面')

        # 循环登录
        while True:
            page.locator("#userAccount").fill(username, timeout=10000)
            page.locator("#userPassword").fill(password, timeout=10000)

            # 随机生成验证码图片
            file_name = str(uuid.uuid1()).replace('-', '') + ".png"
            # 验证码截图
            img_buf = page.query_selector("#SafeCodeImg").screenshot()

            # 获取ocr对象
            ocr = ddddocr.DdddOcr()

            # ocr对象识别图片
            safe_code = ocr.classification(img=img_buf)
            # print("验证码：" + safe_code)

            # 获取验证码输入框
            page.locator("#RANDOMCODE").fill(safe_code)

            # 点击 确认登录 按钮
            page.locator("#btnSubmit").click()

            if "验证码错误" in page.content():
                print("验证码错误！！")
            elif "该帐号不存在或密码错误" in page.content():
                print("该帐号不存在或密码错误!")
                return [None, None, None]
            elif "其它地方登录" in page.content():
                print("您的账号在其它地方登录")
            else:
                break

        cnt = 1
        while "我的桌面" not in page.content():
            # 阻塞cnt秒，依次递增
            time.sleep(cnt)
            # 刷新一次当前页面(感觉像是教务处的问题)
            page.reload()
            cnt += 1
            if cnt > 30:
                raise Exception('登录次数过多，请开发者调试！')

        # 获取必要信息
        tree = etree.HTML(page.content())
        real_name = tree.xpath('/html/body/div[5]/div/div[3]/text()')[0]
        real_name = real_name.strip()
        if real_name[0:3] == "姓名：":
            real_name = real_name[3:]

        # 访问教务处课表
        page.locator("body > div.wap > a.block7").click()
        # page.goto("http://202.119.81.112:9080/njlgdx/xskb/xskb_list.do?Ves632DSdyV=NEW_XSD_PYGL")

        with open('./kb.html', 'w', encoding='utf-8') as fp:
            fp.write(page.content())

        tree = etree.HTML(page.content())

        # 解析课表内容
        blocks_top = tree.xpath('//div[@class="kbcontent"]')

        # 课程
        c_arr = []
        # 周次
        w_arr = []
        for block in blocks_top:
            for content in block.xpath('./text()'):
                if ('-----' not in content) and content != '\xa0' and content != '':
                    c_arr.append(content)
            for content in block.xpath('./font[@title="周次(节次)"]/text()'):
                if content != ' ':
                    w_arr.append(content)

        courses = dict()
        for i in range(len(c_arr)):
            courses[c_arr[i]] = {
                'week': w_arr[i]
            }

        # print(courses)

        # blocks_bottom 是下面一整块表格
        blocks_bottom = tree.xpath('//*[@id="dataList"]//tr')
        # 遍历表格的每一行
        for row in blocks_bottom:
            # 获取每一行的每一列元素
            col = row.xpath('./td')
            if len(col) != 10:
                continue
            # data 是 一行课程数据
            data = []
            for detail in col:
                content = detail.xpath('./text()')
                if len(content) == 1:
                    data.append(content[0])
                elif len(content) == 2:
                    data.append(content)
                else:
                    data.append('')
            # print(data[3])
            courses[data[3]]['code'] = data[1]
            courses[data[3]]['order'] = data[2]
            courses[data[3]]['teacher'] = data[4]
            courses[data[3]]['time'] = data[5]
            courses[data[3]]['score'] = data[6]
            courses[data[3]]['room'] = data[7].split(',')
            courses[data[3]]['type'] = data[8]
            courses[data[3]]['stage'] = data[9]

        # 打印初步结果
        # print('打印初步结果')
        # for name, data in courses.items():
        #     print(name)
        #     print(data)

        for _, data in courses.items():
            data['week'] = parse_week(data['week'])
            p_time = []

            # 解析 ['星期一(02-03小节)', '星期三(02-03小节)'] 或 星期四(04-05小节)
            if len(data['time']) == 2:
                for t in data['time']:
                    p_time.append(parse_single(t))
            else:
                p_time.append(parse_single(data['time']))

            data['time'] = p_time

        # 打印加工后的数据
        # print('打印加工后的数据')
        # for name, data in courses.items():
        #     print(name)
        #     print(data)

        # 格式如下：
        # 操作系统
        # {'week': [2, 9], 'code': '06022005', 'order': '1', 'teacher': '衷宜', 'time': [[1, 2, 2], [3, 2, 2]], 'score': '2', 'room': ['Ⅳ-A106', 'Ⅳ-A106'], 'type': '必修', 'stage': '预置'}
        # 或
        # {'week': [2, 13], 'code': '06029905', 'order': '0', 'teacher': '蔡志成', 'time': [[4, 4, 2]], 'score': '2', 'room': ['Ⅳ-B306'], 'type': '任选', 'stage': '一选'}

        my_courses = [[] for week in range(25)]
        parse_final_table(my_courses, courses)

        # 接下来获取成绩
        # 进入成绩页面
        page.locator("#homepage").click()
        page.locator("body > div.wap > a.block10").click()
        page.locator("#btn_query").click()
        # page.goto("http://202.119.81.112:9080/njlgdx/kscj/cjcx_list")
        with open('./score.html', 'w', encoding='utf-8') as fp:
            fp.write(page.content())

        with open("./score.html", "r", encoding="utf-8") as fp:
            page_content = fp.read()

        tree = etree.HTML(page_content)

        rows = tree.xpath("//*[@id=\"dataList\"]/tbody/tr")

        '''
        score_table 最终数据类型：
        {
            "2020-2021-1": {
                "0": {
                    "name": "计算机导论"
                    "score": 82,
                    "point": 2,
                    "type": "必修",
                    "gpa": 3(自己算)
                },
                "1": {
                    "name": "通用英语（Ⅰ）",
                    "score": 85,
                    "point": 2,
                    "type": "任选",
                    "gpa": 3(自己算)
                },
            },
            "2020-2021-2": {
                "0": {
                    "name": "高等数学",
                    "score": 85,
                    "point": 4.5,
                    "type": "必修",
                    "gpa": 3(自己算)
                },
                "1": {
                    "name": "算法设计与分析",
                    "score": 87,
                    "point": 3.5,
                    "type": "必修",
                    "gpa": 3(自己算)
                },
            },
        }
        '''
        score_table = dict()
        # 每一个row都是一行数据（一门课程的所有信息），row是元素
        # score_index，一个课程信息对象是value
        score_index = 0
        # last_term记录上一次的搜索的学期，如果学期改变，则刷新score_index
        last_term = ""
        for row in rows:
            # data 是 ['39', '2021-2022-2', '06020102', '计算机网络', '92', '3', '48', '考试', '必修', '专业基础课'] 或 [] 格式
            data = row.xpath("./td/text()")
            if len(data) == 0:
                continue
            if data[1] != last_term:
                score_index = 0
                last_term = data[1]
            # print(data)
            if not score_table.__contains__(str(data[1])):
                score_table[str(data[1])] = dict()

            score_table[str(data[1])][str(score_index)] = {
                "name": str(data[3]),
                "score": str(data[4]),
                "point": str(data[5]),
                "type": str(data[8]),
                "gpa": get_gpa(score_str2float(str(data[4])))
            }
            score_index += 1

        # for term, courses in score_table.items():
        #     print(term + ":" + str(courses))

        page.close()
        context.close()
        browser.close()

        return [my_courses, score_table, real_name]


def verify(request):
    if request.method != 'GET':
        return JsonResponse({
            'success': False,
            'msg': 'Only support GET method'
        })
    username = request.GET.get('username')
    password = request.GET.get('password')
    if username is None or password is None:
        return JsonResponse({
            'success': False,
            'msg': 'wrong params'
        })

    try:
        [my_course, score_table, real_name] = login_jwc(str(username), str(password))
        if my_course is None:
            return JsonResponse({
                'success': False,
                'msg': '用户名或密码错误'
            }, json_dumps_params={'ensure_ascii': False})
        return JsonResponse({
            'success': True,
            'courseTable': my_course,
            "scoreTable": score_table,
            'realName': real_name
        }, json_dumps_params={'ensure_ascii': False})
    except:
        print('爬虫异常')
        return JsonResponse({
            'success': False,
            'msg': '爬虫后端异常'
        }, json_dumps_params={'ensure_ascii': False})
