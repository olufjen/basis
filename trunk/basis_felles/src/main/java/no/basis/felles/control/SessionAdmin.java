package no.basis.felles.control;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.restlet.Request;

/**
 * Dette en grensesnittdefinisjon for SessionAdmin
 * Klassen håndterer alle session objekter for Restlet resurser
 * @author olj
 *
 */
public interface SessionAdmin {
	
	public Object getSessionObject(Request request,String idKey);
	public void setSessionObject(Request request,Object o,String idKey);
	public jakarta.servlet.http.HttpSession getSession(Request request,String idKey);
	public String[] getSessionParams();
	public void setSessionParams(String[] sessionParams);

}
