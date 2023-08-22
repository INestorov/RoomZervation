package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.BikeRental;
import application.entities.Restaurant;
import application.entities.Room;
import application.views.AdminView;
import application.views.PictureRoomView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class LateBikeListController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<BikeRental> bikeTable;
    @FXML
    private TableColumn<Integer, BikeRental> bikeId;
    @FXML
    private TableColumn<Integer, BikeRental> userId;
    @FXML
    private TableColumn<String, BikeRental> startTime;
    @FXML
    private TableColumn<String, BikeRental> endTime;
    @FXML
    private Button back;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bikeId.setCellValueFactory(new PropertyValueFactory<>("bikeId"));
        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        startTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        setupBikeRentals();
        back.setOnMouseClicked((event -> {
            try {
                new AdminView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }


    private void setupBikeRentals() {
        JsonParser.parseString(ServerCommunication.getLateBikes()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                JsonObject bikeObject = jsonObject.get("bike").getAsJsonObject();
                Integer bike = bikeObject.get("id").getAsInt();

                JsonObject userObject = jsonObject.get("user").getAsJsonObject();
                Integer user = userObject.get("id").getAsInt();

                String start = jsonObject.get("start").getAsString();

                JsonElement endtime = jsonObject.get("end");
                String end = null;

                if (!endtime.isJsonNull()) {
                    end = endtime.getAsString();
                }

                BikeRental bikeRental = new BikeRental(bike,user,start,end);
                bikeTable.getItems().add(bikeRental);
            });
    }


}
