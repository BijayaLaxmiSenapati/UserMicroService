package com.bridgelabz.fundoo.user.interceptors;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3)
			throws Exception {
		logger.info("Request for" + request.getRequestURI() + " is complete");

		request.setAttribute("endTime", System.nanoTime());
		
		long a=(long)request.getAttribute("endTime");
		logger.info("end time: "+a);
		long b=(long)request.getAttribute("startTime");
		long c=a-b;
		logger.info("time taken for handling "+request.getRequestURL()+" "+c);
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView model)
			throws Exception {
		logger.info("Request for " + request.getRequestURI() + " after execution");		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		logger.info("Request before" + request.getRequestURI());
		request.setAttribute("startTime", System.nanoTime());
	
		logger.info("start time: "+ (long)request.getAttribute("startTime")+" ns");
		return true;
	}

}

