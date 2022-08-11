package com.example.kdoushen.douyin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.bean.protobuf.extra.first.FavoriteAction;
import com.example.kdoushen.douyin.bean.protobuf.extra.first.FavoriteList;
import com.example.kdoushen.douyin.service.LikeService;
import com.example.kdoushen.douyin.service.UserMsgService;
import com.example.kdoushen.douyin.service.VideoService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Controller
public class FavoriteController {
    private static final Logger log= LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    LikeService likeService;

    @Autowired
    VideoService videoService;

    @Autowired
    UserMsgService userMsgService;

    /**
     * 点赞操作
     * @param request
     * @return
     */
    @PostMapping("/douyin/favorite/action/")
    @ResponseBody
    public String favoriteAction(HttpServletRequest request) {
        FavoriteAction.douyin_favorite_action_response.Builder responseBuilder = FavoriteAction.douyin_favorite_action_response.newBuilder();
        String token = request.getParameter("token");
        if (!TokenUtil.verify(token)) {//验证token
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token验证错误！");
            log.error("点赞视频:token验证错误！");
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

    /**
     * 获取用户已点赞视频
     */
    @GetMapping("/douyin/favorite/list/")
    @ResponseBody
    public String favoriteVideos(HttpServletRequest request) throws InterruptedException {
        FavoriteList.douyin_favorite_list_response.Builder responseBuilder = FavoriteList.douyin_favorite_list_response.newBuilder();

        String token = request.getParameter("token");
        if (!TokenUtil.verify(token)) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token验证错误！");
            log.error("已点赞视频拉取:token验证错误！");
        } else {
            String user_id = request.getParameter("user_id");
            //先查询出用户点赞了哪些视频，获取视频id
            QueryWrapper<Like> likeQueryWrapper = new QueryWrapper<>();
            likeQueryWrapper.eq("uid", user_id);
            List<Like> likeList = likeService.list(likeQueryWrapper);
            //再从视频信息表中获取详细信息
            for (int i = 0; i < likeList.size(); i++) {
                Like like = likeList.get(i);

                QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
                videoQueryWrapper.eq("vid", like.getVId());
                Video video = videoService.getOne(videoQueryWrapper);
                //查询视频作者信息
                UserMsg user = userMsgService.getOne(new QueryWrapper<UserMsg>().eq("uid", like.getUId()));
                FavoriteList.User.Builder userBuilder = FavoriteList.User.newBuilder();
                userBuilder.setId(like.getUId());
                userBuilder.setName(user.getUsername());
                userBuilder.setFollowCount(user.getFollowCount());
                userBuilder.setFollowerCount(user.getFollowerCount());
                userBuilder.setIsFollow(true);//未完善
                //查询视频的点赞量和是否已经点赞
                QueryWrapper<Like> favoriteQuery = new QueryWrapper<>();
                favoriteQuery.eq("vid", video.getVId());
                Long favoriteCount=likeService.count(favoriteQuery);

                QueryWrapper<Like> isFavoriteQuery=new QueryWrapper<>();
                isFavoriteQuery.eq("uid", user_id).eq("vid", video.getVId());
                ;
                boolean isFavorite = likeService.count(isFavoriteQuery)==1?true:false;
                //构建返回视频数据
                FavoriteList.Video.Builder videoBuilder = FavoriteList.Video.newBuilder();
                videoBuilder.setId(video.getVId());
                videoBuilder.setAuthor(userBuilder);
                videoBuilder.setPlayUrl(video.getPlayUrl());
                videoBuilder.setCoverUrl(video.getCoverUrl());
                videoBuilder.setFavoriteCount(favoriteCount);//已完善
                videoBuilder.setCommentCount(0);//未完善
                videoBuilder.setIsFavorite(isFavorite);//已完善
                videoBuilder.setTitle(video.getTitle());

                responseBuilder.addVideoList(videoBuilder);
            }
            responseBuilder.setStatusCode(0);
            log.info("已点赞视频拉取成功,用户id："+user_id);
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
