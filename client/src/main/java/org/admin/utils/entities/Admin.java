package org.admin.utils.entities;

import org.admin.utils.Response;

public class Admin extends User {
	protected Boolean isMaster;
	
	public Boolean getIsMaster()				{return isMaster;}
	public void setIsMaster(Boolean isMaster)	{this.isMaster = isMaster;}
}