package com.example.mastermind.praktikumandroid;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//import android.support.v7.app.NotificationCompat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.support.v4.app.NotificationCompat;

import com.example.mastermind.praktikumandroid.ical.CalEntry;
import com.example.mastermind.praktikumandroid.rss.RssEntry;
import com.example.mastermind.praktikumandroid.rss.RssReader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mastermind on 03-Jul-15.
 */
public class CheckServerService extends IntentService {

    SQLiteDatabase lDB;
    int diffFeed_DB;

    List<NotfTest> obavestenjaITEMS_service = new ArrayList<NotfTest>();
    List<NotfTest> najaveITEMS_service = new ArrayList<NotfTest>();
    List<NotfTest> kalendarITEMS_service = new ArrayList<NotfTest>();

    List<NotfTest> rss_obavestenjaITEMS_service = new ArrayList<NotfTest>();
    List<NotfTest> rss_najaveITEMS_service = new ArrayList<NotfTest>();
    List<NotfTest> rss_kalendarITEMS_service = new ArrayList<NotfTest>();

    int check_interval_factor = 20; //10 x 1000 mils
    String [] rss_s = {"http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml",
                        "http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml",
                        "http://www.google.com/calendar/feeds/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic"};

    static int SERVICE_NOTIFICATION_ID = 0;
    private boolean loop = true;



    public CheckServerService() {
        super("HelloIntentService");


    }




    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();
        String path = context.getDatabasePath("favsDB").getAbsolutePath();
        lDB = SQLiteDatabase.openDatabase(path, null, 0);

        // ###

        long endTime = System.currentTimeMillis() + 3*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }

       if(lDB != null)
        {
            //iz tabela u bazi popuni liste za sve tabove
            Cursor c;
            c = lDB.rawQuery("SELECT * FROM obavestenja", null);
            NotfTest item;
            while (c.moveToNext()) {
                item = new NotfTest();
                if (item != null) {
                    item.id = String.valueOf(c.getInt(0));
                    item.rssItemId = c.getString(1);
                    obavestenjaITEMS_service.add(item);
                }
            }

            c = lDB.rawQuery("SELECT * FROM najave", null);
            while (c.moveToNext()) {
                item = new NotfTest();
                if (item != null) {
                    item.id = String.valueOf(c.getInt(0));
                    item.rssItemId = c.getString(1);
                    najaveITEMS_service.add(item);
                }
            }

            c = lDB.rawQuery("SELECT * FROM kalendar", null);
            while (c.moveToNext()) {
                item = new NotfTest();
                if (item != null) {
                    item.id = String.valueOf(c.getInt(0));
                    item.rssItemId = c.getString(1);
                    kalendarITEMS_service.add(item);
                }
            }
        }


        //uzmi linkove i povuci sa fidova stavke
        RssReader rssReader;
        try{

            rssReader = new RssReader(rss_s[0]);
            rssReader.setHandler(RssReader.GENERIC_HANDLER);
            rss_obavestenjaITEMS_service = rssReader.getRssItems(); // ako feed dostupan, citamo sa njega

            rssReader = new RssReader(rss_s[1]);
            rssReader.setHandler(RssReader.GENERIC_HANDLER);
            rss_najaveITEMS_service = rssReader.getRssItems(); // ako feed dostupan, citamo sa njega


            rssReader = new RssReader(rss_s[2]);
            rssReader.setHandler(RssReader.GOOGLEcal_HANDLER);
            rss_kalendarITEMS_service = rssReader.getRssItems(); // ako feed dostupan, citamo sa njega

        }
        catch (Exception e)  { Log.e("ITCRssReader", e.getMessage()); }


        //uporedi liste, ako ima novih, izbroj razliku
       boolean foundfirstHit = false;
        int notHit=0;
        int loopDone=0;

        for(NotfTest rssItem : rss_obavestenjaITEMS_service)
        {
            for(NotfTest dbItem : obavestenjaITEMS_service)
            {
                if(rssItem.rssItemId.equals(dbItem.rssItemId))
                    foundfirstHit = true;
            }
            if(!foundfirstHit)
                notHit++;
            loopDone++;
        }
        if(foundfirstHit==false)
            notHit=0;


        foundfirstHit = false;
        int notHit1=0;
        loopDone=0;
        for(NotfTest rssItem : rss_najaveITEMS_service)
        {
            for(NotfTest dbItem : najaveITEMS_service)
            {
                if(rssItem.rssItemId.equals(dbItem.rssItemId))
                     foundfirstHit = true;
            }
            if(!foundfirstHit)
                notHit1++;
            loopDone++;
        }
        if(foundfirstHit==false)
            notHit1=0;


        foundfirstHit = false;
        int notHit2=0;
        loopDone=0;
        for(NotfTest rssItem : rss_kalendarITEMS_service)
        {
            for(NotfTest dbItem : kalendarITEMS_service)
            {
                if(rssItem.rssItemId.equals(dbItem.rssItemId))
                    foundfirstHit = true;
            }
            if(!foundfirstHit)
                notHit2++;
            loopDone++;
        }
        if(foundfirstHit==false)
            notHit2=0;



        helpNotBuilder("Obave. "+ notHit +" / Najav. "+ notHit1 +" / Kalen. "+ notHit2, notHit + notHit1 + notHit2);




        //this.stopSelf();
    }


    void helpNotBuilder(String str, int numvalue)
    {

        // bilduj notifikaciju definisi akciju na klik i pokrenije

        PendingIntent contentIntent;
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.spraktikum_launcher)
                .setContentTitle("Studentfy status")
                .setAutoCancel(true)
                .setContentText(str)
                .setNumber(numvalue);
        if(numvalue>0) {
            Uri ntfySound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(ntfySound);
        }
        SERVICE_NOTIFICATION_ID++;
        int NOTIFICATION_ID = 66778899;

        Intent targetIntent = new Intent(this, MainActivity.class);
        contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(NOTIFICATION_ID, builder.build());




        // citaj podesavanja iz baze
        int tmpInt=0;
        Cursor c = lDB.rawQuery("SELECT * FROM settings WHERE `key`='autocheckinterval' ;", null);
        if(c.moveToFirst())
            tmpInt = Integer.valueOf(c.getString(2));


        long endTime = System.currentTimeMillis() + (SettingsActivity.INTERVAL_OFFSET + tmpInt) * 1000; //
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                    helpNotBuilder(str, numvalue);
                    Log.v(" >>>>>>>>>>> rPlay ", "" + endTime + " / "+String.valueOf(System.currentTimeMillis()));
                } catch (Exception e) {
                    Log.e(" Exception @ service ", e.getMessage().toString());
                }
            }
        }

    }



}
