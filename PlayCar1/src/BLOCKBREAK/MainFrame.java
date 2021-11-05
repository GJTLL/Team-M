package BLOCKBREAK;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;



public class MainFrame extends BlockGame {
	private JFrame frame;
	
	Button button1 = new Button("게임시작");
	Button button2 = new Button("게임종료");
	

	FlowLayout flow = new FlowLayout(); // Layout Manager
	
	
	public void createFrame()
	{
		frame = new JFrame();
		//프레임 크기 지정
		frame.setSize(600, 900);
		frame.setLocationRelativeTo(null);
		
		
        //프레임에 컴포넌트 추가
		frame.add(button1);
		frame.add(button2);
		
		
		
		
        // 레이아웃 세팅
		//이미지가 안넣어짐
		//frame.setContentPane(new JLabel(new ImageIcon("C:\\Users\\user\\workspace\\BLOCKBREAK\\src\01.jpg")));
		frame.setLayout(flow);
		button1.setPreferredSize(new Dimension(200, 50));
		button2.setPreferredSize(new Dimension(200, 50));
		
		
        //프레임 보이기
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MyFrame("Block Game");
				frame.setVisible(false);
			}
		});
		
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
}
