/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import entities.BadWeatherNotification;
import entities.Event;
import entities.Forecast;
import entities.MainCondition;
import entities.User;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
public class BadWeatherNotificationManager implements BadWeatherNotificationManagerInterface {

    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    Principal principal;
    
    @Override
    public List<BadWeatherNotification> searchByDate(String date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BadWeatherNotification> getAllUserBadWeatherNotification(User creator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   

    @Override
    public List<Event> findWarnings(User creator) {
        
        Query query1 = em.createQuery("Select distinct e From Event e, UserEvent ue, Preference p, Forecast f Where p.event=e and ue.creator=0 and ue.event=e and e.place=f.place and f.date between e.startDate and e.endDate and e.outdoor=1 and  f.mainCondition not in (Select p.main From Preference p where p.event=e) and e.creator.email= :mail").setParameter(("mail"), creator.getEmail());
    List<Event> eventWarning = new ArrayList<>(query1.getResultList());
    
    
    
    if(!eventWarning.isEmpty())
        System.out.println("Primo evento con warning: " + eventWarning.get(0).getTitle());
    else
        System.out.println("No warn");
    return eventWarning;
       }

    @Override
    public List<Timestamp> findSolution(List<Event> eventWarning) {
     
        int numDay;
        int year, month, day;
        Query queryForecast, queryMain;
        List<Forecast> forecast;
        List<Timestamp> daySuggest= new ArrayList<> ();
        List<MainCondition> condition;
        for(int i=0;i<eventWarning.size();i++)
        {
            year=eventWarning.get(i).getEndDate().getYear()-eventWarning.get(i).getStartDate().getYear();
            month=eventWarning.get(i).getEndDate().getMonth()-eventWarning.get(i).getStartDate().getMonth();
            day=eventWarning.get(i).getEndDate().getDay()-eventWarning.get(i).getStartDate().getDay();
            
            day=day+year*365+month*30;
            
            
            queryForecast = em.createQuery("Select  distinct f From Forecast f where f.place= :place and f.date > :startDate").setParameter(("place"), eventWarning.get(i).getPlace()).setParameter("startDate", eventWarning.get(i).getStartDate());
            forecast = queryForecast.getResultList();
            System.out.println("Forecast : " + forecast.size());
            
            queryMain = em.createQuery("Select distinct p.main From Preference p where p.event.idEvent = :id").setParameter(("id"), eventWarning.get(i).getIdEvent());
            condition=queryMain.getResultList();
            System.out.println("condition : " + condition.get(0));
            if(forecast.isEmpty())
            {
                daySuggest.add(null);
            }
            else
            {
               for( int j=0, daysOk = 0; j<forecast.size() && daysOk < day;j++)
               {
                   if(condition.contains(forecast.get(j).getMainCondition()))
                   {
                       if(daysOk==0)
                       {
                            daySuggest.add(i, forecast.get(j).getDate());
                            System.out.println("Evento: "+ i);
                       }
                       daysOk++;
                   }
                   else
                   {
                       daySuggest.add(i, null);
                       daysOk=0;
                   }
               }
            }
        }
        
        return daySuggest;
    
    }
}
