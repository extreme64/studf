package com.example.mastermind.praktikumandroid.ical;

import com.example.mastermind.praktikumandroid.Entry;
import com.example.mastermind.praktikumandroid.ical.ComponentWrap;
import java.util.Date;

/**
 * Created by Mastermind on 15-Jun-15.
 */
public class CalEntry extends Entry {

    public String rssItemId;

    public String dtstart;
    public String dtend;
    public String description;
    public String location;
    public String status;
    public String summary;
    public String dtstamp;
    public String firstcreated;
    public String lastmodified;
    public String sequence;
    public String transp;
/*
    public String begin;
    public String end;
    public String vevent;
    public String vtodo;
    public String vjournal;
    public String vfreebusy;
    public String vtimezone;
    public String valarm;
    public String vavailability;
    public String vvenue;
    public String available;
    public String experimental_prefix;
*/
    public String fromTab="";

    public CalEntry(){}

    public CalEntry(ComponentWrap cW)
    {
        this.id = cW.getUID().getValue();

        this.dtstart = cW.getDtstart().getValue();
        this.dtend = cW.getDtend().getValue();
        this.description =  cW.getDescription().getValue();
        this.location = cW.getLocation().getValue();
        this.status = cW.getStatus().getValue();
        this.summary = cW.getSummary().getValue();
        this.dtstamp = cW.getDtstamp().getValue();
        this.firstcreated = cW.getCreated().getValue();
        this.lastmodified = cW.getLastmodified().getValue();
        this.sequence = cW.getSequence().getValue();
        this.transp = cW.getTransp().getValue();
    }


    public CalEntry(String dtstart, String dtend, String dtstamp, String id,
                        String firstcreated, String description, String lastmodified,
                            String location, String sequence, String status, String summary, String transp)
    {
        this.dtstart = dtstart;
        this.dtend = dtend;
        this.id = id;
        this.description =  description;
        this.location = location;
        this.status = status;
        this.summary = summary;
        this.dtstamp = dtstamp;
        this.firstcreated = firstcreated;
        this.lastmodified = lastmodified;
        this.sequence = sequence;
        this.transp = transp;
    }

    public CalEntry(String id, String title, String desc, String url)
    {
       /* this.id = id;
        this.title = title;
        this.desc =  desc;
        this.date = new Date();
        if(url==null)
            this.url = "";
        else
            this.url = url;*/
    }
    public CalEntry(String id, String rssItemId, String title, String desc, String content, String url, String creator, String email, String category, Date date, Date dateUpdate)
    {
       /* this.id = id;
        this.title = title;
        this.desc =  desc;
        this.date = new Date();
        if(url==null)
            this.url = "";
        else
            this.url = url;*/
    }



}
