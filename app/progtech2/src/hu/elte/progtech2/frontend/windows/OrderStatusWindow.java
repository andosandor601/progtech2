/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.frontend.windows;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import hu.elte.progtech2.backend.enums.OrderStatus;
import hu.elte.progtech2.frontend.components.factory.SwingComponentFactory;

/**
 * OrderStatusWindow A kijelölt rendelés státuszának megváltoztatására
 * létrehozott ablak
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderStatusWindow extends JFrame {

    private JComboBox statusComboBox;
    private JButton modifyStatusOfSelectedOrderButton;
    private final DashboardWindow window;
    private final String index;

    public OrderStatusWindow(DashboardWindow screen) {
        this.window = screen;
        this.index = screen.getSelectedIndex();
        initScreen();
    }

    private void initScreen() {
        setTitle("Rendelés státuszának megváltoztatása");
        setLayout(new FlowLayout());
        JPanel panel = new JPanel(new FlowLayout());

        statusComboBox = SwingComponentFactory.generateComboBox(panel, "Státusz kiválasztása", OrderStatus.values());

        modifyStatusOfSelectedOrderButton = SwingComponentFactory.generateButton(panel, "Kijelölt rendelés státuszának megváltoztatása");

        /**
         * this::modifyStatus => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        modifyStatusOfSelectedOrderButton.addActionListener(this::modifyStatus);

        add(panel);
    }

    private void modifyStatus(ActionEvent event) {
        if (statusComboBox.getSelectedIndex() > -1) {
            window.modifyStatusOfSelectedOrder(statusComboBox.getSelectedItem(), index);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else {
            JOptionPane.showMessageDialog(this, "Nincs kiválasztott státusz");
        }

    }

}
