package com.example.kdoushen.douyin.controller.feed;

import com.example.kdoushen.douyin.controller.publish.ListController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional
public class FeedController {
    private static final Logger log= LoggerFactory.getLogger(FeedController.class);

    @GetMapping("/douyin/feed/")
    @ResponseBody
    public String feed() {

        return "";
    }
}
