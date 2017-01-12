/*
 * Copyright 2016-2017 Shanghai Boyuan IT Services Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private static final int USER_TOKEN_COUNT = 64;

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
        return RandomStringUtils.randomAlphanumeric(USER_TOKEN_COUNT);
    }
}

