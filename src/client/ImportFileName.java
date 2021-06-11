package client;



import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ImportFileName {

	private JFrame frame;
	private JTextField textField;
	private String fileName;
	private BufferedImage image;

	/**
	 * Create the application.
	 */
	public ImportFileName(Graphics g, Graphics2D g2, JPanel paintPanel) {
		initialize(g, g2, paintPanel);
	}
	



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Graphics g, Graphics2D g2, JPanel paintPanel) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 255);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("Please enter the file name: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileName = textField.getText();
				String fn = new String("./"+fileName +".png");
				if (fileName.trim().length()== 0){
					JOptionPane.showMessageDialog(null, "Please enter the flie name!");
				}else {
					try {                
						image = ImageIO.read(new File(fn));
			        } catch (IOException ex) {
			        	JOptionPane.showMessageDialog(null, "The file not exist, please try again!");
			        }
					g.drawImage(image,0,0,paintPanel);
					g2.drawImage(image,0,0,paintPanel);
					
					frame.dispose();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(25)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 379, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 264, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(171)
							.addComponent(btnNewButton)))
					.addContainerGap(32, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(43)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addComponent(btnNewButton)
					.addContainerGap(82, Short.MAX_VALUE))
		);
		frame.getContentPane().setLayout(groupLayout);
		frame.setVisible(true);
	}
}
