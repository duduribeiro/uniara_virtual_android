package br.com.uniara.virtual.client.core;

class UniaraVirtualConfig {
	private static String sessionID;
	
	protected static String getSessionID() {
		return sessionID;
	}

	protected static void setSessionID(String sessionID) {
		UniaraVirtualConfig.sessionID = sessionID;
	}
	
	protected static boolean isLogged() {
		return !(UniaraVirtualConfig.getSessionID() == null);
	}
}
