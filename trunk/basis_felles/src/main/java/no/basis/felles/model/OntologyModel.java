package no.basis.felles.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import no.basis.felles.semanticweb.OntologyHelper;
/*import no.basis.felles.semanticweb.chess.BlackBoardPosition;
import no.basis.felles.semanticweb.chess.BlackPiece;
import no.basis.felles.semanticweb.chess.ChessFactory;
import no.basis.felles.semanticweb.chess.WhiteBoardPosition;
import no.basis.felles.semanticweb.chess.WhitePiece;*/
import no.chess.ontology.BlackBoardPosition;
import no.chess.ontology.BlackPiece;
import no.chess.ontology.ChessFactory;
import no.chess.ontology.ChessPosition;
import no.chess.ontology.Chessboard;
import no.chess.ontology.Taken;
import no.chess.ontology.Vacant;
import no.chess.ontology.WhiteBoardPosition;
import no.chess.ontology.WhitePiece;

//import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.protege.owl.codegeneration.inference.CodeGenerationInference;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.hp.hpl.jena.ontology.ConversionException;
/*import org.swrlapi.core.*;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.parser.SWRLParseException;
import org.swrlapi.sqwrl.SQWRLQueryEngine;
import org.swrlapi.sqwrl.SQWRLResult;
import org.swrlapi.sqwrl.exceptions.SQWRLException;
*/
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.sparql.core.Var;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
/* @author olj
 * This class reads and manipulates chosen ontology models
 * 
 * 
 */
public class OntologyModel {
	/**
	 * Contains ontology model with org.mindswap.pellet.jena Pelletreasoner
	 * .hp.hpl.jena.ontology.OntModel
	 */
	private OntModel model;
	
//	private OWLOntology owlModel = null;
	private OntologyHelper ontlogyHelper;
	private Resource chessGame;
	private OWLOntologyManager owlmanager = null;
	/**
	 * Contains owl ontology model. (org.semanticweb.owlapi.model.OWLOntology) It is used to create java classes of owl individuals.
	 * They represents the individuals in the chess game
	 */
	private OWLOntology ontModel = null;
	/**
	 * A class that serves as the entry point to the generated code providing access
	 * to existing individuals in the ontology and the ability to create new individuals in the ontology.
	 */
	private ChessFactory chessFactory = null;
	private Map<String,BlackBoardPosition> allBlackPositions;
	private Map<String,WhiteBoardPosition> allWhitePositions;
	private CodeGenerationInference owlInference;
	private Map<String,BlackPiece> allBlackPieces;
	private Map<String,WhitePiece> allWhitePieces;
	private Reasoner reasoner;
	private HashSet<Taken> allTakenPositions;
	private HashSet<Vacant> allVacantPositions;
	private HashSet<ChessPosition> allChessPositions;
	/**
	 * Used with infModel and contains rules from separate rules file.
	 */
	private GenericRuleReasoner genRulereasoner;
	private PelletReasoner clarkpelletReasoner;
	/**
	 * Contains ontology model with .hp.hpl.jena Reasoner General Rule reasoner with separate rules file
	 * .hp.hpl.jena.rdf.model.InfModel
	 */
	private InfModel infModel; // Contains the model and rules
	private ResultSet results = null; // Contains a resultset from querying the ontology
	
	public OntologyModel() {
		super();
		owlmanager = OWLManager.createOWLOntologyManager();
		this.model = fetchOntology();
		chessFactory = new ChessFactory(ontModel);
		owlInference = chessFactory.getInference();
//		clarkpelletReasoner = PelletReasonerFactory.getInstance().createReasoner(ontModel);
//		clarkpelletReasoner.getKB().realize();
//		clarkpelletReasoner.

		String format = "RDF/XML";

	}

	public ResultSet getResults() {
		return results;
	}


	public void setResults(ResultSet results) {
		this.results = results;
	}

	public GenericRuleReasoner getGenRulereasoner() {
		return genRulereasoner;
	}


