package Images;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Algo{
    public static int bubbleArray[] = new int[1000];
    public static int selectionS[] = new int[1000];
    public static int stableselectionS[] = new int[1000];
    public static int insertionArray[] = new int[1000];
    public static int qsArray[]= new int[1000];


    public static ArrayList<Integer> bubbleSortVergleiche = new ArrayList<Integer>();
    public static ArrayList<Integer> bubbleSortTausche = new ArrayList<Integer>();
    public static ArrayList<Long> bubbleSortTime = new ArrayList<Long>();

    public static ArrayList<Integer> selectionSortVergleiche = new ArrayList<Integer>();
    public static ArrayList<Integer> selectionSortTausche = new ArrayList<Integer>();
    public static ArrayList<Long> selectionSortTime = new ArrayList<Long>();

    public static ArrayList<Integer> stableselectionSortVergleiche = new ArrayList<Integer>();
    public static ArrayList<Integer> stableselectionSortTausche = new ArrayList<Integer>();
    public static ArrayList<Long> stableselectionSortTime = new ArrayList<Long>();

    public static ArrayList<Integer> InsertionSortVergleich = new ArrayList<Integer>();
    public static ArrayList<Integer> InsertionSortTausch = new ArrayList<Integer>();
    public static ArrayList<Long> InsertionSortTime = new ArrayList<Long>();

    public static ArrayList<Integer> QSVergleich = new ArrayList<Integer>();
    public static ArrayList<Integer> QSTausch = new ArrayList<Integer>();
    public static ArrayList<Long> QSTime = new ArrayList<Long>();


    public static int vglbs = 0;
    public static int swapbs = 0;
    public static int vglss = 0;
    public static int swapss = 0;
    public static int vglsss = 0;
    public static int swapsss = 0;
    public static int swapis = 0;
    public static int vglis = 0;
    public static int qsvgl = 0;
    public static int qsswap = 0;
    public static long startTime = 0, endTime = 0, time = 0;


    public static double bbsDurchschnittvergl;
    public static double bbsDurchschnitttausche;
    public static long bbstime;

    public static double ssDurchschnittvergl;
    public static double ssDurchschnitttausche;
    public static long sstime;

    public static double stablessDurchschnittvergl;
    public static double stablessDurchschnitttausche;
    public static long ssstime;

    public static double insertionsortvergl;
    public static double insertionsorttausche;
    public static long istime;

    public static double qsvergleiche;
    public static double qstausche;
    public static long qstime;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            makeArrays();
            bubblesort(bubbleArray);
            selectionSort(selectionS);
            stableSelectionSort(stableselectionS);
            insertionSort(insertionArray);
            startTime = System.nanoTime();
            quickSort(qsArray, 0,qsArray.length-1);
            endTime = System.nanoTime();
            time = endTime - startTime;
            QSTime.add(time);
            startTime = 0;
            endTime = 0;
            time = 0;

            bubbleSortVergleiche.add(vglbs);

            bubbleSortTausche.add(swapbs);
            selectionSortVergleiche.add(vglss);
            selectionSortTausche.add(swapss);
            stableselectionSortVergleiche.add(vglsss);
            stableselectionSortTausche.add(swapsss);
            InsertionSortTausch.add(swapis);
            InsertionSortVergleich.add(vglis);
            QSTausch.add(qsswap);
            QSVergleich.add(qsvgl);
            clearIt();



        }
        calcavg();
        Collections.sort(bubbleSortTausche);
        Collections.sort(selectionSortTausche);
        Collections.sort(stableselectionSortTausche);
        Collections.sort(InsertionSortTausch);

        System.out.println("Durchschnittstausche Bubblesort: " + bbsDurchschnitttausche);
        System.out.println("Durchschnittsvergleiche Bubblesort: " + bbsDurchschnittvergl);
        System.out.println("Median Tausche Bubblesort: " + bubbleSortTausche.get((bubbleSortTausche.size() / 2)));
        System.out.println("Benötigte Durchschnittszeit(Nanosekunden): " + bbstime);
        System.out.println(" ");
        System.out.println("Durchschnittstausche SelectionSort: " + ssDurchschnitttausche);
        System.out.println("Durchschnittsvergleiche SelectionSort: " + ssDurchschnittvergl);
        System.out.println("Median Tausche Selectionsort: " + selectionSortTausche.get((selectionSortTausche.size() / 2)));
        System.out.println("Benötigte Durchschnittszeit(Nanosekunden): " + sstime);
        System.out.println(" ");
        System.out.println("Durchschnittstausche StableSelectionSort: " + stablessDurchschnitttausche);
        System.out.println("Durchschnittsvergleiche StableSelectionSort: " + stablessDurchschnittvergl);
        System.out.println("Median Tausche StableSelectionsort: " + stableselectionSortTausche.get((stableselectionSortTausche.size() / 2)));
        System.out.println("Benötigte Durchschnittszeit(Nanosekunden): " + ssstime);
        System.out.println("");
        System.out.println("Durchschnittstausche InsertionSort: " + insertionsorttausche);
        System.out.println("Durchschnittsvergleiche Insertionsort: " + insertionsortvergl);
        System.out.println("Median Tausche Insertionsort: " + InsertionSortTausch.get((InsertionSortTausch.size() / 2)));
        System.out.println("Benötigte Durchschnittszeit(Nanosekunden): " + istime);
        System.out.println("");
        System.out.println("Durchschnittstausche Quicksort: " + qstausche);
        System.out.println("Durchschnittsvergleiche Quicksort: " + qsvergleiche);
        System.out.println("keine Ahnung wie das geht ");
        //System.out.println("Median Tausche Insertionsort: " + InsertionSortTausch.get((InsertionSortTausch.size() / 2)));
        System.out.println("Benötigte Durchschnittszeit(Nanosekunden): " + qstime);
        //System.out.println(InsertionSortTausch);
        // System.out.println(InsertionSortVergleich);





        //  for(int i =0;i<ssDurchschnitttausche)
        // System.out.println(bubbleSortTausche);


    }


    public static void bubblesort(int array[]) {
        startTime = System.nanoTime();
        int n = array.length;

        for (int x = 0; x < n - 1; x++) {
            for (int y = 0; y < n - x - 1; y++) {
                if (array[y] > array[y + 1]) {
                    int tempvar = array[y];
                    array[y] = array[y + 1];
                    array[y + 1] = tempvar;
                    swapbs++;
                }
                vglbs++;
            }
        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        bubbleSortTime.add(time);
        startTime = 0;
        endTime = 0;
        time = 0;

    }

    public static void makeArrays() {
        Random rndm = new Random();
        for (int i = 0; i < bubbleArray.length - 1; i++) {
            int a = rndm.nextInt(1000);
            bubbleArray[i] = a;
            selectionS[i] = a;
            stableselectionS[i] = a;
            insertionArray[i]=a;
            qsArray[i]=a;

        }

    }

    public static void selectionSort(int array[]) {
        startTime = System.nanoTime();
        int n = array.length;
        for (int x = 0; x < n - 1; x++) {
            int min = x;
            for (int y = x + 1; y < n; y++) {

                if (array[y] < array[min]) {
                    min = y;


                }
                vglss++;


            }
            int tempvar = array[min];
            if (array[min] != array[x]) {
                swapss++;
            }
            array[min] = array[x];
            array[x] = tempvar;


        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        selectionSortTime.add(time);
        startTime = 0;
        endTime = 0;
        time = 0;
    }

    static void stableSelectionSort(int[] array) {
        startTime = System.nanoTime();
        int lenght = array.length;

        for (int i = 0; i < lenght - 1; i++) {
            int min = i;
            for (int j = i + 1; j < lenght; j++) {
                if (array[min] > array[j]) {
                    swapsss++;
                    min = j;
                }
                vglsss++;
            }


            int key = array[min];
            while (min > i) {
                array[min] = array[min - 1];
                min--;
            }

            array[i] = key;

        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        stableselectionSortTime.add(time);
        startTime = 0;
        endTime = 0;
        time = 0;
    }

    static void insertionSort(int[] array) {
        startTime = System.nanoTime();

        for (int i = 0; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            vglis++;
            while (j >= 0 && array[j] > key) {
                swapis++;
                array[j + 1] = array[j];
                j = j - 1;
            }
            array[j + 1] = key;
        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        InsertionSortTime.add(time);
        startTime = 0;
        endTime = 0;
        time = 0;
    }
    static void swap(int[] arr, int i, int j)
    {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static int partition(int[] arr, int low, int high)
    {

        // pivot
        int pivot = arr[high];


        int i = (low - 1);

        for(int j = low; j <= high - 1; j++)
        {


            if (arr[j] < pivot)
            {


                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return (i + 1);
    }

    static void quickSort(int[] arr, int low, int high)
    {

        if (low < high)
        {


            int pi = partition(arr, low, high);


            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }

    }


    public static void calcavg() {

        //BubbleSortTausche
        double tempvar = 0;
        for (int i = 0; i < bubbleSortTausche.size(); i++) {
            tempvar = tempvar + bubbleSortTausche.get(i);
        }
        bbsDurchschnitttausche = tempvar / bubbleSortTausche.size();
        tempvar = 0;


        //BubbleSortVergleiche
        for (int i = 0; i < bubbleSortVergleiche.size(); i++) {
            tempvar = tempvar + bubbleSortVergleiche.get(i);
        }
        bbsDurchschnittvergl = tempvar / bubbleSortVergleiche.size();
        tempvar = 0;


        //SelectionSortVergleiche
        for (int i = 0; i < selectionSortVergleiche.size(); i++) {
            tempvar = tempvar + selectionSortVergleiche.get(i);
        }
        ssDurchschnittvergl = tempvar / selectionSortVergleiche.size();
        tempvar = 0;


        //SelectionSortTausche
        for (int i = 0; i < selectionSortTausche.size(); i++) {
            tempvar = tempvar + selectionSortTausche.get(i);
        }
        ssDurchschnitttausche = tempvar / selectionSortTausche.size();
        tempvar = 0;

        //StableSS tausche
        for (int i = 0; i < stableselectionSortTausche.size(); i++) {
            tempvar = tempvar + stableselectionSortTausche.get(i);
        }
        stablessDurchschnitttausche = tempvar / stableselectionSortTausche.size();
        tempvar = 0;

        //StableSS vergleiche
        for (int i = 0; i < stableselectionSortVergleiche.size(); i++) {
            tempvar = tempvar + stableselectionSortVergleiche.get(i);
        }
        stablessDurchschnittvergl = tempvar / stableselectionSortVergleiche.size();
        tempvar = 0;


        //Insertionsort
        for (int i = 0; i < InsertionSortVergleich.size(); i++) {
            tempvar = tempvar + InsertionSortVergleich.get(i);
        }
        insertionsortvergl = tempvar / InsertionSortVergleich.size();
        tempvar = 0;

        for (int i = 0; i < InsertionSortTausch.size(); i++) {
            tempvar = tempvar + InsertionSortTausch.get(i);
        }
        insertionsorttausche = tempvar / InsertionSortTausch.size();
        tempvar = 0;

        //Quicksort
        for(int i=0;i< QSVergleich.size();i++){
            tempvar = tempvar/QSVergleich.get(i);
        }
        qsvergleiche = tempvar/QSVergleich.size();
        tempvar=0;
        for(int i=0;i< QSTausch.size();i++){
            tempvar = tempvar/QSTausch.get(i);
        }
        qstausche = tempvar/QSTausch.size();
        tempvar=0;



        //Alle zeiten
        for (int i = 0; i < bubbleSortTime.size(); i++) {
            tempvar = tempvar + bubbleSortTime.get(i);
        }
        bbstime = (long) tempvar / bubbleSortTime.size();
        tempvar = 0;



        for (int i = 0; i < selectionSortTime.size(); i++) {
            tempvar = tempvar + selectionSortTime.get(i);
        }
        sstime = (long) tempvar / selectionSortTime.size();
        tempvar = 0;



        for (int i = 0; i < stableselectionSortTime.size(); i++) {
            tempvar = tempvar + stableselectionSortTime.get(i);
        }
        ssstime = (long) tempvar / stableselectionSortTime.size();

        tempvar = 0;

        for (int i = 0; i < InsertionSortTime.size(); i++) {
            tempvar = tempvar + InsertionSortTime.get(i);
        }
        istime = (long) tempvar / InsertionSortTime.size();

        tempvar=0;
        for(int i=0;i<QSTime.size();i++){
            tempvar=tempvar+QSTime.get(i);
        }
        qstime= (long) tempvar / QSTime.size();
        tempvar=0;




    }

    public static void clearIt() {
        vglbs = 0;
        swapbs = 0;
        vglss = 0;
        swapss = 0;
        swapsss = 0;
        vglsss = 0;
        swapis = 0;
        vglis = 0;

        qsswap=0;
        qsvgl=0;
    }
}