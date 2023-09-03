package com.ld.poetry.utils;

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
}
