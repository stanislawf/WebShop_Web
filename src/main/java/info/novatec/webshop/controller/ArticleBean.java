/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.controller;

import info.novatec.webshop.entities.Article;
import info.novatec.webshop.persistence.ArticleManager;
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
@Named(value = "articleBean")
@RequestScoped
public class ArticleBean implements Serializable{
    
  private static final long serialVersionUID = 1L;
  
  @EJB
  private ArticleManager articleService;
  private List<Article> allArticles;
  private List<Article> randomArticles;
  
//  --> Nichtssagend. Ist das der selektierte? Oder eine Helper? Oder nur was zum Anzeigen? Bitte Namen ändern.
  private Article selectedArticle;
  
  @PostConstruct
  public void init() {
//      --> Warum werden hier ALLE Artikel der DB geladen???? Auf gar keinen Fall!
//               Damit wird Deine Anwendung nicht mehr skalierbar. Überlege mal,
//               was passieren würde, wenn Amazon bei Besuch der Seite ALLE
//               Artikel deren DB laden würde ;)
    allArticles = articleService.getAllArticles();
    
//    --> Würde das hier nicht reichen? Einfach 6 zufällige aus der DB holen?
//    randomArticles = getRandomArticles(allArticles, 6);
//    
    
//    for(Article randomArticle : randomArticles){
//        System.out.println(randomArticle.getName());
//    }
    selectedArticle = new Article();
  }

  public List<Article> getRandomArticles() {
    return randomArticles;
  }

  public void setRandomArticles(List<Article> randomArticles) {
    this.randomArticles = randomArticles;
  }

  public Article getSelectedArticle() {
    return selectedArticle;
  }

  public void setSelectedArticle(Article article) {
    this.selectedArticle = article;
  }


  public List<Article> getAllArticles() {
    return allArticles;
  }

  public void setAllArticles(List<Article> allArticles) {
    this.allArticles = allArticles;
  }
  
//  private List<Article> getRandomArticles(List<Article> allArticles, int numberOfElements){
//    List<Article> tempArticleList = allArticles;
//    Collections.shuffle(tempArticleList);
////    --> Test muss zu Begonn kommen. Falls allArticles empty ist, bekommst Du
////            nämlich schon bei Collections.shuffle(tempArticleList) eine
////            Exception ;)
//    if(allArticles.isEmpty()){
//        return null;
//    }
//    return tempArticleList.subList(0, numberOfElements);
//    
////    --> Könntest Dir überlegen, ob numberOfElements eine globale Variable wird.
////            Ist leichter konfigurierbar und Du musst es nicht passen
//  }
  
//  --> Bin mir nicht sicher, ob Du das hier nocht brauchst. Name lässt einen
//          Returnvalue vermuten. Bzw einen übergebenen Parameter
  public void findArticleByID(){
    selectedArticle = articleService.getArticleById(selectedArticle.getId());
//  System.err.println("Die ARticle-ID ist: " + article.getId() + " " + article.getCategories().size());
  }
  
  
}
