package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.Basket;
import application.entities.Food;
import application.entities.Restaurant;
import application.entities.UserManager;
import application.views.MainView;
import application.views.OrderConfirmView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrderFoodSceneController extends MainSceneController implements Initializable {

    @FXML
    private TableView<Restaurant> restaurants;

    @FXML
    private TableColumn<String, Restaurant> restaurantName;

    @FXML
    private TableColumn<String, Restaurant> restaurantLocation;

    @FXML
    private TableColumn<Time, Restaurant> openingTime;

    @FXML
    private TableColumn<Time, Restaurant> closingTime;

    @FXML
    private TableView<Food> menus;

    @FXML
    private TableColumn<String, Food> name;

    @FXML
    private TableColumn<String, Food> description;

    @FXML
    private TableColumn<Double, Food> price;

    @FXML
    private Button orderFood;

    @FXML
    private Button orderAtOther;

    @FXML
    private Button addToCart;

    @FXML
    private Button toggleBasket;

    @FXML
    private Button clearBasket;

    @FXML
    private Text informationText;

    private Basket basket;

    private boolean activeBasket;

    private boolean activeRestaurant;

    private int restaurantId;

    private Food rowData;

    private Restaurant rowRestaurant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restaurantName.setCellValueFactory(new PropertyValueFactory<>("name"));
        restaurantLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        openingTime.setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        closingTime.setCellValueFactory(new PropertyValueFactory<>("closingTime"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        restaurants.setRowFactory(tv -> {
            TableRow<Restaurant> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    rowRestaurant = row.getItem();
                }
            });
            return row;
        });
        menus.setRowFactory(tv -> {
            TableRow<Food> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    rowData = row.getItem();
                }
            });
            return row;
        });
        if (UserManager.getInstance().getBasket() != null) {
            if (UserManager.getInstance().getBasket().getSize() > 0) {
                activeBasket = true;
                activeRestaurant = true;
                orderFood.setText("Buy Now");
                basket = UserManager.getInstance().getBasket();
                restaurantId = UserManager.getInstance().getBasket().getRestaurantId();
                restaurants.setVisible(false);
                loadFoods();
                menus.getItems().clear();
                informationText.setText("Currently viewing your basket");
                for (int i = 0; i < basket.getSize(); i++) {
                    menus.getItems().add(basket.getFood(i));
                }
                setupRestaurants();
            }
        } else {
            menus.setVisible(false);
            addToCart.setVisible(false);
            toggleBasket.setVisible(false);
            clearBasket.setVisible(false);
            activeBasket = false;
            activeRestaurant = false;
            setupRestaurants();
        }
        setUpMenu();
    }

    private void setupRestaurants() {
        JsonParser.parseString(ServerCommunication.getRestaurants()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonObject buildingObject = jsonObject.get("building").getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                int id = jsonObject.get("id").getAsInt();
                Time open = Time.valueOf(jsonObject.get("openingTime").getAsString());
                Time close = Time.valueOf(jsonObject.get("closingTime").getAsString());
                String location = buildingObject.get("name").getAsString();
                Restaurant restaurant = new Restaurant(id, name, open, close, location);
                restaurants.getItems().add(restaurant);
            });
    }

    /**
     * Handles clicking the order here/order now button.
     * Function changes when restaurant is selected
     */
    public void orderFood() {
        if (activeRestaurant == false) {
            if (rowRestaurant == null) {
                return;
            }
            restaurantId = rowRestaurant.getId();
            loadFoods();
            restaurants.setVisible(false);
            menus.setVisible(true);
            basket = new Basket(restaurantId);
            toggleBasket.setVisible(true);
            clearBasket.setVisible(true);
            addToCart.setVisible(true);
            orderFood.setText("Buy Now");
            activeRestaurant = true;
            informationText.setText("Choose an item to add to your basket");
        } else {
            if (basket.isEmpty()) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Your Basket is empty");
                errorAlert.show();
                return;
            }
            UserManager.getInstance().setBasket(basket);
            try {
                new OrderConfirmView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * load the foods from server.
     */
    public void loadFoods() {
        JsonParser.parseString(
            ServerCommunication.getFoodByRestaurant(restaurantId)).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String description = jsonObject.get("description").getAsString();
                double price = jsonObject.get("price").getAsDouble();
                int id = jsonObject.get("id").getAsInt();
                String type = jsonObject.get("type").getAsString();
                Food food = new Food(name, description, price, restaurantId, id, type);
                menus.getItems().add(food);
            });
    }

    /**
     * Handles clicking the add to cart button.
     * Adds items to cart and saves them to basket
     */
    public void addToCart() {
        if (rowData != null) {
            Food food = rowData;
            basket.add(food);
            if (activeBasket && food != null) {
                menus.getItems().add(food);
            }
        }
    }

    /**
     * Handles clicking the toggle basket button.
     * The basket is revealed.
     */
    public void toggleBasket() {
        menus.getItems().clear();
        if (activeBasket == true) {
            loadFoods();
            informationText.setText("Choose an item to add to your basket");
            activeBasket = false;
        } else {
            for (int i = 0; i < basket.getSize(); i++) {
                menus.getItems().add(basket.getFood(i));
            }
            informationText.setText("Currently viewing your basket");
            activeBasket = true;
        }
    }

    /**
     * Clears all items in basket and lets user choose a different restaurant.
     * If user is still choosing restaurant returns to the main scene instead.
     */
    public void backButton(ActionEvent actionEvent) throws Exception {
        if (activeRestaurant) {
            menus.getItems().clear();
            addToCart.setVisible(false);
            toggleBasket.setVisible(false);
            clearBasket.setVisible(false);
            activeRestaurant = false;
            restaurants.setVisible(true);
            orderFood.setText("Order Here");
            informationText.setText("Please Choose a restaurant to order from");
            basket = new Basket(UserManager.getInstance().getReservationId());
            return;
        }
        Button b = (Button) actionEvent.getTarget();
        Scene s = b.getScene();
        Stage stage = (Stage) s.getWindow();
        stage.close();
        MainView m = new MainView();
        m.start(stage);
    }

    /**
     * Button to clear all items in basket.
     */
    public void clearBasket() {
        basket.getFoods().clear();
        if (activeBasket) {
            menus.getItems().clear();
        }
        if (UserManager.getInstance().getBasket() != null) {
            UserManager.getInstance().setBasket(null);
        }
    }
}
