import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.Random;


/**
 * Class {@code Demon} describe the behaviours of a demon.
 * It inherits all behaviours from the {@code Enemy} class with its own specific metrics.
 */
public class Demon extends Enemy{
    private final Image DEMON_LEFT = new Image ("res/demon/demonLeft.png");
    private final Image DEMON_RIGHT = new Image ("res/demon/demonRight.png");
    private final Image DEMON_INVINCIBLE_LEFT = new Image ("res/demon/demonInvincibleLeft.PNG");
    private final Image DEMON_INVINCIBLE_RIGHT = new Image ("res/demon/demonInvincibleRight.PNG");
    private final static int MAX_HEALTH_POINT = 40;
    private static final int ATTACK_RANGE = 150;
    private Random random = new Random();
    private Fire fire;
    public Demon(int x, int y){
        super (x, y);
        if (moveDirection == LEFT){
            currentImage = DEMON_LEFT;
        } else{
            currentImage = DEMON_RIGHT;
        }
        this.healthPoints = MAX_HEALTH_POINT;
        isAggressive = random.nextBoolean();
    }

    public static int getMaxHealthPoint(){
        return MAX_HEALTH_POINT;
    }

    /**
     * This method draws the demon on screen with various behaviours and status.
     * It considers the intial status of the demon (aggressive), the moving behaviours of the demon,
     * whether the demon has entered an invincible mode and whether it has collided with player or entities.
     * @param entities An array list of entites which the demon may potentially encounter.
     * @param sinkholes An array list of sinkholes which the demon may potentially encounter.
     * @param player Player which the demon may potentially encounter.
     * @param topLeft The top-left position of the window.
     * @param bottomRight The bottom-right position of the window.
     */
    public void draw(ArrayList<Entity> entities, ArrayList<Sinkhole> sinkholes, Player player, Point topLeft, Point bottomRight){
        Point centerPlayer = new Point(player.getX() + player.getCurrentImage().getWidth()/2, player.getY() + player.getCurrentImage().getHeight()/2);
        Point centerDemon = new Point(this.getX() + this.currentImage.getWidth()/2, this.getY() + this.currentImage.getHeight()/2);
        if(isAggressive){
            move();
            if(moveDirection == LEFT){
                if(isInvincible){
                    currentImage = DEMON_INVINCIBLE_LEFT;
                }else{
                    currentImage = DEMON_LEFT;
                }
            } else{
                if(isInvincible){
                    currentImage = DEMON_INVINCIBLE_RIGHT;
                }else{
                    currentImage = DEMON_RIGHT;
                }
            }
        } else{
            if(isInvincible){
                currentImage = DEMON_INVINCIBLE_RIGHT;
            }else{
                currentImage = DEMON_RIGHT;
            }
        }
        if (checkBlockCollision(entities, sinkholes)|| isOutofBound(topLeft, bottomRight, this)){
            moveBack();
        }
        if (inAttackCircle(centerPlayer, centerDemon) && !isDead()){
            fire = new Fire(getX() + currentImage.getWidth()/2, getY() + currentImage.getHeight()/2, player.getX() + player.getCurrentImage().getWidth()/2, player.getY() + player.getCurrentImage().getHeight()/2);
        }
        if (fire != null && inAttackCircle(centerPlayer, centerDemon)){
            fire.draw(player);
        }
        if(isInvincible){
            invincibleFrame++;
        }
        if(invincibleFrame / (REFRESH_RATE / 1000) > INVINCIBLE_TIME){
            isInvincible = false;
            invincibleFrame = 0;
        }
        if(!isDead()){
            currentImage.drawFromTopLeft(getX(),getY());
            renderHealthPoints();
        }
    }

    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints / MAX_HEALTH_POINT) * 100;
        setHealthColour(percentageHP);
        FONT.drawString(Math.round(percentageHP)+"%", getX(), getY() - 6, getColour());
    }

    private boolean inAttackCircle(Point centerPlayer, Point centerDemon){
        if(centerPlayer.distanceTo(centerDemon) <= ATTACK_RANGE){
            return true;
        }
        return false;
    }

}
