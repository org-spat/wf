package org.spat.wf.mvc.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import org.spat.wf.mvc.ActionResult;
import org.spat.wf.mvc.BeatContext;
import org.spat.wf.mvc.User;
import org.spat.wf.mvc.WFController;
import org.spat.wf.mvc.action.ViewActionResult;
import org.spat.wf.mvc.annotation.GET;
import org.spat.wf.mvc.annotation.Login;
import org.spat.wf.mvc.annotation.Output;
import org.spat.wf.mvc.annotation.POST;
import org.spat.wf.mvc.annotation.Path;
import org.spat.wf.mvc.annotation.Setup;

@Login
public class HomeController extends WFController{
	
	@Setup
	@GET
	@Path("gethome")
	public ActionResult gethome(){
		return new ViewActionResult("gethome");
	}
	
	@Output
	@POST
	@Path("posthome")
	public ActionResult posthome(){
		return new ViewActionResult("posthome");
	}
	
	@Login
	@Output
	@Path("bothhome")
	public ActionResult bothhome(){
		System.out.println("============= both home, normal excute");
		
		return new ActionResult() {
			@Override
			public void render() {
				BeatContext beat = BeatContext.current();
				try {
					PrintWriter write = beat.getResponse().getWriter();
					write.println("============= both home result");
					System.out.println("============= both home result, after global interceptor");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	@GET
	@Path("home/{name}")
	public ActionResult home(final String name){
		String username = beat().getRequest().getParameter("name");
		int age = Integer.valueOf(beat().getRequest().getParameter("age"));
		String company = beat().getRequest().getParameter("company");
		
		User user = new User();
		user.setName(username);
		user.setAge(age);
		user.setCompany(company);
		System.out.println(user);
		
		return new ActionResult() {
			@Override
			public void render() {
				System.out.println("home/"+name+">>>>>>>>>>>>>>>>>>>>");
			}
		};
	} 

}

