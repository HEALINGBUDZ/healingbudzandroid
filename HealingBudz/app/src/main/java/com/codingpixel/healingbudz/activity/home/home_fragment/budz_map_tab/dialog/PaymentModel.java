package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog;

import java.io.Serializable;

public class PaymentModel implements Serializable {
    private String email;
    private String expDate;
    private String cardName;
    private String cvc;

    public PaymentModel(String email, String expDate, String cardName, String cvc) {
        this.email = email;
        this.expDate = expDate;
        this.cardName = cardName;
        this.cvc = cvc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}
