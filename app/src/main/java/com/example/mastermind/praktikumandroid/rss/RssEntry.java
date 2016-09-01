package com.example.mastermind.praktikumandroid.rss;

import com.example.mastermind.praktikumandroid.Entry;

import java.util.Date;

/**
 * Created by Acer on 28-Aug-16.
 */
public class RssEntry extends Entry {

    public String rssItemId;
    public String title = "";
    public String desc = "";
    public String content = "";
    public String url = "";
    public String creator = "";
    public String email = "";
    public Date date;
    public Date dateUpdate = new Date();
    public String category="";
    public String fromTab="";


    public RssEntry(){}

    public RssEntry(String id, String title, String desc,  String url)
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

    public RssEntry(String id, String rssItemId, String title, String desc,  String content, String url, String creator, String email, String category, Date date, Date dateUpdate)
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

}
