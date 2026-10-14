package com.acorn.redqueen.service.deletion;

import java.util.Collections;

import com.acorn.redqueen.service.deletion.ServiceDirectoryDeleteSafetyReport.ReasonCode;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.GenericApplicationElement;
import com.pinecone.hydra.service.kom.entity.GenericNamespace;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceTreeNode;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RedServiceDirectoryDeleteServiceTest {
    private final GUID guid = mock( GUID.class );
    private final ServiceInstrument instrument = mock( ServiceInstrument.class );
    private final ServiceMasterManipulator manipulator = mock( ServiceMasterManipulator.class );
    private final RedServiceDirectoryDeleteService service = new RedServiceDirectoryDeleteService( instrument );

    @Before
    public void setUp() {
        when( instrument.getPath( guid ) ).thenReturn( "app" );
        when( instrument.queryGUIDByPath( "/app" ) ).thenReturn( guid );
        when( instrument.fetchChildrenGuids( guid ) ).thenReturn( Collections.emptyList() );
        when( instrument.getServiceMasterManipulator() ).thenReturn( manipulator );
    }

    private void target( ServiceTreeNode node ) {
        when( instrument.get( guid ) ).thenReturn( node );
    }

    @Test
    public void allowsEmptyTopLevelApplicationAndPurgesByPath() {
        target( new GenericApplicationElement() );
        ServiceDirectoryDeleteSafetyReport report = service.checkDirectoryDelete( "/app" );
        assertTrue( report.isDeletable() );
        assertEquals( ReasonCode.Empty, report.getReasonCode() );
        assertEquals( 0, report.getChildCount() );
        service.purgeDirectory( "/app" );
        verify( manipulator ).purgeServiceDirectory( guid );
    }

    @Test
    public void stillAllowsEmptyNamespace() {
        target( new GenericNamespace() );
        assertTrue( service.purgeDirectory( guid ).isDeletable() );
        verify( manipulator ).purgeServiceDirectory( guid );
    }

    @Test
    public void blocksNonEmptyApplicationWithPreview() {
        target( new GenericApplicationElement() );
        GUID child = mock( GUID.class );
        when( instrument.fetchChildrenGuids( guid ) ).thenReturn( Collections.singletonList( child ) );
        when( instrument.getPath( child ) ).thenReturn( "app/child" );
        ServiceDirectoryDeleteSafetyReport report = service.checkDirectoryDelete( guid );
        assertFalse( report.isDeletable() );
        assertEquals( ReasonCode.NotEmpty, report.getReasonCode() );
        assertEquals( 1, report.getChildCount() );
        assertEquals( child, report.getChildrenPreview().get( 0 ).getGuid() );
        assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( guid ) );
        verifyNoInteractions( manipulator );
    }

    @Test
    public void rejectsServiceWithoutClaimingItHasChildren() {
        target( new GenericServiceElement() );
        ServiceDirectoryDeleteSafetyReport report = service.checkDirectoryDelete( guid );
        assertFalse( report.isDeletable() );
        assertEquals( ReasonCode.UnsupportedType, report.getReasonCode() );
        verify( instrument, never() ).fetchChildrenGuids( guid );
        assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( guid ) );
        verifyNoInteractions( manipulator );
    }

    @Test
    public void protectsVirtualRootWithoutLookingItUp() {
        for ( String path : new String[] { "/", "///", "\\", " / " } ) {
            ServiceDirectoryDeleteSafetyReport report = service.checkDirectoryDelete( path );
            assertFalse( report.isDeletable() );
            assertEquals( ReasonCode.RootProtected, report.getReasonCode() );
            assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( path ) );
        }
        verifyNoInteractions( instrument, manipulator );
    }

    @Test
    public void rechecksChildrenAfterSuccessfulPreflight() {
        target( new GenericNamespace() );
        assertTrue( service.checkDirectoryDelete( guid ).isDeletable() );
        when( instrument.fetchChildrenGuids( guid ) ).thenReturn( Collections.singletonList( mock( GUID.class ) ) );
        assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( guid ) );
        verifyNoInteractions( manipulator );
    }

    @Test
    public void rejectsMissingTarget() {
        assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( guid ) );
        assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( "missing" ) );
        assertThrows( IllegalArgumentException.class, () -> service.purgeDirectory( " " ) );
        verifyNoInteractions( manipulator );
    }
}
