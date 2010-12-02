package com.mojojungle.webosstorm;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.ElementBase;
import com.intellij.util.Icons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class WebOSIconProvider extends IconProvider {
	private static final int MY_FLAG = ElementBase.FLAGS_LOCKED << 1;

	public WebOSIconProvider() {
		ElementBase.registerIconLayer(MY_FLAG, WebOSStorm.NATURE_ICON);
	}

	@Override
	public Icon getIcon(@NotNull PsiElement psiElement, int flags) {
		if (psiElement instanceof PsiDirectory) {
			PsiDirectory directory = (PsiDirectory) psiElement;
			if (WebOSStorm.isWebOSAppDir(directory)) {
//				return WebOSIcons.ICON;
				boolean isOpen = (flags & Iconable.ICON_FLAG_OPEN) != 0;
				Icon icon = isOpen ? Icons.DIRECTORY_OPEN_ICON : Icons.DIRECTORY_CLOSED_ICON;
				return ElementBase.createLayeredIcon(icon, MY_FLAG);
			}
		}
		return null;
	}
}
