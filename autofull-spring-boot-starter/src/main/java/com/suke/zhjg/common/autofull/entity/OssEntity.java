package com.suke.zhjg.common.autofull.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author czx
 * @title: OssEntity
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2020/11/2514:27
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssEntity {

    public String previewUrl;

    public String bucketName;

}
