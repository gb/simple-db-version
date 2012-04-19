package com.simpleDbVersion.infra;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.simpleDbVersion.domain.ScriptManager;
import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;

@RunWith(MockitoJUnitRunner.class)
public class SimpleDBVersionWithWildTagsFileMockingTest {
	
	@Mock
	private ScriptManager<File> versionScriptManager;
	
	private VersionRepository versionRepository = mock(VersionRepository.class);
	private VersionManager versionManager = mock(VersionManager.class);
	private VersionFileInstaller versionInstaller = mock(VersionFileInstaller.class);;
	
	private SimpleDbVersion simpleDbVersion;
	
	@Before
	public void setup() {
//		when(versionManager.availablesVersions()).thenReturn(Arrays.asList(1l));
//		when(versionManager.newestVersion()).thenReturn(1L);
		
		when(versionRepository.currentVersionNumber()).thenReturn(null);
		when(versionManager.newestVersion()).thenReturn(1L);
		
//		Map<String, String> tags = new HashMap<String, String>();
//		tags.put("SCHEMA_1", "MY_ORIGINAL_SCHEMA");
//		tags.put("SCHEMA_TWO", "MY_OTHER_ORIGINAL_SCHEMA");
		
//		versionInstaller = new VersionFileInstaller(versionRepository, versionManager, versionScriptManager, tags) {
//			public String toString(File file) {
//				return super.toString(file);
//			}
//		};
		
		simpleDbVersion = new SimpleDbVersion(versionRepository, versionManager, versionScriptManager, versionInstaller);
		
	}
	
	@Test
	public void testMockContentsReading() throws FileNotFoundException, IOException {
//		File file = mock(File.class);
//		Mockito.when(file.getPath()).thenReturn("c:\\");
//		String fileContent = IOUtils.toString(new FileInputStream(file));
		
		when(versionInstaller.toString((File)Mockito.anyObject())).thenReturn("Hi. I am the file content");
		
		Assert.assertEquals("Hi. I am the file content" , versionInstaller.toString(null));
		
		simpleDbVersion.install();
	}
}
