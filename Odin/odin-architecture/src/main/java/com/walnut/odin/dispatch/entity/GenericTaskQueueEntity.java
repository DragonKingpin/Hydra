package com.walnut.odin.dispatch.entity;

import java.util.Map;

public class GenericTaskQueueEntity extends ArchTaskQueueMeta {

    public GenericTaskQueueEntity() {

    }

    public GenericTaskQueueEntity( Map<String, Object> jo ) {
        super( jo );
    }

}
