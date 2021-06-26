package cc.sukazyo.icee.system.module;

import cc.sukazyo.icee.iCee;
import cc.sukazyo.icee.module.Modules;
import cc.sukazyo.icee.system.Log;
import cc.sukazyo.icee.util.SimpleUtils;
import cc.sukazyo.icee.util.Var;
import org.apache.commons.collections4.map.LinkedMap;

import java.util.*;

public class ModuleManager {
	
	private static final LinkedMap<IModule, Class<?>> modules = new LinkedMap<>();
	
	/**
	 * 将一些模块注册进入 iCee 的模块管理器中<br/>
	 * <br/>
	 * <b>正常情况下你不需要调用此方法</b><br/>
	 * 如果你调用了此方法，注意目前的 iCee 的一些部分（例如{@link #getModulesDevelopmentTable()}）
	 * 可能仅支持 {@link cc.sukazyo.icee.module.Modules} 和 {@link cc.sukazyo.icee.system.module.AfferentModulesRegister}
	 * 等几个内置的模块注册器
	 *
	 * @param registrar 注册这个模块的注册器，模块管理器将会以此为模块进行分类
	 * @param moduleSet 需要注册的模块列表
	 */
	public static void register (Class<?> registrar, IModule... moduleSet) {
		for (IModule module : moduleSet) {
			ModuleManager.modules.put(module, registrar);
		}
	}
	
	/**
	 * 给已注册的模块发布模块初始化事件<br/>
	 * <br/>
	 * <font color="red"><b>你不应该调用此方法!</b></font>
	 */
	public static void initializeRegisteredModules () {
		modules.forEach((mod, reg) -> mod.initialize());
		Log.logger.info("All registered modules have been initialized");
		Log.logger.debug("Module List Output\n" + getModulesDevelopmentTable());
	}
	
