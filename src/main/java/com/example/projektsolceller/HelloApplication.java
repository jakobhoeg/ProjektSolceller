package com.example.projektsolceller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    /**
     * @param objectreferences for Site class to load data
     */
    Site solceller = new Site();
    File file = new File("Solcelle_data.tsv");


    /**
     * @param variables for login screen
     */
    Pane loginPane = new Pane();
    Pane mainScreen = new Pane();
    BorderPane borderPane = new BorderPane();
    GridPane gridPane = new GridPane();
    TextField username = new TextField("");
    TextField password = new TextField("");
    Button loginBtn = new Button("Login");
    Label loginLabel = new Label("Aarhus Solcelleanlæg");
    Stage temporaryStage = null;
    Scene loginScene = new Scene(loginPane, 600, 500);

    /**
     * @param variables for main screen
     */
    Scene mainScene = new Scene(mainScreen, 1000, 600);
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
    ChoiceBox<String> choiceChartBox = new ChoiceBox<>();
    HBox buttonHBox = new HBox();

    VBox vbox = new VBox();
    Label labelPeakAndLow = new Label("Den samlede produktion\nfor den valgte måned:");
    TextField peakAndLow = new TextField();
    TextField totalProduction = new TextField();

    Button searchBtn = new Button("Søg");
    Button clearBtn = new Button("Clear");

    @Override
    public void start(Stage stage) throws IOException {
        temporaryStage = stage;
        loginScene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());

        // Load data from file
        solceller.loadFile(file);

        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();

        username.setPromptText("Brugernavn");
        password.setPromptText("Kodeord");
        loginPane.requestFocus();

        borderPane.setLayoutX(210);
        borderPane.setLayoutY(200);
        loginPane.getChildren().add(borderPane);

        loginPane.getChildren().add(loginLabel);
        loginLabel.setLayoutY(160);
        loginLabel.setLayoutX(225);
        loginLabel.getStyleClass().add("login-label");
        borderPane.setPrefHeight(600);
        borderPane.setPrefWidth(500);
        borderPane.setCenter(gridPane);

        gridPane.setPrefHeight(200);
        gridPane.setPrefWidth(200);
        gridPane.addRow(3);
        gridPane.setVgap(10);
        gridPane.add(username, 0, 0);
        gridPane.add(password, 0, 1);
        gridPane.add(loginBtn, 0, 2);

        loginBtn.setPrefWidth(180);
        password.setOnAction(actionEvent -> {
            loginBtn.fire();
        });
        loginBtn.setOnAction(e -> {
            if (username.getText().equals("m") && password.getText().equals("m")) {
                changeToMainScene();
            } else {
                Label error = new Label("Wrong username or password");
                gridPane.add(error, 0, 3);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * Changes scene to main screen after logging in
     * Adds all the elements to the main screen
     * Adds the stylesheet to the main screen
     * Adds the bar chart to the main screen
     */
    public void changeToMainScene() {
        temporaryStage.setTitle("Aarhus Solcelleanlæg");
        temporaryStage.setScene(mainScene);
        temporaryStage.centerOnScreen();
        mainScene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());

        mainScreen.requestFocus();
        mainScreen.getChildren().add(mainPane);
        chartPane.getChildren().add(barChart);
        barChart.setLayoutY(30);
        barChart.setPrefSize(700, 550);
        mainPane.setPrefHeight(600);
        mainPane.setPrefWidth(1000);
        mainPane.setLeft(leftVbox);
        leftVbox.getChildren().addAll(choiceChartBox, labelDay, cbDay, cbMonth, cbYear, labelSite, cbSite,
                lowestProduction, buttonHBox, vbox);
        buttonHBox.getChildren().addAll(searchBtn, clearBtn);
        buttonHBox.setSpacing(15);
        clearBtn.getStyleClass().add("clear-btn");
        leftVbox.setPadding(new Insets(35));
        leftVbox.setSpacing(15);
        leftVbox.setPrefWidth(300);
        vbox.getChildren().addAll(labelPeakAndLow, totalProduction, peakAndLow);
        vbox.setPadding(new Insets(20, 0, 0, 0));
        vbox.setSpacing(5);

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
        searchBtn.setPrefWidth(120);

        loadDataInComboboxes();

        choiceChartBox.getItems().addAll("Line chart", "Bar chart");
        choiceChartBox.setValue("Line chart");
        // Choiccebox for choosing between bar chart and line chart - calls the drawChart method
        choiceChartBox.setOnAction(e -> {
            if (choiceChartBox.getValue().equals("Bar chart")) {
                drawBarChart(cbSite.getSelectionModel().getSelectedItem(), cbDay.getSelectionModel().getSelectedItem(),
                        cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem());
            } else if (choiceChartBox.getValue().equals("Line chart")) {
                drawLineChart(cbSite.getSelectionModel().getSelectedItem(), cbDay.getSelectionModel().getSelectedItem(),
                        cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem());
            }
        });

        lowestProduction.setOnAction(actionEvent -> {

        });

        // Search button - calls drawChart method and if statements to check if
        // the user has chosen a site, day, month or year - then calls
        // according method (calcTotalProduction etc)
        searchBtn.setOnAction(actionEvent -> {
            if (choiceChartBox.getValue().equals("Bar chart")) {
                drawBarChart(cbSite.getSelectionModel().getSelectedItem(), cbDay.getSelectionModel().getSelectedItem(),
                        cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem());
            } else if (choiceChartBox.getValue().equals("Line chart")) {
                drawLineChart(cbSite.getSelectionModel().getSelectedItem(), cbDay.getSelectionModel().getSelectedItem(),
                        cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem());
            }

            if (cbDay.getSelectionModel().isEmpty()) {
                totalProduction.setText(calcTotalProductionPrSite(cbSite.getSelectionModel().getSelectedItem(), cbMonth.getSelectionModel().getSelectedItem(),
                        cbYear.getSelectionModel().getSelectedItem()));
            }

            if (cbDay.getSelectionModel().isEmpty() && cbSite.getSelectionModel().isEmpty()) {
                totalProduction.setText(calcTotalProductionForAllSites(cbMonth.getSelectionModel().getSelectedItem(),
                        cbYear.getSelectionModel().getSelectedItem()));

                if (!lowestProduction.isSelected()) {
                    peakAndLow.setText(showHighestProductionDay(cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem()));
                }
                else peakAndLow.setText(showLowestProductionDay(cbMonth.getSelectionModel().getSelectedItem(), cbYear.getSelectionModel().getSelectedItem())); // FIXME: Metoden skal laves. Det modsatte af den, der står ovenfor.

            }
        });

        // Clear button - clears all choiceboxes and textfields
        clearBtn.setOnAction(actionEvent -> {
            cbDay.getSelectionModel().clearSelection();
            cbDay.setPromptText("Dag");
            cbMonth.setPromptText("Måned");
            cbMonth.getSelectionModel().clearSelection();
            cbYear.setPromptText("År");
            cbYear.getSelectionModel().clearSelection();
            cbSite.setPromptText("Site ID");
            cbSite.getSelectionModel().clearSelection();
            //todo: choicebox
            lowestProduction.setSelected(false);
            totalProduction.clear();
            peakAndLow.clear();
            totalProduction.setPromptText("Samlet produktion i kW/h");
            peakAndLow.setPromptText("Højeste / laveste produktionsdag");
        });

        labelPeakAndLow.setWrapText(true);

        peakAndLow.setPromptText("Højeste / laveste produktionsdag");
        peakAndLow.setEditable(false);
        peakAndLow.setFocusTraversable(false);
        peakAndLow.setOnMousePressed(actionEvent -> {
            leftVbox.requestFocus();
        });
        totalProduction.setPromptText("Samlet produktion i kW/h");
        totalProduction.setEditable(false);
        totalProduction.setFocusTraversable(false);
        totalProduction.setOnMousePressed(actionEvent -> {
            leftVbox.requestFocus();
        });

        mainPane.setRight(chartPane);

    }

    /**
     * Method for loading data into the comboboxes
     * For loops for adding the days, months and years
     */
    public void loadDataInComboboxes()
    {

        cbYear.getItems().add("2022");
        cbYear.getItems().add("2023");

        for (Site s : solceller.Data) {
            if (s == solceller.Data.get(0)) {
                continue;
            }
            if (!cbSite.getItems().contains(s.getSid())) {
                cbSite.getItems().add(s.getSid());
            }
        }

        for (int i = 1; i <= 31; i++) {
            if (i < 10) {
                cbDay.getItems().add("0" + String.valueOf(i));
            } else {
                cbDay.getItems().add(String.valueOf(i));
            }
        }

        for (int i = 1; i <= 12; i++) {
            if (i < 10) {
                cbMonth.getItems().add("0" + String.valueOf(i));
            } else {
                cbMonth.getItems().add(String.valueOf(i));
            }
        }
    }

    /**
     * Method for drawing a bar chart
     * @param sid
     * @param day
     * @param month
     * @param year
     */
    public void drawBarChart(String sid, String day, String month, String year) {
        BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.getData().clear();
        chartPane.getChildren().clear();
        String date = year + "-" + month + "-" + day;
        barChart.setTitle("Produktion i kWh pr. time for " + date + " for solcelle site " + sid + "\n\t\t\t(Tryk på søjlerne for at se data)");
        barChart.getXAxis().setLabel("Klokkeslæt");
        barChart.getYAxis().setLabel("Produktion i kWh");
        barChart.setLayoutY(30);
        barChart.setPrefSize(700, 550);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Online");
        for (int i = 1; i < solceller.Data.size(); i++) {
            if (solceller.Data.get(i).getSid().equals(String.valueOf(sid))
                    && solceller.Data.get(i).getTimeYear().equals(year)
                    && solceller.Data.get(i).getTimeMonth().equals(month)
                    && solceller.Data.get(i).getTimeDay().equals(day)) {
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

    /**
     * Method for drawing a line chart
     * @param sid
     * @param day
     * @param month
     * @param year
     */
    public void drawLineChart(String sid, String day, String month, String year) {
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        lineChart.getData().clear();
        chartPane.getChildren().clear();
        String date = year + "-" + month + "-" + day;
        lineChart.setTitle("Produktion i kWh pr. time for " + date + " for solcelle site " + sid + "\n\t\t\t(Tryk på punkterne for at se data)");
        lineChart.getXAxis().setLabel("Klokkeslæt");
        lineChart.getYAxis().setLabel("Produktion i kWh");
        lineChart.setLayoutY(30);
        lineChart.setPrefSize(700, 550);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Online");
        for (int i = 1; i < solceller.Data.size(); i++) {
            if ((solceller.Data.get(i).getSid().equals(String.valueOf(sid))
                    && solceller.Data.get(i).getTimeYear().equals(year)
                    && solceller.Data.get(i).getTimeMonth().equals(month)
                    && solceller.Data.get(i).getTimeDay().equals(day))) {
                int online = Integer.parseInt(solceller.Data.get(i).getOnline());
                series.getData().add(new XYChart.Data<>(solceller.Data.get(i).getTimeInHours(), online));
            }
        }
        lineChart.getData().add(series);
        chartPane.getChildren().add(lineChart);

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

    /**
     * Method for calculating the total production for a specific site
     * @param sid
     * @param month
     * @param year
     * @return
     */
    public String calcTotalProductionPrSite(String sid, String month, String year) {
        int totalProduction = 0;

        for (int i = 1; i < solceller.Data.size(); i++) {
            if (solceller.Data.get(i).getSid().equals(String.valueOf(sid))
                    && solceller.Data.get(i).getTimeMonth().equals(month)
                    && solceller.Data.get(i).getTimeYear().equals(year)) {
                totalProduction += Integer.parseInt(solceller.Data.get(i).getOnline());
            }
        }
        return totalProduction + " kWh";
    }

    /**
     * Method for calculating the total production for all sites
     * @param month
     * @param year
     * @return
     */
    public String calcTotalProductionForAllSites(String month, String year)
    {
        int totalProduction = 0;

        for (int i = 1; i < solceller.Data.size(); i++) {
            if (solceller.Data.get(i).getTimeMonth().equals(month)
                    && solceller.Data.get(i).getTimeYear().equals(year)) {
                totalProduction += Integer.parseInt(solceller.Data.get(i).getOnline());
            }
        }
        return totalProduction + " kWh";
    }

    /**
     * Method for calculating the highest production day for a specific month
     * @param month
     * @param year
     * @return
     */
    public String showHighestProductionDay(String month, String year) {
        int tempMaxProductionPerDay = 0;
        int maxProductionPerDay = 0;
        String date = "";

        for (int i = 1; i < solceller.Data.size() - 1; i++) {
            if (Integer.parseInt(solceller.Data.get(i).getTimeDay())
                    == Integer.parseInt(solceller.Data.get(i + 1).getTimeDay())
                    && solceller.Data.get(i).getTimeMonth().equals(month)) {
                tempMaxProductionPerDay += Integer.parseInt(solceller.Data.get(i).getOnline());
            }

            else if (Integer.parseInt(solceller.Data.get(i).getTimeDay())
                    != Integer.parseInt(solceller.Data.get(i + 1).getTimeDay())
                    && solceller.Data.get(i).getTimeMonth().equals(month)) {
                tempMaxProductionPerDay += Integer.parseInt(solceller.Data.get(i).getOnline());
            }

            if (tempMaxProductionPerDay > maxProductionPerDay) {
                maxProductionPerDay = tempMaxProductionPerDay;
                date = solceller.Data.get(i).getTimeDay() + "-"
                        + solceller.Data.get(i).getTimeMonth() + "-"
                        + solceller.Data.get(i).getTimeYear();
            }
        }
        return date;
    }

    /**
     * Method for calculating the lowest production day for a specific month
     * @param month
     * @param year
     * @return
     */
    public String showLowestProductionDay(String month, String year) {
        int tempMaxProductionPerDay = 0;
        int minProductionPerDay = 0;
        String date = "";

        for (int i = 1; i < solceller.Data.size() - 1; i++) {
            if (Integer.parseInt(solceller.Data.get(i).getTimeDay())
                    == Integer.parseInt(solceller.Data.get(i + 1).getTimeDay())
                    && solceller.Data.get(i).getTimeMonth().equals(month)) {
                tempMaxProductionPerDay += Integer.parseInt(solceller.Data.get(i).getOnline());
            }

            else if (Integer.parseInt(solceller.Data.get(i).getTimeDay())
                    != Integer.parseInt(solceller.Data.get(i + 1).getTimeDay())
                    && solceller.Data.get(i).getTimeMonth().equals(month)) {
                tempMaxProductionPerDay += Integer.parseInt(solceller.Data.get(i).getOnline());
            }

            if (minProductionPerDay == 0) {
                minProductionPerDay = tempMaxProductionPerDay;
                date = solceller.Data.get(i).getTimeDay() + "-"
                        + solceller.Data.get(i).getTimeMonth() + "-"
                        + solceller.Data.get(i).getTimeYear();
            }

            else if (tempMaxProductionPerDay < minProductionPerDay) {
                minProductionPerDay = tempMaxProductionPerDay;
                date = solceller.Data.get(i).getTimeDay() + "-"
                        + solceller.Data.get(i).getTimeMonth() + "-"
                        + solceller.Data.get(i).getTimeYear();
            }
        }
        return date;
    }
}