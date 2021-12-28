package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.sql.SQLException;
// Fix Enea ini
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

// Fix Enea fin
import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.MDVManager;
import com.thera.thermfw.web.ServletEnvironment;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.cliente.ClienteVendita;
import it.thera.thip.base.documenti.web.DocumentoCambiaJSP;
import it.thera.thip.base.documenti.web.DocumentoDataCollector;
import it.thera.thip.base.documenti.web.DocumentoDatiSessione;
import it.thera.thip.base.documenti.web.DocumentoGridActionAdapter;
import it.thera.thip.base.documenti.web.DocumentoNuovo;
import it.thera.thip.base.documenti.web.DocumentoNuovoFormActionAdapter;
import it.thera.thip.base.listini.ListinoVendita;
import it.thera.thip.base.partner.Indirizzo;
import it.thera.thip.base.partner.IndirizzoPrimRose;
import it.thera.thip.tessile.cliente.VtClienteDivisione;
import it.thera.thip.tessile.cliente.VtClienteVendita;
import it.thera.thip.tessile.tabelle.VtIndirizzo;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVendita;
import it.thera.thip.vendite.generaleVE.CausaleOrdineVendita;

/*
 * Revision:
 * Number Date        Owner			 Description
 */
public class VtOrdVenNuovo extends DocumentoNuovo {

	private static final long serialVersionUID = 1L;

