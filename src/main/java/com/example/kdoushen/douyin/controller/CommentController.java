package com.example.kdoushen.douyin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.Comment;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.bean.protobuf.extra.first.CommentAction;
import com.example.kdoushen.douyin.bean.protobuf.extra.first.CommentList;
import com.example.kdoushen.douyin.service.CommentService;
import com.example.kdoushen.douyin.service.UserMsgService;
import com.example.kdoushen.douyin.util.IdUtils;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class CommentController {
    private static final Logger log= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;
    @Autowired
    UserMsgService userMsgService;

    /**
     * 登录用户对视频进行评论
     * @param request
     * @return
     */
    @PostMapping("/douyin/comment/action/")
    @ResponseBody
    public String commentAction(HttpServletRequest request) {
        CommentAction.douyin_comment_action_response.Builder responseBuilder = CommentAction.douyin_comment_action_response.newBuilder();
        String token = request.getParameter("token");
        //验证token
        if (!TokenUtil.verify(token)) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token验证错误!");
            log.error("token验证错误!");
        } else {
            String user_id = TokenUtil.getTokenPayload(token, "userId");
            String video_id = request.getParameter("video_id");
            String action_type = request.getParameter("action_type");
            //发布评论
            if (action_type.equals("1")) {
                String comment_text = request.getParameter("comment_text");
                //查询出用户信息，以封装进评论
                QueryWrapper<UserMsg> userMsgQueryWrapper = new QueryWrapper<UserMsg>().eq("uid", user_id);
                UserMsg userMsg = userMsgService.getOne(userMsgQueryWrapper);
                CommentAction.User.Builder userBuilder = CommentAction.User.newBuilder();
                userBuilder.setId(userMsg.getUserId());
                userBuilder.setName(userMsg.getUsername());
                userBuilder.setFollowCount(userMsg.getFollowCount());
                userBuilder.setFollowerCount(userMsg.getFollowerCount());
                userBuilder.setIsFollow(false);
                //评论保存进数据库
                Comment comment = new Comment();
                long comment_id = IdUtils.getId(comment);
                comment.setId(comment_id);
                comment.setUid(Long.parseLong(user_id));
                comment.setVid(Long.parseLong(video_id));
                comment.setCommentText(comment_text);
                LocalDateTime now = LocalDateTime.now();
                comment.setCommentTime(new Timestamp(now.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
                commentService.save(comment);
                //封装评论信息
                CommentAction.Comment.Builder commentBuilder = CommentAction.Comment.newBuilder();
                commentBuilder.setId(comment_id);
                commentBuilder.setUser(userBuilder);
                commentBuilder.setContent(comment_text);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM-dd");
                String formatDate = dateTimeFormatter.format(now);
                commentBuilder.setCreateDate(formatDate);
                responseBuilder.setComment(commentBuilder);
            }
            //删除评论
            if (action_type.equals("2")) {
                String comment_id = request.getParameter("comment_id");
                commentService.removeById(comment_id);
            }
            responseBuilder.setStatusCode(0);
        }

        return JsonUtil.builder2Json(responseBuilder);
    }

    @GetMapping("/douyin/comment/list")
    @ResponseBody
    public String commentList(HttpServletRequest request) {
        CommentList.douyin_comment_action_response.Builder responseBuilder = CommentList.douyin_comment_action_response.newBuilder();
        String video_id = request.getParameter("video_id");
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<Comment>().
                eq("vid", video_id).orderByDesc("comment_time");
        List<Comment> comments = commentService.list(commentQueryWrapper);
        for (int i = 0; i < comments.size(); i++) {
            Comment comment=comments.get(i);
            CommentList.Comment.Builder commentBuilder = CommentList.Comment.newBuilder();
            //设置返回值
            commentBuilder.setId(comment.getId());
            commentBuilder.setContent(comment.getCommentText());
            Timestamp commentTime = comment.getCommentTime();
            String time = "" + commentTime.getMonth() + "-" + commentTime.getDay();
            commentBuilder.setCreateDate(time);
            //查询User
            QueryWrapper<UserMsg> userMsgQueryWrapper = new QueryWrapper<UserMsg>().eq("uid", comment.getUid());
            UserMsg userMsg = userMsgService.getOne(userMsgQueryWrapper);
            CommentList.User.Builder userBuilder = CommentList.User.newBuilder();
            userBuilder.setId(userMsg.getUserId());
            userBuilder.setName(userMsg.getUsername());
            userBuilder.setFollowCount(userMsg.getFollowCount());
            userBuilder.setFollowerCount(userMsg.getFollowerCount());
            userBuilder.setIsFollow(false);
            //将User加入评论信息
            commentBuilder.setUser(userBuilder);
            //将评论信息加入返回值
            responseBuilder.addCommentList(commentBuilder);
            log.info("拉取视频评论成功，视频id："+video_id);
        }

        return JsonUtil.builder2Json(responseBuilder);
    }
}
