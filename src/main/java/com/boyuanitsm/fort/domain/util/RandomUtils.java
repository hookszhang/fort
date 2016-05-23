package com.boyuanitsm.fort.domain.util;

import sun.misc.BASE64Encoder;

import java.util.Random;

/**
 * Random utils.
 *
 * @author zhanghua on 5/23/16.
 */
public class RandomUtils {

    private static Random random = new Random();

    private static BASE64Encoder encoder = new BASE64Encoder();

    public static String generateToken(int tokenLength) {
        byte[] newToken = new byte[tokenLength];
        random.nextBytes(newToken);
        return encoder.encode(newToken);
    }
}
