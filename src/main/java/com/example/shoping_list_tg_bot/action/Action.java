package com.example.shoping_list_tg_bot.action;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Action {
    BotApiMethod handle(Update update);

    BotApiMethod callback(Update update);

    String getAction();
}