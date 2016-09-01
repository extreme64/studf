package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;

import com.example.mastermind.praktikumandroid.ical.CalEntry;
import com.example.mastermind.praktikumandroid.ical.ICalWrap;
import com.example.mastermind.praktikumandroid.rss.RssEntry;
import com.example.mastermind.praktikumandroid.rss.RssReader;


import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.util.Log;
import android.widget.Toast;


/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MainItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MainFragment extends ListFragment {

    Activity mActivity;
    MainScreenAdapter mcAdapter;
    /** tabs local heap lists */

    //TODO: preinaciti liste da ih cine objekti novo rasclanjenih klasa, RssEntry i CalEntry
    public static List<RssEntry> obavestenjaITEMS = new ArrayList<RssEntry>();
    public static List<RssEntry> najaveITEMS = new ArrayList<RssEntry>();
    public static List<CalEntry> kalendarITEMS = new ArrayList<CalEntry>();
    public static List<Entry> favsLocal_DB_ITEMS = new ArrayList<Entry>();
    // ### #### ###
    public static List<NotfTest> NotfTestITEMS = new ArrayList<NotfTest>();

    public static String rss_link = "http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml";
    ListView defaultListView;
    public Activity local;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;
    public static String PAGE_ID = "";

    public int mNum;
    private static String LINK_STR_RSS_OBAVESTENJA = "http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml";
    private static String LINK_STR_RSS_NAJAVE = "http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml";
    private static String LINK_STR_RSS_KALENDAR = "http://www.google.com/calendar/feeds/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic";
    //TODO: izmeniti link za dohvatanje falja kalendara 'https://calendar.google.com/calendar/ical/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic.ics'


    private int rssReaderOkFlag; //pokazuje da li je link dobar i da je konekcija ostvarena

    SQLiteDatabase lDB = MainActivity.DB;
    /**
     * A callback interface that all activities containing this fragment must implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {

        RssEntry r = new RssEntry("0", "C#2", "Ispit C#2 popravni", "");
        List<Entry> en = null;
        en.add(r);

        /*favsLocal_DB_ITEMS.add(new NotfTest("0", "C#2", "Ispit C#2 popravni", ""));
        favsLocal_DB_ITEMS.add(new NotfTest("1", "C#1 ", "Ispit C#1 zadatak vezba", ""));
        favsLocal_DB_ITEMS.add(new NotfTest("2", "Rezultati", "Kolokvijum  lista bodova", ""));
        favsLocal_DB_ITEMS.add(new NotfTest("3", "Ispiti prijava", "Prijava  20/21. jun", ""));
        favsLocal_DB_ITEMS.add(new NotfTest("4", "Rok jun", "Ispitni rok pocinje 22. juna", ""));
        favsLocal_DB_ITEMS.add(new NotfTest("5", "Rok jun kraj", "Ispitni rok zavrsava 05. jula", ""));*/
    }

    public void clearFavsList()
    {   favsLocal_DB_ITEMS.clear();}

    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static MainFragment newInstance(int num) {
        MainFragment mf = new MainFragment(); //TODO: novi konstruktor sa parametrom za - int num, eliminisati potrebu za prosledjivanjem sa argumentima
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        String lStrRss;
        switch (num)
        {
            case 0:
                lStrRss = LINK_STR_RSS_OBAVESTENJA;
                break;
            case 1:
                lStrRss = LINK_STR_RSS_NAJAVE;
                break;
            case 2:
                lStrRss = LINK_STR_RSS_KALENDAR;
                break;
            case 3:
                args.putBoolean("favs", true);
                lStrRss = "";
                break;

            default: lStrRss = LINK_STR_RSS_OBAVESTENJA;
        }
        args.putString("rss_link_arg", lStrRss);
        mf.setArguments(args);
        return mf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mNum = getArguments() != null ? getArguments().getInt("num") : 1;


        // Set reference to this activity
        mActivity = local = this.getActivity();
        GetRSSDataTask task = new GetRSSDataTask();

        Bundle bundArgs = new Bundle();
        bundArgs = this.getArguments();
        String tmp = bundArgs.getString("rss_link_arg");
        boolean doesFavs = bundArgs.getBoolean("favs");

        //read DB and pump into the list
        //to save space use helper proc.
        // we use this one also when new fav added from listView
        syncFavsListToDB();

        //shoud we do favs or not
        if(!doesFavs)
        {
            if(tmp!=null){  task.execute(tmp); }// read feeds, favs tab NOT selected
        }
        else
        {
            mcAdapter = new MainScreenAdapter(local, favsLocal_DB_ITEMS); //rssReader.getItems()
            mcAdapter.setTab(mNum);
            setListAdapter(mcAdapter);
        }
        // Debug the thread name
        //Log.d("Rss Reader", Thread.currentThread().getName());

    }




    public void reFresh()
    {
        Activity l = getActivity();
        if(local==null)
            local = l;
        mcAdapter = new MainScreenAdapter(local, favsLocal_DB_ITEMS);
        mcAdapter.setTab(mNum);
        setListAdapter(mcAdapter);
        syncFavsListToDB();
        GetRSSDataTask task = new GetRSSDataTask();

        Bundle bundArgs = new Bundle();
        bundArgs = this.getArguments();
        String tmp = bundArgs.getString("rss_link_arg");

        if(tmp!=null){  task.execute(tmp); }// read feeds, favs tab NOT selected
    }

    //TODO: srediti sinhronizaciju fav liste
    public static boolean syncFavsListToDB() {
       /* SQLiteDatabase lDB = MainActivity.DB;
        Cursor c;
        c = lDB.rawQuery("SELECT * FROM favs;", null);
        int size = c.getCount();
        if (size == 0) {
            //return; // !!
        }
        favsLocal_DB_ITEMS.clear(); // !
        while (c.moveToNext()) {
            NotfTest dbitem = new NotfTest();
            dbitem = pumpItem(dbitem, c);
            favsLocal_DB_ITEMS.add(dbitem);
        }*/
        return true;
    }


    private static RssEntry pumpItem(RssEntry i, Cursor c)
    {
        RssEntry item = i;
        if(item!=null)
        {
            item.id = String.valueOf(c.getInt(0));
            item.title = c.getString(2);
            item.desc = c.getString(3);
            item.url = c.getString(5);

            item.rssItemId = c.getString(1);
            item.content = c.getString(4);
            item.creator = c.getString(6);
            item.email = c.getString(7);
            item.category = c.getString(8);

            String dateString1;
            String dateString2;
            dateString1 = c.getString(9);
            dateString2 = c.getString(10);
            SimpleDateFormat fmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
            try {
                item.date = fmt.parse(dateString1);
                item.dateUpdate = fmt.parse(dateString2);
            } catch (Exception e) {
            }
            item.fromTab = c.getString(11); // from what tab we have saved this one
        }
        if(item==null)
            return new RssEntry();
        return item;
    }

    private static CalEntry pumpCalItem(CalEntry i, Cursor c)
    {
        CalEntry item = i;
        if(item!=null)
        {
            item.id = String.valueOf(c.getInt(0));
          /*  item.title = c.getString(2);
            item.desc = c.getString(3);
            item.url = c.getString(5);*/

            item.rssItemId = c.getString(1);
           /* item.content = c.getString(4);
            item.creator = c.getString(6);
            item.email = c.getString(7);
            item.category = c.getString(8);*/

            String dateString1;
            String dateString2;
            dateString1 = c.getString(9);
            dateString2 = c.getString(10);
            SimpleDateFormat fmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
            try {
               /* item.date = fmt.parse(dateString1);
                item.dateUpdate = fmt.parse(dateString2);*/
            } catch (Exception e) {
            }
            item.fromTab = c.getString(11); //TODO from what tab we have saved this one
        }
        if(item==null)
            return new CalEntry();
        return item;
    }
   /* private static NotfTest pumpItem(NotfTest i, Cursor c)
    {
        NotfTest item = i;
        if(item!=null)
        {
            item.id = String.valueOf(c.getInt(0));
            item.title = c.getString(2);
            item.desc = c.getString(3);
            item.url = c.getString(5);

            item.rssItemId = c.getString(1);
            item.content = c.getString(4);
            item.creator = c.getString(6);
            item.email = c.getString(7);
            item.category = c.getString(8);

            String dateString1;
            String dateString2;
            dateString1 = c.getString(9);
            dateString2 = c.getString(10);
            SimpleDateFormat fmt = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
            try {
                item.date = fmt.parse(dateString1);
                item.dateUpdate = fmt.parse(dateString2);
            } catch (Exception e) {
            }
            item.fromTab = c.getString(11); // from what tab we have saved this one
        }
        if(item==null)
            return new NotfTest();
        return item;
    }*/




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(String.valueOf(position)); //NotfTestITEMS.get(position).id
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        mActivatedPosition = position;
    }

    //      ##########   INNER CLASSES #########

            private class GetRSSDataTask extends AsyncTask<String, Void, List<RssEntry> > {

                @Override
                protected List<RssEntry> doInBackground(String... urls) {

                    try {
                        // Create RSS reader
                        RssReader rssReader = new RssReader(urls[0], mActivity);

                        // Parse RSS, get items
                        switch (mNum)
                        {
                            case 0:
                                rssReader.setHandler(RssReader.GENERIC_HANDLER);
                                //obavestenjaITEMS = rssReader.getItems(); // ako feed dostupan, citamo sa njega
                                obavestenjaITEMS = rssReader.getRssItems();
                                return obavestenjaITEMS;
                            case 1:
                                rssReader.setHandler(RssReader.GENERIC_HANDLER);
                                //return najaveITEMS = rssReader.getItems();
                                return najaveITEMS = rssReader.getRssItems();
                            case 2:

                            default:
                                //TODO: da li nam ovo treba? trenutno ima error!
                               /* MainActivity parAct = (MainActivity)getActivity();
                                ViewPager mainViewPager = parAct.getViewPager();
                                if(mainViewPager!=null) {
                                    mainViewPager.setCurrentItem(3);
                                    getActivity().getActionBar().setSelectedNavigationItem(3);
                                }*/
                        }
                    }
                    catch (Exception e)  { Log.e("ITCRssReader", e.getMessage()); }
                    return null;
                }

                @Override
                protected void onPostExecute(List<RssEntry> result) {

                  /*  SQLiteDatabase lDB = MainActivity.DB;
                    Cursor c;

                    if(result.size() == 0)
                    {
                        //pokusaj citati iz baze, u nadi da ima sta snimljeno
                        // tabela koja se cita se odredjuje na osnovu vrednosti - mNum
                        if(result.size()==0) //ako nista sa fida, pokusajmo uzeti sta ima u bazi
                        {
                            int size;
                            switch (mNum) {

                                case 0:
                                    c = lDB.rawQuery("SELECT * FROM obavestenja;", null);
                                    size = c.getCount();
                                    while(c.moveToNext())
                                    {
                                        NotfTest item = new NotfTest();
                                        pumpItem(item, c);
                                        result.add(item);
                                    }
                                    break;
                                case 1:
                                    c = lDB.rawQuery("SELECT * FROM najave;", null);
                                    while(c.moveToNext())
                                    {
                                        NotfTest item = new NotfTest();
                                        pumpItem(item, c);
                                        result.add(item);
                                    }
                                    break;
                                case 2:
                                    c = lDB.rawQuery("SELECT * FROM kalendar;", null);
                                    while(c.moveToNext())
                                    {
                                        NotfTest item = new NotfTest();
                                        pumpItem(item, c);
                                        result.add(item);
                                    }
                                    break;
                                default:
                            }
                        }
                        //obavestenjaITEMS = rssReader.getItems();
                    }
                    else
                    {
                        //upis u bazu novih item-a iz fida
                        // tabela u koju se zapisuje se odredjuje na osnovu vrednosti - mNum
                        if(lDB == null)
                            return; // mesage "nema baze za pristupiti"
                        NotfTest item_r = new NotfTest();
                        int size = -1;
                        switch (mNum) {
                            case 0:

                                c = lDB.rawQuery("SELECT `id` FROM obavestenja;", null);
                                size = c.getCount();
                                if (size > 0)
                                    lDB.execSQL("delete from obavestenja"); //obrisi staro
                                for (int i = 0; i < ((result.size() > 4) ? 4 : result.size()); i++) {
                                    item_r = result.get(i);
                                    lDB.execSQL("INSERT INTO obavestenja (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                            + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                            + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString()
                                            + "' , '" + String.valueOf(mNum) + "' ); ");
                                }

                                break;
                            case 1:
                                c = lDB.rawQuery("SELECT `id` FROM najave;", null);
                                size = c.getCount();
                                if(size>0)
                                    lDB.execSQL("delete from najave"); //obrisi staro
                                int limit = (result.size() > 4) ?  4 : result.size();
                                for (int i=0; i<limit; i++) {
                                    item_r = result.get(i);
                                    lDB.execSQL("INSERT INTO najave (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                            + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                            + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString()
                                            + "' , '" + String.valueOf(mNum) + "' ); ");
                                }
                                break;
                            case 2:
                                c = lDB.rawQuery("SELECT `id` FROM kalendar;", null);
                                size = c.getCount();
                                if(size>0)
                                    lDB.execSQL("delete from kalendar"); //obrisi staro
                                for (int i=0; i<((result.size() > 4) ?  4 : result.size()); i++) {
                                    item_r = result.get(i);
                                    lDB.execSQL("INSERT INTO kalendar (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                            + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                            + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString() + "' , '"
                                            + String.valueOf(mNum) + "' ); ");
                                }
                                break;
                        }
                    }

                    if(local==null)
                        return;
                    mcAdapter = new MainScreenAdapter(local, result);
                    mcAdapter.setTab(mNum);
                    setListAdapter(mcAdapter);*/

                }
            }

    //      ##########   INNER CLASSES #########

    private class GetICAlDataTask extends AsyncTask<String, Void, List<CalEntry> > {

        @Override
        protected List<CalEntry> doInBackground(String... urls) {

            try {
                // Create RSS reader
                ICalWrap iCalReader = new ICalWrap();


                iCalReader.init(new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                });
                //return kalendarITEMS = rssReader.getItems();
                return kalendarITEMS = iCalReader.getItems();

                //TODO: da li nam ovo treba? trenutno ima error!
                       /* MainActivity parAct = (MainActivity)getActivity();
                        ViewPager mainViewPager = parAct.getViewPager();
                        if(mainViewPager!=null) {
                            mainViewPager.setCurrentItem(3);
                            getActivity().getActionBar().setSelectedNavigationItem(3);
                        }*/

            }
            catch (Exception e)  { Log.e("ITCRssReader", e.getMessage()); }
            return null;
        }

        @Override
        protected void onPostExecute(List<CalEntry> result) {

           /* SQLiteDatabase lDB = MainActivity.DB;
            Cursor c;

            if(result.size() == 0)
            {
                //pokusaj citati iz baze, u nadi da ima sta snimljeno
                // tabela koja se cita se odredjuje na osnovu vrednosti - mNum
                if(result.size()==0) //ako nista sa fida, pokusajmo uzeti sta ima u bazi
                {
                    int size;
                    switch (mNum) {

                        case 0:
                            c = lDB.rawQuery("SELECT * FROM obavestenja;", null);
                            size = c.getCount();
                            while(c.moveToNext())
                            {
                                NotfTest item = new NotfTest();
                                pumpItem(item, c);
                                result.add(item);
                            }
                            break;
                        case 1:
                            c = lDB.rawQuery("SELECT * FROM najave;", null);
                            while(c.moveToNext())
                            {
                                NotfTest item = new NotfTest();
                                pumpItem(item, c);
                                result.add(item);
                            }
                            break;
                        case 2:
                            c = lDB.rawQuery("SELECT * FROM kalendar;", null);
                            while(c.moveToNext())
                            {
                                NotfTest item = new NotfTest();
                                pumpItem(item, c);
                                result.add(item);
                            }
                            break;
                        default:
                    }
                }*/
                //obavestenjaITEMS = rssReader.getItems();
           /* }
            else
            {*/
                //upis u bazu novih item-a iz fida
                // tabela u koju se zapisuje se odredjuje na osnovu vrednosti - mNum
              /*  if(lDB == null)
                    return; // mesage "nema baze za pristupiti"
                NotfTest item_r = new NotfTest();
                int size = -1;
                switch (mNum) {
                    case 0:

                        c = lDB.rawQuery("SELECT `id` FROM obavestenja;", null);
                        size = c.getCount();
                        if (size > 0)
                            lDB.execSQL("delete from obavestenja"); //obrisi staro
                        for (int i = 0; i < ((result.size() > 4) ? 4 : result.size()); i++) {
                            item_r = result.get(i);
                            lDB.execSQL("INSERT INTO obavestenja (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                    + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                    + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString()
                                    + "' , '" + String.valueOf(mNum) + "' ); ");
                        }

                        break;
                    case 1:
                        c = lDB.rawQuery("SELECT `id` FROM najave;", null);
                        size = c.getCount();
                        if(size>0)
                            lDB.execSQL("delete from najave"); //obrisi staro
                        int limit = (result.size() > 4) ?  4 : result.size();
                        for (int i=0; i<limit; i++) {
                            item_r = result.get(i);
                            lDB.execSQL("INSERT INTO najave (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                    + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                    + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString()
                                    + "' , '" + String.valueOf(mNum) + "' ); ");
                        }
                        break;
                    case 2:
                        c = lDB.rawQuery("SELECT `id` FROM kalendar;", null);
                        size = c.getCount();
                        if(size>0)
                            lDB.execSQL("delete from kalendar"); //obrisi staro
                        for (int i=0; i<((result.size() > 4) ?  4 : result.size()); i++) {
                            item_r = result.get(i);
                            lDB.execSQL("INSERT INTO kalendar (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                    + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                    + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString() + "' , '"
                                    + String.valueOf(mNum) + "' ); ");
                        }
                        break;
                }*/
           // }
/*
            if(local==null)
                return;
            mcAdapter = new MainScreenAdapter(local, result);
            mcAdapter.setTab(mNum);
            setListAdapter(mcAdapter);*/

        }
    }

}