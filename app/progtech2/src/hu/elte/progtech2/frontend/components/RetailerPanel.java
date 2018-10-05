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

/**
 * Kereskedők kezelését lehetővé tevő panel (új kereskedő felvétele, ...)
 *
 * @author <Andó Sándor Zsolt>
 */
public class RetailerPanel extends JPanel {

    private JButton newRetailerButton;
    private JTextField addressTextField, creditLineTextField, phoneNumberTextField, retailerNameTextField;

    public RetailerPanel() {
        initProductPanel();
    }

    private void initProductPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        retailerNameTextField = SwingComponentFactory.generateTextField(this, "Kiskereskedő neve:");
        addressTextField = SwingComponentFactory.generateTextField(this, "Cím:");
        phoneNumberTextField = SwingComponentFactory.generateTextField(this, "Telefonszám:");
        creditLineTextField = SwingComponentFactory.generateTextField(this, "Hitelkeret:");

        newRetailerButton = SwingComponentFactory.generateButton(this, "Új kereskedő hozzáadása");
        
        /**
         * this::addNewRetailer => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        newRetailerButton.addActionListener(this::addNewRetailer);
    }

    private void addNewRetailer(ActionEvent event) {
        GuiManager.addNewRetailer(retailerNameTextField.getText(), addressTextField.getText(), creditLineTextField.getText(), phoneNumberTextField.getText());
    }
}
