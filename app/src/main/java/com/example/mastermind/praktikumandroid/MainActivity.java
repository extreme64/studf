package com.example.mastermind.praktikumandroid;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mastermind.praktikumandroid.conn.GetConnSSL;
import com.example.mastermind.praktikumandroid.conn.GetFromURL;
import com.example.mastermind.praktikumandroid.conn.PostConnSSL;

import java.net.URL;


public class MainActivity extends FragmentActivity
        implements MainFragment.Callbacks
{



    private boolean mTwoPane;
    public Toast toast;
    ListView listView;


    /* brojs tranica aplikacije, tj. CollectionPagerAdapter-a */
    protected static final int PAGE_NUMER=5;
    // ime tab za svaku od sranica adaptera 'CollectionPagerAdapter'
    String [] tabNames = { "Obaveštenja", "Najave", "Kalendar", "Sačuvano", "Rezultati"};
    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;


    public static SQLiteDatabase DB;
    public static final int NUM_ITEMS_IN_DB = 4;



    public ViewPager getViewPager()
    {
        if(mViewPager != null)
            return mViewPager;
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);


        /** toolbar actionbar tab
         * tabovi za selekciju stranice viewpager-a
         * */
        final ActionBar abar = this.getActionBar();
        abar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        abar.setHomeButtonEnabled(true);
        /** da bi smo mogli kontrolisati LAYOUT za actionbar */
        abar.setDisplayShowCustomEnabled(true);

        View abarCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        ActionBar.LayoutParams actB_lpars = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        abar.setCustomView(abarCustomView,actB_lpars);// specijalni view



        mCollectionPagerAdapter =
                new CollectionPagerAdapter(
                        getSupportFragmentManager());
        mCollectionPagerAdapter.setNUM_PAGES(PAGE_NUMER); // adapter radi sa ovoliko stranica tj. fragmenta !!!!!!

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCollectionPagerAdapter);


        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

                int locTmpInt = tab.getPosition();
                // show the given tab
                mViewPager.setCurrentItem(locTmpInt);

               /* MainFragment mfn = ((MainFragment)mCollectionPagerAdapter.getItem(locTmpInt));
                if(mfn!=null) {
                    mfn.mNum = locTmpInt;
                    mfn.refreshAdapter();
                }*/

                switch (tab.getPosition())
                {
                    case 0:
                        if (findViewById(R.id.item_detail_container) != null) {
                            // The detail container view will be present only in the large-screen layouts (res/values-large and
                            // res/values-sw600dp). If this view is present, then the activity should be in two-pane mode.
                            mTwoPane = true;

                            // In two-pane mode, list items should be given the 'activated' state when touched.
                            ((MainFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.item_list))
                                    .setActivateOnItemClick(true);
                        }
                        break;

                    default:
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }

        };

        // fill a.Bar with tabs
        for (int i = 0; i < tabNames.length; i++)
            abar.addTab( abar.newTab().setText(tabNames[i]).setTabListener(tabListener));





        //ako treba da se vratimo
        // da bi znali gde
        String tabToSelect;
        boolean  hasE = getIntent().hasExtra("tabToSetAsCurrent");
        if(hasE)
        {
            Intent i = getIntent();
            tabToSelect = i.getStringExtra("tabToSetAsCurrent");
            Integer tab = Integer.valueOf(tabToSelect);
            if(mViewPager!=null) {
                mViewPager.setCurrentItem(tab);
                getActionBar().setSelectedNavigationItem(tab);
            }
        }

        DB = openOrCreateDatabase("favsDB", Context.MODE_PRIVATE, null);
        //DB.execSQL("DROP TABLE IF EXISTS favs; DROP TABLE IF EXISTS obavestenja; DROP TABLE IF EXISTS najave; DROP TABLE IF EXISTS kalendar; " );

        DB.execSQL("CREATE TABLE IF NOT EXISTS favs( " +  "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

        DB.execSQL("CREATE TABLE IF NOT EXISTS obavestenja( " + "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

        DB.execSQL("CREATE TABLE IF NOT EXISTS najave( " + "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

        DB.execSQL("CREATE TABLE IF NOT EXISTS kalendar( " + "id INTEGER PRIMARY KEY ASC, " + "rssItemId VARCHAR, title TEXT, desc TEXT, content TEXT, url TEXT, " +
                "creator VARCHAR, email VARCHAR, category VARCHAR, date VARCHAR, dateUpdate VARCHAR, tab VARCHAR);");

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
            if(c2.getString(2).equals("false"))
            {
                boolean stconf = wrapStopService();
            }
            else
            {
                if (!isMyServiceRunning())
                {
                    wrapStartService();
                }
            }
        }

        // GetConnSSL testConn = new GetConnSSL(this);
        PostConnSSL testConn = new PostConnSSL(this);
         testConn.execute();

        //GetFromURL conn_noSSL = new GetFromURL("http://www.ict.edu.rs");
        //System.out.println("RES on YRL stat:" + conn_noSSL.getResponse("http://www.ict.edu.rs"));


        System.out.println(" ////////////////////// //////////////////////////////////////////////////////////// ");
        //GetFromURL conn_noSSL = new GetFromURL("http://www.ict.edu.rs");
        //conn_noSSL.test();

       // new GetFromURL("http://www.ict.edu.rs").execute();

        /** konekcija za logovanje ne SSl,...
         * dohvatiti rezultati URL
         * naci mesto gde cemo parsirati link */

    }




    public void wrapStartService()
    {
       /* Intent serviceIntent = new Intent("com.example.mastermind.praktikumandroid.CheckServerService");
        this.startService(serviceIntent);*/
    }

    public boolean wrapStopService()
    {
      /*  if (isMyServiceRunning())
            stopService(new Intent(this, CheckServerService.class));
        return isMyServiceRunning();*/
        return  true;
    }

    private boolean isMyServiceRunning() {
        return true;
      /*  ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (CheckServerService.class.getName().equals(service.service.getClassName())) {
                Log.d("Activity", "da radi !!!!");
                return true;
            }
        }
        Log.d("Activity", "NE nismo je nasli !!!!");
        return false;*/
    }


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
                Toast.makeText(this, "[PRAKTIKUM ANDROID] Info {  'Studentfy' : Adam Gičević 317/13 | IT | Visoka ICT | jul 2015.  }  ", Toast.LENGTH_LONG).show();
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
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MainItemDetailActivity.class);
            detailIntent.putExtra(MainItemDetailFragment.ARG_ITEM_ID, id);
            int t = mViewPager.getCurrentItem();
            detailIntent.putExtra("tabSelected", t);
            startActivity(detailIntent);
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

        for(int i=0; i < fragCurnum; i++)
        {
            MainFragment mf = (MainFragment)((CollectionPagerAdapter)mViewPager.getAdapter()).getItem(i);
            mf.mNum=i;
            mf.local = this;
            mf.reFresh();
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

            //TODO:  srediti parsiranje ovih polja DESC i CONTENT   desc / '" + n.desc + "' , '
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
            //List<android.support.v4.app.Fragment> lf = mCollectionPagerAdapter.mFragmentManager.getFragments();
            //MainFragment frag = (MainFragment) mCollectionPagerAdapter.getFragmentWhere(mf.mNum); //3

            //int t = lf.size();
            //int r = frag.mNum;
           //ListView lw = frag.getListView();

            //frag.mcAdapter.setNotifyOnChange(true);
            //lw.scrollBy(0, 0);

            FeedEntry item = (FeedEntry) v.getTag();
            String[] arg = {item.rssItemId};

            int retStat = DB.delete("favs", "rssItemId = ?", arg);
            MainFragment.favsLocal_DB_ITEMS.remove(item);

            //kada favujemo brise stavku iz
            MainFragment frag = (MainFragment) mCollectionPagerAdapter.getFragmentWhere(3);
            if(frag!=null)
                frag.mcAdapter.remove(item);

            /*ListView lw = frag.getListView();
            int frag_fav_size = lw.getCount();
            if(frag_fav_size>0){
                frag.mcAdapter.remove(item);
            }*/


            //frag.mcAdapter.notifyDataSetChanged(); // if this set setNotifyOnChange(true), then not needed

            if (retStat == 1)
                Toast.makeText(this, "Deleted " + arg[0], Toast.LENGTH_LONG).show();
            else
                mainList_favIt(v);
        }


    }




}
