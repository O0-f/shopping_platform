package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = -3258839839160856613L;
    private String id;
    private String user_id;
    private String name;
    private String lng;
    private String lat;

    public void setId(String id) {
        this.id = id;
    }

    @Id
    @Column(unique = true)
    public String getId() {
        return id;
    }

    @Column(name = "user_id")
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "lng")
    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Column(name = "lat")
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
