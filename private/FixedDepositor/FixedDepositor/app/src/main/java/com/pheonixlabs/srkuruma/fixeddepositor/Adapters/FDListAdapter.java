package com.pheonixlabs.srkuruma.fixeddepositor.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pheonixlabs.srkuruma.fixeddepositor.Common.DateTimeUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.ResourceUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.Common.StringUtils;
import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;
import com.pheonixlabs.srkuruma.fixeddepositor.R;
import com.pheonixlabs.srkuruma.fixeddepositor.Tasks.DownloadImageTask;
import com.pheonixlabs.srkuruma.fixeddepositor.Tasks.ImageLoader;
import com.pheonixlabs.srkuruma.fixeddepositor.dummy.DummyContent;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

/**
 * Created by srkuruma on 7/10/2016.
 */
public class FDListAdapter extends ArrayAdapter<FDEntity> {
    private List<FDEntity> items;
    private ImageLoader imageLoader;

    public FDListAdapter(Context context, int resource) {
        super(context, resource);
    }

    public FDListAdapter(Context context, int resource, List<FDEntity> items) {
        super(context, resource, items);
        this.items = items;
        this.imageLoader = new ImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(position > items.size())
        {
            return convertView;
        }

        View v = convertView;

        if(v == null)
        {
            LayoutInflater vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.activity_list_item_fd, null);
        }

        FDEntity entity = items.get(position);

        if(entity != null)
        {
            TextView view = (TextView)v.findViewById(R.id.FDHolderName);
            view.setText(entity.HolderName);

            view = (TextView)v.findViewById(R.id.FDAmountText);
            view.setText(StringUtils.ConvertDoubleToStringWithoutScientificNotation(entity.Amount).concat("₹"));

            view = (TextView)v.findViewById(R.id.FDDuration);

            if(entity.MaturityDate != null) {
                view.setText(entity.GetNormalisedDuration());
            }
            else
            {
                view.setText("Unknown");
            }

            DownloadImageTask imageTask = new DownloadImageTask((ImageView) v.findViewById(R.id.BankImage));
            String imageUrl = ResourceUtils.GetImageUrlForBank(getContext(), entity.BankName);

            if(!StringUtils.IsStringNullOrEmpty(imageUrl)) {
                //imageTask.execute(imageUrl);
                imageLoader.DisplayImage(imageUrl, (ImageView) v.findViewById(R.id.BankImage));
            }

            // Days between the dates
            int days = DateTimeUtils.GetDaysBetweenDates(new Date(), entity.MaturityDate);

            ((View)v.findViewById(R.id.FDImportanceColor)).setVisibility(View.INVISIBLE);
            if(days <= 15)
            {
                ((View)v.findViewById(R.id.FDImportanceColor)).setVisibility(View.VISIBLE);
            }
        }

        return v;
    }
}
