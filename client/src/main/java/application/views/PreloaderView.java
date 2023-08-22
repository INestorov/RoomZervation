package application.views;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

import application.MainApp;
import application.communication.ServerCommunication;
import application.controllers.LoginSceneController;
import application.entities.Room;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PreloaderView {

    public PreloaderView() {
    }

    @FXML
    private ProgressBar progressBar;
    private Parent root;
    private static List<Room> roomModelViews;
    private Thread loadRoom;
    private static ObservableList<Room> roomModels;

    Task<Boolean> task = new Task<Boolean>() {
        @Override
        protected Boolean call() throws Exception {
            try {
                progressBar.progressProperty().unbind();
                ((ProgressBar) root.getChildrenUnmodifiable().get(1)).setProgress(1);
                while (loadRoom.isAlive()) {
                    sleep(100);
                }

                Platform.runLater(() -> {
                    try {
                        new MainView().start(MainApp.stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
    };

    @FXML
    Task taskPb = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            for (int i = 0; i <= 1000; i++) {
                sleep(8);
                int k = i;
                //  ((ProgressBar) root.getChildrenUnmodifiable().get(1)).setProgress(k/100));
                Platform.runLater(() -> updateProgress(k, 1000));
            }
            return null;
        }
    };

    /**
     * Main method for loading the preloader.
     *
     * @param stage stage received to replace the scene
     */
    public void loadSplashScreen(Stage stage) {
        try {
            root = FXMLLoader.load(getClass().getResource("/PreloaderScene.fxml"));
            stage.setScene(new Scene(root));

            new Thread(taskPb).start();
            progressBar = ((ProgressBar) root.getChildrenUnmodifiable().get(1));
            progressBar.progressProperty().bind(taskPb.progressProperty());
            stage.centerOnScreen();
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(4), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeIn.play();
            fadeIn.setOnFinished((e) -> {
                new Thread(task).start();
                loadRoom = new Thread(this::loadRoomListData);
                loadRoom.start();
                loadRoom.setPriority(MAX_PRIORITY);
                fadeOut.play();
            });
            //After fade out, load actual content
            fadeOut.setOnFinished((e) -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Loads data requested by RoomListSceneController here.
     *
     */
    public void loadRoomListData() {
        boolean type;
        roomModelViews = new ArrayList<>();
        String userType = ServerCommunication.getUserType(LoginSceneController.getUser());
        if (userType.equals("Student")) {
            type = true;
        } else {
            type = false;
        }
        JsonParser.parseString(ServerCommunication.getRooms()).getAsJsonArray()
            .forEach(jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                List<String> list = new ArrayList<>();
                JsonArray jsonFacilities = jsonObject.getAsJsonArray("facilities");
                for (int i = 0; i < jsonFacilities.size(); i++) {
                    list.add(jsonFacilities.get(i).getAsJsonObject().get("name").getAsString());
                }
                if (!type || (type && jsonObject.get("size").getAsInt() <= 20)) {
                    roomModelViews.add(new Room(
                        jsonObject.get("id").getAsInt(),
                        jsonObject.get("name").getAsString(),
                        jsonObject.getAsJsonObject("building").get("id").getAsInt(),
                        jsonObject.get("size").getAsInt(),
                        list,
                        jsonObject.get("description").getAsString()));
                }
            });
        roomModels = FXCollections.observableArrayList(roomModelViews);
        loadRoom.interrupt();
    }

    public static ObservableList<Room> getRoomListData() {
        return roomModels;

    }
}
