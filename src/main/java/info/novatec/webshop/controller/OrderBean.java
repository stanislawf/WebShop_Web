/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Account;
import info.novatec.webshop.entities.Address;
import info.novatec.webshop.entities.Bill;
import info.novatec.webshop.entities.CreditCard;
import info.novatec.webshop.entities.OrderLine;
import info.novatec.webshop.entities.Orders;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author sf
 */
@Named(value = "orderBean")
@SessionScoped
public class OrderBean implements Serializable {

    private Orders order = new Orders();
    private Address deliveryAddress = new Address();
    private Address billingAddress = new Address();
    private Bill bill = new Bill();
    private Date orderDate;
    private double totalPrice;
    private List<OrderLine> orderLines;
    private Account account = new Account();
    private CreditCard creditCard = new CreditCard();
    private boolean same = false;
    
    

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        orderLines = (List<OrderLine>) sessionMap.get("cart");
        
        for(OrderLine ol: orderLines){
            System.out.println("Die Quantität ist: " + ol.getQuantity());
        }
    } 

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
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

    public double getTotalPrice(){
        double price = 0;
        for(OrderLine ol : orderLines){
            price += (ol.getQuantity() * ol.getArticle().getPrice());
        }
        return price;
    }
}
