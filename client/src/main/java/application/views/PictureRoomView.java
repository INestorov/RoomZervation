package application.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PictureRoomView {

    /**
     * Starts scene for viewing picture of a room.
     *
     * @param stage - previous stage
     * @throws Exception - throws exception in case of non-existing scene
     */
    public void start(Stage stage) throws Exception {
        Stage stage1;
        Parent root;
        stage1 = new Stage();
        root = FXMLLoader.load(getClass().getResource("/PictureRoomScene.fxml"));
        stage1.setScene(new Scene(root));
        stage1.initModality(Modality.APPLICATION_MODAL);
        stage1.initOwner(stage.getScene().getWindow());
        stage1.showAndWait();

    }
}