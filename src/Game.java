import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

public class Game extends Thread {
    Window gameWindow;
    Map map;
    Pathfinder2 pf;

    public Game(Map map, Pathfinder2 pf) {
        gameWindow = new Window("Milton Arcade", 1000, 700);
        this.map = map;
        this.pf = pf;
    }

    @Override
    public void run() {
        Graphics2D g = gameWindow.getDrawGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, 1000, 700);
        g.setFont(new Font("Helvetica", Font.PLAIN, 10));

        while (true) {
            pf.stuck = !gameWindow.spaceDown;

            for (int x = 0; x < map.cols; x++) {
                for (int y = 0; y < map.rows; y++) {
                    switch (map.getNode(y * map.rows + x).type) {
                        case UNKNOWN:
                            g.setColor(Color.GRAY);
                            break;
                        case DISCOVERED:
                            g.setColor(Color.red);
                            break;
                        case END:
                            g.setColor(Color.BLUE);
                            break;
                        case PATH:
                            g.setColor(Color.GREEN);
                            break;
                        case START:
                            g.setColor(Color.CYAN);
                            break;
                    }
                    g.fillRect(200 + x * 60, 200 + y * 60, 50, 50);
                    g.setColor(Color.white);
                    g.drawString(Float.toString(map.getNode(y * map.rows + x).i_value), 200 + x*60, 200 + y*60 + 10);
                    g.drawString(Integer.toString(y * map.rows + x), 200 + x*60 + 20, 200 + y*60+30);
                }
            }

            gameWindow.render();
        }
    }

    public static void main(String[] args) {
        Map map = new Map(5, 5);
        map.generateMap(24);
        Pathfinder2 pf = new Pathfinder2() {
            @Override
            public void start() {
                findShortestPath(map, map.getNode(0));
            }
        };
        Game g = new Game(map, pf);
        g.start();
        pf.start();
    }
}

class Window extends JFrame implements KeyListener {
    private BufferStrategy bs;
    public int w, h;
    public boolean spaceDown = false;

    public Window(String title, int width, int height) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setSize(width, height);
        w = width;
        h = height;

        JPanel p = (JPanel) this.getContentPane();
        p.setSize(width, height);

        Canvas c = new Canvas();
        c.setBounds(0, 0, width, height);
        p.add(c);
        c.addKeyListener(this);

        c.createBufferStrategy(2);
        bs = c.getBufferStrategy();
        c.requestFocus();
    }

    public Graphics2D getDrawGraphics() {
        return (Graphics2D) bs.getDrawGraphics();
    }

    public void render() {
        bs.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            spaceDown = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            spaceDown = false;
        }
    }
}