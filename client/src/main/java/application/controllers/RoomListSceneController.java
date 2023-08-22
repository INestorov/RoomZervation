package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.Room;
import application.views.DatePickerView;
import application.views.MainView;
import application.views.PictureRoomView;
import application.views.PreloaderView;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

//@author - Silviu Marii
public class RoomListSceneController extends MainSceneController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<Room> roomTable;
    @FXML
    private TableColumn<Room, String> name;
    @FXML
    private TableColumn<Room, String> buildingName;
    @FXML
    private TableColumn<Room, Integer> size;
    @FXML
    private Label nameRoom;
    @FXML
    private GridPane gridPane;

    @FXML
    private Button pictureRoom;

    @FXML
    private Button roomConfirm;
    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Button filterSearch;
    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    private ObservableList<Room> roomModels;
    private ObservableList<Room> roomFiltered;
    private static Room roomChosen;
    private int searchOption;


    public RoomListSceneController() {
        roomTable = new TableView<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUpMenu();
        name.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getName()));
        buildingName.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getBuilding(
                cellData.getValue().getBuildingId())));
        size.setCellValueFactory(new PropertyValueFactory<>("size"));
        //new Thread(this::loadData).start();
        Thread d = new Thread(this::loadData);
        d.start();
        roomTable.setRowFactory(tv -> {
            TableRow<Room> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Room rowData = row.getItem();
                    initializeGrid(rowData);
                }
            });
            return row;
        });
    }

    private void initializeGrid(Room rowData) {
        gridPane.getChildren().clear();
        Label info = new Label("Facilities: " + '\n' + rowData.getFacilities());
        gridPane.add(info, 0, 0);
        Label info2 = new Label("Description: " + rowData.getDescription());
        gridPane.add(info2, 0, 1);
        gridPane.add(pictureRoom, 0, 2);

        pictureRoom.setOpacity(1);
        roomConfirm.setOpacity(1);

        gridPane.getChildren().add(roomConfirm);
        roomConfirm.setOnMouseClicked(event -> {
            System.out.println("Double click on: " + rowData.getName());
            try {
                roomChosen = rowData;
                new DatePickerView().start(MainApp.stage, rowData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pictureRoom.setOnMouseClicked((event -> {
            try {
                roomChosen = rowData;
                new PictureRoomView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public static Room getRoom() {
        return roomChosen;
    }

    private void loadData() {
        roomModels = PreloaderView.getRoomListData();
        roomFiltered = roomModels;
        roomTable.setItems(roomModels);
    }

    /**
     * Back button to the main menu.
     *
     * @param actionEvent needed for getting the scene
     * @throws Exception for throwing IOException
     */
    public void backButton(ActionEvent actionEvent) throws Exception {
        Button b = (Button) actionEvent.getTarget();
        Scene s = b.getScene();
        Stage stage = (Stage) s.getWindow();
        //stage.close();
        MainView m = new MainView();
        m.start(stage);
    }

    /**
     * Prepares GUI for user interaction.
     * @param actionEvent the trigger
     */
    public void getBySize(ActionEvent actionEvent) {
        label1.setText("Enter min size");
        label1.setVisible(true);
        label2.setText("Enter max size");
        label2.setVisible(true);
        filterSearch.setVisible(true);
        text1.setVisible(true);
        text2.setVisible(true);
        searchOption = 1;
    }

    /**
     * Prepares GUI for user interaction.
     * @param actionEvent the trigger
     */
    public void getByBuilding(ActionEvent actionEvent) {
        label1.setText("Enter Building Name");
        label1.setVisible(true);
        label2.setVisible(false);
        filterSearch.setVisible(true);
        text1.setVisible(true);
        text2.setVisible(false);
        searchOption = 2;

    }

    /**
     * Prepares GUI for user interaction.
     * @param actionEvent the trigger
     */
    public void getByFacilities(ActionEvent actionEvent) {
        label1.setText("Enter Facilities");
        label1.setVisible(true);
        label2.setVisible(false);
        filterSearch.setVisible(true);
        text1.setVisible(true);
        text2.setVisible(false);
        searchOption = 3;
    }

    /**
    * Method that determines which filter was selected and applies it.
    * searchOption 1 means filter by size, 2 by building name, 3 by facilities
     *                    At the moment it can look only after 1 facility unfortunately
    */
    public void applyFilter(ActionEvent actionEvent) {
        List<Room> filtered = new ArrayList<>();
        if (searchOption == 1) {
            for (Room e: roomModels) {
                if (Integer.parseInt(text1.getText()) <= e.getSize()
                    && Integer.parseInt(text2.getText()) >= e.getSize()) {
                    filtered.add(e);
                }
            }
        } else if (searchOption == 2) {
            for (Room e: roomModels) {
                if (e.getBuilding(e.getBuildingId()).toLowerCase()
                    .contains(text1.getText().toLowerCase())) {
                    filtered.add(e);
                }
            }
        } else {
            for (Room e: roomModels) {
                if (e.getFacilities().toLowerCase().contains(text1.getText().toLowerCase())) {
                    filtered.add(e);
                }
            }
        }
        roomFiltered = FXCollections.observableArrayList(filtered);
        roomTable.setItems(roomFiltered);
    }

}
