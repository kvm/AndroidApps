package com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.DateTimeUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.StringUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.FDDbHelper;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Years;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by srkuruma on 7/9/2016.
 */
public class FDEntity implements BaseColumns{
    public String BankName;

    public String HolderName;

    public double Amount;

    public double AmountAfterMaturity;

    public Date DepositDate;

    public Date MaturityDate;

    public String FDNumber;

    public String AssociatedAccountNumber;

    public double RateOfInterest;

    public boolean ValidateFields(List<FDEntity> allFds, String[] banks, boolean duplicateFdNumberCheck)
    {
        // validate Bank
        if(!IsValidBankName(banks))
        {
            throw new IllegalArgumentException("Invalid Bank Name. Please select from the drop down.");
        }

        if(StringUtils.IsStringNullOrEmpty(HolderName))
        {
            throw new IllegalArgumentException("Invalid Account Holder Name.");
        }

        if(Amount == 0.0)
        {
            throw new IllegalArgumentException("Invalid Amount.");
        }

        if(DepositDate == null)
        {
            throw new IllegalArgumentException("Invalid FD Start Date.");
        }

        if(MaturityDate == null)
        {
            throw new IllegalArgumentException("Invalid FD End Date.");
        }

        if(StringUtils.IsStringNullOrEmpty(FDNumber))
        {
            throw new IllegalArgumentException("Invalid FD Number.");
        }

        if(RateOfInterest == 0)
        {
            throw new IllegalArgumentException("Invalid ROI.");
        }

        if(!DepositDate.before(MaturityDate))
        {
            throw new IllegalArgumentException("FD Start Date should be before End Date.");
        }

        boolean fdAlreadyExists = false;
        for(int i = 0; i < allFds.size(); i++)
        {
            if(allFds.get(i).FDNumber.equals(this.FDNumber) && !StringUtils.IsStringNullOrEmpty(this.FDNumber))
            {
                fdAlreadyExists = true;
                break;
            }
        }

        if(duplicateFdNumberCheck && fdAlreadyExists)
        {
            throw new IllegalArgumentException("FD with same FDNumber already exists.");
        }

        return true;
    }

    public boolean IsValidBankName(String[] banks)
    {
        if(StringUtils.IsStringNullOrEmpty(BankName))
        {
            return false;
        }

        if(!Arrays.asList(banks).contains(BankName))
        {
            return false;
        }

        return true;
    }

