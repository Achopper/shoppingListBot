package com.example.shoping_list_tg_bot.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BotUser {
    private Long   ChatID;
    private String UserName;
}
