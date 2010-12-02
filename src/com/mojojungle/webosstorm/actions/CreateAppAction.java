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

import java.io.File;

public abstract class CreateAppAction extends AnAction {
	private String title;
	private String templateName;

	protected CreateAppAction(String title, String templateName) {
		this.title = title;
		this.templateName = templateName;
	}

	@Override
	public void actionPerformed(AnActionEvent event) {
		Project project = event.getData(PlatformDataKeys.PROJECT);
		VirtualFile baseDir = project.getBaseDir();
		NewAppDialog dialog = new NewAppDialog(project, title, baseDir);
		dialog.show();
		if (dialog.getExitCode() == 0) {
			NewAppForm form = dialog.getForm();
			if (form.getName().trim().length() != 0) {
				createApp(project, form, baseDir);
			}
		}
	}

	protected void createApp(final Project project, final NewAppForm form, final VirtualFile baseDir) {
		ProgressManager.getInstance().run(new Task.Backgroundable(project, this.title, false) {
			@Override
			public void run(@NotNull ProgressIndicator pi) {
				try {
					final String name = form.getName();
					String path = baseDir.getPath() + File.separator + name;
					Process process = new ProcessBuilder("java", "-jar", WebOSStorm.getWebOSToolsJarPath(), "palm-generate", "-t", templateName,
							"-p", "title=" + form.getAppTitle(),
							"-p", "id=" + form.getAppID(),
							"-p", "vendor=" + form.getAppVendor(),
							"-p", "version=" + form.getAppVersion(),
							path).start();
					process.waitFor();
					baseDir.refresh(false, true);
					ApplicationManager.getApplication().invokeLater(new Runnable() {
						public void run() {
							VirtualFile appDir = baseDir.findChild(name);
							if (appDir != null) {
								VirtualFile newIndex = appDir.findFileByRelativePath("index.html");
								VirtualFile newAppInfo = appDir.findFileByRelativePath("appinfo.json");
								if(newIndex != null)
									FileEditorManager.getInstance(project).openFile(newIndex, true);
								if(newAppInfo != null)
									FileEditorManager.getInstance(project).openFile(newAppInfo, true);
							}
						}
					});
				} catch (final Exception e) {
					ApplicationManager.getApplication().invokeLater(new Runnable() {
						public void run() {
							Messages.showErrorDialog(project, "Cannot create new App:\n" + e.getMessage(), "Error");
						}
					});
				}
			}
		});
	}

}
