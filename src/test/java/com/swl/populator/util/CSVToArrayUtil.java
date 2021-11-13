package com.swl.populator.util;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVToArrayUtil {


    public static List<List<String>> csvToArrayList(String filename) {
        List<List<String>> records = new ArrayList<List<String>>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filename));) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }
}
