/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.dao;

import java.util.List;
import progtech2.backend.entities.OrderLine;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderDao implements IOrderDao{

    @Override
    public List<OrderLine> findOrderLinesByOrderId(long key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
