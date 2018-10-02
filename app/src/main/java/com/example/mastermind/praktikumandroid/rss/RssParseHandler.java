package com.example.mastermind.praktikumandroid.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.example.mastermind.praktikumandroid.FeedEntry;

/**
 * Created by Mastermind on 18-Jun-15.
 */

/**
 * SAX tag handler. The Class contains a list of RssItems which is being filled while the parser is working
 * @author ITCuties
 */
public class RssParseHandler extends DefaultHandler {

    // List of items parsed
    private List<FeedEntry> rssItems;




    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing
    private FeedEntry currentItem;
    // We have two indicators which are used to differentiate whether a tag title or link is being processed by the parser

    private int br = 0;

    private boolean parsingTitle; // Parsing title indicator
    private boolean parsingLink; // Parsing link indicator
    private boolean parsingDescription;
    private boolean parsingCategory;
    private boolean parsingPubDate;
    private boolean parsingDcCreator;
    private boolean parsingGuid;





    public RssParseHandler() {
        rssItems = new ArrayList();
    }

    // We have an access method which returns a list of items that are read from the RSS feed.
    // This method will be called when parsing is done.
    public List<FeedEntry> getItems() { return rssItems; }


    // The StartElement method creates an empty RssItem object when an item start tag is being processed.
    // When a title or link tag are being processed appropriate indicators are set to true.
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {


        if ("item".equals(qName) || "entry".equals(qName)) {

            currentItem =  new FeedEntry();
            currentItem.setEntryFromRss(String.valueOf(br), "", "", "");
            br++;
        }
        else if ("title".equals(qName))
            parsingTitle = true;
        else if ("link".equals(qName))
            parsingLink = true;
        else if("description".equals(qName))
            parsingDescription = true;
        else if("category".equals(qName))
            parsingCategory = true;
        else if("pubDate".equals(qName))
            parsingPubDate = true;
        else if("guid".equals(qName))
            parsingGuid = true;
        else if("dc:creator".equals(qName))
            parsingDcCreator = true;
    }


    // The EndElement method adds the  current RssItem to the list when a closing item tag is processed.
    // It sets appropriate indicators to false -  when title and link closing tags are processed
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName) || "entry".equals(qName)) {
            rssItems.add(currentItem);
            currentItem = null;


        }
        else if ("title".equals(qName))
            parsingTitle = false;
        else if ("link".equals(qName))
            parsingLink = false;
        else if("description".equals(qName)) {
            parsingDescription = false;
            if(currentItem!=null) {
                currentItem.desc = cleanDesc(currentItem.desc, "<.*?>").trim(); // html tags out
                currentItem.desc = currentItem.desc.replace("\n", "").trim(); // \n out
                currentItem.desc = currentItem.desc.replace("&nbsp;", "").trim(); // fix '&nbsp;'

                currentItem.desc = currentItem.desc.replace("<!--THIS FILE IS NOT USED AND IS HERE AS A STARTING POINT FOR CUSTOMIZATION ONLY.See http://api.drupal.org/api/function/theme_field/7 for details.After copying this file to your theme's folder and customizing it, remove thisHTML comment.-->","");
                currentItem.desc = currentItem.desc.trim();
            }
        }
        else if("category".equals(qName))
            parsingCategory = false;
        else if("pubDate".equals(qName))
            parsingPubDate = false;
        else if("guid".equals(qName))
            parsingGuid = false;
        else if("dc:creator".equals(qName))
            parsingDcCreator = false;
    }

    // Characters method fills current rss Item object with data when title and link tag content is being processed
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.title += new String(ch, start, length);

        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.url = new String(ch, start, length);
                parsingLink = false;
            }
        }else if (parsingDescription) {
            if (currentItem != null) {
                currentItem.desc += new String(ch, start, length);
            }
        }else if (parsingGuid) {
            if (currentItem != null) {
                String str;
                str = new String(ch, start, length);
                String[] words = str.split(" ");
                currentItem.rssItemId = words[0];
                parsingGuid = false;
            }
        }else if (parsingCategory) {
            if (currentItem != null) {
                currentItem.category = new String(ch, start, length);
                parsingCategory = false;
            }
        }else if (parsingPubDate) {

            if (currentItem != null) {
                String str;
                str = new String(ch, start, length);
                SimpleDateFormat fmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
                try
                {
                   Date date = fmt.parse(str);
                   currentItem.date = date;
                }
                catch (ParseException pe) {}
                parsingPubDate = false;
            }
        }else if (parsingDcCreator) {
            if (currentItem != null) {
                String str = "";
                currentItem.creator = new String(ch, start, length);
                parsingDcCreator = false;
            }
        }
    }

    @Override
    public void skippedEntity (String name)
            throws SAXException {
        System.out.println("Hello from skippedEntity()!");
    }


    /** helps */
    public String cleanDesc(String str, String patn) {
        Pattern patt = Pattern.compile(patn);// <{1}[a-z,\s]*/?[a-z,\s]*>{1} , "&[a-z]+;+"
        String[] splits = patt.split(str);
        List<String> listSplits = new ArrayList<String>( Arrays.asList(splits));
        StringBuilder strbldr = new StringBuilder();
        for( String s : listSplits) {
            strbldr.append(s);
        }
        return strbldr.toString();
    }

}
