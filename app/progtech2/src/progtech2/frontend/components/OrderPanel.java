/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.components;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import progtech2.backend.entities.OrderLine;
import progtech2.frontend.GuiManager;
import progtech2.frontend.windows.DashboardWindow;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class OrderPanel extends JPanel {
    
    private JButton newOrderButton, deleteSelectedOrderButton, modifyOrderStatusButton, filterStatusButton;
    private JTable orderLineTable;
    private JComboBox retailersComboBox;
    private final DashboardWindow window;
    
    public OrderPanel(DashboardWindow frame) {
        this.window = frame;
        initOrderPanel();
        initButtons();
        initComboBox();
    }
    
    private void initOrderPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        orderLineTable = new JTable(5, 5);
        addTableListener();
        add(new JScrollPane(orderLineTable));
    }
    
    private void initButtons() {
        newOrderButton = generateButton("Új rendelés");
        newOrderButton.addActionListener(this::addNewOrder);
        deleteSelectedOrderButton = generateButton("Kijelölt rendelés törlése");
        deleteSelectedOrderButton.addActionListener(this::deleteSelectedOrder);
        modifyOrderStatusButton = generateButton("Rendelés státuszának megváltoztatása");
        modifyOrderStatusButton.addActionListener(this::modifyStatusOfSelectedOrder);
        filterStatusButton = generateButton("Szűrés még nem kiszállíottakra");
        filterStatusButton.addActionListener(this::filterStatus);
    }
    
    private JButton generateButton(String text) {
        JButton button = new JButton(text);
        add(button);
        return button;
    }
    
    private void initComboBox() {
        retailersComboBox = new JComboBox();
        JLabel label = new JLabel("Szűrés kiskereskedőre:");
        add(label);
        //add data (retailers)
        GuiManager.listAllRetailers().forEach(retailer -> {
            retailersComboBox.addItem(retailer.getName());
        });
        retailersComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                GuiManager.filterRetailer((String) e.getItem());
            }
        });
        add(retailersComboBox);
    }
    
    private void addTableListener() {
        orderLineTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting() && orderLineTable.getSelectedRow() > -1) {
                    GuiManager.showOrderLineWindow(orderLineTable.getValueAt(orderLineTable.getSelectedRow(), 0), orderLineTable.getValueAt(orderLineTable.getSelectedRow(), 1));
                }
            }
        });
    }
    
    private void addNewOrder(ActionEvent event) {
        GuiManager.showOrderWindow();
    }
    
    public <E> void addContentToTable(List<E> content, Object[] columnNames) {
        orderLineTable.removeAll();
        DefaultTableModel dtm = new DefaultTableModel(columnNames, 0);
        
        for (E row : content) {
            dtm.addRow((Object[]) row);
        }
        orderLineTable.setModel(dtm);
    }
    
    private void deleteSelectedOrder(ActionEvent event) {
        window.deleteOrder();
    }
    
    private void modifyStatusOfSelectedOrder(ActionEvent event) {
        GuiManager.showOrderStatusWindow();
    }
    
    private void filterStatus(ActionEvent event) {
        GuiManager.filterStatusOfOrder();
    }
}
