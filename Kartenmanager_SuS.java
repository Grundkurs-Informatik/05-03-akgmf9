import sum.ereignis.*;
import sum.komponenten.*;
import sum.werkzeuge.*;
import sum.strukturen.*;

/**
 * Kartenmanager_SuS:
 * Verwendung einer Liste<Karte> (aus sum.strukturen.Liste) 
 * anstelle eines Arrays. 
 * Die GUI wurde beibehalten (Stapelgr., Wert, Farbe, Position usw.).
 * - Einfügen/Entfernen mit Liste
 * - Sortieren/Erneuern (Platzhalter) 
 * - Zeichnen ab y=150
 */

public class Kartenmanager_SuS extends Ereignisanwendung {
    // -----------------------------
    // 1) GUI-Objekte
    // -----------------------------
    private Knopf startKnopf;   // "Sortieren"
    private Knopf updateKnopf;  // "Erneuern"
    private Knopf einfKnopf;    // "Einfügen"
    private Knopf entfKnopf;    // "Entfernen"

    private Etikett infoEtikett;    
    private Etikett labelStapelsize; 
    private Etikett labelWert;  
    private Etikett labelFarbe; 
    private Etikett labelPos;   

    private Auswahl auswahlWert;   
    private Auswahl auswahlFarbe;  

    private Textfeld tfUmfang;  // neue Stapelgröße
    private Textfeld tfPos;     // Position für Einfügen/Entfernen

    private Buntstift stift;

    // -----------------------------
    // 2) Listen-Datenstruktur
    // -----------------------------
    // Karte => Hilfsklasse für (wert, farbe)
    private class Karte {
        int wert;   // 1..13 => "2..Ass"
        int farbe;  // 0..3  => "Kreuz..Pik"
        public Karte(int w, int f) {
            this.wert = w;
            this.farbe= f;
        }
    }

    // Unsere Liste<Karte> anstelle von int[][]
    private Liste<Karte> karten;
    private int kartenAnzahl = 52;  // "logische" Stapelgröße

    // Namen für Werte und Farben (Index 1..13 => "2..Ass")
    private String[] werteNamen = {
            "", "2", "3", "4", "5", "6", 
            "7", "8", "9", "10", "Bube", "Dame", "König", "Ass"
        };

    private String[] farbenNamen = {
            "Kreuz", "Karo", "Herz", "Pik"
        };

    // Zeitmessung
    private long startZeit;
    private long endZeit;

    public Kartenmanager_SuS() {
        super();
        stift = new Buntstift();

        // 3) GUI: 
        //    - InfoEtikett + Stapelgröße/Erneuern/Sortieren (links)
        //    - Wert/Farbe/Position + Einfügen/Entfernen (rechts)
        //    - Karten ab y=150



        infoEtikett    = new Etikett( 50,  20, 400, 30, 
            "Karten mit Liste (Einfügen/Entfernen).");

        labelStapelsize= new Etikett( 50,  60, 80, 25, "Stapelgr.:");
        tfUmfang       = new Textfeld(130, 60, 60, 25, "52"); // Standard=52
        updateKnopf    = new Knopf(200, 60, 100, 30, "Erneuern");
        startKnopf     = new Knopf(310, 60, 100, 30, "Sortieren");
        tfDurchgänge   = new Textfeld(310, 100, 100, 30, "1000");
        auswahlSort    = new Auswahl(310, 20, 100, 30);

        auswahlSort.haengeAn("Bubblesort");
        auswahlSort.haengeAn("Instertsort");
        auswahlSort.haengeAn("Mergesort");



        // Wert/Farbe/Position 

        labelWert   = new Etikett(450,  20, 40, 25, "Wert:");
        auswahlWert = new Auswahl(500, 20, 80, 25);

        labelFarbe  = new Etikett(600, 20, 50, 25, "Farbe:");
        auswahlFarbe= new Auswahl(660,20, 80, 25);

        labelPos    = new Etikett(450, 60, 60, 25, "Position:");
        tfPos       = new Textfeld(520, 60, 60, 25, "0");

        einfKnopf   = new Knopf(600, 60, 100, 30, "Einfügen");
        entfKnopf   = new Knopf(710, 60, 100, 30, "Entfernen");

        // Auswahllisten befüllen: Index 0..12 => "2..Ass"
        for(int w=1; w<=13; w++){
            auswahlWert.haengeAn(werteNamen[w]);
        }

        // Index 0..3 => "Kreuz..Pik"
        for(int f=0; f<4; f++){
            auswahlFarbe.haengeAn(farbenNamen[f]);
        }


        // 4) Ereignismethoden

        startKnopf.setzeBearbeiterGeklickt("Sort_Klick");

        updateKnopf.setzeBearbeiterGeklickt("Update_Klick");

        einfKnopf.setzeBearbeiterGeklickt("Einfuegen_Klick");

        entfKnopf.setzeBearbeiterGeklickt("Entfernen_Klick");



        // 5) Listen-Datenstruktur anlegen

        karten = new Liste<Karte>(); 

        // => "kartenAnzahl" als "logische" Größe, 

        //    wir initialisieren so viele Einträge



        initialisiereKarten(0); // füllt die Liste mit 52 Karten



        // 6) Zeichnen ab y=150

        zeichneKarten(0, 50, 150);



        this.fuehreAus();

    }



