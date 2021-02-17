package org.example.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "user")
@Table(name = "user",schema="hbt")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "user_name")
    private String user_name;

    @Column(name = "last_message_at")
    private Date last_message_at;

    @Column(name = "user_chat_id")
    private Integer user_chat_id;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    List<MessageReceived> messageReceivedList;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "answeredUser")
    List<MessageAnswered> messageAnsweredList;

    public User(){

    }

    public User(String user_name, Date last_message_at, Integer user_chat_id) {
        this.user_name = user_name;
        this.last_message_at = last_message_at;
        this.user_chat_id = user_chat_id;
    }

    public void addMessageToUser(MessageReceived messageReceived){
        if(messageReceivedList == null){
            messageReceivedList = new LinkedList<>();
        }
        messageReceivedList.add(messageReceived);
        messageReceived.setUser(this);
    }

    public void addMessageAnswerToUser(MessageAnswered messageAnswered){
        if(messageAnsweredList == null){
            messageAnsweredList = new LinkedList<>();
        }
        messageAnsweredList.add(messageAnswered);
        messageAnswered.setUser(this);
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Date getLast_message_at() {
        return last_message_at;
    }

    public void setLast_message_at(Date last_message_at) {
        this.last_message_at = last_message_at;
    }

    public Integer getUser_chat_id() {
        return user_chat_id;
    }

    public void setUser_chat_id(Integer user_chat_id) {
        this.user_chat_id = user_chat_id;
    }

    public List<MessageReceived> getMessageReceivedList() {
        return messageReceivedList;
    }

    public void setMessageReceivedList(List<MessageReceived> messageReceivedList) {
        this.messageReceivedList = messageReceivedList;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_name='" + user_name + '\'' +
                ", last_message_at=" + last_message_at +
                ", user_chat_id=" + user_chat_id +
                '}';
    }
}

