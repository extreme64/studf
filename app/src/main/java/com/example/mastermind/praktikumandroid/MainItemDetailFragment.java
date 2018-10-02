package com.example.mastermind.praktikumandroid;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link MainItemDetailActivity}
 * on handsets.
 */
public class MainItemDetailFragment extends Fragment {


	public  static MainItemDetailActivity midActivity;

	//prosecna ocena
	public static double PROSEK = 0.0;
	public static TextView tw_prosecnaOcenaDetalji;
	//student toolbar zaglavlje text polje
	public static TextView tw_StudentZaglavljeDetalji;

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

		midActivity = (MainItemDetailActivity)this.getActivity();



		if (getArguments().containsKey(ARG_ITEM_ID)) {
			Bundle args = getArguments();
			String s = getArguments().getString(ARG_ITEM_ID);
			int idItem = Integer.parseInt(s);
			int tabSelected =  args.getInt("tabSelected");
			FeedEntry item;
			switch (tabSelected)
			{
				case 0:
					mItem = MainFragment.OBAVESTENJA_ITEMS.get(idItem);
					mItem.fromTab = ""+tabSelected;
					break;
				case 1:
					mItem = MainFragment.NAJAVE_ITEMS.get(idItem);
					mItem.fromTab = ""+tabSelected;
					break;
				case 2:
					mItem = MainFragment.ICALC_ITEMS.get(idItem);
					mItem.fromTab = ""+tabSelected;
					break;
				case 3:
					mItem = MainFragment.FAVS_DB_ITEMS.get(idItem);
					break;
			}

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);


		/* setovanje referenci */
		tw_prosecnaOcenaDetalji = (TextView) midActivity.findViewById(R.id.toolbarOcena);
		tw_StudentZaglavljeDetalji = (TextView) midActivity.findViewById(R.id.toolbarTextSpace);
		/* */

