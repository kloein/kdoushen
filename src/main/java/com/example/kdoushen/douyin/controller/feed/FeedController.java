package com.example.kdoushen.douyin.controller.feed;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.protobuf.feed.Feed;
import com.example.kdoushen.douyin.bean.publish.Video;
import com.example.kdoushen.douyin.bean.user.UserMsg;
import com.example.kdoushen.douyin.controller.publish.ListController;
import com.example.kdoushen.douyin.service.feed.impl.FeedServiceImpl;
import com.example.kdoushen.douyin.service.feed.impl.GetLatestStrategy;
import com.example.kdoushen.douyin.service.user.UserMsgService;
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
import java.sql.Timestamp;
import java.util.List;

@Controller
@Transactional
public class FeedController {
    private static final Logger log= LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedServiceImpl feedService;
    @Autowired
    UserMsgService userMsgService;
    @Autowired
    GetLatestStrategy getLatestStrategy;

    @GetMapping("/douyin/feed")
    @ResponseBody
    public String feed(HttpServletRequest request) {
        Feed.douyin_feed_response.Builder responseBuilder = Feed.douyin_feed_response.newBuilder();
        //获取请求中的数据
        String latest_time_str = request.getParameter("latest_time");
        String token = request.getParameter("token");
        //登录状态下,token错误
        if (token != null && !TokenUtil.verify(token)) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token错误！");
            log.error("feed:token错误！");
        } else {
            Long latestTime=latest_time_str==null?1L:Long.parseLong(latest_time_str);
            //查询出部分视频，返回给客户端
            Timestamp timestamp=new Timestamp(latestTime);
            List<Video> videoList = feedService.getVideoByStrategy(getLatestStrategy, timestamp);
            for (int i = 0; i < videoList.size(); i++) {
                Video video = videoList.get(i);
                Feed.Video.Builder videoBuilder = Feed.Video.newBuilder();
                //查询作者信息
                Feed.User.Builder userBuilder = Feed.User.newBuilder();
                QueryWrapper<UserMsg> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("uid", video.getUId());
                com.example.kdoushen.douyin.bean.user.UserMsg userMsg = userMsgService.getOne(queryWrapper);
                //将查询结果放入返回值中
                userBuilder.setId(userMsg.getUserId());
                userBuilder.setName(userMsg.getUsername());
                userBuilder.setFollowCount(userMsg.getFollowCount());
                userBuilder.setFollowerCount(userMsg.getFollowerCount());
                userBuilder.setIsFollow(true);//未完善
                //查询作者信息结束

                videoBuilder.setId(video.getVId());
                videoBuilder.setAuthor(userBuilder);
                videoBuilder.setPlayUrl(video.getPlayUrl());
                videoBuilder.setCoverUrl(video.getCoverUrl());
                videoBuilder.setFavoriteCount(0);//未完善
                videoBuilder.setCommentCount(0);//未完善
                videoBuilder.setIsFavorite(true);//未完善
                videoBuilder.setTitle(video.getTitle());
                responseBuilder.addVideoList(videoBuilder);
            }
            responseBuilder.setStatusCode(0);
            if (videoList.size() > 0) {
                responseBuilder.setNextTime(videoList.get(videoList.size()-1).getPublishTime().getTime());
            }
            log.info("feed:拉取视频成功");
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
