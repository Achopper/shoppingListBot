package com.example.shoping_list_tg_bot.action;

import com.dropbox.core.DbxException;
import com.example.shoping_list_tg_bot.services.IFileList;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.net.URISyntaxException;

@AllArgsConstructor
public class DeleteListAction implements Action{
    private final String action;
    private final IFileList service;
    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        try {
           service.deleteList();
        } catch (IOException | DbxException | URISyntaxException e) {
             return new SendMessage(chatId, "cant't delete list, try again");
        }
        return new SendMessage(chatId, "Shopping list is cleared");
    }

    @Override
    public BotApiMethod callback(Update update) {
        return null;
    }

    @Override
    public String getAction() {
        return action;
    }
}
