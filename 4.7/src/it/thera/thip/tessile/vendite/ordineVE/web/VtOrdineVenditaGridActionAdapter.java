package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Set;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebMenuBar;
import com.thera.thermfw.web.WebMenuItem;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;

import it.thera.thip.base.azienda.Azienda;

import it.thera.thip.base.comuniVenAcq.StatoEvasione;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.generale.ParametroPsnTM;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.cs.ThipException;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.tagliaColore.servlet.VtGestioneTagliaColoreUtil;
import it.thera.thip.tessile.vendite.ordineVE.*;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRiga;
import it.thera.thip.vendite.ordineVE.web.OrdineVenditaGridActionAdapter;

/**
 * VtOrdineVenditaGridActionAdapter.<br>
 *
 * <br>
 * <br>
 * <b>Copyright (c): SiConsult</b>
 *
 * @author Marco Amato 28/12/2016
 *
 */
public class VtOrdineVenditaGridActionAdapter extends OrdineVenditaGridActionAdapter {

	private static final long serialVersionUID = 1L;
	public static final String ADD_ORD_DERIVATO = "ADD_ORD_DERIVATO";
	// public static final String RIEPILOGO_ORDINI = "RIEPILOGO_ORDINI";
	public static final String RESOURCE = "it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdineVendita";
	public static final String FUNZIONE = "tessile.ordini.a.disporre";
	public static List listSerieADisporre = null;
	public static final String CONFERMA_ORDINE_EDI = "CONFERMA_ORDINE_EDI";
	public static final String SOLLECITO_EDI = "SOLLECITO_EDI";

	public static final String ESTRAZIONE_PSO = "ESTRAZIONE_PSO"; // fix 80987

	// Fix 81132 - Aggiungo tasto per l'apertura degli ordini di vendita ragruppati
	// per patron
	public static final String ORD_VEN_GROUP = "ORD_VEN_GROUP";

	public static final String STAMPA_DIRETTA = "StampaDiretta";
	public static final String AGGR_RIGHE = "AGGR_RIGHE";

	/**
	 * Ridefinizione
	 */
	public void modifyMenuBar(WebMenuBar menuBar) {
		super.modifyMenuBar(menuBar);
		WebMenuItem addConfOrdineEDI = new WebMenuItem("AddConfOrdineEDI", "action_submit", "infoArea", "no", RESOURCE,
				"AddConfOrdineEDI", "CONFERMA_ORDINE_EDI", "multiple", false);
		menuBar.addMenu("SelectedMenu.EvasioneGuidata", addConfOrdineEDI);

		WebMenuItem addInvSollEDI = new WebMenuItem("AddInvSollEDI", "action_submit", "infoArea", "no", RESOURCE,
				"AddInvSollEDI", "SOLLECITO_EDI", "multiple", false);
		menuBar.addMenu("SelectedMenu.EvasioneGuidata", addInvSollEDI);

		WebMenuItem addOrdDerivato = new WebMenuItem("AddOrdDerivato", "action_submit", "infoArea", "no", RESOURCE,
				"AddOrdDerivato", "ADD_ORD_DERIVATO", "single", false);
		menuBar.addMenu("SelectedMenu.EvasioneGuidata", addOrdDerivato);

	}

