package com.dwakenya.techsavanna.newdwa.helpers;

public class CheckPay {
    String checkout;
    String phonenumber;

    public CheckPay(String checkout, String phonenumber) {
        this.checkout = checkout;
        this.phonenumber = phonenumber;
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
}
