package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;

import com.thera.thermfw.web.servlet.BaseServlet;

import it.thera.thip.base.articolo.VistaDisponibilita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.datiTecnici.modpro.AttivitaProdMateriale;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivo;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivoTM;
import it.thera.thip.tessile.datiTecnici.modpro.VtAttivitaProduttiva;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigaPrmRidMag;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVendita;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrm;

public class VtOrdVenRigaPrmRidMagFormModifier extends VtOrdineVenditaRigaPrmRidottaFormModifier {

	public static final String DOC_KEY = "DocKey";
	public static final String FUNZIONE_DISP_MAG = "SiVistaDispMag";
	public static final String PARAM_DISP_MAG = "CodiceVista";

	/**
	 * Costruttore.
	 */

	public VtOrdVenRigaPrmRidMagFormModifier() {
		super();

	}

	public void writeHeadElements(JspWriter out) throws IOException {

		VtOrdVenRigaPrmRidMag rd = (VtOrdVenRigaPrmRidMag) getBODataCollector().getBo();

		String keyRiga = BaseServlet.getStringParameter(getRequest(), "DocKey");
		String idArticolo = BaseServlet.getStringParameter(getRequest(), "idArticolo");
		String magId = BaseServlet.getStringParameter(getRequest(), "idMagazzino");
		String magKey = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), magId });

		VtOrdineVenditaRigaPrm riga = null;
		if (!keyRiga.equals(""))
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRiga,
						VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		rd.setIdAzienda(Azienda.getAziendaCorrente());

		if (riga != null) {
			// FIRAS 27/09/2021

			rd.setAnnoDocumento(riga.getAnnoDocumento());
			rd.setNumeroDocumento(riga.getNumeroDocumento());
			rd.setNumeroRigaDocumento(riga.getNumeroRigaDocumento());
			// FINE FIRAS

			rd.setMagazzinoPart(riga.getMagazzino());
			rd.setNumeroRigaDocumento(riga.getNumeroRigaDocumento());
			rd.setDettaglioRigaDocumento(riga.getDettaglioRigaDocumento());

		} else {
			String keyOrdine = BaseServlet.getStringParameter(getRequest(), "DocKey");
			String groupTC = BaseServlet.getStringParameter(getRequest(), "GruppoTC");

			VtOrdineVendita ordine = null;
			if (!keyOrdine.equals(""))
				try {
					ordine = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, keyOrdine,
							VtOrdineVendita.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (ordine != null) {
				rd.setAnnoDocumento(ordine.getAnnoDocumento());
				rd.setNumeroDocumento(ordine.getNumeroDocumento());

			}

			if (groupTC != null && !groupTC.equals("null"))
				rd.setGruppoTC(Long.parseLong(groupTC));

		}
		rd.setIdArticolo(idArticolo);
		rd.setMagazzinoPartKey(magKey);
	}

	public void writeBodyStartElements(JspWriter out) throws IOException {

	}

	public void writeFormStartElements(JspWriter out) throws IOException {

	}

	public void writeFormEndElements(JspWriter out) throws IOException {

	}

	public void writeBodyEndElements(JspWriter out) throws IOException {

		String keyRiga = BaseServlet.getStringParameter(getRequest(), "DocKey");
		// Add function reload Matric
		if (getServletEnvironment().getRequest().getParameter("ObjectKey") != null
				&& !getServletEnvironment().getRequest().getParameter("ObjectKey").equals("null")
				&& getServletEnvironment().getRequest().getParameter("FatherKey") != null
				&& !getServletEnvironment().getRequest().getParameter("FatherKey").equals("null")) {
			out.println("<script>function matrixReolad(){");
			out.println("var param = 'thAction=MATRIX_RELOAD&ObjectKey="
					+ getServletEnvironment().getRequest().getParameter("ObjectKey") + "&FatherKey="
					+ getServletEnvironment().getRequest().getParameter("FatherKey") + "';");
			out.println("var url = parent.opener.location.href");
			out.println("var b=url.indexOf('?');");
			out.println("if(b> -1)");
			out.println("url=url.substr(0, b);");
			out.println("url = url+'?'+ param;");

			out.println("parent.opener.location.href = url;");
			out.println("}</script>");
		} else {
			out.println("<script>function matrixReolad(){");
			out.println("window.opener.location.reload(true)");
			out.println("}</script>");
		}

		VtOrdineVenditaRigaPrm riga = null;
		if (!keyRiga.equals(""))
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRiga,
						VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		// modify title
		out.println("<script language='JavaScript1.2'>");

		ModelloProduttivo modProd = null;
		if (riga != null)
			modProd = getModProdFromArticoloPatron(Azienda.getAziendaCorrente(), riga.getIdArticolo());
		else {
			out.println(" parent.document.getElementById('OK').style.visibility=\"hidden\";");
			out.println(" parent.document.getElementById('OKTRASF').style.visibility=\"hidden\";");
		}
		if (modProd != null) {

			out.println("document.getElementById('labelWhere').innerHTML = 'Spostamento (IdProdoto: " + modProd.getKey()
					+ ")'");

		} else
			out.println("document.getElementById('labelWhere').innerHTML = 'Spostamento'");

		out.println("</script>");

		/*
		 * VtOrdVenRigaPrmRidMag rd= (VtOrdVenRigaPrmRidMag)
		 * getBODataCollector().getBo();
		 * 
		 * String keyRiga=BaseServlet.getStringParameter(getRequest(), "DocKey");
		 * 
		 * VtOrdineVenditaRigaPrm riga = null; if(!keyRiga.equals("")) try { riga =
		 * (VtOrdineVenditaRigaPrm)
		 * PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRiga,
		 * VtOrdineVenditaRigaPrm.NO_LOCK); } catch (SQLException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 * 
		 * if (riga != null) { String vistaDisponKey =
		 * ParametroPsn.getValoreParametroPsn(FUNZIONE_DISP_MAG, PARAM_DISP_MAG);
		 * 
		 * 
		 * VistaDisponibilita vistaDisponibilita = null;
		 * 
		 * if (!vistaDisponKey.equals("")) { String vistaKey =
		 * KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(),
		 * vistaDisponKey }); try { vistaDisponibilita = (VistaDisponibilita)
		 * PersistentObject.elementWithKey(VistaDisponibilita.class, vistaKey,
		 * PersistentObject.NO_LOCK); } catch (SQLException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } }
		 * 
		 * if ( vistaDisponibilita != null) {
		 * 
		 * Iterator magIter = vistaDisponibilita.getMagazzini().iterator(); // GET Dati
		 * kit
		 * 
		 * //TODO verificare l'IDArticolo da passare //ModelloProduttivo modProd =
		 * getModProdFromArticoloPatron(Azienda.getAziendaCorrente(),
		 * riga.getIdArticolo());
		 * 
		 * ModelloProduttivo modProd =
		 * getModProdFromArticoloPatron(Azienda.getAziendaCorrente(), "63484");
		 * 
		 * VtAttivitaProduttiva attivita = null; if (modProd != null &&
		 * modProd.getAttivita() != null) { Iterator attIter =
		 * modProd.getAttivita().iterator(); attivita = (VtAttivitaProduttiva)
		 * attIter.next(); Iterator compIter=attivita.getMateriali().iterator();
		 * 
		 * 
		 * 
		 * out.println("<script language='JavaScript1.2'>"); int x = 0;
		 * 
		 * //add riga magazzini String prmMagazzini="var magazzini = ['',"; String
		 * prmStyleMag="var prmstyleMag = ['cellStyleMagazzino'";
		 * 
		 * String prmStyleArticolo="var prmstyle = ['cellStyleArticolo'"; String
		 * prmArticolo="var parameter = [";
		 * 
		 * String prmStyleComp="var prmstyleComp = ['cellStyleComponente'";
		 * 
		 * 
		 * //add righe componenti kit while (magIter.hasNext()) { Magazzino
		 * mag=(Magazzino) magIter.next(); prmMagazzini =prmMagazzini + "'" +
		 * mag.getDescrizione().getDescrizione()+ "',"; prmStyleMag =prmStyleMag +
		 * ",'cellStyleMagazzino'";
		 * 
		 * //Add Riga articolo if(x==0) prmArticolo =prmArticolo + "'" +
		 * modProd.getDescrizione().getDescrizione()+ "',"; else if(x==1) prmArticolo
		 * =prmArticolo + "'" + String.valueOf(modProd.getQuantitaBase())+ "',"; else
		 * prmArticolo =prmArticolo + "'',";
		 * 
		 * prmStyleArticolo =prmStyleArticolo + ",'cellStyleDati'";
		 * 
		 * 
		 * prmStyleComp =prmStyleComp + ",'cellStyleDati'";
		 * 
		 * x++; if (x>=5) break;
		 * 
		 * }
		 * 
		 * prmMagazzini = prmMagazzini.substring(0, prmMagazzini.length()-1);
		 * prmMagazzini =prmMagazzini + "];"; prmStyleMag =prmStyleMag + "];";
		 * out.println(prmMagazzini); out.println(prmStyleMag);
		 * out.println(" addRowMatrice('Matrice',magazzini,prmstyleMag);");
		 * 
		 * prmArticolo =prmArticolo + "''];"; prmStyleArticolo =prmStyleArticolo + "];";
		 * out.println(prmStyleArticolo); out.println(prmArticolo);
		 * out.println(" addRowMatrice('Matrice',parameter,prmstyle );");
		 * 
		 * 
		 * prmStyleComp =prmStyleComp + "];"; out.println(prmStyleComp);
		 * 
		 * 
		 * int y=0; //add righe componenti kit while (compIter.hasNext()) {
		 * AttivitaProdMateriale componente=(AttivitaProdMateriale) compIter.next();
		 * String prmArray="var parameter" + String.valueOf(y) + " = [";
		 * 
		 * 
		 * for(int i=0; i<=x; i++) { if(i==0) prmArray =prmArray + "'" +
		 * componente.getDescrizione().getDescrizione()+ "',"; else if(i==1) prmArray
		 * =prmArray + "'" + componente.getCoeffImpiego().toBigInteger().toString()+
		 * "',"; else prmArray =prmArray + "' ',";
		 * 
		 * }
		 * 
		 * prmArray = prmArray.substring(0, prmArray.length()-1); prmArray =prmArray +
		 * "];"; out.println(prmArray); out.println(" addRowMatrice('Matrice',parameter"
		 * + String.valueOf(y) + ", prmstyleComp);"); y++; }
		 * 
		 * 
		 * 
		 * 
		 * out.println("</script>"); }
		 * 
		 * } else {
		 * 
		 * try {
		 * 
		 * out.println("<script language='JavaScript1.2'>"); out.
		 * println("parent.alert('Non sono stati trovati articoli o vista diponibilità non valida.');"
		 * ); out.println("parent.window.close(); "); out.println("</script>"); } catch
		 * (IOException e) { e.printStackTrace(); } return; }
		 * 
		 * 
		 * 
		 * }
		 * 
		 * 
		 */

	}

	public static ModelloProduttivo getModProdFromArticoloPatron(String idAzienda, String idArticolo) {

		String where = ModelloProduttivoTM.ID_AZIENDA + "='" + idAzienda + "' AND " + ModelloProduttivoTM.R_ARTICOLO
				+ "= '" + idArticolo + "'";

		Vector modelli = null;
		try {
			modelli = ModelloProduttivo.retrieveList(ModelloProduttivo.class, where, "", true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (modelli != null && !modelli.isEmpty()) {
			ModelloProduttivo modello = (ModelloProduttivo) modelli.get(0);
			if (modello != null) {
				return modello;
			}
		}

		return null;
	}
}
