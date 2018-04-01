/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.windows;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import progtech2.backend.dao.DaoManager;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Product;
import progtech2.frontend.GuiManager;
import progtech2.frontend.components.DashboardPanel;

/**
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

    private final static String INTEGER_REGEXP = "\\d+";

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

        productsComboBox = generateComboBox(actionPanel, "Termék kiválasztása");
        GuiManager.listAllProducts().forEach(product -> {
            productsComboBox.addItem(product.getProductName());
        });

        quantityTextField = generateTextField(actionPanel, "Rendelés mennyisége:");
        newOrderLineButton = generateButton(actionPanel, "Új rendeléssor felvitele");
        newOrderLineButton.addActionListener(this::addNewOrderLine);

        actionPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        retailersComboBox = generateComboBox(actionPanel, "Kereskedő kiválasztása");
        GuiManager.listAllRetailers().forEach(retailer -> {
            retailersComboBox.addItem(retailer.getName());
        });

        addNewOrderButton = generateButton(actionPanel, "Rendelés leadása");
        addNewOrderButton.addActionListener(this::addNewOrder);
    }

    private JComboBox generateComboBox(JPanel panel, String text) {
        JComboBox comboBox = new JComboBox();
        JLabel label = new JLabel(text);
        panel.add(label);
        //add data (retailers)
        panel.add(comboBox);
        return comboBox;
    }

    private JTextField generateTextField(JPanel panel, String text) {
        //add validation
        JLabel label = new JLabel(text);
        JTextField textField = new JTextField();
        panel.add(label);
        panel.add(textField);
        return textField;
    }

    private JButton generateButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        panel.add(button);
        return button;
    }

    private void addNewOrderLine(ActionEvent event) {
        if (validate((String) productsComboBox.getSelectedItem(), quantityTextField.getText())) {
            orderLines.add(createOrderLine((String) productsComboBox.getSelectedItem(), Integer.parseInt(quantityTextField.getText())));

            addContentToTable(orderLines);
        }
    }

    private void addNewOrder(ActionEvent event) {
        GuiManager.addNewOrder(orderLines, retailersComboBox.getSelectedItem());
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    private boolean validate(String comboItem, String quantityText) {
        if (comboItem.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiányzó mező");
            return false;
        }
        if (!quantityText.matches(INTEGER_REGEXP)) {
            JOptionPane.showMessageDialog(this, "Hibás számbevitel");
            return false;
        }
        return true;
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

        for (OrderLine row : content) {
            dtm.addRow(row.toArrayWithoutIds());
        }
        orderLineTable.setModel(dtm);
    }

}
