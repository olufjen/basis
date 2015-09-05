package no.basis.felles.server.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.Request;
import org.restlet.data.Reference;

import no.naks.biovigilans.model.Annenkomplikasjon;
import no.naks.biovigilans.model.Blodprodukt;
import no.naks.biovigilans.model.Forebyggendetiltak;
import no.naks.biovigilans.model.Giverkomplikasjon;
import no.naks.biovigilans.model.Komplikasjonsklassifikasjon;
import no.naks.biovigilans.model.Pasient;
import no.naks.biovigilans.model.Pasientkomplikasjon;
import no.naks.biovigilans.model.Produktegenskap;
import no.naks.biovigilans.model.Sykdom;
import no.naks.biovigilans.model.Symptomer;
import no.naks.biovigilans.model.Tiltak;
import no.naks.biovigilans.model.Transfusjon;
import no.naks.biovigilans.model.Utredning;
import no.naks.biovigilans.model.Vigilansmelding;
import no.basis.felles.control.EmailWebService;
import no.basis.felles.model.LoginModel;


/**
 * SessionServerResource
 * Denne klassen inneholder alle Webmodel objekter for en session.
 * Den er felles superklasse for alle ResourceHtml klassene
 * @author olj
 *
 */
public class SessionServerResource extends ProsedyreServerResource {

/*
 * Session objekter 
 */

	protected String andreKey = "annenKomp"; 		// Nøkkel dersom melding er av type annenkomplikasjon
	protected String pasientKey = "pasientKomp"; // Nøkkel dersom melding er av type pasientkomplikasjon
	protected String giverKey = "giverkomp"; 	// Nøkkel dersom melding er at type giverkomplikasjon
	protected String meldingsId = "meldinger";  // Nøkkel til en Vigilansmelding (OBS i adm til en liste vigilansmeldinger)
	protected String vigilansmeldinger = "vigilansmeldinger"; // Nøkkel til en liste vigilansmeldinger
	protected String allemeldingerMap = "allemeldinger"; // Nøkkel til en Map som inneholder alle meldingsdetaljer		
	protected String[] avdelinger;
	protected String[] aldergruppe;
	protected String[] kjonnValg; 
	protected String[] blodProdukt; // Plasma blodprodukter for nedtrekk - plasma produkttyper
	protected String[] hemolyseParametre;
	protected String pasientkomplikasjonId = "pasientkomplikasjon"; 	// Benyttes som nøkkel til HTML-sider pasientkomplikasjon
	protected String transfusjonId = "transfusjonwebID";					// Benyttes som nøkkel til HTML-sider pasientkomplikasjon
	protected String kvitteringsId = "kvittering";					// Benyttes som nøkkel for kvitteringssiden
	protected String kvitteringGiverId = "giverKvittering";
	protected String giverkomplikasjonId="giverkomplikasjon"; 		// Benyttes som nøkkel for giverwebmodel
	protected String donasjonId ="donasjon";						// Benyttes som nøkkel for donasjonwebmodel
	protected String komDiagnosegiverId = "komDiagnosegiver";
	protected String vigilansmeldingId="vigilansmelding";
	protected String messageType = "none";
	protected String giverenKey="giver";
	protected String donasjonKey = "donasjonen";
	protected String giverkomplikasjonKey = "giverkomplikasjonen"; 	// Benyttes som nøkkel for giverkomplikasjon
	protected String giverOppfolgingKey = "giveroppfolging";
	protected String giverkomplikasjondiagnoseKey = "giverkomplikasjondiagnose";
	protected String symptomerKey  ="symptomer";
	protected String tiltakKey = "tiltak";
	protected String forebyggendetiltakKey = "forebyggende";
/*
 * Nøkler for pasientkomplikasjoner	
 */
	protected String pasientenKey = "pasienten";
	protected String transfusjonsKey = "transfusjon";
	protected String sykdomKey = "sykdom";
	protected String klassifikasjonKey = "komplikasjonklassifikasjon";
	protected String utredningKey = "utredning";
	protected String blodproduktKey = "blodprodukt";
	protected String produktegenskapKey = "produktegenskap";		
/*
 * Til bruk for oppfølgingsmeldinger	
 */
	protected String displayKey = "display";						
	protected String displayPart = "none";
	protected String displaydateKey = "displaydate";
	protected String datePart = "block";
	

	protected List<String> hvagikkgaltList = new ArrayList<String>();
/*
 * Session objekter for giver	
 */

//	protected GiverKvitteringWebModel giverKvittering = null;

	protected String[] reaksjonengruppe;
	protected String[] utenforBlodbankengruppe;
	protected String[] donasjonsstedgruppe;


	protected String[] systemiskgruppe;
	protected String[] skadeiarmen;
	protected String[] sykemeldinggruppe;
	protected String[] varighetSkadegruppe;
	
	//Rapporter AndreHendelse

