/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Account;
import info.novatec.webshop.entities.Address;
import info.novatec.webshop.entities.AccountRole;
import info.novatec.webshop.entities.AccountUser;
import info.novatec.webshop.persistence.AccountManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author sf
 */
@Named(value = "registration")
@RequestScoped
public class RegistrationBean implements Serializable {
    
  private static final long serialVersionUID = 1L;

  @EJB
  private AccountManager accountService;

  private Account account;
  private Address address;
  private AccountRole role;
  
//Überprüfen ob bereits ein Account existiert.
//
  @PostConstruct
  public void init() {
    account = new AccountUser();
    address = new Address();
    role = new AccountRole();
    
  }

  public AccountRole getRole() {
    return role;
  }

  public void setRole(AccountRole role) {
    this.role = role;
  }

  public Account getAccount() {
    return this.account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String registered() {

//    try {
//      role = accountService.getRoleByRoleType("User");
//      List<AccountRole> roles = new ArrayList<>();
//      roles.add(role);
//      account.s(roles);
//      boolean accountPersisted = accountService.createAccount(account);
//      address.setFirstName(account.getFirstName());
//      address.setLastName(account.getLastName());
//      address.setAccount(account);
//      boolean addressPersisted = accountService.createAddress(address);
//      if(accountPersisted == true && addressPersisted == true){
//         return "success";
//      } 
//    } catch (Exception e) {
//      System.err.println("The user could not be registered");
//      System.err.println(e.getMessage());
//    }
    return null;
  }


  
  public void getAccountInformationByID(){
    account =  accountService.getAccountById(account.getId());
    List<Account> accounts = new ArrayList();
    accounts.add(account);
    address = accountService.getAddressByHomeAddress(true, accounts);
  }
}