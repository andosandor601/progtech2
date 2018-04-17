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
import progtech2.backend.service.exceptions.ServiceException;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class Service implements IService {

    private DaoManager dm = new DaoManager();

    @Override
    public void addOrder(String retailerName, List<OrderLine> orderLines) throws ServiceException {
        Order order = new Order();
        Retailer retailer = dm.findRetailer(retailerName);
        order.setOrderDate(Calendar.getInstance().getTime());
        order.setStatus(OrderStatus.WAITING_FOR_DELIVERY);
        order.setOrderPrice(sumOrderLines(orderLines));
        order.setRetailerName(retailer.getName());

        if (order.getOrderPrice().compareTo(retailer.getCreditLine()) < 1 && areValidOrderLines(orderLines)) {
            order = dm.saveOrder(order, orderLines);
            for (OrderLine orderLine : orderLines) {
                Product product = dm.findProduct(orderLine.getProduct());
                modifyProductQuantity(product.getProductName(), product.getStock() - orderLine.getQuantity());
            }
        } else {
            throw new ServiceException("Túl magas a rendelés értéke, vagy az egyik termékből nincs elég a raktáron");
        }

    }

    private boolean areValidOrderLines(List<OrderLine> orderLines) {
        for (OrderLine orderLine : orderLines) {
            Product product = dm.findProduct(orderLine.getProduct());
            if (product.getStock() < orderLine.getQuantity()) {
                return false;
            }
        }
        return true;
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
        Product product = dm.findProduct(productName);

        orderLine.setProduct(productName);
        orderLine.setQuantity(quantity);
        orderLine.setPrice(product.getPrice().multiply(new BigDecimal(quantity)));

        modifyProductQuantity(product.getProductName(), product.getStock() - quantity);

        orderLine = dm.saveOrderLine(orderLine);

        return orderLine;
    }

    @Override
    public void addProduct(String productName, BigDecimal price, int stock) {
        Product product = new Product();

        product.setProductName(productName);
        product.setPrice(price);
        product.setStock(stock);

        dm.saveProduct(product);
    }

    @Override
    public void addRetailer(String retailerName, String address, BigDecimal creditLine, String phone) {
        Retailer retailer = new Retailer();

        retailer.setName(retailerName);
        retailer.setAddress(address);
        retailer.setCreditLine(creditLine);
        retailer.setPhone(phone);

        dm.saveRetailer(retailer);
    }

    @Override
    public void deleteOrder(long orderId) {
        List<OrderLine> orderLines = dm.findOrderLinesByOrderId(orderId);
        orderLines.forEach(orderLine -> {
            deleteOrderLine(orderLine.getOrderLineId());
        });
        dm.deleteOrder(orderId);
    }

    @Override
    public void deleteOrderLine(long orderLineId) {
        OrderLine orderLine = dm.findOrderLine(orderLineId);
        long orderId = orderLine.getOrderId();
        Order order = dm.findOrder(orderLine.getOrderId());
        if (!order.getStatus().equals(OrderStatus.READY)) {
            returnOrderedProductToStock(orderLine);
        }      
        dm.deleteOrderLine(orderLineId);

        List<OrderLine> orderLines = listOrderLines(orderId);
        modifyOrderPrice(orderLine.getOrderId(), sumOrderLines(orderLines));
    }

    private void returnOrderedProductToStock(OrderLine orderLine) {
        Product product = dm.findProduct(orderLine.getProduct());
        modifyProductQuantity(product.getProductName(), product.getStock() + orderLine.getQuantity());
    }

    @Override
    public void deleteProduct(String productName) throws ServiceException {
        dm.deleteProduct(productName);
    }

    @Override
    public List<Order> listNotDeliveredOrders() {
        return dm.listNotDeliveredOrders();
    }

    @Override
    public List<OrderLine> listOrderLines(long orderId) {
        return dm.listOrderLines(orderId);
    }

    @Override
    public List<Order> listOrders() {
        return dm.listOrders();
    }

    @Override
    public List<Order> listOrdersByRetailer(String retailerName) {
        return dm.listOrdersByRetailer(retailerName);
    }

    @Override
    public List<Product> listProducts() {
        return dm.listProducts();
    }

    @Override
    public List<Retailer> listRetailers() {
        return dm.listRetailers();
    }

    @Override
    public Product findProduct(String productName) {
        return dm.findProduct(productName);
    }

    @Override
    public void modifyOrderStatus(long orderId, OrderStatus status) {
        Order order = dm.findOrder(orderId);
        order.setStatus(status);
        dm.updateOrder(order);
    }

    @Override
    public void modifyProduct(String productName, BigDecimal newPrice, int newQuantity) {
        Product product = dm.findProduct(productName);
        product.setPrice(newPrice);
        product.setStock(newQuantity);
        dm.updateProduct(product);
    }

    public void modifyProductQuantity(String productName, int newQuantity) {
        Product product = dm.findProduct(productName);
        product.setStock(newQuantity);
        dm.updateProduct(product);
    }

    @Override
    public void modifyRetailer(String name, String address, BigDecimal creditLine, String phone) {
        Retailer retailer = dm.findRetailer(name);
        retailer.setAddress(address);
        retailer.setCreditLine(creditLine);
        retailer.setPhone(phone);
        dm.updateRetailer(retailer);
    }

    private void modifyOrderPrice(long orderId, BigDecimal sumOrderLines) {
        Order order = dm.findOrder(orderId);
        order.setOrderPrice(sumOrderLines);
        dm.updateOrder(order);
    }

}
