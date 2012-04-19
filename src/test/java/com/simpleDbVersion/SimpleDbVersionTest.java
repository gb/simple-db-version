package com.simpleDbVersion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.simpleDbVersion.domain.ScriptManager;
import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.VersionInstaller;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;

public class SimpleDbVersionTest {
	
	private SimpleDbVersion simpleDbVersion;
	
	private VersionRepository versionRepository = mock(VersionRepository.class);
	private VersionManager versionManager = mock(VersionManager.class);
	private ScriptManager<?> versionScriptManager = mock(ScriptManager.class);
	private VersionInstaller versionInstaller = mock(VersionInstaller.class);
	
	@Before
	public void setup() {
		simpleDbVersion = new SimpleDbVersion(versionRepository, versionManager, versionScriptManager, versionInstaller);
	}
	
	@Test
	public void testIsOutOfVersionWhenHasScriptsAndNeverWasInstalled() {
		when(versionRepository.currentVersionNumber()).thenReturn(null);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		assertTrue(simpleDbVersion.neverWasInstalled()); 
		assertTrue(simpleDbVersion.versionIsOutdate());
		assertFalse(simpleDbVersion.scriptsOfCurrentVersionIsOutdate());
	}
	
	@Test
	public void testIsOutOfVersionWhenTheresANewVersionAvailable() {
		when(versionRepository.currentVersionNumber()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(2L);
		
		assertTrue(simpleDbVersion.versionIsOutdate());
		assertFalse(simpleDbVersion.neverWasInstalled()); 
		assertFalse(simpleDbVersion.scriptsOfCurrentVersionIsOutdate());
	}
	
	@Test
	public void testIsOutOfVersionWhenTheresANewScriptAvailable() {
		when(versionRepository.currentVersionNumber()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		when(versionRepository.lastScript()).thenReturn(5L);
		when(versionScriptManager.newestScript(1L)).thenReturn(6L);
		
		assertFalse(simpleDbVersion.versionIsOutdate());
		assertTrue(simpleDbVersion.scriptsOfCurrentVersionIsOutdate());
	}
	
	@Test
	public void testIsOutOfVersionWhenAllScriptsWereInstalled() {
		when(versionRepository.currentVersionNumber()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		when(versionRepository.lastScript()).thenReturn(6L);
		when(versionScriptManager.newestScript(1L)).thenReturn(6L);
		
		assertFalse(simpleDbVersion.neverWasInstalled());
		assertFalse(simpleDbVersion.versionIsOutdate());
		assertFalse(simpleDbVersion.scriptsOfCurrentVersionIsOutdate());
	}
	
	@Test
	public void testUpdateVersionShouldNOTBeInvokedWhenDbIsNOTOutOfVersion() {
		when(versionRepository.currentVersionNumber()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(1L);
		
		simpleDbVersion.install();
		
		verify(versionInstaller, never()).upgradeVersion(anyLong(), anyLong());	
		verify(versionInstaller, never()).installFullVersionsFrom(anyLong());
	}
	
	@Test
	public void testUpdateVersionShouldBeInvokedWhenDbIsOutOfVersion() {
		when(versionRepository.currentVersionNumber()).thenReturn(1L);
		when(versionManager.newestVersion()).thenReturn(2L);
		when(versionManager.availablesVersions()).thenReturn(Arrays.asList(1L, 2L));
		
		simpleDbVersion.install();
		
		verify(versionInstaller, times(1)).installFullVersionsFrom(anyLong());		
	}

}
