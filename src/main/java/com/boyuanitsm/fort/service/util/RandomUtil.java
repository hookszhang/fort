package com.boyuanitsm.fort.service.util;

import org.apache.commons.lang.RandomStringUtils;
import sun.misc.BASE64Encoder;

import java.util.Random;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static Random random = new Random();
    private static BASE64Encoder encoder = new BASE64Encoder();

    private static final int DEF_COUNT = 20;
    private static final int APP_KEY_COUNT = 12;
    private static final int APP_SECRET_COUNT = 12;
    private static final int USER_TOKEN_COUNT = 32;

    private RandomUtil() {
    }

    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
    * Generates a reset key.
    *
    * @return the generated reset key
    */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generates a app key.
     *
     * @return the generated app key
     */
    public static String generateAppKey() {
        return RandomStringUtils.randomAlphanumeric(APP_KEY_COUNT).toLowerCase();
    }

    /**
     * Generates a app secret.
     *
     * @return the generated app secret
     */
    public static String generateAppSecret() {
        return RandomStringUtils.randomAlphanumeric(APP_SECRET_COUNT).toLowerCase();
    }

    /**
     * Generates a token.
     *
     * @return the generated user token
     */
    public static String generateToken() {
        byte[] newToken = new byte[USER_TOKEN_COUNT];
        random.nextBytes(newToken);
        return encoder.encode(newToken);
    }
}
