package imaing.expsys.test.api;

import imaing.expsys.server.api.ProductJsonWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@Transactional
public class RESTApiTest {
	
	@Test
	public void shouldTransformObjectToCorrectJson() throws JsonGenerationException, JsonMappingException, IOException {
		ProductJsonWrapper pjson = new ProductJsonWrapper();
		pjson.setDescription("desc1");
		pjson.setId("id1");
		Map<String, String> chrs = new LinkedHashMap<String, String>();
		chrs.put("chr1", "val1");
		chrs.put("chr2", "val2");
		pjson.setCharacteristics(chrs);
		
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mapper.writeValue(bos, pjson);
		
		String genJson = bos.toString();
		
		String trueJson = "{\"description\":\"desc1\",\"id\":\"id1\",\"characteristics\":{\"chr1\":\"val1\",\"chr2\":\"val2\"}}";
		
		Assert.assertEquals("Generated JSON is invalid!", trueJson, genJson);
	}
	
	@Test
	public void shouldTransformJsonToCorrectObject() throws JsonGenerationException, JsonMappingException, IOException {
		String json = "{\"description\":\"desc1\",\"id\":\"id1\",\"characteristics\":{\"chr1\":\"val1\",\"chr2\":\"val2\"}}";
		
		ProductJsonWrapper trueObj = new ProductJsonWrapper();
		trueObj.setDescription("desc1");
		trueObj.setId("id1");
		Map<String, String> chrs = new LinkedHashMap<String, String>();
		chrs.put("chr1", "val1");
		chrs.put("chr2", "val2");
		trueObj.setCharacteristics(chrs);
		
		ObjectMapper mapper = new ObjectMapper();
		ProductJsonWrapper genObj = mapper.readValue(new StringInputStream(json), ProductJsonWrapper.class);
		
		Assert.assertEquals("Generated object is invalid!", trueObj, genObj);
	}
}
