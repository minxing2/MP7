
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;

 
public class Tetris extends JFrame {
    public Tetris() {
        Tetrisblok a = new Tetrisblok();
        addKeyListener(a);
        add(a);
    }
 
    public static void main(String[] args) {
        Tetris frame = new Tetris();
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        JMenu game = new JMenu("Game");
        JMenuItem exit = game.add("Exit");
        exit.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    System.exit(0);
   }
  });
        JMenu about = new JMenu("About");
        JMenu help = new JMenu("Help");
        JMenuItem gameplay = help.add("Gameplay");
        gameplay.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    JFrame alert = new JFrame("Gameplay");
    alert.setSize(200, 200);
    alert.setLayout(null);
    alert.setLocationRelativeTo(null);
    JLabel a = new JLabel("Up arrow for rotation");
    JLabel b = new JLabel("Down arrow for dropping");
    JLabel c = new JLabel("Left arrow for moving left");
    JLabel d = new JLabel("Right arrow for moving right");
    a.setBounds(20, 0, 200, 50);
    b.setBounds(20, 20, 200, 50);
    c.setBounds(15, 40, 200, 50);
    d.setBounds(10, 60, 200, 50);
    JButton okayButton = new JButton("Finish");
    okayButton.setBounds(50, 120, 100, 30);
    okayButton.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
      alert.dispose();
     }
    });
    alert.add(a);
    alert.add(b);
    alert.add(c);
    alert.add(d);
    alert.add(okayButton);
    alert.setResizable(false);
    alert.setVisible(true);
   }
  });
        JMenu score = new JMenu("Score");
  JMenuItem highScore = score.add("High Score");
  highScore.addActionListener(new ActionListener() {
   public void actionPerformed(ActionEvent e) {
    int high = Tetrisblok.showHighestScore();
    JFrame alert = new JFrame("High Score");
    alert.setSize(200, 200);
    alert.setLayout(null);
    alert.setLocationRelativeTo(null);
    JLabel score = new JLabel("The highest score is " + high);
    score.setBounds(20, 0, 200, 50);
    JButton okayButton = new JButton("Finish");
    okayButton.setBounds(50, 120, 100, 30);
    okayButton.addActionListener(new ActionListener() {
     public void actionPerformed(ActionEvent e) {
      alert.dispose();
     }
    });
    alert.add(score);
    alert.add(okayButton);
    alert.setResizable(false);
    alert.setVisible(true);
   }
  });

        menu.add(game);
        menu.add(help);
        menu.add(about);
        menu.add(score);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(220, 275);
        frame.setTitle("Tetris");
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
}
 

class Tetrisblok extends JPanel implements KeyListener {
 
 private int state;
 
    private int type;
    
    private int score = 0;
 
    public static int highestScore = 0;
 
    private int x;
 
    private int y;
 
    private int i = 0;
 
    int j = 0;
    
    int flag = 0; 
    
