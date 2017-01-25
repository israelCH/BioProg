	package client;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;

//import com.sun.java.util.jar.pack.Attribute.Layout;

import persistentdatabase.model.Article;

import org.eclipse.swt.widgets.Composite;

public class test {

	protected Shell shell;
	private Text text;
	private Text result;
	private ScrolledComposite composite;

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
		result.setBounds(132, 62, 329, 248);		
		
		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		composite.setLocation(10, 62);
		//composite.setSize(shell.getSize().x - shell.getClientArea().width,shell.getSize().y - shell.getClientArea().height);
		composite.setSize(shell.getBounds().width / 6, (int)(shell.getBounds().height * 0.6));
		composite.setExpandHorizontal(true);
		composite.setExpandVertical(true);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		
		org.eclipse.swt.widgets.List itemsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client client = new Client();
				try {
					result.setText(" ");
					//String tmp = client.testQuary3(text.getText());
					/*if (tmp =="")
						result.setText("No result");
						
						else
						result.setText(tmp);*/
					// hey how are you
					
					List<Article> art = client.testQuary4(text.getText());
						for (Article article: art){
						 //Button btn = new Button(composite, SWT.NONE);
						// btn.setText(article.getId());	
							itemsList.add(article.getTitle());
					}	
//					itemsList.addListener(SWT.Selection, new Listener() {
//					    public void handleEvent(Event e) {
//					        String string = "";
//					        int[] selection = itemsList.getSelectionIndices();
//					        for (int i = 0; i < selection.length; i++) {
//					          string += selection[i] + " ";
//					        }
//					        System.out.println("Selection={" + string + "}");
//					    }
//					});
					    itemsList.addSelectionListener(new SelectionListener() {
					    public void widgetSelected(SelectionEvent event) {
					      int[] selectedItems = itemsList.getSelectionIndices();
					      String outString = "";
					      for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
					        outString += selectedItems[loopIndex] + " ";
					      result.setText("Selected Items: " + outString);
					    }

					    public void widgetDefaultSelected(SelectionEvent event) {
					      int[] selectedItems = itemsList.getSelectionIndices();
					      String outString = "";
					      for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
					        outString += selectedItems[loopIndex] + " ";
					      //System.out.println("Selected Items: " + outString);
					      result.setText("Selected Items: " + outString);
					    }
					});
					composite.setContent(itemsList);
						
						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					result.setText("server error");
					//e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(345, 24, 116, 32);
		btnNewButton.setText("searce");
		
		shell.setDefaultButton(btnNewButton);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 24, 329, 32);
		text.setFocus();
	}
}
