package com.example.mastermind.praktikumandroid;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mastermind.praktikumandroid.conn.PostConnSSL;
import com.example.mastermind.praktikumandroid.notificationServis.CheckForNewOnlineItems_Service;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity
        implements MainFragment.Callbacks
{


    public  static int CURRENT_TAB_SELECTED = 0;
    public  static int PREV_TAB = 0;
    public  static int LAST_SS_TAB_SELECTED = 4;

    public  static int LAST_TAB_VIEWED;
    public  static HashMap<String, MainFragment> mf_hm = new HashMap<String, MainFragment>();

    private boolean mTwoPane;
    public Toast toast;



    /* brojs tranica aplikacije, tj. CollectionPagerAdapter-a */

    // ime tab za svaku od sranica adaptera 'CollectionPagerAdapter'
    String [] tabNames = { "Obaveštenja", "Najave", "Kalendar", "Sačuvano", "Student Serv."};

    CollectionPagerAdapter mCollectionPagerAdapter;
    public static CollectionPagerAdapter vp_adapter;
    public static TabLayout tabLayoutMain, tabLayoutSS;
    public static ViewPager mViewPager;




    public static SQLiteDatabase DB;
    public static final int NUM_ITEMS_IN_DB = 4;
    public static final int TAB_1 = 0, TAB_2 = 1, TAB_3 = 2, TAB_4 = 3, TAB_5_1 = 4, TAB_5_2 = 5, TAB_5_3 = 6;


    public static CookieManager cookieM;


    public static ViewPager getViewPager()
    {
        if(mViewPager != null)
            return mViewPager;
        return null;
    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        MainActivity.CURRENT_TAB_SELECTED = 0;


        /** toolbar actionbar tab
         * tabovi za selekciju stranice viewpager-a
         * */
        //get toolbar view to get ref.
        Toolbar toolbar_main = (Toolbar) findViewById(R.id.toolbarMain);
        //set toolbar_main to act as actionbar
        setSupportActionBar(toolbar_main);

        // options for toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // tabs for tablayout
        tabLayoutMain = (TabLayout) findViewById(R.id.TabLayoutMain);
        // add tabs to tab layout
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[0]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[1]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[2]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[3]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[4]));
        tabLayoutMain.setTabGravity(TabLayout.GRAVITY_FILL);



        // tabs for stud. serv. prebacivanje
        tabLayoutSS = (TabLayout) findViewById(R.id.TabLayout_SS_Opcije);
        // add tabs to tab layout
        tabLayoutSS.addTab(tabLayoutSS.newTab().setText("PREDMETI"));
        tabLayoutSS.addTab(tabLayoutSS.newTab().setText("ISPITI"));
        tabLayoutSS.addTab(tabLayoutSS.newTab().setText("FINANSIJE"));
        tabLayoutSS.setTabGravity(TabLayout.GRAVITY_FILL);



        // Now for the ViewPager
        mViewPager = (ViewPager)findViewById(R.id.ViewPagerMain);

        //TB. final MainPager_Adapter vp_adapter = new MainPager_Adapter(getSupportFragmentManager(), tabLayoutMain.getTabCount());
        vp_adapter = new CollectionPagerAdapter(getSupportFragmentManager(), tabLayoutMain.getTabCount() + tabLayoutSS.getTabCount() - 1);
        mViewPager.setAdapter(vp_adapter);
        mViewPager.setOffscreenPageLimit(6);




        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain));


        tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(LAST_SS_TAB_SELECTED >= TAB_5_1 && CURRENT_TAB_SELECTED == TAB_5_1)
                    PREV_TAB = LAST_SS_TAB_SELECTED;
                else
                    PREV_TAB = CURRENT_TAB_SELECTED;


                /* osiguravamo selektovanje pravog taba (zadnjeg odabranog), prilikom rotacije */
                CURRENT_TAB_SELECTED = tab.getPosition();
                if(LAST_SS_TAB_SELECTED >= TAB_5_1 && CURRENT_TAB_SELECTED == TAB_5_1) {
                    // trenutno selektovani je zapravo SS tab fragment, gledan zadnji puta
                    CURRENT_TAB_SELECTED = LAST_SS_TAB_SELECTED;
                    // selektujemo tab UI
                    int toSel = (LAST_SS_TAB_SELECTED - 4 < 0) ? 0 : LAST_SS_TAB_SELECTED - 4;
                    TabLayout.Tab tabSelected = tabLayoutSS.getTabAt(toSel);
                    tabSelected.select();
                }

                mViewPager.setCurrentItem(CURRENT_TAB_SELECTED);
                MainActivity ma = MainActivity.this;
                ma.anyTabSelected(CURRENT_TAB_SELECTED);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        /** student servis pW */
        tabLayoutSS.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /** ofsetujemo za student servis 3 fragmenta, jer 'tabLayoutSS' takodje vraca od nule,
                 * kao i 'tabLayoutMain' pa dolazi do preklapanja */
                int offSetTab=0;

                if(tab.getPosition() == 0) {
                    offSetTab = 4;
                }
                else if(tab.getPosition() == 1) {
                    offSetTab = 5;
                }
                else if(tab.getPosition() == 2) {
                    offSetTab = 6;
                }

                if(LAST_SS_TAB_SELECTED!=0)
                    PREV_TAB = LAST_SS_TAB_SELECTED;
                else
                    if(CURRENT_TAB_SELECTED<4)
                        PREV_TAB = 5;

                LAST_SS_TAB_SELECTED = offSetTab; // koristimo kada kliknemo na SS tab, da pravilno selektujemo frament, koji je bio zadnji put gledan u SS tab meniju


                CURRENT_TAB_SELECTED = offSetTab;
                mViewPager.setCurrentItem(offSetTab);

                MainActivity ma = MainActivity.this;
                ma.anyTabSelected(CURRENT_TAB_SELECTED);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });



        /* ++++ Data Base ++++ */
        DB = openOrCreateDatabase("favsDB", Context.MODE_PRIVATE, null);


        DB.execSQL("CREATE TABLE IF NOT EXISTS favs( " +  "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

        DB.execSQL("CREATE TABLE IF NOT EXISTS obavestenja( " + "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

        DB.execSQL("CREATE TABLE IF NOT EXISTS najave( " + "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

        DB.execSQL("CREATE TABLE IF NOT EXISTS kalendar( " + "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");


        // tabela za zadnju stavku iz liste obavestenja, o kojoj je obavesten korisnik {notifikacija}
        DB.execSQL("CREATE TABLE IF NOT EXISTS obavestenjaXmlStatus (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `naziv` TEXT, `vrednost` TEXT);");

        // tabela za korisnika [log in,...]
        DB.execSQL("CREATE TABLE IF NOT EXISTS `korisnikParam` (" + "`id` INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + "`naziv` TEXT UNIQUE," + "`vrednost` TEXT" + ");");
        //DB.execSQL("INSERT INTO favs (rssItemId, title, desc, content, url, creator, email, category, date,dateUpdate, tab) VALUES ( '1','title@@TestmainAct','desctextDB', 'cont' ,'www//dfd.asda.asd','Neko Nekić','asda@asdas.ss.com','opšte','22-2-2015', '22-2-2015 13:12:22', '0' );");
        //DB.execSQL("delete from favs");

        /* settings table */
        //DB.execSQL("DROP TABLE IF EXISTS settings");
        DB.execSQL("CREATE TABLE IF NOT EXISTS settings( id INTEGER PRIMARY KEY ASC, key VARCHAR, value VARCHAR); ");
        //DB.execSQL("DELETE FROM settings");
        DB.execSQL("REPLACE INTO settings (key, value) VALUES ( 'autocheck', 'true' );");
        DB.execSQL("REPLACE INTO settings (key, value) VALUES ( 'autocheckinterval', '120' );");
        //DB.execSQL("UPDATE settings SET `key`='autocheckinterval', `value`='1444' WHERE id=1 ;");
        //DB.execSQL("UPDATE settings SET `key`='autocheck', `value`='true' WHERE id=0 ;");


        /**
         * citaj podesavanja iz baze, da li da pokrecemo servis, ako vec nije pokrenut
         * ili da ako je pokrenut ga ugasimo ako je tako trenutno podeseno u setting-su
         */
        Cursor c2 = DB.rawQuery("SELECT * FROM settings WHERE `key`='autocheck' ;", null);
        if(c2.moveToFirst())
        {
            // gasimo servis ili,
            // startujemo, ako nam servis vec nije pokrenut
            if(c2.getString(2).equals("false")) {
                boolean stconf = wrapStopService();
            }
            else if(!isMyServiceRunning()) {
                wrapStartService();
            }
        }



        /* postavljanje sistema za staranje o kolacicima konekcija */
        cookieM = new java.net.CookieManager();
        CookieHandler.setDefault(cookieM);
        cookieM.setCookiePolicy(CookiePolicy.ACCEPT_ALL);



        /*** ------------------------------------------------------------------ */
        // setovanje mapa, mapirane vrednosti koristimo za stavke u spinerima stud. serv. lista
        // i vrednosti koje uzimamo za https zahtevime pri filtriranju lsita
        MainFragment.filter1_values_map.putAll(MainFragment.getSpinnerValueMapings(4, 1));
        MainFragment.filter2_values_map.putAll(MainFragment.getSpinnerValueMapings(4, 2));
        MainFragment.filter3_values_map.putAll(MainFragment.getSpinnerValueMapings(4, 3));
        MainFragment.filter21_values_map.putAll(MainFragment.getSpinnerValueMapings(5, 1));
        MainFragment.filter22_values_map.putAll(MainFragment.getSpinnerValueMapings(5, 2));
        MainFragment.filter23_values_map.putAll(MainFragment.getSpinnerValueMapings(5, 3));
        MainFragment.filter31_values_map.putAll(MainFragment.getSpinnerValueMapings(6, 1));
        MainFragment.filter32_values_map.putAll(MainFragment.getSpinnerValueMapings(6, 2));
        MainFragment.filter33_values_map.putAll(MainFragment.getSpinnerValueMapings(6, 3));
        /*** --------------------------------------------------------------------------- */


        /** konekcija za logovanje ne SSl,... */
         AsyncTask logovanje_PostConnSSL = new PostConnSSL(this).execute("https://studentskiservis.ict.edu.rs/pocetna?ReturnUrl=%2fpocetna&loginfailure=1", "0" );

        /* setovanje imena i indeksa studenta */
        Cursor locDbCur = DB.rawQuery("SELECT `vrednost` FROM `korisnikParam` WHERE naziv = 'mejl' LIMIT 1", null);
        if (locDbCur.moveToFirst()) {
            String mejl = locDbCur.getString(0);
            MainFragment.STUDENT_ZAGLAVLJE_TEKST = mejl.replace(".", " ");
        }

    }







    @Override
    protected void onResume()
    {
        super.onResume();

        setContentView(R.layout.activity_main);

        /** toolbar actionbar tab
         * tabovi za selekciju stranice viewpager-a
         * */
        //get toolbar view to get ref.
        Toolbar toolbar_main = (Toolbar) findViewById(R.id.toolbarMain);
        //set toolbar_main to act as actionbar
        setSupportActionBar(toolbar_main);

        // options for toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // tabs for tablayout
        tabLayoutMain = (TabLayout) findViewById(R.id.TabLayoutMain);
        // add tabs to tab layout
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[0]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[1]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[2]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[3]));
        tabLayoutMain.addTab(tabLayoutMain.newTab().setText(tabNames[4]));
        tabLayoutMain.setTabGravity(TabLayout.GRAVITY_FILL);



        // tabs for stud. serv. prebacivanje
        tabLayoutSS = (TabLayout) findViewById(R.id.TabLayout_SS_Opcije);
        // add tabs to tab layout
        tabLayoutSS.addTab(tabLayoutSS.newTab().setText("PREDMETI"));
        tabLayoutSS.addTab(tabLayoutSS.newTab().setText("ISPITI"));
        tabLayoutSS.addTab(tabLayoutSS.newTab().setText("FINANSIJE"));
        tabLayoutSS.setTabGravity(TabLayout.GRAVITY_FILL);


        // Now for the ViewPager
        mViewPager = (ViewPager)findViewById(R.id.ViewPagerMain);

        //TB. final MainPager_Adapter vp_adapter = new MainPager_Adapter(getSupportFragmentManager(), tabLayoutMain.getTabCount());
        vp_adapter = new CollectionPagerAdapter(getSupportFragmentManager(), tabLayoutMain.getTabCount()+tabLayoutSS.getTabCount()-1);
        mViewPager.setAdapter(vp_adapter);
        mViewPager.setOffscreenPageLimit(6);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain));

        tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(LAST_SS_TAB_SELECTED >= TAB_5_1 && CURRENT_TAB_SELECTED == TAB_5_1)
                    PREV_TAB = LAST_SS_TAB_SELECTED;
                else
                    PREV_TAB = CURRENT_TAB_SELECTED;


                CURRENT_TAB_SELECTED = tab.getPosition();
                if(LAST_SS_TAB_SELECTED >= TAB_5_1 && CURRENT_TAB_SELECTED == TAB_5_1) {

                    // trenutno selektovani je zapravo SS tab fragment, gledan zadnji puta
                    CURRENT_TAB_SELECTED = LAST_SS_TAB_SELECTED;
                    // selektujemo tab UI
                    int toSel = (LAST_SS_TAB_SELECTED - 4 < 0) ? 0 : LAST_SS_TAB_SELECTED - 4;
                    TabLayout.Tab tabSelected = tabLayoutSS.getTabAt(toSel);
                    tabSelected.select();
                }

                mViewPager.setCurrentItem(CURRENT_TAB_SELECTED);
                MainActivity ma = MainActivity.this;
                ma.anyTabSelected(CURRENT_TAB_SELECTED);


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        /** student servis pW */
        tabLayoutSS.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /** ofsetujemo za student servis 3 fragmenta, jer 'tabLayoutSS' takodje vraca od nule,
                 * kao i 'tabLayoutMain' pa dolazi do preklapanja */
                int offSetTab=0;

                if(tab.getPosition() == 0) {
                    offSetTab = 4;
                }
                else if(tab.getPosition() == 1) {
                    offSetTab = 5;

                }
                else if(tab.getPosition() == 2) {
                    offSetTab = 6;
                }
                if(LAST_SS_TAB_SELECTED!=0)
                    PREV_TAB = LAST_SS_TAB_SELECTED;
                else
                    if(CURRENT_TAB_SELECTED<4)
                        PREV_TAB = 5;

                LAST_SS_TAB_SELECTED = offSetTab;


                CURRENT_TAB_SELECTED = offSetTab;
                mViewPager.setCurrentItem(offSetTab);

                MainActivity ma = MainActivity.this;
                ma.anyTabSelected(CURRENT_TAB_SELECTED);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        if(MainFragment.tw_prosecnaOcena!= null)
        {
            MainFragment.tw_prosecnaOcena.setText(MainFragment.PROSEK+"");
        }
    }


    public void azurirajListu(View view)
    {
        MainFragment selectedTabFragment = (MainFragment) ((CollectionPagerAdapter) mViewPager.getAdapter()).getItem(CURRENT_TAB_SELECTED);
        selectedTabFragment.azurirajSadrzaj(this);
    }



    public void anyTabSelected(int currentTabIndex) {
        MainFragment selectedTabFragment = (MainFragment) ((CollectionPagerAdapter) mViewPager.getAdapter()).getItem(4);
        selectedTabFragment.adaptSpinners(currentTabIndex - 3);

        if (currentTabIndex < MainActivity.TAB_5_1)
            selectedTabFragment.adaptUIStudServis(false);
        else
            selectedTabFragment.adaptUIStudServis(true);
    }



    /**  /////// servis za NOTIFIKACIJU /////// */
    public void wrapStartService()
    {
        Intent serviceIntent = new Intent("com.example.mastermind.praktikumandroid.CheckServerService");
        this.startService(serviceIntent);
    }
    public boolean wrapStopService()
    {
        if (isMyServiceRunning())
            stopService(new Intent(this, CheckForNewOnlineItems_Service.class));
        return isMyServiceRunning();
        //return  true;
    }
    private boolean isMyServiceRunning() {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CheckForNewOnlineItems_Service.class.getName().equals(service.service.getClassName())) {
                Log.d("Activity", "da radi !!!!");
                return true;
            }
        }
        Log.d("Activity", "NE nismo je nasli !!!!");
        return false;
    }
    /**  ///////  /////// */

    /**
     * To specify the options menu for an activity, override onCreateOptionsMenu()
     * (fragments provide their own onCreateOptionsMenu() callback). In this method,
     * you can inflate your menu resource (defined in XML) into the Menu provided in
     * the callback
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {


            case R.id.action_about:
                Toast.makeText(this, "[Diplomski rad] Info { Android aplikacija 'Studentfy'. Autor - Adam Gičević 317/13 | IT | Visoka ICT. Sep. 2018. }", Toast.LENGTH_LONG).show();
                return true;

                case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // ------

    /**
     * Callback method from {@link MainFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {

        if (mTwoPane) {

            Bundle arguments = new Bundle();
            arguments.putString(MainItemDetailFragment.ARG_ITEM_ID, id);
            int t = mViewPager.getCurrentItem();
            arguments.putInt("tabSelected", t);

            MainItemDetailFragment fragment = new MainItemDetailFragment();


            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            // In single-pane mode, simply start the detail activitywi
            // for the selected item ID.
            int t = mViewPager.getCurrentItem();

            // svi imaju detalje za svaku stavku u listi, izuzev tab 5. / 6. / 7.
            if(t != TAB_5_1 || t != TAB_5_2 || t != TAB_5_3)
            {
                Intent detailIntent = new Intent(this, MainItemDetailActivity.class);
                detailIntent.putExtra(MainItemDetailFragment.ARG_ITEM_ID, id);
                detailIntent.putExtra("tabSelected", t);
                startActivity(detailIntent);
            }
        }
    }

    /**
     *
     * REFRESH funkcionalnost i toast izvestaj koliko cega novog imao
     *
     * @param item
     */
    public void favsDisplay(MenuItem item)
    {

        int fragCurnum = 0;
        fragCurnum = (getActionBar().getSelectedTab().getPosition() == 0) ? 2 : 3;

        for(int i=0; i < fragCurnum; i++) // prodji kroz sve, [trenutne] fragmente u memoriji
        {
            // instanciraj nove fragmente i setuj ih
            MainFragment mf = (MainFragment) ((CollectionPagerAdapter) mViewPager.getAdapter()).getItem(i);
            mf.mNum = i;
            mf.local = this;

        }
        Toast.makeText(this, "Refrash", Toast.LENGTH_SHORT).show();
    }


    public void mainList_favIt(View view) {
        System.out.print("KLIKNUTO FUV !!!!!!!!!!!!!");
        FeedEntry n = (FeedEntry) view.getTag();
        String s = "Fav već sačuvan!";
        Cursor c = DB.rawQuery("SELECT * FROM favs WHERE rssItemId='" + n.rssItemId + "' ;", null);
        if(n!=null)
        {
            int t = mViewPager.getCurrentItem();
            if (c.getCount() > 0)
                return;

            DB.execSQL("INSERT INTO favs (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                    + " VALUES ( '" + n.rssItemId + "' , '" + n.title + "' , '" + n.desc + "' , '" + n.content + "' , '" + n.url + "' , '"
                    + n.creator + "' , '" + n.email + "' , '" + n.category + "' , '" + n.date.toString() + "' , '" + n.dateUpdate.toString() + "', '" + String.valueOf(t)+"' ); ");
            s = "FAVed!!! : " + t;
         /*   view.setClickable(false);*/
            view.setEnabled(false);
            view.setVisibility(View.INVISIBLE);
        }

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        toast = Toast.makeText(context,s, duration);
        try
        {
            if(toast.getView().isShown() == false) // ako false ne prikazuj više, tek nakon toga
                toast.show();
        }
        catch (Exception e) { /* ako izuzetak nista ne prikazujemo */ }

        // nakon dodavanja u Db sinhroniyacija sa listom u app
        MainFragment.syncFavsListToDB();
    }

    /**
     *
     * @param v View
     */
    public void onClk_removeFav_mnAct(View v)
    {

        if(mCollectionPagerAdapter != null) {


            MainFragment mf = new MainFragment();


            FeedEntry item = (FeedEntry) v.getTag();
            String[] arg = {item.rssItemId};

            int retStat = DB.delete("favs", "rssItemId = ?", arg);
            MainFragment.FAVS_DB_ITEMS.remove(item);

            //kada favujemo brise stavku iz
            MainFragment frag = (MainFragment) mCollectionPagerAdapter.getFragmentWhere(3);
            if(frag!=null)
                frag.mcAdapter.remove(item);

            if (retStat == 1)
                Toast.makeText(this, "Deleted " + arg[0], Toast.LENGTH_LONG).show();
            else
                mainList_favIt(v);
        }


    }




}

