package com.example.kdoushen.douyin.controller;

import com.example.kdoushen.douyin.bean.protobuf.publish.Action;
import com.example.kdoushen.douyin.service.UploadService;
import com.example.kdoushen.douyin.service.VideoService;
import com.example.kdoushen.douyin.util.JsonUtil;
import com.example.kdoushen.douyin.util.SystemUtil;
import com.example.kdoushen.douyin.util.TokenUtil;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Transactional
@Controller
public class UploadController {
    private static final Logger log= LoggerFactory.getLogger(UploadController.class);

    @Value("${video-config.server-path}")
    private String SERVER_PATH;

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

    /**
     * 登录用户选择视频上传
     * @param data
     * @param request
     * @return
     * @throws IOException
     * @throws FrameGrabber.Exception
     */
    @PostMapping("/douyin/publish/action/")
    @ResponseBody
    public String upload(MultipartFile data, HttpServletRequest request, HttpSession session) throws IOException, FrameGrabber.Exception {
        Action.douyin_publish_action_response.Builder responseBuilder = Action.douyin_publish_action_response.newBuilder();
        String token = request.getParameter("token");

        //验证token
        if (!TokenUtil.verify(token)) {
            responseBuilder.setStatusCode(1);
            responseBuilder.setStatusMsg("token非法!");
        } else {
            //获取static文件夹所在路径,在window系统下以/开头要去除
            /*String staticPath = ResourceUtils.getURL("classpath:").getPath();
            if (SystemUtil.isWindows()) {
                staticPath=staticPath.substring(1);
            }*/
            String title = request.getParameter("title");
            //通过token获取用户id
            Long userId = Long.parseLong(TokenUtil.getTokenPayload(token, "userId"));
            //生成视频名称
            String filename=UUID.randomUUID().toString();
            //保存视频进本地
            ServletContext servletContext = session.getServletContext();
            String videosFolderPath = servletContext.getRealPath(VIDEO_PATH);//获取视频文件夹路径
            File videosFolder=new File(videosFolderPath);
            if (!videosFolder.exists()) {//如果不存在该文件夹，则创建
                videosFolder.mkdir();
            }
            //String videoTargetPath=staticPath+VIDEO_PATH+ File.separator+filename+DEFAULT_VIDEO_FORMAT;
            String videoTargetPath=videosFolderPath + File.separator+filename+DEFAULT_VIDEO_FORMAT;
            uploadService.fetchVideoToFile(videoTargetPath, data);

            //保存封面进本地
            String coversFolderPath = servletContext.getRealPath(VIDEO_COVER_PATH);//获取封面文件夹路径
            File coversFolder=new File(coversFolderPath);
            if (!coversFolder.exists()) {//如果不存在该文件夹则创建
                coversFolder.mkdir();
            }
            //String coverTargetPath=staticPath+VIDEO_COVER_PATH+File.separator+filename+DEFAULT_IMG_FORMAT;
            String coverTargetPath=coversFolderPath+File.separator+filename+DEFAULT_IMG_FORMAT;
            uploadService.fetchFrameToFile(videoTargetPath, coverTargetPath, FRAME_NUM);

            //向mysql中存入视频数据
            String videoPath=SERVER_PATH+VIDEO_PATH+File.separator+filename+DEFAULT_VIDEO_FORMAT;
            String coverPath=SERVER_PATH+VIDEO_COVER_PATH+File.separator+filename+DEFAULT_IMG_FORMAT;
            //coverPath="https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF";
            videoService.saveVideoMsg(userId, videoPath, coverPath, title);
            //返回成功
            responseBuilder.setStatusCode(0);
        }
        return JsonUtil.builder2Json(responseBuilder);
    }
}
