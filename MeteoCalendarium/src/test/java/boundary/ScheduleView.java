/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

import entity.Event;
import entity.IDEvent;
import entity.Place;
import entity.User;
import java.sql.Timestamp;

/**
 *
 * @author Alessandro
 */
public class ScheduleView {

        Event eventCreated = new Event();
        Event notCreatedAccepted = new Event();
        Event notCreatedNotAccepted = new Event();

            private Event initEvent(String id,User u) {
        Event e = new Event();
        e.setIdEvent(new IDEvent(id));
        e.setPlace(new Place());
        Timestamp tmp = new Timestamp(2015, 1, 1, 1, 1, 1, 1);
        e.setCreator(u);
        e.setDescription("Test");
        e.setEndDate(tmp);
        e.setOutdoor(false);
        e.setPublicEvent(true);
        e.setStartDate(tmp);
        e.setTitle("title");
        return e;
    }


}
