package com.baizhi.config;


import org.springframework.core.log.LogMessage;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import jakarta.servlet.http.HttpServletRequest;


/**
 * 自定义记住我 services 实现类
 */
public class MyPersistentTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {


    public MyPersistentTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
    }


  /**
  * 自定义前后端分离获取 remember-me 方式
  *
  * @param request
  * @param parameter
  * @return
  */
 @Override
	protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
		String paramValue = request.getAttribute(parameter).toString();
		if (paramValue != null) {
			if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
					|| paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
				return true;
			}
		}
		this.logger.debug(
				LogMessage.format("Did not send remember-me cookie (principal did not set parameter '%s')", parameter));
		return false;
	}

	
//    /**
//     * 自定义前后端分离获取 remember-me 方式
//     *
//     * @param request
//     * @param parameter
//     * @return
//     */
//    @Override
//    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
//        String paramValue = request.getAttribute(parameter).toString();
//        if (paramValue != null) {
//            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on")
//                    || paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
//                return true;
//            }
//        }
//        return false;
//    }
}
