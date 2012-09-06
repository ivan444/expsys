package imaing.expsys.test.api;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.api.ProductJsonWrapper;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;
import imaing.expsys.test.util.TestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
@Transactional
public class RESTApiTest {
	@Autowired private ProductDAO prodDao;
	@Autowired private ShopDAO shpDao;
	@Autowired private CharacteristicDAO chrDao;
	@Autowired private ShopService shpService;
	
	@Test
	public void shouldTransformObjectToCorrectJson() throws JsonGenerationException, JsonMappingException, IOException {
		ProductJsonWrapper pjson = new ProductJsonWrapper();
		pjson.setDescription("desc1");
		pjson.setIntegId("id1");
		Map<String, String> chrs = new LinkedHashMap<String, String>();
		chrs.put("chr1", "val1");
		chrs.put("chr2", "val2");
		pjson.setCharacteristics(chrs);
		
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		mapper.writeValue(bos, pjson);
		
		String genJson = bos.toString();
		
		String trueJson = "{\"description\":\"desc1\",\"integId\":\"id1\",\"characteristics\":{\"chr1\":\"val1\",\"chr2\":\"val2\"}}";
		
		Assert.assertEquals("Generated JSON is invalid!", trueJson, genJson);
	}
	
	@Test
	public void shouldTransformJsonToCorrectObject() throws JsonGenerationException, JsonMappingException, IOException {
		String json = "{\"description\":\"desc1\",\"integId\":\"id1\",\"characteristics\":{\"chr1\":\"val1\",\"chr2\":\"val2\"}}";
		
		ProductJsonWrapper trueObj = new ProductJsonWrapper();
		trueObj.setDescription("desc1");
		trueObj.setIntegId("id1");
		Map<String, String> chrs = new LinkedHashMap<String, String>();
		chrs.put("chr1", "val1");
		chrs.put("chr2", "val2");
		trueObj.setCharacteristics(chrs);
		
		ObjectMapper mapper = new ObjectMapper();
		ProductJsonWrapper genObj = mapper.readValue(json, ProductJsonWrapper.class);
		
		Assert.assertEquals("Generated object is invalid!", trueObj, genObj);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldTransformJsonToCorrectListOfObjects() throws JsonGenerationException, JsonMappingException, IOException {
		String json = "[{\"description\":\"desc1\",\"integId\":\"id1\"," +
				"\"characteristics\":{\"chr1\":\"val1\",\"chr2\":\"val2\"}}," +
				"{\"description\":\"desc2\",\"integId\":\"id2\"," +
				"\"characteristics\":{\"chr1\":\"val3\",\"chr2\":\"val2\"}}]";
		
		ProductJsonWrapper trueObj1 = new ProductJsonWrapper();
		trueObj1.setDescription("desc1");
		trueObj1.setIntegId("id1");
		Map<String, String> chrs1 = new LinkedHashMap<String, String>();
		chrs1.put("chr1", "val1");
		chrs1.put("chr2", "val2");
		trueObj1.setCharacteristics(chrs1);
		
		ProductJsonWrapper trueObj2 = new ProductJsonWrapper();
		trueObj2.setDescription("desc2");
		trueObj2.setIntegId("id2");
		Map<String, String> chrs2 = new LinkedHashMap<String, String>();
		chrs2.put("chr1", "val3");
		chrs2.put("chr2", "val2");
		trueObj2.setCharacteristics(chrs2);
		
		List<ProductJsonWrapper> prods = new ArrayList<ProductJsonWrapper>();
		prods.add(trueObj1);
		prods.add(trueObj2);
		
		ObjectMapper mapper = new ObjectMapper();
		List<ProductJsonWrapper> genLst = (List<ProductJsonWrapper>) mapper.readValue(json,
				new TypeReference<List<ProductJsonWrapper>>(){});
		
		Assert.assertEquals("Generated list is invalid!", prods, genLst);
		
		for (int i = 0; i < prods.size(); i++) {
			Map<String, String> chrsP = prods.get(i).getCharacteristics();
			Map<String, String> chrsG = genLst.get(i).getCharacteristics();
			Assert.assertEquals("Original and generated characteristics aren't equal!", chrsP, chrsG);
		}
	}
	
	@Test
	public void shouldGenerateObjFromJsonAndSaveProduct() throws JsonParseException, JsonMappingException, IOException, InvalidDataException {
		String json = "{\"description\":\"desc1\",\"integId\":\"id1\",\"characteristics\":{\"chr1\":\"val1\",\"chr2\":\"val2\"}}";
		Shop shop = TestUtils.newShop();
		shop = shpDao.save(shop);
		
		Characteristic chr1 = new Characteristic();
		chr1.setfClsNum(10);
		chr1.setShop(shop);
		chr1.setName("chr1");
		
		Characteristic chr2 = new Characteristic();
		chr2.setfClsNum(10);
		chr2.setShop(shop);
		chr2.setName("chr2");
		
		chr1 = chrDao.save(chr1);
		chr2 = chrDao.save(chr2);
		
		ObjectMapper mapper = new ObjectMapper();
		ProductJsonWrapper genObj = mapper.readValue(json, ProductJsonWrapper.class);
		Product p = genObj.genProduct(shop, chrDao.listCharacteristicsForShop(shop));
		
		shpService.addProduct(p);
		
		Product pSaved = prodDao.getProductWCharsForShopAndIntegrationId(shop, "id1");
		Assert.assertEquals("Generated and saved product aren't equal!", p, pSaved);
		
		Assert.assertEquals("Generated and saved product don't have the same characteristics!",
				p.getCharacteristics(), pSaved.getCharacteristics());
		
	}
}
