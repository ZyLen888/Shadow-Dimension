import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;


/**
 * Class {@code Fire} is the parent class of NavecFire which has similar behaviours.
 * This class describes the behaviours and effects of the fire on the player.
 */
public class Fire {
    private DrawOptions rotation;
    protected Image currentImage;
    protected int damagePoint;
    private double x;
    private double y;
    private double playerX;
    private double playerY;

    public Fire(double x, double y, double playerX, double playerY){
        this.x = x;
        this.y = y;
        this.playerX = playerX;
        this.playerY = playerY;
        this.currentImage = new Image("res/demon/demonFire.png");
        this.damagePoint = 10;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getPlayerX() {
        return playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    double degree180 = Math.PI;
    double degree90 = Math.PI/2;
    double degree270 = Math.PI*3/2;


    /**
     * This method draws the fire on screen and evaluates which direction the fire should be emitted.
     * @param player The position of the player and distance from the fire.
     */
    public void draw(Player player){
        if(playerX <= getX() && playerY <= getY()){
            // top-left
            rotation = new DrawOptions();
            setX(getX() - currentImage.getWidth()/2 - currentImage.getWidth());
            setY(getY() - currentImage.getHeight()/2 - currentImage.getHeight());
            currentImage.drawFromTopLeft(getX(), getY(), rotation);
        }else if(playerX <= getX() && playerY > getY()){
            // bottom-left
            rotation = new DrawOptions();
            rotation.setRotation(degree270);
            setX(getX() - currentImage.getWidth()/2 - currentImage.getWidth());
            setY(getY() + currentImage.getHeight()/2);
            currentImage.drawFromTopLeft(getX(), getY(), rotation);
        }else if(playerX > getX() && playerY <= getY()){
            //top right
            rotation = new DrawOptions();
            rotation.setRotation(degree90);
            setX(getX() + currentImage.getWidth()/2);
            setY(getY() - currentImage.getHeight()/2 - currentImage.getHeight());
            currentImage.drawFromTopLeft(getX(), getY(), rotation);
        }else {
            //bottom right
            rotation = new DrawOptions();
            rotation.setRotation(degree180);
            setX(getX() + currentImage.getWidth()/2);
            setY(getY() + currentImage.getHeight()/2);
            currentImage.drawFromTopLeft(getX(), getY(), rotation);
        }
        if(checkFireCollision(player) && !player.isInvincibleState()){
            player.decreaseHealth(damagePoint);
            player.setInvincibleState(true);
            printMessage(player);
        }
    }

    private boolean checkFireCollision(Player player){
        Rectangle playerBox = player.getBoundingBox();
        return playerBox.intersects(this.getBoundingBox());
    }

    public void printMessage(Player player){
        System.out.println("Demon inflicts 10 damage points on Fae.Fae's current health: "
                            + player.getHealthPoints() + "/" + player.getMAXHealthPoint());
    }

    /**
     * This method {@code getBoundingBox} creates a rectangle box around the fire centered
     * around the center of the fire.
     * @return A rectangular box with its center at the centre of the image (the fire)
     */
    public Rectangle getBoundingBox(){
        return currentImage.getBoundingBoxAt(new Point(this.getX() + currentImage.getWidth()/2, this.getY() + currentImage.getHeight()/2));
    }
}
