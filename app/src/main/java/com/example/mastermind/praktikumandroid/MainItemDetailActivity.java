package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.URL;


/**
 * An activity representing a single Item detail screen. This activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items in a {@link MainActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing more than a {@link MainItemDetailFragment}.
 */
public class MainItemDetailActivity extends FragmentActivity {


    SQLiteDatabase lDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Show the Up button in the action bar.
       //getSupportActionBar().setDisplayHomeAsUpEnabled(true); izmena 1

        // savedInstanceState is non-null when there is fragment state saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape). In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it. For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {

            lDB = MainActivity.DB;

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(MainItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(MainItemDetailFragment.ARG_ITEM_ID));
            arguments.putInt("tabSelected",
                    getIntent().getIntExtra("tabSelected", -1));
            MainItemDetailFragment fragment = new MainItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back

            navigateUpTo(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** otvaranje linka stavke fida,
     * klik na dugme u detaljnom prikazu (frag.)
     */
    public void item_goto_url(View v)
    {
        String url=((NotfTest)v.getTag()).url;
        if (!url.startsWith("https://") && !url.startsWith("http://"))
            url = "http://" + url;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }


    public void onClk_addFav_dtlFrag(View v)
    {
        Cursor cur;
        NotfTest item = (NotfTest)v.getTag();


        cur = lDB.rawQuery("SELECT `rssItemId` FROM favs WHERE `rssItemId`='" + item.rssItemId + "' ;", null);
        if(cur.getCount()>0)
                return;
        lDB.execSQL("INSERT INTO favs (rssItemId, title, desc, content, url, creator, email, category, date, dateUpdate, tab) "
                + " VALUES ( '" + item.rssItemId + "' , '" + item.title + "' , '" + item.desc + "' , '" + item.content + "' , '" + item.url + "' , '"
                + item.creator + "' , '" + item.email + "' , '" + item.category + "' , '" + item.date.toString() + "' , '" + item.dateUpdate.toString() + "', '" + item.fromTab+"' ); ");

        Toast.makeText(this, "Faved it: " + item.rssItemId, Toast.LENGTH_LONG).show();
    }

    public void onClk_removeFav_dtlFrag(View v)
    {

        NotfTest item = (NotfTest)v.getTag();
        String[] arg = { item.rssItemId };
        if(lDB==null)
            lDB = MainActivity.DB;

        int retStat = lDB.delete("favs", "rssItemId = ?", arg );
        Activity tha = this;
        if(retStat==1)
            Toast.makeText(tha, "Deleted FAV "+arg[0], Toast.LENGTH_LONG).show();
        // go back to list activity, pack a TAB index too, to open proper tab
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tabToSetAsCurrent", item.fromTab);
        startActivity(intent);
    }




}
