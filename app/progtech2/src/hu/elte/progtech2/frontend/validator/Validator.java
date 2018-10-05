/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.frontend.validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Validator osztály, az input adatok helyességének vizsgálata
 *
 * @author <Andó Sándor Zsolt>
 */
public final class Validator {

    //Gyakran használt string-regexp-ekre egy Pattern definiálása gyorsabb lesz, mint a string.matches metódus használata
    //lsd. Effective JAVA Item 6.
    private final static Pattern INTEGER_PATTERN = Pattern.compile("\\d+");
    private final static Pattern BIGDECIMAL_PATTERN = Pattern.compile("-?(?:\\d+(?:\\.\\d+)?|\\.\\d+)");

    private Validator() {
    }

    public static boolean validateStringAndInteger(String string, String integer, JFrame frame) {
        if (string.isEmpty() || integer.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Hiányzó mező");
            return false;
        }
        if (!INTEGER_PATTERN.matcher(integer).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibás számbevitel");
            return false;
        } else if (Integer.parseInt(integer) < 0) {
            JOptionPane.showMessageDialog(frame, "Negatív mennyiség");
            return false;
        }
        return true;
    }

    public static boolean validateRetailer(String address, String creditLine, String phone, JFrame frame) {
        if (address.isEmpty() || creditLine.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "hiányzó paraméter");
            return false;
        }
        if (!BIGDECIMAL_PATTERN.matcher(creditLine).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott hitelkeret");
            return false;
        } else if (new BigDecimal(creditLine).compareTo(BigDecimal.ZERO) < 1) {
            JOptionPane.showMessageDialog(frame, "Negatív mennyiség");
            return false;
        }
        return true;
    }

    public static boolean validateRetailer(String name, String address, String creditLine, String phone, JFrame frame) {
        if (name.isEmpty() || address.isEmpty() || creditLine.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "hiányzó paraméter");
            return false;
        }
        if (!BIGDECIMAL_PATTERN.matcher(creditLine).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott hitelkeret");
            return false;
        } else if (new BigDecimal(creditLine).compareTo(BigDecimal.ZERO) < 1) {
            JOptionPane.showMessageDialog(frame, "Negatív mennyiség");
            return false;
        }
        return true;
    }

    public static boolean validateLong(String longValue, JFrame frame) {
        if (!longValue.matches(longValue)) {
            JOptionPane.showMessageDialog(frame, "hibás szám érték");
            return false;
        } else if (Integer.parseInt(longValue) < 0) {
            JOptionPane.showMessageDialog(frame, "Negatív mennyiség mennyiség");
            return false;
        }
        return true;
    }

    public static boolean validateProduct(String name, String price, String stock, JFrame frame) {
        if (name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "hiányzó paraméter");
            return false;
        }
        if (!BIGDECIMAL_PATTERN.matcher(price).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott ár");
            return false;
        }
        if (!INTEGER_PATTERN.matcher(stock).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott mennyiség");
            return false;
        } else if (Integer.parseInt(stock) < 0 || new BigDecimal(price).compareTo(BigDecimal.ZERO) < 1) {
            JOptionPane.showMessageDialog(frame, "Negatív mennyiség");
            return false;
        }
        return true;
    }

    public static boolean validateProduct(String price, String stock, JFrame frame) {
        if (price.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "hiányzó paraméter");
            return false;
        }
        if (!BIGDECIMAL_PATTERN.matcher(price).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott ár");
            return false;
        }
        if (!INTEGER_PATTERN.matcher(stock).matches()) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott mennyiség");
            return false;
        } else if (Integer.parseInt(stock) < 0 || new BigDecimal(price).compareTo(BigDecimal.ZERO) < 1) {
            JOptionPane.showMessageDialog(frame, "Negatív mennyiség");
            return false;
        }
        return true;
    }

    public static <E> boolean validateOrder(String text, List<E> list, JFrame frame) {
        if (text.isEmpty() || list.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "hiányzó paraméter");
            return false;
        }
        return true;
    }

}
