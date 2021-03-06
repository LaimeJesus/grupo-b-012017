package model.recommendations;

import java.util.List;

import model.products.Product;
import util.Entity;

public class Recommendation extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1333900215189403767L;
	private Product relatedProduct;
	private List<Product> recommended;
	
	public Recommendation (Product aProduct , List<Product> recommendedProducts) {
		this.setRelatedProduct(aProduct);
		this.setRecommended(recommendedProducts);
	}
	
	public Recommendation () {
		
	}

	public Product getRelatedProduct() {
		return relatedProduct;
	}

	public void setRelatedProduct(Product relatedProduct) {
		this.relatedProduct = relatedProduct;
	}

	public List<Product> getRecommended() {
		return recommended;
	}

	public void setRecommended(List<Product> recommended) {
		this.recommended = recommended;
	}

	@Override
	public String toString() {
		return "Recommendation for: " + this.getRelatedProduct().toString();
	}
	
}
