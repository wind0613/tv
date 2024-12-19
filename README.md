# 这是什么?
这是一款可自动抓取远程 m3u 列表的工具，它能够对列表中的频道进行分析，接着测试频道的可用性与延迟情况，最终解析出频道的视频和音频流信息存储到本地数据库里。

# 特性
- 自动抓取远程 m3u 列表。
- 自动将频道存储至本地数据库。
- 定时检测频道的可用性以及连接耗时。
- 定时解析频道的视频音频流信息。
- 支持在线管理所有频道。
- 提供 m3u 频道列表地址。

# 直播源
可以在直播软件里通过添加自定义直播源的方式添加这些地址来收看直播频道。

### 订阅 1 (自动更新)
```
所有频道：
[原始]
https://raw.githubusercontent.com/big-mouth-cn/m3u8-checker/main/iptv.m3u
[国内访问]
https://ghgo.xyz/https://raw.githubusercontent.com/big-mouth-cn/m3u8-checker/main/iptv.m3u

仅当前可用的频道：
[原始]
https://raw.githubusercontent.com/big-mouth-cn/m3u8-checker/main/iptv-ok.m3u
[国内访问] 
https://ghgo.xyz/https://raw.githubusercontent.com/big-mouth-cn/m3u8-checker/main/iptv-ok.m3u
```

### 订阅 2
本订阅需要你部署并运行这个项目，然后订阅该项目所在的服务器地址。
```
http://[服务器地址]:8080/m3u/list?justOk=1&withoutRepeat=1
```

**参数说明**
  - `justOk`：值 `1` 只显示可用频道。
  - `withoutRepeat`：值 `1` 过滤重复频道，保留视频分辨率最高的频道。重复的定义是：频道名称。

# 频道管理
可以在浏览器里通过访问 `http://[服务器地址]:8080/mgr.html` 来管理频道。

【频道列表】
![iShot_2024-12-19_11.38.55.png](docs%2FiShot_2024-12-19_11.38.55.png)

【编辑频道】
![iShot_2024-12-19_11.40.15.png](docs%2FiShot_2024-12-19_11.40.15.png)

【在线试播】
![iShot_2024-12-19_11.41.32.png](docs%2FiShot_2024-12-19_11.41.32.png)

**本工具仅供学习交流使用。**