package com.savaleox.hockeyteam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.async")
public class AsyncProperties {

    private long statisticDelayMs = 15000;

    public long getStatisticDelayMs() {
        return statisticDelayMs;
    }

    public void setStatisticDelayMs(long statisticDelayMs) {
        this.statisticDelayMs = statisticDelayMs;
    }
}
