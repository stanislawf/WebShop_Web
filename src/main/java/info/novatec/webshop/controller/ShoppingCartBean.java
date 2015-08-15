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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author sf
 */
@Named(value = "cartBean")
@SessionScoped
public class ShoppingCartBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private List<OrderLine> orderLines;
    private OrderLine orderLine;
    private Long articleID;
    Article article;
    Long orderLineID;

    @EJB
    private ArticleManager articleService;

    @PostConstruct
    public void init() {
        orderLines = new ArrayList();

    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public int getCartSize() {
        return orderLines.size();
    }

//    --> s bei contains zu viel :P
    private boolean checkIfOrderLinesContainsArticle() {
        for (OrderLine ol : orderLines) {
            if (ol.getArticle().getId().equals(article.getId())) {
                return true;
            }
        }
        return false;
    }

    private int getOrderLineIndexByArticleID() {
        for (int i = 0; i < orderLines.size(); i++) {
            if (orderLines.get(i).getArticle().getId().equals(article.getId())) {
                return i;
            }
        }

//        --> Wird auf -1 als Result reagiert?
        return -1;
    }

    public void addArticleToCart() {
        if (checkIfOrderLinesContainsArticle()) {
            int index = getOrderLineIndexByArticleID();
            orderLine = orderLines.get(index);
            orderLine.setQuantity((byte) (orderLine.getQuantity() + 1));
            orderLines.set(index, orderLine);
        } else {
            orderLine = new OrderLine();
            orderLine.setArticle(article);
            orderLine.setQuantity(Byte.valueOf("1"));
            orderLines.add(orderLine);
        }
    }

    public void removeArticleFromCart() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = (Map<String, String>) fc.getExternalContext().getRequestParameterMap();
        orderLineID = Long.parseLong(params.get("orderLineID"));

        int indexToDelete = -1;
        for (int i = 0; i < orderLines.size(); i++) {
            if (orderLines.get(i).getArticle().getId().equals(orderLineID)) {
                indexToDelete = i;
            }
        }
        orderLines.remove(indexToDelete);
    }

    public void increaseArticleQuantity() {
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        orderLineID = Long.parseLong(params.get("orderLineID"));
        for (OrderLine ol : orderLines) {
//            --> Du kannst hier mit dem Vergleich eine NPE bekommen. Bitte Null-safe testen
            if (orderLineID == ol.getArticle().getId()) {
                byte b = (byte) ol.getQuantity();
                b = (byte) (b + 1);
                ol.setQuantity(b);
            }
        }
    }

    public void decreaseArticleQuantity() {
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        orderLineID = Long.parseLong(params.get("orderLineID"));
        for (OrderLine ol : orderLines) {
//            --> Du kannst hier mit dem Vergleich eine NPE bekommen. Bitte Null-safe testen
            if (orderLineID == ol.getArticle().getId()) {
                byte b = (byte) ol.getQuantity();
                b = (byte) (b - 1);
                ol.setQuantity(b);
            }
        }
    }

//     --> ?? JavaDoc
    public String guest() {
        setSessionMap();
        return "guest";
    }

    public void setSessionMap() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("cart", orderLines);
    }

    public void setArticleID(Long id) {
        this.articleID = id;
    }

    public Long getArticleID() {
        return this.articleID;
    }

    public void findArticle() {
        article = articleService.getArticleById(articleID);
    }

    public void clearCart() {
        orderLines.clear();
    }
}
