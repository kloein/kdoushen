package com.example.kdoushen.douyin.controller.publish;

import com.example.kdoushen.douyin.bean.protobuf.publish.Action;
import com.example.kdoushen.douyin.controller.user.LoginController;
import com.example.kdoushen.douyin.service.publish.UploadService;
import com.example.kdoushen.douyin.service.publish.VideoService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.bytedeco.javacv.FrameGrabber;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Transactional
@Controller
public class UploadController {
    private static final Logger log= LoggerFactory.getLogger(UploadController.class);

    @Value("${video-config.ftp-server-path}")
    private String FTP_SERVER_PATH;

    @Value("${video-config.video-save-path}")
    private String VIDEO_PATH;
    @Value("${video-config.frame-num}")
    private int FRAME_NUM;
    @Value("${video-config.video-cover-save-path}")
    private String VIDEO_COVER_PATH;

    private static final String DEFAULT_VIDEO_FORMAT = ".mp4";
    private static final String DEFAULT_IMG_FORMAT = ".jpg";

    @Autowired
    UploadService uploadService;
    @Autowired
    VideoService videoService;

    @PostMapping("/douyin/publish/action/")
    @ResponseBody
    public String upload(MultipartFile data,HttpServletRequest request) throws IOException, FrameGrabber.Exception {
        Action.douyin_publish_action_response.Builder responseBuilder = Action.douyin_publish_action_response.newBuilder();
        String token = request.getParameter("token");

        //验证token
        if (!TokenUtil.verify(token)) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token非法!");
        } else {
            String title = request.getParameter("title");
            //通过token获取用户id
            Long userId = Long.parseLong(TokenUtil.getTokenPayload(token, "userId"));
            //生成视频名称
            String filename=UUID.randomUUID().toString();
            //保存视频进本地
            String videoTargetPath=VIDEO_PATH+ File.separator+filename+DEFAULT_VIDEO_FORMAT;
            uploadService.fetchVideoToFile(videoTargetPath, data);

            //保存封面进本地
            String coverTargetPath=VIDEO_COVER_PATH+File.separator+filename+DEFAULT_IMG_FORMAT;
            uploadService.fetchFrameToFile(videoTargetPath, coverTargetPath, FRAME_NUM);

            //向mysql中存入视频数据
            String ftpVideoPath=FTP_SERVER_PATH+"videos"+File.separator+filename+DEFAULT_VIDEO_FORMAT;
            String ftpCoverPath=FTP_SERVER_PATH+"covers"+File.separator+filename+DEFAULT_IMG_FORMAT;
            videoService.saveVideoMsg(userId, ftpVideoPath, ftpCoverPath, title);
            //返回成功
            responseBuilder.setStatusCode(0);
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
