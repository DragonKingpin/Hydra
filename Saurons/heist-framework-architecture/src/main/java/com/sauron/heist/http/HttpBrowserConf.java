package com.sauron.heist.http;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.homotype.DirectObjectInjector;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.tritium.util.ConfigHelper;
import com.sauron.heist.heistron.HeistConfigConstants;
import com.sauron.heist.heistron.Heistum;

public class HttpBrowserConf implements Pinenut {
    public enum ProxyStrategy {
        NoProxy,
        SystemOnly,
        ProxyGroup
    }

    protected Heistum    mParentHeistum;

    public JSONObject    protoConfig;
    public JSONArray     headConfigGroup;
    public boolean       agentConfusion;
    public JSONObject    systemProxy;
    public JSONArray     proxyGroup;
    public ProxyStrategy proxyStrategy     = ProxyStrategy.NoProxy;
    public boolean       enableRandomDelay = false;
    public int           randomDelayMin    = 0;
    public int           randomDelayMax    = 0;
    public int           socketTimeout     = 20000;
    public String        charset           = "UTF-8";
    public boolean       enableCookieJar   = true;


    // New improved V2

    // 是否跟随 HTTP 3xx 重定向
    public boolean followRedirects          = true;
    // 是否跟随 HTTPS → HTTPS / HTTP → HTTPS 重定向
    public boolean followSslRedirects       = true;
    // 是否在连接异常时自动重试（TCP 层）
    public boolean retryOnConnectionFailure = true;


    public int connectTimeout             = this.socketTimeout;   // ms
    public int readTimeout                = 20000;   // ms
    public int writeTimeout               = 20000;   // ms


    // 一般来说业务会自己限制，可开可不开
    public boolean enableRequestLimit     = false;
    // 全局最大并发请求数
    public int maxRequests                = 4096;
    // 单 Host 最大并发
    public int maxRequestsPerHost         = 512;

    // 连接池配置
    public boolean enableConnectionPool   = true;
    public int maxIdleConnections         = 5;
    public int keepAliveSeconds           = 300;

    public HttpBrowserConf( Heistum heistum ) {
        this.mParentHeistum   = heistum;
        JSONObject parentConf = this.mParentHeistum.getConfig();
        this.protoConfig      = parentConf.optJSONObject( HeistConfigConstants.KeyHttpBrowser );

        if( this.protoConfig == null ) {
            this.protoConfig = this.mParentHeistum.getHeistgram().getComponentsConfig().optJSONObject( HeistConfigConstants.KeyHttpBrowser ) ;
        }

        DirectObjectInjector.instance( ConfigHelper.fnToSmallHumpName, this.getClass() ).typeInject(
                this.protoConfig, this
        );
        this.proxyStrategy = ProxyStrategy.valueOf( this.protoConfig.optString( "ProxyStrategy" ) );
    }

}