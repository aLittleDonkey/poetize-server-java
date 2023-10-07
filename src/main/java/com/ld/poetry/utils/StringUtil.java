package com.ld.poetry.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static void main(String[] args) {
        System.out.println(removeHtml(""));
    }

    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";

    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";

    public static String removeHtml(String content) {
        return content.replace("</", "《/")
                .replace("/>", "/》")
                .replace("<script", "《style")
                .replace("<style", "《style")
                .replace("<img", "《img")
                .replace("<br", "《br")
                .replace("<input", "《input");
    }

    public static boolean matchString(String text, String searchText) {
        Pattern pattern = Pattern.compile(Pattern.quote(searchText), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    public static boolean isValidFileName(String fileName) {
        if (!StringUtils.hasText(fileName) || fileName.length() > 128) {
            return false;
        }
        // 此示例允许文件名包含字母、数字、下划线、点号和连字符（减号），且不能以点号、下划线和连字符（减号）开头
        String regex = "^(?![-._])[a-zA-Z0-9-._]+$";
        return fileName.matches(regex);
    }

    public static boolean isValidDirectoryName(String directoryName) {
        if (!StringUtils.hasText(directoryName) || directoryName.length() > 128) {
            return false;
        }
        // 此示例允许目录名称只包含字母、数字、下划线和连字符（减号）
        String regex = "^[a-zA-Z0-9-_]+$";
        return directoryName.matches(regex);
    }
}
