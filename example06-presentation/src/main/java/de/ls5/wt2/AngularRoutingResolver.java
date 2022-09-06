package de.ls5.wt2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import java.io.IOException;

@Controller
public class AngularRoutingResolver  {



    @RequestMapping("/l")
    public  String index(){
        System.out.println("bbbbbbbb");
        return "index.html" ;
    }

  /*  @GetMapping(path = "index")
    public void test(){
        System.out.println("test");
    }*/




   /*@Bean
    public ClassLoaderTemplateResolver  getViewResolver() {
       ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver ();
        resolver.setPrefix("templates/static/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCheckExistence(true);
        return resolver;
    }

    @RequestMapping("/account")
    public String login(){
        System.out.println("aaaaaaaaaaaaaaaaa");
        return  "/";
    }

    @RequestMapping("/e")
    public String error(){
        System.out.println("aaaaaaaaaaaaaaaaa");
        return "static/error.html";
    }
*/
}