package com.mojojungle.webosstorm.run;

import com.intellij.execution.Location;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.mojojungle.webosstorm.WebOSStorm;

public class WebOSRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {

	private PsiDirectory mySourceFile;

	public WebOSRunConfigurationProducer() {
		super(new WebOSRunConfigurationType());
	}

	@Override
	public PsiElement getSourceElement() {
		return mySourceFile;
	}

	@Override
	protected RunnerAndConfigurationSettings createConfigurationByElement(Location location, ConfigurationContext configurationContext) {
//		Logger log = Logger.getLogger("#com.mojojungle.webosstorm");
//		log.setLevel(Level.INFO);

		PsiElement element = location.getPsiElement();
		PsiDirectory psiAppDir = findAppDir(element);
		if (psiAppDir == null)
			return null;

		this.mySourceFile = psiAppDir;

		final Module module = ModuleUtil.findModuleForPsiElement(psiAppDir);
		if (module == null) {
			return null;
		}

		final Project project = mySourceFile.getProject();

		RunnerAndConfigurationSettings settings = this.cloneTemplateConfiguration(project, configurationContext);
		WebOSRunConfiguration config = (WebOSRunConfiguration) settings.getConfiguration();

//		WebOSRunConfiguration config = (WebOSRunConfiguration) getConfigurationFactory().createTemplateConfiguration(project);
		config.setModule(module);
		config.setAppFolder(psiAppDir.getVirtualFile());
		config.setName(config.suggestedName());
//		return RunManagerImpl.getInstanceImpl(project).createConfiguration(config, getConfigurationFactory());
		return settings;
	}

	private PsiDirectory findAppDir(PsiElement element) {
		if (element == null)
			return null;
		if (element instanceof PsiDirectory) {
			return findAppDirFromPsiDirectory((PsiDirectory) element);
		} else if (element instanceof PsiFile) {
			PsiFile psiFile = (PsiFile) element;
			return findAppDirFromPsiDirectory(psiFile.getContainingDirectory());
		} else {
			return findAppDir(element.getContainingFile());
		}
	}

	private PsiDirectory findAppDirFromPsiDirectory(PsiDirectory directory) {
		while (directory != null) {
			if (WebOSStorm.isWebOSAppDir(directory)) {
				return directory;
			}
			directory = directory.getParentDirectory();
		}
		return null;
	}

//	@Override
//	public RuntimeConfigurationProducer clone() {
//		WebOSRunConfigurationProducer producer = (WebOSRunConfigurationProducer) super.clone();//new WebOSRunConfigurationProducer();
//		producer.isClone = true;
//		return producer;
//	}

	public int compareTo(Object o) {
		return -100;
	}

}
