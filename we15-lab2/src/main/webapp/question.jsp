<%@page import="at.ac.tuwien.big.we15.lab2.api.Question"%>
<%@page import="at.ac.tuwien.big.we15.lab2.api.Category"%>
<%@page import ="at.ac.tuwien.big.we15.lab2.api.Avatar"%>
<%@page import ="at.ac.tuwien.big.we15.lab2.api.Answer"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="gameBean" scope="session" type="at.ac.tuwien.big.we15.lab2.api.impl.GameBeanImpl" class="at.ac.tuwien.big.we15.lab2.api.impl.GameBeanImpl"/>



<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Business Informatics Group Jeopardy! - Fragenbeantwortung</title>
        <link rel="stylesheet" type="text/css" href="style/base.css" />
        <link rel="stylesheet" type="text/css" href="style/screen.css" />
        <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/framework.js" type="text/javascript"></script>
    </head>
    <body id="questionpage">
      <a class="accessibility" href="#questions">Zur Fragenauswahl springen</a>
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
				<li><a class="orangelink navigationlink" id="logoutlink" title="Klicke hier um dich abzumelden" href="login.xhtml" accesskey="l">Abmelden</a></li>
			</ul>
		</nav>
      
      <!-- Content -->
       <% 
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
      
      <div role="main"> 
         <!-- info -->
         <section id="gameinfo" aria-labelledby="gameinfoinfoheading">
            <h2 id="gameinfoinfoheading" class="accessibility">Spielinformationen</h2>
            <section id="firstplayer" class="playerinfo leader" aria-labelledby="firstplayerheading">
               <h3 id="firstplayerheading" class="accessibility">Führender Spieler</h3>
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
            <p id="round">Frage: <%= gameBean.getCurrentRound() %> / 10</p>
         </section>
            
      <!-- Question -->
      
      <% Question currentQuestion = gameBean.getActiveQuestion();%>
      
      <section id="question" aria-labelledby="questionheading">
            <form id="questionform" action="BigJeopardyServlet?action=answerQuestion" method="post">
               <h2 id="questionheading" class="accessibility">Frage</h2>
               <p id="questiontype"><%=currentQuestion.getCategory().getName() %> für € <%=currentQuestion.getValue() %></p>
               <p id="questiontext"><%=currentQuestion.getText()%></p>
               <ul id="answers">
               		<% int i = 1; for(Answer answer : currentQuestion.getAllAnswers()){
               			i++;
               			%><li>
               			<input name="answerIds" value="<%=i %>" id="answer_<%=i%>" type="checkbox"/><label class="tile clickable" for="answer_<%=i%>"><%=answer.getText()%></label></li>
               		<% 
               		}           
               		%>
               </ul>
               <input id="timeleftvalue" type="hidden" value="100"/>
               <input class="greenlink formlink clickable" name="answer_submit" id="next" type="submit" value="antworten" accesskey="s"/>
                              
            </form>
         </section>
            
         <section id="timer" aria-labelledby="timerheading">
            <h2 id="timerheading" class="accessibility">Timer</h2>
            <p><span id="timeleftlabel">Verbleibende Zeit:</span> <time id="timeleft">00:30</time></p>
            <meter id="timermeter" min="0" low="20" value="100" max="100"/>
         </section>
      </div>

      <!-- footer -->
      <footer role="contentinfo">© 2015 BIG Jeopardy!</footer>
        
      <script type="text/javascript">
            //<![CDATA[
            
            // initialize time
            $(document).ready(function() {
                var maxtime = 30;
                var hiddenInput = $("#timeleftvalue");
                var meter = $("#timer meter");
                var timeleft = $("#timeleft");
                
                hiddenInput.val(maxtime);
                meter.val(maxtime);
                meter.attr('max', maxtime);
                meter.attr('low', maxtime/100*20);
                timeleft.text(secToMMSS(maxtime));
            });
            
            // update time
            function timeStep() {
                var hiddenInput = $("#timeleftvalue");
                var meter = $("#timer meter");
                var timeleft = $("#timeleft");
                
                var value = $("#timeleftvalue").val();
                if(value > 0) {
                    value = value - 1;   
                }
                
                hiddenInput.val(value);
                meter.val(value);
                timeleft.text(secToMMSS(value));
                
                if(value <= 0) {
                    $('#questionform').submit();
                }
            }
            
            window.setInterval(timeStep, 1000);
            
            //]]>
        </script>
    </body>
</html>
