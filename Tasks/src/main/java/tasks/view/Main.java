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
    public static Stage primaryStage;
    private static final int defaultWidth = 820;
    private static final int defaultHeight = 520;

    private static final Logger log = Logger.getLogger(Main.class.getName());


    private static ClassLoader classLoader = Main.class.getClassLoader();
    public static File savedTasksFile = new File(classLoader.getResource("data/tasks.txt").getFile());

    private TasksService service = new TasksService(savedTasksFile);

    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("saved data reading");
        try {
            log.info("application start");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();//loader.load(this.getClass().getResource("/fxml/main.fxml"));
            TaskController ctrl= loader.getController();
            service = new TasksService(savedTasksFile);

            ctrl.setService(service);
            primaryStage.setTitle("Task Manager");
            primaryStage.setScene(new Scene(root, defaultWidth, defaultHeight));
            primaryStage.setMinWidth(defaultWidth);
            primaryStage.setMinHeight(defaultHeight);
            primaryStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
            log.error("error reading main.fxml");
        }
        primaryStage.setOnCloseRequest(we -> {
                System.exit(0);
            });
        new Notificator(FXCollections.observableArrayList(service.getObservableList())).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
