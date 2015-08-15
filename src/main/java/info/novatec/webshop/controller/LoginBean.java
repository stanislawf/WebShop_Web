/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Account;
import info.novatec.webshop.entities.AccountUser;
import info.novatec.webshop.helpers.LoadArticleProperties;
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
public class LoginBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Account account;
    private String password, email;
    @EJB
    private AccountManager accountService;
    private boolean loggedIn;

    @PostConstruct
    public void init() {
        account = new AccountUser();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
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

    public void login() throws ServletException {
        account = accountService.getAccountByEmail(email);

        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();

//        --> email und passwort müssen umbedingt getestet werden!! request kann null- values erhalten!
//            if (email.equalsIgnoreCase(account.getEmail()) && password.equals(account.getPassword())) {
                request.login(email, password);
                
//                Principal principal = request.getUserPrincipal();
    }

//    --> Getestet? Dürfte nicht funktionieren ;)!
    public void logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        try {
            request.logout();
        } catch (ServletException exception) {
           Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, exception);
        }
    }

}
