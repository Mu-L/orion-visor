<div align="center"><img src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/9/11/11e7e78e-2af0-4c68-9811-db8a4c4400f4.png" alt="logo" width="520" /></div>
<p style="margin-top: 12px" align="center"><b>【Dromara】 一款高颜值、现代化的自动化运维&轻量堡垒机平台。</b></p>
<p align="center">
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://app.codacy.com/gh/lijiahangmax/orion-visor/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
      <img src="https://app.codacy.com/project/badge/Grade/49eaab3a9a474af3b87e1d21ffec71c4" alt="quality" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://www.apache.org/licenses/LICENSE-2.0">
      <img src="https://img.shields.io/github/license/dromara/orion-visor" alt="License" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://github.com/dromara/orion-visor/releases">
      <img src="https://img.shields.io/github/v/release/dromara/orion-visor" alt="release" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://gitee.com/dromara/orion-visor/stargazers">
      <img src="https://gitee.com/dromara/orion-visor/badge/star.svg?theme=gvp" alt="star" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://gitee.com/dromara/orion-visor/members">
      <img src="https://gitee.com/dromara/orion-visor/badge/fork.svg?theme=gvp" alt="fork" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://github.com/dromara/orion-visor">
      <img src="https://img.shields.io/github/stars/dromara/orion-visor" alt="star" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://github.com/dromara/orion-visor">
      <img src="https://img.shields.io/github/forks/dromara/orion-visor" alt="star" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://gitcode.com/dromara/orion-visor">
      <img src="https://gitcode.com/dromara/orion-visor/star/badge.svg" alt="star"/>
    </a>
</p>

------------------------------

**`orion-visor`** 提供一站式自动化运维解决方案。

* **资产管理**：支持对资产进行分组，实现对主机、密钥和身份的统一管理和授权。
* **在线终端**：提供在线终端 SSH 服务，支持快捷命令、自定义快捷键和主题风格。
* **文件管理**：支持远程主机 SFTP 大文件的批量上传、下载和在线编辑等操作。
* **批量操作**：支持批量执行主机命令、多主机文件分发等功能。
* **计划任务**：支持配置 cron 表达式，定时执行主机命令。
* **安全可靠**：动态配置权限，记录用户操作日志，提供简单的审计功能。

## 演示环境

* 🔗 演示地址: [https://dv.orionsec.cn/](https://dv.orionsec.cn/)
* 🔏 演示账号: admin/admin
* ⭐ 体验后可以点一下 `star`
  这对我很重要! [github](https://github.com/dromara/orion-visor) [gitee](https://gitee.com/dromara/orion-visor) [gitcode](https://gitcode.com/dromara/orion-visor)
* 🌈 如果本项目对你有帮助请帮忙推广一下 让更多的人知道此项目!
* 🎭 演示环境部分功能不可用, 完整功能请本地部署!
* 📛 演示环境请不要随便删除数据!
* 📧 如果演示环境不可用请联系我!
* 📨 **作者已被毕(cai)业(yuan) 寻java高级内推 望京/5号/10号线 有坑位的联系我哦** 微信: `ljh1553488`

## 快速开始

```bash
# clone
git clone --depth=1 https://github.com/dromara/orion-visor
cd orion-visor
# 启动
docker compose up -d
# 等待后端服务启动后 (2min±) 访问 http://localhost:1081/
```

## 项目文档

* [文档地址](https://visor.dromara.org/)
* [安装文档](https://visor.dromara.org/quickstart/docker.html)
* [更新日志](https://visor.dromara.org/update/change-log.html)
* [操作手册](https://visor.dromara.org/operator/asset.html)
* [常见问题](https://visor.dromara.org/support/faq.html)

## 技术栈

* SpringBoot 2.7+
* Mysql 8+
* Redis 6+
* Vue3 3+
* Arco Design 2+

## 主要功能预览

#### 主机终端

![新建连接](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/aa7efb14-f2cc-4a6f-b96b-a47964ed8f79.png "新建连接")
![主机终端](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/66f121de-69b6-49f6-adc4-701a22d481c4.png "主机终端")
![sftp](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/f7a0d141-0ee0-484e-8ddb-24cad9ed2c03.png "sftp")
![主题设置](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/d6f37ab3-62d2-4c5e-a503-e76a1d5ddc8e.png "主题设置")

#### 批量执行

![批量执行](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/3effc2fc-56a5-498d-8dfb-0f4f3b8a4056.png "批量执行")

#### 批量上传

![批量上传任务](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/98240fa9-4056-4520-9034-290d1aa47d80.png "批量上传任务")

#### 计划任务

![计划任务详情](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/29/d5ee6f04-7b2c-42ba-a3b3-642587f40cce.png "计划任务详情")

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=dromara/orion-visor&type=Date)](https://star-history.com/#dromara/orion-visor&Date)

## 关于我

本人专注于使用 Java 和 Vue 进行全栈开发, 并在系统自动化运维方面拥有丰富开发的经验。如果您在这些领域有需求或遇到痛点, 请随时联系我, 并备注“合作”。

## 联系我

<img src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/10/11/cf72c603-3951-4171-95b4-06271dda1c80.jpg" alt="wx" width="628px"/>  

微信: ljh1553488  
QQ群: 755242157

📧 问题/加群微信备注: ops  
📧 合作/功能定制备注: 合作

## 支持一下

<img src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/5/17/a5351e59-294c-4bec-b767-1a44c362fcbc.jpg" alt="收款码" width="540px"/>  

🎁 为了项目能健康持续的发展, 我期望获得相应的资金支持, 你们的支持是我不断更新前进的动力!

## 免责声明

在使用本项目之前, 请确保您已经了解并同意相关的使用协议和隐私政策。[免责声明](https://github.com/dromara/orion-visor/blob/main/DISCLAIMER.md)

## License

本项目遵循 [Apache-2.0](https://github.com/dromara/orion-visor/blob/main/LICENSE) 开源许可证。

## Gitee 最有价值的开源项目 GVP

![GVP](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/10/24/8dd98b8d-9de5-44e6-86d3-04e27ec66123.jpg "GVP")

## GitCode 最有影响力的开源项目 G-Star

![GSTAR](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2025/5/29/d6c17f1c-eddd-408a-ba67-1052a3dad9e9.jpg "GSTAR")
