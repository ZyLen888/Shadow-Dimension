import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;


/**
 * Class {@code Navec} describe the behaviours of a navec.
 * It inherits all behaviours from the {@code Enemy} class with its own specific metrics.
 */
public class Navec extends Enemy {
    private final Image NAVEC_LEFT = new Image("res/navec/navecLeft.png");
    private final Image NAVEC_RIGHT = new Image("res/navec/navecRight.png");
    private final Image NAVEC_INVINCIBLE_LEFT = new Image("res/navec/navecInvincibleLeft.PNG");
    private final Image NAVEC_INVINCIBLE_RIGHT = new Image("res/navec/navecInvincibleRight.PNG");
    private final static int NAVEC_MAX_HEALTH = 80;
    private final static int NAVEC_ATTACK_RANGE = 200;
    private NavecFire navecFire;

    public Navec(double x, double y) {
        super(x, y);
        if (moveDirection == LEFT) {
            currentImage = NAVEC_LEFT;
        } else {
            currentImage = NAVEC_RIGHT;
        }
        this.healthPoints = NAVEC_MAX_HEALTH;
    }

    public static int getNavecMaxHealth() {
        return NAVEC_MAX_HEALTH;
    }

    /**
     * This method draws the Navec on screen with various behaviours and status.
     * It considers the the moving behaviours of the navec,whether the navec has entered an invincible mode
     * and whether it has collided with player or entities.
     * @param entities An array list of entites which the navec may potentially encounter.
     * @param sinkholes An array list of sinkholes which the navec may potentially encounter.
     * @param player Player which the navec may potentially encounter.
     * @param topLeft The top-left position of the window.
     * @param bottomRight The bottom-right position of the window.
     */

    public void draw(ArrayList<Entity> entities, ArrayList<Sinkhole> sinkholes, Player player, Point topLeft, Point bottomRight) {
        Point centerPlayer = new Point(player.getX() + player.getCurrentImage().getWidth()/2, player.getY() + player.getCurrentImage().getHeight()/2);
        Point centerNavec = new Point(this.getX() + this.currentImage.getWidth()/2, this.getY() + this.currentImage.getHeight()/2);
        move();
        if (moveDirection == LEFT) {
            if (isInvincible) {
                currentImage = NAVEC_INVINCIBLE_LEFT;
            } else {
                currentImage = NAVEC_LEFT;
            }
        } else {
            if (isInvincible) {
                currentImage = NAVEC_INVINCIBLE_RIGHT;
            } else {
                currentImage = NAVEC_RIGHT;
            }
        }

        if (checkBlockCollision(entities, sinkholes) || isOutofBound(topLeft, bottomRight, this)) {
            moveBack();
        }
        if (inAttackCircle(centerPlayer,centerNavec) && !isDead()) {
            navecFire = new NavecFire(getX() + currentImage.getWidth() / 2, getY() + currentImage.getHeight() / 2,
                    player.getX() + player.getCurrentImage().getWidth() / 2, player.getY() + player.getCurrentImage().getHeight() / 2);
        }
        if (navecFire != null && inAttackCircle(centerPlayer,centerNavec) && !isDead()) {
            navecFire.draw(player);
        }
        if (isInvincible){
            invincibleFrame++;
        }
        if (invincibleFrame / (REFRESH_RATE / 1000) > INVINCIBLE_TIME) {
            isInvincible = false;
            invincibleFrame = 0;
        }
        if (!isDead()) {
            currentImage.drawFromTopLeft(getX(), getY());
            renderHealthPoints();
        }

    }

    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints / NAVEC_MAX_HEALTH) * 100;
        setHealthColour(percentageHP);
        FONT.drawString(Math.round(percentageHP)+"%", getX(), getY() - 6, getColour());
    }

    private boolean inAttackCircle(Point centerPlayer, Point centerNavec){
        if(centerPlayer.distanceTo(centerNavec) <= NAVEC_ATTACK_RANGE){
            return true;
        }
        return false;
    }

}
