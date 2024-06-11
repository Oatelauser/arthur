package com.arthur.plugin.util;

import com.arthur.common.utils.StringUtils;
import com.arthur.plugin.exception.ArthurException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.arthur.plugin.constant.Constant.LOCAL_ENCRYPTION_ALGORITHM;

/**
 * 本地内部服务验证算法
 *
 * @author DearYang
 * @date 2022-07-18
 * @since 1.0
 */
public class LocalEncryption {

    /**
     * encryption string
     *
     * @param str string
     * @return encryption string
     */
    public static String encrypt(final String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(LOCAL_ENCRYPTION_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new ArthurException(e);
        }
        md5.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md5.digest();
        StringBuilder builder = new StringBuilder(digest.length);
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }

        return builder.toString();
    }

}
