/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.windows;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import progtech2.frontend.GuiManager;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class ProductWindow extends JFrame {

    private final String ID;
    private final BigDecimal PRICE;
    private final int STOCK;
    private final DashboardWindow screen;
    private final String BIGDECIMAL_REGEXP = "-?(?:\\d+(?:\\.\\d+)?|\\.\\d+)";
    private final String INTEGER_REGEXP = "\\d+";

    private JTextField priceTextField, stockTextField;
    private JButton modifyProductButton, deleteProductButton;

    public ProductWindow(DashboardWindow screen, String id, BigDecimal price, int stock) {
        this.ID = id;
        this.PRICE = price;
        this.STOCK = stock;
        this.screen = screen;
        initWindow();
    }

    private void initWindow() {
        setTitle(ID + " termék módosítása");
        setLayout(new FlowLayout());
        priceTextField = generateTextField("ár:", PRICE.toString());
        stockTextField = generateTextField("menniység:", STOCK + "");
        modifyProductButton = generateButton("Termék megváltoztatása");
        modifyProductButton.addActionListener(this::modifyProduct);
        deleteProductButton = generateButton("Termék törlése");
        deleteProductButton.addActionListener(this::deleteProduct);
    }

    private JTextField generateTextField(String labelText, String text) {
        JLabel label = new JLabel(labelText);
        JTextField textField = new JTextField();
        textField.setText(text);
        add(label);
        add(textField);
        return textField;
    }

    private JButton generateButton(String text) {
        JButton button = new JButton(text);
        add(button);
        return button;
    }

    private void modifyProduct(ActionEvent event) {
        if (validateProduct(priceTextField.getText(), stockTextField.getText())) {
            GuiManager.modifyProduct(ID, new BigDecimal(priceTextField.getText()), Integer.parseInt(stockTextField.getText()));
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }

    private void deleteProduct(ActionEvent event) {
        GuiManager.deleteProduct(ID);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private boolean validateProduct(String price, String stock) {
        if (price.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(screen, "hiányzó paraméter");
            return false;
        }
        Pattern pBigDec = Pattern.compile(BIGDECIMAL_REGEXP);
        Matcher mBigDec = pBigDec.matcher(price);
        if (!mBigDec.matches()) {
            JOptionPane.showMessageDialog(screen, "Hibásan megadott ár");
            return false;
        }
        Pattern pInt = Pattern.compile(INTEGER_REGEXP);
        Matcher mInt = pInt.matcher(stock);
        if (!mInt.find()) {
            JOptionPane.showMessageDialog(screen, "Hibásan megadott mennyiség");
            return false;
        }
        return true;
    }

}
