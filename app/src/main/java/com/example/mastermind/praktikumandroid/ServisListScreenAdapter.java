package com.example.mastermind.praktikumandroid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mastermind.praktikumandroid.student.IspitiListaUnos;
import com.example.mastermind.praktikumandroid.student.PredmetiListaUnos;
import com.example.mastermind.praktikumandroid.student.Unos_StudServ;
import com.example.mastermind.praktikumandroid.student.ZaduzenjaListaUnos;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Acer on 29-Sep-17.
 */

public class ServisListScreenAdapter extends ArrayAdapter<Unos_StudServ> {


    public List<Unos_StudServ> feedEntryListITEMS = new ArrayList<Unos_StudServ>();
    Activity context;
    LayoutInflater inflater ;
    private int tab;
    private int inflater_layout;


    public ServisListScreenAdapter(Context context, List<Unos_StudServ> objects) {
        super(context, 0, objects);
        this.context = (Activity)context;
        this.feedEntryListITEMS = objects;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        MainFragment.LISTS_UI_DATA_DONE = true;
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


        PredmetiListaUnos spitem = null;
        IspitiListaUnos siitem = null;
        ZaduzenjaListaUnos szitem = null;

        View view = convertView; // usteda
        TextView tv_ref_holder1, tv_ref_holder2, tv_ref_holder3, tv_ref_holder4;

        if (view == null)
        {
                view = inflater.inflate(this.inflater_layout, parent, false);
        }


        ImageView ico = (ImageView)view.findViewById(R.id.ivIcoS);
        ico.setImageResource(R.drawable.ic_action );

        tv_ref_holder1 = (TextView) view.findViewById(R.id.tvNaziv);
        tv_ref_holder1.setTextSize(15);
        tv_ref_holder2 = (TextView) view.findViewById(R.id.tvInto1);
        tv_ref_holder2.setTextSize(15);
        tv_ref_holder3 = (TextView) view.findViewById(R.id.tvInfo2);
        tv_ref_holder3.setTextSize(15);
        tv_ref_holder4 = (TextView) view.findViewById(R.id.tvInfo3);
        tv_ref_holder4.setTextSize(15);

        if (tab == 4)
        {
            spitem = (PredmetiListaUnos) feedEntryListITEMS.get(position);
            tv_ref_holder1.setText(spitem.naziv);
            tv_ref_holder2.setText(" " + spitem.godinaStudija);
            tv_ref_holder3.setText(" / " + spitem.espb);
            tv_ref_holder4.setText(" / " + spitem.stanje);
        }
        else if(tab == 5)
        {
            siitem = (IspitiListaUnos) feedEntryListITEMS.get(position);
            tv_ref_holder1.setText(siitem.naziv);
            tv_ref_holder2.setText(" " + siitem.brojPrijavaBrojPolaganja);
            tv_ref_holder3.setText(" | " + siitem.godinaStudija);
            tv_ref_holder4.setText(" | " + siitem.ocena);
        }
        else if(tab == 6)
        {
            szitem = (ZaduzenjaListaUnos) feedEntryListITEMS.get(position);
            tv_ref_holder1.setText(szitem.iznos);
            tv_ref_holder2.setText(" " + szitem.osnov);
            tv_ref_holder3.setText(" - " + szitem.tip);
        }
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






