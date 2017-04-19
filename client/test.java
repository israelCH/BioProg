package client;

import java.util.List;

//import javax.swing.JOptionPane;
//import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.events.ControlAdapter;
//import org.eclipse.swt.events.ControlEvent;
//import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import database.DataBase.DBType;

//import com.sun.java.util.jar.pack.Attribute.Layout;

import persistentdatabase.model.Article;
//import persistentdatabase.model.Book;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;

public class test {

	protected Shell shell;
	private Text text;
	
	private TabItem pubmedTab;
	private ScrolledComposite pubmedResultsList;
	private Text pubmedFullData;
	
	private TabItem proteinTab;
	private ScrolledComposite proteinResultsList;
	private Text proteinFullData;
	
	private TabItem geneTab;
	private ScrolledComposite geneResultsList;
	private Text geneFullData;
	
	private TabItem structureTab;
	private ScrolledComposite structureResultsList;
	private Browser structureBrowser;
	
	Client client = null;

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
		shell = new Shell(); // מבנה כל החלון
		shell.setImage(SWTResourceManager.getImage(test.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(test.class, "/Images/search_background.jpg"));
		shell.setSize(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height - 45);
		shell.setLocation(0,0);
		shell.setText("Biology Databases");
		
		text = new Text(shell, SWT.BORDER); // תיבת החיפוש
		text.setBounds(10, 24, 329, 32);
		text.setFocus();
		text.setText("blood"); // ?????????????????????????
		
		// בנית הטאבים
		TabFolder tFol = new TabFolder(shell, SWT.BORDER);
		tFol.setBounds(text.getBounds().x,
				text.getBounds().y + text.getBounds().height + 15,
				(int)(shell.getBounds().width * 0.95),
				(int)(shell.getBounds().height * 0.8));
		
		//---------------------------------------------------------------
		
		structureTab = new TabItem(tFol,SWT.BORDER); // יצירת טאב ראשון
		structureTab.setText("Structure");		
		
		Composite StructureTabComposite = new Composite(tFol,SWT.BORDER); // בניית תוכן הטאב
		StructureTabComposite.setLayout(new FillLayout());
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;

		structureResultsList = new ScrolledComposite(StructureTabComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		structureResultsList.setLocation(10, 10);
//		structureResultsList.setSize(StructureTabComposite.getBounds().width / 6,
//				StructureTabComposite.getBounds().height - 20);
		structureResultsList.setSize(150,250);
		
		structureBrowser = new Browser(StructureTabComposite,SWT.NONE);
		structureBrowser.setBounds(
				structureResultsList.getBounds().x + structureResultsList.getBounds().width + 15,
				structureResultsList.getBounds().y,
				(int)(StructureTabComposite.getBounds().width / 6 * 4.8),
				structureResultsList.getBounds().height  );
		//structureBrowser.setUrl("http://blank.org/");
		//browser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=2POR");
		
		org.eclipse.swt.widgets.List structureItemsList = new org.eclipse.swt.widgets.List(structureResultsList, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		structureTab.setControl(StructureTabComposite);
		
		//---------------------------------------------------------------
		
		pubmedTab = new TabItem(tFol,SWT.BORDER); // יצירת טאב שני
		pubmedTab.setText("Pubmed");	
		
		Composite PubmedTabComposite = new Composite(tFol,SWT.BORDER); // בניית תוכן הטאב
		PubmedTabComposite.setLayout(new FillLayout());
		
		pubmedResultsList = new ScrolledComposite(PubmedTabComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		pubmedResultsList.setLocation(10, 10);
		pubmedResultsList.setSize(PubmedTabComposite.getBounds().width / 6,
				PubmedTabComposite.getBounds().height - 20);
		
		pubmedFullData = new Text(PubmedTabComposite, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		pubmedFullData.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		pubmedFullData.setBounds(
				pubmedResultsList.getBounds().x + pubmedResultsList.getBounds().width + 15,
				pubmedResultsList.getBounds().y,
				(int)(PubmedTabComposite.getBounds().width / 6 * 4.8),
				pubmedResultsList.getBounds().height  );
		
		org.eclipse.swt.widgets.List pubmedItemsList = new org.eclipse.swt.widgets.List(pubmedResultsList, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		pubmedTab.setControl(PubmedTabComposite);

		//---------------------------------------------------------------
		
		proteinTab = new TabItem(tFol,SWT.BORDER); // יצירת טאב שלישי
		proteinTab.setText("Protein");	
		
		Composite ProteinTabComposite = new Composite(tFol,SWT.BORDER); // בניית תוכן הטאב
		ProteinTabComposite.setLayout(new FillLayout());
		
		proteinResultsList = new ScrolledComposite(ProteinTabComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		proteinResultsList.setLocation(10, 10);
		proteinResultsList.setSize(ProteinTabComposite.getBounds().width / 6,
				ProteinTabComposite.getBounds().height - 20);
		
		proteinFullData = new Text(ProteinTabComposite, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		proteinFullData.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		proteinFullData.setBounds(
				proteinResultsList.getBounds().x + proteinResultsList.getBounds().width + 15,
				proteinResultsList.getBounds().y,
				(int)(ProteinTabComposite.getBounds().width / 6 * 4.8),
				proteinResultsList.getBounds().height  );
		
		org.eclipse.swt.widgets.List proteinItemsList = new org.eclipse.swt.widgets.List(proteinResultsList, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		proteinTab.setControl(ProteinTabComposite);
		
		//---------------------------------------------------------------
		
		geneTab = new TabItem(tFol,SWT.BORDER); // יצירת טאב רביעי
		geneTab.setText("Gene");	
		
		Composite GeneTabComposite = new Composite(tFol,SWT.BORDER); // בניית תוכן הטאב
		GeneTabComposite.setLayout(new FillLayout());
		
		geneResultsList = new ScrolledComposite(GeneTabComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		geneResultsList.setLocation(10, 10);
		geneResultsList.setSize(GeneTabComposite.getBounds().width / 6,
				GeneTabComposite.getBounds().height - 20);
		
		geneFullData = new Text(GeneTabComposite, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		geneFullData.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		geneFullData.setBounds(
				geneResultsList.getBounds().x + geneResultsList.getBounds().width + 15,
				geneResultsList.getBounds().y,
				(int)(GeneTabComposite.getBounds().width / 6 * 4.8),
				geneResultsList.getBounds().height  );
		
		org.eclipse.swt.widgets.List geneItemsList = new org.eclipse.swt.widgets.List(geneResultsList, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		
		geneTab.setControl(GeneTabComposite);
		
		//---------------------------------------------------------------
		Button searchBtn = new Button(shell, SWT.NONE);
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				client = new Client();
				try {
					client.InitialConnection();
					// נאפס את התוצאות הקודמות לפני החיפוש
					structureItemsList.removeAll();
					structureBrowser.setUrl("http://blank.org/");
					pubmedItemsList.removeAll();
					pubmedFullData.setText(" ");
					proteinItemsList.removeAll();
					proteinFullData.setText(" ");
					geneItemsList.removeAll();
					geneFullData.setText(" ");
					
					//searchStructure();
					searchPubmed();
					//searchProtein();
					//searchGene();						
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//result.setText("server error:" + e1.toString());
					MessageBox msb = new MessageBox(shell,SWT.ICON_ERROR);
					msb.setText("Warning");
					msb.setMessage("an Error occured while searching \n" + e1.toString());
					msb.open();
					//e1.printStackTrace();
				}

			}
			
			private void searchStructure() throws Exception {
				List<Structure> stru = client.onlineSearch(text.getText(),DBType.STRUCTURE);				
				for (Structure str: stru)
					structureItemsList.add(str.getName());
					
				structureItemsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = structureItemsList.getSelectionIndices();
					Structure str = stru.get(selectedItems[0]);
					structureBrowser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=" + str.getPdbID());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
				    	int[] selectedItems = structureItemsList.getSelectionIndices();
						Structure str = stru.get(selectedItems[0]);
						structureBrowser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=" + str.getPdbID());
					    }
				});
				structureResultsList.setContent(structureItemsList);
			}
			
			private void searchPubmed() throws Exception {
				List<Article> arts = client.onlineSearch(text.getText(),DBType.PUBMED);				
				for (Article art: arts)
					pubmedItemsList.add(art.getTitle());
					
				pubmedItemsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = pubmedItemsList.getSelectionIndices();
					Article art = arts.get(selectedItems[0]);
					pubmedFullData.setText(art.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = pubmedItemsList.getSelectionIndices();
						Article art = arts.get(selectedItems[0]);
						pubmedFullData.setText(art.toString());
					    }
				});
				pubmedResultsList.setContent(pubmedItemsList);
			}
			
			private void searchProtein() throws Exception {
				List<Protein> prots = client.onlineSearch(text.getText(),DBType.PROTEIN);				
				for (Protein pro: prots)
					proteinItemsList.add(pro.getTitle());
					
				proteinItemsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = proteinItemsList.getSelectionIndices();
					Protein pro = prots.get(selectedItems[0]);
					proteinFullData.setText(pro.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = proteinItemsList.getSelectionIndices();
						Protein pro = prots.get(selectedItems[0]);
						proteinFullData.setText(pro.toString());
					    }
				});
				proteinResultsList.setContent(proteinItemsList);
			}
			
			private void searchGene() throws Exception {
				List<Gene> genes = client.onlineSearch(text.getText(),DBType.GENE);				
				for (Gene gen: genes)
					geneItemsList.add(gen.getName());
					
				geneItemsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = geneItemsList.getSelectionIndices();
					Gene gen = genes.get(selectedItems[0]);
					geneFullData.setText(gen.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = geneItemsList.getSelectionIndices();
						Gene gen = genes.get(selectedItems[0]);
						geneFullData.setText(gen.toString());
					    }
				});
				geneResultsList.setContent(proteinItemsList);
			}
		});
		searchBtn.setBounds(467, 24, 116, 32);
		searchBtn.setText("Searce");
		
