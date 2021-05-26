package io.eventuate.examples.tram.ordersandcustomers.commonswagger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class RedirectSwaggerController {

  @GetMapping("/swagger-ui.html")
  public ModelAndView redirectWithUsingRedirectPrefix(ModelMap model) {
    return new ModelAndView("redirect:/swagger-ui/index.html", model);
  }
}