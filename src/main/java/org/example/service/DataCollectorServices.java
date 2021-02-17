package org.example.service;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.Singleton;
import org.example.entity.DailyDomains;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class DataCollectorServices extends Thread{

    static final Logger log = LoggerFactory.getLogger(DataCollectorServices.class);

    private String urlString = "https://backorder.ru/json/?order=desc&ext=1&judicial=1&by=hotness&page=1&items=50";
    private String fileString = "E:\\telegrambot\\telegrambot\\answer.json";

    private SendMessagesServices sendMessagesServices;

    public DataCollectorServices(){
        sendMessagesServices = new SendMessagesServices();
    }


    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        int hour = Integer.parseInt(dateFormat.format(date));

        while(true)
        {
            if(hour == 18 ){

                try {
                    deleteFromDailyDomains();
                    downloadJson();
                    getDataFromJson();
                    sendMessagesServices.sendMessages();
                }
                catch (IOException e) {
                    log.error("[EXCEPTION] trying to download JSON" + e.getMessage());
                } catch (Exception e) {
                    log.error("[EXCEPTION] trying  collect data  " + e.getMessage());
                } finally {

                    try {
                        Thread.sleep(3_600_000);
                    } catch (InterruptedException e) {
                        log.error("[EXCEPTION] " + e.getMessage());
                    }
                }
            }
        }
    }

    private void deleteFromDailyDomains(){
        SessionFactory factory = Singleton.getInstance().getFactory();
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        String hql = String.format("DELETE from daily_domains");
        Query query = session.createQuery(hql);
        query.executeUpdate();

        session.getTransaction().commit();
        session.close();

        log.info("[SUCCES] delete DailyDomains");
    }

    private void downloadJson() throws IOException {
        URL url = new URL(urlString);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(fileString);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();

        log.info("[SUCCES] download JSON");
    }

    private void getDataFromJson() {
        SessionFactory factory = Singleton.getInstance().getFactory();
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        JsonParser parser = new JsonParser();
        FileReader fr;

        try {
            fr = new FileReader(fileString);
            JsonElement jsonElement = parser.parse(fr);
            JsonArray jsonArray = jsonElement.getAsJsonArray();

            Iterator iterator = jsonArray.iterator();
            while (iterator.hasNext()) {

                JsonObject obj = (JsonObject) iterator.next();
                JsonObject domainObjext = obj.getAsJsonObject();

                String name = domainObjext.get("domainname").getAsString();
                String price = domainObjext.get("price").getAsString();

                DailyDomains dailyDomains = new DailyDomains(name , Long.parseLong(price));
                session.save(dailyDomains);
            }
            log.info("[SUCCES] get Data from JSON");
        }
        catch (Exception e){
            log.error("[EXCEPTION] trying to getDataFromJson " + e.getMessage());
        }
        finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
