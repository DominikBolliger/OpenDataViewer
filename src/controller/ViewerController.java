package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import logic.DBConnection;
import modell.Info;

import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


public class ViewerController{
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
    protected TableView<Info> tvInfo;
    @FXML
    TableColumn<Info, String> colLine;
    @FXML
    TableColumn<Info, String> colDay;
    @FXML
    TableColumn<Info, String> colArrival;
    @FXML
    TableColumn<Info, String> colDeparture;
    @FXML
    TableColumn<Info, String> colLate;

    @FXML
    public void initialize(){
        createConnection();
        List<String> stationNames = con.getAllStations();
        Collections.sort(stationNames);
        cbStation.getItems().addAll(stationNames);
        btnShow.disableProperty().bind(cbStation.valueProperty().isNull().or(dpEnd.valueProperty().isNull().or(dpStart.valueProperty().isNull().or(cbLine.valueProperty().isNull()))));
        colLine.setCellValueFactory(new PropertyValueFactory<Info, String>("lineText"));
        colDay.setCellValueFactory(new PropertyValueFactory<Info, String>("day"));
        colArrival.setCellValueFactory(new PropertyValueFactory<Info, String>("arrivalTime"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<Info, String>("departureTime"));
        colLate.setCellValueFactory(new PropertyValueFactory<Info, String>("late"));
        dpStart.setValue(LocalDate.now().minusDays(3));
        dpEnd.setValue(LocalDate.now().minusDays(3));
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
        tvInfo.setItems(con.getTableViewInfoData(cbStation.getSelectionModel().getSelectedItem().toString(), cbLine.getSelectionModel().getSelectedItem().toString(), dpStart.getValue(), dpEnd.getValue()));
    }
}
