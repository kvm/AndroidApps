package com.pheonixlabs.srkuruma.fixeddepositor.Common;

/**
 * Created by srkuruma on 7/9/2016.
 */
public class StringUtils {
    public static boolean IsStringNullOrEmpty(String s)
    {
        if(s != null && !s.isEmpty())
        {
            return false;
        }

        return true;
    }

    public static String ConvertDoubleToStringWithoutScientificNotation(double value)
    {
        return String.format("%.0f", value);
    }
}
