package com.example.mastermind.praktikumandroid;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastermind.praktikumandroid.conn.ConnSSL;
import com.example.mastermind.praktikumandroid.student.GetListPages;
import com.example.mastermind.praktikumandroid.ical.ICalWrap;
import com.example.mastermind.praktikumandroid.rss.RssReader;
import com.example.mastermind.praktikumandroid.student.ObradaLista_StudServ;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MainItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MainFragment extends ListFragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener {



    Activity mActivity = null;
    MainScreenAdapter mcAdapter;


    // distanaca u pikselima pri pomeraju stranice pager-a, koristimo da bi utvrdili start i kraj, tj. da li je swipe finisiran pravilno, ne do pola
    private float[] pixDis = {-1, -1};
    private static int ONPAGESCROLLSTATECHANGED_FLAG = 1;


    public static MainScreenAdapter mcAdapterL1;
    public static MainScreenAdapter mcAdapterL2;
    public static MainScreenAdapter mcAdapterL3;


    /**
     * tabs local heap lists
     */
    public static List<FeedEntry> OBAVESTENJA_ITEMS = new ArrayList<FeedEntry>();
    public static List<FeedEntry> NAJAVE_ITEMS = new ArrayList<FeedEntry>();
    public static List<FeedEntry> ICALC_ITEMS = new ArrayList<FeedEntry>();
    public static List<FeedEntry> FAVS_DB_ITEMS = new ArrayList<FeedEntry>();


    public static List<FeedEntry> rezITEMS = new ArrayList<FeedEntry>();
    // ### #### ###

    public Activity local;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    private Callbacks mCallbacks = sDummyCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;
    public static String PAGE_ID = "";

    public int mNum;

    private static String LINK_STR_RSS_OBAVESTENJA = "http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml"; /* "http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml" */     /* https://web.archive.org/web/20171120174523if_/http://www.ict.edu.rs:80/rss/obavestenja_opsta/rss.xml */

    private static String LINK_STR_RSS_NAJAVE = "https://web.archive.org/web/20160514115413if_/http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml"; /* http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml */             /* https://web.archive.org/web/20160514115413if_/http://ict.edu.rs/rss/raspored_kolokvijuma/rss.xml */
    private static String LINK_STR_RSS_KALENDAR = "https://calendar.google.com/calendar/ical/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic.ics";
    public static int[] LISTE_POZICIJA = {0,0,0,0,0,0,0};


    private static String LINK_STR_RSS_REZ = "http://www.ict.edu.rs/rss/obavestenja_opsta/rss.xml";

    /**
     * ------
     */
    static boolean LISTS_UI_DATA_DONE = false;
    public static String URL_PREDMETI_LIST_STUDSER = "https://studentskiservis.ict.edu.rs/student/predmeti";
    public static String URL_ISPITI_LIST_STUDSER = "https://studentskiservis.ict.edu.rs/student/ispiti";
    public static String URL_ZADUZENJA_LIST_STUDSER = "https://studentskiservis.ict.edu.rs/student/finansije";

    public static ServisListScreenAdapter predmeti_sAdapter;
    public static ServisListScreenAdapter ispiti_sAdapter;
    public static ServisListScreenAdapter zaduzenja_sAdapter;

    public static List PREDMETI_ITEMS_STUDS = new ArrayList();
    public static List ISPITI_ITEMS_STUDS = new ArrayList();
    public static List ZADUZENJA_ITEMS_STUDS = new ArrayList();

    public static Map PREDMETI_ITEMS_PAGES_STUDS;
    public static Map ISPITI_ITEMS_PAGES_STUDS;
    public static Map ZADUZENJA_ITEMS_PAGES_STUDS;

    public static int BROJ_STRANICA_PREDMETI = 1;
    public static int BROJ_STRANICA_ISPITI = 1;
    public static int BROJ_STRANICA_ZADUZENJA = 1;

    public static void setBROJ_STRANICA_PREDMETI(int val) {
        BROJ_STRANICA_PREDMETI = val;
    }

    public static void setBROJ_STRANICA_ISPITI(int val) {
        BROJ_STRANICA_ISPITI = val;
    }

    public static void setBROJ_STRANICA_ZADUZENJA(int val) {
        BROJ_STRANICA_ZADUZENJA = val;
    }

    public static List CURRENTLY_ACTIVE_PROCESSES = new ArrayList();

    public static HashMap<String, String[]> filter1_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter2_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter3_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter21_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter22_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter23_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter31_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter32_values_map = new HashMap<String, String[]>();
    public static HashMap<String, String[]> filter33_values_map = new HashMap<String, String[]>();

    public static List<String> readingProcessChackIn = new ArrayList<String>();

    //BUG: to stop fake event based of how much of spinner we have
    public static int FAKE_SPINNERS_INIT_EVENTS = 0;
    public static int PREDMETI_SPINNERS_EVENT_READY = 0; //-2
    public static int ISPITI_SPINNERS_EVENT_READY = 0; //-3
    public static int ZADUZENJA_SPINNERS_EVENT_READY = 0; //-4
    /**
     * ------
     */
    //prosecna ocena
    public static double PROSEK = 0.0;
    public static TextView  tw_prosecnaOcena;
    //student toolbar zaglavlje text polje
    public static String STUDENT_ZAGLAVLJE_TEKST = "";
    public static TextView  tw_StudentZaglavlje;

    // dugme za azuriranje
    public static Button btnRef;
    public static TextView tv_SS_Info;
    public static Button clear_filters;

    public static Spinner filter1;
    public static Spinner filter2;
    public static Spinner filter3;
    public static Spinner filter21;
    public static Spinner filter22;
    public static Spinner filter23;
    public static Spinner filter31;
    public static Spinner filter32;
    public static Spinner filter33;
    public static int[][] SELECTEDOPTIONSPREDMETI_SPINNERS = new int[3][3];


    private int events = 0;


    /**
     * A callback interface that all activities containing this fragment must implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(String id);
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
     * implements AdapterView.OnItemSelectedListener
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        events++;
        if (mNum == MainActivity.TAB_5_1 || mNum == MainActivity.TAB_5_2 || mNum == MainActivity.TAB_5_3) {
            Spinner spinn1 = null, spinn2 = null, spinn3 = null;
            int fltr1_pos_value, fltr2_pos_value, fltr3_pos_value;
            String[] filt_val_params = {"-1", "-1", "-1"};

            /** trenutno radi ovako CommentCode: 1a */

            if (FAKE_SPINNERS_INIT_EVENTS > 1) {
                FAKE_SPINNERS_INIT_EVENTS--;
            } else {

                boolean t1 = MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_5_1 && PREDMETI_SPINNERS_EVENT_READY >= -1;
                boolean t2 = MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_5_2 && ISPITI_SPINNERS_EVENT_READY >= -1;
                boolean t3 = MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_5_3 && ZADUZENJA_SPINNERS_EVENT_READY >= -1;

                if ((t1 || t2 || t3))/** trenutno radi ovako CommentCode: 1a */ //FAKE_SPINNERS_INIT_EVENTS == 1
                {
                    switch (MainActivity.CURRENT_TAB_SELECTED) {
                        case MainActivity.TAB_5_1:
                            spinn1 = (Spinner) mActivity.findViewById(R.id.filter_1);
                            spinn2 = (Spinner) mActivity.findViewById(R.id.filter_2);
                            spinn3 = (Spinner) mActivity.findViewById(R.id.filter_3);
                            if (PREDMETI_SPINNERS_EVENT_READY >= -1) {
                                if (spinn1.isEnabled()) {
                                    fltr1_pos_value = spinn1.getSelectedItemPosition();
                                    filt_val_params[0] = filter1_values_map.get("" + fltr1_pos_value)[0];
                                }
                                if (spinn2.isEnabled()) {
                                    fltr2_pos_value = spinn2.getSelectedItemPosition();
                                    filt_val_params[1] = filter2_values_map.get("" + fltr2_pos_value)[0];
                                }
                                if (spinn3.isEnabled()) {
                                    fltr3_pos_value = spinn3.getSelectedItemPosition();
                                    filt_val_params[2] = filter3_values_map.get("" + fltr3_pos_value)[0];
                                }
                                new GetListPages(mActivity, this, 1, true, filt_val_params, ObradaLista_StudServ.LISTA_PREDMETI).execute(MainFragment.URL_PREDMETI_LIST_STUDSER, "1");
                            }
                            break;
                        case MainActivity.TAB_5_2:
                            spinn1 = (Spinner) mActivity.findViewById(R.id.filter_21);
                            spinn2 = (Spinner) mActivity.findViewById(R.id.filter_22);
                            spinn3 = (Spinner) mActivity.findViewById(R.id.filter_23);
                            if (ISPITI_SPINNERS_EVENT_READY >= -1) {
                                if (spinn1.isEnabled()) {
                                    fltr1_pos_value = spinn1.getSelectedItemPosition();
                                    filt_val_params[0] = filter21_values_map.get("" + fltr1_pos_value)[0];
                                }
                                if (spinn2.isEnabled()) {
                                    fltr2_pos_value = spinn2.getSelectedItemPosition();
                                    filt_val_params[1] = filter22_values_map.get("" + fltr2_pos_value)[0];
                                }
                                if (spinn3.isEnabled()) {
                                    fltr3_pos_value = spinn3.getSelectedItemPosition();
                                    filt_val_params[2] = filter23_values_map.get("" + fltr3_pos_value)[0];
                                }
                                new GetListPages(mActivity, this, 1, true, filt_val_params, ObradaLista_StudServ.LISTA_ISPITI).execute(MainFragment.URL_ISPITI_LIST_STUDSER, "1");
                            }
                            break;
                        case MainActivity.TAB_5_3:
                            spinn1 = (Spinner) mActivity.findViewById(R.id.filter_31);
                            spinn2 = (Spinner) mActivity.findViewById(R.id.filter_32);
                            spinn3 = (Spinner) mActivity.findViewById(R.id.filter_33);
                            if (ZADUZENJA_SPINNERS_EVENT_READY >= -1) {
                                if (spinn1.isEnabled()) {
                                    fltr1_pos_value = spinn1.getSelectedItemPosition();
                                    filt_val_params[0] = filter31_values_map.get("" + fltr1_pos_value)[0];
                                }
                                if (spinn2.isEnabled()) {
                                    fltr2_pos_value = spinn2.getSelectedItemPosition();
                                    filt_val_params[1] = filter32_values_map.get("" + fltr2_pos_value)[0];
                                }
                                if (spinn3.isEnabled()) {
                                    fltr3_pos_value = spinn3.getSelectedItemPosition();
                                    filt_val_params[2] = filter33_values_map.get("" + fltr3_pos_value)[0];
                                }
                                new GetListPages(mActivity, this, 1, true, filt_val_params, ObradaLista_StudServ.LISTA_ZADUZENJA).execute(MainFragment.URL_ZADUZENJA_LIST_STUDSER, "1");
                            }
                            break;
                    }

                    /** trenutno radi ovako CommentCode: 1a */
                }
            }
        } else {

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
    /** ------ */

    /**
     * onTouch Listener inter.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {


        /* obrada dogadja za dugme, "ponisti" */
        Button btnV;
        try {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS || event.getAction() == MotionEvent.ACTION_DOWN) {
                btnV = (Button) v;
                btnV.setTextColor(ContextCompat.getColor(mActivity, R.color.color_btnText1_pressed));
                onClick(v);
                //return true;
            }
            if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE || event.getAction() == MotionEvent.ACTION_UP) {
                btnV = (Button) v;
                btnV.setTextColor(ContextCompat.getColor(mActivity, R.color.color_btnText1));
                //return true;
            }
        }catch (ClassCastException cce ){}



        return false;
    }

    /**
     * onClick Listener inter.
     */
    @Override
    public void onClick(View v) {

        String tagname = "" + v.getTag();
        if (tagname.equals("clear_filters")) {
            Spinner filter_a = null;
            Spinner filter_b = null;
            Spinner filter_c = null;
            if (MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_5_1) {
                filter_a = (Spinner) mActivity.findViewById(R.id.filter_1);
                filter_b = (Spinner) mActivity.findViewById(R.id.filter_2);
                filter_c = (Spinner) mActivity.findViewById(R.id.filter_3);
                FAKE_SPINNERS_INIT_EVENTS += (filter_a.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
                FAKE_SPINNERS_INIT_EVENTS += (filter_b.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
                FAKE_SPINNERS_INIT_EVENTS += (filter_c.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
            }
            if (MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_5_2) {
                filter_a = (Spinner) mActivity.findViewById(R.id.filter_21);
                filter_b = (Spinner) mActivity.findViewById(R.id.filter_22);
                filter_c = (Spinner) mActivity.findViewById(R.id.filter_23);
                FAKE_SPINNERS_INIT_EVENTS += (filter_a.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
                FAKE_SPINNERS_INIT_EVENTS += (filter_b.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve

            }
            if (MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_5_3) {
                filter_a = (Spinner) mActivity.findViewById(R.id.filter_31);
                filter_b = (Spinner) mActivity.findViewById(R.id.filter_32);
                filter_c = (Spinner) mActivity.findViewById(R.id.filter_33);
                FAKE_SPINNERS_INIT_EVENTS += (filter_a.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
                FAKE_SPINNERS_INIT_EVENTS += (filter_b.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
                FAKE_SPINNERS_INIT_EVENTS += (filter_c.getSelectedItemPosition() != 0) ? 1 : 0;// eliminacija fake eventa pri setovanju, skidamo 2 a jedan putamo da bi ucitali listu sa sve /sve /sve
            }

            if (filter1 != null && filter2 != null && filter3 != null) {
                FAKE_SPINNERS_INIT_EVENTS -= 1; // citaj jednom
                filter_a.setSelection(0, true);
                filter_b.setSelection(0, true);
                filter_c.setSelection(0, true);
            }
        }
    }
    /** ------ */


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainFragment() {
    }



    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static MainFragment newInstance(int num) {


        MainFragment mf = new MainFragment();
        Bundle args = new Bundle();

        args.putInt("num", num);// Supply num input as an argument.

        String lStrRss;
        switch (num) {
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
            case 4:
                lStrRss = LINK_STR_RSS_REZ;
                break;

            default:
                lStrRss = LINK_STR_RSS_OBAVESTENJA;
        }

        args.putString("rss_link_arg", lStrRss);
        mf.setArguments(args);

        return mf;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        MainActivity.mf_hm.put("" + MainActivity.getViewPager().getCurrentItem(), this);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        mActivity = this.getActivity();

        btnRef = (Button) mActivity.findViewById(R.id.btnRef);
        tw_prosecnaOcena = (TextView) mActivity.findViewById(R.id.toolbarOcena);
        tw_StudentZaglavlje = (TextView) mActivity.findViewById(R.id.toolbarTextSpace);


        // pri kreiranju i rotaiji ekrana, obezbedjujemo da su filteri UI vidiljivi
        ((MainActivity)mActivity).anyTabSelected(MainActivity.CURRENT_TAB_SELECTED); // u slucaju da je tab za listu predmeta, tada je problem ako izostavimo ovo pozivanje

        // Prihvatamo poslate argumente pri kreiranju fragmenta
        Bundle bundArgs = this.getArguments();
        String rss_link = bundArgs.getString("rss_link_arg");
        boolean areFavs = bundArgs.getBoolean("favs");

        //read DB and pump into the list to save space use helper proc. we use this one also when new fav added from listView
        syncFavsListToDB(); //????

        /** Prema tabu koji je selektovan, podesavamo adapter */
        GetRSSDataTask GetRSS_dt = new GetRSSDataTask();
        if (!areFavs && mNum != MainActivity.TAB_5_1 && mNum != 5 && mNum != 6) // read feeds, favs tab NOT selected
        {
            if (rss_link != null) {
                if (mNum == MainActivity.TAB_1) {
                    mcAdapterL1 = new MainScreenAdapter(mActivity, OBAVESTENJA_ITEMS);
                    mcAdapterL1.setNotifyOnChange(true);
                    mcAdapterL1.setTab(mNum);
                    setListAdapter(mcAdapterL1);
                    if (OBAVESTENJA_ITEMS.size() == 0)
                        GetRSS_dt.execute(rss_link);
                }
                if (mNum == MainActivity.TAB_2) {
                    mcAdapterL2 = new MainScreenAdapter(mActivity, NAJAVE_ITEMS);
                    mcAdapterL2.setNotifyOnChange(true);
                    mcAdapterL2.setTab(mNum);
                    setListAdapter(mcAdapterL2);
                    if (NAJAVE_ITEMS.size() == 0)
                        GetRSS_dt.execute(rss_link);
                }
                if (mNum == MainActivity.TAB_3) {
                    mcAdapterL3 = new MainScreenAdapter(mActivity, ICALC_ITEMS);
                    mcAdapterL3.setNotifyOnChange(true);
                    mcAdapterL3.setTab(mNum);
                    setListAdapter(mcAdapterL3);
                    if (ICALC_ITEMS.size() == 0)
                        GetRSS_dt.execute(rss_link);
                }
            }
        } else if (areFavs) {


            /** ako su FAV-ovi iz baze */
            mcAdapter = new MainScreenAdapter(mActivity, FAVS_DB_ITEMS); //rssReader.getItems()
            mcAdapter.setNotifyOnChange(true);
            mcAdapter.setTab(mNum);
            setListAdapter(mcAdapter);


        } else {

            /**  ********  STUDENTSKI SERVIS liste ********  */
            if (mNum == MainActivity.TAB_5_1 || mNum == MainActivity.TAB_5_2 || mNum == MainActivity.TAB_5_3) {


                tv_SS_Info = (TextView) mActivity.findViewById(R.id.tv_SS_Info);
                clear_filters = (Button) mActivity.findViewById(R.id.clear_filters);

                filter1 = (Spinner) mActivity.findViewById(R.id.filter_1);
                filter2 = (Spinner) mActivity.findViewById(R.id.filter_2);
                filter3 = (Spinner) mActivity.findViewById(R.id.filter_3);
                filter21 = (Spinner) mActivity.findViewById(R.id.filter_21);
                filter22 = (Spinner) mActivity.findViewById(R.id.filter_22);
                filter23 = (Spinner) mActivity.findViewById(R.id.filter_23);
                filter31 = (Spinner) mActivity.findViewById(R.id.filter_31);
                filter32 = (Spinner) mActivity.findViewById(R.id.filter_32);
                filter33 = (Spinner) mActivity.findViewById(R.id.filter_33);


                // setovanje adaptera za spinere
                // setovanje za eventove promene selektovane stavke
                ArrayAdapter aa_f1 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(4, 1));
                filter1.setAdapter(aa_f1);
                ArrayAdapter aa2_f1 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(5, 1));
                filter21.setAdapter(aa2_f1);
                ArrayAdapter aa3_f1 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(6, 1));
                filter31.setAdapter(aa3_f1);
                setOnItemSelectedListenerWrapAdd(filter1, this);
                setOnItemSelectedListenerWrapAdd(filter21, this);
                setOnItemSelectedListenerWrapAdd(filter31, this);

                ArrayAdapter aa_f2 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(4, 2));
                filter2.setAdapter(aa_f2);
                ArrayAdapter aa2_f2 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(5, 2));
                filter22.setAdapter(aa2_f2);
                ArrayAdapter aa3_f2 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(6, 2));
                filter32.setAdapter(aa3_f2);
                setOnItemSelectedListenerWrapAdd(filter2, this);
                setOnItemSelectedListenerWrapAdd(filter22, this);
                setOnItemSelectedListenerWrapAdd(filter32, this);

                ArrayAdapter aa_f3 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(4, 3));
                filter3.setAdapter(aa_f3);
                ArrayAdapter aa2_f3 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(5, 3));
                filter23.setAdapter(aa2_f3);
                ArrayAdapter aa3_f3 = new ArrayAdapter(mActivity, R.layout.support_simple_spinner_dropdown_item, getSpinnerMapList(6, 3));
                filter33.setAdapter(aa3_f3);
                setOnItemSelectedListenerWrapAdd(filter3, this);
                setOnItemSelectedListenerWrapAdd(filter32, this);
                setOnItemSelectedListenerWrapAdd(filter33, this);

                // setovanje dugmeta za ponistavanje selektovanog u spinerima
                // setovanje eventa za click
                Button clearFiltersBtn = (Button) mActivity.findViewById(R.id.clear_filters);
                clearFiltersBtn.setOnClickListener(this);
                clearFiltersBtn.setOnTouchListener(this);

                MainActivity.mViewPager.setOnTouchListener(this);

                MainActivity.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        pixDis[1] = position; //belezimo poslednju koordinatu pomeraja u realnom vremenu
                    }

                    @Override
                    public void onPageSelected(int position) {

                        if(mNum==4) {
                            int curent_fragment_viewed = MainActivity.mViewPager.getCurrentItem();

                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        // osiguravamo prevenciju ponavljanja, za svaki od 3 frag.
                        if(true)
                        {
                            if (state == 2) {

                            }

                            if (state == 1) {
                                pixDis[0] = pixDis[1]; // belezimo prvu koordinatu
                                ONPAGESCROLLSTATECHANGED_FLAG = 1;
                            }

                            if (state == 0) {
                                int curent_fragment_viewed;
                                int toSel;
                                // osiguravamo prevenciju ponavljanja, za svaki od 3 frag.
                                if (ONPAGESCROLLSTATECHANGED_FLAG==1) //ONPAGESCROLLSTATECHANGED_FLAG==1
                                {
                                    if (MainActivity.CURRENT_TAB_SELECTED >= MainActivity.TAB_5_1) {

                                        TabLayout tabLayoutSS = (TabLayout) mActivity.findViewById(R.id.TabLayout_SS_Opcije);

                                        // imamo finisiran swipe
                                        // ako se koordinate prva i zadnja razlikuju, znaci da je prebacivanje dovrseno, a ne samo do pola
                                        if (pixDis[0] != pixDis[1]) {

                                            curent_fragment_viewed = MainActivity.mViewPager.getCurrentItem();
                                            toSel = curent_fragment_viewed - 4; //normalizacija na vrednosti tabLayoutSS-a
                                            // prevencija vrednosti, van opsega
                                            if (toSel < 0)
                                                toSel = 0;
                                            if (toSel > 2)
                                                toSel = 2;

                                            //swipe levo ka desno
                                            if (pixDis[0] < pixDis[1])
                                            {
                                                /** [s] > [e] */
                                                TabLayout.Tab tabSelected = tabLayoutSS.getTabAt(toSel);
                                                tabSelected.select();
                                            }
                                            //swipe desno ka levo
                                            else
                                            {
                                                /** [e] < [s] */
                                                TabLayout.Tab tabSelected = tabLayoutSS.getTabAt(toSel);
                                                tabSelected.select();
                                            }
                                        }
                                    }
                                }
                                ONPAGESCROLLSTATECHANGED_FLAG = 0;
                            }
                        }
                    }
                });

                // niz sa vrednostima po spinerima, setovane zadnjim cuvanjem Bundle
                // sada koristimo da selektijemo stavke u svakom spineru
                if (filter1 != null) {
                    int tmp = 1;
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[0][0];
                    filter1.setSelection(tmp, true);
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[0][1];
                    filter2.setSelection(tmp, true);
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[0][2];
                    filter3.setSelection(tmp, true);

                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[1][0];
                    filter21.setSelection(tmp, true);
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[1][1];
                    filter22.setSelection(tmp, true);
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[1][2];
                    filter23.setSelection(tmp, true);

                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[2][0];
                    filter31.setSelection(tmp, true);
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[2][1];
                    filter32.setSelection(tmp, true);
                    tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[2][2];
                    filter33.setSelection(tmp, true);
                }

                /*** --------------------------------------------------------------------------- */
                // uzimanje vrednosti iz spinner-a
                int fltr1_pos_value;
                int fltr2_pos_value;
                int fltr3_pos_value;
                filter1 = (Spinner) mActivity.findViewById(R.id.filter_1);
                filter2 = (Spinner) mActivity.findViewById(R.id.filter_2);
                filter3 = (Spinner) mActivity.findViewById(R.id.filter_3);
                fltr1_pos_value = (filter1.getSelectedItemPosition() == -1) ? 0 : filter1.getSelectedItemPosition();
                fltr2_pos_value = (filter2.getSelectedItemPosition() == -1) ? 0 : filter2.getSelectedItemPosition();
                fltr3_pos_value = (filter3.getSelectedItemPosition() == -1) ? 0 : filter3.getSelectedItemPosition();
                String[] filt_val_params = {
                        filter1_values_map.get("" + fltr1_pos_value)[0],
                        filter2_values_map.get("" + fltr2_pos_value)[0],
                        filter3_values_map.get("" + fltr3_pos_value)[0]};
                /*** --------------------------------------------------------------------------- */


                // setujemo adaptere za liste podataka i
                // pozivamo prvo inicijalno citanje, bez filtera
                if (mNum == MainActivity.TAB_5_1) {
                    predmeti_sAdapter = new ServisListScreenAdapter(mActivity, PREDMETI_ITEMS_STUDS); //rssReader.getItems()
                    predmeti_sAdapter.setNotifyOnChange(true);
                    predmeti_sAdapter.setTab(mNum);
                    predmeti_sAdapter.setLayout(R.layout.main_list_sservis);
                    setListAdapter(predmeti_sAdapter);
                    if (!MainFragment.LISTS_UI_DATA_DONE) {
                        new GetListPages(mActivity, this, 1, true, filt_val_params, ObradaLista_StudServ.LISTA_PREDMETI).execute(MainFragment.URL_PREDMETI_LIST_STUDSER, "1");
                    }
                } else if (mNum == MainActivity.TAB_5_2) {
                    ispiti_sAdapter = new ServisListScreenAdapter(mActivity, ISPITI_ITEMS_STUDS); //rssReader.getItems()
                    ispiti_sAdapter.setNotifyOnChange(true);
                    ispiti_sAdapter.setTab(mNum);
                    ispiti_sAdapter.setLayout(R.layout.main_list_sservis);
                    setListAdapter(ispiti_sAdapter);
                    if (!MainFragment.LISTS_UI_DATA_DONE) {
                        new GetListPages(mActivity, this, 1, true, filt_val_params, ObradaLista_StudServ.LISTA_ISPITI).execute(MainFragment.URL_ISPITI_LIST_STUDSER, "1");
                    }

                } else if (mNum == MainActivity.TAB_5_3) {
                    zaduzenja_sAdapter = new ServisListScreenAdapter(mActivity, ZADUZENJA_ITEMS_STUDS); //rssReader.getItems()
                    setListAdapter(zaduzenja_sAdapter);
                    zaduzenja_sAdapter.setNotifyOnChange(true);
                    zaduzenja_sAdapter.setTab(mNum);
                    zaduzenja_sAdapter.setLayout(R.layout.main_list_sservis);
                    if (!MainFragment.LISTS_UI_DATA_DONE) {
                        new GetListPages(mActivity, this, 1, true, filt_val_params, ObradaLista_StudServ.LISTA_ZADUZENJA).execute(MainFragment.URL_ZADUZENJA_LIST_STUDSER, "1");
                    }
                }
                FAKE_SPINNERS_INIT_EVENTS = 24;
                //FAKE_SPINNERS_INIT_EVENTS++;
            }
        }
    }


    /**
     * sortiramo listu sa trenutnim podacima
     */ //TODO:
    public ArrayList ListAdapterSourceSorter(int pageName, Map itemsPages) {
        ArrayList wholeList = new ArrayList();
        switch (pageName) {
            case 1:
                Map predmeti_al = itemsPages;
                ArrayList pageItems;
                for (int p = predmeti_al.size(); p >= 1; p--) {
                    pageItems = (ArrayList) predmeti_al.get(p);
                    if (predmeti_al.containsKey(p) && !pageItems.isEmpty()) {
                        wholeList.addAll(pageItems);
                    }
                }
                break;
        }
        return wholeList;
    }


    protected void setOnItemSelectedListenerWrapAdd(AdapterView adView, MainFragment fragment) {
        adView.setOnItemSelectedListener(fragment);
    }

    /**
     * prilagodjavamo vidljivost UI elemenata,
     */
    public void adaptUIStudServis(boolean doShow)
    {
        if(btnRef != null) {
            Button azurirajListe = btnRef;
            // refresh dugme
            if (azurirajListe != null) {
                if (!doShow)
                    azurirajListe.setVisibility(View.VISIBLE);
                else
                    azurirajListe.setVisibility(View.GONE);
            }
        }

        // ss info box
        if(tv_SS_Info != null) {
            tv_SS_Info.setVisibility(View.GONE);
            MainActivity.tabLayoutSS.setVisibility(View.GONE);
            clear_filters.setVisibility(View.GONE);
            if (doShow) {
                tv_SS_Info.setVisibility(View.VISIBLE);
                MainActivity.tabLayoutSS.setVisibility(View.VISIBLE);
                clear_filters.setVisibility(View.VISIBLE);
            }
        }
    }
    /**
     * prilagodjavamo vizuelni dojam spinera,
     * da reflektuju trenutno aktivnu lisut
     */
    public void adaptSpinners(int whatList) {

        if(MainFragment.filter1 != null) {

            filter1.setVisibility(View.GONE);
            filter2.setVisibility(View.GONE);
            filter3.setVisibility(View.GONE);
            filter21.setVisibility(View.GONE);
            filter22.setVisibility(View.GONE);
            filter23.setVisibility(View.GONE);
            filter31.setVisibility(View.GONE);
            filter32.setVisibility(View.GONE);
            filter33.setVisibility(View.GONE);


            if (whatList == 1) {
                filter1.setVisibility(View.VISIBLE);
                filter2.setVisibility(View.VISIBLE);
                filter3.setVisibility(View.VISIBLE);

            }
            if (whatList == 2) {
                filter21.setVisibility(View.VISIBLE);
                filter22.setVisibility(View.VISIBLE);
                filter23.setVisibility(View.VISIBLE);

            }
            if (whatList == 3) {
                filter31.setVisibility(View.VISIBLE);
                filter32.setVisibility(View.VISIBLE);
                filter33.setVisibility(View.VISIBLE);

            }
        }
    }


    /**
     * omugucen/onesposobljen spiner animiranje
     */
    public void animateSpinnerDisabledStatus(Boolean enabled) {
        if (MainFragment.filter1 == null || MainFragment.filter2 == null || MainFragment.filter3 == null) {

        } else if (MainFragment.filter1.getVisibility() == View.VISIBLE && MainFragment.filter1.getSelectedView() != null) {
            MainFragment.filter1.getSelectedView().setEnabled(enabled);
            MainFragment.filter1.setEnabled(enabled);
            MainFragment.filter2.getSelectedView().setEnabled(enabled);
            MainFragment.filter2.setEnabled(enabled);
            MainFragment.filter3.getSelectedView().setEnabled(enabled);
            MainFragment.filter3.setEnabled(enabled);

            if (!enabled) {
                MainFragment.filter1.setAlpha(0.2F);
                MainFragment.filter2.setAlpha(0.2F);
                MainFragment.filter3.setAlpha(0.2F);
            } else {
                MainFragment.filter1.setAlpha(1);
                MainFragment.filter2.setAlpha(1);
                MainFragment.filter3.setAlpha(1);
            }
        }
    }

    /**
     * mape za spinere
     */
    public static HashMap<String, String[]> getSpinnerValueMapings(int whatTab, int spinnerNum) {
        HashMap<String, String[]> tmpContaner = new HashMap<>();

        if (whatTab == MainActivity.TAB_5_1) {
            if (spinnerNum == 1) {
                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"2017", "2017/18"});
                tmpContaner.put("2", new String[]{"2016", "2016/17"});
                tmpContaner.put("3", new String[]{"2015", "2015/16"});
                tmpContaner.put("4", new String[]{"2014", "2014/15"});
                tmpContaner.put("5", new String[]{"2013", "2013/14"});
            } else if (spinnerNum == 2) {
                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"1", "I"});
                tmpContaner.put("2", new String[]{"2", "II"});
                tmpContaner.put("3", new String[]{"3", "III"});
            } else if (spinnerNum == 3) {

                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"1", "Obavezni"});
                tmpContaner.put("2", new String[]{"2", "Izborni"});
                tmpContaner.put("3", new String[]{"3", "Fakultativni"});
            }

        }
        if (whatTab == MainActivity.TAB_5_2) {
            if (spinnerNum == 1) {
                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"2017", "2017/18"});
                tmpContaner.put("2", new String[]{"2016", "2016/17"});
                tmpContaner.put("3", new String[]{"2015", "2015/16"});
                tmpContaner.put("4", new String[]{"2014", "2014/15"});
                tmpContaner.put("5", new String[]{"2013", "2013/14"});
            } else if (spinnerNum == 2) {
                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"1", "I"});
                tmpContaner.put("2", new String[]{"2", "II"});
                tmpContaner.put("3", new String[]{"3", "III"});
            } else if (spinnerNum == 3) {

                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"1", "Položeni"});
            }

        }
        if (whatTab == MainActivity.TAB_5_3) {
            if (spinnerNum == 1) {
                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"2017", "2017/18"});
                tmpContaner.put("2", new String[]{"2016", "2016/17"});
                tmpContaner.put("3", new String[]{"2015", "2015/16"});
                tmpContaner.put("4", new String[]{"2014", "2014/15"});
                tmpContaner.put("5", new String[]{"2013", "2013/14"});
            } else if (spinnerNum == 2) {
                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"1", "Zaduženja"});
                tmpContaner.put("2", new String[]{"2", "Uplata"});
                tmpContaner.put("3", new String[]{"7", "Prenos u nar. god."});
                tmpContaner.put("4", new String[]{"8", "Prenos iz proš. god."});
            } else if (spinnerNum == 3) {

                tmpContaner.put("0", new String[]{"-1", "SVE"});
                tmpContaner.put("1", new String[]{"1", "Školarina"});
                tmpContaner.put("2", new String[]{"2", "Ispit"});
                tmpContaner.put("3", new String[]{"5", "Zakasnela prij."});
                tmpContaner.put("4", new String[]{"200", "Dug / Preplata"});
            }
        }
        return tmpContaner;
    }

    /**
     * nizovi za adapter spinera,
     * dobijeni iz spiner mapiranja
     */
    public static ArrayList getSpinnerMapList(int whatTab, int spinnerNum) {
        HashMap<String, String[]> tmpContaner = new HashMap<>();

        if (whatTab == MainActivity.TAB_5_1) {
            if (spinnerNum == 1) {
                tmpContaner.putAll(MainFragment.filter1_values_map);
            } else if (spinnerNum == 2) {
                tmpContaner.putAll(MainFragment.filter2_values_map);
            } else if (spinnerNum == 3) {
                tmpContaner.putAll(MainFragment.filter3_values_map);
            }

        }
        if (whatTab == MainActivity.TAB_5_2) {
            if (spinnerNum == 1) {
                tmpContaner.putAll(MainFragment.filter21_values_map);
            } else if (spinnerNum == 2) {
                tmpContaner.putAll(MainFragment.filter22_values_map);
            } else if (spinnerNum == 3) {
                tmpContaner.putAll(MainFragment.filter23_values_map);
            }

        }
        if (whatTab == MainActivity.TAB_5_3) {
            if (spinnerNum == 1) {
                tmpContaner.putAll(MainFragment.filter31_values_map);
            } else if (spinnerNum == 2) {
                tmpContaner.putAll(MainFragment.filter32_values_map);
            } else if (spinnerNum == 3) {
                tmpContaner.putAll(MainFragment.filter33_values_map);
            }

        }

        ArrayList al = new ArrayList();
        for (int li = 0; li < tmpContaner.size(); li++) {
            String tmp = tmpContaner.get("" + li)[1];
            al.add(tmp);
        }
        return al;
    }
    /*   ///////////////   */


    public void clearFavsList() {
        FAVS_DB_ITEMS.clear();
    }



    public void azurirajSadrzaj(MainActivity ma)
    {

        if ( MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_1 ) {

            MainFragment.mcAdapterL1.clear();
            MainFragment.mcAdapterL1.notifyDataSetChanged();
            MainFragment.GetRSSDataTask GetRSS_dt1 = new GetRSSDataTask();
            GetRSS_dt1.init(ma, true, MainActivity.TAB_1);
            GetRSS_dt1.execute(LINK_STR_RSS_OBAVESTENJA);
        }
        else if( MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_2 ) {

            MainFragment.mcAdapterL2.clear();
            MainFragment.mcAdapterL2.notifyDataSetChanged();
            MainFragment.GetRSSDataTask GetRSS_dt2 = new GetRSSDataTask();
            GetRSS_dt2.init(ma, true, MainActivity.TAB_2);
            GetRSS_dt2.execute(LINK_STR_RSS_NAJAVE);
        }
        else if( MainActivity.CURRENT_TAB_SELECTED == MainActivity.TAB_3 ) {

            MainFragment.mcAdapterL3.clear();
            MainFragment.mcAdapterL3.notifyDataSetChanged();
            MainFragment.GetRSSDataTask GetRSS_dt3 = new GetRSSDataTask();
            GetRSS_dt3.init(ma, true, MainActivity.TAB_3);
            GetRSS_dt3.execute(LINK_STR_RSS_KALENDAR);
        }
        Toast.makeText(ma, "Azurirana lista", Toast.LENGTH_SHORT).show();
    }



    public static boolean syncFavsListToDB() {
        SQLiteDatabase lDB = MainActivity.DB;
        Cursor c;
        c = lDB.rawQuery("SELECT * FROM favs;", null);
        int size = c.getCount();

        FAVS_DB_ITEMS.clear(); // !
        while (c.moveToNext()) {
            FeedEntry dbitem = new FeedEntry();
            dbitem = pumpItem(dbitem, c);

            FAVS_DB_ITEMS.add(dbitem);
        }
        return true;
    }

    private static FeedEntry pumpItem(FeedEntry i, Cursor c) {
        FeedEntry item = i;
        if (item != null) {
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
            SimpleDateFormat fmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            try {
                item.date = fmt.parse(dateString1);
                item.dateUpdate = fmt.parse(dateString2);
            } catch (Exception e)
            {
                Log.i("Ex", e.toString());
            }
            item.fromTab = c.getString(11); // from what tab we have saved this one
        }
        if (item == null)
            return new FeedEntry();
        return item;
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        int t = MainActivity.mViewPager.getCurrentItem();

        // svi imaju detalje za svaku stavku u listi, izuzev tabova za Stud. Serv. liste
        if(t != MainActivity.TAB_5_1 && t != MainActivity.TAB_5_2 && t != MainActivity.TAB_5_3)
        {
            Intent detailIntent = new Intent(mActivity, MainItemDetailActivity.class);
            detailIntent.putExtra(MainItemDetailFragment.ARG_ITEM_ID, ""+id);
            detailIntent.putExtra("tabSelected", t);
            startActivity(detailIntent);
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



    /** ###### LIFE cycle ###### */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @CallSuper
    public void onStart() {
       super.onStart();
       //Log.i("METHOD LOG", "onStart " + " / " + mNum);

        FAKE_SPINNERS_INIT_EVENTS = 24;

       /** postavljamo reference, da se i nakon izmena stanja aplikacije */
       if(filter1 == null) {

           btnRef = (Button) mActivity.findViewById(R.id.btnRef);
           tw_prosecnaOcena = (TextView) mActivity.findViewById(R.id.toolbarOcena);
           tw_StudentZaglavlje = (TextView) mActivity.findViewById(R.id.toolbarTextSpace);
           tv_SS_Info = (TextView) mActivity.findViewById(R.id.tv_SS_Info);
           clear_filters = (Button) mActivity.findViewById(R.id.clear_filters);

           filter1 = (Spinner) mActivity.findViewById(R.id.filter_1);
           filter2 = (Spinner) mActivity.findViewById(R.id.filter_2);
           filter3 = (Spinner) mActivity.findViewById(R.id.filter_3);

           filter21 = (Spinner) mActivity.findViewById(R.id.filter_21);
           filter22 = (Spinner) mActivity.findViewById(R.id.filter_22);
           filter23 = (Spinner) mActivity.findViewById(R.id.filter_23);

           filter31 = (Spinner) mActivity.findViewById(R.id.filter_31);
           filter32 = (Spinner) mActivity.findViewById(R.id.filter_32);
           filter33 = (Spinner) mActivity.findViewById(R.id.filter_33);
       }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @CallSuper
    public void onResume() {
        super.onResume();
        //Log.i("METHOD LOG", "onResume " + " / " + mNum );


        /** selektujemo fragment koji smo zadnji gledali
         * takodje setujemo UI tabova da reflektuje prikazan fragment
         */

        // niz sa vrednostima po spinerima, setovane zadnjim cuvanjem Bundle
        // sada koristimo da selektijemo stavke u svakom spineru
        if(filter1 != null ) {
         int tmp = 1;
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[0][0];
         filter1.setSelection(tmp, true);
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[0][1];
         filter2.setSelection(tmp, true);
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[0][2];
         filter3.setSelection(tmp, true);

         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[1][0];
         filter21.setSelection(tmp, true);
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[1][1];
         filter22.setSelection(tmp, true);
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[1][2];
         filter23.setSelection(tmp, true);

         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[2][0];
         filter31.setSelection(tmp, true);
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[2][1];
         filter32.setSelection(tmp, true);
         tmp = SELECTEDOPTIONSPREDMETI_SPINNERS[2][2];
         filter33.setSelection(tmp, true);
         }

        FAKE_SPINNERS_INIT_EVENTS = 24;
        FAKE_SPINNERS_INIT_EVENTS++; // dodati fake dogadja pri promeni



        if(mNum == 6) {
            int ltb = MainActivity.LAST_TAB_VIEWED;
            MainActivity.mViewPager.setCurrentItem(ltb);

            TabLayout tabLayout1 = null, tabLayout2 = null;
            tabLayout1 = (TabLayout) mActivity.findViewById(R.id.TabLayoutMain);
            tabLayout2 = (TabLayout) mActivity.findViewById(R.id.TabLayout_SS_Opcije);
            TabLayout.Tab tabToSelect_main = null;
            TabLayout.Tab tabToSelect_ss = null;
            if(ltb<5) {
                tabToSelect_main = tabLayout1.getTabAt(ltb);
            }else {
                tabToSelect_main = tabLayout1.getTabAt(4);
            }
            if(ltb>=4) {
                tabToSelect_ss = tabLayout2.getTabAt(ltb - 4);
            }


            if(tabToSelect_main != null)
                tabToSelect_main.select();
            if(tabToSelect_ss != null)
                tabToSelect_ss.select();
        }
        /****** */


    }

    @CallSuper
    public void onPause() {
        super.onPause();
    }



    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /* SAVE state */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        /** liste scroll stanje pamcenje */
         // ovde pamtimo trenutno scroll stanje lista u outState objektu
        if(outState != null) {
            outState.clear();
            ListView lw = this.getListView();
            int t = lw.getFirstVisiblePosition();
            String key = "lista_" + mNum;
            if(outState.containsKey(key))
            {
                outState.remove(key);
            }
            outState.putInt(key, t);

            // u slucaju da izlazimo skroz, moramo ovde azurirati
            // jer zadnja promena na listi koja je bila aktivna
            // onda ne bude sacuvana
            LISTE_POZICIJA[mNum] = t;

        }
        /***** */

        super.onSaveInstanceState(outState);

        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        /****** */



        if(mNum == 6 || true)
        {
            Spinner s1 = (Spinner)mActivity.findViewById(R.id.filter_1);
            Spinner s2 = (Spinner)mActivity.findViewById(R.id.filter_2);
            Spinner s3 = (Spinner)mActivity.findViewById(R.id.filter_3);
            Spinner s21 = (Spinner)mActivity.findViewById(R.id.filter_21);
            Spinner s22 = (Spinner)mActivity.findViewById(R.id.filter_22);
            Spinner s23 = (Spinner)mActivity.findViewById(R.id.filter_23);
            Spinner s31 = (Spinner)mActivity.findViewById(R.id.filter_31);
            Spinner s32 = (Spinner)mActivity.findViewById(R.id.filter_32);
            Spinner s33 = (Spinner)mActivity.findViewById(R.id.filter_33);
            outState.putInt("spin1", s1.getSelectedItemPosition());
            outState.putInt("spin2", s2.getSelectedItemPosition());
            outState.putInt("spin3", s3.getSelectedItemPosition());
            outState.putInt("spin21", s21.getSelectedItemPosition());
            outState.putInt("spin22", s22.getSelectedItemPosition());
            outState.putInt("spin23", s23.getSelectedItemPosition());
            outState.putInt("spin31", s31.getSelectedItemPosition());
            outState.putInt("spin32", s32.getSelectedItemPosition());
            outState.putInt("spin33", s33.getSelectedItemPosition());


            // setujemo koji smo zadnji fragment koristili,
            // da ga po potrebi selektujemo
            // na vracanje akticnosti aplikacije u fokus
            MainActivity.LAST_TAB_VIEWED = MainActivity.CURRENT_TAB_SELECTED;
        }
    }

    /* LOAD state */
    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @CallSuper
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(MainFragment.tw_prosecnaOcena!= null)
        {
            MainFragment.tw_prosecnaOcena.setText(MainFragment.PROSEK+"");
        }
        if(tw_StudentZaglavlje != null)
        {
            tw_StudentZaglavlje.setText(STUDENT_ZAGLAVLJE_TEKST.toUpperCase());
        }



        /*** uspostavnljamo sacuvano 'scroll' stanje lista */
        // setovanje vrednosti, onda kada su nam dostupne
        if(savedInstanceState != null) {
            LISTE_POZICIJA[mNum] = savedInstanceState.getInt("lista_"+mNum);;
        }
        // setovanje lista pri zadnjem setu po9ziva ove metode
        // na ovaj nacin nema preklapanja i resetovanja ponovo na vrh liste
        // kada je savedInstanceState == null
        this.setSelection(LISTE_POZICIJA[mNum]);
        /****** */



        /** stavljamo vrednosti u staticki niz,
         * koristimo ga kasnije za podesavanje spinera onCreate() */
        if(filter1 != null && savedInstanceState != null) {
            int tmp = 1;
            tmp = savedInstanceState.getInt("spin1");
            SELECTEDOPTIONSPREDMETI_SPINNERS[0][0] = tmp;
            tmp = savedInstanceState.getInt("spin2");
            SELECTEDOPTIONSPREDMETI_SPINNERS[0][1] = tmp;
            tmp = savedInstanceState.getInt("spin3");
            SELECTEDOPTIONSPREDMETI_SPINNERS[0][2] = tmp;
            tmp = savedInstanceState.getInt("spin21");
            SELECTEDOPTIONSPREDMETI_SPINNERS[1][0] = tmp;
            tmp = savedInstanceState.getInt("spin22");
            SELECTEDOPTIONSPREDMETI_SPINNERS[1][1] = tmp;
            tmp = savedInstanceState.getInt("spin23");
            SELECTEDOPTIONSPREDMETI_SPINNERS[1][2] = tmp;
            tmp = savedInstanceState.getInt("spin31");
            SELECTEDOPTIONSPREDMETI_SPINNERS[2][0] = tmp;
            tmp = savedInstanceState.getInt("spin32");
            SELECTEDOPTIONSPREDMETI_SPINNERS[2][1] = tmp;
            tmp = savedInstanceState.getInt("spin33");
            SELECTEDOPTIONSPREDMETI_SPINNERS[2][2] = tmp;
        }
    }

    /** ###### LIFE cycle ###### */





    //      ##########   INNER CLASSES #########



            public class GetRSSDataTask extends AsyncTask<String, Void, List<FeedEntry> > {

                private MainActivity ma;
                private boolean isRefresh = false;
                private int tabRefresh;
                private ProgressDialog progress;

                /** koristimo da podesimo polja, kada azuriramo liste */
                public void init(MainActivity ma, boolean isRefresh, int tab)
                {
                    this.ma = ma;
                    this.isRefresh = isRefresh;
                    this.tabRefresh = tab;
                }

                @Override
                protected void onPreExecute(){
                    if(mActivity == null)
                        mActivity = this.ma;
                    progress= new ProgressDialog((Context) mActivity, R.style.progressStyle); //AlertDialog.THEME_HOLO_DARK
                    progress.setMessage("Dohvatanje");
                    progress.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                    progress.setProgressStyle(R.style.progressStyle);
                    progress.show();
                }

                @Override
                protected List<FeedEntry> doInBackground(String... urls) {
                    List<FeedEntry>  listTmp = new ArrayList<>(0);

                    try {

                        // Create RSS reader
                        RssReader rssReader = new RssReader(urls[0]);

                        // swith var , ako azuriramo
                        int val = mNum;
                        if(isRefresh)
                            val = tabRefresh;

                        // Parse RSS, get items
                        switch (val)
                        {
                            case 0:
                                if(200 == ConnSSL.urlStatus(LINK_STR_RSS_OBAVESTENJA)) {
                                    rssReader.setHandler(RssReader.GENERIC_HANDLER);
                                    listTmp = rssReader.getRssItems();
                                }
                                OBAVESTENJA_ITEMS = listTmp;
                                break;
                            case 1:
                                if(200 == ConnSSL.urlStatus(LINK_STR_RSS_NAJAVE)) {
                                    rssReader.setHandler(RssReader.GENERIC_HANDLER);
                                    listTmp = rssReader.getRssItems();
                                }
                                NAJAVE_ITEMS = listTmp;
                                break;
                            case 2:
                                if(200 == ConnSSL.urlStatus(LINK_STR_RSS_KALENDAR))
                                {
                                    try {
                                        // dohvatamo resurs

                                        if (isRefresh) {
                                            if (mActivity == null) ;
                                            mActivity = this.ma;
                                        }
                                        Context conx = mActivity;
                                        AssetManager am = conx.getAssets();

                                        InputStream fin;
                                        // otvaramo konekciju
                                        URL url = new URL("https://calendar.google.com/calendar/ical/ict.edu.rs_qiogikrot4cnsjbugnnp02k60c%40group.calendar.google.com/public/basic.ics");   //
                                        HttpsURLConnection conn_ICal = (HttpsURLConnection) url.openConnection();
                                        fin = conn_ICal.getInputStream();
                                        // obrada resursa
                                        ICalWrap iCalReader = new ICalWrap();
                                        iCalReader.init(fin);
                                        // pumpamo item-e
                                        listTmp = iCalReader.getItems();
                                    } catch (Exception e) {
                                        Log.e("ITCRssReader", e.getMessage());
                                    }
                                }
                                ICALC_ITEMS = listTmp;
                                break;

                            case 4:

                                rssReader.setHandler(RssReader.GENERIC_HANDLER);
                                listTmp = rssReader.getRssItems();
                                rezITEMS = listTmp = MainFragment.ISPITI_ITEMS_STUDS;
                                break;


                            default:
                               MainActivity parAct = (MainActivity)getActivity();
                                final ViewPager mainViewPager = parAct.getViewPager();
                                if(mainViewPager!=null) {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mainViewPager.setCurrentItem(3);
                                        }
                                    });


                                }
                        }
                    }
                    catch (Exception e)  { Log.e("ITCRssReader", e.getMessage()); }
                    return listTmp;
                }

                @Override
                protected void onPostExecute(List<FeedEntry> result) {

                    if(progress.isShowing())
                        progress.dismiss();

                    SQLiteDatabase lDB = MainActivity.DB;
                    Cursor c;
                    //FeedEntry item = new FeedEntry();
                    if (result.size() == 0 || false)
                    {
                        result.clear();
                        //pokusaj citati iz baze, u nadi da ima sta snimljeno
                        // tabela koja se cita se odredjuje na osnovu vrednosti - mNum
                        switch (mNum) {
                            case 0:
                                c = lDB.rawQuery("SELECT * FROM obavestenja;", null);
                                while (c.moveToNext()) {
                                    FeedEntry itemO = new FeedEntry();
                                    pumpItem(itemO, c);
                                    result.add(itemO);
                                }
                                break;
                            case 1:
                                c = lDB.rawQuery("SELECT * FROM najave;", null);
                                while (c.moveToNext()) {
                                    FeedEntry itemN = new FeedEntry();
                                    pumpItem(itemN, c);
                                    result.add(itemN);
                                }
                                break;
                            case 2:
                                c = lDB.rawQuery("SELECT * FROM kalendar;", null);
                                while (c.moveToNext()) {
                                    FeedEntry itemK = new FeedEntry();
                                    pumpItem(itemK, c);
                                    result.add(itemK);
                                }
                                break;
                            case 4:
                                break;
                            default:
                        }

                    }
                    else
                    {
                        //upis u bazu novih item-a iz fida
                        // tabela u koju se zapisuje se odredjuje na osnovu vrednosti - mNum
                        if (lDB == null)
                            return; // mesage "nema baze za pristupiti"
                        FeedEntry item_r = new FeedEntry();
                        int size = -1;

                        // swith var , ako azuriramo
                        int val = mNum;
                        if(isRefresh)
                            val = tabRefresh;

                        switch (val) {
                            case 0:

                                c = lDB.rawQuery("SELECT `id` FROM obavestenja;", null);
                                size = c.getCount();
                                if (size > 0)
                                    lDB.execSQL("delete from obavestenja"); //obrisi staro
                                for (int i = 0; i < ((result.size() > 4) ? 4 : result.size()); i++) {
                                    item_r = result.get(i);

                                    /** notifikacija belezenje, iz aplikacije
                                     * upis stavke, najnovije po datumu, u tabelu za notifikaciju,
                                     * da bi smo znali da je korisnik obavesten za sve sa fida 'obavestenja' */
                                    if(i==0)
                                    {
                                        String datumStavke = item_r.date.toString();
                                        Cursor locDbCur = lDB.rawQuery("SELECT * FROM obavestenjaXmlStatus LIMIT 1",  null);
                                        int stavkaBroj = locDbCur.getCount();
                                        if(stavkaBroj == 0) {
                                            lDB.execSQL("INSERT INTO obavestenjaXmlStatus (`naziv`,`vrednost`) VALUES ('zadnjaOcitanaStavka', '" + datumStavke + "' )");
                                        }
                                        else if(stavkaBroj == 1) {
                                            lDB.execSQL("UPDATE obavestenjaXmlStatus  SET `vrednost` = '" + datumStavke + "'  WHERE naziv = 'zadnjaOcitanaStavka' ");
                                        }
                                    }
                                    /** */

                                    /** upis svih stavki u bazu */
                                    lDB.execSQL("INSERT INTO obavestenja (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                            + " VALUES ( '" + item_r.rssItemId + "' , '" + item_r.title + "' , '" + item_r.desc + "' , '" + item_r.content + "' , '" + item_r.url + "' , '"
                                            + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + item_r.date.toString() + "' , '" + item_r.dateUpdate.toString()
                                            + "' , '" + String.valueOf(mNum) + "' ); ");
                                }
                                break;
                            case 1:
                                c = lDB.rawQuery("SELECT `id` FROM najave;", null);
                                size = c.getCount();
                                if (size > 0)
                                    lDB.execSQL("delete from najave"); //obrisi staro
                                int limit = (result.size() > 4) ? 4 : result.size();
                                for (int i = 0; i < limit; i++) {
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

                                if (size > 0)
                                    lDB.execSQL("delete from kalendar"); //obrisi staro

                                int limit2 = result.size();
                                if(limit2>4)
                                    limit2 = 4;
                                for (int i = 0; i < limit2; i++) {
                                    item_r = result.get(i);

                                    Date datumCalURL = new Date();
                                    SimpleDateFormat formatCalDTStamp = new SimpleDateFormat("yyyyMMdd");
                                    try {
                                        datumCalURL = formatCalDTStamp.parse(  item_r.dtstart );
                                    }catch (Exception e){}

                                    // date koristimo za dtstart , posto je date uvek null
                                    lDB.execSQL("INSERT INTO kalendar (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                                            + " VALUES ( '" + item_r.id + "' , '" + item_r.summary + "' , '" + item_r.description + "' , '" + item_r.description + "' , '" + item_r.url + "' , '"
                                            + item_r.creator + "' , '" + item_r.email + "' , '" + item_r.category + "' , '" + datumCalURL + "' , '" + item_r.dateUpdate.toString() + "' , '"
                                            + String.valueOf(mNum) + "' ); ");
                                }
                                break;
                            case 4:
                                break;
                        }
                    }

                    /** prihvatanje resulta popuna statickog niza
                     * koji sluzi kao izvor za adapter.
                     * Popuna adaptera istim */
                    // ako nije azuriranje, uzimamo onako kako se fragmeti kreiraju sa mNum
                    if(!isRefresh) {
                        if (  !isRefresh && mNum == 0 ) {
                            mcAdapterL1.addAll(result);
                        }
                        if (  !isRefresh && mNum == 1 ) {
                            mcAdapterL2.addAll(result);
                        }
                        if ( !isRefresh && mNum == 2 ) {
                            mcAdapterL3.addAll(result);
                        }
                    }
                    else
                    {
                        // za azuriranje nam trebaju vrednosti zapravo selektovanog taba
                        if (tabRefresh == 0) {
                            mcAdapterL1.addAll(result);
                        }
                        if (tabRefresh == 1) {
                            mcAdapterL2.addAll(result);
                        }
                        if (tabRefresh == 2) {
                            mcAdapterL3.addAll(result);
                        }
                    }
                    /** ----  */
                }
            }
}
