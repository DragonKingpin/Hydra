package com.walnut.odin.formation.flow;

import com.walnut.odin.formation.strategy.FormationStrategy;
import com.walnut.odin.formation.strategy.FormationStrategyContext;
import com.walnut.odin.formation.strategy.FormationStrategyRuntime;

public class GenericFormationFlow implements FormationFlow {
    protected FormationStrategy        mStrategy;
    protected FormationProducer<?>     mProducer;
    protected FormationConsumer        mConsumer;
    protected FormationStrategyRuntime mRuntime;

    public GenericFormationFlow( FormationStrategy strategy, FormationStrategyRuntime runtime ) {
        this.mStrategy = strategy;
        this.mRuntime = runtime;
    }

    @Override
    public FormationStrategy strategy() {
        return this.mStrategy;
    }

    @Override
    public FormationProducer<?> producer() {
        return this.mProducer;
    }

    @Override
    public FormationConsumer consumer() {
        return this.mConsumer;
    }

    @Override
    public FormationStrategyRuntime runtime() {
        return this.mRuntime;
    }

    @Override
    public void prepare( FormationStrategyContext context ) {
        this.mStrategy.prepare( context, this.mRuntime );
        this.mProducer = this.mStrategy.formulateProducer( context, this.mRuntime );
        this.mConsumer = this.mStrategy.formulateConsumer( context, this.mRuntime );
    }

    @Override
    public void flow( FormationStrategyContext context ) {
        if ( this.mConsumer == null ) {
            this.prepare( context );
        }
        this.mConsumer.consume();
    }

    @Override
    public boolean isFinished() {
        return this.mStrategy.isFinished( null, this.mRuntime );
    }
}
