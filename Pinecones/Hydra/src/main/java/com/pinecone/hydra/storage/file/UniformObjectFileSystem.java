package com.pinecone.hydra.storage.file;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.GenericFSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.file.entity.GenericFileNode;
import com.pinecone.hydra.storage.file.entity.GenericFolder;
import com.pinecone.hydra.storage.file.entity.LocalFrame;
import com.pinecone.hydra.storage.file.entity.RemoteFrame;
import com.pinecone.hydra.storage.file.operator.FileSystemOperatorFactory;
import com.pinecone.hydra.storage.file.operator.GenericFileSystemOperatorFactory;
import com.pinecone.hydra.storage.file.source.FileSystemAttributeManipulator;
import com.pinecone.hydra.storage.file.source.FileManipulator;
import com.pinecone.hydra.storage.file.source.FileMasterManipulator;
import com.pinecone.hydra.storage.file.source.FileMetaManipulator;
import com.pinecone.hydra.storage.file.source.FolderManipulator;
import com.pinecone.hydra.storage.file.source.FolderMetaManipulator;
import com.pinecone.hydra.storage.file.source.LocalFrameManipulator;
import com.pinecone.hydra.storage.file.source.RemoteFrameManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.storage.file.source.SymbolicMetaManipulator;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.identifier.KOPathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.system.ko.kom.ArchReparseKOMTree;
import com.pinecone.hydra.system.ko.kom.GenericReparseKOMTreeAddition;
import com.pinecone.hydra.system.ko.kom.StandardPathSelector;
import com.pinecone.hydra.unit.udtt.DistributedTreeNode;
import com.pinecone.hydra.unit.udtt.entity.TreeNode;
import com.pinecone.hydra.unit.udtt.operator.TreeNodeOperator;
import com.pinecone.ulf.util.id.GUIDs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 *  Pinecone Ursus For Java UniformObjectFileSystem
 *  Author: Ken, Harold.E (Dragon King)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Uniform Object File System (Ken`s OFS / KOFS)
 *  Uniform Distribute Object Storage File System
 *  Supported TB-PB-ZB Level Big Data Storage
 *
 *  *****************************************************************************************
 */
public class UniformObjectFileSystem extends ArchReparseKOMTree implements KOMFileSystem {
    protected FSNodeAllotment                   fsNodeAllotment;

    protected FileSystemAttributeManipulator    fileSystemAttributeManipulator;
    protected FileManipulator                   fileManipulator;
    protected FileMasterManipulator             fileMasterManipulator;
    protected FileMetaManipulator               fileMetaManipulator;
    protected FolderManipulator                 folderManipulator;
    protected FolderMetaManipulator             folderMetaManipulator;
    protected LocalFrameManipulator             localFrameManipulator;
    protected RemoteFrameManipulator            remoteFrameManipulator;
    protected SymbolicManipulator               symbolicManipulator;
    protected SymbolicMetaManipulator           symbolicMetaManipulator;


    public UniformObjectFileSystem( Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        // Phase [1] Construct system.
        super( hydrarum, masterManipulator, KernelFileSystemConfig);

        // Phase [2] Construct fundamentals.
        this.fileMasterManipulator         = (FileMasterManipulator) masterManipulator;
        this.pathResolver                  =  new KOPathResolver( this.kernelObjectConfig );
        this.guidAllocator                 =  GUIDs.newGuidAllocator();

        // Phase [3] Construct manipulators.
        this.operatorFactory                =  new GenericFileSystemOperatorFactory( this, (FileMasterManipulator) masterManipulator );
        this.fileSystemAttributeManipulator =  this.fileMasterManipulator.getAttributeManipulator();
        this.fileManipulator                =  this.fileMasterManipulator.getFileManipulator();
        this.fileMetaManipulator            =  this.fileMasterManipulator.getFileMetaManipulator();
        this.folderManipulator              =  this.fileMasterManipulator.getFolderManipulator();
        this.folderMetaManipulator          =  this.fileMasterManipulator.getFolderMetaManipulator();
        this.localFrameManipulator          =  this.fileMasterManipulator.getLocalFrameManipulator();
        this.remoteFrameManipulator         =  this.fileMasterManipulator.getRemoteFrameManipulator();
        this.symbolicManipulator            =  this.fileMasterManipulator.getSymbolicManipulator();
        this.symbolicMetaManipulator        =  this.fileMasterManipulator.getSymbolicMetaManipulator();

        // Phase [4] Construct selectors.
        this.pathSelector                  =  new StandardPathSelector(
                this.pathResolver, this.distributedTrieTree, this.folderManipulator, new GUIDNameManipulator[] { this.fileManipulator }
        );
        // Warning: ReparseKOMTreeAddition must be constructed only after `pathSelector` has been constructed.
        this.mReparseKOM                   =  new GenericReparseKOMTreeAddition( this );

        // Phase [5] Construct misc.
//        this.propertyTypeConverter         =  new DefaultPropertyConverter();
//        this.textValueTypeConverter        =  new DefaultTextValueConverter();
        this.fsNodeAllotment              =  new GenericFSNodeAllotment(this.fileMasterManipulator,this);
    }

//    public GenericKOMFileSystem( Hydrarum hydrarum ) {
//        this.hydrarum = hydrarum;
//    }

    public UniformObjectFileSystem( KOIMappingDriver driver ) {
        this(
                driver.getSystem(),
                driver.getMasterManipulator()
        );
    }

    @Override
    public FileTreeNode get(GUID guid, int depth ) {
        return (FileTreeNode) super.get( guid, depth );
    }

    @Override
    public FileTreeNode get( GUID guid ) {
        return (FileTreeNode) super.get( guid );
    }

    @Override
    public FileTreeNode getSelf( GUID guid ) {
        return (FileTreeNode) super.getSelf( guid );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public List<FileTreeNode > listRoot() {
        return (List) super.listRoot();
    }



    @Override
    public FileSystemConfig getConfig() {
        return (FileSystemConfig) this.kernelObjectConfig;
    }

    public FileSystemOperatorFactory getOperatorFactory() {
        return (FileSystemOperatorFactory) this.operatorFactory;
    }

    @Override
    public FileNode getFileNode(GUID guid) {
        return ( FileNode ) this.get( guid );
    }

    @Override
    public Folder getFolder(GUID guid) {
        return ( Folder ) this.get( guid );
    }

    @Override
    public void removeFileNode(GUID guid) {

    }

    @Override
    public void removeFolder(GUID guid) {

    }

    @Override
    public List<TreeNode> getAllTreeNode() {
        List<GUID> nameSpaceNodes = this.fileManipulator.dumpGuid();
        List<GUID> confNodes      = this.folderManipulator.dumpGuid();
        ArrayList<TreeNode> treeNodes = new ArrayList<>();
        for (GUID guid : nameSpaceNodes){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        for ( GUID guid : confNodes ){
            TreeNode treeNode = this.get(guid);
            treeNodes.add(treeNode);
        }
        return treeNodes;
    }

    protected FileTreeNode affirmTreeNodeByPath( String path, Class<? > cnSup, Class<? > nsSup ) {
        String[] parts = this.pathResolver.segmentPathParts( path );
        String currentPath = "";
        GUID parentGuid = GUIDs.Dummy72();

        FileTreeNode node = this.queryElement( path );
        if( node != null ) {
            return node;
        }

        FileTreeNode ret = null;
        for( int i = 0; i < parts.length; ++i ){
            currentPath = currentPath + ( i > 0 ? this.getConfig().getPathNameSeparator() : "" ) + parts[ i ];
            node = this.queryElement( currentPath );
            if ( node == null){
                if ( i == parts.length - 1 && cnSup != null ){
                    FileNode fileNode = (FileNode) this.dynamicFactory.optNewInstance( cnSup, new Object[]{ this } );
                    fileNode.setName( parts[i] );
                    GUID guid = this.put( fileNode );
                    this.affirmOwnedNode( parentGuid, guid );
                    return fileNode;
                }
                else {
                    Folder folder = (Folder) this.dynamicFactory.optNewInstance( nsSup, new Object[]{ this } );
                    folder.setName(parts[i]);
                    GUID guid = this.put(folder);
                    if ( i != 0 ){
                        this.affirmOwnedNode( parentGuid, guid );
                        parentGuid = guid;
                    }
                    else {
                        parentGuid = guid;
                    }

                    ret = folder;
                }
            }
            else {
                parentGuid = node.getGuid();
            }
        }

        return ret;
    }

    @Override
    public FileNode affirmFileNode(String path) {
        return ( FileNode ) this.affirmTreeNodeByPath(path,GenericFileNode.class, GenericFolder.class);
    }

    @Override
    public Folder affirmFolder(String path) {
        return ( Folder ) this.affirmTreeNodeByPath(path, null,GenericFolder.class);
    }

    @Override
    public void setDataAffinityGuid(GUID childGuid, GUID affinityParentGuid) {

    }


    @Override
    public ElementNode queryElement(String path) {
        GUID guid = this.queryGUIDByPath( path );
        if( guid != null ) {
            return (ElementNode) this.get( guid );
        }
        return null;
    }

    @Override
    public List<TreeNode> selectByName(String name) {
        return null;
    }

    @Override
    public void moveTo(String sourcePath, String destinationPath) {
        GUID[] pair = this.assertCopyMove( sourcePath, destinationPath );
        GUID sourceGuid      = pair[ 0 ];
        GUID destinationGuid = pair[ 1 ];

        this.distributedTrieTree.moveTo( sourceGuid, destinationGuid );
        this.distributedTrieTree.removeCachePath( sourceGuid );
    }

    @Override
    public void move(String sourcePath, String destinationPath) {
        GUID sourceGuid         = this.assertPath( sourcePath, "source" );

        List<String > sourParts = this.pathResolver.resolvePathParts( sourcePath );
        List<String > destParts = this.pathResolver.resolvePathParts( destinationPath );

        String szLastDestTarget = destParts.get( destParts.size() - 1 );
        sourcePath      = sourcePath.trim();
        destinationPath = destinationPath.trim();

        //   Case1: Move "game/terraria/npc"   => "game/minecraft/npc", which has the same dest name.
        // Case1-1: Move "game/terraria/npc/"  => "game/minecraft/npc/"
        // Case1-2: Move "game/terraria/npc/." => "game/minecraft/npc/."
        if(
                sourParts.get( sourParts.size() - 1 ).equals( szLastDestTarget ) || szLastDestTarget.equals( "." ) ||
                        ( sourcePath.endsWith( this.getConfig().getPathNameSeparator() ) && destinationPath.endsWith( this.getConfig().getPathNameSeparator() ) )
        ) {
            destParts.remove( destParts.size() - 1 );
            String szParentPath = this.pathResolver.assemblePath( destParts );
            destParts.add( szLastDestTarget );

            // Move to, which has the same name or explicit current dir `.`.
            this.moveTo( sourcePath, szParentPath );
        }
        // Case 2: "game/terraria/npc" => "game/minecraft/character/" || "game/minecraft/character/."
        //    game/terraria/npc => game/minecraft/character/npc
        else if ( !sourcePath.endsWith( this.getConfig().getPathNameSeparator() ) && (
                destinationPath.endsWith( this.getConfig().getPathNameSeparator() ) || destinationPath.endsWith( "." ) )
        ) {
            Folder target = this.affirmFolder( destinationPath );
            this.distributedTrieTree.moveTo( sourceGuid, target.getGuid() );
        }
        // Case3: Move "game/terraria/npc" => "game/minecraft/character", move all children therein.
        //    game/terraria/npc/f1 => game/minecraft/character/f1
        //    game/terraria/npc/f2 => game/minecraft/character/f2
        //    etc.
        else {
            //  Case3-1: Is config or other none namespace node.
            //           Move "game/terraria/file" => "game/minecraft/dir".
            //  Case3-2: "game/terraria/npc/" => "game/minecraft/character"
            // Eq.Case2: Move "game/terraria/npc" => "game/minecraft/character",
            if( !this.folderManipulator.isFolder( sourceGuid ) ) {
                Folder target = this.affirmFolder( destinationPath );
                this.distributedTrieTree.moveTo( sourceGuid, target.getGuid() );
            }
            else {
                List<TreeNode > children = this.getChildren( sourceGuid );
                if( !children.isEmpty() ) {
                    Folder target = this.affirmFolder( destinationPath );
                    for( TreeNode node : children ) {
                        this.distributedTrieTree.moveTo( node.getGuid(), target.getGuid() );
                    }
                }
            }

            this.distributedTrieTree.removeTreeNodeOnly( sourceGuid );
        }

        this.distributedTrieTree.removeCachePath( sourceGuid );
    }

    @Override
    public void copyTo(String sourcePath, String destinationPath) {
    }

    @Override
    public void copy(String sourcePath, String destinationPath) {

    }

    @Override
    public Object querySelectorJ( String szSelector ) {
        return null;
    }


    @Override
    public TreeMap<Long, Frame> getFrameByFileGuid( GUID guid ) {
        TreeMap< Long, Frame > frameMap = new TreeMap<>();
        List<RemoteFrame> remoteFrames = this.remoteFrameManipulator.getRemoteFrameByFileGuid(guid);
        for( RemoteFrame remoteFrame : remoteFrames ){
            if( remoteFrame.getDeviceGuid().equals(GUIDs.GUID72("0000000-000000-0000-00")) ){
                LocalFrame localFrame = this.localFrameManipulator.getLocalFrameByGuid(remoteFrame.getSegGuid());
                frameMap.put( localFrame.getSegId(),localFrame );
            }
            else {
                //todo 远程获取逻辑
            }
        }

        return frameMap;
    }

    @Override
    public FSNodeAllotment getFSNodeAllotment() {
        return this.fsNodeAllotment;
    }

    @Override
    public Object querySelector(String szSelector) {
        return null;
    }

    @Override
    public List querySelectorAll(String szSelector) {
        return null;
    }

    @Override
    public void copyFileNodeTo(GUID sourceGuid, GUID destinationGuid) {

    }

    @Override
    public Frame getLastFrame(GUID guid) {
        RemoteFrame remoteFrame = this.remoteFrameManipulator.getLastFrame(guid);
        if ( remoteFrame.getDeviceGuid().equals( GUIDs.GUID72("0000000-000000-0000-00") ) ){
            return this.localFrameManipulator.getLocalFrameByGuid(remoteFrame.getSegGuid());
        }
        else {
            //todo 远端获取方法
        }
        return null;
    }

    @Override
    public void copyFolderTo(GUID sourceGuid, GUID destinationGuid) {

    }

    @Override
    public void upload( FileNode file, String destDirPath ) {
        if ( file.getIsUploadSuccessful() ){
            //this.upload0(file,destDirPath,0);
        }
        else {
            TreeMap<Long, Frame> frames = file.getFrames();
            Map.Entry<Long, Frame> longFrameEntry = frames.lastEntry();
            long segId = longFrameEntry.getValue().getSegId();
            //this.upload0(file, destDirPath, segId);
        }
    }

    private String getNodeName(DistributedTreeNode node ){
        UOI type = node.getType();
        TreeNode newInstance = (TreeNode)type.newInstance();
        TreeNodeOperator operator = this.getOperatorFactory().getOperator(newInstance.getMetaType());
        TreeNode treeNode = operator.get(node.getGuid());
        return treeNode.getName();
    }

    private boolean allNonNull( List<?> list ) {
        return list.stream().noneMatch( Objects::isNull );
    }

    protected GUID[] assertCopyMove ( String sourcePath, String destinationPath ) throws IllegalArgumentException {
        GUID sourceGuid      = this.queryGUIDByPath( sourcePath );
        if( sourceGuid == null ) {
            throw new IllegalArgumentException( "Undefined source '" + sourcePath + "'" );
        }

        GUID destinationGuid = this.queryGUIDByPath( destinationPath );
        if( !this.folderManipulator.isFolder( destinationGuid ) ){
            throw new IllegalArgumentException( "Illegal destination '" + destinationPath + "', should be namespace." );
        }

        if( destinationGuid == null ) {
            throw new IllegalArgumentException( "Undefined destination '" + destinationPath + "'" );
        }

        if( sourceGuid == destinationGuid ) {
            throw new IllegalArgumentException( "Cyclic path detected '" + sourcePath + "'" );
        }

        return new GUID[] { sourceGuid, destinationGuid };
    }

//    protected void upload0(FileNode file, String destDirPath, long startChunkIndex) {
//        long chunkSize = 10 * 1024 * 1024; // 每片的大小
//        File sourceFile = new File(file.getSourceName());
//        Path sourcePath = sourceFile.toPath();
//
//        try (FileChannel sourceChannel = FileChannel.open(sourcePath, StandardOpenOption.READ)) {
//            ByteBuffer buffer = ByteBuffer.allocateDirect((int) chunkSize);
//
//            long chunkIndex = startChunkIndex; // 从指定的分片开始
//            long bytesRead = chunkIndex * chunkSize; // 计算读取的初始位置
//            long totalBytesRead = bytesRead; // 总读取的字节数
//
//            // 获取上一次的分片信息
//            if (!file.getFrames().isEmpty()) {
//                LocalFrame lastFrame = (LocalFrame) file.getFrames().lastEntry().getValue();
//                bytesRead += lastFrame.getSize();
//            }
//
//            sourceChannel.position(bytesRead); // 设置 FileChannel 的起始位置
//
//            while (bytesRead < file.getSize()) {
//                LocalFrame localFrame = this.fileSystemCreator.dummyLocalFrame();
//                GUID frameGuid = this.guidAllocator.nextGUID72();
//                localFrame.setSegGuid(frameGuid);
//                localFrame.setFileGuid(file.getGuid());
//                localFrame.setSegId(chunkIndex);
//
//                buffer.clear();
//                int readBytes = sourceChannel.read(buffer);
//                int actualBytesRead = 0; // 实际写入的字节数
//
//                if (readBytes == -1) {
//                    break;
//                }
//
//                buffer.flip();
//
//                // 如果读到的字节小于 chunkSize，说明这是最后一片
//                if (bytesRead + readBytes > file.getSize()) {
//                    readBytes = (int)(file.getSize() - bytesRead);
//                }
//
//                try {
//                    // 计算当前分片的 CRC32 校验值
//                    CRC32 crc = new CRC32();
//                    while (buffer.hasRemaining()) {
//                        crc.update(buffer.get());
//                        actualBytesRead++; // 记录已经读取的字节
//                    }
//
//                    long crcValue = crc.getValue(); // 获取CRC32值
//                    localFrame.setCrc32(String.valueOf(crcValue)); // 将CRC32值设置在LocalFrame中
//
//                    buffer.rewind(); // 重置buffer的位置以便再次写入文件
//
//                    // 创建分片文件路径，确保分片的命名和索引一致
//                    Path chunkFile = Path.of(destDirPath, file.getName() + "_" + chunkIndex);
//                    localFrame.setSourceName(chunkFile.toString());
//
//                    // 设置实际的分片大小
//                    localFrame.setSize(actualBytesRead);
//
//                    // 写入分片文件
//                    try (FileChannel chunkChannel = FileChannel.open(chunkFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
//                        chunkChannel.write(buffer);
//                    }
//
//                    localFrame.save();
//                    bytesRead += actualBytesRead;
//                    chunkIndex++;
//
//                } catch (IOException e) {
//                    // 发生异常时，记录当前已写入的字节数
//                    localFrame.setSize(actualBytesRead);
//                    file.getFrames().put(chunkIndex, localFrame); // 记录这个 frame 的大小
//                    this.put(file); // 保存上传状态
//                    throw new RuntimeException("文件传输中断", e);
//                }
//
//                totalBytesRead += actualBytesRead;
//            }
//
//            // 上传完成，更新状态
//            file.setIsUploadSuccessful(true);
//            this.put(file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
