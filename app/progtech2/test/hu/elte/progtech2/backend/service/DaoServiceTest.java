/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.progtech2.backend.service;

import hu.elte.progtech2.backend.dao.DaoManager;
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
import hu.elte.progtech2.backend.entities.Product;
import hu.elte.progtech2.backend.entities.Retailer;
import hu.elte.progtech2.backend.enums.OrderStatus;
import hu.elte.progtech2.backend.service.exceptions.ServiceException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;

/**
 * DaoServiceTest
 *
 * @author <Andó Sándor Zsolt>
 */
public class DaoServiceTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private DaoService underTest;

    @Mock
    private DaoManager daoManager;

    private static OrderLine orderLine1 = new OrderLine();
    private static OrderLine orderLine2 = new OrderLine();
    private static OrderLine orderLine3 = new OrderLine();
    private static Product product1 = new Product();
    private static Product product2 = new Product();
    private static Order order1 = new Order();
    private static Order order2 = new Order();
    private static Retailer retailer1 = new Retailer();
    private static Retailer retailer2 = new Retailer();

    /**
     * A teszt osztály lefutása előtt kerül végrehajtásra, inicializálás
     *
     * @throws Exception
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        initProducts();
        initRetailers();
        initOrders();
        initOrderLines();
    }

    private static void initProducts() {
        product1.setProductName("Tejcsi");
        product1.setPrice(BigDecimal.TEN);
        product1.setStock(10);
        product2.setProductName("KÁVÉ");
        product2.setPrice(BigDecimal.TEN);
        product2.setStock(20);
    }

    private static void initRetailers() {
        retailer1.setName("BESTCOMPANY");
        retailer1.setCreditLine(new BigDecimal(100));
    }

    private static void initOrders() {
        order1.setStatus(OrderStatus.COMPLETED);
        order2.setStatus(OrderStatus.UNDER_DELIVERY);
        order2.setOrderPrice(BigDecimal.valueOf(20));
    }

    private static void initOrderLines() {
        orderLine1.setPrice(BigDecimal.TEN);
        orderLine1.setQuantity(15);
        orderLine1.setOrderLineId(1);
        orderLine2.setPrice(new BigDecimal(101));
        orderLine2.setQuantity(15);
        orderLine3.setPrice(BigDecimal.TEN);
        orderLine3.setQuantity(15);
        orderLine3.setOrderLineId(1);
    }

    /**
     * A teszt osztály utolsó műveletei, az összes teszt lefutása után
     *
     * @throws Exception
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Minden egyes teszt előtt lefut
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        /*
        * Megkeresi az összes annotált mezőt a tesztosztályban, majd létrehozza a mock-objecteket
         */
        MockitoAnnotations.initMocks(this);
        /*
        Beállítjuk, hogy a mock object-et használja a tesztelt osztályunk
         */
        underTest = new DaoService(daoManager);
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
     * A DaoService osztály addOrder metódusának tesztelése, olyan rendeléssel,
     * ami több darabra szól, mint amennyi az adott termékből a raktáron
     * taálható.
     */
    @Test
    public void testAddOrderWithHigherOrderQuantityThanProductsStock() throws ServiceException {
        //GIVEN
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        BDDMockito.given(daoManager.findRetailer(retailer1.getName())).willReturn(retailer1);
        BDDMockito.given(daoManager.findProduct(orderLine1.getProduct())).willReturn(product1);
        //WHEN
        exception.expect(ServiceException.class);
        //exception tesztelése
        underTest.addOrder(retailer1.getName(), orderLines);
        //THEN
    }

    /**
     * A DaoService osztály addOrder metódusának tesztelése, olyan rendeléssel,
     * ami drágább, mint amennyi az adott kereskedő hitelkerete.
     *
     * @throws ServiceException
     */
    @Test
    public void testAddOrderWithHigherOrderPriceThanRetailersCreditline() throws ServiceException {
        //GIVEN
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine2);
        BDDMockito.given(daoManager.findRetailer(retailer1.getName())).willReturn(retailer1);
        BDDMockito.given(daoManager.findProduct(orderLine2.getProduct())).willReturn(product1);
        //WHEN
        exception.expect(ServiceException.class);
        //exception tesztelése
        underTest.addOrder(retailer1.getName(), orderLines);
        //THEN
    }

    /**
     * A DaoService osztály addOrder metódusának tesztelése, olyan rendeléssel,
     * ami várhatóan megfelel a feladat követelményeinek.
     *
     * @throws Exception
     */
    @Test
    public void testAddOrderWithCorrectValues() throws Exception {
        //GIVEN
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        BDDMockito.given(daoManager.findRetailer(retailer1.getName())).willReturn(retailer1);
        BDDMockito.given(daoManager.findProduct(orderLine1.getProduct())).willReturn(product2);
        BDDMockito.given(daoManager.findProduct("KÁVÉ")).willReturn(product2);
        //WHEN
        underTest.addOrder(retailer1.getName(), orderLines);
        //THEN
        assertEquals("Nem írta át a raktáron levő termék mennyiségét", 5, product2.getStock());
        BDDMockito.verify(daoManager).saveOrder(BDDMockito.any(Order.class), BDDMockito.eq(orderLines));
    }

    /**
     * A DaoService osztály deleteOrder metódusának tesztelése.
     */
    @Test
    public void testDeleteCompletedOrder() throws ServiceException {
        //GIVEN
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine3);
        BDDMockito.given(daoManager.findOrderLinesByOrderId(BDDMockito.anyLong())).willReturn(orderLines);
        BDDMockito.given(daoManager.findOrderLine(BDDMockito.anyLong())).willReturn(orderLine3);
        BDDMockito.given(daoManager.findOrder(BDDMockito.anyLong())).willReturn(order1);
        //WHEN
        underTest.deleteOrder(1);
        //THEN
        BDDMockito.verify(daoManager).deleteOrder(1);
        BDDMockito.verify(daoManager).deleteOrderLine(BDDMockito.anyLong());
    }

    /**
     * A DaoService osztály deleteOrderLine metódusának tesztelése.
     */
    @Test
    public void testDeleteOrderLineWithNotCompletedOrder() throws ServiceException {
        //GIVEN
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.add(orderLine1);
        BDDMockito.given(daoManager.findOrderLine(BDDMockito.anyLong())).willReturn(orderLine1);
        BDDMockito.given(daoManager.findOrder(BDDMockito.anyLong())).willReturn(order2);
        BDDMockito.given(daoManager.findProduct(BDDMockito.any())).willReturn(product1);
        //WHEN
        underTest.deleteOrderLine(orderLine1.getOrderLineId());
        //THEN
        BDDMockito.verify(daoManager).deleteOrderLine(orderLine1.getOrderLineId());
        assertEquals(BigDecimal.ZERO, order2.getOrderPrice());
        assertEquals(25, product1.getStock());
    }

    /**
     * Test of addProduct method, of class DaoService.
     */
    @Test
    public void testAddProduct() {
        //GIVEN
        //WHEN
        underTest.addProduct("name", BigDecimal.ONE, 10);
        //THEN
        BDDMockito.verify(daoManager).saveProduct(BDDMockito.any(Product.class));
    }

    /**
     * Test of addRetailer method, of class DaoService.
     */
    @Test
    public void testAddRetailer() {
        //GIVEN
        //WHEN
        underTest.addRetailer("KFT", "asdas", BigDecimal.ONE, "asdasf");
        //THEN
        BDDMockito.verify(daoManager).saveRetailer(BDDMockito.any(Retailer.class));
    }

    /**
     * Test of deleteProduct method, of class DaoService.
     */
    @Test
    public void testDeleteProduct() throws Exception {
        //GIVEN
        //WHEN
        underTest.deleteProduct("tejbepapi");
        //THEN
        BDDMockito.verify(daoManager).deleteProduct("tejbepapi");
    }

    /**
     * Test of deleteRetailer method, of class DaoService.
     */
    @Test
    public void testDeleteRetailer() throws Exception {
        //GIVEN
        //WHEN
        underTest.deleteRetailer("tejbepapigyár");
        //THEN
        BDDMockito.verify(daoManager).deleteRetailer("tejbepapigyár");
    }

    /**
     * Test of listNotDeliveredOrders method, of class DaoService.
     */
    @Test
    public void testListNotDeliveredOrders() {
        //GIVEN
        //WHEN
        underTest.listNotDeliveredOrders();
        //THEN
        BDDMockito.verify(daoManager).listNotDeliveredOrders();
    }

    /**
     * Test of listOrderLines method, of class DaoService.
     */
    @Test
    public void testListOrderLines() {
        //GIVEN
        //WHEN
        underTest.listOrderLines(10);
        //THEN
        BDDMockito.verify(daoManager).listOrderLines(BDDMockito.anyLong());
    }

    /**
     * Test of listOrders method, of class DaoService.
     */
    @Test
    public void testListOrders() {
        //GIVEN
        //WHEN
        underTest.listOrders();
        //THEN
        BDDMockito.verify(daoManager).listOrders();
    }

    /**
     * Test of listOrdersByRetailer method, of class DaoService.
     */
    @Test
    public void testListOrdersByRetailer() {
        //GIVEN
        //WHEN
        underTest.listOrdersByRetailer("KÁEFTÉ");
        //THEN
        BDDMockito.verify(daoManager).listOrdersByRetailer("KÁEFTÉ");
    }

    /**
     * Test of listProducts method, of class DaoService.
     */
    @Test
    public void testListProducts() {
        //GIVEN
        //WHEN
        underTest.listProducts();
        //THEN
        BDDMockito.verify(daoManager).listProducts();
    }

    /**
     * Test of listRetailers method, of class DaoService.
     */
    @Test
    public void testListRetailers() {
       //GIVEN
        //WHEN
        underTest.listRetailers();
        //THEN
        BDDMockito.verify(daoManager).listRetailers();
    }

    /**
     * Test of findProduct method, of class DaoService.
     */
    @Test
    public void testFindProduct() {
        //GIVEN
        BDDMockito.given(daoManager.findProduct(BDDMockito.anyString())).willReturn(product1);
        //WHEN
        underTest.findProduct(product1.getProductName());
        //THEN
        BDDMockito.verify(daoManager).findProduct(product1.getProductName());
    }

    /**
     * Test of modifyOrderStatus method, of class DaoService.
     */
    @Test
    public void testModifyOrderStatus() {
        //GIVEN
        BDDMockito.given(daoManager.findOrder(BDDMockito.anyLong())).willReturn(order1);
        //WHEN
        underTest.modifyOrderStatus(0, OrderStatus.COMPLETED);
        //THEN
        BDDMockito.verify(daoManager).updateOrder(order1);
    }

    /**
     * Test of modifyProduct method, of class DaoService.
     */
    @Test
    public void testModifyProduct() {
        //GIVEN
        BDDMockito.given(daoManager.findProduct(BDDMockito.any())).willReturn(product1);
        //WHEN
        underTest.modifyProduct("asd", BigDecimal.ONE, 10);
        //THEN
        BDDMockito.verify(daoManager).updateProduct(product1);
    }

    /**
     * Test of modifyProductQuantity method, of class DaoService.
     */
    @Test
    public void testModifyProductQuantity() {
        //GIVEN
        BDDMockito.given(daoManager.findProduct(BDDMockito.any())).willReturn(product1);
        //WHEN
        underTest.modifyProductQuantity("asds", 12);
        //THEN
        BDDMockito.verify(daoManager).updateProduct(product1);
    }

    /**
     * Test of modifyRetailer method, of class DaoService.
     */
    @Test
    public void testModifyRetailer() {
         //GIVEN
        BDDMockito.given(daoManager.findRetailer(BDDMockito.any())).willReturn(retailer2);
        //WHEN
        underTest.modifyRetailer("asd", "asdafd", BigDecimal.ONE, "4684684");
        //THEN
        BDDMockito.verify(daoManager).updateRetailer(retailer2);
    }
}
