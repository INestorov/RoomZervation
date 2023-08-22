package application.controllers;

import application.communication.ServerCommunication;
import application.entities.Room;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class PictureRoomSceneController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    private Room room;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        room = RoomListSceneController.getRoom();
        anchorPane.getChildren().clear();

        Image picture = ServerCommunication.getPictureRoom(room.getId());

        if (picture != null && picture.getHeight() != 0) {
            ImageView imageView = new ImageView();
            imageView.setImage(picture);
            anchorPane.getChildren().add(imageView);
        } else {
            Label info = new Label("No picture available");
            info.setFont(new Font(24));
            anchorPane.getChildren().add(info);
        }
    }
}
