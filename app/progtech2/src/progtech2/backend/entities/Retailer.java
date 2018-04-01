/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.backend.entities;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Dell
 */
public class Retailer {
    
    private String address;
    private BigDecimal creditLine;
    private String name;
    private String phone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getCreditLine() {
        return creditLine;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Object[] toArray() {
        String[] array = {name, address, creditLine.toString(), phone};
        return array;
    }
    
}
