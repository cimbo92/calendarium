/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import HelpClasses.OverlappingException;
import entities.Event;
import entities.Invitation;
import entities.Place;
import entities.Preference;
import entities.User;
import entities.UserEvent;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author home
 */
@Stateless
@Remote(EventManagerInterface.class)
public class EventManager implements EventManagerInterface  {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;
    
    @Override
    public void addEvent(Event event) throws OverlappingException  {
        
        if(this.searchEventOverlapping(event))
        {
            throw new OverlappingException();
        }else{
        em.merge(event.getPlace());
        em.persist(event);
       }
        
        
  
    }
    
    @Override
       public boolean modifyEvent(int idEvent, String title, String Date, String startHour, String endHour, String description, Place place, boolean outdoor, List<Preference> preferences) {
           throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
       }

    @Override
    public boolean removeEvent(int idEvent) {
        
        try{
            Event event = em.find(Event.class,idEvent);
            
            
        }catch (Exception e){
            
        }
        
        return false;
    }

    @Override
    public Event loadSpecificEvent(int idEvent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Event> getAllUserEvent(String idUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Event> searchEventIndoorUser(String idUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Event> searchOutdoorEvent(String idUser) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    @Override
    public boolean searchEventOverlapping(Event event) {
        
  
        
        Timestamp startDate = event.getStartDate();
        Timestamp endDate = event.getEndDate();
       
        List<Event> listEvent;
      
        List<UserEvent> listUserEvent;
        
        //lista da controllare Overlapping con eventi creati
               
        try {
                        Query query = em
                                        .createQuery("SELECT e "
                                                        + "FROM Event e JOIN e.creator u "
                                                        + "WHERE u.email =:idUser");
                        
                  //ListEvent contiene gli eventi creati dall'utente
                  listEvent = (List<Event>) query.setParameter("idUser", principal.getName()).getResultList();
            
                 

                  
                  Timestamp tmp;
                  
                  // Get an iterator.
                  Iterator<Event> ite = listEvent.iterator();

                  while(ite.hasNext()){
                      
                      tmp = ite.next().getStartDate();
                      
                      if(!((tmp.getYear() == startDate.getYear()) &&
                           (tmp.getMonth() == startDate.getMonth()) &&
                           (tmp.getDate()== startDate.getDate())) ){
                      ite.remove();
                      }
                      
                  }
                        
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Errore query searchOverlapping");
                        return false;
                }
        
        //aggiungiamo eventuali eventi non creati dall'utente ma a cui esso partecipi
        
        
        
        
        try {
     
            Query query = em
                                        .createQuery("SELECT ue "
                                                        + "FROM UserEvent ue JOIN ue.user u "
                                                        + "WHERE (u.email =:idUser AND ue.accepted = true)");
            
            listUserEvent = (List<UserEvent>) query.setParameter("idUser",principal.getName()).getResultList();
            
            //Dalle lista di inviti aggiungo alla lista di eventi da controllare
            //quelli in cui l'utente abbia accettato un invito!
            
            Timestamp tmp;
            
            // Get an iterator.
            Iterator<UserEvent> ite = listUserEvent.iterator();
            
            while(ite.hasNext()){
                
                Event e = ite.next().getEvent();
                
                tmp = e.getStartDate();
                
                if(((tmp.getYear() == startDate.getYear()) &&
                           (tmp.getMonth() == startDate.getMonth()) &&
                           (tmp.getDate()== startDate.getDate()))){
                    listEvent.add(e);
                }
                
            }
            
            
        } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Errore query searchOverlapping");
                        return false;
                }
        
        for(Event e : listEvent){
            if(overlapping(e.getStartDate(),e.getEndDate(), startDate,endDate))
            return true;
            
        }
        
        return false;
    }
    
    public boolean overlapping(Timestamp beginFirst, Timestamp endFirst, Timestamp beginSecond, Timestamp endSecond){
        
        //Beginning in the same day
 
        if(overlappingLeft(beginFirst, endFirst, beginSecond, endSecond) ||
           overlappingCenter(beginFirst, endFirst, beginSecond, endSecond) ||
           overlappingRight(beginFirst, endFirst, beginSecond, endSecond) )
            return true;
        
        return false;
    }
    
     private boolean overlappingLeft(Timestamp startFirst, Timestamp endFirst, Timestamp startSecond, Timestamp endSecond){
 
         
       return  ( (startFirst.compareTo(startSecond) == 0) || (startFirst.compareTo(startSecond) > 0) ) &&
                (endSecond.compareTo(startFirst) > 0 );
         
         
    //  return  (startFirst >= startSecond && endSecond > startFirst);
    }
    
    private boolean overlappingCenter(Timestamp startFirst, Timestamp endFirst, Timestamp startSecond, Timestamp endSecond){
        
        
       return (  ( (startFirst.compareTo(startSecond) == 0) || (startFirst.compareTo(startSecond) < 0 ) )
               
               &&
               
                 ( (endFirst.compareTo(endSecond) == 0) || (endFirst.compareTo(endSecond) > 0 ) )
              );
        
       // return (startFirst <= startSecond && endFirst >= endSecond);
    }
    
    private boolean overlappingRight(Timestamp startFirst, Timestamp endFirst, Timestamp startSecond, Timestamp endSecond){
        
        return ( ((startFirst.compareTo(startSecond)==0) || ((startFirst.compareTo(startSecond) < 0))) && (endFirst.compareTo(startSecond) > 0) );
        
      //  return startFirst <= startSecond && endFirst > startSecond;
    }

    @Override
    public List<User> getInvitedUsers(Event event) {
       
        /*
        
        List<Invitation> listInvitation = null;
        List<User> listUser = null;
        
        
      try{
        Query query = em.createQuery("SELECT i " +
                                                                                        "FROM Invitation i JOIN i.event e " +
                                                                                        "WHERE e.idEvent = : idEvent");
        
        listInvitation = (List<Invitation>) (Invitation) query.setParameter("idEvent", event.getIdEvent()).getResultList();
           
    }catch (Exception e){
        System.out.println("Errore nella query getInvitedUsers");
    }
      
      for(Invitation i : listInvitation){
          listUser.add(i.getOwner());
      }
     
      return listUser;
                
                */
        return null;
    }

    @Override
    public List<Event> findInvitatedEvent(User user) {
        
    //Query query = em
    //                                    .createQuery("SELECT DISTINCT e FROM Event AS e JOIN UserEvent AS ue WHERE ue.user= :user AND ue.creator=0").setParameter("user", user);
    
    Query query1 = em.createQuery("SELECT ue.event FROM UserEvent ue where ue.user = :user and ue.creator=0").setParameter(("user"), user);
    
    List<Event> tempSet = new ArrayList<>(query1.getResultList());
    
  
    return (List)tempSet;
    }

    @Override
    public List<Event> loadCalendar(User user) {
        
    Query query1 = em.createQuery("SELECT ue.event FROM UserEvent ue where ue.user = :user").setParameter(("user"), user);
    List<Event> tempSet = new ArrayList<>(query1.getResultList());
  
    return (List)tempSet;
    }
    
}