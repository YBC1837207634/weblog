package com.gong.weblog;

import cn.hutool.core.util.IdUtil;
import com.gong.weblog.entity.Tag;
import com.gong.weblog.entity.User;
import com.gong.weblog.mapper.TagMapper;
import com.gong.weblog.mapper.UserMapper;
import com.gong.weblog.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
class WeblogApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TagMapper tagMapper;

    @Test
    void contextLoads() {
//        User user = new User();
//        user.setAccount("admin");
//        user.setPassword("123456");
//        userMapper.insert(user);
//        List<User> users = userService.list();
//        System.out.println(users);

        List<String> tags = List.of(
                ".NET",
        "1年工作经验",
                "211本",
                "211硕",
                "2年工作经验",
                "3年工作经验",
                "4年工作经验",
                "51CTO",
                "58同城",
                "985本",
                "985硕",
                "Android",
                "Angular",
                "Apache",
                "API接口",
                "APP",
                "Arduino",
                "Bootstrap",
                "Bug",
                "B站",
                "C",
                "C#",
                "C++",
                "Canvas",
                "CentOS",
                "ChatGPT",
                "CSDN",
                "CSS",
                "CTF",
                "C语言",
                "DevOps",
                "DIY",
                "Elasticsearch",
                "Es6",
                "ESP32",
                "ESP8266",
                "Git",
                "Gitee",
                "GitHub",
                "Go",
                "Hive",
                "HTML",
                "Http",
                "IOS",
                "Java 基础",
                "Javascript",
                "JQuery",
                "JSON",
                "JVM",
                "LeetCode",
                "Linux",
                "Live2D",
                "Logo",
                "MongoDB",
                "MySQL",
                "NLP",
                "NodeJS",
                "Objective-C",
                "OJ",
                "Oracle",
                "PHP",
                "PixelMe",
                "PPT",
                "PWN",
                "React",
                "Redis",
                "Ruby",
                "Rust",
                "Shell",
                "Shopee",
                "Spring",
                "Springcloud",
                "SQL",
                "Svelte",
                "Swift",
                "UI",
                "vivo",
                "W3Cschool",
                "一年",
                "一本",
                "三年",
                "专科",
                "书籍",
                "云原生",
                "云开发",
                "云服务",
                "云计算",
                "互联网",
                "五年",
                "五年以上",
                "交互",
                "交流社区",
                "产品",
                "产品设计",
                "京东",
                "人工智能",
                "代码",
                "代码托管",
                "代码编辑",
                "伙伴匹配系统",
                "低代码",
                "作品",
                "作图",
                "信息安全",
                "像素画肖像",
                "入门",
                "公众号",
                "内推",
                "写作",
                "分布式",
                "创业",
                "创作专栏",
                "创作者",
                "前端",
                "办公",
                "加密转码",
                "动态规划",
                "动画",
                "包管理",
                "区块链",
                "华为",
                "协作",
                "单曲",
                "博客",
                "博客园",
                "博客系统",
                "压缩",
                "可视化",
                "后端",
                "响应式",
                "商城系统",
                "图像处理",
                "图像视觉",
                "图床",
                "图库",
                "图形学",
                "图标",
                "图片",
                "图解",
                "外包",
                "多线程",
                "大三",
                "大二",
                "大前端",
                "大四",
                "大学生",
                "大数据",
                "奇虎360",
                "字体",
                "字符串",
                "字节跳动",
                "学习建议",
                "学习路线",
                "学术研究",
                "安全",
                "实习",
                "实战",
                "容器",
                "富文本",
                "小工具",
                "小程序",
                "小米",
                "小红书",
                "工具",
                "底层原理",
                "建站",
                "开发",
                "开发平台",
                "开发规范",
                "开源",
                "开源中国",
                "微服务",
                "微软",
                "快手",
                "思否",
                "性能",
                "性能优化",
                "慕课网",
                "打包构建",
                "技术",
                "技术专家",
                "技术团队",
                "招聘",
                "拼多多",
                "掘金",
                "接口文档",
                "插件",
                "插画",
                "搜索",
                "搜索引擎",
                "携程",
                "摄影",
                "播放器",
                "操作系统",
                "效率",
                "教程",
                "数字货币",
                "数学",
                "数据",
                "数据分析",
                "数据库",
                "数据挖掘",
                "数据科学",
                "数据结构",
                "文件",
                "文档",
                "文章",
                "新浪",
                "新闻资讯",
                "日志",
                "时间处理",
                "有赞",
                "服务器",
                "本科",
                "架构",
                "树",
                "树莓派",
                "校招",
                "格式转换",
                "框架",
                "模板",
                "歌单",
                "正则表达式",
                "比特币",
                "比赛",
                "求职",
                "求资源",
                "活动",
                "测试",
                "海外硕",
                "游戏",
                "滴滴",
                "爬虫",
                "牛客",
                "物联网",
                "猿辅导",
                "生活",
                "用户中心",
                "电商项目",
                "电子书",
                "百度",
                "监控统计",
                "知乎",
                "知识库",
                "研一",
                "研三",
                "研二",
                "硕士",
                "社区系统",
                "社招",
                "福利",
                "秋招",
                "竞赛",
                "竞赛证书",
                "笔记",
                "简历",
                "算法",
                "算法工程师",
                "管理系统",
                "类库",
                "系统",
                "系统设计",
                "素材",
                "索引",
                "线程",
                "练习",
                "组件",
                "组件库",
                "经验分享",
                "综合门户",
                "缓存",
                "编程",
                "网易",
                "网易云",
                "网站",
                "网站分析",
                "网络",
                "网络协议",
                "网络安全",
                "网页",
                "网页分析",
                "美团",
                "翻译",
                "考研",
                "考试",
                "职场",
                "职场进阶",
                "腾讯",
                "自我介绍",
                "自我修养",
                "色彩搭配",
                "英语",
                "菜鸟教程",
                "蓝桥杯",
                "视频",
                "计算机基础",
                "计算机网络",
                "计算机视觉",
                "论文",
                "设计",
                "设计模式",
                "证书",
                "谷歌",
                "资源",
                "赚钱",
                "路线",
                "转码",
                "软件",
                "软件开发",
                "软考",
                "运维",
                "运营",
                "进程",
                "递归",
                "镜像",
                "问答",
                "阿里",
                "阿里巴巴",
                "随笔",
                "集合",
                "面经",
                "面试",
                "面试题",
                "面试题挑战",
                "面试题挑战记录",
                "面试题解",
                "音乐",
                "项目",
                "顺丰",
                "高并发"
    );

        for(int i = 1 ; i <= tags.size(); ++i) {
            Tag tag = new Tag();
            tag.setTagName(tags.get(i-1));
            tag.setCreateBy(1L);
            tag.setUpdateBy(1L);
            tag.setVisible(0);
            tag.setSortNum(i);
            tagMapper.insert(tag);
        }

//        for (int i = 0; i < 10; ++i) {
//            long snowflakeNextId = IdUtil.getSnowflakeNextId();
//            log.info("{}", snowflakeNextId);
//        }
    }

}
