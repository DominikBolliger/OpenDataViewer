package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import logic.DBConnection;

import java.util.Collections;
import java.util.List;

public class ViewerController {
    private DBConnection con;
    @FXML
    protected ComboBox cbStation;
    @FXML
    protected ComboBox cbLine;
    @FXML
    protected DatePicker dpStart;
    @FXML
    protected DatePicker dpEnd;
    @FXML
    protected Button btnShow;
    @FXML
    protected Label lblAmountOfTrains;
    @FXML
    protected Label lblDelayedTrainsDeparture;
    @FXML
    protected Label lblDelayedTrainsArrival;
    @FXML
    protected Label lblFailure;
    @FXML
    public void initialize(){
        createConnection();
        List<String> stationNames = con.getAllStations();
        Collections.sort(stationNames);
        cbStation.getItems().addAll(stationNames);
        btnShow.disableProperty().bind(cbStation.valueProperty().isNull().or(dpEnd.valueProperty().isNull().or(dpStart.valueProperty().isNull())));
    }

    private List<String> getStations() {
        return con.getAllStations();
    }

    private void createConnection() {
        con = new DBConnection("jdbc:mysql://localhost:3306/opendata", "root", "");
    }

    public void getLineTexts() {
        cbLine.getItems().clear();
        cbLine.getItems().addAll(con.getAllLineTextsForStation(cbStation.getSelectionModel().getSelectedItem().toString()));
    }

    public void btnShowClick() {
        lblAmountOfTrains.setText(String.valueOf(con.getTrainAmount(cbStation.getSelectionModel().getSelectedItem().toString(), cbLine.getSelectionModel().getSelectedItem().toString(), dpStart.getValue(), dpEnd.getValue())));
        lblDelayedTrainsArrival.setText(String.valueOf(con.getDelayedTrainsAmount(cbStation.getSelectionModel().getSelectedItem().toString(), cbLine.getSelectionModel().getSelectedItem().toString(), dpStart.getValue(), dpEnd.getValue())));
        lblFailure.setText(String.valueOf(con.getFailureTrains(cbStation.getSelectionModel().getSelectedItem().toString(), cbLine.getSelectionModel().getSelectedItem().toString(), dpStart.getValue(), dpEnd.getValue())));
    }
}
