import javax.swing.JPanel;
import java.awt.*;
/**
 * Simple way of displaying trending hashtags
 * 
 * @author Nick Elsasser
 * @version 1
 */
public class Drawer extends JPanel
{
    final int WIDTH = 400;
    final int HEIGHT = 400;
    
    
    private static String[] top10 = new String[10];
    private static int[] top10count = new int[10];
    
    Graphics2D g2d;
    
    public Drawer()
    {
    }
    
    public void drawTextBox(Graphics g, String text, int position) {
        int x = 10;
        int y = position * (HEIGHT / 20) + 100;
        g.drawString(text, x, y);
    }
    
    public void drawText(Graphics g) {
        for(int i = 0; i < 10; i++) {
            if(top10[i] == null) {
                drawTextBox(g, "N/A : " + i, i + 1);
            } else {
                drawTextBox(g, top10[i] + " : " + top10count[i], i);
            }
        }
    }
    
    public void update(Object[] tops) {
        top10 = (String[])tops[0];
        top10count = (int[])tops[1];
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawText(g);
    }
}
