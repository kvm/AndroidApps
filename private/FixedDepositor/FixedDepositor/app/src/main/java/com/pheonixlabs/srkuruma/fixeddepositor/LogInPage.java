package com.pheonixlabs.srkuruma.fixeddepositor;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.AppLockActivity;
import com.pheonixlabs.srkuruma.fixeddepositor.Managers.SharedPreferenceManager;

public class LogInPage extends AppCompatActivity {

    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        this.preferenceManager = new SharedPreferenceManager(getApplicationContext());
        //this.preferenceManager.RemoveFirstTime();
        if(this.preferenceManager.GetFirstTime() != null)
        {
            Intent intent = new Intent(this, UnLockPage.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppLockActivity.CallingActivityNameKey, "ItemListActivity");
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void SignInClick(View view) {

        try
        {
            // Validate All fields
            if(ValidateFields())
            {
                this.preferenceManager.SetEmail(((EditText)findViewById(R.id.EmailId1)).getText().toString());
                this.preferenceManager.SetPin(((EditText) findViewById(R.id.Pin1)).getText().toString());
                this.preferenceManager.SetFirstTime();
                Intent intent = new Intent(this, ItemListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(UnLockPage.UnlockKey, "unlock");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
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

    private boolean ValidateFields()
    {
        // validate email address
        String emailId1 = ((EditText)findViewById(R.id.EmailId1)).getText().toString();
        String emailId2 = ((EditText)findViewById(R.id.EmailId2)).getText().toString();
        String pin1 = ((EditText)findViewById(R.id.Pin1)).getText().toString();
        String pin2 = ((EditText)findViewById(R.id.Pin2)).getText().toString();

        if(!isValidEmail(emailId1) || !isValidEmail(emailId2))
        {
            throw new IllegalArgumentException("Invalid E-Mail address.");
        }

        if(!emailId1.equals(emailId2))
        {
            throw new IllegalArgumentException("EmailIds do not match.");
        }

        if(!(pin1.length() == 4) || !(pin2.length() == 4))
        {
            throw new IllegalArgumentException("Pin must be of length 4.");
        }

        if(!pin1.equals(pin2))
        {
            throw new IllegalArgumentException("Pins do not match.");
        }

        return true;
    }

    public static boolean isValidEmail(String target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
