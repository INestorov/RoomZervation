package application.controllers;

import application.communication.ServerCommunication;
import application.entities.Food;
import application.entities.OrderTitle;
import application.views.MainView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class MyOrderSceneController extends MainSceneController implements Initializable {

    @FXML
    private TableView<OrderTitle> restaurants;

    @FXML
    private TableColumn<String, OrderTitle> restaurantName;

    @FXML
    private TableColumn<Date, OrderTitle> time;

    @FXML
    private TableColumn<Double, OrderTitle> totalPrice;

    @FXML
    private TableColumn<Double, Food> price;

    @FXML
    private TableColumn<Integer, Food> amount;

    @FXML
    private TableColumn<String, Food> foodName;

    @FXML
    private TableColumn<String, Food> description;

    @FXML
    private TableView<Food> items;

    @FXML
    private Button back;

    @FXML
    private Button moreInformation;

    private OrderTitle rowData;

    private boolean isViewingOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isViewingOrder = false;
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        foodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        items.setVisible(false);
        restaurantName.setCellValueFactory(new PropertyValueFactory<>("name"));
        totalPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        restaurants.setRowFactory(tv -> {
            TableRow<OrderTitle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    rowData = row.getItem();
                }
            });
            return row;
        });
        String username = LoginSceneController.getUser();
        JsonParser.parseString(ServerCommunication.getOrders(username)).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date time = null;
                try {
                    time = formatter.parse(jsonObject.get("date").getAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JsonArray jsonArray = jsonObject.get("foods").getAsJsonArray();
                String restaurantName = "";
                int totalPrice = 0;
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject itemObject = jsonArray.get(i).getAsJsonObject();
                    JsonObject foodObject = itemObject.get("food").getAsJsonObject();
                    if (i == 0) {
                        restaurantName = foodObject.get("restaurant").getAsJsonObject()
                            .get("name").getAsString();
                    }
                    totalPrice += foodObject.get("price").getAsInt()
                        * itemObject.get("amount").getAsInt();
                }
                int id = jsonObject.get("id").getAsInt();
                OrderTitle orderTitle = new OrderTitle(totalPrice, restaurantName, time, id);
                restaurants.getItems().add(orderTitle);
            });
        setUpMenu();
    }

    /**
     * Gets all food of clicked order;
     * Shows items table.
     */
    public void moreInformation() {
        if (rowData != null) {
            restaurants.setVisible(false);
            JsonObject jsonObject = JsonParser.parseString(
                ServerCommunication.getOrderById(rowData.getId())).getAsJsonObject();
            jsonObject.get("foods").getAsJsonArray()
                .forEach(jsonElement -> {
                    JsonObject itemObject = jsonElement.getAsJsonObject();
                    JsonObject foodObject = itemObject.get("food").getAsJsonObject();
                    String name = foodObject.get("name").getAsString();
                    String description = foodObject.get("description").getAsString();
                    int amount = itemObject.get("amount").getAsInt();
                    double price = amount * foodObject.get("price").getAsInt();
                    Food food = new Food(name, description, price, amount);
                    items.getItems().add(food);
                });
            items.setVisible(true);
            isViewingOrder = true;
        }
    }

    /**
     * Hides items table and shows order table if items are visible.
     * Otherwise go back to the main menu.
     */
    public void backButton(ActionEvent actionEvent) throws Exception {
        if (isViewingOrder) {
            items.setVisible(false);
            items.getItems().clear();
            restaurants.setVisible(true);
            isViewingOrder = false;
            return;
        }
        Button b = (Button) actionEvent.getTarget();
        Scene s = b.getScene();
        Stage stage = (Stage) s.getWindow();
        stage.close();
        MainView m = new MainView();
        m.start(stage);
    }


}
