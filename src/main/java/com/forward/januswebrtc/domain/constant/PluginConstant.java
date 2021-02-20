package com.forward.januswebrtc.domain.constant;

/**
 *
 * 用来判断哪些响应事件是否需要由服务器推送到前端
 */
public class PluginConstant {
    public final static String[] needToPushEvent = {"incomingcall","accepted","registered"};

    public static Boolean isNeedToPush(String eventType) {
        for (int i=0;i<needToPushEvent.length;i++){
            if(needToPushEvent[i].equals(eventType)) return true;
        }
        return false;
    }
}
