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
    private Long selectedArticleID;

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

    private boolean checkIfOrderLinesContainArticle() {
        for (OrderLine ol : orderLines) {
            if (ol.getArticle().getId().equals(article.getId())) {
                return true;
            }
        }
        return false;
    }

    private int findOrderLineIndexById(Long id) {
        int result = -1;
        for (int i = 0; i < orderLines.size(); i++) {
            if (orderLines.get(i).getArticle().getId().equals(id)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void addArticleToCart() {
        if (checkIfOrderLinesContainArticle()) {
            int index = findOrderLineIndexById(article.getId());
            orderLine = orderLines.get(index);
            orderLine.setQuantity((orderLine.getQuantity() + 1));
            orderLines.set(index, orderLine);
        } else {
            orderLine = new OrderLine();
            orderLine.setArticle(article);
            orderLine.setQuantity(1);
            orderLines.add(orderLine);
        }
    }

    public void removeArticleFromCart() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params = (Map<String, String>) fc.getExternalContext().getRequestParameterMap();
        selectedArticleID = Long.parseLong(params.get("articleID"));
        int indexToDelete = -1;
        if (selectedArticleID != null) {
            indexToDelete = findOrderLineIndexById(selectedArticleID);
        }
        orderLines.remove(indexToDelete);
    }

    public void increaseArticleQuantity() {
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        selectedArticleID = Long.parseLong(params.get("articleID"));
        int indexToDelete = -1;
        if (selectedArticleID != null) {
            indexToDelete = findOrderLineIndexById(selectedArticleID);
        }
        orderLines.get(indexToDelete).setQuantity(orderLines.get(indexToDelete).getQuantity()+1);
    }

    public void decreaseArticleQuantity() {
        Map<String, String> params;
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        selectedArticleID = Long.parseLong(params.get("articleID"));
          int indexToDelete = -1;
        if (selectedArticleID != null) {
            indexToDelete = findOrderLineIndexById(selectedArticleID);
        }
        orderLines.get(indexToDelete).setQuantity(orderLines.get(indexToDelete).getQuantity()-1);
    }

    public String changeToOrderPage() {
        setSessionMap();
        return "orderPage";
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
