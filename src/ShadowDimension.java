import bagel.*;
import bagel.util.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * SWEN20003 Project 1, Semester 2, 2022
 * @author Zhen Liu, 1174094, zhen5@student.unimelb.edu.au
 * @author Level 0 Solution provided by Tharun Dharmawickrema
 * Class {@code ShadowDimension} is the Entry point to the game.
 * This Class includes the window, backgrounds and switching between levels as the player levels up.
 */
public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String LEVEL0 = "res/level0.csv";
    private final static String LEVEL1 = "res/level1.csv";
    private final Image BACKGROUND_IMAGE_LV0 = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_LV1 = new Image("res/background1.png");
    private final static int TITLE_FONT_SIZE = 75;
    private final static int INSTRUCTION_FONT_SIZE = 40;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 90;
    private final static int INS_Y_OFFSET = 190;
    private final Font TITLE_FONT = new Font("res/frostbite.ttf", TITLE_FONT_SIZE);
    private final Font INSTRUCTION_FONT = new Font("res/frostbite.ttf", INSTRUCTION_FONT_SIZE);
    private final static String INSTRUCTION_MESSAGE = "PRESS SPACE TO START\nUSE ARROW KEYS TO FIND GATE";
    private final static String INSTRUCTION_MESSAGE_LV1 = "PRESS SPACE TO START\nPRESS A TO ATTACK\nDEFEAT NAVEC TO WIN";
    private final static String END_MESSAGE = "GAME OVER!";
    private final static String WIN_MESSAGE = "CONGRATULATIONS!";

    private final static double REFRESH_RATE = 60;
    private final static double LEVEL_UP_TIME = 3000;
    private Point topLeft;
    private Point bottomRight;
    private Player player;
    private boolean hasStarted;
    private boolean gameOver;
    private boolean playerWin;
    private boolean levelUp;
    private boolean hasLevelUp;
    private int levelUpFrame;
    private final String wallPath = "res/wall.png";
    private final String sinkholePath = "res/sinkhole.png";
    private final String treePath = "res/tree.png";
    private ArrayList<Entity> walls = new ArrayList<>();
    private ArrayList<Entity> trees = new ArrayList<>();
    private ArrayList<Sinkhole> sinkholes = new ArrayList<>();
    private ArrayList<Demon> demons = new ArrayList<>();
    private Navec navec;
    private TimescaleControl timescaleControl;


    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        readCSV(LEVEL0);
        hasStarted = false;
        gameOver = false;
        playerWin = false;
        levelUp = false;
        hasLevelUp = false;
        levelUpFrame = 0;
        timescaleControl = new TimescaleControl();
    }

    /**
     * Method used to read file and create objects
     */
    private void readCSV(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] sections = line.split(",");
                switch (sections[0]) {
                    case "Player":
                        player = new Player(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Wall":
                        walls.add(new Entity(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]), wallPath));
                        break;
                    case "Sinkhole":
                        sinkholes.add(new Sinkhole(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]), sinkholePath));
                        break;
                    case "TopLeft":
                        topLeft = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "BottomRight":
                        bottomRight = new Point(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                    case "Tree":
                        trees.add(new Entity(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]), treePath));
                        break;
                    case "Demon":
                        demons.add(new Demon(Integer.parseInt(sections[1]), Integer.parseInt(sections[2])));
                    case "Navec":
                        navec = new Navec(Integer.parseInt(sections[1]), Integer.parseInt(sections[2]));
                        break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input Generate behaviours with the windows corresponding to the keyboard input.
     */
    @Override
    public void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        if(!hasStarted){
            drawStartScreen(hasLevelUp);
            if (input.wasPressed(Keys.SPACE)){
                hasStarted = true;
            }
        }

        if (gameOver){
            drawMessage(END_MESSAGE);
        } else if (playerWin) {
            drawMessage(WIN_MESSAGE);
        }

        if(levelUp && !hasLevelUp){
            levelUpFrame++;
            drawEndScreen();
        }
        if (levelUpFrame / (REFRESH_RATE / 1000) > LEVEL_UP_TIME || input.wasPressed(Keys.W)){
            hasLevelUp = true;
            sinkholes = new ArrayList<>();
            readCSV(LEVEL1);
            hasStarted = false;
            levelUpFrame = 0;
        }

        // game is running
        if (hasStarted && !gameOver && !playerWin){
            if(!levelUp && !hasLevelUp){
                BACKGROUND_IMAGE_LV0.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
                for(Entity wall: walls){
                    wall.draw();
                }
                for(Sinkhole sinkhole: sinkholes){
                    sinkhole.draw();
                }

                if (player.reachedGate()){
                    levelUp = true;
                }
                player.draw(input, walls, sinkholes, demons, navec, topLeft, bottomRight, hasLevelUp);
            }
            if (hasLevelUp){
                BACKGROUND_IMAGE_LV1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
                if (input.wasPressed(Keys.L)){
                    timescaleControl.increaseTimeScale(demons, navec);
                }
                if (input.wasPressed(Keys.K)){
                    timescaleControl.decreaseTimeScale(demons, navec); //?
                }
                for(Entity tree: trees){
                    tree.draw();
                }
                for(Sinkhole sinkhole: sinkholes){
                    sinkhole.draw();
                }
                for(Demon demon: demons){
                    if(!demon.isDead()){
                        demon.draw(trees, sinkholes, player, topLeft, bottomRight);
                    }
                }
                navec.draw(trees, sinkholes, player, topLeft, bottomRight);
                if (navec.isDead()){
                    playerWin = true;
                }
                player.draw(input, trees, sinkholes, demons, navec, topLeft, bottomRight, hasLevelUp);
            }


            if (player.isDead()){
                gameOver = true;
            }

        }
    }

    /**
     * Method used to draw the start screen title and instructions
     * @param hasLevelUp Check whether the player has level up and print the message accordingly.
     */
    private void drawStartScreen(boolean hasLevelUp){
        if(!hasLevelUp){
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE,TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
        }else{
            INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE_LV1, (Window.getWidth()/2.0 - (INSTRUCTION_FONT.getWidth(INSTRUCTION_MESSAGE_LV1)/2.0)),
                    (Window.getHeight()/2.0 + (INSTRUCTION_FONT_SIZE/2.0)));
        }

    }

    private void drawEndScreen(){
        //TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
        TITLE_FONT.drawString("LEVEL UP!", (Window.getWidth()/2.0 - (TITLE_FONT.getWidth("LEVEL UP!")/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
        //INSTRUCTION_FONT.drawString(INSTRUCTION_MESSAGE,TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);
    }

    /**
     * Method used to draw message.
     */
    private void drawMessage(String message){
        TITLE_FONT.drawString(message, (Window.getWidth()/2.0 - (TITLE_FONT.getWidth(message)/2.0)),
                (Window.getHeight()/2.0 + (TITLE_FONT_SIZE/2.0)));
    }
}