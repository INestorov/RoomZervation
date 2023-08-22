package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.views.BikeView;
import application.views.MainView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BikeReturnSceneController extends MainSceneController implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Text text;

    @FXML
    private Button returnBike;

    @FXML
    private Button cancelReservation;

    @FXML
    private Button newReservation;

    @FXML
    private Button pickUpBike;

    private Hashtable<String, Integer> buildingIdLookup;

    private GregorianCalendar pickUpTime = new GregorianCalendar();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildingIdLookup = new Hashtable<String, Integer>();
        JsonObject bikeObject = JsonParser.parseString(ServerCommunication.getBikeByUserId())
            .getAsJsonObject();
        String s = bikeObject.get("id").getAsString();
        GregorianCalendar currentTime = new GregorianCalendar();
        currentTime.setTimeInMillis(System.currentTimeMillis());
        TimeZone.setDefault(TimeZone.getTimeZone("Atlantic/Reykjavik"));
        pickUpTime.setTimeZone(TimeZone.getTimeZone("Atlantic/Reykjavik"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            if (bikeObject.get("reservedUntil") != null
                && !bikeObject.get("building").isJsonNull()) {
                pickUpTime.setTime(formatter.parse(bikeObject.get("reservedUntil").getAsString()));
                if (pickUpTime.after(Calendar.getInstance())) {
                    String buildingName = bikeObject.get("building")
                        .getAsJsonObject().get("name").getAsString();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date pickUpTimeDate = new Date(pickUpTime.getTimeInMillis());
                    String startStr = dateFormat.format(pickUpTimeDate);
                    text.setText("You are currently renting bike: " + s
                        + "\nYou can pick up the bike at " + buildingName + " until " + startStr);
                    choiceBox.setVisible(false);
                    returnBike.setVisible(false);
                    newReservation.setVisible(false);
                } else {
                    pickUpExpired();
                }
            } else {
                text.setText("You are currently renting bike: " + s);
                newReservation.setVisible(false);
                pickUpBike.setVisible(false);
                cancelReservation.setVisible(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JsonParser.parseString(ServerCommunication.getBuildings()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                choiceBox.getItems().add(name);
                buildingIdLookup.put(name, jsonObject.get("id").getAsInt());
            });
        setUpMenu();
    }

    private void pickUpExpired() {
        text.setText("Your reservation has expired and you can no "
            + "longer pick up the bike or cancel the reservation");
        choiceBox.setVisible(false);
        returnBike.setVisible(false);
        cancelReservation.setVisible(false);
        pickUpBike.setVisible(false);
        newReservation.setVisible(true);
    }

    /**
     * Handles clicking the 'return bike' button.
     * Starts the 'rent bike' view.
     */
    public void returnBike() {
        if (choiceBox.getValue() != null) {
            JsonElement jsonElement1 = JsonParser.parseString(
                ServerCommunication.getBikeByUserId());
            int bikeId = jsonElement1.getAsJsonObject().get("id").getAsInt();
            boolean isReturned = ServerCommunication
                .returnBike(bikeId, buildingIdLookup.get(choiceBox.getValue()));
            if (isReturned) {
                try {
                    new BikeView().start(MainApp.stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
    }

    /**
     * Pick up the bike from the current reservation.
     */
    public void pickUpBike() {
        if (pickUpTime.after(Calendar.getInstance())) {
            JsonObject jsonObject = ServerCommunication.pickUpBike();
            if (jsonObject.get("status").getAsInt() == 200) {
                JsonObject bikeJson = JsonParser.parseString(
                    ServerCommunication.getBikeByUserId()).getAsJsonObject();
                int id = bikeJson.get("id").getAsInt();
                text.setText("You are currently renting bike: " + id);
                newReservation.setVisible(false);
                pickUpBike.setVisible(false);
                cancelReservation.setVisible(false);
                choiceBox.setVisible(true);
                returnBike.setVisible(true);
            }
        } else {
            pickUpExpired();
        }
    }

    /**
     * Cancel the current bike reservation.
     */
    public void cancelReservation() {
        if (pickUpTime.after(Calendar.getInstance())) {
            JsonObject jsonObject = ServerCommunication.cancelBike();
            if (jsonObject.get("status").getAsInt() == 200) {
                text.setText("Your reservation was successfully canceled");
                newReservation.setVisible(true);
                cancelReservation.setVisible(false);
                pickUpBike.setVisible(false);
            }
        } else {
            pickUpExpired();
        }
    }

    /**
     * Opens BikeScene where user can make a new reservation.
     *
     * @param actionEvent ActionEvent trigger.
     */
    public void newReservation(ActionEvent actionEvent) {
        ServerCommunication.newBikeReservation();
        try {
            new BikeView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
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
