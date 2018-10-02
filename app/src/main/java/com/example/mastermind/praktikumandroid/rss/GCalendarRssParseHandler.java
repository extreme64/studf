package com.example.mastermind.praktikumandroid.rss;

import com.example.mastermind.praktikumandroid.FeedEntry;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mastermind on 26-Jun-15.
 */
public class GCalendarRssParseHandler extends DefaultHandler{
    // List of items parsed
    private List<FeedEntry> rssItems;
    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing
    private FeedEntry currentItem;
    // We have two indicators which are used to differentiate whether a tag title or link is being processed by the parser

    private int br = 0;
    private boolean parsingGuid;
    private boolean parsingPublished;
    private boolean parsingUpdated;
    private boolean parsingCategory;
    private boolean parsingTitle;
    private boolean parsingSummary;
    private boolean parsingContent;
    private boolean parsingLink;
    private boolean parsingLinkSelf;
    private boolean parsingName;
    private boolean parsingEmail;
    private  Attributes currElement_attrLocal;
    private boolean proceedToParsing = false;

    private String attrUrl="";   private String attrUrl2="";
    private int curLinkPars = 0;



    public GCalendarRssParseHandler() {
        rssItems = new ArrayList();
    }

    // We have an access method which returns a list of items that are read from the RSS feed. This method will be called when parsing is done.
    public List<FeedEntry> getItems() {
        return rssItems;
    }

    // The StartElement method creates an empty RssItem object when an item start tag is being processed. When a title or link tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        attrUrl="";
        attrUrl2="";

        currElement_attrLocal = attributes;
        if("entry".equals(qName) && rssItems.size() == 0) //first "<.?.>" valid element, next is all good for parsing so seting up a flag to mark that
            proceedToParsing = true;

        if(proceedToParsing) { // no feed staff no more, parse rest
            if ("entry".equals(qName)) {
                //currentItem =  new FeedEntry(String.valueOf(br), "titleDef", "descDef", "urlStringDef");currentItem = new FeedEntry();
                currentItem =  new FeedEntry();
                currentItem.setEntryFromRss(String.valueOf(br), "titleDef", "descDef", "urlStringDef");
                br++;
            } else if ("id".equalsIgnoreCase(qName))
                parsingGuid = true;
            else if ("published".equalsIgnoreCase(qName))
                parsingPublished = true;
            else if ("updated".equalsIgnoreCase(qName))
                parsingUpdated = true;
            else if ("category".equalsIgnoreCase(qName))
                parsingCategory = true;
            else if ("title".equalsIgnoreCase(qName))
                parsingTitle = true;
            else if ("summary".equalsIgnoreCase(qName))
                parsingSummary = true;
            else if ("content".equalsIgnoreCase(qName))
                parsingContent = true;
            else if ("link".equalsIgnoreCase(qName) && attributes.getValue("rel").equals("alternate")) {
                this.attrUrl = attributes.getValue("href");
                curLinkPars = 1;
                parsingLink = true;
            }
            else if ("link".equalsIgnoreCase(qName) && attributes.getValue("rel").equals("self")) {
                this.attrUrl2 = attributes.getValue("href");
                curLinkPars = 2;
                parsingLinkSelf = true;
            }
            else if ("name".equalsIgnoreCase(qName))
                parsingName = true;
            else if ("email".equalsIgnoreCase(qName))
                parsingEmail = true;
        }

    }

    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed.
    // It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        // no feed staff no more, parse rest
        if(proceedToParsing) {
            if ("entry".equals(qName)) {
                rssItems.add(currentItem);
                currentItem = null;
            } else if ("id".equalsIgnoreCase(qName))
                parsingGuid = false;
            else if ("published".equalsIgnoreCase(qName))
                parsingPublished = false;
            else if ("updated".equalsIgnoreCase(qName))
                parsingUpdated = false;
            else if ("category".equalsIgnoreCase(qName))
                parsingCategory = false;
            else if ("title".equalsIgnoreCase(qName))
                parsingTitle = false;
            else if ("summary".equalsIgnoreCase(qName))
                parsingSummary = false;
            else if ("content".equalsIgnoreCase(qName))
                parsingContent = false;
            else if ("link".equalsIgnoreCase(qName) && curLinkPars == 1) {
                currentItem.url = this.attrUrl;
                parsingLink = parsingLinkSelf = false;
            }
            else if ("link".equalsIgnoreCase(qName) && curLinkPars == 2)
                parsingLinkSelf = false;
            else if ("name".equalsIgnoreCase(qName))
                parsingName = false;
            else if ("email".equalsIgnoreCase(qName))
                parsingEmail = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.title = new String(ch, start, length);
        } else if (parsingLink) {

        }else if (parsingSummary) {
            if (currentItem != null) {
                currentItem.desc = new String(ch, start, length);
                parsingSummary = false;
            }
        }else if (parsingContent) {
            if (currentItem != null) {
                currentItem.content = new String(ch, start, length);
                parsingContent = false;
            }
        }else if (parsingGuid) {
            if (currentItem != null) {
                String str = new String(ch, start, length);
                String[] words = str.split("/");
                currentItem.rssItemId = words[words.length-1];
                parsingGuid = false;
            }
        }else if (parsingCategory) {
            if (currentItem != null) {
                String c = currElement_attrLocal.getValue("term");
                String[] words = c.split("#");
                currentItem.category = words[1];
                parsingCategory = false;
            }
        }else if (parsingPublished) {

            if (currentItem != null) {
                String str = new String(ch, start, length);
                String[] words = str.split("T");
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //2014-09-22T10:55:01.000Z
                try {
                    currentItem.date = fmt.parse(words[0] + " " + words[1]);
                } catch (ParseException pe) {}
                parsingPublished = false;
            }
        }else if (parsingName) {
            if (currentItem != null) {
                currentItem.creator = new String(ch, start, length);
                parsingName = false;
            }
        }else if (parsingEmail) {
            if (currentItem != null) {
                currentItem.email = new String(ch, start, length);
                parsingEmail = false;
            }
        }

    }
}
