package com.pinecone.hydra.orchestration;

public abstract class ArchLoop extends ArchSequential implements Loop {
    @Override
    protected BranchContext init_branch_context() {
        BranchContext    context  = new BranchContext();
        context.doBreak           = false;

        return context;
    }

    @Override
    protected boolean do_process_controller  ( ProcessController controller, BranchContext context ) {
        context.doBreak = this.invoke_process_controller( controller );
        if( context.doBreak ) {
            return true;
        }

        if( controller instanceof BreakController ) {
            context.doBreak = true;
            return true;
        }
        else if( controller instanceof ContinueController ) {
            return true;
        }
        else if( controller instanceof JumpController ) {
            try{
                JumpController jmp = ((JumpController) controller);
                context.jmpPoint   = this.eval_jump_point( jmp.getJumpPoint() );
                return true;
            }
            catch ( InstantJumpOutBranchException e ){
                context.doBreak = true;
                return true;
            }
        }

        throw new IllegalArgumentException( "ProcessController for Loop can ONLY be [break, continue, jump]" );
    }
}
