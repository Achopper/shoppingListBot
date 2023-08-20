package com.example.shoping_list_tg_bot.bot;

import com.example.shoping_list_tg_bot.action.*;
import com.example.shoping_list_tg_bot.config.BotConfig;
import com.example.shoping_list_tg_bot.services.DropBoxService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final DropBoxService service;
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private  Map<String, Action> actions;

    public TelegramBot(BotConfig botConfig, DropBoxService service) {
        this.botConfig = botConfig;
        this.service = service;
        this.actions = Map.of(
                "/start", new InfoAction(
                        List.of(
                                "/start - Whow commands",
                                "/add - Add products",
                                "/del - delete products",
                                "/showlist - show product list",
                                "/deletelist - delete list")
                ),
                "/showlist", new ShowListAction("/showlist", service),
                "/add", new InputAction("/add", service),
                "/del", new InputAction("/del", service),
                "/deletelist", new DeleteListAction("/deletelist", service));
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();

            if (update.hasMessage()) {
                var key = update.getMessage().getText();
                var chatId = update.getMessage().getChatId().toString();
                if (actions.containsKey(key)) {
                    var msg = actions.get(key).handle(update);
                    bindingBy.put(chatId, key);
                    send(msg);
                } else if (bindingBy.containsKey(chatId)) {
                    var msg = actions.get(bindingBy.get(chatId)).callback(update);
                    bindingBy.remove(chatId);
                    send(msg);
                }
            }
        }
    }


    private void send(BotApiMethod msg) {
        try {
            Serializable execute = execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}