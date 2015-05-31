package highscore;

import models.JeopardyGame;
import models.JeopardyUser;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class HighscoreService {

	private static SOAPConnectionFactory soapConnectionFactory;
	private static MessageFactory messageFactory;
	private static String highscoreURL = "http://playground.big.tuwien.ac.at:8080/highscoreservice/PublishHighScoreService?wsdl";
	private static String dataNamespace = "http://big.tuwien.ac.at/we/highscore/data";
	private static String dateFormat = "yyyy-MM-dd";

	public HighscoreService() throws HighScoreException {
		try {
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			messageFactory = MessageFactory.newInstance();
		} catch (SOAPException e) {
			throw new HighScoreException("Could not create a SOAPConnection");
		} catch (UnsupportedOperationException e) {
			throw new HighScoreException("Unsupported Operation Exception");
		}
	}

	public static String postHighscore(JeopardyGame game) throws HighScoreException {

		SOAPConnection conn = null;
		String uuid = "";

		try {
			conn = soapConnectionFactory.createConnection();
			SOAPMessage soapMessage = messageFactory.createMessage();

			SOAPPart soapPart = soapMessage.getSOAPPart();

			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
			soapEnvelope.addNamespaceDeclaration("data", dataNamespace);

			SOAPBody soapBody = soapEnvelope.getBody();

			fillBody(soapBody, game);
			SOAPMessage response = conn.call(soapMessage, highscoreURL);

			uuid = getUuidFromResponse(response);

		} catch(SOAPException e) {
			throw new HighScoreException("Could not Open Connection or get UUID");
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SOAPException e) {
					throw new HighScoreException("Connection was not closed");
				}
			}
		}

		return uuid;
	}

	private static String getUuidFromResponse(SOAPMessage soapMessage) throws SOAPException {
		String uuid = "";

		try {
			SOAPBody body = soapMessage.getSOAPBody();
			Iterator it = body.getChildElements();
			while (it.hasNext()) {
				SOAPBodyElement elem = (SOAPBodyElement) it.next();
				uuid = elem.getValue();
			}
		} catch (SOAPException e) {
			throw new SOAPException("An error occured while trying to get UUID");
		}
		return uuid;
	}

	public static void fillBody(SOAPBody soapBody, JeopardyGame game) throws SOAPException {

		SOAPElement highscorerequestElement = soapBody.addChildElement("HighScoreRequest", "data");
		SOAPElement userkeyElement = highscorerequestElement.addChildElement("UserKey", "data");
		userkeyElement.addTextNode("3ke93-gue34-dkeu9");

		SOAPElement userDataElement = highscorerequestElement.addChildElement(new QName(dataNamespace, "UserData", "data"));
		SOAPElement loserElement = userDataElement.addChildElement("Loser");
		SOAPElement winnerElement = userDataElement.addChildElement("Winner");

		JeopardyUser winner = game.getWinner().getUser();
		String winnerBirthdate  = winner.getBirthDate() == null ? "1992-01-01" : (new SimpleDateFormat(dateFormat)).format(winner.getBirthDate());
		winner.setFirstName(winner.getFirstName() == null || winner.getFirstName().length() == 0 ? "Max" : winner.getFirstName());
		winner.setLastName(winner.getLastName() == null || winner.getLastName().length() == 0 ? "Mustermann" : winner.getLastName());
		JeopardyUser loser = game.getLoser().getUser();
		String loserBirthdate  = loser.getBirthDate() == null ? "1990-01-01" : (new SimpleDateFormat(dateFormat)).format(loser.getBirthDate());
		loser.setFirstName(loser.getFirstName() == null || loser.getFirstName().length() == 0 ? "Max" : loser.getFirstName());
		loser.setLastName(loser.getLastName() == null || loser.getLastName().length() == 0 ? "Mustermann" : loser.getLastName());

		winnerElement.addAttribute(new QName("Gender"), winner.getGender().name());
		winnerElement.addAttribute(new QName("BirthDate"), winnerBirthdate);
		SOAPElement winnerFirstname = winnerElement.addChildElement("FirstName").addTextNode(winner.getFirstName());
		SOAPElement winnerLastName = winnerElement.addChildElement("LastName").addTextNode(winner.getLastName());
		SOAPElement winnerPassword = winnerElement.addChildElement("Password").addTextNode("");
		SOAPElement winnerPoints = winnerElement.addChildElement("Points").addTextNode(Integer.toString(game.getWinner().getProfit()));
		loserElement.addAttribute(new QName("Gender"), loser.getGender().name());
		loserElement.addAttribute(new QName("BirthDate"), loserBirthdate);
		SOAPElement loserFirstname = loserElement.addChildElement("FirstName").addTextNode(loser.getFirstName());
		SOAPElement loserLastName = loserElement.addChildElement("LastName").addTextNode(loser.getLastName());
		SOAPElement loserPassword = loserElement.addChildElement("Password").addTextNode("");
		SOAPElement loserPoints = loserElement.addChildElement("Points").addTextNode(Integer.toString(game.getLoser().getProfit()));
	}
}