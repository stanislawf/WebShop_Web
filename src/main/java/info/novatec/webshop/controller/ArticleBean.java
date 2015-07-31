/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Article;
import info.novatec.webshop.persistence.ArticleManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author stanislawfreund
 */
@Named(value = "articleBean")
@RequestScoped
public class ArticleBean implements Serializable{

  /**
   * Creates a new instance of ArticleBean
   */
  public ArticleBean() {
  }
  
  @EJB
  private ArticleManager articleService;
  private List<Article> allArticles;
  private List<Article> randomArticles;
  private Article article;
  
  @PostConstruct
  public void init() {
    allArticles = articleService.getAllArticles();
    randomArticles = getRandomArticles(allArticles, 6);
//    for(Article randomArticle : randomArticles){
//        System.out.println(randomArticle.getName());
//    }
    article = new Article();
  }

  public List<Article> getRandomArticles() {
    return randomArticles;
  }

  public void setRandomArticles(List<Article> randomArticles) {
    this.randomArticles = randomArticles;
  }



  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }


  public List<Article> getAllArticles() {
    return allArticles;
  }

  public void setAllArticles(List<Article> allArticles) {
    this.allArticles = allArticles;
  }
  
  private List<Article> getRandomArticles(List<Article> allArticles, int numberOfElements){
    List<Article> tempArticleList = allArticles;
    Collections.shuffle(tempArticleList);
    if(allArticles.isEmpty()){
        return null;
    }
    return tempArticleList.subList(0, numberOfElements);
  }
  
  public void findArticleByID(){
    article = articleService.getArticleById(article.getId());
//  System.err.println("Die ARticle-ID ist: " + article.getId() + " " + article.getCategories().size());
  }
  
  
}
