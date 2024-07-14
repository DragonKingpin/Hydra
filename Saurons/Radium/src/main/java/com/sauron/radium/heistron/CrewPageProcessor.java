package com.sauron.radium.heistron;

import com.pinecone.framework.util.Debug;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class CrewPageProcessor implements PageProcessor {
    protected HTTPHeist  parentHeist;

    public CrewPageProcessor( HTTPHeist heist ) {
        this.parentHeist = heist;
    }

    @Override
    public void process( Page page ) {
        Request request = new Request("https://rednest.cn/index.html");
        request.putExtra("requestType", "temp");
        page.addTargetRequest( request );
        Debug.trace( "fuck", page.getHtml().toString() );
    }

    @Override
    public Site getSite() {
        return this.parentHeist.getSite();
    }
}