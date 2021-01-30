package com.java.yxt.logger;

public class BeanNameTool {
//    public static String lowerFirstCapse(String str){
//        char[]chars = str.toCharArray();
//        if(chars[0]>96&&chars[0]<122){
//            chars[0] -= 32;
//        }
//        Character.isLowerCase(str.charAt(0));
//        return String.valueOf(chars);
//    }
    public static String toLowerCaseFirstOne(String s)
    {
        if(Character.isLowerCase(s.charAt(0))){
            return s;
        }
        else{
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
    //首字母转大写
    public static String toUpperCaseFirstOne(String s)
    {
        if(Character.isUpperCase(s.charAt(0))){
            return s;
        }
        else{
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
