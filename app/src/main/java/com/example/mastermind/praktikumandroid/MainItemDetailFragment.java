package com.example.mastermind.praktikumandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link MainItemDetailActivity}
 * on handsets.
 */
public class MainItemDetailFragment extends Fragment {


    public static final String ARG_ITEM_ID = "item_id"; // The fragment argument representing the item ID that this fragment represents.

    private FeedEntry mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MainItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Bundle args = getArguments();
            String s = getArguments().getString(ARG_ITEM_ID);
            int idItem = Integer.parseInt(s);
            int tabSelected =  args.getInt("tabSelected");
            FeedEntry item;
            switch (tabSelected)
            {
                case 0:
                    mItem = MainFragment.obavestenjaITEMS.get(idItem);
                    mItem.fromTab = ""+tabSelected;
                    break;
                case 1:
                    mItem = MainFragment.najaveITEMS.get(idItem);
                    mItem.fromTab = ""+tabSelected;
                    break;
                case 2:
                    mItem = MainFragment.kalendarITEMS.get(idItem);
                    mItem.fromTab = ""+tabSelected;
                    break;
                case 3:
                    mItem = MainFragment.favsLocal_DB_ITEMS.get(idItem);
                    break;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the content as text in a TextView.
       if (mItem != null) {
           TextView titleTV = ((TextView) rootView.findViewById(R.id.item_title));
           //((TextView) rootView.findViewById(R.id.item_link)).setText("" + mItem.url);
           TextView mainTV = (TextView) rootView.findViewById(R.id.item_detail);
           ImageButton imGoWww = (ImageButton)rootView.findViewById(R.id.go_www);
           //Button b = (Button)rootView.findViewById(R.id.item_link);

           switch (mItem.getFlag())
           {
               case FeedEntry.RSS:

                   titleTV.setText("" + mItem.title);
                   mainTV.setText("" + mItem.desc  + " \n " + mItem.content + "\n"
                           + "Kategotrija - " + mItem.category + "\n"
                           + "\n" + "od " + mItem.creator  );
                   if(mItem.email!=null)
                       mainTV.append(" @ " +mItem.email );

                   mainTV.append("\r\n");

                   if(mItem.date!=null)
                       mainTV.append("\n/originalno: " + mItem.date);
                   if(mItem.dateUpdate!=null)
                       mainTV.append("\n/azurirano: " + mItem.dateUpdate);

                   // setovano za koriscebnje pri URI intent lansiranju,
                   // pokrece pretrazivac i ucitava link povezan sa stavkom(item)
                   //b.setTag(mItem);

                   imGoWww.setTag(mItem);

                   break;


               case FeedEntry.ICAL:
                   mainTV.setText("" + mItem.summary + "\n" );

                   if(mItem.description!=null)

                   titleTV.setText("" + mItem.summary);
                        mainTV.append(mItem.description);

                   mainTV.append( " \n\n"
                           + "Start: " + mItem.dtstart + "\n"
                           + "Kraj: " + mItem.dtend + "\n"
                           + "\n" + " "  );

                   mainTV.append("\r\n");

                   if(mItem.firstcreated!=null)
                       mainTV.append("\n/originalno: " + mItem.firstcreated);
                   if(mItem.lastmodified!=null)
                       mainTV.append("\n\n/azurirano: " + mItem.lastmodified);

                   imGoWww.setVisibility(View.INVISIBLE);
                   imGoWww.setEnabled(false);

                   break;

           }


            ImageButton ibtnAddFav = (ImageButton)rootView.findViewById(R.id.onClk_addFav);
            ibtnAddFav.setTag(mItem);
            ImageButton ibtnRmvFav = (ImageButton)rootView.findViewById(R.id.onClk_removeFav);
            ibtnRmvFav.setTag(mItem);

            // ako ves sacuvan u favoritima, prilagoditi UI da to reflektuje
            ibtnAddFav.setEnabled(true);
            ibtnAddFav.setClickable(true);
            ibtnRmvFav.setEnabled(false);
            ibtnRmvFav.setClickable(false);
            if( MainFragment.favsLocal_DB_ITEMS.size()>0) {
                for (FeedEntry notItem : MainFragment.favsLocal_DB_ITEMS) {
                    if (notItem.rssItemId.equals(mItem.rssItemId)) {
                        ibtnAddFav.setEnabled(false);
                        ibtnAddFav.setClickable(false);
                        ibtnRmvFav.setEnabled(true);
                        ibtnRmvFav.setClickable(true);
                        break;
                    }
                }
            }

            TextView tw = (TextView)rootView.findViewById(R.id.item_detail);
            tw.setClickable(true);
        }

        return rootView;
    }






}
