# std:: Architecture Standard

## 全局静态强制标准

### Pinenut 继承约束
- 新增的顶级接口必须 `extends Pinenut`
- 新增的顶级实现类、门面类、顶级接口、顶级类（final 除外），应显式实现对应接口，并纳入 `Pinenut` 体系
- 历史遗留代码不强制追改；新代码从本标准开始执行

### 顶级职责分层
- `*-arch` 只放抽象、协议、异常、领域接口

### JSON 处理标准
- 后续默认使用 `pinecone.json`，包名为 `com.pinecone.framework.util.json`
- 默认 JSON 对象类型使用 `JSONObject`
- 默认 JSON 数组类型使用 `JSONArray`
- 默认 JSON 对象实现使用 `JSONMaptron`
- JSON 文本解析默认直接使用 `new JSONMaptron( szJson )`
- `pinecone.json` 原生支持 `JSON5` 解析，不需要在进入 parser 前额外做注释剥离、引号修复或尾逗号预处理
- 后续新增 JSON / DESON parser，默认先解析为 `JSONObject` / `JSONArray`，再进入领域对象转换
- 后续新增 JSON / DESON decoder，不直接手拼 JSON 文本；应先构造 `JSONObject`，再统一调用 `toJSONStringI( 2 )` 输出格式化文本
- 若已有对象需要稳定字符串输出，优先使用 `toJSONStringI( 2 )`，不要引入第二套手写格式化规则
