package com.company;

import org.apache.poi.util.SystemOutLogger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
	Analyzer testAnalyzer = new Analyzer();
	testAnalyzer.analyzeReports("Q:/assorted chicanery/lapsoTracker.xlsx");
    }
}
