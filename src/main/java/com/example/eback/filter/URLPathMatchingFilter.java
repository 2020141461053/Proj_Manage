package com.example.eback.filter;


import com.example.eback.service.UserService;
import com.example.eback.util.SpringContextUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author PBW
 * @date 1/19
 */
@Log4j2
public class URLPathMatchingFilter extends PathMatchingFilter {

    @Autowired
    UserService userService;


    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws  LoginException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Authorization,"
                + " Content-Type, Accept, Connection, User-Agent, Cookie,token");

        if (HttpMethod.OPTIONS.toString().equals((httpServletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        if (null == userService) {
            userService = SpringContextUtils.getContext().getBean(UserService.class);
        }

        String requestAPI = getPathWithinApplication(request);

        Subject subject = SecurityUtils.getSubject();
        log.trace("访问了：" + requestAPI + "接口");
        if (!subject.isAuthenticated()) {

            log.info("未登录用户尝试访问需要登录的接口：" + requestAPI);
            throw  new LoginException();
        }

        // 判断访问接口是否需要过滤（数据库中是否有对应信息）
        boolean needFilter = !requestAPI.equals("/api/login");
        if (!needFilter) {
            log.trace("访问了：" + requestAPI + "接口");
            return true;
        } else {
            // 判断当前用户是否有相应权限

            boolean hasPermission = false;
            String username = subject.getPrincipal().toString();

            /*if (requestAPI.startsWith("/api/admin")) {
                hasPermission = true;
            }*/
            /**
             *
             * if判断  ：或者role为admin
             */

            if (hasPermission) {
                if (userService.findByUsername(username).getRole().equals("admin")) {
                    log.info("用户：" + username + "   访问了接口：" + requestAPI + "");
                    return true;
                } else {
                    log.warn("用户：" + username + "   访问了没有权限的接口：" + requestAPI);
                    throw  new LoginException();
                }
            }
            else  {
                log.info("用户：" + username + "   访问了接口：" + requestAPI + "");
                return true;}
        }

    }
}