	public void setGenRulereasoner(GenericRuleReasoner genRulereasoner) {
		this.genRulereasoner = genRulereasoner;
	}


	public InfModel getInfModel() {
		return infModel;
	}


	public void setInfModel(InfModel infModel) {
		this.infModel = infModel;
	}


	public Reasoner getReasoner() {
		return reasoner;
	}

	public void setReasoner(Reasoner reasoner) {
		this.reasoner = reasoner;
	}

	public Map<String, BlackPiece> getAllBlackPieces() {
		return allBlackPieces;
	}

	public void setAllBlackPieces(Map<String, BlackPiece> allBlackPieces) {
		this.allBlackPieces = allBlackPieces;
	}

	public Map<String, WhitePiece> getAllWhitePieces() {
		return allWhitePieces;
	}

	public void setAllWhitePieces(Map<String, WhitePiece> allWhitePieces) {
		this.allWhitePieces = allWhitePieces;
	}

	public CodeGenerationInference getOwlInference() {
		return owlInference;
	}

	public void setOwlInference(CodeGenerationInference owlInference) {
		this.owlInference = owlInference;
	}

	public Map<String, BlackBoardPosition> getAllBlackPositions() {
		return allBlackPositions;
	}

	public void setAllBlackPositions(
			Map<String, BlackBoardPosition> allBlackPositions) {
		this.allBlackPositions = allBlackPositions;
	}

	public Map<String, WhiteBoardPosition> getAllWhitePositions() {
		return allWhitePositions;
	}

	public void setAllWhitePositions(
			Map<String, WhiteBoardPosition> allWhitePositions) {
		this.allWhitePositions = allWhitePositions;
	}

	public ChessFactory getChessFactory() {
		return chessFactory;
	}

	public void setChessFactory(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}

/*	public OWLOntology getOwlModel() {
		return owlModel;
	}

	public void setOwlModel(OWLOntology owlModel) {
		this.owlModel = owlModel;
	}*/

	public OWLOntologyManager getOwlmanager() {
		return owlmanager;
	}

	public void setOwlmanager(OWLOntologyManager owlmanager) {
		this.owlmanager = owlmanager;
	}

	/**
	 * @return  OWLOntology type model
	 */
	public OWLOntology getOntModel() {
		return ontModel;
	}

	/**
	 * sets  OWLOntology type model
	 * @param ontModel
	 */
	public void setOntModel(OWLOntology ontModel) {
		this.ontModel = ontModel;
	}

	public Resource getChessGame() {
		return chessGame;
	}

	public void setChessGame(Resource chessGame) {
		this.chessGame = chessGame;
	}

	/**
	 * return Jena  type model
	 * @return Jena type model
	 */
	public OntModel getModel() {
		return model;
	}

	/**
	 * set Jena type model
	 * @param model
	 */
	public void setModel(OntModel model) {
		this.model = model;
	}
	/**
	 * getallBlackpositions
	 * @return all Blackboard positions in a HashSet
	 */
	@SuppressWarnings("unchecked")
	public HashSet<BlackBoardPosition> getallgivenBlackpositions(){
		
		return (HashSet<BlackBoardPosition>) chessFactory.getAllBlackBoardPositionInstances();
	}
	/**
	 * getallWhitepositions
	 * @return all Whiteboard positions 
	 */
	public HashSet<WhiteBoardPosition> getallgivenWhitepositions(){
		
		return (HashSet<WhiteBoardPosition>) chessFactory.getAllWhiteBoardPositionInstances();
	}
	public HashSet<BlackPiece> getallgivenBlackpieces(){
		return (HashSet<BlackPiece>) chessFactory.getAllBlackPieceInstances();
	}
	public HashSet<WhitePiece> getallgivenWhitepieces(){
		return (HashSet<WhitePiece>) chessFactory.getAllWhitePieceInstances();
	}
	public HashSet<Taken> getAllTakenPositions() {
		 allTakenPositions = (HashSet<Taken>) chessFactory.getAllTakenInstances();
		return allTakenPositions;
	}

