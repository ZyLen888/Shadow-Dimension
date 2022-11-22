import java.util.ArrayList;


/**
 * Method allow users to adjust player's speed using keyboard input.
 */
public class TimescaleControl{
    private final static int MAX_COUNT = 3;
    private final static int MIN_COUNT = -3;
    private final static double CONTRAL_SIZE = 0.5;
    private int count;
    public TimescaleControl(){
        this.count = 0;
    }

    /**
     * Set the speed of demons and navec based on keyboard input.
     * @param demons Controlling the speed of demons.
     * @param navec Controlling the speed of navec.
     */
    public void setSpeed(ArrayList<Demon> demons, Navec navec){
        for (Demon demon: demons){
            if(count == -3){
                navec.setMoveSize(navec.getInitSpeed() * Math.pow(CONTRAL_SIZE, 3));
                demon.setMoveSize(demon.getInitSpeed() * Math.pow(CONTRAL_SIZE, 3));
            }
            if(count == -2){
                navec.setMoveSize(navec.getInitSpeed() * Math.pow(CONTRAL_SIZE, 2));
                demon.setMoveSize(demon.getInitSpeed() * Math.pow(CONTRAL_SIZE, 2));
            }
            if(count == -1){
                navec.setMoveSize(navec.getInitSpeed() * CONTRAL_SIZE);
                demon.setMoveSize(demon.getInitSpeed() * CONTRAL_SIZE);
            }
            if(count == 0){
                navec.setMoveSize(navec.getInitSpeed());
                demon.setMoveSize(demon.getInitSpeed());
            }
            if(count == 1){
                navec.setMoveSize(navec.getInitSpeed() * (1 + CONTRAL_SIZE));
                demon.setMoveSize(demon.getInitSpeed() * (1 + CONTRAL_SIZE));
            }
            if(count == 2){
                navec.setMoveSize(navec.getInitSpeed() * Math.pow((1 + CONTRAL_SIZE), 2));
                demon.setMoveSize(demon.getInitSpeed() * Math.pow((1 + CONTRAL_SIZE), 2));
            }
            if(count == 3){
                navec.setMoveSize(navec.getInitSpeed() * Math.pow((1 + CONTRAL_SIZE), 3));
                demon.setMoveSize(demon.getInitSpeed() * Math.pow((1 + CONTRAL_SIZE), 3));
            }
        }
    }

    /**
     * Method checks whether the speed increased or decreased is out of the maximum
     * and minimum count allowed.
     * @return A boolean value of whether further speed change is allowed.
     */
    public boolean checkOutOfBound(){
        if (this.count > MAX_COUNT){
            this.count = MAX_COUNT;
            return false;
        } else if (this.count < MIN_COUNT){
            this.count = MIN_COUNT;
            return false;
        }else {
            return true;
        }
    }

    /**
     * Increase the timescale of demons and navec.
     * @param demons Controlling the speed of demons.
     * @param navec Controlling the speed of navec.
     */
    public void increaseTimeScale(ArrayList<Demon> demons, Navec navec){
        this.count++;
        if(checkOutOfBound()){
            setSpeed(demons, navec);
            System.out.println("Sped up, Speed: " + count);
        }
    }

    /**
     * Decrease the timescale of demons and navec.
     * @param demons Controlling the speed of demons.
     * @param navec Controlling the speed of navec.
     */
    public void decreaseTimeScale(ArrayList<Demon> demons, Navec navec){
        this.count--;
        if(checkOutOfBound()){
            setSpeed(demons, navec);
            System.out.println("Slowed down, Speed: " + count);
        }
    }
}