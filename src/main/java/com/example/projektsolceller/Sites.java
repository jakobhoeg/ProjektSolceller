package com.example.projektsolceller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

    public class Sites {

    private String timeDate;
    private String timeInHours;
    private String _id;
    private String time;
    private String sid;
    private String total;
    private String online;
    private String offline;

    public ArrayList<com.example.projektsolceller.Sites> Data = new ArrayList<>();

    public Sites(String _id, String timeDate, String timeInHours, String sid, String total, String online, String offline) {
        this._id = _id;
        this.timeDate = timeDate;
        this.timeInHours = timeInHours;
        this.sid = sid;
        this.total = total;
        this.online = online;
        this.offline = offline;
    }

    public Sites() {
    }

    public ArrayList<com.example.projektsolceller.Sites> loadFile(File file) {

        try (BufferedReader TSVReader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = TSVReader.readLine()) != null) {
                String[] values = line.split("\t");
                _id = values[0];
                timeDate = values[1];
                timeInHours = values[1];
                time = values[1];
                // For loop som går igennem hele stringen og finder T, som er det der adskiller dato og tid.
                // Gemmer herefter dato og tid i hver deres variabel, så de senere nemt kan bruges individuelt.
                for (int j = 0; j < time.length(); j++)
                {
                    if (time.charAt(j) == 'T')
                    {
                        timeDate = time.substring(0, j);
                        timeInHours = time.substring(j + 1);
                    }
                }
                sid = values[2];
                total = values[3];
                online = values[4];
                offline = values[5];
                Data.add(new com.example.projektsolceller.Sites(_id, timeDate, timeInHours, sid, total, online, offline));

            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return Data;
    }

    public void printDataInConsole() {
        for (int i = 0; i < Data.size(); i++) {
            System.out.println(Data.get(i).get_id() + "\t" + Data.get(i).getTimeDate() + "\t"
                    + Data.get(i).getTimeInHours() + "\t" + Data.get(i).getSid() + "\t"
                    + Data.get(i).getTotal() + "\t" + Data.get(i).getOnline() + "\t" + Data.get(i).getOffline());
        }
    }

    // region Getters and Setters
    public String getTimeDate() {
        return timeDate;
    }

    public String getTimeInHours() {
        return timeInHours;
    }

    public String get_id() {
        return _id;
    }

    public String getTime() {
        return time;
    }

    public String getSid() {
        return sid;
    }

    public String getTotal() {
        return total;
    }

    public String getOnline() {
        return online;
    }

    public String getOffline() {
        return offline;
    }

    public ArrayList<com.example.projektsolceller.Sites> getData() {
        return Data;
    }
    // endregion

}

