package com.walnut.odin.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.walnut.odin.task.troll.LaunchFeature;

public class AdaptiveCapacityDispatchStrategy implements DispatchStrategy {

    protected static final int DEFAULT_HEAP_THRESHOLD = 16;

    protected final int mnHeapThreshold;

    protected static class ProcessorSlot {

        protected TaskExecutionProcessor mProcessor;
        protected int                    mnRemaining;

        protected ProcessorSlot( TaskExecutionProcessor processor, int nRemaining ) {
            this.mProcessor   = processor;
            this.mnRemaining  = nRemaining;
        }
    }

    public AdaptiveCapacityDispatchStrategy() {
        this( DEFAULT_HEAP_THRESHOLD );
    }

    public AdaptiveCapacityDispatchStrategy( int nHeapThreshold ) {
        this.mnHeapThreshold = nHeapThreshold > 0 ? nHeapThreshold : DEFAULT_HEAP_THRESHOLD;
    }

    protected Map<String, ProcessorSlot> buildProcessorSlots(
            Collection<TaskExecutionProcessor> processors, boolean bIncludeExclusive
    ) {
        Map<String, ProcessorSlot> slotMap = new HashMap<>();
        if ( processors == null || processors.isEmpty() ) {
            return slotMap;
        }

        for ( TaskExecutionProcessor processor : processors ) {
            if ( processor.isExclusive() && !bIncludeExclusive ) {
                continue;
            }

            int nPending = processor.getTaskExecutionQueue().pendingCapacity();
            if ( nPending <= 0 ) {
                continue;
            }
            ProcessorSlot slot = new ProcessorSlot( processor, nPending );
            slotMap.put( processor.getName(), slot );
        }
        return slotMap;
    }

    protected List<TaskLaunchContext> handleBindingContexts(
            Collection<TaskLaunchContext> contexts,
            Map<String, ProcessorSlot> slotMap,
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan,
            TaskDispatcher dispatcher
    ) throws TaskDispatchException {

        List<TaskLaunchContext> remaining = new ArrayList<>();

        for ( TaskLaunchContext context : contexts ) {
            String szTarget = null;
            boolean bStrong = false;

            LaunchFeature feature = context.getLaunchFeature();
            if ( feature != null && feature.getProcessorDesignated() != null ) {
                szTarget = feature.getProcessorDesignated();
                bStrong = true;
            }
            else {
                szTarget = context.getAffinityProcessorName();
                if ( szTarget == null ) {
                    TaskExecutionProcessor p = dispatcher.getAffinityTask( context );
                    if ( p != null ) {
                        szTarget = p.getName();
                    }
                }
            }

            if ( szTarget == null ) {
                remaining.add( context );
                continue;
            }

            ProcessorSlot slot = slotMap.get( szTarget );
            if ( slot == null ) {
                if ( bStrong ) {
                    throw new TaskDispatchException(
                            "Designated processor `" + szTarget + "` not available."
                    );
                }
                remaining.add( context );
                continue;
            }

            if ( slot.mnRemaining <= 0 ) {
                if ( bStrong ) {
                    throw new TaskDispatchException(
                            "Designated processor `" + szTarget + "` capacity exceeded."
                    );
                }
                remaining.add( context );
                continue;
            }

            if ( !this.canRun( slot, context ) ) {
                if ( bStrong ) {
                    throw new TaskDispatchException(
                            "Designated processor `" + szTarget + "` cannot run exec_arch `"
                                    + this.getExecArch( context ) + "`."
                    );
                }
                remaining.add( context );
                continue;
            }

            plan.computeIfAbsent( slot.mProcessor, k -> new ArrayList<>() ).add( context );
            --slot.mnRemaining;
        }

        return remaining;
    }

    protected void dispatchNormal(
            Map<String, ProcessorSlot> slotMap,
            List<TaskLaunchContext> contexts,
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan
    ) {
        if ( slotMap.size() <= this.mnHeapThreshold ) {
            this.dispatchLinear( slotMap, contexts, plan );
        }
        else {
            this.dispatchHeap( slotMap, contexts, plan );
        }
    }