	/**
	 * 返回格式化的注册模块表<br>
	 * <br>
	 * 注册模块表以 <u>| 注册名 - 版本号 - 类名 |</u> 进行排版<br>
	 * <br>
	 * 以不同方式进行注册的模块会被分类显示<br>
	 * icee 主程序以<i>特殊模块</i>的身份也会被列于此处<br>
	 * <br>
	 * <b>生成的样式预览：</b>
	 * <pre><code>
==============================================================================================
|                                  Registered iCee Modules                                   |
==============================================================================================
|  icee               | 0.3.1-dev-build31 | cc.sukazyo.icee.iCee                             |
|---------------------+-------------------+--------------------------------------------------|
|  icee_http_request  | ·                 | cc.sukazyo.icee.module.http.HttpListener         |
|  common_bot         | ·                 | cc.sukazyo.icee.module.bot.common.CommonBot      |
|  bot_mirai_qq       | ·                 | cc.sukazyo.icee.module.bot.mirai.MiraiBot        |
|  bot_discord        | ·                 | cc.sukazyo.icee.module.bot.discord.DiscordBot    |
|  bot_telegram       | ·                 | cc.sukazyo.icee.module.bot.telegram.TelegramBot  |
|---------------------+-------------------+--------------------------------------------------|
|  icee_test_support  | ·                 | cc.sukazyo.icee.iCeeTest                         |
|  local_tests        | null              | cc.sukazyo.test.Test$TestNode                    |
|---------------------+-------------------+--------------------------------------------------|
|  suka_cee           | 0.1.4             | cc.sukazyo.icee.module.sukpriv.SukaCee           |
==============================================================================================
	 * </code></pre>
	 *
	 * @return 生成的表格
	 */
	public static String getModulesDevelopmentTable () {
		
		// 数据表的排版数据初始化
		final int[] columnsMaxLength = new int[]{
				iCee.PACKID.length(),
				Var.ICEE_VERSION_DISPLAY.value.length(),
				iCee.class.getName().length()
		};
		final int columnsNamePaddingLeft = 2;
		final int columnsNamePaddingRight = 2;
		final int columnsVersionPaddingLeft = 1;
		final int columnsVersionPaddingRight = 1;
		final int columnsClasspathPaddingLeft = 1;
		final int columnsClasspathPaddingRight = 2;
		final List<IModule> buildInModules = new LinkedList<>();
		final List<IModule> afferentModules = new LinkedList<>();
		final List<IModule> externalModules = new LinkedList<>();
		
		// 遍历模块表更新排版数据
		modules.forEach((mod, reg) -> {
			if (reg == Modules.class) {
				buildInModules.add(mod);
			} else if (reg == AfferentModulesRegister.class) {
				afferentModules.add(mod);
				if (mod.getDisplayVersion().length() > columnsMaxLength[1]) columnsMaxLength[1] = mod.getDisplayVersion().length();
			} else {
				externalModules.add(mod);
				if (mod.getDisplayVersion().length() > columnsMaxLength[1]) columnsMaxLength[1] = mod.getDisplayVersion().length();
			}
			if (mod.getRegistryName().length() > columnsMaxLength[0])
				columnsMaxLength[0] = mod.getRegistryName().length();
			if (mod.getClass().getName().length() > columnsMaxLength[2])
				columnsMaxLength[2] = mod.getClass().getName().length();
		});
		
		// 输出表
		final int lineLen =
				columnsNamePaddingLeft + columnsMaxLength[0] + columnsNamePaddingRight +
				columnsVersionPaddingLeft + columnsMaxLength[1] + columnsVersionPaddingRight +
				columnsClasspathPaddingLeft + columnsMaxLength[2] + columnsClasspathPaddingRight +
				4;
		final StringBuilder table = new StringBuilder();
		final String breakTable = SimpleUtils.repeatChar('=', lineLen);
		table.append(breakTable).append('\n');
		final String title = "Registered iCee Modules";
		final int titleWhitespace = lineLen - 2 - title.length();
		final int titleWhitespaceHalf = titleWhitespace / 2;
		table
				.append('|')
				.append(SimpleUtils.repeatChar(' ', titleWhitespaceHalf))
				.append(title)
				.append(SimpleUtils.repeatChar(' ', titleWhitespace - titleWhitespaceHalf))
				.append('|')
				.append('\n')
		;
		table.append(breakTable).append('\n');
		final String breakRows =
				'|' +
		        SimpleUtils.repeatChar('-', columnsMaxLength[0] + columnsNamePaddingLeft + columnsNamePaddingRight) +
				'+' +
				SimpleUtils.repeatChar('-', columnsMaxLength[1] + columnsVersionPaddingLeft + columnsVersionPaddingRight) +
				'+' +
				SimpleUtils.repeatChar('-', columnsMaxLength[2] + columnsClasspathPaddingLeft + columnsClasspathPaddingRight) +
				'|';
		table.append(tableSimpleRow(
				iCee.PACKID,
				Var.ICEE_VERSION_DISPLAY.value,
				iCee.class.getName(),
				columnsMaxLength[0], columnsMaxLength[1], columnsMaxLength[2])
		).append('\n');
		table.append(breakRows).append('\n');
		buildInModules.forEach(mod -> table.append(tableSimpleRow(
				mod.getRegistryName(),
				"·",
				mod.getClass().getName(),
				columnsMaxLength[0], columnsMaxLength[1], columnsMaxLength[2])
		).append('\n'));
		if (!afferentModules.isEmpty()) {
			table.append(breakRows).append('\n');
			afferentModules.forEach(mod -> table.append(tableSimpleRow(
					mod.getRegistryName(),
					mod.getDisplayVersion(),
					mod.getClass().getName(),
					columnsMaxLength[0], columnsMaxLength[1], columnsMaxLength[2])
			).append('\n'));
		}
		if (!externalModules.isEmpty()) {
			table.append(breakRows).append('\n');
			externalModules.forEach(mod -> table.append(tableSimpleRow(
					mod.getRegistryName(),
					mod.getDisplayVersion(),
					mod.getClass().getName(),
					columnsMaxLength[0], columnsMaxLength[1], columnsMaxLength[2])
			).append('\n'));
		}
		table.append(breakTable);
		
		return table.toString();
	}
	
	private static String tableSimpleRow (String a, String b, String c, int aMax, int bMax, int cMax) {
		return "|  " +
				a +
				SimpleUtils.repeatChar(' ', aMax - a.length()) +
				"  | " +
				b +
				SimpleUtils.repeatChar(' ', bMax - b.length()) +
				" | " +
				c +
				SimpleUtils.repeatChar(' ', cMax - c.length()) +
				"  |"
		;
	}
	
}