    // ----------------------------------------------------------

    // Initialisieren & Zeichnen

    // ----------------------------------------------------------



    /**

     * initialisiereKarten(i):

     *  Legt kartenAnzahl Karten an.

     *  Jede Karte zufällig => wert in [1..13], farbe in [0..3]

     */

    void initialisiereKarten(int i) {

        if(i>=kartenAnzahl) return;



        // Erzeuge eine neue Karte

        int w = (int)(Math.random()*13)+1;

        int f = (int)(Math.random()*4);



        // Zur Sicherheit: Erst zumEnde(), dann fuegeDahinterEin

        karten.zumEnde();

        karten.fuegeDahinterEin(new Karte(w, f));



        initialisiereKarten(i+1);

    }



    /**

     * zeichneKarten(index, x, y):

     *  Durchläuft die Liste per gehZuPosition(index),

     *  holt das aktuelleElement() => (wert, farbe),

     *  zeichnet die Karte.

     */

    void zeichneKarten(int index, int x, int y) {

        if (index >= karten.laenge()) return; // Ende der Liste



        // Zur Position index gehen

        karten.geheZuPosition(index+1);

        Karte c = karten.aktuellesElement(); // => (wert, farbe)



        String name = farbenNamen[c.farbe] + " " + werteNamen[c.wert];

        zeichneEineKarte(x, y, name);



        x += 90;

        if ((index+1)%13 == 0) {

            x = 50;

            y += 130;

        }



        // rekursiver Aufruf

        zeichneKarten(index+1, x, y);

    }



    void zeichneEineKarte(int x, int y, String name) {

        stift.bewegeBis(x,y);

        stift.zeichneRechteck(80,120);

        stift.bewegeBis(x+5, y+50);

        stift.schreibeText(name);

    }



    /**

     * Vorgabe:

     * stift.radiere();

     * zeichneKarten(0,50,150);

     * stift.normal();

     */

    void loescheAnzeige() {

        stift.radiere();

        zeichneKarten(0, 50, 150);

        stift.normal();

    }



    // ----------------------------------------------------------

    // Ereignismethoden

    // ----------------------------------------------------------



