package controllers;

import models.Answer;
import models.Category;
import models.JeopardyDAO;
import models.Question;

import java.util.List;
import java.util.Locale;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import at.ac.tuwien.big.we.dbpedia.api.DBPediaService;
import at.ac.tuwien.big.we.dbpedia.api.SelectQueryBuilder;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPedia;
import at.ac.tuwien.big.we.dbpedia.vocabulary.DBPediaOWL;
import play.Logger;


public class DBPediaQuestions {
	
	public static void createQuestions(){
		
		Category category = new Category();
		category.setName("Geographie", "de");
		category.setName("Geography", "en");
		
		Question question1 = new Question();
		question1.setValue(10);
		question1.setText("Welche Städte sind in Deutschland?", "de");
		question1.setText("Which cities are in Germany?", "en");
		
		
		// Check if DBpedia is available
		if(!DBPediaService.isAvailable()) { Logger.info("DBpedia is currently not available.");
		return; }
		
		//Question 1
		
		// build SPARQL-query
		SelectQueryBuilder cityQuery = DBPediaService.createQueryBuilder().setLimit(3)// at most five statements 
				.addWhereClause(RDF.type, DBPediaOWL.City)
				.addWhereClause(DBPediaOWL.country, DBPedia.createProperty("Germany"))
				.addFilterClause(RDFS.label, Locale.GERMAN) 
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		// retrieve data from dbpedia
		Model germanCities = DBPediaService.loadStatements(cityQuery.toQueryString());
		
		// get english and german city names, e.g., for right choices
		List<String> englishGermanCities = DBPediaService.getResourceNames(germanCities, Locale.ENGLISH);
		List<String> germanGermanCities = DBPediaService.getResourceNames(germanCities, Locale.GERMAN);
		Logger.info(cityQuery.toString());
		
		// alter query to get cities without germany
		cityQuery.removeWhereClause(DBPediaOWL.country, DBPedia.createProperty("Germany")); 
		cityQuery.addMinusClause(DBPediaOWL.country, DBPedia.createProperty("Germany"));
		
		// retrieve data from dbpedia
		Model noGermanCities = DBPediaService.loadStatements(cityQuery.toQueryString());
		
		// get english and german movie names, e.g., for wrong choices
		List<String> englishNoGermanCities = DBPediaService.getResourceNames(noGermanCities, Locale.ENGLISH);
		List<String> germanNoGermanCities = DBPediaService.getResourceNames(noGermanCities, Locale.GERMAN);
		
		for( int i = 0; i < englishGermanCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(true));
			answer.setTextEN(englishGermanCities.get(i));
			answer.setTextDE(germanGermanCities.get(i));
			question1.addRightAnswer(answer);
		}

