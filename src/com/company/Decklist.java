package com.company;

import java.util.Arrays;

public class Decklist {

    private String[] list;
    private String ArchetypeName;
    private String colors;

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public String getArchetypeName() {
        return ArchetypeName;
    }

    public void setArchetypeName(String archetypeName) {
        ArchetypeName = archetypeName;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public int getListSize(){
        return list.length;
    }

    public void rearrangeColorIdentity(){
        String[] nameArray = this.ArchetypeName.split(" ");
        char[] colorIdentity = nameArray[0].toCharArray();
        Arrays.sort(colorIdentity);
        String restOfName = "";
        for (int i = 1; i < nameArray.length; i++) {
            restOfName.concat(" " + nameArray[i]);
        }
        String finalName = new String(colorIdentity) + " " + restOfName;
        this.ArchetypeName = finalName;
    }
}