    public static FDEntity GetFDFromCursor(Cursor cursor)
    {
        FDEntity entity = new FDEntity();
        entity.FDNumber = cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_FD_Number));
        entity.BankName = cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_Bank));
        entity.Amount = cursor.getDouble(cursor.getColumnIndex(FDEntity.COLUMN_NAME_Amount));
        entity.AmountAfterMaturity = cursor.getDouble(cursor.getColumnIndex(FDEntity.COLUMN_NAME_AmountAfterMaturity));
        entity.HolderName = cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_HolderName));
        entity.RateOfInterest = cursor.getDouble(cursor.getColumnIndex(FDEntity.COLUMN_Name_ROI));

        try {
            entity.DepositDate = DateTimeUtils.GetDateFromString(cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_DepositDate)), "yyyy-MM-dd");
        }
        catch (Exception ex)
        {
            // do something here
            entity.DepositDate = null;
        }

        try {
            entity.MaturityDate = DateTimeUtils.GetDateFromString(cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_MaturityDate)), "yyyy-MM-dd");
        }
        catch (Exception ex)
        {
            // do something here
            entity.MaturityDate = null;
        }

        return entity;
    }

    /**
     * This function normalizes maturity date into a friendly string
     * Today, Tomorrow, In 29 days, 3 months, 1 year 3 months
     * @return
     */
    public String GetNormalisedDuration()
    {
        LocalDate maturityLocalDate = new LocalDate(this.MaturityDate);

        LocalDate currentLocalDate = new LocalDate();

        if(maturityLocalDate.compareTo(currentLocalDate) == 0)
        {
            return "Today";
        }

        int days = Days.daysBetween(currentLocalDate, maturityLocalDate).getDays();

        if(days < 0)
        {
            return "Expired";
        }

        if(days == 1)
        {
            return "Tomorrow";
        }

        if(days >= 2 && days <= 29)
        {
            return "After ".concat(Integer.toString(days).concat(" Days"));
        }

        int years = days/365;
        int months = (days - 365*years)/30;

        if(years == 0)
        {
            return "After ".concat(Integer.toString(months).concat(" Month(s)"));
        }

        if(years != 0 && months == 0)
        {
            return "After ".concat(Integer.toString(years).concat(" Year(s)"));
        }

        if(years != 0 && months != 0)
        {
            return "After ".concat(Integer.toString(years).concat(" Year(s) ").concat(Integer.toString(months)).concat(" Month(s)"));
        }

        return "";
    }

    public void SetUnknownValues()
    {
        this.BankName = "unknown";
        this.Amount = -1;
        this.AmountAfterMaturity = -1;
    }

    // Constants
    public static String TableName = "FDEntries";

    public static final String TABLE_NAME = "FDContract";

    public static final String COLUMN_NAME_HolderName = "HolderName";

    public static final String COLUMN_NAME_Amount = "Amount";

    public static final String COLUMN_NAME_AmountAfterMaturity = "AmountAfterMaturity";

    public static final String COLUMN_NAME_DepositDate = "DepositDate";

    public static final String COLUMN_NAME_MaturityDate = "MaturityDate";

    public static final String COLUMN_NAME_Bank = "BankName";

    public static final String COLUMN_NAME_FD_Number = "FDNumber";

    public static final String COLUMN_NAME_AssociatedAccountNumber = "AssociatedAccNumber";

    public static final String COLUMN_Name_ROI = "RateofInterest";

    // CREATE TABLE FDEntries(Id INTEGER PRIMARY KEY AUTOINCREMENT, Amount DOUBLE
    public static final String SQL_CREATE_FDENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FDEntity.TABLE_NAME + " (" +
                    FDEntity.COLUMN_NAME_FD_Number + " TEXT PRIMARY KEY," +
                    FDEntity.COLUMN_NAME_Bank + FDDbHelper.TEXT_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_NAME_AssociatedAccountNumber + FDDbHelper.TEXT_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_NAME_DepositDate + FDDbHelper.DATETIME_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_NAME_Amount + FDDbHelper.DOUBLE_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_NAME_MaturityDate + FDDbHelper.DATETIME_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_NAME_AmountAfterMaturity + FDDbHelper.DOUBLE_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_NAME_HolderName + FDDbHelper.TEXT_TYPE + FDDbHelper.COMMA_SEP +
                    FDEntity.COLUMN_Name_ROI + FDDbHelper.DOUBLE_TYPE +
                    " )";

    // Add Row to Table
    public static final String SQL_INSERT = "INSERT OR REPLACE INTO " + FDEntity.TABLE_NAME + " (" + FDEntity.COLUMN_NAME_FD_Number
             + "," + FDEntity.COLUMN_NAME_Bank
             + "," + FDEntity.COLUMN_NAME_AssociatedAccountNumber
             + "," + FDEntity.COLUMN_NAME_DepositDate
             + "," + FDEntity.COLUMN_NAME_Amount
             + "," + FDEntity.COLUMN_NAME_MaturityDate
             + "," + FDEntity.COLUMN_NAME_AmountAfterMaturity
             + "," + FDEntity.COLUMN_NAME_HolderName
             + "," + FDEntity.COLUMN_Name_ROI + ")"
             + " VALUES (" + "\"%s\", \"%s\", \"%s\", (DATETIME('%s')), %f, (DATETIME('%s')), %f, \"%s\", %f" + ")";


    // Delete Row
    public static final String SQL_DELETE_ROW = "DELETE FROM " + FDEntity.TABLE_NAME + " WHERE " + FDEntity.COLUMN_NAME_FD_Number + " = \"%s\"";

    public static final String SQL_Filter1 = " " + FDEntity.COLUMN_NAME_FD_Number + " = \"%s\"";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
