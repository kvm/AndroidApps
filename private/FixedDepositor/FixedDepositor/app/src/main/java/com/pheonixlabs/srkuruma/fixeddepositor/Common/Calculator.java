package com.pheonixlabs.srkuruma.fixeddepositor.Common;

/**
 * Created by srkuruma on 7/23/2016.
 */
public class Calculator {
    public static double ComputeInterest(double principal, double roi, int durationInDays)
    {
        double interestYoY = (principal * roi)/100;
        return (interestYoY/365)*durationInDays;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