    public void SelectSort_Klick() {
        loescheAnzeige();
        startZeit = System.nanoTime();

        // z.B. 1000 Durchläufe
        for(int I=0; I<1000; I++){
            boolean sortiert = true;
            karten = new Liste<Karte>(); 
            initialisiereKarten(0);
            do {
                sortiert = true;
                for(int i = 1; i < karten.laenge(); i++){
                    karten.geheZuPosition(i);
                    Karte Karte_a = karten.aktuellesElement();           
                    karten.geheZuPosition(i+1);
                    Karte Karte_b = karten.aktuellesElement();
                 if(Karte_a.wert > Karte_b.wert||Karte_a.wert == Karte_b.wert && Karte_a.farbe < Karte_b.farbe){
                        karten.ersetzeAktuelles(Karte_a);
                        karten.geheZuPosition(i);
                        karten.ersetzeAktuelles(Karte_b);
                        sortiert = false;

                    }
                }

            }    while(!sortiert);

        //Startzeit messen
        startZeit = System.currentTimeMillis();

        // z.B. 100 Durchläufe
        for(int i=0; i<100; i++){

            Sortieren();
        }
        endZeit=System.nanoTime();
        double d = endZeit - startZeit;
        d = d/1000000;

        //Endzeit messen
        endZeit = System.currentTimeMillis();
        double dauer = endZeit - startZeit;
        loescheAnzeige();
        zeichneKarten(0, 50, 150);
        infoEtikett.setzeInhalt("Erfolgreich Sortiert. Zeit: "+ d + " ms");
        infoEtikett.setzeInhalt("Karten sotiert in "+ dauer+" ms");
    }





    public void Sortieren(){    
        //boolean eingefügt;
        int n = kartenAnzahl;
        //do {
        karten.zumAnfang();
        loescheAnzeige();
        for (int i = 1; i <= karten.laenge(); i++) {
            karten.geheZuPosition(i); Karte a = karten.aktuellesElement();
            karten.loescheAktuelles();
            boolean eingefügt = false;
            int j = i-1;
            while(j >= 1 && !eingefügt) {
                karten.geheZuPosition(j);
                Karte b = karten.aktuellesElement();
                if(a.wert < b.wert || (a.wert == b.wert && a.farbe < b.farbe)) {
                    j--;
                } else {
                    eingefügt = true; } }
            karten.geheZuPosition(j+1);
            karten.fuegeDavorEin(a);}
        // }
    }
    // n--; // Reduziere die Anzahl der zu vergleichenden Elemente
    // } while (vertauscht);
    // }

     public void BubbleSort_Klick() {
        loescheAnzeige();
        startZeit = System.nanoTime();
    
        // z.B. 1000 Durchläufe
        for(int I=0; I<1000; I++){
            boolean sortiert = true;
            karten = new Liste<Karte>(); 
            initialisiereKarten(0);
            do
            {
                sortiert = true;
                for(int i = 1; i < karten.laenge(); i++)
                {
                    karten.geheZuPosition(i);
                    Karte Karte_a = karten.aktuellesElement();           
                    karten.geheZuPosition(i+1);
                    Karte Karte_b = karten.aktuellesElement();
                    if(Karte_a.wert > Karte_b.wert||Karte_a.wert == Karte_b.wert && Karte_a.farbe < Karte_b.farbe)
                    {
                        karten.ersetzeAktuelles(Karte_a);
                        karten.geheZuPosition(i);
                        karten.ersetzeAktuelles(Karte_b);
                        sortiert = false;
                    }
                }
            }while(!sortiert);
        }
        endZeit=System.nanoTime();
        double d = endZeit - startZeit;
        d = d/1000000;
        zeichneKarten(0, 50, 150);
        infoEtikett.setzeInhalt("Erfolgreich Sortiert. Zeit: "+ d + " ms");
    }

        public void InsertSort()
    {
        for(int i = 2; i <= karten.laenge(); i++)
        {
            karten.geheZuPosition(i);
            Karte a = karten.aktuellesElement();
            karten.loescheAktuelles();
            boolean eingefuegt = false;
            int j = i-1;
            while(j >= 1 && !eingefuegt)
            {
                karten.geheZuPosition(j);
                Karte b = karten.aktuellesElement();
                if(a.wert < b.wert || a.wert == b.wert && a.farbe < b.wert)
                {
                    j--;
                }else
                {
                    eingefuegt = true;

                }

            }
            karten.geheZuPosition(j+1);
            karten.fuegeDavorEin(a);
        }
    }

        
    void mergesort() {
        int[][] KartenArray = new int[karten.laenge()][2]; // falls karten eine eigene Liste ist, dann karten.laenge()
        Karte a;

        for (int i = 0; i < KartenArray.length; i++) { 
            karten.geheZuPosition(i + 1);
            a = karten.aktuellesElement();
            KartenArray[i][0] = a.wert;
            KartenArray[i][1] = a.farbe;
        }

        KartenArray = split(KartenArray, KartenArray.length);

        karten = new Liste<Karte>(); 

        for (int i = 0; i < KartenArray.length; i++) {
            int w = KartenArray[i][0];
            int f = KartenArray[i][1];
            karten.zumEnde();
            karten.fuegeDahinterEin(new Karte(w, f));
        }
    }

    int[][] split(int[][] arr, int length) {
        if (length <= 1) return arr;

        int mitte = length / 2;
        int[][] l = new int[mitte][2];
        int[][] r = new int[length - mitte][2];

        for (int i = 0; i < length; i++) {
            if (i < mitte) l[i] = arr[i];
            else r[i - mitte] = arr[i];
        }

        l = split(l, l.length);
        r = split(r, r.length);

        return merge(l, r);
    }

    int[][] merge(int[][] arr1, int[][] arr2) {        
        int totalLength = arr1.length + arr2.length;
        int[][] merged = new int[totalLength][2];

        int i = 0, j = 0, k = 0;

        while (i < arr1.length && j < arr2.length) {
            if (arr1[i][0] < arr2[j][0] || (arr1[i][0] == arr2[j][0] && arr1[i][1] < arr2[j][1])) {
                merged[k++] = arr1[i++];
            } else {
                merged[k++] = arr2[j++];
            }
        }

        while (i < arr1.length) merged[k++] = arr1[i++];
        while (j < arr2.length) merged[k++] = arr2[j++];

        return merged;
    }



    public void Update_Klick() {

        String s = tfUmfang.inhaltAlsText().trim();

        int neuAnz;


        //Startzeit messen


        startZeit = System.currentTimeMillis();

        try {

          neuAnz = Integer.parseInt(s);

            if(neuAnz<=0) neuAnz=52;
@@ -256,58 +267,38 @@
        initialisiereKarten(0);
        zeichneKarten(0, 50, 150);
        infoEtikett.setzeInhalt("Neuer Stapel: "+kartenAnzahl+" Karten (Liste).");
        //Endzeit messen
        endZeit = System.currentTimeMillis();
        double dauer = endZeit - startZeit;
        infoEtikett.setzeInhalt("Karten erneurt in "+ dauer+" ms");

        //infoEtikett.setzeInhalt("Neuer Stapel: "+kartenAnzahl+" Karten (Liste).");
    }
        
    public void Einfuegen_Klick() {
        try
        {
            int pos = Integer.parseInt(tfPos.inhaltAlsText());
            int w = auswahlWert.index();
            int f = auswahlFarbe.index()-1;
            loescheAnzeige();
            karten.geheZuPosition(pos);
            if(pos >= karten.laenge())
            {
                karten.fuegeDahinterEin(new Karte(w,f));
                tfPos.setzeInhalt(karten.laenge());
            }else if(pos < 0)
            {
                tfPos.setzeInhalt(0);
                karten.fuegeDavorEin(new Karte(w,f));
            }else
            {
                karten.fuegeDavorEin(new Karte(w,f));
            }
            zeichneKarten(0, 50, 150);
        }catch(NumberFormatException e)
        {
            infoEtikett.setzeInhalt("Ungültige Position");
        }
        int wert = auswahlWert.index();
        int farbe = auswahlFarbe.index()-1;
        int pos = Integer.parseInt(tfPos.inhaltAlsText().trim());
        loescheAnzeige();
        karten.geheZuPosition(pos + 1);
        karten.fuegeDahinterEin(new Karte(wert, farbe));
        zeichneKarten(0,50,150);
    }
        
    public void Entfernen_Klick() {
        try
        {
            int pos = Integer.parseInt(tfPos.inhaltAlsText());
            loescheAnzeige();
            if(pos <= 0)
            {
                pos = 1;
                tfPos.setzeInhalt(1);
            }
            if(pos >= karten.laenge())tfPos.setzeInhalt(karten.laenge()-1);
            karten.geheZuPosition(pos);
            karten.entferneAktuelles();
            zeichneKarten(0, 50, 150);
        }catch(NumberFormatException e)
        {
            infoEtikett.setzeInhalt("Ungültige Position");
        }
    }
        int wert = auswahlWert.index();
        int farbe = auswahlFarbe.index()-1;
        int pos = Integer.parseInt(tfPos.inhaltAlsText().trim());
}
        loescheAnzeige();
        karten.geheZuPosition(pos +- 1);
        karten.loescheAktuelles();
        zeichneKarten(0,50,150);
    }
}
