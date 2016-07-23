package com.pheonixlabs.srkuruma.fixeddepositor.Common;

import android.content.Context;

import com.google.gson.Gson;
import com.pheonixlabs.srkuruma.fixeddepositor.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srkuruma on 7/20/2016.
 */
public class ResourceUtils {
    public static String GetImageUrlForBank(Context context, String bankName)
    {
        String imagesDictionaryString = context.getString(R.string.bankImages);
        Gson gson = new Gson();
        Map<String,String> map = new HashMap<String,String>();
        map = (Map<String,String>) gson.fromJson(imagesDictionaryString, map.getClass());
        if(map.containsKey(bankName))
        {
            return map.get(bankName);
        }

        return null;
    }
}
