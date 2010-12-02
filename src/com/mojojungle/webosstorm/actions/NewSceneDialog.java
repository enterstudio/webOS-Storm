package com.mojojungle.webosstorm.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.mojojungle.webosstorm.WebOSStorm;

import javax.swing.*;

public class NewSceneDialog extends DialogWrapper {
	private NewSceneForm newSceneForm;

	protected NewSceneDialog(Project project, String title, VirtualFile file) {
		super(project);
		setTitle(title);
		Object[] projects = WebOSStorm.getWebOSAppsInProject(project).toArray();
		newSceneForm = new NewSceneForm(projects);
		newSceneForm.setSelectedProject(file);
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
		return newSceneForm.getComponent();
	}

	@Override
	public JComponent getPreferredFocusedComponent() {
		return newSceneForm.getNameField();
	}

	public NewSceneForm getForm() {
		return newSceneForm;
	}
}
