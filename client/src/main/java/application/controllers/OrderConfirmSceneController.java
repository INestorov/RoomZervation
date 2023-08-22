package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.Basket;
import application.entities.Order;
import application.entities.UserManager;
import application.views.MainView;
import application.views.MyOrdersView;
import application.views.OrderFoodView;
import application.views.ReservationListView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OrderConfirmSceneController extends MainSceneController implements Initializable {

    @FXML
    private Text whenOrder;

    @FXML
    private Text whereOrder;

    @FXML
    private Button orderButton;

    @FXML
    private Button back;

    @FXML
    private Text confirmText;

    @FXML
    private Text finalConfirmText;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField hour;

    @FXML
    private TextField minutes;

    @FXML
    private Text points;

    private GregorianCalendar orderTime = new GregorianCalendar();

    private JsonObject reservation;

    private boolean isReservation;

    private boolean isConfirmed;

    private Date startTime;

    private Date endTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        whenOrder.setVisible(false);
        hour.setVisible(false);
        minutes.setVisible(false);
        points.setVisible(false);
        finalConfirmText.setVisible(false);
        isConfirmed = false;
        isReservation = false;
        if (UserManager.getInstance().getReservationId() == 0) {
            confirmText.setVisible(false);
            confirmButton.setVisible(false);
        } else {
            whereOrder.setVisible(false);
            reservation = JsonParser.parseString(ServerCommunication.getReservation(
                UserManager.getInstance().getReservationId()
            )).getAsJsonObject();
            JsonObject room = reservation.get("room").getAsJsonObject();
            JsonObject building = room.get("building").getAsJsonObject();
            confirmText.setText("Your order will be delivered to "
                + room.get("name").getAsString() + " in "
                + building.get("name").getAsString());
        }
    }

    /**
     * Button to confirm the reservation chosen.
     * Sets isReservation to true.
     */
    public void confirm() {
        if (!isReservation) {
            isReservation = true;
            confirmText.setVisible(false);
            points.setVisible(true);
            hour.setVisible(true);
            minutes.setVisible(true);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            TimeZone.setDefault(TimeZone.getTimeZone("Atlantic/Reykjavik"));
            try {
                startTime = formatter.parse(reservation.get("start").getAsString());
                endTime = formatter.parse(reservation.get("end").getAsString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String startStr = dateFormat.format(startTime);
            DateFormat dateFormatEnd = new SimpleDateFormat("HH:mm");
            String endStr = dateFormatEnd.format(endTime);
            whenOrder.setText("When would you Like to have your order delivered? "
                + "Your reservation starts at " + startStr
                + " and ends at " + endStr + ".");
            whenOrder.setVisible(true);
            orderButton.setText("Confirm");
            confirmButton.setVisible(false);
            back.setText("Back");
        }
        setUpMenu();
    }

    /**
     * Handles clicking the orderButton.
     * If a reservation is not chosen, go to my reservations
     * where you can choose a reservation.
     * If a reservation is chosen and confirmed,
     * Clicking the button will confirm the chosen time.
     * If the chosen time is confirmed,
     * clicking the button will confirm placing the order.
     * After placing the Order user is fast-forwarded to my orders page.
     */
    public void order() {
        if (!isReservation) {
            try {
                new ReservationListView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (!isConfirmed) {
            try {
                int hourOrder = Integer.parseInt(hour.getText());
                int minuteOrder = Integer.parseInt(minutes.getText());
                if (hourOrder > 24 || hourOrder < 0 || minuteOrder > 59 || minuteOrder < 0) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText(null);
                    a.setContentText("please enter a valid time");
                    a.show();
                    return;
                }
                orderTime.setTimeZone(TimeZone.getTimeZone("Atlantic/Reykjavik"));
                orderTime.setTime(startTime);
                orderTime.set(Calendar.HOUR_OF_DAY, hourOrder);
                orderTime.set(Calendar.MINUTE, minuteOrder);
                Date orderTimeToDate = new Date(orderTime.getTimeInMillis());
                if (orderTimeToDate.before(startTime) || orderTimeToDate.after(endTime)) {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText(null);
                    a.setContentText("You cannot order after the "
                        + "end or before the start of your reservation");
                    a.show();
                    return;
                }
                finalConfirmText.setText("Your order will be delivered at " + orderTimeToDate);
                finalConfirmText.setVisible(true);
                points.setVisible(false);
                hour.setVisible(false);
                minutes.setVisible(false);
                isConfirmed = true;
                whenOrder.setVisible(false);
                orderButton.setText("Confirm and order now");
            } catch (NumberFormatException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText(null);
                a.setContentText("Error: please enter valid numbers");
                a.show();
            } finally {
                return;
            }
        } else if (isConfirmed) {
            Basket basket = UserManager.getInstance().getBasket();
            int totalPrice = 0;
            Order order = new Order(UserManager.getInstance().getReservationId(),
                basket.getRestaurantId(), orderTime.getTimeInMillis());
            for (int i = 0; i < basket.getSize(); i++) {
                int n = basket.getFood(i).getId();
                order.getFoodIds().add(n);
                totalPrice += basket.getFood(i).getPrice();
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to place the order?"
                + "\nYou will pay " + totalPrice + " euros for your order");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                JsonObject jsonObject = ServerCommunication.placeOrder(order);
                if (jsonObject.get("status").getAsInt() == 200) {
                    Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                    confirmation.setHeaderText(null);
                    confirmation.setContentText("successfully placed your order");
                    confirmation.showAndWait();
                    try {
                        new MyOrdersView().start(MainApp.stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Back button for returning to main scene :P.
     */
    public void backToMainMenuButton(ActionEvent actionEvent) throws Exception {
        Button b = (Button) actionEvent.getTarget();
        Scene s = b.getScene();
        Stage stage = (Stage) s.getWindow();
        stage.close();
        MainView m = new MainView();
        m.start(stage);
    }

    /**
     * Go back to the last state of the page.
     */
    public void back() {
        if (!isReservation) {
            try {
                new OrderFoodView().start(MainApp.stage);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return;
            }
        } else if (!isConfirmed) {
            whenOrder.setVisible(false);
            points.setVisible(false);
            minutes.setVisible(false);
            hour.setVisible(false);
            confirmText.setVisible(true);
            confirmButton.setVisible(true);
            orderButton.setText("Choose reservation");
            back.setText("Edit Order");
            isReservation = false;
            return;
        } else if (isConfirmed) {
            orderButton.setText("Confirm");
            whenOrder.setVisible(true);
            points.setVisible(true);
            minutes.setVisible(true);
            hour.setVisible(true);
            finalConfirmText.setVisible(false);
            isConfirmed = false;
        }
    }
}

