這個Minecraft插件可以使你的Minecraft遊戲時間與世界時間同步！

巴哈上的文: https://forum.gamer.com.tw/C.php?bsn=18673&snA=187185&tnum=4&subbsn=14
# 使用方法
適用Minecraft版本: 1.16+

將插件的jar檔放至伺服器的plugins資料夾即可。
![folder](https://truth.bahamut.com.tw/s01/202010/ecca15193f6781bdebf2262609fa387b.PNG)

並且還要設定哪個世界需要同步時間。

開啟config.yml在world的底下寫上需要同步時間的世界名稱。

(每一個不同的世界名稱之間都要換行，然後每一行開頭都要有一個tab，再接一個-符號，然後才是世界名稱。如有不懂的可以參考其他Minecraft插件的config.yml寫法或是上網尋找yaml的書寫方式)

![world](https://truth.bahamut.com.tw/s01/202010/7007c97ce5861cf6182d14d0d65a9d7a.PNG)
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
