/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.frontend.windows;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import hu.elte.progtech2.backend.entities.OrderLine;
import hu.elte.progtech2.frontend.GuiManager;
import hu.elte.progtech2.frontend.components.factory.SwingComponentFactory;
import hu.elte.progtech2.frontend.validator.Validator;

/**
 * OrderWindow Új rendelés felvitelére szolgáló ablak
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderWindow extends JFrame {

    private JTable orderLineTable;
    private JPanel actionPanel;
    private JComboBox retailersComboBox, productsComboBox;
    private JTextField quantityTextField;
    private JButton newOrderLineButton, addNewOrderButton;
    private final Object[] COLUMN_NAMES = new Object[]{"Terméknév", "Mennyiség", "Ár"};
    private List<OrderLine> orderLines;

    public OrderWindow() throws HeadlessException {
        orderLines = new LinkedList<>();
        initScreen();
    }

    private void initScreen() {
        setTitle("Új rendelés");
        setLayout(new FlowLayout());
        initTable();
        initActionPanel();
        add(new JScrollPane(orderLineTable));
        add(actionPanel);
        setTitle("Rendelések");
    }

    private void initTable() {
        orderLineTable = new JTable(5, 5);
    }

    private void initActionPanel() {
        actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));

        productsComboBox = SwingComponentFactory.generateComboBox(actionPanel, "Termék kiválasztása");
        GuiManager.listAllProducts().forEach(product
                -> productsComboBox.addItem(product.getProductName())
        );

        quantityTextField = SwingComponentFactory.generateTextField(actionPanel, "Rendelés mennyisége:");

        newOrderLineButton = SwingComponentFactory.generateButton(actionPanel, "Új rendeléssor felvitele");

        /**
         * this::addNewOrderLine => method reference
         * https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html
         *
         * https://stackoverflow.com/questions/41069817/making-an-action-listener-for-a-jbutton-as-a-method
         *
         */
        newOrderLineButton.addActionListener(this::addNewOrderLine);

        actionPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        retailersComboBox = SwingComponentFactory.generateComboBox(actionPanel, "Kereskedő kiválasztása");
        GuiManager.listAllRetailers().forEach(retailer
                -> retailersComboBox.addItem(retailer.getName())
        );

        addNewOrderButton = SwingComponentFactory.generateButton(actionPanel, "Rendelés leadása");
        addNewOrderButton.addActionListener(this::addNewOrder);
    }

    private void addNewOrderLine(ActionEvent event) {
        if (Validator.validateStringAndInteger((String) productsComboBox.getSelectedItem(), quantityTextField.getText(), this)) {
            orderLines.add(createOrderLine((String) productsComboBox.getSelectedItem(), Integer.parseInt(quantityTextField.getText())));
            addContentToTable(orderLines);
        }
    }

    private void addNewOrder(ActionEvent event) {
        GuiManager.addNewOrder(orderLines, retailersComboBox.getSelectedItem());
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private OrderLine createOrderLine(String productName, int quantity) {
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(productName);
        orderLine.setQuantity(quantity);
        orderLine.setPrice(GuiManager.getProductPrice(productName).multiply(new BigDecimal(quantity)));
        return orderLine;
    }

    private void addContentToTable(List<OrderLine> content) {
        orderLineTable.removeAll();
        DefaultTableModel dtm = new DefaultTableModel(COLUMN_NAMES, 0);

        content.forEach(row -> dtm.addRow(row.toArrayWithoutIds()));
        orderLineTable.setModel(dtm);
    }

}
