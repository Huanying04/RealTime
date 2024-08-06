#该Readme已被转换为Simplified Chinese版本，如需Traditional Chinese版本請轉至https://github.com/Huanying04/RealTime
这个Minecraft插件可以使你的Minecraft游戏时间与世界时间同步！

巴哈上的文: https://forum.gamer.com.tw/C.php?bsn=18673&snA=187185&tnum=4&subbsn=14
# 使用方法
将插件的jar档放至服务器的plugins文件夹即可。
![folder](https://truth.bahamut.com.tw/s01/202010/ecca15193f6781bdebf2262609fa387b.PNG)

并且还要设置哪个世界需要同步时间。

进入游戏并输入以下指令
```
/realtime timezone set 时区名称
```
即可设置游戏同步真实世界时间的时区。

并且再输入
```
/realtime world add 世界名称
```
即可在该世界激活同步时间之功能。

或是输入
```
/realtime world addthisworld
```
即可将玩家所在世界激活同步时间功能。
# 指令功能
### /realtime
显示插件简介
### /realtime reload
Reload插件。
### /realtime timezone set <时区名称>
设置时区位置。
#### 使用方法示例：
**/realtime timezone set GMT+8** - 将时区设置为GMT+8。

**/realtime timezone set JST** - 将时区设置为JST。

**/realtime timezone set Asia/Shanghai** - 将时区设置为Asia/Shanghai。
### /realtime timezone get
显示现在设置的时区。
### /realtime world add <世界名称>
激活该世界同步时间之功能。
### /realtime world remove <世界名称>
禁用该世界同步时间之功能。
### /realtime world addthisworld
激活玩家所在世界同步时间功能。
### /realtime world removethisworld
禁用玩家所在世界同步时间功能。

# Placeholder API
`%realtime_timezone%` - 现在设置的时区。

# 介绍视频
[![我让Minecraft跟现实世界的时间同步了。](https://img.youtube.com/vi/4lTVCK_uFb0/0.jpg)](https://www.youtube.com/watch?v=4lTVCK_uFb0)
