package com.diamondprize.quizme;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class freefire_withdraws {
    private String userId;
    private String emailAddress;
    private String requestedBy;
    private String jumlahkoin;

    public freefire_withdraws(String s, String payPal, String uid, String name, String jumlahkoin) {

    }

    public freefire_withdraws(String userId, String emailAddress, String requestedBy, String jumlahkoin) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.requestedBy = requestedBy;
        this.jumlahkoin = jumlahkoin;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getJumlahkoin() {
        return jumlahkoin;
    }

    public void setJumlahkoin(String jumlahkoin) {
        this.jumlahkoin = jumlahkoin;
    }

    @ServerTimestamp
    private Date createdAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
