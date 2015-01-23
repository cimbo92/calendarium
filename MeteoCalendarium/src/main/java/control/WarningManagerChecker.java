/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import entity.Event;
import entity.Users;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;

/**
 *
 * @author alessandro
 */
@Singleton
@Remote(WarningManagerCheckerInterface.class)
public class WarningManagerChecker implements WarningManagerCheckerInterface {

    /**
     *
     * MANAGERS
     *
     */
    @EJB
    private BadWeatherNotificationManagerInterface bm;
    @EJB
    private MailSenderManagerInterface ms;
    @EJB
    private UserEventManagerInterface uem;

    int count = 0;

    /**
     * this function check every 12 hours if there are some bad weather
     * notifications. If there are, this function sents email to creators
     */
    @Schedules({
        @Schedule(second = "0", minute = "0", hour = "0", persistent = false),
        @Schedule(second = "0", minute = "0", hour = "12", persistent = false)
    })
    private void warningEvery12Hours() {

        System.out.println("Begin warningEvery12Hours " + count);
        count++;

        List<Users> users = uem.getUsersCreator();
        Iterator<Users> ite = users.iterator();
        while (ite.hasNext()) {
            Users u = ite.next();
            List<Event> eventWarning = bm.findWarnings(u);

            for (Event e : eventWarning) {
                ms.sendMail(u.getEmail(), "Warning: " + e.getTitle(), "Message to notify a forecast change(12h). For more info check on your personal page. \nStaff MeteoCalendarium");
            }
        }
    }

    /**
     * this function checks if there are warnings for events that start in three
     * days If there are, this function sents email to creators
     */
    @Schedule(second = "0", minute = "0", hour = "0", persistent = false)
    private void threeDaysWarning() {

        System.out.println("Inizio check three day warning");
        Timestamp now = new Timestamp(new java.util.Date().getTime());

        long deltaThreeDay = 3 * (24 * 60 * 60 * 1000);

        long deltaTwoDay = 2 * (24 * 60 * 60 * 1000);
        long nowLong = now.getTime();

        //Get all users
        List<Users> users = uem.getUsersCreator();
        //Get all warnings
        Iterator<Users> ite = users.iterator();
        while (ite.hasNext()) {
            Users u = ite.next();
            List<Event> eventWarning = bm.findWarnings(u);

            for (Event e : eventWarning) {

                long d = e.getStartDate().getTime();
                if (d - nowLong >= deltaTwoDay && d - nowLong <= deltaThreeDay) {
                    ms.sendMail(u.getEmail(), "Warning: " + e.getTitle(), "Message to notify a three days notification. For more info check on your personal page. \nStaff MeteoCalendarium");
                }
            }
        }
        System.out.println("Fine check three day warning");
    }

    /**
     * this functions checks if there are some bad weather notification for
     * events strarting in one day. If there are, the function sents email to
     * invitees
     */
    @Schedule(second = "0", minute = "0", hour = "0", persistent = false)
    private void oneDayWarning() {

        System.out.println("Inizio check one day warning");
        Timestamp now = new Timestamp(new java.util.Date().getTime());

        long deltaOneDay = (24 * 60 * 60 * 1000);

        long nowLong = now.getTime();

        List<Users> users = uem.getUsersCreator();
        //Get all warnings
        Iterator<Users> ite = users.iterator();
        while (ite.hasNext()) {
            Users u = ite.next();
            List<Event> eventWarning = bm.findWarnings(u);

            for (Event e : eventWarning) {

                long d = e.getStartDate().getTime();

                if (d - nowLong >= 0 && d - nowLong <= deltaOneDay) {
                    List<Users> invited = uem.getInvitedWhoAccepted(e);
                    for (Users i : invited) {
                        ms.sendMail(i.getEmail(), "Warning: " + e.getTitle(), "Message to notify a one days notification for an event in which you are invited. For more info check on your personal page. \nStaff MeteoCalendarium");
                    }
                }
            }
        }
        System.out.println("Fine check one day warning");
    }
}
