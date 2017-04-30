package services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import model.products.Product;
import services.microservices.ProductService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/META-INF/spring-persistence-context.xml", "classpath*:/META-INF/spring-services-context.xml" })
public class ProductServiceTest {

    @Autowired
    @Qualifier("services.microservices.productservice")
    private ProductService productService;

    @Test
    public void testProductCanBeSaved(){
        productService.save(new Product());
        Assert.assertEquals(1, productService.retriveAll().size());
    }

}