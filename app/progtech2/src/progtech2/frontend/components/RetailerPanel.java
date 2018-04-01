/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.components;

import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import progtech2.frontend.GuiManager;
import progtech2.frontend.windows.DashboardWindow;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class RetailerPanel extends JPanel {

    private JButton newRetailerButton;
    private JTextField addressTextField, creditLineTextField, phoneNumberTextField, retailerNameTextField;
    private final DashboardWindow window;

    public RetailerPanel(DashboardWindow frame) {
        this.window = frame;
        initProductPanel();
    }

    private void initProductPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        retailerNameTextField = generateTextField("Kiskereskedő neve:");
        addressTextField = generateTextField("Cím:");
        phoneNumberTextField = generateTextField("Telefonszám:");
        creditLineTextField = generateTextField("Hitelkeret:");
        newRetailerButton = generateButton("Új kereskedő hozzáadása");
        newRetailerButton.addActionListener(this::addNewRetailer);
    }

    private JTextField generateTextField(String text) {
        //add validation
        JLabel label = new JLabel(text);
        JTextField textField = new JTextField();
        add(label);
        add(textField);
        return textField;
    }

    private JButton generateButton(String text) {
        JButton button = new JButton(text);
        add(button);
        return button;
    }

    private void addNewRetailer(ActionEvent event) {
        GuiManager.addNewRetailer(retailerNameTextField.getText(), addressTextField.getText(), creditLineTextField.getText(), phoneNumberTextField.getText());
    }
}
