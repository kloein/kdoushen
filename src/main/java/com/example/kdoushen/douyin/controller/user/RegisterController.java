package com.example.kdoushen.douyin.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.protobuf.user.Register;
import com.example.kdoushen.douyin.bean.user.User;
import com.example.kdoushen.douyin.bean.user.UserMsg;
import com.example.kdoushen.douyin.service.user.UserMsgService;
import com.example.kdoushen.douyin.service.user.UserService;
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

import javax.servlet.http.HttpServletRequest;
@Transactional
@Controller
public class RegisterController {
    private static final Logger log= LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    UserService userService;
    @Autowired
    UserMsgService userMsgService;

    @PostMapping("/douyin/user/register")
    @ResponseBody
    public String userRegister(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Register.douyin_user_register_response.Builder responseBuilder = Register.douyin_user_register_response.newBuilder();
        //验证用户名与密码是否合法
        if (username.length() > 32) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("用户名不合法，请重新输入");
            log.info("用户注册失败：用户名不合法");
        } else if (password.length() > 32) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("用户密码不合法，请重新输入");
            log.info("用户注册失败：用户密码不合法");
        } else {
            //从数据库中验证用户名是否已经存在，若是，返回错误，反之成功注册
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);
            User userServiceOne = userService.getOne(queryWrapper);
            if (userServiceOne != null) {
                responseBuilder.setStatusCode(1);
                responseBuilder.setStatusMsg("用户名已存在，请选择其他用户名");
                log.info("用户注册失败：用户名已存在");
            } else {
                //用户名合法，在t_user中生成对应用户信息
                User user = new User();
                String token = TokenUtil.token(username,user.getUserId());
                user.setUserName(username);
                String md5Password= DigestUtils.md5DigestAsHex(password.getBytes());
                user.setPassword(md5Password);
                userService.save(user);
                //同时，还要在t_user_msg中储存用户信息
                UserMsg userMsg = new UserMsg();
                userMsg.setUserId(user.getUserId());
                userMsg.setUsername(username);
                userMsg.setFollowCount(0L);
                userMsg.setFollowerCount(0L);
                userMsgService.save(userMsg);

                //注册成功，返回对应值
                responseBuilder.setStatusCode(0);
                responseBuilder.setStatusMsg("注册成功！");
                responseBuilder.setUserId(user.getUserId());
                responseBuilder.setToken(token);
                log.info("用户注册成功");
            }
        }
        return JsonUtil.builder2Json(responseBuilder);
    }


}
