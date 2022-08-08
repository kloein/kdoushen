package com.example.kdoushen.douyin.service.publish;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UploadService {
    public void fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException;
    public void fetchFrameToFile(String videoFile, String targetFile, int frameNum);
}
