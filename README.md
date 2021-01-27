這個Minecraft插件可以使你的Minecraft遊戲時間與世界時間同步！

巴哈上的文: https://forum.gamer.com.tw/C.php?bsn=18673&snA=187185&tnum=4&subbsn=14
# 使用方法
將插件的jar檔放至伺服器的plugins資料夾即可。
![folder](https://truth.bahamut.com.tw/s01/202010/ecca15193f6781bdebf2262609fa387b.PNG)

並且還要設定哪個世界需要同步時間。

進入遊戲並輸入以下指令
```
/realtime timezone set 時區名稱
```
即可設定遊戲同步真實世界時間的時區。

並且再輸入
```
/realtime world add 世界名稱
```
即可在該世界啟用同步時間之功能。

或是輸入
```
/realtime world addthisworld
```
即可將玩家所在世界啟用同步時間功能。
# 指令功能
### /realtime
顯示插件簡介
### /realtime reload
Reload插件。
### /realtime timezone set <時區名稱>
設定時區位置。
#### 使用方法範例：
**/realtime timezone set GMT+8** - 將時區設定為GMT+8。

**/realtime timezone set JST** - 將時區設定為JST。

**/realtime timezone set Asia/Taipei** - 將時區設定為Asia/Taipei。
### /realtime timezone get
顯示現在設定的時區。
### /realtime world add <世界名稱>
啟用該世界同步時間之功能。
### /realtime world remove <世界名稱>
禁用該世界同步時間之功能。
### /realtime world addthisworld
啟用玩家所在世界同步時間功能。
### /realtime world removethisworld
禁用玩家所在世界同步時間功能。

# Placeholder API
`%realtime_timezone%` - 現在設定的時區。
