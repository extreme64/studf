# studf https://docs.google.com/document/d/1Nt7WZb_pVSx5ks64WYZF1h439FhdVphjoZ17CngxgQQ/edit?usp=sharing

Predmet: OOP JAVA 
Tema: XML & HTML parsiranje i prikaz udaljenog sadržaja putem HTTP/HTTPS protokola 


APSTRAKT


Predmet ovog rada predstavlja Android operativni sistem i mogućnosti koje on pruža za izradu aplikacija koje mogu koristiti univerzitetima u podizanju efikasnosti rada, kvaliteta studija i potencijalno smanjivanje troškova poslovanja. 

U prvom delu je  opisan  Android  kao  operativni  sistem  i  njegovi  glavni koncepti,  kao  i  arhitektura sa osnovnim komponentama. Takođe, predstavljeni su i servisi koji se mogu naći na ovoj platformi. 

U drugom delu rada opisana je sama aplikacija razvijena za operativni sistem Android, čija osnovna  namena  je da pomogne sudentu fakulteta u praćenju sadržaja objavljenom na oficijalnoj veb aplikaciji. Namera je olakšati praćenje bitnih sadržaja i informacija.

Treće poglavlje predstavlja uputstvo kako je aplikacija namenjena da se koristi.






Koji problem rešavamo

Internet tehnologije danas nam omogućavaju efikasnost u komunikaciji, kakva se nije mogla zamisliti pre svega nekoliko godina. Bilo koji entitet, od velikih korporacija sve do pojedinca, je na gubitku ako ih ne koristi u punom potencijalu. Gledano iz tog ugla svaka institucija bi trebala da nastoji pratiti poslednje tehnologije i da se prilagođava istim. U slučaju univerziteta, to znači dostupnost informacija studentima 24/7, efikasna komunikacija sa osobljem fakulteta i profesorima i sl.  Komunikacione aplikacje poput: internet stranica, društvenih mreža i takođe posebno dizajniranih mobilnih aplikacija, moraju biti dobro osmišljene, kako bi se ovi ciljevi ispunili.





Odabrano rešenje i prednosti

Predmet ovog rada je idejno rešenje mobilne aplikacije koja omogućava studentima bolju informisanost o trenutnim aktivnostima na fakultetu. Među njima su takođe detalji o predmetima i ispitima, kao i jednostavan pregled zaduženja i dugovanja. Mobilna aplikacija za osnovu ideju ima prosleđivanje i filtriranje sadržaja sa oficijalne stranice fakulteta. Namera je da se željeni sadržaj prikaže u jednostavnoj i preglednoj formi kako bi se omogućilo studentu da bude u toku sa bitnim stvarima, u realnom vremenu, bez suvišnih detalja.





Pregled funkcionalnosti kojima je problem rešen

Predviđene funkcionalnosti su realizovane u nekoliko koraka. Prvi je lakoća pregleda informacija sa udaljenih RSS linkva veznih za: opšte informacije, terminime kolokvijuma i ispita, kao i kalendar dešavanja. 	

Dalje, studentu je na raspolaganju pregled prijavljenih predmeta, ispita i zaduženja iz dela internet aplikacije, zaštićenog iz logovanja, koji je predviđen samo za aktivne studente. Celokupan sadržaj je predstavljen u listama sa minimalnim tekstom. Po potrebi korisnik može pregledati svaku stavku i njenom detaljnom prikazu. U tom modu, dostupna je opcija za čuvanje stavke u zasebnoj listi ‘Favorita’. 

Dohvatanje sadržaja se obavlja putem slanja HTTP i HTTPS  zahteva udaljenom serveru (zvanična internet aplikacija fakulteta, dalje u tekstu kao server). Svaki od ovih zahteva ima prilagođene parametre, propisane HTTP protokolom komunikacije između klijenta i servera. 

Lista favorita, omogućava pamćenje bitnih stavki iz liste, koje pružaju tu opciju, u bazi podataka. Ovim postižemo da zabeleženoj stavci sa lakoćom možemo pristupiti i to bez bojazni gde je informacija ogirinalno pronađena i da li je i dalje dostupna. Putem čuvanja stavki liste u bazi same aplikacije, bitne informacije su sačuvane sve dok ih korisiik po potebi ne odstrani iz favorita.

Takođe realizovana je i opcija ažuriranja zasebnih lista sa obaveštenjima škole. Lista ‘Obaveštenja’ ima i dodatak, u vidu praćena udaljenog sadržaja kroz zaseban samostalni servis, koji po detekciji kreira notifikaciju za korisnika.
	
Efikasnost pristupa bitnim informacija iz dela koji je samo za pojedinog studentaje, rešena je filtriranjem studentskih lista po nekom kriterijumu. Odabirom određenih stavki iz padajuće liste, sve studentske liste se mogu filtrirati. Filteri omogućavaju da student ima jednostavan prikaz podataka koji ga trenutno zanimaju. 

Neke od navedenih mogućnosti zahtevaju određene parametre, poput intervala i aktivnosti servisa nofifikacija ili parametara studenta. Ovi podaci se čuvaju u posebnoj tabeli podešavanja i informacije se unose ili menjaju kroz posebnu stavku u meniju ‘podešavanja’. 

