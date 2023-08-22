package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.Building;
import application.entities.UserManager;
import application.views.MainView;
import application.views.ReturnBikeView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BikeSceneController extends MainSceneController implements Initializable {

    @FXML
    private TableView<Building> buildings;

    @FXML
    private TableColumn<String, Building> buildingNames;

    @FXML
    private TableColumn<Integer, Building> nbikes;

    @FXML
    private TableColumn<Time, Building> openingTime;

    @FXML
    private TableColumn<Time, Building> closingTime;

    @FXML
    private Button rentBike;

    @FXML
    private Button rentNow;

    @FXML
    private Text chooseBuildingText;

    @FXML
    private Text selectTimeText;

    @FXML
    private Text points;

    @FXML
    private TextField hours;

    @FXML
    private TextField minutes;

    private Building rowData;

    private boolean isPickUpNow = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpMenu();
        toggleInvisible();
        rentNow.setText("Rent and pick up now\ninstead");
        buildingNames.setCellValueFactory(new PropertyValueFactory<>("name"));
        nbikes.setCellValueFactory(new PropertyValueFactory<>("nbikes"));
        openingTime.setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        closingTime.setCellValueFactory(new PropertyValueFactory<>("closingTime"));
        buildings.setRowFactory(tv -> {
            TableRow<Building> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    rowData = row.getItem();
                    toggleInvisible();
                }
            });
            return row;
        });
        JsonParser.parseString(ServerCommunication.getBuildings()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                Time openingTime = Time.valueOf(jsonObject.get("openingTime").getAsString());
                Time closingTime = Time.valueOf(jsonObject.get("closingTime").getAsString());
                int id = jsonObject.get("id").getAsInt();
                int nbikes = Integer.parseInt(
                    ServerCommunication.getAvailableBikesNumberByBuildingId(id));
                Building building = new Building(
                    jsonObject.get("name").getAsString(), nbikes, id, openingTime, closingTime);
                buildings.getItems().add(building);
            });
    }

    /**
     * Hides buttons for picking date and time upon loading.
     * Shows them again upon clicking on a row.
     */
    private void toggleInvisible() {
        if (rowData == null) {
            rentNow.setVisible(false);
            rentBike.setVisible(false);
            selectTimeText.setVisible(false);
            points.setVisible(false);
            hours.setVisible(false);
            minutes.setVisible(false);
        } else if (rowData.getNbikes() == 0) {
            chooseBuildingText.setText("There are currently no bikes at this building."
                + "\nPlease choose another.");
        } else {
            chooseBuildingText.setVisible(false);
            rentNow.setVisible(true);
            rentBike.setVisible(true);
            selectTimeText.setVisible(true);
            points.setVisible(true);
            hours.setVisible(true);
            minutes.setVisible(true);
        }
    }


    public void pickUpNow() {
        isPickUpNow = true;
        rentBike();
    }

    /**
     * Handles clicking the 'rent bike' button.
     * Starts the 'return bike' view when bike is successfully rent.
     */
    public void rentBike() {
        if (rowData.getNbikes() == 0) {
            return;
        }
        if (rowData != null) {
            JsonArray arr = JsonParser.parseString(
                ServerCommunication.getBikesByBuildingId(
                    rowData.getId())).getAsJsonArray();
            JsonObject jsonObject = (JsonObject) ((JsonArray) arr).get(0);
            int bikeId = jsonObject.get("id").getAsInt();
            int userId = UserManager.getInstance().getUser().getId();
            JsonObject jsonObject1 = new JsonObject();
            if (isPickUpNow) {
                jsonObject1 = ServerCommunication.rentBike(
                    bikeId, userId, System.currentTimeMillis());
            } else {
                try {
                    Date date = new Date(System.currentTimeMillis());
                    System.out.println(System.currentTimeMillis());
                    Calendar calendar = new GregorianCalendar();
                    Calendar pickUpTime = new GregorianCalendar();
                    calendar.setTime(date);
                    pickUpTime.setTime(date);
                    int hour = Integer.parseInt(hours.getText());
                    int minute = Integer.parseInt(minutes.getText());
                    if (hour > 24 || hour < 0 || minute > 59 || minute < 0) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setHeaderText(null);
                        a.setContentText("please enter a valid time");
                        a.show();
                        return;
                    }
                    pickUpTime.set(Calendar.HOUR_OF_DAY, hour);
                    pickUpTime.set(Calendar.MINUTE, minute);
                    if (pickUpTime.before(calendar)) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setHeaderText(null);
                        a.setContentText("You cannot make a reservation in the past");
                        a.show();
                        return;
                    }
                    calendar.setTimeInMillis(System.currentTimeMillis()
                        + TimeUnit.HOURS.toMillis(1));
                    if (pickUpTime.after(calendar)) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setHeaderText(null);
                        a.setContentText("You can only reserve one hour ahead");
                        a.show();
                        return;
                    }
                    long pickUpTimeLong = pickUpTime.getTimeInMillis();
                    jsonObject1 = ServerCommunication.rentBike(bikeId, userId, pickUpTimeLong);
                } catch (NumberFormatException e) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText(null);
                    a.setContentText("Error! Please enter real numbers");
                    a.show();
                }
            }
            int status = jsonObject1.get("status").getAsInt();
            if (status == 200) {
                try {
                    new ReturnBikeView().start(MainApp.stage);
                    MainSceneController.hasBike = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText(null);
                a.setContentText("You have rented twice today.\nYou cannot rent anymore");
                a.show();
            }
        }
    }

    /**
     * Back button for returning to main scene :P.
     */
    public void backButton(ActionEvent actionEvent) throws Exception {
        Button b = (Button) actionEvent.getTarget();
        Scene s = b.getScene();
        Stage stage = (Stage) s.getWindow();
        stage.close();
        MainView m = new MainView();
        m.start(stage);
    }

}
