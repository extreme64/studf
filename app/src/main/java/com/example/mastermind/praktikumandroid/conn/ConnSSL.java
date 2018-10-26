package com.example.mastermind.praktikumandroid.conn;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mastermind.praktikumandroid.notificationServis.CheckForNewOnlineItems_Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Mastermind on 25-Jan-18.
 */

public abstract class ConnSSL extends AsyncTask<String, Void, String> {


    protected static void setParametars_Post( String urlParams, HttpsURLConnection connection) throws ProtocolException {

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setInstanceFollowRedirects(false);    //mora false
        connection.setUseCaches(false);
        connection.setReadTimeout(5000);

        connection.setRequestProperty("Host", "studentskiservis.ict.edu.rs");
        connection.setRequestProperty("Origin", "https://studentskiservis.ict.edu.rs");

        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Cache-Control", "max-age=0");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("DNT", "1");

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.8,el;q=0.6,hr;q=0.4,sr;q=0.2");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp, *;q=0.8");
    }



    public static int urlStatus(String urlToCheck)
    {
        URL url;
        HttpURLConnection conn;
        int retCode = 0;
        try {
            url = new URL(urlToCheck);
            conn = (HttpURLConnection) url.openConnection();
            retCode = conn.getResponseCode();
        }
        catch (Exception e) {}
        return retCode;
    }




    protected static String readResponse(HttpURLConnection conn) throws IOException {
        String reply="";
        BufferedReader in = null;
        try
        {
            InputStream getIs = conn.getInputStream();
            if(conn != null && getIs != null)
            {
                in = new BufferedReader(new InputStreamReader(getIs, StandardCharsets.UTF_8));
                StringBuffer sb = new StringBuffer();

                int chr;
                while ((chr = in.read()) != -1) {
                    sb.append((char) chr);
                }
                reply = sb.toString();
            }
        }
        catch (NullPointerException npe)
        {}
        finally {
            if(in != null)
            {
                in.close();
            }
        }
        return reply;
    }

    public static String readResponseLimited(HttpURLConnection conn, int limit, String patt, String matchCompareTo) throws IOException {
        String reply="";
        BufferedReader in = null;
        StringBuffer sb;
        int sofar = 0;
        String limitBuffer = "";
        try
        {
            InputStream getIs = conn.getInputStream();
            if(conn != null && getIs != null)
            {
                in = new BufferedReader(new InputStreamReader(getIs, StandardCharsets.UTF_8));
                sb = new StringBuffer();

                int chr;
                while ((chr = in.read()) != -1) {
                    if(sofar>60) {
                        String subs = limitBuffer.substring(1);
                        limitBuffer = subs + (char) chr;
                    }else {
                        limitBuffer += (char) chr;
                    }

                    sb.append((char) chr);


                    //"((<pubDate>)([A-z]){1}([a-z][a-z]+)){1}(,){1}\\W([0-9]{2}(\\W)([a-z]{3})(\\W)([0-9]){4}(\\W)(.){0,10}(.){5}(</pubDate>))"
                    Pattern sablonDatuma = Pattern.compile(patt, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Matcher matcher = sablonDatuma.matcher(limitBuffer);
                    if(matcher.find())
                    {
                        String mantStr = matcher.group(0);
                        //Log.i("pubDate mantStr", mantStr);
                        if(mantStr.equals(matchCompareTo)) {  // "<pubDate>Sat, 27 Jan 2018 12:53:28 +0000</pubDate>"
                            sb.append("<"+CheckForNewOnlineItems_Service.OLDITEM_ELEMENT_ADD+">"); // naznaka, da smo prekinuli na pronadjen datum, a ne na kraj stringa, kasnije za ukloniti
                            break;
                        }
                    }

                    if(matcher.find() || sofar == limit)
                        break;
                    sofar++;
                }
                reply = ""+sb;
            }
        }
        catch (NullPointerException npe) { Log.i("NOTIF exc ", npe.toString());}
        finally { if(in != null) {in.close();}}

        return reply;
    }



}
