package neu.csye6225.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.dialect.SpringStandardDialect;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableWebMvc
@ComponentScan("neu.csye6225.controller")
public class ThymeleafSpringJavaConfig {
    static final Logger logger = LoggerFactory.getLogger(ThymeleafSpringJavaConfig.class);

    @Bean
    public ServletContextTemplateResolver thymeleafTemplateResolver() {
        ServletContextTemplateResolver thymeleafTemplateResolver = new ServletContextTemplateResolver();
        //thymeleafTemplateResolver.setPrefix("/WEB-INF/thymeleaf/");
        //thymeleafTemplateResolver.setSuffix(".html");
        thymeleafTemplateResolver.setTemplateMode("HTML5");
        thymeleafTemplateResolver.setOrder(1);
        return thymeleafTemplateResolver;
    }


//    @Bean
//    public SpringTemplateEngine thymeleafTemplateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        addTemlplateResolvers(templateEngine);
//        addDialects(templateEngine);
//        return templateEngine;
//    }
//
//    private void addDialects(SpringTemplateEngine templateEngine) {
//        Set<IDialect> dialects = new HashSet<IDialect>();
//        dialects.add(tiles2Dialect());
//        templateEngine.setAdditionalDialects(dialects);
//
//    }
//
//    private void addTemlplateResolvers(SpringTemplateEngine templateEngine) {
//        Set<ITemplateResolver> templateResolvers = new HashSet<ITemplateResolver>();
//        templateResolvers.add(thymeleafTemplateResolver());
//        templateResolvers.add(jspTemplateResolver());
//        templateEngine.setTemplateResolvers(templateResolvers);
//    }
//
//    @Bean
//    public ThymeleafViewResolver tilesViewResolver() {
//        ThymeleafViewResolver tilesViewResolver = new ThymeleafViewResolver();
//        tilesViewResolver.setViewClass(ThymeleafTilesView.class);
//        tilesViewResolver.setTemplateEngine(thymeleafTemplateEngine());
//        tilesViewResolver.setOrder(0);
//        return tilesViewResolver;
//    }
//
//    @Bean
//    public ThymeleafTilesConfigurer tilesConfigurer() {
//        ThymeleafTilesConfigurer tilesConfigurer = new ThymeleafTilesConfigurer();
//        String[] definitions = { "/WEB-INF/**/tiles-thymeleaf.xml" };
//        tilesConfigurer.setDefinitions(definitions);
//        return tilesConfigurer;
//    }
//
//    @Bean
//    public TilesDialect tiles2Dialect() {
//        return new TilesDialect();
//    }
//
//    @Bean
//    SpringStandardDialect spring3Dialect() {
//        return new SpringStandardDialect();
//    }

}
