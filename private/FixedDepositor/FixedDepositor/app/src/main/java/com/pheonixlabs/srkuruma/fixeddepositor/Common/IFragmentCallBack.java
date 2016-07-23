package com.pheonixlabs.srkuruma.fixeddepositor.Common;

/**
 * Created by srkuruma on 7/7/2016.
 */
public interface IFragmentCallBack {
    /**
     * Call back function which gets called from the main activity passing
     * the data from fragment to another activity
     * @param type type of callback
     * @param year
     * @param month
     * @param day
     */
    public void onDateSetCallBack(int type, int year, int month, int day);
}
