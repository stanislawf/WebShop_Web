/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.AccountUser;
import info.novatec.webshop.entities.Address;
import info.novatec.webshop.entities.Bill;
import info.novatec.webshop.entities.CreditCard;
import info.novatec.webshop.entities.Guest;
import info.novatec.webshop.entities.OrderLine;
import info.novatec.webshop.entities.PurchaseOrder;
import info.novatec.webshop.persistence.AccountManager;
import info.novatec.webshop.persistence.BillManager;
import info.novatec.webshop.persistence.OrderManager;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sf
 */
@ManagedBean(name = "orderBean")
@ViewScoped
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBean.class);

    @EJB
    private OrderManager orderService;
    @EJB
    private AccountManager accountService;
    @EJB
    private BillManager billService;
    private PurchaseOrder order;
    private Address deliveryAddress;
    private Address billingAddress;
    private Bill bill;
    private double totalPrice;
    private List<OrderLine> orderLines;
    private AccountUser account;
    private CreditCard creditCard;
    @Inject
    private ShoppingCartBean shoppingcart;
    FacesContext fc = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
    private Guest guest;
    private boolean accountExists = false;

    @PostConstruct
    public void init() {
        order = new PurchaseOrder();
        deliveryAddress = new Address();
        billingAddress = new Address();
        orderLines = shoppingcart.getOrderLines();
        totalPrice = calculateTotalPrice();
        creditCard = new CreditCard();
        guest = new Guest();

        bill = new Bill();
        if (request.getUserPrincipal() != null) {
            account = (AccountUser) accountService.getAccountByEmail(request.getUserPrincipal().getName());
            billingAddress = accountService.getAddressByHomeAddress(true, account);
            deliveryAddress = accountService.getAddressByHomeAddress(true, account);
        }
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public AccountUser getAccount() {
        return account;
    }

    public void setAccount(AccountUser account) {
        this.account = account;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public double calculateTotalPrice() {
        double price = 0;
        for (OrderLine ol : orderLines) {
            price += (ol.getQuantity() * ol.getArticle().getPrice());
        }
        return price;
    }

    private boolean checkboxValue = false;
    private boolean renderValue = false;

    public boolean isCheckboxValue() {
        return checkboxValue;
    }

    public void setCheckboxValue(boolean checkboxValue) {
        this.checkboxValue = checkboxValue;
    }

    public boolean isRenderValue() {
        return renderValue;
    }

    public void setRenderValue(boolean renderValue) {
        this.renderValue = renderValue;
    }

    public void changeRenderValue(ValueChangeEvent e) {
        checkboxValue = (boolean) e.getNewValue();

        if (isCheckboxValue()) {
            setRenderValue(true);
        } else {
            setRenderValue(false);
        }
    }

    public void prepareOrderForGuest() {
        LOGGER.info("Method prepareOrderForGuest()");
        Guest existingGuest = (Guest) accountService.getAccountByEmail(guest.getEmail());
        Address existingDeliveryAddress = null;
        Address existingBillingAddress = null;
        Bill existingBill = null;
        if (existingGuest != null) {
            guest = existingGuest;
            accountExists = true;

        }

        if (accountExists) {
            existingBillingAddress = accountService.getAddressByStreetAndAccount(billingAddress.getStreet(), guest);
            if (checkboxValue) {
                existingDeliveryAddress = accountService.getAddressByStreetAndAccount(billingAddress.getStreet(), existingGuest);
            } else {
                existingDeliveryAddress = accountService.getAddressByStreetAndAccount(deliveryAddress.getStreet(), existingGuest);
            }
        }

        existingBill = billService.getBillByAccountNumberAndOwner(bill.getAccountNumber(), bill.getAccountOwner());

        if (existingBillingAddress != null) {
            billingAddress = existingBillingAddress;
        }
        if (existingDeliveryAddress != null) {
            deliveryAddress = existingDeliveryAddress;
        }
        if (existingBill != null) {
            bill = existingBill;
        }

        billingAddress.setIsHomeAddress(true);
        deliveryAddress.setAccount(guest);
        billingAddress.setAccount(guest);

        LocalDate orderDate = LocalDate.now();
        LOGGER.info(orderDate.toString());

        order.setOrderDate(orderDate);

        for (OrderLine ol : orderLines) {
            ol.setOrder(order);
        }

        order.setAccount(guest);

        order.setBillingAddress(billingAddress);
        List<Address> addresses = new ArrayList();
        if (checkboxValue || order.getBillingAddress().equals(order.getDeliveryAddress())) {
            order.setDeliveryAddress(billingAddress);
            addresses.add(order.getBillingAddress());
        } else {

            addresses.add(order.getBillingAddress());
            addresses.add(order.getDeliveryAddress());
        }

        guest.setAddresses(addresses);
        order.setTotalPrice(totalPrice);
        order.setOrderLines(orderLines);
        order.setBill(bill);
    }

    public void prepareOrderForAccountUser() {
        LOGGER.info("Method prepareOrderForAccountUser()");
        AccountUser existingAccountUser = null;
        if (request.getUserPrincipal() != null) {
            existingAccountUser = (AccountUser) accountService.getAccountByEmail(request.getUserPrincipal().getName());
        }
        Address existingDeliveryAddress = null;
        Address existingBillingAddress = null;
        Bill existingBill = null;
        if (existingAccountUser != null) {
            account = existingAccountUser;
            accountExists = true;
        }

        if (accountExists) {
            existingBillingAddress = accountService.getAddressByStreetAndAccount(billingAddress.getStreet(), account);
            if (checkboxValue) {
                existingDeliveryAddress = accountService.getAddressByStreetAndAccount(billingAddress.getStreet(), existingAccountUser);
            } else {
                existingDeliveryAddress = accountService.getAddressByStreetAndAccount(deliveryAddress.getStreet(), existingAccountUser);
            }
        }

        existingBill = billService.getBillByAccountNumberAndOwner(bill.getAccountNumber(), bill.getAccountOwner());

        if (existingBillingAddress != null) {
            billingAddress = existingBillingAddress;
        }
        if (existingDeliveryAddress != null) {
            deliveryAddress = existingDeliveryAddress;
        }
        if (existingBill != null) {
            bill = existingBill;
        }

        billingAddress.setIsHomeAddress(true);
        deliveryAddress.setAccount(account);
        billingAddress.setAccount(account);

        LocalDate orderDate = LocalDate.now();
        LOGGER.info(orderDate.toString());

        order.setOrderDate(orderDate);

        for (OrderLine ol : orderLines) {
            ol.setOrder(order);
        }

        order.setAccount(account);

        order.setBillingAddress(billingAddress);
        List<Address> addresses = new ArrayList();
        if (checkboxValue || order.getBillingAddress().equals(order.getDeliveryAddress())) {
            order.setDeliveryAddress(billingAddress);
            addresses.add(order.getBillingAddress());
            LOGGER.info("SAME");
        } else {

            addresses.add(order.getBillingAddress());
            addresses.add(order.getDeliveryAddress());

        }

        account.setAddresses(addresses);
        order.setTotalPrice(totalPrice);
        order.setOrderLines(orderLines);
        order.setBill(bill);

    }

    public void prepareOrder() {
        LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (request.getUserPrincipal() != null) {
            prepareOrderForAccountUser();
        } else {
            prepareOrderForGuest();
        }
        LOGGER.info("THE ORDER HAS BEEN PREPARED!");
    }

    public String persistOrder() {
        boolean orderPersisted = false;
        LOGGER.info("OrderInformation");
        LOGGER.info("Deliveraddress:" + order.getDeliveryAddress().getStreet());
        LOGGER.info("Billingaddress:" + order.getBillingAddress().getStreet());

        if (request.getUserPrincipal() != null) {
            persistOrderWithAccount();
            orderPersisted = true;
        } else {
            persistOrderWithGuest();
            orderPersisted = true;
        }

        if (orderPersisted) {
            LOGGER.info("THE ORDER HAS BEEN PERSISTED!");
            shoppingcart.clearCart();
            return "successfullOrder";
        }
        return null;
    }

    public void persistOrderWithAccount() {
        if (order != null) {
            if (accountExists) {
                LOGGER.info("updateAccount");
                accountService.updateAccount(account);

            } else {
                LOGGER.info("persistGuest");
                accountService.createAccount(account);
            }
            orderService.createOrder(order);
        }
    }

    public void persistOrderWithGuest() {
        LOGGER.info("Method persistOrderWithGuest");
        if (order != null) {
            if (!accountExists) {
                LOGGER.info("persistGuest");
                accountService.createAccount(guest);
            } else {
                accountService.updateAccount(guest);
            }
            orderService.createOrder(order);
        }

    }
}
