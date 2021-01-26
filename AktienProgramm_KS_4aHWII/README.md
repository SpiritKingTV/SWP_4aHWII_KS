
# AktienProgramm
Ist ein Programm, bei dem man Aktien von verschiedenen Unternehmen aus einer API auslesen kann, die wiederum in eine Datenbank
gespeichert wird und aus der Datenbank heraus Grafisch mit JavaFX dargestellt werden

## Was braucht man dafür
* [JavaFX](https://openjfx.io)</br>
* [Java Json](https://jar-download.com/artifacts/org.json)
* [SQLite Datenbank](https://www.sqlite.org/index.html)

* [Alphavantage API](https://www.alphavantage.co)

## JavaFX
![.](https://github.com/SpiritKingTV/SWP_4aHWII_KS/blob/master/AktienProgramm_KS_4aHWII/IBM_Aktie.PNG)

## Funktionsweise
Zuerst darf der Benutzer den Kürzel des Unternehmens eingeben. Danach werden alle Daten von der API in eine Hashmap zwischengespeichgert und danach in die Datenbank geschrieben.
Danach kann man noch angeben, welchen Schnitt man haben will(z.B. 200er Schnitt, 50er Schnitt [dynamisch]). Danach kann man noch auswählen, ob man den ganzen Graphen oder die letzten beliebigen Werte des angegebenen Unternehmens darstellen will(Die letzten 500 Werte z.B [siehe Bild])

## Zusätzliche Features
 1) Wenn der letzte gleitende Mittelwert größer als der Close-Wert ist, ist der Hintergrund des Charts rot, und ansonsten grün
 2) Man kann alle oder eine bestimmte Anzahl der letzten Werte darstellen lassen
 3) Die y-Achse beginnt nicht bei null(Passt sich auf den Graphen an)

## Bild von den letzten 500 WErten von IBM
![.](https://github.com/SpiritKingTV/SWP_4aHWII_KS/blob/master/AktienProgramm_KS_4aHWII/IBM_Aktie.PNG)
