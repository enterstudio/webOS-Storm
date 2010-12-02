package com.mojojungle.webosstorm.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.mojojungle.webosstorm.WebOSStorm;
import org.jetbrains.annotations.NotNull;

public abstract class CreateSceneAction extends AnAction {
	private String title;
	private String templateName;

	protected CreateSceneAction(String title, String templateName) {
		this.title = title;
		this.templateName = templateName;
	}

	@Override
	public void actionPerformed(AnActionEvent event) {
		Project project = event.getData(PlatformDataKeys.PROJECT);
		VirtualFile file = event.getData(PlatformDataKeys.VIRTUAL_FILE);
		if (file != null) {
			file = WebOSStorm.getAppDirForFile(file);
		}
		NewSceneDialog dialog = new NewSceneDialog(project, title, file);
		dialog.show();
		if(dialog.getExitCode() == 0) {
			NewSceneForm form = dialog.getForm();
			if (form.getName().trim().length() != 0) {
				createScene(project, form);
			}
		}
	}

	protected void createScene(final Project project, final NewSceneForm form) {
		ProgressManager.getInstance().run(new Task.Backgroundable(project, this.title, false) {
			@Override
			public void run(@NotNull ProgressIndicator pi) {
				try {
					final String name = form.getName();
					final VirtualFile appFolder = form.getSelectedProject();
					Process process = new ProcessBuilder("cmd", "/c", "palm-generate", "-t", templateName, "-p", "name=" + name, appFolder.getPath()).start();
					process.waitFor();
					appFolder.refresh(false, true);
					ApplicationManager.getApplication().invokeLater(new Runnable() {
						public void run() {
							VirtualFile newAssistant = appFolder.findFileByRelativePath("app/assistants/" + name + "-assistant.js");
							VirtualFile newScene = appFolder.findFileByRelativePath("app/views/" + name + "/" + name + "-scene.html");
							if (newScene != null)
								FileEditorManager.getInstance(project).openFile(newScene, true);
							if (newAssistant != null)
								FileEditorManager.getInstance(project).openFile(newAssistant, true);
						}
					});
				} catch (Exception e) {
//					e.printStackTrace();
					Messages.showErrorDialog(project, "Cannot create new Scene:\n" + e.getMessage(), "Error");
				}
			}
		});
	}

}
