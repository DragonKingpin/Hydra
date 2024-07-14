package com.pinecone.summer;

import com.pinecone.framework.util.lang.ClassNameFetcher;
import com.pinecone.framework.util.lang.NamespaceCollector;
import com.pinecone.summer.prototype.Controller;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.ClassUtils;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.summer.prototype.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArchRouterDispatcher implements RouterDispatcher {
    public class RouterClass {
        public boolean isPagesionController = false;
        public boolean isSimpleController   = false;
        public RouterMapping routerMapping  = null;
        public Class<?>      antetype       = null;
    }

    public class RouterMethod {
        public RouterClass   parent         = null;
        public RouterMapping routerMapping  = null;
        public Method        antetype       = null;
    }

    protected ArchHostSystem             mArchHostSystem            ;
    protected JSONArray                  mRoutumPackageNSs          ;
    protected Map<String, RouterClass >  mDesignatedRouterClassMap  = new LinkedHashMap<>();
    protected Map<String, RouterMethod > mDesignatedRouterMethodMap = new LinkedHashMap<>();
    protected NamespaceCollector         mClassLoader               = new ClassNameFetcher();

    protected static String urlNormalize( String url ) {
        if( !url.startsWith( "/" ) ) {
            url = "/" + url;
        }
        return url;
    }

    protected static String[] fetchPaths( String[] urls, String szDefaultName ) {
        String[] paths = urls;
        if( paths.length == 0 ) {
            paths = new String[] { ArchRouterDispatcher.urlNormalize( szDefaultName ) };
        }
        else {
            String[] norPaths = new String[paths.length];
            for ( int i = 0; i < paths.length; i++ ) {
                String path = paths[i];
                norPaths[i] = ArchRouterDispatcher.urlNormalize( path );
            }
            paths = norPaths;
        }
        return paths;
    }

    protected void fetchRouterClass( String szRoutumPackageNS ) {
        List<String> classNames = this.mClassLoader.fetch( szRoutumPackageNS );
        if ( classNames != null ) {
            for ( String className : classNames ) {
                try {
                    className = className.substring( className.indexOf( szRoutumPackageNS ) );
                    Class<?> antetype = Thread.currentThread().getContextClassLoader().loadClass( className );
                    Annotation[] annotations = antetype.getAnnotations();

                    RouterClass routerClass          = new RouterClass();
                    routerClass.isPagesionController = ClassUtils.isAssignable( Pagesion.class, antetype );
                    routerClass.isSimpleController   = false;
                    routerClass.antetype             = antetype;
                    for( Annotation annotation : annotations ) {
                        if( annotation instanceof Controller) {
                            routerClass.isSimpleController = true;
                        }
                        else if( annotation instanceof RouterMapping ) {
                            routerClass.routerMapping = (RouterMapping)annotation;
                        }
                    }

                    if( routerClass.isPagesionController || routerClass.isSimpleController ) {
                        String[] paths = null;
                        if( routerClass.routerMapping != null ) {
                            RouterMapping routerMapping = routerClass.routerMapping;
                            String szDefaultName = antetype.getSimpleName();
                            if ( routerClass.isPagesionController ) {
                                szDefaultName = antetype.getSuperclass().getSimpleName();
                            }

                            paths = ArchRouterDispatcher.fetchPaths( routerMapping.value(), szDefaultName );
                            for ( String path : paths ) {
                                this.mDesignatedRouterClassMap.put( path, routerClass );
                            }
                        }
                        this.fetchRouterMethod( routerClass, paths, antetype );
                    }


                }
                catch ( ClassNotFoundException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void fetchRouterMethod( RouterClass routerClass, String[] parentPaths, Class<?> that ) {
        Method[] methods = that.getDeclaredMethods();
        for ( Method method : methods ) {
            Annotation[] annotations = method.getAnnotations();
            RouterMapping routerMapping = null;
            for( Annotation annotation : annotations ) {
                if( annotation instanceof RouterMapping ) {
                    routerMapping = (RouterMapping)annotation;
                    break;
                }
            }

            if( routerMapping != null ) {
                String szDefaultName = method.getName();
                String[] paths = ArchRouterDispatcher.fetchPaths( routerMapping.value(), szDefaultName );
                String[] finalPath;
                int nParentLen = 0;
                if( parentPaths != null ){
                    nParentLen = parentPaths.length;
                    finalPath = new String[ parentPaths.length * paths.length ];
                }
                else {
                    finalPath = new String[ paths.length ];
                }

                RouterMethod routerMethod = new RouterMethod();
                routerMethod.antetype = method;
                routerMethod.parent = routerClass;
                routerMethod.routerMapping = routerMapping;


                int k = 0;
                nParentLen = nParentLen == 0 ? 1 : nParentLen; // 1 for only children loop.
                for ( int i = 0; i < nParentLen; i++ ) {
                    for ( int j = 0; j < paths.length; j++ ) {
                        if( routerMapping.relative() && parentPaths != null ) {
                            finalPath[ k ] = parentPaths[i] + paths[j];
                        }
                        else {
                            finalPath[ k ] = paths[j];
                        }
                        this.mDesignatedRouterMethodMap.put( finalPath[k], routerMethod );
                        ++k;
                    }
                }
                Debug.trace( finalPath );
            }
        }

    }

    protected void fetchRouterMaps() {
        for( Object obj : this.mRoutumPackageNSs ) {
            String szNS = (String) obj;
            this.fetchRouterClass( szNS );
        }
    }

    public ArchRouterDispatcher( ArchHostSystem system ){
        this.mArchHostSystem      = system;
        this.mRoutumPackageNSs    = this.mArchHostSystem.getSystemConfig().optJSONArray( "RoutumPackageNSs" );
        this.fetchRouterMaps();
    }

    public JSONArray getRoutumPackageNSs() {
        return this.mRoutumPackageNSs;
    }

    public Object queryRoutum( String szURI ){
        RouterClass routerClass = this.mDesignatedRouterClassMap.get( szURI );
        if( routerClass == null ) {
            return this.mDesignatedRouterMethodMap.get( szURI );
        }
        return routerClass;
    }

    public Pagesion spawnPagesion( RouterClass routerClass, ArchConnection connection ) {
        Pagesion obj = null;
        try {
            try{
                Constructor<?> constructor = routerClass.antetype.getConstructor( ArchConnection.class );
                obj = (Pagesion) constructor.newInstance( connection );
            }
            catch ( NoSuchMethodException | InvocationTargetException e1 ){
                e1.printStackTrace();
            }
        }
        catch ( IllegalAccessException | InstantiationException e ){
            System.err.println( "Summon Compromised: [" + e.toString() + "]" );
        }
        return obj;
    }

    public Object spawnController( RouterClass routerClass ) {
        Object obj = null;
        try {
            try{
                Constructor<?> constructor = routerClass.antetype.getConstructor();
                obj = constructor.newInstance();
            }
            catch ( NoSuchMethodException | InvocationTargetException e1 ){
                e1.printStackTrace();
            }
        }
        catch ( IllegalAccessException | InstantiationException e ){
            System.err.println( "Summon Compromised: [" + e.toString() + "]" );
        }
        return obj;
    }


}
