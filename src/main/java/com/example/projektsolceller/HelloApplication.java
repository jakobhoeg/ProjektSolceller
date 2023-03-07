package com.example.projektsolceller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

        borderPane.setLayoutX(525);
        borderPane.setLayoutY(250);
        login.getChildren().add(borderPane);
        borderPane.setPrefHeight(300);
        borderPane.setPrefWidth(300);
        borderPane.setCenter(gridPane);
        gridPane.setPrefHeight(200);
        gridPane.setPrefWidth(200);
        gridPane.addRow(3);
        gridPane.add(username, 0,0);
        gridPane.add(password,0,1);
        gridPane.add(loginBtn,0,2);
        loginBtn.setOnAction(e -> {
            if (username.getText().equals("admin") && password.getText().equals("admin")) {
                Scene scene2 = new Scene(mainScreen, 1200, 600);
                stage.setScene(scene2);
            }
            else {
                Label error = new Label("Wrong username or password");
                gridPane.add(error, 0, 3);
            }
        });



    }

    public static void main(String[] args) {
        launch();
    }


}


