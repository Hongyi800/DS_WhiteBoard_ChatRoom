package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class test {

	private JFrame frame;
	private Socket socket = null;
	private JPanel paintPanel;
	private String curShape = "pen";
	private int[] mousePos = new int[4];
	private InputStream input;
	private OutputStream output;
	public Graphics g;
	private JTextField txtSend;


	public test(Socket socket) {
		initialize();
		this.socket = socket;
	}
	
	public void start(Socket socket) throws IOException {
		try {
			test window = new test(socket);
			
			input=socket.getInputStream();
			output=socket.getOutputStream();
			
			Receive rec=new Receive();
			rec.start();
			
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,873);
		frame.setResizable(false);
		
		JPanel paintPanel = new JPanel();
		paintPanel.setBackground(Color.WHITE);
		
		JPanel toolPanel = new JPanel();
		toolPanel.setBorder(new TitledBorder(null, "Tool", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton penBtn = new JButton("Pen");
		penBtn.setBackground(Color.ORANGE);
		penBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton lineBtn = new JButton("Line");
		lineBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton circleBtn = new JButton("Circle");
		circleBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton rectBtn = new JButton("Rectangle");
		rectBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton ovalBtn = new JButton("Oval");
		ovalBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout gl_toolPanel = new GroupLayout(toolPanel);
		gl_toolPanel.setHorizontalGroup(
			gl_toolPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_toolPanel.createSequentialGroup()
					.addGap(19)
					.addComponent(penBtn, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(10)
					.addComponent(lineBtn, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(14)
					.addComponent(circleBtn, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ovalBtn, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rectBtn)
					.addContainerGap(19, Short.MAX_VALUE))
		);
		gl_toolPanel.setVerticalGroup(
			gl_toolPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_toolPanel.createSequentialGroup()
					.addGroup(gl_toolPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(penBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(lineBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(circleBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(ovalBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(rectBtn, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		toolPanel.setLayout(gl_toolPanel);
		
		JPanel colorPanel = new JPanel();
		colorPanel.setBorder(new TitledBorder(null, "Color", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblNewLabel = new JLabel("Client List");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JTextArea textAreaClient = new JTextArea();
		
		JLabel lblChattingRome = new JLabel("Chatting Rome");
		lblChattingRome.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		txtSend = new JTextField();
		txtSend.setColumns(10);
		
		JButton sendButton = new JButton("send");
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JButton exportButton = new JButton("export");
		exportButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton cleanButton = new JButton("clean");
		cleanButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton importButton = new JButton("import");
		importButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton textButton = new JButton("text");
		textButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(451)
									.addComponent(colorPanel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE))
								.addComponent(toolPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(49)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(paintPanel, GroupLayout.PREFERRED_SIZE, 790, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 786, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblChattingRome, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
							.addGap(22)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textButton, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
								.addComponent(textAreaClient, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
								.addComponent(exportButton, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
								.addComponent(importButton, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(txtSend, GroupLayout.PREFERRED_SIZE, 692, GroupLayout.PREFERRED_SIZE)
							.addGap(21)
							.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
							.addGap(22)
							.addComponent(cleanButton, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(colorPanel, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addComponent(toolPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(21)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
					.addGap(7)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(paintPanel, GroupLayout.PREFERRED_SIZE, 504, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(42)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblChattingRome, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textAreaClient, GroupLayout.PREFERRED_SIZE, 541, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(textButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addGap(15)
							.addComponent(exportButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(importButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
					.addGap(13)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(2)
							.addComponent(txtSend, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
						.addComponent(sendButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addComponent(cleanButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)))
		);
		
		JTextArea textAreaChat = new JTextArea();
		scrollPane.setViewportView(textAreaChat);
		frame.getContentPane().setLayout(groupLayout);
		
		Color myPurple = new Color(153, 51, 255);
		Color myRed = new Color(255, 204, 204);
		Color myYellow = new Color(255, 255, 204);
		Color myGreen = new Color(204, 255, 153);
		Color myBlue = new Color(204, 255, 255);
		Color[]colors={
				Color.black,Color.white, Color.gray,Color.red,
				Color.orange,Color.yellow,Color.blue,Color.cyan,
				Color.green, Color.pink,myPurple,Color.magenta,
				myRed, myYellow, myGreen, myBlue
				};
		String[]colorButtonNames={
				"black","white","gray","red",
				"orange","yellow","blue","cyan",
				"green","pink","purple","magenta",
				"myRed", "myYellow", "myGreen", "myBlue"
				};
		JButton[]CjbtList=new JButton[colorButtonNames.length];
		for(int i=0;i<colorButtonNames.length;i++){
			CjbtList[i]=new JButton();
			CjbtList[i].setActionCommand(colorButtonNames[i]);
			CjbtList[i].setBackground(colors[i]);
			colorPanel.add(CjbtList[i]);
		}
		
		Listener paintListener=new Listener();
		
		paintPanel.addMouseListener(paintListener);
		paintPanel.addMouseMotionListener(paintListener);
		
		for(int i=0;i<colorButtonNames.length;i++){
			CjbtList[i].addActionListener(paintListener);
		}
		penBtn.addActionListener(paintListener);
		lineBtn.addActionListener(paintListener);
		circleBtn.addActionListener(paintListener);
		rectBtn.addActionListener(paintListener);
		ovalBtn.addActionListener(paintListener);
		
		Graphics g = paintPanel.getGraphics();
		Graphics2D g2d=(Graphics2D) g;
		g2d.setStroke(new BasicStroke(3.0f));
	}
	
	class Listener implements ActionListener, MouseListener, MouseMotionListener{
		private JTextArea jta2;
		private JTextArea jta1;
		
		public Listener() {
			
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			if(curShape.equals("pen")){
				mousePos[2] = mousePos[0];
				mousePos[3] = mousePos[1];
				
				mousePos[0]=arg0.getX();
				mousePos[1]=arg0.getY();
				
				g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
				
				sendPaintBoard();
			}
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			mousePos[0]=arg0.getX();
			mousePos[1]=arg0.getY();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			mousePos[2]=arg0.getX();
			mousePos[3]=arg0.getY();
			
			switch(curShape) {
			case "pen" :
				break;
			case "line" :
				System.out.println("draw line");
				g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
				sendPaintBoard();
				break;
			case "circle" :
				System.out.println("draw circle");
				int width = Math.abs(mousePos[0] - mousePos[2]);
				int height = Math.abs(mousePos[1] - mousePos[3]);
				int diameter = Math.max(width, height);
				g.fillOval(mousePos[2],mousePos[3], diameter, diameter);
				sendPaintBoard();
				break;
			case "oval" :
				System.out.println("draw oval");
				int width2 = Math.abs(mousePos[0] - mousePos[2]);
				int height2 = Math.abs(mousePos[1] - mousePos[3]);
				g.fillOval(mousePos[2], mousePos[3], width2, height2);
				sendPaintBoard();
				break;
			case "rectangle" :
				System.out.println("draw rectangle");
				int width3 = Math.abs(mousePos[0] - mousePos[2]);
				int height3 = Math.abs(mousePos[1] - mousePos[3]);
				int x1 = Math.min(mousePos[0], mousePos[2]);
				int y1 = Math.min(mousePos[1], mousePos[3]);
				g.fillRect(x1, y1, width3, height3);
				sendPaintBoard();
				break;
			}
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String msg = arg0.getActionCommand();
			switch(msg) {
			case "pen" :
				curShape = "pen";
				break;
			case "line" :
				curShape = "line";
				break;
			case "circle" :
				curShape = "circle";
				break;
			case "oval" :
				curShape = "oval";
				break;
			case "rectangle" :
				curShape = "rectangle";
				break;
			case "clean" :
				paintPanel.paint(g);
				requireCleanPaintBoard("clean");
				break;
			}
			
		}
		
		public void requireCleanPaintBoard(String OP) {
			try {
				output.write(OP.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void sendPaintBoard() {
			try {
				switch(curShape){
					case"line":
						System.out.println("line");
						output.write("line".getBytes());
						break;
					case"circle":
						System.out.println("circle");
						output.write("circle".getBytes());
						break;
					case"oval":
						System.out.println("oval");
						output.write("oval".getBytes());
						break;
					case"rectangle":
						System.out.println("rectangle");
						output.write("rectangle".getBytes());
						break;
					case"pen":
						System.out.println("发送铅笔");
						output.write("发送铅笔".getBytes());
						break;
				}
				
				//发送2个点（4个坐标）
				for(int i=0;i<4;i++){
					byte[]bt=getByte(mousePos[i]);
					output.write(bt);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public byte[] getByte(int number){
		byte[]bt=new byte[4];
		bt[0]=(byte) ((number>>0) & 0xff);
		bt[1]=(byte) ((number>>8) & 0xff);
		bt[2]=(byte) ((number>>16) & 0xff);
		bt[3]=(byte) ((number>>24) & 0xff);
		return bt;
	}
	
	public int getInt(byte[] bt){
		int number = (bt[3]& 0xff)<<24|
				     (bt[2]& 0xff)<<16|
				     (bt[1]& 0xff)<<8 |
				     (bt[0]& 0xff)<<0;
		return number;
	}
	
	class Receive extends Thread{
		
		public void run(){
			while(true){
				try {
					byte[]OP=new byte[8];
					input.read(OP);
					
					switch(new String(OP)){
					case "pen" :
						upDatePaintBoard("pen");
						break;
					case "line" :
						upDatePaintBoard("line");
						break;
					case "circle" :
						upDatePaintBoard("circle");
						break;
					case "oval" :
						upDatePaintBoard("oval");
						break;
					case "rectangle" :
						upDatePaintBoard("rectangle");
						break;
					case "clean" :
						cleanPaintBoard("clean");
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void cleanPaintBoard(String OP) {
			switch(OP){
			case"clean":paintPanel.paint(g);break;
			}
		}
		
		private void upDatePaintBoard(String OP) {
			try {
				for(int i=0;i<4;i++){
					byte[]bt=new byte[4];
					input.read(bt);
					mousePos[i]=getInt(bt);
				}
				switch(OP){
				case"line":
					g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"circle":
					g.fillOval(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"oval":
					g.fillOval(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"rectangle":
					g.fillRect(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"pen":
					g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