	protected String andreHendelseId ="andreHendelse";
	protected String annenHendelseId ="annenHendelse";
	protected String[] alvorligHendelse; 
	protected String[] hovedprosesslist;
	protected String[] feilelleravvik;
	protected String[] hendelsenoppdaget;
/*
 * Session objekter for kontakt	
 */

	protected String melderId = "melder"; // Nøkkel for melderwebModel
	protected String melderNokkel = "melderPrimar"; // Nøkkel for melder fra db
	
	protected String nokkelId = "nokkel"; // Til bruk leveranseside
	protected String datoId = "dato";		//Til bruk leveranseside
	
/*
 * Disse objektene inneholder tidligere rapporterte meldinger
 * Benyttes når bruker har angitt oppfølgingsmelding	
 */
	protected Annenkomplikasjon annenKomplikasjon = null;
    protected Pasientkomplikasjon pasientKomplikasjon = null;
    protected Giverkomplikasjon giverKomplikasjon = null;

    protected EmailWebService emailWebService;
    
	protected List<Vigilansmelding>andreMeldingene = null; // Inneholder oppfølgingsmeldinger som er fjernet fra liste (andre hendelser)
	protected String andreMeldingKey = "andreMeldingrem"; //Session key
	protected List<Vigilansmelding>giverMeldingene = null; // Inneholder oppfølgingsmeldinger som er fjernet fra liste (givermeldinger)
	protected String giverMeldingKey = "giverMeldingrem"; //Session key
	protected List<Vigilansmelding>pasientMeldingene = null; // Inneholder oppfølgingsmeldinger som er fjernet fra liste (pasienthendelser)
	protected String pasientMeldingKey = "pasientMeldingrem"; //Session key
    protected List<Vigilansmelding> dobleMeldingene = null;
    protected String dobleMeldingKey = "meldingrem";
/*
 * Login objekter
 */
    protected LoginModel login = null;
    protected String loginKey = "login";
    
	public EmailWebService getEmailWebService() {
		return emailWebService;
	}

	public void setEmailWebService(EmailWebService emailWebService) {
		this.emailWebService = emailWebService;
	}



	public LoginModel getLogin() {
		return login;
	}

