package com.example.busmap;

import com.example.busmap.domain.Bus;
import com.example.busmap.domain.Info;
import com.example.busmap.repository.BusRepository;
import com.example.busmap.repository.InfoRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DBTest {

    @Autowired
    private BusRepository busRepository;
    @Autowired
    private InfoRepository infoRepository;

    @Test
    public void insertDB() throws IOException, CsvValidationException {
        CSVReader cr=new CSVReader(new FileReader("C:/Users/pss/BusRouteId.csv"));
        String[] line;
        while((line=cr.readNext())!=null){
            busRepository.save(new Bus(line[0],Long.parseLong(line[1])));
        }
    }

    @Test
    public void listTest() throws IOException{
        Bus bus=busRepository.findByRouteId(Long.parseLong("100100390"));

        StringBuilder urlBuilder=new StringBuilder("http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid");

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=zMk5MK0KknH99XjaikHWP0dGVbZj7OLk2GdBuj2okFJ867rV9c9Do7tkHqDq33pij2Vc9CcWgoHoxy4eaS6NUg%3D%3D");
        urlBuilder.append("&" + URLEncoder.encode("busRouteId","UTF-8") + "=" + URLEncoder.encode("100100118", "UTF-8"));

        List<Info> infoList=new ArrayList<>();
        Info info;
        Info busInfo;

        try{
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder db=dbf.newDocumentBuilder();
            Document document=db.parse(urlBuilder.toString());
            document.getDocumentElement().normalize();
            NodeList nList=document.getElementsByTagName("itemList");
            for(int i=0;i<nList.getLength();i++){
                Node nNode=nList.item(i);
                if(nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element eElement = (Element) nNode;
                    Long busId=Long.parseLong(eElement.getElementsByTagName("vehId").item(0).getTextContent());
                    double gpsX=Double.parseDouble(eElement.getElementsByTagName("gpsX").item(0).getTextContent());
                    double gpsY=Double.parseDouble(eElement.getElementsByTagName("gpsY").item(0).getTextContent());
                    int isFull=Integer.parseInt(eElement.getElementsByTagName("isFullFlag").item(0).getTextContent());
                    int isrun=Integer.parseInt(eElement.getElementsByTagName("isrunyn").item(0).getTextContent());
                    String numberPlate=eElement.getElementsByTagName("plainNo").item(0).getTextContent();
                    infoRepository.save(new Info(busId, gpsX, gpsY, isFull, isrun, numberPlate, bus));
                    info=infoRepository.findByBusId(busId);
                    busInfo=new Info(info.getGpsX(), info.getGpsY());
                    infoList.add(busInfo);
                }
            }
        }catch(IOException | ParserConfigurationException | SAXException e){
            System.out.print(e);
        }

        for(int i=0;i<infoList.size();i++){
            System.out.println("위도 : "+infoList.get(i).getGpsY()+", "+"경도 : "+infoList.get(i).getGpsX());
        }
    }


}
