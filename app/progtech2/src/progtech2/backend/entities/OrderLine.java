/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.entities;

import java.math.BigDecimal;

/**
 *
 * @author Dell
 */
public class OrderLine {
    
    private long orderLineId;
    private BigDecimal price;
    private Product product;
    private int quantity;

    public long getOrderLineId() {
        return orderLineId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    } 
    
}
