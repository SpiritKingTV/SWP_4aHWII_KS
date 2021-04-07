
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
![.](https://github.com/SpiritKingTV/SWP_4aHWII_KS/blob/master/AktienProgramm_KS_4aHWII/IBM_Aktie.PNG)

## Funktionsweise
Nachdem man die Textfile mit den Aktienkürzel eingegeben hat, und mit den Windows-Steuerungen das Programm automatisch an einer bestimmten Urzeit ausführen lässt,
kann man es, ohne selbst was tun zu müssen laufen lassen.

## Zusätzliche Features
 1) Wenn der letzte gleitende Mittelwert größer als der Close-Wert ist, ist der Hintergrund des Charts rot, und ansonsten grün
 2) Der API-Key wird aus einer Txt-FIle ausgelesen
 3) Die y-Achse beginnt nicht bei null(Passt sich auf den Graphen an)
 
