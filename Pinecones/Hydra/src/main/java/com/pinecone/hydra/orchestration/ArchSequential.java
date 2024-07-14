package com.pinecone.hydra.orchestration;

import java.util.Iterator;
import java.util.List;

public abstract class ArchSequential extends ArchTransaction implements Sequential {
    ArchSequential() {
        super();
    }

    @Override
    public void reset() {
        this.getExertium().reset();
    }

    protected void execute_exertion( Exertion exertion ) {
        try {
            if( this.mExertionStartCB != null ) {
                this.mExertionStartCB.callback( exertion );
            }
            exertion.start();
            this.waiting_for_single_exertion( exertion );
            if( this.is_dfa_status_finished_check_required( exertion ) && !exertion.isFinished() ) {
                throw new UnfulfilledActionException( exertion );
            }

            if( this.mExertionEndCB != null ) {
                this.mExertionEndCB.callback( exertion );
            }
        }
        catch ( RuntimeException e ) {
            if( !this.getSeqExceptionNeglector().isNeglectException( e ) ){
                if( exertion.getIntegrityLevel() != IntegrityLevel.Strict ) {
                    if( exertion.isDefaultRollback() ) {
                        exertion.rollback();
                        // TODO: Notice for warning.
                    }
                    else {
                        throw e;
                    }
                }
            }
        }
    }

    protected void noticeAll( BranchNoticeException e ) {
        List<GraphNode > children = this.getChildren();
        for( GraphNode node : children ) {
            if( node instanceof Notifiable ) {
                ((Notifiable) node).notice( e );
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected Iterator<GraphNode > eval_jump_point( Object at ) throws InstantJumpOutBranchException {
        if( at instanceof Iterator<? > ) {
            return ( Iterator<GraphNode >) at;
        }
        else if( at instanceof Integer ) {
            int id = (int) at;
            int  i = 0;
            List<GraphNode > children = this.getChildren();
            Iterator<GraphNode >   it = children.iterator();

            if( id > children.size() ) {
                throw new IndexOutOfBoundsException( "Jump [Segment:" + id + "] is out of range." );
            }
            else if( id == children.size() ) { // Instant jump-out.
                throw new InstantJumpOutBranchException();
            }

            while ( it.hasNext() ) {
                if( i == id ) {
                    return it;
                }
                it.next();
                ++i;
            }
        }

        return null;
    }

    protected boolean invoke_process_controller( ProcessController controller ) {
        try{
            controller.call();
        }
        catch ( BranchNoticeException e ) {
            if( e.isNoticeAll() ) { // TODO: Notice designed.
                this.noticeAll( e );
            }
        }
        catch ( InstantJumpOutBranchException e ){
            return true;
        }
        catch ( BranchControlException e ){
            e.printStackTrace(); // TODO: BranchControlException for more precisely control granularity.
        }

        return false;
    }

    protected boolean do_process_controller  ( ProcessController controller, BranchContext context ) {
        context.doBreak = this.invoke_process_controller( controller );
        if( context.doBreak ) {
            return true;
        }

        if( controller instanceof BreakController ) {
            context.doBreak = true;
            return true;
        }
        else if( controller instanceof JumpController ) {
            try{
                JumpController jmp = ((JumpController) controller);
                context.jmpPoint   = this.eval_jump_point( jmp.getJumpPoint() );
                context.doBreak    = false;
                return true;
            }
            catch ( InstantJumpOutBranchException e ){
                context.doBreak = true;
                return true;
            }
        }

        throw new IllegalArgumentException( "ProcessController for Sequential can ONLY be [break, jump]" );
    }

    protected BranchContext init_branch_context() {
        BranchContext    context  = new BranchContext( true );

        return context;
    }

    /**
     * Waiting synchronized for all exertions which in pool .
     * Should overridden by Parallel.
     */
    protected void waiting_exertions_pool_synchronized() {

    }

    /**
     * Waiting for single exertion synchronized.
     * Should overridden by Parallel.
     */
    protected void waiting_for_single_exertion( Exertion exertion ) {

    }

    protected boolean is_dfa_status_finished_check_required( Exertion exertion ) {
        return true;
    }

    @Override
    public void start() {
        this.getExertium().intoStart();

        List<GraphNode > children = this.getChildren();
        BranchContext    context  = this.init_branch_context();

        while ( true ) {
            Iterator<GraphNode > iter;
            if( context.jmpPoint != null ) {
                iter              = context.jmpPoint;
                context.jmpPoint  = null;
            }
            else {
                iter              = children.iterator();
            }

            while ( iter.hasNext() ) {
                Exertion exertion = (Exertion) iter.next();

                if( exertion instanceof ProcessController ) {
                    if( this.do_process_controller( (ProcessController) exertion, context ) ){
                        break;
                    }
                }
                else {
                    this.execute_exertion( exertion );
                }

                //Debug.sleep( 100 );
                ++context.nIP;
            }

            if( context.doBreak ) {
                break;
            }
        }

        this.waiting_exertions_pool_synchronized();

        this.getExertium().intoFinished();
    }

    @Override
    public void terminate() {

    }

    @Override
    public void rollback() {

    }

    protected class BranchContext {
        public Iterator<GraphNode >   jmpPoint;
        public boolean                doBreak;
        public int                    nIP;

        BranchContext( boolean doBreak ) {
            this.doBreak = doBreak;
        }

        BranchContext() {
            this( true );
        }
    }
}
