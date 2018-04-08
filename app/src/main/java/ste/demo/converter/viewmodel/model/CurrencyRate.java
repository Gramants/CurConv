package ste.demo.converter.viewmodel.model;


public class CurrencyRate {

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public CurrencyRate(String currency, Float rate, Float value) {

        this.currency = currency;
        this.rate = rate;
        this.value = value;
    }

    private String currency;
    private Float rate;
    private Float value;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyRate that = (CurrencyRate) o;

        if (!getCurrency().equals(that.getCurrency())) return false;
        if (!getRate().equals(that.getRate())) return false;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        int result = getCurrency().hashCode();
        result = 31 * result + getRate().hashCode();
        result = 31 * result + getValue().hashCode();
        return result;
    }

    public String getCurrency() {

        return currency;
    }


}
