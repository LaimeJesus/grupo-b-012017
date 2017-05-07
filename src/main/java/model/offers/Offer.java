package model.offers;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import exceptions.MoneyCannotSubstractException;
import model.products.ProductList;
import util.Entity;
import util.Money;

public abstract class Offer extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 846644012300118458L;
	private Integer discountRate;
	private Interval validPeriod;
	
	public Offer(Integer aDiscountRate , Interval anInterval) {
		this.setDiscountRate(aDiscountRate);
		this.setValidPeriod(anInterval);
	}
	
	public abstract Money getPreviousPrice(ProductList productListToGetPrice);
	
	protected abstract boolean verifyProductRequirements(ProductList productListToVerify);
	
	public Boolean meetRequirements(ProductList productListToVerify){
		return (this.verifyProductRequirements(productListToVerify) &&
				this.verifyOfferRequiremets(productListToVerify) &&
				this.verifyPeriodRequirements(productListToVerify));
	}
	
	private boolean verifyPeriodRequirements(ProductList productListToVerify) {
		DateTime today = DateTime.now();
		return this.getValidPeriod().contains(today);
	}

	private boolean verifyOfferRequiremets(ProductList productListToVerify) {
		return productListToVerify.isApplicable(this);
	}

	public Money getFinalPrice(ProductList productListToGetPrice) throws MoneyCannotSubstractException {
		return productListToGetPrice.getTotalAmount().minus(this.getDiscount(discountRate , productListToGetPrice));
	}

	public Money getDiscount(Integer discountRate , ProductList productListToGetPrice) {
		return this.getPreviousPrice(productListToGetPrice).percentage(discountRate);
	}
	
	
	
	public Offer() {
		
	}
	
	public Integer getDiscountRate() {
		return this.discountRate;
	}
	
	public void setDiscountRate(Integer newDiscountRate) {
		this.discountRate = newDiscountRate;
	}

	public Interval getValidPeriod() {
		return validPeriod;
	}

	public void setValidPeriod(Interval validPeriod) {
		this.validPeriod = validPeriod;
	}
}
