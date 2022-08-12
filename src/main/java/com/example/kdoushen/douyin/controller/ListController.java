package com.example.kdoushen.douyin.controller;

import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.bean.protobuf.publish.List;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.service.LikeService;
import com.example.kdoushen.douyin.service.VideoService;
import com.example.kdoushen.douyin.service.UserMsgService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CountDownLatch;

@Controller
@Transactional
public class ListController {
    private static final Logger log= LoggerFactory.getLogger(ListController.class);

    @Autowired
    VideoService videoService;
    @Autowired
    UserMsgService userMsgService;
    @Autowired
    LikeService likeService;

    /**
     * 登录用户的视频发布列表
     * @param request
     * @return
     */
    @GetMapping("/douyin/publish/list/")
    @ResponseBody
    public String listVideo(HttpServletRequest request) throws InterruptedException {
        String token = request.getParameter("token");
        List.douyin_publish_list_response.Builder responseBuilder = List.douyin_publish_list_response.newBuilder();

        if (!TokenUtil.verify(token)) {//验证token
            responseBuilder.setStatusCode(0);
            responseBuilder.setStatusMsg("token错误！");
            log.error("视频拉取:token错误");
        } else {
            Long userId = Long.parseLong(request.getParameter("user_id"));
            //从数据库中查询该用户发布的所有视频
            java.util.List<Video> videoList = videoService.getUserVideosByUid(userId);
            //查询author信息,并且构造userBuilder
            UserMsg user = userMsgService.getUserMsgByUid(userId);
            List.User.Builder userBuilder = List.User.newBuilder();
            userBuilder.setId(userId);
            userBuilder.setName(user.getUsername());
            userBuilder.setFollowCount(user.getFollowCount());
            userBuilder.setFollowerCount(user.getFollowerCount());
            userBuilder.setIsFollow(true);//未完善
            //构造返回video_list，在此同时需要查询是否已点赞
            for (int i = videoList.size()-1; i >=0; i--) {
                Video video = videoList.get(i);
                List.Video.Builder videoBuilder = List.Video.newBuilder();
                //查询视频信息
                Long favoriteCount=likeService.queryFavoriteCountByVid(video.getVId());
                boolean isFavorite = likeService.queryIsFavoriteByVidAndUid(video.getVId(), video.getUId());

                //设置视频返回值
                videoBuilder.setId(video.getVId());
                videoBuilder.setAuthor(userBuilder);
                videoBuilder.setPlayUrl(video.getPlayUrl());
                videoBuilder.setCoverUrl(video.getCoverUrl());
                videoBuilder.setFavoriteCount(favoriteCount);//已完善
                videoBuilder.setCommentCount(0);//未完善，在此模块中不会显示评论数，因此不必查询
                videoBuilder.setIsFavorite(isFavorite);//已完善
                videoBuilder.setTitle(video.getTitle());

                responseBuilder.addVideoList(videoBuilder);
            }
            //构造response
            responseBuilder.setStatusCode(0);
            log.info("拉取用户视频成功，用户id："+userId);
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
