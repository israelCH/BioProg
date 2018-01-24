package user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import persistentdatabase.main.PersistSettings;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

public class UserSettings extends JFrame {

	private JPanel contentPane;
	private JTextField IpTxt;
	String Ip = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserSettings frame = new UserSettings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UserSettings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 336, 158);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblServerIpSettings = new JLabel("Server IP Settings:");
		lblServerIpSettings.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 18));
		lblServerIpSettings.setBounds(10, 35, 137, 25);
		contentPane.add(lblServerIpSettings);
		
		IpTxt = new JTextField();
		IpTxt.setText("127.0.0.1");
		IpTxt.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 16));
		IpTxt.setColumns(10);
		IpTxt.setBounds(158, 30, 156, 36);
		Ip = new PersistSettings("User").getProp("ServerIp");
		IpTxt.setText(Ip);
		if (Ip.substring(0, 5).equals("error")){ // אם חזרה שגיאה לא ממלאים את השדה
			IpTxt.setText("");
			Ip = "";
		}
		contentPane.add(IpTxt);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PersistSettings ps;
				try	{
					if (!Ip.equals(IpTxt.getText())) {
						// Save IP of server
						ps = new PersistSettings("User");
						String answer = ps.saveProp("ServerIp", IpTxt.getText());
						if (!answer.equals("")){
							throw new Exception(answer);
						}
					}
					dispose(); // סוגר את החלון
				}
				catch (Exception e){
					JOptionPane.showMessageDialog(null, "Error when tring to save settings !!/n" + e.toString());
					dispose(); // סוגר את החלון
				}
				
			}
		});
		btnSave.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 18));
		btnSave.setBounds(221, 83, 89, 25);
		contentPane.add(btnSave);
	}
}
