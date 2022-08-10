package com.example.kdoushen.douyin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UploadService {
    /**
     * 将视频文件保存到指定文件夹
     * @param videoTargetFile
     * @param data
     * @throws IOException
     */
    public void fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException;

    /**
     * 截取视频文件某一帧，将其保存到指定文件夹
     * @param videoFile
     * @param targetFile
     * @param frameNum
     */
    public void fetchFrameToFile(String videoFile, String targetFile, int frameNum);
}
