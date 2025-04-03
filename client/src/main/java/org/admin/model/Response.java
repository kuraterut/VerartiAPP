package org.admin.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class Response{
	private Integer code;
	private String msg;

	public Response(){
		this.code = 404;
		this.msg = "Ошибка подключения";
	}
}