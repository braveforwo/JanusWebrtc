package com.forward.januswebrtc.domain.plugin.videocall;

import com.forward.januswebrtc.domain.Request;
import com.forward.januswebrtc.domain.plugin.RequestBody;
import lombok.Data;

@Data
public class VideoCallRequestBody extends RequestBody {
    private String username;
}
