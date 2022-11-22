import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.*;

import java.util.ArrayList;
import java.util.Random;
import bagel.*;


/**
 * Class {@code Enemy} is the parent class for both {@code Demon} and {@code Navec}.
 * It describes the general behaviours of the enemy and how they interact
 * with the {@code Player} and static {@code Entity}.
 */
public class Enemy{
    private double x;
    private double y;
    private final double MIN_SPEED = 0.2;
    private final double MAX_SPEED = 0.7;
    protected Image currentImage;
    protected final static int FONT_SIZE = 15;
    protected final static Font FONT = new Font("res/frostbite.ttf",FONT_SIZE);
    protected boolean isAggressive;

    // Setting up random speed of an enemy
    private double moveSize = MIN_SPEED + new Random().nextDouble() * (MAX_SPEED - MIN_SPEED);
    // Setting up the initial speed of an enemy
    private double initSpeed;

    // Colour for the Health Bar of an enemy
    private final static int ORANGE_RANGE = 65;
    private final static int RED_RANGE = 35;
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0.0);
    private final static Colour RED = new Colour(1.0, 0.0, 0.0);
    private final DrawOptions COLOUR = new DrawOptions();

    //Direction
    protected final int UP = 0;
    protected final int DOWN = 1;
    protected final int LEFT = 2;
    protected final int RIGHT = 3;
    protected final static int MOVE_DIRECTIONS = 4;
    protected int moveDirection = new Random().nextInt(MOVE_DIRECTIONS);

    // Invincible time
    protected final static double INVINCIBLE_TIME = 3000;
    protected final static double REFRESH_RATE = 60;
    protected int healthPoints;
    protected boolean isInvincible;
    protected double invincibleFrame;

    public Enemy(double x, double y){
        this.x = x;
        this.y = y;
        this.isInvincible = false;
        this.invincibleFrame = 0;
        this.initSpeed = moveSize;
        //this.boundingBox = new BoundingBox(x, y, currentImage);
    }

    public void setX(double x){
        this.x = x;
    }
    public double getX(){
        return x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getY(){
        return y;
    }
    public boolean getIsInvincible(){
        return isInvincible;
    }
    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }
    public double getHealthPoints(){
        return healthPoints;
    }
    public DrawOptions getColour() {
        return COLOUR;
    }
    public double getMoveSize(){
        return moveSize;
    }
    public void setMoveSize(double moveSize){
        this.moveSize = moveSize;
    }
    public double getInitSpeed() {
        return initSpeed;
    }

    /**
     * This method let the enemy lose health point when it's attacked.
     * @param damagePoint The damage being done on the enemy by the player
     */
    public void loseHealthPoint(int damagePoint){
        this.healthPoints -= damagePoint;
    }

    /**
     * This method evaluates whether an enemy is considered dead.
     * @return The boolean value of whether the enemy is considered dead
     * (if the {@code healthPoints} is equal to or below 0.
     */
    public boolean isDead(){
        return healthPoints <= 0;
    }

    /**
     * This method {@code move} enables the enemy to move {@code UP}, {@code DOWN},
     * {@code LEFT} and {@code RIGHT}.
     */
    public void move(){
        if(moveDirection == UP){
            moveAction(0, -moveSize);
        } else if (moveDirection == DOWN){
            moveAction(0, moveSize);
        } else if (moveDirection == LEFT){
            moveAction(-moveSize, 0);
        } else{
            moveAction(moveSize, 0);
        }
    }

    private void moveAction(double xMove, double yMove){
        this.setX(this.getX() + xMove);
        this.setY(this.getY() + yMove);
    }


    /**
     * This method enables the enemy to be halted and move the opposite direction
     * when it encounters a static entity.
     */
    public void moveBack(){
        if(moveDirection == UP){
            moveDirection = DOWN;
        } else if(moveDirection == DOWN){
            moveDirection = UP;
        } else if(moveDirection == LEFT){
            moveDirection = RIGHT;
        } else{
            moveDirection = LEFT;
        }
    }

    /**
     * This method {@code getBoundingBox} creates a rectangle box centered around the enemy.
     * @return A rectangular box with its center at the centre of the image (the enemy)
     */
    public Rectangle getBoundingBox(){
        return currentImage.getBoundingBoxAt(new Point(this.getX() + currentImage.getWidth()/2,
                this.getY() + currentImage.getHeight()/2));
    }



    protected boolean checkBlockCollision(ArrayList<Entity> entities, ArrayList<Sinkhole> sinkholes){
        Rectangle enemyBox = this.getBoundingBox();
        for(Entity entity: entities){
            Rectangle entityBox = entity.getBoundingBox().getBoundingBox();
            if(enemyBox.intersects(entityBox)){
                return true;
            }
        }
        for(Sinkhole sinkhole: sinkholes){
            Rectangle sinkholeBox = sinkhole.getBoundingBox().getBoundingBox();
            if(enemyBox.intersects(sinkholeBox) && sinkhole.getIsActive()){
                return true;
            }
        }
        return false;
    }

    protected boolean isOutofBound(Point topLeft, Point bottomRight, Enemy enemy){
        return (enemy.getY() > bottomRight.y)|| (enemy.getY() < topLeft.y)|| (enemy.getX() < topLeft.x) || (enemy.getX() > bottomRight.x);
    }

    protected void setHealthColour(double percentageHP){
        if(percentageHP <= RED_RANGE){
            COLOUR.setBlendColour(RED);
        }else if(percentageHP <= ORANGE_RANGE){
            COLOUR.setBlendColour(ORANGE);
        }else{
            COLOUR.setBlendColour(GREEN);
        }
    }
}
