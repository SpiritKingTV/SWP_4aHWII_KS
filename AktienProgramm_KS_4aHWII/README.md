
# AktienProgramm
Ist ein Programm, bei dem man Aktien von verschiedenen Unternehmen aus einer API auslesen kann, die wiederum in eine Datenbank
gespeichert wird und Grafisch mit JavaFX ausgegeben werden

## Was braucht man dafür
* [JavaFX](https://openjfx.io)</br>
* [Java Json](https://jar-download.com/artifacts/org.json)
* [SQLite Datenbank](https://www.sqlite.org/index.html)

* [Alphavantage API](https://www.alphavantage.co)

## JavaFX
![.](https://github.com/SpiritKingTV/SWP_4aHWII_KS/blob/master/AktienProgramm_KS_4aHWII/IBM_Aktie.PNG)

note: Leider habe ich es noch nicht geschafft, dass sich die Farbe auf grün ändert, wenn der Graph über den SChnitt ist,
deswegen wurde es in diesem Programm heraus gelassen.

## Funktionsweise
Zuerst darf der Benutzer den Kürzel des Unternehmens eingeben. Danach werden Alle Daten von der API in eine Liste geschrieben, sortiert, und in eine Hashmap geschrieben
Danach wird es in die Datenbank geschrieben und man kann den Schnitt auswählen (Bsp. 200er Schnitt, 50er Schnitt usw...)dabei ist die Zahl frei wählbar.
Als nächstes werden die Daten aus der Datenbank geholt und für den Graphen von JavaFX verwendet.

