package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.servlet.BaseServlet;
import com.thera.thermfw.web.servlet.FillObject;
import com.thera.thermfw.persist.*;

import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigaPrmMontSmontK;

//Setta il tipo Smontaggio montaggio recuperando il valore dall'onchange del magazzino di partenza
//se il magPartenza ha un fornitore associata Tipo =Esterno 
//altrimenti Tipo=interno
public class VtOrdVenRigaPrmMontSmontImpostaTipo extends FillObject {

	public void processAction(ServletEnvironment se) throws SQLException, ServletException, IOException  {

		String idMag = BaseServlet.getStringParameter(se.getRequest(), "IdMagazzino");
		String tipo = BaseServlet.getStringParameter(se.getRequest(), "Tipo");
		String azienda = BaseServlet.getStringParameter(se.getRequest(), "IdAzienda");
		String className =getStringParameter(se.getRequest(), "thClassName");
		String magKey = KeyHelper.buildObjectKey(new String[] { azienda, idMag });
		String collectorName = getStringParameter(se.getRequest(), "thCollectorName");

		
		if (collectorName == null || collectorName.equals(""))
			collectorName = BODataCollector.class.getName();
		ClassADCollection cadc = getClassADCollection(className);
		VtOrdVenRigaPrmMontSmontK rd = null;
		BODataCollector boDC = (BODataCollector) Factory.createObject(collectorName);
		boDC.initialize(className, true, PersistentObject.NO_LOCK);

		if (boDC != null)
			rd = (VtOrdVenRigaPrmMontSmontK) boDC.getBo();

		Magazzino mag = null;

		mag = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magKey, PersistentObject.NO_LOCK);
		if (mag != null && rd != null) {
			rd.setRMagazzinoPart(mag);
			if (mag.getIdFornitoreAzienda() != null && !mag.getIdFornitoreAzienda().equals(""))
				rd.setTipoMontSmont('E');
			else
				rd.setTipoMontSmont('I');
		}
		se.getRequest().setAttribute("TipoMontSmont", rd.getTipoMontSmont());
		se.getRequest().setAttribute("IdAzienda",azienda);
		se.getRequest().setAttribute("IdMagazzino", idMag);
		se.getRequest().setAttribute("Tipo", "M");
		
		String URL = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmMontSmontK.jsp";
		//se.sendRequest(getServletContext(), URL, false);
	}

}
