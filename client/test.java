package client;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

//import com.sun.java.util.jar.pack.Attribute.Layout;

import persistentdatabase.model.Article;
import persistentdatabase.model.Book;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

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
//		searchOnlineDatabases();
		searchOnlineDatabasesTabs();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	protected void searchOnlineDatabasesTabs()  {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
		shell.setSize(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height - 45);
		shell.setLocation(0,0);
		shell.setText("Biology Databases");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(10, 24, 329, 32);
		text.setFocus();
		text.setText("blood");
		
		TabFolder tFol = new TabFolder(shell, SWT.BORDER);
		tFol.setBounds(text.getBounds().x,
				text.getBounds().y + text.getBounds().height + 15,
				(int)(shell.getBounds().width * 0.95),
				(int)(shell.getBounds().height * 0.8));
		TabItem tab = new TabItem(tFol,SWT.BORDER);
		tab.setText("Pubmed");		
		Composite pubmedTabComposite = new Composite(tFol,SWT.BORDER);
		pubmedTabComposite.setLayout(new FillLayout());
		//new Text(pubmedTabComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		tab.setControl(pubmedTabComposite);
		
		composite = new ScrolledComposite(pubmedTabComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		composite.setLocation(10, 10);
		composite.setSize(pubmedTabComposite.getBounds().width / 6,
					pubmedTabComposite.getBounds().height - 20);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		
		result = new Text(pubmedTabComposite, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(
				composite.getBounds().x + composite.getBounds().width + 15,
				composite.getBounds().y,
				(int)(pubmedTabComposite.getBounds().width / 6 * 4.8),
				composite.getBounds().height  );
		result.setVisible(false); // !!!!!!!!!!!!!!!!!!!
		
		Browser browser = new Browser(pubmedTabComposite,SWT.NONE);
		browser.setBounds(
				composite.getBounds().x + composite.getBounds().width + 15,
				composite.getBounds().y,
				(int)(pubmedTabComposite.getBounds().width / 6 * 4.8),
				composite.getBounds().height  );
		browser.setVisible(true); // !!!!!!!!!!!!!!!!!
		browser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=5U68");
		
		org.eclipse.swt.widgets.List itemsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		tab.setControl(pubmedTabComposite);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client clientSearch = new Client();
				try {
					clientSearch.InitialConnection();
					result.setText(" ");
					
					List<Structure> books = clientSearch.onlineSearch(text.getText());
					itemsList.removeAll(); // ניקוי היסטוריה
					for (Structure str: books)
						itemsList.add(str.toString());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Structure str = books.get(selectedItems[0]);
					    result.setText(str.toString());
					} // לשנות לפניה לשרת לשלוף מאמר שלם

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Structure str = books.get(selectedItems[0]);
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
		btnNewButton.setBounds(467, 24, 116, 32);
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
					
					List<Structure> books = clientSearch.onlineSearch(text.getText());
					itemsList.removeAll(); // ניקוי היסטוריה
					for (Structure str: books)
						itemsList.add(str.getName());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Structure str = books.get(selectedItems[0]);
					      result.setText(str.toString());
					    } // לשנות לפניה לשרת לשלוף מאמר שלם

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Structure str = books.get(selectedItems[0]);
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
		
//		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
//		tabFolder.setBounds(10, 74, 573, 341);
//		
//		TabItem tbtmPubmed = new TabItem(tabFolder, SWT.NONE);
//		tbtmPubmed.setText("Pubmed");
//		
//		TabItem tbtmGene = new TabItem(tabFolder, SWT.NONE);
//		tbtmGene.setText("Gene");
	}
	
	/**
	 * Create contents of the window.
	 */
//	protected void createContentsPubmed()  {
//		shell = new Shell();
//		shell.setSize(676, 458);
//		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
//		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
//		shell.setText("SWT Application");
//		
//		result = new  Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
//		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
//		result.setBounds(132, 62, 329, 248);	
//		result.setSize((int)(shell.getBounds().width / 2), (int)(shell.getBounds().height * 0.6));
//		
//		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		composite.setLocation(10, 62);
//		//composite.setSize(shell.getSize().x - shell.getClientArea().width,shell.getSize().y - shell.getClientArea().height);
//		composite.setSize(shell.getBounds().width / 6, (int)(shell.getBounds().height * 0.6));
//		composite.setExpandHorizontal(true);
//		composite.setExpandVertical(true);
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 1;
//		
//		org.eclipse.swt.widgets.List itemsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
//		
//		Button btnNewButton = new Button(shell, SWT.NONE);
//		btnNewButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Client client = new Client();
//				try {
//					result.setText(" ");
//					
//					List<Article> art = client.testQuary4(text.getText());
//					for (Article article: art)
//						itemsList.add(article.getTitle());
//						
//					itemsList.addSelectionListener(new SelectionListener() {
//					public void widgetSelected(SelectionEvent event) {
//						int[] selectedItems = itemsList.getSelectionIndices();
//						Article article = art.get(selectedItems[0]);
////					      int[] selectedItems = itemsList.getSelectionIndices();
////					      String outString = "";
////					      for (int loopIndex = 0; loopIndex < selectedItems.length; loopIndex++)
////					        outString += selectedItems[loopIndex] + " ";
//					      result.setText(article.getAbstract());
//					    }
//
//					    public void widgetDefaultSelected(SelectionEvent event) {
//							int[] selectedItems = itemsList.getSelectionIndices();
//							Article article = art.get(selectedItems[0]);
//						    result.setText(article.getAbstract());
//					    }
//					});
//					composite.setContent(itemsList);
//						
//						
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					result.setText("server error");
//					//e1.printStackTrace();
//				}
//
//			}
//		});
//		btnNewButton.setBounds(345, 24, 116, 32);
//		btnNewButton.setText("searce");
//		
//		shell.setDefaultButton(btnNewButton);
//		
//		text = new Text(shell, SWT.BORDER);
//		text.setBounds(10, 24, 329, 32);
//		text.setFocus();
//		text.setText("blood"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
//	}
	
//	protected void createContentsNlmCatalog()  {
//		shell = new Shell();
//		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
//		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
//		shell.setSize(676, 458);
//		shell.setText("SWT Application");
//		
//		result = new  Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
//		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
//		result.setBounds(132, 62, 329, 248);	
//		result.setSize((int)(shell.getBounds().width / 2), (int)(shell.getBounds().height * 0.6));
//		
//		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		composite.setLocation(10, 62);
//		composite.setSize(shell.getBounds().width / 6, (int)(shell.getBounds().height * 0.6));
//		composite.setExpandHorizontal(true);
//		composite.setExpandVertical(true);
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 1;
//		
//		org.eclipse.swt.widgets.List itemsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
//		
//		Button btnNewButton = new Button(shell, SWT.NONE);
//		btnNewButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Client client = new Client();
//				try {
//					result.setText(" ");
//					
//					List<Book> books = client.testQuary5(text.getText());
//					for (Book book: books)
//						itemsList.add(book.getTitle());
//						
//					itemsList.addSelectionListener(new SelectionListener() {
//					public void widgetSelected(SelectionEvent event) {
//						int[] selectedItems = itemsList.getSelectionIndices();
//						Book book = books.get(selectedItems[0]);
//					      result.setText(book.toString());
//					    }
//
//					    public void widgetDefaultSelected(SelectionEvent event) {
//							int[] selectedItems = itemsList.getSelectionIndices();
//							Book book = books.get(selectedItems[0]);
//						      result.setText(book.toString());
//						    }
//					});
//					composite.setContent(itemsList);
//						
//						
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					result.setText("server error");
//					//e1.printStackTrace();
//				}
//
//			}
//		});
//		btnNewButton.setBounds(345, 24, 116, 32);
//		btnNewButton.setText("searce");
//		
//		shell.setDefaultButton(btnNewButton);
//		
//		text = new Text(shell, SWT.BORDER);
//		text.setBounds(10, 24, 329, 32);
//		text.setFocus();
//		text.setText("blood"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
//	}
	
	protected void searchOnlineDatabases()  {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
		shell.setSize(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height - 45);
		shell.setLocation(0,0);
		shell.setText("Biology Databases");
		
		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		composite.setLocation(10, 70);
		composite.setSize(shell.getBounds().width / 6, (int)(shell.getBounds().height * 0.8));
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		
		result = new Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		result.setBounds(
				composite.getBounds().x + composite.getBounds().width + 15,
				composite.getBounds().y,
				(int)(Display.getCurrent().getBounds().width),
				composite.getBounds().height  );
		result.setVisible(false); // !!!!!!!!!!!!!!!!!!!
		
		Browser browser = new Browser(shell,SWT.NONE);
		//browser.setBounds(132,62,451,274);
		browser.setBounds(
				composite.getBounds().x + composite.getBounds().width + 15,
				composite.getBounds().y,
				(int)(Display.getCurrent().getBounds().width / 1.5),
				composite.getBounds().height  );
		browser.setVisible(true); // !!!!!!!!!!!!!!!!!
		browser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=5U68");
		
		org.eclipse.swt.widgets.List itemsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client clientSearch = new Client();
				try {
					clientSearch.InitialConnection();
					result.setText(" ");
					
					List<Structure> books = clientSearch.onlineSearch(text.getText());
					itemsList.removeAll(); // ניקוי היסטוריה
					for (Structure str: books)
						itemsList.add(str.toString());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Structure str = books.get(selectedItems[0]);
					    result.setText(str.toString());
					} // לשנות לפניה לשרת לשלוף מאמר שלם

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Structure str = books.get(selectedItems[0]);
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
		btnNewButton.setBounds(467, 24, 116, 32);
		btnNewButton.setText("searce");
		
		shell.setDefaultButton(btnNewButton);		
		
//		shell.addControlListener(new ControlAdapter() {			
//			@Override
//			public void controlResized(ControlEvent e) {
//				// TODO Auto-generated method stub
//				//shell.setBounds(shell.getClientArea());
//			}
//		});
		
		Button saveButton = new Button(shell, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Client clientSearch = new Client();
				try {
					clientSearch.InitialConnection();
					result.setText(" ");
					
					List<Structure> books = clientSearch.onlineSearch(text.getText());
					itemsList.removeAll(); // ניקוי היסטוריה
					for (Structure str: books)
						itemsList.add(str.getName());
						
					itemsList.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent event) {
						int[] selectedItems = itemsList.getSelectionIndices();
						Structure str = books.get(selectedItems[0]);
					      result.setText(str.toString());
					    } // לשנות לפניה לשרת לשלוף מאמר שלם

					    public void widgetDefaultSelected(SelectionEvent event) {
							int[] selectedItems = itemsList.getSelectionIndices();
							Structure str = books.get(selectedItems[0]);
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
		text.setText("blood");
	}
}
