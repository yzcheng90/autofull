package com.suke.zhjg.common.autofull.util;

import cn.hutool.core.codec.Base64;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * Created by czx on 2017/3/13.
 */
@Slf4j
public class CryptUtil {
    private static final CryptUtil a = new CryptUtil();
    private static Key b;

    static {
        try {
            ConfigProperties bean = ApplicationContextRegister.getApplicationContext().getBean(ConfigProperties.class);
            b = new SecretKeySpec(bean.getEncryptKeys().getBytes("UTF-8"),"AES");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static CryptUtil setKey(String key) {
        try {
            b = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;
    }

    public static String encrypt(String str) {
        byte[] encrypt = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(b.getEncoded(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypt = cipher.doFinal(str.getBytes("UTF-8"));
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("encrypt error :{}",e.getMessage());
        }
        return Base64.encode(encrypt);
    }

    public static String decrypt(String str) {
        byte[] decrypt = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(b.getEncoded(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypt = cipher.doFinal(Base64.decode(str));
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("decrypt error :{}",e.getMessage());
        }
        return new String(decrypt).trim();
    }

//    public static void main(String args[]){
////        Crypt.setkey("asc_efghiklmnius2018");
//        Crypt.setkey("asc_efghiklm2018");
//        String content="CP878|PU3004|SM001|4872034|V1";
//
//        String result= Crypt.encrypt(content);
//        System.out.println(result);
//        result= Crypt.decrypt(result);
//        System.out.println(result);
//
//    }
}






