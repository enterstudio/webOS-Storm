package com.mojojungle.webosstorm.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class HelloAction extends AnAction {
	@Override
	public void actionPerformed(AnActionEvent anActionEvent) {
		Messages.showMessageDialog("Hello", "Hello", Messages.getInformationIcon());
	}
}
