package com.example.projektsolceller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {

    Sites solceller = new Sites();
    Pane login = new Pane();
    Pane mainScreen = new Pane();
    BorderPane borderPane = new BorderPane();
    GridPane gridPane = new GridPane();
    TextField username = new TextField("Username");
    TextField password = new TextField("Password");
    Button loginBtn = new Button("Login");

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(login, 1200, 600);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        File file = new File("Solcelle_data.tsv");
        solceller.loadFile(file);

        borderPane.setLayoutX(600);
        borderPane.setLayoutY(300);
        login.getChildren().add(borderPane);
        borderPane.setPrefHeight(200);
        borderPane.setPrefWidth(200);
        borderPane.setCenter(gridPane);
        gridPane.setPrefHeight(200);
        gridPane.setPrefWidth(200);
        gridPane.addRow(3);
        gridPane.add(username, 0,0);
        gridPane.add(password,0,1);
        gridPane.add(loginBtn,0,2);



    }

    public static void main(String[] args) {
        launch();
    }


}


