package client;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

public class WhiteBoard {
	private InputStream input;
	private OutputStream output;
	private JPanel paintPanel;
	private JTextArea textAreaChat;
	private JTextArea textAreaClient;
	private BufferedImage bImg;
	private boolean ifClose = false;
	private boolean ifSocketRun = true;

	private String curShape = "pen";//表示目前所选的绘制功能按钮（默认为绘制直线）
	private int[] mousePos = new int[4];
	public Graphics g;
	public Graphics2D g2;
	private Socket socket = null;
	private String name;
	
	
	public WhiteBoard(Socket socket, String name) {
		this.socket = socket;
		this.name = name;

	}

	@SuppressWarnings("deprecation")
	void start(Socket socket) throws IOException {
		input=socket.getInputStream();
		output=socket.getOutputStream();

		Receive rec=new Receive();
		rec.start();
		
		showFrame();
		
		if(ifClose) {
			rec.stop();
		}

	}
	
	private void showFrame() {
		JFrame frame=new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000,900);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		paintPanel = new JPanel();
		paintPanel.setBackground(Color.WHITE);
		paintPanel.setBounds(10, 81, 790, 681);
		
		JPanel toolPanel = new JPanel();
		toolPanel.setBorder(new TitledBorder(null, "Tool", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		toolPanel.setBounds(10, 10, 455, 64);
		
		
		JButton penBtn = new JButton("pen");
		penBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton lineBtn = new JButton("line");
		lineBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton circleBtn = new JButton("circle");
		circleBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton ovalBtn = new JButton("oval");
		ovalBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton rectBtn = new JButton("rectangle");
		rectBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		
		
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
		colorPanel.setBounds(461, 10, 340, 64);
		
		
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
				"green","pink","myPurple","magenta",
				"myRed", "myYellow", "myGreen", "myBlue"
				};
		JButton[]CjbtList=new JButton[colorButtonNames.length];
		for(int i=0;i<colorButtonNames.length;i++){
			CjbtList[i]=new JButton();
			CjbtList[i].setActionCommand(colorButtonNames[i]);
			CjbtList[i].setBackground(colors[i]);
			colorPanel.add(CjbtList[i]);
		}
		
		
		JLabel lblNewLabel = new JLabel("Client List");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		textAreaClient = new JTextArea();
		textAreaClient.setEditable(false);
		
		JLabel lblChattingRome = new JLabel("Chatting Rome");
		lblChattingRome.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JTextField txtSend = new JTextField();
		txtSend.setColumns(10);
		
		JButton sendButton = new JButton("send");
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		
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
		
		JButton textButton = new JButton("text");
		textButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
		
		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		textAreaChat.setLineWrap(true);
		textAreaChat.setWrapStyleWord(true);
		
		scrollPane.setViewportView(textAreaChat);
		frame.getContentPane().setLayout(groupLayout);
		
		frame.add(paintPanel);
		frame.add(toolPanel);
		frame.add(colorPanel);

		//添加监听器
		Listener paintListener = new Listener(txtSend,textAreaChat);
		WinListener winListener = new WinListener();
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
		
		sendButton.addActionListener(paintListener);
		cleanButton.addActionListener(paintListener);
		exportButton.addActionListener(paintListener);
		importButton.addActionListener(paintListener);
		textButton.addActionListener(paintListener);
		
		frame.addWindowListener(winListener);
		
		frame.setVisible(true);
		
		g = paintPanel.getGraphics();
		
		bImg = new BufferedImage(paintPanel.getWidth(), paintPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		g2 = bImg.createGraphics();
		g2.setColor(Color.white);
	    g2.fillRect(0, 0, paintPanel.getWidth(), paintPanel.getHeight());
	    g2.setColor(Color.black);

		Graphics2D g2d=(Graphics2D) g;
		g2d.setStroke(new BasicStroke(3.0f));
	}
	
	
	class WinListener implements WindowListener{

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("the window closed");
			try {
				output.write("exit".getBytes());
				int nameLen= name.getBytes().length;
				
				output.write(getByte(nameLen));
				output.write(name.getBytes());
				ifClose = true;
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	class Listener implements ActionListener, MouseListener, MouseMotionListener{
		private JTextField txtSend;
		private JTextArea textAreaChat;
		
		public Listener(JTextField txtSend, JTextArea textAreaChat) {
			this.txtSend = txtSend;
			this.textAreaChat = textAreaChat;
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			if(curShape.equals("pen")){
				mousePos[2] = mousePos[0];
				mousePos[3] = mousePos[1];
				
				mousePos[0]=arg0.getX();
				mousePos[1]=arg0.getY();
				
				g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
				g2.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
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
				g2.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
				sendPaintBoard();
				break;
			case "circle" :
				System.out.println("draw circle");
				int x1 = Math.min(mousePos[0], mousePos[2]);
				int y1 = Math.min(mousePos[1], mousePos[3]);
				int width = Math.abs(mousePos[0] - mousePos[2]);
				int height = Math.abs(mousePos[1] - mousePos[3]);
				int diameter = Math.max(width, height);
				g.drawOval(x1, y1, diameter, diameter);
				g2.drawOval(x1, y1, diameter, diameter);
				mousePos[0] = x1;
				mousePos[1] = y1;
				mousePos[2] = diameter;
				mousePos[3] = diameter;
				
				sendPaintBoard();
				break;
			case "oval" :
				System.out.println("draw oval");
				int x2 = Math.min(mousePos[0], mousePos[2]);
				int y2 = Math.min(mousePos[1], mousePos[3]);
				int width2 = Math.abs(mousePos[0] - mousePos[2]);
				int height2 = Math.abs(mousePos[1] - mousePos[3]);
				g.drawOval(x2, y2, width2, height2);
				g2.drawOval(x2, y2, width2, height2);
				mousePos[0] = x2;
				mousePos[1] = y2;
				mousePos[2] = width2;
				mousePos[3] = height2;
				sendPaintBoard();
				break;
			case "rectangle" :
				System.out.println("draw rectangle");
				int width3 = Math.abs(mousePos[0] - mousePos[2]);
				int height3 = Math.abs(mousePos[1] - mousePos[3]);
				int x3 = Math.min(mousePos[0], mousePos[2]);
				int y3 = Math.min(mousePos[1], mousePos[3]);
				g.drawRect(x3, y3, width3, height3);
				g2.drawRect(x3, y3, width3, height3);
				mousePos[0] = x3;
				mousePos[1] = y3;
				mousePos[2] = width3;
				mousePos[3] = height3;
				sendPaintBoard();
				break;
			}
			
		}
		

		@Override
		public void actionPerformed(ActionEvent arg0) {
			Color myPurple = new Color(153, 51, 255);
			Color myRed = new Color(255, 204, 204);
			Color myYellow = new Color(255, 255, 204);
			Color myGreen = new Color(204, 255, 153);
			Color myBlue = new Color(204, 255, 255);
			String msg = arg0.getActionCommand();
			switch(msg) {
			// Draw
			case "pen" :
				curShape = "pen";
				System.out.println("change to pen");
				break;
			case "line" :
				curShape = "line";
				System.out.println("change to line");
				break;
			case "circle" :
				curShape = "circle";
				System.out.println("change to circle");
				break;
			case "oval" :
				curShape = "oval";
				System.out.println("change to oval");
				break;
			case "rectangle" :
				curShape = "rectangle";
				System.out.println("change to rectangle");
				break;
			// other command
			case "clean" :
				paintPanel.paint(g);
				paintPanel.paint(g2);
				requireCleanPaintBoard("clea");
				break;
				
			case "export" :
				export("expo");
				break;
				
			case "import" :
				importImg();
				break;
			case "text" :
				insertTxt();
				break;
			// change color
			case "black" :
				System.out.println("change to black");
				g.setColor(Color.black);
				g2.setColor(Color.black);
				sendColor(Color.black.hashCode());
				break;
				
			case "white" :
				System.out.println("change to white");
				g.setColor(Color.white);
				g2.setColor(Color.white);
				sendColor(Color.white.hashCode());
				break;
				
			case "gray" :
				System.out.println("change to gray");
				g.setColor(Color.gray);
				g2.setColor(Color.gray);
				sendColor(Color.gray.hashCode());
				break;
			
			case "red" :
				System.out.println("change to red");
				g.setColor(Color.red);
				g2.setColor(Color.red);
				sendColor(Color.red.hashCode());
				break;
				
			case "orange" :
				System.out.println("change to orange");
				g.setColor(Color.orange);
				g2.setColor(Color.orange);
				sendColor(Color.orange.hashCode());
				break;
				
			case "yellow" :
				System.out.println("change to yellow");
				g.setColor(Color.yellow);
				g2.setColor(Color.yellow);
				sendColor(Color.yellow.hashCode());
				break;
				
			case "blue" :
				System.out.println("change to blue");
				g.setColor(Color.blue);
				g2.setColor(Color.blue);
				sendColor(Color.blue.hashCode());
				break;
			
			case "cyan" :
				System.out.println("change to cyan");
				g.setColor(Color.cyan);
				g2.setColor(Color.cyan);
				sendColor(Color.cyan.hashCode());
				break;
				
			case "green" :
				System.out.println("change to green");
				g.setColor(Color.green);
				g2.setColor(Color.green);
				sendColor(Color.green.hashCode());
				break;
				
			case "pink" :
				System.out.println("change to pink");
				g.setColor(Color.pink);
				g2.setColor(Color.pink);
				sendColor(Color.pink.hashCode());
				break;
				
			case "myPurple" :
				System.out.println("change to myPurple");
				g.setColor(myPurple);
				g2.setColor(myPurple);
				sendColor(myPurple.hashCode());
				break;
				
			case "magenta" :
				System.out.println("change to magenta");
				g.setColor(Color.magenta);
				g2.setColor(Color.magenta);
				sendColor(Color.magenta.hashCode());
				break;
			
			case "myRed" :
				System.out.println("change to myRed");
				g.setColor(myRed);
				g2.setColor(myRed);
				sendColor(myRed.hashCode());
				break;
			case "myYellow" :
				System.out.println("change to myYellow");
				g.setColor(myYellow);
				g2.setColor(myYellow);
				sendColor(myYellow.hashCode());
				break;
			case "myGreen" :
				System.out.println("change to myGreen");
				g.setColor(myGreen);
				g2.setColor(myGreen);
				sendColor(myGreen.hashCode());
				break;
			case "myBlue" :
				System.out.println("change to myBlue");
				g.setColor(myBlue);
				g2.setColor(myBlue);
				sendColor(myBlue.hashCode());
				break;
			// send message
			case "send" :
				sendMsg();
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
						output.write("line".getBytes());
						break;
					case"circle":
						output.write("circ".getBytes());
						break;
					case"oval":
						output.write("oval".getBytes());
						break;
					case"rectangle":
						output.write("rect".getBytes());
						break;
					case"pen":
						output.write("penc".getBytes());
						break;
				}
				
				
				for(int i = 0 ; i < 4; i++){
					byte[]bt=getByte(mousePos[i]);
					output.write(bt);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void sendColor(int colorHashCode) {
			try {
				String OP="colo";
				output.write(OP.getBytes());

				output.write(getByte(colorHashCode));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		public void sendMsg(){
			try {
				String OP="chat";
				output.write(OP.getBytes());
				
				int nameLen= name.getBytes().length;
				output.write(getByte(nameLen));
				
				output.write(name.getBytes());

				int msgLen=txtSend.getText().getBytes().length;
				output.write(getByte(msgLen));

				output.write(txtSend.getText().getBytes());
				
				System.out.println(name+ "send："+txtSend.getText());
				

				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
				Date date = new Date(System.currentTimeMillis());

				textAreaChat.append("\n\r"+"From me	"+formatter.format(date)+"\n\r"+txtSend.getText());
	
				textAreaChat.setCaretPosition(textAreaChat.getText().length());

				txtSend.setText("");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		private void export(String OP) {
			FileName fn = new FileName(bImg);
		}

		private void importImg() {
			ImportFileName fn = new ImportFileName(g, g2, paintPanel);
		}
		
		private void insertTxt() {
			TextShape fn = new TextShape(g, g2, output);
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
			
			while(ifSocketRun){
				try {
					byte[]OP = new byte[4];
					input.read(OP);
					String msgR = new String(OP);
					System.out.println(msgR);
					switch(msgR){
					case "penc" :
						System.out.println("Server said print draw");
						upDatePaintBoard("pen");
						break;
					case "line" :
						System.out.println("Server said print line");
						upDatePaintBoard("line");
						break;
					case "circ" :
						System.out.println("Server said print circle");
						upDatePaintBoard("circle");
						break;
					case "oval" :
						System.out.println("Server said print oval");
						upDatePaintBoard("oval");
						break;
					case "rect" :
						System.out.println("Server said print rectangle");
						upDatePaintBoard("rectangle");
						break;
					case "clea" :
						cleanPaintBoard("clean");
						break;
					case "colo" :
						updateColor();
						break;
					case "chat" :
						receiveMsg();
						break;
						
					case "join" :
						System.out.println("Server said someone come");
						updateClientJoin();
						break;
					case "exit" :
						socket.close();
						ifSocketRun = false;
						break;
					case "sbex" :
						System.out.println("Server said someone leave");
						updateClientExit();
						break;
						
					case "text" :
						System.out.println("Server said someone add text");
						insertText("text");

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void cleanPaintBoard(String OP) {
			paintPanel.paint(g);
			paintPanel.paint(g2);
		}
		
		private void insertText(String OP) {
			try {
				byte[] msgLen = new byte[4];
				input.read(msgLen);
				int msgLenInt = getInt(msgLen);
				
				byte[] message = new byte[msgLenInt];
				input.read(message);
				String msg = new String(message);
				
				byte[] xLen = new byte[4];
				input.read(xLen);
				int x = getInt(xLen);
				
				byte[] yLen = new byte[4];
				input.read(yLen);
				int y = getInt(yLen);
				
				
				g.drawString(msg,x,y); 
				g2.drawString(msg,x,y); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void upDatePaintBoard(String OP) {
			try {

				for(int i=0;i<4;i++){
					byte[]bt = new byte[4];
					input.read(bt);
					mousePos[i] = getInt(bt);
				}

				switch(OP){
				case"line":
					g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					g2.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"circle":
					g.drawOval(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					g2.drawOval(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"oval":
					g.drawOval(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					g2.drawOval(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"rectangle":
					g.drawRect(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					g2.drawRect(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				case"pen":
					g.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					g2.drawLine(mousePos[0],mousePos[1],mousePos[2],mousePos[3]);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void updateColor() {
			try {
				byte[]color = new byte[4];
				input.read(color);
				g.setColor(new Color(getInt(color)));
				g2.setColor(new Color(getInt(color)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void receiveMsg() {
			try {
				byte[] namelen = new byte[4];
				input.read(namelen);
				int nameLenInt = getInt(namelen);
				
				byte[] name = new byte[nameLenInt];
				input.read(name);
				String clientName = new String(name);
				
				byte[] msgLen = new byte[4];
				input.read(msgLen);
				int msgLenInt = getInt(msgLen);
				
				byte[] message = new byte[msgLenInt];
				input.read(message);
				String msg = new String(message);
				
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
				Date date = new Date(System.currentTimeMillis());
				
				textAreaChat.append("\n\r"+"From "+clientName+ "  " + formatter.format(date)+"\n\r"+ msg);
				textAreaChat.setCaretPosition(textAreaChat.getText().length());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void updateClientJoin() {
			try {
				textAreaClient.setText("");
				byte[] num = new byte[4];
				input.read(num);
				int numInt = getInt(num);
				
				byte[] namelen = new byte[4];
				input.read(namelen);
				int nameLenInt = getInt(namelen);
				
				byte[] name = new byte[nameLenInt];
				input.read(name);
				String nameString = new String(name);
				String[] totalName = nameString.split(",");
				
				for(String clientName: totalName) {
					textAreaClient.append(clientName + "\n");
				}
				
				byte[] joinNamelen = new byte[4];
				input.read(joinNamelen);
				int joinNamelenInt = getInt(joinNamelen);
				
				byte[] joinName = new byte[joinNamelenInt];
				input.read(joinName);
				String joinNameString = new String(joinName);
				
				textAreaChat.append("\n\r"+"Client "+joinNameString+ " join the room" + "\n\r");
				textAreaChat.setCaretPosition(textAreaChat.getText().length());
				
				String message = new String(joinNameString+ " join the room");
				JOptionPane.showMessageDialog(null, message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void updateClientExit() {
			try {
				textAreaClient.setText("");
				byte[] num = new byte[4];
				input.read(num);
				int numInt = getInt(num);
				
				byte[] namelen = new byte[4];
				input.read(namelen);
				int nameLenInt = getInt(namelen);
				
				byte[] name = new byte[nameLenInt];
				input.read(name);
				String nameString = new String(name);
				String[] totalName = nameString.split(",");
				
				for(String clientName: totalName) {
					textAreaClient.append(clientName + "\n");
				}
				
				byte[] exitNamelen = new byte[4];
				input.read(exitNamelen);
				int exitNamelenInt = getInt(exitNamelen);
				
				byte[] exitName = new byte[exitNamelenInt];
				input.read(exitName);
				String exitNameString = new String(exitName);
				
				textAreaChat.append("\n\r"+"Client "+exitNameString+ " leave the room" + "\n\r");
				textAreaChat.setCaretPosition(textAreaChat.getText().length());
				
				String message = new String(exitNameString+ " leave the room");
				JOptionPane.showMessageDialog(null, message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}