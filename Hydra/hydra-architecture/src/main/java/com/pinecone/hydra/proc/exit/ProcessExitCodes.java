package com.pinecone.hydra.proc.exit;

public final class ProcessExitCodes {

    /**
     * The process finished successfully.
     */
    public static final int Success = 0;

    /**
     * The entry point threw an unhandled application exception.
     */
    public static final int UnhandledException = 70;

    /**
     * The runtime failed before a user-defined entry point result was produced.
     */
    public static final int RuntimeFailure = 71;

    /**
     * The process was interrupted by a cooperative interrupt signal.
     */
    public static final int Interrupted = 130;

    /**
     * The process was killed by a forceful kill signal.
     */
    public static final int KilledBySignal = 137;

    /**
     * The process was terminated by a graceful termination signal.
     */
    public static final int TerminatedBySignal = 143;

    private ProcessExitCodes() {
    }
}
