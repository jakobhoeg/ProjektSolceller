package com.example.projektsolceller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {

    Sites solceller = new Sites();

    // Login Scene
    Pane loginPane = new Pane();
    Pane mainScreen = new Pane();
    BorderPane borderPane = new BorderPane();
    File file = new File("Solcelle_data.tsv");
    GridPane gridPane = new GridPane();
    TextField username = new TextField("");
    TextField password = new TextField("");
    Button loginBtn = new Button("Login");
    Label loginLabel = new Label("Please enter username and password to login");

    Stage temporaryStage = null;
    Scene loginScene = new Scene(loginPane, 600, 500);


    // Main scene
    Scene mainScene = new Scene(mainScreen, 1200, 600);
    BorderPane mainPane = new BorderPane();
    Pane chartPane = new Pane();
    ComboBox<String> cbYear = new ComboBox<>();



    @Override
    public void start(Stage stage) throws IOException {
        temporaryStage = stage;

        solceller.loadFile(file);

        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();

        username.setPromptText("Username");
        password.setPromptText("Password");
        loginPane.requestFocus();

        borderPane.setLayoutX(225);
        borderPane.setLayoutY(200);
        loginPane.getChildren().add(borderPane);


        loginPane.getChildren().add(loginLabel);
        loginLabel.setLayoutY(140);
        loginLabel.setLayoutX(200);
        borderPane.setPrefHeight(600);
        borderPane.setPrefWidth(500);
        borderPane.setCenter(gridPane);

        gridPane.setPrefHeight(200);
        gridPane.setPrefWidth(200);
        gridPane.addRow(3);
        gridPane.setVgap(10);
        gridPane.add(username, 0,0);
        gridPane.add(password,0,1);
        gridPane.add(loginBtn,0,2);

        // Skips login
        changeToMainScene();

        password.setOnAction(actionEvent -> {
            loginBtn.fire();
        });
        loginBtn.setOnAction(e -> {
            if (username.getText().equals("m") && password.getText().equals("m")) {
                changeToMainScene();

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

    public void changeToMainScene()
    {
        temporaryStage.setTitle("Aarhus Solcelleanlæg");
        temporaryStage.setScene(mainScene);
        temporaryStage.centerOnScreen();

        mainScreen.getChildren().add(mainPane);
        mainPane.setPrefHeight(600);
        mainPane.setPrefWidth(1200);

        mainPane.setLeft(cbYear);
        mainPane.setRight(chartPane);

        for (int i = 1; i < solceller.Data.size(); i++) {
            if (!solceller.Data.get(i).getTimeDate().contains("2023"))
            {
                cbYear.getItems().add(solceller.Data.get(i).getTimeDate().substring(0,4));
            }

        }

        cbYear.getItems().add("2022");
        cbYear.getItems().add("2023");

        drawBarChart(16021, "2023-02-14");



    }



    public void drawBarChart(Integer sid, String date) {
        BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.getData().clear();
        chartPane.getChildren().clear();
        barChart.setTitle("Produktion i kWh for " + date + " for solcelle site " + sid);
        barChart.setPrefSize(700, 550);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Online");
        for (int i = 1; i < solceller.Data.size(); i++) {
            if (solceller.Data.get(i).getSid().equals(String.valueOf(sid)) && solceller.Data.get(i).getTimeDate().equals(date)) {
                int online = Integer.parseInt(solceller.Data.get(i).getOnline());
                series.getData().add(new XYChart.Data<>(solceller.Data.get(i).getTimeInHours(), online));
            }
        }
        barChart.getData().add(series);
        chartPane.getChildren().add(barChart);

        // Klik på en bar og få tallet vist

        for (XYChart.Data<String, Number> item : series.getData()) {
            // Vis tallet ved at holde musen inde
            item.getNode().setOnMousePressed((MouseEvent event) -> {
                Tooltip tooltip = new Tooltip(item.getYValue().toString());
                Tooltip.install(item.getNode(), tooltip);
                tooltip.show(item.getNode(), event.getScreenX(), event.getScreenY());
                // Hide tooltip når man slipper musen
                item.getNode().setOnMouseReleased((MouseEvent e) -> {
                    Tooltip.uninstall(item.getNode(), tooltip);
                    tooltip.hide();
                });
            });
        }
    }

}


