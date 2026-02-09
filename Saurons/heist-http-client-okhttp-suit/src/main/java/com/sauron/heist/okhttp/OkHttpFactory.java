package com.sauron.heist.okhttp;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.sauron.heist.heistron.Heistum;
import com.sauron.heist.http.HttpBrowserConf;

import okhttp3.OkHttpClient;

public interface OkHttpFactory extends Pinenut {

    List<OkHttpClient> make(HttpBrowserConf conf );

    List<OkHttpClient> make();

    List<OkClientConstructionScheme> makeScheme( HttpBrowserConf conf );

}
