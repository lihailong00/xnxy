package com.lee.xnxydev.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
 
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
 

/**
 * @author 晓龙coding
 */
public class HttpContextUtil {
	/**
	 * 获取HttpServletRequest请求
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
	}
}