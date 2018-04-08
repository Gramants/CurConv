package ste.demo.converter.ui;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import ste.demo.converter.R;
import ste.demo.converter.viewmodel.model.CurrencyRate;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public View rateLayout;
    public TextView currencyName;
    public ImageView currencyImage;
    public TextView currencyDesc;
    public EditText currencyRateEdit;

    public ItemViewHolder(View itemView) {
        super(itemView);

        rateLayout = this.itemView.findViewById(R.id.rateLayout);
        currencyImage = this.itemView.findViewById(R.id.currencyImageView);
        currencyName = this.itemView.findViewById(R.id.currencyNameTextView);
        currencyDesc = this.itemView.findViewById(R.id.currencyDescTextView);
        currencyRateEdit = this.itemView.findViewById(R.id.rateEditText);

    }


    public void bindTo(CurrencyRate currencyRate, int position, TextWatcher valueWatcher) {


        currencyRateEdit.setEnabled(position == 0);
        currencyRateEdit.removeTextChangedListener(valueWatcher);

        if (position == 0)
            currencyRateEdit.addTextChangedListener(valueWatcher);

        updateValue(currencyRate, position);
        updateCurrency(currencyRate, position);
    }

    private void updateCurrency(CurrencyRate currencyRate, int position) {

        String updatedCurrency = currencyRate.getCurrency();
        currencyName.setText(updatedCurrency);
        currencyDesc.setText(getCurrencyDesc(updatedCurrency));

        Drawable flagDrawable = ResourcesCompat.getDrawable(currencyImage.getResources(), getCurrencyFlag(updatedCurrency), null);
        currencyImage.setImageDrawable(flagDrawable);

    }

    private int getCurrencyFlag(String updatedCurrency) {
        int currencyFlag = 0;
        Resources resources = currencyImage.getContext().getResources();
        String packageName = currencyImage.getContext().getPackageName();
        String drawableName = "ic_" + currencyName.getText().toString().substring(0, 2).toLowerCase();

        try {
            currencyFlag = resources.getIdentifier(drawableName, "drawable", packageName);
        } catch (Exception e) {

        }

        if (currencyFlag == 0)
            currencyFlag = R.drawable.ic_eu;

        return currencyFlag;
    }

    private String getCurrencyDesc(String updatedCurrency) {

        String currencyDescName;
        Resources resources = currencyImage.getContext().getResources();
        String packageName = currencyImage.getContext().getPackageName();

        try {
            currencyDescName = resources.getString(resources.getIdentifier(updatedCurrency, "string", packageName));
        } catch (Exception e) {
            currencyDescName = "N/A";
        }
        return currencyDescName;

    }


    public void updateValue(CurrencyRate currencyRate, int position) {

        String newValue = String.format("%.2f", currencyRate.getValue());

        if (currencyRateEdit.getText().toString() != newValue) {
            currencyRateEdit.setText(newValue);
        }
    }


    public View getRateLayout() {
        return rateLayout;
    }
}
