/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.frontend.components;

import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import hu.elte.progtech2.frontend.GuiManager;
import hu.elte.progtech2.frontend.components.factory.SwingComponentFactory;
import hu.elte.progtech2.frontend.windows.DashboardWindow;

/**
 * Termékekhez tartozó Panel (új termék felvétele, ... )
 *
 * @author <Andó Sándor Zsolt>
 */
public class ProductPanel extends JPanel {

    private JButton newProductButton;
    private JTextField priceTextField, productNameTextField, quantityTextField;

    public ProductPanel(DashboardWindow frame) {
        initProductPanel();
    }

    private void initProductPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        productNameTextField = SwingComponentFactory.generateTextField(this, "Terméknév:");
        priceTextField = SwingComponentFactory.generateTextField(this, "Termék ára:");
        quantityTextField = SwingComponentFactory.generateTextField(this, "Mennyiség:");
        newProductButton = SwingComponentFactory.generateButton(this, "Új termék hozzáadása");

        /**
         * this::addNewProduct => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        newProductButton.addActionListener(this::addNewProduct);
    }

    private void addNewProduct(ActionEvent event) {
        GuiManager.addNewProduct(productNameTextField.getText(), priceTextField.getText(), quantityTextField.getText());
    }

}
