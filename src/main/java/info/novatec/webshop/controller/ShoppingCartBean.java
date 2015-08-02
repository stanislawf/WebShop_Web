/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Article;
import info.novatec.webshop.entities.OrderLine;
import info.novatec.webshop.persistence.ArticleManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author sf
 */
@Named(value = "cartBean")
@SessionScoped
public class ShoppingCartBean implements Serializable{
    
    private List<OrderLine> orderLines;
    private OrderLine orderLine;
    private Long articleID;
    Article article;
    Long orderLineID;
    
    @EJB
    private ArticleManager articleService;
    
    @PostConstruct
    public void init(){
        orderLines = new ArrayList();
      
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
    
    public int getCartSize(){
        return orderLines.size();
    }
    
    private boolean checkIfOrderLinesContainsArticle(){
         for(OrderLine ol : orderLines){
            if(ol.getArticle().getId().equals(article.getId())){
                return true;
            }
        }
        return false;
    }
    
    private int getOrderLineIndexByArticleID(){
        for(int i= 0; i<orderLines.size(); i++){
            if(orderLines.get(i).getArticle().getId().equals(article.getId())){
                return i;
            }
        }
        return -1;
    }
    
    public void addArticleToCart(){
        System.out.println("ArticleID is:" + articleID);
        if(checkIfOrderLinesContainsArticle()){
            int index = getOrderLineIndexByArticleID();
            orderLine = orderLines.get(index);
            orderLine.setQuantity((byte)(orderLine.getQuantity() + 1));
            orderLines.set(index, orderLine);
        System.out.println("I´m old");
        }else{
            orderLine = new OrderLine();
        orderLine.setArticle(article);
        orderLine.setQuantity(Byte.valueOf("1"));
        orderLines.add(orderLine);
        System.out.println("I´m new");
        }
        
        
    }
    
    public void orderLineIDActionListener(Long id){
        this.orderLineID = id;
        System.out.println(orderLineID);
         System.out.println("actionListener 2");
  }
    
    public void removeArticleFromCart(){
        System.out.println("1 is here");
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params =  fc.getExternalContext().getRequestParameterMap();
        
        System.out.println("Test IS: " + params.get("orderLineID"));
        System.out.println("2 is here");
        Long id = Long.parseLong(params.get("orderLineID"));
        for(OrderLine ol: orderLines){
            if(ol.getId().equals(orderLineID)){
                
                orderLines.remove(ol);
            }
        }
    }
    
    public void increaseArticleQuantity(){
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params =  fc.getExternalContext().getRequestParameterMap();
        Long id = Long.getLong(params.get("orderLineID"));
        System.out.println("OrderLineArticleID: " + id);
        for(OrderLine ol: orderLines){
            if(ol.getId().equals(id)){
                System.out.println("Quantity before is: " + ol.getQuantity());
                System.out.println("Article ID is: "+ ol.getArticle().getId());
                byte b = (byte) ol.getQuantity();
                b = (byte) (b+1);
                ol.setQuantity(b);
                   System.out.println("Quantity after is: " + ol.getQuantity());
            }
        }
    }
    
     public void decreaseArticleQuantity(){
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params =  fc.getExternalContext().getRequestParameterMap();
        Long id = Long.getLong(params.get("orderLineID"));
         System.out.println("OrderLineArticleID: " + id);
        for(OrderLine ol: orderLines){
            if(ol.getId().equals(id)){
                System.out.println("Article ID is: "+ ol.getArticle().getId());
                 byte b = (byte) ol.getQuantity();
                b = (byte) (b-1);
                ol.setQuantity(b);
            }
        }
    }
    
    
    
//    public void addToCartListener(ActionEvent event){
//        Long s = (Long) event.getComponent().getAttributes().get("articleIDAction");
//        System.out.println("Test: " + s);
//        articleID = s;
//    }
    
    public void setArticleID(Long id){
        this.articleID = id;
    }
    
    public Long getArticleID(){
        return this.articleID;
    }
    
    public void findArticle(){
       article = articleService.getArticleById(articleID);
    }
    
    public void clearCart(){
        orderLines.clear();
    }
}
