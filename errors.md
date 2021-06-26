# Error Code List

<br/>

## 使用中

<br/>

### iCee.exit(0);

> 正常情况下的退出码，
> 发生于：
> - 当 exit 命令执行信号被传入时 `cc.sukazyo.icee.system.command.core.CommandExit#execute(String[], Map<String, String>)`
> - 当 CLI 进程的工作结束时 `cc.sukazyo.icee.iCee#main()`

<br/>

### iCee.exit(9);

Command conflict occurred while registering core commands!

> 当核心命令（也就是 `help` `system` `stop` ）一类的命令在注册时出现命令名已经被占用的情况时，则会出现此问题
> 
> 这也是玄学错误之一，因为核心命令理论上是最早注册的，根本没有机会供别的模块来占用命令名
> 
> ~~（至少是正常发行版范围内？）~~
> 
> 发生于<br/>`cc.sukazyo.icee.system.command.CoreCommands#registerAll()`

<br/>

### iCee.exit(10);

Command Conflict when registering Built-in Module!

> 当核心命令（也就是 `help` `system` `stop` ）一类的命令在注册时出现命令名已经被占用的情况时，则会出现此问题
>
> 同样是玄学错误之一，因为内建模块的注册一定会早于外置模块（好吧尽管现在没有外置模块支持），
> 只有核心命令会有机会占用它的命令名，但是怎么想，这种问题都是过不去测试的
> 
> 发生于<br/>`cc.sukazyo.icee.module.Modules#registerModules()`

<br/>

### iCee.exit(11);

There is already an instance running on the directory.

> 已经有一个 iCee 实例在当前目录下运行
> 
> 当前已经有一个 iCee 实例占用了当前目录下的 iCee 实例锁，触发了 iCee 的单实例保护机制。 
> 
> 可以从运行目录下的 `.instance` 文件找到此目录下运行的 PID
> 
> 发生于<br/>`cc.sukazyo.icee.iCee#initializeAsSystemMode()`

<br/>

### iCee.exit(12);

LockFile generate failed: 

> 当系统生成锁文件时出现问题时触发
> 
> 这个问题可能来源于 iCee 程序没有足够的权限在此位置生成文件，也可能是某个程序抢先生成了锁文件。
> 
> 发生于<br/>`cc.sukazyo.icee.system.InstanceManager#generateLockFile()`

<br/>

### iCee.exit(13);

Generate instance information failed: 

> 当当前实例在向运行目录写入实例信息时出现错误时触发
> 
> 此错误大概率是由于 `.instance` 文件的写入权限缺失导致的，也可能是由于系统不支持 java 文件锁机制而出现的异常
> 
> 发生于<br/>`cc.sukazyo.icee.system.InstanceManager#lock()`

<br/>

### iCee.exit(14);

> 当出现无法被运行时程序捕获的未知错误时，iCee的入口方法将会捕获此错误并执行安全退出
> 
> 这个捕获是为了兼容 log 记录而写的。~~如果没有此捕获语句，未知错误将不会被记录于 Log 中，而是被 jvm 输出于控制台，这会导致在 log 文件中完全找不到错误日志~~（现在已经有`StdLogAdapter`了）
> 
> 发生于<br/>`cc.sukazyo.icee.iCee#main(String[])`

<br/>

### iCee.exit(15);

Error while loading localization data:

- Current language %s not found on language map while summon tree
- Too much language meta defined on %s
- The superior %s of %s is not a valid language
- The priority of %s is defined as a non-numerical or too large value %s

> 当系统初始化国际化组件的时候出现解析错误时抛出的异常
>
> 发生于<br/>`cc.sukazyo.icee.iCee#commonUtilsLoad()`

<br/>

### iCee.exit(16);

Some error occurred while loading system config!<br/>
EXCEPTION[`error-config-path`]::

