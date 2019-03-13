package com.wf.ew.common.layui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LayTable<T> implements Serializable {
	
	private List<T> data;
	private int count;
	private String msg;
	private int code;

	public LayTable()
	{
		data=new ArrayList<T>();
		count=0;
		code=0;
		msg="";
	}

	public LayTable(List<T> l, int i)
	{
		data=l;
		count=i;
		code=0;
		msg="";
	}

	public LayTable(List<T> l, int i, int c, String m)
	{
		data=l;
		count=i;
		code=c;
		msg=m;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
