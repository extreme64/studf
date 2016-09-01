package com.example.mastermind.praktikumandroid.rss;

import com.example.mastermind.praktikumandroid.NotfTest;

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
    private List<NotfTest> rssItems;
    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing
    private NotfTest currentItem;
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


    {
    /*
        * <entry>
        *     <id>http://www.google.com/calendar/feeds/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic/53mgifdl7j2n8405srlg4olsps</id>
        *     <published>2014-09-22T10:55:01.000Z</published>
        *     <updated>2015-01-04T13:47:14.000Z</updated>
        *     <category scheme='http://schemas.google.com/g/2005#kind' term='http://schemas.google.com/g/2005#event'/>
        *     <title type='html'>Зимски распуст-први дан</title>
        *     <summary type='html'>When: Mon Dec 29, 2014&lt;br&gt; &lt;br&gt;Event Status: confirmed</summary>
              <content type='html'>When: Mon Dec 29, 2014&lt;br /&gt; &lt;br /&gt;Event Status: confirmed</content>
              <link rel='alternate' type='text/html' href='http://www.google.com/calendar/event?eid=NTNtZ2lmZGw3ajJuODQwNXNybGc0b2xzcHMgaWN0LmVkdS5yc19xaW9naWtyb3Q0Y25zamJ1Z25ucDAyazYwY0Bn' title='alternate'/>
              <link rel='self' type='application/atom+xml' href='http://www.google.com/calendar/feeds/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic/53mgifdl7j2n8405srlg4olsps'/>
              <author>
                <name>Dušan Stojanovic</name>
                <email>dule@ict.edu.rs</email>
              </author>
         </entry>
    * */
    }


    public GCalendarRssParseHandler() {
        rssItems = new ArrayList();
    }

    // We have an access method which returns a list of items that are read from the RSS feed. This method will be called when parsing is done.
    public List<NotfTest> getItems() {
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
                currentItem = new NotfTest(String.valueOf(br), "titleDef", "descDef", "urlStringDef");
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

    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed. It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(proceedToParsing) { // no feed staff no more, parse rest
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
        if (parsingTitle) { //
            if (currentItem != null)
                currentItem.title = new String(ch, start, length);
        } else if (parsingLink) { //
            /*if (currentItem != null) {
                currentItem.url = this.attrUrl; //currElement_attrLocal.getValue("href");
                parsingLink = false;
            }*/
        }else if (parsingSummary) { //
            if (currentItem != null) {
                currentItem.desc = new String(ch, start, length);
                parsingSummary = false;
            }
        }else if (parsingContent) { //
            if (currentItem != null) {
                currentItem.content = new String(ch, start, length);
                parsingContent = false;
            }
        }else if (parsingGuid) {
            if (currentItem != null) { //
                String str = new String(ch, start, length);
                String[] words = str.split("/");
                currentItem.rssItemId = words[words.length-1];
                parsingGuid = false;
            }
        }else if (parsingCategory) { //
            if (currentItem != null) {
                String c = currElement_attrLocal.getValue("term");
                String[] words = c.split("#");
                currentItem.category = words[1];
                parsingCategory = false;
            }
        }else if (parsingPublished) {//

            if (currentItem != null) {
                String str = new String(ch, start, length);
                String[] words = str.split("T");
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //2014-09-22T10:55:01.000Z
                try {
                    currentItem.date = fmt.parse(words[0] + " " + words[1]);
                } catch (ParseException pe) {}
                parsingPublished = false;
            }
        }else if (parsingName) { //
            if (currentItem != null) {
                currentItem.creator = new String(ch, start, length);
                parsingName = false;
            }
        }else if (parsingEmail) { //
            if (currentItem != null) {
                currentItem.email = new String(ch, start, length);
                parsingEmail = false;
            }
        }

    }
}