> 当系统初始化/读取/生成配置文件时出现错误列表，
> 这个错误后面会跟随一到多个`EXCEPTION[error-config-path]::`的报错
> 
> 其中 `error-config-path` 是这个报错所属的配置文件的保存位置
> 
> 发生于<br/>
> `cc.sukazyo.icee.iCee#commonUtilsLoad()`

<br/>

### iCee.exit(17);

Config types in Core meets conflict!

> 当核心自带的配置节点类型注册时出现冲突的时候出现的异常。
> 
> 发生于<br/>
> `cc.sukazyo.icee.system.config.common.CommonConfigTypes#loadDefaultConfigTypes()`

<br/>

### iCee.exit(18);

Conflict occurred while registering core configures!

> 当核心所使用的配置文件的id或者保存路径被占用时出现的异常
> 
> 发生于<br/>
> `cc.sukazyo.icee.system.config.Configure#registerCoreConfigures()`

<br/>

<br/>

## 已废弃

<br/>

### ~~iCee.exit.(1);~~

> ~~可能是一个很早之前在哪里用过但是被删除了，开发者也没有印象的错误码~~
> 
> ~~这个错误码没有在代码中被搜索到，但是鉴于后面的错误码已经被使用了，所以开发者觉得使用它可能会造成冲突~~
> 
> ~~所以如果你发现程序返回了这个错误码，那就是出现玄学了，请一定要报告a！~~

<br/>

### iCee.exit(2);

Can't output config `iniFile` to the root dir!

> 当配置生成出现错误时触发
> 
> 可能在配置文件自动升级时或者初次开启程序生成配置文件时
> 
> 发生于<br/>`cc.sukazyo.icee.system.Conf#summonConf(String)`

<br/>

### iCee.exit(3);

Can't copy config `iniFile`

> 同样是当配置生成出现错误时触发
>
> 可能在配置文件自动升级时或者初次开启程序生成配置文件时
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#summonConf(String)`

<br/>

### iCee.exit(4);

Default Config File Not Found, might the package had benn broken!

> 当系统从包中读取默认配置文件时出现错误时触发
> 
> 鉴于普通用户并不会对程序文件本身作出修改，因此这个问题很可能是在程序打包或者传输时的错误导致的
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#load()`

<br/>

### iCee.exit(5);

en_us config template not found or out of date, might the package had benn broken!

> 当 iCee 程序包内找不到配置文件模板的本地化翻译时，将会使用默认的（英文）配置文件，而当英文配置文件也缺失时，将会触发此bug
> 
> 英文配置文件正常情况下是绝对会在包内存在的，同样，鉴于普通用户并不会对程序文件本身作出修改，因此这个问题很可能是在程序打包或者传输时的错误导致的
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#summonConf(String)`

<br/>

### iCee.exit(6);

Missing Config `key`!

> 当程序在进行配置文件检查时，如果发现有配置缺失则会触发
> 
> 报错会显示缺失了哪一个配置节点的值
> 
> iCee 目前并没有未定义的配置值使用默认值的设计，所以如果你（图方便?）删除了某一个配置节点声明的话，就会触发此错误
> 
> 由于并没有进行完善的测试，触发此错误的原因可能也会和默认配置文件有关，若发生此问题，还是要看情况考虑
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#load()`

<br/>

### iCee.exit(7);

> 当程序在进行配置文件检查时，如果配置的给予类型和要求类型不同则会发生此错误
> 
> 报错会显示具体配置节点和基于的类型和要求的类型
>
> 和上面的一样由于并没有进行完善的测试，触发此错误的原因可能也会和默认配置文件有关，若发生此问题，还是要看情况考虑
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#load()`

<br/>

### iCee.exit(8);

> 当程序在进行配置文件检查时，如果出现了解析错误，则会触发此报错
> 
> 一般是由于默认配置文件要求了一个代码中并不支持的配置类型
> 
> 又或者是真·配置文件解析错误
>
> 和上面的~~抑或是上面的上面的~~一样由于并没有进行完善的测试，触发此错误的原因可能也会和此处的说明有出入，
> 若发生此问题，还是要看情况考虑
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#load()`

<br/>

