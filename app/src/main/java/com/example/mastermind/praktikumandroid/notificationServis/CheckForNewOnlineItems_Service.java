package com.example.mastermind.praktikumandroid.notificationServis;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mastermind.praktikumandroid.FeedEntry;
import com.example.mastermind.praktikumandroid.MainActivity;
import com.example.mastermind.praktikumandroid.R;
import com.example.mastermind.praktikumandroid.SettingsActivity;
import com.example.mastermind.praktikumandroid.conn.GetUrlLimited;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Mastermind on 03-Jul-15.
 */
public class CheckForNewOnlineItems_Service extends IntentService {

    SQLiteDatabase lDB;

    List<FeedEntry> obavestenjaITEMS_service = new ArrayList<FeedEntry>();
    String obavestenjaITEMS_lastItemDate_db = "";

    List<FeedEntry> rss_obavestenjaITEMS_service = new ArrayList<FeedEntry>();

    String [] rss_s = {"http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml",
                        "http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml",
                        "http://www.google.com/calendar/feeds/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic"};

    static int SERVICE_ID = 0;
    static int APP_NOTIFICATION_ID = 66778899;
    public static String OLDITEM_ELEMENT_ADD = "oldItemObav";

    public CheckForNewOnlineItems_Service() {
        super("CheckForNewOnlineItems_Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = getApplicationContext();
        String path = context.getDatabasePath("favsDB").getAbsolutePath();
        lDB = SQLiteDatabase.openDatabase(path, null, 0);

        if(lDB != null)
        {
            Cursor c;
            c = lDB.rawQuery("SELECT `vrednost` FROM obavestenjaXmlStatus WHERE `naziv` = 'zadnjaOcitanaStavka' LIMIT 1",  null);
            FeedEntry item;
            while (c.moveToNext()) {
                obavestenjaITEMS_lastItemDate_db = c.getString(0);

            }
        }

        newEntriesCheck("Objave ", 1);
    }


    void newEntriesCheck(String str, int numvalue)
    {

        boolean doWeHaveANotif = false;
        int brojNovihStavkiNaFidu = 0;

        /** ++ DA LI IMA NOVO NESTO ++ */
        // 1. parse link, read up to an element that has newer date then one in DB
        AsyncTask<String, Void, String> fromUrlTask;
        String xmlFromUrl = "";
        Elements tags = new Elements(0);
        String lastItemDate;
        SimpleDateFormat formatkaoSDB = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        SimpleDateFormat formatkaoSURL = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        try {

            /** normalise formats url and DB */

            Date datumSaURL = formatkaoSDB.parse(  obavestenjaITEMS_lastItemDate_db);

            lastItemDate = formatkaoSURL.format(datumSaURL);

            Pattern sablonVremZonaIspravka = Pattern.compile("((?:\\+|-){1}\\d{4})", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher matcher = sablonVremZonaIspravka.matcher(lastItemDate);
            if(matcher.find()) {

                String danUNedelji = lastItemDate.substring(0,3);

                Calendar cal = new GregorianCalendar();
                cal.setTime(datumSaURL);
                // remove diff. time keeping EDT
                if(matcher.group(0).equals("-0500")) { // outside of the zone 2. w. Mart >. 1. w. nov. [-0500] otherwise [-0400]
                    cal.add(Calendar.HOUR, 1);
                }

                DateFormatSymbols dfs = new DateFormatSymbols();
                String[] dfsShortMonths = dfs.getShortMonths();
                String mesec = dfsShortMonths[cal.get(Calendar.MONTH)];

                DecimalFormat saNulom = new DecimalFormat("00"); // single digit have no 0 in front of it
                String normalizovanDatumZaPomeranjeSata = saNulom.format(cal.get(Calendar.DAY_OF_MONTH))
                        + " " + mesec + " "
                        + cal.get(Calendar.YEAR) + " "
                        + saNulom.format(cal.get(Calendar.HOUR_OF_DAY)) + ":"
                        + saNulom.format(cal.get(Calendar.MINUTE)) + ":"
                        + saNulom.format(cal.get(Calendar.SECOND));

                // fin
                lastItemDate = danUNedelji + ", " + normalizovanDatumZaPomeranjeSata + " +0000";
                lastItemDate = "<pubDate>" + lastItemDate + "</pubDate>";
            }

            // read url up to a point of a date from the DB
            String mPatt = "((<pubDate>)([A-z]){1}([a-z][a-z]+)){1}(,){1}\\W([0-9]{2}(\\W)([a-z]{3})(\\W)([0-9]){4}(\\W)(.){0,10}(.){5}(</pubDate>))";
            fromUrlTask = new GetUrlLimited(this, mPatt, lastItemDate).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, "http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml");
            xmlFromUrl = fromUrlTask.get();


            // parse string
            Document doc = Jsoup.parse(xmlFromUrl);
            Elements content = doc.getElementsByTag("channel");
            Element pagesEle = content.first(); // there is just one
            tags = pagesEle.getElementsByTag("pubDate");

            // count 'pubDate' to know many new ones there are
            brojNovihStavkiNaFidu = tags.size();

            // dates match, count one less
            Elements oldItemFlafs = doc.getElementsByTag(OLDITEM_ELEMENT_ADD);
            if(oldItemFlafs.size()>0)
                brojNovihStavkiNaFidu = tags.size() - 1;
        }
        catch (Exception s){ System.out.println(s.toString());}

        // 2. Are there one ones?
        if(brojNovihStavkiNaFidu !=0)
            doWeHaveANotif = true;

        /** ++  ++ */


        /** ++ CREATE NOTIFICATION ++ */

        if(doWeHaveANotif) {

            // 3. define params to create notification with
            if(xmlFromUrl.length() > 0) {
                String seg = xmlFromUrl.substring(xmlFromUrl.indexOf("</title>") + 8);
                String titleString = seg.substring(seg.indexOf("<title>") + 7, seg.indexOf("</title>"));
                str = titleString;
            }

            // build notification, define action on click and start
            PendingIntent contentIntent;
            NotificationCompat.Builder builder;
            builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.spraktikum_launcher)
                    .setContentTitle("Studentfy")
                    .setAutoCancel(true)
                    .setContentText(str)
                    .setNumber(brojNovihStavkiNaFidu);

            Uri ntfySound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(ntfySound);

            //int APP_NOTIFICATION_ID = 66778899;
            Intent targetIntent = new Intent(this, MainActivity.class);
            contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nManager.notify(APP_NOTIFICATION_ID, builder.build());


            /** notifikacija belezenje, iz servisa notifikacije
             * newes one to be saved
             * for later usage in comparing dates */
            Element najnovijaStavka = tags.first();
            String najnovijastavkaDatum = najnovijaStavka.text();
            try {
                Date datumNajnovijeStavke = formatkaoSURL.parse(  najnovijastavkaDatum);

                Calendar cal2 = new GregorianCalendar();
                cal2.setTime(datumNajnovijeStavke);

                /** da li je period kada dodajemo jedan sat vise */
                int nedeljaGodine = cal2.get(Calendar.WEEK_OF_YEAR);
                String prvNedNov = "";
                cal2.add(Calendar.HOUR, 5); // 5 je razliku u odnosu na GMT
                if((nedeljaGodine==10 && cal2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY) || nedeljaGodine>=11)
                {
                    if(nedeljaGodine<=40 || (nedeljaGodine==41 && cal2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY))
                    {
                        cal2.add(Calendar.HOUR, -1); // 4 is difference compared to GMT
                    }
                }

                DecimalFormat saNulom = new DecimalFormat("00");
                DateFormatSymbols dfs = new DateFormatSymbols();
                String dan = (dfs.getShortWeekdays())[cal2.get(Calendar.DAY_OF_WEEK)];
                String mesec = (dfs.getShortMonths())[cal2.get(Calendar.MONTH)];
                najnovijastavkaDatum = dan + " " + mesec + " " + saNulom.format(cal2.get(Calendar.DAY_OF_MONTH))  + " "
                        + saNulom.format(cal2.get(Calendar.HOUR_OF_DAY)) + ":" + saNulom.format(cal2.get(Calendar.MINUTE)) + ":"
                        + saNulom.format(cal2.get(Calendar.SECOND)) + " " + "EDT " + saNulom.format(cal2.get(Calendar.YEAR));


                Cursor obavsStavkeCur = lDB.rawQuery("SELECT `vrednost` FROM obavestenjaXmlStatus WHERE `naziv` = 'zadnjaOcitanaStavka' LIMIT 1",  null);
                String datumIzBaze = "";
                while (obavsStavkeCur.moveToNext()) {
                    datumIzBaze = obavsStavkeCur.getString(0);
                }

            }catch (ParseException pe){System.out.println(pe.toString());}


            Cursor locDbCur = lDB.rawQuery("SELECT * FROM obavestenjaXmlStatus WHERE `naziv` = 'zadnjaOcitanaStavka' LIMIT 1",  null);
            int stavkaBroj = locDbCur.getCount();
            if(stavkaBroj == 0) {
                lDB.execSQL("INSERT INTO obavestenjaXmlStatus (`naziv`,`vrednost`) VALUES ('zadnjaOcitanaStavka', '" + najnovijastavkaDatum + "' )");
            }
            else if(stavkaBroj == 1) {
                lDB.execSQL("UPDATE obavestenjaXmlStatus  SET `vrednost` = '" + najnovijastavkaDatum + "'  WHERE naziv = 'zadnjaOcitanaStavka' ");
            }
        }
        /** ++  ++ */
        SERVICE_ID++;



        /** ++ CHECKING FOR NEW STUFF ++ */
        // get settings values
        int autocheckinterval=0;
        Cursor c = lDB.rawQuery("SELECT * FROM settings WHERE `key`='autocheckinterval' ;", null);
        if(c.moveToFirst())
            autocheckinterval = Integer.valueOf(c.getString(2));

        long endTime = System.currentTimeMillis() + (SettingsActivity.INTERVAL_OFFSET + autocheckinterval) * 1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {

                    // wait interval before recheck
                    wait(endTime - System.currentTimeMillis());

                    newEntriesCheck(str, brojNovihStavkiNaFidu);

                } catch (Exception e) {
                    Log.e(" Exception @ service ", e.getMessage().toString());
                }
            }
        }
        /** ++  ++ */

    }
}
