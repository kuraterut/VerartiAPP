package org.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response{
	private Integer code;
	private String msg;

	public Response(){
		this.code = 404;
		this.msg = "Ошибка подключения";
	}

	public Response(Integer code, String msg){
		this.code = code;
		this.msg = msg;
	}
}