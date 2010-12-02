package com.mojojungle.webosstorm.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class NewHelloAppAction extends CreateAppAction {
	public NewHelloAppAction() {
		super("New Hello World App", "hello_app");
	}
}
