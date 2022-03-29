package com.company;

import java.util.Arrays;

public class Decklist {

    private String[] list;
    private String ArchetypeName;
    private String colors;
    boolean areTwoDecks; // for if there's a tie and no conclusive winning deck
    String firstDeckName;
    String secondDeckName;

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

    public String getFirstDeckName(){return this.firstDeckName;}

    public String getSecondDeckName(){return this.secondDeckName; }

    public void rearrangeColorIdentity(){ // first half of method handles two decks, second half handles one deck
        if (this.ArchetypeName.contains("/")){
            areTwoDecks = true;
            String[] names = ArchetypeName.split("/");
            for (int i = 0; i < names.length; i++){
                String[] nameArray = names[i].split(" ");
                char[] colorIdentity = nameArray[0].toCharArray();
                Arrays.sort(colorIdentity);
//                String restOfName = "";
//                for (int j = 1; j < nameArray.length; j++) {
//                    restOfName.concat(" " + nameArray[j]);
//                }
                String finalName = new String(colorIdentity);
                if (i == 0){
                    firstDeckName = finalName;
                } else if (i == 1){
                    secondDeckName = finalName;
                }
            }
            this.ArchetypeName = firstDeckName + ", " + secondDeckName;
        } else {
            String[] nameArray = this.ArchetypeName.split(" ");
            char[] colorIdentity = nameArray[0].toCharArray();
            Arrays.sort(colorIdentity);
//            String restOfName = "";
//            for (int i = 1; i < nameArray.length; i++) {
//                restOfName.concat(" " + nameArray[i]);
//            }
            String finalName = new String(colorIdentity);
            this.ArchetypeName = finalName;
        }
    }
}
