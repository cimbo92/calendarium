/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import HelpClasses.OverlappingException;
import entities.Event;
import entities.Preference;

import entities.User;
import entities.UserEvent;
import entities.iDEvent;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
    public void addEvent(Event event, User user) throws OverlappingException  {
        
        
        
        
        if(this.searchEventOverlapping(event,user))
        {
            throw new OverlappingException();
        }else{
        em.merge(event.getPlace());
        em.persist(event.getIdEvent());
        em.persist(event);
       }
        
        
  
    }

    @Override
    public void removeEvent(Event event) {
        
        Query query1 = em.createQuery("Delete From Preference p Where p.event.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query1.executeUpdate();
        Query query2 = em.createQuery("Delete From UserEvent ue Where ue.event.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query2.executeUpdate();
        Query query3 = em.createQuery("Delete From Event e Where e.idEvent.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query3.executeUpdate();
        Query query4 = em.createQuery("Delete From iDEvent e Where e.id= :event").setParameter(("event"), event.getIdEvent().getId());
        query4.executeUpdate();
    }

    /**
     *
     * @param iDEvent
     * @return
     */
    @Override
    public Event loadSpecificEvent(String iDEvent) {
       iDEvent id = new iDEvent();
       Long idLong=Long.parseLong(iDEvent);
       id.setId(idLong);
                    Query query = em.createQuery("SELECT e FROM Event e WHERE e.idEvent =:id").setParameter("id", id);
                  List<Event> result = new ArrayList<>(query.getResultList());    
  Query query2 = em.createQuery("SELECT id FROM iDEvent id WHERE id.id =:id").setParameter("id", id.getId());
                  List<iDEvent> result2 = new ArrayList<>(query2.getResultList());
                  
                  result.get(0).setIdEvent(result2.get(0));
       return result.get(0);    
    }

    
    
    
    @Override
    public boolean searchEventOverlapping(Event event,User user) {
        
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
                  listEvent = (List<Event>) query.setParameter("idUser", user.getEmail()).getResultList();
            
                 

                  
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
            
            listUserEvent = (List<UserEvent>) query.setParameter("idUser",user.getEmail()).getResultList();
            
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
    public List<Event> findInvitatedEvent(User user) {
        
    Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE ue.user = :user AND ue.creator=false and ue.view=false").setParameter(("user"), user);
    List<Event> tempSet = new ArrayList<>(query.getResultList());

  
    return (List)tempSet;
    }

    @Override
    public List<Event> loadCalendar(User user) {
            
    Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE (ue.user = :user AND (ue.accepted=true OR ue.creator=true))").setParameter(("user"), user);
    List<Event> tempSet = new ArrayList<>(query.getResultList());

    Query query2 = em.createQuery("SELECT ue.event.idEvent FROM UserEvent ue WHERE (ue.user = :user AND (ue.accepted=true OR ue.creator=true))").setParameter(("user"), user);

    List<iDEvent> lista = new ArrayList<>(query2.getResultList());
   
    for(int i =0;i<tempSet.size();i++)
    {
        tempSet.get(i).setIdEvent(lista.get(i));
    }

    return (List)tempSet;
    }
    
    @Override
    public List<Event> loadPublicCalendar(String username) {
            
    Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE (ue.user.email = :user AND (ue.accepted=true OR ue.creator=true) AND ue.event.publicEvent=true)").setParameter(("user"), username);
    List<Event> tempSet = new ArrayList<>(query.getResultList());

    Query query2 = em.createQuery("SELECT ue.event.idEvent FROM UserEvent ue WHERE (ue.user.email = :user AND (ue.accepted=true OR ue.creator=true) AND ue.event.publicEvent=true)").setParameter(("user"), username);

    List<iDEvent> lista = new ArrayList<>(query2.getResultList());
   
    for(int i =0;i<tempSet.size();i++)
    {
        tempSet.get(i).setIdEvent(lista.get(i));
    }

    return (List)tempSet;
    }

    @Override
    public List<Event> loadPublicCalendar(User user) {
      Query query = em.createQuery("SELECT ue.event FROM UserEvent ue WHERE (ue.user = :user AND (ue.accepted=true OR ue.creator=true) AND ue.event.publicEvent=true)").setParameter(("user"), user);
    List<Event> tempSet = new ArrayList<>(query.getResultList());

    Query query2 = em.createQuery("SELECT ue.event.idEvent FROM UserEvent ue WHERE (ue.user = :user AND (ue.accepted=true OR ue.creator=true) AND ue.event.publicEvent=true)").setParameter(("user"), user);

    List<iDEvent> lista = new ArrayList<>(query2.getResultList());
   
    for(int i =0;i<tempSet.size();i++)
    {
        tempSet.get(i).setIdEvent(lista.get(i));
    }

    return (List)tempSet;}

    @Override
    public void removeAllEvent(User user) {
        System.out.println("Utente: " + user.getEmail());
        Query query1 = em.createQuery("Delete From Preference p Where p.event in (Select e From Event e Where e.creator.email = :mail)").setParameter(("mail"), user.getEmail());
        query1.executeUpdate();
        Query query2 = em.createQuery("Delete From UserEvent ue  Where ue.event in (Select e From Event e Where e.creator.email= :mail)").setParameter(("mail"), user.getEmail());
        query2.executeUpdate();
        Query query3 = em.createQuery("Delete From Event e where e.creator.email = :mail").setParameter(("mail"), user.getEmail());
        query3.executeUpdate();
        System.out.println("Delete completed");
    }
    
    
    
}
