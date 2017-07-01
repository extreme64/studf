package com.example.mastermind.praktikumandroid.conn;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.mastermind.praktikumandroid.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by Acer on 14-Dec-16.
 */
public class GetConnSSL extends AsyncTask<String, Void, Void> {

    private final Context context;

    private ProgressDialog progress;
    private String coocStr;
    private String klasican_log_url = "http://www.ict.edu.rs/front";
    private String sservis_login_url = "http://studentskiservis.ict.edu.rs/pocetna";


    public GetConnSSL(Context c) {
        this.context = c;
    }

    protected void onPreExecute() {
        progress = new ProgressDialog(this.context);
        progress.setMessage("Loading");
        progress.show();
    }

    @Override
    protected Void doInBackground(String... params) {
        try {


            //final TextView outputView = (TextView) findViewById(R.id.showOutput);


            CertificateValidation cerValid = new CertificateValidation(context.getAssets(), "posta_ssl_skola_b64.crt");

            //URL url = new URL("http://www.ict.edu.rs/studiranje/rezultati_new");
            URL url = new URL("https://studentskiservis.ict.edu.rs/student/finansije"); // MORA biti HTTPS link koji trazimo da dohvatimo


            HttpsURLConnection connection =
                    (HttpsURLConnection) url.openConnection();
            SSLContext SSLContext = cerValid.getSSLcontext();
            connection.setSSLSocketFactory(SSLContext.getSocketFactory());

            String urlParameters = "";
            connection.setRequestMethod("GET");
            connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

            String coocsAdd = "_ga=GA1.3.129672168.1452025326; SESSdcb6275812148e15b218bd8c67cffde0=k6amgelupjcj4ebv2fvgtihku6; ASP.NET_SessionId=a3koh0qcmccyu5b5slowxka5; SESS248bbd5b85db2c5440b02a19409197a2=s86q9stf9ceocjh6hohtmj23c1; __utma=60889780.129672168.1452025326.1468406872.1468500221.54; __utmb=60889780.2.10.1468500221; __utmc=60889780; __utmz=60889780.1459016320.15.7.utmcsr=webdizajn.ict.edu.rs|utmccn=(referral)|utmcmd=referral|utmcct=/;";


            //System.out.println("\ncoocsAdd + coocStr : " + coocsAdd + coocStr);
            connection.setRequestProperty("Cookie", coocsAdd + coocStr);

            int responseCode = connection.getResponseCode();

            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParameters);
            //System.out.println("Response Code : " + responseCode);

            final StringBuilder output = new StringBuilder("Request URL " + url);
            output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
            output.append(System.getProperty("line.separator") + "Response Code " + responseCode);
            output.append(System.getProperty("line.separator") + "Type " + "GET");

            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while ((line = br.readLine()) != null) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

            }


            ((MainActivity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    System.out.println("OUT get >>>>> " + output);
                    progress.dismiss();

                }
            });


        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
