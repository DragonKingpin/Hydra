package com.auto_proc;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.proc.ProcessManager;
import com.pinecone.hydra.proc.UProcess;
import com.pinecone.hydra.proc.UProcessStatus;
import com.pinecone.hydra.proc.event.ProcessEventHandler;
import com.pinecone.hydra.proc.image.ArchEntryPointRunnable;
import com.pinecone.hydra.proc.image.EntryPointRunnable;
import com.pinecone.hydra.proc.image.ExecutionImage;
import com.pinecone.hydra.proc.image.LocalHostedClassImage;
import com.walnut.archcraft.ender.EnderHydra;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;

class CodexExecutionImagePrototype30Rig extends EnderHydra {

    protected static final int PROCESS_COUNT = 30;
    protected static final long READY_TIMEOUT_MILLIS = 10000L;

    public CodexExecutionImagePrototype30Rig( String[] args, CascadeSystem parent ) {
        super( args, "CodexExecutionImagePrototype30Rig", parent );
    }

    @Override
    public void vitalize() throws Exception {
        ProcessManager manager = this.processManager();
        ConcurrentMap<String, String> expectedPidMap = new ConcurrentHashMap<>();
        Set<String> observedPidSet = ConcurrentHashMap.newKeySet();
        List<String> failures = new CopyOnWriteArrayList<>();
        CountDownLatch doneLatch = new CountDownLatch( PROCESS_COUNT );

        ProcessEventHandler eventHandler = new ProcessEventHandler() {
            @Override
            public void fired( EntryPointRunnable runnable, UProcessStatus event ) {
                Debug.bluef( "[CodexExecutionImagePrototype30] Image event:", event );
            }
        };

        ExecutionImage imagePrototype = new LocalHostedClassImage( "codex_prototype_30", new ArchEntryPointRunnable( eventHandler ) {
            @Override
            public int main( Map<String, String> args ) {
                try {
                    String index = args.get( "index" );
                    String expectedPid = expectedPidMap.get( index );
                    String actualPid = this.ownedProcess().getPID().toString();
                    if ( !actualPid.equals( expectedPid ) ) {
                        failures.add( "index=" + index + ", expected=" + expectedPid + ", actual=" + actualPid );
                    }
                    observedPidSet.add( actualPid );
                    return 0;
                }
                finally {
                    doneLatch.countDown();
                }
            }
        }, manager );

        List<UProcess> processes = new ArrayList<>();
        for ( int i = 0; i < PROCESS_COUNT; ++i ) {
            String index = String.valueOf( i );
            Map<String, String> startupArgs = new HashMap<>();
            startupArgs.put( "index", index );

            UProcess process = manager.createLocalHostedProcessPrototypically(
                    imagePrototype, manager.getRootUProcess(), startupArgs, null
            );
            expectedPidMap.put( index, process.getPID().toString() );
            processes.add( process );

            if ( process.getExecutionImage() == imagePrototype ) {
                failures.add( "process image was not cloned: " + process.getPID() );
            }
            if ( process.getExecutionImage().getEntryPoint() == imagePrototype.getEntryPoint() ) {
                failures.add( "process entry point was not cloned: " + process.getPID() );
            }
        }

        ExecutorService starter = Executors.newFixedThreadPool( PROCESS_COUNT );
        try {
            for ( UProcess process : processes ) {
                starter.submit( process::start );
            }
            starter.shutdown();
            if ( !doneLatch.await( READY_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS ) ) {
                throw new IllegalStateException( "Timed out waiting for prototype concurrency processes." );
            }
            if ( !starter.awaitTermination( READY_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS ) ) {
                throw new IllegalStateException( "Timed out waiting for process starters." );
            }
        }
        finally {
            starter.shutdownNow();
        }

        await( "local process cleanup", READY_TIMEOUT_MILLIS, () -> {
            for ( UProcess process : processes ) {
                if ( manager.containProcess( process.getPID() ) && !process.getStatus().isTerminal() ) {
                    return false;
                }
            }
            return true;
        } );

        if ( observedPidSet.size() != PROCESS_COUNT ) {
            failures.add( "observed pid count=" + observedPidSet.size() + ", expected=" + PROCESS_COUNT );
        }
        if ( !failures.isEmpty() ) {
            throw new IllegalStateException( "Prototype concurrency check failed: " + failures );
        }
        Debug.greenfs( "[CodexExecutionImagePrototype30] Prototype concurrency check passed." );
    }

    protected static void await( String what, long timeoutMillis, BooleanSupplier condition ) {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while ( System.currentTimeMillis() < deadline ) {
            if ( condition.getAsBoolean() ) {
                return;
            }
            LockSupport.parkNanos( TimeUnit.MILLISECONDS.toNanos( 50 ) );
        }
        throw new IllegalStateException( "Timed out waiting for " + what + " after " + timeoutMillis + " ms." );
    }
}

public class CodexExecutionImagePrototype30Test {

    //@Test
    public void prototypeConcurrency30() throws Exception {
        Path workingPath = repositoryWorkingDirectory();
        if ( workingPath == null ) {
            throw new IllegalStateException( "Unable to locate repository working directory." );
        }

        List<String> command = new ArrayList<>();
        command.add( Paths.get( System.getProperty( "java.home" ), "bin", "java" ).toString() );
        command.add( "-cp" );
        command.add( testClasspath() );
        command.add( CodexExecutionImagePrototype30Test.class.getName() );
        command.add( "--workingPath=" + workingPath );
        command.add( "--config=" + workingPath.resolve( "system/setup/config.json5" ) );

        Path logPath = workingPath.resolve( "Sparta/sparta-core-console/target/codex-execution-image-prototype-30.log" );
        Process process = new ProcessBuilder( command )
                .directory( workingPath.toFile() )
                .redirectOutput( logPath.toFile() )
                .redirectErrorStream( true )
                .start();
        int exitCode = process.waitFor();
        if ( exitCode != 0 ) {
            throw new IllegalStateException( "Codex execution image prototype child process exited with code " + exitCode + ". See " + logPath );
        }
    }

    public static void main( String[] args ) throws Exception {
        Path workingPath = repositoryWorkingDirectory();
        String[] startupArgs = args;
        if ( workingPath != null ) {
            System.setProperty( "user.dir", workingPath.toString() );
            if ( startupArgs == null || startupArgs.length == 0 ) {
                startupArgs = new String[] {
                        "--workingPath=" + workingPath,
                        "--config=" + workingPath.resolve( "system/setup/config.json5" )
                };
            }
        }
        final String[] rigArgs = startupArgs;
        int exitCode = Pinecone.init( ( Object... cfg ) -> {
            CodexExecutionImagePrototype30Rig rig = (CodexExecutionImagePrototype30Rig) Pinecone.sys().getTaskManager().add(
                    new CodexExecutionImagePrototype30Rig( rigArgs, Pinecone.sys() )
            );
            rig.vitalize();
            return 0;
        }, (Object[]) rigArgs );
        System.exit( exitCode );
    }

    protected static Path repositoryWorkingDirectory() {
        Path cursor = Paths.get( System.getProperty( "user.dir" ) ).toAbsolutePath();
        while ( cursor != null ) {
            if ( Files.exists( cursor.resolve( "system/setup/config.json5" ) ) ) {
                return cursor;
            }
            cursor = cursor.getParent();
        }
        return null;
    }

    protected static String testClasspath() {
        String cp = System.getProperty( "surefire.test.class.path" );
        if ( cp == null || cp.isEmpty() ) {
            cp = System.getProperty( "java.class.path" );
        }
        return cp;
    }
}
