package com.pheonixlabs.srkuruma.fixeddepositor.Common;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.ViewParent;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by srkuruma on 6/5/2016.
 */
public class EndDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Activity activity;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        ((IFragmentCallBack)activity).onDateSetCallBack(1, year, monthOfYear, dayOfMonth);
    }
}
