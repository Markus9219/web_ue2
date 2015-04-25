package at.ac.tuwien.big.we15.lab2.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GameBean bean = (GameBean) request.getSession(true).getAttribute("gameBean");
		
		String nextPage = "/login.jsp";
		
		if(bean == null) {
			bean = new GameBean(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
		
		String action = request.getParameter("action");
		
		if(action != null) {
			if(action.equals("newGame")) {
				bean = new GameBean(categories);
				nextPage = "/jeopardy.jsp";
			}else if (action.equals("answerQuestion")) {
				String[] answerIds = request.getParameterValues("answerIds");
				List<Integer> answers = new ArrayList<Integer>();
				for(String a:answerIds) {
					answers.add(Integer.parseInt(a));
				}
				bean.answerQuestion(answers);
				if(bean.getWinner() != null) {
					nextPage = "/winner.jsp";
				}else{
					nextPage = "/jeopardy.jsp";
				}
			}else if (action.equals("selectQuestion")) {
				int qId = Integer.parseInt(request.getParameter("questionId"));
				bean.selectQuestion(qId);
				nextPage = "/question.jsp";
			}else if (action.equals("login")){
				nextPage = "/jeopardy.jsp";
			}
		}
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}
}
