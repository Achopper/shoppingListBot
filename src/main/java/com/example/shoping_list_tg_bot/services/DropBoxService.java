package com.example.shoping_list_tg_bot.services;


import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import com.example.shoping_list_tg_bot.bot.Auth;
import com.example.shoping_list_tg_bot.config.BotConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



@Component
public class DropBoxService {
    private DbxClientV2 client;
    private final BotConfig config;
    private final String FILE = "/List";

    public DropBoxService(BotConfig config) {
        this.config = config;
    }

    private List<String> getFile() throws DbxException, IOException {
        List<String> content = new ArrayList<>();
        InputStream in = client.files().download(FILE).getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null)
            content.add(line);
        return content;
    }

    public void modifyList(String action, List<String> lines) throws DbxException, IOException {
        DbxDownloader<FileMetadata> downloader = client.files().download(FILE);
        String curContent = new BufferedReader(new InputStreamReader(downloader.getInputStream())).lines().
                reduce("", (content, line) -> content + line + "\n");
        StringBuilder newContentBuilder = new StringBuilder(curContent);
        if (action.equals("/add")) {
            addToList(lines, newContentBuilder);
        }else if (action.equals("/del")) {
            delFromList(lines, newContentBuilder);
        }else if (action.equals("/deleteList")){
            newContentBuilder.delete(0,newContentBuilder.length()).append("Список\n");
        }
        ByteArrayInputStream in = new ByteArrayInputStream(newContentBuilder.toString().getBytes());
        FileMetadata updatedMetaData = client.files().uploadBuilder(FILE)
                .withMode(WriteMode.OVERWRITE)
                .uploadAndFinish(in);
    }
    private void addToList(List<String> lines, StringBuilder newContentBuilder) throws DbxException, IOException {
        for (var line : lines){
            newContentBuilder.append(line.trim()).append("\n");
        }
    }

    private void delFromList(List<String> list, StringBuilder newContentBuilder) {
        String[] separatedContent = newContentBuilder.toString().split(System.getProperty("line.separator"));
        newContentBuilder.delete(0,newContentBuilder.length());
        for (int i = 0; i < separatedContent.length; ++i) {
            if (!list.contains(Integer.toString(i))) {
                newContentBuilder.append(separatedContent[i]).append("\n");
            }
        }
    }

    public String showList() throws IOException, DbxException {
        StringBuilder res = new StringBuilder();
        List<String> content = getFile();
        int coutnter = 0;
        for (var line : content) {
            if (coutnter > 0) {
                res.append(coutnter).append(") ").append(line).append("\n");
            }
            coutnter++;
        }
        if (res.isEmpty())
            res.append("List is empty");
        return res.toString();
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        DbxRequestConfig dropBoxConfig = DbxRequestConfig.newBuilder("dropbox/shopping-list").build();
        client = new DbxClientV2(dropBoxConfig, new Auth().authorize(config,dropBoxConfig));
    }

    public void deleteList() throws IOException, DbxException {
        modifyList("/deleteList", null);
    }
}