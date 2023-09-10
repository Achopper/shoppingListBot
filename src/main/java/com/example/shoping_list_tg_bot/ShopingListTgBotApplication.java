package com.example.shoping_list_tg_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopingListTgBotApplication {

    public static String CODE;
    public static void main(String[] args) {
        CODE = args[0];
        SpringApplication.run(ShopingListTgBotApplication.class, args);
    }

}
