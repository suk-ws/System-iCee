package cc.sukazyo.icee.util;

import java.util.*;

public class CommandHelper {
	
	public static String[] format (String com) {
		ArrayList<String> arr = new ArrayList<>();
		
		StringBuilder tmp = new StringBuilder();
		char[] coma = com.toCharArray();
		for (int i = 0; i < coma.length; i++) {
			if (coma[i] == ' ') {
				if (!tmp.toString().equals("")) { arr.add(tmp.toString()); }
				tmp.setLength(0);
			} else if (coma[i] == '"') {
				while (true) {
					i++;
					if (coma[i] == '"') {
						break;
					} else if (coma[i] == '\\' && (coma[i+1] == '"' || coma[i+1] == '\\')) {
						i++;
						tmp.append(coma[i]);
					} else {
						tmp.append(coma[i]);
					}
				}
			} else if (coma[i] == '\\' && (coma[i+1] == ' ' || coma[i+1] == '"' || coma[i+1] == '\\')) {
				i++;
				tmp.append(coma[i]);
			} else {
				tmp.append(coma[i]);
			}
		}
		if (!tmp.toString().equals("")) { arr.add(tmp.toString()); }
		tmp.setLength(0);
		
		String[] out = new String[arr.size()];
		arr.toArray(out);
		return out;
	}
	
}
