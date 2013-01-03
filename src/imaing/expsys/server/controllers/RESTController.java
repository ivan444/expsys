package imaing.expsys.server.controllers;

import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.FuzzyClass;
import imaing.expsys.client.domain.Product;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.client.services.ShopService;
import imaing.expsys.server.api.APIExtendedResponse.Status;
import imaing.expsys.server.api.JSONObjectMapper;
import imaing.expsys.server.api.ProductJsonWrapper;
import imaing.expsys.server.api.Ranking;
import imaing.expsys.server.engine.ExpSys;
import imaing.expsys.server.model.CharacteristicDAO;
import imaing.expsys.server.model.FuzzyClassDAO;
import imaing.expsys.server.model.ProductDAO;
import imaing.expsys.server.model.RuleDAO;
import imaing.expsys.server.model.ShopDAO;
import imaing.expsys.shared.exceptions.InvalidDataException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Controller responsible for handling REST API calls.
 * 
 */
@Controller
public class RESTController {
	@Autowired private ShopDAO shpDao;
	@Autowired private CharacteristicDAO chrDao;
	@Autowired private ProductDAO prodDao;
	@Autowired private RuleDAO ruleDao;
	@Autowired private FuzzyClassDAO fclsDao;
	@Autowired private ShopService shpService;
	@Autowired private JSONObjectMapper mapper;
	
	/**
	 * Save list of products. List is received in request body as JSON formatted as following:
	 * <pre>
	 * [{
	 *   "description": "example description",
	 *   "integId": "example integration id - unique for each product of one shop",
	 *   "characteristics": {
	 *     "chr1": "chr1 value",
	 *     "chr2": "chr2 value",
	 *     "chr3": "chr3 value",
	 *     ...
	 *   },
	 *   ...
	 * ]
	 * </pre>
	 * Given JSON fits the mapping of {@code List} of {@code ProductJsonWrapper}.
	 * <p>
	 * Method sends response to the caller through {@code HttpServletResponse} by filling
	 * response with JSON format of {@code APIExtendedResponse}.
	 * </p>
	 * 
	 * 
	 * @param body Request body which contains JSON with list of products.
	 * @param shpemail E-mail of the shop for which we add products.
	 * @param resp Servlet response object for sending response to the caller.
	 * @throws IOException Thrown if filling of servlet response fails.
	 */
	@RequestMapping(value="/v1/shop/{shpemail:[a-zA-Z0-9_\\.\\-\\+]+\\@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9]+}/product", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public void saveProducts(@RequestBody String body, @PathVariable String shpemail, HttpServletResponse resp) throws IOException {
		try {
			List<ProductJsonWrapper> genLst = mapper.read(body, new TypeReference<List<ProductJsonWrapper>>(){});
			
			Shop shop = shpDao.getShopForEmail(shpemail);
			if (shop == null) {
				mapper.writeResponse(resp.getOutputStream(), Status.ERROR,
						"E-mail " + shpemail + " isn't associated with any shop.");
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
				mapper.writeResponse(resp.getOutputStream(), Status.ERROR, "Failed to save product.");
				return;
			}
			
		} catch (Exception e) {
			mapper.writeResponse(resp.getOutputStream(), Status.ERROR, "Invalid JSON format.");
			e.printStackTrace();
			return;
		}
		
		mapper.writeResponse(resp.getOutputStream(), Status.OK, "Products are saved.");
	}
	
	@RequestMapping(value="/v1/shop/{shpemail:[a-zA-Z0-9_\\.\\-\\+]+\\@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9]+}/product/ranking", method=RequestMethod.GET, consumes="application/json", produces="application/json")
	public void rankProducts(@RequestBody String body, @PathVariable String shpemail, HttpServletResponse resp) throws IOException {
		try {
			Ranking genReq = mapper.read(body, Ranking.class);
			
			if (genReq.getRuleIds().isEmpty()) {
				mapper.writeResponse(resp.getOutputStream(),
						Status.ERROR, "Request must contain at least one rule!");
				return;
			}
			
			Shop shop = shpDao.getShopForEmail(shpemail);
			if (shop == null) {
				mapper.writeResponse(resp.getOutputStream(),
						Status.ERROR, "E-mail " + shpemail + " isn't associated with any shop.");
				return;
			}
			
			List<Product> prods = prodDao.listProductsWCharsForShop(shop);
			List<Rule> rules = ruleDao.listRulesForShop(shop);
			
			List<Product> inclProds = new ArrayList<Product>(genReq.getIntegIds().size());
			if (genReq.getIntegIds().isEmpty()) {
				inclProds.addAll(prods);
			} else {
				Set<String> integIds = new HashSet<String>(genReq.getIntegIds());
				for (Product p : prods) {
					if (integIds.contains(p.getIntegrationId())) {
						inclProds.add(p);
					}
				}
			}
			
			List<Rule> inclRules = new ArrayList<Rule>();
			List<Long> inclRuleIds = new ArrayList<Long>();
			Set<Long> ruleIds = new HashSet<Long>(genReq.getRuleIds());
			for (Rule r : rules) {
				if (ruleIds.contains(r.getId())) {
					inclRules.add(r);
					inclRuleIds.add(r.getId());
				}
			}
			
			// eval rules and return result
			List<FuzzyClass> fclses = fclsDao.listFClsForShop(shop);
			List<String> sortedProds = ExpSys.sortProducts(inclProds, inclRules, fclses);
			Ranking rnk = new Ranking();
			rnk.setIntegIds(sortedProds);
			rnk.setRuleIds(inclRuleIds);

			mapper.writeResponse(resp.getOutputStream(), Status.OK, "Sorting done!", rnk);
			return;
			
		} catch (Exception e) {
			mapper.writeResponse(resp.getOutputStream(), Status.ERROR, "Invalid JSON format.");
			e.printStackTrace();
			return;
		}
	}
	
}
