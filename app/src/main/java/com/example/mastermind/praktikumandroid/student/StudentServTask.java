package com.example.mastermind.praktikumandroid.student;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mastermind.praktikumandroid.MainActivity;
import com.example.mastermind.praktikumandroid.MainFragment;
import com.example.mastermind.praktikumandroid.MainItemDetailFragment;

import java.util.ArrayList;

import static com.example.mastermind.praktikumandroid.MainFragment.ISPITI_ITEMS_PAGES_STUDS;
import static com.example.mastermind.praktikumandroid.MainFragment.PREDMETI_ITEMS_PAGES_STUDS;
import static com.example.mastermind.praktikumandroid.MainFragment.PREDMETI_ITEMS_STUDS;
import static com.example.mastermind.praktikumandroid.MainFragment.ZADUZENJA_ITEMS_PAGES_STUDS;
import static com.example.mastermind.praktikumandroid.MainFragment.predmeti_sAdapter;
import static java.util.Collections.reverse;


/**
 * Created by Mastermind on 23-Jan-18.
 */

public class StudentServTask extends AsyncTask<String, Void, String> {



    Context context;
    public String response;
    private int page;

    ObradaLista_StudServ predmeti_obrada_liste;
    ObradaLista_StudServ ispiti_obrada_lista;
    ObradaLista_StudServ zaduzenja_obrada_lista;

    private MainFragment mf;


    public StudentServTask(Context c, MainFragment originFragment, String response, int page){
        this.context = c;
        this.mf = originFragment;
        this.response = response;
        this.page = page;
    }


    protected void onPreExecute(){}

    @Override
    protected String doInBackground(String... strings) {

        String servisId = strings[0];
        predmeti_obrada_liste = new ObradaLista_StudServ();
        ispiti_obrada_lista = new ObradaLista_StudServ();
        zaduzenja_obrada_lista = new ObradaLista_StudServ();

        if(servisId=="1" )
        {

            if(PREDMETI_ITEMS_PAGES_STUDS != null)
            {
                PREDMETI_ITEMS_PAGES_STUDS.put(page, predmeti_obrada_liste.ObradiListuPredmeti("" + response, "MainContentPlaceHolder_GridViewLista"));
                ArrayList al_tmp = (ArrayList)PREDMETI_ITEMS_PAGES_STUDS.get(page);
                if(al_tmp != null && !al_tmp.isEmpty())
                {
                    try {
                        reverse((ArrayList) PREDMETI_ITEMS_PAGES_STUDS.get(page));

                        MainFragment.PREDMETI_ITEMS_STUDS.clear();
                        ArrayList tmp_al = mf.ListAdapterSourceSorter(1, PREDMETI_ITEMS_PAGES_STUDS);
                        MainFragment.PREDMETI_ITEMS_STUDS.addAll(tmp_al);
                    }
                    catch (NullPointerException npe)
                    {}

                }
            }
        }
        else if(servisId=="2")
        {

            if(ISPITI_ITEMS_PAGES_STUDS != null)
            {
                ISPITI_ITEMS_PAGES_STUDS.put(page, ispiti_obrada_lista.ObradiListuIspiti("" + response, "MainContentPlaceHolder_GridViewLista"));
                ArrayList al_tmp = (ArrayList)ISPITI_ITEMS_PAGES_STUDS.get(page);
                if(al_tmp != null && !al_tmp.isEmpty())
                {
                    try {
                        reverse((ArrayList) ISPITI_ITEMS_PAGES_STUDS.get(page));

                        MainFragment.ISPITI_ITEMS_STUDS.clear();
                        ArrayList tmp_al = mf.ListAdapterSourceSorter(1, ISPITI_ITEMS_PAGES_STUDS);
                        MainFragment.ISPITI_ITEMS_STUDS.addAll(tmp_al);
                    }
                    catch (NullPointerException npe)
                    {}

                }
            }
        }
        else if(servisId=="3")
        {

            if(ZADUZENJA_ITEMS_PAGES_STUDS != null)
            {
                ZADUZENJA_ITEMS_PAGES_STUDS.put(page, zaduzenja_obrada_lista.ObradiListuZaduzenja("" + response, "MainContentPlaceHolder_GridViewLista"));
                ArrayList al_tmp = (ArrayList)ZADUZENJA_ITEMS_PAGES_STUDS.get(page);
                if(al_tmp != null && !al_tmp.isEmpty())
                {
                    try {
                        reverse((ArrayList) ZADUZENJA_ITEMS_PAGES_STUDS.get(page));

                        MainFragment.ZADUZENJA_ITEMS_STUDS.clear();
                        ArrayList tmp_al = mf.ListAdapterSourceSorter(1, ZADUZENJA_ITEMS_PAGES_STUDS);
                        MainFragment.ZADUZENJA_ITEMS_STUDS.addAll(tmp_al);
                    }
                    catch (NullPointerException npe)
                    {}

                }
            }
        }

        return servisId;
    }


