package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.SystemColor;

public class ServerGui extends JFrame {

	private JPanel contentPane;
	private final JLabel lblNisayon = new JLabel("<html>Server BIOPROG is running...<br>close this window to stop server</html>");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGui frame = new ServerGui();
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
	public ServerGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 120);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblNisayon.setFont(new Font("Tempus Sans ITC", Font.BOLD, 18));
		lblNisayon.setBounds(10, 11, 290, 59);
		contentPane.add(lblNisayon);
	}
}
