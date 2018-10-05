/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.entities;

import java.math.BigDecimal;

/**
 *
 * @author Dell
 */
public class OrderLine {

    private long orderLineId;
    private long orderId;
    private BigDecimal price;
    private String productName;
    private int quantity;

    public long getOrderLineId() {
        return orderLineId;
    }

    public void setOrderLineId(long orderLineId) {
        this.orderLineId = orderLineId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProduct() {
        return productName;
    }

    public void setProduct(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Object[] toArrayWithoutIds() {
        String[] array = {productName, quantity + "", price.toString()};
        return array;
    }

    public Object[] toArray() {
        String[] array = {orderLineId + "", orderId + "", productName, quantity + "", price.toString()};
        return array;
    }

}
