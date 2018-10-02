package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by Mastermind on 11-Jan-17.
 */
public class MainScreenAdapter extends ArrayAdapter<FeedEntry> {

    public List<FeedEntry> feedEntryListITEMS = new ArrayList<FeedEntry>();
    Activity context;
    LayoutInflater inflater ;
    private int tab;
    private int inflater_layout;

    public MainScreenAdapter(Context context, List<FeedEntry> objects) {
        super(context, 0, objects);
        this.context = (Activity)context;
        this.feedEntryListITEMS = (List<FeedEntry>)objects;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(feedEntryListITEMS ==null)
            return 0;
        return feedEntryListITEMS.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FeedEntry ecitem = (FeedEntry) feedEntryListITEMS.get(position);
        FeedEntry item = ecitem;

        View view = convertView; // saving res.
        TextView title;
        TextView date;

        if (view == null)
        {
            if(this.tab==MainActivity.TAB_5_1 +1)
            {
                view = inflater.inflate(this.inflater_layout, parent, false);

            }
            else
            {
                view = inflater.inflate(R.layout.main_list_item_pattern_favs, parent, false);
            }

        }
        if(this.tab==MainActivity.TAB_5_1 +1)
        {
            view = inflater.inflate(this.inflater_layout, parent, false);
        }
        else
        {

        }
        title = (TextView) view.findViewById(R.id.tvTitle);
        title.setTextSize(15);
        date = (TextView) view.findViewById(R.id.tvDate);


        /** popuna ispisa u zavisnosti od tip podataka
         * kojim popunjavamo listu */
        if(this.tab==MainActivity.TAB_3)/* ako kalendar, onda dizajn za njega... 2 */
        {
            title.setText("\n'" + item.title + " '");
            if((item.title).equals("") || item.title==null )
            {
                title.setText("\n'" + item.summary + " '");
            }


            String dateAsString = "";
            /** datum setovanje */
            // kada citamo iz baze, radimo sa date objektom
            if(item.date != null)
            {
                SimpleDateFormat formatAdapter = new SimpleDateFormat("EEE, dd MMM yyyy");
                try {
                    dateAsString = formatAdapter.format(item.date);
                }catch (Exception e){}
            }
            // kada citamo sa URL-a imamo string
            else if(item.dtstart != null)
            {
                dateAsString = item.dtstart;

                SimpleDateFormat formatAdapterURL = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat formatAdapter = new SimpleDateFormat("EEE, dd MMM yyyy");
                try {
                    Date dateDB = formatAdapterURL.parse(dateAsString);
                    dateAsString = formatAdapter.format(dateDB);
                }catch (Exception e){}
            }
            date.setText(dateAsString);
            date.setTextSize(13);

        }
        else
        { /* ...za ostale tabove */

            title.setText("\n'" + item.title + " '");

            SimpleDateFormat fmt2 = new SimpleDateFormat("EEE, dd MM yyyy HH:mm");

            if(item.date!=null)
            {
                fmt2.format(item.date);
                date.setText(fmt2.format(item.date));
                date.setTextSize(13);
            }

        }





        ImageButton imgButton = (ImageButton) view.findViewById(R.id.imgButton);
        imgButton.setClickable(true);
        imgButton.setEnabled(true);
        imgButton.setFocusable(false); // anti focus grab

        ImageView ico = (ImageView)view.findViewById(R.id.ivIco);
        int [] iconsForTabs = { R.drawable.ic_action,
                                R.drawable.ic_action_clock,
                                R.drawable.ic_action_calcool,
                                R.drawable.ic_action_favbook_dblue  };

        /** FavIt UI
         * odlucivanje UI, da li smo neku stavku vec favovali ili ne */
        switch (this.tab)
        {
        case 0:
            ico.setImageResource(iconsForTabs[0]);
            if (MainFragment.FAVS_DB_ITEMS.size() > 0) {
                for (FeedEntry itemToCompTo : MainFragment.FAVS_DB_ITEMS) {

                    if (((FeedEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                        // imgButton.setClickable(false);
                        imgButton.setEnabled(false);
                        imgButton.setVisibility(View.INVISIBLE);
                        break;
                    }else{
                        imgButton.setEnabled(true);
                        //imgButton.setVisibility(View.VISIBLE);
                    }
                }
            }
            break;
        case 1:
            ico.setImageResource(iconsForTabs[1]);
            if (MainFragment.FAVS_DB_ITEMS.size() > 0) {
                for (FeedEntry itemToCompTo : MainFragment.FAVS_DB_ITEMS) {

                    if (((FeedEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                        // imgButton.setClickable(false);
                        imgButton.setEnabled(false);
                        break;
                    }else{
                        imgButton.setEnabled(true);
                        //imgButton.setVisibility(View.VISIBLE);
                    }
                }
            }
            break;
        case 2:
            // sklanjamo dugme iz sablona, ne treba za kalendar
            imgButton.setEnabled(false);
            imgButton.setVisibility(View.GONE);

            break;
        case 3: // FAVS
            ico.setImageResource(iconsForTabs[3]);

            imgButton.setEnabled(true);
            item.fromTab = "3";

            break;
        case 4:
            break;
        }
        /* end; FavIt UI */

        imgButton.setTag(item);
        view.setTag(item);
        return view;
    }


    /**
     *  help set tab, so we know for what
     *  fragment we are being set
     *  */
    public void setTab(int tabIndex)
    {
        this.tab = tabIndex;
    }

    public void setLayout(int r)
    {
        this.inflater_layout = r;
    }
}
