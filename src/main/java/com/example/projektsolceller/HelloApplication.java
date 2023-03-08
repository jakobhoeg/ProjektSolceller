package com.example.projektsolceller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
    VBox leftVbox = new VBox();
    Pane chartPane = new Pane();
    BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());

    Label labelDay = new Label("Undlad at vælge dag, for at se data for hele måneden");
    Label labelSite = new Label("Undlad at vælge site, for at se den samlede produktion for alle sites i den valgte måned");
    ComboBox<String> cbSite = new ComboBox<>();
    ComboBox<String> cbYear = new ComboBox<>();
    ComboBox<String> cbMonth = new ComboBox<>();
    ComboBox<String> cbDay = new ComboBox<>();
    CheckBox lowestProduction = new CheckBox("Vis laveste produktion");

    Button searchBtn = new Button("Søg");


    @Override
    public void start(Stage stage) throws IOException {
        temporaryStage = stage;

        solceller.loadFile(file);

        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();

        username.setPromptText("Brugernavn");
        password.setPromptText("Kodeord");
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
        chartPane.getChildren().add(barChart);
        barChart.setPrefSize(700, 550);
        mainPane.setPrefHeight(600);
        mainPane.setPrefWidth(1200);
        mainPane.setLeft(leftVbox);
        leftVbox.getChildren().addAll(labelDay,cbDay,cbMonth, cbYear,labelSite,cbSite,lowestProduction,searchBtn);
        leftVbox.setPadding(new Insets(50));
        leftVbox.setSpacing(15);
        leftVbox.setPrefWidth(300);

        labelDay.setWrapText(true);
        labelSite.setWrapText(true);
        cbDay.setPromptText("Dag");
        cbDay.setPrefWidth(120);
        cbMonth.setPromptText("Måned");
        cbMonth.setPrefWidth(120);
        cbYear.setPromptText("År");
        cbYear.setPrefWidth(120);

        cbSite.setEditable(true);
        cbSite.setPromptText("Site ID");
        cbSite.setPrefWidth(120);

        searchBtn.setOnAction(actionEvent -> {
            drawBarChart(cbSite.getSelectionModel().getSelectedItem(), cbDay.getSelectionModel().getSelectedItem(),
                    cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem());
        });




        mainPane.setRight(chartPane);

        for (Sites s : solceller.Data) {
            if (s == solceller.Data.get(0)) {
                continue;
            }
            if (!cbSite.getItems().contains(s.getSid()))
            {
                cbSite.getItems().add(s.getSid());
            }
        }

        for (int i = 1; i <= 31; i++) {
            if (i < 10)
            {
                cbDay.getItems().add("0"+String.valueOf(i));
            }
            else {
                cbDay.getItems().add(String.valueOf(i));
            }

        }

        for (int i = 1; i <= 12; i++) {
            if (i < 10)
            {
                cbMonth.getItems().add("0"+String.valueOf(i));
            }
            else {
                cbMonth.getItems().add(String.valueOf(i));
            }

        }


        cbYear.getItems().add("2022");
        cbYear.getItems().add("2023");



    }

    public void drawBarChart(String sid, String day, String month, String year) {
        BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.getData().clear();
        chartPane.getChildren().clear();
        String date = year + "-" + month + "-" + day;
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