package com.forward.januswebrtc.util;

import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.annotation.PluginCommand;
import com.forward.januswebrtc.domain.Event;
import com.forward.januswebrtc.domain.Request;
import com.forward.januswebrtc.domain.Response;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;

public class ReflectUtil {
    private static final String PACKAGE_NAME="com.forward.januswebrtc.domain";

    /**
     * 返回请求对应的结果
     * @param request 请求
     * @return response
     * @throws IllegalAccessException h
     * @throws InstantiationException h
     */
    public static Response getCommonResponse(Request request) throws IllegalAccessException, InstantiationException {
        String requestType = null;
//        Response response = null;
        //获取注解的值
        for(Annotation annotation:request.getClass().getAnnotations()){
            if(annotation instanceof JanusCommand) requestType = ((JanusCommand)annotation).value();
            if(annotation instanceof PluginCommand) requestType = ((PluginCommand)annotation).value();
        }
        Reflections reflections = new Reflections(PACKAGE_NAME);
        //返回对应的response类
        for(Class classes:reflections.getSubTypesOf(Response.class)){
            for(Annotation annotation:classes.getAnnotations()){
                if(annotation instanceof JanusCommand) {
                   if(requestType.equalsIgnoreCase(((JanusCommand)annotation).value()))return (Response) classes.newInstance();
                   continue;
                }
                if(annotation instanceof PluginCommand) {
                    if(requestType.equalsIgnoreCase(((PluginCommand)annotation).value()))return (Response) classes.newInstance();
                    continue;
                }
            }
        }
        return null;
    }

    /**
     * 返回对应的事件类
     * @param eventType 事件类型
     * @return 事件类
     * @throws IllegalAccessException h
     * @throws InstantiationException h
     */
    public static Event getEventResponse(String eventType) throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections(PACKAGE_NAME);
        for(Class classes:reflections.getSubTypesOf(Response.class)){
            for(Annotation annotation:classes.getAnnotations()){
                if(annotation instanceof JanusCommand) {
                    if(eventType.equalsIgnoreCase(((JanusCommand)annotation).value()))return (Event) classes.newInstance();
                    continue;
                }
                if(annotation instanceof PluginCommand) {
                    if(eventType.equalsIgnoreCase(((PluginCommand)annotation).value()))return (Event) classes.newInstance();
                    continue;
                }
            }
        }
        return null;
    }


}
