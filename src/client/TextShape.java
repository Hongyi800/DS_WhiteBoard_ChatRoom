package client;



import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.awt.event.ActionEvent;
import javax.swing.LayoutStyle.ComponentPlacement;

public class TextShape {

	private JFrame frame;
	private JTextField txtText;
	private String myText;
	private String myX;
	private String myY;
	private JLabel lblPleaseEnterThe;
	private JTextField txtX;
	private JTextField txtY;

	/**
	 * Create the application.
	 */
	public TextShape(Graphics g, Graphics2D g2, OutputStream output) {
		initialize(g, g2, output);
	}
	



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Graphics g, Graphics2D g2, OutputStream output) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 398);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Please enter the text you want to insert: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		txtText = new JTextField();
		txtText.setColumns(10);
		
		txtX = new JTextField();
		txtX.setColumns(10);
		
		txtY = new JTextField();
		txtY.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myText = txtText.getText();
				myX = txtX.getText();
				myY = txtY.getText();
				
				
				if(myX.trim().length()== 0 || myY.trim().length()== 0) {
					JOptionPane.showMessageDialog(null, "Please enter the X and Y position!");
				}else if (myText.trim().length()== 0){
					JOptionPane.showMessageDialog(null, "Please enter the text!");
				}else {
					
					int x = Integer.parseInt(myX);
					int y = Integer.parseInt(myY);
					g.drawString(myText,x,y); 
					g2.drawString(myText,x,y); 
					
					try {
						output.write("text".getBytes());
						
						int textLen= myText.getBytes().length;
						output.write(getByte(textLen));
						
						output.write(myText.getBytes());
						
						output.write(getByte(x));
						output.write(getByte(y));
						
						frame.dispose();
					} catch (IOException e) {

						e.printStackTrace();
					}
				}
				
				
				
				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		lblPleaseEnterThe = new JLabel("Please enter the position you want to insert: ");
		lblPleaseEnterThe.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		
		JLabel lblXPosition = new JLabel("X Position:");
		lblXPosition.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JLabel lblYPosition = new JLabel("Y Position:");
		lblYPosition.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblYPosition, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtY, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblXPosition, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtX, GroupLayout.PREFERRED_SIZE, 273, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 340, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPleaseEnterThe, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(txtText, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(168)
							.addComponent(btnNewButton)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(43)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txtText, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblPleaseEnterThe, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblXPosition, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtX, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblYPosition, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtY, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnNewButton)
					.addGap(11))
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
	
	public byte[] getByte(int number){
		byte[]bt=new byte[4];
		bt[0]=(byte) ((number>>0) & 0xff);
		bt[1]=(byte) ((number>>8) & 0xff);
		bt[2]=(byte) ((number>>16) & 0xff);
		bt[3]=(byte) ((number>>24) & 0xff);
		return bt;
	}
}
