package com.mojojungle.webosstorm.run;

import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.mojojungle.webosstorm.WebOSStorm;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class WebOSRunConfigurationType extends ConfigurationTypeBase implements ConfigurationType {

	public WebOSRunConfigurationType() {
		super("WebOS.RunConfigurationType", "webOS Application", "webOS Application", WebOSStorm.ICON);
		addFactory(new WebOSConfigurationFactory(this));
	}

	@NonNls
	@NotNull
	public String getComponentName() {
		return "WebOS.RunConfigurationType";
	}

}
