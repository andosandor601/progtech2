/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.service;

import hu.elte.progtech2.backend.service.DaoService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import hu.elte.progtech2.backend.entities.Order;
import hu.elte.progtech2.backend.entities.OrderLine;
import hu.elte.progtech2.backend.service.exceptions.ServiceException;

/**
 * DaoServiceTest
 *
 * @author <Andó Sándor Zsolt>
 */
public class DaoServiceTest {

    static String productName, retailerName, address, phone;
    static BigDecimal productPrice, creditLine;
    static int productStock, orderQuantity1, orderQuantity2, orderQuantity3;
    static DaoService instance;
    static List<OrderLine> orderLines;
    static OrderLine orderline1, orderline2, orderline3;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    /**
     * A teszt osztály lefutása előtt kerül végrehajtásra, inicializálás
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = new DaoService();

        productName = "testProduct";
        productPrice = new BigDecimal(10);
        productStock = 55;

        retailerName = "testRetailer";
        address = "testAddress";
        creditLine = new BigDecimal(100);
        phone = "testPhone";

        orderQuantity1 = 1;
        orderQuantity2 = 11;
        orderQuantity3 = productStock + 1;

        createOrderLines();

        orderLines = new ArrayList<>();
        instance.addRetailer(retailerName, address, creditLine, phone);
        instance.addProduct(productName, productPrice, productStock);
    }

    /**
     * A teszt osztály utolsó műveletei, az összes teszt lefutása után
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        instance.listOrdersByRetailer(retailerName).forEach(order -> {
            instance.deleteOrder(order.getOrderId());
        });

        instance.deleteProduct(productName);
        instance.deleteRetailer(retailerName);
    }

    private static void createOrderLines() {
        orderline1 = new OrderLine();
        orderline2 = new OrderLine();
        orderline3 = new OrderLine();

        orderline1.setProduct(productName);
        orderline1.setQuantity(orderQuantity1);
        orderline1.setPrice(productPrice.multiply(new BigDecimal(orderQuantity1)));

        orderline2.setProduct(productName);
        orderline2.setQuantity(orderQuantity2);
        orderline2.setPrice(productPrice.multiply(new BigDecimal(orderQuantity2)));

        orderline3.setProduct(productName);
        orderline3.setQuantity(orderQuantity3);
        orderline3.setPrice(productPrice.multiply(new BigDecimal(orderQuantity3)));
    }

    /**
     * Minden egyes teszt előtt lefut
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        orderLines.clear();
    }

    /**
     * Minden egyes teszt után lefut
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * A DaoService osztály addOrder metódusának tesztelése, olyan rendeléssel, ami
 több darabra szól, mint amennyi az adott termékből a raktáron taálható.
     */
    @Test
    public void testAddOrderWithHigherOrderQuantityThanProductsStock() throws ServiceException {
        System.out.println("addOrder");
        orderLines.add(orderline3);

        //exception tesztelése
        exception.expect(ServiceException.class);
        instance.addOrder(retailerName, orderLines);
    }

    /**
     * A DaoService osztály addOrder metódusának tesztelése, olyan rendeléssel, ami
 drágább, mint amennyi az adott kereskedő hitelkerete.
     *
     * @throws ServiceException
     */
    @Test
    public void testAddOrderWithHigherOrderPriceThanRetailersCreditline() throws ServiceException {
        System.out.println("addOrder");
        orderLines.add(orderline2);

        exception.expect(ServiceException.class);
        instance.addOrder(retailerName, orderLines);
    }

    /**
     * A DaoService osztály addOrder metódusának tesztelése, olyan rendeléssel, ami
 várhatóan megfelel a feladat követelményeinek.
     *
     * @throws Exception
     */
    @Test
    public void testAddOrderWithCorrectValues() throws Exception {
        System.out.println("addOrder");
        orderLines.add(orderline1);

        int preQuantity = instance.findProduct(productName).getStock();

        instance.addOrder(retailerName, orderLines);

        int postQuantity = instance.findProduct(productName).getStock();

        assertEquals("Nem írta át a raktáron levő termék mennyiségét", preQuantity - orderline1.getQuantity(), postQuantity);
    }

    /**
     * A DaoService osztály deleteOrder metódusának tesztelése.
     */
    @Test
    public void testDeleteOrder() throws ServiceException {
        System.out.println("deleteOrder");
        orderLines.add(orderline1);
        orderLines.add(orderline1);

        //A leadott rendelés összesített darabszáma egy termékre vonatkozóan
        int sumQuantity = orderline1.getQuantity() * 2;

        instance.addOrder(retailerName, orderLines);

        instance.listOrdersByRetailer(retailerName).forEach(order -> {
            List<OrderLine> testOrderLines = instance.listOrderLines(order.getOrderId());
            if (testOrderLines.size() > 1) {
                int preQuantity = instance.findProduct(productName).getStock();

                instance.deleteOrder(order.getOrderId());

                int postQuantity = instance.findProduct(productName).getStock();

                assertEquals("Nem került vissza a raktárba a termék mennyisége a rendelés törlése után", preQuantity + sumQuantity, postQuantity);
            }
        });
    }

    /**
     * A DaoService osztály deleteOrderLine metódusának tesztelése.
     */
    @Test
    public void testDeleteOrderLine() throws ServiceException {
        System.out.println("deleteOrderLine");
        orderLines.add(orderline1);
        orderLines.add(orderline1);

        instance.addOrder(retailerName, orderLines);

        instance.listOrdersByRetailer(retailerName).forEach(order -> {
            List<OrderLine> testOrderLines = instance.listOrderLines(order.getOrderId());
            if (testOrderLines.size() > 1) {
                BigDecimal orderPrice = order.getOrderPrice();
                OrderLine testOrderLine = testOrderLines.get(0);

                int preQuantity = instance.findProduct(testOrderLine.getProduct()).getStock();
                int testOrderLineQuantity = testOrderLine.getQuantity();

                instance.deleteOrderLine(testOrderLine.getOrderLineId());

                int postQuantity = instance.findProduct(testOrderLine.getProduct()).getStock();

                assertEquals("Nem került vissza a raktárba a termék mennyisége a rendeléssor törlése után", preQuantity + testOrderLineQuantity, postQuantity);

                List<Order> orders = instance.listOrdersByRetailer(retailerName);
                for (Order order1 : orders) {
                    if (order1.getOrderId() == order.getOrderId()) {
                        assertEquals("Nem csökkent a rendelés értéke", orderPrice.subtract(testOrderLine.getPrice()), order1.getOrderPrice());
                    }
                }
            }
        });
    }
}
