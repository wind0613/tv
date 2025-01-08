# 这是什么?
这是一款能够自动抓取远程直播源列表（m3u）的工具。它可以对抓取到的频道列表进行分析，测试每个频道的可用性与延迟，解析出视频和音频流信息，并将这些数据存储到本地数据库中。最终，这款工具会根据数据库中的频道信息整理出一个优化后的直播源列表，方便在直播软件中直接播放。

# 特性
- 自动抓取远程 m3u 列表，简化直播源收集流程。
- 将频道信息自动存储至本地数据库，便于后续管理和优化。
- 定时检测频道的可用性，并记录连接速度和延迟情况。
- 定期解析频道的视频和音频流信息，确保播放质量。
- 支持在线管理所有频道，实现高效的分类与维护。
- 提供整理后的直播源地址，方便直接在直播软件中使用。

# 直播源地址
可以在直播软件里通过添加自定义直播源的方式添加这些地址来收看直播频道。

### 订阅 1 (自动更新)

**所有频道：**
```
所有频道：
[原始]
https://raw.githubusercontent.com/big-mouth-cn/tv/main/iptv.m3u
[国内访问]
https://ghgo.xyz/https://raw.githubusercontent.com/big-mouth-cn/tv/main/iptv.m3u
```

:+1::smiley: **仅当前可用的频道：**
```
[原始]
https://raw.githubusercontent.com/big-mouth-cn/tv/main/iptv-ok.m3u
[国内访问] 
https://ghgo.xyz/https://raw.githubusercontent.com/big-mouth-cn/tv/main/iptv-ok.m3u
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