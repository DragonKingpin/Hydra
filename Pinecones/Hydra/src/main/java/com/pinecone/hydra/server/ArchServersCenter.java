package com.pinecone.hydra.server;

import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.system.ArchSystemCascadeComponent;
import com.pinecone.hydra.system.HyComponent;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.JSONMaptron;

import java.util.Map;

public abstract class ArchServersCenter extends ArchSystemCascadeComponent implements ServersCenter {
    protected JSONObject     serversConfig;

    protected JSONObject     nameMap;
    protected JSONObject     nickNameMap;

    public ArchServersCenter( Namespace name, Hydrarum system, HyComponent parent ) {
        super( name, system, system.getComponentManager(), parent );

        this.nameMap     = new JSONMaptron();
        this.nickNameMap = new JSONMaptron();
        this.loadConfig();
    }

    public ArchServersCenter( Hydrarum system, HyComponent parent ) {
        this( null, system, parent );
    }

    public ArchServersCenter( Hydrarum system ) {
        this( system, null );
    }

    protected abstract void   loadConfig() ;

    protected abstract Server newServer( JSONObject prototype ) ;

    protected void fetchAll() {
        for( Map.Entry<String, Object > skv : this.serversConfig.entrySet() ){
            JSONObject seg = (JSONObject) skv.getValue();

            for( Map.Entry<String, Object > seg_kv : seg.entrySet() ){
                Object v = seg_kv.getValue();
                if( v instanceof JSONObject ) {
                    JSONObject archy = (JSONObject) seg_kv.getValue();
                    archy.put( "Hierarchy", seg_kv.getKey() );
                    this.addServer( this.newServer( archy ) );
                }
                else if( v instanceof JSONArray) {
                    JSONArray archy = (JSONArray) seg_kv.getValue();
                    for ( int i = 0; i < archy.size(); i++ ) {
                        JSONObject each = archy.optJSONObject(i);
                        each.put( "Hierarchy", seg_kv.getKey() );
                        this.addServer( this.newServer( each ) );
                    }
                }
            }
        }
    }


    @Override
    public ServersCenter addServer( Server server ) {
        this.getNameMap().put( server.getName(), server );
        this.getNickNameMap().put( server.getNickName(), server );
        return this;
    }

    @Override
    public ServersCenter removeServer( Server server ) {
        this.getNameMap().remove( server.getName() );
        this.getNickNameMap().remove( server.getNickName() );
        return this;
    }

    @Override
    public JSONObject getNameMap() {
        return this.nameMap;
    }

    @Override
    public JSONObject getNickNameMap() {
        return this.nickNameMap;
    }
}
