package application.views;

import application.communication.ServerCommunication;
import application.controls.DateTimePicker;
import application.entities.Room;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Date;
import java.time.LocalDate;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class DatePickerView {
    private DateTimePicker date;
    private Label label;
    private Set<LocalDate> holidays;
    final Callback<DatePicker, DateCell> dayCellFactory =
        new Callback<javafx.scene.control.DatePicker, DateCell>() {
            public DateCell call(final javafx.scene.control.DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (MonthDay.from(item).compareTo(MonthDay.of(LocalDate.now().getMonth(),
                            LocalDate.now().getDayOfMonth())) < 0
                            //  (!getStyleClass().contains("next-month")  )
                            || (MonthDay.from(item).getMonthValue()
                                == LocalDate.now().getMonthValue() + 1
                                && MonthDay.from(item).getDayOfMonth()
                                > LocalDate.now().getDayOfMonth())
                            || MonthDay.from(item).getMonthValue()
                            > LocalDate.now().getMonthValue() + 1
                            || (LocalDate.now().getYear() != item.getYear()
                            && LocalDate.now().getMonthValue() != 12)
                            || holidays.contains(item.minusDays(1))
                        ) {
                            setDisable(true);
                            //here you can switch the unavailable cell color
                            setStyle("-fx-background-color: #ff4440;");
                        } else {
                            setTooltip(null);
                            setStyle(null);
                        }
                    }
                };
            }
        };

    /**
     * Loads the Date picker window.
     *
     * @param stage previous stage .
     * @throws Exception nasty exception to be thrown.
     */
    public void start(Stage stage, Room roomClicked) throws Exception {
        //stage.close();
        //MainApp.stage = new Stage();
        stage
            .setScene(new Scene(FXMLLoader.load(getClass()
                .getResource("/DatePickerScene.fxml"))));
        stage.setTitle("Date picker");
        getHolidays();
        date = new DateTimePicker();
        label = new Label("No date selected");
        date.setShowWeekNumbers(true);
        date.setOnAction(event1);
        date.setDayCellFactory(dayCellFactory);
        stage.getScene().getStylesheets().add("calendarstyle.css");
        ObservableList<Node> paneChildren = ((AnchorPane) stage.getScene().getRoot()
            .getChildrenUnmodifiable().get(0)).getChildren();
        paneChildren.add(date);
        paneChildren.add(label);
        label.setLayoutX(177);
        label.setLayoutY(4);
        stage.show();
    }

    private void getHolidays() {
        holidays = new HashSet<>();
        JsonParser.parseString(ServerCommunication.getHolidays())
            .getAsJsonArray().forEach(jsonElement  -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                LocalDate start = LocalDate.parse(jsonObject.get("start").getAsString());
                LocalDate end = LocalDate.parse(jsonObject.get("end").getAsString());
                while (start.compareTo(end) <= 0) {
                    holidays.add(start);
                    start = start.plusDays(1);
                    System.out.println(start);
                }
            });
    }

    private EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            //  b.disarm();
            LocalDate i = date.getValue();
            String j = date.toStringTime();
            label.setText("Date :" + j);
        }
    };
}
