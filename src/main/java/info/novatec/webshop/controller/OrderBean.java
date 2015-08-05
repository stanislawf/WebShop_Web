/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author sf
 */
@Named(value = "orderBean")
@RequestScoped
public class OrderBean {

   @PostConstruct
   public void init(){
       
   }
    
   
   public String guest(){
       return "guest";
   }
   
   public String login(){
       return "login";
   }
   
}
