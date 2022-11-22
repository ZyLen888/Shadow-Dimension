import bagel.*;

/**
 * This class {@Entity} contains all behaviours of static entity (including walls and trees).
 */
public class Entity {
    private int x;
    private int y;
    private final Image currentImage;
    private BoundingBox boundingBox;
    public Entity(int x, int y, String filename){
        this.x = x;
        this.y = y;
        this.currentImage = new Image (filename);
        this.boundingBox = new BoundingBox(x, y, currentImage);
    }

    /**
     * This method {@code getBoundingBox} creates a rectangle box centered around the entity.
     * @return A rectangular box with its center at the centre of the image (the entity)
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }


    /**
     * This method draws the image of entities (walls and trees) from given top left points.
     */
    public void draw(){
        currentImage.drawFromTopLeft(this.x, this.y);
    }

}
