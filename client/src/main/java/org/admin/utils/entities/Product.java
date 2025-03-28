package org.admin.utils.entities;

import org.admin.utils.Response;

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
}