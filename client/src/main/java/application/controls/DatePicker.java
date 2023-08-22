package application.controls;

import application.MainApp;
import application.communication.ServerCommunication;
import application.controllers.LoginSceneController;
import application.controllers.RoomListSceneController;
import application.entities.Room;
import application.entities.TimeSlot;
import application.views.RoomListView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//@author - Silviu Marii
public class DatePicker extends Stage {
    @FXML
    private GridPane grid;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane anchorPaneDate;
    @FXML
    private SplitPane splitPane;
    @FXML
    private GridPane gridTimeSelection;
    @FXML
    private ScrollBar scrollBar;
    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label availableLabel;
    @FXML
    private Label invalidTimeSlot;
    public double endValue;
    private int openingHour;
    private int closingHour;
    private int openingMinute;
    private int closingMinute;
    private String openingTime;
    private String closingTime;
    private String startValue;
    private ObservableList<TimeSlot> roomModels;
    private ObservableList<TimeSlot> availableSlots;
    private String startValueToSend;
    private Progress progress;
    private DateTimePicker date;
    private Stage stage;
    private Room room;
    private DecimalFormat df = new DecimalFormat("#.00");
    private boolean cond;

    private Node target;
    private Node previous;

    /**
     * Checkstyle is stupid..
     * Time slots conditions checked
     * Comparator functionality checked
     */
    @FXML
    private void datePicked(ActionEvent e) {
        // Day selected
        //button.setDisable(true);
        date = (DateTimePicker) anchorPaneDate.getChildren().get(3);
        LocalDate i = date.getValue();
        System.out.println(i);
        room = RoomListSceneController.getRoom();
        availableLabel.setVisible(true);
        setupTimeSlots();
    }

