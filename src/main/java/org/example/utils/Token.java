package org.example.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Token implements Serializable {

    private static final long serialVersionUID = 491691775166685841L;
    private String user_id;
    private String device_id;
    private String device_type;
    private Timestamp timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(user_id, token.user_id) && Objects.equals(device_id, token.device_id) && Objects.equals(device_type, token.device_type) && Objects.equals(timestamp, token.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, device_id, device_type, timestamp);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
