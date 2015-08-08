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
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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

    private Orders order;
    private Address deliveryAddress;
    private Address billingAddress;
    private Bill bill = new Bill();
    private Date orderDate;
    private double totalPrice;
    private List<OrderLine> orderLines;
    private Account account;
    private CreditCard creditCard;
    private boolean same = false;
    private boolean confirmedDeliveryAddress = false;
    private boolean checked = false;
    private boolean disable;
    private boolean rendered;

    @PostConstruct
    public void init() {
        order = new Orders();
        deliveryAddress = new Address();
        billingAddress = new Address();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        orderLines = (List<OrderLine>) sessionMap.get("cart");
        totalPrice = calculateTotalPrice();
        creditCard = new CreditCard();
        account = new Account();
        isTabEnabled = false;
       
    }
    
    

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isConfirmedDeliveryAddress() {
        return confirmedDeliveryAddress;
    }

    public void setConfirmedDeliveryAddress(boolean confirmedDeliveryAddress) {
        this.confirmedDeliveryAddress = confirmedDeliveryAddress;
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

    public double calculateTotalPrice() {
        double price = 0;
        for (OrderLine ol : orderLines) {
            price += (ol.getQuantity() * ol.getArticle().getPrice());
        }
        return price;
    }

    public void adaptAddresses() {
        if (same) {
            billingAddress = deliveryAddress;
            System.out.println("deliveryAddress: " + deliveryAddress.getFirstName());
        } else {
            billingAddress = new Address();
        }
        System.out.println(deliveryAddress.getFirstName());
    }

    private boolean disableTab = true;

    public boolean isDisableTab() {
        return disableTab;
    }

    public void setDisableTab(boolean disableTab) {
        this.disableTab = disableTab;
    }

    private boolean isTabEnabled;

    public boolean isIsTabEnabled() {
        return isTabEnabled;
    }

    public void setIsTabEnabled(boolean isTabEnabled) {
        this.isTabEnabled = isTabEnabled;
    }

    //new
    private boolean mostrar = false;

    public boolean isMostrar() {
        return mostrar;
    }

    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    private String claveEtiqueta;

    public String getClaveEtiqueta() {
        return claveEtiqueta;
    }

    public void setClaveEtiqueta(String claveEtiqueta) {
        this.claveEtiqueta = claveEtiqueta;
    }

    public void mostrarOcultar() {
        if (isInvalidarEtiqueta()) {
            setMostrar(true);
        } else {
            setMostrar(false);
        }
        System.out.println("inv: " + isInvalidarEtiqueta());
        System.out.println("most: " + isMostrar());
    }

    private boolean invalidarEtiqueta;

    public boolean isInvalidarEtiqueta() {
        return invalidarEtiqueta;
    }

    public void setInvalidarEtiqueta(boolean invalidarEtiqueta) {
        this.invalidarEtiqueta = invalidarEtiqueta;
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
        System.out.println("The old values are:");
        System.out.println("checkboxValue: " + checkboxValue);
        System.out.println("renderValue: " + renderValue);
        
        checkboxValue = (boolean) e.getNewValue();
        
        if(isCheckboxValue() == true){
            setRenderValue(true);
        }else{
           setRenderValue(false);
        }
        
        System.out.println("The new values are:");
        System.out.println("checkboxValue: " + checkboxValue);
        System.out.println("renderValue: " + renderValue);
    }

}
