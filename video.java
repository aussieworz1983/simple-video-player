 import java.io.File;  
  
import javafx.application.Application;  
import javafx.scene.Group;  
import javafx.scene.Scene;  
import javafx.scene.paint.Color;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;  
import javafx.stage.Stage;  
import javafx.scene.layout.StackPane;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.binding.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert; 
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;

public class video extends Application  
{  
  public static String path;
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        // TODO Auto-generated method stub  
        //Initialising path of the media file, replace this with your file path  
        
        
        path = "/home/aussie/java_projects/video/test-720.avi";  
        if(path.contains(".avi")){
            try{
            Process process = Runtime.getRuntime().exec("ffmpeg -i "+path+" /home/aussie/java_projects/video/test.mp4");
            path = "test.mp4";
        }catch(Exception e){
        System.out.println("error"+e);
        }
            
        }
        //Instantiating Media class 
        try{
        Media media = new Media(new File(path).toURI().toString());  
        System.out.println(media);
        //Instantiating MediaPlayer class   
        MediaPlayer mediaPlayer = new MediaPlayer(media);  
          
        //Instantiating MediaView class   
        MediaView mediaView = new MediaView(mediaPlayer);  
        
        video_controller videoControl = new video_controller(mediaPlayer);
        
        //by setting this property to true, the Video will be played   
        mediaPlayer.setAutoPlay(false);  
       

        
        
        //setting group and scene   
        StackPane root = new StackPane();  
        //root.getChildren().add(mediaView);  
        Scene scene = new Scene(root,Color.BLACK); 
        scene.setRoot(videoControl);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);  
        primaryStage.setTitle("Worzels Video Player");  
        primaryStage.show(); 
    }catch(Exception e){
    System.out.println("error loading video"+e);
    //make seperate function to contain alert so can be called eslewhere
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Player Error");
    alert.setHeaderText("The player encountered an error");
    alert.setContentText("error recorded please check the error log");

    alert.showAndWait();
    }
        
         
    }  
   
    public static void main(String[] args) {  
        launch(args);  
    }  
      
}  