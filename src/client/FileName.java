package client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class FileName {

	private JFrame frame;
	private JTextField textField;
	private String fileName;

	/**
	 * Create the application.
	 */
	public FileName(BufferedImage bImg) {
		initialize(bImg);
	}
	



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(BufferedImage bImg) {
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
				if (fileName.trim().length()== 0){
					JOptionPane.showMessageDialog(null, "Please enter the flie name!");
				}else {
					String fn = new String("./"+fileName +".png");
					try {
			            if (ImageIO.write(bImg, "png", new File(fn))){
			                System.out.println("Image saved");
			                JOptionPane.showMessageDialog(null, "The image is saved!");
			            }
				    } catch (IOException e) {
			            e.printStackTrace();
				    }
					
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