	/**
	 * Ridefinizione
	 */
	public void modifyToolBar(WebToolBar toolBar) {
		super.modifyToolBar(toolBar);
		
		
		//toolBar.getOwnerForm().getBODataCollector().getErrorList().addError(new ErrorMessage(""));
		
		
		WebToolBarButton addOrdDerivato = new WebToolBarButton("AddOrdDerivato", "action_submit", "new", "no", RESOURCE,
				"AddOrdDerivato", "it/thera/thip/tessile/vendite/ordineVE/images/AddOrdDerivato.gif", ADD_ORD_DERIVATO,
				"single", false, false, "");
		toolBar.addButton("New", addOrdDerivato);
		toolBar.addSeparator("StampaDiretta");
		// 80839
		// WebToolBarButton modificaArticolo = new WebToolBarButton(
		// "riepilogoOrdini", "action_submit", "new", "no", RESOURCE,
		// "riepilogoOrdini",
		// "it/thera/thip/tessile/vendite/generaleVE/images/VtStampaRiepilogoOrdini.gif",
		// RIEPILOGO_ORDINI,
		// "multipleSelSingleWindow",
		// false);
		// toolBar.addButton(modificaArticolo);

		WebToolBarButton addConfOrdineEDI = new WebToolBarButton("AddConfOrdineEDI", "action_submit", "infoArea", "no",
				RESOURCE, "AddConfOrdineEDI", "it/thera/thip/tessile/vendite/ordineVE/images/InvioConfOrdineEDI.gif",
				CONFERMA_ORDINE_EDI, "multiple", true, false, "");
		toolBar.addButton(addConfOrdineEDI);
		WebToolBarButton addInvSollEDI = new WebToolBarButton("AddInvSollEDI", "action_submit", "infoArea", "no",
				RESOURCE, "AddInvSollEDI", "it/thera/thip/tessile/vendite/ordineVE/images/InvioConfOrdineEDI.gif",
				SOLLECITO_EDI, "multiple", true, false, "");
		toolBar.addButton(addInvSollEDI);

		// Fix 80987 - Inizio
		WebToolBarButton estrazionePSO = new WebToolBarButton("EstrazionePSO", "action_submit", "new", "no", RESOURCE,
				"EstrazionePSO", "com/thera/thermfw/gui/images/Batch.gif", ESTRAZIONE_PSO, "none", false, false, "");
		toolBar.addButton(estrazionePSO);
		// Fix 80987 - Fine

		// Fix 81132 - Inizio
		WebToolBarButton apriOrdVenGroup = new WebToolBarButton("apriOrdVenGroup", "action_submit", "new", "no",
				RESOURCE, "apriOrdVenGroup", "it/thera/thip/tessile/tagliaColore/images/matrice_add.gif", ORD_VEN_GROUP,
				"none", false, false, "");
		toolBar.addButton(apriOrdVenGroup);
		// Fix 81132 - Fine

		WebToolBarButton inviaPezza = new WebToolBarButton("StampaDiretta", "action_submit", "new", "no",
				"it.thera.thip.tessile.acquisti.generaleAC.resources.VtCausaleOrdineAcquisto", "StampaDiretta",
				"it/thera/thip/tessile/base/documenti/images/StampaDiretta.gif", STAMPA_DIRETTA, "single", false, false,
				"");

		toolBar.addButton(inviaPezza);
		// BRY add button aggregazione righe ordine
		toolBar.addSeparator("StampaDiretta");

		WebToolBarButton btnAggrRighe = new WebToolBarButton("AggrRighe", "action_submit", "infoArea", "no", RESOURCE,
				"AggrRighe", "it/thera/thip/tessile/vendite/ordineVE/images/Aggregazione_OrdVenRighe.gif", AGGR_RIGHE,
				"multiple", true, false, "");

		toolBar.addButton(btnAggrRighe);
	}