Kao bi iskoristili sve mogućnosti trenutnog hardvera u današnjim mobilnim uređajima, sva obrada podataka ili njihovo dohvatanje sa udaljenih lokacija se obavlja u zasebnim nitima. Sva ostala funkcionalnost je uklopljena da radi sinhronizovano sa istima.

Svi korisnički unosti i interakcija sa aplikacijom je adekvatno propraćena adekvatnim povratnim obaveštenjima (Toast, ProgressBar), kako bi korisnik imao uvid u trenutno stanje i aktivnosti aplikacija na osnovu njegovog inputa.





Zaključak

Kroz opisane funkcionalnosti uspeli smo da omogućimo bilo kom studentu da bude u toku sa bitnim informacijama vezano za fakultet i svoje studije. Iz vrlo kompleksne veb aplikacije izdvojena su samo bitna obaveštenja i informacije. Kalendar lista omogućava lako praćenje dešavanja. Kroz listu favorita sačuvani detalji su uvek momentalno dostupni i permanentni. Dok studenski tab pruža vrlo efektivan pregled najbitnijih podataka za pojedinog studenta, no aplikacija se može znatno proširiti i učiniti još vrednijom, kako studentu tako i fakultetu.

Možemo navesti neke od ideja koje bi znatno doprinele cilju proširenja mogućnosti aplikacije. Mogu se npr. realizovati dodatnr RSS liste iz glavnog dela sajta ili dohvatati dodatan sadržaj iz poddomena Online Kurseva. Novo dodani sadržaj, ali takođe i već postojeći se može dopuniti funkcionalnošću notifikacija, nad svim listama.

Značajan korak bi takođe bio i omogućiti notifikacije koje obaveštavaju o novim porukama na školskom mejlu, što bi pružilo studentima opciju da se i ne moraju logovati na školski mejl server, a opet uvek biti u toku sa konverzacijama. Ove prepiske su najčešće sa profesorima ili studentskom službom, a opšte je poznato koliko je efikasnost u komunikaciji bitna. Kompaktnije prepiske omogućavaju efikasnije iskorišćenje radnog vremena.

U okviru Studenskog Servisa, dodatne opcije vredne razmatranja bi bile: forma za popunu prijave predmeta, obaveštenja/notifikacije o prispelim zaduženjima i dugovanju za prethodne neizmirene obaveze prema fakultetu. 

Učestala je pojava da novim studentima treba određeni period da se upoznaju sa praksom i generalno bitnim stvarima, vezanim za studiranje. U tu svrhu bi koristilo da im se omogući pristupačniji uvid u detalje o fakultetu, smerovima, modulima, upisu, terminima konsultacija  i sl. informacijama. 

Zanimljiva je i opcija auto-popune uplatnica, na osnovu podataka o studentu, trenutnih zaduženja i odabrane svrhe uplate. Mogućnost pogrešno unetih podataka bi tako bila drastično smanjena, a proces popunjavanja znatno olakšan za studenta.

Vredi za kraj napomenuti i to da bi primena detaljnijeg dizajna korisničkog interfejsa unapredila aplikaciju estetski i time bi bila još primamljivija studentima, kroz poboljšan konfor korišćenja.

Trenutna aplikacije je već vrlo korisna svakom studentu - uz gore navedene nadogradnje i proširenja - ova koncepcija bi izrasla u vrlo moćan adut koji bi bio značajan plus u očima svakog studenta pri izboru fakulteta.






Literatura

[1] D. Pain / J. L. Heron, „Educational Technology & Society,“ WebCT and online assessment: the best thing since SOAP?, t. 6, br. 2, pp. 62-71, 2003.

[2] B. Dong, Q. Zheng, J. Yang / L. Haifei, „An E-learning Ecosystem Based on Cloud Computing Infrastructure,“ Advanced Learning Technologies, 2009. ICALT 2009. Ninth IEEE International Conference on, pp. 125-127, 2009.

[3] M. El-Hussein / C. Cronje, „Defining Mobile Learning in the Higher Education Landscape,“ Educational Technology & Society, t. 13, br. 3, pp. 12-21, 2010.

[4] N. Miloradović, Integracija mobilnih obrazovnih servisa u sisteme elektronskog obrazovanja, 120 magistarska teza, Beograd: FON, 2010.

[5]  Android Programming: Pushing the Limits 1st Edition by Erik Hellman
https://www.amazon.com/Android-Programming-Pushing-Erik-Hellman/dp/1118717376/
[6]  Android Design Patterns: Interaction Design Solutions for Developers 1st Edition by by Greg Nudelman 					 				
https://www.amazon.com/Greg-Nudelman/e/B004Z9O224/
[7]  Efficient Android Threading: Asynchronous Processing Techniques for Android Applications 1st Edition by Anders Goransson			 			
https://www.amazon.com/Efficient-Android-Threading-Asynchronous-Applications/dp/1449364136/

[8]  Android High Performance Programming Paperback – August 29, 2016
by Enrique Lopez Manas, Diego Grancini
https://www.amazon.com/Android-Performance-Programming-Enrique-Lopez/dp/1785288954/



https://docs.google.com/document/d/1Nt7WZb_pVSx5ks64WYZF1h439FhdVphjoZ17CngxgQQ/edit?usp=sharing
