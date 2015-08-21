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
import info.novatec.webshop.enums.RoleType;
import info.novatec.webshop.helpers.PasswordEncryption;
import info.novatec.webshop.persistence.AccountManager;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author sf
 */
@Named(value = "registration")
@RequestScoped
public class RegistrationBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Date birthDate;

    @EJB
    private AccountManager accountService;

    private AccountUser accountUser;
    private Address address;
    private AccountRole role;

    @PostConstruct
    public void init() {
        accountUser = new AccountUser();
        address = new Address();
        role = new AccountRole();

    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public AccountUser getAccount() {
        return this.accountUser;
    }

    public void setAccount(AccountUser account) {
        this.accountUser = account;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String registered() {
        boolean accountPersisted = false;
        try {
            role = accountService.getRoleByRoleType(RoleType.User);
            List<AccountRole> roles = new ArrayList();
            if (role != null) {
                roles.add(role);
            } else {
                role.setRoleType(RoleType.User);
                roles.add(role);
            }

            AccountUser existentUser = (AccountUser) accountService.getAccountByEmail(accountUser.getEmail());

            if (existentUser == null) {
                accountUser.setBirthday(convertToLocaDate(birthDate));
                accountUser.setIsActive(true);
                String password = accountUser.getPassword();
                accountUser.setPassword(PasswordEncryption.securePassword(password));
                accountUser.setRoles(roles);
                address.setIsHomeAddress(true);
                List<Address> addresses = new ArrayList();
                addresses.add(address);
                accountUser.setAddresses(addresses);

                
                address.setAccount(accountUser);

                accountPersisted = accountService.createAccount(accountUser);
            }else{
                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('confirmDialog').show();");
            }
            if (accountPersisted) {
                return "success";
            }
        } catch (Exception exeption) {
            Logger.getLogger(RegistrationBean.class.getName()).log(Level.SEVERE, "The user couldn´t be registered", exeption);
        }
        return null;
    }

    public LocalDate convertToLocaDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public void getAccountInformationByID() {
        accountUser = (AccountUser) accountService.getAccountById(accountUser.getId());
        address = accountService.getAddressByHomeAddress(true, accountUser);
    }
}
