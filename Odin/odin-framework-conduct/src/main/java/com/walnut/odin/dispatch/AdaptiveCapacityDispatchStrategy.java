package com.walnut.odin.dispatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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

    @Override
    public Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> dispatch(
            Collection<TaskExecutionProcessor> processors,
            Collection<TaskLaunchContext> contexts
    ) {
        Map<TaskExecutionProcessor, Collection<TaskLaunchContext>> plan = new HashMap<>();

        if ( processors == null || processors.isEmpty() ) {
            return plan;
        }
        if ( contexts == null || contexts.isEmpty() ) {
            return plan;
        }

        Map<String, ProcessorSlot> slotMap = new HashMap<>();
        List<TaskExecutionProcessor> availableProcessors = new ArrayList<>();

        for ( TaskExecutionProcessor processor : processors ) {
            int nPending = processor.getTaskExecutionQueue().pendingCapacity();
            if ( nPending > 0 ) {
                ProcessorSlot slot = new ProcessorSlot( processor, nPending );
                slotMap.put( processor.getName(), slot );
                availableProcessors.add( processor );
            }
        }

        if ( slotMap.isEmpty() ) {
            return plan;
        }

        List<TaskLaunchContext> normalContexts = new ArrayList<>();

        // 先处理 affinity
        for ( TaskLaunchContext context : contexts ) {
            String szAffinity = context.getAffinityProcessorName();
            if ( szAffinity == null ) {
                normalContexts.add( context );
                continue;
            }

            ProcessorSlot slot = slotMap.get( szAffinity );
            if ( slot != null && slot.mnRemaining > 0 ) {
                plan.computeIfAbsent( slot.mProcessor, k -> new ArrayList<>() ).add( context );
                --slot.mnRemaining;
            }
            else {
                normalContexts.add( context );
            }
        }

        if ( normalContexts.isEmpty() ) {
            return plan;
        }

        // 再走普通调度
        if ( availableProcessors.size() <= this.mnHeapThreshold ) {
            this.dispatchLinear( slotMap, normalContexts, plan );
        }
        else {
            this.dispatchHeap( slotMap, normalContexts, plan );
        }

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

                if ( best == null || this.compareSlot( slot, best ) < 0 ) {
                    best = slot;
                }
            }

            if ( best == null ) {
                break;
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
            ProcessorSlot slot = heap.poll();
            if ( slot == null ) {
                break;
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
}