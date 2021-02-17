package org.example;


import org.example.entity.DailyDomains;
import org.example.entity.MessageAnswered;
import org.example.entity.MessageReceived;
import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Singleton {
    private static Singleton instance;

    private SessionFactory factory;
    private ClassPathXmlApplicationContext context;

    private Singleton(){
        factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(MessageReceived.class)
                .addAnnotatedClass(MessageAnswered.class)
                .addAnnotatedClass(DailyDomains.class)
                .buildSessionFactory();
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public SessionFactory getFactory(){
        return factory;
    }

    public ClassPathXmlApplicationContext getContext(){
        return context;
    }

    public Session getSession() {
        Session session = factory.getCurrentSession().getSession();
        return session;
    }

    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}
