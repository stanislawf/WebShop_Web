/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Article;
import info.novatec.webshop.entities.Category;
import info.novatec.webshop.persistence.CategoryManager;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author stanislawfreund
 */
@Named(value = "categoryBean")
@RequestScoped
public class CategoryBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CategoryManager categoryService;
    private Category category;
    private List<Category> allCategories;
    private List<Article> articlesByCategory;

    public List<Article> getArticlesByCategory() {
        return articlesByCategory;
    }

    public void setArticlesByCategory(List<Article> articlesByCategory) {
        this.articlesByCategory = articlesByCategory;
    }

    @PostConstruct
    public void init() {
        allCategories = categoryService.getAllCategories();
        category = new Category();
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void findselectedCategory() {
        for(Category cat : allCategories){
            if(cat.getId().equals(category.getId())){
                category = cat;
            }
        }
        articlesByCategory = findArticlesForCategory();
    }
    
    private List<Article> findArticlesForCategory() {
        return categoryService.getAllArticleByCategoryName(category.getName());
    }

    public String showCategoryArticles() {
        String result = null;
        if (category.getId() != null) {
            result =  "show";
        }
        return result;
    }

}
