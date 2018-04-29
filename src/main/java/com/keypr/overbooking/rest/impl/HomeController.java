package com.keypr.overbooking.rest.impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Kirill Bazarov (es.kelevra@gmail.com)
  *
 *
 * Controller for misc operations
 */
@ApiIgnore
@Controller
public class HomeController {

    @RequestMapping("/swagger")
    public String home() {
        return "redirect:/swagger-ui.html";
    }

}
