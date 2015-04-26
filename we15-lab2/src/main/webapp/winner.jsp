<%@page import="at.ac.tuwien.big.we15.lab2.api.Question"%>
<%@page import="at.ac.tuwien.big.we15.lab2.api.Category"%>
<%@page import ="at.ac.tuwien.big.we15.lab2.api.Avatar"%>
<%@page import ="at.ac.tuwien.big.we15.lab2.api.Answer"%>
<%@page import="java.util.List"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="gameBean" scope="session" type="at.ac.tuwien.big.we15.lab2.api.impl.GameBean" class="at.ac.tuwien.big.we15.lab2.api.impl.GameBean"/>


<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="de" lang="de">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Business Informatics Group Jeopardy! - Gewinnanzeige</title>
        <link rel="stylesheet" type="text/css" href="style/base.css" />
        <link rel="stylesheet" type="text/css" href="style/screen.css" />
		  <script src="js/jquery.js" type="text/javascript"></script>
        <script src="js/framework.js" type="text/javascript"></script>
    </head>
    <body id="winner-page">
      <a class="accessibility" href="#winner">Zur Gewinnanzeige springen</a>
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
      
      <div role="main">
         <section id="gameinfo" aria-labelledby="winnerinfoheading">
            <h2 id="winnerinfoheading" class="accessibility">Gewinnerinformationen</h2>
            <p class="user-info positive-change"><%=gameBean.getMessageLog() %> +<%=gameBean.getActiveQuestion().getValue() %> €</p>
            <p class="user-info negative-change"><%=gameBean.getMessageLog() %> -<%=gameBean.getActiveQuestion().getValue() %> €</p>
            <section class="playerinfo leader" aria-labelledby="winnerannouncement">
               <h3 id="winnerannouncement">Gewinner: <%=gameBean.getWinner().getName() %></h3>
               <img class="avatar" src="<%=gameBean.getWinner().getImageFull() %>" alt="Spieler-Avatar <%=gameBean.getWinner().getName() %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%=gameBean.getWinner().getName() %></td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints">€ <%=gameBean.getScoreWinner() %></td>
                  </tr>
               </table>
            </section>
            <section class="playerinfo" aria-labelledby="loserheading">
               <h3 id="loserheading" class="accessibility">Verlierer: <%=gameBean.getLoser().getName() %></h3>
               <img class="avatar" src="<%=gameBean.getLoser().getImageHead() %>" alt="Spieler-Avatar <%=gameBean.getLoser().getName() %>" />
               <table>
                  <tr>
                     <th class="accessibility">Spielername</th>
                     <td class="playername"><%=gameBean.getLoser().getName() %></td>
                  </tr>
                  <tr>
                     <th class="accessibility">Spielerpunkte</th>
                     <td class="playerpoints">€ <%=gameBean.getScoreLoser() %></td>
                  </tr>
               </table>
            </section>
         </section>
         <section id="newgame" aria-labelledby="newgameheading">
             <h2 id="newgameheading" class="accessibility">Neues Spiel</h2>
         	<form action="BigJeopardyServlet?action=newGame" method="post">
               	<input class="clickable orangelink contentlink" id="new_game" type="submit" name="restart" value="Neues Spiel" />
            </form>
         </section>
      </div>
        <!-- footer -->
        <footer role="contentinfo">© 2015 BIG Jeopardy</footer>  
		<script type="text/javascript">
        //<![CDATA[
           $(document).ready(function(){
         	   if(supportsLocalStorage()){
         		   localStorage["lastGame"] = new Date().getTime();
         	   }
           });
        //]]>
        </script>  
    </body>
</html>
