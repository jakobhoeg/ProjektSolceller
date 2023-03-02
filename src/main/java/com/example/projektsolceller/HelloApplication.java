package com.example.projektsolceller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        File file = new File("Solcelle_data.tsv");
        loadFile(file);
    }

    public static void main(String[] args) {
        launch();
    }


    public static ArrayList<String[]> loadFile(File file) {
        ArrayList<String[]> Data = new ArrayList<>();
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(file))) {
            String line = null;
            while ((line = TSVReader.readLine()) != null) {
                String[] values = line.split("\t");
                String _id = values[0];
                String time = values[1];
                String sid = values[2];
                String total = values[3];
                String online = values[4];
                String offline = values[5];

                System.out.println("_id: " + _id + ", Time: " + time + ", sid: " + sid + ", total: "
                        + total + ", online: " + online + ", offline: " + offline);

            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return Data;
    }

}


