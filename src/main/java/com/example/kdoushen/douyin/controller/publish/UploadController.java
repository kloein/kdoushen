package com.example.kdoushen.douyin.controller.publish;

import com.example.kdoushen.douyin.controller.user.LoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Transactional
@Controller
public class UploadController {
    private static final Logger log= LoggerFactory.getLogger(UploadController.class);


    @PostMapping("/douyin/publish/action/")
    @ResponseBody
    public String upload() {

    }
}
