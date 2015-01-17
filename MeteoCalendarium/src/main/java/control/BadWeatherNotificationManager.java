/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;


import entity.Event;
import entity.Forecast;
import entity.MainCondition;
import entity.User;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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

    @EJB
    private EventManagerInterface emi;



     @Override
    public boolean isWarned(Event event) {
         Query query1 = em.createQuery("Select distinct e From Event e, UserEvent ue, Preference p, Forecast f Where ue.event= :event and e.outdoor=1 and p.event= :event and f.place=e.place and CAST(f.date AS DATE) between CAST(e.startDate AS DATE) and CAST(e.endDate AS DATE) and f.mainCondition not in (Select pr.main From Preference pr where pr.event= :event) ").setParameter("event", event);
         List<Event> eventWarning=query1.getResultList();
         return !eventWarning.isEmpty();
     }

    @Override
    public List<Event> findWarnings(User creator) {

        Query query1 = em.createQuery("Select distinct e From Event e, UserEvent ue, Preference p, Forecast f Where ue.event=e and ue.creator=1 and e.outdoor=1 and e.creator.email= :mail and p.event=e and f.place=e.place and CAST(f.date AS DATE) between  CAST(e.startDate AS DATE) and  CAST(e.endDate AS DATE) and f.mainCondition not in (Select pr.main From Preference pr where pr.event=e) ").setParameter("mail", creator.getEmail());
    List<Event> eventWarning=query1.getResultList();

    if(!eventWarning.isEmpty())
        System.out.println("Primo evento con warning: " + eventWarning.get(0).getTitle());
    else
        System.out.println("No warn");
    return eventWarning;
       }

    @Override
    public List<Timestamp> findSolution(List<Event> eventWarning) {

        int  day;
        long dayy;
        Timestamp help;
        Query queryForecast, queryMain;
        List<Forecast> forecast;
        List<Timestamp> daySuggest= new ArrayList<> ();
        List<MainCondition> condition;
        for(int i=0;i<eventWarning.size();i++)
        {
            dayy=eventWarning.get(i).getEndDate().getTime()-eventWarning.get(i).getStartDate().getTime();
            System.out.println("Event: " + eventWarning.get(i).getTitle() + " is long " + dayy);
            if(dayy/(1000*60*60*24)<1)
            {
                day=1;
                System.out.println("Less than 1 day");
            } else
            {

                day=(int)(dayy/(1000*60*60*24));
                System.out.println("More than 1 day: " + day);

            }

            queryForecast = em.createQuery("Select  distinct f From Forecast f where f.place= :place and CAST(f.date AS DATE) > CAST(:startDate AS DATE)").setParameter(("place"), eventWarning.get(i).getPlace()).setParameter("startDate", eventWarning.get(i).getStartDate());
            forecast = queryForecast.getResultList();
            System.out.println("Forecast : " + forecast.size() + " type: " + forecast.get(0).getMainCondition().getCondition());

            queryMain = em.createQuery("Select distinct p.main From Preference p where p.event.idEvent = :id").setParameter(("id"), eventWarning.get(i).getIdEvent());
            condition=queryMain.getResultList();
            System.out.println("condition : " + condition.get(0).getCondition());
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

                           if(!checkOverlapping(forecast.get(j).getDate(), dayy, eventWarning.get(i).getStartDate(), eventWarning.get(i).getEndDate(), eventWarning.get(i).getCreator()))
                           {
                               System.out.println("no overlapping");
                               help=forecast.get(j).getDate();
                               help.setHours(eventWarning.get(i).getStartDate().getHours());
                               help.setMinutes(eventWarning.get(i).getStartDate().getMinutes());
                               help.setSeconds(eventWarning.get(i).getStartDate().getSeconds());
                                daySuggest.add(i, help);

                           }
                           else
                           {
                               daySuggest.add(i, null);
                               System.out.println("Suggest gets overlapping");
                               daysOk=-1;
                           }
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
    public boolean checkOverlapping(Timestamp start, long day, Timestamp oldStart, Timestamp oldEnd, User creator)
    {
        Event event = new Event();
        start.setHours(oldStart.getHours());
        start.setMinutes(oldStart.getMinutes());
        start.setSeconds(oldStart.getSeconds());
        event.setStartDate(start);
        Timestamp end;
        System.out.println(start.toString());
        end=new Timestamp(0);
        System.out.println("End = start");
        end.setTime(start.getTime()+day);
        System.out.println(end.toString());
        event.setEndDate(end);
        event.setCreator(creator);
        return emi.searchOverlapping(event,event.getCreator());
        }


    }

