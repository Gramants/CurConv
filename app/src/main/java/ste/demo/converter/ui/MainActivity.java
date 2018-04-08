package ste.demo.converter.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ste.demo.converter.R;
import ste.demo.converter.viewmodel.RatesViewModel;
import ste.demo.converter.viewmodel.model.CurrencyRate;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements RatesAdapterJava.OnRateInteraction {

    private RatesAdapterJava ratesAdapter;
    private RatesViewModel ratesViewModel;
    private RecyclerView ratesRecyclerView;
    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ratesRecyclerView = findViewById(R.id.ratesRecyclerView);
        ratesViewModel = ViewModelProviders.of(this).get(RatesViewModel.class);
        ratesAdapter = new RatesAdapterJava(this);
        ratesRecyclerView.setAdapter(ratesAdapter);
        ratesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        observeModel();
    }

    private void observeModel() {
        ratesViewModel.getCurrencyRates().observe(this,
                currencyRates -> {
                    if (currencyRates != null) {
                        ratesAdapter.updateList((List<CurrencyRate>) currencyRates);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                ratesViewModel.refreshRates();
            }
        }, 0, 60000);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        t.cancel();
        t.purge();

    }

    @Override
    public void onRateChanged(String currencyName, Float value) {
        ratesViewModel.setNewBase(currencyName, value);
    }

    @Override
    public void onValueChanged(Float value) {
        ratesViewModel.setNewBaseValue(value);
    }

    @Override
    public void scrollToTop() {
        ratesRecyclerView.scrollToPosition(0);
    }


}