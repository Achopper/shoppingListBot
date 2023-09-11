package com.example.shoping_list_tg_bot.services;

import com.dropbox.core.DbxException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface IFileList {
    public List<String> getFile() throws DbxException, IOException;
    public void modifyList(String action, List<String> lines) throws DbxException, IOException, URISyntaxException;
    public void addToList(List<String> lines, StringBuilder newContentBuilder) throws DbxException, IOException;
    void delFromList(List<String> list, StringBuilder newContentBuilder);
    public String showList() throws IOException, DbxException;
    public void deleteList() throws IOException, DbxException, URISyntaxException;
}
