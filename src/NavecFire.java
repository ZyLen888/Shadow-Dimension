import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * Class {@code Navec} is the child class of Fire which has similar behaviours to it.
 * This class describes the behaviours and effects of the NavecFire on the player.
 */
public class NavecFire extends Fire{
    public NavecFire(double x, double y, double playerX, double playerY){
        super(x, y, playerX, playerY);
        currentImage = new Image("res/navec/navecFire.png");
        damagePoint = 20;
    }
    public void printMessage(Player player){
        System.out.println("Navec inflicts 20 damage points on Fae. Fae's current health: "
                + player.getHealthPoints() + "/" + player.getMAXHealthPoint());
    }
}
