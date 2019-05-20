package com.planeTicketManager.helpers;

public class StringHelper {
    public static boolean isNullOrEmpty(String str) {
        if (str == null)
            return true;
        else if (str.isEmpty())
            return true;
        else
            return false;
    }
}