		for( int i = 0; i < englishNoGermanCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(false));
			answer.setTextEN(englishNoGermanCities.get(i));
			answer.setTextDE(germanNoGermanCities.get(i));
			question1.addWrongAnswer(answer);
		}
		
		category.addQuestion(question1);
		Logger.info(cityQuery.toString());
		
		
		// Question 2
		
		Question question2 = new Question();
		question2.setValue(20);
		question2.setText("Welche Städte sind in Österreich?", "de");
		question2.setText("Which cities are in Austria?", "en");
				
		// build SPARQL-query
		SelectQueryBuilder cityQuery2 = DBPediaService.createQueryBuilder().setLimit(3)// at most five statements 
				.addWhereClause(RDF.type, DBPediaOWL.City)
				.addWhereClause(DBPediaOWL.country, DBPedia.createProperty("Austria"))
				.addFilterClause(RDFS.label, Locale.GERMAN) 
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		// retrieve data from dbpedia
		Model austriaCities = DBPediaService.loadStatements(cityQuery2.toQueryString());
		
		// get english and german city names, e.g., for right choices
		List<String> englishAustriaCities = DBPediaService.getResourceNames(austriaCities, Locale.ENGLISH);
		List<String> germanAustriaCities = DBPediaService.getResourceNames(austriaCities, Locale.GERMAN);		
		Logger.info(cityQuery2.toString());
		
		// alter query to get cities without germany
		cityQuery2.removeWhereClause(DBPediaOWL.country, DBPedia.createProperty("Austria")); 
		cityQuery2.addMinusClause(DBPediaOWL.country, DBPedia.createProperty("Austria"));
		
		// retrieve data from dbpedia
		Model noAustriaCities = DBPediaService.loadStatements(cityQuery2.toQueryString());
		
		// get english and german movie names, e.g., for wrong choices
		List<String> englishNoAustriaCities = DBPediaService.getResourceNames(noAustriaCities, Locale.ENGLISH);
		List<String> germanNoAustriaCities = DBPediaService.getResourceNames(noAustriaCities, Locale.GERMAN);
		
		for( int i = 0; i < englishAustriaCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(true));
			answer.setTextEN(englishAustriaCities.get(i));
			answer.setTextDE(germanAustriaCities.get(i));
			question2.addRightAnswer(answer);
		}

		for( int i = 0; i < englishNoAustriaCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(false));
			answer.setTextEN(englishNoAustriaCities.get(i));
			answer.setTextDE(germanNoAustriaCities.get(i));
			question2.addWrongAnswer(answer);
		}
		
		category.addQuestion(question2);		
		Logger.info(cityQuery2.toString());
		
		
		// Question 3
		
		Question question3 = new Question();
		question3.setValue(30);
		question3.setText("Welche Städte sind in Spanien?", "de");
		question3.setText("Which cities are in Spain?", "en");
						
		// build SPARQL-query
		SelectQueryBuilder cityQuery3 = DBPediaService.createQueryBuilder().setLimit(3)// at most three statements 
				.addWhereClause(RDF.type, DBPediaOWL.City)
				.addWhereClause(DBPediaOWL.country, DBPedia.createProperty("Spain"))
				.addFilterClause(RDFS.label, Locale.GERMAN) 
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		// retrieve data from dbpedia
		Model spainCities = DBPediaService.loadStatements(cityQuery3.toQueryString());
		
		// get english and german city names, e.g., for right choices
		List<String> englishSpainCities = DBPediaService.getResourceNames(spainCities, Locale.ENGLISH);
		List<String> germanSpainCities = DBPediaService.getResourceNames(spainCities, Locale.GERMAN);				
		Logger.info(cityQuery3.toString());
		
		// alter query to get cities without germany
		cityQuery3.removeWhereClause(DBPediaOWL.country, DBPedia.createProperty("Spain")); 
		cityQuery3.addMinusClause(DBPediaOWL.country, DBPedia.createProperty("Spain"));
		
		// retrieve data from dbpedia
		Model noSpainCities = DBPediaService.loadStatements(cityQuery3.toQueryString());
		
		// get english and german movie names, e.g., for wrong choices
		List<String> englishNoSpainCities = DBPediaService.getResourceNames(noSpainCities, Locale.ENGLISH);
		List<String> germanNoSpainCities = DBPediaService.getResourceNames(noSpainCities, Locale.GERMAN);
		
		for( int i = 0; i < englishSpainCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(true));
			answer.setTextEN(englishSpainCities.get(i));
			answer.setTextDE(germanSpainCities.get(i));
			question3.addRightAnswer(answer);
		}

		for( int i = 0; i < englishNoSpainCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(false));
			answer.setTextEN(englishNoSpainCities.get(i));
			answer.setTextDE(germanNoSpainCities.get(i));
			question3.addWrongAnswer(answer);
		}
		
		category.addQuestion(question3);				
		Logger.info(cityQuery3.toString());	
		
		
		// Question 4
		
		Question question4 = new Question();
		question4.setValue(40);
		question4.setText("Welche Städte sind in Polen?", "de");
		question4.setText("Which cities are in Poland?", "en");
						
		// build SPARQL-query
		SelectQueryBuilder cityQuery4 = DBPediaService.createQueryBuilder().setLimit(3)// at most three statements 
				.addWhereClause(RDF.type, DBPediaOWL.City)
				.addWhereClause(DBPediaOWL.country, DBPedia.createProperty("Poland"))
				.addFilterClause(RDFS.label, Locale.GERMAN) 
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		// retrieve data from dbpedia
		Model polandCities = DBPediaService.loadStatements(cityQuery4.toQueryString());
		
		// get english and german city names, e.g., for right choices
		List<String> englishPolandCities = DBPediaService.getResourceNames(polandCities, Locale.ENGLISH);
		List<String> germanPolandCities = DBPediaService.getResourceNames(polandCities, Locale.GERMAN);				
		Logger.info(cityQuery4.toString());
		
		// alter query to get cities without germany
		cityQuery4.removeWhereClause(DBPediaOWL.country, DBPedia.createProperty("Poland")); 
		cityQuery4.addMinusClause(DBPediaOWL.country, DBPedia.createProperty("Poland"));
		
		// retrieve data from dbpedia
		Model noPolandCities = DBPediaService.loadStatements(cityQuery4.toQueryString());
		
		// get english and german movie names, e.g., for wrong choices
		List<String> englishNoPolandCities = DBPediaService.getResourceNames(noPolandCities, Locale.ENGLISH);
		List<String> germanNoPolandCities = DBPediaService.getResourceNames(noPolandCities, Locale.GERMAN);
		
		for( int i = 0; i < englishPolandCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(true));
			answer.setTextEN(englishPolandCities.get(i));
			answer.setTextDE(germanPolandCities.get(i));
			question4.addRightAnswer(answer);
		}

		for( int i = 0; i < englishNoPolandCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(false));
			answer.setTextEN(englishNoPolandCities.get(i));
			answer.setTextDE(germanNoPolandCities.get(i));
			question4.addWrongAnswer(answer);
		}
		
		category.addQuestion(question4);				
		Logger.info(cityQuery4.toString());		
		
		
		// Question 5
		
		Question question5 = new Question();
		question5.setValue(50);
		question5.setText("Welche Städte sind in Portugal?", "de");
		question5.setText("Which cities are in Portuguese?", "en");
						
		// build SPARQL-query
		SelectQueryBuilder cityQuery5 = DBPediaService.createQueryBuilder().setLimit(3)// at most three statements 
				.addWhereClause(RDF.type, DBPediaOWL.City)
				.addWhereClause(DBPediaOWL.country, DBPedia.createProperty("Portugal"))
				.addFilterClause(RDFS.label, Locale.GERMAN) 
				.addFilterClause(RDFS.label, Locale.ENGLISH);
		
		// retrieve data from dbpedia
		Model portugalCities = DBPediaService.loadStatements(cityQuery5.toQueryString());
		
		// get english and german city names, e.g., for right choices
		List<String> englishPortugalCities = DBPediaService.getResourceNames(portugalCities, Locale.ENGLISH);
		List<String> germanPortugalCities = DBPediaService.getResourceNames(portugalCities, Locale.GERMAN);				
		Logger.info(cityQuery5.toString());
		
		// alter query to get cities without germany
		cityQuery5.removeWhereClause(DBPediaOWL.country, DBPedia.createProperty("Portugal")); 
		cityQuery5.addMinusClause(DBPediaOWL.country, DBPedia.createProperty("Portugal"));
		
		// retrieve data from dbpedia
		Model noPortugalCities = DBPediaService.loadStatements(cityQuery5.toQueryString());
		
		// get english and german movie names, e.g., for wrong choices
		List<String> englishNoPortugalCities = DBPediaService.getResourceNames(noPortugalCities, Locale.ENGLISH);
		List<String> germanNoPortugalCities = DBPediaService.getResourceNames(noPortugalCities, Locale.GERMAN);
		
		for( int i = 0; i < englishPortugalCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(true));
			answer.setTextEN(englishPortugalCities.get(i));
			answer.setTextDE(germanPortugalCities.get(i));
			question5.addRightAnswer(answer);
		}

		for( int i = 0; i < englishNoPortugalCities.size(); i++){
			Answer answer = new Answer();
			answer.setCorrectAnswer(new Boolean(false));
			answer.setTextEN(englishNoPortugalCities.get(i));
			answer.setTextDE(germanNoPortugalCities.get(i));
			question5.addWrongAnswer(answer);
		}
		
		category.addQuestion(question5);				
		Logger.info(cityQuery5.toString());
				
		
		JeopardyDAO.INSTANCE.persist(category);
	}

}
