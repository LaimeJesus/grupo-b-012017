package services.microservices;

import javax.annotation.PreDestroy;

import org.joda.time.Duration;
import org.springframework.transaction.annotation.Transactional;

import exceptions.CanNotGetCashRegister;
import exceptions.InvalidSelectedProduct;
import model.products.ProductList;
import model.registers.CashRegisterManager;
import model.registers.PurchaseRecord;
import model.users.User;
import rest.dtos.users.WaitingUser;

public class ShopService{
	
	private ProductService productService;
	private UserService userService;
	private ProductListService productListService;
	private CashRegisterManager cashRegisterManager;
	
	@Transactional
	public void initialize(Integer n){
		setCashRegisterManager(new CashRegisterManager(n));
	}
	
	public CashRegisterManager getCashRegisterManager() {
		return cashRegisterManager;
	}

	public void setCashRegisterManager(CashRegisterManager cashRegisterManager) {
		this.cashRegisterManager = cashRegisterManager;
	}
	
	@Transactional
	public WaitingUser ready(User user, ProductList pl) throws InvalidSelectedProduct, CanNotGetCashRegister{
		productService.updateStock(pl);
		return cashRegisterManager.queueUserWithAProductlist(user, pl);
	}
	
	@Transactional
	public void shop(User user, ProductList productList){
		user.newPurchase(new PurchaseRecord(productList));		
		userService.update(user);
	}
	
	@Transactional
	public Duration waitingTime(User u, ProductList p) throws CanNotGetCashRegister{
		return cashRegisterManager.getWaitingTime(p);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public ProductListService getProductListService() {
		return productListService;
	}

	public void setProductListService(ProductListService productListService) {
		this.productListService = productListService;
	}
	
	@PreDestroy
	public void stop(){
		cashRegisterManager.stop();
	}
}
