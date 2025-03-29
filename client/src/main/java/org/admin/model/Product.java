package org.admin.model;

import org.json.simple.JSONObject;

public class Product extends Response {
	private Long id;
	private String name;
	private String description;
	private Long price;
	private Integer count;
	
	public Boolean equals(Product other){
		return this.id == other.getId();
	}

	public Long getId()				{return this.id;}
	public String getName()			{return this.name;}
	public String getDescription()	{return this.description;}
	public Long getPrice()		{return this.price;}
	public Integer getCount()		{return this.count;}
	
	public void setId(Long id)						{this.id = id;}
	public void setName(String name)				{this.name = name;}
	public void setDescription(String description)	{this.description = description;}
	public void setPrice(Long price)				{this.price = price;}
	public void setCount(Integer count)				{this.count = count;}


	public JSONObject toJson(){
		JSONObject obj = new JSONObject();

		obj.put("name", this.name);
		obj.put("description", this.description);
		obj.put("price", this.price);
		obj.put("count", this.count);

		return obj;
	}

	public static Product fromJson(JSONObject obj){
		Product product = new Product();

		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		String description = (String) obj.get("description");
		Long price = (Long) obj.get("price");
		Integer count = (Integer) obj.get("count");

		product.setId(id);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setCount(count);

		return product;
	}
}