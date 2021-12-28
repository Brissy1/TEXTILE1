package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.base.IniFile;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebMenuAbstract;
import com.thera.thermfw.web.WebMenuBar;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;
import com.thera.thermfw.web.servlet.BaseServlet;
import com.thera.thermfw.web.servlet.GridActionAdapter;

import it.sicons.thip.tessile.web.utils.Constant;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.CentroLavoro;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.comuniVenAcq.web.DocOrdNavigazioneWeb;
import it.thera.thip.base.disponibilita.InquiryAnalisiDispWrapper;
import it.thera.thip.base.documenti.web.DocumentoDatiSessione;
import it.thera.thip.base.documenti.web.DocumentoGridActionAdapter;
import it.thera.thip.base.documenti.web.DocumentoNavigazioneWeb;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.interrogazioni.NavigatoreInterrogazione;
import it.thera.thip.base.interrogazioni.web.InterrogazioneMultiplaActionAdapter;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.cs.ThipException;
import it.thera.thip.datiTecnici.configuratore.Configurazione;
import it.thera.thip.datiTecnici.modpro.AttivitaRisorsa;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivo;
import it.thera.thip.magazzino.analisidisp.ParametriAnalisi;
import it.thera.thip.magazzino.analisidisp.web.AnalisiDispDataCollector;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiMat;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiPrd;
import it.thera.thip.produzione.ordese.AttivitaEsecMateriale;
import it.thera.thip.produzione.ordese.AttivitaEsecRisorsa;
import it.thera.thip.produzione.ordese.AttivitaEsecutiva;
import it.thera.thip.produzione.ordese.AttivitaEsecutivaTM;
import it.thera.thip.produzione.ordese.AzioniOrdine;
import it.thera.thip.tessile.acquisti.ordineAC.VtOrdineAcquistoRigaPrm;
import it.thera.thip.tessile.articolo.VtArticoliTex;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.base.articolo.ChiaveProdotto;
import it.thera.thip.tessile.base.articolo.SchemaProdottoLotto;
import it.thera.thip.tessile.datiTecnici.modpro.VtAttivita;
import it.thera.thip.tessile.generaleDT.VtConfParamTessili;
import it.thera.thip.tessile.produzione.ordese.VtAttivitaEsecMateriale;
import it.thera.thip.tessile.produzione.ordese.VtAttivitaEsecProdotto;
import it.thera.thip.tessile.produzione.ordese.VtAttivitaEsecProdottoTM;
import it.thera.thip.tessile.produzione.ordese.VtOrdEsecOrdVenRig;
import it.thera.thip.tessile.produzione.ordese.VtOrdEsecPezze;
import it.thera.thip.tessile.produzione.ordese.VtOrdineEsecutivo;
import it.thera.thip.tessile.produzione.rilDaOrdVen.VtGestioneOrdineEsecutivoProduzione;
import it.thera.thip.tessile.produzione.rilDaOrdVen.servlet.VtRilascioDaOrdVenTessConferma;
import it.thera.thip.tessile.produzione.rilDaOrdVen.web.VtRilascioDaOrdVenGridActionAdapter;
import it.thera.thip.tessile.tabelle.VtGeneraDibaModello;
import it.thera.thip.tessile.tabelle.VtGestioneRiserveLotti;
import it.thera.thip.tessile.tabelle.VtSchemiAbbinati;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.tagliaColore.servlet.VtGestioneTagliaColoreUtil;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigApriRilascioTessile;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigAtv;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigAtvTM;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigPrmMat;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigPrmMatTM;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigVarTM;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVendita;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrm;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrmTM;
import it.thera.thip.vendite.generaleVE.CausaleOrdineVendita;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaLottoPrm;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrm;
import it.thera.thip.vendite.ordineVE.web.OrdineVenditaRigaPrmGridActionAdapter;

/**
 * VtOrdineVenditaRigaPrmGridActionAdapter.<br>
 *
 * <br><br><b>Copyright (c): SIConsulting</b>
 *
 * @autore Andrea Calligaro
 */
/**
 * Revisions: Date Owner Description 14/12/2015 ANDC Prima stesura 01/03/2016
 * ANDC Inserita funzione nuovo ordine finito (e tessuto) 09/09/2016 ANDC
 * Inserita funzione formato riga ridotta per TESSILE (esclusa ridotta
 * tessuti/finito)
 */
public class VtOrdineVenditaRigaPrmGridActionAdapter extends OrdineVenditaRigaPrmGridActionAdapter {

	private static final long serialVersionUID = 1L;
	public static final BigDecimal ZERO = new BigDecimal(0.00D);
	public static final BigDecimal ZERO2 = new BigDecimal(0.00);

	protected static final String UPDATE_FLAG_ATV_FINALE = "UPDATE " + AttivitaEsecutivaTM.TABLE_NAME + " SET "
			+ AttivitaEsecutivaTM.ATV_FINALE + " = 'N' " + " WHERE " + AttivitaEsecutivaTM.ID_AZIENDA + " = ? "
			+ " AND " + AttivitaEsecutivaTM.ID_ANNO_ORD + " = ? " + " AND " + AttivitaEsecutivaTM.ID_NUMERO_ORD
			+ " = ? " + " AND " + AttivitaEsecutivaTM.ID_RIGA_ATTIVITA + " = ? ";

	protected static CachedStatement cUpdateFlagAtvFinale = new CachedStatement(UPDATE_FLAG_ATV_FINALE);

	public static final String RESOURCE = "it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrm";
	public static final String NUOVOTEX = "NUOVOTEX";
	public static final String NUOVOTEX_NOGRID = "NUOVOTEX_NOGRID";
	public static final String NUOVOFIN = "NUOVOFIN";
	public static final String NEWDISP = "NEWDISP";
	public static final String DELETE = "DELETE";
	public static final String CONF_PRE_ORD = "CONF_PRE_ORD";

	// Fix 81750
	public static final String NUOVOCOMP_NOGRID = "NUOVOCOMP_NOGRID";

	// MAAM 03/10/2016
	public static final String CARTELLA_CLIENTE = "CARTELLA_CLIENTE";
	public static final String ESTRAZIONE_PSO = "ESTRAZIONE_PSO";

	public static final String ORDINE_ACQUISTO = "ORDINE_ACQUISTO"; // fix Davide apre Ordine acquisto
	public static final String ORDINE_ESECUTIVO = "ORDINE_ESECUTIVO"; // fix Davide apre Ordine esecutivo
	public static final String CAMBIO_ARTICOLO_ORD_VEN = "CAMBIO_ARTICOLO_ORD_VEN";

	// Cristi Badacà Mihai 07/02/2017
	public static final String CREA_DISPO = "CREA_DISPO";

	protected static final String MATRICE_ADD = "MATRICE_ADD";
	protected static final String MATRICE_MOD = "MATRICE_MOD";
	// FIX 82134 FIRAS
	protected static final String MATRICE_DISP_MAG = "MATRICE_DISP_MAG";
	// FIX 82134 FINE

	protected static final String EVADI_MAG = "EVADI_MAG";
	protected static final String MONT_SMONT_KIT = "MONT_SMONT_KIT";
	
	protected static final String MONT_KIT = "MONT_KIT";
	protected static final String ONLY_SMONT_KIT = "ONLY_SMONT_KIT";
	protected static final String SMONT_KIT = "SMONT_KIT";
	protected static final String VIEW_DOC_MONT_SMONT = "VIEW_DOC_MONT_SMONT";
	protected static final String LIST_PSO = "LIST_PSO"; // 81165

	private static VtOrdineVenditaRigaPrm cInstance;

	protected static final String PASSA_A_PATRON = "PASSA_A_PATRON"; // 81903
	public static final String RESOURCE2 = "it.thera.thip.tessile.acquisti.ordineAC.resources.VtOrdineAcquisto"; // 81903

	public VtOrdineVenditaRigaPrmGridActionAdapter() {

	}

	public void modifyToolBar(WebToolBar toolBar) {
		super.modifyToolBar(toolBar);
		WebToolBarButton nuovoTex = new WebToolBarButton("NuovoTex", "action_submit", "new", "no", RESOURCE, "NuovoTex",
				"it/thera/thip/tessile/vendite/ordineVE/images/NewTex.gif", NUOVOTEX, "none", false, false, "");
		// WebToolBarButton nuovoFin = new WebToolBarButton("NuovoFin",
		// "action_submit", "new", "no", RESOURCE, "NuovoFin",
		// "it/thera/thip/tessile/vendite/ordineVE/images/NewFin.gif", NUOVOFIN,
		// "none", false, false, "");
		// fix 80779
		WebToolBarButton nuovoFin = new WebToolBarButton("NuovoFin", "action_submit", "new", "no", RESOURCE, "NuovoFin",
				"it/thera/thip/tessile/vendite/ordineVE/images/Cravatta.gif", NUOVOFIN, "none", false, false, "");

		WebToolBarButton confPre = new WebToolBarButton("ConfPre", "action_submit", "new", "no", RESOURCE, "ConfPre",
				"it/thera/thip/tessile/vendite/ordineVE/images/ConfFin.gif", CONF_PRE_ORD, "single", false, false, "");

		// MAAM 03/10/2016
		WebToolBarButton cartellaCli = new WebToolBarButton("CartellaCli", "action_submit", "new", "no", RESOURCE,
				"CartellaCli", "it/thera/thip/tessile/vendite/ordineVE/images/CartellaCiente.gif", CARTELLA_CLIENTE,
				"none", false, false, "");

		// Cristi Badacà Mihai 07/02/2017
		// WebToolBarButton creaDispo = new WebToolBarButton("CreaDsipo",
		// "action_submit", "new", "no", RESOURCE, "CreaDispo",
		// "it/thera/thip/cs/images/OrdPrd.gif", CREA_DISPO, "single", false, false,
		// "");
		WebToolBarButton creaDispo = new WebToolBarButton("CreaDsipo", "action_submit", "new", "no", RESOURCE,
				"CreaDispo", "it/thera/thip/cs/images/OrdPrd.gif", CREA_DISPO, "multipleSelSingleWindow", false, false,
				""); // Fix 81327

		// aggiungo il tasto dopo la NEW

		// toolBar.addButton("New", nuovoTex);
		// toolBar.addButton("NuovoTex", nuovoFin);

		// toolBar.addButton("NewCambioCau", confPre); //Fix 81155 - tolto tasto del
		// martelletto
		toolBar.addButton("NewCambioCau", cartellaCli);
		toolBar.addButton("NewCambioCau", nuovoFin);
		toolBar.addButton("NewCambioCau", nuovoTex);

		// Fix 80918 - Inizio
		WebToolBarButton apriRilascio = new WebToolBarButton("ApriRilascio", "action_submit", "new", "no",
				"it.thera.thip.produzione.pianifica.resources.RilascioEventiPianifica", "ApriRilascio",
				"it/thera/thip/produzione/pianifica/images/Rilascio.gif", "APRI_RILASCIO_TESSILE",
				"multipleSelSingleWindow", false, false, "");

		toolBar.addButton(apriRilascio);
		// Fix 80918 - Fine

		// Fix 80973 - Inizio
		WebToolBarButton estrazionePSO = new WebToolBarButton("EstrazionePSO", "action_submit", "new", "no", RESOURCE,
				"EstrazionePSO", "com/thera/thermfw/gui/images/Batch.gif", ESTRAZIONE_PSO, "none", false, false, "");
		toolBar.addButton(estrazionePSO);
		// Fix 80973 - Fine

		// Fix 81165 Davide INIZIO
		WebToolBarButton listPSO = new WebToolBarButton("ListPSO", "action_submit", "new", "no", RESOURCE, "ListPSO",
				"it/thera/thip/tessile/vendite/ordineVE/images/list_pso.gif", LIST_PSO, "none", false, false, "");

		toolBar.addButton(listPSO);
		// Fix 81165 Davide Fine

		/*
		 * WebToolBarButton ordAcq = new WebToolBarButton("OrdAcq", "action_submit",
		 * "new", "no", RESOURCE, "OrdAcq", "it/thera/thip/cs/images/OrdAcq.gif",
		 * ORDINE_ACQUISTO, "single", false, false, ""); toolBar.addButton(ordAcq);
		 * 
		 * WebToolBarButton ordEsec = new WebToolBarButton("OrdEsec", "action_submit",
		 * "new", "no", RESOURCE, "OrdEsec", "it/thera/thip/cs/images/OrdPrd.gif",
		 * ORDINE_ESECUTIVO, "single", false, false, ""); toolBar.addButton(ordEsec);
		 */

		ServletEnvironment se = toolBar.getServletEnvironment();
		if (se != null) {
			DocumentoDatiSessione datiSessione = getDatiSessione(se);
			if (datiSessione != null) {
				String keyOrd = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
				if (keyOrd != null) {
					VtOrdineVendita ordVen = null;
					try {
						ordVen = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, keyOrd,
								VtOrdineVendita.NO_LOCK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (ordVen != null) {
						CausaleOrdineVendita causale = ordVen.getCausale();
						Magazzino mag = causale.getMagazzino();
						// Fix 81327 if(mag.getTipoMagazzino() == '5')
						if (mag != null && mag.getTipoMagazzino() == '5')
							// toolBar.addButton("CartellaCli", creaDispo);
							toolBar.addButton("PassaSmart", creaDispo); // Fix 81327
						// toolBar.addButton("HelpTopics", creaDispo); // Fix 81327

					}
				}
			}
		}

		// Cristi - Inizio
		WebToolBarButton matriceAdd = new WebToolBarButton("MatriceAdd", "action_submit", "new", "no", RESOURCE,
				"MatriceAdd", "it/thera/thip/tessile/tagliaColore/images/matrice_add.gif", MATRICE_ADD, "none", false,
				false, "");

		toolBar.addButton(matriceAdd);

		WebToolBarButton matriceMod = new WebToolBarButton("MatriceMod", "action_submit", "new", "no", RESOURCE,
				"MatriceMod", "it/thera/thip/tessile/tagliaColore/images/matrice_mod.gif", MATRICE_MOD, "single", false,
				false, "");

		toolBar.addButton(matriceMod);
		// Cristi - Fine
		// FIX 82134 FIRAS
		WebToolBarButton matriceDispMag = new WebToolBarButton("MatriceDispMag", "action_submit", "new", "no", RESOURCE,
				"MatriceDispMag", "it/thera/thip/magazzino/movimenti/images/Configurazione.gif", MATRICE_DISP_MAG,
				"single", false, false, "");

		toolBar.addButton(matriceDispMag);
		// FIX 82134 FINE

		// AS 000116 18/04/2018

		WebToolBarButton cambioArtOrdineVen = new WebToolBarButton("CambioArticoloOrdVen", "action_submit", "infoArea",
				"no", RESOURCE, "CambioArticoloOrdVen", "it/thera/thip/tessile/CambioArt/images/CambioArt.gif",
				CAMBIO_ARTICOLO_ORD_VEN, "multiple", false);
		toolBar.addButton("Copy", cambioArtOrdineVen);

		// FINE AS 000116 18/04/2018

		// 81903 ini
		WebToolBarButton passsaAPatron = new WebToolBarButton("PassaAPatron", "action_submit", "same", "no", RESOURCE2,
				"PassaAPatron", "it/thera/thip/base/documenti/ToRighe.gif", PASSA_A_PATRON, "none", false, false, "");
		toolBar.addButton(passsaAPatron);
		// 81903 fin
		toolBar.addSeparator("PassaAPatron");
		// BRY add smontaggio kit
		WebToolBarButton viewDocMontSmont = new WebToolBarButton("ViewDocMontSmont", "action_submit", "new", "no",
				RESOURCE, "ViewDocMontSmont", "it/thera/thip/base/comuniVenAcq/image/DocumentoRow.gif",
				VIEW_DOC_MONT_SMONT, "none", false, false, "");
		toolBar.addButton(viewDocMontSmont);

		// BRY add evasione con sdoppio riga o magazzino di trasferimento
		WebToolBarButton evadiMagDest = new WebToolBarButton("EvadiMagDest", "action_submit", "new", "no", RESOURCE,
				"EvadiMagDest", "it/thera/thip/base/comuniVenAcq/image/EvadiDocumento.gif", EVADI_MAG, "", false, false,
				"");
		toolBar.addButton(evadiMagDest);

		// BRY add evasione con sdoppio riga o magazzino di trasferimento
		WebToolBarButton onlySmontKit = new WebToolBarButton("OnlySmontKit", "action_submit", "new", "no", RESOURCE,
				"OnlySmontKit", "it/thera/thip/base/comuniVenAcq/image/EvaOrdAcqDir.gif", ONLY_SMONT_KIT, "multipleSelSingleWindow", false,
				false, "");
		toolBar.addButton(onlySmontKit);

		// BRY add montaggio e smontaggio kit
		WebToolBarButton montSmontKit = new WebToolBarButton("MontSmontKit", "action_submit", "new", "no", RESOURCE,
				"MontSmontKit", "it/thera/thip/tessile/vendite/ordineVE/images/IconaMontaggio.gif", MONT_SMONT_KIT,
				"single", false, false, "");
		// toolBar.addButton(montSmontKit);
		// BRY add montaggio kit
		WebToolBarButton montKit = new WebToolBarButton("MontKit", "action_submit", "new", "no", RESOURCE, "MontKit",
				"it/thera/thip/tessile/vendite/ordineVE/images/IconaMontaggio.gif", MONT_KIT, "single", false, false,
				"");
		toolBar.addButton(montKit);
		// BRY add smontaggio kit
		WebToolBarButton smontKit = new WebToolBarButton("SmontKit", "action_submit", "new", "no", RESOURCE, "SmontKit",
				"it/thera/thip/tessile/vendite/ordineVE/images/IconaSmontaggio.gif", SMONT_KIT, "none", false, false,
				"");
		toolBar.addButton(smontKit);

	}

	public void modifyMenuBar(WebMenuBar menuBar) {
		super.modifyMenuBar(menuBar);
		WebMenuAbstract selectedMenu = menuBar.getMenu("SelectedMenu");

		ArrayList removeList = new ArrayList();

	}

	// MAAM 04/10/2016
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		String azione = getAzione(se);
		Trace.println("======================AZIONE=" + azione);
		if (azione.equals(MATRICE_ADD)) {
			apriGestioneTagliaColore(se);
		} else if (azione.equals(MATRICE_MOD)) {
			apriGestioneTagliaColoreModifica(se);
			// FIX 82136 FIRAS
		} else if (azione.equals(MATRICE_DISP_MAG)) {
			apriGestioneTagliaColoreDisMag(se);
			// FIX 82136 FINE
		} else if (azione.equals(EVADI_MAG)) {
			apriEvasioneMagPartenzaDestinazione(se);
		} else if (azione.equals(MONT_KIT)) {
			apriGestioneMontaggioKit(se, "M");
		} else if (azione.equals(SMONT_KIT)) {
			apriGestioneSmontaggioKit(se, "S");
		} else if (azione.equals(ONLY_SMONT_KIT)) {
			apriGestioneOnlySmontaggioKit(se);
		} else if (azione.equals(VIEW_DOC_MONT_SMONT)) {
			apriVistaDocGeneratiMontSmont(se);
		} else if (azione.equals(CAMBIO_ARTICOLO_ORD_VEN)) {

			String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);

			if (objectKeys.length > 1) {

				ServletOutputStream out = se.getResponse().getOutputStream();

				out.println("<script language='JavaScript1.2'>");
				out.println(
						"parent.alert(\"Non è possibile modificare l'articolo su piu righe nell'ordine vendita, selezionare una sola riga\");");
				out.println("</script>");
			} else {

				String key = objectKeys[0];
				VtOrdineVenditaRigaPrm oldRiga = null;
				try {
					oldRiga = (VtOrdineVenditaRigaPrm) VtOrdineVenditaRigaPrm
							.elementWithKey(VtOrdineVenditaRigaPrm.class, key, PersistentObject.NO_LOCK);

				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

				if (oldRiga.getStatoEvasione() != '0') {

					ServletOutputStream out = se.getResponse().getOutputStream();
					out.println("<script language='JavaScript1.2'>");
					out.println(
							"parent.alert(\"Non è possibile modificare l'articolo su una riga già evasa, impossibile continuare\");");
					out.println("</script>");

				} else
					cambioArticoloOrdVen(se, objectKeys[0]);
			}
		} else if (azione.equals(CARTELLA_CLIENTE)) {
			String url = "/it/thera/thip/tessile/vendite/ordineVE/VtCartellaCliente.jsp" + "?Mode=NEW&Key="
					+ URLEncoder.encode(docKey) + "&InitialActionAdapter=" + getClass().getName();
			se.sendRequest(getServletContext(), url, false);
		} else
		// 80918 - Inizio
		if (azione.equals("APRI_RILASCIO_TESSILE")) {
			String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");

			if (chiaveOrdine != null) {

				// Fix 81032 - Inizio

				boolean statoRilOk = controllaStatoRilascio(chiaveOrdine);

				if (statoRilOk) {
					// Fix 81032 - Fine

					// Fix 80993 - Inizio
					boolean pianifOk = VtOrdVenRigApriRilascioTessile.controllaPianifArticolo(chiaveOrdine);

					if (pianifOk) {
						// Fix 80993 - Fine

						String[] chiaviEstr = VtOrdVenRigApriRilascioTessile.creaRigheEstrazione(chiaveOrdine);

						VtRilascioDaOrdVenGridActionAdapter ad = new VtRilascioDaOrdVenGridActionAdapter();

						String url = ad.apriRilascioTessile(se, chiaviEstr);

						if (url != null && !url.equals("")) {

							if (!url.equals(
									"it/thera/thip/tessile/produzione/rilDaOrdVen/servlet/VtRilascioDaOrdVenGridError.jsp")) {

								Integer settingKey = null;
								VtConfParamTessili conf = VtConfParamTessili.getVtConfParamTessili();
								if (conf != null)
									settingKey = conf.getSettingDfRilDispo();

								if (settingKey != null) {
									url += "&SettingKey=" + settingKey;
									se.sendRequest(getServletContext(), url, true);
								} else {
									ServletOutputStream out = se.getResponse().getOutputStream();
									out.println("<script language='JavaScript1.2'>");
									out.println(
											"parent.alert(\"Inserire Vista di default sul parametro di personalizzazione VtSettingRilOrdVen\");");
									out.println("parent.window.close();");
									out.println("</script>");
								}
							} else {
								VtRilascioDaOrdVenTessConferma rilTess = new VtRilascioDaOrdVenTessConferma();
								if (VtRilascioDaOrdVenGridActionAdapter.Error != null
										&& VtRilascioDaOrdVenGridActionAdapter.ErrorVal != null) {
									String er = rilTess.getErrors(VtRilascioDaOrdVenGridActionAdapter.Error,
											VtRilascioDaOrdVenGridActionAdapter.ErrorVal);
									if (er != null && !er.equals("")) {
										ServletOutputStream out = se.getResponse().getOutputStream();
										out.println("<script language='JavaScript1.2'>");
										out.println("parent.alert('" + er + "');");
										out.println("parent.window.close();");
										out.println("</script>");
									}
								}
							}
						}
						// Fix 80993 - Inizio
					} else {
						ServletOutputStream out = se.getResponse().getOutputStream();
						out.println("<script language='JavaScript1.2'>");
						out.println("parent.alert(\"Pianificatore sull'articolo dell'ordine mancante\");");
						out.println("parent.window.close();");
						out.println("</script>");
					}
					// Fix 80993 - Fine

					// Fix 81032 - Inizio
				} else {
					ServletOutputStream out = se.getResponse().getOutputStream();
					out.println("<script language='JavaScript1.2'>");
					out.println(
							"parent.alert(\"Lo stato rilascio ordine acquisto o lav. esterna è da non estrarre\");");
					out.println("parent.window.close();");
					out.println("</script>");
				}
				// Fix 81032 - Fine

			}

		} else {
			// 80973 - Inizio
			if (azione.equals(ESTRAZIONE_PSO)) {
				// String[] objectKeys = se.getRequest().getParameterValues("ObjectKey"); //fix
				// 80871
				String[] keys = KeyHelper.unpackObjectKey(docKey); // fix 80871
				String idAzienda = keys[0];
				String anno = keys[1];
				String numero = keys[2];
				// String riga =keys[3]; //fix 80871
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
				// //fix 80871 inizio
				// VtOrdineVenditaRigaPrm ordineVenditaRiga =null;
				// String descRiga="";
				// try {
				// ordineVenditaRiga = (VtOrdineVenditaRigaPrm) VtOrdineVenditaRigaPrm
				// .elementWithKey(
				// VtOrdineVenditaRigaPrm.class,
				// KeyHelper.buildObjectKey(new Object[] {
				// idAzienda, anno,
				// numero,riga }),
				// PersistentObject.NO_LOCK);
				// } catch (SQLException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// //fix 80871
				if (ordineVendita != null) {
					nDocFormat = ordineVendita.getNumeroDocumentoFormattato();
				}
				// fix 80871
				// if (ordineVenditaRiga!=null) {
				// descRiga = ordineVenditaRiga.getDescrizioneArticolo();
				// if (descRiga==null)
				// descRiga="";
				// }
				// String url =
				// "/it/thera/thip/tessile/produzione/rilDaOrdVen/VtRilascioDaOrdVenEstrBatch.jsp"+
				// "?IdAnnoOrdCli="
				// + anno+"&NumeroOrdCli=" + numero + "&RigaOrdCli=" + riga +
				// "&NumeroDocumentoFormattato=" + nDocFormat
				// + "&DescrizioneArticolo=" + descRiga;
				// fix 80871
				String url = "/it/thera/thip/tessile/produzione/rilDaOrdVen/VtRilascioDaOrdVenEstrBatch.jsp"
						+ "?IdAnnoOrdCli=" + anno + "&NumeroOrdCli=" + numero + "&NumeroDocumentoFormattato="
						+ nDocFormat;
				se.sendRequest(getServletContext(), url, false);
				// 80973 - Fine
			} else {
				if (azione.equals(ORDINE_ACQUISTO)) {
					String[] objectKeys = se.getRequest().getParameterValues("ObjectKey");
					// System.out.println(se.getRequest().getParameterValues("ObjectKey"));
					VtOrdineAcquistoRigaPrm ordAcq = trovaOrdAcq(objectKeys[0]);
					String[] sKey = KeyHelper.unpackObjectKey(objectKeys[0]); // sKey: 0= IdAzienda, 1= Anno, 2= Numero
																				// Ordine, 3= N. Riga
					if (ordAcq != null) {

						String url = "com.thera.thermfw.web.servlet.ShowGrid?ClassName=VtVistaOrdAcqRig&thGridType=list"
								+ "&thRestrictConditions=IdAzienda=" + sKey[0] + " ;Anno=" + sKey[1] + " ;Codice="
								+ sKey[2] + " ;Nriga=" + sKey[3] + ""
								+ "&thSpecificDOList=it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineAcquistoGridDoList";

						HttpServletRequest request = se.getRequest();
						String ss = request.getContextPath() + se.getServletPath() + url;
						se.getResponse().sendRedirect(ss);

					} else {
						ServletOutputStream out = se.getResponse().getOutputStream();
						out.println("<script language='JavaScript1.2'>");
						out.println("parent.alert(\"Ordine acquisto non presente!\");");
						out.println("parent.window.close();");
						out.println("</script>");
					}
				} else {
					if (azione.equals(ORDINE_ESECUTIVO)) {
						String[] objectKeys = se.getRequest().getParameterValues("ObjectKey");
						// System.out.println(se.getRequest().getParameterValues("ObjectKey"));
						VtOrdineEsecutivo ordEsec = trovaOrdEsec(objectKeys[0]);
						String[] sKey = KeyHelper.unpackObjectKey(objectKeys[0]);// sKey: 0= IdAzienda, 1= Anno, 2=
																					// Numero Ordine, 3= N. Riga
						if (ordEsec != null) {

							String url = "com.thera.thermfw.web.servlet.ShowGrid?ClassName=VtOrdineEsecutivo&thGridType=list"
									+ "&thRestrictConditions=IdAzienda=" + sKey[0] + " ;Anno=" + sKey[1] + " ;Codice="
									+ sKey[2] + " ;Nriga=" + sKey[3] + ""
									+ "&thSpecificDOList=it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineEsecutivoGridDoList";

							HttpServletRequest request = se.getRequest();
							String ss = request.getContextPath() + se.getServletPath() + url;
							se.getResponse().sendRedirect(ss);

						} else {
							ServletOutputStream out = se.getResponse().getOutputStream();
							out.println("<script language='JavaScript1.2'>");
							out.println("parent.alert(\"Ordine esecutivo non presente!\");");
							out.println("parent.window.close();");
							out.println("</script>");
						}
					}

					// crea dispo nuova
					else if (azione.equals(CREA_DISPO)) {
						String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
						if (objectKeys != null) {

							boolean checkLav = checkLavorazioni(objectKeys[0]);
							boolean checkComp = checkCompatibilitaRighe(objectKeys);
							boolean checkParamPers = checkParametriPers();

							if (checkLav && checkComp && checkParamPers) {

								boolean checkRilOrdEcse = checkStatoRilascioOrdEsec(objectKeys);

								if (checkRilOrdEcse) {
									BigDecimal qtaDaProd = calcolaQtaDaProdurreTot(objectKeys);
									// posso creare l'ordine escutivo
									creaOrdineEsecutivo(objectKeys, qtaDaProd, se);
								} else {
									ServletOutputStream out = se.getResponse().getOutputStream();
									out.println(" <script language='JavaScript1.2'>");
									// out.println("parent.alert(\"Lo stato rilascio ordine acquisto o lav. esterna
									// è da non estrarre\");");
									out.println(" parent.alert(\"Ordine esecutivo già rilasciato! \");");
									out.println(" parent.window.close();");
									out.println(" </script>");

								}
							} else {
								// MESSAGGIO ERRORE MANCANZA DATI
								String errore = "Impossibile creare Ordine produzione per i seguenti alert: \\n";
								if (!checkParamPers)
									errore = errore
											+ "- Non sono stati valorizzati i parametri di personalizzazione \\'pers.ParametriPerContoLavoro\\'\\n";
								if (!checkLav)
									errore = errore
											+ "- Non sono state dichiarate le attività nel tab \\'Lavorazioni\\'\\n";
								if (!checkComp)
									errore = errore + " - Non c'è compatibilità tra le righe selezionate ";

								ServletOutputStream out = se.getResponse().getOutputStream();
								out.println(" <script language='JavaScript1.2'>");
								out.println(" alert('" + errore + "');");
								out.println(" parent.window.close();");
								out.println(" </script>");
							}

						}
					}

					/*
					 * crea dispo vecchia else if(azione.equals(CREA_DISPO)) { String[] objectKeys =
					 * se.getRequest().getParameterValues(OBJECT_KEY); if(objectKeys != null){
					 * String keyOrd = objectKeys[0]; try {
					 * 
					 * creaDispo(keyOrd,se); ServletOutputStream out =
					 * se.getResponse().getOutputStream();
					 * out.println("<script language='JavaScript1.2'>");
					 * out.println("alert('"+"dai"+"');"); out.println("parent.window.close();");
					 * out.println("</script>");
					 * 
					 * } catch (NoSuchElementException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); } catch (NoSuchFieldException e) { // TODO
					 * Auto-generated catch block e.printStackTrace(); } catch
					 * (ClassNotFoundException e) { // TODO Auto-generated catch block
					 * e.printStackTrace(); } catch (SQLException e) { // TODO Auto-generated catch
					 * block e.printStackTrace(); } } } crea dispo vecchia
					 */

					else {
						if (azione.equals(LIST_PSO)) {
							listPSO(se);
						} else // 81903
						if (azione.equals(PASSA_A_PATRON)) {
							try {

								passaAPatron(cadc, se, datiSessione, azione);

							} catch (Exception ex) {
								ex.printStackTrace(Trace.excStream);
							}
						}

						else

							super.otherActions(cadc, se);
					}
				}
			}
		}
	}

