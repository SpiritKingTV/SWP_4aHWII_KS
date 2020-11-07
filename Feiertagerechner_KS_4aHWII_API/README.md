Um dieses Programm ausführen zu können brauchen Sie:
1) Die Main.java datei

2) DIe holidays.xml file

3) Alle .jar Files vom "Feriertagrechner_KS_4aHWII_API"-Ordner



Zunächst müssen Sie noch ein paar Zeilen in der Main.Java umschreiben:
1) Bei Zeile 41: Ihren Speicherort von der hlidays.xml FIle angeben

2) Bei Zeile 42: Ihren Speicherort von Ihrer Database.db file angeben (falls sie nicht existiert wird sie 
automatisch am angegebenen Speicherort erstellt)

3) Danach müssen Sie die .jar Files (Libraries die für dieses Programm benötigt werden) importiert werden
Dies ist bei den verschiedenen IDE's unterschiedlich..
a) IntelliJ: File -> Project Structure -> Modules -> Dependencies -> + -> Libraries -> (Libraries auswählen)
-> apply

b) Eclipse: Rechtsklick auf Ihr Java Projekt -> Properties -> Java Build Path -> Add external Jars -> Jar Files
auswählen -> Apply and close


Nach diesen Schritten, ist das Programm startbereit:
Zuerst Fragt es SIe nach dem Startjahr, nach der Eingabe nach der Anzahl der Jahre.
Danach wird ein JavaFX Fenster mit einer Tabelle geöffnet, welche Ihnen die Anzahl der Tage in den Gewünschten Zeitraum
grafisch anzeigt, an denen ein Feiertag ist.
Danach wird in dem Konsolenfenster noch die Anzahl ausgegeben.
Zudem wird in der Konsole noch die Datenbank ausgeben wie folgt:
bsp.:
    Startdatum - Enddatum    Montag    Dienstag    Mittwoch    Donnerstag    Freitag
   01.01.2020 - 31.12.2030		37		     13		       15		         35		       14	 
   
   


