/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Account;
import info.novatec.webshop.entities.AccountUser;
import info.novatec.webshop.entities.Address;
import info.novatec.webshop.entities.Bill;
import info.novatec.webshop.entities.CreditCard;
import info.novatec.webshop.entities.Guest;
import info.novatec.webshop.entities.OrderLine;
import info.novatec.webshop.entities.PurchaseOrder;
import info.novatec.webshop.persistence.AccountManager;
import info.novatec.webshop.persistence.BillManager;
import info.novatec.webshop.persistence.OrderLineManager;
import info.novatec.webshop.persistence.OrderManager;
import java.io.Serializable;
import java.security.Principal;
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
    private Bill bill = new Bill();
    private double totalPrice;
    private List<OrderLine> orderLines;
    private Account account;
    private CreditCard creditCard;
    @Inject
    private ShoppingCartBean shoppingcart;
    FacesContext fc = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
    private Principal principal;
    private Guest guest;
//    private boolean orderSucceeded = false;
//    private boolean billExists = false;
//    private boolean deliveryAddressExists = false;
//    private boolean billingAddressExists = false;
    private boolean accountExists = false;

    @PostConstruct
    public void init() {
        order = new PurchaseOrder();
        deliveryAddress = new Address();
        billingAddress = new Address();
        orderLines = shoppingcart.getOrderLines();
        totalPrice = calculateTotalPrice();
        creditCard = new CreditCard();
        principal = request.getUserPrincipal();
        guest = new Guest();
        if (request.getUserPrincipal() != null) {
            principal = request.getUserPrincipal();
            account = accountService.getAccountByEmail(principal.getName());
        }
        if (account != null) {
            deliveryAddress = accountService.getAddressByHomeAddress(true, account);
            billingAddress = accountService.getAddressByHomeAddress(true, account);
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
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
    

    public void prepareOrder() {
        LOGGER.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        Guest existingGuest = null;
        if (account == null) {
            existingGuest = (Guest) accountService.getAccountByEmail(guest.getEmail());
        }

        Address existingDeliveryAddress = null;
        Address existingBillingAddress;
        Bill existingBill;
        if (existingGuest != null) {
            guest = existingGuest;
            accountExists = true;
            LOGGER.info("ExistingGuest: " + existingGuest.getEmail());
        }
        if (account != null) {
            accountExists = true;
        }

        if (existingGuest != null) {
            existingDeliveryAddress = accountService.getAddressByStreetAndAccount(deliveryAddress.getStreet(), existingGuest);
        } else {
            existingDeliveryAddress = accountService.getAddressByStreetAndAccount(deliveryAddress.getStreet(), account);
        }

        if (existingDeliveryAddress != null) {
            deliveryAddress = existingDeliveryAddress;
            LOGGER.info("ExistingDeliveryAddress: " + existingDeliveryAddress.getCity());

        }
        if (checkboxValue) {
            if (existingGuest != null) {
                existingBillingAddress = accountService.getAddressByStreetAndAccount(deliveryAddress.getStreet(), existingGuest);
            } else {
                existingBillingAddress = accountService.getAddressByStreetAndAccount(deliveryAddress.getStreet(), account);
            }

        } else {
            if (existingGuest != null) {
                existingBillingAddress = accountService.getAddressByStreetAndAccount(billingAddress.getStreet(), existingGuest);
            } else {
                existingBillingAddress = accountService.getAddressByStreetAndAccount(billingAddress.getStreet(), account);
                LOGGER.info("IÂ´m HERE");
            }

        }
        if (existingBillingAddress != null) {
            billingAddress = existingBillingAddress;
            billingAddress.setIsHomeAddress(false);
            LOGGER.info("ExistingBillingAddress: " + existingBillingAddress.getCity());
            LOGGER.info("And HERE");
        }

        existingBill = billService.getBillByAccountNumberAndOwner(bill.getAccountNumber(), bill.getAccountOwner());
        if (existingBill != null) {
            bill = existingBill;
//                billExists = true;
            LOGGER.info("ExistingBill: " + existingBill.getAccountOwner());
        }

        if (existingGuest != null) {
            deliveryAddress.setAccount(guest);
            billingAddress.setAccount(guest);
        } else {
            deliveryAddress.setAccount(account);
            billingAddress.setAccount(account);
            LOGGER.info("The billingAddress is: " + billingAddress.getCity());
        }

        LocalDate orderDate = LocalDate.now();
        LOGGER.info(orderDate.toString());

        order.setOrderDate(orderDate);

        for (OrderLine ol : orderLines) {
            ol.setOrder(order);
        }

        if (existingGuest != null) {
            order.setAccount(guest);
        } else {
            order.setAccount(account);
        }

        order.setDeliveryAddress(deliveryAddress);
        if (checkboxValue) {
            order.setBillingAddress(deliveryAddress);
        } else {
            order.setBillingAddress(billingAddress);
        }
        List<Address> addresses = new ArrayList();
        addresses.add(order.getBillingAddress());
        if (existingGuest != null) {
              guest.setAddresses(addresses);
            
        }
      
       

        order.setTotalPrice(totalPrice);
        order.setOrderLines(orderLines);
        order.setBill(bill);
        LOGGER.info("THE ORDER HAS BEEN PREPARED!");

    }

    public String persistOrder() {

        LOGGER.info("ORDER ADDRESS: " + order.getBillingAddress().getStreet());
        boolean accountPersisted = false;
        boolean orderPersisted = false;
        if (order != null) {
            if (accountExists) {
                if (account != null) {
                    accountService.updateAccount(account);
                } else {
                    accountService.updateAccount(guest);
                }

                accountPersisted = true;
            } else {
                if (account != null) {
                    accountPersisted = accountService.createAccount(account);
                } else {
                    accountPersisted = accountService.createAccount(guest);
                }
            }

            orderPersisted = orderService.createOrder(order);
            LOGGER.info("THE ORDER HAS BEEN PERSISTED!");
            shoppingcart.clearCart();

        }

        if (accountPersisted && orderPersisted) {
            return "successfulOrder";
        }
        return null;
    }

}
