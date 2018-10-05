/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.dao;

import java.util.List;
import hu.elte.progtech2.backend.entities.Order;
import hu.elte.progtech2.backend.entities.OrderLine;

/**
 * Rendeléshez tartozó Dao Interfész
 *
 * @author Andó Sándor Zsolt
 */
public interface OrderDao extends CRUDDao<Order, Long> {

    /**
     * A megadott kulcsú rendeléshez tartozó rendeléssor megkeresése
     *
     * @param key
     * @return
     */
    List<OrderLine> findOrderLinesByOrderId(long key);
    
    
    /**
     * Az összes olyan rendelés kilistázása, amit még nem kezdtek el
     * kiszállítani
     *
     * @return
     */
    List<Order> findOrdersWithUnderDeliveryOrWaitingForDeliveryStatus();

}
