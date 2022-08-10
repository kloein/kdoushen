package com.example.kdoushen.douyin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.bean.protobuf.extra.first.FavoriteAction;
import com.example.kdoushen.douyin.service.LikeService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class FavoriteController {
    private static final Logger log= LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    LikeService likeService;

    @PostMapping("/douyin/favorite/action/")
    @ResponseBody
    public String favoriteAction(HttpServletRequest request) {
        FavoriteAction.douyin_favorite_action_response.Builder responseBuilder = FavoriteAction.douyin_favorite_action_response.newBuilder();
        String token = request.getParameter("token");
        if (!TokenUtil.verify(token)) {//验证token
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token验证错误！");
            log.error("token验证错误！");
        } else {//token正确
            //获取所需参数
            String user_id = TokenUtil.getTokenPayload(token,"userId");
            String video_id = request.getParameter("video_id");
            String action_type = request.getParameter("action_type");

            QueryWrapper<Like> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.eq("uid", user_id);
            likeQueryWrapper.eq("vid", video_id);
            boolean hasLiked=likeService.count(likeQueryWrapper)==1?true:false;
            if (action_type.equals("1") ) {
                if (!hasLiked) {//先前没赞过才需要操作
                    Like like = new Like();
                    like.setUId(Long.parseLong(user_id));
                    like.setVId(Long.parseLong(video_id));
                    likeService.save(like);
                }
                responseBuilder.setStatusCode(0);
                log.info("用户"+user_id+"对视频"+video_id+"点赞");
            } else if (action_type.equals("2") ) {
                if (hasLiked) {//先前有赞过才需要操作
                    likeService.remove(likeQueryWrapper);
                }
                responseBuilder.setStatusCode(0);
                log.info("用户"+user_id+"取消对视频"+video_id+"的点赞");
            } else {
                responseBuilder.setStatusCode(1);
                responseBuilder.setStatusMsg("参数错误：action_type:"+action_type);
                log.error("参数错误：action_type:"+action_type);
            }
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
