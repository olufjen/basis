/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package no.basis.felles.semanticweb;


import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Set;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
//import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLOntologyMerger;


/**
 * Original authors:
 * @author elvio
 * @author andrea.nuzzolese
 * @author oluf
 * @since 14.08.17
 * Changed from Jena to Owl converter to an Ontology helper
 * 
 */
public class OntologyHelper {

 private boolean available = true;
 private boolean availablemain = true;
 public OntologyHelper()
 {
	 
 }

////////////////////////////////////////////////////////////////////////////////
//////////////////////////////FUNCTIONS/////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////


 /**
 * This function converts an ontology object from Jena to OWLapi
 *
 * @param jenamodel {An OntModel object}
 * @param format {only in "RDF/XML"}
 * @return {An OWLOntology  that is an owl object}
 * @deprecated
 */

public synchronized OWLOntology ModelJenaToOwlConvert(Model jenamodel,String format){

    while (availablemain == false) {
        try {wait();} catch (InterruptedException e) {System.err.println("ModelJenaToOwlConvert::: "+e);}
    }

    availablemain=false;

     ByteArrayOutputStream out = new ByteArrayOutputStream();

    if(!format.equals("RDF/XML")){
        System.err.println("The only format supported is RDF/XML. Please check the format!");

        availablemain = true;
        notifyAll();
        return null;
    }else{

    jenamodel.write(out,format);
    OWLOntologyManager owlmanager = null;
 //   OWLManager m = new OWLManager();
 // Lagt til en try catch løkke   
/*    try{
        owlmanager = OWLManager.createOWLOntologyManager();
    }catch(java.lang.NoClassDefFoundError e){
    	System.err.print("No class found");
    	e.printStackTrace();
    	return null;
    }*/


    OWLOntology owlmodel = null;
/*	try {
		owlmodel = owlmanager.loadOntologyFromOntologyDocument(new ByteArrayInputStream(out.toByteArray()));
	} catch (OWLOntologyCreationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
//    OWLOntology owlmodel = null;
    availablemain = true;
    notifyAll();
    return owlmodel;
 /*   }}catch (OWLOntologyCreationException eoc){
        System.err.print("ModelJenaToOwlConvert::: ");
        eoc.printStackTrace();
        return null;
    }*/
    }
	}

}