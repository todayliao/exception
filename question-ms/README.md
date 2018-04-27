# question-ms

问答社区

# 发版历史

## V1.0.0

- UI 展示，包括问答的首页分页展示，首页热门标签展示，首页热门问题展示，问题详情展示，提交问题UI(完成)
- 功能：用户登录，提交问题(完成)

## V1.0.1

- UI 卡片化
- 添加问题编辑，方案编辑功能
- 替换 markdown 解析引擎, 提高解析速度，以及 fixed 部分冷格式解析错误的问题

## V1.0.2

- elasticsearch, 站内搜索功能

## V1.0.3

- 首页 VO 字段命名优化
- 网站模式确定，问答改为 问题方案，以维护某个问题的方案为主
- 搜索页面，相关度，点赞数，浏览数，最新搜索条件添加
- 相关问题异步请求显示功能
- html 中部分代码不遵守 bootstrap4 规范的代码重构

## V1.0.4

- UI 美化，参照 知乎 UI 和 stackoverflow UI 的设计, 特别是问题详情页的 UI (持续优化)
- 接入百度流量统计 TODO
- 结合 redis 实现问题被浏览数功能(防止恶意刷浏览数)

## V1.0.5

- 用户登录，更新最后一次登录时间
- 基本 SEO 功能实现，head 加入 keywords description meta 信息，供搜索引擎更好的抓取
- 线上 nginx 容器化
- 点赞功能，异步实现，spring security 返回 json 格式，非登录页格式
，并记录点赞的用户信息，以便显示 TODO


## V1.0.6

- 用户中心，展示用户相关信息 TODO
- 图片点击放大 TODO
- 除问题详情页外，其他页面的 SEO 优化 TODO
- 安全退出添加前置图标 TODO
- elasticsearch 容器化 TODO

## V1.0.7

- nginx 流量统计 TODO
- 日志切面改良，request 参数无法打印问题，lambada 表达式 TODO
- markdown editor 加载过慢的问题 TODO

## V1.0.8

- es 搜索，拓展词典，stop 词典 TODO
- 搜索页面，未搜索到内容，提示字添加 TODO
- 关于静态页面开发 TODO

## V1.0.9

- 用户维护日排行，周排行，总排行 TODO
- 登录页面 UI 美化 TODO


