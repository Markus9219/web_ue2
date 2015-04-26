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
	}
	
	@Override
	public void init() throws ServletException {
        super.init();
        
        ServletContext servletcontext = getServletContext();
    	JeopardyFactory factory = new ServletJeopardyFactory(servletcontext);
    	QuestionDataProvider provider = factory.createQuestionDataProvider();
    	categories = provider.getCategoryData();
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GameBeanImpl bean = (GameBeanImpl) request.getSession(true).getAttribute("gameBean");
		String nextPage = "/login.jsp";
		
		if(bean == null) {
			bean = new GameBeanImpl();
			bean.setCategories(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
		request.getServletContext().setAttribute("categories", categories);
		
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("in der doPost methode");
		GameBeanImpl bean = (GameBeanImpl) request.getSession(true).getAttribute("gameBean");
		String nextPage = "/login.jsp";
		request.getServletContext().setAttribute("categories", categories);
		
		if(bean == null) {
			bean = new GameBeanImpl();
			bean.setCategories(categories);
			request.getSession().setAttribute("gameBean", bean);
		}
		
		String action = request.getParameter("action");
		
		if(action != null) {
			if(action.equals("newGame")) {
				System.out.println("STarte neues GAME");
				bean = new GameBeanImpl();
				bean.setCategories(categories);
				request.getSession().setAttribute("gameBean", bean);
				nextPage = "/jeopardy.jsp";
			}else if (action.equals("answerQuestion")) {
				String[] answerIds = request.getParameterValues("answerIds");
				List<Integer> answers = new ArrayList<Integer>();
				if(answerIds != null){
					for(String a:answerIds) {
						answers.add(Integer.parseInt(a));
					}
				}
				
				int roundBeforePlayerAnswer = bean.getCurrentRound();
				
				bean.setActivePlayer(bean.getPlayerAvatar());
				bean.answerQuestion(answers);
				
				int roundAfterPlayerAnswer = bean.getCurrentRound();
				
				if(roundBeforePlayerAnswer == roundAfterPlayerAnswer) {
					bean.setActivePlayer(bean.getNpcAvatar());
					bean.kiTurn();
				}
				
				int pScore = bean.getScorePlayer();
				int vScore = bean.getScoreNpc();
				
				if(vScore < pScore){
					bean.setActivePlayer(bean.getNpcAvatar());
					bean.kiTurn();
				}
				
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
