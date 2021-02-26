package com.forward.januswebrtc.domain.plugin.videoroom;

import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

import java.util.Map;

@Data
public class VideoRoomCreateRequestBody extends RequestBody {

    private Boolean permanent;

    private Long room;

    private String description;

    private String secret;

    private String pin;

    private Boolean is_private;

    private String[] allowed;

    private Long publishers;

    private Map<String, String> ext;
}
