package client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WhiteBoardClient {

	private JFrame frame;
	private JTextField txtIP;
	private JTextField txtPort;
	
	private Socket socket = null;
	private JTextField textClientName;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WhiteBoardClient window = new WhiteBoardClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WhiteBoardClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 488, 360);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHongyisDictionary = new JLabel("Hongyi's Paint Board");
		lblHongyisDictionary.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblHongyisDictionary.setBounds(101, 22, 364, 31);
		frame.getContentPane().add(lblHongyisDictionary);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 63, 426, 255);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Inter Client Infomation:");
		lblNewLabel.setBounds(10, 10, 150, 17);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblNewLabel_1 = new JLabel("IP:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(28, 49, 62, 35);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Port:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(28, 106, 62, 35);
		panel.add(lblNewLabel_1_1);
		
		txtIP = new JTextField();
		txtIP.setColumns(10);
		txtIP.setBounds(98, 55, 277, 31);
		panel.add(txtIP);
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(98, 112, 277, 31);
		panel.add(txtPort);
		
		textClientName = new JTextField();
		textClientName.setColumns(10);
		textClientName.setBounds(98, 164, 277, 31);
		panel.add(textClientName);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sendStrIP = txtIP.getText();
				String sendStrPort = txtPort.getText();
				String sendName = textClientName.getText();
				
				int curPort=Integer.parseInt(sendStrPort);  
				
				if(sendStrIP.trim().length()== 0 || sendStrPort.trim().length()== 0) {
					JOptionPane.showMessageDialog(null, "Please enter server port and ip!");
				}else if (sendName.trim().length()== 0){
					JOptionPane.showMessageDialog(null, "Please enter your name!");
				}else {
					try{
						socket = new Socket(sendStrIP, curPort);
//						socket = new Socket("localhost", 3002);

						WhiteBoard dc = new WhiteBoard(socket, sendName);
						dc.start(socket);
						
						OutputStream output = socket.getOutputStream();
						
						output.write("join".getBytes());
						int nameLen= sendName.getBytes().length;
						
						output.write(getByte(nameLen));
						output.write(sendName.getBytes());
						
						frame.dispose();
						
					}catch (UnknownHostException e)
					{
						JOptionPane.showMessageDialog(null, "The input ip is unknow !");
						e.printStackTrace();
					}
					catch (IOException e) 
					{
						JOptionPane.showMessageDialog(null, "Server is closed on input port !");
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
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setBounds(157, 205, 111, 35);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Name:");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1_1.setBounds(28, 164, 62, 35);
		panel.add(lblNewLabel_1_1_1);
		
		
	}
}
