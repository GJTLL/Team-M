package BLOCKBREAK;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BlockGame {
	
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
		static Timer time = null; 
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS]; // 배열생성; 
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x; //Target Value = interpolation 한칸씩 쪼개서 움직임을 조절
		static int dir = 0; // 0 : Up-Right 1 : Down-Right, 2 : Up-Left, 3 : Down-Left 공의 움직임 상하좌우 대각선
		static int ballSpeed = 5;
		
		static class Ball {
			int x = CANVAS_WIDTH/2 - BALL_WIDTH/2;  //공의 시작 위치 :  가로 중간, 반지름 정도 빼줘야 중앙됨
			int y = CANVAS_HEIGHT/2 - BALL_HEIGHT/2; // "" : 세로 중간
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
			
		}
		
		public void startTimer() {
			
		}
	}
	
	public static void main(String args[]) {
		new MyFrame("Block Game");
	}

}

