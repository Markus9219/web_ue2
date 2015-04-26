<%@page import="at.ac.tuwien.big.we15.lab2.api.Question"%>
<%@page import="at.ac.tuwien.big.we15.lab2.api.Category"%>
<%@page import="at.ac.tuwien.big.we15.lab2.api.impl.*" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="gameBean" scope="session" class="at.ac.tuwien.big.we15.lab2.api.impl.GameBeanImpl"/>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Business Informatics Group Jeopardy! - Fragenauswahl</title>
        <link rel="stylesheet" type="text/css" href="style/base.css" />
        <link rel="stylesheet" type="text/css" href="style/screen.css" />
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/framework.js" type="text/javascript"></script>
   </head>
   <body id="selection-page">
      <a class="accessibility" href="#question-selection">Zur Fragenauswahl springen</a>
      <!-- Header -->
      <header role="banner" aria-labelledby="bannerheading">
         <h1 id="bannerheading">
            <span class="accessibility">Business Informatics Group </span><span class="gametitle">Jeopardy!</span>
         </h1>
      </header>
      
      <!-- Navigation -->
		<nav role="navigation" aria-labelledby="navheading">
			<h2 id="navheading" class="accessibility">Navigation</h2>
			<ul>
				<li><a class="orangelink navigationlink" id="logoutlink" title="Klicke hier um dich abzumelden" href="#" accesskey="l">Abmelden</a></li>
			</ul>
		</nav>
      
      <!-- Content -->
      <div role="main"> 
         <!-- info -->
         <section id="gameinfo" aria-labelledby="gameinfoinfoheading">
            <h2 id="gameinfoinfoheading" class="accessibility">Spielinformationen</h2>
            <section id="firstplayer" class="playerinfo leader" aria-labelledby="firstplayerheading">
               <h3 id="firstplayerheading" class="accessibility">Führender Spieler</h3>
               
               <% 
               System.out.println("mit request: " + request.getServletContext().getAttribute("test123"));
               System.out.println("ohne request: " + getServletContext().getAttribute("test123"));
               System.out.println("session: " + request.getSession().getAttribute("test123"));

               // Fuehrt der spieler oder der NPC
               int scoreNpc = gameBean.getScoreNpc();
               int scorePlayer = gameBean.getScorePlayer();
               
               String name1, name2, img1, img2;
               int score1, score2;
               
               if(scoreNpc > scorePlayer) {
            	   score1 = scoreNpc;
            	   name1 = gameBean.getNpcAvatar().getName();
            	   img1 = gameBean.getNpcAvatar().getImagePath();
            	   score2 = scorePlayer;
            	   name2 = gameBean.getPlayerAvatar().getName();
            	   img2 = gameBean.getPlayerAvatar().getImagePath();
               } else {
            	   score2 = scoreNpc;
            	   name2 = gameBean.getNpcAvatar().getName();
            	   img2 = gameBean.getNpcAvatar().getImagePath();
            	   score1 = scorePlayer;
            	   name1 = gameBean.getPlayerAvatar().getName();
            	   img1 = gameBean.getPlayerAvatar().getImagePath();
               }
               %>
               
               <img class="avatar" src="<%= img1 %>" alt="<%= name1 %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%= name1 %> (Du)</td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints"><%= score1 %> €</td>
                  </tr>
               </table>
            </section>
            <section id="secondplayer" class="playerinfo" aria-labelledby="secondplayerheading">
               <h3 id="secondplayerheading" class="accessibility">Zweiter Spieler</h3>
               <img class="avatar" src="<%= img2 %>" alt="<%= name2 %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%= name2 %></td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints"><%= score2 %> €</td>
                  </tr>
               </table>
            </section>
            <p id="round">Fragen: <%= gameBean.getCurrentRound() %> / 10</p>
         </section>

         <!-- Question -->
                           
         <section id="question-selection" aria-labelledby="questionheading">
            <h2 id="questionheading" class="black accessibility">Jeopardy</h2>
            <%
            List<Message> messageLog = gameBean.getMessageLog();
            
            // zeige nur die letzen drei
            for(int i = 0; i < 3; i++){
            	Message message;
            	if(messageLog.size()-3+i<0){
            		// dummy einfuegen falls weniger als 3 nachrichten
            		message = new Message("", MessageType.NEUTRAL);	
            	}else{
            		message = messageLog.get(messageLog.size()-3+i);
            	}
            	
            	String style;
            	
            	if(message.getType() == MessageType.POSITIVE)
            		style = "user-info positive-change";
            	else if (message.getType() == MessageType.NEGATIVE)
            		style = "user-info negative-change";
            	else
            		style = "user-info";
            	%>
            	<p class="<%= style %>"><%= message.getText() %></p>
            	<%
            }
            
            %>
           <form id="questionform" action="BigJeopardyServlet?action=selectQuestion" method="post">
               
               <fieldset>
               <legend class="accessibility">Fragenauswahl</legend>
               <% 
               System.out.println("test123: " + request.getServletContext().getAttribute("test123"));         
        
               List<Category> categories = (List<Category>) request.getServletContext().getAttribute("categories");
               
               System.out.println("category size: " + categories.size());
               
	         for(Category cat:categories) {
	        	 String catName = cat.getName();
	        	 List<Question> questions = cat.getQuestions();
	        	 %>
	        	 <section class="questioncategory" aria-labelledby="<%=catName%>heading">
	             <h3 id="<%=catName%>heading" class="tile category-title"><span class="accessibility">Kategorie: </span><%=catName%></h3>
	             <ol class="category_questions">
				 <%
	        	 for(Question question:questions){
	        		 int questionValue = question.getValue();
	        		 int questionId = question.getId();
	        		 %>
	                 <li><input name="questionId" id="question_<%=questionId%>" value="<%=questionId%>" type="radio" <%= (gameBean.wasAnswered(questionId)) ? "disabled=\"disabled\"" : "" %> /><label class="tile clickable" for="question_<%=questionId%>">€ <%=questionValue%></label></li>
	        		 <%
	        	 }
				 %>			
				 </ol>
				 </section>
				 <%
	          }
	          %>
		   
               </fieldset>               
               <input class="greenlink formlink clickable" name="question_submit" id="next" type="submit" value="wählen" accesskey="s" />
               
            </form>
         </section>
         
      <!-- muss kontrolliert werden -->
         <section id="lastgame" aria-labelledby="lastgameheading">
            <h2 id="lastgameheading" class="accessibility">Letztes Spielinfo</h2>
            <p>Letztes Spiel: Nie</p>
            <script type="text/javascript">
            		if (supportsLocalStorage()) {
                		if (localStorage.getItem('lastGame') != null) {
                    		$("div.lastG").replaceWith("<p>Letztes Spiel: " + $.datepicker.parseDate("dd-mm-yyyy", localStorage.getItem('lastGame')) + "</p>");
                		}
            		}
        	</script>
         </section>
		</div>
		
      <!-- footer -->
      <footer role="contentinfo">© 2015 BIG Jeopardy!</footer>
	  
	  <script type="text/javascript">
            //<![CDATA[
            
            // initialize time
            $(document).ready(function() {
                // set last game
                if(supportsLocalStorage()) {
	                var lastGameMillis = parseInt(localStorage['lastGame'])
	                if(!isNaN(parseInt(localStorage['lastGame']))){
	                    var lastGame = new Date(lastGameMillis);
	                	$("#lastgame p").replaceWith('<p>Letztes Spiel: <time datetime="'
	                			+ lastGame.toUTCString()
	                			+ '">'
	                			+ lastGame.toLocaleString()
	                			+ '</time></p>')
	                }
            	}
            });            
            //]]>
        </script>
    </body>
</html>
