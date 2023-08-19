package com.touki.blog.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Touki
 */
public abstract class StringUtil {

    public static boolean hasSpecialChar(String trimStr) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(trimStr);
        return m.find();
    }
}
