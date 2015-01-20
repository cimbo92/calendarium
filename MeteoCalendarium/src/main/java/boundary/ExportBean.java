/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundary;

/**
 *
 * @author alessandro
 */
import entity.Event;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import control.EventManagerInterface;
import control.PreferenceManagerInterface;
import control.UserEventManagerInterface;
import control.UserManagerInterface;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * class for exporting calendar
 *
 * @author alessandro
 */
@Named
@RequestScoped
public class ExportBean {

    /*
     * ******************************************************************
     * MANAGERS
     *******************************************************************
     */
    @EJB
    private EventManagerInterface em;
    @EJB
    UserManagerInterface um;
    @EJB
    PreferenceManagerInterface pm;
    @EJB
    UserEventManagerInterface uem;
    List<Event> events;
    List<String> preferences;
    List<String> userInvited;

    public ExportBean() {
    }

    @PostConstruct
    public void init() {
        events = new ArrayList<>();
        events = em.getEventsCreated(um.getLoggedUser());
        preferences = new ArrayList<>();
        userInvited = new ArrayList<>();
    }

    /**
     * function that exports the calendar in a xml file
     */
    public void export() {

        events = new ArrayList<>();
        events = em.getEventsCreated(um.getLoggedUser());
        preferences = new ArrayList<>();
        userInvited = new ArrayList<>();

        FacesContext context = FacesContext.getCurrentInstance();

        String help;
        //System.out.println("Evento " + events.get(0).getTitle());

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("data");
            doc.appendChild(rootElement);

            for (int i = 0; i < events.size(); i++) {

                // staff elements
                Element event = doc.createElement("event");
                rootElement.appendChild(event);

                // set attribute to staff element
                Attr attr = doc.createAttribute("id");
                attr.setValue(events.get(i).getIdEvent().toString());
                event.setAttributeNode(attr);

                // shorten way
                // staff.setAttribute("id", "1");
                // firstname elements
                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(events.get(i).getTitle()));
                event.appendChild(title);

                Element creator = doc.createElement("creator");
                creator.appendChild(doc.createTextNode(events.get(i).getCreator().getEmail()));
                event.appendChild(creator);

                // lastname elements
                Element description = doc.createElement("description");
                description.appendChild(doc.createTextNode(events.get(i).getDescription()));
                event.appendChild(description);

                Element place = doc.createElement("place");
                place.appendChild(doc.createTextNode(events.get(i).getPlace().getCity()));
                event.appendChild(place);

                // nickname elements
                Element startDate = doc.createElement("startDate");
                startDate.appendChild(doc.createTextNode(events.get(i).getStartDate().toString()));
                event.appendChild(startDate);

                // nickname elements
                Element endDate = doc.createElement("endDate");
                endDate.appendChild(doc.createTextNode(events.get(i).getEndDate().toString()));
                event.appendChild(endDate);

                // salary elements
                Element outdoor = doc.createElement("outdoor");
                if (events.get(i).isOutdoor()) {
                    help = "true";
                } else {
                    help = "false";
                }
                outdoor.appendChild(doc.createTextNode(help));
                event.appendChild(outdoor);

                Element publicEvent = doc.createElement("publicEvent");
                if (events.get(i).isPublicEvent()) {
                    help = "true";
                } else {
                    help = "false";
                }
                publicEvent.appendChild(doc.createTextNode(help));
                event.appendChild(publicEvent);

                preferences = pm.getPreferenceOfEvent(events.get(i));
                for (int j = 0; j < preferences.size(); j++) {
                    Element pref = doc.createElement("preference");
                    pref.appendChild(doc.createTextNode(preferences.get(j)));
                    event.appendChild(pref);
                }
                userInvited = uem.invitedUsersOfEvent(events.get(i));
                for (int z = 0; z < userInvited.size(); z++) {
                    Element invit = doc.createElement("invitated");
                    invit.appendChild(doc.createTextNode(userInvited.get(z)));
                    event.appendChild(invit);
                }

            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            String DefaultFolder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString();
            String os = System.getProperty("os.name");
            String slash = "\\";
            if (os.equalsIgnoreCase("Mac OS X")) {
                slash = "/";
            }
            File file = new File(DefaultFolder + slash + "calendar.xml");

            StreamResult result = new StreamResult(file);
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException pce) {

            context.addMessage(null, new FacesMessage("Error", "Error in Exporting"));

        }

        context.addMessage(null, new FacesMessage("Export", "Export Completed"));

    }
}
