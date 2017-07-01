 package com.example.mastermind.praktikumandroid.conn;


/*

    private ProgressDialog progress;
    private String coocStr;
    private String klasican_log_url = "http://www.ict.edu.rs/front";
    private String sservis_login_url = "http://studentskiservis.ict.edu.rs/pocetna";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendPostRequest(View View) {
        new PostClass(this).execute();
    }

    public void sendGetRequest(View View) {
        new GetClass(this).execute();
    }





    private class PostClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public PostClass(Context c){

            this.context = c;
//            this.error = status;
//            this.type = t;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {

                final TextView outputView = (TextView) findViewById(R.id.showOutput);

                /*                String [] list;
                try {
                    list = getAssets().list("");
                    for (String file : list) {  System.out.println("\nFile in f : " + file);  }

                } catch (IOException e) {  e.printStackTrace();  }*/

/***************

                CertificateValidation cerValid = new CertificateValidation(getAssets(), "posta_ssl_skola_b64.crt");

                //this.context = cerValid.context;

                URL url = new URL("https://studentskiservis.ict.edu.rs/pocetna"); //https://studentskiservis.ict.edu.rs/pocetna

                String urlParameters = "__EVENTTARGET=&__EVENTARGUMENT=&__VIEWSTATE=%2FwEPDwUKMTg1MzM1NTI5Mw9kFgJmD2QWAgIDDxYCHgVjbGFzcwUadmlzb2thaWN0IHBvY2V0bmEgbG9nZWRvdXQWAgIBD2QWBgICDxYCHgRUZXh0BSzQodGC0YPQtNC10L3RgiBAINCS0LjRgdC%2B0LrQsCBJQ1Qg0YjQutC%2B0LvQsGQCCA9kFgICAQ9kFgJmD2QWAgIDD2QWAmYPZBYCAgkPEA8WAh4HQ2hlY2tlZGhkZGRkAgkPFgIeCWlubmVyaHRtbAWrGA0KICAgICAgDQoNCg0KDQoNCg0KDQoNCg0KDQoNCg0KPGRpdiBjbGFzcz0ibW9kYWwgZmFkZSBpbiIgaWQ9Im1vZGFsSGVscCIgdGFiaW5kZXg9Ii0xIiByb2xlPSJkaWFsb2ciIGFyaWEtbGFiZWxsZWRieT0ibXlNb2RhbExhYmVsIiBhcmlhLWhpZGRlbj0idHJ1ZSIgc3R5bGU9IiAiPg0KICAgIDxkaXYgY2xhc3M9Im1vZGFsLWRpYWxvZyAiPg0KICAgICAgICA8ZGl2IGNsYXNzPSJtb2RhbC1jb250ZW50Ij4NCiAgICAgICAgICAgIDxkaXYgY2xhc3M9Im1vZGFsLWhlYWRlciI%2BDQogICAgICAgICAgICAgICAgPGJ1dHRvbiB0eXBlPSJidXR0b24iIGNsYXNzPSJjbG9zZSIgZGF0YS1kaXNtaXNzPSJtb2RhbCI%2BDQogICAgICAgICAgICAgICAgICAgIDxzcGFuIGFyaWEtaGlkZGVuPSJ0cnVlIj4mdGltZXM7PC9zcGFuPg0KICAgICAgICAgICAgICAgICAgICA8c3BhbiBjbGFzcz0ic3Itb25seSI%2B0JfQsNGC0LLQvtGA0Lg8L3NwYW4%2BDQogICAgICAgICAgICAgICAgPC9idXR0b24%2BDQogICAgICAgICAgICAgICAgPGg0IGNsYXNzPSJtb2RhbC10aXRsZSI%2B0KPQv9GD0YLRgdGC0LLQviDQt9CwINC%2F0YDQuNGB0YLRg9C%2FPC9oND4NCiAgICAgICAgICAgIDwvZGl2Pg0KICAgICAgICAgICAgPGRpdiBjbGFzcz0ibW9kYWwtYm9keSI%2BDQogICAgICAgICAgICAgICAgPHA%2BPHA%2B0J%2FQvtGI0YLQvtCy0LDQvdC4L9C90LAsPC9wPjxwPtCU0L7QsdGA0L7QtNC%2B0YjQu9C4INC90LAg0YHRgtGD0LTQtdC90YLRgdC60Lgg0YHQtdGA0LLQuNGBINCS0LjRgdC%2B0LrQtSBJQ1Qg0YjQutC%2B0LvQtS4g0KHQtdGA0LLQuNGBINC%2B0LzQvtCz0YPRm9Cw0LLQsCDRgdGC0YPQtNC10L3RgtC40LzQsCDQtNCwINGI0LrQvtC70YHQutC1INC%2B0LHQsNCy0LXQt9C1KNC%2F0YDQuNGY0LDQstGDINC40YHQv9C40YLQsCwg0L%2FRgNC40ZjQsNCy0YMg0L%2FRgNC10LTQvNC10YLQsCwg0L7QtNCw0LHQuNGAINC80L7QtNGD0LvQsCwg0L%2FRgNC10LPQu9C10LQg0LfQsNC00YPQttC10ZrQsCkg0L7QsdCw0LLQtSDQv9GD0YLQtdC8INC40L3RgtC10YDQvdC10YLQsC4g0JTQsCDQsdC4INGB0YLQtSDQv9GA0LjRgdGC0YPQv9C40LvQuCDRgdC10YDQstC40YHRgyDQvNC%2B0LbQtdGC0LUg0LrQvtGA0LjRgdGC0LjRgtC4INCx0LjQu9C%2BINC60L7RmNC4IFdlYiBicm93c2VyIChJbnRlcm5ldCBFeHBsb3JlciwgR29vZ2xlIENocm9tZSwgTW96aWxsYSBGaXJlZm94LCBTYWZhcmkg0Lgg0LTRgNGD0LPQtSkg0YLQsNC60L4g0YjRgtC%2BINGb0LXRgtC1INGB0LUg0YPQu9C%2B0LPQvtCy0LDRgtC4INC%2F0L7QvNC%2B0ZvRgyDQktCw0YjQtdCzINC60L7RgNC40YHQvdC40YfQutC%2B0LMg0LjQvNC10L3QsCDQuCDQu9C%2B0LfQuNC90LrQtS4g0JrQvtGA0LjRgdC90LjRh9C60L4g0LjQvNC1INC4INC70L7Qt9C40L3QutCwINGB0YMg0LjRgdGC0Lgg0LrQsNC%2BINC30LAg0LvQvtCz0L7QstCw0ZrQtSDQvdCwINGB0LDRmNGCINGI0LrQvtC70LUg0Lgg0YPQsdGD0LTRg9Gb0LUg0ZvQtdGC0LUg0L7QstC1INC%2F0L7QtNCw0YLQutC1INC60L7RgNC40YHRgtC40YLQuCDQt9CwINC%2F0YDQuNGB0YLRg9C%2FINGB0LDRmNGC0YMg0Lgg0L7QstC%2B0Lwg0YHQtdGA0LLQuNGB0YMuPC9wPiA8cD7QoyDRgdC70YPRh9Cw0ZjRgyDQtNCwINC40LzQsNGC0LUg0L%2FRgNC%2B0LHQu9C10LzQsCDRgdCwINC%2F0YDQuNGB0YLRg9C%2F0L7QvCDRgdC10YDQstC40YHRgywg0LzQvtGA0LDRgtC1INGB0LUg0L7QsdGA0LDRgtC40YLQuCDQsNC00LzQuNC90LjRgdGC0YDQsNGC0L7RgNC40LzQsCDRiNC60L7Qu9C1INC70LjRh9C90L4g0LjQu9C4INC%2F0YPRgtC10Lwg0YjQutC%2B0LvRgdC60LUg0LUtbWFpbCDQsNC00YDQtdGB0LUuPC9wPjxwPtCjINC%2F0L7RmdC1INC60L7RgNC40YHQvdC40YfQutC%2BINC40LzQtSDRg9C90LXRgdC40YLQtSDQuNGB0LrRmdGD0YfQuNCy0L4g0JLQsNGI0YMg0LUt0LzQsNC40Lsg0LDQtNGA0LXRgdGDLtCjINC%2F0L7RmdC1INC70L7Qt9C40L3QutCwINGD0L3QtdGB0LjRgtC1INC70L7Qt9C40L3QutGDINC60L7RmNGDINGB0YLQtSDQtNC%2B0LHQuNC70Lgg0LjQu9C4INGY0LUg0LLQtdGbINC%2F0L7RgdC10LTRg9GY0LXRgtC1INC30LAg0YHQsNGY0YIg0YjQutC%2B0LvQtS48L3A%2BPHA%2BINCb0L7Qt9C40L3QutGDINC60L7RmNGDINGB0YLQtSDQtNC%2B0LHQuNC70Lgg0Lgg0KjQutC%2B0LvQuCwg0L%2FRgNC%2B0LzQtdC90LjRgtC1INC%2F0L7RgdC70LUg0L%2FRgNCy0L7QsyDQu9C%2B0LPQvtCy0LDRmtCwLjwvcD48cD7Qn9C%2B0LLRgNCw0YLQvdC1INC40L3RhNC%2B0YDQvNCw0YbQuNGY0LUg0L4g0LjQt9Cy0YDRiNC10L3QuNC8INCw0LrRgtC40LLQvdC%2B0YHRgtC40LzQsCDQv9GA0LXQutC%2BINGB0YLRg9C00LXQvdGC0YHQutC%2B0LMg0YHQtdGA0LLQuNGB0LAsICjQvdC%2F0YAuINC%2F0YDQuNGY0LDQstCwINC%2F0YDQtdC00LzQtdGC0LApINC00L7QsdC40ZjQsNGb0LXRgtC1INC90LAg0YjQutC%2B0LvRgdC60YMg0LUtbWFpbCDQsNC00YDQtdGB0YMu0KHQutGA0LXRm9C10LzQviDQv9Cw0LbRmtGDINGB0YLRg9C00LXQvdGC0LjQvNCwINC00LAg0YHQstC%2B0ZjQtSDQv9C%2B0LTQsNGC0LrQtSDQvdC1INC00LDRmNGDINGC0YDQtdGb0LjQvCDQu9C40YbQuNC80LAg0ZjQtdGAINCo0LrQvtC70LAg0L3QtdGb0LUg0YHQvdC%2B0YHQuNGC0Lgg0L3QuNC60LDQutCy0YMg0L7QtNCz0L7QstGA0L3QvtGB0YIg0YMg0YHQu9GD0YfQsNGY0YMg0LfQu9C%2B0YPQv9C%2B0YLRgNC10LHQtSDRgdGC0YPQtNC10L3RgtGB0LrQvtCzINC90LDQu9C%2B0LPQsC48L3A%2BPHA%2B0JLQuNGB0L7QutCwIElDVCDRiNC60L7Qu9CwPC9wPjwvcD4NCiAgICAgICAgICAgIDwvZGl2Pg0KICAgICAgICAgICAgPGRpdiBjbGFzcz0ibW9kYWwtZm9vdGVyIj4NCiAgICAgICAgICAgICAgICA8YnV0dG9uIHR5cGU9ImJ1dHRvbiIgY2xhc3M9ImJ0biBidG4tZGVmYXVsdCIgZGF0YS1kaXNtaXNzPSJtb2RhbCI%2B0JfQsNGC0LLQvtGA0Lg8L2J1dHRvbj4NCiAgICAgICAgICAgICAgICA8YnV0dG9uIHR5cGU9ImJ1dHRvbiIgY2xhc3M9ImJ0biBidG4tcHJpbWFyeSAgaGlkZSAiPjwvYnV0dG9uPg0KICAgICAgICAgICAgPC9kaXY%2BDQogICAgICAgIDwvZGl2Pg0KICAgICAgICANCiAgICA8L2Rpdj4NCiAgICANCjwvZGl2Pg0KDQpkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYBBT9jdGwwMCRNYWluQ29udGVudFBsYWNlSG9sZGVyJExvZ2luVmlld01haW4kTG9naW5Gb3JtJFJlbWVtYmVyTWW8BQoGdobdSO1Kbmxe9dd2DSZW9XiW7DbX86WlggNZcg%3D%3D&__EVENTVALIDATION=%2FwEWCQKOzsE6Avvf89oDAqHct7AFAr2Hl4gKAuHhzf4IAqff%2F5QJAq7u180DAqvu180DAtrv18gFrFsmkh803cSQXWJBotCMNr0cp5Ftxmvxm5tjpqlXYnU%3D&ctl00%24HiddenServerTime=&ctl00%24hfJSLocalization=sr-SR&ctl00%24MainContentPlaceHolder%24LoginViewMain%24LoginForm%24UserName=adam.gicevic.317.13&ctl00%24MainContentPlaceHolder%24LoginViewMain%24LoginForm%24Password=z-7Y%214Ly&ctl00%24MainContentPlaceHolder%24LoginViewMain%24LoginForm%24HiddenFieldTest1=&ctl00%24MainContentPlaceHolder%24LoginViewMain%24LoginForm%24HiddenFieldTest2=&ctl00%24MainContentPlaceHolder%24LoginViewMain%24LoginForm%24LoginButton=%D0%9F%D1%80%D0%B8%D1%81%D1%82%D1%83%D0%BF";

                // Tell the URLConnection to use a SocketFactory from our SSLContext
                HttpsURLConnection connection =
                        (HttpsURLConnection)url.openConnection();
                //connection.setSSLSocketFactory(context.getSocketFactory());
                SSLContext SSLContext = cerValid.getSSLcontext();
                connection.setSSLSocketFactory(SSLContext.getSocketFactory());

                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0 (MeeGo; NokiaN9) AppleWebKit/534.13 (KHTML, like Gecko) NokiaBrowser/8.5.0 Mobile Safari/534.13");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;q=0.8,el;q=0.6,hr;q=0.4,sr;q=0.2");
                connection.setRequestProperty("Connection", "keep-alive");
                connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp, *;q=0.8");
                connection.setRequestProperty("Host", "studentskiservis.ict.edu.rs");
                connection.setRequestProperty("Cache-Control", "max-age=0");
                connection.setRequestProperty("Origin", "https://studentskiservis.ict.edu.rs");
                connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
                connection.setRequestProperty("DNT", "1");
                connection.setRequestProperty("Referer", "https://studentskiservis.ict.edu.rs/pocetna");
                connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                connection.setRequestProperty( "charset", "utf-8");
                connection.setInstanceFollowRedirects( false );
                connection.setRequestProperty( "Content-Length", Integer.toString( urlParameters.length() ));

                connection.setUseCaches( false );
                connection.setDoOutput(true);

                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());

                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "POST");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                System.out.println("output===============" + br);
                while((line = br.readLine()) != null ) {
                    responseOutput.append(line);
                }
                br.close();

                output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                for (int i = 0;; i++) {
                    String headerName = connection.getHeaderFieldKey(i);
                    String headerValue = connection.getHeaderField(i);

                    if (headerName == null && headerValue == null) {
                        break;
                    }
                    if ("Set-Cookie".equalsIgnoreCase(headerName)) {
                        String[] fields = headerValue.split(";\\s*");

                        coocStr = fields[0];

                        for (int j = 0; j < fields.length; j++) {
                            System.out.println(fields[j]);

                        }
                    }
                }

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        outputView.setText(output);
                        progress.dismiss();
                    }
                });


            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }

    private class GetClass extends AsyncTask<String, Void, Void> {

        private final Context context;

        public GetClass(Context c){
            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {


                final TextView outputView = (TextView) findViewById(R.id.showOutput);


                CertificateValidation cerValid = new CertificateValidation(getAssets(), "posta_ssl_skola_b64.crt");

                //URL url = new URL("http://www.ict.edu.rs/studiranje/rezultati_new");
                URL url = new URL("https://studentskiservis.ict.edu.rs/student/finansije");


                HttpsURLConnection connection =
                        (HttpsURLConnection)url.openConnection();
                SSLContext SSLContext = cerValid.getSSLcontext();
                connection.setSSLSocketFactory(SSLContext.getSocketFactory());

                String urlParameters = "";
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

                String coocsAdd = "_ga=GA1.3.129672168.1452025326; SESSdcb6275812148e15b218bd8c67cffde0=k6amgelupjcj4ebv2fvgtihku6; ASP.NET_SessionId=a3koh0qcmccyu5b5slowxka5; SESS248bbd5b85db2c5440b02a19409197a2=s86q9stf9ceocjh6hohtmj23c1; __utma=60889780.129672168.1452025326.1468406872.1468500221.54; __utmb=60889780.2.10.1468500221; __utmc=60889780; __utmz=60889780.1459016320.15.7.utmcsr=webdizajn.ict.edu.rs|utmccn=(referral)|utmcmd=referral|utmcct=/;";

                System.out.println("\ncoocsAdd + coocStr : " + coocsAdd + coocStr);
                connection.setRequestProperty("Cookie", coocsAdd + coocStr);

                int responseCode = connection.getResponseCode();

                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);

                final StringBuilder output = new StringBuilder("Request URL " + url);
                output.append(System.getProperty("line.separator") + "Request Parameters " + urlParameters);
                output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
                output.append(System.getProperty("line.separator")  + "Type " + "GET");

                if(responseCode == 200)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    StringBuilder responseOutput = new StringBuilder();
                    System.out.println("output===============" + br);
                    while((line = br.readLine()) != null ) {
                        responseOutput.append(line);
                    }
                    br.close();

                    output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());

                }


                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        outputView.setText(output);
                        progress.dismiss();

                    }
                });


            } catch (MalformedURLException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return null;
        }


    }
*/