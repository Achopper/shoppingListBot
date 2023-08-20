package com.example.shoping_list_tg_bot.action;

import com.dropbox.core.DbxException;
import com.example.shoping_list_tg_bot.services.DropBoxService;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

@AllArgsConstructor
public class ShowListAction implements Action{
    private final String action;
    private final DropBoxService service;

    @Override
    public BotApiMethod handle(Update update) {
        var msg = update.getMessage();
        var chatId = msg.getChatId().toString();
        String text = null;
        try {
            text = service.showList();
        } catch (IOException | DbxException e) {
            throw new RuntimeException(e);
        }
        return new SendMessage(chatId, text);
    }

    @Override
    public BotApiMethod callback(Update update) {
        return null;
    }


}
