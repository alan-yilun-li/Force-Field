import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class ForceField extends Application {

    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage theStage) 
    {
    	
    	// Setting the Stage
    	theStage.setTitle( "ForceField" );
    
    	Group root = new Group();
    	Scene theScene = new Scene( root );
    	theStage.setScene( theScene );
    	
        Canvas canvas = new Canvas( 512, 512 );
        root.getChildren().add( canvas );
        
        
        // Handling User Input
        ArrayList<String> input = new ArrayList<String>();

        theScene.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent event)
                {
                    String code = event.getCode().toString();
                    if ( !input.contains(code) )
                        input.add( code );
                }
            });

        theScene.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent event)
                {
                    String code = event.getCode().toString();
                    input.remove( code );
                }
            });
        
        
        
        
    }
	
}
