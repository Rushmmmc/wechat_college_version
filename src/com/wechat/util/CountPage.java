package com.mywechat.util;

/**
 * 通过总数量根据页数所需呈现的条数而返回页数
 */
public class CountPage {
    public static int getSize(int number, int size) {
        int countPages;
        if (number % size == 0) {
            countPages = number / size;
        } else if (number / size == 0) {
            countPages = 1;
        } else {
            countPages = number / size + 1;
        }
        return countPages;
    }
}
