package com.simpleDbVersion;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.VersionInstaller;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;
import com.simpleDbVersion.domain.VersionScriptManager;

public class SimpleDbVersionTest {
	
	private SimpleDbVersion simpleDbVersion;
	
	private VersionRepository versionRepository = mock(VersionRepository.class);
	private VersionManager versionManager = mock(VersionManager.class);
	private VersionScriptManager versionScriptManager = mock(VersionScriptManager.class);
	private VersionInstaller versionInstaller = mock(VersionInstaller.class);
	
	@Before
	public void setup() {
		simpleDbVersion = new SimpleDbVersion(versionRepository, versionManager, versionScriptManager, versionInstaller);
	}
	
	@Test
	public void testIsOutOfVersionWhenHasScriptsAndNeverWasInstalled() {
		when(versionRepository.currentVersion()).thenReturn(null);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		assertTrue(simpleDbVersion.dbIsOutOfVersion());
	}
	
	@Test
	public void testIsOutOfVersionWhenTheresANewVersionAvailable() {
		when(versionRepository.currentVersion()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(2L);
		
		assertTrue(simpleDbVersion.dbIsOutOfVersion());
	}
	
	@Test
	public void testIsOutOfVersionWhenTheresANewScriptAvailable() {
		when(versionRepository.currentVersion()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		when(versionRepository.lastScript()).thenReturn(5L);
		when(versionScriptManager.newestScript(1L)).thenReturn(6L);
		
		assertTrue(simpleDbVersion.dbIsOutOfVersion());
	}
	
	@Test
	public void testIsOutOfVersionWhenAllScriptsWereInstalled() {
		when(versionRepository.currentVersion()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		when(versionRepository.lastScript()).thenReturn(6L);
		when(versionScriptManager.newestScript(1L)).thenReturn(6L);
		
		assertFalse(simpleDbVersion.dbIsOutOfVersion());
	}
	
	@Test
	public void testUpdateVersionShouldNOTBeInvokedWhenDbIsNOTOutOfVersion() {
		when(versionRepository.currentVersion()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		simpleDbVersion.install();
		
		verify(versionInstaller, never()).upgradeVersion(anyLong(), anyLong());	
		verify(versionInstaller, never()).installFullVersion(anyLong());
	}
	
	@Test
	public void testUpdateVersionShouldBeInvokedWhenDbIsOutOfVersion() {
		when(versionRepository.currentVersion()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(2L);
		when(versionManager.availablesVersions()).thenReturn(Arrays.asList(1L, 2L));
		
		simpleDbVersion.install();
		
		verify(versionInstaller, times(1)).installFullVersion(anyLong());		
	}

}
