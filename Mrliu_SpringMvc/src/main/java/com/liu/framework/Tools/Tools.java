package com.liu.framework.Tools;

/**
 * @author liu_l
 * @Title: Tools
 * @ProjectName workspace-idea
 * @Description: TODO
 * @date 2018/6/2611:49
 */
public class Tools {

    //将String字符串首字母转为小写（默认首字母为大写）
    public static String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