		shell.setDefaultButton(searchBtn);		
		
//		Button saveButton = new Button(shell, SWT.NONE);
//		saveButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Client clientSearch = new Client();
//				try {
//					clientSearch.InitialConnection();
//					result.setText(" ");
//					
//					List<Structure> books = clientSearch.onlineSearch(text.getText());
//					itemsList.removeAll(); // ניקוי היסטוריה
//					for (Structure str: books)
//						itemsList.add(str.getName());
//						
//					itemsList.addSelectionListener(new SelectionListener() {
//					public void widgetSelected(SelectionEvent event) {
//						int[] selectedItems = itemsList.getSelectionIndices();
//						Structure str = books.get(selectedItems[0]);
//					      result.setText(str.toString());
//					    } // לשנות לפניה לשרת לשלוף מאמר שלם
//
//					    public void widgetDefaultSelected(SelectionEvent event) {
//							int[] selectedItems = itemsList.getSelectionIndices();
//							Structure str = books.get(selectedItems[0]);
//						      result.setText(str.toString());
//						    } // 
//					});
//					composite.setContent(itemsList);
//						
//						
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					result.setText("server error:" + e1.toString());
//					//e1.printStackTrace();
//				}
//
//			}
//		});
//		saveButton.setBounds(345, 24, 116, 32);
//		saveButton.setText("save to local");
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
		
//		composite = new ScrolledComposite(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
//		composite.setLocation(10, 70);
//		composite.setSize(shell.getBounds().width / 6, (int)(shell.getBounds().height * 0.8));
//		GridLayout gl = new GridLayout();
//		gl.numColumns = 1;
		
//		result = new Text(shell, SWT.READ_ONLY | SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
//		result.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
//		result.setBounds(
//				composite.getBounds().x + composite.getBounds().width + 15,
//				composite.getBounds().y,
//				(int)(Display.getCurrent().getBounds().width),
//				composite.getBounds().height  );
//		result.setVisible(false); // !!!!!!!!!!!!!!!!!!!
		
//		Browser browser = new Browser(shell,SWT.NONE);
		//browser.setBounds(132,62,451,274);
//		browser.setBounds(
//				composite.getBounds().x + composite.getBounds().width + 15,
//				composite.getBounds().y,
//				(int)(Display.getCurrent().getBounds().width / 1.5),
//				composite.getBounds().height  );
//		browser.setVisible(true); // !!!!!!!!!!!!!!!!!
//		browser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=5U68");
//		
//		org.eclipse.swt.widgets.List itemsList = new org.eclipse.swt.widgets.List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
//		
//		Button btnNewButton = new Button(shell, SWT.NONE);
//		btnNewButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Client clientSearch = new Client();
//				try {
//					clientSearch.InitialConnection();
////					result.setText(" ");
//					
//					List<Structure> books = clientSearch.onlineSearch(text.getText());
//					itemsList.removeAll(); // ניקוי היסטוריה
//					for (Structure str: books)
//						itemsList.add(str.toString());
//						
//					itemsList.addSelectionListener(new SelectionListener() {
//					public void widgetSelected(SelectionEvent event) {
//						int[] selectedItems = itemsList.getSelectionIndices();
//						Structure str = books.get(selectedItems[0]);
////					    result.setText(str.toString());
//					} // לשנות לפניה לשרת לשלוף מאמר שלם
//
//					    public void widgetDefaultSelected(SelectionEvent event) {
//							int[] selectedItems = itemsList.getSelectionIndices();
//							Structure str = books.get(selectedItems[0]);
////						      result.setText(str.toString());
//						    } // 
//					});
////					composite.setContent(itemsList);
//						
//						
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
////					result.setText("server error:" + e1.toString());
//					//e1.printStackTrace();
//				}
//
//			}
//		});
//		btnNewButton.setBounds(467, 24, 116, 32);
//		btnNewButton.setText("searce");
//		
//		shell.setDefaultButton(btnNewButton);		
		
//		shell.addControlListener(new ControlAdapter() {			
//			@Override
//			public void controlResized(ControlEvent e) {
//				// TODO Auto-generated method stub
//				//shell.setBounds(shell.getClientArea());
//			}
//		});
		
//		Button saveButton = new Button(shell, SWT.NONE);
//		saveButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				Client clientSearch = new Client();
//				try {
//					clientSearch.InitialConnection();
////					result.setText(" ");
//					
//					List<Structure> books = clientSearch.onlineSearch(text.getText());
//					itemsList.removeAll(); // ניקוי היסטוריה
//					for (Structure str: books)
//						itemsList.add(str.getName());
//						
//					itemsList.addSelectionListener(new SelectionListener() {
//					public void widgetSelected(SelectionEvent event) {
//						int[] selectedItems = itemsList.getSelectionIndices();
//						Structure str = books.get(selectedItems[0]);
////					      result.setText(str.toString());
//					    } // לשנות לפניה לשרת לשלוף מאמר שלם
//
//					    public void widgetDefaultSelected(SelectionEvent event) {
//							int[] selectedItems = itemsList.getSelectionIndices();
//							Structure str = books.get(selectedItems[0]);
////						      result.setText(str.toString());
//						    } // 
//					});
////					composite.setContent(itemsList);
//						
//						
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
////					result.setText("server error:" + e1.toString());
//					//e1.printStackTrace();
//				}
//
//			}
//		});
//		saveButton.setBounds(345, 24, 116, 32);
//		saveButton.setText("save to local");
//		
//		text = new Text(shell, SWT.BORDER);
//		text.setBounds(10, 24, 329, 32);
//		text.setFocus();
//		text.setText("blood");
	}
}
