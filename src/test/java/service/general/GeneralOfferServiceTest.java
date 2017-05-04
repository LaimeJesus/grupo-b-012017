package service.general;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import model.offers.CategoryOffer;
import model.offers.Offer;
import services.general.GeneralOfferService;
import services.microservices.CategoryOfferService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/META-INF/spring-persistence-context.xml", "classpath*:/META-INF/spring-services-context.xml" })
public class GeneralOfferServiceTest {
	
	
	@Autowired
    @Qualifier("services.general.generalofferservice")
    private GeneralOfferService generalOfferService;
	
	@Autowired
    @Qualifier("services.microservices.categoryofferservice")
    private CategoryOfferService categoryOfferService;
	
    @Test
    public void testGeneralOfferServiceCanSaveAnyOffer(){
    	Offer someOffer = new CategoryOffer();
    	
    	generalOfferService.save(someOffer);
    	
    	Assert.assertEquals(1 , categoryOfferService.retriveAll().size());  	
    }

}
