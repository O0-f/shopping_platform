package org.example.utils;

public enum Status {

    SUCCESS {
        public String getStatus() {
            return "success";
        }
    },
    ERROR {
        public String getStatus() {
            return "error";
        }
    };

    public abstract String getStatus();
}
