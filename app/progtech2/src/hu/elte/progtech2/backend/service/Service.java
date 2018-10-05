/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.service;

import java.math.BigDecimal;
import java.util.List;
import hu.elte.progtech2.backend.entities.OrderLine;
import hu.elte.progtech2.backend.entities.Order;
import hu.elte.progtech2.backend.entities.Product;
import hu.elte.progtech2.backend.entities.Retailer;
import hu.elte.progtech2.backend.enums.OrderStatus;
import hu.elte.progtech2.backend.service.exceptions.ServiceException;

/**
 * Az alkalmazás szerviz interfésze
 *
 * @author <Andó Sándor Zsolt>
 */
public interface Service {

    /**
     * Új rendelés elmentése
     *
     * @param retailerName
     * @param orderLines
     * @throws ServiceException
     */
    void addOrder(String retailerName, List<OrderLine> orderLines) throws ServiceException;

    /**
     * Új termék elmentése
     *
     * @param productName
     * @param price
     * @param stock
     */
    void addProduct(String productName, BigDecimal price, int stock);

    /**
     * Új kereskedő felvétele
     *
     * @param retailerName
     * @param address
     * @param creditLine
     * @param phone
     */
    void addRetailer(String retailerName, String address, BigDecimal creditLine, String phone);

    /**
     * A megadott azonosítójú rendelés törlése a megadott kiskereskedőtől
     *
     * @param retailerName
     * @param orderId
     */
    void deleteOrder(long orderId);

    /**
     * A megadott azonosítójú rendeléssor törlése, a megadott azonosítójú
     * rendelésből
     *
     * @param orderId
     * @param orderLineId
     */
    void deleteOrderLine(long orderLineId);

    /**
     * A megadott azonosítójú termék törlése
     *
     * @param productName
     * @throws ServiceException
     */
    void deleteProduct(String productName) throws ServiceException;

    /**
     * Azon termékek listázáse, amik még nincsenek kiszállítás alatt, vagy nem
     * kerültek kiszállításra
     *
     * @return
     */
    List<Order> listNotDeliveredOrders();

    /**
     * Az adott azonosítójú rendeléshez tartozó rendeléssorok listázása
     *
     * @param orderId
     * @return
     */
    List<OrderLine> listOrderLines(long orderId);

    /**
     * Rendelések listázása
     *
     * @return
     */
    List<Order> listOrders();

    /**
     * Rendelések listázása megadott kereskedő neve alapján
     *
     * @param retailerName
     * @return
     */
    List<Order> listOrdersByRetailer(String retailerName);

    /**
     * Termékek listázása
     *
     * @return
     */
    List<Product> listProducts();

    /**
     * Kiskereskedők listázása
     *
     * @return
     */
    List<Retailer> listRetailers();
    
    /**
     * Termék keresése id alapján
     * @param productName
     * @return 
     */
    Product findProduct(String productName);

    /**
     * A megadott rendelés státuszának módosítása
     *
     * @param orderId
     * @param status
     */
    void modifyOrderStatus(long orderId, OrderStatus status);

    /**
     * A megadott termék árának módosítása
     *
     * @param productName
     * @param newPrice
     */
    void modifyProduct(String productName, BigDecimal newPrice, int newQuantity);

    /**
     * A megadott kiskereskedő módosítása a paraméterben megadott adatokkal
     *
     * @param name
     * @param address
     * @param creditLine
     * @param phone
     */
    void modifyRetailer(String name, String address, BigDecimal creditLine, String phone);

}
