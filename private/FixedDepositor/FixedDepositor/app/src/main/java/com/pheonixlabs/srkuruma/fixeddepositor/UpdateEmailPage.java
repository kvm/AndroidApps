package com.pheonixlabs.srkuruma.fixeddepositor;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pheonixlabs.srkuruma.fixeddepositor.Managers.SharedPreferenceManager;

public class UpdateEmailPage extends AppCompatActivity {

    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email_page);
        this.preferenceManager = new SharedPreferenceManager(getApplicationContext());
    }

    public boolean ValidateFields() {
        String oldEmail = ((EditText) findViewById(R.id.OldEmail)).getText().toString();
        String newEmail = ((EditText) findViewById(R.id.NewEmail)).getText().toString();
        String confirmedEmail = ((EditText) findViewById(R.id.ConfirmEmail)).getText().toString();

        if(!oldEmail.equals(this.preferenceManager.GetEmail()))
        {
            throw new IllegalArgumentException("Incorrect old email");
        }

        if(!LogInPage.isValidEmail(newEmail) || !LogInPage.isValidEmail(confirmedEmail))
        {
            throw new IllegalArgumentException("Email is not valid");
        }

        if(!newEmail.equals(confirmedEmail))
        {
            throw new IllegalArgumentException("Emails don't match.");
        }

        if(newEmail.equals(oldEmail))
        {
            throw new IllegalArgumentException("New Email same as the old one.");
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void UpdateEmail(View view) {
        try
        {
            if(ValidateFields())
            {
                this.preferenceManager.SetEmail(((EditText) findViewById(R.id.NewEmail)).getText().toString());
                Snackbar snackbar = Snackbar.make(view, "Email successfully updated.", Snackbar.LENGTH_SHORT);
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
}
