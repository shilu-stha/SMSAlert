package com.shilu.leapfrog.smsalert.components;

/**
 * Constant file for all the constant values.
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/11/15
 */
public class MessagesDetails {

    public String DATE_TIME;
    public String MESSAGE_BODY;
    public String MESSAGE_TYPE;
    public String MESSAGES_TABLE_ID;

    public String getDATE_TIME() {
        return DATE_TIME;
    }

    public void setDATE_TIME(String DATE_TIME) {
        this.DATE_TIME = DATE_TIME;
    }

    public String getMESSAGE_BODY() {
        return MESSAGE_BODY;
    }

    public void setMESSAGE_BODY(String MESSAGE_BODY) {
        this.MESSAGE_BODY = MESSAGE_BODY;
    }

    public String getMESSAGE_TYPE() {
        return MESSAGE_TYPE;
    }

    public void setMESSAGE_TYPE(String MESSAGE_TYPE) {
        this.MESSAGE_TYPE = MESSAGE_TYPE;
    }

    public String getMESSAGES_TABLE_ID() {
        return MESSAGES_TABLE_ID;
    }

    public void setMESSAGES_TABLE_ID(String MESSAGES_TABLE_ID) {
        this.MESSAGES_TABLE_ID = MESSAGES_TABLE_ID;
    }
}
