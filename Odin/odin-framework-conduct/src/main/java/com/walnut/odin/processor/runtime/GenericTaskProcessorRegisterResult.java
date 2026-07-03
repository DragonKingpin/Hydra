package com.walnut.odin.processor.runtime;

import com.walnut.odin.dispatch.entity.TaskProcessorEntity;
import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;
import com.walnut.odin.processor.event.TaskProcessorEstablishment;
import com.walnut.odin.processor.runtime.TaskProcessorRegisterResult;

public class GenericTaskProcessorRegisterResult implements TaskProcessorRegisterResult {

    protected TaskProcessorEstablishment          mEstablishment;
    protected boolean                             mbAccepted;
    protected String                              mszReason;
    protected TaskProcessorEntity                 mIncorporatedEntity;
    protected AnonymousTaskProcessorRegistration  mAnonymousRegistration;

    public GenericTaskProcessorRegisterResult(
            TaskProcessorEstablishment establishment,
            boolean bAccepted,
            String szReason,
            TaskProcessorEntity incorporatedEntity,
            AnonymousTaskProcessorRegistration anonymousRegistration
    ) {
        this.mEstablishment         = establishment;
        this.mbAccepted             = bAccepted;
        this.mszReason              = szReason;
        this.mIncorporatedEntity    = incorporatedEntity;
        this.mAnonymousRegistration = anonymousRegistration;
    }

    public static GenericTaskProcessorRegisterResult acceptedIncorporated( TaskProcessorEntity entity ) {
        return new GenericTaskProcessorRegisterResult(
                TaskProcessorEstablishment.Incorporated, true, null, entity, null
        );
    }

    public static GenericTaskProcessorRegisterResult acceptedAnonymous( AnonymousTaskProcessorRegistration registration ) {
        return new GenericTaskProcessorRegisterResult(
                TaskProcessorEstablishment.Anonymous, true, null, null, registration
        );
    }

    public static GenericTaskProcessorRegisterResult rejected(
            TaskProcessorEstablishment establishment, String szReason
    ) {
        return new GenericTaskProcessorRegisterResult( establishment, false, szReason, null, null );
    }

    @Override
    public TaskProcessorEstablishment getEstablishment() {
        return this.mEstablishment;
    }

    @Override
    public boolean isAccepted() {
        return this.mbAccepted;
    }

    @Override
    public String getReason() {
        return this.mszReason;
    }

    @Override
    public TaskProcessorEntity getIncorporatedEntity() {
        return this.mIncorporatedEntity;
    }

    @Override
    public AnonymousTaskProcessorRegistration getAnonymousRegistration() {
        return this.mAnonymousRegistration;
    }
}
