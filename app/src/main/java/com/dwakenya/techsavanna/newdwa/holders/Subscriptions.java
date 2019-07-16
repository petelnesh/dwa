package com.dwakenya.techsavanna.newdwa.holders;

public class Subscriptions {
    String subscriptionid;
    String subscriptionType;
    String subscriptionAmount_KSH;
    String subscriptionDuration_Days;

    public Subscriptions() {
        this.subscriptionid = subscriptionid;
        this.subscriptionType = subscriptionType;
        this.subscriptionAmount_KSH = subscriptionAmount_KSH;
        this.subscriptionDuration_Days = subscriptionDuration_Days;
    }


    public String getSubscriptionid() {
        return subscriptionid;
    }

    public void setSubscriptionid(String subscriptionid) {
        this.subscriptionid = subscriptionid;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getSubscriptionAmount_KSH() {
        return subscriptionAmount_KSH;
    }

    public void setSubscriptionAmount_KSH(String subscriptionAmount_KSH) {
        this.subscriptionAmount_KSH = subscriptionAmount_KSH;
    }

    public String getSubscriptionDuration_Days() {
        return subscriptionDuration_Days;
    }

    public void setSubscriptionDuration_Days(String subscriptionDuration_Days) {
        this.subscriptionDuration_Days = subscriptionDuration_Days;
    }
}
