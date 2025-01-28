package src.admin.utils;


public class ResourceRequestInfo extends Response{
	private Long id;
	private ResourceInfo resource;
	private Integer count;
	private Integer status;
	private String statusDescription;
	private String comment;
	
	public Boolean equals(ResourceRequestInfo other){
		return this.id == other.getId();
	}

	public Long getId()				 	{return this.id;}
	public ResourceInfo getResource()	{return this.resource;}
	public Integer getCount()			{return this.count;}
	public Integer getStatus()			{return this.status;}
	public String getStatusDescription()	{return this.statusDescription;}
	public String getComment()			{return this.comment;}
	
	public void setId(Long id)									{this.id = id;}
	public void setResource(ResourceInfo resource)				{this.resource = resource;}
	public void setCount(Integer count)							{this.count = count;}
	public void setStatus(Integer status)						{this.status = status;}
	public void setComment(String comment)						{this.comment = comment;}
	public void setStatusDescription(String statusDescription)	{this.statusDescription = statusDescription;}
	

}