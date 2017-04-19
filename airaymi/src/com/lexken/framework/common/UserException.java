package com.lexken.framework.common;

public class UserException extends Exception{
	private int port=9999;
	public UserException(String msg){
		super(msg);
	}
	public UserException(String msg, int port){
		super(msg);
		this.port =port;
	}
	public int getPort(){
		return port;
	}
}
