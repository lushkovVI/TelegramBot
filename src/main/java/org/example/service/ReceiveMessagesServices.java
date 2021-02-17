package org.example.service;


import org.example.Singleton;
import org.example.entity.MessageReceived;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.Query;
import java.util.Date;

public class ReceiveMessagesServices {
    static final Logger log = LoggerFactory.getLogger(ReceiveMessagesServices.class);

    public ReceiveMessagesServices(){

    }


    public static void receiveMessage(Update update){
        Integer chatId = Math.toIntExact(update.getMessage().getChatId());

        if(chatId == null){
            return;
        }
        SessionFactory factory = Singleton.getInstance().getFactory();
        Session session = factory.getCurrentSession();

        String strQuery = "from user where user_chat_id = :user_chat_id";
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("from user where user_chat_id = :user_chat_id", User.class);
        query.setParameter("user_chat_id", chatId);
        User telegramUser;
        try{
            telegramUser = (User) query.getSingleResult();
        }
        catch (Exception e){
            telegramUser = null;
            log.info("[EXCEPTION] trying to init query" + e.getMessage());
        }

        if(telegramUser == null){
            MessageReceived message = createMessage(update);

            String name = update.getMessage().getFrom().getUserName();
            Integer unixTime = update.getMessage().getDate();
            Date date = new Date(unixTime*1000L);

            saveUser(message,name,date,chatId);
        }
        else{
            MessageReceived message = createMessage(update);

            saveMessage(message,telegramUser);
        }
    }

    private static MessageReceived createMessage(Update update){
        String text = update.getMessage().getText();
        Integer unixTime = update.getMessage().getDate();
        Date date = new Date(unixTime*1000L);

        MessageReceived message = new MessageReceived(text,date);
        return message;
    }

    private static void saveMessage(MessageReceived message,User telegramUser){
        telegramUser.setLast_message_at(message.getMessage_date_received());
        telegramUser.addMessageToUser(message);
        Session session = Singleton.getInstance().getSession();
        try{
            session.save(telegramUser);
            session.getTransaction().commit();
            log.info("[SUCCES] save a MessageReceived");
        }
        catch (Exception e){
            log.info("[EXCEPTION] trying to save MessageReceived" + e.getMessage());
        }
        finally {
            session.close();
        }
    }

    private static void saveUser(MessageReceived message, String name, Date date, Integer chatId){
        SessionFactory factory = Singleton.getInstance().getFactory();
        Session session = factory.getCurrentSession();

        User user = new User(name,date,chatId);
        user.addMessageToUser(message);
        try{
            session.save(user);
            session.getTransaction().commit();
            log.info("[SUCCES] save a User");
        }
        catch (Exception e){
            log.info("[EXCEPTION] trying to save User" + e.getMessage());
        }
        finally {
            session.close();
        }
    }



}
