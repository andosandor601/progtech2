/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progtech2.frontend.windows;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jdk.nashorn.internal.codegen.CompilerConstants;
import progtech2.backend.entities.Order;
import progtech2.backend.entities.OrderLine;
import progtech2.backend.entities.Product;
import progtech2.backend.entities.Retailer;
import progtech2.frontend.GuiManager;
import progtech2.frontend.components.DashboardPanel;
import progtech2.frontend.components.OrderPanel;
import progtech2.frontend.components.ProductPanel;
import progtech2.frontend.components.RetailerPanel;

/**
 *
 * @author <Andó Sándor Zsolt>
 */
public class DashboardWindow extends JFrame {

    private static JPanel actionPanel;
    private static DashboardPanel contentPanel;

    private final static Object[] PRODUCT_COLUMN_NAMES = new Object[]{"Termék neve", "Termék ára", "Termék mennyiésge"};
    private final static Object[] RETAILER_COLUMN_NAMES = new Object[]{"Kereskedő neve", "Kereskedő címe", "Kereskedő hitelkerete", "Kereskedő telefonszáma"};
    private final static Object[] ORDER_COLUMN_NAMES = new Object[]{"Rendelés azonosító", "Kereskedő neve", "Rendelés Dátuma", "Rendelés ára", "Rendelés státusza"};
    private final static Object[] ORDERLINE_COLUMN_NAMES = new Object[]{"Rendeléssor azonosító", "Rendelés azonosító", "Termék neve", "Rendelt mennyiség", "Rendeléssor ára"};

    public DashboardWindow() throws HeadlessException {
        initScreen();
    }

    private void initScreen() {
        setLayout(new FlowLayout());
        contentPanel = new DashboardPanel(this);
        actionPanel = new JPanel();
        add(contentPanel, actionPanel);
        setTitle("Rendelések");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void doListOrders() {
        //layout igaztása, hogy csupán a táblázat mellé kerüljenek a dolgok, ne a gombok mellé

        remove(actionPanel);
        actionPanel = new OrderPanel(this);
        add(actionPanel);

        List<Order> orders = GuiManager.listAllOrders();
        List<Object> content = new ArrayList<>();
        for (Order row : orders) {
            content.add(row.toArray());
        }
        contentPanel.addContentToTable(content, ORDER_COLUMN_NAMES);

        revalidate();
        repaint();
        pack();
    }

    public void doListProducts() {
        //layout igaztása, hogy csupán a táblázat mellé kerüljenek a dolgok, ne a gombok mellé

        remove(actionPanel);
        actionPanel = new ProductPanel(this);
        add(actionPanel);

        List<Product> products = GuiManager.listAllProducts();
        List<Object[]> content = new ArrayList<>();
        for (Product row : products) {
            content.add(row.toArray());
        }
        contentPanel.addContentToTable(content, PRODUCT_COLUMN_NAMES);

        revalidate();
        repaint();
        pack();
    }

    public void doListRetailers() {
        //layout igaztása, hogy csupán a táblázat mellé kerüljenek a dolgok, ne a gombok mellé

        remove(actionPanel);
        actionPanel = new RetailerPanel(this);
        add(actionPanel);

        List<Retailer> retailers = GuiManager.listAllRetailers();
        List<Object[]> content = new ArrayList<>();
        for (Retailer row : retailers) {
            content.add(row.toArray());
        }
        contentPanel.addContentToTable(content, RETAILER_COLUMN_NAMES);

        revalidate();
        repaint();
        pack();
    }

    public void listOrderLines(Object orderId) {

        if (actionPanel instanceof OrderPanel) {
            List<OrderLine> orderLines = GuiManager.listOrderLines((String) orderId);
            List<Object[]> content = new ArrayList<>();
            for (OrderLine row : orderLines) {
                content.add(row.toArray());
            }
            ((OrderPanel) actionPanel).addContentToTable(content, ORDERLINE_COLUMN_NAMES);

            revalidate();
            repaint();
            pack();
        }
    }

    public void deleteOrder() {
        GuiManager.deleteOrder(contentPanel.getSelectedRowIndex());
        doListOrders();
    }
    
    public void deleteOrderLine(long orderLineId, long orderId) {
        GuiManager.deleteOrderLine(orderLineId);
        if (actionPanel instanceof OrderPanel){
            listOrderLines(orderId+"");
        }
        doListOrders();
    }

    public void modifyStatusOfSelectedOrder(Object selectedItem, String orderId) {
        if (actionPanel instanceof OrderPanel) {
            GuiManager.modifyOrder(orderId, selectedItem);

            revalidate();
            repaint();
            pack();
        }
    }

    public String getSelectedIndex() {
        return contentPanel.getSelectedRowIndex();
    }

    public void addContent(List<Order> orders) {
            List<Object[]> content = new ArrayList<>();
            for (Order row : orders) {
                content.add(row.toArray());
            }
            contentPanel.addContentToTable(content, ORDERLINE_COLUMN_NAMES);

            revalidate();
            repaint();
            pack();
    }

}
