package logic;


import javafx.collections.FXCollections;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    private String url;
    private String user;
    private String pass;
    private Connection con;

    public DBConnection(String url, String user, String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public void connect() {
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllStations() {
        this.connect();
        List<String> stationNames = FXCollections.observableArrayList();
        try {
            String sql = "Select `haltestelle_name` from `opendata`.`haltestelle`";
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                stationNames.add(rs.getString("haltestelle_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.close();
        return stationNames;
    }

    private int getStationIdByStationName(String stationName) {
        int stationID = 0;
        try {
            String sql = "Select `haltestelle_id` from `opendata`.`haltestelle` where `haltestelle_name` = ?";
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            prepareStatement.setString(1, stationName);
            ResultSet rs = prepareStatement.executeQuery();
            if (rs.next()) {
                stationID = rs.getInt("haltestelle_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return stationID;
    }

    public List<String> getAllLineTextsForStation(String stationName) {
        this.connect();
        List<String> lineTexts = FXCollections.observableArrayList();
        try {
            String sql = "Select `linien_text` from `opendata`.`fahrt` where `haltestelle_id_fk` = ? group by `linien_text` Order By `linien_text` ASC";
            PreparedStatement prepareStatement = con.prepareStatement(sql);
            prepareStatement.setInt(1, getStationIdByStationName(stationName));
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                lineTexts.add(rs.getString("linien_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.close();
        return lineTexts;
    }

    public int getDelayedTrainsAmount(){
        int amount = 0;

        return amount;
    }

    public int getTrainAmount(String stationName, String lineText, LocalDate start, LocalDate end) {
        int amount = 0;
        List<Integer> betriebstagIDLIst = getBetriebstagID(start, end);
        this.connect();
        int stationId = getStationIdByStationName(stationName);
        for (int i = 0; i < betriebstagIDLIst.size(); i++) {
            try {
                String sql = "Select count(*) from `opendata`.`fahrt` where haltestelle_id_fk = ? and linien_text = ? and betreibstag_nr_fk = ?";
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setInt(1, stationId);
                prepareStatement.setString(2, lineText);
                prepareStatement.setInt(3, betriebstagIDLIst.get(i));
                ResultSet rs = prepareStatement.executeQuery();
                if (rs.next()) {
                    amount += rs.getInt("count(*)");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        this.close();
        return amount;
    }

    private List<Integer> getBetriebstagID(LocalDate start, LocalDate end){
        List<Integer> betriebstagIDList = new ArrayList<>();
        this.connect();
        int count = 0;
        while(start.plusDays(count).isBefore(end.plusDays(1))){
            try {
                String sql = "Select `betriebstag_ID` from `opendata`.`betriebstag` where `betriebstag` = ?";
                PreparedStatement prepareStatement = con.prepareStatement(sql);
                prepareStatement.setString(1, start.plusDays(count).toString());
                ResultSet rs = prepareStatement.executeQuery();
                if (rs.next()) {
                    betriebstagIDList.add(rs.getInt("betriebstag_id"));
                }
                count++;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        this.close();
        return betriebstagIDList;
    }

}
