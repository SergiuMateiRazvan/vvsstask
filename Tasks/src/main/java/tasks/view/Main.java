package tasks.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.controller.TaskController;
import tasks.model.Notificator;

import tasks.services.TasksService;

import java.io.File;
import java.io.IOException;


public class Main extends Application {
    private static Stage primaryStage;
    private static final int DEFAULT_WIDTH = 820;
    private static final int DEFAULT_HEIGHT = 520;

    private static final Logger log = Logger.getLogger(Main.class.getName());


    private static ClassLoader classLoader = Main.class.getClassLoader();
    public static final File savedTasksFile = new File(classLoader.getResource("data/tasks.txt").getFile());

    private TasksService service = new TasksService(savedTasksFile);

    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("saved data reading");
        try {
            log.info("application start");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            TaskController ctrl= loader.getController();
            service = new TasksService(savedTasksFile);

            ctrl.setService(service);
            primaryStage.setTitle("Task Manager");
            primaryStage.setScene(new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT));
            primaryStage.setMinWidth(DEFAULT_WIDTH);
            primaryStage.setMinHeight(DEFAULT_HEIGHT);
            primaryStage.show();
        }
        catch (IOException e){
            log.error("error reading main.fxml");
            log.error(e.getStackTrace());
        }
        primaryStage.setOnCloseRequest(we ->
                System.exit(0)
        );
        new Notificator(FXCollections.observableArrayList(service.getObservableList())).start();
    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
