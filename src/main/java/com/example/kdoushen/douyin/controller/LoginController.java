package com.example.kdoushen.douyin.controller;


import com.example.kdoushen.douyin.bean.protobuf.user.Login;
import com.example.kdoushen.douyin.bean.User;
import com.example.kdoushen.douyin.service.UserService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@Controller
public class LoginController {
    private static final Logger log= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    /**
     * 用户登录接口
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/douyin/user/login/")
    @ResponseBody
    public String userLogin(String username,String password) {
        Login.douyin_user_login_response.Builder responseBuilder = Login.douyin_user_login_response.newBuilder();
        //验证用户名与密码是否合法
        if (username.length() > 32) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("用户名不合法，请重新输入");
            log.info("用户登录失败：用户名不合法");
        } else if (password.length() > 32) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("用户密码不合法，请重新输入");
            log.info("用户登录失败：用户密码不合法");
        } else {
            User userServiceOne = userService.getUserByUsernameAndPassword(username, password);
            if (userServiceOne != null) {//登录成功
                responseBuilder.setStatusCode(0);
                responseBuilder.setUserId(userServiceOne.getUserId());
                String token = TokenUtil.token(username,userServiceOne.getUserId());
                responseBuilder.setToken(token);
                log.info("用户登录,uid:"+userServiceOne.getUserId());
            } else {//密码错误
                responseBuilder.setStatusCode(1);
                responseBuilder.setStatusMsg("用户名或密码错误");
                log.info("用户登录失败：用户密码错误");
            }
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
