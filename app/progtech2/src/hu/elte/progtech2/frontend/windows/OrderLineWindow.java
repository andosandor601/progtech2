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
import javax.swing.JFrame;
import javax.swing.JPanel;
import hu.elte.progtech2.frontend.components.factory.SwingComponentFactory;

/**
 * OrderLineWindow A táblázatban kiválasztott rendeléssorhoz kapcsolódó
 * műveletek (most csak a törlés) kezelése
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderLineWindow extends JFrame {

    private JButton deleteOrderLineButton;
    private final long orderId;
    private final long orderLineId;
    private final DashboardWindow screen;

    public OrderLineWindow(DashboardWindow screen, long orderLineId, long orderId) {
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.screen = screen;
        initWindow();
    }

    private void initWindow() {
        setTitle(orderLineId + " azonosítójú rendeléssor törlése");
        setLayout(new FlowLayout());

        JPanel panel = new JPanel(new FlowLayout());

        deleteOrderLineButton = SwingComponentFactory.generateButton(panel, "Kijelölt rendeléssor törlése");

        /**
         * this::deleteOrderLine => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        deleteOrderLineButton.addActionListener(this::deleteOrderLine);

        add(panel);
    }

    private void deleteOrderLine(ActionEvent event) {
        screen.deleteOrderLine(orderLineId, orderId);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
