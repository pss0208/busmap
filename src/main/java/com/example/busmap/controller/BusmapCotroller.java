package com.example.busmap.controller;

import com.example.busmap.domain.Bus;
import com.example.busmap.domain.Info;
import com.example.busmap.repository.BusRepository;
import com.example.busmap.repository.InfoRepository;

//import org.json.JSONObject;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Controller
public class BusmapCotroller {

    @Autowired
    private BusRepository busRepository;
    @Autowired
    private InfoRepository infoRepository;

    @PostMapping(value = "/searchRtId")
    public String searchRtId(@RequestParam("busNum") String busNum, Model model){

            Bus bus = busRepository.findByBusNum(busNum);
            System.out.println(busNum);
            if (bus != null) {
                model.addAttribute("busNum",busNum);
                model.addAttribute("routeId", bus.getRouteId());
                return "map";
            } else {
                return "error";
            }
    }

    @ResponseBody
    @RequestMapping(value = "/busInformation", method = RequestMethod.GET, produces = "application/text; charset=UTF-8")
    public String busInformation(@RequestParam("data") Long routeId, Model model)
            throws IOException, SAXException, ParserConfigurationException {

        Bus bus=busRepository.findByRouteId(routeId);

        StringBuilder urlBuilder=new StringBuilder("http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid");

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8")
                + "=zMk5MK0KknH99XjaikHWP0dGVbZj7OLk2GdBuj2okFJ867rV9c9Do7tkHqDq33pij2Vc9CcWgoHoxy4eaS6NUg%3D%3D");
        urlBuilder.append("&" + URLEncoder.encode("busRouteId","UTF-8")
                + "=" + URLEncoder.encode(routeId.toString(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        sb.append("<msgBody>");

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

                    infoRepository.save(new Info(busId, gpsX, gpsY, isFull, isrun, numberPlate,bus));
                    Info info=infoRepository.findByBusId(busId);

                    sb.append(
                            "<itemList><gpsX>"+info.getGpsX()+"</gpsX><gpsY>"+info.getGpsY()+"</gpsY></itemList>"
                    );

                }
            }
        }catch(IOException e){
            System.out.print(e);
        }

        sb.append("</msgBody>");

        return sb.toString();
    }

}
