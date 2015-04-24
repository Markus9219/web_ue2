package at.ac.tuwien.big.we15.lab2.servlet;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.GameBean;
import at.ac.tuwien.big.we15.lab2.api.impl.ServletJeopardyFactory;

public class BigJeopardyServlet extends HttpServlet{
	ServletContext servletcontext = getServletContext();
	JeopardyFactory factory = new ServletJeopardyFactory(servletcontext);
	QuestionDataProvider provider = factory.createQuestionDataProvider();
	List<Category> categories = provider.getCategoryData();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public BigJeopardyServlet(){

	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		GameBean bean = (GameBean) request.getSession(true).getAttribute("gameBean");
		
		if(bean == null) {
			bean = new GameBean(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		GameBean bean = (GameBean) request.getSession(true).getAttribute("gameBean");
		
		if(bean == null) {
			bean = new GameBean(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
		
		String action = request.getParameter("action");
		
		if(action != null) {
			if(action.equals("newGame")) {
				bean.startNewGame();
			}else if (action.equals("answerQuestion")) {
				
			}
		}
		
		
	}
}
