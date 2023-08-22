package application;

import application.communication.ServerCommunication;
import application.views.LoginView;
import javafx.stage.Stage;

public class MainApp {
    public static Stage stage;

    /**
     * Starts the program.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.trustStore",
            MainApp.class.getClassLoader().getResource("cacerts").getPath());
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        LoginView.main(new String[0]);
        Runtime.getRuntime().addShutdownHook(new Thread(ServerCommunication::logout));
    }
}
