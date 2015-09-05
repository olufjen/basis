package no.basis.felles.control;

import no.naks.rammeverk.mailer.*;
public interface EmailWebService {

	public void setMailTo(String mailTo) ;
	public void sendEmail(String mailInfo);
}
