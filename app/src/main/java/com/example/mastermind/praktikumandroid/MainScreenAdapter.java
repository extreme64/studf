package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mastermind.praktikumandroid.rss.RssEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mastermind on 15-Jun-15.
 */
public class MainScreenAdapter extends ArrayAdapter<Entry> {

    public List<Entry> notfTestListITEMS = new ArrayList<Entry>();
    Activity context;
    LayoutInflater inflater ;
    private int tab;

    public MainScreenAdapter(Context context, List<Entry> objects) {
        super(context, 0, objects);
        this.context = (Activity)context;
        this.notfTestListITEMS = (List<Entry>)objects;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(notfTestListITEMS==null)
            return 0;
        return notfTestListITEMS.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RssEntry ecitem = (RssEntry)notfTestListITEMS.get(position);
        RssEntry item = ecitem;
        View view = convertView;

        if (view == null)
        {
            if(tab==3)
            {
                view = inflater.inflate(R.layout.main_list_item_pattern_favs, parent, false);
            }
            else{ view = inflater.inflate(R.layout.main_list_item_pattern, parent, false); }
        }
        TextView title = (TextView)view.findViewById(R.id.tvTitle);
        title.setTextSize(15);

        TextView date = (TextView)view.findViewById(R.id.tvDate);
        title.setText("\n'" + item.title + " '"); //item.fromTab + " ID: " + item.id +" rID: "+ item.rssItemId +
        SimpleDateFormat fmt2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if(item.date!=null)
        {
            fmt2.format(item.date);
            date.setText(fmt2.format(item.date));
            date.setTextSize(13);
        }

        int tab = ((MainActivity)context).getActionBar().getSelectedTab().getPosition();

        ImageButton imgButton = (ImageButton) view.findViewById(R.id.imgButton);
        /**
         * possibly multiple resetting of properties, causes UI to misbehave !!!! SOLVED do nothing
         */
        //imgButton.setClickable(true);
        //imgButton.setEnabled(true);
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
                    for (Entry itemToCompTo : MainFragment.favsLocal_DB_ITEMS) {

                        if (((RssEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                            // imgButton.setClickable(false);
                            imgButton.setEnabled(false);
                            break;
                        }
                    }
                }
                break;
            case 1:
                ico.setImageResource(iconsForTabs[1]);
                if (MainFragment.favsLocal_DB_ITEMS.size() > 0) {
                    for (Entry itemToCompTo : MainFragment.favsLocal_DB_ITEMS) {

                        if (((RssEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                            // imgButton.setClickable(false);
                            imgButton.setEnabled(false);
                            break;
                        }
                    }
                }
                break;
            case 2:
                ico.setImageResource(iconsForTabs[2]);
                if (MainFragment.favsLocal_DB_ITEMS.size() > 0) {
                    for (Entry itemToCompTo : MainFragment.favsLocal_DB_ITEMS) {

                        if (((RssEntry)itemToCompTo).rssItemId.equals(item.rssItemId)) {
                            // imgButton.setClickable(false);
                            imgButton.setEnabled(false);
                            break;
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
