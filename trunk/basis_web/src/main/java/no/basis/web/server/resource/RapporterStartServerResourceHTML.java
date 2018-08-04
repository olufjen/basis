package no.basis.web.server.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import no.basis.felles.server.resource.SessionServerResource;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import freemarker.template.SimpleScalar;

/**
 * @author olj
 *  Denne resursen er knyttet til startsiden for Hemovigilans. 
 *  Her velger bruker om det er en ny melding eller en oppfølgingsmelding
 *  Ved oppfølgingsmeldinger hentes all informasjon om alle meldinger til pålogget melder.
 *  Meldingene fra databasen og settes i sessionadmin
 *  Velger bruker ny melding hentes siden rapporter_hendelse_main.html direkte
 */
public class RapporterStartServerResourceHTML extends SessionServerResource {

	
	private String delMelding = "delmelding";
	private String meldeTxtId = "melding";
	private String passordCheck = "none";
	private String displayPassord = "passord";

	public String getDisplayPassord() {
		return displayPassord;
	}

	public void setDisplayPassord(String displayPassord) {
		this.displayPassord = displayPassord;
	}

	public String getPassordCheck() {
		return passordCheck;
	}

	public void setPassordCheck(String passordCheck) {
		this.passordCheck = passordCheck;
	}

	public String getDelMelding() {
		return delMelding;
	}

	public void setDelMelding(String delMelding) {
		this.delMelding = delMelding;
	}


	public String getMeldeTxtId() {
		return meldeTxtId;
	}

	public void setMeldeTxtId(String meldeTxtId) {
		this.meldeTxtId = meldeTxtId;
	}



	/**
	 * getHemovigilans
	 * Denne rutinen starter med startside.html
	 * Denne rutinen henter inn nødvendige session objekter og  setter opp nettsiden for å ta i mot
	 * en meldingsnøkkel til en oppfølgingsmelding
	 * @return
	 */
	@Get
	public Representation getHemovigilans() {


	     Reference reference = new Reference(getReference(),"..").getTargetRef();
	     Request request = getRequest();
	     Map<String, Object> dataModel = new HashMap<String, Object>();
	     String meldingsText = " ";
	     SimpleScalar simple = new SimpleScalar(meldingsText);
		 dataModel.put(meldeTxtId,simple );
//		 SimpleScalar pwd = new SimpleScalar(passordCheck);
//		 dataModel.put(displayPassord,pwd);
	     LocalReference pakke = LocalReference.createClapReference(LocalReference.CLAP_CLASS,
                 "/basis");
	    
	     LocalReference localUri = new LocalReference(reference);
	
// Denne client resource forholder seg til src/main/resource katalogen !!!	
	     ClientResource clres2 = new ClientResource(LocalReference.createClapReference(LocalReference.CLAP_CLASS,"/basis/startside.html"));

	        Representation pasientkomplikasjonFtl = clres2.get();

	        TemplateRepresentation  templatemapRep = new TemplateRepresentation(pasientkomplikasjonFtl,dataModel,
	                MediaType.TEXT_HTML);
		 return templatemapRep;
	
	}
	
