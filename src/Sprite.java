import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {

    private Image image;
    private double xPos;
    private double yPos;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;

    
    public void update(double time)
    {
        xPos += velocityX * time;
        yPos += velocityY * time;
    }
 
    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, xPos, yPos );
    }
    
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D( xPos, yPos, width, height);
    }
 
    public boolean intersects(Sprite target)
    {
        return target.getBoundary().intersects( this.getBoundary() );
    }
 
}
