package com.jbit.utils;

import org.springframework.util.DigestUtils;
import sun.misc.BASE64Decoder;

import java.security.MessageDigest;

public class AppUtils {

    public static String encoderByMd5(String str){
        if (str == null){
            return null;
        }
        try {
            return DigestUtils.md5DigestAsHex(str.getBytes());
        }catch (Exception e){
            return  null;
        }
    }
}
