package com.walnut.sparta.utask.console.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.sparta.utask.console.infrastructure.dto.TaskChildDto;
import com.walnut.sparta.utask.console.infrastructure.dto.TaskDto;

public interface TaskInstrumentService extends Pinenut {

    String addTask( TaskDto taskDto );

    String addTaskChild( TaskChildDto taskChildDto );

    String addTaskNamespaceChild( String path, String parentPath );

    String queryTaskGuidInfo( String szGuid );

    String updateTask( String path, TaskChildDto taskChildDto );

    String updateTaskNamespace( String path, String name );

    String queryTaskPath( String path );
}
