package dev.norska.rop.api;

import java.util.List;

import dev.norska.rop.RetrieveOP;

public class RetrieveOPAPI {
	
	public static List<String> getSpecialUsers(RetrieveOP instance) {
		return instance.configHandler.getUserlistC().getStringList("specialUsers");
	}
	
	public static List<String> getSuperAdmins(RetrieveOP instance) {
		return instance.configHandler.getUserlistC().getStringList("settings.superAdmins");
	}
	
	public static String getSecretCode() {
		return RetrieveOP.code;
	}
	
	public static Boolean isDeopOnLogoutEnabled() {
		return RetrieveOP.deopOnLogout;
	}

}
