package com.example.shoping_list_tg_bot.action;

import com.dropbox.core.DbxException;
import com.example.shoping_list_tg_bot.services.DropBoxService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.Arrays;


public class InputAction implements Action{
    private final String action;
    private final DropBoxService service;

    public InputAction(String action, DropBoxService service) {
        this.action = action;
        this.service = service;
    }

    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        String text = null;
        try {
            text = msg.getText().equals("/add") ? "Add product or products separated by comma" :
                    service.showList() + "Enter number of products, separated by comma";
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        try {
                service.modifyList(action, Arrays.asList(msg.getText().split(",", -1)));
        } catch (DbxException | IOException e) {
            return new SendMessage(chatId, "Looks like wrong input");
        }
        //var text = "Action: " + action + ", data: " + msg.getText();
        return new SendMessage(chatId, "\u263A");

    }

}