package imaing.expsys.server.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RESTController {

	@RequestMapping(value = "/v1/hello", method = RequestMethod.GET)
	public void getMe(@RequestBody String body, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().write("hello\n" + body + "\n");
	}
}
