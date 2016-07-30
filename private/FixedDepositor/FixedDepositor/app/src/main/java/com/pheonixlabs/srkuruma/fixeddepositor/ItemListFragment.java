package com.pheonixlabs.srkuruma.fixeddepositor;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pheonixlabs.srkuruma.fixeddepositor.Adapters.FDListAdapter;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.StringUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Comparators.FDComparator;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.FDDbHelper;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;
import com.pheonixlabs.srkuruma.fixeddepositor.Managers.SharedPreferenceManager;
import com.pheonixlabs.srkuruma.fixeddepositor.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private FDDbHelper dbHelper;
    private SQLiteDatabase db;
    private SharedPreferenceManager sharedPreferenceManager;
    private String filterBank;
    private String filterHolder;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sharedPreferenceManager = new SharedPreferenceManager(getContext());

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        // read from db all the fds
        this.dbHelper = new FDDbHelper(getContext());
        this.db = this.dbHelper.getWritableDatabase();
        this.filterBank = this.sharedPreferenceManager.GetBankFilter();
        this.filterHolder = this.sharedPreferenceManager.GetHolderFilter();
        //this.sharedPreferenceManager.RemoveBankFilter();
        //this.sharedPreferenceManager.RemoveHolderFilter();

        PopulateFdList();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(DummyContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onResume()
    {
        // After a pause OR at startup
        super.onResume();
        PopulateFdList();
    }

    private void PopulateFdList()
    {
        List<FDEntity> fds = FilterFDs(GetAllFDs(this.db));

        FDListAdapter adapter = new FDListAdapter(
                getContext(),
                android.R.layout.list_content,
                fds);

        setListAdapter(adapter);
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public static List<FDEntity> GetAllFDs(SQLiteDatabase db)
    {
        List<FDEntity> entities = new ArrayList<FDEntity>();
        Cursor cursor = db.query(FDEntity.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                // String data = cursor.getString(cursor.getColumnIndex(FDEntity.COLUMN_NAME_HolderName));
                // do what ever you want here
                FDEntity entity = FDEntity.GetFDFromCursor(cursor);
                entities.add(entity);
            }while(cursor.moveToNext());
        }
        cursor.close();

        Collections.sort(entities, new FDComparator());

        return entities;
    }

    public List<FDEntity> FilterFDs(List<FDEntity> allFds)
    {
        if(this.filterHolder == null && this.filterBank == null)
        {
            return allFds;
        }

        List<FDEntity> filteredFds = new ArrayList<FDEntity>();
        if(!StringUtils.IsStringNullOrEmpty(this.filterBank) && !this.filterBank.equals("None"))
        {
            for (int i = 0; i < allFds.size(); i++)
            {
                if(allFds.get(i).BankName.equals(this.filterBank))
                {
                    filteredFds.add(allFds.get(i));
                }
            }
            allFds = new ArrayList<FDEntity>(filteredFds);
        }

        if(!StringUtils.IsStringNullOrEmpty(this.filterHolder) && !this.filterHolder.equals("None"))
        {
            filteredFds = new ArrayList<FDEntity>();
            for (int i = 0; i < allFds.size(); i++)
            {
                if(allFds.get(i).HolderName.equals(this.filterHolder))
                {
                    filteredFds.add(allFds.get(i));
                }
            }
            allFds = new ArrayList<FDEntity>(filteredFds);
        }

        return allFds;
    }
}
