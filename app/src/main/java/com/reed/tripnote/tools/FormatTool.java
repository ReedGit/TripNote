package com.reed.tripnote.tools;

/**
 * Created by 伟 on 2016/2/14.
 */
public class FormatTool {

    public static boolean isEmail(String email){
        String regex = "^[a-z0-9]+([._\\\\\\\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$";
        return email.matches(regex);
    }
}
