package com.pheonixlabs.srkuruma.fixeddepositor.Managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by srkuruma on 7/23/2016.
 */
public class SharedPreferenceManager {

    private String PREFS_NAME = "preferences";

    private String EmailKey = "Email";

    private String PinKey = "Pin";

    private String IsFirstTimeKey = "FirstTime";

    private String BankFilterKey = "BankFilter";

    private String HolderFilterKey = "HolderFilter";

    private SharedPreferences settings;

    public SharedPreferenceManager(Context context)
    {
        this.settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    public void SetEmail(String email)
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(EmailKey, email);
        editor.commit();
    }

    public void SetPin(String pin)
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(PinKey, pin);
        editor.commit();
    }

    public String GetPin()
    {
        return this.settings.getString(PinKey, null);
    }

    public String GetEmail()
    {
        return this.settings.getString(EmailKey, null);
    }

    public String GetFirstTime()
    {
        return this.settings.getString(IsFirstTimeKey, null);
    }

    public void SetFirstTime()
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(IsFirstTimeKey, "Yes");
        editor.commit();
    }

    public void RemoveFirstTime()
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.remove(IsFirstTimeKey);
        editor.commit();
    }

    public String GetHolderFilter()
    {
        return this.settings.getString(HolderFilterKey, null);
    }

    public String GetBankFilter()
    {
        return this.settings.getString(BankFilterKey, null);
    }

    public void SetBankFilter(String bank)
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(BankFilterKey, bank);
        editor.commit();
    }

    public void SetHolderFilter(String holder)
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(HolderFilterKey, holder);
        editor.commit();
    }

    public void RemoveBankFilter()
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.remove(BankFilterKey);
        editor.commit();
    }

    public void RemoveHolderFilter()
    {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.remove(HolderFilterKey);
        editor.commit();
    }
}
