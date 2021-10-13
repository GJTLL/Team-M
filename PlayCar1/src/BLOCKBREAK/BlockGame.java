package BLOCKBREAK;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGame {
	
	static class MyFrame extends JFrame {
		
		//constant : ���
		static int BALL_WIDTH = 20;  //���� ����
		static int BALL_HEIGHT = 20; // ���� ũ��
		static int BLOCK_ROWS = 5; //������ ���� ��
		static int BLOCK_COLUMNS = 10; // ������ ���� ĭ
		static int BLOCK_WIDTH = 40; // ���� ũ��
		static int BLOCK_HEIGHT = 20; // ���� ����
		static int BLOCK_GAP = 3; // ���� ���� ����
		static int BAR_WIDTH = 80; // ����ڰ� �����̴� ���� ũ��
		static int BAR_HEIGHT = 20; // ���� ����
		static int CANVAS_WIDTH = 400 + (BLOCK_GAP * BLOCK_COLUMNS) - BLOCK_GAP; //�׷��� ����(���� ������ŭ ���� �� �߰�)
		static int CANVAS_HEIGHT = 600; // �׷��� ũ��
		
		//variable : ����
		static MyPanel myPanel = null;
		static int score = 0;  //����
		static Timer time = null; 
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS]; // �迭����; 
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x; //Target Value = interpolation ��ĭ�� �ɰ��� �������� ����
		static int dir = 0; // 0 : Up-Right 1 : Down-Right, 2 : Up-Left, 3 : Down-Left ���� ������ �����¿� �밢��
		static int ballSpeed = 5;
		
		static class Ball {
			int x = CANVAS_WIDTH/2 - BALL_WIDTH/2;  //���� ���� ��ġ :  ���� �߰�, ������ ���� ����� �߾ӵ�
			int y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2; // "" : ���� �߰�
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;
		}
		
		static class Bar {
			int x = CANVAS_WIDTH/2 - BALL_WIDTH/2;   
			int y = CANVAS_HEIGHT - 100; // 600-100
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}
		
		static class Block {
			// ������ ���߿� �ݺ������� ����
			int x = 0;    
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0:white, 1:yellow, 2:blue, 3:magenta, 4:red 10~50�� ���ھ� ����
			boolean isHidden = false; // �浹 �� ���� ����
		}
		
		static class MyPanel extends JPanel { //VANVAS for Draw!
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
				
			}
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g; //2d xy �гο��� �׷����� �׸��� Graphics2D�� �ҷ���
				
				drawUI( g2d );
			}
			
			private void drawUI(Graphics2D g2d) {
				//draw Blocks
				for(int i=0; i < BLOCK_ROWS; i++) {
					for(int j=0; j < BLOCK_COLUMNS; j++) {
						if(blocks[i][j].isHidden) {   		// isHidden�̱� ������ continue�� ���� ��ȯ
							continue;
						}
						if(blocks[i][j].color==0) {
							g2d.setColor(Color.WHITE);
						}
						else if(blocks[i][j].color==1) {
							g2d.setColor(Color.YELLOW);
						}
						else if(blocks[i][j].color==2) {
							g2d.setColor(Color.BLUE);
						}
						else if(blocks[i][j].color==3) {
							g2d.setColor(Color.MAGENTA);
						}
						else if(blocks[i][j].color==4) {
							g2d.setColor(Color.RED);
						}
						
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);
					}
				}	
			}
		}
		
		public MyFrame(String title) {
			super(title); 
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400, 300); // ������ ȭ�鿡�� 400x300 ������ â���
			this.setLayout(new BorderLayout()); // �������̾ƿ�
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������â �������ϴ� �ɼ� 
			
			initData();
			
			myPanel = new MyPanel(); //Canvas ����
			this.add("Center", myPanel); //��ġ�� Center
			
			setKeyListener();
			startTimer();
		}
		
		
		public void initData() {
			for(int i=0; i < BLOCK_ROWS; i++) {
				for(int j=0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); // ������ ������ �������ִ� �ڵ�
					blocks[i][j].x = BLOCK_WIDTH*j + BLOCK_GAP*j; // �������� gap���� ����(����)
					blocks[i][j].y = 100 + BLOCK_HEIGHT*i + BLOCK_GAP*i; // ���gap=100, �������� gap���� ����(����)
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i; // 0:white, 1:yellow, 2:blue, 3:mazanta, 4:red 10~50�� ���ھ� ����
					blocks[i][j].isHidden = false; 
				}
			}
		}
		
		public void setKeyListener() {
			
		}
		
		public void startTimer() {
			
		}
	}
	
	public static void main(String args[]) {
		new MyFrame("Block Game");
	}

}
