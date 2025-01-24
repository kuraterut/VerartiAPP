package src.admin.utils;

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

	public Integer getCode()			{return this.code;}
	public String getMsg()			{return this.msg;}
	
	public void setCode(Integer code)	{this.code = code;}
	public void setMsg(String msg)	{this.msg = msg;}
	
}