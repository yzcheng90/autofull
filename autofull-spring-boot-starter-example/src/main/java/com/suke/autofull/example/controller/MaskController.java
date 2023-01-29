package com.suke.autofull.example.controller;

import com.suke.autofull.example.entity.TbUser;
import com.suke.zhjg.common.autofull.annotation.AutoDecodeMask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czx
 * @title: MaskController
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2022/12/810:48
 */
@Slf4j
@RestController
public class MaskController {

    @PostMapping(value = "/testMaskBody")
    public TbUser testMaskBody(@RequestBody @AutoDecodeMask TbUser param){
        log.info("param:{}",param);
        return param;
    }

    @GetMapping(value = "/get")
    public String testMaskBody(@AutoDecodeMask String userName){
        log.info("param:{}",userName);
        return userName;
    }

}
