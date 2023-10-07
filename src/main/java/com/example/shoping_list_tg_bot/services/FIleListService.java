package com.example.shoping_list_tg_bot.services;

import com.dropbox.core.DbxException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Component
public class FIleListService implements IFileList{
    @Override
    public List<String> getFile() {
        List<String> ret = new ArrayList<>();
        InputStream is = getClass().getClassLoader().getResourceAsStream("FileList");
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    ret.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    @Override
    public void modifyList(String action, List<String> lines) throws URISyntaxException, IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("FileList");
        String curContent = new BufferedReader(new InputStreamReader(is)).lines().
                reduce("", (content, line) -> content + line + "\n");
        StringBuilder newContentBuilder = new StringBuilder(curContent);
        if (action.equals("/add")) {
            addToList(lines, newContentBuilder);
        }else if (action.equals("/del")) {
            delFromList(lines, newContentBuilder);
        }else if (action.equals("/deleteList")){
            newContentBuilder.delete(0,newContentBuilder.length()).append("List\n");
        }
        Path resPath = Path.of(getClass().getClassLoader().getResource("FileList").toURI());
        Files.writeString(resPath, newContentBuilder.toString(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void addToList(List<String> lines, StringBuilder newContentBuilder) {
        for (var line : lines){
            newContentBuilder.append(StringUtils.capitalize(line.trim())).append("\n");
        }
    }

    @Override
    public void delFromList(List<String> list, StringBuilder newContentBuilder) {
        String[] separatedContent = newContentBuilder.toString().split(System.getProperty("line.separator"));
        newContentBuilder.delete(0,newContentBuilder.length());
        for (int i = 0; i < separatedContent.length; ++i) {
            if (!list.contains(Integer.toString(i))) {
                newContentBuilder.append(separatedContent[i]).append("\n");
            }
        }
    }

    @Override
    public String showList() {
        StringBuilder res = new StringBuilder();
        List<String> content = getFile();
        int counter = 0;
        for (var line : content) {
            if (counter > 0) {
                res.append(counter).append(") ").append(line).append("\n");
            }
            counter++;
        }
        if (res.isEmpty())
            res.append("List is empty");
        return res.toString();
    }

    @Override
    public void deleteList() throws IOException, DbxException, URISyntaxException {
        modifyList("/deleteList", null);
    }
}
