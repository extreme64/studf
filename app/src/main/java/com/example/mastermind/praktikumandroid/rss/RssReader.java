package com.example.mastermind.praktikumandroid.rss;


import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.example.mastermind.praktikumandroid.Entry;
import com.example.mastermind.praktikumandroid.EntryContainer;
import com.example.mastermind.praktikumandroid.NotfTest;
import com.example.mastermind.praktikumandroid.ical.CalEntry;
import com.example.mastermind.praktikumandroid.ical.ICalWrap;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


/**
 * Created by Mastermind on 18-Jun-15.
 */

/**
 * Class reads RSS data.
 * @author ITCuties
 */
public class RssReader {

    Activity activity;
    // Our class has an attribute which represents RSS Feed URL
    private String rssUrl;
    private String TAG = "RSSReader";
    private int handlerFlag = 1;

    public static int GENERIC_HANDLER = 1;
    public static int GOOGLEcal_HANDLER = 2;

    /**
     * We set this URL with the constructor
     */
    public RssReader(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public RssReader(String rssUrl, Activity a) {
        this.rssUrl = rssUrl;
        this.activity = a;
    }



    public void setHandler(int flag)
    {
        this.handlerFlag = flag;
    }

    /**
     * Get RSS items. This method will be called to get the parsing process result.
     * @return
     */

    public List<RssEntry> getRssItems() throws Exception {
        List<RssEntry> localItemsreturn = new ArrayList<RssEntry>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser mSaxParser = factory.newSAXParser();
            XMLReader mXmlReader = mSaxParser.getXMLReader();

            RssParseHandler handler = new RssParseHandler();
            mXmlReader.setContentHandler(handler);
            InputStream mInputStream = new URL(rssUrl).openStream();
            Reader reader = new InputStreamReader(mInputStream,"UTF-8");

            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");


            mXmlReader.parse(is);//new InputSource(mInputStream));
            localItemsreturn = handler.getItems();

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return localItemsreturn;
    }

  /*  public List<CalEntry> getICalItems() throws Exception {
        List<CalEntry> localItemsreturn = new ArrayList<CalEntry>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser mSaxParser = factory.newSAXParser();
            XMLReader mXmlReader = mSaxParser.getXMLReader();

            InputStream fin = null;
            ICalWrap iCW = new ICalWrap();
            try {
                /*Context conx = activity;
                AssetManager am = conx.getAssets();*/
    /*            AssetManager am = activity.getAssets();

                fin = am.open("basic.ics");
                iCW.init(fin);

            } catch (IOException e) {
                e.printStackTrace();
            }

            localItemsreturn =  iCW.getItems();

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return localItemsreturn;
    }*/


   /* public List<NotfTest> getItems() throws Exception {
        //List<NotfTest> localItemsreturn = new ArrayList<NotfTest>();

        List<Entry> localItemsreturn = new ArrayList<Entry>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser mSaxParser = factory.newSAXParser();
            XMLReader mXmlReader = mSaxParser.getXMLReader();

            switch (handlerFlag)
            {
                case 1:
                /*    RssParseHandler handler = new RssParseHandler();
                    mXmlReader.setContentHandler(handler);
                    InputStream mInputStream = new URL(rssUrl).openStream();
                    Reader reader = new InputStreamReader(mInputStream,"UTF-8");

                    InputSource is = new InputSource(reader);
                    is.setEncoding("UTF-8");


                    mXmlReader.parse(is);//new InputSource(mInputStream));
                    localItemsreturn = handler.getItems();
                    break;
                case 2:
/*
                    GCalendarRssParseHandler GCalRssParseHandler = new GCalendarRssParseHandler();
                    mXmlReader.setContentHandler(GCalRssParseHandler);
                    InputStream mInputStreamGoo = new URL(rssUrl).openStream();
                    mXmlReader.parse(new InputSource(mInputStreamGoo));
                    localItemsreturn = GCalRssParseHandler.getItems();*/

                   /* InputStream fin = null;
                    ICalWrap iCW = new ICalWrap();
                    try {
                        /*Context conx = activity;
                        AssetManager am = conx.getAssets();*/
                    /*    AssetManager am = activity.getAssets();

                        fin = am.open("basic.ics");
                        iCW.init(fin);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    localItemsreturn =  iCW.getItems();*/
            //        break;
           //     default:
                   /* RssParseHandler handlerDef = new RssParseHandler();
                    mXmlReader.setContentHandler(handlerDef);
                    InputStream mInputStreamDef = new URL(rssUrl).openStream();
                    mXmlReader.parse(new InputSource(mInputStreamDef));
                    localItemsreturn = handlerDef.getItems();*/
      //      }

    //    } catch (Exception e) {
    //        Log.e(TAG, "Exception: " + e.getMessage());
    //    }
   //     return localItemsreturn;
   // }*/
}