    /**
     * storeHemovigilans
     * Denne rutinen rutinen kjøres dersom epost og passord er gitt fra bruker.
     * Den tar imot epost og passord og henter frem riktig meldingsinformasjon fra
     * databasen basert på melders id
     * @param form
     * @return
     */
    /**
     * @param form
     * @return
     */
    @Post
    public Representation storeHemovigilans(Form form) {
    	TemplateRepresentation  templateRep = null;
 	    Map<String, Object> dataModel = new HashMap<String, Object>();
 	    String meldingsText = "Melders epost/passord finnes ikke, prøv igjen";
	    dataModel.put( meldeTxtId, meldingsText);
	    Request request = getRequest();
	    
/*	    Map<String,List> alleMeldinger = new HashMap<String,List>();
 	    List<Vigilansmelding> meldinger = null;
 //	    List delMeldinger = null;
 	    List<Vigilansmelding> andreMeldinger = null;
 	    List<Vigilansmelding> pasientMeldinger = null;
 	    List<Vigilansmelding> giverMeldinger = null;*/
 	    
    	if(form == null){
    		invalidateSessionobjects();
    	}
/*
 * Verdier angitt av bruker    	
 */
    	String melderEpost = null;
    	String melderPassord = null;
    	String meldingsNokkel = null;
/*
 * Verdier fra database
 */
		String name ="";
		String passord = "";

    	Long melderid = null; 
    	Parameter nyttPassord = form.getFirst("nyttpassord");
        String page = "../hemovigilans/melder_rapport.html"; 
    	for (Parameter entry : form) {
			if (entry.getValue() != null && !(entry.getValue().equals(""))){
					System.out.println(entry.getName() + "=" + entry.getValue());
					if (entry.getName().equals("k-melderepost")){
						melderEpost = entry.getValue();
					}
					if (entry.getName().equals("k-melderpassord")){
						melderPassord = entry.getValue();
					}

			}
			
    	}
		Parameter formValue = form.getFirst("formValue"); // Bruker oppgir epost og passord
	
		if (formValue != null && melderEpost != null){
			

		}
		if (nyttPassord != null){
			page = "../hemovigilans/passord.html"; 
			redirectPermanent(page);
		}
    
/*    	if (formValue != null && meldingsNokkel != null){ // Meldingsnøkkel oppgitt 
    		alleMeldinger = melderWebService.selectMeldinger(meldingsNokkel);
    		meldinger = alleMeldinger.get(meldingsNokkel);
    		andreMeldinger = alleMeldinger.get(andreKey);
    		pasientMeldinger = alleMeldinger.get(pasientKey);
    		giverMeldinger = alleMeldinger.get(giverKey);
     
    		Vigilansmelding melding = null;
    		if (!meldinger.isEmpty())
    			melding = meldinger.get(0);  
    		
    		if (andreMeldinger != null){
    			if (!andreMeldinger.isEmpty()){
        			Annenkomplikasjon annenKomplikasjon = (Annenkomplikasjon)andreMeldinger.get(0);
        			Vigilansmelding lokalMelding = (Vigilansmelding) annenKomplikasjon;
        			setMeldingsValues(lokalMelding, melding);
        			List klassifikasjoner = alleMeldinger.get(klassifikasjonKey);
        			sessionAdmin.setSessionObject(request, annenKomplikasjon,andreKey);
        			sessionAdmin.setSessionObject(request, lokalMelding, meldingsId);
        			sessionAdmin.setSessionObject(request, klassifikasjoner,klassifikasjonKey);
        		} 			
    		}
      		if (pasientMeldinger != null){
    			if (!pasientMeldinger.isEmpty()){
  //  			   page = "../hemovigilans/rapportert_pasient.html";
        			Pasientkomplikasjon pasientKomplikasjon = (Pasientkomplikasjon)pasientMeldinger.get(0);
        			Vigilansmelding lokalMelding = (Vigilansmelding) pasientKomplikasjon;
        			setMeldingsValues(lokalMelding, melding);
        			sessionAdmin.setSessionObject(request, lokalMelding, meldingsId);       			
        			sessionAdmin.setSessionObject(request, pasientKomplikasjon,pasientKey);
        			Pasient pasient = null;
          			Transfusjon transfusjon = null;
        			Komplikasjonsklassifikasjon klassifikasjon = null;
        			List pasienter = alleMeldinger.get(pasientenKey);
        			List sykdommer = alleMeldinger.get(sykdomKey);
        			List transfusjoner = alleMeldinger.get(transfusjonsKey);
        			List klassifikasjoner = alleMeldinger.get(klassifikasjonKey);
        			List utredninger = alleMeldinger.get(utredningKey);
        			List blodprodukter = alleMeldinger.get(blodproduktKey);
        			List egenskaper = alleMeldinger.get(produktegenskapKey);
        			List symptomer = alleMeldinger.get(symptomerKey);
        			List tiltak = alleMeldinger.get(tiltakKey);
        			List forebyggendeTiltak = alleMeldinger.get(forebyggendetiltakKey);
        			
        			if (pasienter != null && !pasienter.isEmpty()){
        				pasient = (Pasient)pasienter.get(0);
        				sessionAdmin.setSessionObject(request, pasient,pasientenKey);
        				
        			}
        			if (transfusjoner != null && !transfusjoner.isEmpty()){
        				transfusjon = (Transfusjon)transfusjoner.get(0);
        				sessionAdmin.setSessionObject(request, transfusjon,transfusjonsKey);
        			}
        			sessionAdmin.setSessionObject(request, sykdommer,sykdomKey);
        			sessionAdmin.setSessionObject(request, klassifikasjoner,klassifikasjonKey);
        			sessionAdmin.setSessionObject(request,utredninger, utredningKey);
        			sessionAdmin.setSessionObject(request,blodprodukter, blodproduktKey);
        			sessionAdmin.setSessionObject(request,egenskaper,produktegenskapKey);
        			sessionAdmin.setSessionObject(request,symptomer,symptomerKey);
        			sessionAdmin.setSessionObject(request,tiltak,tiltakKey);
        			sessionAdmin.setSessionObject(request,forebyggendeTiltak,forebyggendetiltakKey);
        			
        		} 			
    		}
 
      		if (giverMeldinger != null){
    			if (!giverMeldinger.isEmpty()){
  //  				 page = "../hemovigilans/rapportert_giver.html";
        			Giverkomplikasjon giverKomplikasjon = (Giverkomplikasjon)giverMeldinger.get(0);
        			Vigilansmelding lokalMelding = (Vigilansmelding) giverKomplikasjon;
        			setMeldingsValues(lokalMelding, melding);
        			Giver giver = null;
        			Donasjon donasjonen = null;
        			List givere = alleMeldinger.get(giverenKey);
        			List donasjoner = alleMeldinger.get(donasjonKey);
        			List giveroppfolginger = alleMeldinger.get(giverOppfolgingKey);
        			List komplikasjonsdiagnosergiver = alleMeldinger.get(giverkomplikasjondiagnoseKey);
        			
        			if (givere != null && !givere.isEmpty()){
        				giver = (Giver)givere.get(0);
        				sessionAdmin.setSessionObject(request, giver,giverenKey);
        			}
        			if (donasjoner != null && !donasjoner.isEmpty()){
        				donasjonen = (Donasjon)donasjoner.get(0);
        				sessionAdmin.setSessionObject(request, donasjonen,donasjonKey);
        			}
        			if (giveroppfolginger != null && !giveroppfolginger.isEmpty()){
        				Giveroppfolging giveroppfolging = (Giveroppfolging)giveroppfolginger.get(0);
        				sessionAdmin.setSessionObject(request,giveroppfolging, giverOppfolgingKey);
        			}
        			if (komplikasjonsdiagnosergiver != null && !komplikasjonsdiagnosergiver.isEmpty()){
        				Komplikasjonsdiagnosegiver komplikasjonsdiagnoseGiver = (Komplikasjonsdiagnosegiver)komplikasjonsdiagnosergiver.get(0);
        				sessionAdmin.setSessionObject(request, komplikasjonsdiagnoseGiver,giverkomplikasjondiagnoseKey);
        			}
        			sessionAdmin.setSessionObject(request, giverKomplikasjon,giverKey);
        			sessionAdmin.setSessionObject(request, lokalMelding, meldingsId);
        		} 			
    		}
    	      		
    	}
    	
    	 * Meldingsnøkkel ikke oppgitt
    	 
    	if (meldinger != null){
        	if (meldinger.isEmpty()){
        		ClientResource clres2 = new ClientResource(LocalReference.createClapReference(LocalReference.CLAP_CLASS,"/hemovigilans/startside.html"));
        		Representation pasientkomplikasjonFtl = clres2.get();
        		templateRep = new TemplateRepresentation(pasientkomplikasjonFtl, dataModel,
        				MediaType.TEXT_HTML);	
        	}
        	
        	if (!meldinger.isEmpty()){
        	//	Vigilansmelding melding = (Vigilansmelding) meldinger.get(0);
        	//	sessionAdmin.setSessionObject(request, melding, meldingsId);
        		redirectPermanent(page);
        	}    		
    	}else{ // Meldingsnøkkel ikke oppgitt går til startside.
     		ClientResource clres2 = new ClientResource(LocalReference.createClapReference(LocalReference.CLAP_CLASS,"/hemovigilans/startside.html"));
    		Representation pasientkomplikasjonFtl = clres2.get();
    		templateRep = new TemplateRepresentation(pasientkomplikasjonFtl, dataModel,
    				MediaType.TEXT_HTML);	
    	}
*/

    	return templateRep;
    }
}
