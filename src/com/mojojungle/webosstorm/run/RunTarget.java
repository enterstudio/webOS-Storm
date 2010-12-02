package com.mojojungle.webosstorm.run;

public enum RunTarget {
	EMULATOR("tcp"), DEVICE("usb");

	private String id;

	RunTarget(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		switch (this) {
			case EMULATOR:
				return "Emulator";
			case DEVICE:
				return "Device";
		}
		return "Undefined";
	}

	/**
	 * Returns the id of this target as used in the palm command line tools. For the emulator the id is 'tcp', for a real device it is
	 * 'usb'.
	 */
	public String getId() {
		return id;
	}
}
