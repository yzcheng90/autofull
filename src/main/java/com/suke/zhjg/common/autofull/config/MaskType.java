package com.suke.zhjg.common.autofull.config;

/**
 * @author czx
 * @title: MaskType
 * @projectName zhjg-common-autofull
 * @description: TODO 脱敏类型
 * @date 2020/11/2514:11
 */
public enum MaskType {
    idCard,
    phone;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
