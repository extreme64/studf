package com.example.mastermind.praktikumandroid.student;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.mastermind.praktikumandroid.MainActivity;
import com.example.mastermind.praktikumandroid.MainFragment;
import com.example.mastermind.praktikumandroid.appInterfaces.VarOnChangeListener;
import com.example.mastermind.praktikumandroid.conn.PostConnSSL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.mastermind.praktikumandroid.MainFragment.ISPITI_ITEMS_PAGES_STUDS;
import static com.example.mastermind.praktikumandroid.MainFragment.PREDMETI_ITEMS_PAGES_STUDS;
import static com.example.mastermind.praktikumandroid.MainFragment.ZADUZENJA_ITEMS_PAGES_STUDS;


/**
 * Created by Mastermind on 05-Feb-18.
 */

public class GetListPages extends PostConnSSL implements VarOnChangeListener {



    public GetListPages(Context c, MainFragment f, int p, boolean use_filters, String[] filters_values, String whatList) {
        super(c, f, p, use_filters, filters_values, whatList);
    }



    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        onVarEvent(new String[]{ this.whatList, result});
    }



    @Override
    public void onVarEvent(Object[] objChanged) {

        int pagesNumber = 1;

        String html = (String) objChanged[1];


        mf.animateSpinnerDisabledStatus(false);

        // parse raw html
        try {
            Document doc = Jsoup.parse(html);
            Elements content = doc.getElementsByClass("bts-grid-pagination");
            Element pagesEle = content.first(); // there is just one
            Elements tags = pagesEle.getElementsByTag("a");

            if(tags.size() > 0)
            {
                pagesNumber = tags.size() + 1;
            }

            //TODO: BUG account for faulty website pages display, assume more or less pages

        }
        catch(NullPointerException npe){}

        switch (this.whatList)
        {
            case ObradaLista_StudServ.LISTA_PREDMETI :

                /** note, how much pages we have to read */
                for(int i = 0; i < pagesNumber; i++)   { MainFragment.readingProcessChackIn.add(this.whatList); }

                MainFragment.setBROJ_STRANICA_PREDMETI(pagesNumber);
                MainFragment.PREDMETI_ITEMS_PAGES_STUDS = new HashMap(pagesNumber);

                GetListsHtml_StudServ((this.use_filters) ? "1" : "2", MainFragment.URL_PREDMETI_LIST_STUDSER, pagesNumber, html);
                break;

            case ObradaLista_StudServ.LISTA_ISPITI :
                for(int i = 0; i < pagesNumber; i++)   { MainFragment.readingProcessChackIn.add(this.whatList); }
                MainFragment.setBROJ_STRANICA_ISPITI(pagesNumber);
                MainFragment.ISPITI_ITEMS_PAGES_STUDS = new HashMap(pagesNumber);
                GetListsHtml_StudServ((this.use_filters) ? "1" : "2", MainFragment.URL_ISPITI_LIST_STUDSER, pagesNumber, html);
                break;

            case ObradaLista_StudServ.LISTA_ZADUZENJA :
                for(int i = 0; i < pagesNumber; i++)   { MainFragment.readingProcessChackIn.add(this.whatList); }
                MainFragment.setBROJ_STRANICA_ZADUZENJA(pagesNumber);
                MainFragment.ZADUZENJA_ITEMS_PAGES_STUDS = new HashMap(pagesNumber);
                GetListsHtml_StudServ((this.use_filters) ? "1" : "2", MainFragment.URL_ZADUZENJA_LIST_STUDSER, pagesNumber, html);
                break;
        }

    }

    public  void GetListsHtml_StudServ(String usedFor, String url, int pagesNum, String html){

        /** fill first page, to use this 'page number' call, to a max use (less pages to parse, bigger the gain) */
        ObradaLista_StudServ predmeti_obrada_liste;
        ObradaLista_StudServ ispiti_obrada_liste;
        ObradaLista_StudServ zaduzenja_obrada_liste;
        ArrayList al_tmp = new ArrayList();



        switch (Integer.parseInt(this.whatList)+3) {
            case MainActivity.TAB_5_1:
                predmeti_obrada_liste = new ObradaLista_StudServ();

                PREDMETI_ITEMS_PAGES_STUDS.put(1, predmeti_obrada_liste.ObradiListuPredmeti("" + html, "MainContentPlaceHolder_GridViewLista"));

                al_tmp = (ArrayList) PREDMETI_ITEMS_PAGES_STUDS.get(page);
                if (al_tmp != null && !al_tmp.isEmpty()) {
                    try {
                        MainFragment.PREDMETI_ITEMS_STUDS.clear();
                        ArrayList tmp_al = mf.ListAdapterSourceSorter(1, PREDMETI_ITEMS_PAGES_STUDS);
                        MainFragment.PREDMETI_ITEMS_STUDS.addAll(tmp_al); // OVDE
                    } catch (NullPointerException npe) {
                    }

                }
                break;
            case MainActivity.TAB_5_2:
                ispiti_obrada_liste = new ObradaLista_StudServ();

                ISPITI_ITEMS_PAGES_STUDS.put(1, ispiti_obrada_liste.ObradiListuIspiti("" + html, "MainContentPlaceHolder_GridViewLista"));

                al_tmp = (ArrayList) ISPITI_ITEMS_PAGES_STUDS.get(page);
                if (al_tmp != null && !al_tmp.isEmpty()) {
                    try {
                        MainFragment.ISPITI_ITEMS_STUDS.clear();
                        ArrayList tmp_al = mf.ListAdapterSourceSorter(1, ISPITI_ITEMS_PAGES_STUDS);
                        MainFragment.ISPITI_ITEMS_STUDS.addAll(tmp_al); // OVDE
                    } catch (NullPointerException npe) {
                    }
                }
                break;
            case MainActivity.TAB_5_3:
                zaduzenja_obrada_liste = new ObradaLista_StudServ();

                ZADUZENJA_ITEMS_PAGES_STUDS.put(1, zaduzenja_obrada_liste.ObradiListuZaduzenja("" + html, "MainContentPlaceHolder_GridViewLista"));

                al_tmp = (ArrayList) ZADUZENJA_ITEMS_PAGES_STUDS.get(page);
                if (al_tmp != null && !al_tmp.isEmpty()) {
                    try {
                        MainFragment.ZADUZENJA_ITEMS_STUDS.clear();
                        ArrayList tmp_al = mf.ListAdapterSourceSorter(1, ZADUZENJA_ITEMS_PAGES_STUDS);
                        MainFragment.ZADUZENJA_ITEMS_STUDS.addAll(tmp_al); // OVDE
                    } catch (NullPointerException npe) {
                    }
                }
                break;
        }


        /** nothing to show, clear adap. list and update UI */
        if(al_tmp.size() == 0 && mf != null )
        {
            final FragmentActivity fa = mf.getActivity();
            if(fa != null)
            {
                try {
                    fa.runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    int tab = MainActivity.CURRENT_TAB_SELECTED;
                                    switch (tab) {
                                        case MainActivity.TAB_5_1:
                                            MainFragment.PREDMETI_ITEMS_STUDS.clear();
                                            MainFragment.predmeti_sAdapter.notifyDataSetChanged();
                                            break;
                                        case MainActivity.TAB_5_2:
                                            MainFragment.ISPITI_ITEMS_STUDS.clear();
                                            MainFragment.ispiti_sAdapter.notifyDataSetChanged();
                                            break;
                                        case MainActivity.TAB_5_3:
                                            MainFragment.ZADUZENJA_ITEMS_STUDS.clear();
                                            MainFragment.zaduzenja_sAdapter.notifyDataSetChanged();
                                            break;
                                    }
                                    Toast t= Toast.makeText(fa, "Prazna lista I", Toast.LENGTH_SHORT );
                                    t.show();
                                }
                            }
                    );
                }
                catch (NullPointerException npe)
                {}
            }
        }


        /** read [ ,n...] number of pages */
        if(pagesNum > 1) {
            for (int pn = 2; pn <= pagesNum; pn++) //pn = 2 , starting from 2. we did 1. in
            {
                if(use_filters)
                    new PostConnSSL(context, mf, pn, use_filters, filter_values, this.whatList).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, url, usedFor);
                else
                    new PostConnSSL(context, mf, pn, use_filters).executeOnExecutor( AsyncTask.THREAD_POOL_EXECUTOR, url, usedFor);
            }
        }
    }



}