		// Show the content as text in a TextView.
		if (mItem != null) {
			TextView titleTV = ((TextView) rootView.findViewById(R.id.item_title));
			//((TextView) rootView.findViewById(R.id.item_link)).setText("" + mItem.url);
			TextView mainTV = (TextView) rootView.findViewById(R.id.item_detail);
            TextView mainTV2 = (TextView) rootView.findViewById(R.id.item_detail2);
			ImageButton imGoWww = (ImageButton)rootView.findViewById(R.id.go_www);


			switch (mItem.getFlag())
            {
                case FeedEntry.RSS:

                    // naslov
                    titleTV.setText("" + mItem.title);

                    // opis
                    mainTV.setText("" + mItem.desc + " \n " + mItem.content + "\n");
                    if (!mItem.category.equals("")) {
                        mainTV.append("Kategotrija - " + mItem.category + "\n");
                    }
                    mainTV.append("\n" + "od " + mItem.creator);
                    if (mItem.email != null)
                        mainTV.append(" @ " + mItem.email);


                    // datumi
                    String dateAsString = "";
                    mainTV2.setText("");

                    SimpleDateFormat formatAdapter = new SimpleDateFormat("EEE, dd MMM yyyy");
                    if (mItem.dateUpdate != null){
                        try {
                            dateAsString = formatAdapter.format(mItem.dateUpdate);
                        }catch (Exception e){}
                        mainTV2.append("/azurirano: " + dateAsString);
                    }
                    if (mItem.date != null) {
                        try {
                            dateAsString = formatAdapter.format(mItem.date);
                        }catch (Exception e){}
                        mainTV2.append("\n/originalno: " + dateAsString);
                    }

					// setovano za koriscebnje pri URI intent lansiranju, link povezan sa stavkom(item)

					imGoWww.setTag(mItem);

					break;


				case FeedEntry.ICAL:
					mainTV.setText("" + mItem.summary + "\n" );

					if(mItem.description!=null)

						titleTV.setText("" + mItem.summary);
					mainTV.append(mItem.description);



                    // datumi
                    String dateAsString2 = "";
                    mainTV2.setText("");
                    SimpleDateFormat formatAdapterURL = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat formatAdapterICAL = new SimpleDateFormat("EEE, dd MMM yyyy");
                    if (mItem.dtstart != null){
                        try {
                            dateAsString2 = mItem.dtstart;
                            Date dateDB_ICAL = formatAdapterURL.parse(dateAsString2);
                            dateAsString2 = formatAdapterICAL.format(dateDB_ICAL);
                        }catch (Exception e){}
                        mainTV2.append("/start: " + dateAsString2);
                    }
                    if (mItem.dtend != null) {
                        try {
                            dateAsString2 = mItem.dtend;
                            Date dateDB_ICAL = formatAdapterURL.parse(dateAsString2);
                            dateAsString2 = formatAdapterICAL.format(dateDB_ICAL);
                        }catch (Exception e){}
                        mainTV2.append("\n/kraj: " + dateAsString2);
                    }

					imGoWww.setVisibility(View.INVISIBLE);
					imGoWww.setEnabled(false);

					break;

				default:
					titleTV.setText("" + mItem.title);
					mainTV.setText("" + mItem.desc  + " \n " + mItem.content + "\n");

					if (!mItem.category.equals("")) {
						mainTV.append("Kategotrija - " + mItem.category + "\n");
					}
					mainTV.append("\n" + "od " + mItem.creator);
					if (mItem.email != null)
						mainTV.append(" @ " + mItem.email);


					// datumi
					String dateAsStringF = "";
					mainTV2.setText("");
					SimpleDateFormat formatAdapterF = new SimpleDateFormat("EEE, dd MMM yyyy");
					if (mItem.dateUpdate != null){
						try {
							dateAsStringF = formatAdapterF.format(mItem.dateUpdate);
						}catch (Exception e){}
						mainTV2.append("/azurirano: " + dateAsStringF);
					}
					if (mItem.date != null) {
						try {
							dateAsStringF = formatAdapterF.format(mItem.date);
						}catch (Exception e){}
						mainTV2.append("\n/originalno: " + dateAsStringF);
					}


					//koriscebnje pri URI intent lansiranju,
					imGoWww.setTag(mItem);
			}


			ImageButton ibtnAddFav = (ImageButton)rootView.findViewById(R.id.onClk_addFav);
			ibtnAddFav.setTag(mItem);
			ImageButton ibtnRmvFav = (ImageButton)rootView.findViewById(R.id.onClk_removeFav);
			ibtnRmvFav.setTag(mItem);
			if(mItem.getFlag() == FeedEntry.ICAL)
			{
				ibtnAddFav.setVisibility(View.GONE);
				ibtnRmvFav.setVisibility(View.GONE);
			}else {
				// ako ves sacuvan u favoritima, prilagoditi UI da to reflektuje
				ibtnAddFav.setEnabled(true);
				ibtnAddFav.setClickable(true);
				ibtnRmvFav.setEnabled(false);
				ibtnRmvFav.setClickable(false);
				if (MainFragment.FAVS_DB_ITEMS.size() > 0) {
					for (FeedEntry notItem : MainFragment.FAVS_DB_ITEMS) {
						if (notItem.rssItemId.equals(mItem.rssItemId)) {
							ibtnAddFav.setEnabled(false);
							ibtnAddFav.setClickable(false);
							ibtnRmvFav.setEnabled(true);
							ibtnRmvFav.setClickable(true);
							break;
						}
					}
				}
			}

			TextView tw = (TextView)rootView.findViewById(R.id.item_detail);
			tw.setClickable(true);
		}

		return rootView;
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

		if (tw_prosecnaOcenaDetalji != null) {
			tw_prosecnaOcenaDetalji.setText(MainFragment.PROSEK + "");
		}
		if (tw_StudentZaglavljeDetalji != null) {
			tw_StudentZaglavljeDetalji.setText(MainFragment.STUDENT_ZAGLAVLJE_TEKST.toUpperCase());
		}

	}


}
