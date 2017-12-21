package Admin;

//import javax.persistence.Convert;

//import org.eclipse.persistence.oxm.json.JsonParserSource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
//import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
//import org.jsoup.Jsoup;
//import org.jsoup.helper.W3CDom;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;

import database.DataBase.DBType;
//import parsers.XMLparser;
import persistentdatabase.model.Article;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

public class AdminGui {

	protected Shell shell;
	private Text text;
	
	private List pubmedList;
	private Text pubmedFulldata;
	public java.util.List<Article> arts;
	
	private List proteinList;
	private Text proteinFulldata;
	public java.util.List<Protein> prots;
	
	private List geneList;
	private Text geneFulldata;
	public java.util.List<Gene> genes;
	private Browser geneBrowser;

	private List structureList;
	private Browser structureBrowser;
	public java.util.List<Structure> stru;
	
	private List malacardsList;
	private Text malacardsFulldata;	
	
	AdminClient client = null;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AdminGui window = new AdminGui();
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
		shell.setImage(SWTResourceManager.getImage(AdminGui.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(AdminGui.class, "/Images/search_background.jpg"));
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
				client = new AdminClient();
				try {
					client.InitialConnection();
					// נאפס את התוצאות הקודמות לפני החיפוש
					pubmedList.removeAll();
					pubmedFulldata.setText(" ");
					proteinList.removeAll();
					proteinFulldata.setText(" ");
					geneList.removeAll();
					geneFulldata.setText(" ");
					geneBrowser.setUrl("http://blank.org/");
					structureList.removeAll();
					structureBrowser.setUrl("http://blank.org/");
					malacardsList.removeAll();
					malacardsFulldata.setText(" ");
										
					searchGene();
					System.out.println("### Gene completed ###");
					searchMalaCards();
					System.out.println("### MalaCard completed ###");
				    searchProtein();
					System.out.println("### Protein completed ###");
					searchStructure();
					System.out.println("### Structure completed ###");
					searchPubmed();
					System.out.println("### Pubmed completed ###");
					
					// ....... ניסיונות
					////W3CDom w3cDom = new W3CDom(); w3cDom.fromJsoup(____)...
					//Document doc = Jsoup.connect("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=5LRB").timeout(10*1000).get();
					//Element daf = doc.getElementsByTag("script").get(4);
					// לנסות לשתול סקריפט בתוך הדף ולהציג בבראוזר שלנו
					//StringBuilder str = new StringBuilder(daf.getTextContent());					
					//str.insert(str.toString().indexOf("$( document ).ready"), "var myVar; function localFunction() { myVar = setTimeout(setVis, 3000); } function setVis () { $('div_0').text('ddd'); } ");
					//str.insert(str.toString().indexOf("}); // document ready"), "localFunction();");
					//doc.getElementsByTagName("script").item(4).setTextContent(str.toString());
					//doc.setTextContent("ddd");
					//structureBrowser.setText(str.toString());
					
				} catch (Exception e1) {
					MessageBox msb = new MessageBox(shell,SWT.ICON_ERROR);
					msb.setText("Warning");
					msb.setMessage("David/Israel, \n an Error occured while searching: \n" + e1.toString());
					msb.open();
				}

			}
			
