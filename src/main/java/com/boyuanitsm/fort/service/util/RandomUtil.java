package com.boyuanitsm.fort.service.util;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private static final int APP_KEY_COUNT = 12;

    private static final int APP_SECRET_COUNT = 12;

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
}
