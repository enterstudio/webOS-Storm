package com.mojojungle.webosstorm.run;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.DefaultProgramRunner;
import org.jetbrains.annotations.NotNull;

public class WebOSProgramRunner extends DefaultProgramRunner {

	@NotNull
	public String getRunnerId() {
		return "WebOSRunner";
	}

	public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
		return profile instanceof WebOSRunConfiguration;
	}

	
}
