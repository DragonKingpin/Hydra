package com.walnut.odin.formation.plan;

import java.util.Collection;

import com.pinecone.slime.chunk.Page;

public interface FormationPage extends FormationProduct, Page {
    long pageNo();

    Collection<FormationFrame> frames();
}
