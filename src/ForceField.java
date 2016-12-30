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
    	
    	int canvasWidth = 1080;
    	int canvasHeight = 600;
    		
    	
        Canvas canvas = new Canvas( canvasWidth, canvasHeight);
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
        
        Font theFont = Font.font( "Calibri", FontWeight.BOLD, 24 );
        gc.setFont( theFont );
        gc.setFill( Color.BLACK);
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        // sprite initialization 
        int harambeVerticalPosition = 350;
        Image ogHarambeLeft = new Image("file:graphics/sprites/harambeleft.png");
        Image ogHarambeRight = new Image("file:graphics/sprites/haramberight.png");
        
        Double harambeWidth = ogHarambeLeft.getWidth() * 0.7;
        Double harambeHeight = ogHarambeRight.getHeight() * 0.7;
        
        Image harambeLeft = new Image("file:graphics/sprites/harambeleft.png", harambeWidth, harambeHeight, false, false);
        Image harambeRight = new Image("file:graphics/sprites/haramberight.png", harambeWidth, harambeHeight, false, false);
        
        Sprite harambe = new Sprite();
        harambe.setImage(harambeLeft);
        harambe.setPosition(400, harambeVerticalPosition);
        
        ArrayList<Sprite> moneybagList = new ArrayList<Sprite>();
        
        for (int i = 0; i < 15; i++)
        {
            Sprite moneybag = new Sprite();
            moneybag.setImage("file:graphics/sprites/banana.png");
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
                    harambe.setImage(harambeLeft);
                }
                    
                if (input.contains("RIGHT")) {
                    harambe.addVelocity(150,0);
                    harambe.setImage(harambeRight);
                }
                
                final double futurePos = harambe.xPos + harambe.velocityX * elapsedTime;
                
                if (!(0 > futurePos || futurePos > (canvasWidth - harambeWidth))) {
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
                
                Image background = new Image("file:graphics/background.png", 1080, 600, false, false);
                
                //gc.clearRect(0, 0, 1080, 600);
                gc.drawImage(background, 0, 0);
                harambe.render( gc );
                
                for (Sprite moneybag : moneybagList )
                    moneybag.render( gc );

                String pointsText = "Cash: $" + (100 * score.value);
                gc.fillText( pointsText, 925, 36 );
                gc.strokeText( pointsText, 925, 36 );
            }
        }.start();

        theStage.show();
  
    }
	
}
