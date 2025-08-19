package com.walnut.sparta.uis.console.domain.dto;

import com.pinecone.framework.util.id.GUID;

public class IntelligenceTagDto {

    private GUID intelligenceGuid;

    public IntelligenceTagDto() {
    }

    public IntelligenceTagDto(GUID intelligenceGuid) {
        this.intelligenceGuid = intelligenceGuid;
    }

    public GUID getIntelligenceGuid() {
        return intelligenceGuid;
    }

    public void setIntelligenceGuid(GUID intelligenceGuid) {
        this.intelligenceGuid = intelligenceGuid;
    }
}
