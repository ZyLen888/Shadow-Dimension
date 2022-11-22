import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;


/**
 * Class {@code BoundingBox} creates a rectangle box for an entity (movable or static) with its image and x, y axis.
 */
public class BoundingBox {
    private Image currentImage;
    private double x;
    private double y;

    /**
     * @param x Horizontal point on x-axis at the centre  of the rectangle
     * @param y Vertical point on y-axis at the centre of the rectangle
     * @param currentImage The image of the player, entity or the enemy
     */
    public BoundingBox(double x, double y, Image currentImage){
        this.x = x;
        this.y = y;
        this.currentImage = currentImage;
    }

    private double getX() {
        return x;
    }

    private double getY() {
        return y;
    }

    /**
     * This method {@code getBoundingBox} creates a rectangle box centered around the entity.
     * @return A rectangular box with its center at the centre of the image (the entity)
     */
    public Rectangle getBoundingBox(){
        return currentImage.getBoundingBoxAt(new Point(this.getX() + currentImage.getWidth()/2,
                this.getY() + currentImage.getHeight()/2));
    }
}
