package com.walnut.odin.task;

import com.pinecone.framework.system.executum.Processum;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchKOMTree;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.task.kom.UniformTaskInstrument;
import com.pinecone.hydra.task.kom.entity.ElementNode;
import com.pinecone.hydra.task.kom.entity.JobElement;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.task.kom.entity.TaskElement;
import com.pinecone.hydra.unit.imperium.entity.ReparseLinkNode;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.odin.task.service.CategoryService;
import com.walnut.odin.task.service.RavenCategoryService;
import com.walnut.odin.task.source.RavenTaskMasterManipulator;
import com.walnut.odin.task.system.TaskPathInvalidException;

public class RavenTaskInstrument extends ArchKOMTree implements CentralizedTaskInstrument {
    protected RavenTaskMasterManipulator ravenTaskMasterManipulator;

    protected UniformTaskInstrument     uniformTaskInstrument;

    protected CategoryService           categoryService;

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator, KOMInstrument parent, String name ) {
        super( superiorProcess, masterManipulator, RAVEN_TASK_CONFIG, parent, name );
        this.ravenTaskMasterManipulator = (RavenTaskMasterManipulator) masterManipulator;
        this.pathResolver          = new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator         = GUIDs.newGuidAllocator();

        this.uniformTaskInstrument = new UniformTaskInstrument( this.ravenTaskMasterManipulator.getTaskMappingDriver() );
        this.categoryService       = new RavenCategoryService( this );
    }

    public RavenTaskInstrument( Processum superiorProcess, KOIMasterManipulator masterManipulator ) {
        this( superiorProcess, masterManipulator, null, CentralizedTaskInstrument.class.getSimpleName() );
    }

    public RavenTaskInstrument( KOIMappingDriver driver, CentralizedTaskInstrument parent, String name ){
        this( driver.getSuperiorProcess(), driver.getMasterManipulator(), parent, name );
    }

    public RavenTaskInstrument( KOIMappingDriver driver ) {
        this( driver.getSuperiorProcess(), driver.getMasterManipulator() );
    }

    @Override
    public GUID assertGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException {
        GUID guid = this.uniformTaskInstrument.queryGUIDByPath( taskTreePath );
        if ( guid == null ) {
            throw new TaskPathInvalidException( taskTreePath );
        }

        return guid;
    }

    @Override
    public GUID assertTaskGUIDByPath ( String taskTreePath ) throws TaskPathInvalidException, IllegalArgumentException {
        ElementNode node = this.uniformTaskInstrument.queryElement( taskTreePath );
        if ( node == null ) {
            throw new TaskPathInvalidException( taskTreePath );
        }
        if ( node.evinceTaskElement() == null ) {
            throw new IllegalArgumentException( "Path `" + taskTreePath + "` is not a task." );
        }

        return node.getGuid();
    }


    @Override
    public UniformTaskInstrument getUniformTaskInstrument() {
        return this.uniformTaskInstrument;
    }

    @Override
    public RavenTaskMasterManipulator getRavenTaskMasterManipulator() {
        return this.ravenTaskMasterManipulator;
    }

    @Override
    public void newLinkTag(String originalPath, String dirPath, String tagName) {

    }

    @Override
    public void removeReparseLink(GUID guid) {

    }

    @Override
    public void affirmOwnedNode(GUID parentGuid, GUID childGuid) {

    }

    @Override
    public void newHardLink(GUID sourceGuid, GUID targetGuid) {

    }

    @Override
    public void newLinkTag(GUID originalGuid, GUID dirGuid, String tagName) {

    }

    @Override
    public void updateLinkTag(GUID tagGuid, String tagName) {

    }

    @Override
    public ReparseLinkNode queryReparseLinkByNS(String path, String szBadSep, String szTargetSep) {
        return null;
    }

    @Override
    public Object queryEntityHandleByNS( String path, String szBadSep, String szTargetSep ) {
        return null;
    }

    @Override
    public ReparseLinkNode queryReparseLink(String path) {
        return null;
    }

    @Override
    public CategoryService getCategoryService() {
        return this.categoryService;
    }








    @Override
    public JobElement affirmApplication( String path ) {
        return null;
    }

    @Override
    public Namespace affirmNamespace( String path ) {
        return null;
    }

    @Override
    public TaskElement affirmService(String path) {
        return null;
    }

    @Override
    public ElementNode queryElement(String path) {
        return null;
    }

    @Override
    public boolean containsChild(GUID parentGuid, String childName) {
        return false;
    }

    @Override
    public void update( TreeNode treeNode ) {

    }


}