	public void eseguiAzioneSpecifica(ServletEnvironment se, ClassADCollection cadc, DocumentoDataCollector docBODC,
			DocumentoDatiSessione datiSessione) throws ServletException, IOException, SQLException {
		if (datiSessione == null) {
			datiSessione = DocumentoDatiSessione.getDocumentoDatiSessione(se);
		}
		// se non ho indicato un ordine a disporre eseguo l'azione specifica standard

		String idSerie = getStringParameter(se.getRequest(), "NumeratoreHandler.IdSerie");
		String azi = getStringParameter(se.getRequest(), "IdAzienda");
		String ann = getStringParameter(se.getRequest(), "AnnoOrdineAdisp");
		String num = getStringParameter(se.getRequest(), "NumeroOrdAdisp");
		String indirizzo = getStringParameter(se.getRequest(), "IdSequenzaIndirizzo");

		if (ann == null || ann.equals("") || num == null || num.equals("")) {
			super.eseguiAzioneSpecifica(se, cadc, docBODC, datiSessione);
			// aggiorna serie per successivo controllo se ordine a disporre o meno
			aggiornaSerie(se, cadc, docBODC, datiSessione, idSerie);

			// Fix 81103 - inizio
			String cliente = getStringParameter(se.getRequest(), "Cliente");
			String rDivCliente = getStringParameter(se.getRequest(), "RDivisioneCliente");
			aggiornaRespVendite(se, cadc, docBODC, datiSessione, cliente, rDivCliente);
			// Fix 81103 - Fine
			aggiornaMagTrasferimento(se, cadc, docBODC, datiSessione, cliente, indirizzo);

			return;
		}

		// se indicato ordini a disporre valorizzo campi nuova testata con quelli
		// dell'ordine a disporre
		VtOrdineVendita ort = null;
		String keyOt = KeyHelper.buildObjectKey(new String[] { azi, ann, num });
		try {
			ort = (VtOrdineVendita) VtOrdineVendita.elementWithKey(VtOrdineVendita.class, keyOt,
					PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		docBODC.initialize(cadc.getClassName(), true, getCurrentLockType(se));

		// Fix 12962 inizio
		boolean mdvPresent = MDVManager.isMDVPresent(docBODC);
		if (mdvPresent)
			MDVManager.setMDVValues(se.getRequest().getParameter("thMDV"), se.getRequest().getParameter("MDVFilter"),
					docBODC);
		// Fix 12962 fine

		// impostazione dei valori provenienti da finestra di nuovo
		setValues(cadc, docBODC, se);

		// Fix 09084 FR Inizio
		// docBODC.completaDocumento(); sposto completaDocumento() dopo il primo check
		docBODC.setNuovoDocumento(true);
		// Fix 09084 FR Fine

		// Fix Enea ini
		// if(docBODC.check() != BODataCollector.OK)
		if (docBODC.check() == BODataCollector.ERROR) {
			se.addErrorMessages(docBODC.getErrorList().getErrors());
		}
		// Fix Enea fin

		if (se.isErrorListEmpity()) {
			// Fix 09084 FR Inizio
			docBODC.setNuovoDocumento(false);
			docBODC.completaDocumento();
			// Fix 09084 FR Fine

			// fix 80563 - Se indicato ordine a disporre (passo di qui) e valorizzo nuova
			// testata uguale a quella ordine a disporre
			// ad esclusione dei dati del pannello che li salvo prima
			// con questo sistema aggiorno i singoli campi della testata e poi salvo
			VtOrdineVendita ov = (VtOrdineVendita) docBODC.getBo();
//          ort.setRSettore(ov.getRSettore());
//          ort.setIdDivisione(ov.getIdDivisione());
//          ort.setIdCliente(ov.getIdCliente());
//          ort.setRDivisioneCliente(ov.getRDivisioneCliente());
//          ort.setIdCau(ov.getIdCau());
//          ort.setRLineaVen(ov.getRLineaVen());
//          ort.setRStagione(ov.getRStagione());
//          
//          // ricavo i nuovi numero/data ordine
//          //String serie = ov.
//          String anno = ov.getAnnoDocumento();
//          String numero = ov.getNumeroDocumento();
//          String nuf = ov.getNumeroDocumentoFormattato();
//          Date dataOrd = ov.getDataDocumento();
//          
//          // ricavo i riferimenti ordine a disporre
//          String annoD = ort.getAnnoDocumento();
//          String numD = ort.getNumeroDocumento();
//          
//          // ri-aggiorno i riferimenti nuovo ordine  
//          ort.setAnnoDocumento(anno);
//          ort.setNumeroDocumento(numero);
//          ort.setNumeroDocumentoFormattato(nuf);
//          ort.setDataDocumento(dataOrd);
//          
//          // aggiorno i riferimenti all'ordine a disporre
//          ort.setAnnoOrdineAdisp(annoD);
//          ort.setNumeroOrdAdisp(numD);

			// Fix 81900 - Se il cliente è uguale aggiornare i campi altrimenti no
			if (ov.getIdCliente().equals(ort.getIdCliente())) {

				// Aggiorno su nuovo ordine i campi dall'ordine a disporre (i principali
				// gestibili a pannello)
				ov.setTipoDestinatario(ort.getTipoDestinatario());
				ov.setIdSequenzaInd(ort.getIdSequenzaInd());
				ov.setListinoPrezzi(ort.getListinoPrezzi());
				ov.setPrcScontoIntestatario(ort.getPrcScontoIntestatario());
				ov.setPrcScontoModalita(ort.getPrcScontoModalita());
				ov.setPrcScontoFineFattura(ort.getPrcScontoFineFattura());
				ov.setScontoTabellare(ort.getScontoTabellare());
				ov.setRifDataPerPrezzoSconti(ort.getRifDataPerPrezzoSconti());
				// ov.setRicalcoloPrzScontiFattura(ort.getRicalcoloPrzScontiFattura()); //
				// deprecato
				ov.setResponsabileVendite(ort.getResponsabileVendite());
				ov.setAgente(ort.getAgente());
				ov.setProvvigioneAgente(ort.getProvvigioneAgente());
				ov.setDifferenzaPrezzoAgente(ort.hasDifferenzaPrezzoAgente());
				ov.setSubagente(ort.getSubagente());
				ov.setProvvigioneSubagente(ort.getProvvigioneSubagente());
				ov.setDifferenzaPrezzoSubagente(ort.hasDifferenzaPrezzoSubagente());
				ov.setTipoLiquidazionePrv(ort.getTipoLiquidazionePrv());
				ov.setValuta(ort.getValuta());
				ov.setAssoggettamentoIVA(ort.getAssoggettamentoIVA());
				ov.setSpesa1(ort.getSpesa1());
				ov.setSpesa2(ort.getSpesa2());
				ov.setModalitaPagamento(ort.getModalitaPagamento());
				ov.setDataInizioPagamento(ort.getDataInizioPagamento());
				ov.setLingua(ort.getLingua());
				ov.getIdentificativoBanca().setIdABI(ort.getIdentificativoBanca().getIdABI());
				ov.getIdentificativoBanca().setIdCAB(ort.getIdentificativoBanca().getIdCAB());
				ov.getIdentificativoBanca().setCodificaSIA(ort.getIdentificativoBanca().isCodificaSIA());
				ov.setContoCorrente(ort.getContoCorrente());
				ov.setDataConsegnaRichiesta(ort.getDataConsegnaRichiesta());
				ov.setDataConsegnaConfermata(ort.getDataConsegnaConfermata());
				ov.setDataConsegnaProduzione(ort.getDataConsegnaProduzione());
				ov.setSettConsegnaRichiesta(ort.getSettConsegnaRichiesta());
				ov.setSettConsegnaConfermata(ort.getSettConsegnaConfermata());
				ov.setSettConsegnaProduzione(ort.getSettConsegnaProduzione());
				ov.setMagazzino(ort.getMagazzino());

				ov.setMagazzinoTrasferimento(ort.getMagazzinoTrasferimento());

				ov.setCommessa(ort.getCommessa());
				ov.setCentroCosto(ort.getCentroCosto());
				ov.setFornitore(ort.getFornitore());
				ov.setTipoEvasioneOrdine(ort.getTipoEvasioneOrdine());
				ov.setPriorita(ort.getPriorita());
				ov.setRaggruppamentoOrdBolla(ort.getRaggruppamentoOrdBolla());
				ov.setMotivoBloccoEvasione(ort.getMotivoBloccoEvasione());
				ov.setMotivoBloccoPianif(ort.getMotivoBloccoPianif());
				ov.setCadenzaConsegna(ort.getCadenzaConsegna());
				ov.setGiroConsegne(ort.getGiroConsegne());
				ov.setModalitaSpedizione(ort.getModalitaSpedizione());
				ov.setDescrModalitaSpedizione(ort.getDescrModalitaSpedizione());
				ov.setModalitaConsegna(ort.getModalitaConsegna());
				ov.setDescrModalitaConsegna(ort.getDescrModalitaConsegna());
				ov.setAspettoEsteriore(ort.getAspettoEsteriore());
				ov.setDescrAspettoEsteriore(ort.getDescrAspettoEsteriore());
				ov.setCausaleTrasporto(ort.getCausaleTrasporto());
				ov.setDescrCausaleTrasporto(ort.getDescrCausaleTrasporto());
				ov.setVettore1(ort.getVettore1());
				ov.setVettore2(ort.getVettore2());
				ov.setVettore3(ort.getVettore3());
				ov.setDescrVettore1(ort.getDescrVettore1());
				ov.setDescrVettore2(ort.getDescrVettore2());
				ov.setDescrVettore3(ort.getDescrVettore3());
				ov.setPesoLordo(ort.getPesoLordo());
				ov.setPesoNetto(ort.getPesoNetto());
				ov.setVolume(ort.getVolume());
				ov.setRicalcolaPesiEVolume(ort.isRicalcolaPesiEVolume());
				ov.setClienteFatturazione(ort.getClienteFatturazione());
				ov.setCondizioneFatturazione(ort.getCondizioneFatturazione());
				ov.setFatturazionePeriodica(ort.getFatturazionePeriodica());
				ov.setFatturazioneSospesa(ort.getFatturazioneSospesa());
				ov.setRaggruppamentoBolleFat(ort.getRaggruppamentoBolleFat());
				ov.setCategoriaVenditaCliente(ort.getCategoriaVenditaCliente());
				ov.setCategoriaContabile(ort.getCategoriaContabile());
				ov.setGruppoContiAnalitica(ort.getGruppoContiAnalitica());
			}
			// Fix 81900

			// salvo il numero ordine a disporre recuperato
			ov.setAnnoOrdineAdisp(ort.getAnnoDocumento());
			ov.setNumeroOrdAdisp(ort.getNumeroDocumento());
			ov.setAlfanumRiservatoUtente2(idSerie);

			// riaggiorno il BO con i campi presi dall'ordine a disporre
			docBODC.setBo(ov);

			// oppure potrei riportare tutto l'ordine a disporre (ma dovrei considerare ciò
			// che eventualmente è modificato nel pannello NUOVO Ordine)
			// docBODC.setBo(ort);

			// fix 80563 - fine

			// rimozione vecchio oggetto in sessione
			/*
			 * String chiaveDocInSessione = getStringParameter(se.getRequest(),
			 * DocumentoDataCollector.CHIAVE_DOC_SESSIONE); if(chiaveDocInSessione != null)
			 * se.getSession().removeAttribute(chiaveDocInSessione);
			 */
			// completamento documento (da dati di causale et altri)
			// aggiorna datacollector secondo causale
			docBODC.impostaSecondoCausale();
			// controllo secondo causale

			// Fix Enea ini
			// Inizio 4513
			boolean erroriPresenti = docBODC.check() != BODataCollector.OK;
			// Fine 4513

			// boolean erroriPresenti = docBODC.check() == BODataCollector.ERROR;
			// Inizio 4513
			if (!erroriPresenti) {
				docBODC.getErrorList().removeAllErrors();
			}
			// Fine 4513
			// Fix Enea fin

			String azione = getAzione(se);
			if (erroriPresenti || azione.equals(DocumentoNuovoFormActionAdapter.OK_TESTATA)) {
				se.addErrorMessages(docBODC.getErrorList().getErrors());
				salvaDocumentoInSessione(se, docBODC, datiSessione);
				String parAdd = "&" + DocumentoGridActionAdapter.NON_PASSA_DA_NUOVO + "=true";
				se.getRequest().setAttribute(DocumentoCambiaJSP.PARAMETRI_ADDIZIONALI, parAdd);
				richiediCambiamentoJSP(se, docBODC, datiSessione, erroriPresenti);
			} else {
				salvaDocumento(se, docBODC);
				// Fix Enea ini
				Iterator iErrDocBODC = docBODC.getErrorList().getErrors().iterator();
				char statoErrRig = ErrorMessage.INFORMATION;
				while (iErrDocBODC.hasNext()) {
					ErrorMessage errMess = (ErrorMessage) iErrDocBODC.next();
					if (errMess.getSeverity() == ErrorMessage.ERROR) {
						statoErrRig = ErrorMessage.ERROR;
					} else if (statoErrRig != ErrorMessage.ERROR && errMess.getSeverity() == ErrorMessage.WARNING) {
						statoErrRig = ErrorMessage.WARNING;
					}
				}

//            if(se.isErrorListEmpity()) {
				// Fix 07214 inizio Chakhari Abdelaziz 11-05-2007
				// if(statoErrRig != docBODC.ERROR) {
				if (statoErrRig != ErrorMessage.ERROR) {
					// Fix 07214 fine
//               docBODC.getErrorList().removeAllErrors();
					// Fix Enea fin
					se.getRequest().setAttribute(AGGIORNA_GRIGLIA, "Y");
					richiediCambiamentoJSP(se, docBODC, datiSessione, erroriPresenti);
				} else {
					closeAction(docBODC, se);
					se.sendRequest(getServletContext(), "com/thera/thermfw/common/ErrorListHandler.jsp", true);
				}
			}
		} else {
			closeAction(docBODC, se);
			se.sendRequest(getServletContext(), "com/thera/thermfw/common/ErrorListHandler.jsp", true);
		}
	}

//Fix 81103 - Inizio
	public void aggiornaRespVendite(ServletEnvironment se, ClassADCollection cadc, DocumentoDataCollector docBODC,
			DocumentoDatiSessione datiSessione, String cliente, String rDivCliente) {
		docBODC.initialize(cadc.getClassName(), true, getCurrentLockType(se));

		// Fix 81052 - Inizio
		boolean erroriPresenti = docBODC.check() != BODataCollector.OK;

		if (!erroriPresenti) {
			VtOrdineVendita ov = (VtOrdineVendita) docBODC.getBo();
			docBODC.setBo(ov);

			// ClienteVendita cliVen =
			// (ClienteVendita)PersistentObject.elementWithKey(ClienteVendita.class,
			// Azienda.getAziendaCorrente()+KeyHelper.KEY_SEPARATOR+cliente,
			// PersistentObject.NO_LOCK);
			VtClienteVendita cliVen = null;
			try {
				cliVen = (VtClienteVendita) PersistentObject.elementWithKey(VtClienteVendita.class,
						Azienda.getAziendaCorrente() + KeyHelper.KEY_SEPARATOR + cliente, PersistentObject.NO_LOCK);

				List lstDivisioni = cliVen.getVtClienteDivisione();

				Iterator iterDivisioni = lstDivisioni.iterator();

				while (iterDivisioni.hasNext()) {
					// VtClienteVenditaDivisione divCli =
					// (VtClienteVenditaDivisione)iterDivisioni.next();

					VtClienteDivisione divCli = (VtClienteDivisione) iterDivisioni.next();

					String a = divCli.getIdDivisioneCliente();
					if (divCli.getIdDivisioneCliente().equals(rDivCliente)) {
						ov.setIdResponVendite(divCli.getRResponVendite());
						ov.setROpeComm(divCli.getROperatComm());
					}

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// List lstDivisioni = cliVen.getDivisioni();

			// Fix 80820 - Inizio
			String azione = getAzione(se);
			if (azione.equals(DocumentoNuovoFormActionAdapter.OK_RIGHE)) {
				saveOrdVen(ov);
			}
		}
	}

//Fix 81103 - Fine

//
	public void aggiornaMagTrasferimento(ServletEnvironment se, ClassADCollection cadc, DocumentoDataCollector docBODC,
			DocumentoDatiSessione datiSessione, String cliente, String idSeqIndirizzo) {
		docBODC.initialize(cadc.getClassName(), true, getCurrentLockType(se));

		boolean erroriPresenti = docBODC.check() != BODataCollector.OK;

		//if (!erroriPresenti) {
			VtOrdineVendita ov = (VtOrdineVendita) docBODC.getBo();
			docBODC.setBo(ov);

			// ClienteVendita cliVen =
			// (ClienteVendita)PersistentObject.elementWithKey(ClienteVendita.class,
			// Azienda.getAziendaCorrente()+KeyHelper.KEY_SEPARATOR+cliente,
			// PersistentObject.NO_LOCK);
			VtClienteVendita cliVen = null;
			try {

				cliVen = (VtClienteVendita) PersistentObject.elementWithKey(VtClienteVendita.class,
						Azienda.getAziendaCorrente() + KeyHelper.KEY_SEPARATOR + cliente, PersistentObject.NO_LOCK);

				if (idSeqIndirizzo != null && !idSeqIndirizzo.equals("")) {
					CausaleOrdineVendita causale = ov.getCausale();

					String keyIndirizzo = KeyHelper
							.buildObjectKey(new String[] { String.valueOf(cliVen.getIdAnagrafico()), idSeqIndirizzo });

					VtIndirizzo ind = (VtIndirizzo) PersistentObject.elementWithKey(VtIndirizzo.class, keyIndirizzo,
							PersistentObject.NO_LOCK);

					Magazzino magTrasferimento = null;

					if (ind != null  ) {
						if(!ind.getMagDestin().equals(""))
						{
						String keyMagazzino = KeyHelper
								.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), ind.getMagDestin() });

						magTrasferimento = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, keyMagazzino,
								PersistentObject.NO_LOCK);
						}
						if(ind.getPriorita()!=null)
							ov.setPriorita(ind.getPriorita());
					}

					if (causale != null && causale.isTrasferimentoTraMag() && magTrasferimento != null) {
						ov.setMagazzinoTrasferimento(magTrasferimento);
					}
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String azione = getAzione(se);
			if (azione.equals(DocumentoNuovoFormActionAdapter.OK_RIGHE)) {
				saveOrdVen(ov);
			}
		//}
	}

	/**
	 * Aggiorno il codice serie da utilizzare poi in FormActionAdapter (nel caso di
	 * nuovo ordine)
	 * 
	 * @param se
	 * @param cadc
	 * @param docBODC
	 * @param datiSessione
	 * @param idSerie
	 * @throws ServletException
	 * @throws IOException
	 * @throws SQLException
	 */
	public void aggiornaSerie(ServletEnvironment se, ClassADCollection cadc, DocumentoDataCollector docBODC,
			DocumentoDatiSessione datiSessione, String idSerie) throws ServletException, IOException, SQLException {
		docBODC.initialize(cadc.getClassName(), true, getCurrentLockType(se));

		// Fix 81052 - Inizio
		boolean erroriPresenti = docBODC.check() != BODataCollector.OK;

		if (!erroriPresenti) {
			// Fix 81052 - Fine

			VtOrdineVendita ov = (VtOrdineVendita) docBODC.getBo();
			ov.setAlfanumRiservatoUtente2(idSerie);

			// Fix inizio 80808
			ov.getRStagione();

			Object[] key = { Azienda.getAziendaCorrente(), ov.getRStagione() };
			String keyLst = KeyHelper.buildObjectKey(key);

			ListinoVendita lstVendita = null;
			try {
				lstVendita = (ListinoVendita) PersistentObject.elementWithKey(ListinoVendita.class, keyLst,
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (lstVendita != null)
				ov.setIdListino(lstVendita.getIdListino());

			if (ov.getIdAgente() == null)
				ov.setIdAgente(ov.getCliente().getIdAgente());
			if (ov.getIdResponVendite() == null)
				ov.setIdResponVendite(ov.getCliente().getIdResponsabileVendite());

			// Fix fine 80808

			docBODC.setBo(ov);

			// Fix 80820 - Inizio
			String azione = getAzione(se);
			if (azione.equals(DocumentoNuovoFormActionAdapter.OK_RIGHE)) {
				saveOrdVen(ov);
			}
			// Fix 80820 - Fine
		}
	}

	public String getAzioneDopoCambio(ServletEnvironment se, boolean erroriPresenti)
			throws ServletException, IOException, SQLException {
		String azione = getAzione(se);
		if (erroriPresenti)
			return DocumentoGridActionAdapter.NEW;
		else {
			if (azione.equals(DocumentoNuovoFormActionAdapter.OK_TESTATA))
				return DocumentoGridActionAdapter.NEW;
			else
				return DocumentoGridActionAdapter.UPDATE_RIGHE;
		}
	}

	public static boolean saveOrdVen(VtOrdineVendita ordVen) {
		boolean success = false;
		if (ordVen != null) {
			int rt = 0;
			try {
				ConnectionManager.pushConnection();
				rt = ordVen.save();
				if (rt > 0) {
					ConnectionManager.commit();
					success = true;
				} else {
					ConnectionManager.rollback();
					success = false;
				}
				ConnectionManager.popConnection();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

}
