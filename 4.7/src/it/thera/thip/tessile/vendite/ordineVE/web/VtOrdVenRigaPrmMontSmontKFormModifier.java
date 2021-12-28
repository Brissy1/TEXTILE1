package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.articolo.VistaDisponibilita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.datiTecnici.modpro.AttivitaProdMateriale;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivo;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivoTM;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.datiTecnici.modpro.VtAttivitaProduttiva;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.vendite.ordineVE.VTMontSmontUtility;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigaPrmMontSmontK;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVendita;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrm;

public class VtOrdVenRigaPrmMontSmontKFormModifier extends VtOrdineVenditaRigaPrmRidottaFormModifier {

	public static final String DOC_KEY = "DocKey";
	public static final String FUNZIONE_DISP_MAG = "SiVistaDispMag";
	public static final String PARAM_DISP_MAG = "CodiceVista";

	/**
	 * Costruttore.
	 */

	public VtOrdVenRigaPrmMontSmontKFormModifier() {
		super();

	}

	public void writeHeadElements(JspWriter out) throws IOException {

		VtOrdVenRigaPrmMontSmontK rd = (VtOrdVenRigaPrmMontSmontK) getBODataCollector().getBo();

		String tipo = BaseServlet.getStringParameter(getRequest(), "Tipo");
		String magId = BaseServlet.getStringParameter(getRequest(), "idMagazzino");
		String magKey = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), magId });
		String idArticolo = BaseServlet.getStringParameter(getRequest(), "idArticolo");
		String[] objectKey = (String[]) getServletEnvironment().getRequest().getAttribute("ObjectKey");
	
		 getServletEnvironment().getSession().setAttribute("ObjectKey", objectKey);
		
		Magazzino mag = null;
		rd.setIdAzienda(Azienda.getAziendaCorrente());
		
		if (tipo.equals("M")) {
			// ************** MONTAGGIO ***************
			String keyRiga = BaseServlet.getStringParameter(getRequest(), "DocKey");

			VtOrdineVenditaRigaPrm riga = null;
			if (!keyRiga.equals(""))
				try {
					riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
							keyRiga, VtOrdineVenditaRigaPrm.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			if (riga != null) {
				// FIRAS 27/09/2021
				
				rd.setAnnoDocumento(riga.getAnnoDocumento());
				rd.setNumeroDocumento(riga.getNumeroDocumento());
				rd.setNumeroRigaDocumento(riga.getNumeroRigaDocumento());
				// FINE FIRAS

				mag = riga.getMagazzino();
				rd.setRMagazzinoKit(mag);
				rd.setNumeroRigaDocumento(riga.getNumeroRigaDocumento());
				rd.setDettaglioRigaDocumento(riga.getDettaglioRigaDocumento());
				// set tipo mont /smont

			}

		} else  if(tipo.equals("S")){
			// ************** SMONTAGGIO ***************

			String keyOrdine = BaseServlet.getStringParameter(getRequest(), "DocKey");
			
			String groupTC = BaseServlet.getStringParameter(getRequest(), "GruppoTC");
			String idRiga = BaseServlet.getStringParameter(getRequest(), "idRiga");
			
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
				// FIRAS 27/09/2021
				
				rd.setAnnoDocumento(ordine.getAnnoDocumento());
				rd.setNumeroDocumento(ordine.getNumeroDocumento());

				// FINE FIRAS

			}

			if (idRiga!=null && !idRiga.equals(""))
				rd.setNumeroRigaDocumento(Integer.valueOf(idRiga));
			
			try {
				
				if (groupTC != null && !groupTC.equals("null"))
					rd.setGruppoTC(Long.parseLong(groupTC));

				mag = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magKey, Magazzino.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		rd.setIdArticolo(idArticolo);
		if (mag != null) {
			
			rd.setRMagazzinoPart(mag);
			// rd.setIdMagazzinoPart(mag.getIdMagazzino());
			if (mag.getIdFornitoreAzienda() != null && !mag.getIdFornitoreAzienda().equals(""))
				rd.setTipoMontSmont('E');
			else
				rd.setTipoMontSmont('I');
		}

	}

	public void writeBodyStartElements(JspWriter out) throws IOException {

	}

	public void writeFormStartElements(JspWriter out) throws IOException {
		
	}

	public void writeFormEndElements(JspWriter out) throws IOException {
		
		 
	}

	public void writeBodyEndElements(JspWriter out) throws IOException {
		VtOrdVenRigaPrmMontSmontK rd = (VtOrdVenRigaPrmMontSmontK) getBODataCollector().getBo();
		String[] objectKey = (String[]) getServletEnvironment().getRequest().getAttribute("ObjectKey");
		 getServletEnvironment().getSession().setAttribute("ObjectKey", objectKey);
		 
		String keyRiga = BaseServlet.getStringParameter(getRequest(), "DocKey");
		String tipo = BaseServlet.getStringParameter(getRequest(), "Tipo");

		String idArticolo = null;
		idArticolo = BaseServlet.getStringParameter(getRequest(), "idArticolo");

		//Add function reload Matric
		if( getServletEnvironment().getRequest().getParameter("ObjectKey")!=null  
				&& !getServletEnvironment().getRequest().getParameter("ObjectKey").equals("null") && getServletEnvironment().getRequest().getParameter("FatherKey")!=null 
				&& !getServletEnvironment().getRequest().getParameter("FatherKey").equals("null"))
		{			
			
		out.println("<script>function matrixReolad(){");
		out.println("var param = 'thAction=MATRIX_RELOAD&ObjectKey=" + getServletEnvironment().getRequest().getParameter("ObjectKey") + "&FatherKey=" + getServletEnvironment().getRequest().getParameter("FatherKey") +"';");
		out.println("var url = parent.opener.location.href");
		out.println("var b=url.indexOf('?');");
		out.println("if(b> -1)");
		out.println("url=url.substr(0, b);");
		out.println("url = url+'?'+ param;");
		
		out.println("parent.opener.location.href = url;");
		out.println("}</script>");
		}
		else
		{
			out.println("<script>function matrixReolad(){");
			out.println("window.opener.location.reload(true)");
			out.println("}</script>");
		}
		
		if (tipo.equals("M")) {
			if (idArticolo == null) {
				VtOrdineVenditaRigaPrm riga = null;
				if (keyRiga != null && !keyRiga.equals(""))
					try {
						riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
								keyRiga, VtOrdineVenditaRigaPrm.NO_LOCK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				if (riga != null)
					idArticolo = riga.getIdArticolo();
			}
		}

		if (idArticolo != null && !idArticolo.equals("null") && !tipo.equals("OS")) {
			String vistaDisponKey = ParametroPsn.getValoreParametroPsn(FUNZIONE_DISP_MAG, PARAM_DISP_MAG);

			VistaDisponibilita vistaDisponibilita = null;

			if (!vistaDisponKey.equals("")) {
				String vistaKey = KeyHelper
						.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), vistaDisponKey });
				try {
					vistaDisponibilita = (VistaDisponibilita) PersistentObject.elementWithKey(VistaDisponibilita.class,
							vistaKey, PersistentObject.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (vistaDisponibilita != null) {

				Iterator magIter = vistaDisponibilita.getMagazzini().iterator();
				// GET Dati kit

				// TODO verificare l'IDArticolo da passare
				// ModelloProduttivo modProd =
				// getModProdFromArticoloPatron(Azienda.getAziendaCorrente(),
				// riga.getIdArticolo());

				ModelloProduttivo modProd = getModProdFromArticoloPatron(Azienda.getAziendaCorrente(), idArticolo);
				// TODO se modProd=null verificare se esiste l'articolo PAtron perchè in questo
				// caso si tratta di idarticolo singolo
				VtAttivitaProduttiva attivita = null;
				if (modProd != null && modProd.getAttivita() != null) {
					Iterator attIter = modProd.getAttivita().iterator();
					attivita = (VtAttivitaProduttiva) attIter.next();
					Iterator compIter = attivita.getMateriali().iterator();

					out.println("<script language='JavaScript1.2'>");
					
						
					if (tipo.equals("M")) {
						// Set per montaggio
						out.println("document.getElementById('labelWhere').innerHTML = 'Montaggio Kit (IdProdoto: "
								+ modProd.getKey() + ")'");
						out.println(" parent.document.getElementById('OKSMONT').style.visibility=\"hidden\";");

					} else {
						// set per smontaggio
						out.println("document.getElementById('labelWhere').innerHTML = 'Smontaggio Kit (IdProdoto: "
								+ modProd.getKey() + ")'");
						out.println(" parent.document.getElementById('OKMONT').style.visibility=\"hidden\";");
					}

					// modify title

					int x = 0;
					String rigaValue = "";
					String rigaKey = "";
					// add riga magazzini
					String prmMagazzini = "var magazzini = [''";
					String prmStyleMag = "var prmstyleMag = ['cellStyleMagazzino'";
					String indexMagArray = "var indexMag= [''";
					String indexArtArray = "var indexArt= [''";

					String prmStyleArticolo = "var prmstyle = ['cellStyleArticolo'";
					String prmArticolo = "var parameter = [";

					String prmStyleComp = "var prmstyleComp = ['cellStyleComponente'";
					List magazzini = new ArrayList();

					// add righe componenti kit
					while (magIter.hasNext()) {
						Magazzino mag = (Magazzino) magIter.next();
						prmMagazzini = prmMagazzini + ",'" + mag.getDescrizione().getDescrizione() + "'";
						prmStyleMag = prmStyleMag + ",'cellStyleMagazzino'";
						magazzini.add(mag.getKey());
						VtArticolo articolo = VtTessileUtil.getVtArticoloFromId(Azienda.getAziendaCorrente(),
								modProd.getIdArticolo());
						// rigaValue=articolo.getChiaveProdotto16().trim()+"-" +
						// articolo.getColoreVariante().trim() +"-PZ " +
						// String.valueOf(modProd.getQuantitaBase());
						rigaValue = articolo.getChiaveProdotto1().trim() + "-" + articolo.getChiaveProdotto16().trim()
								+ "-" + articolo.getColoreVariante().trim() + "-"
								+ articolo.getChiaveProdotto17().trim() + "("
								+ String.valueOf(modProd.getQuantitaBase()) + ")";
						rigaKey = articolo.getChiaveProdotto16().trim() + "-KIT-"
								+ String.valueOf(modProd.getQuantitaBase()) + "-" + mag.getKey();
						// Add Riga articolo
						if (x == 0)
							prmArticolo = prmArticolo + "'" + rigaValue + "',";
						// else if(x==1)
						// prmArticolo =prmArticolo + "'" + String.valueOf(modProd.getQuantitaBase())+
						// "',";
						else
							prmArticolo = prmArticolo + "'',";

						// prmArticolo =prmArticolo + "'',";

						prmStyleArticolo = prmStyleArticolo + ",'cellStyleDati'";
						indexMagArray = indexMagArray + ",'" + mag.getKey() + "'";

						prmStyleComp = prmStyleComp + ",'cellStyleDati'";
						indexArtArray = indexArtArray + ",'" + rigaKey + "'";

						x++;
						if (x >= 5)
							break;

					}

					prmMagazzini = prmMagazzini + "];";
					prmStyleMag = prmStyleMag + "];";
					indexMagArray = indexMagArray + "];";

					out.println(prmMagazzini);
					out.println(prmStyleMag);
					out.println(indexMagArray);
					out.println(" addRowMatrice('Matrice',indexMag,magazzini,prmstyleMag);");

					prmArticolo = prmArticolo + "''];";
					prmStyleArticolo = prmStyleArticolo + "];";

					indexArtArray = indexArtArray + "];";
					out.println(prmStyleArticolo);
					out.println(prmArticolo);
					out.println(indexArtArray);
					out.println(" addRowMatrice('Matrice',indexArt,parameter,prmstyle );");

					prmStyleComp = prmStyleComp + "];";
					out.println(prmStyleComp);

					int y = 0;
					// add righe componenti kit
					while (compIter.hasNext()) {
						AttivitaProdMateriale componente = (AttivitaProdMateriale) compIter.next();
						String prmArray = "var parameter" + String.valueOf(y) + " = [";
						String indexArray = "var index" + String.valueOf(y) + " = [";
						VtArticolo art = VtTessileUtil.getVtArticoloFromId(Azienda.getAziendaCorrente(),
								componente.getIdArticolo());

						String colore = art.getColoreVariante();
						rigaValue = VTMontSmontUtility.makeComponenValue(art, componente);
						// rigaValue=art.getChiaveProdotto16().trim()+"-" + colore.trim() +"-PZ " +
						// componente.getCoeffImpiego().toBigInteger().toString();
						rigaKey = "";

						prmArray = prmArray + "'" + rigaValue + "',";
						indexArray = indexArray + "'',";

						int count = 1;
						Iterator magazziniIter = magazzini.iterator();

						while (magazziniIter.hasNext()) {
							String magazzinoKey = (String) magazziniIter.next();
							rigaKey = VTMontSmontUtility.makeComponenKey(art, componente, magazzinoKey);
							// if(count==1)
							// prmArray =prmArray + "'" +
							// componente.getCoeffImpiego().toBigInteger().toString()+ "',";
							// else
							prmArray = prmArray + "'',";
							indexArray = indexArray + "'" + rigaKey + "',";
							count++;
						}

						prmArray = prmArray.substring(0, prmArray.length() - 1);
						prmArray = prmArray + "];";
						indexArray = indexArray.substring(0, indexArray.length() - 1);
						indexArray = indexArray + "];";
						out.println(prmArray);
						out.println(indexArray);
						out.println(" addRowMatrice('Matrice',index" + String.valueOf(y) + ",parameter"
								+ String.valueOf(y) + ", prmstyleComp);");
						y++;
					}

					out.println("</script>");
				}

			} else {

				try {

					out.println("<script language='JavaScript1.2'>");
					out.println("parent.alert('Non sono stati trovati articoli o vista diponibilità non valida.');");
					out.println("parent.window.close(); ");
					out.println("</script>");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}

		}

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
