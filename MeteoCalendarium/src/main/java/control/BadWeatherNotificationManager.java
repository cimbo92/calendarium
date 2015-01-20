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

    /**
     * This funtion controls if an event has bad weather warning
     *
     * @param event
     * @return true if there is no match between preferences and forecasts
     */
    @Override
    public boolean isWarned(Event event) {
        Query query1 = em.createQuery("Select distinct e From Event e, UserEvent ue, Preference p, Forecast f Where ue.event= :event and e.outdoor=1 and p.event= :event and f.place=e.place and CAST(f.date AS DATE) between CAST(e.startDate AS DATE) and CAST(e.endDate AS DATE) and f.mainCondition not in (Select pr.main From Preference pr where pr.event= :event) ").setParameter("event", event);
        List<Event> eventWarning = query1.getResultList();
        return !eventWarning.isEmpty();
    }

    /**
     * this function search from the database all the events of a user that get
     * overlapping
     *
     * @param creator
     * @return list of events with warning
     */
    @Override
    public List<Event> findWarnings(User creator) {

        Query query1 = em.createQuery("Select distinct e From Event e, UserEvent ue, Preference p, Forecast f Where ue.event=e and ue.creator=1 and e.outdoor=1 and e.creator.email= :mail and p.event=e and f.place=e.place and CAST(f.date AS DATE) between  CAST(e.startDate AS DATE) and  CAST(e.endDate AS DATE) and f.mainCondition not in (Select pr.main From Preference pr where pr.event=e) ").setParameter("mail", creator.getEmail());
        List<Event> eventWarning = query1.getResultList();

        return eventWarning;
    }

    /**
     * This function search for each event a possible new schedule that respects
     * the preference
     *
     * @param eventWarning
     * @return list of new dates
     */
    @Override
    public List<Timestamp> findSolution(List<Event> eventWarning) {

        int day;
        long dayy;
        Timestamp help;
        Query queryForecast, queryMain;
        List<Forecast> forecast;
        List<Timestamp> daySuggest = new ArrayList<>();
        List<MainCondition> condition;
        for (int i = 0; i < eventWarning.size(); i++) {
            dayy = eventWarning.get(i).getEndDate().getTime() - eventWarning.get(i).getStartDate().getTime();
            if (dayy / (1000 * 60 * 60 * 24) < 1) {
                day = 1;
            } else {
                day = (int) (dayy / (1000 * 60 * 60 * 24));
            }

            queryForecast = em.createQuery("Select  distinct f From Forecast f where f.place= :place and CAST(f.date AS DATE) > CAST(:startDate AS DATE)").setParameter(("place"), eventWarning.get(i).getPlace()).setParameter("startDate", eventWarning.get(i).getStartDate());
            forecast = queryForecast.getResultList();

            queryMain = em.createQuery("Select distinct p.main From Preference p where p.event.idEvent = :id").setParameter(("id"), eventWarning.get(i).getIdEvent());
            condition = queryMain.getResultList();
            if (forecast.isEmpty()) {
                daySuggest.add(null);
            } else {
                for (int j = 0, daysOk = 0; j < forecast.size() && daysOk < day; j++) {
                    if (condition.contains(forecast.get(j).getMainCondition())) {
                        if (daysOk == 0) {

                            if (!checkOverlapping(forecast.get(j).getDate(), dayy, eventWarning.get(i).getStartDate(), eventWarning.get(i).getEndDate(), eventWarning.get(i).getCreator())) {
                                help = forecast.get(j).getDate();
                                help.setHours(eventWarning.get(i).getStartDate().getHours());
                                help.setMinutes(eventWarning.get(i).getStartDate().getMinutes());
                                help.setSeconds(eventWarning.get(i).getStartDate().getSeconds());
                                daySuggest.add(i, help);

                            } else {
                                daySuggest.add(i, null);
                                daysOk = -1;
                            }
                        }
                        daysOk++;
                    } else {
                        daySuggest.add(i, null);
                        daysOk = 0;
                    }
                }
            }
        }

        return daySuggest;

    }

    /**
     * this function check if an event, with new start and end Date, gets
     * overlapping
     *
     * @param start
     * @param day
     * @param oldStart
     * @param oldEnd
     * @param creator
     * @return true if gets overlapping
     */
    public boolean checkOverlapping(Timestamp start, long day, Timestamp oldStart, Timestamp oldEnd, User creator) {
        Event event = new Event();
        start.setHours(oldStart.getHours());
        start.setMinutes(oldStart.getMinutes());
        start.setSeconds(oldStart.getSeconds());
        event.setStartDate(start);
        Timestamp end;
        end = new Timestamp(0);
        end.setTime(start.getTime() + day);
        event.setEndDate(end);
        event.setCreator(creator);
        return emi.searchOverlapping(event, event.getCreator());
    }

}
