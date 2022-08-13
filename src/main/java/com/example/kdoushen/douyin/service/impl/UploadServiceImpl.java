package com.example.kdoushen.douyin.service.impl;

import com.example.kdoushen.douyin.service.UploadService;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
//import java.awt.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {
    private static final Logger log= LoggerFactory.getLogger(UploadServiceImpl.class);
    private static final String DEFAULT_IMG_FORMAT = "jpg";
    @Override
    public void fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException {
//        File file = new File(videoTargetFile);
//        file.createNewFile();
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        fileOutputStream.write(data.getBytes());
//        fileOutputStream.close();
        data.transferTo(new File(videoTargetFile));
        log.info("视频存入成功："+videoTargetFile);
    }

    @Override
    public void fetchFrameToFile(String videoFile, String targetFile, int frameNum) {
        try {
            File frameFile = new File(targetFile);
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            int length = ff.getLengthInFrames();
            //*第几帧判断设置*//*
            if (frameNum < 0) {
                frameNum = 0;
            }
            if (frameNum > length) {
                frameNum = length - 5;
            }
            //指定第几帧
            ff.setFrameNumber(frameNum);
            int i = 0;
            Frame f = null;
            while (i < length) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                f = ff.grabFrame();
                if ((i >= 5) && (f.image != null)) {
                    break;
                }
                i++;
            }
            opencv_core.IplImage img = f.image;
            int width = img.width();
            int height = img.height();
            BufferedImage bi = new BufferedImage(height, width, BufferedImage.TYPE_3BYTE_BGR);
            //截取出来的图是歪的，旋转九十度
            BufferedImage targetImage = rotateClockwise90(f.image.getBufferedImage());

            bi.getGraphics().drawImage(targetImage.getScaledInstance(targetImage.getWidth(), targetImage.getHeight(), Image.SCALE_SMOOTH),
                    0, 0, null);
            ff.flush();
            ff.stop();
            ImageIO.write(bi, DEFAULT_IMG_FORMAT, frameFile);
            log.info("视频封面保存成功："+targetFile);
        } catch (Exception e) {
            throw new RuntimeException("转换视频图片异常");
        }
    }


    /**
     * 将图片顺时针旋转90度（通过交换图像的整数像素RGB 值）
     * @param bi
     * @return
     */
    public static BufferedImage rotateClockwise90(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        BufferedImage bufferedImage = new BufferedImage(height, width, bi.getType());
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                //第一个参数未x轴，第二个为y轴
                bufferedImage.setRGB(height - 1 - j, i , bi.getRGB(i, j));
        return bufferedImage;
    }
}