    protected void onPostExecute(String obj)
    {

        final String servisId = obj;
        if(mf != null)
        {
            final MainActivity fa = (MainActivity) mf.getActivity();
            if(fa != null)
            {
                try {
                    fa.runOnUiThread(
                         new Runnable() {
                             @Override
                             public void run() {
                                 String tab = servisId;
                                 if(tab == "1") {
                                     if (predmeti_sAdapter != null) {

                                         /** za svaku stranu ocitanu izbrisi jednu iz liste chekiranja */
                                         MainFragment.readingProcessChackIn.remove(ObradaLista_StudServ.LISTA_PREDMETI);

                                         if (!MainFragment.readingProcessChackIn.contains(ObradaLista_StudServ.LISTA_PREDMETI)) {
                                             mf.animateSpinnerDisabledStatus(true);
                                         }
                                         predmeti_sAdapter.notifyDataSetChanged();



                                         /** average score */
                                         int brojPredmeta = 0;
                                         int zbir = 0;
                                         float prosek = 0;
                                         for (Object o : PREDMETI_ITEMS_STUDS) {
                                             PredmetiListaUnos pedObj = (PredmetiListaUnos)o;
                                             int ocena = Integer.parseInt(pedObj.ocena);
                                             if(ocena>=6)
                                             {
                                                brojPredmeta++;
                                                zbir += ocena;
                                             }
                                         }
                                         MainFragment.PROSEK = zbir/brojPredmeta;

                                         if(MainFragment.tw_prosecnaOcena != null)
                                            MainFragment.tw_prosecnaOcena.setText(MainFragment.PROSEK+"");

                                         if(MainItemDetailFragment.tw_prosecnaOcenaDetalji != null)
                                            MainItemDetailFragment.tw_prosecnaOcenaDetalji.setText(MainFragment.PROSEK+"");
                                     }
                                 }

                                 if(tab == "2") {
                                     if (MainFragment.ispiti_sAdapter != null) {


                                         MainFragment.readingProcessChackIn.remove(ObradaLista_StudServ.LISTA_ISPITI);

                                         if (!MainFragment.readingProcessChackIn.contains(ObradaLista_StudServ.LISTA_ISPITI)) {
                                             mf.animateSpinnerDisabledStatus(true);
                                         }
                                         MainFragment.ispiti_sAdapter.notifyDataSetChanged();
                                     }
                                 }


                                 if(tab == "3") {
                                     if (MainFragment.zaduzenja_sAdapter != null) {


                                         MainFragment.readingProcessChackIn.remove(ObradaLista_StudServ.LISTA_ZADUZENJA);

                                         if (!MainFragment.readingProcessChackIn.contains(ObradaLista_StudServ.LISTA_ZADUZENJA)) {
                                             mf.animateSpinnerDisabledStatus(true);
                                         }
                                         MainFragment.zaduzenja_sAdapter.notifyDataSetChanged();
                                     }
                                 }

                             }
                         }
                    );
                }
                catch (NullPointerException npe)
                {
                    Log.e("npEx", npe.toString());
                }
                catch (Exception e)
                {
                    Log.e("EX", e.toString());
                }
            }

        }

    }





}
