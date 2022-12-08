package com.suke.zhjg.common.autofull.endpoint;

import com.suke.zhjg.common.autofull.annotation.AutoDecodeMask;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czx
 * @title: MaskDecodeEnpoint
 * @projectName zhjg
 * @description: TODO
 * @date 2021/1/2816:43
 */
@RestController
@RequestMapping("/mask")
public class MaskDecodeEndpoint {

    @RequestMapping(value = "/decode",method = RequestMethod.GET)
    public String decode(@AutoDecodeMask String data){
        return data;
    }

}
