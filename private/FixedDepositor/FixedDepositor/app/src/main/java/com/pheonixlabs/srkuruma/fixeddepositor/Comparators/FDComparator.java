package com.pheonixlabs.srkuruma.fixeddepositor.Comparators;

import com.pheonixlabs.srkuruma.fixeddepositor.LocalDB.Tables.FDEntity;

import java.util.Comparator;

/**
 * Created by srkuruma on 7/18/2016.
 */
public class FDComparator implements Comparator<FDEntity> {
    @Override
    public int compare(FDEntity lhs, FDEntity rhs) {
        if(lhs.MaturityDate == null)
        {
            return 1;
        }

        if(rhs.MaturityDate == null)
        {
            return -1;
        }

        return lhs.MaturityDate.compareTo(rhs.MaturityDate);
    }
}
