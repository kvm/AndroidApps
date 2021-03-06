package com.pheonixlabs.srkuruma.fixeddepositor;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.AppLockActivity;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.FDDbHelper;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;

import static com.pheonixlabs.srkuruma.fixeddepositor.ItemDetailFragment.GetFDFromId;
import static com.pheonixlabs.srkuruma.fixeddepositor.ItemDetailFragment.GetIdFromBundles;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ItemDetailFragment}.
 */
public class ItemDetailActivity extends AppLockActivity {

    private SQLiteDatabase db;
    private FDDbHelper fdHelper;
    private FDEntity entity;
    private String fdIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        this.fdHelper = new FDDbHelper(getApplicationContext());
        this.db = this.fdHelper.getWritableDatabase();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // Show the Up button in the action bar.
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            fdIndex = getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID);
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, fdIndex);
            this.entity = GetFDFromId(GetIdFromBundles(arguments), this.db);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container1, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onDeleteClick(View view) {
        this.db.execSQL(String.format(FDEntity.SQL_DELETE_ROW, this.entity.FDNumber));
        finish();
    }

    public void onEditClick(View view) {
        Bundle arguments = new Bundle();
        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, this.fdIndex);
        arguments.putString(UnLockPage.UnlockKey, "unlock");
        Intent intent = new Intent(ItemDetailActivity.this, AddFDPage.class);
        intent.putExtras(arguments);
        ItemDetailActivity.this.startActivity(intent);
        finish();
    }
}
