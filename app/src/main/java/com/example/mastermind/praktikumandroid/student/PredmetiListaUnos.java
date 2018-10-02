package com.example.mastermind.praktikumandroid.student;

import java.util.Date;

/**
 * Created by Acer on 20-Nov-17.
 */

public class PredmetiListaUnos extends Unos_StudServ {


    public String  naziv;
    public String  godinaStudija;
    public int     espb;
    public String  statusPredmeta;
    public String  stanje;
    public Date    datumPolaganja;
    public String  skolskaGodina;
    public String     ocena;


    public PredmetiListaUnos( String naziv, String godinaStudija, int espb, String statusPredmeta, String stanje, Date datumPolaganja, String skolskaGodina, String ocena)
    {

        this.naziv          = naziv;
        this.godinaStudija  = godinaStudija;
        this.espb           = espb;
        this.statusPredmeta = statusPredmeta;
        this.stanje         = stanje;
        this.datumPolaganja = datumPolaganja;
        this.skolskaGodina  = skolskaGodina;
        this.ocena          = ocena;

    }
}
