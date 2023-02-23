package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class OpenDataViewerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene viewerScene = new Scene(new FXMLLoader(OpenDataViewerApplication.class.getResource("/view/Viewer.fxml")).load());
        primaryStage.setScene(viewerScene);
        primaryStage.setTitle("Open Data View");
        primaryStage.show();
    }
}
