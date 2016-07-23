package com.pheonixlabs.srkuruma.fixeddepositor;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.AppLockActivity;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.Calculator;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.DateTimeUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.EndDatePickerFragment;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.IFragmentCallBack;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.StartDatePickerFragment;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.StringUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.FDDbHelper;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

import static com.pheonixlabs.srkuruma.fixeddepositor.ItemDetailFragment.GetFDFromId;
import static com.pheonixlabs.srkuruma.fixeddepositor.ItemDetailFragment.GetIdFromBundles;

public class AddFDPage extends AppLockActivity implements IFragmentCallBack {

    private SQLiteDatabase db;

    private FDDbHelper fdDbHelper;

    private FDEntity entity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fdpage);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.save);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FDSaveButtonClick(view);
            }
        });

        InitiateDB();
        SetBankAutoComplete();

        // set ranges for the number picker
        SetNumberPickerRanges();

        this.entity = GetFDFromId(GetIdFromBundles(getIntent().getExtras()), this.db);

        if(this.entity != null)
        {
            PreLoadDetails();
        }
    }

    private void InitiateDB()
    {
        fdDbHelper = new FDDbHelper(getApplicationContext());
        db = fdDbHelper.getWritableDatabase();
        // fdDbHelper.onUpgrade(db, FDDbHelper.DATABASE_VERSION, FDDbHelper.DATABASE_VERSION);
        // fdDbHelper.onCreate(db);
    }

    private void SetBankAutoComplete()
    {
        String[] arr = getResources().getStringArray(R.array.banks);

        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
                findViewById(R.id.BankAutoComplete);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item, arr);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
    }

    private void SetNumberPickerRanges()
    {
        NumberPicker picker = (NumberPicker)findViewById(R.id.YearPicker);
        picker.setMinValue(0);
        picker.setMaxValue(20);

        picker = (NumberPicker)findViewById(R.id.DaysPicker);
        picker.setMinValue(0);
        picker.setMaxValue(31);

        picker = (NumberPicker) findViewById(R.id.MonthsPicker);
        picker.setMaxValue(12);
        picker.setMinValue(0);
    }

    public void showDatePickerDialog(View view) {

        if (view.getId() == R.id.FDStartDate) {
            DialogFragment newFragment = new StartDatePickerFragment();
            newFragment.setArguments(new Bundle());
            newFragment.show(getFragmentManager(), "StartDatePicker");
        }
        else if(view.getId() == R.id.FDEndDate)
        {
            DialogFragment newFragment = new EndDatePickerFragment();
            newFragment.setArguments(new Bundle());
            newFragment.show(getFragmentManager(), "EndDatePicker");
        }
    }

    @Override
    public void onDateSetCallBack(int type, int year, int month, int day) {
        // indicates start date
        if(type == 0)
        {
            Button startDateButton = (Button)findViewById(R.id.FDStartDate);
            startDateButton.setText(day + "/" + month + "/" + year);
        }
        // indicates end date
        else if(type == 1)
        {
            Button endDateButton = (Button)findViewById(R.id.FDEndDate);
            endDateButton.setText(day + "/" + month + "/" + year);        }
    }

    public void FDSaveButtonClick(View view)
    {
        // Get All FD Details from the object
        FDEntity entity = new FDEntity();

        try
        {
            entity = GetFDDetailsFromView();
        }
        catch (Exception ex)
        {
            final String exceptionMessage = ex.getMessage();
            Snackbar.make(view, exceptionMessage, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }

        try
        {
            List<FDEntity> allFds = ItemListFragment.GetAllFDs(this.db);

            if(this.entity != null)
            {
                entity.ValidateFields(allFds, getResources().getStringArray(R.array.banks), false);
            }
            else
            {
                entity.ValidateFields(allFds, getResources().getStringArray(R.array.banks), true);
            }
        }
        catch (IllegalArgumentException ex)
        {
            final String exceptionMessage = ex.getMessage();
            Snackbar.make(view, exceptionMessage, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }

        // save data in DB
        String insertQuery = CreateInsertQuery(entity);
        db.execSQL(insertQuery);

        Cursor cursor = db.query(FDEntity.TABLE_NAME, null, String.format(FDEntity.SQL_Filter1, entity.FDNumber), null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_HolderName));
                // do what ever you want here
            }while(cursor.moveToNext());
        }
        cursor.close();

        finish();
    }

    public FDEntity GetFDDetailsFromView() throws Exception {
        FDEntity entity = new FDEntity();
        entity.HolderName = ((TextView) findViewById(R.id.HolderNameText)).getText().toString();

        try {
            entity.Amount = Double.parseDouble(((TextView) findViewById(R.id.FDAmountText)).getText().toString());
        }
        catch (Exception ex)
        {
            throw new Exception("Invalid FD Amount.");
        }

        try {
            entity.RateOfInterest = Double.parseDouble((String) ((TextView) findViewById(R.id.ROIInput)).getText().toString());
        }
        catch (Exception ex) {
            throw new Exception("Invalid ROI.");
        }

        entity.AssociatedAccountNumber = (String)((TextView) findViewById(R.id.AssociatedAccountNumber)).getText().toString();
        entity.BankName = (String)((TextView) findViewById(R.id.BankAutoComplete)).getText().toString();
        entity.FDNumber = (String)((TextView) findViewById(R.id.FDNumber)).getText().toString();

        try
        {
            entity.DepositDate = DateTimeUtils.GetDateFromString((String) ((TextView) findViewById(R.id.FDStartDate)).getText().toString(), "dd/MM/yyyy");
        }
        catch (Exception ex)
        {
            throw new Exception("Invalid FD Start Date.");
        }

        try {
            entity.MaturityDate = DateTimeUtils.GetDateFromString((String) ((TextView) findViewById(R.id.FDEndDate)).getText().toString(), "dd/MM/yyyy");
        }
        catch (Exception ex)
        {
            throw new Exception("Invalid FD End Date.");
        }

        entity.AmountAfterMaturity = entity.Amount + Calculator.ComputeInterest(entity.Amount, entity.RateOfInterest, DateTimeUtils.GetDaysBetweenDates(entity.DepositDate, entity.MaturityDate));

        return entity;
    }

    public String CreateInsertQuery(FDEntity entity)
    {
        String insertQuery = String.format(FDEntity.SQL_INSERT, entity.FDNumber, entity.BankName, entity.AssociatedAccountNumber,
                DateTimeUtils.ConvertDateTimeToSQLFormat(entity.DepositDate), entity.Amount, DateTimeUtils.ConvertDateTimeToSQLFormat(entity.MaturityDate), entity.AmountAfterMaturity,
                entity.HolderName, entity.RateOfInterest);

        return insertQuery;
    }

    public void CancelDetailsClick(View view) {
        finish();
    }

    public void ClearAllDetailsClick(View view) {
        ((AutoCompleteTextView)findViewById(R.id.BankAutoComplete)).setText("");
        ((TextView)findViewById(R.id.HolderNameText)).setText("");
        ((TextView)findViewById(R.id.FDAmountText)).setText("");
        ((NumberPicker)findViewById(R.id.YearPicker)).setValue(0);
        ((NumberPicker)findViewById(R.id.MonthsPicker)).setValue(0);
        ((NumberPicker)findViewById(R.id.DaysPicker)).setValue(0);
        ((TextView)findViewById(R.id.ROIInput)).setText("");
        ((TextView)findViewById(R.id.AssociatedAccountNumber)).setText("");
        if(this.entity == null) {
            ((TextView) findViewById(R.id.FDNumber)).setText("");
        }

        ((Button)findViewById(R.id.FDEndDate)).setText("FD END DATE");
        ((Button)findViewById(R.id.FDStartDate)).setText("FD START DATE");
    }

    private void PreLoadDetails()
    {
        ((AutoCompleteTextView)findViewById(R.id.BankAutoComplete)).setText(this.entity.BankName);
        ((EditText)findViewById(R.id.HolderNameText)).setText(this.entity.HolderName);
        ((EditText)findViewById(R.id.FDAmountText)).setText(Double.toString(this.entity.Amount));
        ((NumberPicker)findViewById(R.id.YearPicker)).setValue(0);
        ((NumberPicker)findViewById(R.id.MonthsPicker)).setValue(0);
        ((NumberPicker)findViewById(R.id.DaysPicker)).setValue(0);
        ((EditText)findViewById(R.id.ROIInput)).setText(Double.toString(this.entity.RateOfInterest));
        ((EditText)findViewById(R.id.AssociatedAccountNumber)).setText(this.entity.AssociatedAccountNumber);
        ((EditText)findViewById(R.id.FDNumber)).setText(this.entity.FDNumber);
        ((Button)findViewById(R.id.FDEndDate)).setText(DateTimeUtils.ConvertDateTimeToStringWithFormat(this.entity.MaturityDate, "dd/MM/yyyy"));
        ((Button)findViewById(R.id.FDStartDate)).setText(DateTimeUtils.ConvertDateTimeToStringWithFormat(this.entity.DepositDate, "dd/MM/yyyy"));
        ((EditText)findViewById(R.id.FDNumber)).setEnabled(false);
    }
}
