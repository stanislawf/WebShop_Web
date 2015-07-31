/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.novatec.webshop.test.dataGeneration;

import info.novatec.webshop.entities.Account;
import info.novatec.webshop.entities.Address;
import info.novatec.webshop.entities.Article;
import info.novatec.webshop.entities.Bill;
import info.novatec.webshop.entities.Category;
import info.novatec.webshop.entities.CreditCard;
import info.novatec.webshop.entities.OrderLine;
import info.novatec.webshop.entities.Orders;
import info.novatec.webshop.entities.Role;
import info.novatec.webshop.helpers.LoadArticleProperties;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author sf
 */
public class DataGeneration {

  
    
    @Test
    public void testCreateRole(){
          EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
            EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        Role roleUser = new Role();
        roleUser.setRoleType("User");
        
        Role roleAdmin = new Role();
        roleAdmin.setRoleType("Admin");
        
        em.persist(roleUser);
        em.persist(roleAdmin);
        
        assertTrue(em.contains(roleAdmin));
        assertTrue(em.contains(roleUser));
        
         
        em.getTransaction().commit();
        em.close();
    }
    
    
//    @Test
//    public void testCreateAccountWithRole() {
//         EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
//         EntityManager em = emf.createEntityManager();
//      
//        em.getTransaction().begin();
//        Role role = (Role) em.createNamedQuery("Role.findRoleByString").setParameter("roleType", "User").getSingleResult();
//
//        List<Role> accountRoles = new ArrayList();
//        accountRoles.add(role);
//        
//        Role roleAdmin = (Role) em.createNamedQuery("Role.findRoleByString").setParameter("roleType", "Admin").getSingleResult();
//
//       
//        accountRoles.add(roleAdmin);
//        
//        Account account = new Account();
//        account.setFirstName("Stanislaw");
//        account.setLastName("Freund");
//        account.setPhoneNumber("0172/3607116");
//        account.setEmail("stas.2HG@gmx.net");
//        account.setPassword("09876543210");
//        String inputStr = "25-06-1987";
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Date inputDate = null;
//        try {
//            inputDate = dateFormat.parse(inputStr);
//        } catch (ParseException ex) {
//            System.err.println("An error occured while creating the role and the account!");
//            Logger.getLogger(DataGeneration.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        account.setBirthday(inputDate);
//        account.setIsActive(true);
//        account.setRoles(accountRoles);
//        
//        Address address = new Address();
//        address.setFirstName(account.getFirstName());
//        address.setLastName(account.getLastName());
//        address.setStreet("Teststraße 12");
//        address.setCity("Winnenden");
//        address.setZipCode("71364");
//        address.setCountry("Deutschland");
//        address.setAccount(account);
//        
//        List<Address> addresses = new ArrayList();
//        addresses.add(address);
//
//        account.setHomeAddress(addresses);
//
//        em.persist(account);
//        em.persist(address);
//        assertTrue(em.contains(account));
//        assertTrue(em.contains(address));
//        
//        
//        
//        
//          //---------------------------------------------
//        //              Categories
//        //---------------------------------------------
//       
//        
//        
//        //Smartphones
//        Category categorySmartphone = new Category();
//        categorySmartphone.setName("Smartphones");
//
//        em.persist(categorySmartphone);
//        assertTrue(em.contains(categorySmartphone));
//        
//        //Notebooks
//        Category categoryNotebooks = new Category();
//        categoryNotebooks.setName("Notebooks");
//        
//        em.persist(categoryNotebooks);
//        assertTrue(em.contains(categoryNotebooks));
//
//        //Cameras
//        Category categoryCameras = new Category();
//        categoryCameras.setName("Cameras");
//        
//        em.persist(categoryCameras);
//        assertTrue(em.contains(categoryCameras));
//
//        //Television
//        Category categoryTelevision = new Category();
//        categoryTelevision.setName("Television");
//        
//        em.persist(categoryTelevision);
//        assertTrue(em.contains(categoryTelevision));
//
//        //---------------------------------------------
//        //              Articles
//        //---------------------------------------------
//        
//         List<Article> articles = LoadArticleProperties.loadArticlePropertiesFromSystem();
//        assertThat(articles.size(), is(21));
//        Category category;
//        for (Article article : articles) {
//            category = (Category) em.createNamedQuery("Category.findCategoryByName").setParameter("name", article.getCategories().get(0).getName()).getSingleResult();
//            if (category == null) {
//                category = new Category();
//                category.setName(article.getCategories().get(0).getName());
//                List<Article> categoryArticles = new ArrayList();
//                categoryArticles.add(article);
//                category.setArticles(categoryArticles);
//                em.persist(category);
//                assertTrue(em.contains(category));
//                em.persist(article);
//                assertTrue(em.contains(article));
//            } else {
//                List<Article> categoryArticles = new ArrayList();
//                categoryArticles.add(article);
//                category.setArticles(categoryArticles);
//                em.merge(category);
//                assertTrue(em.contains(category));
//                em.persist(article);
//                assertTrue(em.contains(article));
//            }
//
//        }
//        
//        
//        //---------------------------------------------
//        //              Orders
//        //---------------------------------------------
//        
//
//        Orders order = new Orders();
//        
//        String inputStr2 = "25-06-1987";
//        DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
//        Date orderDate = null;
//        try {
//            orderDate = dateFormat.parse(inputStr);
//        } catch (ParseException ex) {
//            System.err.println("An error occured while creating the role and the account!");
//            Logger.getLogger(DataGeneration.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        order.setOrderDate(orderDate);
//        order.setAccount(account);
//        order.setDeliveryAddress(address);
//        order.setBillingAddress(address);
//        
//             
//        Bill bill = new Bill();
//        bill.setAccountNumber("123456478");
//        bill.setAccountOwner(account.getFirstName() + " " + account.getLastName());
//        bill.setBankCode("6206543031");
//        bill.setBankName("Bank A");
//        em.persist(bill);
//        assertTrue(em.contains(bill));
//        
//        order.setBill(bill);
//        
//        OrderLine orderLine = new OrderLine();
//        orderLine.setArticle(articles.get(0));
//        orderLine.setOrder(order);
//        orderLine.setQuantity(Byte.valueOf("3"));
//        em.persist(orderLine);
//        assertTrue(em.contains(orderLine));
//        
//        List<OrderLine> orderLines = new ArrayList();
//        orderLines.add(orderLine);
//        order.setOrderLines(orderLines);
//        
//        order.setTotalPrice(orderLines);
//        em.persist(order);
//        assertTrue(em.contains(order));
//        
//        em.getTransaction().commit();
//        em.close();
//    }
//    
}
