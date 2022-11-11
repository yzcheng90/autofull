package com.suke.zhjg.common.autofull.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author czx
 * @title: ConfigProperties
 * @projectName zhjg
 * @description: TODO
 * @date 2020/12/713:29
 */
@ConfigurationProperties(prefix = "autofull")
public class ConfigProperties {

    // 是否显示日志
    @Setter
    @Getter
    public boolean showLog = true;

    // 最大填充层级
    @Setter
    @Getter
    public int maxLevel = 1;

    // 当前层级
    @Setter
    @Getter
    public int currLevel = 0;

    // 加密分隔标志
    @Setter
    @Getter
    public String encryptFlag = "@suke@";

    // 加密密钥  16位
    @Setter
    @Getter
    public String encryptKeys = "suke172839456000";


}
