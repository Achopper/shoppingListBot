package com.example.shoping_list_tg_bot.bot;


import com.dropbox.core.*;
import com.example.shoping_list_tg_bot.ShopingListTgBotApplication;
import com.example.shoping_list_tg_bot.config.BotConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Auth {
    public String authorize(BotConfig config, DbxRequestConfig dbxConfig) throws IOException {
        DbxAppInfo info = new DbxAppInfo(config.getDropBoxApiKey(),config.getDropBoxApiSecret());
        DbxWebAuth webAuth = new DbxWebAuth(dbxConfig, info);
        String webAuthAutorize = webAuth.authorize(DbxWebAuth.newRequestBuilder().withNoRedirect().build());

        System.out.println("1. Go to " + "https://www.dropbox.com/oauth2/authorize?response_type=code&client_id=" + config.getDropBoxApiKey());
        System.out.println("2. Click \"Allow\" (you might have to log in first).");
        System.out.println("3. Copy the authorization code. Rerun bot with code as parameter");
        //System.out.print("Enter the authorization code here: ");

        String code = ShopingListTgBotApplication.CODE;// BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        try {
            DbxAuthFinish authFinish = webAuth.finishFromCode(code);
            return authFinish.getAccessToken();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
    }
}