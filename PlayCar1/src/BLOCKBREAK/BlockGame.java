package BLOCKBREAK;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class BlockGame {
	public static boolean show = false, run = true, ON = false;

	static class MyFrame extends JFrame {
		
		//constant : 상수
		static int BALL_WIDTH = 20;  //공의 넓이
		static int BALL_HEIGHT = 20; // 공의 크기
		static int BLOCK_ROWS = 5; //블록이 놓일 줄
		static int BLOCK_COLUMNS = 10; // 블록이 놓일 칸
		static int BLOCK_WIDTH = 40; // 블록 크기
		static int BLOCK_HEIGHT = 20; // 블록 높이
		static int BLOCK_GAP = 3; // 블록 사이 간격
		static int BAR_WIDTH = 80; // 사용자가 움직이는 바의 크기
		static int BAR_HEIGHT = 20; // 바의 높이
		static int CANVAS_WIDTH = 400 + (BLOCK_GAP * BLOCK_COLUMNS) - BLOCK_GAP; //그려질 넓이(블록 갯수만큼 간격 수 추가)
		static int CANVAS_HEIGHT = 600; // 그려질 크기
		
		//variable : 변수
		static MyPanel myPanel = null;
		static int score = 0;  //점수
		static Timer timer = null; 
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS]; // 배열생성; 
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x; //Target Value = interpolation 한칸씩 쪼개서 움직임을 조절
		static int dir = 0; // 0 : Up-Right 1 : Down-Right, 2 : Up-Left, 3 : Down-Left 공의 움직임 상하좌우 대각선
		static int ballSpeed = 5;
		static boolean isGameFinish = false;
		
		static class Ball {
			int x = CANVAS_WIDTH/2 - BALL_WIDTH/2;  //공의 시작 위치 :  가로 중간, 반지름 정도 빼줘야 중앙됨
			int y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2; // "" : 세로 중간
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;
			
			Point getCenter() {
				return new Point( x + (BALL_WIDTH/2), y + (BALL_HEIGHT/2));
			}
			Point getBottomCenter() {
				return new Point( x + (BALL_WIDTH/2), y + (BALL_HEIGHT));
			}
			
			Point getTopCenter() {
				return new Point( x + (BALL_WIDTH/2), y);
			}
			
			Point getLeftCenter() {
				return new Point( x, y + (BALL_HEIGHT/2));
			}
			
			Point getRightCenter() {
				return new Point( x + (BALL_WIDTH), y + (BALL_HEIGHT/2));
			}
		}
		
		static class Bar {
			int x = CANVAS_WIDTH/2 - BALL_WIDTH/2;   
			int y = CANVAS_HEIGHT - 100; // 600-100
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}
		
		static class Block {
			// 블록은 나중에 반복문으로 생성
			int x = 0;    
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0:white, 1:yellow, 2:blue, 3:magenta, 4:red 10~50점 스코어 블록
			boolean isHidden = false; // 충돌 후 블록 삭제
		}
		
		static class MyPanel extends JPanel { //VANVAS for Draw!
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
				
			}
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g; //2d xy 패널에서 그래픽을 그리는 Graphics2D를 불러옴
				
				drawUI( g2d );
			}
			
			
			
			private void drawUI(Graphics2D g2d) {
				//draw Blocks
				for(int i=0; i < BLOCK_ROWS; i++) {
					for(int j=0; j < BLOCK_COLUMNS; j++) {
						if(blocks[i][j].isHidden) {   		// isHidden이기 때문에 continue로 루프 순환
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
					
					//draw score
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.drawString("score : " + score, CANVAS_WIDTH/2 -30, 20);
					if(isGameFinish) {
						g2d.setColor(Color.RED);
					}
					g2d.drawString("Game Finished!", CANVAS_WIDTH/2 -55, 50);
					
					//draw Ball
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);
					
					//draw Bar
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
		
					
				}	
			}
		}
		
		public MyFrame(String title) {
			super(title); 
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400, 300); // 윈도우 화면에서 400x300 정도에 창출력
			this.setLayout(new BorderLayout()); // 보더레이아웃
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 윈도우창 닫히게하는 옵션 
			
			initData();
			
			myPanel = new MyPanel(); //Canvas 역할
			this.add("Center", myPanel); //위치는 Center
			
			setKeyListener();
			startTimer();
		}
		
		
		public void initData() {
			for(int i=0; i < BLOCK_ROWS; i++) {
				for(int j=0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); // 실제로 공간을 생성해주는 코드
					blocks[i][j].x = BLOCK_WIDTH*j + BLOCK_GAP*j; // 벽돌마다 gap으로 구분(가로)
					blocks[i][j].y = 100 + BLOCK_HEIGHT*i + BLOCK_GAP*i; // 상단gap=100, 벽돌마다 gap으로 구분(세로)
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i; // 0:white, 1:yellow, 2:blue, 3:mazanta, 4:red 10~50점 스코어 블록
					blocks[i][j].isHidden = false; 
				}
			}
		}
		 
		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) { //Key Event
					if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						System.out.println("Pressed Left Key");
						barXTarget -= 25;
						if(bar.x < barXTarget) { //계속해서 키보드를 눌렀을 경우
							barXTarget = bar.x;
						}
					}
					else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						System.out.println("Pressed Right Key");
						barXTarget += 25;
						if(bar.x > barXTarget) { //계속해서 키보드를 눌렀을 경우
							barXTarget = bar.x;
					}
				}
			}
		});
		}
		
		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) { //Timer Event
					movement();
					checkCollision(); //벽과 충돌하면 움직임이 달라짐
					checkCollisionBlock();
					myPanel.repaint(); // 다시 그리기
					
					//라운드 성공
					isGamefinish();
				
				}
				
			});
			timer.start(); //Start Timer
			
		}
		public void isGamefinish() {
			// 게임 성공
			int count = 0;
			for(int i =0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if(block.isHidden) {
						count++;
					}
				}
				if (count == BLOCK_ROWS * BLOCK_COLUMNS) {
					// 게임 종료
					//timer.stop();
					isGameFinish = true;
				}
			}
		}
			
		public void movement() { //공의 움직임
			if(bar.x < barXTarget) {
				bar.x += 5;
			}else if(bar.x > barXTarget) {
				bar.x -= 5;
			}
			
			if(dir==0) { //0: Up-Right
				ball.x += ballSpeed;
				ball.y -= ballSpeed;
			}else if(dir==1) {//1 : Down-Right
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			}else if(dir==2 ) {//2 : Up-Left
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			}else if(dir==3 ) {//3 : Down-Left
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}
		}
		public boolean duplRect(Rectangle rect1, Rectangle rect2) {
			return rect1.intersects(rect2); // 두개의 사각형이 중복되는지 확인
		}
		public void checkCollision() {
			if(dir==0) { //0: Up-Right
				//벽에 부딪혔을 때
				if(ball.y < 0) { // 벽 상단
					dir = 1;
				}
				 if(ball.x > CANVAS_WIDTH - BALL_WIDTH) { //벽 우측
					dir = 2;
				}
				// 위로 올라가는 공은 Bar와 닿지 않으므로 Bar와와의 충돌처리는 없음
			}else if(dir==1) {//1 : 우측 하단
				//벽에 부딪혔을 때
				if(ball.y > CANVAS_HEIGHT-BALL_HEIGHT) { // 벽 하단
					dir = 1;
					
					//게임을 리셋함
					dir = 0;
					ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;  
					ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
					score = 0;
				}
				if(ball.x > CANVAS_WIDTH - BALL_WIDTH) { // 벽
					dir = 3;
				}
	//			 Bar
				if (ball.getBottomCenter().y >= bar.y) {
					if( duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), 
							    new Rectangle(bar.x, bar.y, bar.width, bar.height) ))  {
						dir = 0;
					}
				}
			}else if(dir==2 ) {//2 : 우측 상단
				//벽
				if( ball.y < 0 ) { // 벽 우측
					dir = 3;
				}
				if( ball.x < 0 ) { // 벽 좌측
					dir = 0;
				}
				//바
			}else if(dir==3 ) {//3 : 좌측 하단
				// 벽
				if( ball.y > CANVAS_HEIGHT-BALL_HEIGHT) { //벽 하단
					dir = 2;
					
					//게임을 리셋함
					dir = 0;
					ball.x = CANVAS_WIDTH/2 - BALL_WIDTH/2;  
					ball.y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2;
					score = 0;
			}
				if(ball.x < 0) { // 벽 좌측
					dir = 1;
				}
				// Bar-none
				if (ball.getBottomCenter().y >= bar.y) {
					if( duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), 
							    new Rectangle(bar.x, bar.y, bar.width, bar.height) ))  {
						dir = 2;
					}
				}
			}
		}
		public void checkCollisionBlock() {
			// 0:Up-Right, 1:Down-Right, 2:Up-Left, 3:Down-Left 공의 움직임 상하좌우 대각선
			for(int i =0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if(block.isHidden == false) {
						if(dir==0) { //0 : Up-Right
							if( duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
										 new Rectangle(block.x, block.y, block.width, block.height))) {
								if(ball.x > block.x + 2 && 
								   ball.getRightCenter().x <= block.x + block.width - 2) {
									//block bottom collision
									dir=1;
								}else {
									//block left collision
									dir=2;
								}
								block.isHidden = true;
								if(block.color==0) {
									score += 10;
								}else if(block.color==1) {
									score += 20;
								}else if(block.color==2) {
									score += 30;
								}else if(block.color==3) {
									score += 40;
								}else if(block.color==4) {
									score += 50;
								}
							}	
						}
						else if(dir==1) { //1 : Down=Right
							if( duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									 new Rectangle(block.x, block.y, block.width, block.height))) {
								if(ball.x > block.x + 2 && 
								   ball.getRightCenter().x <= block.x + block.width - 2) {
									//block top collision
									dir=0;
								}else {
									//block left collision
									dir=3;
								}
								block.isHidden = true;
								if(block.color==0) {
									score += 10;
								}else if(block.color==1) {
									score += 20;
								}else if(block.color==2) {
									score += 30;
								}else if(block.color==3) {
									score += 40;
								}else if(block.color==4) {
									score += 50;
								}
							}	
					
						}
						else if(dir==2) { //2 : Up-Left
							if( duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									 new Rectangle(block.x, block.y, block.width, block.height))) {
							if(ball.x > block.x + 2 && 
							   ball.getRightCenter().x <= block.x + block.width - 2) {
								//block bottom collision
								dir=3;
							}else {
								//block right collision
								dir=0;
							}
							block.isHidden = true;
						}
						}
						else if(dir==3) { //3 : Down-Left
							if( duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									 new Rectangle(block.x, block.y, block.width, block.height))) {
							if(ball.x > block.x + 2 && 
							   ball.getRightCenter().x <= block.x + block.width - 2) {
								//block top collision
								dir=2;
							}else {
								//block right collision
								dir=1;
							}
							block.isHidden = true;
							if(block.color==0) {
								score += 10;
							}else if(block.color==1) {
								score += 20;
							}else if(block.color==2) {
								score += 30;
							}else if(block.color==3) {
								score += 40;
							}else if(block.color==4) {
								score += 50;
							}
							}
						}
					}
				}
			}
			
		}
	}
	
	public static void main(String args[]) {
		
		MainFrame frm = new MainFrame();
		frm.createFrame();
	}
	
	

}

