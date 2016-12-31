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
	
	// Canvas Size
	private int canvasWidth = 1080;
	private int canvasHeight = 600;
    
	
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    public Sprite makeBanana() {
    	   Sprite banana = new Sprite();
           banana.setImage("file:graphics/sprites/banana.png");
           double bananaWidth = banana.image.getWidth();
           
           
           double potentialX = canvasWidth * Math.random() - bananaWidth;
           
           while (potentialX < 0) {
           	potentialX = canvasWidth * Math.random() - bananaWidth;
           }
           
           double px = potentialX;
           double py = Math.random() * -40 - banana.image.getHeight();
           banana.setPosition(px,py);
           banana.setVelocity(0, Math.random() * 30 + 60);
           return banana;
    }
    
    
    @Override
    public void start(Stage theStage) 
    {
    	
    	// Setting the Stage
    	theStage.setTitle( "Harambe Simulator" );
    
    	Group root = new Group();
    	Scene theScene = new Scene( root );
    	theStage.setScene( theScene );
    	
    	
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
        
        Font theFont = Font.font( "Calibri", FontWeight.BOLD, 30 );
        gc.setFont( theFont );
        gc.setFill( Color.BLACK);
        gc.setStroke( Color.BLACK );
        gc.setLineWidth(1);

        // Initializing Sprites
        int harambeVerticalPosition = 350;
        Image ogHarambeLeft = new Image("file:graphics/sprites/harambeleft.png");
        Image ogHarambeRight = new Image("file:graphics/sprites/haramberight.png");
        
        Double harambeScaleFactor = 0.5;
        Double harambeWidth = ogHarambeLeft.getWidth() * harambeScaleFactor;
        Double harambeHeight = ogHarambeRight.getHeight() * harambeScaleFactor;
        
        Image harambeLeft = new Image("file:graphics/sprites/harambeleft.png",
        		harambeWidth, harambeHeight, false, false);
        Image harambeRight = new Image("file:graphics/sprites/haramberight.png",
        		harambeWidth, harambeHeight, false, false);
        
        Sprite harambe = new Sprite();
        harambe.setImage(harambeLeft);
        harambe.setPosition(canvasWidth/2 - harambeWidth/2, harambeVerticalPosition);
        
        ArrayList<Sprite> bananaList = new ArrayList<Sprite>();
        
        for (int i = 0; i < 8; i++) {
            bananaList.add( makeBanana() );
        }

        System.out.println(System.getProperty("user.dir"));
        
        LongValue lastNanoTime = new LongValue( System.nanoTime() );
        IntValue score = new IntValue(0);
        IntValue counter = new IntValue(0);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                // Calculating Time and Score Since Last Update
                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;
                
                
                if (counter.value == 30) {
                	score.value++;
                	counter.value = 0;
                }
                else counter.value++;
                
                // Handling Game Logic Corresponding to Input
                
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
                
                // Making New Bananas
                if (bananaList.size() < Math.random() * 6 + 3) {
                		bananaList.add(makeBanana());
                }
                // Detecting Collisions
                
                Iterator<Sprite> bananaIter = bananaList.iterator();
                while ( bananaIter.hasNext() )
                {
                    Sprite banana = bananaIter.next();
                    if ( harambe.intersects(banana) )
                    {
                        bananaIter.remove();
                        score.value += 100;
                    }
                }
                
                // Rendering Graphics
                
                Image background = new Image("file:graphics/background.png",
                		canvasWidth, canvasHeight, false, false);
                
                gc.drawImage(background, 0, 0);
                harambe.render( gc );
                
                for (Sprite banana : bananaList ) {
                	if (5 > (Math.abs(banana.yPos - harambeVerticalPosition
                			- harambeHeight + banana.image.getHeight()))) {
                		banana.timer++;
                		if (banana.timer == 50) {
                			banana.setImage("file:graphics/sprites/badbanana.png");
                		}
                		if (banana.timer == 80) {

                			banana.setImage("file:graphics/sprites/realbadbanana.png");
                		}
                	 	if (banana.timer == 100) {
                    		bananaList.remove(bananaList.indexOf(banana));
                    	} 
                	}
 
                	else banana.update(elapsedTime);
                	
                    banana.render( gc );
                }

                String pointsText = "Score: " + score.value;
                gc.fillText( pointsText, 20, 40 );
                gc.strokeText( pointsText, 20, 40 );
            }
        }.start();

        theStage.show();
  
    }
	
}
