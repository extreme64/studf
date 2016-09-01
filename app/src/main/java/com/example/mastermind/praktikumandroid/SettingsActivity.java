package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Mastermind on 04-Jul-15.
 */
public class SettingsActivity extends Activity {

    SeekBar intervalControl;
    static int INTERVAL_OFFSET = 10; // u sekundama ofset, za minimalnu vrednos ponvljanja (0-3000 -> 60-3600) koristi se u servisu
    int intervalAutoCheckFeeds;
    boolean doAutoCheck;
    SQLiteDatabase lDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        lDB = MainActivity.DB;

        /** setujemo vrednsoti za UI */
        Cursor c = lDB.rawQuery("SELECT * FROM settings WHERE `key`='autocheckinterval' ;", null);
        if(c.moveToFirst())
            intervalAutoCheckFeeds = Integer.valueOf(c.getString(2));
        SeekBar intervalControl = (SeekBar) findViewById(R.id.seekBarAutoProveraInterval);
        intervalControl.setProgress(intervalAutoCheckFeeds);

        c = lDB.rawQuery("SELECT * FROM settings WHERE `key`='autocheck' ;", null);
        if(c.moveToFirst())
            if(c.getString(2).equals("true"))
                doAutoCheck = true;
            else
                doAutoCheck = false;
        Switch swchAutoProvera = (Switch) findViewById(R.id.switchAutoProvera);
        swchAutoProvera.setChecked(doAutoCheck);

        /** seekBar Listener */
        intervalControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                intervalAutoCheckFeeds = progressChanged = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onStopTrackingTouch(SeekBar seekBar) {
                intervalAutoCheckFeeds = progressChanged;
                // novo stanje seekBar-a azurirati u bazi
                lDB.execSQL("UPDATE settings SET `value`='" + intervalAutoCheckFeeds + "' WHERE `key`='autocheckinterval' ;");
                Toast.makeText(SettingsActivity.this, "Interval provere:" + ((Integer)((INTERVAL_OFFSET + intervalAutoCheckFeeds) / 60)) + " min.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onClk_autoproveraswitch(View view)
    {
        boolean on = ((Switch) view).isChecked();
        if (on){
            doAutoCheck=true;
            Intent serviceIntent = new Intent("com.example.mastermind.praktikumandroid.CheckServerService");
            this.startService(serviceIntent);
        } else {
            doAutoCheck = false;
            stopService(new Intent(this, CheckServerService.class));
        }
        // novo stanje Auto-provere azurirati u bazi

        lDB.execSQL("UPDATE settings SET `value`='" + doAutoCheck + "' WHERE `key`='autocheck' ;");


        Toast.makeText(SettingsActivity.this, "Auto-provera status promenjen", Toast.LENGTH_SHORT).show();
    }


}
