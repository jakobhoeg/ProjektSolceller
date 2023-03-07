package com.example.projektsolceller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class HelloApplication extends Application {

    Sites solceller = new Sites();

    Pane root = new Pane();

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(root, 1200, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        File file = new File("Solcelle_data.tsv");
        solceller.loadFile(file);
    }

    public static void main(String[] args) {
        launch();
    }


}


