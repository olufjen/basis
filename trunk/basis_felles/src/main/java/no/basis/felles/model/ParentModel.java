package no.basis.felles.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import no.naks.biovigilans.model.Vigilansmelding;

import org.restlet.data.Parameter;

/**
 * @author olj
 * Denne klassen er superklassen til alle modelklasser som representerer et skjermbilde (et brukergrensesnitt)
 *
 */
public class ParentModel {

	protected boolean lagret = false; // Satt true dersom session objects er lagret
	private Map<String,String> formMap; // Inneholder brukers input verdier fra skjermbildet
	private String[] formNames; // Inneholder navn på input felt i skjermbildet
	private String accountRef;

	
	public ParentModel() {
		super();
		formMap = new HashMap<String,String>();
		// TODO Auto-generated constructor stub
	}


	public boolean isLagret() {
		return lagret;
	}



	public void setLagret(boolean lagret) {
		this.lagret = lagret;
	}







	/**
	 * setValues
	 * Denne rutinen setter alle verdier mottatt fra bruker.
	 * Verdier må lagres avhengig av hvilke knapper bruker har valgt
	 * @param entry
	 */
	public void setValues(Parameter entry){
		String name = entry.getName();
		String value = entry.getValue();
		/*
		boolean finnes = formMap.containsKey(name);
		if(finnes){
			String val =  formMap.get(name);
			value = val + "-" + value; 
		}*/
		formMap.put(name, value);
	
	}


	public Map getFormMap() {
		return formMap;
	}


	public void setFormMap(Map formMap) {
		this.formMap = formMap;
	}


	public String[] getFormNames() {
		return formNames;
	}


	public void setFormNames(String[] formNames) {
		this.formNames = formNames;
	}


	public String getAccountRef() {
		return accountRef;
	}


	public void setAccountRef(String accountRef) {
		this.accountRef = accountRef;
	}



	
}