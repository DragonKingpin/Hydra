package com.pinecone.hydra.system.ko.kom;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

/**
 * Kernel Object Model
 * Same as Document Object Model (DOM)
 */
public interface KOMSelector extends Pinenut {
    // Return with json.
    Object querySelectorJ                 ( String szSelector );

    Object querySelector                  ( String szSelector );

    List querySelectorAll                 ( String szSelector );
}
