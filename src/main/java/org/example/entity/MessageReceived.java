package org.example.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "message_received")
@Table(name = "message_received",schema="hbt")
public class MessageReceived {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer message_id;

    @Column(name = "message_text")
    private String message_text;

    @Column(name = "message_date_received")
    private Date message_date_received;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private User user;

    public MessageReceived(){

    }

    public MessageReceived(String message_text, Date message_date_received) {
        this.message_text = message_text;
        this.message_date_received = message_date_received;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public Date getMessage_date_received() {
        return message_date_received;
    }

    public void setMessage_date_received(Date message_date_received) {
        this.message_date_received = message_date_received;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "MessageReceived{" +
                "message_text='" + message_text + '\'' +
                ", message_date_received=" + message_date_received +
                '}';
    }
}
