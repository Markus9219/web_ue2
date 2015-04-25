<%@page import="at.ac.tuwien.big.we15.lab2.api.Question"%>
<%@page import="at.ac.tuwien.big.we15.lab2.api.Category"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="gameBean" scope="session" type="at.ac.tuwien.big.we15.lab2.api.impl.GameBean" class="at.ac.tuwien.big.we15.lab2.api.impl.GameBean"/>

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
               <img class="avatar" src="<%= gameBean.getWinner().getImageHead() %>" alt="Spieler-Avatar <%=gameBean.getWinner().getName() %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%= gameBean.getPlayerAvatar().getName() %> (Du)</td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints"><%= gameBean.getScorePlayer() %> €</td>
                  </tr>
               </table>
            </section>
            <section id="secondplayer" class="playerinfo" aria-labelledby="secondplayerheading">
               <h3 id="secondplayerheading" class="accessibility">Zweiter Spieler</h3>
               <img class="avatar" src="img/avatar/deadpool_head.png" alt="Spieler-Avatar Deadpool" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%=gameBean.getNpcAvatar().getName() %></td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints"><%= gameBean.getScoreNpc() %> €</td>
                  </tr>
               </table>
            </section>
            <p id="round">Fragen: <%= gameBean.getCurrentRound() %> / 10</p>
         </section>

         <!-- Question -->
                           
         <section id="question-selection" aria-labelledby="questionheading">
            <h2 id="questionheading" class="black accessibility">Jeopardy</h2>
            <p class="user-info positive-change"><%=gameBean.getMessageLog() %> +<%=gameBean.getActiveQuestion().getValue() %> €</p>
            <p class="user-info negative-change"><%=gameBean.getMessageLog() %> -<%=gameBean.getActiveQuestion().getValue() %> €</p>
            <p class="user-info"><%=gameBean.getNpcAvatar().getName() %> hat <%=gameBean.getActiveQuestion().getCategory() %> für € <%=gameBean.getActiveQuestion().getValue() %> gewählt.</p>
            <form id="questionform" action="BigJeopardyServlet" method="post">
               
               <fieldset>
               <legend class="accessibility">Fragenauswahl</legend>
               <% 
         List<Category> categories = gameBean.getQuestions();
         
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
                 <li><input name="question_selection" id="question_<%=questionId%>" value="<%=questionId%>" type="radio" disabled="disabled" /><label class="tile clickable" for="question_<%=questionId%>">€ <%=questionValue%></label></li>
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
               <input type="hidden" value="selectQuestion" name="action"/>
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
