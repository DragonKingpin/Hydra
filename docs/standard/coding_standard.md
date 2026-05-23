# 编码规范
本标准对 C/C++、Java、ECMAScript 都生效，以下是核心风格规范：

1. 不要省略任何 this，无论是函数调用还是成员变量访问等。
2. 不要省略任何括号，例如：
```java
    switch ( exp ) {
        case 1: {
            doSomething();
            break;
        }
        default: {
            doSomethingElse();
        }
    }
```
3. 除了泛型括号之外（<T>），其他所有括号前后都要有空格，例如：
3.1. if、for、while、switch 等控制流语句的括号前后都要有空格，例如：
```java
    if ( 2 * ( condition[ "key" ] + 4 ) ) {
        doSomething();
    }
```
4. 对于代码单行不超过140字符时，不要换行（字符串场景除外）
5. 对于 C/C++、Java使用如下匈牙利命名法（除非上下文禁用）：
5.1. 成员变量(基本数据类型需要叠加): mMember，mszString，mnNumber，mbFlag
5.2. 字符串: szString，const char*: lpszString
5.3. 任意数字（integer, decimal）: nNumber
5.4. 逻辑（bool）: bFlag
5.5. 结构体、对象: DataStruct dataStruct, mDataStruct（成员态） 全称即可，不用额外叠加
5.6. 指针: pDataStruct, lpDataStruct
5.7. 临时变量无须使用匈牙利命名法

6. 抽象类：
模板模式下强制使用：ArchClass（Archetypic Class）
其他场景下使用：AbstractClass
7. 对齐，例如：
```
    public class MyClass {
        private int     mMemberVariable     = XXX;
        private String  mszStringVariable   = XX ;
    }
    
    function() {
        var i  = 1234;
        var sz = "Hello World";
    }
    
```
8. 单词：
8.1 接口默认实现使用 Generic，除非明确给出。
8.2 getOrCreate 语义统一使用 affirm，如affirmPath( String szPath )
8.3 子类对基类映射实现，统一使用 evince，例如：
```java
    public interface ElementNode extends TaskTreeNode {
        default AppElement evinceAppElement() { return null; }
    
        default TaskElement evinceTaskElement() { return null; }
    }

    public interface TaskElement extends ElementNode {
        @Override
        default TaskElement evinceTaskElement() {
            return this;
        }
    }
```

9. Hydra内核项目，原则上禁用三目表达式。


