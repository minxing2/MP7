
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

 
public class Tetris extends JFrame {
	
    public Tetris() {
        Tetrispiece a = new Tetrispiece();
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
        			int high = Tetrispiece.showHighestScore();
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
 

	class Tetrispiece extends JPanel implements KeyListener {
    public static int highestScore = 0;
    int x;
    int y;
    int a = 0;
    int b = 0;
    int flag = 0; 
    int state;
	int type;
    int point = 0;
    int[][] map = new int[13][23];
 
    
    public void boundary() {
    		for (a = 0; a < 12; a++) {
            map[a][21] = 2;
        }
        for (b = 0; b < 22; b++) {
            map[11][b] = 2;
            map[0][b] = 2;
        }
    }
 
    public void piece() {
        x = 4;
        y = 0;
        if (end(x, y) == 1) {
        		boundary();
     
        		JOptionPane.showMessageDialog(null, "YOU FAILED");
        }
        state = (int) (Math.random() * 4);
        type = (int) (Math.random() * 7);
    }
    
   
    Tetrispiece() {
    		Timer time = new Timer(700, new TimerListener());
        time.start();
    		piece();
        boundary();
        
    }
   
    public void moveL() {
        if (detect(x - 1, y, type, state) == 1) {
            x = x - 1;
        }
        
    }
 
 
    public void moveR() {
        if (detect(x + 1, y, type, state) == 1) {
            x = x + 1;
        }
        
       }
 
    public void moveD() {
        if (detect(x, y + 1, type, state) == 1) {
            y = y + 1;
            checkLine();
        }
        
        if (detect(x, y + 1, type, state) == 0) {
            add(x, y, type, state);
            piece();
            checkLine();
        }
        
       }
    
    public void rotate() {
        int a = state;
        state = (state + 1) % 4;
        if (detect(x, y, type, state) == 0) {
            state = a;
        }
        
    }
 
    public final int shape[][][] = new int[][][] {
    	   
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

                
 
   

    
    
    public int detect(int x, int y, int type, int state) {
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                if (((shape[type][state][a * 4 + b] == 1) && (map[x + b + 1]
                        [y + a] == 1))
                        || ((shape[type][state][a * 4 + b] == 1) && (map[x + b + 1]
                                [y + a] == 2))) {
 
                    return 0;
                }
            }
        }
        return 1;
    }
    
    public void checkLine() {
        int full = 0;
          for (int i = 0; i < 22; i++) {
              for (int j = 0; j < 12; j++) {
                  if (map[j][i] == 1) {
                      full++;
                      if (full == 10) {
                          point += 10;
                          remove(i);
                      }
                  }
              }
              full = 0;
          }

      }
      
      public void remove(int row) {
        for (int i = row; i > 0; i--) {
              for (int j = 0; j < 11; j++) {
                  map[j][i] = map[j][i - 1];

              }
          }
      }
 
  
    public int end(int x, int y) {
        if (detect(x, y, type, state) == 0) {
          if (point > highestScore) {
           highestScore = point;
          }
            return 1;
        }
        return 0;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        for (b = 0; b < 16; b++) {
            if (shape[type][state][b] == 1) {
            		g.setColor(Color.BLUE);
                g.fillRect((b % 4 + x + 1) * 10, (b / 4 + y) * 10, 10, 10);
            }
        }

        for (b = 0; b < 22; b++) {
            for (a = 0; a < 12; a++) {
                if (map[a][b] == 1) {
                	g.setColor(Color.BLUE);
                    g.fillRect(a * 10, b * 10, 10, 10);
 
                }
                if (map[a][b] == 2) {
                    g.drawRect(a * 10, b * 10, 10, 10);
 
                }
            }
        }
        
        
    }
 
    public void add(int a, int b, int type, int state) {
        int c = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (map[a + j + 1][b + i] == 0) {
                    map[a + j + 1][b + i] = shape[type][state][c];
                }
                c++;
            }
        }
    }
    
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_DOWN:
            moveD();
            break;
        case KeyEvent.VK_UP:
            rotate();
            break;
        case KeyEvent.VK_RIGHT:
            moveR();
            break;
        case KeyEvent.VK_LEFT:
            moveL();
            break;
        }
 
    }
 
    
    public class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        		repaint();
            if (detect(x, y + 1, type, state) == 1) {
                y += 1;
                checkLine();
            }
            if (detect(x, y + 1, type, state) == 0) {
 
                if (flag == 1) {
                    add(x, y, type, state);
                    checkLine();
                    piece();
                    flag = 0;
                }
               flag = 1;
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
    
    public static int showHighestScore() {
      return highestScore;
    }
    
}