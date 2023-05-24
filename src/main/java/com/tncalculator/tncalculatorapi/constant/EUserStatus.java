package com.tncalculator.tncalculatorapi.constant;

public enum EUserStatus {
        ACTIVE("active"),
        INACTIVE("inactive");

        private final String status;

        EUserStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

    }