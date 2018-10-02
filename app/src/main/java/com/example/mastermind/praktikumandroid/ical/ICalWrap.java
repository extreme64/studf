package com.example.mastermind.praktikumandroid.ical;


import com.example.mastermind.praktikumandroid.FeedEntry;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Acer on 29-Jul-16.
 */
public class ICalWrap {

    // List of items parsed
    protected ComponentList calCompsList;
    private List<FeedEntry> calItems;

    public ICalWrap() {}

    public void init(InputStream fin){

        calItems = new ArrayList();
        Calendar calendar = null;
        CalendarBuilder builder = new CalendarBuilder();

        try {
            calendar = builder.build(fin);
        } catch (ParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        calCompsList = calendar.getComponents();


        boolean obraditiProslu = false;
        for (Iterator i = calCompsList.iterator(); i.hasNext();) {
            Component component = (Component) i.next();
            ComponentWrap cW = new ComponentWrap(component.getName(), component.getProperties());

            try {

                // uzimamo datum starit iz URL kalendara
                String d = (cW.getDtstart().getValue()).substring(0,4);
                String fd = (cW.getDtstart().getValue()).substring(0,8);
                SimpleDateFormat formatCalDTStamp = new SimpleDateFormat("yyyyMMdd");
                Date datumCalURL = formatCalDTStamp.parse(  fd );
                java.util.Calendar cal = new GregorianCalendar();
                cal.setTime(datumCalURL);
                int mesecICal = cal.get(java.util.Calendar.MONTH);
                int godinaICal = cal.get(java.util.Calendar.YEAR);

                // odredjujemo trenutni datum i opsec koliko mesec pre i posle njega
                // da smatramo potrebnim za korisnika
                int mesecMin;
                int mesecMax;
                java.util.Calendar calSada = new GregorianCalendar();
                int opseg = 4;
                int trenutniMesec = calSada.get(java.util.Calendar.MONTH);
                int trenutnaGodina = calSada.get(java.util.Calendar.YEAR);
                java.util.Calendar calMax = calSada;
                calMax.add(java.util.Calendar.MONTH, 4);
                int mesecMaksimum = calMax.get(java.util.Calendar.MONTH);


                // ako opsecu sve okej, ako ne prekini popunu liste
                if( godinaICal == trenutnaGodina && (mesecICal >= trenutniMesec &&  mesecICal <= mesecMaksimum) ) {
                    obraditiProslu = true; // bug patch - 2017 u listi pre 2018
                }
                else if(obraditiProslu && (godinaICal == trenutnaGodina-1 && (mesecICal >= 9)) ) {
                }
                else {
                    continue;
                }

                // popuna liste trenutnom stavkom
                FeedEntry locTmp = new FeedEntry();
                locTmp.setEntryFromICal(cW);
                calItems.add(locTmp);

            }catch (Exception e){}
        }

    }


    public List<FeedEntry> getItems() {
        return calItems;
    }


}
