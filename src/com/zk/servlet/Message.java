package com.zk.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
                                                         
public class Message extends HttpServlet{
	
	public static Map<String, String> users = new HashMap<String,String>();
	public static Set<String> userskeys = new HashSet<String>() { };
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		System.out.println("执行get方法");
		
		//接收用户传来的账号信息
		String method = req.getParameter("method");
		switch (method) {
		case "login":
			login(req, resp);
			break;
		case "register":
			register(req, resp);
			break;
		default:
			break;
		}
	}
		
	public void login(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		String name = req.getParameter("username")==null?"":req.getParameter("username");
		String pass = req.getParameter("password")==null?"":req.getParameter("password");
		//账号匹配，用接收到的账号信息和数据库中的账号信息进行匹配
		//登录验证
		if(users.containsKey(name)&&users.get(name).equals(pass)){
			String sessionname = req.getSession().getAttribute("name")==null?"":(String) req.getSession().getAttribute("name");
			if( userskeys.contains(name) ){
				req.setAttribute("mess","登陆失败，该用户已经登录过系统，正在返回，请重新登陆。");
				req.setAttribute("url","index.jsp");
				req.getRequestDispatcher("user_status.jsp").forward(req, resp);
			}else{
				req.getSession().setAttribute("name", name);
				userskeys.add(name);
				// 将页面转发到login_ok.jsp,并且携带requset和response对象信息
				req.setAttribute("mess","登陆成功,即将转向首页");
				req.setAttribute("url","main.jsp");
				req.getRequestDispatcher("user_status.jsp").forward(req, resp);
			}
		} else {
			req.setAttribute("mess","登陆失败，用户名或密码错误，正在返回，请重新登陆。");
			req.setAttribute("url","index.jsp");
			req.getRequestDispatcher("user_status.jsp").forward(req, resp);
		}
	}
	
	public void register(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		String name = req.getParameter("username")==null?"":req.getParameter("username");
		String pass = req.getParameter("password")==null?"":req.getParameter("password");
		if( name.equals("") || pass.equals("") ){
			req.setAttribute("mess","注册失败,请输入正确的用户名和密码。");
			req.setAttribute("url","register.jsp");
			req.getRequestDispatcher("user_status.jsp").forward(req, resp);
		} else if ( users.containsKey(name) ){
			req.setAttribute("mess","注册失败,用户名已被占用。");
			req.setAttribute("url","register.jsp");
			req.getRequestDispatcher("user_status.jsp").forward(req, resp);
		} else {
			users.put(name, pass);
			req.setAttribute("mess","注册成功，即将转向登陆页面");
			req.setAttribute("url","index.jsp");
			req.getRequestDispatcher("user_status.jsp").forward(req, resp);
		}
	}
}
