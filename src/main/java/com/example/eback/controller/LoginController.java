package com.example.eback.controller;

import com.example.eback.constans.UserRegistryCode;
import com.example.eback.entity.User;
import com.example.eback.result.Result;
import com.example.eback.result.ResultFactory;
import com.example.eback.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;

/**
 * Login and register controller.
 *
 * @author Evan
 * @date 2019/4
 */
@ApiOperation(value = "登录注册相关接口")
@RestController
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "登录", notes = "账号+密码")
    @PostMapping("/api/login")
    public Result login(@RequestBody @Valid User requestUser) {

        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            log.info("{}登录成功", username);
            return ResultFactory.buildSuccessResult(username);
        } catch (IncorrectCredentialsException e) {
            log.warn("{}登录，密码错误", username);
            return ResultFactory.buildFailResult("密码错误");
        } catch (UnknownAccountException e) {
            return ResultFactory.buildFailResult("账号不存在");
        }

    }

    @PostMapping("/api/register")
    public Result register(@RequestBody User user) {
        log.info("{}注册", user.getUsername());
        UserRegistryCode status = userService.register(user);
        return ResultFactory.buildFailResult(status.getMsg());
    }

    @GetMapping("/api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        System.out.println(SecurityUtils.getSubject().getPrincipal().toString());
        return ResultFactory.buildSuccessResult("成功登出");
    }

    @RequiresPermissions("admin")
    @GetMapping("/api/admin/authentication")
    public Result authentication() {
        return ResultFactory.buildSuccessResult("success");
    }

}
