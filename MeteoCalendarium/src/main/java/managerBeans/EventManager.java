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
import java.security.Principal;
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
    public void addEvent(String idUser, Event event) throws OverlappingException  {
        
      //  if(searchEventOverlapping(idUser, event.getDate(), event.getStartHour(), event.getEndHour()))
      //      throw new OverlappingException();
        event.setCreator(em.find(User.class, idUser));
        System.out.println(idUser+ " " + event+"\n");
        em.persist(event);
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
    public boolean searchEventOverlapping(String idUser, String date, String startHour, String endHour) {
        
        List<Event> listEvent;
        
        List<Invitation> listInvitations;
        
        //lista da controllare Overlapping con eventi creati
        
        try {
                        Query query = em
                                        .createQuery("SELECT e "
                                                        + "FROM Event e JOIN e.owner u"
                                                        + "WHERE e.date = :date AND u.email =: idUser");
                        
                  //ListEvent contiene gli eventi creati dall'utente nella data incriminata
                  listEvent = (List<Event>) query.setParameter("date", date).setParameter("idUser", idUser).getResultList();
                        
                } catch (Exception e) {
                    System.out.println("Errore query searchOverlapping");
                        return false;
                }
        
        //aggiungiamo eventuali eventi non creati dall'utente ma a cui esso partecipi
        
        try {
            
            Query query = em
                                        .createQuery("SELECT i "
                                                        + "FROM Invitation i JOIN i.owner u"
                                                        + "WHERE u.email =: idUser AND i.accepted = true");
            
            listInvitations = (List<Invitation>) query.setParameter("idUser", idUser).getResultList();
            
            //Dalle lista di inviti aggiungo alla lista di eventi da controllare
            //quelli in cui l'utente abbia accettato un invito!
            
            for(Invitation i : listInvitations){
                if(i.getDate().equals(date)){
                    listEvent.add(i.getEvent());
                }
            }
            
            
        } catch (Exception e) {
                    System.out.println("Errore query searchOverlapping");
                        return false;
                }
        
        for(Event e : listEvent){
            
            int startH = Integer.parseInt(e.getStartHour());
            int endH = Integer.parseInt(e.getEndHour());
            
            if(overlapping(Integer.parseInt(startHour),Integer.parseInt(endHour), startH, endH))
            return true;
            
        }
        
        return false;
    }
    
    public boolean overlapping(int beginFirst, int endFirst, int beginSecond, int endSecond){
        //Beginning in the same day
        
        if(overlappingLeft(beginFirst, endFirst, beginSecond, endSecond) ||
           overlappingCenter(beginFirst, endFirst, beginSecond, endSecond) ||
           overlappingRight(beginFirst, endFirst, beginSecond, endSecond) )
            return true;
        
        return false;
    }
    
     private boolean overlappingLeft(int startFirst, int endFirst, int startSecond, int endSecond){
      return  (startFirst >= startSecond && endSecond > startFirst);
    }
    
    private boolean overlappingCenter(int startFirst, int endFirst, int startSecond, int endSecond){
        return (startFirst <= startSecond && endFirst >= endSecond);
    }
    
    private boolean overlappingRight(int startFirst, int endFirst, int startSecond, int endSecond){
        return startFirst <= startSecond && endFirst > startSecond;
    }

    @Override
    public List<User> getInvitedUsers(Event event) {
       
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
    }

}
