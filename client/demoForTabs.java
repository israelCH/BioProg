package client;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class demoForTabs {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			demoForTabs window = new demoForTabs();
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
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("Demo for Tabs");
		final TabFolder tabFolder = new TabFolder(shell, SWT.BORDER);
		Composite composite;
		List list;
		List list2;
	    for (int i = 0; i < 6; i++) {
	      TabItem item = new TabItem(tabFolder, SWT.NONE);
	      item.setText("Tab Item " + i);
	      composite = new Composite(tabFolder, SWT.NONE);
	      composite.setLayout(new FillLayout());
	      list  = new List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	      list2  = new List(composite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
	      for(int j = i; j < (i + 6); j++) {
	    	  list.add("Button " + j);
	    	  list2.add("Israel " + j);
	      }	      
	      item.setControl(composite);
	    }
	    tabFolder.pack();
	}

}
