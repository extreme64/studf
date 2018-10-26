package com.example.mastermind.praktikumandroid.conn;

import android.content.Context;
import android.os.AsyncTask;

import com.example.mastermind.praktikumandroid.MainFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Acer on 14-Dec-16.
 */
public class GetUrlLimited extends AsyncTask<String, Void, String> {

    protected final Context context;
    protected MainFragment mf;
    protected String reply="";
    protected String pattern;
    protected String matchCompareTo="";
    protected HttpURLConnection connection_statGet;


    public GetUrlLimited(Context context, String p, String comp){
        this.context = context;
        this.pattern = p;
        this.matchCompareTo = comp;}

    public GetUrlLimited(Context c, MainFragment mf, String p, String comp){
        this.context = c;
        this.mf = mf;
        this.pattern = p;
        this.matchCompareTo = comp;
    }


    protected void onPreExecute(){
    }

    protected void onPostExecute(String result){
    }


    @Override
    protected String doInBackground(String... params) {

        final String url_str = params[0];

        try {
            URL url = new URL(url_str);
            connection_statGet = (HttpURLConnection)url.openConnection();

            if(connection_statGet != null) {
                reply += ConnSSL.readResponseLimited(connection_statGet, 50000, pattern, matchCompareTo);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return reply;
    }


}
