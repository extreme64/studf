package com.example.mastermind.praktikumandroid.ical;


import com.example.mastermind.praktikumandroid.FeedEntry;

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
    private List<FeedEntry> calItems;
    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing


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
                */

                /**TODO: sada formirati niz koji je izvor adaptera liste za tab koji prikazuje kalendar */
                FeedEntry locTmp = new FeedEntry();
                locTmp.setEntryFromICal(cW);
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


    public List<FeedEntry> getItems() {
        return calItems;
    }


}
