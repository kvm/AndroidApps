package com.pheonixlabs.srkuruma.fixeddepositor;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.Calculator;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.DateTimeUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.ResourceUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.StringUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.FDDbHelper;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;
import com.pheonixlabs.srkuruma.fixeddepositor.Tasks.DownloadImageTask;
import com.pheonixlabs.srkuruma.fixeddepositor.Tasks.ImageLoader;
import com.pheonixlabs.srkuruma.fixeddepositor.dummy.DummyContent;

import java.util.List;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    private FDDbHelper dbHelper;

    private SQLiteDatabase db;

    private ImageLoader imageLoader;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // read from db all the fds
        this.dbHelper = new FDDbHelper(getContext());
        this.db = this.dbHelper.getWritableDatabase();
        this.imageLoader = new ImageLoader(getContext());

        /*if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            //Activity activity = this.getActivity();
            /*CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }*/
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Get FD Details from view
        FDEntity entity = GetFDFromId(GetIdFromBundles(getArguments()), this.db);

        if(entity == null)
        {
            entity = new FDEntity();
            entity.SetUnknownValues();
        }

        String imageUrl = ResourceUtils.GetImageUrlForBank(getContext(), entity.BankName);

        if(!StringUtils.IsStringNullOrEmpty(imageUrl)) {
            this.imageLoader.DisplayImage(imageUrl, (ImageView) rootView.findViewById(R.id.BankIcon1));
        }

        imageUrl = ResourceUtils.GetImageUrlForBank(getContext(), entity.BankName);

        if(!StringUtils.IsStringNullOrEmpty(imageUrl)) {
            this.imageLoader.DisplayImage(imageUrl, (ImageView) rootView.findViewById(R.id.BankIcon2));
        }

        ((TextView) rootView.findViewById(R.id.BankName1)).setText(entity.BankName);
        if(entity.DepositDate != null) {
            ((TextView) rootView.findViewById(R.id.FDStartDate1)).setText(DateTimeUtils.ConvertDateTimeToStringWithFormat(entity.DepositDate, "dd MMM yyyy"));
        }

        if(entity.MaturityDate != null) {
            ((TextView) rootView.findViewById(R.id.FDEndDate1)).setText(DateTimeUtils.ConvertDateTimeToStringWithFormat(entity.MaturityDate, "dd MMM yyyy"));
        }

        ((TextView)rootView.findViewById(R.id.HolderNameText_2)).setText(entity.HolderName);
        ((TextView)rootView.findViewById(R.id.FDAmountText1)).setText(StringUtils.ConvertDoubleToStringWithoutScientificNotation(Calculator.round(entity.Amount, 2)));
        ((TextView)rootView.findViewById(R.id.FDAmountText2)).setText(StringUtils.ConvertDoubleToStringWithoutScientificNotation(Calculator.round(entity.AmountAfterMaturity, 2)));

        return rootView;
    }

    public static int GetIdFromBundles(Bundle bundle)
    {
        if(bundle != null && bundle.containsKey(ARG_ITEM_ID))
        {
            String itemId = bundle.getString(ARG_ITEM_ID);
            return Integer.parseInt(itemId) - 1;
        }

        return -1;
    }

    public static FDEntity GetFDFromId(int id, SQLiteDatabase db)
    {
        List<FDEntity> fds = ItemListFragment.GetAllFDs(db);

        if(id != -1)
        {
            return fds.get(id);
        }

        return null;
    }
}
