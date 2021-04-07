
# AktienProgramm
Ist ein Program, welches einmal am Tag an einer bestimmten Urzeit ausgeführt wird, und in einer Textdatei angegebenen Aktien aus einer API herausliest, in der Datenbank einspeichert 
und anschließend ein Bild davon speichert.

## Was braucht man dafür
* [JavaFX](https://openjfx.io)</br>
* [Java Json](https://jar-download.com/artifacts/org.json)
* [SQLite Datenbank](https://www.sqlite.org/index.html)

* [Alphavantage API](https://www.alphavantage.co)

## JavaFX
Mithilfe von JavaFX wird der Graph erstellt und danach mithilfe einer Java Klasse gescreenshottet wird
![.](https://github.com/SpiritKingTV/SWP_4aHWII_KS/blob/master/AktienProgramm_RENEWED_KS_4aHWII/googl_2021-04-04.png)

## Funktionsweise
Zuerst muss man die 1. Zeile beim Starten des Programms kopieren, und in dem Texteditor einfügen. Die File muss anschließend als .cmd gespeichert werden.
Danach muss man die Textfile mit den Aktienkürzel erstellen,dessen Pfad im Programm angegeben werden muss. Mit den Windows-Steuerungen kann man dann die vorher
erstellte CMD-File jeden Tag an einer bestimmten Urzeit ausführen lassen. Danach funktioniert das Program automatisch


## Zusätzliche Features
 1) Wenn der letzte gleitende Mittelwert größer als der Close-Wert ist, ist der Hintergrund des Charts rot, und ansonsten grün
 2) Der API-Key wird aus einer Txt-FIle ausgelesen
 3) Die y-Achse beginnt nicht bei null(Passt sich auf den Graphen an)
 
