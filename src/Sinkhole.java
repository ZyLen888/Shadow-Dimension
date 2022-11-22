public class Sinkhole extends Entity {
    private boolean isActive;
    public Sinkhole(int x, int y, String filename){
        super(x, y, filename);
        this.isActive = true;
    }
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }
    public boolean getIsActive(){
        return isActive;
    }

    /**
     * Method draws the sinkhole on screen.
     */
    public void draw(){
        if(isActive){
            super.draw();
        }
    }
}