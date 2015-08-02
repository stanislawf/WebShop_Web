/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Account;
import info.novatec.webshop.persistence.AccountManager;
import java.io.Serializable;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sf
 */
@Named(value = "loginBean")
@RequestScoped
public class LoginBean implements Serializable{

    private Account account = new Account();
    private String password, email;
    @EJB
    private AccountManager accountService;
    
    @PostConstruct
    public void init(){
        account = new Account();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void test(){
        System.out.println("IT WOKRED");
       
         FacesContext fc = FacesContext.getCurrentInstance();
       
           HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
      System.out.println("Der Account ist: " + request.getUserPrincipal().getName());
    }
    
    public void login(){
       
       
    }
    
      public void logout() {
    
    }
    
}
