package com.example.busmap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Test {

    public static void main(String[] args) throws IOException {
        StringBuilder urlBuilder=new StringBuilder("http://ws.bus.go.kr/api/rest/buspos/getLowBusPosByRtid");

        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=zMk5MK0KknH99XjaikHWP0dGVbZj7OLk2GdBuj2okFJ867rV9c9Do7tkHqDq33pij2Vc9CcWgoHoxy4eaS6NUg%3D%3D");
        urlBuilder.append("&" + URLEncoder.encode("busRouteId","UTF-8") + "=" + URLEncoder.encode("100100118", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml; charset=UTF-8");


        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString().indexOf("gpsX"));
        System.out.println(sb.toString().indexOf("gpsY"));
        System.out.println(sb.toString());

//        sb.toString().
    }

}
