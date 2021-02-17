package org.example.bot;

import org.example.service.ReceiveMessagesServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    @Value("telegramTestTaskBot")
    private String botUsername;
    @Value("1602521338:AAHN5VRLlQz5hOPAho9TVxKD52_pDDpfOCE")
    private String botToken;

    static final Logger log = LoggerFactory.getLogger(Bot.class);

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void sendMessage(String text,Integer chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        execute(sendMessage);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            System.out.println(update.toString());
            ReceiveMessagesServices.receiveMessage(update);

        }
        catch (Exception e){
            log.error("[EXCEPTION] trying to send Message" + e.getMessage());
        }
    }
}
