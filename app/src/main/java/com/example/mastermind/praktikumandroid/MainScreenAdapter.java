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
import java.util.List;


/**
 * Created by Mastermind on 15-Jun-15.
 */
public class MainScreenAdapter extends ArrayAdapter<FeedEntry> {

    public List<FeedEntry> feedEntryListITEMS = new ArrayList<FeedEntry>();
    Activity context;
    LayoutInflater inflater ;
    private int tab;

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

        View view = convertView; // usteda
        TextView title;
        TextView date;

        if (view == null)
        {
            view = inflater.inflate(R.layout.main_list_item_pattern_favs, parent, false);
        }
        title = (TextView) view.findViewById(R.id.tvTitle);
        title.setTextSize(15);
        date = (TextView) view.findViewById(R.id.tvDate);

        if(tab==2)/* ako kalendar, onda dizajn za njega... */
        {

            title.setText("\n'" + item.summary + " '"); //item.fromTab + " ID: " + item.id +" rID: "+ item.rssItemId +

            if(item.lastmodified!=null)
            {

                String priStr = item.lastmodified;
                String[] a_priStr = priStr.split("Z"); //eleminisemo Z
                a_priStr = a_priStr[0].split("T"); //delimo na T, datum od vremena
                if(a_priStr[0].length() != 8){a_priStr[0]="0";}
                /* stringovi datuma */
                String y = a_priStr[0].substring(0,4);
                String m = a_priStr[0].substring(4,6);
                String d = a_priStr[0].substring(6,8);
                /* stringovi vremena */
                String s = a_priStr[1].substring(0,2);
                String mi = a_priStr[1].substring(2,4);
                String se = a_priStr[1].substring(4,6);

                date.setText(d + "/" + m + "/" + y + " " + s + ":" + mi); //fmt2.format(a_priStr[0])
                date.setTextSize(13);
            }
        }
        else
        { /* ...za ostale tabove */

            title.setText("\n'" + item.title + " '"); //item.fromTab + " ID: " + item.id +" rID: "+ item.rssItemId +

            SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if(item.date!=null)
            {
                fmt2.format(item.date);
                date.setText(fmt2.format(item.date));
                date.setTextSize(13);
            }

        }



        int tab = ((MainActivity)context).getActionBar().getSelectedTab().getPosition();

        ImageButton imgButton = (ImageButton) view.findViewById(R.id.imgButton);
        imgButton.setClickable(true);
        imgButton.setEnabled(true);
        imgButton.setFocusable(false); // anti focus grab

        ImageView ico = (ImageView)view.findViewById(R.id.ivIco);
        int [] iconsForTabs = { R.drawable.ic_action,
                                R.drawable.ic_action_clock,
                                R.drawable.ic_action_calcool,
                                R.drawable.ic_action_favbook_dblue  };


        switch (this.tab)
        {
            case 0:
                ico.setImageResource(iconsForTabs[0]);
                if (MainFragment.favsLocal_DB_ITEMS.size() > 0) {
                    for (FeedEntry itemToCompTo : MainFragment.favsLocal_DB_ITEMS) {

                        if (((FeedEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                            // imgButton.setClickable(false);
                            imgButton.setEnabled(false);
                            imgButton.setVisibility(View.INVISIBLE);
                            break;
                        }else{
                            imgButton.setEnabled(true);
                            imgButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case 1:
                ico.setImageResource(iconsForTabs[1]);
                if (MainFragment.favsLocal_DB_ITEMS.size() > 0) {
                    for (FeedEntry itemToCompTo : MainFragment.favsLocal_DB_ITEMS) {

                        if (((FeedEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                            // imgButton.setClickable(false);
                            imgButton.setEnabled(false);
                            break;
                        }else{
                            imgButton.setEnabled(true);
                            imgButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case 2:
                ico.setImageResource(iconsForTabs[2]);
                if (MainFragment.favsLocal_DB_ITEMS.size() > 0) {
                    for (FeedEntry itemToCompTo : MainFragment.favsLocal_DB_ITEMS) {

                        if (((FeedEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                            // imgButton.setClickable(false);
                            imgButton.setEnabled(false);
                            break;
                        }else{
                            imgButton.setEnabled(true);
                            imgButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
            case 3:
                ico.setImageResource(iconsForTabs[3]);

                imgButton.setEnabled(true);
                item.fromTab = "3";

                break;
        }

        imgButton.setTag(item);
        view.setTag(item);
        return view;
    }


    /**
     *  help set tab, so we know for what
     *  fragment we are beeing set
     *  */
    public void setTab(int tabIndex)
    {
        this.tab = tabIndex;
    }

}
