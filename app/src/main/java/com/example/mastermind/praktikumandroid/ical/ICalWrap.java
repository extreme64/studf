package com.example.mastermind.praktikumandroid.ical;


import com.example.mastermind.praktikumandroid.EntryContainer;
import com.example.mastermind.praktikumandroid.NotfTest;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Acer on 29-Jul-16.
 */
public class ICalWrap {

    // List of items parsed
    private List<CalEntry> calItems;
    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing

    //private NotfTest currentItem;

    //protected Calendar cal;
    //protected PropertyList calPropList;
    protected ComponentList calCompsList;


    public ICalWrap() {
    }

    public void init(InputStream fin){

        calItems = new ArrayList();
        CalendarBuilder builder = new CalendarBuilder();

        Calendar calendar = null;

        try {
            calendar = builder.build(fin);
            PropertyList cPropList = calendar.getProperties();

            for (Iterator pp = cPropList.iterator(); pp.hasNext();) {
                Property pr = (Property) pp.next();
                System.out.println("PR >>>>>>>>>> [" + pr.getName() + "] = [" + pr.getValue() + "]");
            }
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        calCompsList = calendar.getComponents();

        for (Iterator i = calCompsList.iterator(); i.hasNext();) {
            Component component = (Component) i.next();
            ComponentWrap cW = new ComponentWrap(component.getName(), component.getProperties());
            //System.out.println("SIZE  [" + calendar.getComponents().size() + "]");
            try {

                System.out.println("Component [" + component.getName() + "]");
                /* alternativa A */
                /*  System.out.println("Property [" + cW.getDtstart().getName() + ", " + cW.getDtstart().getValue() + "]");
                System.out.println("Property [" + cW.getDtend().getName() + ", " + cW.getDtend().getValue() + "]");
                System.out.println("Property [" + cW.getDtstamp().getName() + ", " + cW.getDtstamp().getValue() + "]");
                System.out.println("Property [" + cW.getUID().getName() + ", " + cW.getUID().getValue() + "]");
                System.out.println("Property [" + cW.getCreated().getName() + ", " + cW.getCreated().getValue() + "]");
                System.out.println("Property [" + cW.getDescription().getName() + ", " + cW.getDescription().getValue() + "]");
                System.out.println("Property [" + cW.getLastmodified().getName() + ", " + cW.getLastmodified().getValue() + "]");
                System.out.println("Property [" + cW.getSequence().getName() + ", " + cW.getSequence().getValue() + "]");
                System.out.println("Property [" + cW.getStatus().getName() + ", " + cW.getStatus().getValue() + "]");
                System.out.println("Property [" + cW.getSummary().getName() + ", " + cW.getSummary().getValue() + "]");
                System.out.println("Property [" + cW.getTransp().getName() + ", " + cW.getTransp().getValue() + "]");
                */

                /**TODO: sada formirati niz koji je izvor adaptera liste za tab koji prikazuje kalendar */
                CalEntry locTmp = new CalEntry(cW);
                calItems.add(locTmp);



                /* alternativa B za dobijanje polja */
                /*System.out.println("Izlistaj sve iz cW.getPropertiesWrap() ///////////////////// ");
                for (Iterator j = cW.getPropertiesWrap().iterator(); j.hasNext(); ) {
                    //vratiti nazad obican ne kastovan objekat
                    property = (Property) j.next();
                    System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
                }*/
            }catch (Exception e){}
        }

    }

    public List<CalEntry> getItems() {
        return calItems;
    }


}
