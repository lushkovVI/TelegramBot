package org.example.service;


import org.example.bot.Bot;
import org.example.Singleton;
import org.example.entity.DailyDomains;
import org.example.entity.MessageAnswered;
import org.example.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendMessagesServices {

    static final Logger log = LoggerFactory.getLogger(SendMessagesServices.class);

    public void sendMessages() throws Exception{
        Bot telegramBot = getTelegramBot();

        Long count = getDailyDomainsCount();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        String strInfo = formatter.format(calendar.getTime()) + "  Собрано доменов : " + count.toString();

        List userList = getUserList();

        for (Object tmp: userList) {
            User user = (User) tmp;
            Integer chatId = user.getUser_chat_id();
            telegramBot.sendMessage(strInfo,chatId);
            MessageAnswered message = new MessageAnswered(strInfo,new Date());
            saveMessage(message,user);
        }
        log.info("[SUCCES] trying to save MessageAnswered" + strInfo);
    }

    private static void saveMessage(MessageAnswered message, User telegramUser){
        telegramUser.setLast_message_at(message.getMessage_date_received());
        telegramUser.addMessageAnswerToUser(message);
        Session session = Singleton.getInstance().getSession();
        try{
            session.save(telegramUser);
            session.getTransaction().commit();
        }
        catch (Exception e){
            log.error("[EXCEPTION] trying to save MessageAnswered" + e.getMessage());
        }
        finally {
            session.close();
        }
    }

    private Bot getTelegramBot(){
        Bot telegramBot = Singleton.getInstance().getContext().getBean(Bot.class);
        return telegramBot;
    }

    private List<User> getUserList(){
        SessionFactory factory = Singleton.getInstance().getFactory();
        Session session = factory.getCurrentSession();

        String strQuery = "from user p";
        Query query = session.createQuery(strQuery, User.class);

        List<User> list = query.getResultList();
        return list;
    }


    private Long getDailyDomainsCount(){
        SessionFactory factory = Singleton.getInstance().getFactory();
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        Criteria crit = session.createCriteria(DailyDomains.class);
        crit.setProjection(Projections.rowCount());
        Long count = (Long)crit.uniqueResult();
        return count;
    }
}
