package application.controllers;

import static java.lang.Thread.MIN_PRIORITY;
import static java.lang.Thread.sleep;

import application.MainApp;
import application.communication.ServerCommunication;
import application.views.LoginView;
import com.google.gson.JsonObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class RegistrationController extends Stage implements Initializable {
    @FXML
    private TextField id;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordConfirm;
    @FXML
    private Button button;
    @FXML
    private TextField mail;
    @FXML
    private TextField phone;
    private Thread t1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (id.getText().length() == 0 || username.getText().length() == 0
                    || password.getText().length() == 0 || passwordConfirm.getText().length()
                    == 0 || mail.getText().length() == 0 || phone.getText().length() == 0) {
                    sleep(100);
                }
                button.setDisable(false);
                button.setOpacity(1);
                return null;
            }
        };
        t1 = new Thread(task);
        t1.start();
        t1.setPriority(MIN_PRIORITY);
    }

    /**
     * Handles a key event.
     *
     * @param keyEvent key event
     */
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object source = keyEvent.getSource();
            if (source.equals(id)) {
                username.requestFocus();
            } else if (source.equals(username)) {
                mail.requestFocus();
            } else if (source.equals(mail)) {
                phone.requestFocus();
            } else if (source.equals(phone)) {
                password.requestFocus();
            } else if (source.equals(password)) {
                passwordConfirm.requestFocus();
            } else {
                button.fire();
            }
        }
    }

    /**
     * Handles an action event.
     * RegisterButton
     * @throws Exception if something goes wrong
     */
    public void onAction() throws Exception {
        String idText = id.getText();
        if (idText.isEmpty() || !idText.matches("[1-9]\\d*")) {
            reset(id);
            return;
        }
        String usernameText = username.getText();
        if (usernameText.isEmpty() || !usernameText.matches("\\w+")) {
            reset(username);
            return;
        }
        String passwordText = password.getText();
        if (passwordText.isEmpty() || passwordText.length() < 8) {
            reset(password);
            return;
        }
        if (!passwordText.equals(passwordConfirm.getText())) {
            reset(passwordConfirm);
            return;
        }
        String phoneNumber = phone.getText();
        if (!phoneNumber.matches("[+]\\d+")) {
            reset(phone);
            return;
        }
        String email = mail.getText();
        JsonObject jsonObject = ServerCommunication
            .register(Integer.parseInt(idText), usernameText, passwordText, phoneNumber, email);
        boolean isSuccess = jsonObject == null;
        Alert alert = new Alert(isSuccess ? Alert.AlertType.CONFIRMATION : Alert.AlertType.ERROR);
        alert.setTitle(
            isSuccess ? "Account was successfully registered" : "Failed to register account");
        alert.setHeaderText(null);
        alert.setContentText(isSuccess ? "Account was successfully registered" :
            jsonObject.getAsJsonObject("error").get("message").getAsString());
        alert.showAndWait();
        if (isSuccess) {
            button.getScene().getWindow().hide();
            new LoginView().start(this);
        }
    }

    private void reset(Node n) {
        n.requestFocus();
    }

    /**
     * Switches to the Login scene.
     */
    public void goBack() {
        try {
            if (t1.isAlive()) {
                t1.interrupt();
            }
            new LoginView().start(MainApp.stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
