package com.forward.januswebrtc.domain.plugin;

import com.forward.januswebrtc.annotation.JanusCommand;
import com.forward.januswebrtc.domain.common.JanusRequest;
import lombok.Data;

import java.util.Objects;

/**
 * 绑定插件请求
 */
@JanusCommand("janus.attach")
@Data
public class PluginHandleCreateRequest extends JanusRequest {
    private String plugin;

    private static final String PLUGIN_NAME = "janus.plugin.videocall";

    public PluginHandleCreateRequest()
    {
        this.plugin = PLUGIN_NAME;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction);
    }
}
