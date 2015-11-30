package finalProject.main;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController/* extends WebMvcConfigurerAdapter */{
	/*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }*/

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String showForm(Video video) {
        return "form";
    }

    @RequestMapping(value="/", method=RequestMethod.POST)
    public String checkVideoInfo(@Valid Video video, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        return "redirect:/progress";
    }
}
