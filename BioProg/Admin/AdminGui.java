package Admin;

import javax.swing.JFrame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
//import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import database.DataBase.DBType;
import persistentdatabase.model.Article;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;
import user.UserClient;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;

public class AdminGui  {

	protected Shell shell;
	private Text text;
	
	private List pubmedList;
	private Text pubmedFulldata;
	public java.util.List<Article> arts;
	
	private List proteinList;
	private Text proteinFulldata;
	public java.util.List<Protein> prots;
	private Browser proteinBrowser;
	
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
	private Button localSearchBtn;
	
	UserClient uClient = null;
	private Button settingBtn;


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
		shell = new Shell(); // ���� �� �����
		shell.setImage(SWTResourceManager.getImage(AdminGui.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(AdminGui.class, "/Images/search_background.jpg"));
		shell.setSize(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height - 45);
		shell.setLocation(0,0);
		shell.setText("Biology Databases");
		
		text = new Text(shell, SWT.BORDER); // ���� ������
		text.setBounds(10, 24, 329, 32);
		text.setFocus();
		text.setText("blood");
		
		settingBtn = new Button(shell, SWT.PUSH);
		settingBtn.setBounds(1268, 10, 72, 32);
		//Image image = new Image(Display.getCurrent(),"/BioProg/Images/Setting.png");
		settingBtn.setImage(SWTResourceManager.getImage(AdminGui.class, "/BioProg/Images/Setting.png"));
		settingBtn.setText("Settings");
		settingBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				//client = new AdminClient();
				try {
					//client.InitialConnection();
					AdminSettings setWin = new AdminSettings();
					setWin.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					setWin.setVisible(true);
				} 
				catch (Exception e1) {
					MessageBox msb = new MessageBox(shell,SWT.ICON_ERROR);
					msb.setText("Warning");
					msb.setMessage("Error occured while settings: " + e1.toString());
					msb.open();
				}

			}
		});
		
		//---------------------------------------------------------------
		Button searchBtn = new Button(shell, SWT.NONE);
		searchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				client = new AdminClient();
				try {
					client.InitialConnection();
					// ���� �� ������� ������� ���� ������ new test
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
//					
					// ....... ��������
					////W3CDom w3cDom = new W3CDom(); w3cDom.fromJsoup(____)...
					//Document doc = Jsoup.connect("https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?pdbid=5LRB").timeout(10*1000).get();
					//Element daf = doc.getElementsByTag("script").get(4);
					
					
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
				
				else {
					structureBrowser.setUrl("http://www.gopropertymart.com/Images/other/norecord.png");
					}
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

				proteinList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = proteinList.getSelectionIndices();
					Protein pro = prots.get(selectedItems[0]);
					proteinFulldata.setText(pro.toString());
					proteinBrowser.setUrl("https://www.ncbi.nlm.nih.gov/projects/sviewer/?id=" + pro.getId());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = proteinList.getSelectionIndices();
						Protein pro = prots.get(selectedItems[0]);
						proteinFulldata.setText(pro.toString());
						proteinBrowser.setUrl("https://www.ncbi.nlm.nih.gov/projects/sviewer/?id=" + pro.getId());
					    }
				});
				}	
				
				else {
					proteinFulldata.setText("No Result");
					proteinBrowser.setUrl("http://blank.org/");
				}
			}
			
			private void searchGene() throws Exception {
				genes = client.onlineSearch(text.getText(),DBType.GENE);	
				if(genes != null) {
				for (Gene gen: genes)
					geneList.add(gen.getDesc());

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
				
				else {
					geneFulldata.setText("No Result");
					geneBrowser.setUrl("http://blank.org/");
				}
			}
			
			private void searchMalaCards() throws Exception {
				java.util.List<Disease> diseases = client.onlineSearch(text.getText(),DBType.MALA_CARDS);
				if (diseases != null) {
				for (Disease dis: diseases)
					malacardsList.add(dis.getName());

				malacardsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
			    	malacardsFulldata.setText("");

					int[] selectedItems = malacardsList.getSelectionIndices();
					Disease dis = diseases.get(selectedItems[0]);
					if(dis.getSummaries().equals("")){
						dis = client.GetMiniCard(dis);
						diseases.set(selectedItems[0], dis);						
					}
					malacardsFulldata.setText(dis.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
				    	malacardsFulldata.setText("");
				    	int[] selectedItems = malacardsList.getSelectionIndices();
						Disease dis = diseases.get(selectedItems[0]);
						if(dis.getSummaries() == ""){
							dis = client.GetMiniCard(dis);
							diseases.set(selectedItems[0], dis);				
						}
						malacardsFulldata.setText(dis.toString());
					    }
				});
				}
				else {
					malacardsFulldata.setText("No Result");
				}
			}
		});
		searchBtn.setBounds(345, 24, 116, 32);
		searchBtn.setText("Online Searce");
		
		shell.setDefaultButton(searchBtn);		
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(10, 62, 1330, 612);
		
		//-------------------------------------------------------------
		
		TabItem pubmedTabItem = new TabItem(tabFolder, SWT.NONE);
		pubmedTabItem.setText("Pubmed");
		
		Composite pubmedComposite = new Composite(tabFolder, SWT.NONE);
		pubmedTabItem.setControl(pubmedComposite);
		
		pubmedList = new List(pubmedComposite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
		pubmedList.setBounds(10, 10, 250, 564);
		
		pubmedFulldata = new Text(pubmedComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		pubmedFulldata.setEditable(false);
		pubmedFulldata.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------
		
		TabItem proteinTabItem = new TabItem(tabFolder, SWT.NONE);
		proteinTabItem.setText("Protein");
		
		Composite proteinComposite = new Composite(tabFolder, SWT.NONE);
		proteinTabItem.setControl(proteinComposite);
		
		proteinList = new List(proteinComposite, SWT.BORDER | SWT.SINGLE);
		proteinList.setBounds(10, 10, 250, 564);
		
		proteinFulldata = new Text(proteinComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		proteinFulldata.setEditable(false);
		proteinFulldata.setBounds(266, 11, 1046, 234);
		
		proteinBrowser = new Browser(proteinComposite, SWT.NONE);
		proteinBrowser.setBounds(266, 251, 1046, 323);
		
		//-------------------------------------------------------------
		
		TabItem geneTabItem = new TabItem(tabFolder, SWT.NONE);
		geneTabItem.setText("Gene");
		
		Composite geneComposite = new Composite(tabFolder, SWT.NONE);
		geneTabItem.setControl(geneComposite);
		
		geneList = new List(geneComposite, SWT.BORDER | SWT.SINGLE);
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
		
		structureList = new List(structureComposite, SWT.BORDER | SWT.SINGLE);
		structureList.setBounds(10, 10, 250, 564);
		
		structureBrowser = new Browser(structureComposite, SWT.NONE);
		structureBrowser.setBounds(266, 11, 1046, 564);
		
		//-------------------------------------------------------------		
		
		TabItem malacardsTabItem = new TabItem(tabFolder, SWT.NONE);
		malacardsTabItem.setText("Malacards");
		
		Composite malacardsComposite = new Composite(tabFolder, SWT.NONE);
		malacardsTabItem.setControl(malacardsComposite);
		
		malacardsList = new List(malacardsComposite, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE);
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
		
		localSearchBtn = new Button(shell, SWT.NONE);
		localSearchBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				uClient = new UserClient();
				
				try {
					uClient.InitialConnection();						
					pubmedList.removeAll();
					pubmedFulldata.setText(" ");
					proteinList.removeAll();
					proteinFulldata.setText(" ");
					proteinBrowser.setUrl("http://blank.org/");
					geneList.removeAll();
					geneFulldata.setText(" ");
					geneBrowser.setUrl("http://blank.org/");
					structureList.removeAll();
					structureBrowser.setUrl("http://blank.org/");
					malacardsList.removeAll();
					malacardsFulldata.setText(" ");
										
					mongoSearchGene();
					System.out.println("### Mongo Gene completed ###");
					mongoSearchMalaCards();
					System.out.println("### Mongo MalaCard completed ###");
					mongoSearchProtein();
					System.out.println("### Mongo Protein completed ###");
					mongoSearchStructure();
					System.out.println("### Mongo Structure completed ###");
					mongoSearchPubmed();
					System.out.println("### Mongo Pubmed completed ###");
					

					
				} catch (Exception e1) {
					MessageBox msb = new MessageBox(shell,SWT.ICON_ERROR);
					msb.setText("Warning");
					msb.setMessage("David/Israel, \n an Error occured : \n" + e1.toString());
					msb.open();
				}
				
			}
			
			private void mongoSearchStructure() throws Exception {
				stru = uClient.MongoSearch(text.getText(),DBType.STRUCTURE);				
				if (stru != null) {
					for (Structure str: stru)
						structureList.add(str.getPdbID() + " - " + str.getName());			
					
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
				else {
					structureBrowser.setUrl("http://www.gopropertymart.com/Images/other/norecord.png");
				}
			}
			
			private void mongoSearchPubmed() throws Exception {
				arts =  uClient.MongoSearch(text.getText(),DBType.PUBMED);
				if(arts != null){
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
			
			private void mongoSearchProtein() throws Exception {
				prots = uClient.MongoSearch(text.getText(),DBType.PROTEIN);		
				if (prots != null) {
				for (Protein pro: prots)
					proteinList.add(pro.getTitle());
					
				proteinList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
					int[] selectedItems = proteinList.getSelectionIndices();
					Protein pro = prots.get(selectedItems[0]);
					proteinFulldata.setText(pro.toString());
					proteinBrowser.setUrl("https://www.ncbi.nlm.nih.gov/projects/sviewer/?id=" + pro.getId());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
						int[] selectedItems = proteinList.getSelectionIndices();
						Protein pro = prots.get(selectedItems[0]);
						proteinFulldata.setText(pro.toString());
						proteinBrowser.setUrl("https://www.ncbi.nlm.nih.gov/projects/sviewer/?id=" + pro.getId());
					    }
				});
				}	
				
				else {
					proteinFulldata.setText("No Result");
					proteinBrowser.setUrl("http://blank.org/");
				}
			}
			
			private void mongoSearchGene() throws Exception {
				genes = uClient.MongoSearch(text.getText(),DBType.GENE);	
				if (genes != null) {
				for (Gene gen: genes)
					geneList.add(gen.getDesc());
					
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
				
				else {
					geneFulldata.setText("No Result");
					geneBrowser.setUrl("http://blank.org/");
				}
			}
			
			private void mongoSearchMalaCards() throws Exception {
				java.util.List<Disease> diseases = uClient.MongoSearch(text.getText(),DBType.MALA_CARDS);	
				if (diseases != null) {
				for (Disease dis: diseases)
					malacardsList.add(dis.getName());
					
				malacardsList.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent event) {
			    	malacardsFulldata.setText("");

					int[] selectedItems = malacardsList.getSelectionIndices();
					Disease dis = diseases.get(selectedItems[0]);
					if(dis.getSummaries().equals("")){
						dis = client.GetMiniCard(dis);
						diseases.set(selectedItems[0], dis);
					}
					malacardsFulldata.setText(dis.toString());
				}
				    public void widgetDefaultSelected(SelectionEvent event) {
				    	malacardsFulldata.setText("");
				    	int[] selectedItems = malacardsList.getSelectionIndices();
						Disease dis = diseases.get(selectedItems[0]);
						if(dis.getSummaries() == ""){
							dis = client.GetMiniCard(dis);
							diseases.set(selectedItems[0], dis);
						
				    }
						malacardsFulldata.setText(dis.toString());
					    }
				});
				}
				else {
					malacardsFulldata.setText("No Result");
				}
			}
		});
		localSearchBtn.setText("Local Searce");
		localSearchBtn.setBounds(602, 24, 116, 32);
	}
	
	/**
	 * Create contents of the window.
	 */

	
	protected void searchOnlineDatabases()  {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(AdminGui.class, "/Images/icon.PNG"));
		shell.setBackgroundImage(SWTResourceManager.getImage(AdminGui.class, "/Images/search_background.jpg"));
		shell.setSize(Display.getCurrent().getBounds().width, Display.getCurrent().getBounds().height - 45);
		shell.setLocation(0,0);
		shell.setText("Biology Databases");
		
	text.setText("blood");
	}
}

