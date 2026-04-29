# Import Standard

## 形式文法
@import <path>

wildcard fmt -> '*' 表示通配符，匹配当前目录下的所有文件，

e.g.: '@import /docs/standard/import_standard.md'
表示你需要级联递归向下把依赖文件一同加载。

核心说明：@import 文法必须位于文件头，其他位置必须强制性忽略。

## 导包形式
1.'./docs/' 下有一个 `import_root.md` 文件，表示入口点，从这个文件按导航路径做递归向下导入，且不要循环引入这个文件。
2.其他目录下如果遇到 `import_*.md` 文件，表示需要导入的文件，请导入。
3.`import_*.md`文件有系统命名法规则，严格服从形式文法：import_EXTRAINFO.TYPE（其中TYPE一般是 .md 或 .txt）

## 异常处理
1.如果文件导入不了，直接忽略就行，同时提示一下用户，不要暴力重试。
2.相对路径寻址，文档的 import 作用域为 /docs/，找不到时请从这个根目录寻址。