package com.simpleDbVersion;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.simpleDbVersion.domain.ScriptManager;
import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.VersionInstaller;
import com.simpleDbVersion.domain.VersionManager;
import com.simpleDbVersion.domain.VersionRepository;
import com.simpleDbVersion.infra.VersionFileInstaller;

@RunWith(MockitoJUnitRunner.class)
public class SimpleDBVersionWithWildTagsFileReadingTest {
	
	@Mock
	private ScriptManager<File> versionScriptManager;
	
	private VersionRepository versionRepository = mock(VersionRepository.class);
	private VersionManager versionManager = mock(VersionManager.class);
	
	private SimpleDbVersion simpleDbVersion;
	private VersionInstaller versionInstaller;
	
	@Before
	public void setup() {
		when(versionManager.availablesVersions()).thenReturn(Arrays.asList(1l));
		when(versionManager.newestVersion()).thenReturn(1L);
		
		final Map<String, String> tags = new HashMap<String, String>();
		tags.put("SCHEMA_1", "MY_ORIGINAL_SCHEMA");
		tags.put("SCHEMA_TWO", "MY_OTHER_ORIGINAL_SCHEMA");
		
		versionInstaller = new VersionFileInstaller(versionRepository, versionManager, versionScriptManager, tags);
		simpleDbVersion = new SimpleDbVersion(versionRepository, versionManager, versionScriptManager, versionInstaller);
	}
	
	@Test
	public void fileWithOneTag() throws URISyntaxException {
		when(versionScriptManager.availablesScripts(anyLong())).thenReturn(new File[] {getFile("/script_01.sql")});
		
		simpleDbVersion.install();
		
		verify(versionRepository).executeScript("create table MY_ORIGINAL_SCHEMA.mytable\n" + 
				"(\n" + 
				"  ID  NUMBER 		not null,\n" + 
				"  CREATE_DATE		DATE not null,\n" + 
				"  OP_ID				NUMBER,\n" + 
				"  DESCRIPTION		VARCHAR2(4000 BYTE),\n" + 
				"  DATA_ALTERACAO_DT DATE\n" + 
				")\n"  
				);
	}
	
	@Test
	public void fileWithOneTagTwoOccurs() {
		when(versionScriptManager.availablesScripts(anyLong())).thenReturn(new File[] {getFile("/script_02.sql")});
		
		simpleDbVersion.install();
		
		verify(versionRepository).executeScript("create table MY_ORIGINAL_SCHEMA.mytable\n" + 
				"(\n" + 
				"  ID  NUMBER 		not null,\n" + 
				"  CREATE_DATE		DATE not null,\n" + 
				"  OP_ID				NUMBER,\n" + 
				"  DESCRIPTION		VARCHAR2(4000 BYTE),\n" + 
				"  DATA_ALTERACAO_DT DATE\n" + 
				")\n"  
				);
		
		verify(versionRepository).executeScript("\ncreate table MY_ORIGINAL_SCHEMA.myothertable\n" + 
				"(\n" + 
				"  SERVICE_ID NUMBER not null,\n" + 
				"  TOTAL_NUM  NUMBER\n" + 
				")");
		
	}
	
	@Test
	public void fileWithTwoTags() throws URISyntaxException {
		when(versionScriptManager.availablesScripts(anyLong())).thenReturn(new File[] {getFile("/script_03.sql")});
		
		simpleDbVersion.install();
		
		verify(versionRepository).executeScript("create table MY_ORIGINAL_SCHEMA.mytable\n" + 
				"(\n" + 
				"  ID  NUMBER 		not null,\n" + 
				"  CREATE_DATE		DATE not null,\n" + 
				"  OP_ID				NUMBER,\n" + 
				"  DESCRIPTION		VARCHAR2(4000 BYTE),\n" + 
				"  DATA_ALTERACAO_DT DATE\n" + 
				")\n"  
				);
		
		verify(versionRepository).executeScript("\ncreate table MY_OTHER_ORIGINAL_SCHEMA.myothertable\n" + 
				"(\n" + 
				"  SERVICE_ID NUMBER not null,\n" + 
				"  TOTAL_NUM  NUMBER\n" + 
				")");
		
	}
	
	private File getFile(String fileName){
		try {
			return new File(getClass().getResource(fileName).toURI());
		} catch (URISyntaxException e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
}
