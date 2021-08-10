package cc.sukazyo.icee.util;

import cc.sukazyo.icee.iCee;
import org.jetbrains.annotations.NotNull;

public class Var implements Comparable<Var> {
	
	public static final String ICEE_PACKID_KEY = "icee_pack_id";
	@SuppressWarnings("unused")
	public static final Var ICEE_PACKID = new Var(ICEE_PACKID_KEY, iCee.PACKID);
	public static final String ICEE_VERSION_KEY = "icee_ver";
	@SuppressWarnings("unused")
	public static final Var ICEE_VERSION = new Var(ICEE_VERSION_KEY, iCee.VERSION);
	public static final String ICEE_VERSION_DISPLAY_KEY = "icee_ver_display";
	@SuppressWarnings("unused")
	public static final Var ICEE_VERSION_DISPLAY = new Var(ICEE_VERSION_DISPLAY_KEY, iCee.VERSION + '-' + iCee.BUILD_VER);
	public static final String ICEE_BUILD_VER_KEY = "icee_build_ver";
	@SuppressWarnings("unused")
	public static final Var ICEE_BUILD_VER = new Var(ICEE_BUILD_VER_KEY, String.valueOf(iCee.BUILD_VER));
	public static final String ICEE_DEBUG_MODE_KEY = "icee_debug_mode";
	@SuppressWarnings("unused")
	public static final Var ICEE_DEBUG_MODE = new Var(ICEE_DEBUG_MODE_KEY, String.valueOf(iCee.DEBUG_MODE));
	
	public final String key;
	public final String value;
	
	public Var (String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public int compareTo (@NotNull Var var) {
		return this.key.compareTo(var.key);
	}
	
}
