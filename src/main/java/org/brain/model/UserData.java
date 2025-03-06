package org.brain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserData {

    private List<Folder> folders;

    public UserData() {
        this.folders = new ArrayList<>();
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public void addFolder(Folder folder) {
        folders.add(folder);
    }
}