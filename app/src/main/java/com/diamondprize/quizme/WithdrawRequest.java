package com.diamondprize.quizme;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WithdrawRequest {
    private String userId;
    private String emailAddress;
    private String requestedBy;
    private String idpalyer;

    public WithdrawRequest(String s, String payPal) {

    }

    public WithdrawRequest(String userId, String emailAddress, String requestedBy, String jumlahkoin, String permainan, String idpalyer) {
        this.userId = userId;
        this.emailAddress = emailAddress;
        this.requestedBy = requestedBy;
        this.idpalyer = idpalyer;
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



    public String getIdpalyer() {
        return idpalyer;
    }

    public void setIdpalyer(String idpalyer) {
        this.idpalyer = idpalyer;
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
