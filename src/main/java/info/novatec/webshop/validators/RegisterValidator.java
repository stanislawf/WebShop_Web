/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.validators;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author stanislawfreund
 */
@Named(value = "registerValidator")
@RequestScoped
public class RegisterValidator {

    /**
     * Creates a new instance of RegisterValidator
     */
    public RegisterValidator() {
    }

    public void nameValidation(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String message = "";
        String name = value.toString();
        if (name.length() < 3) {
            throw new ValidatorException(new FacesMessage("Please provide a valid Name!"));
        }
    }

    public void passwordValidation(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String message = "";
        String password = value.toString();
        if (password.length() < 3) {
            throw new ValidatorException(new FacesMessage("Please provide a password which is longer than 3"));
        }
    }

    public void mailValidation(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        String mail = value.toString();
        String messsage = "";
        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (!mail.matches(pattern)) {
            throw new ValidatorException(new FacesMessage("Invalid E-Mail"));
        }
    }
}
