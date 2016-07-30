package com.pheonixlabs.srkuruma.fixeddepositor;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.StringUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.FDDbHelper;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;
import com.pheonixlabs.srkuruma.fixeddepositor.Managers.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class FilterDialog extends AppCompatActivity {

    private SQLiteDatabase db;
    private FDDbHelper dbHelper;
    private List<String> allBanks;
    private List<String> allHolderNames;
    private SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_dialog);
        this.dbHelper = new FDDbHelper(getApplicationContext());
        this.db = this.dbHelper.getWritableDatabase();
        List<FDEntity> allFds = ItemListFragment.GetAllFDs(this.db);
        this.allBanks = new ArrayList<String>();
        this.allHolderNames = new ArrayList<>();
        this.sharedPreferenceManager = new SharedPreferenceManager(getApplicationContext());

        for (int i = 0; i < allFds.size(); i++)
        {
            if(!this.allBanks.contains(allFds.get(i).BankName))
            {
                this.allBanks.add(allFds.get(i).BankName);
            }

            if(!this.allHolderNames.contains(allFds.get(i).HolderName))
            {
                this.allHolderNames.add(allFds.get(i).HolderName);
            }
        }

        Spinner bankView = (Spinner) findViewById(R.id.BankNameFilter);
        this.allBanks.add(0, "None");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, this.allBanks);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankView.setAdapter(dataAdapter);
        String setBankFilter = sharedPreferenceManager.GetBankFilter();
        if(!StringUtils.IsStringNullOrEmpty(setBankFilter) && !setBankFilter.equals("None"))
        {
            int index = this.allBanks.indexOf(setBankFilter);
            bankView.setSelection(index);
        }

        Spinner holderView = (Spinner) findViewById(R.id.HolderNameFilter);
        this.allHolderNames.add(0, "None");
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, this.allHolderNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holderView.setAdapter(dataAdapter);
        String setHolderFilter = sharedPreferenceManager.GetHolderFilter();
        if(!StringUtils.IsStringNullOrEmpty(setHolderFilter) && !setHolderFilter.equals("None"))
        {
            int index = this.allHolderNames.indexOf(setHolderFilter);
            holderView.setSelection(index);
        }
    }

    public void onClickFilter(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("BankName", ((Spinner) findViewById(R.id.BankNameFilter)).getSelectedItem().toString());
        bundle.putString("HolderName", ((Spinner) findViewById(R.id.HolderNameFilter)).getSelectedItem().toString());
        bundle.putString(UnLockPage.UnlockKey, "unlock");
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtras(bundle);
        this.sharedPreferenceManager.SetBankFilter(((Spinner) findViewById(R.id.BankNameFilter)).getSelectedItem().toString());
        this.sharedPreferenceManager.SetHolderFilter(((Spinner) findViewById(R.id.HolderNameFilter)).getSelectedItem().toString());

        startActivity(intent);
        finish();
    }

    private boolean ValidateInputFields()
    {
        //String bankName = ((AutoCompleteTextView)findViewById(R.id.BankNameFilter)).getText().toString();
        //String holderName = ((AutoCompleteTextView)findViewById(R.id.HolderNameFilter)).getText().toString();
        /*String lowerRange = ((TextView)findViewById(R.id.LowerRange)).getText().toString();
        String upperRange =  ((TextView)findViewById(R.id.HigherRange)).getText().toString();

        if(!StringUtils.IsStringNullOrEmpty(lowerRange) || !StringUtils.IsStringNullOrEmpty(upperRange))
        {
            if(StringUtils.IsStringNullOrEmpty(lowerRange) || StringUtils.IsStringNullOrEmpty(upperRange))
            {
                throw new IllegalArgumentException("One of the amount ranges is empty.");
            }
        }*/

        return true;
    }

    public void onClickClear(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(UnLockPage.UnlockKey, "unlock");
        Intent intent = new Intent(this, ItemListActivity.class);
        intent.putExtras(bundle);
        this.sharedPreferenceManager.SetBankFilter("None");
        this.sharedPreferenceManager.SetHolderFilter("None");

        startActivity(intent);
        finish();
    }
}
