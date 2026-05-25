package com.walnut.odin.specific.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigest;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigestQuery;
import com.walnut.odin.specific.digest.TaskSpecificDigest;
import com.walnut.odin.specific.digest.TaskSpecificDigestQuery;

import java.util.Collection;
import java.util.List;

public interface TaskSpecificManipulator extends Pinenut {

    long countTaskSpecificDigests( TaskSpecificDigestQuery query );

    List<TaskSpecificDigest> fetchTaskSpecificDigests( TaskSpecificDigestQuery query );

    List<TaskSpecificDigest> fetchTaskSpecificDigestsByGuids( Collection<GUID> taskGuids );

    long countTaskInstanceSpecificDigests( TaskInstanceSpecificDigestQuery query );

    List<TaskInstanceSpecificDigest> fetchTaskInstanceSpecificDigests( TaskInstanceSpecificDigestQuery query );

    String selectTaskProjectGuid( GUID taskGuid );

    int updateTaskProjectGuid( GUID taskGuid, GUID projectGuid );
}
