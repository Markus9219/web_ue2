package controllers;

import play.Logger;
import play.mvc.Security;
import play.mvc.Http.Context;
import play.mvc.Result;

public class Authenticator extends Security.Authenticator{
	
	@Override
	public String getUsername(Context ctx){
		Logger.debug("Authenticator.getUsername: " + ctx.session().get("username"));
		return ctx.session().get("username");
	}
	
	@Override
	public Result onUnauthorized(Context arg0){
		Logger.debug("Authenticator.onUnauthorized");
		return redirect(routes.AuthenticationController.authentication());
	}

}
