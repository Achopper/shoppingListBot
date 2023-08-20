package com.example.shoping_list_tg_bot.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@Getter
@PropertySource("classpath:application.properties")
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String botToken;
    @Value("${dropBox.apikey}")
    String dropBoxApiKey;
    @Value("${dropBox.apiSecret}")
    String dropBoxApiSecret;
    @Value("${dropBox.token}")
    String dropBoxToken;
}