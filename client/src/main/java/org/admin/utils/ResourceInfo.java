package org.admin.utils;


public class ResourceInfo extends Response{
	private Long id;
	private String name;
	private String description;
	private Double price;
	
	public Boolean equals(ResourceInfo other){
		return this.id == other.getId();
	}

	public Long getId()				{return this.id;}
	public String getName()			{return this.name;}
	public String getDescription()	{return this.description;}
	public Double getPrice()		{return this.price;}
	
	public void setId(Long id)						{this.id = id;}
	public void setName(String name)				{this.name = name;}
	public void setDescription(String description)	{this.description = description;}
	public void setPrice(Double price)				{this.price = price;}
	

}