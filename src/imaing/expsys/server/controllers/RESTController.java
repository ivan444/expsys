package imaing.expsys.server.controllers;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.api.APIResponse;
import imaing.expsys.server.api.APIResponse.Status;
import imaing.expsys.server.api.ProductJsonWrapper;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class RESTController {
	@Autowired private ShopDAO shpDao;
	@Autowired private CharacteristicDAO chrDao;
	@Autowired private ShopService shpService;
	
	
	// Test
	@RequestMapping(value = "/v1/hello", method = RequestMethod.GET)
	public void getMe(@RequestBody String body, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().write("hello\n" + body + "\n");
	}
	
	@RequestMapping(value="/v1/shop/{shpemail:[a-zA-Z0-9_\\.\\-\\+]+\\@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9]+}/product", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public void saveProduct(@RequestBody String body, @PathVariable String shpemail, HttpServletResponse resp) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<ProductJsonWrapper> genLst = mapper.readValue(body, new TypeReference<List<ProductJsonWrapper>>(){});
			
			Shop shop = shpDao.getShopForEmail(shpemail);
			if (shop == null) {
				mapper.writeValue(resp.getOutputStream(),
						new APIResponse(Status.ERROR, "E-mail " + shpemail + " isn't associated with any shop."));
				return;
			}
			
			List<Characteristic> chrs = chrDao.listCharacteristicsForShop(shop);
			
			List<Product> genProds = new ArrayList<Product>(genLst.size());
			for (ProductJsonWrapper pjw : genLst) {
				Product p = pjw.genProduct(shop, chrs);
				genProds.add(p);
			}

			try {
				shpService.addAllProducts(genProds);
			} catch (InvalidDataException de) {
				mapper.writeValue(resp.getOutputStream(), new APIResponse(Status.ERROR, "Failed to save product."));
				return;
			}
			
		} catch (Exception e) {
			mapper.writeValue(resp.getOutputStream(), new APIResponse(Status.ERROR, "Invalid JSON format."));
			e.printStackTrace();
			return;
		}
		
		mapper.writeValue(resp.getOutputStream(), new APIResponse(Status.OK, "Products are saved."));
		
	}
	
}
