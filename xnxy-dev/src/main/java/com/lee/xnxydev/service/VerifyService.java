package com.lee.xnxydev.service;

import java.util.Map;

/**
 * @author 晓龙coding
 */
public interface VerifyService {
    /**
     * 异步爬取数据
     * @param params 包含 username：教务处用户名 password：教务处密码
     * @param uId
     * @param verifyUrl
     * @return
     */
    void asyncCrawlData(Map<String, Object> params, Long uId, String verifyUrl);

    /**
     * 异步爬取数据
     * @param params 包含 username：教务处用户名 password：教务处密码
     * @param uId
     * @param verifyUrl
     * @return
     */
    Boolean syncCrawlData(Map<String, Object> params, Long uId, String verifyUrl);
}
