package ste.demo.converter.ui;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import ste.demo.converter.viewmodel.model.CurrencyRate;

import java.util.ArrayList;

public class DiffUtilCallback extends DiffUtil.Callback {
    ArrayList<CurrencyRate> newList;
    ArrayList<CurrencyRate> oldList;
    public static final String HAS_CHANCHED = "HAS_CHANGED";

    public DiffUtilCallback(ArrayList<CurrencyRate> newList, ArrayList<CurrencyRate> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getCurrency() == newList.get(newItemPosition).getCurrency();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        ArrayList<String> payloadSet = new ArrayList();

        if (oldList.get(oldItemPosition).getValue() != newList.get(newItemPosition).getValue())
            payloadSet.add(HAS_CHANCHED);

        return payloadSet;
    }
}
