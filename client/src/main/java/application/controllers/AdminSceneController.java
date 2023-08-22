package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.Holiday;
import application.views.AdminView;
import application.views.LateBikeListView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AdminSceneController extends Stage implements Initializable {
    String contentText;
    String list;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger ham;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button buildingB;
    @FXML
    private Button roomB;
    @FXML
    private Button search;
    @FXML
    private Button bikeB;
    @FXML
    private Button facilityB;
    @FXML
    private Button menuB;
    @FXML
    private Button restaurantB;
    @FXML
    private TextField textField;
    @FXML
    private TextField textField1;
    @FXML
    private Label label;
    @FXML
    private Label labelInfo;
    @FXML
    private Button confirm;
    @FXML
    private Label reminder;
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField text;
    @FXML
    private TextField text1;
    @FXML
    private TextField text2;
    @FXML
    private TextField text3;
    @FXML
    private Label info;
    @FXML
    private Label info1;
    @FXML
    private Label info2;
    @FXML
    private Label info3;
    @FXML
    private Label info4;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private ChoiceBox<String> choiceBox1;
    @FXML
    private ChoiceBox<String> choiceBox3;
    @FXML
    private ChoiceBox<String> choiceBox4;
    @FXML
    private ChoiceBox<String> choiceBox5;
    @FXML
    private ChoiceBox<String> choiceBox6;
    private TableView<Holiday> holidayTable = new TableView<>();
    private Hashtable<String, Integer> buildingIdLookup;
    private Hashtable<String, Integer> roomIdLookup;
    private Hashtable<String, Integer> bikeIdLookup;
    private Hashtable<String, Integer> facilityIdLookup;
    private Hashtable<String, Integer> foodIdLookup;
    private Hashtable<String, Integer> restaurantIdLookUp;
    private int restaurantId;
    private int buildingId;
    private int size;
    private String namer;
    private String description;
    private String name;
    private double price;
    private String type;
    private List<Integer> toUpdate = new ArrayList<>();
    private Holiday rowHoliday;
    private EventHandler<ActionEvent> buildingToModify = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(80);
            gridPane.setLayoutX(80);
            info = new Label("Name");
            gridPane.add(info, 0, 0);
            info1 = new Label("Opening hour");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Closing hour");
            gridPane.add(info2, 0, 2);
            gridSetUp();
            confirm.setOnAction(event1 -> {


                int id1 = buildingIdLookup.get(choiceBox.getValue());
                String list = ServerCommunication.getBuildingById(id1);
                String[] result = list.split(":");
                System.out.println(list);
                String name = text.getText();

                if (name.equals("")) {
                    name = result[2].substring(1, result[2].length() - 15);
                }

                String openingHour = text1.getText();

                if (openingHour.equals("")) {
                    openingHour = result[3].substring(1) + ":" + result[4];
                }

                String closingHour = text2.getText();

                if (closingHour.equals("")) {
                    closingHour = result[6].substring(1) + ":" + result[7];
                }
                String id = Integer.toString(buildingIdLookup.get(choiceBox.getValue()));
                Alert alert;
                if (ServerCommunication.updateBuilding(id, name, openingHour, closingHour)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully updated a building");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated a building");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to update a building");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update a building");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> restaurantToModify = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(80);
            gridPane.setLayoutX(80);
            info = new Label("Name");
            gridPane.add(info, 0, 0);
            info1 = new Label("Opening hour");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Closing hour");
            info3 = new Label("Building");
            gridPane.add(info2, 0, 2);
            gridPane.add(info3, 0, 3);
            GridPane.setMargin(info3, new Insets(15, 15, 0, 0));
            buildingSetUp();
            gridPane.add(choiceBox, 1, 3);
            GridPane.setMargin(choiceBox, new Insets(15, 15, 0, 0));
            gridSetUp();
            confirm.setOnAction(event1 -> {


                int id1 = restaurantIdLookUp.get(choiceBox4.getValue());
                String list = ServerCommunication.getRestaurantsById(id1);
                String[] result = list.split(":");
                System.out.println(list);

                String name = text.getText();
                if (name.equals("")) {
                    name = result[2].substring(1, result[2].length() - 15);
                }

                String openingHour = text1.getText();

                if (openingHour.equals("")) {
                    openingHour = result[3].substring(1) + ":" + result[4];
                }

                String closingHour = text2.getText();

                if (closingHour.equals("")) {
                    closingHour = result[6].substring(1) + ":" + result[7];
                }
                try {
                    buildingId = buildingIdLookup.get(choiceBox.getValue());
                } catch (Exception e) {
                    buildingId = Integer.parseInt(result[10].substring(0, result[10].length() - 7));
                }
                String id = Integer.toString(restaurantIdLookUp.get(choiceBox4.getValue()));
                Alert alert;
                if (ServerCommunication.updateRestaurant(id, name, openingHour,
                    closingHour, buildingId)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully updated a restaurant");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated a restaurant");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to update a restaurant");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update a restaurant");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> roomToModify = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(80);
            gridPane.setLayoutX(80);
            addCancelButton();
            info = new Label("Building");
            gridPane.add(info, 0, 0);
            info1 = new Label("Name");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Size");
            gridPane.add(info2, 0, 2);
            info3 = new Label("Description");
            gridPane.add(info3, 0, 3);
            text3 = new TextField();
            gridPane.add(text3, 1, 3);
            GridPane.setMargin(info, new Insets(5, 5, 0, 0));
            GridPane.setMargin(info1, new Insets(5, 5, 0, 0));
            GridPane.setMargin(info2, new Insets(5, 5, 0, 0));
            GridPane.setMargin(info3, new Insets(5, 5, 0, 0));
            GridPane.setMargin(text3, new Insets(15, 15, 0, 0));

            gridSetUp();
            gridPane.getChildren().remove(text);
            buildingSetUp();
            gridPane.add(choiceBox, 1, 0);
            int id = roomIdLookup.get(choiceBox1.getValue());
            confirm.setOnAction(event1 -> {
                //here u have all the data for the room to change


                String roomInfo = ServerCommunication.getRoomById(id);
                String[] list = roomInfo.split(":");
                try {
                    buildingId = buildingIdLookup.get(choiceBox.getValue());
                    try {
                        size = Integer.parseInt(text2.getText());
                    } catch (Exception exc) {
                        size = JsonParser.parseString(ServerCommunication.getRoomById(id))
                            .getAsJsonObject().get("size").getAsInt();
                    }
                } catch (Exception ex) {
                    buildingId = Integer.parseInt(list[3].substring(0, list[3].length() - 7));
                    try {
                        size = Integer.parseInt(text2.getText());
                    } catch (Exception exc) {
                        size = JsonParser.parseString(ServerCommunication.getRoomById(id))
                            .getAsJsonObject().get("size").getAsInt();
                    }
                }

                namer = text1.getText();

                if (namer.equals("")) {
                    namer = list[11].substring(1, list[11].length() - 8);
                }

                description = text3.getText();
                if (description.equals("")) {
                    description = JsonParser.parseString(ServerCommunication.getRoomById(id))
                        .getAsJsonObject().get("description").getAsString();
                }
                Alert alert;
                if (ServerCommunication.updateRoom(id, buildingId, namer, size, description)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully updated a room");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated a room");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to update a room");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update a room");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> foodToModify = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            choiceBox5 = new ChoiceBox<>();
            gridPane = new GridPane();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(80);
            gridPane.setLayoutX(80);
            text = new TextField();
            gridPane.add(text, 1, 1);
            text1 = new TextField();
            gridPane.add(text1, 1, 2);
            text2 = new TextField();
            gridPane.add(text2, 1, 3);
            text3 = new TextField();
            gridPane.add(text3, 1, 4);
            GridPane.setMargin(text, new Insets(15, 15, 0, 0));
            GridPane.setMargin(text1, new Insets(15, 15, 0, 0));
            GridPane.setMargin(text2, new Insets(15, 15, 0, 0));
            GridPane.setMargin(text3, new Insets(15, 15, 0, 0));
            confirm = new Button("Confirm");
            reminder = new Label("If you don't enter anything,"
                + '\n' + " that value won't change");
            gridPane.add(reminder, 0, 5);
            gridPane.add(confirm, 1, 5);
            addCancelButton();
            info = new Label("Item");
            gridPane.add(info, 0, 0);
            info1 = new Label("Name");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Description");
            gridPane.add(info2, 0, 2);
            info3 = new Label("Price");
            gridPane.add(info3, 0, 3);
            info4 = new Label("Type");
            gridPane.add(info4, 0, 4);
            restaurantId = restaurantIdLookUp.get(choiceBox4.getValue());
            foodSetUp();
            gridPane.add(choiceBox5, 1, 0);
            confirm.setOnAction(event1 -> {
                int id = foodIdLookup.get(choiceBox5.getValue());

                try {
                    price = Double.parseDouble(text2.getText());
                } catch (Exception e) {
                    price = JsonParser.parseString(ServerCommunication.getFoodById(id))
                        .getAsJsonObject().get("price").getAsDouble();
                }

                name = text.getText();
                if (name.equals("")) {
                    name = JsonParser.parseString(ServerCommunication.getFoodById(id))
                        .getAsJsonObject().get("name").getAsString();
                }

                description = text1.getText();
                if (description.equals("")) {
                    description = JsonParser.parseString(ServerCommunication.getFoodById(id))
                        .getAsJsonObject().get("description").getAsString();
                }

                type = text3.getText();
                if (type.equals("")) {
                    type = JsonParser.parseString(ServerCommunication.getFoodById(id))
                        .getAsJsonObject().get("type").getAsString();
                }

                Alert alert;
                if (ServerCommunication.updateFood(id, restaurantId, name, description,
                    type, price)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully updated a food item");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated a food item");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to update a food item");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update a food item");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> bikeToModify = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            gridPane.setLayoutY(80);
            gridPane.setLayoutX(80);
            choiceBox = new ChoiceBox<>();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            info = new Label("Enter building");
            buildingSetUp();
            gridPane.add(info, 0, 0);
            gridPane.add(choiceBox, 1, 0);
            GridPane.setMargin(info, new Insets(0, 5, 10, 0));
            GridPane.setMargin(choiceBox, new Insets(0, 5, 10, 0));

            confirm = new Button("Confirm");
            reminder = new Label("If you don't enter anything,"
                + '\n' + " that value won't change");
            GridPane.setMargin(reminder, new Insets(0, 10, 0, 0));
            gridPane.add(reminder, 0, 2);
            gridPane.add(confirm, 1, 2);
            confirm.setOnAction(event2 -> {
                //here u have all the data for the room to change
                int id = bikeIdLookup.get(choiceBox6.getValue());
                int buildingId = buildingIdLookup.get(choiceBox.getValue());
                Alert alert;
                if (ServerCommunication.updateBike(id, buildingId)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully updated bike location");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully updated bike location");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to update bike location");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update bike location");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> buildingToDelete = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            buildingSetUp();
            int buildingId = buildingIdLookup.get(choiceBox.getValue());
            Alert alert;

            if (ServerCommunication.deleteBuilding(buildingId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a building");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a building");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a building");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a building");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> foodToDelete = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            addCancelButton();
            choiceBox5 = new ChoiceBox<>();
            confirm = new Button("Confirm");
            info = new Label("Choose what to delete");
            gridPane.add(info, 0, 0);
            gridPane.add(confirm, 1, 2);
            foodSetUp();
            gridPane.add(choiceBox5, 0, 1);


            confirm.setOnAction(event1 -> {
                Alert alert;
                int foodId = foodIdLookup.get(choiceBox5.getValue());
                if (ServerCommunication.deleteFood(foodId)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully deleted a food item");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully deleted a food item");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to delete a food item");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to delete a food item");
                }
                alert.showAndWait();
            });


        }
    };
    private EventHandler<ActionEvent> restaurantToDelete = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            restaurantSetUp();
            int restaurantId = restaurantIdLookUp.get(choiceBox4.getValue());
            Alert alert;
            if (ServerCommunication.deleteRestaurant(restaurantId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a restaurant");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a restaurant");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a restaurant");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a restaurant");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> roomToDelete = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            roomSetUp();
            int roomId = roomIdLookup.get(choiceBox1.getValue());
            Alert alert;
            if (ServerCommunication.deleteRoom(roomId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a room");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a room");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a room");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a room");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> addRoom = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            addCancelButton();
            choiceBox = new ChoiceBox<>();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            info = new Label("Choose a building");
            gridPane.add(info, 0, 0);
            info1 = new Label("Enter name");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Enter size");
            info3 = new Label("Add description");
            text3 = new TextField();
            gridPane.add(info2, 0, 2);
            gridPane.add(info3, 0, 3);
            gridPane.add(text3, 1, 3);
            GridPane.setMargin(info, new Insets(5, 5, 0, 0));
            GridPane.setMargin(info1, new Insets(5, 5, 0, 0));
            GridPane.setMargin(info2, new Insets(5, 5, 0, 0));
            GridPane.setMargin(info3, new Insets(5, 5, 0, 0));
            GridPane.setMargin(text3, new Insets(15, 5, 0, 0));
            gridSetUp();
            gridPane.getChildren().remove(text);
            buildingSetUp();
            gridPane.add(choiceBox, 1, 0);
            gridPane.getChildren().remove(reminder);
            confirm.setOnAction(event1 -> {
                int buildingId = buildingIdLookup.get(choiceBox.getValue());
                String name = text1.getText();
                int size = Integer.parseInt(text2.getText());
                String description = text3.getText();
                Alert alert;
                if (ServerCommunication.addRoom(buildingId, name, size, description)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added a room");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added a room");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to add a room");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add a room");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> addMenu = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            info = new Label("Name");
            gridPane.add(info, 0, 0);
            info1 = new Label("Description");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Price");
            info3 = new Label("Type");
            gridPane.add(info2, 0, 2);
            TextField text3 = new TextField();

            gridPane.add(info3, 0, 4);
            gridPane.add(text3, 1, 4);
            GridPane.setMargin(info, new Insets(15, 5, 0, 0));
            GridPane.setMargin(info1, new Insets(15, 5, 0, 0));
            GridPane.setMargin(info2, new Insets(15, 5, 0, 0));
            GridPane.setMargin(info3, new Insets(15, 5, 0, 0));

            gridSetUp();
            gridPane.getChildren().remove(reminder);
            gridPane.getChildren().remove(confirm);
            gridPane.add(reminder, 0, 5);
            gridPane.add(confirm, 1, 5);

            GridPane.setMargin(text3, new Insets(15, 0, 0, 0));
            confirm.setOnAction(event1 -> {
                restaurantId = restaurantIdLookUp.get(choiceBox4.getValue());
                name = text.getText();
                description = text1.getText();
                price = Double.parseDouble(text2.getText());
                type = text3.getText();
                Alert alert;
                if (ServerCommunication.addFoodToRes(restaurantId, name,
                    description, price, type)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added food to a restaurant");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added food to a restaurant");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to add food to a restaurant");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add food to a restaurant");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> addBuilding = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            info = new Label("Enter name");
            gridPane.add(info, 0, 0);
            info1 = new Label("Enter opening hour");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Enter closing hour");
            gridPane.add(info2, 0, 2);
            GridPane.setMargin(info, new Insets(15, 15, 0, 0));
            GridPane.setMargin(info1, new Insets(15, 15, 0, 0));
            GridPane.setMargin(info2, new Insets(15, 15, 0, 0));
            gridSetUp();
            gridPane.getChildren().remove(reminder);
            confirm.setOnAction(event1 -> {
                String name = text.getText();
                String openingHour = text1.getText();
                String closingHour = text2.getText();
                Alert alert;
                if (ServerCommunication.addBuilding(name, openingHour, closingHour)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added a building");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added a building");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to add a building");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add a building");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> addBike = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            choiceBox = new ChoiceBox<>();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            buildingSetUp();
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            info = new Label("Enter building");
            gridPane.add(info, 0, 0);
            GridPane.setMargin(info, new Insets(0, 15, 0, 0));
            gridPane.add(choiceBox, 1, 0);
            GridPane.setMargin(choiceBox, new Insets(0, 15, 0, 0));
            confirm = new Button("Confirm");
            gridPane.add(confirm, 2, 0);
            confirm.setOnAction(event1 -> {
                int buildingId = buildingIdLookup.get(choiceBox.getValue());
                Alert alert;
                if (ServerCommunication.addBike(buildingId)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added a bike");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added a bike");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to add a bike");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add a bike");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> addFacility = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            searchRoom();
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            info = new Label("Enter facility");
            text = new TextField();
            gridPane.add(info, 0, 0);
            GridPane.setMargin(info, new Insets(0, 15, 0, 0));
            gridPane.add(text, 1, 0);
            GridPane.setMargin(text, new Insets(0, 15, 0, 0));
            confirm = new Button("Confirm");
            gridPane.add(confirm, 0, 5);
            GridPane.setMargin(confirm, new Insets(15, 5, 0, 0));
            gridPane.add(choiceBox1, 3, 0);
            GridPane.setMargin(choiceBox1, new Insets(15, 5, 0, 0));
            Label buildingInfo = new Label("Choose room");
            gridPane.add(buildingInfo, 2, 0);
            GridPane.setMargin(buildingInfo, new Insets(0, 5, 0, 0));
            Label roomUpdated = new Label("Rooms to be updated:\n");
            gridPane.add(roomUpdated, 0, 3);
            GridPane.setMargin(roomUpdated, new Insets(0, 0, 0, 0));
            Button addRoom = new Button("add room");
            gridPane.add(addRoom, 3, 1);
            GridPane.setMargin(addRoom, new Insets(0, 0, 0, 0));
            addRoom.setOnAction(event2 -> {
                int roomId = roomIdLookup.get(choiceBox1.getValue());
                if (!toUpdate.contains(roomId)) {
                    toUpdate.add(roomId);
                    roomUpdated.setText(roomUpdated.getText() + choiceBox1.getValue() + "\n");
                }
            });
            confirm.setOnAction(event1 -> {
                String name = text.getText();
                Alert alert;
                if (name.equals("")) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Please enter a name");
                    alert.setHeaderText(null);
                    alert.setContentText("No name entered");
                    alert.show();
                    return;
                }
                if (ServerCommunication.addFacility(name, toUpdate)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added a facility");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added a facility");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to add a facility");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add a facility");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> addRestaurant = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            choiceBox = new ChoiceBox<>();
            addCancelButton();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(85);
            gridPane.setLayoutX(85);
            buildingSetUp();
            info3 = new Label("Choose building: ");
            gridPane.add(info3, 0, 3);
            gridPane.add(choiceBox, 1, 3);
            GridPane.setMargin(choiceBox, new Insets(15, 0, 0, 0));
            info = new Label("Enter restaurant name");
            gridPane.add(info, 0, 0);
            info1 = new Label("Enter opening hour");
            gridPane.add(info1, 0, 1);
            info2 = new Label("Enter closing hour");
            gridPane.add(info2, 0, 2);
            GridPane.setMargin(info, new Insets(15, 15, 0, 0));
            GridPane.setMargin(info1, new Insets(15, 15, 0, 0));
            GridPane.setMargin(info2, new Insets(15, 15, 0, 0));
            GridPane.setMargin(info3, new Insets(15, 15, 0, 0));
            gridSetUp();
            gridPane.getChildren().remove(reminder);
            confirm.setOnAction(event1 -> {
                int buildingId = buildingIdLookup.get(choiceBox.getValue());
                String name = text.getText();
                String openingHour = text1.getText();
                String closingHour = text2.getText();
                Alert alert;
                if (ServerCommunication.addRestaurant(buildingId, name, openingHour, closingHour)) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Successfully added a restaurant");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully added a restaurant");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Failed to add a restaurant");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to add a restaurant");
                }
                alert.showAndWait();
            });
        }
    };
    private EventHandler<ActionEvent> deleteUser = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            int userId = Integer.parseInt(textField.getText());
            Alert alert;
            if (ServerCommunication.deleteUser(userId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a user");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a user");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a user");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a user");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> deleteBike = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            int bikeId = bikeIdLookup.get(choiceBox6.getValue());
            Alert alert;
            if (ServerCommunication.deleteBike(bikeId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a bike");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a bike");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a bike");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a bike");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> deleteFacility = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            int facilityId = facilityIdLookup.get(choiceBox3.getValue());
            Alert alert;
            if (ServerCommunication.deleteFacility(facilityId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a facility");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a facility");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a facility");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a facility");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> deleteReservation = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            int reservationId = Integer.parseInt(textField.getText());
            Alert alert;
            if (ServerCommunication.deleteReservation(reservationId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a reservation");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a reservation");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a reservation");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a reservation");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> authoriseUser = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            int userID = Integer.parseInt(textField.getText());
            String type = choiceBox.getValue();
            Alert alert;
            boolean resp = false;
            try {
                resp = Boolean.valueOf(ServerCommunication.authorizeUser(userID, type));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (resp) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully authorised the user");
                alert.setHeaderText(null);
                alert.setContentText("Successfully authorised the user");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to authorise the user");
                alert.setHeaderText(null);
                alert.setContentText("Failed to authorise the user");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> addFood = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRestaurant();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(addMenu);
        }
    };
    private EventHandler<ActionEvent> removeBuilding = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchBuilding();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(buildingToDelete);
        }
    };
    private EventHandler<ActionEvent> removeFood = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRestaurant();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(foodToDelete);
        }
    };
    private EventHandler<ActionEvent> removeBike = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchBike();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(deleteBike);
        }
    };
    private EventHandler<ActionEvent> deleteFacilityFromRoom = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            int facilityId = facilityIdLookup.get(choiceBox3.getValue());
            int roomId = roomIdLookup.get(choiceBox1.getValue());
            Alert alert;
            if (ServerCommunication.deleteFacilityFromRoom(facilityId, roomId)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Successfully deleted a facility from the room");
                alert.setHeaderText(null);
                alert.setContentText("Successfully deleted a facility from the room");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to delete a facility from the room");
                alert.setHeaderText(null);
                alert.setContentText("Failed to delete a facility from the room");
            }
            alert.showAndWait();
        }
    };
    private EventHandler<ActionEvent> removeFacilityFromRoom = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            searchFacilityAndRooms();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(deleteFacilityFromRoom);
        }
    };
    private EventHandler<ActionEvent> removeFacility = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchFacility();
            gridPane = new GridPane();
            anchorPane.getChildren().add(gridPane);
            gridPane.setLayoutY(150);
            gridPane.setLayoutX(150);
            Button b = new Button("remove from single room instead");
            gridPane.add(b, 3, 1);
            GridPane.setMargin(b, new Insets(0, 0, 0, 0));
            b.setOnAction(removeFacilityFromRoom);
            anchorPane.getChildren().remove(textField);
            search.setOnAction(deleteFacility);
        }
    };
    private EventHandler<ActionEvent> removeRoom = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRoom();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(roomToDelete);
        }
    };
    private EventHandler<ActionEvent> removeRestaurant = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRestaurant();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(restaurantToDelete);
        }
    };
    private EventHandler<ActionEvent> editBuilding = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchBuilding();
            labelInfo = new Label("You are now editing a building");
            labelInfo.setId("information");
            anchorPane.getChildren().add(labelInfo);
            labelInfo.setLayoutX(40);
            labelInfo.setLayoutY(30);
            anchorPane.getChildren().remove(textField);
            search.setOnAction(buildingToModify);
        }
    };
    private EventHandler<ActionEvent> editRoom = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRoom();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(roomToModify);
        }
    };
    private EventHandler<ActionEvent> editFood = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRestaurant();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(foodToModify);
        }
    };
    private EventHandler<ActionEvent> editBike = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            choiceBox6 = new ChoiceBox<>();
            anchorPane.getChildren().clear();
            bikeSetUpEdit();
            anchorPane.getChildren().add(choiceBox6);
            label.setText("Choose a bike");
            addCancelButton();
            search = new Button();
            search.setText("Confirm");
            anchorPane.getChildren().add(search);
            anchorPane.getChildren().add(label);
            search.setLayoutX(150);
            search.setLayoutY(300);
            label.setLayoutX(30);
            label.setLayoutY(87);
            choiceBox6.setLayoutX(250);
            choiceBox6.setLayoutY(87);
            search.setOnAction(bikeToModify);
        }
    };
    private EventHandler<ActionEvent> editRestaurant = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            searchRestaurant();
            anchorPane.getChildren().remove(textField);
            search.setOnAction(restaurantToModify);
        }
    };
    private EventHandler<ActionEvent> addHoliday = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            anchorPane.getChildren().clear();
            gridPane = new GridPane();
            anchorPane.getChildren().add(gridPane);
            Label nameInfo = new Label("enter name");
            gridPane.add(nameInfo, 0, 3);
            TextField enterName = new TextField();
            gridPane.add(enterName, 1, 3);
            Label startInfo = new Label(("choose start date of holiday"));
            gridPane.add(startInfo, 0, 4);
            DatePicker startPicker = new DatePicker();
            gridPane.add(startPicker, 1, 4);
            Label endInfo = new Label("choose end date of holiday");
            gridPane.add(endInfo, 0, 5);
            DatePicker endPicker = new DatePicker();
            gridPane.add(endPicker, 1, 5);
            startPicker.setValue(LocalDate.now());
            endPicker.setValue(LocalDate.now().plusDays(7));
            ChoiceBox chooseRepeat = new ChoiceBox();
            chooseRepeat.getItems().add("Is a repeating holiday?");
            chooseRepeat.getItems().add("yes");
            chooseRepeat.getItems().add("no");
            chooseRepeat.setValue("Is a repeating holiday?");
            gridPane.add(chooseRepeat, 1, 6);
            Button createButton = new Button("create");
            gridPane.add(createButton, 1, 9);
            createButton.setOnAction(event1 -> {
                if (enterName.getText() != "") {
                    Holiday holiday;
                    if (endPicker.getValue().isAfter(startPicker.getValue())) {
                        if (chooseRepeat.getValue() == "yes") {
                            holiday = new Holiday(enterName.getText(),
                                startPicker.getValue(), endPicker.getValue(), true);
                        } else {
                            holiday = new Holiday(enterName.getText(),
                                startPicker.getValue(), endPicker.getValue(), false);
                        }
                        JsonObject jsonObject = ServerCommunication.addHoliday(holiday);
                        if (jsonObject.get("status").getAsInt() == 200) {
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setContentText(null);
                            a.setHeaderText("Successfully added a holiday");
                            a.show();
                        } else {
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setContentText(null);
                            a.setHeaderText("Failed to add a holiday");
                            a.show();
                        }
                    }
                }
            });
        }
    };
    private EventHandler<ActionEvent> deleteHoliday = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            setUpHolidays();
        }
    };

    /**
     * Find holidays in server and add them to the table.
     */
    private void setUpHolidays() {
        TableColumn<Holiday, String> nameColumn = new TableColumn<>("name");
        TableColumn<Holiday, LocalDate> startColumn = new TableColumn<>("start");
        TableColumn<Holiday, LocalDate> endColumn = new TableColumn<>("end");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        holidayTable.getColumns().addAll(nameColumn, startColumn, endColumn);
        JsonParser.parseString(ServerCommunication.getHolidays())
            .getAsJsonArray().forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                LocalDate start = LocalDate.parse(jsonObject.get("start").getAsString());
                LocalDate end = LocalDate.parse(jsonObject.get("end").getAsString());
                int id = jsonObject.get("id").getAsInt();
                Holiday holiday = new Holiday(id, name, start, end);
                holidayTable.getItems().add(holiday);
            });
        anchorPane.getChildren().add(holidayTable);
        holidayTable.setRowFactory(tv -> {
            TableRow<Holiday> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    rowHoliday = row.getItem();
                }
            });
            return row;
        });
        Button confirmButton = new Button("delete selected Holiday");
        anchorPane.getChildren().add(confirmButton);
        confirmButton.setLayoutY(200);
        confirmButton.setLayoutX(500);
        confirmButton.setOnAction(tv -> {
            if (rowHoliday != null) {
                boolean isSuccess = ServerCommunication.deleteHoliday(rowHoliday.getId());
                if (isSuccess) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setHeaderText(null);
                    a.setContentText("deletion successful");
                    a.show();
                    holidayTable.getItems().remove(rowHoliday);
                } else {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setHeaderText(null);
                    a.setContentText("deletion failed");
                    a.show();
                }
            }
        });
    }

    /**
     * Method that sets up the grid.
     */
    public void gridSetUp() {
        text = new TextField();
        gridPane.add(text, 1, 0);
        text1 = new TextField();
        gridPane.add(text1, 1, 1);
        text2 = new TextField();
        gridPane.add(text2, 1, 2);
        GridPane.setMargin(text, new Insets(15, 15, 0, 0));
        GridPane.setMargin(text1, new Insets(15, 15, 0, 0));
        GridPane.setMargin(text2, new Insets(15, 15, 0, 0));
        confirm = new Button("Confirm");
        reminder = new Label("If you don't enter anything,"
            + '\n' + " that value won't change");
        gridPane.add(reminder, 0, 4);
        gridPane.add(confirm, 1, 4);
        GridPane.setMargin(confirm, new Insets(15, 15, 0, 0));
        GridPane.setMargin(reminder, new Insets(15, 15, 0, 0));

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            VBox box = FXMLLoader.load(getClass().getResource("/AdminSideBar.fxml"));
            drawer.setSidePane(box);
            for (Node node : box.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getAccessibleText()) {
                            case "addBuildingRoom":
                                addBuildingRoom();
                                break;
                            case "editBuildingRoom":
                                editBuildingRoom();
                                break;
                            case "deleteBuildingRoom":
                                removeBuildingRoom();
                                break;
                            case "removeUser":
                                labelInfo = new Label("Write the user id to be deleted");
                                labelInfo.setId("information");
                                removeAccount();
                                break;
                            case "cancelReservation":
                                labelInfo = new Label("Write the reservation id to be canceled");
                                labelInfo.setId("information");
                                infoSetUp();
                                confirm.setOnAction(deleteReservation);
                                break;
                            case "editHolidays":
                                editHolidays();
                                break;
                            case "authoriseUser":
                                userSetUp();
                                confirm.setOnAction(authoriseUser);
                                break;
                            case "getLateBikes":
                                try {
                                    getLateBikes();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case "logout":
                                MainSceneController.logout();
                                break;
                            default:
                                break;
                        }

                    });
                }
            }
            HamburgerBackArrowBasicTransition taskNew = new HamburgerBackArrowBasicTransition(ham);
            taskNew.setRate(-1);
            ham.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
                drawer.toggle();
            });
            drawer.setOnDrawerOpening((event) -> {
                taskNew.setRate(taskNew.getRate() * -1);
                taskNew.play();
            });
            drawer.setOnDrawerClosed((event) -> {
                drawer.toBack();
                taskNew.setRate(taskNew.getRate() * -1);
                taskNew.play();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editHolidays() {
        anchorPane.getChildren().clear();
        labelInfo = new Label("What would you like to do?");
        labelInfo.setId("information");
        anchorPane.getChildren().add(labelInfo);
        addCancelButton();
        Button create = new Button("create");
        Button delete = new Button("delete");
        create.setLayoutX(20);
        delete.setLayoutX(180);
        create.setLayoutY(100);
        delete.setLayoutY(100);
        create.setOnAction(addHoliday);
        delete.setOnAction(deleteHoliday);
        anchorPane.getChildren().add(create);
        anchorPane.getChildren().add(delete);
    }


    /**
     * method that calls another method that decides
     * if the client wants to edit a room or a building.
     */
    public void editBuildingRoom() {
        buildingOrRoom();
        label = new Label();
        label.setText("Choose what you want to edit");
        label.setLayoutX(150);
        label.setLayoutY(30);
        label.setId("information");
        anchorPane.getChildren().add(label);
        anchorPane.getChildren().remove(facilityB);
        buildingB.setOnAction(editBuilding);
        roomB.setOnAction(editRoom);
        bikeB.setOnAction(editBike);
        menuB.setOnAction(editFood);
        restaurantB.setOnAction(editRestaurant);
        restaurantB.setLayoutX(410);
        restaurantB.setLayoutY(173);
        menuB.setLayoutX(510);
    }

    /**
     * method that calls another method that decides
     * if the client wants to add a room or a building.
     */
    public void addBuildingRoom() {
        buildingOrRoom();
        label = new Label();
        label.setText("Choose what you want to add");
        label.setId("information");
        label.setLayoutX(150);
        label.setLayoutY(30);
        anchorPane.getChildren().add(label);
        buildingB.setOnAction(addBuilding);
        roomB.setOnAction(addRoom);
        bikeB.setOnAction(addBike);
        facilityB.setOnAction(addFacility);
        restaurantB.setOnAction(addRestaurant);
        menuB.setOnAction(addFood);
    }

    /**
     * method that calls another method that decides
     * if the client wants to delete a room or a building.
     */
    public void removeBuildingRoom() {
        buildingOrRoom();
        label = new Label();
        label.setText("Choose what you want to remove");
        label.setLayoutX(150);
        label.setLayoutY(30);
        label.setId("information");
        anchorPane.getChildren().add(label);
        buildingB.setOnAction(removeBuilding);
        roomB.setOnAction(removeRoom);
        bikeB.setOnAction(removeBike);
        facilityB.setOnAction(removeFacility);
        restaurantB.setOnAction(removeRestaurant);
        menuB.setOnAction(removeFood);
    }

    /**
     * method that calls another method that decides
     * which account to be removed.
     */
    public void removeAccount() {
        searchUser();
        search.setOnAction(deleteUser);
        infoSetUp();
        confirm.setOnAction(deleteUser);
    }

    /**
     * Method that sets up a text field,
     * and a label, also adds a cancel and
     * confirm button.
     */
    public void infoSetUp() {
        anchorPane.getChildren().clear();
        textField = new TextField();
        anchorPane.getChildren().add(labelInfo);
        anchorPane.getChildren().add(textField);
        textField.setLayoutX(112);
        textField.setLayoutY(85);
        labelInfo.setLayoutX(112);
        labelInfo.setLayoutY(30);
        addConfirmButton();
        addCancelButton();
    }

    /**
     * method that calls another method that decides
     * which account to be edited.
     *
     * @param actionEvent the event trigger
     */

    public void editUser(ActionEvent actionEvent) {
        searchUser();
    }


    private void searchBuilding() {
        enterId(0);
    }


    private void searchRoom() {
        enterId(1);
    }

    private void searchUser() {
        enterId(2);
    }

    private void searchBike() {
        enterId(3);
    }

    private void searchRestaurant() {
        enterId(5);
    }

    private void searchFacility() {
        enterId(4);
    }

    private void searchFacilityAndRooms() {
        enterId(6);
    }

    private void enterId(int option) {  // option = 0 -> roomId else buildingId
        anchorPane.getChildren().clear();
        textField = new TextField();
        label = new Label();
        choiceBox = new ChoiceBox<>();
        choiceBox1 = new ChoiceBox<>();
        choiceBox6 = new ChoiceBox<>();
        choiceBox3 = new ChoiceBox<>();
        choiceBox4 = new ChoiceBox<>();
        if (option == 0) {
            buildingSetUp();
            label.setText("Choose a building");
            anchorPane.getChildren().add(choiceBox);
            choiceBox.setLayoutX(175);
            choiceBox.setLayoutY(85);
        } else if (option == 1) {
            roomSetUp();
            label.setText("Choose a room");
            anchorPane.getChildren().add(choiceBox1);
            choiceBox1.setLayoutX(150);
            choiceBox1.setLayoutY(85);
        } else if (option == 3) {
            bikeSetUpEdit();
            anchorPane.getChildren().add(choiceBox6);
            choiceBox6.setLayoutX(150);
            choiceBox6.setLayoutY(85);
            label.setText("Choose a bike");
        } else if (option == 4) {
            facilitySetUp();
            anchorPane.getChildren().add(choiceBox3);
            choiceBox3.setLayoutX(150);
            choiceBox3.setLayoutY(85);
            label.setText("Choose a facility");
        } else if (option == 5) {
            restaurantSetUp();
            anchorPane.getChildren().add(choiceBox4);
            choiceBox4.setLayoutX(175);
            choiceBox4.setLayoutY(85);
            label.setText("Choose a restaurant");
        } else if (option == 6) {
            roomSetUp();
            label.setText("Choose a room and a facility");
            anchorPane.getChildren().add(choiceBox1);
            choiceBox1.setLayoutX(300);
            choiceBox1.setLayoutY(85);
            facilitySetUp();
            anchorPane.getChildren().add(choiceBox3);
            choiceBox3.setLayoutX(300);
            choiceBox3.setLayoutY(170);
        } else {
            label.setText("Enter user id");
        }
        addCancelButton();
        search = new Button();
        search.setText("Confirm");
        anchorPane.getChildren().add(textField);
        anchorPane.getChildren().add(search);
        anchorPane.getChildren().add(label);
        search.setLayoutX(150);
        search.setLayoutY(300);
        label.setLayoutX(30);
        label.setLayoutY(87);
        textField.setLayoutX(150);
        textField.setLayoutY(85);
    }

    /**
     * Used to set up the scene to authorise an user.
     */
    public void userSetUp() {
        anchorPane.getChildren().clear();
        gridPane = new GridPane();
        anchorPane.getChildren().add(gridPane);
        gridPane.setLayoutY(85);
        gridPane.setLayoutX(85);
        info = new Label("Enter id");
        gridPane.add(info, 0, 0);
        textField = new TextField();
        gridPane.add(textField, 1, 0);
        info2 = new Label("Select user type");
        gridPane.add(info2, 0, 1);
        choiceBox = new ChoiceBox<>();
        choiceBox.getItems().add("Admin");
        choiceBox.getItems().add("Employee");
        anchorPane.getChildren().add(choiceBox);
        gridPane.add(choiceBox, 1, 1);
        confirm = new Button("Confirm");
        gridPane.add(confirm, 1, 2);
        GridPane.setMargin(info, new Insets(5, 5, 0, 0));
        GridPane.setMargin(choiceBox, new Insets(10, 5, 0, 0));
        GridPane.setMargin(info2, new Insets(5, 5, 0, 0));
        GridPane.setMargin(confirm, new Insets(20, 5, 0, 0));
        addCancelButton();
    }

    /**
     * method that decides
     * if the client wants to delete a room, a building,
     * a bike or a facility.
     */
    public void buildingOrRoom() {
        anchorPane.getChildren().clear();
        //   anchorPain.setVisible(true);
        addCancelButton();
        buildingB = new Button();
        roomB = new Button();
        bikeB = new Button();
        facilityB = new Button();
        restaurantB = new Button();
        menuB = new Button();
        buildingB.setText("Building");
        roomB.setText("Room");
        bikeB.setText("Bike");
        facilityB.setText("Facility");
        restaurantB.setText("Restaurant");
        menuB.setText("Menu");
        buildingB.setLayoutX(207);
        buildingB.setLayoutY(173);
        roomB.setLayoutX(288);
        roomB.setLayoutY(173);
        bikeB.setLayoutX(355);
        bikeB.setLayoutY(173);
        facilityB.setLayoutX(410);
        facilityB.setLayoutY(173);
        restaurantB.setLayoutX(485);
        restaurantB.setLayoutY(173);
        menuB.setLayoutX(585);
        menuB.setLayoutY(173);
        anchorPane.getChildren().add(buildingB);
        anchorPane.getChildren().add(roomB);
        anchorPane.getChildren().add(bikeB);
        anchorPane.getChildren().add(facilityB);
        anchorPane.getChildren().add(restaurantB);
        anchorPane.getChildren().add(menuB);

    }

    /**
     * Method that adds a confirm button.
     */
    private void addConfirmButton() {
        confirm = new Button("Confirm");
        anchorPane.getChildren().add(confirm);
        confirm.setLayoutX(265);
        confirm.setLayoutY(85);
    }

    /**
     * Method that adds a cancel button.
     */
    private void addCancelButton() {
        Button cancelB = new Button("Cancel");
        anchorPane.getChildren().add(cancelB);
        cancelB.setLayoutX(350);
        cancelB.setLayoutY(300);
        cancelB.setOnMouseClicked(e -> {
            cancel();
        });
    }

    /**
     * Method that calls from the server all the
     * late bikes.
     */
    public void getLateBikes() throws Exception {
        new LateBikeListView().start(MainApp.stage);
    }

    /**
     * Method that returns you back to the Admin page.
     */
    public void cancel() {
        try {
            new AdminView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that add all buildings to a choicebox.
     */
    public void buildingSetUp() {
        buildingIdLookup = new Hashtable<String, Integer>();
        JsonParser.parseString(ServerCommunication.getBuildings()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                choiceBox.getItems().add(name);
                buildingIdLookup.put(name, jsonObject.get("id").getAsInt());
            });
    }

    /**
     * Method that add rooms to a choicebox.
     */
    public void roomSetUp() {
        roomIdLookup = new Hashtable<String, Integer>();
        JsonParser.parseString(ServerCommunication.getRooms()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                choiceBox1.getItems().add(name);
                roomIdLookup.put(name, jsonObject.get("id").getAsInt());
            });
    }

    /**
     * Method that add all restaurants to a choicebox.
     */
    public void restaurantSetUp() {
        restaurantIdLookUp = new Hashtable<String, Integer>();
        JsonParser.parseString(ServerCommunication.getRestaurants()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                choiceBox4.getItems().add(name);
                restaurantIdLookUp.put(name, jsonObject.get("id").getAsInt());
            });
    }

    /**
     * Method that add all bikes
     * without building to a choicebox.
     */
    public void bikeSetUpEdit() {
        bikeIdLookup = new Hashtable<String, Integer>();
        JsonParser.parseString(ServerCommunication.getBikesWithBuilding()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("id").getAsString();
                choiceBox6.getItems().add(name);
                bikeIdLookup.put(name, jsonObject.get("id").getAsInt());
            });
    }

    /**
     * Method that add all facilities in a choicebox.
     */
    public void facilitySetUp() {
        facilityIdLookup = new Hashtable<String, Integer>();
        JsonParser.parseString(ServerCommunication.getFacility()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                choiceBox3.getItems().add(name);
                facilityIdLookup.put(name, jsonObject.get("id").getAsInt());
            });
    }

    /**
     * Method that add all food items by
     * restaurant in a choicebox.
     */
    public void foodSetUp() {
        int restaurantId = restaurantIdLookUp.get(choiceBox4.getValue());
        foodIdLookup = new Hashtable<String, Integer>();
        JsonParser.parseString(ServerCommunication.getFoodByRestaurant(restaurantId))
            .getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int id = jsonObject.get("id").getAsInt();
                String name = jsonObject.get("name").getAsString();
                choiceBox5.getItems().add(name);
                foodIdLookup.put(name, jsonObject.get("id").getAsInt());
            });
    }
}


