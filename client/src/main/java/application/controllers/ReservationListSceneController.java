package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.controls.Progress;
import application.entities.Room;
import application.entities.TimeSlot;
import application.entities.UserManager;
import application.views.MainView;
import application.views.OrderConfirmView;
import application.views.OrderFoodView;
import application.views.RoomListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
//@author - Silviu Marii

public class ReservationListSceneController extends MainSceneController implements Initializable {
    @FXML
    private TableView<TimeSlot> myRoomsTable;
    @FXML
    private TableColumn<TimeSlot, String> name;
    @FXML
    private TableColumn<TimeSlot, String> date;
    @FXML
    private TableColumn<TimeSlot, String> interval;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label label;
    @FXML
    private Button foodButton;

    private ObservableList<TimeSlot> roomModels;
    private int size;
    private String buttonName;
    private String facilities;
    private TimeSlot rowData;
    private boolean isSelected;

    /**
     * Deletes a reservation.
     * Left to send cancel req to server
     *
     * @param actionEvent the event is when the user clicks the cancel button
     */
    public void cancelReservationButton(ActionEvent actionEvent) {
        TimeSlot row = myRoomsTable.getSelectionModel().getSelectedItem();
        int id = row.getId();
        System.out.println("this reservation will be deleted: "
            + ServerCommunication.getReservation(id));
        myRoomsTable.getItems().remove(row);
        // myRoomsTable.getVisibleLeafColumns()
        //send data
        Progress p = new Progress();
        Stage tempStage = p.initStage();
        try {
            p.showStatus(0, tempStage);
            ServerCommunication.deleteReservation(id);
            p.showStatus(1, tempStage);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unsuccessful delete on room " + id);
        } finally {
            p.unsuccesful(tempStage);
            p.close(tempStage);
        }
        gridPane.getChildren().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpMenu();
        isSelected = false;
        name.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getRoom().getName()));
        date.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getStart().substring(0,10)));
        interval.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getStart().substring(11,16) + " - "
                +  cellData.getValue().getEnd().substring(0,5)));
        // myRoomsTable.setItems(myRoomModels);
        new Thread(this::loadData).start();
        myRoomsTable.setRowFactory(tv -> {
            TableRow<TimeSlot> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    rowData = row.getItem();
                    System.out.println("click on: " + rowData.getRoom().getName());
                    size = rowData.getRoom().getSize();
                    buttonName = rowData.getRoom().getBuilding(rowData.getRoom().getBuildingId());
                    facilities = rowData.getRoom().getFacilities();
                    setupGridPane();
                    isSelected = true;
                }
            });
            return row;
        });
    }


    /**
     * Handles clicking the order here button.
     * Opens the OrderFood Scene.
     */
    public void orderFood() {
        if (!isSelected) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("please select a reservation first");
            a.show();
            return;
        }
        UserManager.getInstance().setReservationId(rowData.getId());
        if (UserManager.getInstance().getBasket() != null) {
            try {
                new OrderConfirmView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                new OrderFoodView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setup the grid pane.
     */
    private void setupGridPane() {
        //  gridPane = new GridPane();
        gridPane.getChildren().clear();
        label.setVisible(true);
        //  gridPane.setGridLinesVisible(true);
        Label l = new Label("Size: " + String.valueOf(size));
        gridPane.add((Node) l, 0, 0);
        if (facilities == null) {
            facilities = "Not uploaded yet.";
        }
        l = new Label("Facilities:  \n " + facilities);
        gridPane.setColumnSpan(l, 2);
        gridPane.add(l, 0, 1);
        gridPane.setFillWidth(l, true);
        l = new Label("Building: \n" + buttonName);
        gridPane.add(l, 1, 0);
    }

    /**
     * Load the data from the server.
     */
    private void loadData() {
        List<TimeSlot> roomModelViews = new ArrayList<>();
        String username = LoginSceneController.getUser();
        //int id = JsonParser.parseString(ServerCommunication.getUserId1(username)).getAsInt();
        JsonParser.parseString(ServerCommunication.getReservationByUsername(username))
            .getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                List<String> list = new ArrayList<>();
                JsonArray jsonFacilities = jsonObject.getAsJsonObject("room")
                    .getAsJsonArray("facilities");
                for (int i = 0; i < jsonFacilities.size(); i++) {
                    list.add(jsonFacilities.get(i).getAsJsonObject().get("name").getAsString());
                }
                roomModelViews.add(new TimeSlot(
                    new Room(jsonObject.getAsJsonObject("room").get("id").getAsInt(),
                        jsonObject.getAsJsonObject("room").get("name").getAsString(),
                        jsonObject.getAsJsonObject("room").getAsJsonObject("building")
                            .get("id").getAsInt(),
                        jsonObject.getAsJsonObject("room").get("size").getAsInt(),
                        list,
                        jsonObject.getAsJsonObject("room").get("description").getAsString()),
                    jsonObject.get("id").getAsInt(),
                    jsonObject.get("start").getAsString().substring(0, jsonObject
                        .get("start").getAsString().lastIndexOf(":") + 3)
                        .replace("T", " "),
                    jsonObject.get("end").getAsString().substring(11, jsonObject
                        .get("end").getAsString().lastIndexOf(":") + 3)));

            });
        roomModels = FXCollections.observableArrayList(roomModelViews);
        myRoomsTable.setItems(roomModels);
    }

    /**
     * Back button for returning to main scene :P.
     */
    public void backButton(ActionEvent actionEvent) throws Exception {
        Button b = (Button) actionEvent.getTarget();
        Scene s = (Scene) b.getScene();
        Stage stage = (Stage) s.getWindow();
        //stage.close();
        MainView m = new MainView();
        m.start(stage);
    }

    /**
     * go to the room page.
     *
     * @param actionEvent the event trigger
     */
    public void goToRoomList(ActionEvent actionEvent) {
        try {
            new RoomListView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
