package org.datagen.ui.main;

import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class JFaceExample extends ApplicationWindow {
	public class ExitAction extends Action {
		ApplicationWindow window;

		public ExitAction(ApplicationWindow w) {
			window = w;
			setText("E&xit@Ctrl+W");
			setToolTipText("Exit the application");
			try {
				setImageDescriptor(ImageDescriptor.createFromURL(new URL(
						"file:icons/close.gif")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			window.close();
		}
	}

	private final ExitAction exitAction;

	public JFaceExample() {
		super(null);
		exitAction = new ExitAction(this);
		this.addMenuBar();
		this.addStatusLine();
		this.addToolBar(SWT.FLAT | SWT.WRAP);
	}

	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("JFace File Explorer");
		setStatus("JFace Example v1.0");
		return parent;
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuBar = new MenuManager("");
		MenuManager fileMenu = new MenuManager("&File");
		MenuManager helpMenu = new MenuManager("&Help");
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		// fileMenu.add(exitAction);
		return menuBar;
	}

	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager slm = new StatusLineManager();
		slm.setMessage("Hello, world!");
		return slm;
	}

	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		toolBarManager.add(exitAction);
		return toolBarManager;
	}

	public static void main(String[] args) {
		JFaceExample fe = new JFaceExample();
		fe.setBlockOnOpen(true);
		fe.open();
		Display.getCurrent().dispose();
	}
}
