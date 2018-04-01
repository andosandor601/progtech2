/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.windows;

import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import progtech2.backend.enums.OrderStatus;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderStatusWindow extends JFrame {

    private JComboBox statusComboBox;
    private JButton modifyStatusOfSelectedOrderButton;
    private DashboardWindow window;
    private String index;

    public OrderStatusWindow(DashboardWindow screen) {
        this.window = screen;
        this.index = screen.getSelectedIndex();
        initScreen();
    }

    private void initScreen() {
        setTitle("Új rendelés");
        setLayout(new FlowLayout());
        statusComboBox = generateComboBox("Státusz kiválasztása");
        modifyStatusOfSelectedOrderButton = generateButton("Kijelölt rendelés státuszának megváltoztatása");
        modifyStatusOfSelectedOrderButton.addActionListener(this::modifyStatus);
        setTitle("Rendelés státuszának megváltoztatása");
    }

    private JComboBox generateComboBox(String text) {
        JComboBox comboBox = new JComboBox(OrderStatus.values());
        JLabel label = new JLabel(text);
        add(label);
        add(comboBox);
        return comboBox;
    }

    private JButton generateButton(String text) {
        JButton button = new JButton(text);
        add(button);
        return button;
    }

    private void modifyStatus(ActionEvent event) {
        if (statusComboBox.getSelectedIndex() > -1) {
            window.modifyStatusOfSelectedOrder(statusComboBox.getSelectedItem(), index);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }else{
            JOptionPane.showMessageDialog(this, "Nincs kiválasztott státusz");
        }
        
    }

}
