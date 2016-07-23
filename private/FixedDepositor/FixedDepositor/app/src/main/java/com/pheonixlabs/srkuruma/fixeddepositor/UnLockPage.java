package com.pheonixlabs.srkuruma.fixeddepositor;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.AppLockActivity;
import com.pheonixlabs.srkuruma.fixeddepositor.Managers.SharedPreferenceManager;

public class UnLockPage extends AppCompatActivity {

    public static final String UnlockKey = "Unlock";

    private EditText passText;

    private SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_lock_page);
        this.preferenceManager = new SharedPreferenceManager(getApplicationContext());
        passText = (EditText)findViewById(R.id.UnLockText);
        passText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String pin = preferenceManager.GetPin();

                    String inputPin = v.getText().toString();

                    if(pin == null || !inputPin.equals(pin))
                    {
                        Snackbar snackbar = Snackbar.make(v, "Invalid Pin.", Snackbar.LENGTH_SHORT);
                        snackbar.getView().setBackgroundColor(Color.parseColor("#FF0000"));
                        snackbar.getView().setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        snackbar.setAction("Action", null).show();
                        return false;
                    }

                    Bundle receivedBundle = getIntent().getExtras();

                    String className = receivedBundle.getString(AppLockActivity.CallingActivityNameKey);

                    Intent intent = new Intent();
                    if(className.equals("ItemListActivity")) {
                        intent = new Intent(getApplicationContext(), ItemListActivity.class);
                    }
                    else if(className.equals("AddFDPage")) {
                        intent = new Intent(getApplicationContext(), AddFDPage.class);
                    }
                    else if(className.equals("ItemDetailActivity"))
                    {
                        intent = new Intent(getApplicationContext(), ItemDetailActivity.class);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString(UnlockKey, "unlock");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

}