	public void setAllTakenPositions(HashSet<Taken> allTakenPositions) {
		this.allTakenPositions = allTakenPositions;
	}

	public HashSet<Vacant> getAllVacantPositions() {
		allVacantPositions = (HashSet<Vacant>) chessFactory.getAllVacantInstances();
		return allVacantPositions;
	}

	public void setAllVacantPositions(HashSet<Vacant> allVacantPositions) {
		this.allVacantPositions = allVacantPositions;
	}
	
	public HashSet<ChessPosition> getAllChessPositions() {
		 allChessPositions = (HashSet<ChessPosition>) chessFactory.getAllChessPositionInstances();
		return allChessPositions;
	}

	public void setAllChessPositions(HashSet<ChessPosition> allChessPositions) {
		this.allChessPositions = allChessPositions;
	}

	/**
	 * fetchOntology()
	 * This method establishes the model in three variants:
	 * It fetches the chess ontology and stores it in both a jena ontology model (OntModel) with a Pellet reasoner as well as
	 * an hp.hpl.jena.rdf.model.InfModel with a Reasoner
	 * and an OWLOntology model
	 * It also populates the models and binds reasoners to the infModel object and the model object
	 * 
	 * @return
	 */
	public OntModel fetchOntology() {
//	      String inputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chess.owl";
//	      String inputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chess-rev01.owl";
	      String inputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chessnoimport.owl";
	      String inputFileFormat = "RDF/XML";
	      String reasoningLevel = "owl";//
//	      File ontFile = new File("C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chess.owl");
//	      File ontFile = new File("C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chess-rev01.owl");
	      File ontFile = new File("C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chessnoimport.owl");
	      try {
	    	  ontModel  = owlmanager.loadOntologyFromOntologyDocument(ontFile);
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      // Create a SWRL rule engine using the SWRLAPI Dette biblioteket skaper problemer:
// java.lang.IllegalAccessError: tried to access method com.google.common.collect.MapMaker.makeComputingMap(Lcom/google/common/base/Function;)Ljava/util/concurrent/ConcurrentMap; 	      
/*	      SWRLRuleEngine swrlRuleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontModel);
	      SQWRLQueryEngine queryEngine = SWRLAPIFactory.createSQWRLQueryEngine(ontModel);
	      swrlRuleEngine.infer();*/
/*	      try {
			SQWRLResult result = queryEngine.runSQWRLQuery("q1", "swrlb:add(?x, 2, 2) -> sqwrl:select(?x)");
		} catch (SQWRLException | SWRLParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	      //create an input stream for the input file
	      FileInputStream inputStream = null;
	      FileInputStream defaultModelStream = null;
	      PrintWriter writer = null;
	      try 
	      {
	         inputStream = new FileInputStream(inputFileName);
	      } catch (FileNotFoundException e) {
	         System.err.println("'" + inputFileName 
	            + "' is an invalid input file.");
	         return null;
	      }
	      try 
	      {
	    	  defaultModelStream = new FileInputStream(inputFileName);
	      } catch (FileNotFoundException e) {
	         System.err.println("'" + inputFileName 
	            + "' is an invalid input file.");
	         return null;
	      }	      
	      //create an output print writer for the results

	      
	      //create the appropriate jena model
	      OntModel model = null;
	     
	      if("none".equals(reasoningLevel.toLowerCase()))
	      {
	         /*
	          * "none" is jena model with OWL_DL
	          * ontologies loaded and no inference enabled
	          */
	         model = ModelFactory.createOntologyModel(
	            OntModelSpec.OWL_DL_MEM);
	      }
	      else if("rdfs".equals(reasoningLevel.toLowerCase()))
	      {
	         /*
	          * "rdfs" is jena model with OWL_DL
	          * ontologies loaded and RDFS inference enabled 
	          */
	         model = ModelFactory.createOntologyModel(
	            OntModelSpec.OWL_DL_MEM_RDFS_INF); 
	      }
	      else if("owl".equals(reasoningLevel.toLowerCase()))
	      {
	         /*
	          * "owl" is jena model with OWL_DL ontologies
	          * wrapped around a pellet-based inference model
	          */
	    	  System.out.println("Preparing model - owl reasoning ");
	    	  Model defaultModel = ModelFactory.createDefaultModel();
	          Resource configuration  = defaultModel.createResource();
//		         configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
		         configuration.addProperty(ReasonerVocabulary.PROPruleSet, "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\rules.txt");
		         configuration.addProperty(ReasonerVocabulary.PROPruleMode,"backward");
/*	         reasoner = 
	        		 org.mindswap.pellet.jena.PelletReasonerFactory.theInstance().create(configuration);*/
	         reasoner = 
	        		 org.mindswap.pellet.jena.PelletReasonerFactory.theInstance().create();
	         Model infxModel = ModelFactory.createInfModel(
	            reasoner, ModelFactory.createDefaultModel());
	         model = ModelFactory.createOntologyModel(
	            OntModelSpec.OWL_DL_MEM, infxModel);
	         model.read(inputStream, null, inputFileFormat);
	         reasoner = reasoner.bindSchema(model);
	         model.prepare();
	         
	         defaultModel.read(defaultModelStream, null, inputFileFormat);
	         
	
	         List<Rule> rules = Rule.rulesFromURL("C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\rules.txt");
	         
	         genRulereasoner = (GenericRuleReasoner) GenericRuleReasonerFactory.theInstance().create(configuration);
	         genRulereasoner.setRules(rules);
//	         String prefix = "@prefix <http://www.co-ode.org/ontologies/ont.owl>.";
//	         String rule = prefix + " [ruleFriends: (?p ont:WhitePiece ?p) (?y ont:WhitePiece ?y) -> (?p ont:isFriendOf ?y)]";
//	         genRulereasoner = new GenericRuleReasoner( Rule.rulesFromURL( "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\rules.txt" ) );
//	         genRulereasoner = new GenericRuleReasoner( Rule.parseRules(rule));
	         genRulereasoner.setDerivationLogging(true);
	         infModel = ModelFactory.createInfModel( genRulereasoner, defaultModel );
	         infModel.prepare();
	         System.out.println("Infmodel prepared ");
	         genRulereasoner.setTraceOn(true);
	         
	      }
	      else
	      {
	         //invalid inference setting
	         System.err.println("Invalid inference setting, "
	            + "choose one of <none|rdfs|owl>.");
	         return null;
	      }
	      
	      //load the facts into the model
	        System.out.println("Reading Model");	  
	      model.read(inputStream, null, inputFileFormat);
         System.out.println("Model read ");	      
	      chessGame = model.getResource("http://www.co-ode.org/ontologies/ont.owl#ChessGame");
	      //validate the file
	      ValidityReport validityReport = model.validate();
	      if(validityReport != null && !validityReport.isValid())
	      {
	         Iterator i = validityReport.getReports();
	         while(i.hasNext())
	         {
	            System.err.println("Feilmelding: "+
	               ((ValidityReport.Report)i.next()).getDescription());
	         }
	         System.out.println("returning null ");	  
	         return null;
	      }
	        System.out.println("returning model ");	  
	      return model;
	 }
	public void printOntology(){
		PrintWriter writer = null;
	    String outputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\individuals.txt";
	      //create an output print writer for the results
	      try 
	      {
	         writer = new PrintWriter(outputFileName);
	      } catch (FileNotFoundException e) {
	         System.err.println("'" + outputFileName 
	            + "' is an invalid output file.");
	         return;
	      }
	      ExtendedIterator iIndividuals = null;
	      try {
	    	  iIndividuals = model.listIndividuals();
	      } catch (ConversionException e) {
	    	  System.out.println("No individual "+e.getMessage());
	      }
	      while(iIndividuals != null && iIndividuals.hasNext())
	      {
	         Individual i = (Individual)iIndividuals.next();
	         printIndividual(i, writer);
	      }
//	      iIndividuals.close();
	      
	      writer.close();
	      model.close();
	      System.out.println("Printing individuals complete");
	      printStatements();
	}
	/**
	 * queryOntology
	 * This method queries the model
	 * Executes a query on the ontlogy using sparql
	 * @param q The query
	 * @return a List of QuerySolutions
	 */
	public  List<QuerySolution> queryOntology(String q){
		String queryString = q;
		List<QuerySolution> variables = new ArrayList();
		
		 Query query = QueryFactory.create(queryString) ;
		 results = null;
//		 QuerySolutionMap solutionMap = new QuerySolutionMap();
		 QuerySolution soln = null;
//		 Statement stmt = 
//		 infModel.getDerivation(arg0)
	       try 
	        {
	            query = QueryFactory.create(queryString) ;
	            QueryExecution qexec = QueryExecutionFactory.create(query, infModel) ; // or model or infModel!!
	            results = qexec.execSelect() ;
//	            soln = results.nextSolution() ;
//	            Literal l = soln.getLiteral("subject");
//	            Resource res = soln.getResource("subject");
	           
//	            System.out.println(res.toString()+" "+res.getLocalName());
//	            solutionMap.addAll(soln);
//	            ResultSetFormatter.out(results);
	        }catch(Exception ex) { 
	         ex.printStackTrace();
	        }
	       if (results != null){
		       while (results.hasNext()  ){
		    	   soln = results.next();
		    	   variables.add(soln);
		       }
   	       }
//	       ResultSetFormatter.out(results);
	       return variables;
	}
	 public void printIndividual(
		      Individual i, PrintWriter writer)
		   {
		      //print the local name of the individual (to keep it terse)
		      writer.println("Individual: " + i.getLocalName());
		      
		      //print the statements about this individual
		      StmtIterator iProperties = i.listProperties();
		      while(iProperties.hasNext())
		      {
		         Statement s = (Statement)iProperties.next();
		         writer.println("  " + s.getPredicate().getLocalName() 
		            + " : " + s.getObject().toString());
		      }
		      iProperties.close();
		      writer.println();
		   }
	 public void printStatements(){
			PrintWriter writer = null;
		    String outputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\statements.txt";
		      //create an output print writer for the results
		      try 
		      {
		         writer = new PrintWriter(outputFileName);
		      } catch (FileNotFoundException e) {
		         System.err.println("'" + outputFileName 
		            + "' is an invalid output file.");
		         return;
		      }		
		  	StmtIterator it = infModel.listStatements();
			while ( it.hasNext() )
				{
					Statement stmt = it.nextStatement();
					
					Resource subject = stmt.getSubject();
					Property predicate = stmt.getPredicate();
					RDFNode object = stmt.getObject();
					String name = subject.getLocalName();
					String predicateName = predicate.getLocalName();
					writer.println("Subject name: "+name+" Predicate name: "+predicateName);
					writer.println( subject.toString() + " " + predicate.toString() + " " + object.toString() );
				}	
			writer.println();
		    writer.close();
		    infModel.close();
	 }
	 public void fetchandprintOntology(){
//	      String inputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chess.owl";
//	      String inputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chess-rev01.owl";
	      String inputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\chessnoimport.owl";
	      String inputFileFormat = "RDF/XML";
	      String outputFileName = "C:\\Users\\bruker\\Google Drive\\privat\\ontologies\\individuals.txt";
	      String reasoningLevel = "none";

	      //create an input stream for the input file
	      FileInputStream inputStream = null;
	      PrintWriter writer = null;
	      try 
	      {
	         inputStream = new FileInputStream(inputFileName);
	      } catch (FileNotFoundException e) {
	         System.err.println("'" + inputFileName 
	            + "' is an invalid input file.");
	         return;
	      }
	      
	      //create an output print writer for the results
	      try 
	      {
	         writer = new PrintWriter(outputFileName);
	      } catch (FileNotFoundException e) {
	         System.err.println("'" + outputFileName 
	            + "' is an invalid output file.");
	         return;
	      }
	      
	      //create the appropriate jena model
	      OntModel model = null;
	     
	      if("none".equals(reasoningLevel.toLowerCase()))
	      {
	         /*
	          * "none" is jena model with OWL_DL
	          * ontologies loaded and no inference enabled
	          */
	         model = ModelFactory.createOntologyModel(
	            OntModelSpec.OWL_DL_MEM);
	      }
	      else if("rdfs".equals(reasoningLevel.toLowerCase()))
	      {
	         /*
	          * "rdfs" is jena model with OWL_DL
	          * ontologies loaded and RDFS inference enabled 
	          */
	         model = ModelFactory.createOntologyModel(
	            OntModelSpec.OWL_DL_MEM_RDFS_INF); 
	      }
	      else if("owl".equals(reasoningLevel.toLowerCase()))
	      {
	         /*
	          * "owl" is jena model with OWL_DL ontologies
	          * wrapped around a pellet-based inference model
	          */
	         Reasoner reasoner = 
	        		 org.mindswap.pellet.jena.PelletReasonerFactory.theInstance().create();
	         Model infModel = ModelFactory.createInfModel(
	            reasoner, ModelFactory.createDefaultModel());
	         model = ModelFactory.createOntologyModel(
	            OntModelSpec.OWL_DL_MEM, infModel);
	      }
	      else
	      {
	         //invalid inference setting
	         System.err.println("Invalid inference setting, "
	            + "choose one of <none|rdfs|owl>.");
	         return;
	      }
	      
	      //load the facts into the model
	      model.read(inputStream, null, inputFileFormat);
	      
	      //validate the file
	      ValidityReport validityReport = model.validate();
	      if(validityReport != null && !validityReport.isValid())
	      {
	         Iterator i = validityReport.getReports();
	         while(i.hasNext())
	         {
	            System.err.println(
	               ((ValidityReport.Report)i.next()).getDescription());
	         }
	         return;
	      }
	      
	      //Iterate over the individuals, print statements about them
	      ExtendedIterator iIndividuals = null;
	      try {
	    	  iIndividuals = model.listIndividuals();
	      } catch (ConversionException e) {
	    	  System.out.println("No individual "+e.getMessage());
	      }
	      while(iIndividuals != null && iIndividuals.hasNext())
	      {
	         Individual i = (Individual)iIndividuals.next();
	         printIndividual(i, writer);
	      }
	      iIndividuals.close();
	      
	      writer.close();
	      model.close();
	 }
	 /**
	  * getIndividuals()
	  * This method returns all individuals from the jena model
	 * @return List
	 */
	public List getIndividuals(){
		 List allIndividuals = new ArrayList<Individual>();
//		 System.out.println("Fetching individuals (model)");
		 ExtendedIterator iIndividuals = null;
		 try {
		  iIndividuals = model.listIndividuals();
		 } catch (ConversionException e) {
			 System.out.println("No individual "+e.getMessage());
		 }
	      while(iIndividuals != null && iIndividuals.hasNext())
	      {
	         Individual i = (Individual)iIndividuals.next();
	         allIndividuals.add(i);
	      }
	      return  allIndividuals;
	 }
	 /**
	  * getProperties
	  * This method returns a HashMap containing all properties from all individuals in the jena model
	 * @param allindividuals
	 * @return
	 */
	public Map<String, List<Statement>> getProperties(List<Individual> allindividuals){
		 Map<String, List<Statement>> statements = new HashMap();
		 for (Individual individual:allindividuals){
			 List<Statement> allStatements = new ArrayList();
			 StmtIterator iProperties = individual.listProperties();
//			 System.out.println("Found iterator for individual");
			 while (iProperties.hasNext()){
				 Statement s = iProperties.next();
				 allStatements.add(s);
			 }
			 String key = individual.getLocalName();
			 statements.put(key, allStatements);
		 }
		 return statements;
	 }


	
}