	// Fix 12808 inizio
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		// super.otherActions(cadc, se); //80839
		String azione = getAzione(se);
		// Fix 81192
		boolean isGestioneTC = VtGestioneTagliaColoreUtil.isGestioneTagliaColore(Azienda.getAziendaCorrente());
		// 81903 INIZIO - IGPA APERTURA STD ANCHE SE TC ATTIVO -PER ORA LASCIO COME
		// PRIMA
		if (azione.equals("UPDATE_RIGHE") && isGestioneTC) {
			apriOrdineVenditaGroup(se);
		}
		// Fix 81192 fine
		else
		// 81903 FINE
		if (azione.equals(ADD_ORD_DERIVATO)) {
			aggiungiOrdineDerivato(cadc, se);
		}
		// Fix 81132
		else if (azione.equals(ORD_VEN_GROUP)) {
			apriOrdineVenditaGroup(se);
		} else if (azione.equalsIgnoreCase(STAMPA_DIRETTA)) {
			stampaDiretta(se);
		} else if (azione.equalsIgnoreCase(AGGR_RIGHE)) {
			try {
				apriAggregazioneRighe(se);
			} catch (ThipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Fix 81132 fine

		// 80839 - inizio - Stampa riepilogo ordini
		// else {
		// if (azione.equals(RIEPILOGO_ORDINI)) {
		// lanciaRiepilogoOrdini(se);
		// }
		else if (azione.equals(CONFERMA_ORDINE_EDI) || azione.equals(SOLLECITO_EDI)) {
			String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
			String chiave = "";
			String chiaveTot = "";
			for (int i = 0; i < objectKeys.length; i++) {
				chiaveTot = objectKeys[i];
				chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) + PersistentObject.KEY_SEPARATOR
						+ KeyHelper.getTokenObjectKey(chiaveTot, 2) + PersistentObject.KEY_SEPARATOR
						+ KeyHelper.getTokenObjectKey(chiaveTot, 3) + PersistentObject.KEY_SEPARATOR
						+ KeyHelper.getTokenObjectKey(chiaveTot, 4);
				eseguiAzione(chiave, azione);
			}
		} else {
			// 80987 - Inizio
			if (azione.equals(ESTRAZIONE_PSO)) {
				String[] objKeys = se.getRequest().getParameterValues("ObjectKey");
				String[] keys = KeyHelper.unpackObjectKey(objKeys[0]);
				String idAzienda = keys[0];
				String anno = keys[1];
				String numero = keys[2];
				VtOrdineVendita ordineVendita = null;
				String nDocFormat = "";
				try {
					ordineVendita = (VtOrdineVendita) VtOrdineVendita.elementWithKey(VtOrdineVendita.class,
							KeyHelper.buildObjectKey(new Object[] { idAzienda, anno, numero }),
							PersistentObject.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (ordineVendita != null) {
					nDocFormat = ordineVendita.getNumeroDocumentoFormattato();
				}

				String url = "/it/thera/thip/tessile/produzione/rilDaOrdVen/VtRilascioDaOrdVenEstrBatch.jsp"
						+ "?IdAnnoOrdCli=" + anno + "&NumeroOrdCli=" + numero + "&NumeroDocumentoFormattato="
						+ nDocFormat;
				se.sendRequest(getServletContext(), url, false);
				// 80987 - Fine
			} else
				super.otherActions(cadc, se);
		}
		// 80839 - fine
		// }

	}

	public void stampaDiretta(ServletEnvironment se) throws ServletException, IOException {
		String[] objectKeys = se.getRequest().getParameterValues("ObjectKey");
		if (objectKeys != null) {
			String key = objectKeys[0];
			se.sendRequest(getServletContext(),
					"it/thera/thip/tessile/vendite/ordineVE/VtReportConfermaOrdVenBatch.jsp?DocKey=" + key, true);
		}
	}

	/**
	 * Aggiungo un ordine derivato a fronte di un ordine a disporre scelto
	 * 
	 * @param cadc
	 * @param se
	 * @throws ServletException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public void aggiungiOrdineDerivato(ClassADCollection cadc, ServletEnvironment se)
			throws ServletException, IOException {

		String selectedKey = getSelectedKeyForAction(se.getRequest());
		String idAzi = KeyHelper.getTokenObjectKey(selectedKey, 1);
		String anno = KeyHelper.getTokenObjectKey(selectedKey, 2);
		String num = KeyHelper.getTokenObjectKey(selectedKey, 3);

		String ser = num.substring(0, 2);
		// controllo che la serie dell'ordine scelto corrisponda con una serie
		// relativa a ORDINI A DISPORRE (impostati su parametri
		// personalizzazione)
		boolean ok = isOrdineADisporre(ser);
		if (ok) {
			String url = "it/thera/thip/tessile/vendite/ordineVE/VtOrdVenTestataNuovo.jsp"
					+ "?Mode=NEW&AnnoOrdineAdisp=" + anno + "&NumeroOrdAdisp=" + num + "&IdAzienda=" + idAzi
					+ "&InitialActionAdapter=" + getClass().getName();
			se.sendRequest(getServletContext(), url, false);

			String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
			se.sendRequest(getServletContext(), urlr, true);
		} else {
			String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenNuovoErr1.jsp";
			se.sendRequest(getServletContext(), urlr, true);
		}

	}

	/**
	 * In base alla serie passata, restituisco TRUE se l'ordine è a disporre, FALSE
	 * per tutti gli altri tipi ordine
	 * 
	 * @param serie
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	public static boolean isOrdineADisporre(String serie) {
		// preparo lettura parametri di personalizzazione relativi alle serie di
		// Ordini a Disporre
		String where = ParametroPsnTM.ID_FUNZIONE + " = '" + FUNZIONE + "'" + " AND " + ParametroPsnTM.STATO + " = '"
				+ DatiComuniEstesi.VALIDO + "'";
		String orderby = "";

		List pars = new ArrayList();
		try {
			pars = ParametroPsn.retrieveList(where, orderby, false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Iterator iter = pars.iterator();
		while (iter.hasNext()) {
			ParametroPsn psn = (ParametroPsn) iter.next();

			// alla prima serie tra quelle relative a Ordini A Disporre che
			// corrisponde alla serie indicata, allora restituisco TRUE
			if (serie.equals(psn.getValore())) {
				return true;
			}

		}

		return false;
	}

	// 80839
	public void lanciaRiepilogoOrdini(ServletEnvironment se) throws ServletException, IOException {
		String[] selectedKey = se.getRequest().getParameterValues("ObjectKey");
		String key = "";
		for (int i = 0; i < selectedKey.length; i++) {
			key = key + selectedKey[i];
			key = key + "$";
		}
		String url = "/it/thera/thip/tessile/vendite/generaleVE/VtStampaRiepilogoOrdini.jsp?Mode=NEW&thAction=RIEPILOGO_ORDINI"
				+ "&chiave=" + URLEncoder.encode(key) + "&IdAzienda=" + Azienda.getAziendaCorrente();
		se.sendRequest(getServletContext(), url, false);
	}

	// IGPA 20160915 INIZIO GESTIONE INVIO CONFERMA EDI E SOLLECITO

	protected void eseguiAzione(String chiave, String azione) throws ServletException, IOException {
		if (azione.equals(CONFERMA_ORDINE_EDI) || azione.equals(SOLLECITO_EDI))
			invioConfermaEDI(chiave, azione);

	}

	public boolean invioConfermaEDI(String key, String azione) throws ServletException, IOException {
		boolean isOk = false;

		VtConfermaOrdineEDI confermaEDI = (VtConfermaOrdineEDI) Factory.createObject(VtConfermaOrdineEDI.class);
		try {
			confermaEDI.initialize(key, azione);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isOk;
	}

	// IGPA 20160915 FINE GESTIONE INVIO CONFERMA EDI

	// Fix 81057 - Inizio
	public void processAction(ServletEnvironment se) throws ServletException, IOException {
		String azione = getAzione(se);
		HttpServletRequest req = se.getRequest();
		String[] keys = req.getParameterValues(OBJECT_KEY);
		
		
		VtOrdineVendita ordVen = null;

		if (azione.equals("DELETE")) // Se è un ordine derivato aggiorno le qta dell'ordine a disporre
		{
			try {
				ordVen = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, keys[0],
						PersistentObject.NO_LOCK);
				List lstRighe = ordVen.getRighe();
				Iterator iter = lstRighe.iterator();
				while (iter.hasNext()) {
					VtOrdineVenditaRigaPrm rigaOrd = (VtOrdineVenditaRigaPrm) iter.next();
					if (rigaOrd != null) {
						BigDecimal qta1 = rigaOrd.getQtaInUMRif();
						// aggancio la riga a disporre collegata

						if (ordVen.getAnnoOrdineAdisp() != null && !ordVen.getAnnoOrdineAdisp().equals("")) {
							String keyAdisp = rigaOrd.getIdAzienda() + KeyHelper.KEY_SEPARATOR
									+ ordVen.getAnnoOrdineAdisp() + KeyHelper.KEY_SEPARATOR + ordVen.getNumeroOrdAdisp()
									+ KeyHelper.KEY_SEPARATOR + rigaOrd.getNumRigaAdisp();
							VtOrdineVenditaRigaPrm ordAdi = (VtOrdineVenditaRigaPrm) PersistentObject
									.elementWithKey(VtOrdineVenditaRigaPrm.class, keyAdisp, PersistentObject.NO_LOCK);

							if (ordAdi != null) {
								if (ordAdi.getQtaDisposta() == null)
									ordAdi.setQtaDisposta(new BigDecimal(0));

								// tolgo qta disposta
								ordAdi.setQtaDisposta(ordAdi.getQtaDisposta().subtract(qta1));
								// aggiungo a qta residua
								ordAdi.setQtaResidua(ordAdi.getQtaResidua().add(qta1));

								VtTessileUtil.genericSave(ordAdi);
							}
						}

					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		super.processAction(se);
	}
	// Fix 81057 - Fine

	// Fix 81132 - Metodo per aprire gli ordini di vendita con griglia righe
	// raggruppate per patron
	public void apriOrdineVenditaGroup(ServletEnvironment se) throws ServletException, IOException {

		String selectedKey = getSelectedKeyForAction(se.getRequest());
		if (selectedKey != null) {

			String url = "/it/thera/thip/tessile/vendite/ordineVE/DocumentoEstrattoTagliaColore.jsp?thMode=UPDATE&thModoIniziale=UPDATE&thModoInizialeRigaPrm=UPDATE&Key="
					+ selectedKey
					+ "&thTipoEstratto=4&InitialActionAdapter=it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineVenditaGridActionAdapter";
			/// it/thera/thip/base/documenti/DocumentoEstratto.jsp?Mode=UPDATE&Key=001%162017%16IT++000014&InitialActionAdapter=it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineVenditaGridActionAdapter&thTipoEstratto=4&thChiaveDatiSessione=ADMIN_001-OrdineVendita-1507812545258
			se.sendRequest(getServletContext(), url, true);
		}

	}

	public void apriAggregazioneRighe(ServletEnvironment se) throws ThipException, ServletException, IOException {
		HttpServletRequest req = se.getRequest();
		String[] keys = req.getParameterValues(OBJECT_KEY);

		VtOrdineVendita ordVen = null;
		List lstRighe = new ArrayList();
		int NumRigheTot=0;

		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {

				try {
					ordVen = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, keys[i],
							PersistentObject.NO_LOCK);

					if (ordVen != null) {
						lstRighe = ordVen.getRighe();
						setGruppoTCFromPatron(lstRighe.iterator());
						
						//Aggregazione righe 
						aggregaRighe(lstRighe.iterator(), ordVen);
						NumRigheTot +=lstRighe.size();
						lstRighe.clear();
						
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			invokeAlertWindow(se, "Aggregazione righe  terminata con successo: elaborate N."
					+ String.valueOf(NumRigheTot) + " righe.");
		
		}
		else			
			invokeAlertWindow(se, "Aggregazione righe  terminata: nessuna riga elaborata.");
			
			//new ErrorMessage("THIP300113");
		
	}

	protected void aggregaRighe(Iterator iterRighe, VtOrdineVendita ordVenOrig) throws SQLException {
		Map mapRighe = mappaKeyRigaQuantita(iterRighe);
		List lstRigheSec=new ArrayList();
		
		Set idriga = mapRighe.keySet();
		Iterator artIter = idriga.iterator();
		while (artIter.hasNext()) {
			String key = (String) artIter.next();
			List lstKeyRighe = (List) mapRighe.get(key);
			if (lstKeyRighe.size() > 1) {
				int res = -1;

				// ciclo le righe con lo stesso Patron

				Iterator iter = lstKeyRighe.iterator();
				VtOrdineVenditaRigaPrm rigaNew = null;
				lstRigheSec.clear();
				
				//aggrega righe primarie
				while (iter.hasNext()) {
					VtOrdineVenditaRigaPrm rigaOrd = (VtOrdineVenditaRigaPrm) iter.next();

					if (rigaNew == null) {
						rigaNew = rigaOrd;

					} else {
						
						if(rigaOrd.getQtaInUMRif()!=null)
							rigaNew.setQtaInUMRif(rigaNew.getQtaInUMRif().add(rigaOrd.getQtaInUMRif()));
						if(rigaOrd.getQtaInUMPrmMag()!=null)
							rigaNew.setQtaInUMPrmMag(rigaNew.getQtaInUMPrmMag().add(rigaOrd.getQtaInUMPrmMag()));

						
						ordVenOrig.getRighe().remove(rigaOrd);
						res = rigaOrd.delete();
					}
					lstRigheSec.addAll(rigaOrd.getRigheSecondarie());
				}
				
				//Aggrega righe secondarie
				if(lstRigheSec.size()>0)
				{
					Map mapRigheSeco = mappaKeyRigaQuantita(lstRigheSec.iterator());
					
					Set idRigaSec = mapRigheSeco.keySet();
					Iterator rigaIter = idRigaSec.iterator();
					while (rigaIter.hasNext()) {
						String keyRigaSec = (String) rigaIter.next();
						List lstKeyRigheSec = (List) mapRigheSeco.get(keyRigaSec);
						if (lstKeyRigheSec.size() > 1) {
							int resSec = -1;
							Iterator iterSec = lstKeyRigheSec.iterator();
							VtOrdineVenditaRigaSec rigaSecNew = null;
							
							while (iterSec.hasNext()) {
								VtOrdineVenditaRigaSec rigaOrdSec = (VtOrdineVenditaRigaSec) iterSec.next();

								if (rigaSecNew == null) {
									rigaSecNew = rigaOrdSec;

								} else {
									
									if(rigaOrdSec.getQtaInUMRif()!=null)
										rigaSecNew.setQtaInUMRif(rigaSecNew.getQtaInUMRif().add(rigaOrdSec.getQtaInUMRif()));
									if(rigaOrdSec.getQtaInUMPrmMag()!=null)
										rigaSecNew.setQtaInUMPrmMag(rigaSecNew.getQtaInUMPrmMag().add(rigaOrdSec.getQtaInUMPrmMag()));

									rigaNew.getRigheSecondarie().remove(rigaOrdSec);
									res = rigaOrdSec.delete();
								}								
							}							
						}
					}
					
				}
				
				if (rigaNew != null && res > 0) {
					res = rigaNew.save();
					if (res >= 0) {
						ConnectionManager.commit();
					} else
						ConnectionManager.rollback();
				}
				
							
				
			}
		}
	}
	
	protected void setGruppoTCFromPatron(Iterator iterRighe) {
		Map ret = new HashMap();
		while (iterRighe.hasNext()) {
			VtOrdineVenditaRigaPrm rigaOrd = (VtOrdineVenditaRigaPrm) iterRighe.next();
			String keyMap = KeyHelper.buildObjectKey(new String[] { rigaOrd.getAlfanumRiservatoUtente2() });
			List valueMap = new ArrayList();
			if (ret.containsKey(keyMap)) {
				valueMap = (List) ret.get(keyMap);
				valueMap.add(rigaOrd);
			} else
				valueMap.add(rigaOrd);

			ret.put(keyMap, valueMap);
		}
		try {
			Set idriga = ret.keySet();
			Iterator artIter = idriga.iterator();
			while (artIter.hasNext()) {
				String key = (String) artIter.next();
				List lstKeyRighe = (List) ret.get(key);

				Iterator iter = lstKeyRighe.iterator();
				VtOrdineVenditaRigaPrm rigaNew = null;

				Long gruppoTC = null;
				int myRes = -1;
				while (iter.hasNext()) {
					VtOrdineVenditaRigaPrm rigaO = (VtOrdineVenditaRigaPrm) iter.next();
					if (gruppoTC == null)
						gruppoTC = rigaO.getGruppoTC();
					else {
						rigaO.setGruppoTC(gruppoTC);

						myRes = rigaO.save();

					}
				}

				ConnectionManager.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Map mappaKeyRigaQuantita(Iterator iterRighe) {
		Map ret = new HashMap();
		while (iterRighe.hasNext()) {
			OrdineVenditaRiga rigaOrd = (OrdineVenditaRiga) iterRighe.next();
			if (rigaOrd.getStatoEvasione() == StatoEvasione.INEVASO && rigaOrd.getQtaAttesaEvasione().isZero() && rigaOrd.getQtaPropostaEvasione().isZero()) {
				String keyMap = KeyHelper.buildObjectKey(new String[] { rigaOrd.getIdMagazzino(),
						rigaOrd.getAlfanumRiservatoUtente2(), rigaOrd.getIdArticolo(), rigaOrd.getIdCauRig() });
				List valueMap = new ArrayList();
				if (ret.containsKey(keyMap)) {
					valueMap = (List) ret.get(keyMap);
					valueMap.add(rigaOrd);
				} else
					valueMap.add(rigaOrd);

				ret.put(keyMap, valueMap);
			}
		}
		return ret;
	}

	private void invokeAlertWindow(ServletEnvironment se, String message) throws ServletException, IOException {

		PrintWriter wr = se.getResponse().getWriter();
		wr.println("<script language='JavaScript1.2'>");
		wr.println("alert('" + message + "');");
		wr.println("</script>");
	}
}