# Error Code List

### System.exit(0);

> 正常情况下的退出码，
> 发生于：
> - 当主程序执行 exit/quit/stop 命令时 `cc.sukazyo.icee.util.ConsoleScanner#run()`
> - 当 CLI 进程的工作结束时 `cc.sukazyo.icee.iCee#main()`
> - 当程序第一次生成配置文件时自动结束等待用户进行配置  `cc.sukazyo.icee.system.Conf#load()`

### ~~System.exit.(1);~~

> ~~不要问，问就是开发者也不知道~~
> 
> ~~这个错误码没有在代码中被搜索到，但是鉴于后面的错误码已经被使用了，所以开发者觉得使用它可能会造成冲突~~
> 
> ~~所以如果你发现程序返回了这个错误码，那就是出现玄学了，请一定要报告a！~~

### System.exit(2);

Can't output config `iniFile` to the root dir!

> 当配置生成出现错误时触发
> 
> 可能在配置文件自动升级时或者初次开启程序生成配置文件时
> 
> 发生于<br/>`cc.sukazyo.icee.system.Conf#summonConf(String)`

### System.exit(3);

Can't copy config `iniFile`

> 同样是当配置生成出现错误时触发
>
> 可能在配置文件自动升级时或者初次开启程序生成配置文件时
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#summonConf(String)`

### System.exit(4);

Default Config File Not Found, might the package had benn broken!

> 当系统从包中读取默认配置文件时出现错误时触发
> 
> 鉴于普通用户并不会对程序文件本身作出修改，因此这个问题很可能是在程序打包或者传输时的错误导致的
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#load()`

### System.exit(5);

en_us config template not found or out of date, might the package had benn broken!

> 当 iCee 程序包内找不到配置文件模板的本地化翻译时，将会使用默认的（英文）配置文件，而当英文配置文件也缺失时，将会触发此bug
> 
> 英文配置文件正常情况下是绝对会在包内存在的，同样，鉴于普通用户并不会对程序文件本身作出修改，因此这个问题很可能是在程序打包或者传输时的错误导致的
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#summonConf(String)`

### System.exit(6);

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

### System.exit(7);

> 当程序在进行配置文件检查时，如果配置的给予类型和要求类型不同则会发生此错误
> 
> 报错会显示具体配置节点和基于的类型和要求的类型
>
> 和上面的一样由于并没有进行完善的测试，触发此错误的原因可能也会和默认配置文件有关，若发生此问题，还是要看情况考虑
>
> 发生于<br/>`cc.sukazyo.icee.system.Conf#load()`



### System.exit(8);

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

### System.exit(9);

Command conflict occurred while registering core commands!

> 当核心命令（也就是 `help` `system` `stop` ）一类的命令在注册时出现命令名已经被占用的情况时，则会出现此问题
> 
> 这也是玄学错误之一，因为核心命令理论上是最早注册的，根本没有机会供别的模块来占用命令名
> 
> ~~（至少是正常发行版范围内？）~~
> 
> 发生于<br/>`cc.sukazyo.icee.system.command.CoreCommands#registerAll()`

### System.exit(10);

Command Conflict when registering Built-in Module!

> 当核心命令（也就是 `help` `system` `stop` ）一类的命令在注册时出现命令名已经被占用的情况时，则会出现此问题
>
> 同样是玄学错误之一，因为内建模块的注册一定会早于外置模块（好吧尽管现在没有外置模块支持），
> 只有核心命令会有机会占用它的命令名，但是怎么想，这种问题都是过不去测试的
> 
> 发生于<br/>`cc.sukazyo.icee.module.Modules#registerModules()`
