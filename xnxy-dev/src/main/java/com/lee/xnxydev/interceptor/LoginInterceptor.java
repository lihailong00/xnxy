package com.lee.xnxydev.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lee.xnxydev.pojo.VO.ResponseResult;
import com.lee.xnxydev.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 晓龙coding
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("进入拦截器");
        String token = request.getHeader("token");
        logger.info("token=" + token);
        try {
            JWTUtil.checkToken(token);
        } catch (Exception e) {
            response.setContentType("application/json;charset=utf-8");
            Map<String, Object> ans = new HashMap<>();
            ans.put("code", 200);
            ans.put("success", false);
            ans.put("msg", "token验证失败");
            ans.put("data", "");
            // Map 转 JSON
            ObjectMapper mapper = new ObjectMapper();
            String ansStr = "";
            try {
                ansStr = mapper.writeValueAsString(ans);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
            response.getWriter().print(ansStr);
            return false;
        }
        logger.info("通过拦截器");
        return true;
    }
}