package com.example.busmap;

import com.example.busmap.domain.Bus;
import com.example.busmap.repository.BusRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
public class DBTest {

    @Autowired
    private BusRepository busRepository;

    @Test
    public void insertDB() throws IOException, CsvValidationException {
        CSVReader cr=new CSVReader(new FileReader("D:/pss/BusRouteId.csv"));
        String[] line;
        while((line=cr.readNext())!=null){
            busRepository.save(new Bus(line[0],Long.parseLong(line[1])));
        }
    }


}
