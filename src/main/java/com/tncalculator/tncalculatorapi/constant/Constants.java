package com.tncalculator.tncalculatorapi.constant;

public class Constants {
    public enum UserStatus {
        ACTIVE("active"),
        INACTIVE("inactive");

        private final String status;

        UserStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }

}
