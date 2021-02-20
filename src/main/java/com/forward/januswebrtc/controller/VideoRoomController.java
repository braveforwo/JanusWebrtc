package com.forward.januswebrtc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VideoRoomController {
    @RequestMapping("second")
    public String index(){
        return "videoroom";
    }
}
