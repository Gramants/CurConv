package ste.demo.converter.ui;


import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ste.demo.converter.R;
import ste.demo.converter.viewmodel.model.CurrencyRate;

import android.os.Handler;


import java.util.*;

public class RatesAdapterJava extends RecyclerView.Adapter<ItemViewHolder> {
    private OnRateInteraction callback;
    private final static int ROOT_RATE = 0;
    private final static int OTHER_RATE = 1;

    private Handler handler = new Handler();
    private TextWatcher valueWatcher;
    private List<CurrencyRate> ratesList;
    private List<CurrencyRate> pendingList;


    private RecyclerView mRecyclerView;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public RatesAdapterJava(OnRateInteraction callback) {
        this.callback = callback;
        this.pendingList = new ArrayList<>();

        valueWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable newValue) {
                String strValue = newValue.toString().trim();
                Float value;

                try {
                    value = Float.parseFloat(strValue);
                } catch (Exception e) {
                    value = 0F;
                }

                ratesList.get(0).setValue(value);
                callback.onValueChanged(value);
            }

        };
    }

    interface OnRateInteraction {
        void onRateChanged(String currencyName, Float value);

        void onValueChanged(Float value);

        void scrollToTop();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rate, parent, false);

        ItemViewHolder rateHolder = new ItemViewHolder(view);
        rateHolder.getRateLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = rateHolder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION && position > 0) {
                    callback.onRateChanged(ratesList.get(position).getCurrency(), ratesList.get(position).getValue());
                }
            }
        });


        return rateHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.bindTo(ratesList.get(position), position, valueWatcher);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position, List<Object> payloads) {

        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof String) {
                if (((String) payloads.get(0)).contains(DiffUtilCallback.HAS_CHANCHED)) {
                    holder.updateValue(ratesList.get(position), position);
                } else {
                    super.onBindViewHolder(holder, position, payloads);
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }

    }

    public void updateItems(List<CurrencyRate> latest) {
        ratesList.clear();
        ratesList.addAll(latest);
        postAndNotifyAdapter(handler, mRecyclerView, this);

    }


    protected void postAndNotifyAdapter(final Handler handler, final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                } else {
                    postAndNotifyAdapter(handler, recyclerView, adapter);
                }
                callback.scrollToTop();
            }
        });
    }

    public void updateList(List<CurrencyRate> newRatesList) {
        if (ratesList == null) {
            ratesList = newRatesList;
            notifyItemRangeInserted(0, newRatesList.size());
            return;
        } else {
            updateItems(newRatesList);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return ROOT_RATE;
        else
            return OTHER_RATE;
    }


    @Override
    public int getItemCount() {
        if (ratesList == null)
            return 0;
        else
            return ratesList.size();
    }
}
