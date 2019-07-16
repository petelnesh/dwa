package com.dwakenya.techsavanna.newdwa.holders;

public class CheckSubscriptions {
    String mpesa_receipt_number, account_reference, amount, partya, result_code, confirm_date;

    public CheckSubscriptions() {
        this.mpesa_receipt_number = mpesa_receipt_number;
        this.account_reference = account_reference;
        this.amount = amount;
        this.partya = partya;
        this.result_code = result_code;
        this.confirm_date = confirm_date;
    }



    public String getMpesa_receipt_number() {
        return mpesa_receipt_number;
    }

    public void setMpesa_receipt_number(String mpesa_receipt_number) {
        this.mpesa_receipt_number = mpesa_receipt_number;
    }

    public String getAccount_reference() {
        return account_reference;
    }

    public void setAccount_reference(String account_reference) {
        this.account_reference = account_reference;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPartya() {
        return partya;
    }

    public void setPartya(String partya) {
        this.partya = partya;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getConfirm_date() {
        return confirm_date;
    }

    public void setConfirm_date(String confirm_date) {
        this.confirm_date = confirm_date;
    }
}
