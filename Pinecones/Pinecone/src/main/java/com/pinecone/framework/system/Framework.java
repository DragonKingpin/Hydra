package com.pinecone.framework.system;

import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.functions.Function;
import com.pinecone.Pinecone;
import com.pinecone.framework.util.config.JSONSystemConfig;
import com.pinecone.framework.util.config.StartupCommandParser;
import com.pinecone.framework.util.io.Tracer;
import com.pinecone.framework.util.io.Tracerson;
import com.pinecone.framework.util.json.JSONException;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class Framework extends ArchProcessum implements Pinecore {
    public static final String  DEFAULT_MAIN_CONFIG_FILE_NAME = "config";

    // System properties
    protected JSONSystemConfig       mjoGlobalConfig           ;
    protected JSONSystemConfig       mjoSystemConfig           ;

    // Startup & Environment properties
    protected String                 mszMajorPackagePath       ; // The path of this class file.
    protected String                 mszRuntimeContextPath     ; // System startup command, the 'user.dir'.
    protected String                 mszRuntimePath            ; // System real runtime path.
    protected Map<String, String[]>  mStartupCommandMap        ;
    protected String[]               mStartupCommand           ;
    protected Thread                 mMainThread               ;
    protected InputStream            mIn = System.in           ;
    protected OutputStream           mOut = System.out         ;
    protected Tracer                 mConsole = new Tracerson();

    private   long                   mnBootTime                ;
    private   Function               mfnAfterGlobalExpCaught   = new Function() {
        @Override
        public Object invoke( Object... obj ) throws Exception {
            Framework.this.console().cerr( "Unhandled exception in \"" + Framework.this.getAffiliateThread().getName() + "\" : \n" );
            ( ( Throwable ) obj[0] ).printStackTrace();
            return null;
        }
    };

    private   ClassLoader            mGlobalClassLoader        ;


    protected void setStartupCommand( String[] args ) {
        if( args == null ) {
            args = new String[0];
        }
        this.mStartupCommand    = args;
        this.mStartupCommandMap = StartupCommandParser.DefaultParser.parse( args );
    }

    protected void dispatchStartupCommand() {

    }

    protected Thread searchMainThread() {
        Set<Thread > all = this.fetchAllProcessThreads();
        Thread main = null;
        Thread tid1 = null;
        for( Thread thread : all ) {
            if(
                    // The thread name can be modify, so it is hard to believe all those conditions are mismatched, Jesus! Who would ever do that...
                    thread.getName().equals( "main" ) && !thread.isDaemon() &&
                    Thread.currentThread().getThreadGroup().getName().equals( "main" ) &&
                    Thread.currentThread().getThreadGroup().getParent().getName().equals("system")
            ){
                main = thread;
            }

            if( thread.getId() == 1 ) {
                tid1 = thread;
            }
        }
        if( main == null ) {
            this.console().warn( "[PineconeLifecycle] [WARN] System main thread will using thread[id=1] as main thread." );
            main = tid1;
        }
        return main;
    }

    private File findDefaultConfigFile() {
        String szDefaultConfMajorPath  = Path.of( this.getRuntimePath(), Framework.DEFAULT_MAIN_CONFIG_FILE_NAME ).toString();
        String szDefaultConfFilePath   = szDefaultConfMajorPath + ".json5";
        File f = new File( szDefaultConfFilePath );
        if( f.exists() ) {
            return f;
        }

        szDefaultConfFilePath   = szDefaultConfMajorPath + ".json";
        f = new File( szDefaultConfFilePath );
        if( f.exists() ) {
            return f;
        }

        return null;
    }

    protected void loadConfig() {
        if( this.mjoGlobalConfig == null ) {
            File f = this.findDefaultConfigFile();
            if( f != null ) {
                try{
                    this.mjoGlobalConfig   = new JSONSystemConfig( this );
                    this.mjoGlobalConfig.apply( f );
                    this.mjoSystemConfig   = this.mjoGlobalConfig.getChild( "System" );
                }
                catch ( IOException | JSONException e ) {
                    this.handleIgnoreException( e );
                }
            }

            this.mjoGlobalConfig = new JSONSystemConfig( this );
            this.mjoSystemConfig = new JSONSystemConfig( this );
            this.mjoGlobalConfig.put( "System", this.mjoSystemConfig );
        }

        this.mExceptionRestartTime = this.getSystemConfig().optInt( "ExceptionRestartTime", 0 );
    }

    protected void onlyLoadTaskManager() {
        this.mTaskManager          = new GenericMasterTaskManager( this );
    }

    protected void init(){
        this.traceWelcomeInfo();

        this.mszMajorPackagePath   = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        this.mszRuntimeContextPath = System.getProperty("user.dir");
        this.mszRuntimePath        = this.mszRuntimeContextPath;
        this.mMainThread           = this.searchMainThread();
        this.mGlobalClassLoader    = this.mMainThread.getContextClassLoader();

        this.setThreadAffinity( Thread.currentThread() );
        this.loadConfig();

        this.onlyLoadTaskManager();
    }

    public Map<String, String[] > getStartupCommandMap() {
        return this.mStartupCommandMap;
    }

    public Framework(){
        this( new String[0], null, null );
    }

    public Framework( String[] args ){
        this( args, null, null );
    }

    public Framework( String[] args, String szName ){
        this( args, szName, null );
    }

    public Framework( String[] args, CascadeSystem parent ){
        this( args, null, parent );
    }

    public Framework( String[] args, String szName, CascadeSystem parent ){
        this( szName, parent );
        this.setStartupCommand( args );
        this.init();
    }

    public Framework( String szName, CascadeSystem parent ) {
        super( szName, parent );
    }


    public void registerPineExpCatcher( Function fn ){
        this.mfnAfterGlobalExpCaught = fn;
    }

    public long getBootTime(){
        return this.mnBootTime;
    }

    public long getRunTime(){ //This function is using to calculate program run time
        return System.currentTimeMillis() - this.mnBootTime;
    }

    public void traceRunTime() {
        System.out.print( String.format(
                "\n%s Runtime : %d /ms !\n", Pinecone.MY_PROGRAM_NAME, this.getRunTime()
        ) );
    }

    private void initCommit() throws Throwable {
        this.mnBootTime = System.currentTimeMillis();
    }

    protected Object invokeInitHandle(  Function fnInlet, Object...args  ) throws Exception {
        this.setStartupCommand( (String[]) (Object[])args );
        this.dispatchStartupCommand();
        int nRetNum = 0;

        try {
            this.initCommit();
            nRetNum = (int) fnInlet.invoke( args );

            if( Pinecone.S_DEBUG_MODE ){
                this.traceRunTime();
            }
        }
        catch ( Throwable throwable ){
            try{
                this.handleRootKillException( throwable );
            }
            catch ( RestartSignalException e ) {
                this.handleIgnoreException( e );
                return e;
            }
            nRetNum = -1;
        }

        return nRetNum;
    }

    public int init ( Function fnInlet, Object...args ) throws Exception {
        Object ret = null;
        while ( true ) {
            ret = this.invokeInitHandle( fnInlet, args );

            if( ! (ret instanceof RestartSignalException ) ) {
                return (int) ret;
            }
            else {
                RestartSignalException e = (RestartSignalException) ret;
                this.console().warn( String.format(
                        "[PineconeLifecycle] [WARN] System restart [Time: %s] [What:<%s>:%s]",
                        this.mExceptionRestartCount, e.getCause().getClass().getSimpleName(), e.getCause().getMessage()
                ));
                e.getCause().printStackTrace();
            }
        }

    }

    @Override
    public ClassLoader getGlobalClassLoader() {
        return this.mGlobalClassLoader;
    }

    @Override
    public void setGlobalClassLoader( ClassLoader classLoader ) {
        this.mGlobalClassLoader = classLoader;
    }

    public InputStream in(){
        return this.mIn;
    }

    public InputStream inSync(){
        this.mResourceLock.readLock().lock();
        try{
            return this.in();
        }
        finally {
            this.mResourceLock.readLock().unlock();
        }
    }

    public OutputStream out(){
        return this.mOut;
    }

    public PrintStream  pout(){
        try{
            return (PrintStream) this.mOut;
        }
        catch ( ClassCastException e ) {
            return new PrintStream( this.mOut );
        }
    }

    public OutputStream outSync(){
        this.mResourceLock.readLock().lock();
        try{
            return this.out();
        }
        finally {
            this.mResourceLock.readLock().unlock();
        }
    }


    @Override
    public Tracer console() {
        return this.mConsole;
    }

    public Tracer consoleSync() {
        this.mResourceLock.readLock().lock();
        Tracer tracer = this.console();
        this.mResourceLock.readLock().unlock();
        return tracer;
    }

    public Tracer setConsole( Tracer tracer ) {
        this.mResourceLock.writeLock().lock();
        this.mConsole = tracer;
        this.mResourceLock.writeLock().unlock();
        return this.mConsole;
    }

    public InputStream setIn( InputStream in ) {
        this.mResourceLock.writeLock().lock();
        this.mIn = in;
        this.mResourceLock.writeLock().unlock();
        return this.mIn;
    }

    public OutputStream setOut( OutputStream out ) {
        this.mResourceLock.writeLock().lock();
        this.mOut = out;
        this.mResourceLock.writeLock().unlock();
        return this.mOut;
    }

    protected void traceWelcomeInfo() { }

    @Override
    public CascadeSystem parentExecutum(){
        return (CascadeSystem)super.parentExecutum();
    }

    @Override
    public String[] getStartupCommand(){
        return this.mStartupCommand;
    }

    @Override
    public String getMajorPackagePath() {
        return this.mszMajorPackagePath;
    }

    @Override
    public String getRuntimeContextPath() {
        return this.mszRuntimeContextPath;
    }

    @Override
    public String getRuntimePath() {
        return this.mszRuntimePath;
    }

    @Override
    public void   setRuntimePath( String szRealRuntimePath ){
        this.mszRuntimePath = szRealRuntimePath;
    }

    @Override
    public JSONSystemConfig getGlobalConfig() {
        return this.mjoGlobalConfig;
    }

    @Override
    public JSONSystemConfig getSystemConfig() {
        return this.mjoSystemConfig;
    }

    @Override
    public CascadeSystem rootSystem(){
        CascadeSystem system = this.getParent();
        CascadeSystem root   = system;
        while ( true ) {
            if( system != null ){
                root   = system;
                system = system.getParent();
            }
            else {
                break;
            }
        }
        return root;
    }

    @Override
    public long getPrimaryId() {
        if( this.getId() == 0 ) {
            return this.getId();
        }

        CascadeSystem root   = this.rootSystem();

        if( root == null ) {
            this.console().warn( "[PineconeLifecycle] [WARN] Id of primary system should be always 0." );
            return this.getId();
        }

        return root.getPrimaryId();
    }

    @Override
    public CascadeSystem getParent(){
        return (CascadeSystem)this.mParentSystem;
    }

    @Override
    public Thread getProcessMainThread() {
        return this.mMainThread;
    }

    @Override
    public void handleLiveException( Exception e ) throws ProvokeHandleException {
        this.console().warn( e.toString() );
    }

    @Override
    public void handleAsynLiveException( Exception e ) throws ProvokeHandleException {

    }

    @Override
    public void handleAsynKillException( Exception e ) throws ProvokeHandleException {

    }

    // Lifecycle
    protected void handleRootKillException( Throwable e ) throws RestartSignalException {
        try{
            this.mfnAfterGlobalExpCaught.invoke( e );
        }
        catch ( Exception e1 ) {
            e = e1;
        }

        if( e instanceof InstantKillError ) {
            this.kill();
        }
        if( e instanceof Error ) {
            this.kill();
        }

        if( e instanceof Exception ) {
            if( this.mExceptionRestartCount < this.mExceptionRestartTime ) {
                ++this.mExceptionRestartCount;
                throw new RestartSignalException( e );
            }
            else {
                this.kill();
            }
        }

    }

    protected void beforeReluctantDeath() {

    }

    @Override
    public void  entreatLive() {
        this.beforeReluctantDeath();
    }

}
