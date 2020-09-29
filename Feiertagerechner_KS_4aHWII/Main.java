package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static NodeList dateOH, daysOH;
    static int mon = 0, tue = 0, wed = 0, thu = 0, fri = 0;
    static File file = new File("C:\\Users\\Admin\\Desktop\\Repositories\\Briefe\\src\\holidays.xml");
    static int year;
    static LocalDate startDate;
    static List<LocalDate> holidaysMonth = new ArrayList<LocalDate>();
    static List<String> holidaysDays = new ArrayList<String>();
    static Scanner reader = new Scanner(System.in);


    public static void main(String[] args) {
        importFile();
        input();
        fillList(holidaysDays, holidaysMonth);
       checkday(holidaysDays, holidaysMonth);
       output();


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

    public static void fillList(List<String> holidaysDays, List<LocalDate> holidaysDates) {

        for (int i = 0; i < daysOH.getLength(); i++) {
            Node node = daysOH.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                holidaysDays.add(eElement.getElementsByTagName("Day").item(0).getTextContent());

            }
        }


        for (int i = 0; i < dateOH.getLength(); i++) {
            Node node = dateOH.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                holidaysDates.add(LocalDate.of(startDate.getYear(), Integer.parseInt(eElement.getElementsByTagName("Month").item(0).getTextContent()), Integer.parseInt(eElement.getElementsByTagName("Day").item(0).getTextContent())));

            }
        }
        for (int y = 1; y < year; y++) {
            for (int i = 0; i < dateOH.getLength(); i++) {
                holidaysDates.add(holidaysDates.get(i).plusYears(y));
            }
        }


    }

    public static void input() {
        System.out.print("Anzahl der Jahre eingeben: ");
        year = reader.nextInt();
        System.out.print("Startjahr[yyyy]: ");
        startDate = LocalDate.of(reader.nextInt(), 1, 1);

    }

    public static void checkday(List<String> holidaysDays, List<LocalDate> holidaysDates) {
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
        for (int i = 0; i < holidaysDays.size(); i++) {
            switch (holidaysDays.get(i)) {
                case "MONDAY":
                    mon = mon + year;
                    break;

                case "TUESDAY":
                    tue = tue + year;
                    break;

                case "WEDNESDAY":
                    wed = wed + year;
                    break;

                case "THURSDAY":
                    thu = thu + year;

                    break;
                case "FRIDAY":
                    fri = fri + year;
                    break;
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