    @Override
    public Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> dispatch(
            Collection<TaskExecutionProcessor> processors,
            Collection<TaskLaunchContext> contexts,
            TaskDispatcher dispatcher
    ) throws TaskDispatchException {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = new HashMap<>();

        if ( contexts == null || contexts.isEmpty() ) {
            return plan;
        }

        Map<String, ProcessorSlot> boundSlotMap = this.buildProcessorSlots( processors, true );
        Map<String, ProcessorSlot> normalSlotMap = this.buildProcessorSlots( processors, false );
        if ( normalSlotMap.isEmpty() ) {
            this.handleBindingContexts( contexts, boundSlotMap, plan, dispatcher );
            return plan;
        }

        List<TaskLaunchContext> remaining = this.handleBindingContexts( contexts, boundSlotMap, plan, dispatcher );
        if ( remaining.isEmpty() ) {
            return plan;
        }

        this.dispatchNormal( normalSlotMap, remaining, plan );
        return plan;
    }

    protected void dispatchLinear(
            Map<String, ProcessorSlot> slotMap,
            Collection<TaskLaunchContext> contexts,
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan
    ) {
        List<ProcessorSlot> slots = new ArrayList<>( slotMap.values() );

        for ( TaskLaunchContext context : contexts ) {
            ProcessorSlot best = null;
            for ( ProcessorSlot slot : slots ) {
                if ( slot.mnRemaining <= 0 ) {
                    continue;
                }

                if ( !this.canRun( slot, context ) ) {
                    continue;
                }

                if ( best == null || this.compareSlot( slot, best ) < 0 ) {
                    best = slot;
                }
            }

            if ( best == null ) {
                continue;
            }

            plan.computeIfAbsent( best.mProcessor, k -> new ArrayList<>() ).add( context );
            --best.mnRemaining;
        }
    }

    protected void dispatchHeap(
            Map<String, ProcessorSlot> slotMap,
            Collection<TaskLaunchContext> contexts,
            Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan
    ) {
        PriorityQueue<ProcessorSlot> heap = new PriorityQueue<>( this::compareSlot );
        for ( ProcessorSlot slot : slotMap.values() ) {
            if ( slot.mnRemaining > 0 ) {
                heap.offer( slot );
            }
        }

        for ( TaskLaunchContext context : contexts ) {
            List<ProcessorSlot> skipped = new ArrayList<>();
            ProcessorSlot slot = null;
            while ( !heap.isEmpty() ) {
                ProcessorSlot polled = heap.poll();
                if ( this.canRun( polled, context ) ) {
                    slot = polled;
                    break;
                }
                skipped.add( polled );
            }
            for ( ProcessorSlot skippedSlot : skipped ) {
                heap.offer( skippedSlot );
            }
            if ( slot == null ) {
                continue;
            }

            plan.computeIfAbsent( slot.mProcessor, k -> new ArrayList<>() ).add( context );

            --slot.mnRemaining;

            if ( slot.mnRemaining > 0 ) {
                heap.offer( slot );
            }
        }
    }

    protected int compareSlot( ProcessorSlot a, ProcessorSlot b ) {
        if ( a.mnRemaining != b.mnRemaining ) {
            return Integer.compare( b.mnRemaining, a.mnRemaining );
        }

        if ( a.mProcessor.getPriority() != b.mProcessor.getPriority() ) {
            return Integer.compare( b.mProcessor.getPriority(), a.mProcessor.getPriority() );
        }

        return a.mProcessor.getName().compareTo( b.mProcessor.getName() );
    }

    protected boolean canRun( ProcessorSlot slot, TaskLaunchContext context ) {
        if ( slot == null || slot.mProcessor == null || context == null ) {
            return false;
        }
        return ExecutionArchitects.canRun( slot.mProcessor.getExecCaps(), this.getExecArch( context ) );
    }

    protected String getExecArch( TaskLaunchContext context ) {
        if ( context == null || context.getTaskInstance() == null ) {
            return ExecutionArchitecture.ANY.name();
        }
        return context.getTaskInstance().getExecArch();
    }
}
