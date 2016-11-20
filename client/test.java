package client;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

//import com.sun.java.util.jar.pack.Attribute.Layout;

import persistentdatabase.model.Article;

import org.eclipse.swt.widgets.Composite;

public class test {

	protected Shell shell;
	private Text text;
	private Text result;
	static Composite composite;

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
		shell.setSize(676, 458);
		shell.setText("SWT Application");
		
		result = new  Text(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(189, 80, 401, 312);
		
		composite = new Composite(shell, SWT.NONE);
		composite.setBounds(60, 80, 116, 312);
		GridLayout gl = new GridLayout();
		 gl.numColumns = 1;
		 composite.setLayout(gl);
				
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client client = new Client();
				try {
					result.setText(" ");
					//String tmp = client.testQuary3(text.getText());
					
					
					List<String> art = client.testQuary6(text.getText());
						composite.dispose();
						composite = new Composite(shell, SWT.NONE);
						composite.setBounds(60, 80, 116, 312);
						composite.setLayout(gl);
						for (String article: art){
						 Button btn = new Button(composite, SWT.NONE);
						 btn.setText(article);	
						 btn.setToolTipText(article);
						 btn.addSelectionListener(new SelectionAdapter() {
							 @Override
								public void widgetSelected(SelectionEvent e) {
								 Client client = new Client();
								 try {
									 result.setText(" ");
									 result.setText(client.testQuary7(article));
								 } 
								 catch (Exception e1) {
										// TODO Auto-generated catch block
										result.setText("server error ");
										//e1.printStackTrace();
									}
								 
							 }
						});
					}
						
						composite.layout(); 
						
						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					result.setText("server error ");
					//e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(474, 18, 116, 32);
		btnNewButton.setText("searce");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(50, 24, 401, 32);
		
		
		
		
		
		

	}
}