    int[][] map = new int[13][23];
 
    
    private final int shapes[][][] = new int[][][] {
   
            { { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },
                    { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 } },
           
            { { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 } },
            
            { { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 } },
           
            { { 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
        
            { { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            
            { { 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 } },
            
            { { 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0 } } };
 
    
    
 
    
    public void wall() {
        for (i = 0; i < 12; i++) {
            map[i][21] = 2;
        }
        
        for (j = 0; j < 22; j++) {
            map[11][j] = 2;
            map[0][j] = 2;
        }
    }
 
   
    public void map() {
        for (i = 0; i < 12; i++) {
            for (j = 0; j < 22; j++) {
                map[i][j] = 0;
            }
        }
    }
    
    public void block() {
        type = (int) (Math.random() * 1000) % 7;
        state = (int) (Math.random() * 1000) % 4;
        x = 4;
        y = 0;
        if (gameOver(x, y) == 1) {
 
            map();
            wall();
            score = 0;
            JOptionPane.showMessageDialog(null, "GAME OVER");
        }
    }
  
    Tetrisblok() {
        block();
        map();
        wall();
        Timer timer = new Timer(1000, new TimerListener());
        timer.start();
    }
 
   
    public void turn() {
        int tempstate = state;
        state = (state + 1) % 4;
        if (blow(x, y, type, state) == 1) {
        }
        if (blow(x, y, type, state) == 0) {
            state = tempstate;
        }
        repaint();
    }
 
   
    public void left() {
        if (blow(x - 1, y, type, state) == 1) {
            x = x - 1;
        }
        ;
        repaint();
    }
 
 
    public void right() {
        if (blow(x + 1, y, type, state) == 1) {
            x = x + 1;
        }
        ;
        repaint();
    }
 
 
    public void down() {
        if (blow(x, y + 1, type, state) == 1) {
            y = y + 1;
            delline();
        }
        ;
        if (blow(x, y + 1, type, state) == 0) {
            add(x, y, type, state);
            block();
            delline();
        }
        ;
        repaint();
    }
 
   
    public int blow(int x, int y, int type, int state) {
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (((shapes[type][state][a * 4 + b] == 1) && (map[x
                        + b + 1][y + a] == 1))
                        || ((shapes[type][state][a * 4 + b] == 1) && (map[x
                                + b + 1][y + a] == 2))) {
 
                    return 0;
                }
            }
        }
        return 1;
    }
 
   
    public void delline() {
        int c = 0;
        for (int b = 0; b < 22; b++) {
            for (int a = 0; a < 12; a++) {
                if (map[a][b] == 1) {
 
                    c = c + 1;
                    if (c == 10) {
                        score += 10;
                        for (int d = b; d > 0; d--) {
                            for (int e = 0; e < 11; e++) {
                                map[e][d] = map[e][d - 1];
 
                            }
                        }
                    }
                }
            }
            c = 0;
        }
    }
 
  
    public int gameOver(int x, int y) {
        if (blow(x, y, type, state) == 0) {
          if (score > highestScore) {
           highestScore = score;
           
          }
            return 1;
        }
        return 0;
    }
 

    public void add(int x, int y, int blockType, int turnState) {
        int j = 0;
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (map[x + b + 1][y + a] == 0) {
                    map[x + b + 1][y + a] = shapes[blockType][turnState][j];
                }
                ;
                j++;
            }
        }
    }
 
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        for (j = 0; j < 16; j++) {
            if (shapes[type][state][j] == 1) {
                g.fillRect((j % 4 + x + 1) * 10, (j / 4 + y) * 10, 10, 10);
            }
        }

        for (j = 0; j < 22; j++) {
            for (i = 0; i < 12; i++) {
                if (map[i][j] == 1) {
                    g.fillRect(i * 10, j * 10, 10, 10);
 
                }
                if (map[i][j] == 2) {
                    g.drawRect(i * 10, j * 10, 10, 10);
 
                }
            }
        }
        g.setColor(Color.BLACK);
  g.setFont(new Font("Calibri", Font.PLAIN, 15));
        g.drawString("score=" + score, 125, 50);
        g.drawString("CS125", 125, 70);
        g.drawString("MP7", 125, 90);
        g.drawString("Constructor:", 125, 110);
        g.drawString("Leyao Zhou", 125, 130);
        g.drawString("Minxing Sun", 125, 150);
        
        
    }
 
   
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN:
            down();
            break;
        case KeyEvent.VK_UP:
            turn();
            break;
        case KeyEvent.VK_RIGHT:
            right();
            break;
        case KeyEvent.VK_LEFT:
            left();
            break;
        }
 
    }
 
    public void keyReleased(KeyEvent e) {
    }
 
   
    public void keyTyped(KeyEvent e) {
    }
    
    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
 
            repaint();
            if (blow(x, y + 1, type, state) == 1) {
                y = y + 1;
                delline();
            }
            ;
            if (blow(x, y + 1, type, state) == 0) {
 
                if (flag == 1) {
                    add(x, y, type, state);
                    delline();
                    block();
                    flag = 0;
                }
                flag = 1;
            }
            ;
        }
    }
    
    public static int showHighestScore() {
      return highestScore;
    }
    
}