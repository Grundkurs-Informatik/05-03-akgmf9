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
}


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
