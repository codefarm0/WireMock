package com.greenlearner.paymoney.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author - GreenLearner(https://www.youtube.com/c/greenlearner)
 */
public class FraudCheck {
    private final boolean blacklisted;
    //more fraud flags to go here..

    public FraudCheck(final boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    @Override
    public String toString() {
        return "FraudCheck{" +
                "blacklisted=" + blacklisted +
                '}';
    }
}
