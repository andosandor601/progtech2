/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.util.List;
import progtech2.backend.entities.Order;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class RetailerDao implements IRetailerDao{

    @Override
    public List<Order> findOrdersByRetailerId(String key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> findOrdersWithUnderDeliveryOrWaitingForDeliveryStatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
