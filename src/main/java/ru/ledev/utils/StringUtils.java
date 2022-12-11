package ru.ledev.utils;

public class StringUtils {
    public static String formatString(String sourceString) {
        return sourceString.trim().replace('&', '§');
    }
}
