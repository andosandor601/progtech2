/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.windows;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderLineWindow extends JFrame {

    private JButton deleteOrderLineButton;
    private long orderId, orderLineId;
    private DashboardWindow screen;

    public OrderLineWindow(DashboardWindow screen, long orderLineId, long orderId) {
        this.orderId = orderId;
        this.orderLineId = orderLineId;
        this.screen = screen;
        initWindow();
    }

    private void initWindow() {
        setTitle(orderLineId + " azonosítójú rendeléssor törlése");
        setLayout(new FlowLayout());
        deleteOrderLineButton = generateButton("Kijelölt rendeléssor törlése");
        deleteOrderLineButton.addActionListener(this::deleteOrderLine);
        add(deleteOrderLineButton);
    }

    private JButton generateButton(String text) {
        JButton button = new JButton(text);
        add(button);
        return button;
    }
    
    private void deleteOrderLine(ActionEvent event){
        screen.deleteOrderLine(orderLineId, orderId);
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

}
