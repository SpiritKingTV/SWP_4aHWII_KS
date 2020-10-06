package main;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static NodeList dateOH, daysOH;
    static int mon=0,tue=0,wed=0,thu=0,fri =0;
    static int startyear, countyear, yearnow;
    static List<LocalDate> dynamicDates = new ArrayList<>();
    static List<LocalDate> holidayDates = new ArrayList<>();
    static String url;
    static Scanner reader = new Scanner(System.in);
    static String Bundesland = "&nur_land=BY";
    static File file = new File("C:\\Users\\Admin\\Desktop\\Repositories\\Briefe\\src\\holidays.xml");

    public static void main(String[] args) throws IOException {
        importFile();
        anzahlJahre();
        String url = "https://feiertage-api.de/api/?jahr=" + startyear + Bundesland;
        yearnow = startyear;
        JSONObject json = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        fillList(holidayDates);

        for (int i = 0; i < countyear; i++) {

            getDynamicDate(json, "Ostermontag");
            getDynamicDate(json, "Christi Himmelfahrt");
            getDynamicDate(json, "Pfingstmontag");
            getDynamicDate(json, "Fronleichnam");
            yearnow++;
            URLFromYear(yearnow);
        }
        checkday(dynamicDates,holidayDates);
        output();
        System.out.println(dynamicDates.toString());
        System.out.println(holidayDates.toString());




    }

    public static void URLFromYear(int year) throws MalformedURLException {
        url = ("https://feiertage-api.de/api/?jahr=" + year + Bundesland);
    }

    public static void anzahlJahre() {
        System.out.print("Geben Sie das Startjahr ein: ");
        startyear = reader.nextInt();

        System.out.print("Geben Sie die Anzahl der Jahre ein: ");
        countyear = reader.nextInt();
    }

    private static void getDynamicDate(JSONObject json, String key) {
        JSONObject daten = (JSONObject) json.get(key);
        String date = daten.get("datum").toString();
        LocalDate ldate = LocalDate.parse(date);
        dynamicDates.add(ldate);
        System.out.println(ldate.toString());

    }

    public static void checkday(List<LocalDate> dynamicDates, List<LocalDate> holidaysDates) {
        for (int i = 0; i < holidaysDates.size(); i++) {
            switch (holidaysDates.get(i).getDayOfWeek()) {
                case MONDAY:
                    mon++;
                    break;
                case TUESDAY:
                    tue++;
                    break;
                case WEDNESDAY:
                    wed++;

                    break;
                case THURSDAY:

                    thu++;
                    break;


                case FRIDAY:

                    fri++;
                    break;

            }
        }
        for (int i = 0; i < dynamicDates.size(); i++) {
            switch (dynamicDates.get(i).getDayOfWeek()) {
                case MONDAY:
                    mon++;
                    break;
                case TUESDAY:
                    tue++;
                    break;
                case WEDNESDAY:
                    wed++;

                    break;
                case THURSDAY:

                    thu++;
                    break;


                case FRIDAY:

                    fri++;
                    break;

            }
        }
    }

    public static void importFile() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            dateOH = doc.getElementsByTagName("Date");
            daysOH = doc.getElementsByTagName("Days");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillList(List<LocalDate> holidaysDates){
        for (int i = 0; i < dateOH.getLength(); i++) {
            Node node = dateOH.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                holidaysDates.add(LocalDate.of(startyear, Integer.parseInt(eElement.getElementsByTagName("Month").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("Day").item(0).getTextContent())));

            }
        }
        for (int y = 1; y < countyear; y++) {
            for (int i = 0; i < dateOH.getLength(); i++) {
                holidaysDates.add(holidaysDates.get(i).plusYears(y));
            }
        }


    }

    public static void output(){

        System.out.println("Montage: " + mon);
        System.out.println("Dienstage: "+ tue);
        System.out.println("Mittwoche: " + wed);
        System.out.println("Donnerstage: " + thu);
        System.out.println("Freitage: "+ fri);
    }

}
