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
    	theStage.setTitle( "Harambe Simulator" );
    
    	Group root = new Group();
    	Scene theScene = new Scene( root );
    	theStage.setScene( theScene );
    	
        Canvas canvas = new Canvas( 1080, 600 );
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

        GraphicsContext gc = canvas.getGraphicsContext2D();

        // sprite initialization 
        int harambeVerticalPosition = 300;
        
        Sprite harambe = new Sprite();
        harambe.setImage("file:graphics/sprites/harambeleft.png");
        harambe.setPosition(400, harambeVerticalPosition);
        
        ArrayList<Sprite> moneybagList = new ArrayList<Sprite>();
        
        for (int i = 0; i < 15; i++)
        {
            Sprite moneybag = new Sprite();
            moneybag.setImage("file:graphics/sprites/harambeleft.png");
            double px = 350 * Math.random() + 50;
            double py = 350 * Math.random() + 50;          
            moneybag.setPosition(px,py);
            moneybagList.add( moneybag );
        }

        System.out.println(System.getProperty("user.dir"));
        
        LongValue lastNanoTime = new LongValue( System.nanoTime() );

        IntValue score = new IntValue(0);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // calculate time since last update.
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                // game logic
                
                harambe.setVelocity(0,0);
                if (input.contains("LEFT")) {
                    harambe.addVelocity(-150,0);
                    harambe.setImage("file:graphics/sprites/harambeleft.png");
                }
                    
                if (input.contains("RIGHT")) {
                    harambe.addVelocity(150,0);
                    harambe.setImage("file:graphics/sprites/haramberight.png");
                }
                
                final double futurePos = harambe.xPos + harambe.velocityX * elapsedTime;
                
                if (!(0 > futurePos || futurePos > 805)) {
                harambe.update(elapsedTime);
                }
                
                // collision detection
                
                Iterator<Sprite> moneybagIter = moneybagList.iterator();
                while ( moneybagIter.hasNext() )
                {
                    Sprite moneybag = moneybagIter.next();
                    if ( harambe.intersects(moneybag) )
                    {
                        moneybagIter.remove();
                        score.value++;
                    }
                }
                
                // render
                
                gc.clearRect(0, 0, 1080, 600);
                harambe.render( gc );
                
                for (Sprite moneybag : moneybagList )
                    moneybag.render( gc );

                String pointsText = "Cash: $" + (100 * score.value);
                gc.fillText( pointsText, 360, 36 );
                gc.strokeText( pointsText, 360, 36 );
            }
        }.start();

        theStage.show();
  
    }
	
}
