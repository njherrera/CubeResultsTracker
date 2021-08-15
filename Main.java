package com.company;

import org.apache.poi.util.SystemOutLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
/*	ArrayList<NightReport> test = new ArrayList<NightReport>();
	NightReport testReport = new NightReport();
	Decklist winning = new Decklist();
	Decklist losing = new Decklist();
	String[] winList = new String[23];
	winList[0] = "Preordain";
	winList[1] = "Opt";
	winList[2] = "Mind Stone";
	winList[3] = "Chart a Course";
	winList[4] = "Counterspell";
	winList[5] = "Mana Leak";
	winList[6] = "Riftwing Cloudskate";
	winList[7] = "Essence Scatter";
	winList[8] = "Dissolve";
	winList[9] = "Treasure Cruise";
	winList[10] = "Phantasmal Image";
	winList[11] = "Control Magic";
	winList[12] = "Cryptic Command";
	winList[13] = "Fact or Fiction";
	winList[14] = "Thassa's Intervention";
	winList[15] = "Jace, Architect of Thought";
	winList[16] = "Vendillion Clique";
	winList[17] = "Solemn Simulacrum";
	winList[18] = "Biogenic Ooze";
	winList[19] = "Primeval Titan";
	winList[20] = "Walking Ballista";
	winList[21] = "Shark Typhoon";
	winList[22] = "Ugin, the Ineffable";
	winning.setList(winList);
	winning.setArchetypeName("UG Control");
	testReport.setWinningDeck(winning);
	test.add(testReport);
	NightReport testReport2 = new NightReport();
	String[] winList2 = new String[5];
	winList2[0] = "Mana Leak";
	winList2[1] = "Preordain";
	winList2[2] = "Dissolve";
	winList2[3] = "Jace, the Mind Sculptor";
	winList2[4] = "Sol Ring";
	Decklist winning2 = new Decklist();
	winning2.setList(winList2);
	winning2.setArchetypeName("U Junk");
	testReport2.setWinningDeck(winning2);
	test.add(testReport2);
	NightReport testReport3 = new NightReport();
	String[] winList3 = new String[5];
	winList3[0] = "Mana Leak";
	winList3[1] = "Preordain";
	winList3[2] = "Sol Ring";
	winList3[3] = "Biogenic Ooze";
	winList3[4] = "Shark Typhoon";
	Decklist winning3 = new Decklist();
	winning3.setList(winList3);
	winning3.setArchetypeName("GU Control");
	testReport3.setWinningDeck(winning3);
	test.add(testReport3);
	ExcelWriter testWriter = new ExcelWriter();
	testWriter.createNewFile(test);*/
	Analyzer testAnalyzer = new Analyzer();
	testAnalyzer.analyzeReports("Q:/IntelliJ Projects/IdeaProjects/CubeResultsTracker/test.xls");
    }
}
