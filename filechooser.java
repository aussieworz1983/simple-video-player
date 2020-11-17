
 import java.io.File;  
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.layout.VBox;
/**
 * Write a description of JavaFX class filechooser here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class filechooser 
{
    // We keep track of the count, and label displaying the count:
    
    String path = "";
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    
    public void filechooser()
    {
       Stage stage = new Stage();
        stage.setTitle("Choose Video File");
      FileChooser fileChooser = new FileChooser();
      
        Button button = new Button("Select File");
        button.setOnAction(e -> {
           File selectedFile = fileChooser.showOpenDialog(stage);
           System.out.println(selectedFile);
           video vid = new video();
           video.path=selectedFile.toString();
           
        });

        VBox vBox = new VBox(button);
        Scene scene = new Scene(vBox, 80, 40);

        stage.setScene(scene);
        stage.show();
    }
    public void start(){
     
    }
}
