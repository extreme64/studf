package com.example.mastermind.praktikumandroid;

import com.example.mastermind.praktikumandroid.ical.ComponentWrap;

import java.net.URL;
import java.util.Date;

/**
 * Created by Mastermind on 15-Jun-15.
 */
public class FeedEntry {

    public final static int RSS = 1;
    public final static int ICAL = 2;

    /* staro */
    public String id;
    public String rssItemId = "";
    public String title = "", desc = "", content = "", url = "", creator = "", email = "";
    public Date date, dateUpdate = new Date();
    public String category="";
    public String fromTab="";


    /* ICal membs */

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

    private int flag;


    public int getFlag() { return flag; }

    public void setEntryFromICal(ComponentWrap cW)
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

        flag = ICAL;
    }

    public void setEntryFromICal(String dtstart, String dtend, String dtstamp, String id,
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

        flag = ICAL;
    }

    public void setEntryFromRss(String id, String title, String desc,  String url)
    {
        this.id = id;
        this.title = title;
        this.desc =  desc;
        this.date = new Date();
        if(url==null)
            this.url = "";
        else
            this.url = url;

        flag = RSS;
    }

    public void setEntryFromRss(String id, String rssItemId, String title, String desc,  String content, String url, String creator, String email, String category, Date date, Date dateUpdate)
    {
        this.id = id;
        this.title = title;
        this.desc =  desc;
        this.date = new Date();
        if(url==null)
            this.url = "";
        else
            this.url = url;

        flag = RSS;
    }

    /* constr */

    public FeedEntry() {}

   /* public FeedEntry(String id, String title, String desc,  String url)
    {
        this.id = id;
        this.title = title;
        this.desc =  desc;
        this.date = new Date();
        if(url==null)
            this.url = "";
        else
            this.url = url;
    }
    public FeedEntry(String id, String rssItemId, String title, String desc,  String content, String url, String creator, String email, String category, Date date, Date dateUpdate)
    {
        this.id = id;
        this.title = title;
        this.desc =  desc;
        this.date = new Date();
        if(url==null)
            this.url = "";
        else
            this.url = url;
    }*/

}
