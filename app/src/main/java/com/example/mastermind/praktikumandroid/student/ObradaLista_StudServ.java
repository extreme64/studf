package com.example.mastermind.praktikumandroid.student;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Acer on 25-Nov-17.
 */

public class ObradaLista_StudServ {

    public static final String LISTA_PREDMETI   =   "1";
    public static final String LISTA_ISPITI     =   "2";
    public static final String LISTA_ZADUZENJA  =   "3";

    public ObradaLista_StudServ(){}






    public List ObradiListuPredmeti(String pageHtml, String contentTag)
    {
        // kolekcija objekata za popunu liste
        ArrayList listaPredmeti = new ArrayList();

        // parsujemo raw html u dokument
        // dohvatamo element: "MainContentPlaceHolder_GridViewLista"
        // i prolazimo kroz "tr" tagove
        try {
            Document doc = Jsoup.parse(pageHtml);
            Element content = doc.getElementById(contentTag);
            Elements tags = content.getElementsByTag("tr");
            for (Element tag : tags) {

                // FIX za prevenciju prolazenja kroz lazni JUNK tag
                // na kraju liste i tag koji je element stranicenja
                if (tag.child(0).text() != "" && tag.child(0).attr("class") != "bts-grid-pagination") {

                    Date d = new Date();
                    try {
                        if(tag.child(5).text() !="") {

                            SimpleDateFormat sdf;
                            try {
                                sdf = new SimpleDateFormat("dd.MM.yyyy."); //sdf.applyPattern("dd MMM yyyy hh:mm");
                                d = sdf.parse(tag.child(5).text());  //tag.child(5).text()
                            } catch (Exception ex) {
                            }
                        }
                        if ((tag.child(2).text()).matches("\\d+(?:\\.\\d+)?")) {
                            // ako je tag validan dodati njegovu reprezentaciju u listu
                            listaPredmeti.add(
                                    new PredmetiListaUnos(
                                            tag.child(0).text(),
                                            tag.child(1).text(), Integer.parseInt(tag.child(2).text()),
                                            tag.child(3).text(), tag.child(4).text(), d,
                                            tag.child(6).text(), tag.child(7).text()));
                        }
                    } catch (IndexOutOfBoundsException e) { /* tag.child(2)*/ }
                }
            }
        }
        catch(NullPointerException npe){}

        return listaPredmeti;
    }

    public List ObradiListuIspiti(String pageHtml, String contentTag)
    {

        ArrayList listaIspiti = new ArrayList();

        // parse raw html
        try {
            Document doc = Jsoup.parse(pageHtml);
            Element content = doc.getElementById(contentTag);
            Elements tags = content.getElementsByTag("tr");
            for (Element tag : tags) {

                Date d = null;
                SimpleDateFormat sdf;
                try {
                    sdf = new SimpleDateFormat("dd.MM.yyyy."); //sdf.applyPattern("dd MMM yyyy hh:mm");
                    d = sdf.parse(tag.child(1).text());  //tag.child(5).text()
                } catch (Exception ex) {
                }

                // FIX faux JUNK tag
                if (d != null && tag.child(0).text() != "" && tag.child(0).attr("class") != "bts-grid-pagination" && tag.child(0).tagName() != "th") {
                    try {
                        if ((tag.child(4).text()).matches("\\d+(?:\\.\\d+)?")) {

                            listaIspiti.add(
                                    new IspitiListaUnos(tag.child(0).text(), d, tag.child(2).text(),
                                            tag.child(3).text(), tag.child(4).text())
                            );
                        }
                    } catch (IndexOutOfBoundsException e) { }
                }
            }
        }
        catch (NullPointerException npe){}
        return listaIspiti;
    }

    public List ObradiListuZaduzenja(String pageHtml, String contentTag)
    {

        ArrayList listaZaduzenja = new ArrayList();

        // parse raw html
        try {
            Document doc = Jsoup.parse(pageHtml);
            Element content = doc.getElementById(contentTag);
            Elements tags = content.getElementsByTag("tr");
            for (Element tag : tags) {

                Date d = null;
                SimpleDateFormat sdf;
                try {
                    sdf = new SimpleDateFormat("dd.MM.yyyy"); //sdf.applyPattern("dd MMM yyyy hh:mm");
                    d = sdf.parse(tag.child(0).text());  //tag.child(5).text()
                } catch (Exception ex) {
                }

                // FIX faux JUNK tag
                if (d != null && tag.child(0).text() != "" && tag.child(0).text() != " " && tag.child(0).attr("class") != "bts-grid-pagination" && tag.child(0).tagName() != "th") {
                    try {

                        listaZaduzenja.add(
                                new ZaduzenjaListaUnos(d, tag.child(1).text(),
                                        tag.child(2).text(), tag.child(3).text(), tag.child(4).text())
                        );

                    } catch (IndexOutOfBoundsException e) {  }
                }
            }
        }
        catch (NullPointerException npe){}
        return listaZaduzenja;
    }

}
