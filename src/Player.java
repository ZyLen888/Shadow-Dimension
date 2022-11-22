import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;


/**
 * Class {@code Player} describes all behaviours and status of the player.
 */
public class Player{
    private final Image FAE_LEFT = new Image ("res/fae/faeLeft.png");
    private final Image FAE_RIGHT = new Image ("res/fae/faeRight.png");
    private final Image FAE_ATTACK_LEFT = new Image ("res/fae/faeAttackLeft.png");
    private final Image FAE_ATTACK_RIGHT = new Image ("res/fae/faeAttackRight.png");
    private final static int MAX_HEALTH_POINTS = 100;
    private final static double MOVE_SIZE = 2;
    private final static int WIN_X = 950;
    private final static int WIN_Y = 670;

    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int ORANGE_RANGE = 65;
    private final static int RED_RANGE = 35;
    private final static double COOL_DOWN_TIME = 2000;
    private final static double ATTACK_TIME = 1000;
    private final static double INVINCIBLE_TIME = 3000;
    private final static double REFRESH_RATE = 60;
    private final static int SINKHOLE_DAMAGE = 30;
    protected int damagePoint = 20;
    private double attackFrame;
    private double coolDownFrame;
    private double invincibleFrame;
    private boolean attackState;
    private boolean coolDownState;
    private boolean invincibleState;

