package Admin;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.mongodb.Mongo;

import persistentdatabase.main.PersistSettings;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;
import java.awt.event.ActionEvent;

public class AdminSettings extends JFrame {

	private JPanel contentPane;
	private JTextField IpTxt;
	//private static Shell parent;
	AdminClient client = null;
	String MongoUri = "";
	String Ip = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//parent = shell;
					AdminSettings frame = new AdminSettings();
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
	public AdminSettings() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblServerIpSettings = new JLabel("Server IP Settings:");
		lblServerIpSettings.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 18));
		lblServerIpSettings.setBounds(10, 29, 137, 25);
		contentPane.add(lblServerIpSettings);
		
		JLabel lblMongodbUrlConnection = new JLabel("MongoDB URL connection:");
		lblMongodbUrlConnection.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 18));
		lblMongodbUrlConnection.setBounds(10, 89, 220, 25);
		contentPane.add(lblMongodbUrlConnection);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 125, 414, 63);
		contentPane.add(scrollPane);
		
		JTextArea mongoTxt = new JTextArea();
		scrollPane.setViewportView(mongoTxt);
		mongoTxt.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		mongoTxt.setLineWrap(true);
		// נבדוק אם קיימת הגדרת בשרת
		try {
			client = new AdminClient();
			client.InitialConnection();
			MongoUri = client.getMongoDbConnection();
			mongoTxt.setText(MongoUri);
			if (MongoUri.substring(0, 4).equals("error")){ // אם חזרה שגיאה לא ממלאים את השדה
				mongoTxt.setText("");
				MongoUri = "";
			}
		}
		catch (Exception ex) {
			mongoTxt.setText("");
			MongoUri = "";
		}
		
		IpTxt = new JTextField();
		IpTxt.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 16));
		IpTxt.setBounds(158, 24, 156, 36);
		Ip = new PersistSettings().getProp("ServerIp");
		IpTxt.setText(Ip);
		if (Ip.substring(0, 4).equals("error")){ // אם חזרה שגיאה לא ממלאים את השדה
			IpTxt.setText("");
			Ip = "";
		}
		contentPane.add(IpTxt);
		IpTxt.setColumns(10);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try	{
					if (!Ip.equals(IpTxt.getText())) {
						// Save IP of server
						PersistSettings ps = new PersistSettings();
						String answer = ps.saveProp("ServerIp", IpTxt.getText());
						if (answer.equals("")){
							dispose(); // סוגר את החלון
						}
						else {
							throw new Exception(answer);
						}
					}
					
					if (!MongoUri.equals(mongoTxt.getText())) {
						// Save Mongo URI in server
						client = new AdminClient();
						client.InitialConnection();
						client.updateMongoDbConnection(mongoTxt.getText());
					}
				}
				catch (Exception e){
					JOptionPane.showMessageDialog(null, "Error when tring to save settings !!/n" + e.toString());
					dispose(); // סוגר את החלון
				}
				
			}
		});
		btnSave.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 18));
		btnSave.setBounds(335, 225, 89, 25);
		contentPane.add(btnSave);
	}
}
