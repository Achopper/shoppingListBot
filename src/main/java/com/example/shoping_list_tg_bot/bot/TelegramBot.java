package com.example.shoping_list_tg_bot.bot;

import com.example.shoping_list_tg_bot.action.*;
import com.example.shoping_list_tg_bot.config.BotConfig;
import com.example.shoping_list_tg_bot.model.BotUser;
import com.example.shoping_list_tg_bot.services.IFileList;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final IFileList service;
    private final Map<String, String> bindingBy = new ConcurrentHashMap<>();
    private  Map<String, Action> actions;
    private ArrayList<BotUser> activeUsers;

    public TelegramBot(BotConfig botConfig, IFileList service) {
        this.botConfig = botConfig;
        this.service = service;
        this.actions = Map.of(
                "/start", new InfoAction(
                        List.of(
                                "/start - Show commands",
                                "/add - Add products",
                                "/del - Delete products",
                                "/showlist - Show product list",
                                "/deletelist - Delete list")
                ),
                "/showlist", new ShowListAction("/showlist", service),
                "/add", new InputAction("/add", service),
                "/del", new InputAction("/del", service),
                "/deletelist", new DeleteListAction("/deletelist", service));
        this.activeUsers = new ArrayList<>();
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

            if (update.hasMessage()) {
                var key = update.getMessage().getText();
                var chatId = update.getMessage().getChatId();
                if (activeUsers.stream().noneMatch(botUser -> Objects.equals(botUser.getChatID(), chatId)))
                    activeUsers.add(new BotUser(chatId, update.getMessage().getFrom().getUserName()));
                if (actions.containsKey(key)) {
                    var msg = actions.get(key).handle(update);
                    bindingBy.put(chatId.toString(), key);
                    send(msg);
                } else if (bindingBy.containsKey(chatId.toString())) {
                    var msg = actions.get(bindingBy.get(chatId.toString())).callback(update);
                    boolean needBroadcats = actions.get(bindingBy.get(chatId.toString())).getAction().equals("/add");
                    bindingBy.remove(chatId.toString());
                    send(msg);
                    if (needBroadcats)
                        sendNoticeToAll(chatId, update.getMessage().getFrom().getUserName());
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
    private void sendNoticeToAll(Long chatId, String userName)
    {
        try {
            for (BotUser user : activeUsers) {
                if (!Objects.equals(user.getChatID(), chatId))
                {
                    String msg = userName + " updated shopping list";
                    Serializable exec = execute(new SendMessage(user.getChatID().toString(), msg));
                }
            }
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}