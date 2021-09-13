package ConwayGOL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.io.*;

public class GameOfLife extends JFrame implements Runnable, MouseListener, MouseMotionListener {
    private final BufferStrategy strategy;
    private static final Dimension WindowSize = new Dimension(800, 800);
    private static boolean isInitialised = false;
    private final Graphics offscreenGraphics;
    private boolean[][][] gameState = new boolean[40][40][2];
    private boolean gameInProgress = false;
    private int k = 0;
    private final String filename = "C:\\Users\\bobby\\OneDrive\\Desktop\\LifeGame.txt";

    public GameOfLife(){
        //Create and set up the window.
        this.setTitle("Pacman, or something..");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window, centred on the screen
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);

        addMouseListener(this);
        addMouseMotionListener(this);


        Thread t = new Thread(this);
        t.start();

        createBufferStrategy(2);
        strategy = getBufferStrategy();
        offscreenGraphics = strategy.getDrawGraphics();

        isInitialised = true;



    }

    public void run() {
        while (true) {
            try { Thread.sleep(50);
            } catch (InterruptedException e) { e.printStackTrace(); }
            if (gameInProgress){ start();}
            this.repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        Point clicked = e.getPoint();

        if (check(clicked.x, 50, 150 ) && check(clicked.y, 50, 80)){
            gameInProgress = true;
        }

        if (check(clicked.x, 200, 300) && check(clicked.y, 50, 80)){
            randomise();
        }

        if (check(clicked.x, 350, 450) && check(clicked.y, 50, 80)){
            saveFile();
        }
        if (check(clicked.x, 500, 700) && check(clicked.y, 50, 80)){
            loadFile();
            System.out.println(2);
        }

        int xVal = (int) (clicked.getX() / 20);
        int yVal = (int) (clicked.getY() / 20);

        if(!gameState[xVal][yVal][0]) gameState[xVal][yVal][0] = true;
        else if(gameState[xVal][yVal][0]) gameState[xVal][yVal][0] = false;
    }

    private void randomise() {
        for(int x=0;x<40;x++) {
            for(int y=0;y<40;y++) {
                gameState[x][y][0]= (Math.random()<0.25);
            }
        }
    }

    public void start() {
        for (int x=0; x<40; x++){
            for (int y=0; y<40; y++){
                if (gameState[x][y][0]){
                    int count = 0;
                    count += (gameState[(39+x) % 40][y][0]) ? 1:0;
                    count += (gameState[(39+x) % 40][(39 + y) % 40][0]) ? 1:0;
                    count += (gameState[(39+x) % 40][(y + 1) % 40][0]) ? 1:0;
                    count += (gameState[x][(39+y)%40][0]) ? 1:0;
                    count += (gameState[x][(y + 1)%40][0]) ? 1:0;
                    count += (gameState[(1+x) % 40][y][0]) ? 1:0;
                    count += (gameState[(1+x) % 40][(39 + y) % 40][0]) ? 1 :0;
                    count += (gameState[(1+x) % 40][(y + 1)%40][0]) ? 1:0;

                    if (gameState[x][y][0] && count < 2){ gameState[x][y][1] = false; }
                    else if(gameState[x][y][0] && (count == 2 || count == 3)){gameState[x][y][1] = true;}
                    else if(gameState[x][y][0] && count > 3){gameState[x][y][1] = false;}
                    else if(gameState[x][y][0] && count == 3){gameState[x][y][1] = true;}
                }
            }
        }

        for (int x = 0; x < 40; x++) { for (int y=0; y<40; y++) { gameState[x][y][(k + 1) % 2] = gameState[x][y][k]; } }
        k = (k + 1) % 2;
        }

    public void saveFile(){
        String outputtext = "";

        for (boolean[][] booleans : gameState) {
            for (int j = 0; j < gameState.length; j++) {
                if (booleans[j][0]) {
                    outputtext += "0";
                } else {
                    outputtext += "1";
                }
            }
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(outputtext);
            writer.close();
        }
        catch(IOException ignored) { }
    }
    public void loadFile(){
        String line = null;
        int j = -1;
        int x = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            do {
                try{
                    line = reader.readLine();
                    for (int i = 0; i < line.length(); i++) {
                        if (i % 40 == 0){
                            j++;
                            x = 0;
                        }
                        gameState[j][x][0] = line.charAt(i) == '0';
                        x++;
                    }
                }
                catch (IOException ignored){}
            }
            while (line != null);
            reader.close();
        }
        catch (IOException ignored){}
    }
    public boolean check(int number, int startVal, int endVal){
        boolean test = false;
        int[] numbers = new int[endVal-startVal + 1];
        int j = 0;
        for (int i = startVal; i <= endVal ; i++) {
            numbers[j] = i;
            j++;
        }

        for (int element : numbers){
            if (element == number) {
                test = true;
                break;
            }
        }
        return test;
    }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    
    public void paint(Graphics g) {
        if(!isInitialised) return;
        g = offscreenGraphics;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);


        for (int i = 0; i < gameState.length; i++) {
            for (int j = 0; j< gameState.length; j++){
                if (gameState[i][j][0]){
                    g.setColor(Color.white);
                    g.fillRect(i*20, j*20, 20, 20);
                }
            }
        }

        if(!gameInProgress){
            g.setColor(Color.GREEN);
            g.fillRect(50, 50, 100, 30);
            g.fillRect(200, 50, 100, 30);
            g.fillRect(350, 50, 100, 30);
            g.fillRect(500, 50, 100, 30);

            g.setFont(new Font("Times", Font.PLAIN, 24));
            g.setColor(Color.BLACK);
            g.drawString("Start", 60, 70);
            g.drawString("Random", 210, 70);
            g.drawString("Save",360, 70 );
            g.drawString("Load", 510, 70);
        }
        strategy.show();
    }

    public static void main(String[] args){ new GameOfLife(); }

    @Override
    public void mouseDragged(MouseEvent e) {

        Point clicked = e.getPoint();
        System.out.println(clicked);
        int xVal = (int) (clicked.getX() / 20);
        int yVal = (int) (clicked.getY() / 20);

        gameState[xVal][yVal][0] = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) { }
}
