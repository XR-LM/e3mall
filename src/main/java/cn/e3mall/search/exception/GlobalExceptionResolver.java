package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理
 * @author XR
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	//log4j日志对象
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception exception) {
		// 打印控制台
		exception.printStackTrace();
		//写日志
		logger.debug("测试输出的日志。。。。");
		logger.info("系统出现了异常。。。。");
		logger.error("系统发生了异常",exception);
		//发邮件、短信
		//使用jmail工具包。发送短信使用第三方的WebService
		//显示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