	protected void cambioArticoloOrdVen(ServletEnvironment se, String elencoChiavi)
			throws ServletException, IOException {
		try {
			se.sendRequest(getServletContext(),
					"it/thera/thip/tessile/vendite/ordineVE/VtCambioArticoloOrdVen.jsp?thAction="
							+ CAMBIO_ARTICOLO_ORD_VEN + "&thDescrName=VtCambioArticoloOrdVen&ElencoChiavi="
							+ URLEncoder.encode(elencoChiavi),
					false);
		} catch (Exception ex) {
			ex.printStackTrace(Trace.excStream);
			throw new RuntimeException(ex);
		}
	}

	private void listPSO(ServletEnvironment se) {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		String[] keys = KeyHelper.unpackObjectKey(docKey); // fix 80871
		String idAzienda = keys[0];
		String anno = keys[1];
		String numero = keys[2];
		String url = "/it/thera/thip/tessile/vendite/ordineVE/VtListPSO.jsp" + "?IdAnnoOrdCli=" + anno
				+ "&NumeroOrdCli=" + numero;
		// + "?IdAnnoOrdCli=" + anno+"&NumeroOrdCli=" + numero +
		// "&NumeroDocumentoFormattato=" + nDocFormat;
		try {
			se.sendRequest(getServletContext(), url, false);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void creaDispo(String keyODV, ServletEnvironment se) throws NoSuchElementException, NoSuchFieldException,
			ClassNotFoundException, SQLException, ServletException, IOException {
		if (keyODV != null) {
			VtOrdineVenditaRigaPrm ordVen = null;
			try {
				ordVen = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, keyODV,
						VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean okParametri = false;
			String idStabilimento = "";
			String idSerie = "";
			/*
			 * RECUPERO INIZIALMENTE I DATI DEI PARAMETRI RIFERITI ALLO STABILIMENTO E ALLA
			 * SERIE DELL'ORDINE PRODUZIONE CHE SONO INDISPENSABILI PER CREARE L'ORDINE DI
			 * PRODUZIONE
			 */
			ParametroPsn paramStabil = ParametroPsn.getParametroPsn("pers.ParametriPerContoLavoro", "IdStabilimento");
			ParametroPsn paramSerie = ParametroPsn.getParametroPsn("pers.ParametriPerContoLavoro",
					"IdSerieOrdEsecutivo");
			if (paramStabil != null && paramSerie != null && paramStabil.getValore() != null
					&& paramSerie.getValore() != null) {
				idStabilimento = paramStabil.getValore();
				idSerie = paramSerie.getValore();
				okParametri = true;
			}

			// Controllo che siano stati creati i componenti
			List componenti = new ArrayList();
			if (ordVen != null) {
				componenti = getListaRiservaComponenti(ordVen);
			}

			// CREO LA TESTATA DELL'ORDINE ESECUTIVO
			if (ordVen != null && okParametri && ordVen.getVtOrdVenRigAtv() != null
					&& !ordVen.getVtOrdVenRigAtv().isEmpty() && componenti != null && !componenti.isEmpty()) {

				// Magazzino magazzinoPrl = getMagazzinoPrl(getListaRiservaComponenti(ordVen));
				// String idMagazzinoPrl = getMagazzinoPrl(componenti); //Fix 81120

				// Fix 81120
				// recupero il magazzino di versamento dalle attività indicate sull'ordine di
				// vendita riga
				String idMagPrelDaAttivita = null;
				String idMagVersDaAttivita = null;
				// Prelievo
				VtOrdVenRigAtv atvX = (VtOrdVenRigAtv) ordVen.getVtOrdVenRigAtv().get(0);
				if (atvX != null && atvX.getAttivita() != null && atvX.getAttivita().getRMagPrelievo() != null)
					idMagPrelDaAttivita = atvX.getAttivita().getRMagPrelievo();
				// Versamento
				atvX = (VtOrdVenRigAtv) ordVen.getVtOrdVenRigAtv().get(ordVen.getVtOrdVenRigAtv().size() - 1);
				if (atvX != null && atvX.getAttivita() != null && atvX.getAttivita().getRMagVersamento() != null)
					idMagVersDaAttivita = atvX.getAttivita().getRMagVersamento();

				// Fix 81120 Fine

				VtOrdineEsecutivo nuovoOrdEsec = (VtOrdineEsecutivo) Factory.createObject(VtOrdineEsecutivo.class);

				nuovoOrdEsec.setIdAzienda(ordVen.getIdAzienda());
				nuovoOrdEsec.setWithModelloProduttivo("false");
				nuovoOrdEsec.getNumeratoreHandler().setIdSerie(idSerie);

				if (ordVen.getArticolo() != null) {
					nuovoOrdEsec.getDescrizione()
							.setDescrizione(ordVen.getArticolo().getDescrizioneArticoloNLS().getDescrizione());
					nuovoOrdEsec.getDescrizione().setDescrizioneRidotta(
							ordVen.getArticolo().getDescrizioneArticoloNLS().getDescrizioneRidotta());

					nuovoOrdEsec.setUMPrmMag(ordVen.getArticolo().getUMPrmMag());
					nuovoOrdEsec.setUMSecMag(ordVen.getArticolo().getUMSecMag());

					// nuovoOrdEsec.setIdArticolo(ordVen.getArticolo().getIdArticolo());
					nuovoOrdEsec.setArticolo(ordVen.getArticolo());
					// nuovoOrdEsec.setArticoloKey(ordVen.getArticolo().getKey());

					// Fix 81533 - Inizio
					String[] keyArt = { ordVen.getIdAzienda(), ordVen.getIdArticolo() };
					String sKeyArt = KeyHelper.buildObjectKey(keyArt);
					VtArticoliTex artTex = (VtArticoliTex) VtArticoliTex.elementWithKey(VtArticoliTex.class, sKeyArt,
							PersistentObject.NO_LOCK);
					if (artTex != null)
						nuovoOrdEsec.setTipoColorante(artTex.getTipoColorante());
					// Fix 81533 - Fine

					nuovoOrdEsec.setIdVersione(new Integer(1));

				}

				// nuovoOrdEsec.setMagazzinoVrs(ordVen.getMagazzino()); //Fix 81120
				// nuovoOrdEsec.setMagazzinoPrl(magazzinoPrl);
				// nuovoOrdEsec.setIdMagazzinoPrl(idMagazzinoPrl); //Fix 81120
				nuovoOrdEsec.setIdMagazzinoVrs(idMagVersDaAttivita);
				nuovoOrdEsec.setIdMagazzinoPrl(idMagPrelDaAttivita);

				nuovoOrdEsec.setIdStabilimento(idStabilimento);
				nuovoOrdEsec.setQtaOrdinataUMPrm(ordVen.getQtaInUMRif());

				nuovoOrdEsec.setStatoOrdine('0'); // 0: Immesso
				nuovoOrdEsec.setSaldoManuale(false);
				nuovoOrdEsec.setStatoSchedulazione('0');
				nuovoOrdEsec.setIdCliente(ordVen.getIdCliente());
				nuovoOrdEsec.setMetodoResiduoOrdine('0');

				nuovoOrdEsec.setAnnoOrdineCliente(ordVen.getAnnoDocumento());
				nuovoOrdEsec.setNumeroOrdineCliente(ordVen.getNumeroDocumento());
				nuovoOrdEsec.setRigaOrdineCliente(ordVen.getNumeroRigaDocumento());
				nuovoOrdEsec.setDettaglioRigaOrdine(ordVen.getDettaglioRigaDocumento());

				nuovoOrdEsec.getDateRichieste().setStartDate(ordVen.getDataConsegnaConfermata());
				nuovoOrdEsec.getDateRichieste().setEndDate(ordVen.getDataConsegnaConfermata());

				// boolean salvato = VtTessileUtil.saveVtOrdineEsecutivo(nuovoOrdEsec);
				ConnectionManager.pushConnection();
				int rc = nuovoOrdEsec.save();
				if (rc >= 0)
					ConnectionManager.commit();
				else
					ConnectionManager.rollback();
				ConnectionManager.pushConnection();

				boolean salvato = false;
				if (rc >= 0)
					salvato = true;

				if (salvato) {
					// Rileggo testata appena creata
					String testataKey = nuovoOrdEsec.getKey();
					VtOrdineEsecutivo testata = null;
					try {
						testata = (VtOrdineEsecutivo) VtOrdineEsecutivo.elementWithKey(testataKey,
								PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					if (testata != null) {
						List atvNuovoOrdEsec = creaListaAttivitaOrdineEsecutivo(ordVen, testata);
						if (!atvNuovoOrdEsec.isEmpty()) {
							List attivitaEsec = testata.getAttivitaEsecutive();
							attivitaEsec.addAll(atvNuovoOrdEsec);
						}

						// salvato = VtTessileUtil.saveVtOrdineEsecutivo(testata);
						// rc = nuovoOrdEsec.save();
						ConnectionManager.pushConnection();
						rc = testata.save();
						if (rc >= 0)
							ConnectionManager.commit();
						else
							ConnectionManager.rollback();
						ConnectionManager.pushConnection();
						salvato = false;
						if (rc >= 0)
							salvato = true;

						if (salvato) {
							boolean creato = creaVtAttivitaEsecProdottoProduzione(ordVen, testata, idMagVersDaAttivita);
						}

						// Fix 81141 - Inizio

						// List righeSec = ordVen.getRigheSecondarie();
						List righeComponenti = componenti;
						if (righeComponenti != null && !righeComponenti.isEmpty()) {
							List atvMat = creaVtAttivitaEsecMaterialeProduzione(righeComponenti, testata,
									idMagPrelDaAttivita);

							// salvaListaVtAttivitaEsecMateriali(atvMat); //Fix 81216
						}

						// Fix 81120 - Per ogni attività devo andare a creare anche un sotto prodotto di
						// versamento e un prodotto di prelievo
						// Che hanno come articolo intestatario quello del componente
						if (testata != null && testata.getAttivitaEsecutive() != null) {
							AttivitaEsecMateriale atvMat = null;
							testata.retrieve();
							Iterator iterAttivita = testata.getAttivitaEsecutive().iterator();
							int y = 1;
							while (iterAttivita.hasNext()) {
								AttivitaEsecutiva attivita = (AttivitaEsecutiva) iterAttivita.next();
								if (y == 1 && attivita.getMateriali() != null && !attivita.getMateriali().isEmpty()) {
									atvMat = (AttivitaEsecMateriale) attivita.getMateriali().get(0);

									// Creo prodotto per il versamento con lo stesso id articolo dell'attività
									// materiale
									VtAttivita atv = (VtAttivita) attivita.getAttivita();
									// VtAttivitaEsecProdotto prod =
									// creaProdottoPerVersamento(testata,atvMat.getArticolo(),atv.getRMagVersamento(),ordVen,atvMat);
									// //Fix 81264
									VtAttivitaEsecProdotto prod = creaProdottoPerVersamento(testata,
											atvMat.getArticolo(), atv.getRMagVersamento(), ordVen,
											attivita.getMateriali());
									if (prod != null)
										attivita.getProdotti().add(prod);
								} else if (y > 1 && atvMat != null) {
									VtAttivita atv = (VtAttivita) attivita.getAttivita();
									VtAttivitaEsecMateriale atvMatNew = null;
									if (!iterAttivita.hasNext()) {
										atvMatNew = generaMaterialePerPrelievo(testata, attivita.getIdRigaAttivita(),
												atvMat.getIdArticolo(), atv.getRMagPrelievo(), new Integer("1"),
												atvMat);
										if (atvMatNew != null) {
											attivita.getMateriali().add(atvMatNew);
										}
									} else {
										atvMatNew = generaMaterialePerPrelievo(testata, attivita.getIdRigaAttivita(),
												atvMat.getIdArticolo(), atv.getRMagPrelievo(), new Integer("1"),
												atvMat);
										if (atvMatNew != null) {
											attivita.getMateriali().add(atvMatNew);
											// VtAttivitaEsecProdotto prod =
											// creaProdottoPerVersamento(testata,atvMat.getArticolo(),atv.getRMagVersamento(),ordVen,atvMat);
											// //Fix 81264
											VtAttivitaEsecProdotto prod = creaProdottoPerVersamento(testata,
													atvMat.getArticolo(), atv.getRMagVersamento(), ordVen,
													attivita.getMateriali());
											if (prod != null)
												attivita.getProdotti().add(prod);
										}
									}
								}
								y = y + 1;
								ConnectionManager.pushConnection();
								int rcX = attivita.save();
								if (rcX >= 0)
									ConnectionManager.commit();
								else
									ConnectionManager.rollback();
								ConnectionManager.popConnection();
							}
						}

						if (testata != null) {
							rc = 0;

							try {
								testata = (VtOrdineEsecutivo) VtOrdineEsecutivo.elementWithKey(testataKey,
										PersistentObject.NO_LOCK);
								setAttivitaPrecedenti(testata);
							} catch (Exception e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

							try {
								List atv = testata.getAttivitaEsecutive();
								Iterator iter = atv.iterator();
								int i = 1;
								AttivitaEsecutiva atvPrec = null;
								while (iter.hasNext()) {
									AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) iter.next();
									// ConnectionManager.pushConnection();
									if (i == 1 && iter.hasNext()) {
										updateFlagAttivitaFinale(atvEsec.getKey());

										// Fix 81471 - Inizio
										// atvEsec.setPoliticaConsAttivita('1');

									} else if (!iter.hasNext()) {
										// atvEsec.setAttivitaIniziale(false);
										// atvEsec.setAttivitaFinale(true);
										// atvEsec.getAttivitaPrecedenti().add(atvPrec);
										// atvEsec.getAttivitaPrecedentiAzElements().remove(atvPrec);
									} else {
										updateFlagAttivitaFinale(atvEsec.getKey());
									}
									/*
									 * rc = atvEsec.save(); if(rc >= 0) ConnectionManager.commit(); else
									 * ConnectionManager.rollback(); ConnectionManager.popConnection();
									 */
									i++;
									atvPrec = atvEsec;
								}
								// setAttivitaPrecedenti(testata.getKey());
								testata = (VtOrdineEsecutivo) VtOrdineEsecutivo.elementWithKey(testataKey,
										PersistentObject.NO_LOCK);
								testata.setStatoOrdine('1');
								testata.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
								// rc = testata.save();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

							try {
								ConnectionManager.pushConnection();
								rc = testata.save();
								if (rc >= 0) {
									ConnectionManager.commit();
									// String url = "/" + "it/thera/thip/produzione/ordese/OrdineEsecutivo.jsp" +
									// "?Mode=UPDATE&Key=" + testata.getKey() +
									// "&InitialActionAdapter=it.thera.thip.tessile.produzione.ordese.web.VtOrdineEsecutivoGridActionAdapter";
									// se.sendRequest(getServletContext(), url, false);
								} else
									ConnectionManager.rollback();
								ConnectionManager.popConnection();

								if (salvato) {
									String url = "/" + "it/thera/thip/produzione/ordese/OrdineEsecutivo.jsp"
											+ "?Mode=UPDATE&Key=" + testata.getKey()
											+ "&InitialActionAdapter=it.thera.thip.tessile.produzione.ordese.web.VtOrdineEsecutivoGridActionAdapter";
									se.sendRequest(getServletContext(), url, false);
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ServletException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

			} else {
				// MESSAGGIO ERRORE MANCANZA DATI
				String errore = "Impossibile creare Ordine produzione per i seguenti alert: \\n";
				if (!okParametri)
					errore = errore
							+ "- Non sono stati valorizzati i parametri di personalizzazione \\'pers.ParametriPerContoLavoro\\'\\n";
				if (ordVen.getVtOrdVenRigAtv() == null || ordVen.getVtOrdVenRigAtv().isEmpty())
					errore = errore + "- Non sono state dichiarate le attività nel tab \\'Lavorazioni\\'\\n";
				if (componenti == null || componenti.isEmpty())
					errore = errore + "- Non sono presenti i componenti\\n";
				ServletOutputStream out = se.getResponse().getOutputStream();
				out.println("<script language='JavaScript1.2'>");
				out.println("alert('" + errore + "');");
				out.println("parent.window.close();");
				out.println("</script>");
			}
		}
	}

	/*
	 * public Magazzino getMagazzinoPrl(List righeSec){ if(righeSec != null){
	 * Iterator iter = righeSec.iterator(); while(iter.hasNext()){
	 * VtOrdineVenditaRigaSec rigaSec = (VtOrdineVenditaRigaSec)iter.next(); return
	 * rigaSec.getMagazzino(); } } return null; }
	 */

	public static synchronized void updateStatoOrigine(String key) {

		boolean success = false;
		String UPDATE_STATO_ORIGINE = "UPDATE " + AttivitaEsecutivaTM.TABLE_NAME + " SET "
				+ AttivitaEsecutivaTM.STATO_ORIG + "= 'V' " + // 07-05-2018

				" WHERE " + AttivitaEsecutivaTM.ID_AZIENDA + " = '" + KeyHelper.getTokenObjectKey(key, 1) + "'"
				+ " AND " + AttivitaEsecutivaTM.ID_ANNO_ORD + " = '" + KeyHelper.getTokenObjectKey(key, 2) + "'"
				+ " AND " + AttivitaEsecutivaTM.ID_NUMERO_ORD + " = '" + KeyHelper.getTokenObjectKey(key, 3) + "'";
		// " AND " + AttivitaEsecutivaTM.ID_RIGA_ATTIVITA + " = " +
		// KeyHelper.getTokenObjectKey(key, 4) + "";

		CachedStatement cStatemant = new CachedStatement(UPDATE_STATO_ORIGINE);

		try {
			PreparedStatement ps = cStatemant.getStatement();
			int rs = ps.executeUpdate();
			// ConnectionManager.pushConnection();
			if (rs >= 0) {
				ConnectionManager.commit();
				success = true;
			} else {
				ConnectionManager.rollback();
				success = false;
			}
			// ConnectionManager.popConnection();
			ps.close();
			cStatemant.free();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * updateFlagAttivitaFinale(String key)
	 * 
	 * @param key String
	 */
	public static synchronized void updateFlagAttivitaFinale(String key) {
		/*
		 * try { Database db = ConnectionManager.getCurrentDatabase(); PreparedStatement
		 * ps = cUpdateFlagAtvFinale.getStatement();
		 * 
		 * db.setString(ps, 1, KeyHelper.getTokenObjectKey(key, 1)); db.setString(ps, 2,
		 * KeyHelper.getTokenObjectKey(key, 2)); db.setString(ps, 3,
		 * KeyHelper.getTokenObjectKey(key, 3)); db.setString(ps, 4,
		 * KeyHelper.getTokenObjectKey(key, 4)); //Adjusted by Rachida, executeUpdate()
		 * is more correct than executeQuery(). cUpdateFlagAtvFinale.executeUpdate(); }
		 * catch (Exception ex) { ex.printStackTrace(); }
		 */

		boolean success = false;
		String UPDATE_FLAG_ATV_FINALE = "UPDATE " + AttivitaEsecutivaTM.TABLE_NAME + " SET "
				+ AttivitaEsecutivaTM.ATV_FINALE + " = 'N' " + " WHERE " + AttivitaEsecutivaTM.ID_AZIENDA + " = '"
				+ KeyHelper.getTokenObjectKey(key, 1) + "'" + " AND " + AttivitaEsecutivaTM.ID_ANNO_ORD + " = '"
				+ KeyHelper.getTokenObjectKey(key, 2) + "'" + " AND " + AttivitaEsecutivaTM.ID_NUMERO_ORD + " = '"
				+ KeyHelper.getTokenObjectKey(key, 3) + "'" + " AND " + AttivitaEsecutivaTM.ID_RIGA_ATTIVITA + " = "
				+ KeyHelper.getTokenObjectKey(key, 4) + "";

		CachedStatement cStatemant = new CachedStatement(UPDATE_FLAG_ATV_FINALE);

		try {
			PreparedStatement ps = cStatemant.getStatement();
			int rs = ps.executeUpdate();
			// ConnectionManager.pushConnection();
			if (rs >= 0) {
				ConnectionManager.commit();
				success = true;
			} else {
				ConnectionManager.rollback();
				success = false;
			}
			// ConnectionManager.popConnection();
			ps.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void setAttivitaPrecedenti(VtOrdineEsecutivo ordesec1) throws Exception {

		/*
		 * Una volta terminati tutti i passaggi, devo controllare che tutte le attività
		 * abbiano ancora valorizzato l'attributo inerente all'attività precedente
		 */
		// OrdineEsecutivo ordesec1 =
		// (OrdineEsecutivo)PersistentObject.elementWithKey(OrdineEsecutivo.class,
		// keyOrd, OrdineEsecutivo.NO_LOCK);
		List attivitaGiaPresenti1 = ordesec1.getAttivitaEsecutive();
		Iterator atvEsecIter = attivitaGiaPresenti1.iterator();
		int i = 1;
		AttivitaEsecutiva atvPrec = new AttivitaEsecutiva();
		while (atvEsecIter.hasNext()) {
			AttivitaEsecutiva atv = (AttivitaEsecutiva) atvEsecIter.next();
			List listAtvPrec = atv.getAttivitaPrecedenti();
			List listAtvPrecAz = atv.getAttivitaPrecedentiAzElements();
			if (i != 1) {
				// deleteAttivitaPrecedente(atv.getIdAzienda(),atv.getIdAnnoOrdine(),
				// atv.getIdNumeroOrdine(), atv.getIdRigaAttivita());
				insertAttivitaPrecedente(atv.getIdAzienda(), atv.getIdAnnoOrdine(), atv.getIdNumeroOrdine(),
						atv.getIdRigaAttivita(), atvPrec.getIdRigaAttivita());

				/*
				 * ConnectionManager.pushConnection(); /*listAtvPrec.add(atvPrec);
				 * listAtvPrecAz.remove(atvPrec);
				 */
				/*
				 * int rs = ordesec1.save(true); if(rs >= 0){ ConnectionManager.commit(); }
				 * else{ ConnectionManager.rollback(); } ConnectionManager.popConnection();
				 */

			}
			atvPrec = atv;
			i++;
		}

	}

	public static boolean insertAttivitaPrecedente(String idAzienda, String idAnnoOrd, String idNumeroOrd,
			Integer integer, Integer integer2) {

		boolean success = false;
		String insert = "INSERT INTO THIP.ORDESE_ATV_PCD (ID_AZIENDA, ID_ANNO_ORD ,ID_NUMERO_ORD ,ID_RIGA_ATTIVITA ,ID_RIGA_ATTIV_PCD, SEQUENZA) "
				+ " VALUES( '" + idAzienda + "', '" + idAnnoOrd + "', '" + idNumeroOrd + "', '" + integer + "', '"
				+ integer2 + "', '1')";

		CachedStatement cStatemant = new CachedStatement(insert);

		try {
			PreparedStatement ps = cStatemant.getStatement();
			int rs = ps.executeUpdate();
			// ConnectionManager.pushConnection();
			if (rs >= 0) {
				ConnectionManager.commit();
				success = true;
			} else {
				ConnectionManager.rollback();
				success = false;
			}
			// ConnectionManager.popConnection();
			ps.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return success;

	}

	public String getMagazzinoPrl(List righeComponenti) {
		if (righeComponenti != null) {
			Iterator iter = righeComponenti.iterator();
			while (iter.hasNext()) {
				VtOrdVenRigPrmMat componente = (VtOrdVenRigPrmMat) iter.next();
				String idMag = KeyHelper.getTokenObjectKey(componente.getKey(), 8);
				return idMag;
				// return componente.getMagazzino();
			}
		}
		return null;
	}

	public static boolean salvaListaVtAttivitaEsecMateriali(List atvEsecMat) throws SQLException {

		boolean aggiornato = false;

		if (!atvEsecMat.isEmpty()) {
			Iterator iteAtvMat = atvEsecMat.iterator();

			while (iteAtvMat.hasNext()) {
				VtAttivitaEsecMateriale atvMat = (VtAttivitaEsecMateriale) iteAtvMat.next();

				if (atvMat != null) {

					String atvMatKey = atvMat.getKey();

					VtAttivitaEsecMateriale atvMatDaSalvare = null;
					try {
						atvMatDaSalvare = (VtAttivitaEsecMateriale) VtAttivitaEsecMateriale
								.elementWithKey(VtAttivitaEsecMateriale.class, atvMatKey, PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						e.printStackTrace();
					}

					if (atvMatDaSalvare != null) {
						// aggiornato =
						// VtTessileUtil.saveVtAttivitaEsecMaterialeConDummy(atvMatDaSalvare);
						ConnectionManager.pushConnection();
						int rc = atvMatDaSalvare.save();
						if (rc >= 0)
							ConnectionManager.commit();
						else
							ConnectionManager.rollback();
						ConnectionManager.pushConnection();
						if (rc >= 0)
							aggiornato = true;
					}
				}
			}

		}
		return aggiornato;
	}

	public static List creaVtAttivitaEsecMaterialeProduzione(List righeComponenti, VtOrdineEsecutivo ordEsec,
			String idMagPrelievo) throws NoSuchElementException, NoSuchFieldException, ClassNotFoundException,
			SQLException, ServletException, IOException {
		// VtOrdVenRigPrmMatTM
		List atvMatCreate = new ArrayList();
		if (!righeComponenti.isEmpty()) {

			List comp2 = new ArrayList(); // Fix 81264 Creo una seconda lista dei componenti in modo da poter ciclare
			// tutti quelli
			comp2.addAll(righeComponenti); // Con lo stesso idArticolo per creare un'unica attività materiale

			Iterator ite = righeComponenti.iterator();
			int i = 1;
			Integer idRigaAttivita = new Integer(1);

			if (ordEsec.getAttivitaEsecutive() != null && !ordEsec.getAttivitaEsecutive().isEmpty()) { // Fix 81216
				AttivitaEsecutiva attivita = (AttivitaEsecutiva) ordEsec.getAttivitaEsecutive().get(0);

				List idArticControllati = new ArrayList();

				while (ite.hasNext()) {
					// VtOrdineVenditaRigaSec ordRigaSec = (VtOrdineVenditaRigaSec)ite.next();
					VtOrdVenRigPrmMat componente = (VtOrdVenRigPrmMat) ite.next();
					// Creo Attività Materiale
					String idMag = KeyHelper.getTokenObjectKey(componente.getKey(), 8);

					String idArticolo = componente.getIdArticolo();

					if (idArticControllati.isEmpty() || !idArticControllati.contains(idArticolo)) {
						idArticControllati.add(idArticolo);
						// Fix 81264 Ciclo adesso tutti i componenti per creare un'attività per
						// idArticolo
						BigDecimal qtaImpegnata = new BigDecimal("0");
						Iterator iterComp2 = comp2.iterator();
						while (iterComp2.hasNext()) {
							VtOrdVenRigPrmMat compTmp = (VtOrdVenRigPrmMat) iterComp2.next();
							if (compTmp.getIdArticolo().equals(idArticolo)) {
								qtaImpegnata = qtaImpegnata.add(compTmp.getQtaImpegnata());
							}
						}

						// Fix 81347 - Inizio
						attivita.setPoliticaConsAttivita('1'); // manuale
						// Fix 81347 - Fine

						// VtAttivitaEsecMateriale atvMat = generaVtAttivitaEsecMateriale(ordEsec,
						// idRigaAttivita, componente.getIdArticolo(), idMag, new Integer(i),
						// componente.getQtaImpegnata()); //Fix 81120
						VtAttivitaEsecMateriale atvMat = generaVtAttivitaEsecMateriale(ordEsec, idRigaAttivita,
								componente.getIdArticolo(), idMagPrelievo, new Integer(i), qtaImpegnata); // Fix 81264

						// 04-05-2018
						String idLotto = "";

						// if(ordRigaSec.getRigheLotto() != null &&
						// !ordRigaSec.getRigheLotto().isEmpty()){
						iterComp2 = comp2.iterator();

						// 01-08-2018 prova non serve
						// int cont=0;

						while (iterComp2.hasNext()) {
							VtOrdVenRigPrmMat compTmp = (VtOrdVenRigPrmMat) iterComp2.next();

							// 01-08-2018
							/*
							 * prova non serve if (cont==0 && compTmp.getIdLotto() != null) idLotto=
							 * compTmp.getIdLotto(); prova non serve
							 */

							if (compTmp.getIdArticolo().equals(idArticolo)) {
								// 04-05-2018 if(compTmp.getIdLotto() != null){
								if (compTmp.getIdLotto() != null && compTmp.getIdLotto().equals(idLotto)) {
									/*
									 * List righeLotto = ordRigaSec.getRigheLotto(); Iterator righeLottoIter =
									 * righeLotto.iterator(); while(righeLottoIter.hasNext()){
									 * OrdineVenditaRigaLottoSec lottoRigaSec =
									 * (OrdineVenditaRigaLottoSec)righeLottoIter.next();
									 */

									idLotto = compTmp.getIdLotto();

									AttivitaEsecLottiMat lotto = (AttivitaEsecLottiMat) Factory
											.createObject(AttivitaEsecLottiMat.class);

									lotto.setFather(atvMat);
									lotto.setIdArticolo(compTmp.getIdArticolo());
									lotto.setIdLotto(compTmp.getIdLotto());

									lotto.setQtaRichiestaUMPrm(compTmp.getQtaImpegnata());
									lotto.setQtaRichiestaUMSec(compTmp.getQtaSec());

									if (lotto != null) {
										// boolean salvato = VtTessileUtil.saveAttivitaEsecLottiMat(lotto);
										/*
										 * boolean salvato = false; //Fix 81216 ConnectionManager.pushConnection(); int
										 * rc = lotto.save(); if(rc >= 0) ConnectionManager.commit(); else
										 * ConnectionManager.rollback(); ConnectionManager.pushConnection(); if(rc >= 0)
										 * salvato = true;
										 * 
										 * if(salvato){ atvMat.getLottiMateriali().add(lotto); }
										 */
										// Fix 81216
										atvMat.getLottiMateriali().add(lotto);
										atvMat.setAggiornaLottoDummy(true);
									}
									// }
								}

							}
						}

						if (atvMat != null) {
							atvMatCreate.add(atvMat);
							attivita.getMateriali().add(atvMat);
						}
						i++;
						// idRigaAttivita++;
					}
				}

				// Fix 81216
				ConnectionManager.pushConnection();
				attivita.setStatoOrigine('V');

				// PROVA 12-07-2018 int rc = ordEsec.save();
				int rc = attivita.save();

				if (rc >= 0)
					ConnectionManager.commit();
				else
					ConnectionManager.rollback();
				ConnectionManager.pushConnection();
			}
		}
		return atvMatCreate;
	}

	public static VtAttivitaEsecMateriale generaVtAttivitaEsecMateriale(VtOrdineEsecutivo ordEsec,
			Integer idRigaAttivita, String idArticolo, String idMagPrl, Integer idAtvMat, BigDecimal qtaRichiesta)
			throws SQLException {

		boolean creato = false;
		VtAttivitaEsecMateriale nuovaAtvMat = (VtAttivitaEsecMateriale) Factory
				.createObject(VtAttivitaEsecMateriale.class);

		// Imposto la chiave
		nuovaAtvMat.setIdAzienda(ordEsec.getIdAzienda());
		nuovaAtvMat.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
		nuovaAtvMat.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());

		// Riga Attivita
		nuovaAtvMat.setIdRigaAttivita(idRigaAttivita);

		List atvEsecList = ordEsec.getAttivitaEsecutive();
		if (!atvEsecList.isEmpty()) {

			Iterator iteAtvEsec = atvEsecList.iterator();
			while (iteAtvEsec.hasNext()) {

				AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) iteAtvEsec.next();

				if (atvEsec.getIdRigaAttivita().equals(idRigaAttivita)) {
					nuovaAtvMat.setAttivitaEsecutiva(atvEsec);
					break;
				}
			}

		}
		nuovaAtvMat.setIdRigaMateriale(idAtvMat);

		// la riga materiale moltiplicata per 10: creo la sequenza ordinamento (Short)
		Short sequenza = new Short((short) (idAtvMat.intValue() * 10));
		nuovaAtvMat.setSequenzaOrdin(sequenza);

		nuovaAtvMat.setIdArticolo(idArticolo);

		// Coeff Impiego = qtaRichiesta / qtaOrdEsec
		BigDecimal coeffImpiego = new BigDecimal(0);

		BigDecimal qtaOrd = ordEsec.getQtaOrdinataUMPrm();

		if (qtaOrd.compareTo(new BigDecimal(0)) > 0)
			coeffImpiego = qtaRichiesta.divide(qtaOrd, 6, BigDecimal.ROUND_HALF_UP);

		nuovaAtvMat.setCoeffImpiego(coeffImpiego);

		// Set descrizioni
		Articolo art = (Articolo) VtTessileUtil.getVtArticoloFromId(Azienda.getAziendaCorrente(), idArticolo);
		if (art != null) {

			nuovaAtvMat.setArticolo(art);
			nuovaAtvMat.getDescrizione().setDescrizione(art.getDescrizioneArticoloNLS().getDescrizione());
			nuovaAtvMat.getDescrizione().setDescrizioneRidotta(art.getDescrizioneArticoloNLS().getDescrizioneRidotta());

			String umMag = art.getIdUMPrmMag();
			String umTecnica = art.getIdUMTecnica();

			nuovaAtvMat.setIdUMPrmMag(umMag);

			if (umMag != null && umTecnica != null && qtaRichiesta != null) {
				qtaRichiesta = VtTessileUtil.getUMRicalcolata(umTecnica, umMag, qtaRichiesta);
				nuovaAtvMat.setQtaRichiestaUMPrm(qtaRichiesta);
			}

			QuantitaInUMRif qtaRif = VtTessileUtil.eseguiRicalcoloQuantita(art, art.getUMRiferimento(),
					art.getUMPrmMag(), art.getUMSecMag(), qtaRichiesta, Articolo.UM_PRM);

			nuovaAtvMat.setQtaRichiestaUMPrm(qtaRif.getQuantitaInUMPrm());
			nuovaAtvMat.setQtaRichiestaUMSec(qtaRif.getQuantitaInUMSec());

		}

		if (idMagPrl != null && !idMagPrl.equals(""))
			nuovaAtvMat.setIdMagazzinoPrl(idMagPrl);
		else
			nuovaAtvMat.setIdMagazzinoPrl(ordEsec.getIdMagazzinoPrl());

		// Fix 81214
		nuovaAtvMat.setEsponiInBollaPrelievo(true);

		// creato = VtTessileUtil.saveVtAttivitaEsecMaterialeConDummy(nuovaAtvMat);
		/*
		 * ConnectionManager.pushConnection(); int rc = nuovaAtvMat.save(); if(rc >= 0)
		 * ConnectionManager.commit(); else ConnectionManager.rollback();
		 * ConnectionManager.pushConnection(); if(rc >= 0)
		 */
		creato = true;

		if (creato) {
			return nuovaAtvMat;
		} else {
			System.out.println("ERR: Si sono verificati degli errori durante la creazione delle attività Materiale");
			return null;
		}
	}

	public Vector getListaRiservaComponenti(VtOrdineVenditaRigaPrm ordVen) {
		String where = VtOrdVenRigPrmMatTM.ID_AZIENDA + "='" + ordVen.getIdAzienda() + "' AND "
				+ VtOrdVenRigPrmMatTM.ID_ANNO_ORD + "= '" + ordVen.getAnnoDocumento() + "' AND "
				+ VtOrdVenRigPrmMatTM.ID_NUMERO_ORD + "= '" + ordVen.getNumeroDocumento() + "' AND "
				+ VtOrdVenRigPrmMatTM.ID_RIGA_ORD + "= '" + ordVen.getNumeroRigaDocumento() + "'";

		Vector ordVenMat = null;
		try {
			ordVenMat = VtOrdVenRigPrmMat.retrieveList(where, "", true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (ordVenMat != null && !ordVenMat.isEmpty()) {
			return ordVenMat;
		}
		return null;
	}

	public static List creaListaAttivitaOrdineEsecutivo(VtOrdineVenditaRigaPrm ordVen, VtOrdineEsecutivo nuovoOrdEsec) {

		List atvOrdEsecList = new ArrayList();

		if (ordVen != null) {

			// Fix 81411 - Inizio
			List atvOrdVenList = new ArrayList();

			String where = VtOrdVenRigAtvTM.ID_AZIENDA + "='" + ordVen.getIdAzienda() + "' AND "
					+ VtOrdVenRigAtvTM.ID_ANNO_ORD + "='" + ordVen.getAnnoDocumento() + "' AND "
					+ VtOrdVenRigAtvTM.ID_NUMERO_ORD + "='" + ordVen.getNumeroDocumento() + "' AND "
					+ VtOrdVenRigVarTM.ID_RIGA_ORD + "='" + ordVen.getNumeroRigaDocumento() + "' AND "
					+ VtOrdVenRigVarTM.ID_DET_RIGA_ORD + "='" + ordVen.getDettaglioRigaDocumento() + "' ";

			try {
				atvOrdVenList = VtOrdVenRigAtv.retrieveList(where, VtOrdVenRigAtvTM.SEQUENZA, false);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Fix 81411 - Fine

			// Fix 81411 List atvOrdVenList = ordVen.getVtOrdVenRigAtv();

			if (!atvOrdVenList.isEmpty()) {
				Iterator iteAtvModPro = atvOrdVenList.iterator();

				Integer idRigaAttivita = new Integer(1);
				Integer idOperazione = new Integer(10);
				AttivitaEsecutiva atvPrec = null;

				for (int i = 1; iteAtvModPro.hasNext(); i++) {
					VtOrdVenRigAtv atvOrdVen = (VtOrdVenRigAtv) iteAtvModPro.next();

					VtAttivita atvModPro = atvOrdVen.getAttivita();

					if (atvModPro != null) {
						AttivitaEsecutiva atvOrdEsec = creaAttivitaOrdineEsecutivoByAtvModPro(atvModPro, nuovoOrdEsec,
								idRigaAttivita, idOperazione);
						if (atvOrdEsec != null) {

							// Creo l'attività risorsa
							AttivitaEsecRisorsa aeRis = creaListaAttivitaRisorsaOrdEsec(atvModPro, atvOrdEsec);

							if (aeRis != null) {
								atvOrdEsec.getRisorse().add(aeRis);
							} else {
								System.out.println("Nessuna risorsa creata");
							}

							int rigAtt = idRigaAttivita.intValue();
							rigAtt++;
							idRigaAttivita = new Integer(rigAtt);

							int op = idOperazione.intValue();
							op = op + 10;
							idOperazione = new Integer(op);

							if (i == 1) {
								atvOrdEsec.setAttivitaIniziale(true);
								atvOrdEsec.setAttivitaFinale(false);

								// Fix 81447 - Inizio
								atvOrdEsec.setPoliticaConsAttivita('1');// manuale

							} else if (!iteAtvModPro.hasNext()) {
								atvOrdEsec.setAttivitaIniziale(false);
								atvOrdEsec.setAttivitaFinale(true);
								atvOrdEsec.getAttivitaPrecedenti().add(atvPrec);

								atvOrdEsec.setPoliticaConsAttivita('1'); // manuale
							} else {
								atvOrdEsec.setAttivitaIniziale(false);
								atvOrdEsec.setAttivitaFinale(false);
								atvOrdEsec.getAttivitaPrecedenti().add(atvPrec);

								atvOrdEsec.setPoliticaConsAttivita('2'); // 2: Automatica
							}

							// 07-05-2018
							// atvOrdEsec.setPoliticaConsAttivita('1'); //1: Manuale - 2: Automatica
							// fix 81471 atvOrdEsec.setPoliticaConsAttivita('2'); //1: Manuale - 2:
							// Automatica

							atvOrdEsec.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
							atvOrdEsec.setStatoAttivita('1');// 1: COnfermato
							atvOrdEsec.setStatoOrigine('V');

							atvOrdEsecList.add(atvOrdEsec);
							atvPrec = atvOrdEsec;
						} else {
							System.out.println("Errore durante la creazione dell'attività dell'ordine esecutivo");
						}
					}
				}
			}
		}
		return atvOrdEsecList;
	}

	public static AttivitaEsecutiva creaAttivitaOrdineEsecutivoByAtvModPro(VtAttivita atvModPro,
			VtOrdineEsecutivo nuovoOrdEsec, Integer idRigaAttivita, Integer idOperazione) {

		AttivitaEsecutiva nuovaAtvOrdEsec = (AttivitaEsecutiva) Factory.createObject(AttivitaEsecutiva.class);

		if (atvModPro != null) {

			// Chiavi
			nuovaAtvOrdEsec.setIdAzienda(nuovoOrdEsec.getIdAzienda());

			nuovaAtvOrdEsec.setIdAnnoOrdine(nuovoOrdEsec.getIdAnnoOrdine());
			nuovaAtvOrdEsec.setIdNumeroOrdine(nuovoOrdEsec.getIdNumeroOrdine());

			nuovaAtvOrdEsec.setIdRigaAttivita(idRigaAttivita);

			nuovaAtvOrdEsec.setIdOperazione(idOperazione.toString());

			nuovaAtvOrdEsec.setIdAttivita(atvModPro.getIdAttivita());

			nuovaAtvOrdEsec.getDescrizione().setDescrizione(atvModPro.getDescrizione().getDescrizione());
			nuovaAtvOrdEsec.getDescrizione().setDescrizioneRidotta(atvModPro.getDescrizione().getDescrizioneRidotta());

			CentroLavoro cL = atvModPro.getCentroLavoro();
			if (cL != null) {
				nuovaAtvOrdEsec.setCentroLavoro(cL);
				nuovaAtvOrdEsec.setReparto(cL.getReparto());
			}
			nuovaAtvOrdEsec.setCentroCosto(atvModPro.getCentroCosto());

			nuovaAtvOrdEsec.setStabilimento(nuovoOrdEsec.getStabilimento());

			nuovaAtvOrdEsec.setQtaRichiestaUMPrm(nuovoOrdEsec.getQtaOrdinataUMPrm());
			nuovaAtvOrdEsec.setQtaRichiestaUMSec(nuovoOrdEsec.getQtaOrdinataUMSec());

			nuovaAtvOrdEsec.setPoliticaConsAttivita('1'); // 1: Manuale - 2: Automatica

			nuovaAtvOrdEsec.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);

			nuovaAtvOrdEsec.setStatoAttivita('1');// 1: COnfermato

		}
		return nuovaAtvOrdEsec;
	}

	public static AttivitaEsecRisorsa creaListaAttivitaRisorsaOrdEsec(VtAttivita atvModPro,
			AttivitaEsecutiva atvOrdEsec) {

		AttivitaEsecRisorsa aeRis = null;
		List atvRisorsaModProList = atvModPro.getAttivitaRisorsaColl();
		if (!atvRisorsaModProList.isEmpty()) {
			Iterator iteAtvRisModPro = atvRisorsaModProList.iterator();

			while (iteAtvRisModPro.hasNext()) {
				AttivitaRisorsa atvRisModPro = (AttivitaRisorsa) iteAtvRisModPro.next();
				aeRis = (AttivitaEsecRisorsa) Factory.createObject(AttivitaEsecRisorsa.class);
				aeRis = creaAttivitaEsecRisorsa(atvOrdEsec, atvRisModPro);
			}
		}

		return aeRis;
	}

	public static AttivitaEsecRisorsa creaAttivitaEsecRisorsa(AttivitaEsecutiva attivitaEsecutiva,
			AttivitaRisorsa atvRisorsa) {
		AttivitaEsecRisorsa aeRis = (AttivitaEsecRisorsa) Factory.createObject(AttivitaEsecRisorsa.class);

		aeRis.getDescrizione().setDescrizione(atvRisorsa.getDescrizione().getDescrizione());
		aeRis.getDescrizione().setDescrizioneRidotta(atvRisorsa.getDescrizione().getDescrizioneRidotta());
		aeRis.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
		aeRis.setBufferDopo(atvRisorsa.getBufferDopo());
		aeRis.setBufferPrima(atvRisorsa.getBufferPrima());
		aeRis.setCoeffUtilizzo(atvRisorsa.getCoeffUtilizzo());
		aeRis.setCostoFisso(atvRisorsa.getCostoFisso());
		aeRis.setCostoPrevisto(atvRisorsa.getCostoPrevisto());

		aeRis.setCostoSpecifico(atvRisorsa.getCostoSpecifico());
		aeRis.setEsponiInBollaLav(atvRisorsa.getEsponiInBolla());
		aeRis.setIdRisorsa(atvRisorsa.getRRisorsa());
		aeRis.setTipoRisorsa(atvRisorsa.getTipoRisorsa());
		aeRis.setLivelloRisorsa(atvRisorsa.getLivelloRisorsa());
		aeRis.setLivRisorsaRichiesto('3'); // Risorsa

		aeRis.setParametroConv(atvRisorsa.getParametroConver());
		aeRis.setPoliticaConsRisorsa(atvRisorsa.getPolConsRisorse());

		Short sequenza = new Short((short) atvRisorsa.getSequenzaOrdin());
		aeRis.setSequenzaOrdin(sequenza);

		aeRis.setTempoAssegnato(atvRisorsa.getTempoAssegnato());
		aeRis.setTempoBase(atvRisorsa.getTempoBase());
		aeRis.setTempoFisso(atvRisorsa.getTempoFisso());
		aeRis.setTempoUnitario(atvRisorsa.getTempoUnitario());
		aeRis.setTipoCosto(atvRisorsa.getTipoCosto());
		aeRis.setIdUMCosti(atvRisorsa.getRUmCosti());
		aeRis.setIdUMTempi(atvRisorsa.getRUmTempi());

		aeRis.setAttivitaEsecutiva(attivitaEsecutiva);

		// Fix 81535 - Inizio
		AzioniOrdine azioniOrdini = (AzioniOrdine) Factory.createObject(AzioniOrdine.class); // Fix 4974
		BigDecimal qtaRich = attivitaEsecutiva.getQtaRichiestaUMPrm();
		try {
			azioniOrdini.ricalcolaTempiAtvRsr(aeRis, qtaRich, true);
		} catch (ThipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Fix 81535 - Fine

		return aeRis;
	}

	// Fix 81327 - Inizio
	public static boolean creaVtAttivitaEsecProdottoProduzione(VtOrdineVenditaRigaPrm ordVenRig,
			VtOrdineEsecutivo ordEsec, int index) throws SQLException {
		boolean creato = false;

		List atvPrdCreate = new ArrayList();

		if (ordVenRig != null) {

			VtAttivitaEsecProdotto nuovaAtvPrd = (VtAttivitaEsecProdotto) Factory
					.createObject(VtAttivitaEsecProdotto.class);

			// Imposto la chiave

			nuovaAtvPrd.setIdAzienda(ordEsec.getIdAzienda());
			nuovaAtvPrd.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
			nuovaAtvPrd.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());

			Integer rigaAtv = new Integer(1);

			// Inserisco nell'ultima attività immessa
			List atvEsecList = ordEsec.getAttivitaEsecutive();
			if (!atvEsecList.isEmpty()) {
				rigaAtv = new Integer(atvEsecList.size());
				nuovaAtvPrd.setIdRigaAttivita(rigaAtv);

				AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) atvEsecList.get(rigaAtv.intValue() - 1);

				// Fix 81447 - Inizio
				atvEsec.setPoliticaConsAttivita('1');
				// Fix 81447 - Fine

				nuovaAtvPrd.setAttivitaEsecutiva(atvEsec);
			}

			nuovaAtvPrd.setIdRigaProdotto(new Integer(index));
			nuovaAtvPrd.setTipoProdotto('0'); // Prodotto primario

			// Set descrizioni
			Articolo art = ordVenRig.getArticolo();
			if (art != null) {
				nuovaAtvPrd.setArticolo(art);
				nuovaAtvPrd.getDescrizione().setDescrizione(art.getDescrizioneArticoloNLS().getDescrizione());
				nuovaAtvPrd.getDescrizione()
						.setDescrizioneRidotta(art.getDescrizioneArticoloNLS().getDescrizioneRidotta());

			}
			nuovaAtvPrd.setCoeffProduzione(new BigDecimal(0));

			/*
			 * String idMagVers = ordEsec.getIdMagazzinoVrs();
			 * nuovaAtvPrd.setIdMagazzinoVrs(idMagVers);
			 */ // Fix 81120
			nuovaAtvPrd.setIdMagazzinoVrs(ordEsec.getIdMagazzinoVrs());

			// Set riferimenti ordine vendita dalla riga estratta
			nuovaAtvPrd.setIdCliente(ordVenRig.getIdCliente());
			nuovaAtvPrd.setAnnoOrdineCliente(ordVenRig.getAnnoDocumento());
			nuovaAtvPrd.setNumeroOrdineCliente(ordVenRig.getNumeroDocumento());
			nuovaAtvPrd.setRigaOrdineCliente(ordVenRig.getNumeroRigaDocumento());

			nuovaAtvPrd.setQtaRichiestaUMPrm(ordVenRig.getQtaInUMPrmMag());

			// Fix 81214
			nuovaAtvPrd.setEsponiInBollaVersam(true);
			nuovaAtvPrd.setPoliticaConsVersam('2');

			// Fix 81411
			nuovaAtvPrd.setAzioneSuDispon('1');
			nuovaAtvPrd.setAggiornaLottoDummy(true);

			// Se sono presenti lotti std nell'ordine di vendita li devo aggiungere ai lotti
			// del prodotto finito
			if (ordVenRig.getRigheLotto() != null && !ordVenRig.getRigheLotto().isEmpty()) {
				List lotti = ordVenRig.getRigheLotto();
				Iterator iter = lotti.iterator();
				while (iter.hasNext()) {
					OrdineVenditaRigaLottoPrm lottoOrdVen = (OrdineVenditaRigaLottoPrm) iter.next();
					if (!lottoOrdVen.getIdLotto().equals("-")) {
						// Creo nuovo lotto per prodotto
						AttivitaEsecLottiPrd lottoNew = (AttivitaEsecLottiPrd) Factory
								.createObject(AttivitaEsecLottiPrd.class);
						lottoNew.setFather(nuovaAtvPrd);

						lottoNew.setIdLotto(lottoOrdVen.getIdLotto());
						lottoNew.setIdArticolo(nuovaAtvPrd.getIdArticolo());
						lottoNew.setQtaRichiestaUMPrm(lottoOrdVen.getQtaInUMPrmMag());
						lottoNew.setQtaRichiestaUMSec(lottoOrdVen.getQtaInUMSecMag());

						nuovaAtvPrd.getLottiProdotti().add(lottoNew);
					}
				}
			}
			// Fix 81214 Fine

			// creato = VtTessileUtil.saveVtAttivitaEsecProdotto(nuovaAtvPrd);
			// creato = VtTessileUtil.saveVtAttivitaEsecProdottoConDummy(nuovaAtvPrd);

			// Inserisco nell'ultima attività immessa
			atvEsecList = ordEsec.getAttivitaEsecutive();
			if (!atvEsecList.isEmpty()) {
				rigaAtv = new Integer(atvEsecList.size());
				nuovaAtvPrd.setIdRigaAttivita(rigaAtv);

				AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) atvEsecList.get(rigaAtv.intValue() - 1);

				atvEsec.getProdotti().add(nuovaAtvPrd);
				ConnectionManager.pushConnection();
				int rc = atvEsec.save();
				if (rc >= 0)
					ConnectionManager.commit();
				else
					ConnectionManager.rollback();
				ConnectionManager.pushConnection();
				if (rc >= 0)
					creato = true;
			}

			/*
			 * ConnectionManager.pushConnection(); int rc = nuovaAtvPrd.save(); if(rc >= 0)
			 * ConnectionManager.commit(); else ConnectionManager.rollback();
			 * ConnectionManager.pushConnection(); if(rc >= 0) creato = true;
			 */
		}

		return creato;

	}
	// Fix 81327 - Fine

	public static boolean creaVtAttivitaEsecProdottoProduzione(VtOrdineVenditaRigaPrm ordVenRig,
			VtOrdineEsecutivo ordEsec, String idMagVersamento) throws SQLException {

		boolean creato = false;
		List atvPrdCreate = new ArrayList();

		if (ordVenRig != null) {

			VtAttivitaEsecProdotto nuovaAtvPrd = (VtAttivitaEsecProdotto) Factory
					.createObject(VtAttivitaEsecProdotto.class);

			// Imposto la chiave

			nuovaAtvPrd.setIdAzienda(ordEsec.getIdAzienda());
			nuovaAtvPrd.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
			nuovaAtvPrd.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());

			Integer rigaAtv = new Integer(1);

			// Inserisco nell'ultima attività immessa
			List atvEsecList = ordEsec.getAttivitaEsecutive();
			if (!atvEsecList.isEmpty()) {
				rigaAtv = new Integer(atvEsecList.size());
				nuovaAtvPrd.setIdRigaAttivita(rigaAtv);

				AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) atvEsecList.get(rigaAtv.intValue() - 1);

				// Fix 81447 - Inizio
				atvEsec.setPoliticaConsAttivita('1'); // manuale
				// Fix 81447 - Fine

				nuovaAtvPrd.setAttivitaEsecutiva(atvEsec);
			}

			nuovaAtvPrd.setIdRigaProdotto(new Integer(1));
			nuovaAtvPrd.setTipoProdotto('0'); // Prodotto primario

			// Set descrizioni
			Articolo art = ordVenRig.getArticolo();
			if (art != null) {
				nuovaAtvPrd.setArticolo(art);
				nuovaAtvPrd.getDescrizione().setDescrizione(art.getDescrizioneArticoloNLS().getDescrizione());
				nuovaAtvPrd.getDescrizione()
						.setDescrizioneRidotta(art.getDescrizioneArticoloNLS().getDescrizioneRidotta());

			}
			nuovaAtvPrd.setCoeffProduzione(new BigDecimal(0));

			/*
			 * String idMagVers = ordEsec.getIdMagazzinoVrs();
			 * nuovaAtvPrd.setIdMagazzinoVrs(idMagVers);
			 */ // Fix 81120
			nuovaAtvPrd.setIdMagazzinoVrs(idMagVersamento);

			// Set riferimenti ordine vendita dalla riga estratta
			nuovaAtvPrd.setIdCliente(ordVenRig.getIdCliente());
			nuovaAtvPrd.setAnnoOrdineCliente(ordVenRig.getAnnoDocumento());
			nuovaAtvPrd.setNumeroOrdineCliente(ordVenRig.getNumeroDocumento());
			nuovaAtvPrd.setRigaOrdineCliente(ordVenRig.getNumeroRigaDocumento());

			nuovaAtvPrd.setQtaRichiestaUMPrm(ordVenRig.getQtaInUMPrmMag());

			// Fix 81214
			nuovaAtvPrd.setEsponiInBollaVersam(true);
			nuovaAtvPrd.setPoliticaConsVersam('2');

			// Se sono presenti lotti std nell'ordine di vendita li devo aggiungere ai lotti
			// del prodotto finito
			if (ordVenRig.getRigheLotto() != null && !ordVenRig.getRigheLotto().isEmpty()) {
				List lotti = ordVenRig.getRigheLotto();
				Iterator iter = lotti.iterator();
				while (iter.hasNext()) {
					OrdineVenditaRigaLottoPrm lottoOrdVen = (OrdineVenditaRigaLottoPrm) iter.next();
					if (!lottoOrdVen.getIdLotto().equals("-")) {
						// Creo nuovo lotto per prodotto
						AttivitaEsecLottiPrd lottoNew = (AttivitaEsecLottiPrd) Factory
								.createObject(AttivitaEsecLottiPrd.class);
						lottoNew.setFather(nuovaAtvPrd);

						lottoNew.setIdLotto(lottoOrdVen.getIdLotto());
						lottoNew.setIdArticolo(nuovaAtvPrd.getIdArticolo());
						lottoNew.setQtaRichiestaUMPrm(lottoOrdVen.getQtaInUMPrmMag());
						lottoNew.setQtaRichiestaUMSec(lottoOrdVen.getQtaInUMSecMag());

						nuovaAtvPrd.getLottiProdotti().add(lottoNew);
					}
				}
			}
			// Fix 81214 Fine

			// creato = VtTessileUtil.saveVtAttivitaEsecProdotto(nuovaAtvPrd);
			// creato = VtTessileUtil.saveVtAttivitaEsecProdottoConDummy(nuovaAtvPrd);

			// Inserisco nell'ultima attività immessa
			atvEsecList = ordEsec.getAttivitaEsecutive();
			if (!atvEsecList.isEmpty()) {
				rigaAtv = new Integer(atvEsecList.size());
				nuovaAtvPrd.setIdRigaAttivita(rigaAtv);

				AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) atvEsecList.get(rigaAtv.intValue() - 1);

				atvEsec.getProdotti().add(nuovaAtvPrd);
				ConnectionManager.pushConnection();
				int rc = atvEsec.save();
				if (rc >= 0)
					ConnectionManager.commit();
				else
					ConnectionManager.rollback();
				ConnectionManager.pushConnection();
				if (rc >= 0)
					creato = true;
			}

			/*
			 * ConnectionManager.pushConnection(); int rc = nuovaAtvPrd.save(); if(rc >= 0)
			 * ConnectionManager.commit(); else ConnectionManager.rollback();
			 * ConnectionManager.pushConnection(); if(rc >= 0) creato = true;
			 */
		}

		return creato;
	}

	// protected void otherActions(ClassADCollection cadc, ServletEnvironment
	// se) throws ServletException, IOException {
	// String azione = se.getRequest().getParameter("thAction").toUpperCase();
	// String cD = azione;
	// }

	private static VtOrdineEsecutivo trovaOrdEsec(String key) {
		boolean exist = false;
		VtOrdineEsecutivo ordEsec = null;
		String[] keys = KeyHelper.unpackObjectKey(key);
		String anno = keys[1];
		String numOrd = keys[2];
		String rigaOrd = keys[3];

		String idAzienda = " ";
		String annoEsec = " ";
		String numEsec = " ";
		String rigaEsec = " ";

		//

		String select = "  select B.ID_AZIENDA,B.ID_ANNO_ORD,B.ID_NUMERO_ORD  from THIP.VT_ORDESE_ATV_PRD B  "
				+ " inner JOIN THIP.ORD_VEN_RIG A ON B.R_ANNO_ORD_CLI=A.ID_ANNO_ORD AND A.ID_NUMERO_ORD=B.R_NUMERO_ORD_CLI  "
				+ " AND A.ID_RIGA_ORD=B.R_RIGA_ORD_CLI AND A.ID_AZIENDA=B.ID_AZIENDA " + " WHERE  A.ID_ANNO_ORD='"
				+ anno + "' AND A.ID_NUMERO_ORD='" + numOrd + "'  AND A.ID_RIGA_ORD='" + rigaOrd
				+ "' AND A.ID_AZIENDA='" + Azienda.getAziendaCorrente() + "'";
		CachedStatement cIstance = new CachedStatement(select);

		try {
			ResultSet rs = cIstance.executeQuery();
			if (rs.next()) {
				idAzienda = rs.getString(1).trim();
				annoEsec = rs.getString(2).trim();
				numEsec = rs.getString(3).trim();

			}

			rs.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (idAzienda != null && !idAzienda.trim().equals("null") && !idAzienda.trim().equals("")) {
			Object[] keyOrdEsec = { Azienda.getAziendaCorrente(), annoEsec, numEsec };
			String chiave = KeyHelper.buildObjectKey(keyOrdEsec);
			try {
				ordEsec = (VtOrdineEsecutivo) PersistentObject.elementWithKey(VtOrdineEsecutivo.class, chiave,
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ordEsec;
	}

	private static VtOrdineAcquistoRigaPrm trovaOrdAcq(String key) {
		boolean exist = false;
		VtOrdineAcquistoRigaPrm ordAcqRigaPrm = null;
		String[] keys = KeyHelper.unpackObjectKey(key);
		String anno = keys[1];
		String numOrd = keys[2];
		String rigaOrd = keys[3];

		String idAzienda = " ";
		String annoAcq = " ";
		String numAcq = " ";
		String rigaAcq = " ";

		//

		String select = " 	SELECT DISTINCT B.ID_AZIENDA,B.ID_ANNO_ORD,B.ID_NUMERO_ORD,B.ID_RIGA_ORD  "
				+ "	FROM THIP.ORD_ACQ_RIG B   "
				+ "	inner JOIN THIP.ORD_VEN_RIG A ON B.R_ANNO_ORDC=A.ID_ANNO_ORD AND A.ID_NUMERO_ORD=B.R_NUMERO_ORDC  "
				+ "				AND A.ID_RIGA_ORD=B.R_RIGA_ORDC AND A.ID_DET_RIGA_ORD=B.R_DET_RIGA_ORDC AND A.ID_AZIENDA=B.ID_AZIENDA "
				+ "	WHERE  A.ID_ANNO_ORD='" + anno + "' AND A.ID_NUMERO_ORD='" + numOrd + "'  AND A.ID_RIGA_ORD='"
				+ rigaOrd + "' AND A.ID_AZIENDA='" + Azienda.getAziendaCorrente() + "'";
		CachedStatement cIstance = new CachedStatement(select);

		try {
			ResultSet rs = cIstance.executeQuery();
			if (rs.next()) {
				idAzienda = rs.getString(1).trim();
				annoAcq = rs.getString(2).trim();
				numAcq = rs.getString(3).trim();
				rigaAcq = rs.getString(4).trim();

			}

			rs.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (idAzienda != null && !idAzienda.trim().equals("null") && !idAzienda.trim().equals("")) {
			Object[] keyOrdAcqRiga = { Azienda.getAziendaCorrente(), annoAcq, numAcq, rigaAcq };
			String chiave = KeyHelper.buildObjectKey(keyOrdAcqRiga);
			try {
				ordAcqRigaPrm = (VtOrdineAcquistoRigaPrm) PersistentObject.elementWithKey(VtOrdineAcquistoRigaPrm.class,
						chiave, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ordAcqRigaPrm;
	}

	public void processAction(ServletEnvironment se) throws ServletException, IOException {
		boolean adisp = true;
		if (getAzione(se) != null) {
			String azione = getAzione(se);
			if (azione.equals("MATRIX_RELOAD")) {
				VtOrdineVenditaRigaPrm riga = null;
				String key = se.getRequest().getParameter("ObjectKey");
				String fatherKey = se.getRequest().getParameter("FatherKey");
				try {
					riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
							key, VtOrdineVenditaRigaPrm.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (riga != null && riga.getGruppoTC() != null)
					VtGestioneTagliaColoreUtil.apriGestioneTagliaColoreModifica(se, "VtOrdineVenditaRigaPrm", fatherKey,
							riga.getGruppoTC());
				return;
			}
			String modoForm = getStringParameter(se.getRequest(), DocumentoNavigazioneWeb.MODO_FORM);

			boolean eseguireSuperPerUpdate = false; // se sono in update riga
			// ridotta tessuto richiamp
			// pannello specifico,
			// altrimenti richiamo super
			String tesKey = null;
			boolean isOrdineDerivato = false; // se l'ordine ha riferimenti
			// ordine a disporre è un ordine
			// derivato e quindi devo
			// visualizzare griglia
			DocumentoDatiSessione datiSession = getDatiSessione(se);

			if (datiSession != null) {
				String[] cD2 = datiSession.getChiaviDocumento();
				if (cD2 == null) {
					ClassADCollection cadc = getClassADCollection(getStringParameter(se.getRequest(), CLASS_NAME));
					datiSession = DocumentoDatiSessione.getDocumentoDatiSessione(se, creaDocumentoDatiSessione());
				}
			}
			if (datiSession == null) {
				ClassADCollection cadc = getClassADCollection(getStringParameter(se.getRequest(), CLASS_NAME));
				DocumentoDatiSessione datiSessione = DocumentoDatiSessione.getDocumentoDatiSessione(se,
						creaDocumentoDatiSessione());
				String[] leChiavi = datiSessione.getChiaviDocRigaPrm();
				String[] cD = datiSessione.getChiaviDocumento();

				// tesKey =
				// KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
				String objectKeys[] = se.getRequest().getParameterValues("ObjectKey");
				if (objectKeys != null) // 80983
					tesKey = objectKeys[0];

			} else {
				tesKey = KeyHelper.buildObjectKey(datiSession.getValoriChiaviDocumento());
			}
			VtOrdineVendita tesO = null;
			try {
				tesO = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (tesO != null) { // 80983

				String numD = tesO.getNumeroOrdAdisp();
				String annD = tesO.getAnnoOrdineAdisp();

				Object mF = modoForm;// Matteo190418

				// Matteo190418 if (numD != null && !numD.equals("") && annD != null &&
				// !annD.equals("")) {
				if (numD != null && !numD.equals("") && annD != null && !annD.equals("") && mF != null) {
					isOrdineDerivato = true; // ordine derivato: quando inserisco
					// nuova riga visualizzo griglia
					// relativo ordine a disporre
				}

				// se contiene NEW indica che sto arrivando dalla griglia degli
				// ordini a disporre
				String newUpd = getStringParameter(se.getRequest(), "thUpdateObjectMode");
				String newKeyAdisp = getStringParameter(se.getRequest(), "thUpdateObjectKey");

				// Immissione standard
				// MATTEO190418 if (azione.equals(NEW) || azione.equals(NEW_CAMBIO_CAU)) {

				if (azione.equals(NEW)) {
					if (isOrdineDerivato) {

						// Fix 80970 - Devo intervenire qua per aprire altro tipo di griglia

						// Fix 80970
						// adisp = grigliaOrdiniADisporre(se, tesO, tesKey);
						adisp = grigliaEditOrdiniADisporre(se, tesO, tesKey);

						if (!adisp) {
							super.processAction(se);
						}
					} else {
						if (modoForm != null) {

							super.processAction(se);

						} else {
							super.processAction(se);
						}
					}
				} else {

					// String azione = getAzione(se);

					// ---------- NUOVO TESSUTO --------------------------------
					if (azione.equals(NUOVOTEX_NOGRID) || azione.equals(NUOVOTEX)) {
						if (azione.equals(NUOVOTEX)) {
							adisp = grigliaOrdiniADisporre(se, tesO, tesKey);
							// se non è a disporre faccio il NUOVOTEX standard
							if (!adisp) {
								eseguiNuovoTex(se, newKeyAdisp);
							}
						} else {
							eseguiNuovoTex(se, newKeyAdisp);

						}
					}

					// Fix 81750 >
					else if (azione.equals(NUOVOCOMP_NOGRID)) {
						eseguiNuovoComp(se, newKeyAdisp);
					}
					// Fix 81750 <

					// ---------- NUOVO FINITO --------------------------------
					else if (azione.equals(NUOVOFIN)) {
						DocumentoDatiSessione datiSessione = getDatiSessione(se);
						String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
						String modoFormIni = datiSessione.getModoForm();
						String modoFormPrec = cercaModoForm(se, "OrdineVenditaRigaPrm");
						// modo per individuare se COMPLETA o RIDOTTA

						DocOrdNavigazioneWeb navigatore = (DocOrdNavigazioneWeb) datiSessione.getNavigatore();
						String ret = navigatore.getJspRigaPrmCompleta();

						// al momento la nuova immissione veloce avviene sia per
						// ridotta che per completa
						// if(modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
						String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
						String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(), "thObjectKeyNum");
						int objectKeyNum = 0;
						objectKeyNum = Integer.parseInt(objectKeyNumStr);
						String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
						String chiave = "";
						String chiaveTot = "";
						// cercare la riga "chiave" e leggere tutte le righe di quel
						// gruppo
						// poi valorizzare sui nuovi attributi i dati delle righe
						// per il caso di
						// manutenzione con il comando qui sotto (attributo con nomi
						// nuovi)
						// poi su ONLOAD della JS spostare i nuovi attributi su
						// quelli a video
						se.getRequest().setAttribute("operazione", "IMMISSIONE");
						// Richiamo il pannello di gestione righe
						String codRagRig = "0";

						String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidFin.jsp"
								+ "?Mode=NEW&Key=" + URLEncoder.encode(docKey) + "&InitialActionAdapter="
								+ getClass().getName() + "&CodRagRig=" + codRagRig + "&ImmMod=IMM";
						se.sendRequest(getServletContext(), url, false);

						// Eseguo refresh della griglia
						String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
						se.sendRequest(getServletContext(), urlr, true);

						// }
						// else {
						// se COMPLETA
						// Emetto errore: nuova funzionalità immissione solo per
						// ridotta (per ora)
						// String url1 =
						// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrNewCompleta.jsp";
						// se.sendRequest(getServletContext(), url1, true);

						// Eseguo refresh della griglia
						// String urlr =
						// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
						// se.sendRequest(getServletContext(), urlr, true);
						// }
					}
					// ---------- CONFERMA PRE-ORDINE TESSUTO IN FINITO
					// -------------------------------
					else if (azione.equals(CONF_PRE_ORD)) {
						DocumentoDatiSessione datiSessione = getDatiSessione(se);
						String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
						String modoFormIni = datiSessione.getModoForm();
						String modoFormPrec = cercaModoForm(se, "OrdineVenditaRigaPrm");
						// modo per individuare se COMPLETA o RIDOTTA

						DocOrdNavigazioneWeb navigatore = (DocOrdNavigazioneWeb) datiSessione.getNavigatore();
						String ret = navigatore.getJspRigaPrmCompleta();

						// al momento la nuova immissione veloce avviene sia per
						// ridotta che per completa
						// if(modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
						String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
						String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(), "thObjectKeyNum");
						int objectKeyNum = 0;
						objectKeyNum = Integer.parseInt(objectKeyNumStr);
						String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
						String chiave = "";
						String chiaveTot = "";
						chiaveTot = objectKeys[0]; // la prima chiave corrisponde al
						// dettaglio chiave scelta
						// cercare la riga "chiave" e leggere tutte le righe di quel
						// gruppo
						// poi valorizzare sui nuovi attributi i dati delle righe
						// per il caso di
						// manutenzione con il comando qui sotto (attributo con nomi
						// nuovi)
						// poi su ONLOAD della JS spostare i nuovi attributi su
						// quelli a video

						chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) + PersistentObject.KEY_SEPARATOR
								+ KeyHelper.getTokenObjectKey(chiaveTot, 2) + PersistentObject.KEY_SEPARATOR
								+ KeyHelper.getTokenObjectKey(chiaveTot, 3) + PersistentObject.KEY_SEPARATOR
								+ KeyHelper.getTokenObjectKey(chiaveTot, 4);
						VtOrdineVenditaRigaPrm rigaOrdPrm = null;
						try {
							rigaOrdPrm = (VtOrdineVenditaRigaPrm) PersistentObject
									.elementWithKey(VtOrdineVenditaRigaPrm.class, chiave, PersistentObject.NO_LOCK);
						} catch (SQLException e) {
							e.printStackTrace();
						}
						char tipoRiga = rigaOrdPrm.getTipologiaOrdine(); // P=pre-ordine,
						// C=ordine-finito,
						// -=tutti
						// gli
						// altri

						// Fix 80820 - Inizio
						// controllo se ci sono pezze assegnate
						List ctrlRighe = rigaOrdPrm.getRigheLotto();

						// Cambio controllo se ci sono pezze assegnate
						boolean ctrlPezze = false;

						String selectPezze = "SELECT ID_AZIENDA, ID_MATRICOLA " + "FROM THIP.VT_PEZZE "
								+ "WHERE ID_AZIENDA='" + rigaOrdPrm.getIdAzienda() + "'" + " AND R_ANNO_ORD_VEN='"
								+ rigaOrdPrm.getAnnoDocumento() + "'" + " AND R_NR_ORD_VEN='"
								+ rigaOrdPrm.getNumeroDocumento() + "'" + " AND R_RIGA_ORD_VEN="
								+ rigaOrdPrm.getNumeroRigaDocumento() + " AND R_DET_RIGA_ORD_VEN="
								+ rigaOrdPrm.getDettaglioRigaDocumento() + " AND R_ARTICOLO ='"
								+ rigaOrdPrm.getIdArticolo() + "'";

						// VtOrdineVendita ordVenDaCopia = null;
						String idAzienda = "";
						String idMatricola = "";

						CachedStatement cInstance = new CachedStatement(selectPezze);
						try {

							ResultSet rs = cInstance.executeQuery();

							while (rs.next()) {

								idAzienda = rs.getString(1);
								idMatricola = rs.getString(2);

								break;
							}

							rs.close();

						} catch (Exception e) {
							e.printStackTrace();
						}

						if (idMatricola != "")
							ctrlPezze = true;
						// fine controllo cambiato

						// Fix 80820 - Fine

						char rilOrdine = rigaOrdPrm.getRilascioOrdineProd(); // 0=
						// non
						// estratto,
						// 1=estratto,
						// 2=
						// generato
						// ordine
						// ,
						// 3=
						// da
						// non
						// estrarre
						// se la riga non e' PRE-ORDINE non posso fare conferma
						// pre-ordine

						if (tipoRiga != 'P') {

							String urle = "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrCnf2.jsp";
							se.sendRequest(getServletContext(), urle, false);

						}
						// fix 80779 se la riga ha rilascio ordine prod diverso da
						// "Generato ordine"
						// else if (rilOrdine!='2')
						// {
						// String urle =
						// "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrRilOrd.jsp";
						// se.sendRequest(getServletContext(), urle, false);
						// }
						// Fix 80820 - Inizio
						// controllo se ci sono pezze assegnate
						// else if (ctrlRighe.isEmpty()) { //Cambio controllo

						/*
						 * Fix 80984 - Inizio else if (!ctrlPezze){ //Mettere il confim continuare si/no
						 * Fix 80984
						 * 
						 * String urle =
						 * "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrRilOrd.jsp";
						 * se.sendRequest(getServletContext(), urle, false);
						 * 
						 * 
						 * String adapter = getClass().getName(); ServletOutputStream out =
						 * se.getResponse().getOutputStream();
						 * out.println("<script language='JavaScript1.2'>");
						 * out.println(" var okInit = confirm('Conferma?'); ");
						 * out.println(" if(okInit){ ");
						 * 
						 * String webApplicationPath =
						 * IniFile.getValue("thermfw.ini","Web","WebApplicationPath"); String
						 * servletPath = IniFile.getValue("thermfw.ini","Web","ServletPath");
						 * 
						 * out.println("parent.opener.setLocationOnWindow(window, '/servlet/" +
						 * "it.thera.thip.tessile.vendite.ordineVE.servlet.VtContinua?Chiave=" +
						 * chiaveTot + "&Adapter" + adapter +"'); ");
						 * 
						 * out.println(" } "); out.println("</script>");
						 * 
						 * 
						 * }
						 */ // Fix 80894
						// Fix 8020 - Fine
						else {
							String idAzi = rigaOrdPrm.getIdAzienda();
							VtArticolo arT = (VtArticolo) rigaOrdPrm.getArticolo();

							String idArtFin = rigaOrdPrm.getRArtFinito();
							String[] keyArtF = { idAzi, idArtFin };
							String keyArF = KeyHelper.buildObjectKey(keyArtF);
							VtArticolo arF = null;
							try {
								arF = (VtArticolo) VtArticolo.elementWithKey(VtArticolo.class, keyArF,
										PersistentObject.NO_LOCK);
							} catch (SQLException e) {
								e.printStackTrace();
							}

							String ris = null;
							try {
								ris = controllaSchemiArticoli(arT, arF, idAzi);
							} catch (SQLException e) {
								e.printStackTrace();
							}
							// Segnala errore per schema prodotto MODELLO+TESSUTO
							// diverso da schema prodotto CONFEZIONE
							if (ris != null) {
								if (ris.equals("C")) {
									String urle = "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrCnf3.jsp";
									se.sendRequest(getServletContext(), urle, false);

								}
								// Segnala errore per mancanza abbinamento su
								// tabella per gli schemmi MODELLO+TESSUTO
								else if (ris.equals("E")) {
									String urle = "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrCnf4.jsp";
									se.sendRequest(getServletContext(), urle, false);
								}
							}
							// Fix 80984 - Inizio
							else if (!ctrlPezze) {
								String adapter = getClass().getName();
								ServletOutputStream out = se.getResponse().getOutputStream();
								out.println("<script language='JavaScript1.2'>");
								out.println(
										" var okInit = confirm('ATTENZIONE: non ci sono pezze riservate. Continuare? '); ");
								out.println(" if(okInit){ ");

								String webApplicationPath = IniFile.getValue("thermfw.ini", "Web",
										"WebApplicationPath");
								String servletPath = IniFile.getValue("thermfw.ini", "Web", "ServletPath");

								out.println("parent.opener.setLocationOnWindow(window, '/servlet/"
										+ "it.thera.thip.tessile.vendite.ordineVE.servlet.VtContinua?Chiave="
										+ chiaveTot + "&Adapter" + adapter + "'); ");

								out.println(" } else window.parent.close(); ");
								out.println("</script>");
							}
							// Fix 80894 - Fine
							// Fix 02/05/2017 - Inizio
							else if (ctrlPezze) {
								/*
								 * String adapter = getClass().getName(); ServletOutputStream out =
								 * se.getResponse().getOutputStream();
								 * out.println("<script language='JavaScript1.2'>"); String webApplicationPath =
								 * IniFile.getValue("thermfw.ini","Web","WebApplicationPath"); String
								 * servletPath = IniFile.getValue("thermfw.ini","Web","ServletPath");
								 * 
								 * out.println("parent.opener.setLocationOnWindow(window, '/servlet/" +
								 * "it.thera.thip.tessile.vendite.ordineVE.servlet.VtContinua?Chiave=" +
								 * chiaveTot + "&Adapter" + adapter +"'); ");
								 * 
								 * out.println("</script>");
								 */
								se.getRequest().setAttribute("operazione", "IMMISSIONE");
								// Richiamo il pannello di gestione righe
								String codRagRig = "0";

								String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidCnf.jsp"
										+ "?Mode=UPDATE&Key=" + URLEncoder.encode(chiaveTot) + "&InitialActionAdapter="
										+ getClass().getName() + "&CodRagRig=" + codRagRig + "&ImmMod=MOD";
								se.sendRequest(getServletContext(), url, false);
							}
							// Fix 02/05/2017 - Fine

							/*
							 * Fix 80894 - Inizio else { se.getRequest().setAttribute("operazione",
							 * "IMMISSIONE"); // Richiamo il pannello di gestione righe String codRagRig =
							 * "0";
							 * 
							 * String url =
							 * "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidCnf.jsp" +
							 * "?Mode=UPDATE&Key=" + URLEncoder.encode(chiaveTot) + "&InitialActionAdapter="
							 * + getClass().getName() + "&CodRagRig=" + codRagRig + "&ImmMod=MOD";
							 * se.sendRequest(getServletContext(), url, false);
							 * 
							 * // Eseguo refresh della griglia (non serve perchè // parte subito) // String
							 * urlr = //
							 * "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
							 * // se.sendRequest(getServletContext(), urlr, true);
							 * 
							 * // } // else { // se COMPLETA // Emetto errore: nuova funzionalità immissione
							 * solo // per ridotta (per ora) // String url1 = //
							 * "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrNewCompleta.jsp";
							 * // se.sendRequest(getServletContext(), url1, true);
							 * 
							 * // Eseguo refresh della griglia // String urlr = //
							 * "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
							 * // se.sendRequest(getServletContext(), urlr, true); // }
							 * 
							 * }
							 */ // questo else non serve Fix 80894
						}
					}
					// --------------------------------------------------------------------------------
					// --------------------------------------------------------------------------------
					// --------------------------------------------------------------------------------
					else if (azione.equals(DELETE)) {
						String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
						String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(), "thObjectKeyNum");
						int objectKeyNum = 0;
						objectKeyNum = Integer.parseInt(objectKeyNumStr);
						String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
						String chiave = "";
						String chiaveTot = "";
						for (int i = 0; i < objectKeys.length; i++) {
							chiaveTot = objectKeys[i];
							chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) + PersistentObject.KEY_SEPARATOR
									+ KeyHelper.getTokenObjectKey(chiaveTot, 2) + PersistentObject.KEY_SEPARATOR
									+ KeyHelper.getTokenObjectKey(chiaveTot, 3) + PersistentObject.KEY_SEPARATOR
									+ KeyHelper.getTokenObjectKey(chiaveTot, 4);
							VtOrdineVenditaRigaPrm rigaOrdPrm = null;
							try {
								rigaOrdPrm = (VtOrdineVenditaRigaPrm) PersistentObject
										.elementWithKey(VtOrdineVenditaRigaPrm.class, chiave, PersistentObject.NO_LOCK);
							} catch (SQLException e) {
								e.printStackTrace();
							}

							// inserire decurtamento quantità annullate da
							// disposizione
							BigDecimal q1 = rigaOrdPrm.getQtaInUMRif();
							VtOrdineVendita ot = (VtOrdineVendita) rigaOrdPrm.getTestata();
							if (ot.getAnnoOrdineAdisp() != null && !ot.getAnnoOrdineAdisp().equals("")) {
								try {
									ConnectionManager.pushConnection();
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								// aggancio la riga a disporre collegata
								String keyAdisp = KeyHelper
										.buildObjectKey(new String[] { ot.getIdAzienda(), ot.getAnnoOrdineAdisp(),
												ot.getNumeroOrdAdisp(), String.valueOf(rigaOrdPrm.getNumRigaAdisp()),
												String.valueOf(rigaOrdPrm.getDetRigaAdisp()) });
								VtOrdineVenditaRigaPrm ordAdi = null;
								try {
									ordAdi = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(
											VtOrdineVenditaRigaPrm.class, keyAdisp, PersistentObject.NO_LOCK);
								} catch (SQLException e) {
									e.printStackTrace();
								}

								// Fix 80897 - Inizio
								if (ordAdi.getQtaDisposta() == null)
									ordAdi.setQtaDisposta(new BigDecimal(0));
								// Fix 80897 - Fine

								// tolgo qta disposta
								ordAdi.setQtaDisposta(ordAdi.getQtaDisposta().subtract(q1));
								// aggiungo a qta residua
								ordAdi.setQtaResidua(ordAdi.getQtaResidua().add(q1));

								int rcs = 0;
								try {
									rcs = ordAdi.save();
								} catch (SQLException e) {
									e.printStackTrace();
								}
								if (rcs > 0) {
									try {
										ConnectionManager.commit();
									} catch (SQLException e) {
										e.printStackTrace();
									}
									Trace.excStream.println("(aggiorna a disporre) Riga ordine commit: "
											+ ordAdi.getKey() + " cod.ritorno: " + rcs);
								} else {
									try {
										ConnectionManager.rollback();
									} catch (SQLException e) {
										e.printStackTrace();
									}
									Trace.excStream.println("(aggiorna a disporre) Non aggiorno riga ordine rollback: "
											+ ordAdi.getKey() + " cod.ritorno: " + rcs);
								}

								ConnectionManager.popConnection();
							}
						}

						// Fix 80668 - Inizio

						// Elimino la riserva lotto quando viene eliminata la riga
						String key = objectKeys[0];
						if (key != null) {
							VtGestioneRiserveLotti.eliminaVtRiserveLottoOrdVenRigPrm(key);
							VtTessileUtil.eliminaVtOrdVenRigPrmMatList(key);
						}

						// Fix 80668 - Fine

						super.processAction(se);
					}
					// --------------------------------------------------------------------------
					// ----------------- MODIFICA
					// -----------------------------------------------
					// --------------------------------------------------------------------------
					else if (azione.equals(UPDATE)) {
						if (modoForm != null) {
							// ----------------- UPDATE RIDOTTA
							// -----------------------------------------------
							if (modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
								String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
								String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(),
										"thObjectKeyNum");
								int objectKeyNum = 0;
								objectKeyNum = Integer.parseInt(objectKeyNumStr);
								String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
								String chiave = "";
								String chiaveTot = "";
								for (int i = 0; i < objectKeys.length; i++) {
									chiaveTot = objectKeys[i];
									chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 2) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 3) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 4);
									VtOrdineVenditaRigaPrm rigaOrdPrm = null;
									try {
										rigaOrdPrm = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(
												VtOrdineVenditaRigaPrm.class, chiave, PersistentObject.NO_LOCK);
									} catch (SQLException e) {
										e.printStackTrace();
									}

									// inserire valorizzazione di un flag per capire
									// se ci sono qta evasione/spedizione
									// in tal caso non proseguire nella modifica e
									// segnalare errore
									BigDecimal q1 = rigaOrdPrm.getQtaAttesaEvasione().getQuantitaInUMPrm();
									BigDecimal q2 = rigaOrdPrm.getQtaPropostaEvasione().getQuantitaInUMPrm();
									BigDecimal q3 = rigaOrdPrm.getQuantitaSpedita().getQuantitaInUMPrm();
									BigDecimal qtot = q1.add(q2.add(q3));
									// flag per indicare se evaso
									boolean evaso = true;
									if (qtot.compareTo(ZERO) == 0) {
										evaso = false;
									}

									// solo se trovata riga ordine
									if (rigaOrdPrm != null) {
										// se riga non passata su ordine produzione:
										// eseguo modifica
										// if(rigaOrdPrm.getRilascioOrdineProd() !=
										// '2' &&
										// !evaso) { // test per evitare modifica se
										// riga evasa
										// ricavo articolo per vedere se richiamare
										// la nuova immissione ridotte tessuti o
										// quella Standard ridotta
										Articolo art = rigaOrdPrm.getArticolo();
										char tipologia = art.getTipologiaProdotto();
										// fix 80498 - Nuovo tipo riga ordine per
										// determinare pre-ordini di tessuto per
										// prodotti finiti
										char tipoRiga = rigaOrdPrm.getTipologiaOrdine(); // P=pre-ordine,
										// C=ordine-finito,
										// -=tutti
										// gli
										// altri

										// tipologie articolo di tipo TESSUTO
										if (tipologia == 'R' || tipologia == 'J' || tipologia == 'M' || tipologia == 'S'
												|| tipologia == 'T') {
											// se non c'è settaggio è una riga
											// aperta senza gestione varianti (viene
											// aperta con gestione ridotta standard)
											if (rigaOrdPrm.getSettaggio() != null) {
												// devo verificare che tutte le
												// righe del settaggio non siano
												// passate su ordine produzione
												boolean okay = verificaStatoSettaggio(rigaOrdPrm);

												// trovate righe collegate al
												// settaggio con passaggio in
												// produzione e quindi non
												// modificabili
												if (!okay) {
													String urle = "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrMod3.jsp";
													se.sendRequest(getServletContext(), urle, false);

													// Eseguo refresh della griglia
													String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
													se.sendRequest(getServletContext(), urlr, true);
												} else {
													// fix 80598 - se la tipologia
													// riga non è 'P' (pre-ordine)
													// richiamo funzione di righe
													// tessuto
													// Fix 81239 - Sostituito la tipologia P con C if (tipoRiga != 'P')
													// {
													if (tipoRiga != 'C') {
														// per la MODIFICA righe
														// passare "Mode=UPDATE"
														// alla stessa JSP (il resto
														// come sopra)
														se.getRequest().setAttribute("operazione", "IMMISSIONE");
														// Richiamo il pannello di
														// gestione righe
														String codRagRig = "0";

														String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidotta.jsp"
																+ "?Mode=UPDATE&Key=" + URLEncoder.encode(chiaveTot)
																+ "&InitialActionAdapter=" + getClass().getName()
																+ "&CodRagRig=" + codRagRig + "&ImmMod=MOD";
														se.sendRequest(getServletContext(), url, false);
													}
													// fix 80598 - se la tipologia
													// riga è 'P' (pre-ordine)
													// richiamo funzione di righe
													// finito
													// Fix 81239 else if (tipoRiga == 'P') {
													if (tipoRiga == 'C') {
														String codRagRig = "0";
														// per la MODIFICA righe
														// passare "Mode=UPDATE"
														// alla stessa JSP (il resto
														// come sopra)
														se.getRequest().setAttribute("operazione", "IMMISSIONE");
														// Richiamo il pannello di
														// gestione righe ordini
														// finito
														String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidFin.jsp"
																+ "?Mode=UPDATE&Key=" + URLEncoder.encode(chiaveTot)
																+ "&InitialActionAdapter=" + getClass().getName()
																+ "&CodRagRig=" + codRagRig + "&ImmMod=MOD";
														se.sendRequest(getServletContext(), url, false);
													} else {
														eseguireSuperPerUpdate = true; // devo
														// eseguire
														// la
														// super
														// per
														// emettere
														// il
														// pannello
														// standard
														// ridotta
														// (update)
													}
													// Eseguo refresh della griglia
													String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
													se.sendRequest(getServletContext(), urlr, true);

												}
											} else {
												eseguireSuperPerUpdate = true; // devo
												// eseguire
												// la
												// super
												// per
												// emettere
												// il
												// pannello
												// standard
												// ridotta
												// (update)
											}
										} else {
											eseguireSuperPerUpdate = true; // devo
											// eseguire
											// la
											// super
											// per
											// emettere
											// il
											// pannello
											// standard
											// ridotta
											// (update)
										}

										// }
										// // se riga passata su ordine produzione:
										// segnalo errore per impossibile modifica
										// else
										// if(rigaOrdPrm.getRilascioOrdineProd() ==
										// '2') {
										// String urle =
										// "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrMod1.jsp";
										// se.sendRequest(getServletContext(), urle,
										// false);
										//
										// // Eseguo refresh della griglia
										// String urlr =
										// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
										// se.sendRequest(getServletContext(), urlr,
										// true);
										// }
										//
										// // se riga evasa: segnalo errore per
										// impossibile modifica
										// else if(evaso) {
										// String urle =
										// "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrMod2.jsp";
										// se.sendRequest(getServletContext(), urle,
										// false);
										//
										// // Eseguo refresh della griglia
										// String urlr =
										// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
										// se.sendRequest(getServletContext(), urlr,
										// true);
										// }
									}
								}
								// se non è un TESSUTO devo emettere il pannello
								// standard riga ridotta
								if (eseguireSuperPerUpdate) {

									// super.processAction(se);
									// per la MODIFICA righe passare "Mode=UPDATE"
									// alla stessa JSP (il resto come sopra)
									se.getRequest().setAttribute("operazione", "IMMISSIONE");
									// Richiamo il pannello di gestione righe

									String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidottaStd.jsp"
											+ "?Mode=UPDATE&Key=" + URLEncoder.encode(chiaveTot)
											+ "&InitialActionAdapter=" + getClass().getName() + "&CodRagRig=" + "0"
											+ "&ImmMod=MOD";
									se.sendRequest(getServletContext(), url, false);
								}

								// Eseguo refresh della griglia
								// String urlr =
								// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
								String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigRefreshGrid.jsp";
								se.sendRequest(getServletContext(), urlr, true);
							}
							// ----------------- UPDATE COMPLETA
							// -----------------------------------------------
							// Implementazione per revisione riga FINITO completa
							else if (modoForm.equals(DocumentoNavigazioneWeb.MF_COMPLETA)) {
								// String chiaveDatisessione =
								// se.getRequest().getParameter("thChiaveDatiSessione");
								// String objectKeyNumStr =
								// BaseServlet.getStringParameter(se.getRequest(),
								// "thObjectKeyNum");
								// int objectKeyNum = 0;
								// objectKeyNum = Integer.parseInt(objectKeyNumStr);
								String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
								String chiave = "";
								String chiaveTot = "";
								for (int i = 0; i < objectKeys.length; i++) {
									chiaveTot = objectKeys[i];
									chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 2) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 3) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 4);
									VtOrdineVenditaRigaPrm rigaOrdPrm = null;
									try {
										rigaOrdPrm = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(
												VtOrdineVenditaRigaPrm.class, chiave, PersistentObject.NO_LOCK);
									} catch (SQLException e) {
										e.printStackTrace();
									}

									// inserire valorizzazione di un flag per capire
									// se ci sono qta evasione/spedizione
									// in tal caso non proseguire nella modifica e
									// segnalare errore
									// Cristi - controllo sul null
									if (rigaOrdPrm != null) {
										BigDecimal q1 = rigaOrdPrm.getQtaAttesaEvasione().getQuantitaInUMPrm();
										BigDecimal q2 = rigaOrdPrm.getQtaPropostaEvasione().getQuantitaInUMPrm();
										BigDecimal q3 = rigaOrdPrm.getQuantitaSpedita().getQuantitaInUMPrm();
										BigDecimal qtot = q1.add(q2.add(q3));
										// flag per indicare se evaso
										boolean evaso = true;
										if (qtot.compareTo(ZERO) == 0) {
											evaso = false;
										}
									}

									// CONTROLLO VALIDITA' RIGA
									// ------------------------------------------------
									// solo se trovata riga ordine
									if (rigaOrdPrm != null) {
										// se riga non passata su ordine produzione:
										// eseguo modifica
										// if(rigaOrdPrm.getRilascioOrdineProd() !=
										// '2' &&
										// !evaso) { // test per evitare modifica se
										// riga evasa
										// ricavo articolo per vedere se richiamare
										// la nuova gestione completa FINITO o
										// quella Standard completa
										Articolo art = rigaOrdPrm.getArticolo();
										char tipologia = art.getTipologiaProdotto();
										// fix 80498 - Nuovo tipo riga ordine per
										// determinare pre-ordini di tessuto per
										// prodotti finiti
										char tipoRiga = rigaOrdPrm.getTipologiaOrdine(); // P=pre-ordine,
										// C=ordine-finito,
										// -=tutti
										// gli
										// altri

										// tipologie articolo di tipo TESSUTO
										if (tipologia == 'R' || tipologia == 'J' || tipologia == 'M' || tipologia == 'S'
												|| tipologia == 'T') {

											// devo verificare che tutte le righe
											// del settaggio non siano passate su
											// ordine produzione
											boolean okay = verificaStatoSettaggio(rigaOrdPrm);

											// trovate righe collegate al settaggio
											// con passaggio in produzione e quindi
											// non modificabili
											if (!okay) {
												String urle = "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrMod3.jsp";
												se.sendRequest(getServletContext(), urle, false);

												// Eseguo refresh della griglia
												String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
												se.sendRequest(getServletContext(), urlr, true);
											} else {
												// SOLO FINITO
												// --------------------------------------------------------------------------
												// se la tipologia riga è 'P'
												// (pre-ordine) richiamo funzione di
												// righe finito
												// if(tipoRiga == 'P') {
												// String codRagRig = "0";
												// // per la MODIFICA righe passare
												// "Mode=UPDATE" alla stessa JSP (il
												// resto come sopra)
												// se.getRequest().setAttribute("operazione",
												// "IMMISSIONE");
												// // Richiamo il pannello di
												// gestione righe ordini finito
												// String url =
												// "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmCompFin.jsp"
												// +
												// "?Mode=UPDATE&Key=" +
												// URLEncoder.encode(chiaveTot) +
												// "&InitialActionAdapter=" +
												// getClass().getName() +
												// "&CodRagRig=" + codRagRig +
												// "&ImmMod=MOD";
												// se.sendRequest(getServletContext(),
												// url, false);
												// }
												// else {
												eseguireSuperPerUpdate = true; // devo
												// eseguire
												// la
												// super
												// per
												// emettere
												// il
												// pannello
												// standard
												// ridotta
												// (update)
												// }
												// Eseguo refresh della griglia
												String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
												se.sendRequest(getServletContext(), urlr, true);

											}
										} else {
											eseguireSuperPerUpdate = true; // devo
											// eseguire
											// la
											// super
											// per
											// emettere
											// il
											// pannello
											// standard
											// ridotta
											// (update)
										}

										// }
										// // se riga passata su ordine produzione:
										// segnalo errore per impossibile modifica
										// else
										// if(rigaOrdPrm.getRilascioOrdineProd() ==
										// '2') {
										// String urle =
										// "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrMod1.jsp";
										// se.sendRequest(getServletContext(), urle,
										// false);
										//
										// // Eseguo refresh della griglia
										// String urlr =
										// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
										// se.sendRequest(getServletContext(), urlr,
										// true);
										// }
										//
										// // se riga evasa: segnalo errore per
										// impossibile modifica
										// else if(evaso) {
										// String urle =
										// "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrMod2.jsp";
										// se.sendRequest(getServletContext(), urle,
										// false);
										//
										// // Eseguo refresh della griglia
										// String urlr =
										// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
										// se.sendRequest(getServletContext(), urlr,
										// true);
										// }
									}
								}
								// se non è un TESSUTO devo emettere il pannello
								// standard riga COMPLETA -----------------
								if (eseguireSuperPerUpdate) {
									super.processAction(se);
								}

								// Eseguo refresh della griglia
								// String urlr =
								// "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
								String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigRefreshGrid.jsp";
								se.sendRequest(getServletContext(), urlr, true);
							} else {
								super.processAction(se);
							}
						} else {
							// Fix 81091
							/*
							 * Controllo se l'ordine viene salvato con stato = Definitivo. In questo caso
							 * eseguo le operazioni per la conferma del pre-ordine modello in quanto viene
							 * abolito il tasto apposito e deve essere eseguito direttamente al salvataggio
							 * in definitivo -Aggancio prima il vecchio record e controllo che sia
							 * effettivamente la prima volta che viene salvato in stato definitivo per
							 * evitare di eseguire più volte lo stesso passaggio
							 */
							String objectKeys[] = se.getRequest().getParameterValues("ObjectKey");
							if (objectKeys.length > 0) {
								String chiaveOrdine = objectKeys[0];
								VtOrdineVenditaRigaPrm ordineOld = null;
								try {
									ordineOld = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(
											VtOrdineVenditaRigaPrm.class, chiaveOrdine, VtOrdineVenditaRigaPrm.NO_LOCK);
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (ordineOld != null && ordineOld.getStatoAvanzamento() != '2') {

								}

							}

							super.processAction(se);
						}
					}
					// ---------------- VISUALIZZA
					// ------------------------------------
					else if (azione.equals(SHOW)) {
						if (modoForm != null) {
							if (modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
								String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
								String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(),
										"thObjectKeyNum");
								int objectKeyNum = 0;
								objectKeyNum = Integer.parseInt(objectKeyNumStr);
								String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
								String chiave = "";
								String chiaveTot = "";
								for (int i = 0; i < objectKeys.length; i++) {
									chiaveTot = objectKeys[i];
									chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 2) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 3) + PersistentObject.KEY_SEPARATOR
											+ KeyHelper.getTokenObjectKey(chiaveTot, 4);

									// per la MODIFICA righe passare "Mode=SHOW"
									// alla stessa JSP (il resto come sopra)
									se.getRequest().setAttribute("operazione", "IMMISSIONE");
									// Richiamo il pannello di gestione righe
									String codRagRig = "0";

									String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidotta.jsp"
											+ "?Mode=SHOW&Key=" + URLEncoder.encode(chiaveTot)
											+ "&InitialActionAdapter=" + getClass().getName() + "&CodRagRig="
											+ codRagRig + "&ImmMod=MOD";
									se.sendRequest(getServletContext(), url, false);

								}
								// Eseguo refresh della griglia
								String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
								se.sendRequest(getServletContext(), urlr, true);
							} else {
								super.processAction(se);
							}
						} else {
							super.processAction(se);
						}
					}
					// ----------------- COPIA ----------------------
					else if (azione.equals(COPY)) {
						// Fix PROVA
						/*
						 * if (modoForm != null) {
						 * 
						 * //da mettere
						 * datiSessione.getModoForm().equals(DocumentoNavigazioneWeb.MF_COMPLETA)
						 * DocumentoDatiSessione datiSessione = getDatiSessione(se);
						 * datiSessione.setModoForm(DocumentoNavigazioneWeb.MF_COMPLETA);
						 * datiSessione.salvaInSessione(se); modoForm =
						 * DocumentoNavigazioneWeb.MF_COMPLETA; //
						 * 
						 * 
						 * if (modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) { String
						 * chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
						 * String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(),
						 * "thObjectKeyNum"); int objectKeyNum = 0; objectKeyNum =
						 * Integer.parseInt(objectKeyNumStr); String[] objectKeys =
						 * se.getRequest().getParameterValues(OBJECT_KEY); String chiave = ""; String
						 * chiaveTot = ""; for (int i = 0; i < objectKeys.length; i++) { chiaveTot =
						 * objectKeys[i]; chiave = KeyHelper.getTokenObjectKey(chiaveTot, 1) +
						 * PersistentObject.KEY_SEPARATOR + KeyHelper.getTokenObjectKey(chiaveTot, 2) +
						 * PersistentObject.KEY_SEPARATOR + KeyHelper.getTokenObjectKey(chiaveTot, 3) +
						 * PersistentObject.KEY_SEPARATOR + KeyHelper.getTokenObjectKey(chiaveTot, 4);
						 * OrdineVenditaRigaPrm rigaOrdPrm = null; try {
						 * 
						 * rigaOrdPrm = (OrdineVenditaRigaPrm) PersistentObject.elementWithKey(
						 * OrdineVenditaRigaPrm.class, chiave, PersistentObject.NO_LOCK); } catch
						 * (SQLException e) { e.printStackTrace(); } // solo se trovata riga ordine if
						 * (rigaOrdPrm != null) {
						 * 
						 * // per la MODIFICA righe passare // "Mode=UPDATE" alla stessa JSP di //
						 * SGestioneRighe (il resto come sopra)
						 * se.getRequest().setAttribute("operazione", "IMMISSIONE"); // Richiamo il
						 * pannello di gestione righe String codRagRig = "0";
						 * 
						 * // Fix 81106 //String url =
						 * "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidotta.jsp" // +
						 * "?Mode=NEW&Key=" + URLEncoder.encode(chiaveTot) + "&InitialActionAdapter=" //
						 * + getClass().getName() + "&CodRagRig=" + codRagRig + "&ImmMod=COP"; // fix
						 * 81106
						 * 
						 * String url =
						 * "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidotta.jsp" +
						 * "?Mode=COPY&Key=" + URLEncoder.encode(chiaveTot) + "&InitialActionAdapter=" +
						 * getClass().getName() + "&CodRagRig=" + codRagRig + "&ImmMod=IMM";
						 * 
						 * se.sendRequest(getServletContext(), url, false);
						 * 
						 * } } // Eseguo refresh della griglia String urlr =
						 * "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
						 * se.sendRequest(getServletContext(), urlr, true); } else { String[] objectKeys
						 * = se.getRequest().getParameterValues(OBJECT_KEY); OrdineVenditaRigaPrm
						 * rigaOrdPrm = null; try { rigaOrdPrm = (OrdineVenditaRigaPrm)
						 * PersistentObject.elementWithKey( OrdineVenditaRigaPrm.class, objectKeys[0],
						 * PersistentObject.NO_LOCK); } catch (SQLException e) { e.printStackTrace(); }
						 * 
						 * super.processAction(se); } } else { super.processAction(se); }
						 */
						// Fine fix prova

						String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
						VtOrdineVenditaRigaPrm rigaOrdPrm = null;
						try {
							rigaOrdPrm = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(
									OrdineVenditaRigaPrm.class, objectKeys[0], PersistentObject.NO_LOCK);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						if (rigaOrdPrm.getTipologiaOrdine() != 'C')
							super.processAction(se);
						else {
							// Non si può effettuare la copia
							ServletOutputStream out = se.getResponse().getOutputStream();
							out.println("<script language='JavaScript1.2'>");
							out.println("parent.alert(\"Non si puo copiare questa tipologia di ordine\");");
							out.println("parent.window.close();");
							out.println("</script>");

						}

					}

					// ---------- altre operazioni ------------------------------
					else {

						super.processAction(se);
					}

				}

				// Fix 80983 - Inizio
			} else {
				super.processAction(se);
			}

			// Fix 80983 - Fine

		}
	}

	/**
	 * Verifico che nessuna riga del settaggio abbia la quantità in produzione
	 * diversa da ZERO >>> se almeno una riga con qta in produzione, blocco le
	 * modifiche con l'opzione "veloce per variante"
	 * 
	 * @param rig
	 * @return
	 */
	public boolean verificaStatoSettaggio(OrdineVenditaRigaPrm rig) {

		VtOrdineVenditaRigaPrm vtRig = null;
		String keyR = rig.getKey();
		try {
			vtRig = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, keyR,
					PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String where = VtOrdineVenditaRigaPrmTM.ID_AZIENDA + "='" + rig.getIdAzienda() + "' AND "
				+ VtOrdineVenditaRigaPrmTM.ID_ANNO_ORD + "= '" + rig.getAnnoDocumento() + "' AND "
				+ VtOrdineVenditaRigaPrmTM.ID_NUMERO_ORD + "= '" + rig.getNumeroDocumento() + "'";

		cInstance = (VtOrdineVenditaRigaPrm) Factory.createObject(VtOrdineVenditaRigaPrm.class);

		// Reperisco righe ordine (di quell'ordine )
		Vector righeOrd = null;
		try {
			righeOrd = PersistentObject.retrieveList(cInstance, where, "", true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (righeOrd != null) {
			Iterator iter = righeOrd.iterator();
			while (iter.hasNext()) {
				VtOrdineVenditaRigaPrm rigaOrd = (VtOrdineVenditaRigaPrm) iter.next();

				if (rigaOrd != null) {

					// Leggo solo righe dello stesso settaggio e della stessa
					// radice prodotto (fino a variante)
					if (vtRig.getSettaggio() != null && rigaOrd.getSettaggio() != null) {
						Integer set1 = vtRig.getSettaggio();
						Integer set2 = rigaOrd.getSettaggio();
						if (set1.equals(set2)) {

							// test per capire se ci sono qta
							// evasione/spedizione
							// in tal caso non proseguire nella modifica e
							// segnalare errore
							BigDecimal q1 = rigaOrd.getQtaAttesaEvasione().getQuantitaInUMPrm();
							BigDecimal q2 = rigaOrd.getQtaPropostaEvasione().getQuantitaInUMPrm();
							BigDecimal q3 = rigaOrd.getQuantitaSpedita().getQuantitaInUMPrm();
							BigDecimal qtot = q1.add(q2.add(q3));
							// flag per indicare se evaso
							boolean evaso = true;
							if (qtot.compareTo(ZERO) == 0) {
								// evaso = false;
							}
							// se riga non passata su ordine produzione: eseguo
							// modifica
							if (rigaOrd.getRilascioOrdineProd() == '2' || evaso) { // test
								// per
								// evitare
								// modifica
								// se
								// riga
								// evasa
								// return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Eseguo controllo sugli schemi articolo modello e articolo tessuto
	 * 
	 * @param rig
	 * @throws SQLException
	 * 
	 *                      Ritorna: C = la somme schemi prodotto MOD+TEX non
	 *                      corrisponde al nuovo schema prodotto, E = non trovato
	 *                      abbinamento schemi su tabella
	 */
	public String controllaSchemiArticoli(Articolo artTex, Articolo artFin, String idAzienda) throws SQLException {

		String idProd = null; // nuovo ID PRODOTTO
		Articolo artNew = null; // nuovo articolo restituito

		// ricavo gli schemi prodotto tessuto/finito
		String schemaTex = artTex.getIdSchemaProdotto();
		String schemaFin = artFin.getIdSchemaProdotto();

		String keyAbb = KeyHelper.buildObjectKey(new String[] { idAzienda, schemaFin, schemaTex });
		VtSchemiAbbinati sa = null;
		try {
			sa = VtSchemiAbbinati.elementWithKey(keyAbb, PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (sa != null) {

			char tipo = 'P';
			// Schema prodotto FINITO
			String keyStf = KeyHelper.buildObjectKey(new String[] { idAzienda, String.valueOf(tipo), schemaFin });
			SchemaProdottoLotto sfi = null;
			try {
				sfi = SchemaProdottoLotto.elementWithKey(keyStf, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Schema prodotto TESSUTO
			String keyStt = KeyHelper.buildObjectKey(new String[] { idAzienda, String.valueOf(tipo), schemaTex });
			SchemaProdottoLotto ste = null;
			try {
				ste = SchemaProdottoLotto.elementWithKey(keyStt, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			// Schema prodotto CONFEZIONE
			String keyStc = KeyHelper
					.buildObjectKey(new String[] { idAzienda, String.valueOf(tipo), sa.getIdScheCmb() });
			SchemaProdottoLotto scf = null;
			try {
				scf = SchemaProdottoLotto.elementWithKey(keyStc, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List chiaF = new ArrayList(); // chiavi schema finito
			List chiaT = new ArrayList(); // chiavi schema tessuto
			List chiaC = new ArrayList(); // chiavi schema CONFEZIONE
			// (risultante finito + tessuto)
			List chiaX = new ArrayList(); // chiavi finito + tessuto (per
			// controllo)

			List nomeF = new ArrayList();
			List nomeT = new ArrayList();
			List nomeC = new ArrayList();
			List nomeX = new ArrayList();

			List valoF = new ArrayList();
			List valoT = new ArrayList();
			List valoC = new ArrayList();
			List valoX = new ArrayList();

			List indcC = new ArrayList(); // indice della chiave
			List posiC = new ArrayList(); // posizione della chiave

			chiaF = sfi.getChiaviProdotto();
			chiaT = ste.getChiaviProdotto();
			chiaC = scf.getChiaviProdotto();

			// Ciclo caricamento chiavi del Finito (su list di confronto)
			Iterator iteF = chiaF.iterator();
			while (iteF.hasNext()) {
				ChiaveProdotto chia = (ChiaveProdotto) iteF.next();
				nomeF.add(chia.getIdChiaveProdotto());
				valoF.add(VtOrdineVenditaRigaPrmRidCnfFormModifier.ricavaValoreChiave(chia, artFin));
				chiaX.add(chia);
				nomeX.add(chia.getIdChiaveProdotto());
				valoX.add(VtOrdineVenditaRigaPrmRidCnfFormModifier.ricavaValoreChiave(chia, artFin));
			}
			// Ciclo caricamento chiavi del Tessuto (su list di confronto)
			Iterator iteT = chiaT.iterator();
			while (iteT.hasNext()) {
				ChiaveProdotto chia = (ChiaveProdotto) iteT.next();
				nomeT.add(chia.getIdChiaveProdotto());
				valoT.add(VtOrdineVenditaRigaPrmRidCnfFormModifier.ricavaValoreChiave(chia, artTex));
				chiaX.add(chia);
				nomeX.add(chia.getIdChiaveProdotto());
				valoX.add(VtOrdineVenditaRigaPrmRidCnfFormModifier.ricavaValoreChiave(chia, artTex));
			}

			// Ciclo caricamento chiavi ABBINATE (schema abbinato)
			List chiaCC = new ArrayList();
			Iterator iteC = chiaC.iterator();
			while (iteC.hasNext()) {
				ChiaveProdotto chia = (ChiaveProdotto) iteC.next();
				nomeC.add(chia.getIdChiaveProdotto());
				indcC.add(String.valueOf(chia.getCampoChiave()));
			}

			// se almeno una chiave prodotto dello schema "Confezione" non
			// esiste nel totale chiavi schema Modello + Tessuto
			// allora segnalo incongruenza

			// ATTENZIONE !!! il controllo di corrispondenza non serve, vengono
			// copiate solo le chiavi corrispondenti tra i due schemi abbinati e
			// il risultante (anche se fosse solo una)
			// Iterator iteXX = nomeC.iterator();
			// while (iteXX.hasNext()) {
			// // nome della chiave schema "Confezione"
			// String nome = (String)iteXX.next();
			// // se il nome della chiave schema "Confezione" non esiste tra le
			// chiavi schema Modello+Tessuto: segnalo errore
			// if(!nomeX.contains(nome)) {
			// return "C";
			// }
			// }

			// non serve il controllo al contrario: infatti possono esserci
			// chiavi presenti nei 2 schemi abbinati che non esistono sullo
			// schema risultante (l'importante è il contrario)
			/*
			 * if(!nomeC.equals(nomeX)) { return "C"; }
			 */
		} else {
			return "E";
		}

		return null;
	}

	/**
	 * Formattazione valore chiave schema prodotto
	 * 
	 * @param lunghezza
	 * @param valore
	 * @return
	 */
	public String formattaValore(int lunghezza, String valore) {
		String valoreFormattato = valore;
		if (valoreFormattato == null) {
			valoreFormattato = "";
		}
		if (valoreFormattato.length() > lunghezza) {
			valoreFormattato = valoreFormattato.substring(0, lunghezza);
		}
		while (valoreFormattato.length() < lunghezza) {
			valoreFormattato += " ";
		}
		return valoreFormattato;
	}

	/**
	 * carica griglia righe ordini a disporre per l'ordine a disporre scelto
	 * (ritorna FALSE se non ha ordini a disporre collegati)
	 * 
	 * @param se
	 * @param tesO
	 * @throws IOException
	 * @throws ServletException
	 */
	public boolean grigliaOrdiniADisporre(ServletEnvironment se, VtOrdineVendita tesO, String docKey)
			throws ServletException, IOException {

		if (tesO.getAnnoOrdineAdisp() == null) {
			return false;
		}
		if (tesO.getAnnoOrdineAdisp().equals("")) {
			return false;
		}

		String np = se.getRequest().getParameter(NEW_PAGE);
		String cn = se.getRequest().getParameter(CLASS_NAME);
		String sk = se.getRequest().getParameter(TH_SETTING_KEY);
		String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigAdispGriglia.jsp" + "?Mode=NEW&Key="
				+ URLEncoder.encode(docKey) + "&InitialActionAdapter=" + getClass().getName() + "&IdAzienda="
				+ tesO.getIdAzienda() + "&AnnoOrdAdisp=" + tesO.getAnnoOrdineAdisp() + "&NumOrdAdisp="
				+ tesO.getNumeroOrdAdisp() + "";
		se.sendRequest(getServletContext(), url, false);

		// se caricata la griglia righe a disporre ritorno TRUE
		return true;

		// annullate tutte le seguenti specifiche
		/*
		 * //String url = se.getServletPath() +
		 * "com.thera.thermfw.web.servlet.Execute?Page=" + np + "&ClassName=" + cn +
		 * "&SettingKey=" + sk; String url = ""; //Inserisco la mia showGrid al posto di
		 * quella standard //url +=
		 * "&ShowGridName=it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigAdispGrid";
		 * url += se.getServletPath() +
		 * "it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigAdispShowGrid";
		 * //---------- String restrictConditionsParam = "IdAzienda=" +
		 * tesO.getIdAzienda() + ";AnnoOrdine=" + tesO.getAnnoOrdineAdisp() +
		 * ";NumeroOrdine=" + tesO.getNumeroOrdAdisp() + ""; //restrictConditionsParam =
		 * "&thRestrictConditions=" +
		 * java.net.URLEncoder.encode(restrictConditionsParam); restrictConditionsParam
		 * = "&thRestrictConditions=" + restrictConditionsParam; //url +=
		 * "?ClassName=VtOrdineVenditaRigaPrm" + restrictConditionsParam; url +=
		 * "?ClassName=VtOrdVenRigAdispGrid" + restrictConditionsParam;
		 * 
		 * // inserisco il riferimento numero ordine url += "&AnnoNewOrd=" +
		 * tesO.getAnnoDocumento() + "&NumeroNewOrd=" + tesO.getNumeroDocumento();
		 * 
		 * 
		 * String sg = se.getRequest().getParameter(TH_SHOW_GRID_NAME); if (sg != null
		 * && !sg.equals("")) url += "&ShowGridName=" + sg;
		 * 
		 * 
		 * String navigationMapName =
		 * se.getRequest().getParameter(NavigationMapServlet.NAV_MAP_NAME_PARAM) ;
		 * String navigationMapBoxName =
		 * se.getRequest().getParameter(NavigationMapServlet. NAV_MAP_BOX_NAME_PARAM);
		 * String navigationMapUserKey =
		 * se.getRequest().getParameter(NavigationMapServlet.
		 * NAV_MAP_BOX_USER_KEY_PARAM);
		 * 
		 * if (navigationMapName != null){ url += "&" +
		 * NavigationMapServlet.NAV_MAP_NAME_PARAM + "=" + navigationMapName; } if
		 * (navigationMapBoxName != null){ url += "&" +
		 * NavigationMapServlet.NAV_MAP_BOX_NAME_PARAM + "=" + navigationMapBoxName; }
		 * if (navigationMapUserKey != null){ url += "&" +
		 * NavigationMapServlet.NAV_MAP_BOX_USER_KEY_PARAM + "=" + navigationMapUserKey;
		 * }
		 * 
		 * 
		 * url = Utils.replace(url, "&", "$");
		 * 
		 * // imposto il numero ordine a disporre String idOrdine =
		 * tesO.getAnnoOrdineAdisp() + " - " + tesO.getNumeroOrdAdisp();
		 * 
		 * String uuu = se.getServletPath() +
		 * "it.thera.thip.tessile.vendite.ordineVE.servlet.VtOrdVenRigAdispSetSetting?";
		 * uuu += "IdOrdine=" + idOrdine + "&UrlDest=" + url + "&Specific=" + "";
		 * 
		 * se.sendRequest(getServletContext(), uuu , false);
		 * 
		 * 
		 * 
		 * // String newServ =
		 * "it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigAdispGrid"; // String
		 * param = "ClassName=VtOrdineVenditaRigaPrm"; // String restrictConditionsParam
		 * = "IdAzienda=" + tesO.getIdAzienda() + ";AnnoOrdine=" +
		 * tesO.getAnnoOrdineAdisp() + ";NumeroOrdine=" // + tesO.getNumeroOrdAdisp() +
		 * ""; // //restrictConditionsParam = "thRestrictConditions=" +
		 * java.net.URLEncoder.encode(restrictConditionsParam); // String tipoGrid =
		 * "thGridType=List"; // String url = se.getServletPath() + newServ + "?" +
		 * param + "&" + tipoGrid + "&" + restrictConditionsParam; //
		 * se.sendRequest(getServletContext(), url, false);
		 */
	}

	/**
	 * Esegue apertura nuovo tessuto (modalità ridotta)
	 * 
	 * @param se
	 * @param newKeyAdisp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void eseguiNuovoTex(ServletEnvironment se, String newKeyAdisp) throws ServletException, IOException {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String selectedKey = getSelectedKeyForAction(se.getRequest());
		String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		String modoFormIni = datiSessione.getModoForm();
		String modoFormPrec = cercaModoForm(se, "OrdineVenditaRigaPrm");
		// modo per individuare se COMPLETA o RIDOTTA

		DocOrdNavigazioneWeb navigatore = (DocOrdNavigazioneWeb) datiSessione.getNavigatore();
		String ret = navigatore.getJspRigaPrmCompleta();

		// al momento la nuova immissione veloce avviene sia per ridotta che per
		// completa
		// if(modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
		String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
		String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(), "thObjectKeyNum");
		int objectKeyNum = 0;
		objectKeyNum = Integer.parseInt(objectKeyNumStr);
		String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
		String chiave = "";
		String chiaveTot = "";
		// cercare la riga "chiave" e leggere tutte le righe di quel gruppo
		// poi valorizzare sui nuovi attributi i dati delle righe per il caso di
		// manutenzione con il comando qui sotto (attributo con nomi nuovi)
		// poi su ONLOAD della JS spostare i nuovi attributi su quelli a video
		se.getRequest().setAttribute("operazione", "IMMISSIONE");
		// Richiamo il pannello di gestione righe
		String codRagRig = "0";

		String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidotta.jsp" + "?Mode=NEW&Key="
				+ URLEncoder.encode(docKey) + "&InitialActionAdapter=" + getClass().getName() + "&CodRagRig="
				+ codRagRig + "&ImmMod=IMM" + "&NewKeyAdisp=" + newKeyAdisp;
		se.sendRequest(getServletContext(), url, false);

		// Eseguo refresh della griglia
		String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
		se.sendRequest(getServletContext(), urlr, true);

	}

	// Fix 81750 >
	public void eseguiNuovoComp(ServletEnvironment se, String newKeyAdisp) throws ServletException, IOException {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String selectedKey = getSelectedKeyForAction(se.getRequest());
		String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		String modoFormIni = datiSessione.getModoForm();
		String modoFormPrec = cercaModoForm(se, "OrdineVenditaRigaPrm");
		// modo per individuare se COMPLETA o RIDOTTA

		DocOrdNavigazioneWeb navigatore = (DocOrdNavigazioneWeb) datiSessione.getNavigatore();
		String ret = navigatore.getJspRigaPrmCompleta();

		// al momento la nuova immissione veloce avviene sia per ridotta che per
		// completa
		// if(modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
		String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
		String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(), "thObjectKeyNum");
		int objectKeyNum = 0;
		objectKeyNum = Integer.parseInt(objectKeyNumStr);
		String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
		String chiave = "";
		String chiaveTot = "";
		// cercare la riga "chiave" e leggere tutte le righe di quel gruppo
		// poi valorizzare sui nuovi attributi i dati delle righe per il caso di
		// manutenzione con il comando qui sotto (attributo con nomi nuovi)
		// poi su ONLOAD della JS spostare i nuovi attributi su quelli a video
		se.getRequest().setAttribute("operazione", "IMMISSIONE");
		// Richiamo il pannello di gestione righe
		String codRagRig = "0";

		String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmCompleta.jsp" + "?Mode=NEW&Key="
				+ URLEncoder.encode(docKey) + "&InitialActionAdapter=" + getClass().getName() + "&CodRagRig="
				+ codRagRig + "&ImmMod=IMM" + "&NewKeyAdisp=" + newKeyAdisp;
		se.sendRequest(getServletContext(), url, false);

		// Eseguo refresh della griglia
		String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
		se.sendRequest(getServletContext(), urlr, true);

	}
	// Fix 81750 <

	/**
	 * Esegue apertura nuova riga ridotta standard (personalizzata per tessuti)
	 * 
	 * @param se
	 * @param newKeyAdisp
	 * @throws ServletException
	 * @throws IOException
	 */
	public void eseguiNuovoRidStd(ServletEnvironment se, String newKeyAdisp) throws ServletException, IOException {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String selectedKey = getSelectedKeyForAction(se.getRequest());
		String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		String modoFormIni = datiSessione.getModoForm();
		String modoFormPrec = cercaModoForm(se, "OrdineVenditaRigaPrm");
		// modo per individuare se COMPLETA o RIDOTTA

		DocOrdNavigazioneWeb navigatore = (DocOrdNavigazioneWeb) datiSessione.getNavigatore();
		String ret = navigatore.getJspRigaPrmCompleta();

		// al momento la nuova immissione veloce avviene sia per ridotta che per
		// completa
		// if(modoForm.equals(DocumentoNavigazioneWeb.MF_RIDOTTA)) {
		String chiaveDatisessione = se.getRequest().getParameter("thChiaveDatiSessione");
		String objectKeyNumStr = BaseServlet.getStringParameter(se.getRequest(), "thObjectKeyNum");
		int objectKeyNum = 0;
		objectKeyNum = Integer.parseInt(objectKeyNumStr);
		String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
		String chiave = "";
		String chiaveTot = "";
		// cercare la riga "chiave" e leggere tutte le righe di quel gruppo
		// poi valorizzare sui nuovi attributi i dati delle righe per il caso di
		// manutenzione con il comando qui sotto (attributo con nomi nuovi)
		// poi su ONLOAD della JS spostare i nuovi attributi su quelli a video
		se.getRequest().setAttribute("operazione", "IMMISSIONE");
		// Richiamo il pannello di gestione righe
		String codRagRig = "0";

		String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidottaStd.jsp" + "?Mode=NEW&Key="
				+ URLEncoder.encode(docKey) + "&InitialActionAdapter=" + getClass().getName() + "&CodRagRig="
				+ codRagRig + "&ImmMod=IMM" + "&NewKeyAdisp=" + newKeyAdisp;
		se.sendRequest(getServletContext(), url, false);

		// Eseguo refresh della griglia
		String urlr = "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdineRigheEnableGridRefresh.jsp";
		se.sendRequest(getServletContext(), urlr, true);

	}

	// Fix 80970 - Inizio
	public boolean grigliaEditOrdiniADisporre(ServletEnvironment se, VtOrdineVendita tesO, String docKey)
			throws ServletException, IOException {

		if (tesO.getAnnoOrdineAdisp() == null) {
			return false;
		}
		if (tesO.getAnnoOrdineAdisp().equals("")) {
			return false;
		}

		String np = se.getRequest().getParameter(NEW_PAGE);
		String cn = se.getRequest().getParameter(CLASS_NAME);
		String sk = se.getRequest().getParameter(TH_SETTING_KEY);

		String url = "/it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigAdispGrigliaTes.jsp" + "?Mode=NEW&Key="
				+ URLEncoder.encode(docKey) + "&InitialActionAdapter=" + getClass().getName() + "&IdAzienda="
				+ tesO.getIdAzienda() + "&AnnoOrdAdisp=" + tesO.getAnnoOrdineAdisp() + "&NumOrdAdisp="
				+ tesO.getNumeroOrdAdisp() + "";
		se.sendRequest(getServletContext(), url, false);

		// se caricata la griglia righe a disporre ritorno TRUE
		return true;

	}
	// Fix 80970 - Fine

	// Fix 81032 - Inizio

	public static boolean controllaStatoRilascio(String[] chiaviOrdVen) {
		boolean ok = true;

		if (chiaviOrdVen != null) {

			for (int i = 0; i < chiaviOrdVen.length; i++) {

				String key = (String) chiaviOrdVen[i];

				VtOrdineVenditaRigaPrm ordVenRig = VtTessileUtil.getVtOrdineVenditaRigaPrmByKey(key);

				if (ordVenRig != null) {
					if (ordVenRig.getRilascioOrdineAcq() == '3' || ordVenRig.getRilascioOrdineLavEsterna() == '3') {
						ok = false;
						break;
					}
				}
			}
		}

		return ok;
	}

	// Ridefinizione eliminazione: non consente di eliminare righe collegate a dispo
	protected void deleteObject(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {

		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");

		if (chiaveOrdine != null) {

			List righe = controllaEsistenzaOrdEsec(chiaveOrdine);

			if (righe.isEmpty()) {
				se.sendRequest(getServletContext(), getCurrentViewSelector().getDeleteObjectURL(cadc, se), false);
			} else {
				String messaggio = "";
				for (int i = 0; i < righe.size(); i++) {
					String riga = (String) righe.get(i);
					if (messaggio.equals(""))
						messaggio = riga;
					else
						messaggio += ", " + riga;
				}

				messaggio = "Impossibile eliminare. Dispo già rilasciate per le righe: " + messaggio;

				ServletOutputStream out = se.getResponse().getOutputStream();
				out.println("<script language='JavaScript1.2'>");
				out.println("parent.alert(\"" + messaggio + "\");");
				out.println("parent.gridActionsDisabled = false; ");
				out.println("</script>");
			}
		}

	}

	public List controllaEsistenzaOrdEsec(String[] chiaviOrdVen) {
		List righe = new ArrayList();

		if (chiaviOrdVen != null) {

			for (int i = 0; i < chiaviOrdVen.length; i++) {

				String key = (String) chiaviOrdVen[i];

				if (existOrdEsec(key) || existOrdAcq(key)) {
					String[] chiave = KeyHelper.unpackObjectKey(key);
					righe.add(chiave[3]);
				}

			}
		}

		return righe;
	}

	public static boolean existOrdEsec(String chiaveOrdVen) {
		boolean exist = false;

		String[] chiave = KeyHelper.unpackObjectKey(chiaveOrdVen);

		if (chiave != null) {

			String select = "SELECT ID_NUMERO_ORD " + " FROM THIP.VT_ORDESE_ATV_PRD " + " WHERE ID_AZIENDA = '"
					+ chiave[0] + "' " + " AND R_ANNO_ORD_CLI = '" + chiave[1] + "' " + " AND R_NUMERO_ORD_CLI = '"
					+ chiave[2] + "' " + " AND R_RIGA_ORD_CLI = " + chiave[3];

			CachedStatement cIstance = new CachedStatement(select);

			try {
				ResultSet rs = cIstance.executeQuery();
				if (rs.next()) {
					exist = true;
				}

				rs.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		return exist;
	}

	public static boolean existOrdAcq(String chiaveOrdVen) {
		boolean exist = false;

		String[] chiave = KeyHelper.unpackObjectKey(chiaveOrdVen);

		if (chiave != null) {

			String select = "SELECT ID_NUMERO_ORD " + " FROM THIP.ORD_ACQ_RIG " + " WHERE ID_AZIENDA = '" + chiave[0]
					+ "' " + " AND R_ANNO_ORDC = '" + chiave[1] + "' " + " AND R_NUMERO_ORDC = '" + chiave[2] + "' "
					+ " AND R_RIGA_ORDC = " + chiave[3];

			CachedStatement cIstance = new CachedStatement(select);

			try {
				ResultSet rs = cIstance.executeQuery();
				if (rs.next()) {
					exist = true;
				}

				rs.close();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

		return exist;
	}

	// Fix 81032 - Fine

	// Cristi
	public void apriGestioneTagliaColore(ServletEnvironment se) {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String docKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		se.getRequest().setAttribute("FatherKey", docKey);
		se.getRequest().setAttribute("ClassHDR", "VtOrdineVenditaRigaPrm");

		String url = "/" + "it/thera/thip/tessile/tagliaColore/servlet/VtSceltaArticoloGestioneTagliaColore.jsp";
		try {
			se.sendRequest(getServletContext(), url, true);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// VtGestioneTagliaColoreUtil.apriGestioneTagliaColore(se,"VtOrdineVenditaRigaPrm");
	}

	public void apriGestioneTagliaColoreModifica(ServletEnvironment se) {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		se.getRequest().setAttribute("FatherKey", fatherKey);

		// Controllo eventualmente se è stata selezionata una riga. se si andrò a
		// prendere solo quell'articolo
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");
		if (chiaveOrdine[0] != null) {
			VtOrdineVenditaRigaPrm riga = null;
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
						chiaveOrdine[0], VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (riga != null && riga.getGruppoTC() != null)
				VtGestioneTagliaColoreUtil.apriGestioneTagliaColoreModifica(se, "VtOrdineVenditaRigaPrm", fatherKey,
						riga.getGruppoTC());
			else if (riga != null && riga.getGruppoTC() == null) {

				try {
					String modoForm = datiSessione.getModoForm();

					String url = "it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmCompleta.jsp?Mode=UPDATE&Key="
							+ riga.getKey();

					if (modoForm != null && modoForm.equals("RIDOTTA")) {
						url = "it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidotta.jsp?Mode=UPDATE&Key="
								+ riga.getKey();
					}
					se.sendRequest(getServletContext(), url, true);
					/*
					 * ServletOutputStream out = se.getResponse().getOutputStream();
					 * out.println("<script language='JavaScript1.2'>"); out.
					 * println("parent.alert(\"Selezionare una riga che appartenga a un gruppo Taglia e Colore\");"
					 * ); out.println("parent.window.close();"); out.println("</script>");
					 */
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void apriEvasioneMagPartenzaDestinazione(ServletEnvironment se) throws ServletException, IOException {

		DocumentoDatiSessione datiSessione = getDatiSessione(se);

		String fatherKey = getStringParameter(se.getRequest(), "FatherKey");
		if (fatherKey == null || fatherKey.equals(""))
			fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());

		se.getRequest().setAttribute("FatherKey", fatherKey);

		// Controllo eventualmente se è stata selezionata una riga. se si andrò a
		// prendere solo quell'articolo
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");
		String articoloId = se.getRequest().getParameter("idArticolo");
		String magKey = se.getRequest().getParameter("idMagazzino");
		String groupTC = se.getRequest().getParameter("GruppoTC");

		if (chiaveOrdine != null && chiaveOrdine[0] != null) {
			VtOrdineVenditaRigaPrm riga = null;
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
						chiaveOrdine[0], VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (riga != null) {
				fatherKey = riga.getKey();
				magKey = riga.getIdMagazzino();
				articoloId = riga.getIdArticolo();
				if (riga.getGruppoTC() != null && !riga.getGruppoTC().equals(""))
					groupTC = String.valueOf(riga.getGruppoTC());
			}
		}

		se.sendRequest(
				getServletContext(), "it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidMag.jsp?DocKey="
						+ fatherKey + "&idArticolo=" + articoloId + "&idMagazzino=" + magKey + "&GruppoTC=" + groupTC,
				true);

	}

	// BRY
	public void apriVistaDocGeneratiMontSmont(ServletEnvironment se) throws ServletException, IOException {

		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		String idRiga = null;
		se.getRequest().setAttribute("FatherKey", fatherKey);

		// Controllo eventualmente se è stata selezionata una riga. se si andrò a
		// prendere solo quell'articolo
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");

		if (chiaveOrdine != null && chiaveOrdine[0] != null) {
			idRiga = KeyHelper.getTokenObjectKey(chiaveOrdine[0], 4);

		}

		/*
		 * String url = IniFile.getValue("Web","ServletPath") +
		 * "/it.thera.thip.tessile.vendite.ordineVE.web.VtDocKitOrdVenRigaGridActionAdapter?thClassName=VtDocKitOrdVenRiga&"
		 * + BaseServlet.ACTION + "=" + "VIEW&ObjectKey="+ fatherKey; url=url
		 * +"&thSpecificDOList=it.thera.thip.tessile.vendite.ordineVE.web.VtDocKitOrdVenRigaDoList"+
		 * "&thRestrictConditions=chiave="+fatherKey+";";
		 * 
		 * se.sendRequest(getServletContext(), url , true);
		 */

		String restrictCondition = "IdAzienda=" + Azienda.getAziendaCorrente();
		restrictCondition += ";OrdineKey=" + fatherKey;

		// if(idRiga!=null && !idRiga.equals(""))
		restrictCondition += ";IdRiga=" + idRiga;

		String restrictConditions = "&thRestrictConditions=" + URLEncoder.encode(restrictCondition);

		String urlDo = "&thSpecificDOList=it.thera.thip.tessile.vendite.ordineVE.web.VtDocKitOrdVenRigaDoList";
		String servletName = "com.thera.thermfw.web.servlet.ShowGrid?ClassName=VtDocKitOrdVenRiga&thGridType=list"
				+ restrictConditions + urlDo;

		se.sendRequest(getServletContext(), se.getServletPath() + servletName, true);

	}

	// FIX 82134 FIRAS
	public void apriGestioneTagliaColoreDisMag(ServletEnvironment se) {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		se.getRequest().setAttribute("FatherKey", fatherKey);

		String url = "servlet/it.sicons.thip.tessile.tagliaColore.servlet.SiVtGestioneTagliaColoreDispMag";
		try {
			se.sendRequest(getServletContext(), url, true);

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
	// FIX 82134 FINE

	protected void apriGestioneMontaggioSmontaggioKit(ServletEnvironment se, String Tipo)
			throws ServletException, IOException {

		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		se.getRequest().setAttribute("FatherKey", fatherKey);

		// Controllo eventualmente se è stata selezionata una riga. se si andrò a
		// prendere solo quell'articolo
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");

		if (chiaveOrdine != null && chiaveOrdine[0] != null) {
			VtOrdineVenditaRigaPrm riga = null;
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
						chiaveOrdine[0], VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (riga != null) {
				se.sendRequest(getServletContext(),
						"it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmMontSmontK.jsp?Tipo=" + Tipo + "&DocKey="
								+ riga.getKey(),
						true);
			}

		} else
			se.sendRequest(getServletContext(),
					"it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmMontSmontK.jsp?Tipo=" + Tipo + "&DocKey=",
					true);
	}

	protected void apriGestioneSmontaggioKit(ServletEnvironment se, String Tipo) throws ServletException, IOException {

		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = getStringParameter(se.getRequest(), "FatherKey");
		if (fatherKey == null || fatherKey.equals(""))
			fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());

		se.getRequest().setAttribute("FatherKey", fatherKey);
		String articoloKey = se.getRequest().getParameter("idArticolo");
		String magKey = se.getRequest().getParameter("idMagazzino");
		String groupTC = se.getRequest().getParameter("GruppoTC");
		String idRiga = "";

		// if (magKey == null || magKey.equals("")) {
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");

		if (chiaveOrdine != null && chiaveOrdine[0] != null && !chiaveOrdine[0].equals("null")) {
			VtOrdineVenditaRigaPrm riga = null;
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
						chiaveOrdine[0], VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (riga != null) {
				if (magKey == null || magKey.equals("")) {
					magKey = riga.getIdMagazzino();
					if (riga.getGruppoTC() != null && !riga.getGruppoTC().equals(""))
						groupTC = String.valueOf(riga.getGruppoTC());
					articoloKey = riga.getIdArticolo();
				}

				idRiga = String.valueOf(riga.getNumeroRigaDocumento());
			}

		}

		se.sendRequest(getServletContext(),
				"it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmMontSmontK.jsp?Tipo=" + Tipo + "&DocKey="
						+ fatherKey + "&idArticolo=" + articoloKey + "&idMagazzino=" + magKey + "&GruppoTC=" + groupTC
						+ "&idRiga=" + idRiga,
				true);
	}

	protected void apriGestioneOnlySmontaggioKit(ServletEnvironment se) throws ServletException, IOException {

		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = getStringParameter(se.getRequest(), "FatherKey");
		if (fatherKey == null || fatherKey.equals(""))
			fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());

		se.getRequest().setAttribute("FatherKey", fatherKey);
	
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");
		se.getRequest().setAttribute("ObjectKey", chiaveOrdine);
		
		se.sendRequest(getServletContext(),
				"it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmSmontK.jsp?Tipo=OS?DocKey=" + chiaveOrdine,true);
	}

	protected void apriGestioneMontaggioKit(ServletEnvironment se, String Tipo) throws ServletException, IOException {

		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		String fatherKey = getStringParameter(se.getRequest(), "FatherKey");// se.getRequest().getParameter("FatherKey");

		if (fatherKey == null || fatherKey.equals(""))
			fatherKey = KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento());
		se.getRequest().setAttribute("FatherKey", fatherKey);

		// Controllo eventualmente se è stata selezionata una riga. se si andrò a
		// prendere solo quell'articolo
		String[] chiaveOrdine = se.getRequest().getParameterValues("ObjectKey");

		if (chiaveOrdine != null && chiaveOrdine[0] != null) {
			VtOrdineVenditaRigaPrm riga = null;
			try {
				riga = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
						chiaveOrdine[0], VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (riga != null) {
				se.sendRequest(getServletContext(),
						"it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmMontSmontK.jsp?Tipo=" + Tipo + "&DocKey="
								+ riga.getKey(),
						true);
			}

		}
	}

	// Fix 81106
	protected void copyObject(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		DocumentoDatiSessione datiSessione = getDatiSessione(se);
		// Fix 3230 - inizio
		datiSessione.salvaInSessione(se);
		// Fix 3230 - fine

		// Fix 81106 - Inizio
		datiSessione.setModoForm(DocumentoNavigazioneWeb.MF_COMPLETA);
		// Fix 81106 - Fine

		String azione = getAzione(se);
		String url = "/" + getJSPCorrente(se, datiSessione, azione) + "?Mode=COPY&Key="
				+ getStringParameter(se.getRequest(), "ObjectKey") + "&InitialActionAdapter=" + getClass().getName();
		se.sendRequest(getServletContext(), url, false);
	}

	// Fix 81120
	// public VtAttivitaEsecProdotto creaProdottoPerVersamento(VtOrdineEsecutivo
	// ordEsec, Articolo articolo,String idMagVersamento,VtOrdineVenditaRigaPrm
	// ordVenRig, AttivitaEsecMateriale atvMat){//fix 81264
	public VtAttivitaEsecProdotto creaProdottoPerVersamento(VtOrdineEsecutivo ordEsec, Articolo articolo,
			String idMagVersamento, VtOrdineVenditaRigaPrm ordVenRig, List materiali) {
		VtAttivitaEsecProdotto nuovaAtvPrd = (VtAttivitaEsecProdotto) Factory
				.createObject(VtAttivitaEsecProdotto.class);

		// Imposto la chiave

		nuovaAtvPrd.setIdAzienda(ordEsec.getIdAzienda());
		nuovaAtvPrd.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
		nuovaAtvPrd.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());

		nuovaAtvPrd.setIdRigaProdotto(new Integer(1));
		nuovaAtvPrd.setTipoProdotto('2'); // Prodotto primario

		// Set descrizioni
		if (articolo != null) {
			nuovaAtvPrd.setArticolo(articolo);
			nuovaAtvPrd.getDescrizione().setDescrizione(articolo.getDescrizioneArticoloNLS().getDescrizione());
			nuovaAtvPrd.getDescrizione()
					.setDescrizioneRidotta(articolo.getDescrizioneArticoloNLS().getDescrizioneRidotta());

		}
		nuovaAtvPrd.setCoeffProduzione(ordEsec.getQtaBaseUMPrm());

		nuovaAtvPrd.setIdMagazzinoVrs(idMagVersamento);

		// Set riferimenti ordine vendita dalla riga estratta
		nuovaAtvPrd.setIdCliente(ordVenRig.getIdCliente());
		nuovaAtvPrd.setAnnoOrdineCliente(ordVenRig.getAnnoDocumento());
		nuovaAtvPrd.setNumeroOrdineCliente(ordVenRig.getNumeroDocumento());
		nuovaAtvPrd.setRigaOrdineCliente(ordVenRig.getNumeroRigaDocumento());

		nuovaAtvPrd.setQtaRichiestaUMPrm(ordVenRig.getQtaInUMPrmMag());

		nuovaAtvPrd.setEsponiInBollaVersam(true);

		// Fix 81214
		nuovaAtvPrd.setPoliticaConsVersam('2');

		// Aggiungo anche i lotti presenti nel primo materiale di prelievo
		// Fix 81264 - Aggiungo tutti i lotti di tutti i materiali dell'attività che
		// hanno idArticolo uguale
		if (materiali != null && !materiali.isEmpty()) {
			Iterator atvIter = materiali.iterator();
			while (atvIter.hasNext()) {
				AttivitaEsecMateriale atvMat = (AttivitaEsecMateriale) atvIter.next();
				if (atvMat.getIdArticolo().equals(nuovaAtvPrd.getIdArticolo()) && atvMat.getLottiMateriali() != null
						&& !atvMat.getLottiMateriali().isEmpty()) {
					Iterator iter = atvMat.getLottiMateriali().iterator();
					while (iter.hasNext()) {
						AttivitaEsecLottiMat lottoEsistente = (AttivitaEsecLottiMat) iter.next();

						AttivitaEsecLottiPrd lottoNew = (AttivitaEsecLottiPrd) Factory
								.createObject(AttivitaEsecLottiPrd.class);
						lottoNew.setIdAzienda(ordEsec.getIdAzienda());
						lottoNew.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
						lottoNew.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());
						lottoNew.setIdRigaAttivita(nuovaAtvPrd.getIdRigaAttivita());
						lottoNew.setIdRigaProdotto(nuovaAtvPrd.getIdRigaProdotto());
						lottoNew.setIdLotto(lottoEsistente.getIdLotto());
						lottoNew.setIdArticolo(lottoEsistente.getIdArticolo());
						lottoNew.setQtaRichiestaUMPrm(lottoEsistente.getQtaRichiestaUMPrm());
						lottoNew.setQtaRichiestaUMSec(lottoEsistente.getQtaRichiestaUMSec());

						nuovaAtvPrd.getLottiProdotti().add(lottoNew);
					}
				}
			}
		}

		return nuovaAtvPrd;
	}

	// Fix 81325 - Inizio

	public void creaOrdineEsecutivo(String[] keyRigheOrd, BigDecimal qtaDaProd, ServletEnvironment se) {

		String idStabilimento = ParametroPsn.getParametroPsn("pers.ParametriPerContoLavoro", "IdStabilimento")
				.getValore(); // da chiedere se prendere dal modello produttivo
		String idSerie = ParametroPsn.getParametroPsn("pers.ParametriPerContoLavoro", "IdSerieOrdEsecutivo")
				.getValore();

		// Controllo che i modelli di gruppo degli articoli selezionati siano uguali
		String url = "";
		List lstModProGrp = recuperaListaModGruppo(keyRigheOrd);

		boolean modProGrpOk = true;

		if (lstModProGrp.size() > 1)
			modProGrpOk = VtTessileUtil.allElementModProListAreSame(lstModProGrp);
		// boolean modProGrpOk =
		// VtTessileUtil.allElementModProListAreSame(lstModProGrp);

		if (lstModProGrp.isEmpty()) {
			ServletOutputStream out;

			try {
				out = se.getResponse().getOutputStream();
				out.println(" <script language='JavaScript1.2'> ");
				out.println(" alert('Modello di gruppo mancante! '); ");
				out.println(" parent.window.close(); ");
				out.println(" </script> ");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (!modProGrpOk) {
			ServletOutputStream out;
			try {
				out = se.getResponse().getOutputStream();
				out.println(" <script language='JavaScript1.2'> ");
				out.println(" alert('I modelli di gruppo delle righe ordini selezionate non sono uguali'); ");
				out.println(" parent.window.close(); ");
				out.println(" </script> ");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * se.getRequest().setAttribute("modProError", "Y"); //I modelli di gruppo degli
			 * ordini selezionati non sono uguali url =
			 * "it/thera/thip/tessile/produzione/rilDaOrdVen/servlet/VtRilascioDaOrdVenGridError.jsp";
			 */
		} else {

			ModelloProduttivo modProGrp = null;

			boolean daCreare = false;
			String idMagPrelDaAttivita = null;
			String idMagVersDaAttivita = null;

			Iterator iterModProGrp = lstModProGrp.iterator();
			while (iterModProGrp.hasNext()) {

				modProGrp = (ModelloProduttivo) iterModProGrp.next();

				if (modProGrp != null) {

					if (modProGrp.getMagazzinoPrelievo() != null && modProGrp.getMagazzinoVersamento() != null) {
						idMagPrelDaAttivita = modProGrp.getIdMagazzinoPrelievo();
						idMagVersDaAttivita = modProGrp.getIdMagazzinoVersamento();

						daCreare = true;

					} else {

						ServletOutputStream out;
						try {
							out = se.getResponse().getOutputStream();

							/*
							 * out.println("<script language='JavaScript1.2'>");
							 * out.println("alert('"+"dai"+"');"); out.println("parent.window.close();");
							 * out.println("</script>");
							 */

							out.println(" <script language='JavaScript1.2'> ");
							out.println(
									" alert('Il modello produttivo di gruppo deve avere valorizzati i campi Magazzino Versamento e Magazzino Prelievo'); ");
							out.println(" parent.window.close(); ");
							out.println(" </script> ");

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					break;
				} else {
					se.getRequest().setAttribute("artError", "NE"); // Modelli di gruppo mancanti
					url = "it/thera/thip/tessile/produzione/rilDaOrdVen/servlet/VtRilascioDaOrdVenGridError.jsp";
				}

			}

			// Prima riga dell'ordine di vendita
			VtOrdineVenditaRigaPrm ordVen = null;
			try {
				ordVen = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
						keyRigheOrd[0], VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// se ci sono errori non creo l'ordine esecutivo
			if (daCreare) {
				// Inizio creazione testata ordine esecutivo
				VtOrdineEsecutivo nuovoOrdEsec = (VtOrdineEsecutivo) Factory.createObject(VtOrdineEsecutivo.class);

				nuovoOrdEsec.setIdAzienda(ordVen.getIdAzienda());
				nuovoOrdEsec.setWithModelloProduttivo("false");
				nuovoOrdEsec.getNumeratoreHandler().setIdSerie(idSerie);

				if (ordVen.getArticolo() != null) {
					// nuovoOrdEsec.getDescrizione().setDescrizione(ordVen.getArticolo().getDescrizioneArticoloNLS().getDescrizione());
					// nuovoOrdEsec.getDescrizione().setDescrizioneRidotta(ordVen.getArticolo().getDescrizioneArticoloNLS().getDescrizioneRidotta());

					nuovoOrdEsec.getDescrizione().setDescrizione(modProGrp.getDescrizione().getDescrizione());
					nuovoOrdEsec.getDescrizione()
							.setDescrizioneRidotta(modProGrp.getDescrizione().getDescrizioneRidotta());

					nuovoOrdEsec.setUMPrmMag(ordVen.getArticolo().getUMPrmMag());
					nuovoOrdEsec.setUMSecMag(ordVen.getArticolo().getUMSecMag());

					// nuovoOrdEsec.setIdArticolo(ordVen.getArticolo().getIdArticolo());
					nuovoOrdEsec.setArticolo(ordVen.getArticolo());
					// nuovoOrdEsec.setArticoloKey(ordVen.getArticolo().getKey());

					// Fix 81533 - Inizio
					String[] keyArt = { ordVen.getIdAzienda(), ordVen.getIdArticolo() };
					String sKeyArt = KeyHelper.buildObjectKey(keyArt);
					VtArticoliTex artTex = null;
					try {
						artTex = (VtArticoliTex) VtArticoliTex.elementWithKey(VtArticoliTex.class, sKeyArt,
								PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (artTex != null)
						nuovoOrdEsec.setTipoColorante(artTex.getTipoColorante());
					// Fix 81533 - Fine

					nuovoOrdEsec.setIdVersione(new Integer(1));

				}

				// nuovoOrdEsec.setMagazzinoVrs(ordVen.getMagazzino()); //Fix 81120
				// nuovoOrdEsec.setMagazzinoPrl(magazzinoPrl);
				// nuovoOrdEsec.setIdMagazzinoPrl(idMagazzinoPrl); //Fix 81120
				nuovoOrdEsec.setIdMagazzinoVrs(idMagVersDaAttivita);
				nuovoOrdEsec.setIdMagazzinoPrl(idMagPrelDaAttivita);

				nuovoOrdEsec.setIdStabilimento(idStabilimento);
				// nuovoOrdEsec.setQtaOrdinataUMPrm(ordVen.getQtaInUMRif());
				nuovoOrdEsec.setQtaOrdinataUMPrm(qtaDaProd);

				nuovoOrdEsec.setStatoOrdine('0'); // 0: Immesso
				nuovoOrdEsec.setSaldoManuale(false);
				nuovoOrdEsec.setStatoSchedulazione('0');
				nuovoOrdEsec.setIdCliente(ordVen.getIdCliente());
				nuovoOrdEsec.setMetodoResiduoOrdine('0');

				nuovoOrdEsec.setAnnoOrdineCliente(ordVen.getAnnoDocumento());
				nuovoOrdEsec.setNumeroOrdineCliente(ordVen.getNumeroDocumento());
				nuovoOrdEsec.setRigaOrdineCliente(ordVen.getNumeroRigaDocumento());
				nuovoOrdEsec.setDettaglioRigaOrdine(ordVen.getDettaglioRigaDocumento());

				nuovoOrdEsec.getDateRichieste().setStartDate(ordVen.getDataConsegnaConfermata());
				nuovoOrdEsec.getDateRichieste().setEndDate(ordVen.getDataConsegnaConfermata()); // da vedere

				nuovoOrdEsec.setStatoOrigine('V'); // valido

				boolean a = VtTessileUtil.genericSave(nuovoOrdEsec);

				// Fine creazione testata

				List lstRigheOrdVen = this.getRigheOrdVen(keyRigheOrd);

				// set riferimenti - INIZIO
				try {
					nuovoOrdEsec = setRiferimentiOrdineEsecutivo(this.getRigheOrdVen(keyRigheOrd), nuovoOrdEsec);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// set riferimenti - FINE

				List atvNuovoOrdEsec = creaListaAttivitaOrdineEsecutivo(ordVen, nuovoOrdEsec);
				if (!atvNuovoOrdEsec.isEmpty()) {
					List attivitaEsec = nuovoOrdEsec.getAttivitaEsecutive();
					attivitaEsec.addAll(atvNuovoOrdEsec);
				}

				List componentiTot = new ArrayList();
				List componenti = new ArrayList();

				// Creazione Attività prodotto tramite righe ordine di vendita
				for (int index = 1; index <= lstRigheOrdVen.size(); index++) {

					VtOrdineVenditaRigaPrm ordVenRig = (VtOrdineVenditaRigaPrm) lstRigheOrdVen.get(index - 1);

					if (ordVen != null)
						componenti = getListaRiservaComponenti(ordVenRig);

					try {
						creaVtAttivitaEsecProdottoProduzione(ordVenRig, nuovoOrdEsec, index);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (componenti != null)
						componentiTot.addAll(componenti);
				}

				// 03-05-2018
				// Controllo che siano stati creati i componenti

				// 04-05-2018
				/*
				 * List componenti = new ArrayList();
				 * 
				 * if(ordVen != null){ componenti = getListaRiservaComponenti(ordVen); }
				 */

				boolean ret1 = VtTessileUtil.genericSave(nuovoOrdEsec);

				// if(componenti != null && !componenti.isEmpty()){
				if (componentiTot != null && !componentiTot.isEmpty()) {
					try {
						List atvMat = creaVtAttivitaEsecMaterialeProduzione(componentiTot, nuovoOrdEsec,
								idMagPrelDaAttivita);

						if (!atvMat.isEmpty()) {
							System.out.println(" - Attività materiali create con successo: " + atvMat.size());
							// Creazione Lotti Materiale
							List lottiMat = VtGestioneOrdineEsecutivoProduzione.creaAttivitaEsecLottiMat(nuovoOrdEsec);
							if (!lottiMat.isEmpty()) {

							}
						}
					} catch (NoSuchElementException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// 07-05-2018
				else {
					VtGeneraDibaModello dibMod = new VtGeneraDibaModello();
					VtArticolo art = (VtArticolo) ordVen.getArticolo();
					try {
						List lstComp = dibMod.getFinalList(art);
						Object[] rigaComp = (Object[]) lstComp.get(0); // mi fermo al componente di primo livello
						VtArticolo componente = (VtArticolo) VtArticolo.elementWithKey(
								KeyHelper.buildObjectKey(
										new String[] { Azienda.getAziendaCorrente(), (String) rigaComp[1] }),
								PersistentObject.NO_LOCK);

						// creaVtAttivitaEsecMaterialeProduzione(componentiTot,
						// nuovoOrdEsec,idMagPrelDaAttivita);

						// 07-05-2018
						VtAttivitaEsecMateriale atvMat = generaVtAttivitaEsecMateriale(nuovoOrdEsec, new Integer(1),
								componente.getIdArticolo(), idMagPrelDaAttivita, new Integer(1), qtaDaProd);
						List lstAtvEsec = nuovoOrdEsec.getListaAttivita();

						// Fix 81471 - Inizio creazione lotto dummy
						List lstLotti = atvMat.getLottiMateriali();

						if (lstLotti.isEmpty()) {

							AttivitaEsecLottiMat lotto = (AttivitaEsecLottiMat) Factory
									.createObject(AttivitaEsecLottiMat.class);
							// ...FIX 5283 fine
							lotto.setIdAzienda(atvMat.getIdAzienda());
							lotto.setIdAnnoOrdine(atvMat.getIdAnnoOrdine());
							lotto.setIdNumeroOrdine(atvMat.getIdNumeroOrdine());
							lotto.setIdRigaAttivita(atvMat.getIdRigaAttivita());
							lotto.setIdRigaMateriale(atvMat.getIdRigaMateriale());
							lotto.setIdArticolo(atvMat.getIdArticolo());
							lotto.setIdLotto(Lotto.LOTTO_DUMMY);

							atvMat.getLottiMateriali().add(lotto);
							atvMat.setAggiornaLottoDummy(true);
						}
						// Fix 81471 - Fine

						// Fix 81471
						// atvMat.getAttivitaEsecutiva().setPoliticaConsAttivita('1');
						// atvMat.getAttivitaEsecutiva().save();

						boolean ret2 = VtTessileUtil.genericSave(atvMat);

						// setStatoOrigine(nuovoOrdEsec);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				// Fix 81411 - Inizio copia dei lotti materiali anche su cartellini pezza
				// Fix 81534 nuova richiesta no deve scrivere i cartellini pezza
				// creaVtOrdEsecPezze(nuovoOrdEsec);
				// Fix 81411 - Fine

				if (nuovoOrdEsec != null) {

					try {

						setAttivitaPrecedenti(nuovoOrdEsec);
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					try {
						List atv = nuovoOrdEsec.getAttivitaEsecutive();
						Iterator iter = atv.iterator();
						int i = 1;
						AttivitaEsecutiva atvPrec = null;
						while (iter.hasNext()) {
							AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) iter.next();
							// ConnectionManager.pushConnection();
							if (i == 1 && iter.hasNext()) {
								updateFlagAttivitaFinale(atvEsec.getKey());
							} else if (!iter.hasNext()) {
								// atvEsec.setAttivitaIniziale(false);
								// atvEsec.setAttivitaFinale(true);
								// atvEsec.getAttivitaPrecedenti().add(atvPrec);
								// atvEsec.getAttivitaPrecedentiAzElements().remove(atvPrec);
							} else {
								updateFlagAttivitaFinale(atvEsec.getKey());
							}
							/*
							 * rc = atvEsec.save(); if(rc >= 0) ConnectionManager.commit(); else
							 * ConnectionManager.rollback(); ConnectionManager.popConnection();
							 */
							i++;
							atvPrec = atvEsec;

							// non serve boolean rtAtvEsec = VtTessileUtil.genericSave(atvEsec);

						}
						// setAttivitaPrecedenti(testata.getKey());

						nuovoOrdEsec.setStatoOrdine('1');
						nuovoOrdEsec.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
						// rc = testata.save();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					int rc = 0;

					updateStatoOrigine(nuovoOrdEsec.getKey());

					// Fix 81411
					updateStatoOrigineProd(nuovoOrdEsec.getKey());

					try {

						nuovoOrdEsec = (VtOrdineEsecutivo) VtOrdineEsecutivo.elementWithKey(nuovoOrdEsec.getKey(),
								PersistentObject.NO_LOCK);
						nuovoOrdEsec.setStatoOrigine('V');

						// Fix 81519 - Inizio
						nuovoOrdEsec.setStatoOrdine('0'); // 0: Immesso

						ConnectionManager.pushConnection();
						rc = nuovoOrdEsec.save();
						if (rc >= 0) {
							ConnectionManager.commit();
							// String url = "/" + "it/thera/thip/produzione/ordese/OrdineEsecutivo.jsp" +
							// "?Mode=UPDATE&Key=" + testata.getKey() +
							// "&InitialActionAdapter=it.thera.thip.tessile.produzione.ordese.web.VtOrdineEsecutivoGridActionAdapter";
							// se.sendRequest(getServletContext(), url, false);
						} else
							ConnectionManager.rollback();
						ConnectionManager.popConnection();

						if (rc > 0) {
							String urlF = "/" + "it/thera/thip/produzione/ordese/OrdineEsecutivo.jsp"
									+ "?Mode=UPDATE&Key=" + nuovoOrdEsec.getKey()
									+ "&InitialActionAdapter=it.thera.thip.tessile.produzione.ordese.web.VtOrdineEsecutivoGridActionAdapter";
							se.sendRequest(getServletContext(), urlF, false);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// Fix 81411 - Inizio
					/*
					 * List atvEsecList = nuovoOrdEsec.getAttivitaEsecutive();
					 * if(!atvEsecList.isEmpty()){
					 * 
					 * Iterator iteAtvEsec = atvEsecList.iterator(); while(iteAtvEsec.hasNext()){
					 * 
					 * AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) iteAtvEsec.next(); //Fix
					 * 81411 - Inizio boolean rtAtvProd = false; List attivitaEsecProdList =
					 * atvEsec.getProdotti(); int size = attivitaEsecProdList.size(); for (int z =
					 * 0; z < size; z++) { AttivitaEsecProdotto attivitaEsecProd =
					 * (AttivitaEsecProdotto) attivitaEsecProdList.get(z); if
					 * (attivitaEsecProd.getDatiComuniEstesi().getStato() ==
					 * DatiComuniEstesi.VALIDO)
					 * 
					 * 
					 * attivitaEsecProd.setSaveFathers(true); rtAtvProd =
					 * VtTessileUtil.genericSave(attivitaEsecProd);
					 * 
					 * System.out.println(rtAtvProd);
					 * 
					 * }
					 * 
					 * //Fix 81411 - Fine
					 * 
					 * 
					 * } }//Fix 81411 - Fine
					 */
				}

			}
		}
	}

	/*
	 * DA RIVEDERE public static List creaVtAttivitaEsecProdottoProduzione(List
	 * righeOrdVen, VtOrdineEsecutivo ordEsec){
	 * 
	 * boolean creato = false;
	 * 
	 * List atvPrdCreate = new ArrayList();
	 * 
	 * 
	 * if(!righeEstratte.isEmpty()){
	 * 
	 * Iterator iteRighe = righeEstratte.iterator();
	 * 
	 * for(int index = 1; iteRighe.hasNext(); index++){
	 * 
	 * VtOrdineVenditaEstrattoRil rigaEstr =
	 * (VtOrdineVenditaEstrattoRil)iteRighe.next();
	 * 
	 * if(rigaEstr != null){
	 * 
	 * VtAttivitaEsecProdotto nuovaAtvPrd =
	 * (VtAttivitaEsecProdotto)Factory.createObject(VtAttivitaEsecProdotto.class);
	 * 
	 * //Imposto la chiave
	 * 
	 * nuovaAtvPrd.setIdAzienda(ordEsec.getIdAzienda());
	 * nuovaAtvPrd.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
	 * nuovaAtvPrd.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());
	 * 
	 * Integer rigaAtv = new Integer(1);
	 * 
	 * //Inserisco nell'ultima attività immessa List atvEsecList =
	 * ordEsec.getAttivitaEsecutive(); if(!atvEsecList.isEmpty()){ rigaAtv = new
	 * Integer(atvEsecList.size()); nuovaAtvPrd.setIdRigaAttivita(rigaAtv);
	 * 
	 * AttivitaEsecutiva atvEsec = (AttivitaEsecutiva)
	 * atvEsecList.get(rigaAtv.intValue() - 1);
	 * 
	 * nuovaAtvPrd.setAttivitaEsecutiva(atvEsec); }
	 * 
	 * nuovaAtvPrd.setIdRigaProdotto(new Integer(index));
	 * 
	 * 
	 * 
	 * nuovaAtvPrd.setTipoProdotto('0'); //Prodotto primario
	 * 
	 * //Fix 80829 - Fine
	 * 
	 * //Set descrizioni Articolo art = rigaEstr.getArticolo(); if(art != null){
	 * nuovaAtvPrd.setArticolo(art);
	 * nuovaAtvPrd.getDescrizione().setDescrizione(art.getDescrizioneArticoloNLS().
	 * getDescrizione()); nuovaAtvPrd.getDescrizione().setDescrizioneRidotta(art.
	 * getDescrizioneArticoloNLS().getDescrizioneRidotta());
	 * 
	 * }
	 * 
	 * nuovaAtvPrd.setCoeffProduzione(new BigDecimal(0));
	 * 
	 * // Fix 80800 - Inizio
	 * 
	 * //nuovaAtvPrd.setMagazzinoVersamento(rigaEstr.getMagazzino());
	 * 
	 * String idMagVers = ordEsec.getIdMagazzinoVrs();
	 * if(rigaEstr.getModelloProduttivo() != null){ List atvModProList =
	 * rigaEstr.getModelloProduttivo().getAttivita(); if(atvModProList != null &&
	 * !atvModProList.isEmpty()){ AttivitaProduttiva atv =
	 * (AttivitaProduttiva)atvModProList.get(rigaAtv.intValue() -1); if(atv !=
	 * null){ List atvPrdList = atv.getProdotti();
	 * 
	 * if(atvPrdList != null && !atvPrdList.isEmpty()){ AttivitaProdProdotto atvPrd
	 * = (AttivitaProdProdotto)atvPrdList.get(0); if(atvPrd != null){ String
	 * magVersPrd = atvPrd.getIdMagazzinoVrs(); if(magVersPrd != null) idMagVers =
	 * magVersPrd; } }
	 * 
	 * } } }
	 * 
	 * nuovaAtvPrd.setIdMagazzinoVrs(idMagVers);
	 * 
	 * // Fix 80800 - Fine
	 * 
	 * 
	 * //Set riferimenti ordine vendita dalla riga estratta
	 * nuovaAtvPrd.setCliente(rigaEstr.getCliente());
	 * nuovaAtvPrd.setAnnoOrdineCliente(rigaEstr.getAnnoOrdineCli());
	 * nuovaAtvPrd.setNumeroOrdineCliente(rigaEstr.getNumeroOrdineCli());
	 * nuovaAtvPrd.setRigaOrdineCliente(rigaEstr.getRigaOrdineCli());
	 * 
	 * 
	 * //nuovaAtvPrd.setDettaglioRigaOrdine(rigaEstr.getDettRigaOrdineCli());
	 * 
	 * //Fix 80944 - Inizio
	 * //nuovaAtvPrd.setQtaRichiestaUMPrm(rigaEstr.getQtaDaProd());
	 * if(righeEstratte.size() > 1){
	 * nuovaAtvPrd.setQtaRichiestaUMPrm(rigaEstr.getQtaDaProd()); } else{
	 * nuovaAtvPrd.setQtaRichiestaUMPrm(ordEsec.getQtaOrdinataUMPrm());
	 * nuovaAtvPrd.setQtaRichiestaUMSec(ordEsec.getQtaOrdinataUMSec()); }
	 * 
	 * 
	 * 
	 * //Fix 80944 - Fine
	 * 
	 * 
	 * String chiave = nuovaAtvPrd.getKey();
	 * 
	 * 
	 * //Fix 80631 - Inizio //creato =
	 * VtTessileUtil.saveVtAttivitaEsecProdotto(nuovaAtvPrd); creato =
	 * VtTessileUtil.saveVtAttivitaEsecProdottoConDummy(nuovaAtvPrd); //Fix 80631 -
	 * Fine
	 * 
	 * 
	 * if(creato){ atvPrdCreate.add(nuovaAtvPrd); } else{ System.out.
	 * println("ERR: Si sono verificati degli errori durante la creazione delle attività prodotto con ID_RIGA_PRODOTTO = "
	 * + index); //break; //80678 } } } }
	 * 
	 * return atvPrdCreate; }
	 * 
	 */

	// Set riferimenti ordine cliente
	public static VtOrdineEsecutivo setRiferimentiOrdineEsecutivo(List righeOrdVen, VtOrdineEsecutivo ordEsec)
			throws Exception {

		if (!righeOrdVen.isEmpty()) {

			Iterator iteRighe = righeOrdVen.iterator();

			if (righeOrdVen.size() == 1) {

				if (iteRighe.hasNext()) {
					VtOrdineVenditaRigaPrm rigaOrdVen = (VtOrdineVenditaRigaPrm) iteRighe.next();

					// Set riferimenti ordine vendita dalla riga estratta
					// ordEsec.setCliente(rigaOrdVen.getIdCliente());
					ordEsec.setIdCliente(rigaOrdVen.getIdCliente()); // da verificare
					ordEsec.setAnnoOrdineCliente(rigaOrdVen.getAnnoDocumento());
					ordEsec.setNumeroOrdineCliente(rigaOrdVen.getNumeroDocumento());
					ordEsec.setRigaOrdineCliente(rigaOrdVen.getNumeroRigaDocumento());
					ordEsec.setDettaglioRigaOrdine(rigaOrdVen.getDettaglioRigaDocumento());

					aggiornaFlagRilascioOrdineProd(rigaOrdVen);
				}

			} else {

				VtOrdineVenditaRigaPrm primaRigaOrdVen = (VtOrdineVenditaRigaPrm) righeOrdVen.get(0);
				if (primaRigaOrdVen != null) {
					// ordEsec.setCliente(rigaE.getCliente());
					ordEsec.setIdCliente(primaRigaOrdVen.getIdCliente()); // da verificare
					ordEsec.setAnnoOrdineCliente(primaRigaOrdVen.getAnnoDocumento());
					ordEsec.setNumeroOrdineCliente(primaRigaOrdVen.getNumeroDocumento());
				}

				while (iteRighe.hasNext()) {

					VtOrdineVenditaRigaPrm rigaOrdVen = (VtOrdineVenditaRigaPrm) iteRighe.next();

					String idAzienda = ordEsec.getIdAzienda();
					String idAnnoOrdEsec = ordEsec.getIdAnnoOrdine();
					String idNumOrdEsec = ordEsec.getIdNumeroOrdine();
					String idAnnoOrdVenRig = rigaOrdVen.getAnnoDocumento();
					String idNumOrdVenRig = rigaOrdVen.getNumeroDocumento();
					String idRigaOrdVenRig = String.valueOf(rigaOrdVen.getNumeroRigaDocumento());
					String idDetRigaOrdVenRig = String.valueOf(rigaOrdVen.getDettaglioRigaDocumento());

					Object[] keyParts = { idAzienda, idAnnoOrdEsec, idNumOrdEsec, idAnnoOrdVenRig, idNumOrdVenRig,
							idRigaOrdVenRig, idDetRigaOrdVenRig };

					String key = KeyHelper.buildObjectKey(keyParts);

					if (key != null) {
						VtOrdEsecOrdVenRig ordEsecOrdVenRig = VtTessileUtil.creaVtOrdEsecOrdVenRig(key);

						// aggiorna flag rilascio ordine produzione a rilasciato ordine

						aggiornaFlagRilascioOrdineProd(rigaOrdVen);

					}
				}
			}

		}

		return ordEsec;
	}

	public static boolean aggiornaFlagRilascioOrdineProd(VtOrdineVenditaRigaPrm rigaOrdineVendita) {
		boolean ret = false;

		// 07-05-2018
		VtOrdineVenditaRigaPrm rigaOrdVen = null;
		try {
			rigaOrdVen = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class,
					rigaOrdineVendita.getKey(), VtOrdineVenditaRigaPrm.NO_LOCK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rigaOrdVen.setRilascioOrdineProd('2'); // generato Ordine

		ret = VtTessileUtil.genericSave(rigaOrdVen);

		return ret;
	}

	public List getRigheOrdVen(String[] keyRigheOrd) {
		List lstRigheOrdVen = new ArrayList();
		for (int i = 0; i < keyRigheOrd.length; i++) {
			try {
				VtOrdineVenditaRigaPrm ordVen = (VtOrdineVenditaRigaPrm) PersistentObject
						.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRigheOrd[i], VtOrdineVenditaRigaPrm.NO_LOCK);
				if (ordVen != null) {
					lstRigheOrdVen.add(ordVen);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lstRigheOrdVen;
	}

	public List recuperaListaModGruppo(String[] keyRigheOrd) {
		List lstModProGrp = new ArrayList();

		for (int i = 0; i < keyRigheOrd.length; i++) {
			try {
				VtOrdineVenditaRigaPrm ordVen = (VtOrdineVenditaRigaPrm) PersistentObject
						.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRigheOrd[i], VtOrdineVenditaRigaPrm.NO_LOCK);
				if (ordVen != null) {
					VtArticolo art = (VtArticolo) ordVen.getArticolo();
					String modProGruppo = art.getModelloProd();

					ModelloProduttivo modProGrp = VtTessileUtil.getModProGruppoByArt(art.getIdAzienda(),
							art.getIdArticolo());

					// lstModProGrp.add(modProGruppo);
					if (modProGrp != null)
						lstModProGrp.add(modProGrp);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return lstModProGrp;
	}

	public boolean checkParametriPers() {

		boolean okParametri = false;

		String idStabilimento = "";
		String idSerie = "";

		/*
		 * RECUPERO INIZIALMENTE I DATI DEI PARAMETRI RIFERITI ALLO STABILIMENTO E ALLA
		 * SERIE DELL'ORDINE PRODUZIONE CHE SONO INDISPENSABILI PER CREARE L'ORDINE DI
		 * PRODUZIONE
		 */
		ParametroPsn paramStabil = ParametroPsn.getParametroPsn("pers.ParametriPerContoLavoro", "IdStabilimento");
		ParametroPsn paramSerie = ParametroPsn.getParametroPsn("pers.ParametriPerContoLavoro", "IdSerieOrdEsecutivo");
		if (paramStabil != null && paramSerie != null && paramStabil.getValore() != null
				&& paramSerie.getValore() != null) {
			idStabilimento = paramStabil.getValore();
			idSerie = paramSerie.getValore();
			okParametri = true;
		}

		return okParametri;
	}

	public boolean checkLavorazioni(String keyRigaOrd) {
		boolean okLav = false;

		try {
			VtOrdineVenditaRigaPrm ordVen = (VtOrdineVenditaRigaPrm) PersistentObject
					.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRigaOrd, VtOrdineVenditaRigaPrm.NO_LOCK);
			if (ordVen.getVtOrdVenRigAtv() != null && !ordVen.getVtOrdVenRigAtv().isEmpty())
				okLav = true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return okLav;
	}

	public static BigDecimal calcolaQtaDaProdurreTot(String[] chiaviOrdVen) {
		BigDecimal qtaTot = new BigDecimal(0);

		if (chiaviOrdVen != null) {

			for (int i = 0; i < chiaviOrdVen.length; i++) {

				String key = (String) chiaviOrdVen[i];

				VtOrdineVenditaRigaPrm ordVenRig = VtTessileUtil.getVtOrdineVenditaRigaPrmByKey(key);

				if (ordVenRig != null) {
					qtaTot = qtaTot.add(ordVenRig.getQtaInUMPrmMag());
				}
			}
		}

		return qtaTot;
	}

	public static boolean checkStatoRilascioOrdEsec(String[] chiaviOrdVen) {
		boolean ok = true;

		if (chiaviOrdVen != null) {

			for (int i = 0; i < chiaviOrdVen.length; i++) {

				String key = (String) chiaviOrdVen[i];

				VtOrdineVenditaRigaPrm ordVenRig = VtTessileUtil.getVtOrdineVenditaRigaPrmByKey(key);

				if (ordVenRig != null) {
					if (ordVenRig.getRilascioOrdineProd() == '2') { // Generato ordine (è il flag che aggiorno una volta
																	// creato l'ordine esecutivo)
						ok = false;
						break;
					}
				}
			}
		}

		return ok;
	}

	public boolean checkCompatibilitaRighe(String[] keyRigheOrd) {
		boolean ret = true;

		List lstAtv = new ArrayList();
		List lstAtvDaConf = new ArrayList();

		for (int i = 0; i < keyRigheOrd.length; i++) {
			try {
				VtOrdineVenditaRigaPrm ordVen = (VtOrdineVenditaRigaPrm) PersistentObject
						.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRigheOrd[i], VtOrdineVenditaRigaPrm.NO_LOCK);
				if (i == 0) // primo giro
				{
					lstAtv = ordVen.getVtOrdVenRigAtv();
					/*
					 * da vedere if (lstAtv.isEmpty()) return ret; da vedere
					 */
				} else {
					lstAtvDaConf = ordVen.getVtOrdVenRigAtv();

					Iterator iterLstAtv = lstAtv.iterator();
					Iterator iterLstAtvDaConf = lstAtvDaConf.iterator();

					if (lstAtv.size() == lstAtvDaConf.size()) {
						while (iterLstAtv.hasNext()) {
							VtOrdVenRigAtv atv = (VtOrdVenRigAtv) iterLstAtv.next();

							VtOrdVenRigAtv atvDaConf = (VtOrdVenRigAtv) iterLstAtvDaConf.next();

							if (!atv.getIdAttivita().equals(atvDaConf.getIdAttivita())) {
								ret = false;
								return ret;
							}
						}
					} else {
						ret = false;
						return ret;
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}
	// Fix 81325 - Fine

	public static VtAttivitaEsecMateriale generaMaterialePerPrelievo(VtOrdineEsecutivo ordEsec, Integer idRigaAttivita,
			String idArticolo, String idMagPrl, Integer idAtvMat, AttivitaEsecMateriale atvMat) throws SQLException {

		boolean creato = false;
		VtAttivitaEsecMateriale nuovaAtvMat = (VtAttivitaEsecMateriale) Factory
				.createObject(VtAttivitaEsecMateriale.class);

		// Imposto la chiave
		nuovaAtvMat.setIdAzienda(ordEsec.getIdAzienda());
		nuovaAtvMat.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
		nuovaAtvMat.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());

		// Riga Attivita
		nuovaAtvMat.setIdRigaAttivita(idRigaAttivita);

		List atvEsecList = ordEsec.getAttivitaEsecutive();
		if (!atvEsecList.isEmpty()) {

			Iterator iteAtvEsec = atvEsecList.iterator();
			while (iteAtvEsec.hasNext()) {

				AttivitaEsecutiva atvEsec = (AttivitaEsecutiva) iteAtvEsec.next();

				if (atvEsec.getIdRigaAttivita().equals(idRigaAttivita)) {
					nuovaAtvMat.setAttivitaEsecutiva(atvEsec);
					break;
				}
			}

		}
		nuovaAtvMat.setIdRigaMateriale(idAtvMat);

		// la riga materiale moltiplicata per 10: creo la sequenza ordinamento (Short)
		Short sequenza = new Short((short) (idAtvMat.intValue() * 10));
		nuovaAtvMat.setSequenzaOrdin(sequenza);

		nuovaAtvMat.setIdArticolo(idArticolo);

		// Set descrizioni
		Articolo art = (Articolo) VtTessileUtil.getVtArticoloFromId(Azienda.getAziendaCorrente(), idArticolo);
		if (art != null) {

			nuovaAtvMat.setArticolo(art);
			nuovaAtvMat.getDescrizione().setDescrizione(art.getDescrizioneArticoloNLS().getDescrizione());
			nuovaAtvMat.getDescrizione().setDescrizioneRidotta(art.getDescrizioneArticoloNLS().getDescrizioneRidotta());

			String umMag = art.getIdUMPrmMag();
			String umTecnica = art.getIdUMTecnica();

			nuovaAtvMat.setIdUMPrmMag(umMag);

		}

		nuovaAtvMat.setIdMagazzinoPrl(idMagPrl);

		nuovaAtvMat.setCoeffImpiego(atvMat.getCoeffImpiego());
		nuovaAtvMat.setQtaRichiestaUMPrm(atvMat.getQtaRichiestaUMPrm());
		nuovaAtvMat.setQtaRichiestaUMSec(atvMat.getQtaRichiestaUMSec());

		// Aggiungo anche i lotti presenti nel primo materiale di prelievo
		if (atvMat.getLottiMateriali() != null && !atvMat.getLottiMateriali().isEmpty()) {
			Iterator iter = atvMat.getLottiMateriali().iterator();
			while (iter.hasNext()) {
				AttivitaEsecLottiMat lottoEsistente = (AttivitaEsecLottiMat) iter.next();

				AttivitaEsecLottiMat lottoNew = (AttivitaEsecLottiMat) Factory.createObject(AttivitaEsecLottiMat.class);
				lottoNew.setIdAzienda(ordEsec.getIdAzienda());
				lottoNew.setIdAnnoOrdine(ordEsec.getIdAnnoOrdine());
				lottoNew.setIdNumeroOrdine(ordEsec.getIdNumeroOrdine());
				lottoNew.setIdRigaAttivita(idRigaAttivita);
				lottoNew.setIdRigaMateriale(nuovaAtvMat.getIdRigaMateriale());
				lottoNew.setIdLotto(lottoEsistente.getIdLotto());
				lottoNew.setIdArticolo(lottoEsistente.getIdArticolo());
				lottoNew.setQtaRichiestaUMPrm(lottoEsistente.getQtaRichiestaUMPrm());
				lottoNew.setQtaRichiestaUMSec(lottoEsistente.getQtaRichiestaUMSec());

				nuovaAtvMat.getLottiMateriali().add(lottoNew);
			}
		}

		return nuovaAtvMat;
	}

	// Fix 81120 - Fine

	// Fix 81411 -Inizio
	public static synchronized void updateStatoOrigineProd(String key) {

		boolean success = false;
		String UPDATE_STATO_ORIGINE = "UPDATE " + VtAttivitaEsecProdottoTM.TABLE_NAME + " SET "
				+ VtAttivitaEsecProdottoTM.STATO_ORIG + "= 'V' " + // 07-05-2018

				" WHERE " + VtAttivitaEsecProdottoTM.ID_AZIENDA + " = '" + KeyHelper.getTokenObjectKey(key, 1) + "'"
				+ " AND " + VtAttivitaEsecProdottoTM.ID_ANNO_ORD + " = '" + KeyHelper.getTokenObjectKey(key, 2) + "'"
				+ " AND " + VtAttivitaEsecProdottoTM.ID_NUMERO_ORD + " = '" + KeyHelper.getTokenObjectKey(key, 3) + "'";
		// " AND " + AttivitaEsecutivaTM.ID_RIGA_ATTIVITA + " = " +
		// KeyHelper.getTokenObjectKey(key, 4) + "";

		CachedStatement cStatemant = new CachedStatement(UPDATE_STATO_ORIGINE);

		try {
			PreparedStatement ps = cStatemant.getStatement();
			int rs = ps.executeUpdate();
			// ConnectionManager.pushConnection();
			if (rs >= 0) {
				ConnectionManager.commit();
				success = true;
			} else {
				ConnectionManager.rollback();
				success = false;
			}
			// ConnectionManager.popConnection();
			ps.close();
			cStatemant.free();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static synchronized void creaVtOrdEsecPezze(VtOrdineEsecutivo ordEsec) {

		List lstMatricole = getMatricolePezze(ordEsec);

		Iterator iterMatricole = lstMatricole.iterator();
		while (iterMatricole.hasNext()) {
			String idMatricola = (String) iterMatricole.next();

			Object[] keyParts = { ordEsec.getIdAzienda(), ordEsec.getIdAnnoOrdine(), ordEsec.getIdNumeroOrdine(), "1",
					idMatricola };
			String chiaveOrdEsecPezze = KeyHelper.buildObjectKey(keyParts);

			VtOrdEsecPezze ordEsecPezzaEsistente = null;
			try {
				ordEsecPezzaEsistente = VtOrdEsecPezze.elementWithKey(chiaveOrdEsecPezze, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (ordEsecPezzaEsistente == null) {
				VtOrdEsecPezze ordEsecPezza = (VtOrdEsecPezze) Factory.createObject(VtOrdEsecPezze.class);

				ordEsecPezza.setIdAzienda(ordEsec.getIdAzienda());
				ordEsecPezza.setIdMatricola(idMatricola);

				ordEsecPezza.setIdAnnoOrdEsec(ordEsec.getIdAnnoOrdine());
				ordEsecPezza.setIdNumOrdEsec(ordEsec.getIdNumeroOrdine());
				ordEsecPezza.setIdRigaProdotto(new Integer("1")); // 1 fisso perche non sono collegati ad un prodotto

				boolean creato = VtTessileUtil.saveVtOrdEsecPezze(ordEsecPezza);

			}

		}

	}

	protected static List getMatricolePezze(VtOrdineEsecutivo ordEsec) {
		List lstMatricole = new ArrayList();

		String idMatricola = "";

		String select = " SELECT C.ID_MATRICOLA, B.ID_LOTTO_PRODOTTO , (ISNULL(A.QTA_RCS_UM_PRM,0) - ISNULL(A.QTA_PRL_UM_PRM,0)) AS QTA_RESIDUA "
				+ " FROM THIP.ORDESE_ATV_LTM AS A, THIP.LOTTI AS B , THIP.VT_PEZZE AS C "
				+ " WHERE A.ID_AZIENDA = B.ID_AZIENDA AND A.ID_ARTICOLO = B.ID_ARTICOLO AND A.ID_LOTTO=B.ID_LOTTO "
				+ " AND A.ID_AZIENDA = C.ID_AZIENDA AND A.ID_LOTTO = C.R_LOTTO AND A.ID_ARTICOLO = C.R_ARTICOLO " +

				" AND A.ID_AZIENDA = '" + ordEsec.getIdAzienda() + "' " + " AND A.ID_ANNO_ORD = '"
				+ ordEsec.getIdAnnoOrdine() + "' " + " AND A.ID_NUMERO_ORD = '" + ordEsec.getIdNumeroOrdine() + "' ";

		CachedStatement cInstance = new CachedStatement(select);
		ResultSet rs = null;

		try {

			rs = cInstance.executeQuery();

			while (rs.next()) {

				idMatricola = rs.getString(1);

				lstMatricole.add(idMatricola);

			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (cInstance != null)
					cInstance.free();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return lstMatricole;
	}

	// Fix 81411 - Fine

	public void passaAPatron(ClassADCollection cadc, ServletEnvironment se, DocumentoDatiSessione datiSessione,
			String azione) throws ServletException, IOException, NoSuchFieldException, NoSuchElementException { // 81903

		String url = "it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineVenditaGridActionAdapter?thClassName=VtOrdineVendita&ObjectKey="
				+ java.net.URLEncoder.encode(KeyHelper.buildObjectKey(datiSessione.getValoriChiaviDocumento())) + "&"
				+ BaseServlet.ACTION + "=" + "ORD_VEN_GROUP";

		PrintWriter out = se.getResponse().getWriter();
		out.println("<script language=\"JavaScript1.2\">");
		out.println("var url = \"" + url + "\";");
		out.println("window.top.location=url;");
		out.println("</script>");
	}

	// FIX 82134 FIRAS : TIPO APERTURA DISPONIBILITA : CUSTOM con Articolo DA
	// Matrice Nuova
	@Override
	protected void apriAnalisiDisp(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {

		HttpServletRequest req = se.getRequest();
		String idArticolo = req.getParameter("idArticolo");
		String idMagazzino = req.getParameter("idMagazzino");
		String reqType = req.getParameter("SiReqType");

		if (reqType == null || !reqType.equalsIgnoreCase(Constant.Labels.CUSTOM))
			super.apriAnalisiDisp(cadc, se);

		else {
			String selectedKey = getSelectedKeyForAction(req);
			OrdineVenditaRigaPrm riga = getObject(selectedKey);

			String keyDisp = getKeyAnalisiDispCustom(riga, idArticolo, idMagazzino, reqType);

			InquiryAnalisiDispWrapper inqDispW = new InquiryAnalisiDispWrapper();
			ParametriAnalisi parametri = inqDispW.getParamDisponibilita(keyDisp);
			if (parametri != null) {
				parametri.impostaDatiRigaOrdVen(riga, true);// Fix 16837
				se.getRequest().setAttribute(InterrogazioneMultiplaActionAdapter.OGGETTO_INTERROGAZIONE, parametri);
				String params = "?thClassName=ParametriAnalisi" + "&thAction="
						+ InterrogazioneMultiplaActionAdapter.INTERROGA +
						// fix10873
						"&thCollectorName=" + AnalisiDispDataCollector.class.getName();
				// end 10873

				// Inizio 6364
				params += "&" + NavigatoreInterrogazione.PARAM_MOD_APERTURA + "=" + NavigatoreInterrogazione.AL_VOLO;
				// Fine 6364
				se.sendRequest(getServletContext(), se.getServletPath() + OPEN_DISP_SERVLET + params, false);
			}
		}
	}

	protected String getKeyAnalisiDispCustom(OrdineVenditaRigaPrm riga, String idArticolo, String idMagazzino,
			String reqType) {
		String idAzienda = (riga == null) ? Azienda.getAziendaCorrente() : riga.getIdAzienda();

		Integer idVersione = new Integer(1);
		if (riga != null && riga.getIdVersioneSal() != null)
			idVersione = riga.getIdVersioneSal();
		Integer idConfig = Configurazione.CONFIGURAZIONE_DUMMY;
		if (riga != null && riga.getIdConfigurazione() != null)
			idConfig = riga.getIdConfigurazione();

		return KeyHelper.buildObjectKey(
				new String[] { idAzienda, idArticolo, idVersione.toString(), idConfig.toString(), idMagazzino });
	}

//FIX 82134 FINE

}
