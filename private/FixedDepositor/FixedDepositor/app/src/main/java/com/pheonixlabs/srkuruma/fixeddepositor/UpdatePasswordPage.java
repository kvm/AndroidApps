package com.pheonixlabs.srkuruma.fixeddepositor;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.AppLockActivity;
import com.pheonixlabs.srkuruma.fixeddepositor.Managers.SharedPreferenceManager;

public class UpdatePasswordPage extends AppCompatActivity {

    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password_page);
        this.preferenceManager = new SharedPreferenceManager(getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void UpdatePassword(View view) {
        try
        {
            if(ValidateFields())
            {
                this.preferenceManager.SetPin(((EditText) findViewById(R.id.NewPassword)).getText().toString());
                Snackbar snackbar = Snackbar.make(view, "Pin successfully updated.", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(Color.parseColor("#00FF00"));
                snackbar.getView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                snackbar.setAction("Action", null).show();
                /*Bundle bundle = new Bundle();
                bundle.putString(UnLockPage.UnlockKey, "unlock");
                Intent intent = new Intent(this, ItemListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();*/
            }
        }
        catch (IllegalArgumentException ex)
        {
            Snackbar snackbar = Snackbar.make(view, ex.getMessage(), Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(Color.parseColor("#ff0000"));
            snackbar.getView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.setAction("Action", null).show();
        }
    }

    public boolean ValidateFields() {
        String oldPin = ((EditText) findViewById(R.id.OldPassword)).getText().toString();
        String newPin = ((EditText) findViewById(R.id.NewPassword)).getText().toString();
        String confirmedNewPin = ((EditText) findViewById(R.id.ConfirmPassword)).getText().toString();

        if(!oldPin.equals(this.preferenceManager.GetPin()))
        {
            throw new IllegalArgumentException("Incorrect old Pin");
        }

        if(newPin.length() != 4 || confirmedNewPin.length() != 4)
        {
            throw new IllegalArgumentException("Pin must be of 4 digits.");
        }

        if(!newPin.equals(confirmedNewPin))
        {
            throw new IllegalArgumentException("New pins don't match.");
        }

        if(newPin.equals(oldPin))
        {
            throw new IllegalArgumentException("New pin can't be same as the old pin.");
        }

        return true;
    }
}