    @FXML
    private EventHandler<MouseEvent> eventFirstTimePicked = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            previous = target;
            target = (Node) event.getTarget();
            if (target != gridTimeSelection) {
                Node parent;
                while ((parent = target.getParent()) != gridTimeSelection) {
                    target = parent;
                }
            }
            Integer colIndex = gridTimeSelection.getColumnIndex(target);
            if (colIndex != null) {
                if (colIndex == 0) {
                    endLabel.setVisible(true);
                    startValueToSend = ((Text) event.getTarget()).getText();
                    target.setStyle("-fx-background-color: red");
                    fillEndOptions(startValueToSend);
                } else {
                    waitConfirmation(((Text) event.getTarget()).getText());
                }
            }
        }
    };

    /** Last step of the reservation process.
     *  The user confirms if he wishes to make this reservation.
     *
     * @param endVl endValue chosen by user.
     */
    public void waitConfirmation(String endVl) {
        endValue = Double.parseDouble(endVl.replace(":","."));
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Last step");
        alert.setHeaderText("Confirm reservation");
        alert.setContentText("Your reservation is from "
            + startValueToSend.substring(0,5) + " until " + endVl + '\n'
            + "on day " + date.getDateTimeValue().toString().substring(0,10));
        Optional<ButtonType> choice = alert.showAndWait();
        target.setStyle("-fx-background-color: transparent");
        previous.setStyle("-fx-background-color: transparent");
        if (choice.get() == ButtonType.OK) {
            sendReservation();
        }

    }

    private EventHandler<MouseEvent> event3 = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getTarget() instanceof Text) {
                Text t = (Text) event.getTarget();
                startValue = t.getText();
                try {
                    setupGridTimeSelection(startValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void sendReservation() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(ts);
        String dateChosen = date.getDateTimeValue().toString().substring(0,10) + " ";
        startValue = startValue.substring(0,5);
        endValue = Math.round(endValue * 100) / 100.0d;
        checkData();
        String processedEndValue;
        if (endValue - Math.round(endValue * 10) / 10.0d != 0) {
            processedEndValue = dateChosen + endValue + ":00";
        } else {
            processedEndValue = dateChosen + endValue + "0:00";
        }
        startValueToSend = dateChosen + startValueToSend + ":00";
        System.out.println(cond);
        if (cond == true) {
            stage = (Stage) anchorPane.getScene().getWindow();
            progress = new Progress(stage);
            progress.initStage();
            progress.showStatus(0, stage);
            int respond = -1;
            try {
                respond = ServerCommunication.addReservation(room, startValueToSend,
                    processedEndValue, LoginSceneController.getUser());
            } catch (Exception e) {
                System.out.println("Exception thrown: " + e);
                progress.unsuccesful(stage);
            }
            try {
                if (respond == 1) {
                    progress.showStatus(1, stage);
                } else {
                    progress.showStatus(2,stage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gridTimeSelection.getChildren().clear();
        }
    }

    /**
     * Loads data from the server, stores it.
     */
    public void load() {
        List<TimeSlot> roomModelViews = new ArrayList<>();
        String dateChosen = date.getDateTimeValue().toString().substring(0,10);
        String user = LoginSceneController.getUserType();
        boolean inCheck = true; // true for teachers
        if (!user.equals("Employee")) {
            inCheck = false;
        }
        Boolean c = inCheck;
        JsonParser.parseString(ServerCommunication.getReservations()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String type = jsonObject.getAsJsonObject("user").get("type").getAsString();
                System.out.println(type);
                String entry = jsonObject.get("start").getAsString().substring(0, 10);
                if (jsonObject.getAsJsonObject("room").get("id").getAsInt() == room.getId()
                    && entry.equals(dateChosen) && ((c == true && type.equals("Employee"))
                    || c == false)) {
                    roomModelViews.add(new TimeSlot(
                        jsonObject.getAsJsonObject("room").get("id").getAsInt(),
                        jsonObject.get("start").getAsString(),
                        jsonObject.get("end").getAsString(),
                        jsonObject.get("id").getAsInt()));
                    System.out.println(
                        jsonObject.getAsJsonObject("room").get("id").getAsInt() + " | "
                            + jsonObject.get("start").getAsString() + " | "
                            + jsonObject.get("end").getAsString());
                }
            });

        openingTime = JsonParser.parseString(ServerCommunication.getRoomById(room.getId()))
            .getAsJsonObject()
            .getAsJsonObject("building")
            .get("openingTime").getAsString();
        closingTime = JsonParser.parseString(ServerCommunication.getRoomById(room.getId()))
            .getAsJsonObject()
            .getAsJsonObject("building")
            .get("closingTime").getAsString();
        System.out.println("Building openingTime: " + openingTime);
        System.out.println("Building closingTime: " + closingTime);
        int closeBug = Integer.parseInt(closingTime.substring(0, 2));
        if (closeBug < Integer.parseInt(openingTime.substring(0, 2))) {
            closeBug += 24;
            closingTime = closingTime.replace(closingTime.substring(0, 2),
                String.valueOf(closeBug));
        }
        System.out.println("Building closingTime after correction: " + closingTime);
        roomModels = FXCollections.observableArrayList(roomModelViews);
        Collections.sort(roomModels, new Sort());
        System.out.println("Reservations at the moment:");
        for (int i = 0; i < roomModels.size(); i++) {
            System.out.println(roomModels.get(i).getStart() + " "
                + roomModels.get(i).getEnd());
        }

    }

    /**
     * Creates the available time slots.
     */
    public void fillAvailableSlots() {
        List<TimeSlot> availableSlotsView = new ArrayList<>();
        openingHour = Integer.parseInt(openingTime.substring(0, openingTime.indexOf(":")));
        openingMinute = Integer.parseInt(openingTime.substring(openingTime.indexOf(":") + 1,
            openingTime.lastIndexOf(":")));
        closingHour = Integer.parseInt(closingTime.substring(0, closingTime.indexOf(":")));
        closingMinute = Integer.parseInt(closingTime.substring(closingTime.indexOf(":") + 1,
            openingTime.lastIndexOf(":")));
        if (roomModels.size() != 0) {
            int startingHour = Integer.parseInt(roomModels.get(0).getStart().substring(11,
                roomModels.get(0).getStart().indexOf(":")));
            int startingMinute = Integer.parseInt(roomModels.get(0).getStart().substring(
                roomModels.get(0).getStart().indexOf(":") + 1,
                roomModels.get(0).getStart().lastIndexOf(":")));
            if (startingHour > openingHour || (startingHour == openingHour
                && startingMinute >= openingMinute + 15)) {
                availableSlotsView.add(new TimeSlot(0, openingTime.substring(0,5),
                    roomModels.get(0).getStart().substring(11, 16)));
            }
            int endingHour;
            int endingMinute;
            int k;
            for (k = 0; k < roomModels.size() - 1; k++) {
                endingHour = Integer.parseInt(roomModels.get(k).getEnd().substring(11,
                    roomModels.get(k).getEnd().indexOf(":")));
                endingMinute = Integer.parseInt(roomModels.get(k).getEnd().substring(
                    roomModels.get(k).getEnd().indexOf(":") + 1,
                    roomModels.get(k).getEnd().lastIndexOf(":")));
                startingHour = Integer.parseInt(roomModels.get(k + 1).getStart().substring(11,
                    roomModels.get(k + 1).getStart().indexOf(":")));
                startingMinute = Integer.parseInt(roomModels.get(k + 1).getStart().substring(
                    roomModels.get(k + 1).getStart().indexOf(":") + 1,
                    roomModels.get(k + 1).getStart().lastIndexOf(":")));
                if (startingHour > endingHour || (startingHour == endingHour
                    && startingMinute >= endingMinute + 15)) {
                    availableSlotsView.add(new TimeSlot(k,
                        roomModels.get(k).getEnd().substring(11, 16),
                        roomModels.get(k + 1).getStart().substring(11, 16)));
                }
            }
            endingHour = Integer.parseInt(roomModels.get(k).getEnd().substring(11,
                roomModels.get(k).getEnd().indexOf(":")));
            endingMinute = Integer.parseInt(roomModels.get(k).getEnd().substring(
                roomModels.get(k).getEnd().indexOf(":") + 1,
                roomModels.get(k).getEnd().lastIndexOf(":")));
            if (closingHour > endingHour || (closingHour == endingHour
                && closingMinute >= endingMinute + 15)) {
                System.out.println("closingHOUR " + closingHour);
                System.out.println("endingHOUR" + endingHour);
                availableSlotsView.add(new TimeSlot(++k,
                    roomModels.get(--k).getEnd().substring(11, 16),
                    closingTime.substring(0,5)));
            }

            for (int i1 = 0; i1 < availableSlotsView.size(); i1++) {
                System.out.println(availableSlotsView.get(i1).getStart() + " | "
                    + availableSlotsView.get(i1).getEnd());
            }
        } else {
            availableSlotsView.add(new TimeSlot(0,openingTime.substring(0,5),
                closingTime.substring(0,5)));
        }
        availableSlots = FXCollections.observableArrayList(availableSlotsView);
    }

    private void setupTimeSlots() {
        load();
        fillAvailableSlots();
        grid.getChildren().clear();
        for (int k = 0; k < availableSlots.size(); k++) {
            String ts = availableSlots.get(k).getStart() + " - " + availableSlots.get(k).getEnd();
            int correction = Integer.parseInt(ts.substring(8,10));
            if (correction == 24) {
                ts = ts.replace(ts.substring(8,10),
                    "00");
            } else if (correction > 24) {
                ts = ts.replace(ts.substring(8,10),
                    String.valueOf(correction - 24));
            }
            if (ts.substring(ts.indexOf("-") + 2).length() < 5)  {
                ts = ts.substring(0,8) + "0" + ts.substring(8);
            }
            LocalTime localCompare = LocalTime.parse(ts.substring(0,5));
            if (LocalTime.parse(ts.substring(ts.indexOf("-") + 2)).minusMinutes(16).compareTo(
                LocalTime.now()) < 0 && date.getDateTimeValue().getDayOfMonth()
                == LocalDate.now().getDayOfMonth()) {
                int bug = Integer.parseInt(ts.substring(ts.indexOf("-") + 2,
                    ts.lastIndexOf(":") - 1));
                int local = Integer.parseInt(String.valueOf(LocalTime.now().getHour()));
                if (bug != 0 || (bug == 0 && local == 0)) {
                    ts = null;
                }
            }
            if (ts != null && date.getDateTimeValue().getDayOfMonth()
                == LocalDate.now().getDayOfMonth()) {
                if (localCompare.compareTo(LocalTime.now()) < 0) {
                    ts = ts.replace(ts.substring(0, 5), String.valueOf(LocalTime.now()
                        .plusMinutes(1)).substring(0, 5));
                }
            }
            if (ts != null) {
                Label label = new Label(ts);
                grid.add((Node) label, 0, k);
                grid.setFillWidth(label, true);
            }
        }
        if (grid.getChildren().size() == 0) {
            Label l = new Label("No reservations");
            grid.add(l,0,0);
            Label g = new Label("available today");
            grid.add(g,0,1);
        }
        splitPane.setVisible(true);
        grid.setVisible(true);
        grid.setOnMouseClicked(event3);
    }

    /**
     * Creates a slider in order to pick the end time for a time slot.
     *
     * @param timeGap chosen by user.
     */
    public void setupGridTimeSelection(String timeGap) throws ParseException {
        gridTimeSelection.getChildren().clear();
        endLabel.setVisible(false);
        startLabel.setVisible(true);
        gridTimeSelection.setVisible(true);
        System.out.println("time gap chosen " + timeGap);
        double startOpt = Double.parseDouble(timeGap.replace(":",".")
            .substring(0,5));
        double endOpt = Double.parseDouble(timeGap.replace(":",".")
            .substring(timeGap.indexOf("-") + 2,timeGap.length()));
        if (endOpt < startOpt) {
            endOpt += 24;
        }
        final double startScroll = startOpt;
        final double endScroll = endOpt;
        int k = 0;
        while (startOpt + 0.15 < endOpt) {
            String toDisplay = String.valueOf(startOpt)
                .replace(".",":");
            if (toDisplay.indexOf(":") == 1) {
                toDisplay = "0" + toDisplay;
            }
            if (toDisplay.substring(toDisplay.indexOf(":") + 1).length() < 2) {
                toDisplay = toDisplay + "0";
            } else if (toDisplay.substring(0,toDisplay.indexOf(":")).length() == 1) {
                toDisplay = toDisplay.substring(0,4);
            } else {
                toDisplay = toDisplay.substring(0,5);
            }
            int correction = Integer.parseInt(toDisplay.substring(0,2));
            if (correction == 24) {
                toDisplay = toDisplay.replace(toDisplay.substring(0,2),
                    "00");
            } else if (correction > 24) {
                toDisplay = toDisplay.replace(toDisplay.substring(0,2),
                    String.valueOf(correction - 24));
            }
            Label option = new Label(toDisplay);
            gridTimeSelection.add((Node) option,0,k++);
            startOpt += 0.15;
            if (startOpt - (int) startOpt >= 0.59) {
                startOpt += 0.4;
            }
        }
        scrollBar.setVisible(true);
        scrollBar.setUnitIncrement(30);
        scrollBar.setBlockIncrement(30);
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number oldVal, Number newVal) {
                gridTimeSelection.setLayoutY(-newVal.doubleValue() * (endScroll - startScroll));
            }
        });
        gridTimeSelection.setOnMouseClicked(eventFirstTimePicked);
    }

    /** Fills the option for end Time.
     *
     * @param text start Time chosen by user.
     */
    public void fillEndOptions(String text) {
        System.out.println("text " + text);
        if (previous != null) {
            if (previous != target) {
                previous.setStyle("-fx-background-color: transparent");
            }
        }
        gridTimeSelection.getChildren().removeIf(node ->
            gridTimeSelection.getColumnIndex(node) == 1);
        double start = Double.parseDouble(text.replace(":","."));
        double end = start + 4;
        System.out.println(closingTime);
        double closingBuilding = Double.parseDouble(closingTime.replace(":",".")
            .substring(0,closingTime.lastIndexOf(":")));
        int k = 0;
        if (end > closingBuilding) {
            end = closingBuilding;
        }
        if (LoginSceneController.getUserType().equals("Employee")) {
            end = closingBuilding;
        }
        while (start + 0.15 < end) {
            start += 0.15;
            if (start - (int) start >= 0.59) {
                start += 0.4;
            }
            String toDisplay = String.valueOf(start)
                .replace(".",":");
            if (toDisplay.substring(toDisplay.indexOf(":") + 1).length() < 2) {
                toDisplay = toDisplay + "0";
            } else if (toDisplay.substring(0,toDisplay.indexOf(":")).length() == 1) {
                toDisplay = toDisplay.substring(0,4);
            } else {
                toDisplay = toDisplay.substring(0,5);
            }
            if (toDisplay.indexOf(":") == 1) {
                toDisplay = "0" + toDisplay;
            }
            int correction = Integer.parseInt(toDisplay.substring(0,2));
            if (correction == 24) {
                toDisplay = toDisplay.replace(toDisplay.substring(0,2),
                    "00");
            } else if (correction > 24) {
                toDisplay = toDisplay.replace(toDisplay.substring(0,2),
                    String.valueOf(correction - 24));
            }
            Label option = new Label(toDisplay);
            gridTimeSelection.add(option,1,k++);
        }
    }

    /** Return button to main scene.
     *
     * @param actionEvent the trigger.
     */
    public void back(ActionEvent actionEvent) {
        try {
            new RoomListView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Checks whether the user has a reservation in that interval of time or not.
     *
     */
    public void checkData() {
        cond = true;
        String user = LoginSceneController.getUser();
        System.out.println("USER TYPE" + ServerCommunication.getUserType(user));
        JsonParser.parseString(ServerCommunication.getReservationByUsername(user)).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                int day = Integer.parseInt(jsonObject.get("start").getAsString().substring(8, 10));
                int month = Integer.parseInt(jsonObject.get("start").getAsString().substring(5, 7));
                double start = Double.parseDouble(jsonObject.get("start").getAsString()
                    .substring(11, 16).replace(":", "."));
                double end = Double.parseDouble(jsonObject.get("end").getAsString()
                    .substring(11, 16).replace(":", "."));
                double startSelected = Double.parseDouble(startValueToSend.replace(":",
                    "."));
                if (date.getDateTimeValue().getMonthValue() == month && date.getDateTimeValue()
                    .getDayOfMonth() == day) {
                    if ((startSelected > start && startSelected < end)) {
                        System.out.println("INVALID");
                        invalidTimeSlot.setVisible(true);
                        invalidTimeSlot.setText("You already have a reservation" + '\n' + "in that "
                            + "interval of time");
                        cond = false;
                        return;
                    }
                }
            });
    }
}