			private void searchStructure() throws Exception {
				stru = client.onlineSearch(text.getText(),DBType.STRUCTURE);	
				if (stru != null) {
				for (Structure str: stru)
					structureList.add(str.getPdbID() + " - " + str.getName());
				}
				
				else {
					structureBrowser.setUrl("http://blank.org/");
				}
					
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
				arts =  client.onlineSearch(text.getText(),DBType.PUBMED);
				if(arts != null)
					{
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
				else {
					pubmedFulldata.setText("No Result");
				}
			}
			
			private void searchProtein() throws Exception {
				prots = client.onlineSearch(text.getText(),DBType.PROTEIN);	
				if(prots != null) {
				for (Protein pro: prots)
					proteinList.add(pro.getTitle());
				}	
				
				else {
					proteinFulldata.setText("No Result");
				}
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
				genes = client.onlineSearch(text.getText(),DBType.GENE);	
				if(genes != null) {
				for (Gene gen: genes)
					geneList.add(gen.getDesc());
				}
				
				else {
					geneFulldata.setText("No Result");
					geneBrowser.setUrl("http://blank.org/");
				}
				geneList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = geneList.getSelectionIndices();
					Gene gen = genes.get(selectedItems[0]);
					geneFulldata.setText(gen.toString());
					geneBrowser.setUrl("https://www.ncbi.nlm.nih.gov/genome/gdv/browser/?context=gene&acc=" + gen.getId());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = geneList.getSelectionIndices();
						Gene gen = genes.get(selectedItems[0]);
						geneFulldata.setText(gen.toString());
						geneBrowser.setUrl("https://www.ncbi.nlm.nih.gov/genome/gdv/browser/?context=gene&acc=" + gen.getId());
					    }
				});
			}
			
			private void searchMalaCards() throws Exception {
				java.util.List<Disease> diseases = client.onlineSearch(text.getText(),DBType.MALA_CARDS);
				if (diseases != null) {
				for (Disease dis: diseases)
					malacardsList.add(dis.getName());
				}
				else {
					malacardsFulldata.setText("No Result");
				}
				malacardsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
			    	malacardsFulldata.setText("");

					int[] selectedItems = malacardsList.getSelectionIndices();
					Disease dis = diseases.get(selectedItems[0]);
					if(dis.getSummaries().equals("")){
						dis = client.GetMiniCard(dis);
//						dis.byRefPaste();						
					}
					malacardsFulldata.setText(dis.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
				    	malacardsFulldata.setText("");
				    	int[] selectedItems = malacardsList.getSelectionIndices();
						Disease dis = diseases.get(selectedItems[0]);
						if(dis.getSummaries() == ""){
							dis = client.GetMiniCard(dis);
//							dis.byRefPaste();						
						}
						malacardsFulldata.setText(dis.toString());
					    }
				});
			}
		});
		searchBtn.setBounds(345, 24, 116, 32);
		searchBtn.setText("Searce");
		
		shell.setDefaultButton(searchBtn);		
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 62, 1330, 612);
		
		//-------------------------------------------------------------
		
		TabItem pubmedTabItem = new TabItem(tabFolder, SWT.NONE);
		pubmedTabItem.setText("Pubmed");
		
		Composite pubmedComposite = new Composite(tabFolder, SWT.NONE);
		pubmedTabItem.setControl(pubmedComposite);
		
		pubmedList = new List(pubmedComposite, SWT.BORDER | SWT.V_SCROLL);
		pubmedList.setBounds(10, 10, 250, 564);
		
		pubmedFulldata = new Text(pubmedComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		pubmedFulldata.setEditable(false);
		pubmedFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		TabItem proteinTabItem = new TabItem(tabFolder, SWT.NONE);
		proteinTabItem.setText("Protein");
		
		Composite proteinComposite = new Composite(tabFolder, SWT.NONE);
		proteinTabItem.setControl(proteinComposite);
		
		proteinList = new List(proteinComposite, SWT.BORDER);
		proteinList.setBounds(10, 10, 250, 564);
		
		proteinFulldata = new Text(proteinComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		proteinFulldata.setEditable(false);
		proteinFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		TabItem geneTabItem = new TabItem(tabFolder, SWT.NONE);
		geneTabItem.setText("Gene");
		
		Composite geneComposite = new Composite(tabFolder, SWT.NONE);
		geneTabItem.setControl(geneComposite);
		
		geneList = new List(geneComposite, SWT.BORDER);
		geneList.setBounds(10, 10, 250, 564);
		
		geneFulldata = new Text(geneComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		geneFulldata.setEditable(false);
		geneFulldata.setBounds(266, 11, 1046, 234);
		
		geneBrowser = new Browser(geneComposite, SWT.NONE);
		geneBrowser.setBounds(266, 251, 1046, 323);
		
		//-------------------------------------------------------------
		
		TabItem structureTabItem = new TabItem(tabFolder, SWT.NONE);
		structureTabItem.setText("Structure");
		
		Composite structureComposite = new Composite(tabFolder, SWT.NONE);
		structureTabItem.setControl(structureComposite);
		
		structureList = new List(structureComposite, SWT.BORDER);
		structureList.setBounds(10, 10, 250, 564);
		
		structureBrowser = new Browser(structureComposite, SWT.NONE);
		structureBrowser.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------		
		
		TabItem malacardsTabItem = new TabItem(tabFolder, SWT.NONE);
		malacardsTabItem.setText("Malacards");
		
		Composite malacardsComposite = new Composite(tabFolder, SWT.NONE);
		malacardsTabItem.setControl(malacardsComposite);
		
		malacardsList = new List(malacardsComposite, SWT.BORDER | SWT.V_SCROLL);
		malacardsList.setBounds(10, 10, 250, 564);
		
		malacardsFulldata = new Text(malacardsComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		malacardsFulldata.setEditable(false);
		malacardsFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		Button saveButton = new Button(shell, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				client = new AdminClient();
				try {
					client.InitialConnection();						
					client.onlineSaveLocal();
					MessageBox msb = new MessageBox(shell,SWT.ICON_WORKING);
					msb.setText("Save complete");
					msb.setMessage("Save To Local DataBase COMPLETED Successfully :)");
					msb.open();
					
				} catch (Exception e1) {
					MessageBox msb = new MessageBox(shell,SWT.ICON_ERROR);
					msb.setText("Warning");
					msb.setMessage("David/Israel, \n an Error occured while saving: \n" + e1.toString());
					msb.open();
				}
			}
		});
		saveButton.setBounds(471, 24, 116, 32);
		saveButton.setText("save to local");
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
		shell.setImage(SWTResourceManager.getImage(AdminGui.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(AdminGui.class, "/Images/search_background.jpg"));
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
