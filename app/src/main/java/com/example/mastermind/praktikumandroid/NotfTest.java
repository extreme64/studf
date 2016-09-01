package com.example.mastermind.praktikumandroid;

import java.net.URL;
import java.util.Date;

/**
 * Created by Mastermind on 15-Jun-15.
 */
public class NotfTest {

    public String id, rssItemId = "";
    public String title = "", desc = "", content = "", url = "", creator = "", email = "";
    public Date date, dateUpdate = new Date();
    public String category="";
    public String fromTab="";

    public NotfTest()
    {
    }

    public NotfTest(String id, String title, String desc,  String url)
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
    public NotfTest(String id, String rssItemId, String title, String desc,  String content, String url, String creator, String email, String category, Date date, Date dateUpdate)
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
