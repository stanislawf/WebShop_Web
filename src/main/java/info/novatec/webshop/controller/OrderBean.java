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
import info.novatec.webshop.entities.OrderLine;
import info.novatec.webshop.entities.PurchaseOrder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author sf
 */
@Named(value = "orderBean")
@RequestScoped
public class OrderBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private PurchaseOrder order;
    private Address deliveryAddress;
    private Address billingAddress;
    private Bill bill = new Bill();
    private double totalPrice;
    private List<OrderLine> orderLines;
    private Account account;
    private CreditCard creditCard;
  

    @PostConstruct
    public void init() {
        order = new PurchaseOrder();
        deliveryAddress = new Address();
        billingAddress = new Address();
//        --> Was, wenn sessionMap leer ist oder cart nicht enthÃ¤lt??
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        orderLines = (List<OrderLine>) sessionMap.get("cart");
        totalPrice = calculateTotalPrice();
        creditCard = new CreditCard();
        account = new AccountUser();
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
        
        if(isCheckboxValue()){
            setRenderValue(true);
        }else{
           setRenderValue(false);
        }
    }
    
    public void prepareOrder(){
        order.setDeliveryAddress(deliveryAddress);
        if(checkboxValue){
            order.setBillingAddress(deliveryAddress);
        }else{
            order.setBillingAddress(billingAddress);
        }
        order.setTotalPrice(totalPrice);
        order.setOrderLines(orderLines);
        order.setBill(bill);
        

        
    }

}
