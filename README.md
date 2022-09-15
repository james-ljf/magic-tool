# magic-tool


[![OSCS Status](https://www.oscs1024.com/platform/badge/james-ljf/magic-tool.svg?size=small)](https://www.oscs1024.com/project/james-ljf/magic-tool?ref=badge_small)

#### 介绍
工具SDK


#### 使用说明
通过依赖引入
~~~
<dependency>
  <groupId>org.example</groupId>
  <artifactId>magic-tool</artifactId>
  <version>1.0.0</version>
</dependency>
~~~

配置maven的settins.xml
~~~
<activeProfiles>
    ...
    <activeProfile>github-lisi-test</activeProfile>
  </activeProfiles>

  <profiles>
    ...
    <profile>
      <id>tools</id>
      <repositories>
        <repository>
          <id>tools</id>
          <!--  https://maven.pkg.github.com/OWNER/REPOSITORY 替换后的url, 即实际的仓库地址 -->
          <url>https://maven.pkg.github.com/tools/magic-tool</url>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    ...
    <server>
      <id>tools</id>
      <!-- 访问该仓库的用户或组织帐户的名称 -->
      <username>USERNAME</username>
      <!-- 访问依赖的TOKEN -->
      <password>TOKEN</password>
    </server>
  </servers>
~~~

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
