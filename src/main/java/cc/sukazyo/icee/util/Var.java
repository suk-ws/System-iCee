package cc.sukazyo.icee.util;

import cc.sukazyo.icee.iCee;

public class Var {
	
	public static final Var ICEE_PACKID = new Var("icee_pack_id", iCee.PACKID);
	public static final Var ICEE_VERSION = new Var("icee_ver", iCee.VERSION);
	public static final Var ICEE_VERSION_DISPLAY = new Var("icee_ver_display", iCee.VERSION + '-' + iCee.BUILD_VER);
	public static final Var ICEE_BUILD_VER = new Var("icee_build_ver", String.valueOf(iCee.BUILD_VER));
	public static final Var ICEE_DEBUG_MODE = new Var("icee_debug_mode", String.valueOf(iCee.DEBUG_MODE));
	
	public final String key;
	public final String value;
	
	public Var (String key, String value) {
		this.key = key;
		this.value = value;
	}
	
}
