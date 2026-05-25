package com.walnut.odin.specific;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigest;
import com.walnut.odin.specific.digest.TaskInstanceSpecificDigestQuery;
import com.walnut.odin.specific.digest.TaskSpecificPage;
import com.walnut.odin.specific.digest.TaskSpecificDigest;
import com.walnut.odin.specific.digest.TaskSpecificDigestQuery;

import java.util.Collection;
import java.util.List;

public interface TaskSpecificService extends Pinenut {

    TaskSpecificPage<TaskSpecificDigest> pageTaskSpecificDigests( TaskSpecificDigestQuery query );

    List<TaskSpecificDigest> fetchTaskSpecificDigestsByGuids( Collection<GUID> taskGuids );

    TaskSpecificPage<TaskInstanceSpecificDigest> pageTaskInstanceSpecificDigests( TaskInstanceSpecificDigestQuery query );

    String queryTaskProjectGuid( GUID taskGuid );

    void bindTaskProject( GUID taskGuid, GUID projectGuid );
}
