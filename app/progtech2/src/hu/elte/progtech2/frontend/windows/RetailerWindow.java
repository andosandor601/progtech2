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
 * RetailerWindow kijelölt kiskereskedő módosítására szolgáló ablak
 *
 * @author <Andó Sándor Zsolt>
 */
public class RetailerWindow extends JFrame {

    private final DashboardWindow screen;
    private final String ID;
    private final String ADDRESS;
    private final BigDecimal CREDITLINE;
    private final String PHONE;

    private JTextField addressTextField, creditLineTextField, phoneTextField;
    private JButton modifyRetailerButton;

    public RetailerWindow(DashboardWindow screen, String id, String address, BigDecimal creditLine, String phone) {
        this.screen = screen;
        this.ID = id;
        this.ADDRESS = address;
        this.CREDITLINE = creditLine;
        this.PHONE = phone;
        initWindow();
    }

    private void initWindow() {
        setTitle(ID + " kereskedő adatainak módosítása");
        setLayout(new FlowLayout());
        JPanel panel = new JPanel(new FlowLayout());
        add(panel);

        addressTextField = SwingComponentFactory.generateTextField(panel, "Cím:", ADDRESS);
        creditLineTextField = SwingComponentFactory.generateTextField(panel, "Hitelkeret:", CREDITLINE.toString());
        phoneTextField = SwingComponentFactory.generateTextField(panel, "Telefonszám:", PHONE);

        modifyRetailerButton = SwingComponentFactory.generateButton(panel, "Kereskedő megváltoztatása");
        
        /**
         * this::modifyProduct => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        modifyRetailerButton.addActionListener(this::modifyRetailer);
    }

    private void modifyRetailer(ActionEvent event) {
        if (Validator.validateRetailer(addressTextField.getText(), creditLineTextField.getText(), phoneTextField.getText(), screen)) {
            GuiManager.modifyRetailer(ID, addressTextField.getText(), new BigDecimal(creditLineTextField.getText()), phoneTextField.getText());
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
}
