package com.example.mastermind.praktikumandroid.rss;


import android.app.Activity;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.example.mastermind.praktikumandroid.FeedEntry;

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
    // class has an attribute which represents RSS Feed URL
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

    public List<FeedEntry> getRssItems() throws Exception {
        List<FeedEntry> localItemsreturn = new ArrayList<FeedEntry>();

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


            mXmlReader.parse(is);
            localItemsreturn = handler.getItems();

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return localItemsreturn;
    }


}