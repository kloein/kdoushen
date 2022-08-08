package com.example.kdoushen.douyin.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.example.kdoushen.douyin.bean.protobuf.user.UserMsg;
import com.example.kdoushen.douyin.service.user.UserMsgService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Transactional
@Controller
public class UserMsgController {
    private static final Logger log= LoggerFactory.getLogger(UserMsgController.class);
    @Autowired
    UserMsgService userMsgService;

    @GetMapping("/douyin/user")
    @ResponseBody
    public String userMsgController(HttpServletRequest request) {
        Long user_id = Long.parseLong(request.getParameter("user_id"));
        String token = request.getParameter("token");

        UserMsg.douyin_user_response.Builder responseBuilder = UserMsg.douyin_user_response.newBuilder();
        boolean verify = TokenUtil.verify(token);
        if (verify == false) {//验证token
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token错误");
        } else {
            //查询出用户数据，放入返回值
            UserMsg.User.Builder userBuilder = UserMsg.User.newBuilder();
            LambdaQueryWrapper<com.example.kdoushen.douyin.bean.user.UserMsg> userMsgLambdaQueryWrapper = new LambdaQueryWrapper<com.example.kdoushen.douyin.bean.user.UserMsg>();
            userMsgLambdaQueryWrapper.select(com.example.kdoushen.douyin.bean.user.UserMsg::getUserId, com.example.kdoushen.douyin.bean.user.UserMsg::getUsername, com.example.kdoushen.douyin.bean.user.UserMsg::getFollowCount, com.example.kdoushen.douyin.bean.user.UserMsg::getFollowerCount)
                    .eq(com.example.kdoushen.douyin.bean.user.UserMsg::getUserId, user_id);
            com.example.kdoushen.douyin.bean.user.UserMsg userMsg = userMsgService.getOne(userMsgLambdaQueryWrapper);

            if (userMsg == null) {
                responseBuilder.setStatusCode(1);
                responseBuilder.setStatusMsg("查无此用户");
                log.info("用户信息拉取：用户名不存在");
            } else {
                userBuilder.setId(user_id);
                userBuilder.setName(userMsg.getUsername());
                userBuilder.setFollowCount(userMsg.getFollowCount());
                userBuilder.setFollowerCount(userMsg.getFollowerCount());
                userBuilder.setIsFollow(false);
                UserMsg.User user = userBuilder.build();
                //将结果放入返回
                responseBuilder.setStatusCode(0);
                responseBuilder.setUser(user);
                log.info("返回用户信息");
            }
        }

        return JsonUtil.builder2Json(responseBuilder);
    }
}
