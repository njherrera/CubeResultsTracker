package com.company;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Analyzer {

    private XSSFWorkbook workbook = new XSSFWorkbook();

    public void analyzeReports(String filePath) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.workbook = new XSSFWorkbook(inputStream); // set up a new workbook by reading the file given to the program
        XSSFSheet resultsPage = workbook.getSheetAt(0);
        Iterator<Row> it = resultsPage.iterator();
        while (it.hasNext()){ // wipe all the rows on the first sheet clean so that we can write the updated data on it
            Row row = it.next();
            it.remove();
        }
        HashMap<String, Integer> winningCardsList = new HashMap<String, Integer>(); // these are what we use to calculate the percent each card/list has been part of/the winning/losing decklist
        HashMap<String, Integer> losingCardsList = new HashMap<String, Integer>(); // the value of each item will be the number of times it has won/lost or appeared in a winning/losing list, and the key will be its name
        HashMap<String, Integer> winningDecksList = new HashMap<String, Integer>();
        HashMap<String, Integer> losingDecksList = new HashMap<String, Integer>();
        for (int i = 1; i < workbook.getNumberOfSheets(); i++) {
            readDeckNames(workbook.getSheetAt(i), winningDecksList, losingDecksList);
            readDeckLists(workbook.getSheetAt(i), winningCardsList, losingCardsList);
        }
        HashMap<String, Integer> winningCardsSorted = sortByValue(winningCardsList); // sort all the hashmaps in order of value so they're written in order of dominance when it comes time to writing down the data
        HashMap<String, Integer> losingCardsSorted = sortByValue(losingCardsList); // this way, when we pass the HashMaps as parameters in the writeData method, writeData doesn't have to do any work
        HashMap<String, Integer> winningDecksSorted = sortByValue(winningDecksList);
        HashMap<String,Integer> losingDecksSorted = sortByValue(losingDecksList);
        writeData(resultsPage, winningCardsSorted, losingCardsSorted, winningDecksSorted, losingDecksSorted);
        FileOutputStream out = new FileOutputStream(filePath);
        workbook.write(out);
        out.close();
    }

    public void readDeckNames(XSSFSheet sheet, HashMap<String, Integer> winningDecks, HashMap<String, Integer> losingDecks){
        if (sheet.getRow(0).getCell(0) != null){ // first check to see if there's actually an archetype name there
           if (winningDecks.containsKey(sheet.getRow(0).getCell(0).toString())){
               winningDecks.put(sheet.getRow(0).getCell(0).toString(), winningDecks.get(sheet.getRow(0).getCell(0).toString())+1);
               //if hashmap already has deck, increment value by 1, if not make a new entry
           } else {
               winningDecks.put(sheet.getRow(0).getCell(0).toString(),1);
           }
        }
        if (sheet.getRow(0).getCell(2) != null){ // again, checking to see if there's an archetype name there
            if (losingDecks.containsKey(sheet.getRow(0).getCell(2).toString())){
                winningDecks.put(sheet.getRow(0).getCell(2).toString(), winningDecks.get(sheet.getRow(0).getCell(2).toString())+1);
            } else {
                losingDecks.put(sheet.getRow(0).getCell(2).toString(),1);
            }
        }
    }

    public void readDeckLists(XSSFSheet sheet, HashMap<String, Integer> winningCards, HashMap<String, Integer> losingCards){
        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            if (sheet.getRow(i).getCell(0) != null){ // same thing we just did for deck names, if hashmap already has card increment, if not add a new entry
                if (winningCards.containsKey(sheet.getRow(i).getCell(0).toString())){
                    winningCards.put(sheet.getRow(i).getCell(0).toString(), winningCards.get(sheet.getRow(i).getCell(0).toString()) + 1);
                } else {
                    winningCards.put(sheet.getRow(i).getCell(0).toString(), 1);
                }
            }
            if (sheet.getRow(i).getCell(2) != null){
                if (winningCards.containsKey(sheet.getRow(i).getCell(2).toString())){
                    winningCards.put(sheet.getRow(i).getCell(2).toString(), winningCards.get(sheet.getRow(i).getCell(2).toString()) + 1);
                } else {
                    winningCards.put(sheet.getRow(i).getCell(2).toString(), 1);
                }
            }
        }
    }

    public void writeData (XSSFSheet dataSheet, HashMap<String, Integer> winningCards, HashMap<String, Integer> losingCards, HashMap<String, Integer> winningDecks, HashMap<String, Integer> losingDecks){
        Row labelRow = dataSheet.createRow(0);
        System.out.println("row created"); // create headers for columns
        labelRow.createCell(0).setCellValue("Winning decks");
        System.out.println(labelRow.getCell(0));
        labelRow.createCell(1).setCellValue("% of events won");
        labelRow.createCell(2).setCellValue("Losing decks");
        labelRow.createCell(3).setCellValue("% of events lost");
        labelRow.createCell(4).setCellValue("Winning cards");
        labelRow.createCell(5).setCellValue("% of times in winning decklist");
        labelRow.createCell(6).setCellValue("Losing cards");
        labelRow.createCell(7).setCellValue("% of times in losing decklist");
        writeWinningDecks(dataSheet, winningDecks); // pass in the sheet we're working on, as well as the HashMap of winning decks
        writeLosingDecks(dataSheet, losingDecks); // same but with losing decks
        writeWinningCards(dataSheet, winningCards, winningDecks);
        writelosingCards(dataSheet, losingCards, losingDecks);
    }

    public void writeWinningDecks(XSSFSheet dataSheet, HashMap<String, Integer> winningDecks){
        int rowMarker = 1; // starting at the first(second) row, since the 0th(first) was already used for column headers
        int numOfWinningDecks = countNumberOfWinningDecks(winningDecks);
        for (Map.Entry<String, Integer> entry : winningDecks.entrySet()) {
            Row currentRow = dataSheet.createRow(rowMarker); // since writeWinningDecks is the first of the write methods to be called, no need to check to see if a row already exists
            currentRow.createCell(0).setCellValue(entry.getKey());
            double winPercentage = (double) (entry.getValue() / (double) numOfWinningDecks); // casting everything as doubles so that we get a decimal win percentage
            currentRow.createCell(1).setCellValue(winPercentage);
            rowMarker++;
        }
    }

    public void writeLosingDecks(XSSFSheet dataSheet, HashMap<String, Integer> losingDecks){
        int rowMarker = 1;
        int numOfLosingDecks = countNumberOfLosingDecks(losingDecks);
        for (Map.Entry<String, Integer> entry : losingDecks.entrySet()) {
            if(dataSheet.getRow(rowMarker) == null) { // if there isn't a pre-existing row, make a new one and then make and fill cells in it
                Row currentRow = dataSheet.createRow(rowMarker);
                currentRow.createCell(2).setCellValue(entry.getKey());
                double lossPercentage = (double) (entry.getValue() / (double) numOfLosingDecks);
                currentRow.createCell(3).setCellValue(lossPercentage);
                rowMarker++;
            } else { // if there is a pre-existing row, just create cells in it and fill them
                dataSheet.getRow(rowMarker).createCell(2).setCellValue(entry.getKey());
                double lossPercentage = (double) (entry.getValue() / (double) numOfLosingDecks);
                dataSheet.getRow(rowMarker).createCell(3).setCellValue(lossPercentage);
                rowMarker++;
            }
        }
    }

    public void writeWinningCards(XSSFSheet dataSheet, HashMap<String, Integer> winningCards, HashMap<String, Integer> winningDecks){ //passing in winningDecks so that we can use it to calculate win% of cards
        int rowMarker = 1; //doing the same thing that we've done in writeLosingDecks, using rowMarker to track our progress through the sheet
        int numOfWinningDecks = countNumberOfWinningDecks(winningDecks);
        for (Map.Entry<String, Integer> entry : winningCards.entrySet()) {
            if(dataSheet.getRow(rowMarker) == null) {
                Row currentRow = dataSheet.createRow(rowMarker);
                currentRow.createCell(4).setCellValue(entry.getKey());
                double winPercentage = (double) (entry.getValue() / (double) numOfWinningDecks);
                currentRow.createCell(5).setCellValue(winPercentage);
                rowMarker++;
            } else {
                dataSheet.getRow(rowMarker).createCell(4).setCellValue(entry.getKey());
                double winPercentage = (double) entry.getValue() / (double) numOfWinningDecks;
                dataSheet.getRow(rowMarker).createCell(5).setCellValue(winPercentage);
                rowMarker++;
            }
        }
    }

    public void writelosingCards(XSSFSheet dataSheet, HashMap<String, Integer> losingCards, HashMap<String, Integer> losingDecks){
        int rowMarker = 1;
        int numOfLosingDecks = countNumberOfLosingDecks(losingDecks);
        for (Map.Entry<String, Integer> entry : losingCards.entrySet()) {
            if(dataSheet.getRow(rowMarker) == null) {
                Row currentRow = dataSheet.createRow(rowMarker);
                currentRow.createCell(6).setCellValue(entry.getKey());
                double lossPercentage = (double) (entry.getValue() / (double) numOfLosingDecks);
                currentRow.createCell(7).setCellValue(lossPercentage);
                rowMarker++;
            } else {
                dataSheet.getRow(rowMarker).createCell(6).setCellValue(entry.getKey());
                dataSheet.getRow(rowMarker).createCell(7).setCellValue(entry.getValue() / numOfLosingDecks);
            }
        }
    }

    public int countNumberOfWinningDecks(HashMap<String, Integer> winningDecks){ // simple method that goes through a provided HashMap
        int deckCount = 0;
        for (Map.Entry<String, Integer> deck: winningDecks.entrySet()) {
            deckCount = deckCount + deck.getValue();
            System.out.println("deck name: " + deck.getKey() + "deck value:" + deck.getValue());
            System.out.println(deckCount);
        }
        return deckCount;
    }

    public int countNumberOfLosingDecks(HashMap<String, Integer> losingDecks){
        int deckCount = 0;
        for (Map.Entry<String, Integer> deck: losingDecks.entrySet()) {
            deckCount = deckCount + deck.getValue();
        }
        return deckCount;
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    { // taken from https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
