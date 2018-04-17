/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.validator;

import java.math.BigDecimal;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Validator osztály, az input adatok helyességének vizsgálata
 *
 * @author <Andó Sándor Zsolt>
 */
public final class Validator {

    private final static String INTEGER_REGEXP = "\\d+";
    private final static String BIGDECIMAL_REGEXP = "-?(?:\\d+(?:\\.\\d+)?|\\.\\d+)";

    private Validator() {
    }

    public static boolean validateStringAndInteger(String string, String integer, JFrame frame) {
        if (string.isEmpty() || integer.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Hiányzó mező");
            return false;
        }
        if (!integer.matches(INTEGER_REGEXP)) {
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
        if (!creditLine.matches(BIGDECIMAL_REGEXP)) {
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
        if (!creditLine.matches(BIGDECIMAL_REGEXP)) {
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
        if (!price.matches(BIGDECIMAL_REGEXP)) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott ár");
            return false;
        }
        if (!stock.matches(INTEGER_REGEXP)) {
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
        if (!price.matches(BIGDECIMAL_REGEXP)) {
            JOptionPane.showMessageDialog(frame, "Hibásan megadott ár");
            return false;
        }
        if (!stock.matches(INTEGER_REGEXP)) {
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
