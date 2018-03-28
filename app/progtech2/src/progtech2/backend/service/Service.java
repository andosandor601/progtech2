/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import progtech2.backend.dao.DaoManager;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Product;
import progtech2.backend.entities.Retailer;
import progtech2.backend.enums.OrderStatus;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class Service implements IService {

    @Override
    public void addOrder(String retailerName, List<OrderLine> orderLines) {
        Order order = new Order();
        Retailer retailer = DaoManager.findRetailer(retailerName);
        //TODO: ellenőrízni, hogy a leadott rendelés nem-e lépi túl, a megadott hitelkeretet
        order.setOrderlines(orderLines);
        order.setOrderDate(Calendar.getInstance().getTime());
        order.setStatus(OrderStatus.WAITING_FOR_DELIVERY);
        order.setOrderPrice(sumOrderLines(orderLines));

        order = DaoManager.saveOrder(order);
        DaoManager.addNewOrderToRetailer(retailer, order);

    }

    private BigDecimal sumOrderLines(List<OrderLine> orderLines) {
        BigDecimal sum = new BigDecimal(0);
        for (OrderLine orderLine : orderLines) {
            sum = sum.add(orderLine.getPrice());
        }
        return sum;
    }

    @Override
    public OrderLine addOrderLine(String productName, int quantity) {
        OrderLine orderLine = new OrderLine();
        Product product = DaoManager.findProduct(productName);

        orderLine.setProduct(product);
        orderLine.setQuantity(quantity);
        orderLine.setPrice(product.getPrice().multiply(new BigDecimal(quantity)));

        modifyProductQuantity(product.getProductName(), product.getStock() - quantity);

        orderLine = DaoManager.saveOrderLine(orderLine);

        return orderLine;
    }

    @Override
    public void addProduct(String productName, BigDecimal price, int stock) {
        Product product = new Product();

        product.setProductName(productName);
        product.setPrice(price);
        product.setStock(stock);

        DaoManager.saveProduct(product);
    }

    @Override
    public void deleteOrder(String retailerName, long orderId) {
        removeOrderFromRetailer(retailerName, orderId);
        returnAllOrderedProductsToStock(orderId);

        DaoManager.deleteOrder(orderId);
    }

    private void removeOrderFromRetailer(String retailerName, long orderId) {
        Retailer retailer = DaoManager.findRetailer(retailer);
        Order order = DaoManager.findOrder(orderId);
        retailer.getOrders().remove(order);
        DaoManager.updateOrder(order);
    }

    private void returnAllOrderedProductsToStock(long orderId) {
        Order order = DaoManager.findOrder(orderId);
        order.getOrderlines().forEach(orderLine -> {
            modifyProductQuantity(orderLine.getProduct().getProductName(), orderLine.getProduct().getStock() + orderLine.getQuantity());
        });
        //majd cascade-olni az adatbázisban az orderline-okat (delete cascade property beállítása)
    }

    @Override
    public void deleteOrderLine(long orderId, long orderLineId) {
        removeOrderLineFromOrder(orderId, orderLineId);
        modifyProductQuantity(productName, 0);
        DaoManager.deleteOrderLine(orderLineId);
    }

    private void removeOrderLineFromOrder(long orderId, long orderLineId) {
        Order order = DaoManager.findOrder(orderId);
        OrderLine orderLine = DaoManager.findOrderLine(orderLineId);
        modifyProductQuantity(orderLine.getProduct().getProductName(), orderLine.getProduct().getStock() + orderLine.getQuantity());
        order.getOrderlines().remove(orderLine);
        DaoManager.updateOrder(order);
    }

    @Override
    public void deleteProduct(String productName) {
        //Ellenőrízni, hogy van-e a termékre rendelés, különben nem törlünk
        DaoManager.deleteProduct(productName);
    }

    @Override
    public List<Order> listNotDeliveredOrders() {
        return DaoManager.listNotDeliveredOrders();
    }

    @Override
    public List<OrderLine> listOrderLines(long orderId) {
        return DaoManager.listOrderLines(orderId);
    }

    @Override
    public List<Order> listOrders() {
        return DaoManager.listOrders();
    }

    @Override
    public List<Order> listOrdersByRetailer(String retailerName) {
        return DaoManager.listOrdersByRetailer(retailerName);
    }

    @Override
    public List<Product> listProducts() {
        return DaoManager.listProducts();
    }

    @Override
    public List<Retailer> listRetailers() {
        return DaoManager.listRetailers();
    }

    @Override
    public void modifyOrderStatus(long orderId, OrderStatus status) {
        Order order = DaoManager.findOrder(orderId);
        order.setStatus(status);
        DaoManager.updateOrder(order);
    }

    @Override
    public void modifyProductPrice(String productName, BigDecimal newPrice) {
        Product product = DaoManager.findProduct(productName);
        product.setPrice(newPrice);
        DaoManager.updateProduct(product);
    }

    @Override
    public void modifyProductQuantity(String productName, int newQuantity) {
        Product product = DaoManager.findProduct(productName);
        product.setStock(newQuantity);
        DaoManager.upddateProduct(product);
    }

}
