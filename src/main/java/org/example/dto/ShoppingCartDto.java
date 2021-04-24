package org.example.dto;

import java.io.Serializable;

public class ShoppingCartDto implements Serializable {

    private static final long serialVersionUID = -3258839839160856613L;
    private String shopping_cart_name;
    private String user_id;
    private String commodity_name;
    private long quantity;
    private double price;

    public String getShopping_cart_name() {
        return shopping_cart_name;
    }

    public void setShopping_cart_name(String shopping_cart_name) {
        this.shopping_cart_name = shopping_cart_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCommodity_name() {
        return commodity_name;
    }

    public void setCommodity_name(String commodity_name) {
        this.commodity_name = commodity_name;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
