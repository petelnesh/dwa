package com.dwakenya.techsavanna.newdwa.helpers;

public class CheckPay {
    String checkout;
    String phonenumber;
    double amount;

    public CheckPay(String checkout, String phonenumber, Double amount) {
        this.checkout = checkout;
        this.phonenumber = phonenumber;
        this.amount = amount;
    }

    public CheckPay() {
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
