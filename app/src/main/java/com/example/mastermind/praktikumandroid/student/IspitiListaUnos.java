package com.example.mastermind.praktikumandroid.student;

import java.util.Date;

/**
 * Created by Acer on 20-Nov-17.
 */

public class IspitiListaUnos extends Unos_StudServ {


    public String  naziv;
    public Date    datumIspita;
    public String  brojPrijavaBrojPolaganja;
    public String  godinaStudija;
    public String     ocena;


    public IspitiListaUnos( String naziv, Date datumIspita,
             String brojPrijavaBrojPolaganja, String godinaStudija, String ocena)
    {
        this.naziv                      = naziv;
        this.datumIspita                = datumIspita;
        this.brojPrijavaBrojPolaganja   = brojPrijavaBrojPolaganja;
        this.godinaStudija              = godinaStudija;
        this.ocena                      = ocena;

    }
}
