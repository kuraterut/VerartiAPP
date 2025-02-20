package org.admin.utils.entities;


import org.admin.utils.Response;

public class ResourceRequest extends Response {
	private Long id;
	private Resource resource;
	private Integer count;
	private Integer status;
	private String statusDescription;
	private String comment;
	
	public Boolean equals(ResourceRequest other){
		return this.id == other.getId();
	}

	public Long getId()				 	{return this.id;}
	public Resource getResource()	{return this.resource;}
	public Integer getCount()			{return this.count;}
	public Integer getStatus()			{return this.status;}
	public String getStatusDescription(){return this.statusDescription;}
	public String getComment()			{return this.comment;}
	
	public void setId(Long id)									{this.id = id;}
	public void setResource(Resource resource)				{this.resource = resource;}
	public void setCount(Integer count)							{this.count = count;}
	public void setStatus(Integer status)						{this.status = status;}
	public void setComment(String comment)						{this.comment = comment;}
	public void setStatusDescription(String statusDescription)	{this.statusDescription = statusDescription;}
	

}