package controllers;

import play.mvc.Security;
import play.mvc.Http.Context;
import play.mvc.Result;

public class Authenticator extends Security.Authenticator{
	
	@Override
	public String getUsername(Context ctx){
		return ctx.session().get("username");
	}
	
	@Override
	public Result onUnauthorized(Context arg0){
		return redirect(routes.Application.authentication());
	}

}
