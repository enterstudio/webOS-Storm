package com.mojojungle.webosstorm;

import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;

public class WebOSAppListCellRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value != null) {
			VirtualFile f = (VirtualFile) value;
			setText(f.getName());
			setIcon(WebOSStorm.ICON);
		}
		return this;
	}
}
