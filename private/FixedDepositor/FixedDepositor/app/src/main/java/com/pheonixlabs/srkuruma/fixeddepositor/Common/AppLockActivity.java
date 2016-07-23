package com.pheonixlabs.srkuruma.fixeddepositor.Common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pheonixlabs.srkuruma.fixeddepositor.ItemListActivity;
import com.pheonixlabs.srkuruma.fixeddepositor.UnLockPage;

/**
 * Created by srkuruma on 7/23/2016.
 */
public class AppLockActivity extends AppCompatActivity {

    public static final String CallingActivityNameKey = "CallingActivity";

    @Override
    public void onResume()
    {
        super.onResume();

        Bundle receivedBundle = getIntent().getExtras();

        if(!receivedBundle.containsKey(UnLockPage.UnlockKey)) {

            Bundle bundle = new Bundle();
            String className = this.getClass().getSimpleName();
            bundle.putString(CallingActivityNameKey, className);

            Intent intent = new Intent(this, UnLockPage.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
