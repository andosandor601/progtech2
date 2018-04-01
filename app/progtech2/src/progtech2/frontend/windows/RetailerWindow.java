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
import javax.swing.JTextField;
import progtech2.frontend.GuiManager;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class RetailerWindow extends JFrame {

    private final DashboardWindow screen;
    private final String ID;
    private final String ADDRESS;
    private final BigDecimal CREDITLINE;
    private final String PHONE;
    private final String BIGDECIMAL_REGEXP = "-?(?:\\d+(?:\\.\\d+)?|\\.\\d+)";
    private final String INTEGER_REGEXP = "\\d+";
    
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
        addressTextField = generateTextField("Cím:", ADDRESS);
        creditLineTextField = generateTextField("Hitelkeret:", CREDITLINE.toString());
        phoneTextField = generateTextField("Telefonszám:", PHONE);
        modifyRetailerButton = generateButton("Rendelés megváltoztatása");
        modifyRetailerButton.addActionListener(this::modifyRetailer);
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

    private void modifyRetailer(ActionEvent event) {
        if (validateRetailer(addressTextField.getText(), creditLineTextField.getText(), phoneTextField.getText())) {
            GuiManager.modifyRetailer(ID, addressTextField.getText(), new BigDecimal(creditLineTextField.getText()), phoneTextField.getText());
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }

    }

    private boolean validateRetailer(String address, String creditLine, String phone) {
        if (address.isEmpty() || creditLine.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(screen, "hiányzó paraméter");
            return false;
        }
        Pattern pBigDec = Pattern.compile(BIGDECIMAL_REGEXP);
        Matcher mBigDec = pBigDec.matcher(creditLine);
        if (!mBigDec.matches()) {
            JOptionPane.showMessageDialog(screen, "Hibásan megadott hitelkeret");
            return false;
        }
        return true;
    }

}
