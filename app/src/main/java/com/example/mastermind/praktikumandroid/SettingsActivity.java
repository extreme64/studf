package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastermind.praktikumandroid.notificationServis.CheckForNewOnlineItems_Service;

/**
 * Created by Mastermind on 04-Jul-15.
 */
public class SettingsActivity extends Activity {

    SQLiteDatabase lDB;

    String mejl = "";
    String lozinka = "";


    SeekBar intervalControl;
    public static int INTERVAL_OFFSET = 10; // u sekundama ofset, za minimalnu vrednos ponvljanja (0-3000 -> 60-3600) koristi se u servisu
    int intervalAutoCheckFeeds;
    boolean doAutoCheck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        lDB = MainActivity.DB;

        /** setujemo vrednsoti za UI */
        /* korisnik param / mejl UI*/
        Cursor cKor = lDB.rawQuery("SELECT `vrednost` FROM `korisnikParam` WHERE `naziv` = 'mejl' LIMIT 1", null);
        if(cKor.moveToFirst())
            mejl = cKor.getString(0);
        Log.i("settEvent", "m: " + mejl);
        TextView txMejl = (TextView) findViewById(R.id.et_mejlLogovanje);
        // setujemo vrednost UI iz baze
        txMejl.setText(mejl);
        txMejl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /** upis u bazu */
                if(s.length() > 0)
                {
                    TextView txMejl = (TextView) findViewById(R.id.et_mejlLogovanje);
                    Cursor locDbCur = lDB.rawQuery("SELECT * FROM `korisnikParam` WHERE naziv = 'mejl' LIMIT 1",  null);
                    String text = txMejl.getText().toString();
                    if(text.length() > 0) {
                        if (locDbCur.getCount() == 0) {
                            lDB.execSQL("INSERT INTO `korisnikParam` (`naziv`,`vrednost`) VALUES ('mejl', '" + text + "')");
                            Log.i("settEvent M", "INSERT " + text);
                        } else if (locDbCur.getCount() == 1) {
                            lDB.execSQL("UPDATE `korisnikParam`  SET `vrednost` = '" + text + "'  WHERE naziv = 'mejl'");
                            Log.i("settEvent M", "UPDATE " + text);
                        }
                    }
                }
            }
        });
        /* korisnik param / sifra UI*/
        cKor = lDB.rawQuery("SELECT `vrednost` FROM `korisnikParam` WHERE `naziv` = 'lozinka' LIMIT 1", null);
        if(cKor.moveToFirst())
            lozinka = cKor.getString(0);
        Log.i("settEvent", "l: " + lozinka);
        TextView txLozinka = (TextView) findViewById(R.id.et_lozinkaLogovanje);
        // setujemo vrednost UI iz baze
        txLozinka.setText(lozinka);
        txLozinka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                /** upis u bazu */
                if(s.length() > 0)
                {
                    TextView txLozinka = (TextView) findViewById(R.id.et_lozinkaLogovanje);
                    Cursor locDbCur = lDB.rawQuery("SELECT * FROM `korisnikParam` WHERE naziv = 'lozinka' LIMIT 1",  null);
                    String text = txLozinka.getText().toString();
                    if(text.length() > 0) {
                        if (locDbCur.getCount() == 0) {
                            lDB.execSQL("INSERT INTO `korisnikParam` (`naziv`,`vrednost`) VALUES ('lozinka', '" + text + "')");
                            Log.i("settEvent L", "INSERT " + text);
                        } else if (locDbCur.getCount() == 1) {
                            lDB.execSQL("UPDATE `korisnikParam`  SET `vrednost` = '" + text + "'  WHERE naziv = 'lozinka'");
                            Log.i("settEvent L", "UPDATE " + text);
                        }
                    }
                }
            }
        });


        /* notif servis / interval UI*/
        Cursor c = lDB.rawQuery("SELECT * FROM settings WHERE `key`='autocheckinterval' ;", null);
        if(c.moveToFirst())
            intervalAutoCheckFeeds = Integer.valueOf(c.getString(2));
        SeekBar intervalControl = (SeekBar) findViewById(R.id.seekBarAutoProveraInterval);
        // setujemo vrednost UI iz baze
        intervalControl.setProgress(intervalAutoCheckFeeds);
        /* notif servis / switch UI*/
        c = lDB.rawQuery("SELECT * FROM settings WHERE `key`='autocheck' ;", null);
        if(c.moveToFirst())
            if(c.getString(2).equals("true"))
                doAutoCheck = true;
            else
                doAutoCheck = false;
        Switch swchAutoProvera = (Switch) findViewById(R.id.switchAutoProvera);
        // setujemo vrednost UI iz baze
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
            stopService(new Intent(this, CheckForNewOnlineItems_Service.class));
        }

        // novo stanje Auto-provere azurirati u bazi
        lDB.execSQL("UPDATE settings SET `value`='" + doAutoCheck + "' WHERE `key`='autocheck' ;");

        Toast.makeText(SettingsActivity.this, "Auto-provera status promenjen", Toast.LENGTH_SHORT).show();
    }


}
