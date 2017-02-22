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
import persistentdatabase.model.Book;

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
//		createContentsPubmed();
//		createContentsNlmCatalog();
		createContentsOnlineNlmCatalog(); // בונה את החלון ומקשר לפונקציות על השרת
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
	protected void createContentsPubmed()  {
		shell = new Shell();
		shell.setSize(676, 458);
		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
		shell.setText("SWT Application");
		
		result = new  Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(132, 62, 329, 248);	
		result.setSize((int)(shell.getBounds().width / 2), (int)(shell.getBounds().height * 0.6));
		
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
					
					List<Article> art = client.testQuary4(text.getText());
					for (Article article: art)
						itemsList.add(article.getTitle());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Article article = art.get(selectedItems[0]);
//					      int[] selectedItems = itemsList.getSelectionIndices();
//					      String outString = "";
//					      for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
//					        outString += selectedItems[loopIndex] + " ";
					      result.setText(article.getAbstract());
					    }

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Article article = art.get(selectedItems[0]);
						    result.setText(article.getAbstract());
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
		text.setText("blood"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	}
	
	protected void createContentsNlmCatalog()  {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
		shell.setSize(676, 458);
		shell.setText("SWT Application");
		
		result = new  Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(132, 62, 329, 248);	
		result.setSize((int)(shell.getBounds().width / 2), (int)(shell.getBounds().height * 0.6));
		
		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		composite.setLocation(10, 62);
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
					
					List<Book> books = client.testQuary5(text.getText());
					for (Book book: books)
						itemsList.add(book.getTitle());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Book book = books.get(selectedItems[0]);
					      result.setText(book.toString());
					    }

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Book book = books.get(selectedItems[0]);
						      result.setText(book.toString());
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
		text.setText("blood"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	}
	
	protected void createContentsOnlineNlmCatalog()  {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
		shell.setSize(676, 458);
		shell.setText("SWT Application");
		
		result = new  Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(132, 62, 329, 248);	
		result.setSize((int)(shell.getBounds().width / 2), (int)(shell.getBounds().height * 0.6));
		
		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		composite.setLocation(10, 62);
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
				Client clientSearch = new Client();
				try {
					clientSearch.InitialConnection();
					result.setText(" ");
					
					List<Book> books = clientSearch.onlineSearch(text.getText());
					itemsList.removeAll(); // ניקוי היסטוריה
					for (Book str: books)
						itemsList.add(str.getTitle());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Book str = books.get(selectedItems[0]);
					      result.setText(str.toString());
					    } // לשנות לפניה לשרת לשלוף מאמר שלם

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Book str = books.get(selectedItems[0]);
						      result.setText(str.toString());
						    } // 
					});
					composite.setContent(itemsList);
						
						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					result.setText("server error:" + e1.toString());
					//e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(345, 24, 116, 32);
		btnNewButton.setText("searce");
		
		shell.setDefaultButton(btnNewButton);
		
		Button saveButton = new Button(shell, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client clientSearch = new Client();
				try {
					clientSearch.InitialConnection();
					result.setText(" ");
					
					List<Book> books = clientSearch.onlineSearch(text.getText());
					itemsList.removeAll(); // ניקוי היסטוריה
					for (Book str: books)
						itemsList.add(str.getTitle());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Book str = books.get(selectedItems[0]);
					      result.setText(str.toString());
					    } // לשנות לפניה לשרת לשלוף מאמר שלם

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Book str = books.get(selectedItems[0]);
						      result.setText(str.toString());
						    } // 
					});
					composite.setContent(itemsList);
						
						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					result.setText("server error:" + e1.toString());
					//e1.printStackTrace();
				}

			}
		});
		saveButton.setBounds(345, 24, 116, 32);
		saveButton.setText("save to local");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 24, 329, 32);
		text.setFocus();
		text.setText("blood"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	}
}
