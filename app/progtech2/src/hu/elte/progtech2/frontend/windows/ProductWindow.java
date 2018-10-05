/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.frontend.windows;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import hu.elte.progtech2.frontend.GuiManager;
import hu.elte.progtech2.frontend.components.factory.SwingComponentFactory;
import hu.elte.progtech2.frontend.validator.Validator;

/**
 * ProductWindow Kijelölt termék módosítására/törlésére szolgáló ablak
 *
 * @author <Andó Sándor Zsolt>
 */
public class ProductWindow extends JFrame {

    private final String ID;
    private final BigDecimal PRICE;
    private final int STOCK;
    private final DashboardWindow screen;

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
        JPanel panel = new JPanel(new FlowLayout());
        add(panel);

        priceTextField = SwingComponentFactory.generateTextField(panel, "ár:", PRICE.toString());
        stockTextField = SwingComponentFactory.generateTextField(panel, "menniység:", STOCK + "");

        modifyProductButton = SwingComponentFactory.generateButton(panel, "Termék megváltoztatása");
        
        /**
         * this::modifyProduct => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        modifyProductButton.addActionListener(this::modifyProduct);

        deleteProductButton = SwingComponentFactory.generateButton(panel, "Termék törlése");
        deleteProductButton.addActionListener(this::deleteProduct);
    }

    private void modifyProduct(ActionEvent event) {
        if (Validator.validateProduct(priceTextField.getText(), stockTextField.getText(), screen)) {
            GuiManager.modifyProduct(ID, new BigDecimal(priceTextField.getText()), Integer.parseInt(stockTextField.getText()));
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }

    private void deleteProduct(ActionEvent event) {
        GuiManager.deleteProduct(ID);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
