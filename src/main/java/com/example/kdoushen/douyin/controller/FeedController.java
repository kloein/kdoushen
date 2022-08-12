package com.example.kdoushen.douyin.controller;

import com.example.kdoushen.douyin.bean.Comment;
import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.bean.protobuf.feed.Feed;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.service.CommentService;
import com.example.kdoushen.douyin.service.LikeService;
import com.example.kdoushen.douyin.service.impl.FeedServiceImpl;
import com.example.kdoushen.douyin.service.impl.GetLatestStrategy;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Controller
@Transactional
public class FeedController {
    private static final Logger log= LoggerFactory.getLogger(FeedController.class);

    @Autowired
    FeedServiceImpl feedService;
    @Autowired
    UserMsgService userMsgService;
    @Autowired
    LikeService likeService;
    @Autowired
    CommentService commentService;

    @Autowired
    GetLatestStrategy feedStrategy;

    /**
     * 抖音首页返回视频流
     * @param request
     * @return
     */
    @GetMapping("/douyin/feed")
    @ResponseBody
    public String feed(HttpServletRequest request) throws InterruptedException {
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
            String userId ;
            if (token != null) {
                userId = TokenUtil.getTokenPayload(token, "userId");
            } else {//未登录
                userId="-1";
            }
            //查询出部分视频，返回给客户端
            Timestamp timestamp=new Timestamp(latestTime);
            List<Video> videoList = feedService.getVideoByStrategy(feedStrategy, timestamp);
            CountDownLatch countDownLatch = new CountDownLatch(videoList.size());
            for (int i = 0; i < videoList.size(); i++) {
                Video video = videoList.get(i);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Feed.Video.Builder videoBuilder = Feed.Video.newBuilder();
                            //查询作者信息
                            Feed.User.Builder userBuilder = Feed.User.newBuilder();
                            UserMsg userMsg = userMsgService.getUserMsgByUid(video.getUId());
                            //将查询结果放入返回值中
                            userBuilder.setId(userMsg.getUserId());
                            userBuilder.setName(userMsg.getUsername());
                            userBuilder.setFollowCount(userMsg.getFollowCount());
                            userBuilder.setFollowerCount(userMsg.getFollowerCount());
                            userBuilder.setIsFollow(true);//未完善
                            //查询作者信息结束

                            //查询视频信息
                            Long favoriteCount=likeService.queryFavoriteCountByVid(video.getVId());
                            boolean isFavorite = likeService.queryIsFavoriteByVidAndUid(video.getVId(), video.getUId());
                            //查询评论数量
                            long commentCount = commentService.queryCommentCountByVid(video.getVId());
                            //设置返回视频信息
                            videoBuilder.setId(video.getVId());
                            videoBuilder.setAuthor(userBuilder);
                            videoBuilder.setPlayUrl(video.getPlayUrl());
                            videoBuilder.setCoverUrl(video.getCoverUrl());
                            videoBuilder.setFavoriteCount(favoriteCount);//已完善
                            videoBuilder.setCommentCount(commentCount);//已完善
                            videoBuilder.setIsFavorite(isFavorite);//已完善
                            videoBuilder.setTitle(video.getTitle());
                            synchronized (responseBuilder) {
                                responseBuilder.addVideoList(videoBuilder);
                            }
                        }  finally {
                            countDownLatch.countDown();
                        }
                    }
                }).start();
            }
            countDownLatch.await();
            responseBuilder.setStatusCode(0);
            if (videoList.size() > 0) {
                responseBuilder.setNextTime(videoList.get(videoList.size() - 1).getPublishTime().getTime());
            } else {
                //否则设置下次返回七天内的视频，此行仅方便开发测试有用
                responseBuilder.setNextTime(System.currentTimeMillis()-7*24*60*60*1000);
            }
            log.info("feed:拉取视频成功");
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
