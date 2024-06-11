package com.broadtech.arthur.admin.resource.config;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TokenConfigTest {


    /**
     * 是否匹配用于SQL（正则表达式）
     * % 号表示所有字符
     * _ 号表示单个字符
     *
     * @param src 原始字符串
     * @param des 可以含有通配符的需要匹配的字符串
     * @return 是否匹配
     */

    public static boolean isMatchingForSQL(String src, String des) {

        char[] strs = des.toCharArray();
        StringBuilder sbNew = new StringBuilder();
        for (char c : strs) {
            switch (c) {
                case '.':
                case '*':
                case '+':
                case '$':
                case '?':
                case '(':
                case ')':
                case '[':
                case ']':
                case '\\':
                case '^':
                case '{':
                case '}':
                case '|':
                    sbNew.append("\\").append(c);
                    break;
                case '%':
                    sbNew.append("(.*)");
                    break;
                case '_':
                    sbNew.append(".{1}");
                    break;
                default:
                    sbNew.append(c);
            }
        }
        Pattern p = Pattern.compile(sbNew.toString());
        Matcher m = p.matcher(src);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMatchingForSQL("8610556324234234234", "%10556%423%"));
    }

}