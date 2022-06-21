package com.awbd.scheduleservice.config;

import com.awbd.scheduleservice.model.Hours;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("hours")
@Getter
@Setter
public class PropertiesConfig {
    private Hours start;
    private Hours stop;
}
