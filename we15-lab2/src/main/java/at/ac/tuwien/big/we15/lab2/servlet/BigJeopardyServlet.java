package at.ac.tuwien.big.we15.lab2.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.GameBeanImpl;
import at.ac.tuwien.big.we15.lab2.api.impl.ServletJeopardyFactory;

public class BigJeopardyServlet extends HttpServlet{
	private List<Category> categories;

	private static final long serialVersionUID = 1L;
	
	
	public BigJeopardyServlet(){
		System.out.println("constructor");
//		getServletContext().setAttribute("test123", "abcdefghijklmnopqrstuvwxyz");
	}
	
	@Override
	public void init() throws ServletException {
        super.init();
        System.out.println("init");
        
        ServletContext servletcontext = getServletContext();
    	JeopardyFactory factory = new ServletJeopardyFactory(servletcontext);
    	QuestionDataProvider provider = factory.createQuestionDataProvider();
    	categories = provider.getCategoryData();
        
//        getServletContext().setAttribute("categories", categories);
//        getServletContext().setAttribute("test123", "abcdefghijklmnopqrstuvwxyz");
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("in der doGet Methode");
		GameBeanImpl bean = (GameBeanImpl) request.getSession(true).getAttribute("gameBean");
		request.getServletContext().setAttribute("test123", "abcdefghijklmnopqrstuvwxyz");
		request.getSession().setAttribute("test123", "ein text");
		if(bean == null) {
			bean = new GameBeanImpl();
			bean.setCategories(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
		request.getServletContext().setAttribute("categories", categories);
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in der doPost methode");
		GameBeanImpl bean = (GameBeanImpl) request.getSession(true).getAttribute("gameBean");
		request.getServletContext().setAttribute("test123", "abcdefghijklmnopqrstuvwxyz");
		request.getServletContext().setAttribute("categories", categories);
		String nextPage = "/login.jsp";
		
		if(bean == null) {
			bean = new GameBeanImpl();
			bean.setCategories(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
		
		String action = request.getParameter("action");
		
		if(action != null) {
			if(action.equals("newGame")) {
				bean = new GameBeanImpl();
				bean.setCategories(categories);
				nextPage = "/jeopardy.jsp";
			}else if (action.equals("answerQuestion")) {
				String[] answerIds = request.getParameterValues("answerIds");
				List<Integer> answers = new ArrayList<Integer>();
				if(answerIds != null){
					for(String a:answerIds) {
						answers.add(Integer.parseInt(a));
					}
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
		System.out.println("Weiterleiten auf " + nextPage);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}
}
