
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;
import javafx.scene.media.MediaPlayer.Status;
import javafx.event.EventHandler;
import javafx.beans.*;
import javafx.application.Platform;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;
/**
 * Write a description of JavaFX class video_controller here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class video_controller extends BorderPane
{
    // We keep track of the count, and label displaying the count:
   

    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    
    private MediaPlayer mp;
    private MediaView mediaView;
    private final boolean repeat = false;
    private boolean stopRequested = false;
    private boolean atEndOfMedia = false;
    private Duration duration;
    private Slider timeSlider;
    private Label playTime;
    private Slider volumeSlider;
    private HBox mediaBar;
 

    public video_controller(final MediaPlayer mp) {
        this.mp = mp;
        setStyle("-fx-background-color: #bfc2c7;");
        mediaView = new MediaView(mp);
        MenuBar menuBar = new MenuBar();

        VBox vBox = new VBox(menuBar);
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Options");
        Menu menu3 = new Menu("System");
        MenuItem itemOpen= new MenuItem("open");
        MenuItem itemPrefs= new MenuItem("Preferences");
        MenuItem itemExit = new MenuItem("Exit");
        StackPane mvPane = new StackPane() {                };
        mvPane.getChildren().add(mediaView);
        mvPane.getChildren().add(vBox);
        mvPane.setStyle("-fx-background-color: black;"); 
      
        setCenter(mvPane);
       
        

        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);
        menuBar.getMenus().add(menu3);
        
        menu1.getItems().add(itemOpen);
        menu2.getItems().add(itemPrefs);
        menu3.getItems().add(itemExit);
        
        mediaBar = new HBox();
        mediaBar.setAlignment(Pos.CENTER);
        mediaBar.setPadding(new Insets(5, 10, 5, 10));
        BorderPane.setAlignment(mediaBar, Pos.CENTER);
        
        final Button playButton  = new Button(">");
        
        mediaView.fitWidthProperty().bind(mvPane.widthProperty());
        mediaView.fitHeightProperty().bind(mvPane.heightProperty());
        
        
        
       
        
        mediaView.setPreserveRatio(true);
        itemOpen.setOnAction(new EventHandler<ActionEvent>(){
        
        public void handle(ActionEvent e){
        System.out.println("file selected");
        filechooser fileChooser = new filechooser();
        fileChooser.filechooser();
        }
        });
            itemPrefs.setOnAction(new EventHandler<ActionEvent>(){
        
        public void handle(ActionEvent e){
        System.out.println("options selected");
        }
        });
            itemExit.setOnAction(new EventHandler<ActionEvent>(){
        
        public void handle(ActionEvent e){
        System.exit(0);
        }
        });
        
        playButton.setOnAction(new EventHandler<ActionEvent>() {
    public void handle(ActionEvent e) {
        Status status = mp.getStatus();
 
        if (status == Status.UNKNOWN  || status == Status.HALTED)
        {
           // don't do anything in these states
           return;
        }
 
          if ( status == Status.PAUSED
             || status == Status.READY
             || status == Status.STOPPED)
          {
             // rewind the movie if we're sitting at the end
             if (atEndOfMedia) {
                mp.seek(mp.getStartTime());
                atEndOfMedia = false;
             }
             mp.play();
             } else {
               mp.pause();
             }
         }
   });
        mediaBar.getChildren().add(playButton);
        // Add spacer
Label spacer = new Label("   ");
mediaBar.getChildren().add(spacer);
 
// Add Time label
Label timeLabel = new Label("Time: ");
mediaBar.getChildren().add(timeLabel);
 
// Add time slider
timeSlider = new Slider();
HBox.setHgrow(timeSlider,Priority.ALWAYS);
timeSlider.setMinWidth(50);
timeSlider.setMaxWidth(Double.MAX_VALUE);
timeSlider.valueProperty().addListener(new InvalidationListener() {
    public void invalidated(Observable ov) {
       if (timeSlider.isValueChanging()) {
       // multiply duration by percentage calculated by slider position
          mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
       }
    }
});
mediaBar.getChildren().add(timeSlider);

// Add Play label
playTime = new Label();
playTime.setPrefWidth(130);
playTime.setMinWidth(50);
mp.currentTimeProperty().addListener(new InvalidationListener() 
        {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });
 
        mp.setOnPlaying(new Runnable() {
            public void run() {
                if (stopRequested) {
                    mp.pause();
                    stopRequested = false;
                } else {
                    playButton.setText("||");
                }
            }
        });
 
        mp.setOnPaused(new Runnable() {
            public void run() {
                System.out.println("onPaused");
                playButton.setText(">");
            }
        });
 
        mp.setOnReady(new Runnable() {
            public void run() {
                duration = mp.getMedia().getDuration();
                updateValues();
            }
        });
 
        mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        mp.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (!repeat) {
                    playButton.setText(">");
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            }
       });
mediaBar.getChildren().add(playTime);
 
// Add the volume label
Label volumeLabel = new Label("Vol: ");
mediaBar.getChildren().add(volumeLabel);
 
// Add Volume slider
volumeSlider = new Slider();        
volumeSlider.setPrefWidth(70);
volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
volumeSlider.setMinWidth(30);
volumeSlider.valueProperty().addListener(new InvalidationListener() {
    public void invalidated(Observable ov) {
       if (volumeSlider.isValueChanging()) {
           mp.setVolume(volumeSlider.getValue() / 100.0);
       }
    }
});
mediaBar.getChildren().add(volumeSlider);
        setBottom(mediaBar); 
     }
     protected void updateValues() {
  if (playTime != null && timeSlider != null && volumeSlider != null) {
     Platform.runLater(new Runnable() {
        public void run() {
          Duration currentTime = mp.getCurrentTime();
          playTime.setText(formatTime(currentTime, duration));
          timeSlider.setDisable(duration.isUnknown());
          if (!timeSlider.isDisabled() 
            && duration.greaterThan(Duration.ZERO) 
            && !timeSlider.isValueChanging()) {
              timeSlider.setValue(currentTime.divide(duration).toMillis()
                  * 100.0);
          }
          if (!volumeSlider.isValueChanging()) {
            volumeSlider.setValue((int)Math.round(mp.getVolume() 
                  * 100));
          }
        }
     });
  }
}
private static String formatTime(Duration elapsed, Duration duration) {
   int intElapsed = (int)Math.floor(elapsed.toSeconds());
   int elapsedHours = intElapsed / (60 * 60);
   if (elapsedHours > 0) {
       intElapsed -= elapsedHours * 60 * 60;
   }
   int elapsedMinutes = intElapsed / 60;
   int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 
                           - elapsedMinutes * 60;
 
   if (duration.greaterThan(Duration.ZERO)) {
      int intDuration = (int)Math.floor(duration.toSeconds());
      int durationHours = intDuration / (60 * 60);
      if (durationHours > 0) {
         intDuration -= durationHours * 60 * 60;
      }
      int durationMinutes = intDuration / 60;
      int durationSeconds = intDuration - durationHours * 60 * 60 - 
          durationMinutes * 60;
      if (durationHours > 0) {
         return String.format("%d:%02d:%02d/%d:%02d:%02d", 
            elapsedHours, elapsedMinutes, elapsedSeconds,
            durationHours, durationMinutes, durationSeconds);
      } else {
          return String.format("%02d:%02d/%02d:%02d",
            elapsedMinutes, elapsedSeconds,durationMinutes, 
                durationSeconds);
      }
      } else {
          if (elapsedHours > 0) {
             return String.format("%d:%02d:%02d", elapsedHours, 
                    elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes, 
                    elapsedSeconds);
            }
        }
    }
    }

    /**
     * This will be executed when the button is clicked
     * It increments the count by 1
     */
   

