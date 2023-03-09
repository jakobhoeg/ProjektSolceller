package com.example.projektsolceller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Site {

    private String timeDate;
    private String timeYear;
    private String timeMonth;
    private String timeDay;
    private String timeInHours;

    public String getTimeYear() {
        return timeYear;
    }

    public String getTimeMonth() {
        return timeMonth;
    }

    public String getTimeDay() {
        return timeDay;
    }

    private String _id;
    private String time;
    private String sid;
    private String total;
    private String online;
    private String offline;

    public ArrayList<Site> Data = new ArrayList<>();

    public Site(String _id, String timeYear, String timeMonth, String timeDay,
                String timeInHours, String sid, String total, String online, String offline) {
        this._id = _id;
        this.timeYear = timeYear;
        this.timeMonth = timeMonth;
        this.timeDay = timeDay;
        this.timeInHours = timeInHours;
        this.sid = sid;
        this.total = total;
        this.online = online;
        this.offline = offline;
    }

    public Site() {
    }

    public ArrayList<Site> loadFile(File file) {

        try (BufferedReader TSVReader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = TSVReader.readLine()) != null) {
                String[] values = line.split("\t");
                _id = values[0];
                timeYear = values[1];
                timeMonth = values[1];
                timeInHours = values[1];
                time = values[1];
                // For loop som går igennem hele stringen og finder T, som er det der adskiller dato og tid.
                // Gemmer herefter dato og tid i hver deres variabel, så de senere nemt kan bruges individuelt.
                for (int j = 0; j < time.length(); j++)
                {
                    if (time.charAt(j) == 'T')
                    {
                        timeYear = time.substring(0, 4);
                        timeMonth = time.substring(5, 7);
                        timeDay = time.substring(8, 10);
                        timeInHours = time.substring(j + 1, j + 6);
                    }
                }
                sid = values[2];
                total = values[3];
                online = values[4];
                offline = values[5];
                Data.add(new Site(_id,timeYear, timeMonth, timeDay, timeInHours, sid, total, online, offline));

            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return Data;
    }

    public void printDataInConsole() {
        for (int i = 0; i < Data.size(); i++) {
            System.out.println(Data.get(i).getTimeMonth());
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

    public ArrayList<Site> getData() {
        return Data;
    }
    // endregion

}

