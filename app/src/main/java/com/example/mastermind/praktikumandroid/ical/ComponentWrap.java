package com.example.mastermind.praktikumandroid.ical;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.ValidationException;

/**
 * Created by Acer on 28-Aug-16.
 */
public class ComponentWrap extends Component {


    public static final String DTSTART = "DTSTART";
    public static final String DTEND = "DTEND";
    public static final String UID = "UID";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String LOCATION = "LOCATION";
    public static final String STATUS = "STATUS";
    public static final String SUMMARY = "SUMMARY";
    public static final String DTSTAMP= "DTSTAMP";
    public static final String CREATED= "CREATED";
    public static final String LASTMODIFIED= "LAST-MODIFIED";
    public static final String SEQUENCE= "SEQUENCE";
    public static final String TRANSP= "TRANSP";

    private String name;
    private PropertyList properties;


    public ComponentWrap(String s){
        super(s);
    }

    public ComponentWrap(final String s, final PropertyList cl){
        super(s);
        this.name = s;
        this.properties = cl;
    }

    /* polja iz ove klase */
    public Property getDtstart(){return getPropertiesWrap().getProperty(DTSTART);}
    public Property getDtend(){return getPropertiesWrap().getProperty(DTEND);}
    public Property getUID(){return getPropertiesWrap().getProperty(UID);}
    public Property getDescription(){return getPropertiesWrap().getProperty(DESCRIPTION);}
    public Property getLocation(){return getPropertiesWrap().getProperty(LOCATION);}
    public Property getStatus(){return getPropertiesWrap().getProperty(STATUS);}
    public Property getSummary(){return getPropertiesWrap().getProperty(SUMMARY);}
    public Property getDtstamp(){return getPropertiesWrap().getProperty(DTSTAMP);}
    public Property getCreated(){return getPropertiesWrap().getProperty(CREATED);}
    public Property getLastmodified(){return getPropertiesWrap().getProperty(LASTMODIFIED);}
    public Property getSequence(){return getPropertiesWrap().getProperty(SEQUENCE);}
    public Property getTransp(){return getPropertiesWrap().getProperty(TRANSP);}


    /* polja nasledjena */
    public Property getBegin(){return getPropertiesWrap().getProperty(BEGIN);}
    public Property getEnd(){return getPropertiesWrap().getProperty(END);}
    public Property getVevent(){return getPropertiesWrap().getProperty(VEVENT);}
    public Property getVtodo(){return getPropertiesWrap().getProperty(VTODO);}
    public Property getVjournal(){return getPropertiesWrap().getProperty(VJOURNAL);}
    public Property getVfreebusy(){return getPropertiesWrap().getProperty(VFREEBUSY);}
    public Property getVtimezone(){return getPropertiesWrap().getProperty(VTIMEZONE);}
    public Property getValarm(){return getPropertiesWrap().getProperty(VALARM);}
    public Property getVavailability(){return getPropertiesWrap().getProperty(VAVAILABILITY);}
    public Property getVenue(){return getPropertiesWrap().getProperty(VVENUE);}
    public Property getAvailable(){return getPropertiesWrap().getProperty(AVAILABLE);}
    public Property getExperimental_prefix(){return getPropertiesWrap().getProperty(EXPERIMENTAL_PREFIX);}


    /**
     * @return Returns the properties.
     */
    public PropertyList getPropertiesWrap() {
        return this.properties;
    }

    @Override
    public void validate(boolean b) throws ValidationException {

    }
}

