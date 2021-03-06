package com.hflw.vasp.framework.components;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties("upload")
public class UploadProperties {

    // 获取存放位置
    private Map<String, String> location;

    public String getBasePath() {
        String location = "";
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            location = this.getLocation().get("windows");
        } else {
            location = this.getLocation().get("linux");
        }
        return location;
    }
}
