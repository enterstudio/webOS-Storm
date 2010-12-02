package com.mojojungle.webosstorm.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.mojojungle.webosstorm.WebOSStorm;

import javax.swing.*;

public class NewAppDialog extends DialogWrapper {
	private NewAppForm newAppForm;

	protected NewAppDialog(Project project, String title, VirtualFile baseDir) {
		super(project);
		setTitle(title);
		newAppForm = new NewAppForm(baseDir);
		init();
	}

	@Override
	protected void createDefaultActions() {
		super.createDefaultActions();
	}

	@Override
	protected JComponent createCenterPanel() {
		return null;
	}

	@Override
	protected JComponent createNorthPanel() {
		return newAppForm.getComponent();
	}

	@Override
	public JComponent getPreferredFocusedComponent() {
		return newAppForm.getNameField();
	}

	public NewAppForm getForm() {
		return newAppForm;
	}
}
