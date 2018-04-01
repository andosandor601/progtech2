/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import progtech2.backend.enums.OrderStatus;

/**
 *
 * @author Dell
 */
public class Order {
    
    private Date orderDate;
    private long orderId;
    private BigDecimal orderPrice;
    private OrderStatus status;
    private String retailerName;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }
    
    public Object[] toArray() {
        String[] array = {orderId+"", retailerName, orderDate.toString(), orderPrice.toString(), status.name()};
        return array;
    }

    @Override
    public String toString() {
        return "Order{" + "orderDate=" + orderDate + ", orderId=" + orderId + ", orderPrice=" + orderPrice + ", status=" + status + ", retailerName=" + retailerName + '}';
    }
    
}