    private final static int FONT_SIZE = 30;
    private final Font FONT = new Font("res/frostbite.ttf", FONT_SIZE);
    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);

    private double x;
    private double y;
    private double prevX;
    private double prevY;
    private int healthPoints;
    private Image currentImage;
    private boolean facingRight;

    public Player(double x, double y){
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.healthPoints = MAX_HEALTH_POINTS;
        this.currentImage = FAE_RIGHT;
        this.coolDownState = false;
        this.attackState = false;
        this.invincibleState = false;
        this.coolDownFrame = 0;
        this.attackFrame = 0;
        this.invincibleFrame = 0;
        this.facingRight = true;
        COLOUR.setBlendColour(GREEN);
    }

    public boolean isInvincibleState() {
        return invincibleState;
    }

    public void setInvincibleState(boolean invincibleState) {
        this.invincibleState = invincibleState;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getPrevX() {
        return prevX;
    }

    public void setPrevX(double prevX) {
        this.prevX = prevX;
    }

    public double getPrevY() {
        return prevY;
    }

    public void setPrevY(double prevY) {
        this.prevY = prevY;
    }

    public int getHealthPoint(){
        return healthPoints;
    }

    public int getMAXHealthPoint(){
        return MAX_HEALTH_POINTS;
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public static int getMaxHealthPoints() {
        return MAX_HEALTH_POINTS;
    }

    /**
     * This method decreases the player's health point when it's attacked.
     * @param decrease The amount of health points to decrease by.
     */
    public void decreaseHealth(double decrease){
        if(this.healthPoints <= 0){
            this.healthPoints = 0;
        }else{
            this.healthPoints -= decrease;
        }
    }

    /**
     *  This method draws the player on screen with various behaviours and status.
     *  It considers the status of the player, the moving behaviours of the player corresponding to the key input,
     *  whether the player has entered an invincible mode and whether it has collided with entities or enemies.
     * @param input The keyboard input
     * @param entities An array list of entites which the player may potentially encounter.
     * @param sinkholes An array list of sinkholes which the player may potentially encounter.
     * @param demons An array list of demons which the player may potentially encounter.
     * @param navec An array list of navec which the player may potentially encounter.
     * @param topLeft The top-left position of the window.
     * @param bottomRight The bottom-right position of the window.
     * @param hasLevelUp Check whether the player has leveled up.
     */
    public void draw(Input input, ArrayList<Entity> entities, ArrayList<Sinkhole> sinkholes,
                     ArrayList<Demon> demons, Navec navec, Point topLeft, Point bottomRight, boolean hasLevelUp){

        if (input.isDown(Keys.UP)){
            setPrevPosition();
            move(0, -MOVE_SIZE);
        } else if (input.isDown(Keys.DOWN)){
            setPrevPosition();
            move(0, MOVE_SIZE);
        } else if (input.isDown(Keys.LEFT)){
            setPrevPosition();
            move(-MOVE_SIZE,0);
            if (facingRight) {
                this.currentImage = FAE_LEFT;
                facingRight = !facingRight;
            }
        } else if (input.isDown(Keys.RIGHT)){
            setPrevPosition();
            move(MOVE_SIZE,0);
            if (!facingRight) {
                this.currentImage = FAE_RIGHT;
                facingRight = !facingRight;
            }
        }

        if (input.wasPressed(Keys.A) && !coolDownState){
            attackState = true;
            if(facingRight){
                currentImage = FAE_ATTACK_RIGHT;
            }else{
                currentImage = FAE_ATTACK_LEFT;
            }
        }

        if(attackState){
            checkDemonCollision(demons, navec);
            attackFrame++;
        }

        if(attackFrame / (REFRESH_RATE / 1000) > ATTACK_TIME){
            attackState = false;
            coolDownState = true;
            attackFrame = 0;
            if(facingRight){
                currentImage = FAE_RIGHT;
            } else{
                currentImage = FAE_LEFT;
            }
        }

        if(coolDownState){
            coolDownFrame++;
        }

        if(coolDownFrame / (REFRESH_RATE / 1000) > COOL_DOWN_TIME){
            coolDownState = false;
            coolDownFrame = 0;
            if(facingRight){
                currentImage = FAE_RIGHT;
            } else{
                currentImage = FAE_LEFT;
            }
        }

        if(invincibleState){
            invincibleFrame++;
        }

        if(invincibleFrame / (REFRESH_RATE / 1000) > INVINCIBLE_TIME){
            // ?
            invincibleState = false;
            invincibleFrame = 0;
        }

        if(checkEntityCollision(entities, sinkholes) || isOutofBound(topLeft, bottomRight, this)){
            moveBack();
        }

        currentImage.drawFromTopLeft(this.getX(), this.getY());
        renderHealthPoints();

    }
    /**
     * Method that stores Fae's previous position
     */
    public void setPrevPosition(){
        this.prevX = x;
        this.prevY = y;
    }
    /**
     * Method that moves Fae back to previous position
     */
    public void moveBack(){
        this.x = prevX;
        this.y = prevY;
    }

    /**
     * Method that moves Fae given the direction
     * @param xMove The amount of movement on x-axis
     * @param yMove The amount of movement on y-axis
     */
    private void move(double xMove, double yMove){
        double newX = x + xMove;
        double newY = y + yMove;
        x = newX;
        y = newY;
    }

    /**
     * This method {@code getBoundingBox} creates a rectangle box centered around the player.
     * @return A rectangular box with its center at the center of the image (the player)
     */
    public Rectangle getBoundingBox(){
        return currentImage.getBoundingBoxAt(new Point(this.getX() + currentImage.getWidth()/2,
                this.getY() + currentImage.getHeight()/2));
    }

    /**
     * Method that renders the current health as a percentage on screen
     */
    private void renderHealthPoints(){
        double percentageHP = ((double) healthPoints/MAX_HEALTH_POINTS) * 100;
        if (percentageHP <= RED_RANGE){
            COLOUR.setBlendColour(RED);
        } else if (percentageHP <= ORANGE_RANGE){
            COLOUR.setBlendColour(ORANGE);
        }
        FONT.drawString(Math.round(percentageHP) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    /**
     * Method that checks if Fae's health has depleted
     */
    public boolean isDead() {
        return healthPoints <= 0;
    }

    /**
     * Method that checks if Fae has found the gate at Level 0
     */
    public boolean reachedGate(){
        return (this.x >= WIN_X) && (this.y >= WIN_Y);
    }

    private boolean checkEntityCollision(ArrayList<Entity> entities, ArrayList<Sinkhole> sinkholes){
        Rectangle playerBox = this.getBoundingBox();
        for(Entity entity: entities){
            Rectangle entityBox = entity.getBoundingBox().getBoundingBox();
            if(playerBox.intersects(entityBox)){
                return true;
            }
        }
        for(Sinkhole sinkhole: sinkholes){
            Rectangle sinkholeBox = sinkhole.getBoundingBox().getBoundingBox();
            if(playerBox.intersects(sinkholeBox) && sinkhole.getIsActive()){
                sinkhole.setIsActive(false);
                this.decreaseHealth(SINKHOLE_DAMAGE);
                System.out.println("Sinkhole inflicts 30 damage points on Fae. Fae's current health: "
                            + this.getHealthPoints() + "/" + this.getMAXHealthPoint());
                return true;
            }
        }
        return false;
    }

    private boolean isOutofBound(Point topLeft, Point bottomRight, Player player){
        return (player.getY() > bottomRight.y)|| (player.getY() < topLeft.y)||
                (player.getX() < topLeft.x) || (player.getX() > bottomRight.x);
    }

    private boolean checkDemonCollision(ArrayList<Demon> demons, Navec navec){
        Rectangle playerBox = this.getBoundingBox();
        Rectangle navecBox = navec.getBoundingBox();
        for(Demon demon: demons){
            Rectangle demonBox = demon.getBoundingBox();
            if(playerBox.intersects(demonBox) && !demon.isInvincible && !demon.isDead()){
                demon.loseHealthPoint(damagePoint);
                demon.setInvincible(true);
                System.out.println("Fae inflicts 20 damage points on Demon. Demon's current health: "
                        + (int)demon.getHealthPoints() + "/" + demon.getMaxHealthPoint());
                return true;
            }
        }
        if (playerBox.intersects(navecBox) && !navec.getIsInvincible()){
            navec.loseHealthPoint(damagePoint);
            navec.setInvincible(true);
            System.out.println("Fae inflicts 20 damage points on Navec. Navec's current health: "
                            + (int)navec.getHealthPoints() + "/" + navec.getNavecMaxHealth());
            return true;
        }
        return false;
    }

}