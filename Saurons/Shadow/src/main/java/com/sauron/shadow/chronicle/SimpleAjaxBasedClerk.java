package com.sauron.shadow.chronicle;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.auto.ArchSuggestation;
import com.pinecone.hydra.auto.ContinueException;
import com.pinecone.hydra.auto.Instructation;
import com.sauron.radium.heistron.HTTPCrew;
import com.sauron.radium.heistron.HTTPHeist;
import com.sauron.radium.heistron.orchestration.Instructations;

import java.time.LocalDateTime;
import java.util.List;

public class SimpleAjaxBasedClerk extends ArchClerk implements Clerk {
    protected JSONArray           mTasks;

    public SimpleAjaxBasedClerk( HTTPHeist heist, int id, JSONObject joConfig ){
        super( heist, id, joConfig, SimpleAjaxBasedClerk.class );

        this.mTasks                  = this.mConfig.optJSONArray( "Tasks" );
    }

    @Override
    public void toRavage() {
        Instructations.infoConformed( SimpleAjaxBasedClerk.this );
        for( Object o : this.mTasks ) {
            JSONObject jo       = (JSONObject) o;
            String szObjectName = jo.optString( "ObjectName" );
            String szApi        = jo.optString( "Api" );

            try{
                String szNewsIndex = this.getHTTPPage( szApi ).getRawText();
                JSONObject tmp     = new JSONMaptron( szNewsIndex );
                this.parentHeist().getBasicChronicleManipulator().insertOneNews(
                        this.mszNewsDataTable, szObjectName, this.nowDateTime(), StringUtils.addSlashes( tmp.toJSONString() )
                );
                //Debug.trace( this.getHTTPPage( szApi ).getRawText() );
            }
            catch ( Exception e ) {
                SimpleAjaxBasedClerk.this.tracer().warn(
                        String.format("[Fatality] (%s::%s : %s) <Continue>", szObjectName, e.getClass().getSimpleName(), e.getMessage())
                );
            }
        }
    }

    @Override
    public Instructation getPrimeDirective() {
        return this.mAffinityPrimeDirective;
    }
}
