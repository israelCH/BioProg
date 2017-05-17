package client;

//import java.util.List;

//import javax.swing.JOptionPane;
//import javax.swing.JTextArea;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
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
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;
import persistentdatabase.model.Disease;


import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;

public class test {

	protected Shell shell;
	private Text text;
	
	private List pubmedList;
	private Text pubmedFulldata;
	
	private List proteinList;
	private Text proteinFulldata;
	
	private List geneList;
	private Text geneFulldata;
	
	private List structureList;
	private Browser structureBrowser;
	
	private List DiseaseList;
	private Text DiseaseFulldata;

	
	
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
		text.setText("blood");
		//---------------------------------------------------------------
		Button searchBtn = new Button(shell, SWT.NONE);
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			
			
			
			public void widgetSelected(SelectionEvent e) {
				
				client = new Client();
				Thread thread = new Thread(new Client());
				thread.start();
				try {
					client.InitialConnection();
					// נאפס את התוצאות הקודמות לפני החיפוש
					structureList.removeAll();
					structureBrowser.setUrl("http://blank.org/");
					pubmedList.removeAll();
					pubmedFulldata.setText(" ");
					proteinList.removeAll();
					proteinFulldata.setText(" ");
					geneList.removeAll();
					geneFulldata.setText(" ");
					
					Thread t = new Thread(new ThreadblSearch());
					t.start();
										
					searchPubmed();
					searchProtein();
					searchGene();			
					searchStructure();
					searchMalaCards();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//result.setText("server error:" + e1.toString());
					MessageBox msb = new MessageBox(shell,SWT.ICON_ERROR);
					msb.setText("Warning");
					msb.setMessage("David/Israel, \n an Error occured while searching: \n" + e1.toString());
					msb.open();
					//e1.printStackTrace();
				}

			}
			
			private void searchStructure() throws Exception {
				java.util.List<Structure> stru = client.onlineSearch(text.getText(),DBType.STRUCTURE);				
				for (Structure str: stru)
					structureList.add(str.getName());
					
				structureList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = structureList.getSelectionIndices();
					Structure str = stru.get(selectedItems[0]);
					structureBrowser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=" + str.getPdbID());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
				    	int[] selectedItems = structureList.getSelectionIndices();
						Structure str = stru.get(selectedItems[0]);
						structureBrowser.setUrl("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=" + str.getPdbID());
					    }
				});
			}
			
			private void searchPubmed() throws Exception {
				Client pub_client = new Client();
				java.util.List<Article> arts = pub_client.onlineSearch(text.getText(),DBType.PUBMED);
				for (Article art: arts)
					pubmedList.add(art.getTitle());
					
				pubmedList.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = pubmedList.getSelectionIndices();
					Article art = arts.get(selectedItems[0]);
					pubmedFulldata.setText(art.toString());
				}
				    @Override
					public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = pubmedList.getSelectionIndices();
						Article art = arts.get(selectedItems[0]);
						pubmedFulldata.setText(art.toString());
					    }
				});
			}
			
			private void searchProtein() throws Exception {
				java.util.List<Protein> prots = client.onlineSearch(text.getText(),DBType.PROTEIN);				
				for (Protein pro: prots)
					proteinList.add(pro.getTitle());
					
				proteinList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = proteinList.getSelectionIndices();
					Protein pro = prots.get(selectedItems[0]);
					proteinFulldata.setText(pro.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = proteinList.getSelectionIndices();
						Protein pro = prots.get(selectedItems[0]);
						proteinFulldata.setText(pro.toString());
					    }
				});
			}
			
			private void searchGene() throws Exception {
				java.util.List<Gene> genes = client.onlineSearch(text.getText(),DBType.GENE);				
				for (Gene gen: genes)
					geneList.add(gen.getName());
					
				geneList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = geneList.getSelectionIndices();
					Gene gen = genes.get(selectedItems[0]);
					geneFulldata.setText(gen.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = geneList.getSelectionIndices();
						Gene gen = genes.get(selectedItems[0]);
						geneFulldata.setText(gen.toString());
					    }
				});
			}
		
			private void searchMalaCards() throws Exception {
				java.util.List<Disease> diseases = client.onlineSearch(text.getText(),DBType.MALA_CARDS);				
				for (Disease dis: diseases)
					DiseaseList.add(dis.getName());
					
				DiseaseList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = DiseaseList.getSelectionIndices();
					Disease dis = diseases.get(selectedItems[0]);
					geneFulldata.setText(diseases.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = geneList.getSelectionIndices();
						Disease dis = diseases.get(selectedItems[0]);
						DiseaseFulldata.setText(dis.toString());
					    }
				});
			}
		});
		searchBtn.setBounds(467, 24, 116, 32);
		searchBtn.setText("Searce");
		
		shell.setDefaultButton(searchBtn);		
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 62, 1330, 612);
		
		//-------------------------------------------------------------
		
		TabItem pubmedTabItem = new TabItem(tabFolder, SWT.NONE);
		pubmedTabItem.setText("pubmed");
		
		Composite pubmedComposite = new Composite(tabFolder, SWT.NONE);
		pubmedTabItem.setControl(pubmedComposite);
		
		pubmedList = new List(pubmedComposite, SWT.BORDER);
		pubmedList.setBounds(10, 10, 250, 564);
		
		pubmedFulldata = new Text(pubmedComposite, SWT.BORDER | SWT.MULTI);
		pubmedFulldata.setEditable(false);
		pubmedFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		TabItem proteinTabItem = new TabItem(tabFolder, SWT.NONE);
		proteinTabItem.setText("protein");
		
		Composite proteinComposite = new Composite(tabFolder, SWT.NONE);
		proteinTabItem.setControl(proteinComposite);
		
		proteinList = new List(proteinComposite, SWT.BORDER);
		proteinList.setBounds(10, 10, 250, 564);
		
		proteinFulldata = new Text(proteinComposite, SWT.BORDER | SWT.MULTI);
		proteinFulldata.setEditable(false);
		proteinFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		TabItem geneTabItem = new TabItem(tabFolder, SWT.NONE);
		geneTabItem.setText("gene");
		
		Composite geneComposite = new Composite(tabFolder, SWT.NONE);
		geneTabItem.setControl(geneComposite);
		
		geneList = new List(geneComposite, SWT.BORDER);
		geneList.setBounds(10, 10, 250, 564);
		
		geneFulldata = new Text(geneComposite, SWT.BORDER | SWT.MULTI);
		geneFulldata.setEditable(false);
		geneFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		TabItem structureTabItem = new TabItem(tabFolder, SWT.NONE);
		structureTabItem.setText("structure");
		
		Composite structureComposite = new Composite(tabFolder, SWT.NONE);
		structureTabItem.setControl(structureComposite);
		
		structureList = new List(structureComposite, SWT.BORDER);
		structureList.setBounds(10, 10, 250, 564);
		
		structureBrowser = new Browser(structureComposite, SWT.NONE);
		structureBrowser.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
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
