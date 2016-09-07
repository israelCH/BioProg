package client;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class test {

	protected Shell shell;
	private Text text;
	private Text result;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			test window = new test();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()  {
		shell = new Shell();
		shell.setSize(531, 305);
		shell.setText("SWT Application");
		
		result = new  Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(60, 62, 401, 166);
		
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client client = new Client();
				try {
					result.setText(" ");
					String tmp = client.testQuary2(text.getText());
					if (tmp =="")
						result.setText("No result");
						
						else
						result.setText(tmp);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					result.setText("server error ");
					//e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(332, 24, 116, 32);
		btnNewButton.setText("searce");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(50, 24, 276, 32);
		
		
		
		

	}
}
