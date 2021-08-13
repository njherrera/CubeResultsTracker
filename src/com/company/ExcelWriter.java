package com.company;


import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

public class ExcelWriter {

    private XSSFWorkbook workbook = new XSSFWorkbook();

    public void makeWorkbook(){
        this.workbook = new XSSFWorkbook();
    }

    public void createNewFile(ArrayList<NightReport> deckLists) {
        int sheetCountTracker = 0;
        makeWorkbook();
        XSSFSheet dataPage = this.workbook.createSheet("Metagame breakdown");
        for (NightReport night : deckLists){ // going through all the decklists and formatting the color identity
            if (night.hasBothLists()){
                night.getWinningDeck().rearrangeColorIdentity();
                night.getLosingDeck().rearrangeColorIdentity();
            } else if (night.getWinningDeck() != null){
                night.getWinningDeck().rearrangeColorIdentity();
            } else if (night.getLosingDeck() != null){
                night.getLosingDeck().rearrangeColorIdentity();
            }
        }
        for (NightReport report : deckLists) {
            if (report.hasBothLists() == true) { // if the report has two lists (one winning and one losing), we call writePageBothLists
                writePageBothLists(report, sheetCountTracker); //sheetCounterTracker is what we use to generate names for each sheet i.e. sheet1 sheet2 sheet3
            }
            if(report.getLosingDeck() == null){ // if there's only a winning list provided, use the method for writing a page using only a winning list
                writePageWinningList(report, sheetCountTracker);
            }
            if(report.getWinningDeck() == null){ // if there's only a losing list provided, use the method for writing a page using only a losing list
                writePageLosingList(report, sheetCountTracker);
            }
            sheetCountTracker++; // after writing a new cube session report page, increment sheetCountTracker by one
        }
        try (FileOutputStream fileOut = new FileOutputStream("test.xls")){
            this.workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writePageBothLists(NightReport report, int  sheetCount){
        XSSFSheet winningLosingLists = this.workbook.createSheet("Cube session report" + sheetCount); // format is "Cube session reportsheetCount", with sheetCount starting at 0
        // i.e. Cube session report0, Cube session report 1, etc.
        Row nameRow = winningLosingLists.createRow(0); // writing the headers of the sheet, the names of the winning and losing decks
        Cell winningListTitle = nameRow.createCell(0);
        winningListTitle.setCellValue(report.getBothLists().get(0).getArchetypeName());
        Cell losingListTitle = nameRow.createCell(2);
        losingListTitle.setCellValue(report.getBothLists().get(1).getArchetypeName());
        for (int i = 0; i < report.getLargestListSize(); i++) {
            if (report.getBothLists().get(0).getList()[i] != null && report.getBothLists().get(1).getList()[i] != null){ // if there's a card available from both lists, write out both card names
                Row newRow = winningLosingLists.createRow(i+1);
                Cell winningListCard = newRow.createCell(0);
                winningListCard.setCellValue(report.getWinningDeck().getList()[i]);
                Cell losingListCard = newRow.createCell(2);
                losingListCard.setCellValue(report.getLosingDeck().getList()[i]);
            }
            if (report.getBothLists().get(0).getList()[i] != null && report.getBothLists().get(1).getList()[i] == null){ // if there's only a card from the winning list, write the card name in the winning list column
                Row newRow = winningLosingLists.createRow(i+1); // if one list has more cards than the other, the method will keep writing cards down on the excel sheet until both lists are exhausted
                Cell winningListCard = newRow.createCell(0);
                winningListCard.setCellValue(report.getWinningDeck().getList()[i]);
            }
            if (report.getBothLists().get(0).getList()[i] == null && report.getBothLists().get(1).getList()[i] != null){ // if there's only a card from the losing list, write the card name in the losing list column
                Row newRow = winningLosingLists.createRow(i+1);
                Cell losingListCard = newRow.createCell(2);
                losingListCard.setCellValue(report.getLosingDeck().getList()[i]);
            }
        }
    }

    public void writePageWinningList(NightReport report, int sheetCount){
        XSSFSheet winningLists = this.workbook.createSheet("Cube session report" + sheetCount);
        Row nameRow = winningLists.createRow(0); // if we only have a winning list to write down cards from, things get a lot simpler
        Cell winningListTitle = nameRow.createCell(0);
        winningListTitle.setCellValue(report.getWinningDeck().getArchetypeName());
        for (int i = 0; i < report.getWinningDeck().getListSize(); i++) { // for each card in the winning deck list, write it down on the excel sheet
            Row newRow = winningLists.createRow(i+1);
            Cell winningListCard = newRow.createCell(0);
            winningListCard.setCellValue(report.getWinningDeck().getList()[i]);
        }
    }

    public void writePageLosingList(NightReport report, int sheetCount){
        XSSFSheet losingLists = this.workbook.createSheet("Cube session report" + sheetCount);
        Row nameRow = losingLists.createRow(0);
        Cell losingListTitle = nameRow.createCell(0);
        losingListTitle.setCellValue(report.getWinningDeck().getArchetypeName());
        for (int i = 0; i < report.getLosingDeck().getListSize(); i++) { // for each card in the losing deck list, write it down on the excel sheet
            Row newRow = losingLists.createRow(i+1);
            Cell losingListCard = newRow.createCell(0);
            losingListCard.setCellValue(report.getLosingDeck().getList()[i]);
        }
    }

    public void writeOnExistingFile(String filePath, ArrayList<NightReport> deckLists) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.workbook = new XSSFWorkbook(inputStream);
        int reportCounter = 0;
        int sheetCountTracker = this.workbook.getNumberOfSheets() - 1;
        System.out.println(this.workbook.getNumberOfSheets());
        int sheetTracker = this.workbook.getNumberOfSheets() - 1;
        for (int z = 0; z <deckLists.size(); z++){ // very similar to the method for creating a new file, except this time we figure out how many sheets are in the file and then use that for the sheetCountTracker variable instead of starting from 0
            if (deckLists.get(z).hasBothLists() == true) {
                writePageBothLists(deckLists.get(z), sheetCountTracker);
            }
            if(deckLists.get(z).getLosingDeck() == null){
                writePageWinningList(deckLists.get(z), sheetCountTracker);
            }
            if(deckLists.get(z).getWinningDeck() == null){
                writePageLosingList(deckLists.get(z), sheetCountTracker);
            }
            sheetCountTracker++;
        }
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            this.workbook.write(out);
            out.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
    }
}


