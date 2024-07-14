package com.pinecone.summer;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.summer.prototype.ModelEnchanter;
import com.pinecone.summer.prototype.Pagesion;
import com.pinecone.summer.prototype.Pageson;
import com.pinecone.summer.prototype.Wizard;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public abstract class ArchPageson extends ArchWizardum implements Pageson {
    protected JSONObject  mPageData                     =  null  ;
    protected boolean     mbGlobalEnchanter             =  false ;

    public ArchPageson( ArchConnection session ) {
        super( session );
        this.mPageData = new JSONMaptron();
        if( this instanceof Pagesion ){
            this.mbGlobalEnchanter = this.hasEnchanterTrait();
        }
        this.appendDefaultPageDate();
    }

    protected boolean hasEnchanterTrait() {
        Annotation[] annotations = this.getClass().getAnnotations();
        for( Annotation annotation : annotations ){
            if( annotation instanceof ModelEnchanter ){
                return ((ModelEnchanter) annotation).value();
            }
        }
        return false;
    }

    protected void appendDefaultPageDate(){
        this.mPageData.put( "PrototypeName", this.prototypeName() );
        this.mPageData.put( "szMainTitle", ((Wizard)this).getTitle() );
        this.mPageData.put( "szWizardRole", ((Wizard)this).getModularRole() );
    }

    public void forward ( ArchPageson that ) {
        this.mPageData = that.mPageData;
    }


    public JSONObject getPageData(){
        return this.mPageData;
    }

    public String toJSONString(){
        return this.mPageData.toString();
    }

    public void setEnchanterRole( boolean bRole ){
        this.mbGlobalEnchanter = bRole;
    }

    public boolean isEnchanter() {
        return this.mbGlobalEnchanter;
    }

    public void setRenderum( Method fnRenderum ) {

    }

    public void render() throws ServletException, IOException {
        if( this instanceof Pagesion && this.mbGlobalEnchanter ){
            this.writer().print( ((Pagesion)this).toJSONString() );
        }
    }
}
