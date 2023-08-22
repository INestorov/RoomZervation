package application.controllers;

import application.MainApp;
import application.communication.ServerCommunication;
import application.entities.User;
import application.entities.UserManager;
import application.views.AdminView;
import application.views.ForgotPasswordView;
import application.views.PreloaderView;
import application.views.RegistrationView;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginSceneController extends Stage {
    private static String user;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField netId;
    private static String userType;
    private String password;

    public static String getUser() {
        return user;
    }

    /**
     * Handles clicking the button.
     * resources always with / before !!!!!!!!!
     * TO DO -getter for menu button
     * - think of an idea of implementing both reservation pages together
     * - read about grid
     * -cancel button
     * -maybe a back button
     */

    public void login() throws Exception {
        user = netId.getText();
        password = passwordField.getText();
        JsonObject jsonObject = ServerCommunication.login(user, password);
        if (jsonObject != null && jsonObject.has("data")) {
            userType = ServerCommunication.getUserType(user);
            User currentUser = new User(ServerCommunication.getUserId(netId.getText()));
            UserManager.getInstance().setUser(currentUser);
            JsonObject data = jsonObject.getAsJsonObject("data");
            MainSceneController.isAdmin = data.get("isAdmin").getAsBoolean();
            MainSceneController.hasBike = data.get("hasBike").getAsBoolean();
            PreloaderView p = new PreloaderView();
            if (!MainSceneController.isAdmin) {
                p.loadSplashScreen(MainApp.stage);
            } else {
                new AdminView().start(MainApp.stage);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Failed to log in");
            alert.setHeaderText(null);
            alert.setContentText(jsonObject != null
                ? jsonObject.getAsJsonObject("error").get("message").getAsString()
                : "Unknown error");
            alert.showAndWait();
        }
    }

    /**
     * Method for opening the Registration window.
     *
     * @param event the trigger for this method
     */
    public void register(ActionEvent event) {
        try {
            new RegistrationView().start((Stage) ((Node) event.getSource()).getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If ENTER is pressed while username text field is focused, changes focus to password field.
     * If ENTER is pressed while password field is focused, login is prompted.
     *
     * @param event Key event
     */
    public void onKeyPressed(KeyEvent event) throws Exception {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource().equals(netId)) {
                passwordField.requestFocus();
            } else if (event.getSource().equals(passwordField)) {
                login();
            }
        }
    }

    /**
     * Go to passwordRecoveryPage.
     * @param event ActionEvent
     */
    public void forgotPassword(ActionEvent event) {
        try {
            new ForgotPasswordView().start((Stage)
                ((Node) event.getSource()).getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserType() {
        return userType;
    }


}