	public void setLogin(LoginModel login) {
		this.login = login;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getDisplayKey() {
		return displayKey;
	}

	public void setDisplayKey(String displayKey) {
		this.displayKey = displayKey;
	}

	public String getDisplayPart() {
		return displayPart;
	}

	public void setDisplayPart(String displayPart) {
		this.displayPart = displayPart;
	}

	public String getDisplaydateKey() {
		return displaydateKey;
	}

	public void setDisplaydateKey(String displaydateKey) {
		this.displaydateKey = displaydateKey;
	}

	public String getDatePart() {
		return datePart;
	}

	public void setDatePart(String datePart) {
		this.datePart = datePart;
	}

	public String getMeldingsId() {
		return meldingsId;
	}

	public void setMeldingsId(String meldingsId) {
		this.meldingsId = meldingsId;
	}
	public String getPasientKey() {
		return pasientKey;
	}
	public void setPasientKey(String pasientKey) {
		this.pasientKey = pasientKey;
	}



	public String getGiverKey() {
		return giverKey;
	}



	public void setGiverKey(String giverKey) {
		this.giverKey = giverKey;
	}

	public String getAndreKey() {
		return andreKey;
	}



	public void setAndreKey(String andreKey) {
		this.andreKey = andreKey;
	}

	
	public String[] getVarighetSkadegruppe() {
		return varighetSkadegruppe;
	}
	public void setVarighetSkadegruppe(String[] varighetSkadegruppe) {
		this.varighetSkadegruppe = varighetSkadegruppe;
	}
	public String[] getSykemeldinggruppe() {
		return sykemeldinggruppe;
	}
	public void setSykemeldinggruppe(String[] sykemeldinggruppe) {
		this.sykemeldinggruppe = sykemeldinggruppe;
	}
	public String[] getSystemiskgruppe() {
		return systemiskgruppe;
	}
	public void setSystemiskgruppe(String[] systemiskgruppe) {
		this.systemiskgruppe = systemiskgruppe;
	}
	public String[] getSkadeiarmen() {
		return skadeiarmen;
	}
	public void setSkadeiarmen(String[] skadeiarmen) {
		this.skadeiarmen = skadeiarmen;
	}
	public String[] getHendelsenoppdaget() {
		return hendelsenoppdaget;
	}
	public void setHendelsenoppdaget(String[] hendelsenoppdaget) {
		this.hendelsenoppdaget = hendelsenoppdaget;
	}
	public String[] getFeilelleravvik() {
		return feilelleravvik;
	}
	public void setFeilelleravvik(String[] feilelleravvik) {
		this.feilelleravvik = feilelleravvik;
	}
	public String[] getHovedprosesslist() {
		return hovedprosesslist;
	}
	public void setHovedprosesslist(String[] hovedprosesslist) {
		this.hovedprosesslist = hovedprosesslist;
	}

	public String getAndreHendelseId() {
		return andreHendelseId;
	}
	public void setAndreHendelseId(String andreHendelseId) {
		this.andreHendelseId = andreHendelseId;
	}
	public String[] getAlvorligHendelse() {
		return alvorligHendelse;
	}
	public void setAlvorligHendelse(String[] alvorligHendelse) {
		this.alvorligHendelse = alvorligHendelse;
	}

	public String[] getAvdelinger() {
		return avdelinger;
	}
	public void setAvdelinger(String[] avdelinger) {
		this.avdelinger = avdelinger;
	}
	public String[] getAldergruppe() {
		return aldergruppe;
	}
	public void setAldergruppe(String[] aldergruppe) {
		this.aldergruppe = aldergruppe;
	}
	public String[] getKjonnValg() {
		return kjonnValg;
	}
	public void setKjonnValg(String[] kjonnValg) {
		this.kjonnValg = kjonnValg;
	}
	public String[] getBlodProdukt() {
		return blodProdukt;
	}
	public void setBlodProdukt(String[] blodProdukt) {
		this.blodProdukt = blodProdukt;
	}
	public String[] getHemolyseParametre() {
		return hemolyseParametre;
	}
	public void setHemolyseParametre(String[] hemolyseParametre) {
		this.hemolyseParametre = hemolyseParametre;
	}
	public String getPasientkomplikasjonId() {
		return pasientkomplikasjonId;
	}
	public void setPasientkomplikasjonId(String pasientkomplikasjonId) {
		this.pasientkomplikasjonId = pasientkomplikasjonId;
	}
	public String getTransfusjonId() {
		return transfusjonId;
	}
	public void setTransfusjonId(String transfusjonId) {
		this.transfusjonId = transfusjonId;
	}
	public String getKvitteringsId() {
		return kvitteringsId;
	}
	public void setKvitteringsId(String kvitteringsId) {
		this.kvitteringsId = kvitteringsId;
	}

	public String[] getReaksjonengruppe() {
		return reaksjonengruppe;
	}
	public void setReaksjonengruppe(String[] reaksjonengruppe) {
		this.reaksjonengruppe = reaksjonengruppe;
	}
	public String[] getUtenforBlodbankengruppe() {
		return utenforBlodbankengruppe;
	}
	public void setUtenforBlodbankengruppe(String[] utenforBlodbankengruppe) {
		this.utenforBlodbankengruppe = utenforBlodbankengruppe;
	}
	public String[] getDonasjonsstedgruppe() {
		return donasjonsstedgruppe;
	}
	public void setDonasjonsstedgruppe(String[] donasjonsstedgruppe) {
		this.donasjonsstedgruppe = donasjonsstedgruppe;
	}
	public String getGiverkomplikasjonId() {
		return giverkomplikasjonId;
	}
	public void setGiverkomplikasjonId(String giverkomplikasjonId) {
		this.giverkomplikasjonId = giverkomplikasjonId;
	}
	public String getDonasjonId() {
		return donasjonId;
	}
	public void setDonasjonId(String donasjonId) {
		this.donasjonId = donasjonId;
	}
	public String getKomDiagnosegiverId() {
		return komDiagnosegiverId;
	}
	public void setKomDiagnosegiverId(String komDiagnosegiverId) {
		this.komDiagnosegiverId = komDiagnosegiverId;
	}
	public String getVigilansmeldingId() {
		return vigilansmeldingId;
	}
	public void setVigilansmeldingId(String vigilansmeldingId) {
		this.vigilansmeldingId = vigilansmeldingId;
	}
	/*public GiverKvitteringWebModel getGiverKvittering() {
		return giverKvittering;
	}
	public void setGiverKvittering(GiverKvitteringWebModel giverKvittering) {
		this.giverKvittering = giverKvittering;
	}*/

	public String getMelderId() {
		return melderId;
	}
	public void setMelderId(String melderId) {
		this.melderId = melderId;
	}
	
	public List<String> getHvagikkgaltList() {
		return hvagikkgaltList;
	}
	public void setHvagikkgaltList(List<String> hvagikkgaltList) {
		this.hvagikkgaltList = hvagikkgaltList;
	}
	
	
	/**
	 * invalidateSessionobjects
	 * Denne rutinen fjerner alle session objekter
	 */
	public void invalidateSessionobjects(){

	}
	/**
	 * setalleTiltak
	 * Denne rutinen setter opp alle initielle tiltak
	 * dersom de ikke finnes fra før.
	 * Den kalles fra setTransfusjonsObjects() og RapporterHendelsesserverresourceHTML
	 */
	protected void setalleTiltak(){
		Request request = getRequest();

    	List<Tiltak> tiltak = new ArrayList<Tiltak>();
    	List<Forebyggendetiltak> forebyggende = new ArrayList<Forebyggendetiltak>();

    	sessionAdmin.setSessionObject(request, tiltak,tiltakKey);
    	sessionAdmin.setSessionObject(request, forebyggende,forebyggendetiltakKey);
	}



}
