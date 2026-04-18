这里是 std:: 标准资料库，面向本项目的基础架构的标准资料库

资料命名空间：std（./std/*），不要乱丢乱放

后面允许 append 模式增量添加新文件或 override 模式覆盖旧标准，override时需标记版本号和变更说明
变更形式语言：

@Revision( 
    datetime = 'yyyy-MM-dd HH:mm:ss', 
    author = '{{modelName/humanName}}', 
    why = 'T -> string' 
)