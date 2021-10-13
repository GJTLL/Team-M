import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Screen extends Canvas implements KeyListener {

	private int x = 0;
	private int y = 0;
	private int z = 50;
	
	private BufferedImage image;
	private static final long serialVersionUID = 1L;

		public Screen() {
		try {
			image = ImageIO.read(new File("src/car.png"));
			addKeyListener(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.drawImage(image, x, y, this);
		g.drawImage(image, x, z, this);
		}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int gap = 10;
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			y -= gap;
			z -= gap;
			break;
		case KeyEvent.VK_DOWN:
			y += gap;
			z += gap;
			break;
		case KeyEvent.VK_LEFT:
			x -= gap;
			break;
		case KeyEvent.VK_RIGHT:
			x += gap;
			break;
		}
		System.out.println(x+", "+y);
		System.out.println(x+", "+z);
		repaint();
	}

}
// github ¿Ã¿Á¡¯