package highscore;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import models.JeopardyGame;
import models.JeopardyUser;

public class HighscoreService {
	
	private static MessageFactory messageFactory;
	private static SOAPConnectionFactory soapConnectionFactory;
	
	private static String highscoreURL = "http://playground.big.tuwien.ac.at:8080/highscoreservice";
	private static String userKey = "3ke93-gue34-dkeu9";
	private static String dataNameSpace = "http://big.tuwien.ac.at/we/highscore/data";
	private static String dateFormat = "yyyy-MM-dd";
	
	public HighscoreService() {
		try {
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
			messageFactory = MessageFactory.newInstance();
		} catch (UnsupportedOperationException | SOAPException e) {
			e.printStackTrace();
		}
	}
	
	public static String postHighscore(JeopardyGame game) throws Exception{
		SOAPConnection conn = null;
		String uuid = "";
		
		try{
			conn = soapConnectionFactory.createConnection();
			SOAPMessage message = messageFactory.createMessage();
			SOAPPart soapPart = message.getSOAPPart();
			
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("data", dataNameSpace);
			
			SOAPBody body = envelope.getBody();
			fillBody(body, game);
			
			System.out.println();
            System.out.println("SOAP Message:");
            try {
				message.writeTo(System.out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println();
            System.out.flush();
            
            SOAPMessage response = conn.call(message,  highscoreURL);
            
            uuid = getUuidFromResponse(response);
		}catch(SOAPException e){
			throw new Exception("Exception blabla");
		}
		return uuid;
	}
	
	private static String getUuidFromResponse(SOAPMessage soapMessage){
		return "testMessage";
	}
	
	public static void fillBody(SOAPBody soapBody, JeopardyGame game) throws SOAPException {
		SOAPElement highscoreElement = soapBody.addChildElement("HighScoreRequest", "data");
		SOAPElement userkeyElem = highscoreElement.addChildElement("UserKey", "data");
		userkeyElem.addTextNode(userKey);
		
		SOAPElement userDataElement = highscoreElement.addChildElement(new QName(dataNameSpace, "UserData", "data"));
		SOAPElement winnerElement = userDataElement.addChildElement("Winner");
		SOAPElement loserElement = userDataElement.addChildElement("Loser");
		
		JeopardyUser loser = game.getLoser().getUser();
		String loserBirthdate = loser.getBirthDate() == null ? "" : (new SimpleDateFormat(dateFormat)).format(loser.getBirthDate());
		loser.setFirstName(loser.getFirstName() == null ? "" : loser.getFirstName());
		loser.setLastName(loser.getLastName() == null ? "" : loser.getLastName());
		
		JeopardyUser winner = game.getWinner().getUser();
		String winnerBirthdate = winner.getBirthDate() == null ? "" : (new SimpleDateFormat(dateFormat)).format(winner.getBirthDate());
		winner.setFirstName(winner.getFirstName() == null ? "" : winner.getFirstName());
        winner.setLastName(winner.getLastName() == null ? "" : winner.getLastName());
        
        loserElement.addAttribute(new QName("Gender"), loser.getGender().name());
        loserElement.addAttribute(new QName("Birthdate"), loserBirthdate);
        
        SOAPElement loserFirstname = loserElement.addChildElement("FirstName").addTextNode(loser.getFirstName());
        SOAPElement loserLastName = loserElement.addChildElement("LastName").addTextNode(loser.getLastName());

        SOAPElement loserPassword = loserElement.addChildElement("Password").addTextNode("");
        SOAPElement loserPoints = loserElement.addChildElement("Points").addTextNode(Integer.toString(game.getLoser().getProfit()));

        winnerElement.addAttribute(new QName("Gender"), winner.getGender().name());
        winnerElement.addAttribute(new QName("BirthDate"), winnerBirthdate);
        SOAPElement winnerFirstname = winnerElement.addChildElement("FirstName").addTextNode(winner.getFirstName());
        SOAPElement winnerLastName = winnerElement.addChildElement("LastName").addTextNode(winner.getLastName());

        SOAPElement winnerPassword = winnerElement.addChildElement("Password").addTextNode("");

        SOAPElement winnerPoints = winnerElement.addChildElement("Points").addTextNode(Integer.toString(game.getWinner().getProfit()));

	}


	
}
