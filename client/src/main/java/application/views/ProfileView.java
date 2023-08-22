package application.views;

import application.communication.ServerCommunication;
import application.controllers.LoginSceneController;
import application.entities.Room;
import application.entities.TimeSlot;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileView extends Application {
    private List<TimeSlot> reservations;
    private Map<String, List<LocalDate>> holidays;
    private Thread updateTimeThread;

    /** Start view method.
     *
     * @param stage previous stage
     */
    @Override
    public void start(Stage stage) {
        //stage.close();
        //MainApp.stage = new Stage();

        Calendar personalEvents = new Calendar("PersonalEvents");
        Calendar holidayCalendar = new Calendar("Holidays");
        Calendar reservationsCalendar = new Calendar("My reservations");

        holidayCalendar.setStyle(Calendar.Style.STYLE2);
        personalEvents.setStyle(Calendar.Style.STYLE3);
        reservationsCalendar.setStyle(Calendar.Style.STYLE4);
        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(personalEvents, reservationsCalendar,
            holidayCalendar);
        load();
        loadHolidays();
        for (TimeSlot t : reservations) {
            LocalDateTime startTime = LocalDateTime.of(
                LocalDate.parse(t.getStart().substring(0, 10)), LocalTime.parse(
                    t.getStart().substring(11)));
            LocalDateTime endTime = LocalDateTime.of(LocalDate.parse(t.getStart()
                .substring(0, 10)), LocalTime.parse(t.getEnd()));
            Interval interval = new Interval(startTime, endTime);
            Entry<String> entry = new Entry<>(t.getRoom().getBuilding(t.getRoom()
                .getBuildingId()), interval);
            reservationsCalendar.addEntry(entry);
        }
        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        for (Map.Entry<String, List<LocalDate>> e : holidays.entrySet()) {
            Interval interval = new Interval(LocalDateTime.parse(
                e.getValue().get(0).toString() + "T00:00:00"),
                LocalDateTime.parse(e.getValue().get(1)
                    .toString() + "T00:00:00"));
            Entry<String> entry = new Entry<>(e.getKey(), interval);
            holidayCalendar.addEntry(entry);
            entry.fullDayProperty().setValue(true);
        }
        reservationsCalendar.setReadOnly(true);
        holidayCalendar.setReadOnly(true);
        updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        System.out.printf("%s was interrupted: %s%n", this, e.getMessage());
                    }
                }
            }
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        Stage stage1;
        Parent root;
        stage1 = new Stage();
        root = calendarView;
        stage1.setScene(new Scene(root));
        stage1.setWidth(1300);
        stage1.setHeight(800);
        stage1.centerOnScreen();
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage1.initOwner(stage.getScene().getWindow());
        stage1.setOnCloseRequest(event -> updateTimeThread.interrupt());
        stage1.showAndWait();
    }

    private void load() {
        List<TimeSlot> reservationsRaw = new ArrayList<>();
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
                reservationsRaw.add(new TimeSlot(
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
        reservations = FXCollections.observableArrayList(reservationsRaw);
    }

    /**
     * Load the holidays from the server.
     */
    private void loadHolidays() {
        holidays = new HashMap<>();
        JsonParser.parseString(ServerCommunication.getHolidays())
            .getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                LocalDate start = LocalDate.parse(jsonObject.get("start").getAsString());
                LocalDate end = LocalDate.parse(jsonObject.get("end").getAsString());
                String s = jsonObject.get("name").getAsString();
                if (start.getYear() == LocalDate.now().getYear()) {
                    List<LocalDate> list = new ArrayList<>();
                    list.add(start);
                    list.add(end);
                    holidays.put(s, list);
                }
            });
    }
}

