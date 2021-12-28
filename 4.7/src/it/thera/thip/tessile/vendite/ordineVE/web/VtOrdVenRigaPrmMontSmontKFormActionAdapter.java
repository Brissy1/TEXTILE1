package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;

import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.type.DecimalType;
import com.thera.thermfw.web.ServletEnvironment;

import it.thera.thip.acquisti.documentoAC.DocumentoAcqRigaPrm;
import it.thera.thip.acquisti.documentoAC.DocumentoAcquisto;
import it.thera.thip.acquisti.generaleAC.CausaleDocumentoTestataAcq;
import it.thera.thip.acquisti.generaleAC.CausaleOrdineTestataAcq;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.articolo.VistaDisponibilita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.base.documenti.web.DocumentoDatiSessione;
import it.thera.thip.base.fornitore.Fornitore;
import it.thera.thip.base.fornitore.FornitoreAcquisto;
import it.thera.thip.base.generale.Numeratore;
import it.thera.thip.base.generale.NumeratoreException;
import it.thera.thip.base.generale.NumeratoreHandler;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.generale.UnitaMisura;
import it.thera.thip.datiTecnici.modpro.AttivitaProdMateriale;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivo;
import it.thera.thip.logis.lgb.RigaOrdine;
import it.thera.thip.magazzino.documenti.CausaleDocumentoGen;
import it.thera.thip.magazzino.documenti.CausaleDocumentoTrasf;
import it.thera.thip.magazzino.documenti.DocMagGenerico;
import it.thera.thip.magazzino.documenti.DocMagGenericoRiga;
import it.thera.thip.magazzino.documenti.DocMagTrasferimento;
import it.thera.thip.qualita.base.PersDatiSGQ;
import it.thera.thip.tessile.acquisti.VtMontSmontKit;
import it.thera.thip.tessile.acquisti.VtMontSmontKitNew;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcqRigaPrm;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcqRigaSec;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcquisto;
//import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcquisto;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.base.fornitore.VtFornitore;
import it.thera.thip.tessile.datiTecnici.modpro.VtAttivitaProduttiva;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagGenerico;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagTrasferimento;
//import it.thera.thip.tessile.magazzino.documenti.VtDocMagGenerico;
//import it.thera.thip.tessile.magazzino.documenti.VtDocMagTrasferimento;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagTrasferimentoRiga;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.vendite.ordineVE.VT_TipoMontSmontKit;
//import it.thera.thip.tessile.vendite.ordineVE.VtDocKitOrdVenRiga;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVendita;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrm;

import it.thera.thip.tessile.vendite.ordineVE.VTMontSmontUtility;
//import it.thera.thip.tessile.vendite.ordineVE.VTMontSmontUtility.TypeDoc;

public class VtOrdVenRigaPrmMontSmontKFormActionAdapter extends VtOrdineVenditaRigaPrmRidottaFormActionAdapter {

	public static final String CLASS_NAME = "thClassName";
	public static final String MONT = "MONT";
	public static final String SMONT = "SMONT";
	public static final String SMONT_RIGHE = "SMONT_RIGHE";
	public static final String KEY_PAR1_SERIE_MAG = "pers.serie.docmag.pezze";
	public static final String KEY_PAR2_SERIE_MAG = "Serie documento magazzino";
	public static final String KEY_PAR2_TRASF_SERIE_MAG = "Serie documento magazzino trasferimento";
	public static final String FUNZIONE_DISP_MAG = "SiVistaDispMag";
	public static final String PARAM_DISP_MAG = "CodiceVista";
	String cauMontInt = "";
	String cauSmontInt = "";
	String cauCaricoInt = "";
	String cauScaricoInt = "";
	String cauTrasfEst = "";
	String cauTrasfInt = "";
	String cauTrasfEstMont = "";
	String cauTrasfIntMont = "";
	String cauTrasfEstSmont = "";
	String cauTrasfIntSmont = "";
	String serieDocAcqCompEst = "";
	String cauTrasfMerce = "";
	String vistaDisponKey = "";
	String codFornitore = null;
	List listaErrori = new ArrayList();
	String[] errore = null;

	public void processAction(ServletEnvironment se) throws ServletException, IOException {

		/*
		 * super.processAction(se); invokeAlertWindow(se,"CIAO", true); return;
		 */

		retrieveValues();
		String azione = getStringParameter(se.getRequest(), "thAction").toUpperCase();

		listaErrori.clear();

		Trace.println("======================AZIONE=" + azione);

		if (azione.equals(MONT)) {

			apriGestioneMontaggio(se);

		} else if (azione.equals(SMONT)) {

			apriGestioneSmontaggio(se);

		} else if (azione.equals(SMONT_RIGHE)) {

			apriGestioneSmontaggioRighe(se);

		}

		if (listaErrori != null) {
			Iterator iter = listaErrori.iterator();
			String strErroti = "";
			while (iter.hasNext()) {
				String em = (String) iter.next();
				strErroti = strErroti + "\\r  - " + em;
			}
			invokeAlertWindow(se, "ATTENZIONE: " + ((azione.equals(MONT)) ? "Montaggio" : "Smontaggio")
					+ " Kit fallito,si sono verificati i seguenti errori: " + strErroti, true);
		}

	}

	public void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {

		super.otherActions(cadc, se);

	}

	public void apriGestioneMontaggio(ServletEnvironment se) throws ServletException, IOException {

		String tesKey = null;
		String rigaKey = null;
		String qta = getStringParameter(se.getRequest(), "QtaKit");
		String magPart = getStringParameter(se.getRequest(), "IdMagazzinoPart");
		String magKit = getStringParameter(se.getRequest(), "IdMagazzinoKit");
		String idArt = getStringParameter(se.getRequest(), "IdArticolo");
		char tipo = getStringParameter(se.getRequest(), "TipoMontSmont").charAt(0);
		String azienda = getStringParameter(se.getRequest(), "IdAzienda");
		String annoOrd = getStringParameter(se.getRequest(), "AnnoOrdine");
		String numOrd = getStringParameter(se.getRequest(), "NumeroOrdine");
		String rigaOrd = getStringParameter(se.getRequest(), "RigaOrdine");
		String cauKey = "";

		if (qta == null || qta.equals("") || qta.equals("null"))
			qta = "0.00";

		BigDecimal qtariga = new BigDecimal(qta.replace(',', DecimalType.getThousandsSeparator()));

		tesKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd });

		rigaKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd, rigaOrd });

		String fornitoreKey = KeyHelper.buildObjectKey(new String[] { azienda, codFornitore });

		VtOrdineVendita tesO = null;
		VtOrdineVenditaRigaPrm rigaO = null;
		// VtDocKitOrdVenRiga vista=null;

		try {
			tesO = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
					PersistentObject.NO_LOCK);
			rigaO = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, rigaKey,
					PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (tesO != null && rigaO != null) {
			if (idArt == null || idArt.equals(""))
				idArt = rigaO.getIdArticolo();

			VtArticolo art = VtTessileUtil.getVtArticoloFromId(azienda, idArt);

			if (art != null) {
				Iterator compIter = getComponenti(se, art.getIdArticolo(), vistaDisponKey, null, null).iterator();
				if (compIter != null && compIter.hasNext()) {
					int i = -1;
					if (tipo == VT_TipoMontSmontKit.INTERNO) {
						// ******** MONTAGGIO INTERNO *******************

						CausaleDocumentoGen causDoc = null;
						DocMagGenerico docTestata = null;
						try {

							cauKey = KeyHelper.buildObjectKey(new String[] { azienda, cauMontInt });
							causDoc = (CausaleDocumentoGen) PersistentObject.elementWithKey(CausaleDocumentoGen.class,
									cauKey, PersistentObject.NO_LOCK);

							docTestata = creaDocumentoMagazzinoTestata(azienda, art, causDoc, magPart,
									rigaO.getAnnoDocumento(), rigaO.getNumeroDocumento(),
									((rigaO.getGruppoTC() != null) ? String.valueOf(rigaO.getGruppoTC()) : ""),
									String.valueOf(rigaO.getNumeroRigaDocumento()));

							i = docTestata.save();

						} catch (SQLException e) {
							if (causDoc == null)
								listaErrori.add(" causale non trovata (cod:" + cauKey + ");");
							else
								listaErrori.add(" impossibile salvare la testata documento di magazzino;");
							e.printStackTrace();
						}
						if (i >= 0) {
							try {

								DocMagGenericoRiga riga = creaDocMagGenericoRiga(docTestata, art, cauCaricoInt, magPart,
										qtariga);

								i = riga.save();
								List listaComMagEst = new ArrayList();
								List listaComMagInt = new ArrayList();

								while (compIter.hasNext()) {
									String strComp = (String) compIter.next();
									String strQta = strComp.split("-")[2];
									String idMagComp = strComp.split("-")[1];
									VtArticolo artComp = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class,
											strComp.split("-")[0], PersistentObject.NO_LOCK);

									BigDecimal qtacomp = new BigDecimal(
											strQta.replace(',', DecimalType.getThousandsSeparator()));
									riga = creaDocMagGenericoRiga(docTestata, artComp, cauScaricoInt, magPart, qtacomp);

									i = riga.save();

									// verifico se il componente è caricato su un magazzino esterno o interno
									// diverso dal mag partenza
									String keymag = KeyHelper.buildObjectKey(new String[] { azienda, idMagComp });
									Magazzino magComp = (Magazzino) PersistentObject.elementWithKey(Magazzino.class,
											keymag, Magazzino.NO_LOCK);

									String keyComp = magComp.getIdMagazzino() + "-" + artComp.getKey() + "-" + qtacomp
											+ "-" + magComp.getIdFornitoreAzienda();
									if (magComp.getIdFornitoreAzienda() != null) {
										listaComMagEst.add(keyComp);
									} else if (!idMagComp.equals(magPart)) {
										listaComMagInt.add(keyComp);
									}
								}

								// Creare documento magazzino di trasferimento per componenti su mag interni !=
								// mag partenza
								if (listaComMagInt != null && listaComMagInt.size() > 0)
									CreaDocCompInt(listaComMagInt, azienda, magPart, art, "", "", "", cauTrasfInt,
											rigaO);

								// creare il documento di acquisto per componenti richiesti su magazzino esterno

								if (listaComMagEst != null && listaComMagEst.size() > 0)
									CreaDocAcqTraCompEst(listaComMagEst, azienda, magPart, "", "", "", cauTrasfIntMont,
											rigaO);

								if (i >= 0) {
									ConnectionManager.commit();
									invokeAlertWindow(se, "Montaggio Kit interno eseguito con successo! ", true);

								} else {
									ConnectionManager.rollback();
									listaErrori.add(" errore in fase di salvataggio della riga documento magazzino;");

								}
							} catch (SQLException e) {
								listaErrori.add(" impossibile creare la riga documento magazzino;");
								e.printStackTrace();
							}
						} else
							listaErrori.add(" impossibile creare il documento magazzino testata;");

					} else {
						// ********* MONTAGGIO ESTERNO ***************

						int res = -1;

						String kitKey;
						String magAZ = "", magComp = "", magCL = "";
						magAZ = magPart;
						magCL = magKit;

						DocumentoAcquisto testata = null;
						DocumentoAcquisto testataCL = null;
						// DocMagTrasferimento testataTrasf = null;
						kitKey = KeyHelper.buildObjectKey(new String[] { azienda, magAZ, magCL });
						VtMontSmontKitNew kit = null;
						try {
							kit = (VtMontSmontKitNew) PersistentObject.elementWithKey(VtMontSmontKitNew.class, kitKey,
									PersistentObject.NO_LOCK);
						} catch (SQLException e) {
							listaErrori.add(" kit non trovato - dati di configurazione non impostati (magAz= " + magAZ
									+ ", magCL= " + magCL + ");");
							e.printStackTrace();
						}

						if (kit != null) {
							String fornitoreKeyCL = KeyHelper
									.buildObjectKey(new String[] { azienda, kit.getRFornitore() });
							VtFornitore forn = VTMontSmontUtility.getFornitoreFromMag(azienda, magPart);

							VtDocumentoAcqRigaPrm rigaDocAcqCL = null;
							try {
								testata = creaDocumentoAcquistoTestata(azienda, forn.getKey(), art, cauTrasfEst,
										magPart, "",
										((rigaO.getGruppoTC() != null) ? String.valueOf(rigaO.getGruppoTC()) : ""),
										rigaO.getAnnoDocumento(), rigaO.getNumeroDocumento(),
										String.valueOf(rigaO.getNumeroRigaDocumento()));

								res = testata.save();

								/*
								 * testataTrasf = creaDocMagazTrasferimentoTestata(azienda, art, cauTrasfEst,
								 * magCL, rigaO, kit.getRFornitore(), "", "", ""); res = testataTrasf.save();
								 */
								testataCL = creaDocumentoAcquistoTestata(azienda, fornitoreKeyCL, art,
										kit.getRCauDocAcqMont(), magPart, "",
										((rigaO.getGruppoTC() != null) ? String.valueOf(rigaO.getGruppoTC()) : ""),
										rigaO.getAnnoDocumento(), rigaO.getNumeroDocumento(),
										String.valueOf(rigaO.getNumeroRigaDocumento()));
								res = testataCL.save();

								rigaDocAcqCL = creaDocumentoAcquistoRigaPrm(testataCL, kit.getRCauCaricoMont(), "",
										qtariga, art, magPart);
								res = rigaDocAcqCL.save();

							} catch (SQLException e) {
								res = -1;
								listaErrori.add(" impossibile creare la testata documenti;");
								e.printStackTrace();
							}

							if (res >= 0) {
								// Iterator compIter = getComponenti(se, art.getIdArticolo(),
								// vistaDisponKey).iterator();
								boolean testataHasRow = false;

								List listaComMagEst = new ArrayList();
								List listaComMagInt = new ArrayList();

								while (compIter.hasNext()) {
									String strComp = (String) compIter.next();

									// SCARICO I COMPONENTI DAL MAGAZZINO INDICATO AL MAGAZZINO AZIENDALE
									magComp = strComp.split("-")[1];
									String strQta = strComp.split("-")[2];
									VtArticolo artComp = null;
									try {
										artComp = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class,
												strComp.split("-")[0], PersistentObject.NO_LOCK);
									} catch (SQLException e) {
										listaErrori.add(" articolo componente non trovato (cod:" + strComp.split("-")[0]
												+ ");");

										e.printStackTrace();
									}

									BigDecimal qtacomp = new BigDecimal(
											strQta.replace(',', DecimalType.getThousandsSeparator()));
									try {

										if (qtacomp.compareTo(new BigDecimal("0")) > 0) {
											// diverso dal mag partenza
											String keymag = KeyHelper.buildObjectKey(new String[] { azienda, magComp });
											Magazzino magCompobj = (Magazzino) PersistentObject
													.elementWithKey(Magazzino.class, keymag, Magazzino.NO_LOCK);
											if (!magComp.equals(magPart)
													&& magCompobj.getIdFornitoreAzienda() != null) {

												VtDocumentoAcqRigaPrm rigaDocAcq = creaDocumentoAcquistoRigaPrm(testata,
														cauTrasfEst, "", qtacomp, artComp, magPart);

												res = rigaDocAcq.save();
												testataHasRow = true;
											}

											// add righe componenti

											VtDocumentoAcqRigaPrm rigaDocAcqComp = creaDocumentoAcquistoRigaPrm(
													testataCL, kit.getRCauScaricoMont(), "", qtacomp, artComp, magPart);
											res = rigaDocAcqComp.save();

											// verifico se il componente è caricato su un magazzino esterno o interno

											String keyComp = magCompobj.getIdMagazzino() + "-" + artComp.getKey() + "-"
													+ qtacomp + "-" + magCompobj.getIdFornitoreAzienda();
											if (magCompobj.getIdFornitoreAzienda() != null) {
												listaComMagEst.add(keyComp);
											} else if (!magComp.equals(magPart)) {
												listaComMagInt.add(keyComp);
											}

										}

									} catch (SQLException e) {
										listaErrori.add(" impossibile creare la riga documento; ");
										e.printStackTrace();
									}
								}

								try {

									if (!testataHasRow)
										res = testata.delete();
									// testata.save();
									// Creare documento magazzino di trasferimento per componenti su mag interni !=
									// mag partenza
									if (listaComMagInt != null && listaComMagInt.size() > 0)
										CreaDocCompInt(listaComMagInt, azienda, magPart, art, "", "", "",
												cauTrasfEstMont, rigaO);
									/*
									 * if (listaComMagEst != null && listaComMagEst.size() > 0)
									 * CreaDocAcqTraCompEst(listaComMagEst, azienda, magPart, "", "", "",
									 * cauTrasfEstSmont, rigaO);
									 */
									if (res >= 0) {

										ConnectionManager.commit();

										invokeAlertWindow(se, "Montaggio Kit esterno eseguito con successo! ", true);

									} else {
										ConnectionManager.rollback();
										listaErrori.add(" impossibile inserire riga documento;");

									}
								} catch (SQLException e) {
									listaErrori.add(" si è verificato un errore in fase di salvataggio riga documento");
									e.printStackTrace();
								}
							} else
								listaErrori.add(" impossibile creare documenti testata;");

						} else
							listaErrori.add(" dati di configurazione kit non impostati (magAz= " + magAZ + ", magCL= "
									+ magCL + ");");

					}
				} else
					listaErrori.add(" nessun componente trovato; ");
			} else
				listaErrori.add(" articolo non valido;");
		} else
			listaErrori.add(" riga non valida;");

	}

	public void apriGestioneSmontaggio(ServletEnvironment se) throws ServletException, IOException {

		String tesKey = null;
		String rigaKey = null;
		List lRigheDocAcq = new ArrayList();

		String qta = getStringParameter(se.getRequest(), "QtaKit");
		String magPart = getStringParameter(se.getRequest(), "IdMagazzinoPart");
		String magKit = getStringParameter(se.getRequest(), "IdMagazzinoKit");
		String idArt = getStringParameter(se.getRequest(), "IdArticolo");
		char tipo = getStringParameter(se.getRequest(), "TipoMontSmont").charAt(0);

		String annoOrd = getStringParameter(se.getRequest(), "AnnoOrdine");
		String numOrd = getStringParameter(se.getRequest(), "NumeroOrdine");
		String groupTC = getStringParameter(se.getRequest(), "GruppoTC");
		String rigaOrd = getStringParameter(se.getRequest(), "RigaOrdine");

		gestioneSmontaggioKit(se, qta, magPart, magKit, idArt, tipo, annoOrd, numOrd, groupTC, rigaOrd);

		if (listaErrori != null && listaErrori.size() > 0) {
			Iterator iter = listaErrori.iterator();
			String strErroti = "";
			while (iter.hasNext()) {
				String em = (String) iter.next();
				strErroti = strErroti + "\\r  - " + em;
			}
			invokeAlertWindow(se, "Smontaggio Kit terminato. Si sono verificate le seguenti anomalie: " + strErroti,
					true);
		} else
			invokeAlertWindow(se, "Smontaggio Kit terminato con successo! ", true);

	}

	public void apriGestioneSmontaggioRighe(ServletEnvironment se) throws ServletException, IOException {
		listaErrori.clear();
		String tesKey = null;

		String[] chiaveRiga = (String[]) se.getRequest().getSession().getAttribute("ObjectKey");
		String[] chiaveOrdine = (String[]) se.getRequest().getSession().getAttribute("FatherKey");
		String rigaKey = null;

		String qta = getStringParameter(se.getRequest(), "QtaKit");
		String magPart = null;
		String magKit = getStringParameter(se.getRequest(), "IdMagazzinoKit");
		String idArt = getStringParameter(se.getRequest(), "IdArticolo");
		char tipo = 0;

		String azienda = Azienda.getAziendaCorrente();
		String annoOrd = getStringParameter(se.getRequest(), "AnnoOrdine");
		String numOrd = getStringParameter(se.getRequest(), "NumeroOrdine");
		String groupTC = getStringParameter(se.getRequest(), "GruppoTC");
		String rigaOrd = getStringParameter(se.getRequest(), "RigaOrdine");
		// String dettaRigaOrd = getStringParameter(se.getRequest(),
		// "DettaglioRigaOrdine");

		for (int i = 0; i < chiaveRiga.length; i++) {
			rigaKey = chiaveRiga[i];
			VtOrdineVenditaRigaPrm rigaO = null;
			try {

				rigaO = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, rigaKey,
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (rigaO != null) {
				// TODO verificare se si tratta di un kit
				// Get quantità

				qta = String.valueOf(rigaO.getQtaInUMRif());

				// CHECK KIT
				if (rigaO.getArticolo().getTipoParte() != '6')// kit gestito a magazzino
					continue;
				
				rigaO.getRigheSecondarie();
				idArt = rigaO.getIdArticolo();
				magPart = rigaO.getIdMagazzino();
				Magazzino mag = null;
				try {
					mag = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, rigaO.getMagazzinoKey(),
							Magazzino.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mag != null) {

					// rd.setIdMagazzinoPart(mag.getIdMagazzino());
					if (mag.getIdFornitoreAzienda() != null && !mag.getIdFornitoreAzienda().equals(""))
						tipo = VT_TipoMontSmontKit.ESTERNO;
					else
						tipo = VT_TipoMontSmontKit.INTERNO;
				}

				if (rigaO.getGruppoTC() != null)
					groupTC = String.valueOf(rigaO.getGruppoTC());

				rigaOrd = String.valueOf(rigaO.getNumeroRigaDocumento());

			}
			if (gestioneSmontaggioKit(se, qta, magPart, magKit, idArt, tipo, annoOrd, numOrd, groupTC, rigaOrd)) {
				rigaO.saldaRiga();
				try {
					int res=rigaO.save();
					if(res>0)
						ConnectionManager.commit();
					else
						listaErrori.add(" Impossibile salvare la riga.");
				} catch (SQLException e) {
					
					listaErrori.add(" Impossibile salvare la riga.");
					e.printStackTrace();
				}
			}
		}

		if (listaErrori != null && listaErrori.size() > 0) {
			Iterator iter = listaErrori.iterator();
			String strErroti = "";
			while (iter.hasNext()) {
				String em = (String) iter.next();
				strErroti = strErroti + "\\r  - " + em;
			}
			invokeAlertWindow(se, "Smontaggio Kit terminato. Si sono verificate le seguenti anomalie: " + strErroti,
					true);
		} else
			invokeAlertWindow(se, "Smontaggio Kit terminato con successo! ", true);

	}

	public boolean gestioneSmontaggioKit(ServletEnvironment se, String qta, String magPart, String magKit, String idArt,
			char tipo, String annoOrd, String numOrd, String groupTC, String rigaOrd)
			throws ServletException, IOException {

		String tesKey = null;
		boolean esito = false;
		String azienda = Azienda.getAziendaCorrente();// getStringParameter(se.getRequest(), "IdAzienda");

		// String dettaRigaOrd = getStringParameter(se.getRequest(),
		// "DettaglioRigaOrdine");

		tesKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd });
		String artKey = KeyHelper.buildObjectKey(new String[] { azienda, idArt });
		String magPartKey = KeyHelper.buildObjectKey(new String[] { azienda, magPart });
		// rigaKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd,
		// rigaOrd });
		String cauKey;
		// VtOrdineVendita tesO = null;
		// VtOrdineVenditaRigaPrm rigaO = null;
		Articolo artO = null;
		try {
			// tesO = (VtOrdineVendita)
			// PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
			// VtOrdineVendita.NO_LOCK);
			artO = (Articolo) PersistentObject.elementWithKey(Articolo.class, artKey, Articolo.NO_LOCK);
			// rigaO = (VtOrdineVenditaRigaPrm)
			// PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, rigaKey,
			// PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// if (tesO != null) {
		VtArticolo art = VtTessileUtil.getVtArticoloFromId(azienda, artO.getIdArticolo());
		if (art != null) {
			if (qta == null || qta.equals("") || qta.equals("null"))
				qta = "0.00";

			BigDecimal qtariga = new BigDecimal(qta.replace(',', DecimalType.getThousandsSeparator()));
			// CARICO COMPONENTI
			Iterator compIter = getComponenti(null, art.getIdArticolo(), vistaDisponKey, magPartKey, qtariga)
					.iterator();
			if (compIter != null && compIter.hasNext()) {

				if (tipo == VT_TipoMontSmontKit.INTERNO) {
					// *************** SMONTAGGIO INTERNO *********************************

					// Creare un documento di magazzino di trasferimento di carico kit e
					// scarico componente
					cauKey = KeyHelper.buildObjectKey(new String[] { azienda, cauSmontInt });
					CausaleDocumentoGen causDoc;
					try {
						causDoc = (CausaleDocumentoGen) PersistentObject.elementWithKey(CausaleDocumentoGen.class,
								cauKey, PersistentObject.NO_LOCK);

						DocMagGenerico docTestata = creaDocumentoMagazzinoTestata(azienda, art, causDoc, magPart,
								annoOrd, numOrd, groupTC, rigaOrd);

						// DocMagTrasferimento docTestataTrasf=creaDocMagazTrasferimentoTestata(azie,
						// art, idCauDoc, mag, rigaOrigine, idFor, gruppoTC, numOrd, annoOrd)
						int i = docTestata.save();
						if (i >= 0 && art != null) {

							// SCARICO KIT
							DocMagGenericoRiga riga = creaDocMagGenericoRiga(docTestata, art, cauScaricoInt, magPart,
									qtariga);

							i = riga.save();

							List listaComMagEst = new ArrayList();
							List listaComMagInt = new ArrayList();
							while (compIter.hasNext()) {
								String strComp = (String) compIter.next();
								String strQta = strComp.split("-")[2];// la qta deve essere negativa;
								VtArticolo artComp = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class,
										strComp.split("-")[0], PersistentObject.NO_LOCK);

								BigDecimal qtacomp = new BigDecimal(
										strQta.replace(',', DecimalType.getThousandsSeparator()));

								riga = creaDocMagGenericoRiga(docTestata, artComp, cauCaricoInt, magPart, qtacomp);

								i = riga.save();

								// verifico se il componente è caricato su un magazzino esterno
								String keymag = KeyHelper
										.buildObjectKey(new String[] { azienda, strComp.split("-")[1] });
								Magazzino magComp = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, keymag,
										Magazzino.NO_LOCK);
								String keyComp = magComp.getIdMagazzino() + "-" + artComp.getKey() + "-" + qtacomp + "-"
										+ magComp.getIdFornitoreAzienda();
								if (magComp.getIdFornitoreAzienda() != null) {

									listaComMagEst.add(keyComp);
								} else if (!magComp.getIdMagazzino().equals(magPart))
									listaComMagInt.add(keyComp);
							}

							if (listaComMagEst != null && listaComMagEst.size() > 0)
								CreaDocAcqTraCompEst(listaComMagEst, azienda, magPart, groupTC, annoOrd, numOrd,
										cauTrasfIntSmont, null);

							if (listaComMagInt != null && listaComMagInt.size() > 0)
								CreaDocCompInt(listaComMagInt, azienda, magPart, art, groupTC, annoOrd, numOrd,
										cauTrasfInt, null);

							if (i >= 0) {
								ConnectionManager.commit();
								esito = true;

							} else {
								ConnectionManager.rollback();
								listaErrori.add(art.getChiaveProdotto1().trim() + " " + art.getChiaveProdotto16().trim()
										+ " " + art.getColoreVariante().trim()
										+ " - Smontaggio Kit interno fallito, verificare i dati inseriti ! ");
							}

						}
					} catch (SQLException e) {
						invokeAlertWindow(se,
								"ATTENZIONE:Smontaggio Kit interno fallito, verificare i dati inseriti ! ", true);
						e.printStackTrace();
					}
				} else {
					// ************ SMONTAGGIO ESTERNO **************

					// 1) Creare un documento di acquisto (BEM) intestato al fornitore get dal kit

					String magAZ = "", magCompId = "", magCL = "";
					magAZ = magPart;
					magCL = magKit;

					String kitKey = KeyHelper.buildObjectKey(new String[] { azienda, magAZ, magCL });
					VtMontSmontKitNew kit = null;
					try {
						kit = (VtMontSmontKitNew) PersistentObject.elementWithKey(VtMontSmontKitNew.class, kitKey,
								PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						listaErrori.add(" kit non trovato( cod :" + kitKey + ");");
						e.printStackTrace();
					}

					if (kit != null) {
						// CREARE DOCUMENTO DI ACQUISTO
						String fornitoreKeyCL = KeyHelper.buildObjectKey(new String[] { azienda, kit.getRFornitore() });

						int res = -1;

						try {
							DocumentoAcquisto testataCL = creaDocumentoAcquistoTestata(azienda, fornitoreKeyCL, art,
									kit.getRCauDocAcqSmont(), magPart, "", groupTC, annoOrd, numOrd, rigaOrd);
							res = testataCL.save();

							VtDocumentoAcqRigaPrm rigaDocAcqCL = creaDocumentoAcquistoRigaPrm(testataCL,
									kit.getRCauScaricSmont(), "", qtariga, art, magPart);

							res = rigaDocAcqCL.save();
							List listaComMagEst = new ArrayList();
							while (compIter.hasNext()) {
								String strComp = (String) compIter.next();
								String strQta = strComp.split("-")[2];
								magCompId = strComp.split("-")[1];

								VtArticolo artComp = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class,
										strComp.split("-")[0], PersistentObject.NO_LOCK);

								BigDecimal qtacomp = new BigDecimal(
										strQta.replace(',', DecimalType.getThousandsSeparator()));

								VtDocumentoAcqRigaPrm rigaDocAcqComp = creaDocumentoAcquistoRigaPrm(testataCL,
										kit.getRCauCaricSmont(), "", qtacomp, artComp, magPart);

								res = rigaDocAcqComp.save();

								// verifico se il componente è caricato su un magazzino esterno
								String keymag = KeyHelper.buildObjectKey(new String[] { azienda, magCompId });
								Magazzino magComp = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, keymag,
										Magazzino.NO_LOCK);
								if (magComp.getIdFornitoreAzienda() == null) {
									String keyComp = magComp.getIdMagazzino() + "-" + artComp.getKey() + "-" + qtacomp
											+ "-" + magComp.getIdFornitoreAzienda();
									listaComMagEst.add(keyComp);
								}
							}

							if (listaComMagEst != null && listaComMagEst.size() > 0)
								CreaDocAcqTraCompEst(listaComMagEst, azienda, magPart, groupTC, annoOrd, numOrd,
										cauTrasfEstSmont, null);

							if (res >= 0) {
								ConnectionManager.commit();
								esito = true;

							} else {
								ConnectionManager.rollback();
								listaErrori.add(art.getChiaveProdotto1().trim() + " " + art.getChiaveProdotto16().trim()
										+ " " + art.getColoreVariante().trim()
										+ " - Smontaggio Kit esterno fallito, verificare i dati inseriti ! ");
							}

						} catch (SQLException e) {
							listaErrori.add(art.getChiaveProdotto1().trim() + " " + art.getChiaveProdotto16().trim()
									+ " " + art.getColoreVariante().trim() + " - impossibile creare riga documento;");
							e.printStackTrace();
						}
					} else
						listaErrori.add(art.getChiaveProdotto1().trim() + " " + art.getChiaveProdotto16().trim() + " "
								+ art.getColoreVariante().trim() + " - dati di configurazione kit non impostati (magAz="
								+ magAZ + ", magCL= " + magCL + ")! ");

				}

			} else
				listaErrori.add(art.getChiaveProdotto1().trim() + " " + art.getChiaveProdotto16().trim() + " "
						+ art.getColoreVariante().trim() + " - Nessun componente trovato;");
		} else
			listaErrori.add(" - aticolo non valido;");
		/*
		 * } else listaErrori.add(" riga documento non valida;");
		 */

		return esito;
	}

	// Setto la riga doc se esiste riga ordine riferimento
	public void settoDatiRigaDocAcq(DocumentoAcqRigaPrm rigaDocAcq, DocumentoAcquisto testata, BigDecimal qtaCalcolata,
			String cau, String mag, VtArticolo articolo) {
		rigaDocAcq.setTestata(testata);

		rigaDocAcq.setNumeroRigaDocumento(new Integer(rigaDocAcq.getNumeroNuovaRiga(rigaDocAcq.getTestata())));
		rigaDocAcq.setCausaleRigaKey(KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), cau }));
		// rigaDocAcq.setIdCauRig(cau);
		rigaDocAcq.setQtaInUMPrm(qtaCalcolata);

		rigaDocAcq.setStatoAvanzamento(testata.getStatoAvanzamento());

		rigaDocAcq.setIdMagazzino(mag);
		rigaDocAcq.setArticolo(articolo);

		rigaDocAcq.setQtaInUMAcq(qtaCalcolata);
		// rigaDocAcq.setQtaInUMPrm(qta2Calcolata);
		// rigaDocAcq.setQtaInUMSec(qta3Calcolata);
		rigaDocAcq.setUMPrm(articolo.getUMPrmMag());
		UnitaMisura UMRiferimento = articolo.getUMPrimariaAcquisto();
		if (UMRiferimento == null)
			UMRiferimento = articolo.getUMDefaultAcquisto();

		rigaDocAcq.setUMRif(UMRiferimento);
		rigaDocAcq.setUMSec(articolo.getUMSecMag());
		rigaDocAcq.setRicalcoloQtaFattoreConv(false);

		rigaDocAcq.completaBO();

	}

	public DocMagGenerico creaDocumentoMagazzinoTestata(String azie, VtArticolo art, CausaleDocumentoGen cauDoc,
			String idMag, String annoOrd, String numOrd, String gruppoTC, String idRiga) throws NumeratoreException {
		VtDocMagGenerico docTestata = (VtDocMagGenerico) Factory.createObject(VtDocMagGenerico.class);

		Date dataFine = TimeUtils.getCurrentDate();

		docTestata.setIdAzienda(azie);
		docTestata.setIdCau(cauDoc.getIdCausale());
		docTestata.setIdMagazzino(idMag);

		// docTestata.getNumeratoreHandler().setIdSerie(cauDoc.getIdSerieDocAcq());
		docTestata.setStatoAvanzamento(it.thera.thip.base.documenti.StatoAvanzamento.PROVVISORIO);

		docTestata.getNumeratoreHandler().setDataDocumento(dataFine);

		// Recupero la Serie del documento
		String serieDoc = null;
		ParametroPsn paramPsn = ParametroPsn.getParametroPsn(KEY_PAR1_SERIE_MAG, KEY_PAR2_SERIE_MAG);
		if (paramPsn != null && paramPsn.getValore() != null)
			serieDoc = paramPsn.getValore();

		docTestata.getNumeratoreHandler().setIdSerie(serieDoc);

		// trace documento generato
		/*
		 * if (rigaOrigine != null) {
		 * docTestata.setRigaOrdineCli(rigaOrigine.getNumeroRigaDocumento());
		 * docTestata.setAnnoOrdineCli(rigaOrigine.getAnnoDocumento());
		 * docTestata.setNumeroOrdineCli(rigaOrigine.getNumeroDocumento()); if
		 * (rigaOrigine.getGruppoTC() != null)
		 * docTestata.setDettaglioRigaOrdineCli(rigaOrigine.getGruppoTC().intValue()); }
		 * else {
		 * 
		 * docTestata.setAnnoOrdineCli(annoOrd); docTestata.setNumeroOrdineCli(numOrd);
		 * if (gruppoTC != null && !gruppoTC.equals(""))
		 * docTestata.setDettaglioRigaOrdineCli(Integer.valueOf(gruppoTC)); }
		 */

		docTestata.setAnnoOrdineCli(annoOrd);
		docTestata.setNumeroOrdineCli(numOrd);
		if (idRiga != null && !idRiga.equals(""))
			docTestata.setRigaOrdineCli(Integer.valueOf(idRiga));
		if (gruppoTC != null && !gruppoTC.equals(""))
			docTestata.setDettaglioRigaOrdineCli(Integer.valueOf(gruppoTC));

		docTestata.completaBO();

		return docTestata;
	}

	public DocMagGenericoRiga creaDocMagGenericoRiga(DocMagGenerico testata, Articolo art, String idCausRiga,
			String idMag, BigDecimal qtaPrm) {
		DocMagGenericoRiga docGenRiga = (DocMagGenericoRiga) Factory.createObject(DocMagGenericoRiga.class);
		docGenRiga.setTestata(testata);
		docGenRiga.setIdAzienda(testata.getIdAzienda());
		docGenRiga.completaBO();
		testata.getRighe().add(docGenRiga);
		docGenRiga.setIdCauRig(idCausRiga);
		docGenRiga.setIdArticolo(art.getIdArticolo());
		docGenRiga.setIdMagazzino(idMag);
		docGenRiga.setIdUMPrm(art.getIdUMPrmMag());
		docGenRiga.setIdUMSec(art.getIdUMSecMag());
		docGenRiga.setOperatoreConversioneUM(art.getOperConverUM());
		docGenRiga.setFattConverUMPrimSec(art.getFttConverUM());
		docGenRiga.getQuantita().setQuantitaInUMPrm(qtaPrm);

		docGenRiga.setAnnoDocumento(testata.getAnnoDocumento());
		docGenRiga.setNumeroDocumento(testata.getNumeroDocumento());
		docGenRiga.setDataRegistrazione(testata.getDataDocumento());
		docGenRiga.setOperatoreConversioneUM('-');

		return docGenRiga;
	}

	public VtDocMagTrasferimento creaDocMagazTrasferimentoTestata(String azie, String idCauDoc, String magId,
			String magArrivo, String idFor, String gruppoTC, String numOrd, String annoOrd, String idRiga)
			throws SQLException {
		VtDocMagTrasferimento docTestata = (VtDocMagTrasferimento) Factory.createObject(VtDocMagTrasferimento.class);

		String cauKey = KeyHelper.buildObjectKey(new String[] { azie, idCauDoc });
		CausaleDocumentoTrasf causVenTra = (CausaleDocumentoTrasf) PersistentObject
				.elementWithKey(CausaleDocumentoTrasf.class, cauKey, PersistentObject.NO_LOCK);

		Date dataFine = TimeUtils.getCurrentDate();

		docTestata.setIdAzienda(azie);

		docTestata.setIdCau(causVenTra.getIdCausale());
		docTestata.setIdMagazzino(magId);
		docTestata.setCodiceMagazzinoArrivo(magArrivo);
		docTestata.setStatoAvanzamento(it.thera.thip.base.documenti.StatoAvanzamento.PROVVISORIO);

		docTestata.getNumeratoreHandler().setDataDocumento(dataFine);

		/*
		 * // set riga ordine if (rigaOrigine != null) {
		 * docTestata.setRigaOrdineCli(rigaOrigine.getNumeroRigaDocumento());
		 * docTestata.setAnnoOrdineCli(rigaOrigine.getAnnoDocumento());
		 * docTestata.setNumeroOrdineCli(rigaOrigine.getNumeroDocumento()); if
		 * (rigaOrigine.getGruppoTC() != null)
		 * docTestata.setDettaglioRigaOrdineCli(rigaOrigine.getGruppoTC().intValue());
		 * 
		 * } else {
		 * 
		 * docTestata.setAnnoOrdineCli(annoOrd); docTestata.setNumeroOrdineCli(numOrd);
		 * if (!gruppoTC.equals(""))
		 * docTestata.setDettaglioRigaOrdineCli(Integer.parseInt(gruppoTC));
		 * 
		 * }
		 */
		// Trace documento
		docTestata.setAnnoOrdineCli(annoOrd);
		docTestata.setNumeroOrdineCli(numOrd);
		if (idRiga != null && !idRiga.equals(""))
			docTestata.setRigaOrdineCli(Integer.valueOf(idRiga));
		if (gruppoTC != null && !gruppoTC.equals(""))
			docTestata.setDettaglioRigaOrdineCli(Integer.parseInt(gruppoTC));

		// Recupero la Serie del documento
		String serieDoc = null;
		ParametroPsn paramPsn = ParametroPsn.getParametroPsn(KEY_PAR1_SERIE_MAG, KEY_PAR2_TRASF_SERIE_MAG);
		if (paramPsn != null && paramPsn.getValore() != null)
			serieDoc = paramPsn.getValore();

		docTestata.getNumeratoreHandler().setIdSerie(serieDoc);

		docTestata.completaBO();
		docTestata.setIdFornitoreArrivo(idFor);

		return docTestata;
	}

	public VtDocMagTrasferimentoRiga creaDocMagTrasferimentoRiga(DocMagTrasferimento testata, Articolo art,
			String idCausRiga, String idMag, String magDest, BigDecimal qtaPrm, String idFor) {
		VtDocMagTrasferimentoRiga docGenRiga = (VtDocMagTrasferimentoRiga) Factory
				.createObject(VtDocMagTrasferimentoRiga.class);
		FornitoreAcquisto fornAcq = null;
		try {
			fornAcq = (FornitoreAcquisto) PersistentObject.elementWithKey(FornitoreAcquisto.class, idFor,
					PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		docGenRiga.setTestata(testata);
		docGenRiga.setIdAzienda(testata.getIdAzienda());
		docGenRiga.setFornitoreArrivo(fornAcq);
		docGenRiga.completaBO();
		// testata.getRighe().add(docGenRiga);

		docGenRiga.setIdCauRig(idCausRiga);
		docGenRiga.setIdArticolo(art.getIdArticolo());
		docGenRiga.setIdMagazzino(idMag);
		docGenRiga.setCodiceMagazzinoArrivo(magDest);
		docGenRiga.setIdUMPrm(art.getIdUMPrmMag());
		docGenRiga.setIdUMSec(art.getIdUMSecMag());
		docGenRiga.setOperatoreConversioneUM(art.getOperConverUM());
		docGenRiga.setFattConverUMPrimSec(art.getFttConverUM());
		docGenRiga.getQuantita().setQuantitaInUMPrm(qtaPrm);

		docGenRiga.setAnnoDocumento(testata.getAnnoDocumento());
		docGenRiga.setNumeroDocumento(testata.getNumeroDocumento());
		docGenRiga.setDataRegistrazione(testata.getDataDocumento());
		docGenRiga.setOperatoreConversioneUM('-');

		return docGenRiga;
	}

	public VtDocumentoAcquisto creaDocumentoAcquistoTestata(String azienda, String fornAcqKey, VtArticolo art,
			String idCau, String magPartenza, String magDest, String groupTC, String annoOrd, String numOrd,
			String idRiga) throws SQLException {

		FornitoreAcquisto fornAcq = (FornitoreAcquisto) PersistentObject.elementWithKey(FornitoreAcquisto.class,
				fornAcqKey, PersistentObject.NO_LOCK);

		String cauKey = KeyHelper.buildObjectKey(new String[] { azienda, idCau });
		CausaleDocumentoTestataAcq causVen = (CausaleDocumentoTestataAcq) PersistentObject
				.elementWithKey(CausaleDocumentoTestataAcq.class, cauKey, CausaleDocumentoTestataAcq.NO_LOCK);

		Date dataFine = TimeUtils.getCurrentDate();
		VtDocumentoAcquisto nuovoDocAcq = (VtDocumentoAcquisto) Factory.createObject(VtDocumentoAcquisto.class);

		nuovoDocAcq.setIdAzienda(azienda);
		nuovoDocAcq.getNumeratoreHandler().setDataDocumento(dataFine);

		nuovoDocAcq.getNumeratoreHandler().setIdSerie(serieDocAcqCompEst);

		nuovoDocAcq.setIdCau(causVen.getIdCausale());

		nuovoDocAcq.setIdFornitore(fornAcq.getIdFornitore());
		nuovoDocAcq.setIdAnagrafico(fornAcq.getIdAnagrafico());

		/*
		 * // set riga ordine if (rigaOrigine != null) { idRiga =
		 * String.valueOf(rigaOrigine.getSequenzaRiga());
		 * 
		 * nuovoDocAcq.setRigaOrdineCli(rigaOrigine.getNumeroRigaDocumento());
		 * nuovoDocAcq.setAnnoOrdineCli(rigaOrigine.getAnnoDocumento());
		 * nuovoDocAcq.setNumeroOrdineCli(rigaOrigine.getNumeroDocumento()); if
		 * (rigaOrigine.getGruppoTC() != null) {
		 * nuovoDocAcq.setDettaglioRigaOrdineCli(rigaOrigine.getGruppoTC().intValue());
		 * gruppoTC = String.valueOf(rigaOrigine.getGruppoTC().intValue()); } } else {
		 * nuovoDocAcq.setAnnoOrdineCli(annoOrd);
		 * nuovoDocAcq.setNumeroOrdineCli(numOrd); if (groupTC != null &&
		 * !groupTC.equals(""))
		 * nuovoDocAcq.setDettaglioRigaOrdineCli(Integer.parseInt(groupTC)); }
		 */

		// Trace documento
		nuovoDocAcq.setAnnoOrdineCli(annoOrd);
		nuovoDocAcq.setNumeroOrdineCli(numOrd);
		if (idRiga != null && !idRiga.equals(""))
			nuovoDocAcq.setRigaOrdineCli(Integer.valueOf(idRiga));
		if (groupTC != null && !groupTC.equals(""))
			nuovoDocAcq.setDettaglioRigaOrdineCli(Integer.parseInt(groupTC));

		Fornitore f = fornAcq.getFornitore();
		if (f != null) {
			nuovoDocAcq.setIdModPagamento(f.getIdModalitaPagamento());

			String valuta = f.getIdValuta();
			if (valuta == null)
				valuta = "EUR";

			nuovoDocAcq.setIdValuta(valuta);

			nuovoDocAcq.setIdLingua(f.getIdLingua());

		}

		// nuovoDocAcq.setDataConsegnaRichiesta(dataFine);
		// nuovoDocAcq.setDataConsegnaConfermata(dataFine);

		nuovoDocAcq.setCambio(new BigDecimal(1));

		nuovoDocAcq.completaBO();
		nuovoDocAcq.completaBODatiCausale();
		nuovoDocAcq.setIdMagazzino(magPartenza);
		if (!magDest.equals("")) {
			nuovoDocAcq.setIdMagazzinoLavEsterna(magDest);
			nuovoDocAcq.setIdMagazzinoTra(magDest);
		}
		nuovoDocAcq.setStatoAvanzamento(StatoAvanzamento.PROVVISORIO);

		// salvato = VtTessileUtil.genericSave(nuovoDocAcq);

		// if (salvato)

		return nuovoDocAcq;

	}

	public VtDocumentoAcqRigaPrm creaDocumentoAcquistoRigaPrm(DocumentoAcquisto testata, String idCausaleRiga,
			String idMagLavEs, BigDecimal totQta, VtArticolo art, String idMag) {

		VtDocumentoAcqRigaPrm nuovaRigaDocAcq = (VtDocumentoAcqRigaPrm) Factory
				.createObject(VtDocumentoAcqRigaPrm.class);

		nuovaRigaDocAcq.setTestata(testata);

		nuovaRigaDocAcq.setIdCauRig(idCausaleRiga);

		nuovaRigaDocAcq.setIdAzienda(testata.getIdAzienda());
		nuovaRigaDocAcq.setAnnoDocumento(testata.getAnnoDocumento());
		nuovaRigaDocAcq.setNumeroDocumento(testata.getNumeroDocumento());

		nuovaRigaDocAcq.setQtaInUMAcq(totQta);
		nuovaRigaDocAcq.setQtaInUMPrm(totQta);

		nuovaRigaDocAcq.setDataConsegnaRichiesta(testata.getDataDocumento());
		nuovaRigaDocAcq.setDataConsegnaConfermata(testata.getDataDocumento());

		nuovaRigaDocAcq.completaBO();
		nuovaRigaDocAcq.setIdMagazzino(idMag);

		if (!idMagLavEs.equals(""))
			nuovaRigaDocAcq.setIdMagazzinoLavEsterna(idMagLavEs);
		// nuovaRigaDocAcq.setMagazzinoKey(magKey);
		nuovaRigaDocAcq.setStatoAvanzamento(testata.getStatoAvanzamento());

		QuantitaInUMRif qtaRif = null;

		if (art != null) {
			nuovaRigaDocAcq.setIdArticolo(art.getIdArticolo());
			nuovaRigaDocAcq.setArticolo(art);
			nuovaRigaDocAcq.setDescrizioneArticolo(art.getDescrizioneArticoloNLS().getDescrizione());

			nuovaRigaDocAcq.setDescrizioneExtArticolo(art.getDescrizioneArticoloNLS().getDescrizioneEstesa());

			nuovaRigaDocAcq.setUMPrm(art.getUMPrmMag());
			nuovaRigaDocAcq.setUMRif(art.getUMRiferimento());
			nuovaRigaDocAcq.setUMSec(art.getUMSecMag());
			nuovaRigaDocAcq.setIdAssogIVA(art.getIdAssoggettamentoIVA());

			qtaRif = VtTessileUtil.eseguiRicalcoloQuantita(art, art.getUMRiferimento(), art.getUMPrmMag(),
					art.getUMSecMag(), totQta, Articolo.UM_PRM);

			nuovaRigaDocAcq.setQtaInUMAcq(qtaRif.getQuantitaInUMRif());
			nuovaRigaDocAcq.setQtaInUMPrm(qtaRif.getQuantitaInUMPrm());
			nuovaRigaDocAcq.setQtaInUMSec(qtaRif.getQuantitaInUMSec());

		}

		// boolean rc1 = VtTessileUtil.genericSave(nuovaRigaDocAcq);

		return nuovaRigaDocAcq;

	}

	public VtDocumentoAcqRigaSec creaDocumentoAcquistoRigaSec(DocumentoAcquisto testata, VtDocumentoAcqRigaPrm rigaPrm,
			String idCausaleRiga, String idMag, BigDecimal totQta, VtArticolo art) {

		VtDocumentoAcqRigaSec nuovaRigaDocAcqSec = (VtDocumentoAcqRigaSec) Factory
				.createObject(VtDocumentoAcqRigaSec.class);

		nuovaRigaDocAcqSec.setTestata(testata);
		nuovaRigaDocAcqSec.setRigaPrimaria(rigaPrm);
		nuovaRigaDocAcqSec.setIdCauRig(idCausaleRiga);

		nuovaRigaDocAcqSec.setIdAzienda(testata.getIdAzienda());
		nuovaRigaDocAcqSec.setAnnoDocumento(testata.getAnnoDocumento());
		nuovaRigaDocAcqSec.setNumeroDocumento(testata.getNumeroDocumento());

		nuovaRigaDocAcqSec.setQtaInUMAcq(totQta);
		nuovaRigaDocAcqSec.setQtaInUMPrm(totQta);

		nuovaRigaDocAcqSec.setDataConsegnaRichiesta(testata.getDataDocumento());
		nuovaRigaDocAcqSec.setDataConsegnaConfermata(testata.getDataDocumento());

		nuovaRigaDocAcqSec.completaBO();
		nuovaRigaDocAcqSec.setIdMagazzino(idMag);
		// nuovaRigaDocAcq.setMagazzinoKey(magKey);
		nuovaRigaDocAcqSec.setStatoAvanzamento(testata.getStatoAvanzamento());

		QuantitaInUMRif qtaRif = null;

		if (art != null) {
			nuovaRigaDocAcqSec.setIdArticolo(art.getIdArticolo());
			nuovaRigaDocAcqSec.setArticolo(art);
			nuovaRigaDocAcqSec.setDescrizioneArticolo(art.getDescrizioneArticoloNLS().getDescrizione());

			nuovaRigaDocAcqSec.setDescrizioneExtArticolo(art.getDescrizioneArticoloNLS().getDescrizioneEstesa());

			nuovaRigaDocAcqSec.setUMPrm(art.getUMPrmMag());
			nuovaRigaDocAcqSec.setUMRif(art.getUMRiferimento());
			nuovaRigaDocAcqSec.setUMSec(art.getUMSecMag());
			nuovaRigaDocAcqSec.setIdAssogIVA(art.getIdAssoggettamentoIVA());

			qtaRif = VtTessileUtil.eseguiRicalcoloQuantita(art, art.getUMRiferimento(), art.getUMPrmMag(),
					art.getUMSecMag(), totQta, Articolo.UM_PRM);

			nuovaRigaDocAcqSec.setQtaInUMAcq(qtaRif.getQuantitaInUMRif());
			nuovaRigaDocAcqSec.setQtaInUMPrm(qtaRif.getQuantitaInUMPrm());
			nuovaRigaDocAcqSec.setQtaInUMSec(qtaRif.getQuantitaInUMSec());

		}

		// boolean rc1 = VtTessileUtil.genericSave(nuovaRigaDocAcq);

		return nuovaRigaDocAcqSec;

	}

	private boolean CreaDocAcqTraCompEst(List listaComMagEst, String azienda, String mag, String groupTC,
			String annoOrd, String numOrd, String cauTestata, VtOrdineVenditaRigaPrm rigaOrigine) throws SQLException {
		// Documento di trasferimento per montaggio interno con componente richisto a
		// magazzino esterno(BEM)
		boolean esito = false;

		// ordino la lista per il primo campo che contiene il magazzino
		Collections.sort(listaComMagEst);
		// Collections.reverse(testList);

		Iterator iterCompEst = listaComMagEst.iterator();

		String magKeyCompOld = null;

		VtDocumentoAcqRigaPrm rigaDocAcqComp = null;
		VtDocumentoAcquisto testataCompEst = null;

		int resC = -1;
		while (iterCompEst.hasNext()) {

			String compkey = (String) iterCompEst.next();
			String magComp = compkey.split("-")[0];
			String artKeyComp = compkey.split("-")[1];
			String fornKey = KeyHelper.buildObjectKey(new String[] { azienda, compkey.split("-")[3] });

			VtArticolo artComp = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class, artKeyComp,
					PersistentObject.NO_LOCK);
			BigDecimal qtaComp = new BigDecimal(compkey.split("-")[2]);

			if (magKeyCompOld == null || (magKeyCompOld != null && !magKeyCompOld.equals(magComp))) {
				resC = -1;
				if (rigaOrigine != null) {
					// MONTAGGIO
					fornKey = KeyHelper.buildObjectKey(new String[] { azienda, codFornitore });

					testataCompEst = creaDocumentoAcquistoTestata(azienda, fornKey, artComp, cauTestata, magComp, mag,
							groupTC, rigaOrigine.getAnnoDocumento(), rigaOrigine.getNumeroDocumento(),
							String.valueOf(rigaOrigine.getNumeroRigaDocumento()));
				} else {
					// SMONTAGGIO
					String magKey = KeyHelper.buildObjectKey(new String[] { azienda, mag });
					Magazzino magPartenza = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magKey,
							Magazzino.NO_LOCK);
					if (magPartenza.getIdFornitoreAzienda() != null)
						fornKey = KeyHelper
								.buildObjectKey(new String[] { azienda, magPartenza.getIdFornitoreAzienda() });

					testataCompEst = creaDocumentoAcquistoTestata(azienda, fornKey, artComp, cauTestata, mag, magComp,
							groupTC, annoOrd, numOrd, null);
				}
				resC = testataCompEst.save();

				magKeyCompOld = magComp;
			}

			if (resC > 0) {
				if (rigaOrigine != null)
					rigaDocAcqComp = creaDocumentoAcquistoRigaPrm(testataCompEst, cauTrasfMerce, "", qtaComp, artComp,
							magComp);
				else
					rigaDocAcqComp = creaDocumentoAcquistoRigaPrm(testataCompEst, cauTrasfMerce, "", qtaComp, artComp,
							mag);
				resC = rigaDocAcqComp.save();

			}
		}

		return esito;
	}

	private boolean CreaDocCompInt(List listaComMagInt, String azienda, String mag, VtArticolo art, String groupTC,
			String annoOrd, String numOrd, String cauTestata, VtOrdineVenditaRigaPrm rigaOrigine) throws SQLException {
		// Documento di trasferimento per montaggio interno con componente richisto a
		// magazzino esterno(BEM)
		boolean esito = false;
		String keymag = KeyHelper.buildObjectKey(new String[] { azienda, mag });

		Magazzino magPartenza = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, keymag, Magazzino.NO_LOCK);
		// ordino la lista per il primo campo che contiene il magazzino
		Collections.sort(listaComMagInt);
		// Collections.reverse(testList);

		Iterator iterCompInt = listaComMagInt.iterator();

		String magKeyCompOld = null;
		VtDocumentoAcquisto testataDocAcqComp = null;
		VtDocumentoAcqRigaPrm rigaDocAcqComp = null;
		VtDocMagTrasferimento testataMagTraComp = null;
		VtDocMagTrasferimentoRiga rigaMagTraComp = null;
		int resC = -1;
		while (iterCompInt.hasNext()) {

			String compkey = (String) iterCompInt.next();
			String magComp = compkey.split("-")[0];
			String artKeyComp = compkey.split("-")[1];
			String idFornComp = compkey.split("-")[3];
			// String fornKey = KeyHelper.buildObjectKey(new String[] { azienda,
			// compkey.split("-")[3] });

			VtArticolo artComp = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class, artKeyComp,
					PersistentObject.NO_LOCK);
			BigDecimal qtaComp = new BigDecimal(compkey.split("-")[2]);

			if (magPartenza.getIdFornitoreAzienda() != null) {
				// ESTERNO

				if (magKeyCompOld == null || (magKeyCompOld != null && !magKeyCompOld.equals(magComp))) {
					resC = -1;
					String fornKey = KeyHelper
							.buildObjectKey(new String[] { azienda, magPartenza.getIdFornitoreAzienda() });
					if (rigaOrigine != null) {
						// MONTAGGIO
						testataDocAcqComp = creaDocumentoAcquistoTestata(azienda, fornKey, art, cauTestata, magComp,
								mag, groupTC, annoOrd, numOrd, String.valueOf(rigaOrigine.getNumeroRigaDocumento()));
					} else {
						// SMONTAGGIO

						testataDocAcqComp = creaDocumentoAcquistoTestata(azienda, fornKey, art, cauTestata, mag,
								magComp, groupTC, annoOrd, numOrd,
								String.valueOf(rigaOrigine.getNumeroRigaDocumento()));
					}
					resC = testataDocAcqComp.save();

					magKeyCompOld = magComp;
				}

				if (resC > 0) {
					if (rigaOrigine != null)
						// MONTAGGIO
						rigaDocAcqComp = creaDocumentoAcquistoRigaPrm(testataDocAcqComp, cauTrasfMerce, mag, qtaComp,
								artComp, magComp);
					else
						// SMONTAGGIO
						rigaDocAcqComp = creaDocumentoAcquistoRigaPrm(testataDocAcqComp, cauTrasfMerce, magComp,
								qtaComp, artComp, magComp);

					resC = rigaDocAcqComp.save();

				}

			} else {
				// INTERNO

				if (magKeyCompOld == null || (magKeyCompOld != null && !magKeyCompOld.equals(magComp))) {
					resC = -1;
					if (rigaOrigine != null)
						// MONTAGGIO
						testataMagTraComp = creaDocMagazTrasferimentoTestata(azienda, cauTestata, magComp, mag,
								codFornitore, groupTC, numOrd, annoOrd, rigaOrigine.getNumeroDocumento());
					else {
						// SMONTAGGIO

						testataMagTraComp = creaDocMagazTrasferimentoTestata(azienda, cauTestata, mag, magComp,
								codFornitore, groupTC, numOrd, annoOrd, rigaOrigine.getNumeroDocumento());
					}
					resC = testataMagTraComp.save();

					magKeyCompOld = magComp;
				}

				if (resC > 0) {
					if (rigaOrigine != null) {
						// MONTAGGIO
						rigaMagTraComp = creaDocMagTrasferimentoRiga(testataMagTraComp, artComp, cauTrasfMerce, magComp,
								mag, qtaComp, codFornitore);
					} else {
						rigaMagTraComp = creaDocMagTrasferimentoRiga(testataMagTraComp, artComp, cauTrasfMerce, mag,
								magComp, qtaComp, codFornitore);
					}
					resC = rigaMagTraComp.save();

				}

			}
		}

		return esito;
	}

	private void invokeAlertWindow(ServletEnvironment se, String message, boolean close)
			throws ServletException, IOException {

		PrintWriter wr = se.getResponse().getWriter();
		wr.println("<script language='JavaScript1.2'>");
		wr.println("alert('" + message + "');");
		//wr.println("matrixReolad();");

		
		wr.println("top.window.close();");
		// wr.println("parent.window.opener.runAction('REFRESH_GRID','none','infoArea','no')");

		// wr.println("window.opener.runAction('REFRESH_GRID','none','infoArea','no')");
		
		
		//wr.println("window.opener.location.reload(true)");

		wr.println("</script>");
	}

	public void retrieveValues() {

		ParametroPsn cauMontIntP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleMontaggio");
		ParametroPsn cauSmontIntP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleSmontaggio");
		ParametroPsn cauCaricoIntP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleCarico");
		ParametroPsn cauScaricoIntP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleScarico");
		ParametroPsn cauTrasfEstP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoEst");
		ParametroPsn cauTrasfIntP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoInt");
		ParametroPsn cauTrasfEstMontP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoEstMont");
		ParametroPsn cauTrasfIntMontP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoIntMont");
		ParametroPsn cauTrasfEstSmontP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoEstSmont");
		ParametroPsn cauTrasfIntSmontP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoIntSmont");
		ParametroPsn serieDocAcqCompEstP = ParametroPsn.getParametroPsn("VTMONTSMONT",
				"Serie documento Acquisto trasferimento");
		ParametroPsn cauTrasfMerceP = ParametroPsn.getParametroPsn("VTMONTSMONT", "CausaleTrasferimentoMerce");
		ParametroPsn vistaDisponP = ParametroPsn.getParametroPsn(FUNZIONE_DISP_MAG, PARAM_DISP_MAG);
		ParametroPsn fornitorePsn = ParametroPsn.getParametroPsn("VTOVFORN", "VT_TESSILE_OV_FORNITORE");

		if (fornitorePsn != null && fornitorePsn.getValore() != null)
			codFornitore = fornitorePsn.getValore();

		if (cauMontIntP != null && cauMontIntP.getValore() != null)
			cauMontInt = cauMontIntP.getValore();

		if (cauSmontIntP != null && cauSmontIntP.getValore() != null)
			cauSmontInt = cauSmontIntP.getValore();

		if (cauCaricoIntP != null && cauCaricoIntP.getValore() != null)
			cauCaricoInt = cauCaricoIntP.getValore();
		if (cauScaricoIntP != null && cauScaricoIntP.getValore() != null)
			cauScaricoInt = cauScaricoIntP.getValore();

		if (cauTrasfEstP != null && cauTrasfEstP.getValore() != null)
			cauTrasfEst = cauTrasfEstP.getValore();

		if (cauTrasfIntP != null && cauTrasfIntP.getValore() != null)
			cauTrasfInt = cauTrasfIntP.getValore();

		if (cauTrasfIntMontP != null && cauTrasfIntMontP.getValore() != null)
			cauTrasfIntMont = cauTrasfIntMontP.getValore();

		if (cauTrasfEstMontP != null && cauTrasfEstMontP.getValore() != null)
			cauTrasfEstMont = cauTrasfEstMontP.getValore();

		if (cauTrasfEstSmontP != null && cauTrasfEstSmontP.getValore() != null)
			cauTrasfEstSmont = cauTrasfEstSmontP.getValore();

		if (cauTrasfIntSmontP != null && cauTrasfIntSmontP.getValore() != null)
			cauTrasfIntSmont = cauTrasfIntSmontP.getValore();

		if (cauTrasfMerceP != null && cauTrasfMerceP.getValore() != null)
			cauTrasfMerce = cauTrasfMerceP.getValore();

		if (vistaDisponP != null && vistaDisponP.getValore() != null)
			vistaDisponKey = vistaDisponP.getValore();

		if (serieDocAcqCompEstP != null && serieDocAcqCompEstP.getValore() != null)
			serieDocAcqCompEst = serieDocAcqCompEstP.getValore();
	}

	public List getComponenti(ServletEnvironment se, String idModProd, String vistaDisponKey, String magPart,
			BigDecimal qtaCoeff) {
		List componenti = new ArrayList();

		VistaDisponibilita vistaDisponibilita = null;

		if (!vistaDisponKey.equals("")) {
			String vistaKey = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), vistaDisponKey });
			try {
				vistaDisponibilita = (VistaDisponibilita) PersistentObject.elementWithKey(VistaDisponibilita.class,
						vistaKey, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (vistaDisponibilita != null) {

			ModelloProduttivo modProd = VTMontSmontUtility.getModProdFromArticoloPatron(Azienda.getAziendaCorrente(),
					idModProd);
			VtAttivitaProduttiva attivita = null;
			if (modProd != null && modProd.getAttivita() != null) {
				Iterator attIter = modProd.getAttivita().iterator();
				attivita = (VtAttivitaProduttiva) attIter.next();
				Iterator compIter = attivita.getMateriali().iterator();
				String compKey = null;
				String qtaCompPrm = null;
				String strComponente = "";

				while (compIter.hasNext()) {

					Iterator magIter = vistaDisponibilita.getMagazzini().iterator();

					AttivitaProdMateriale componente = (AttivitaProdMateriale) compIter.next();
					VtArticolo art = VtTessileUtil.getVtArticoloFromId(Azienda.getAziendaCorrente(),
							componente.getIdArticolo());

					while (magIter.hasNext()) {
						Magazzino mag = (Magazzino) magIter.next();
						compKey = VTMontSmontUtility.makeComponenKey(art, componente, mag.getKey());
						if (se != null) {
							qtaCompPrm = getStringParameter(se.getRequest(), compKey);
							if (qtaCompPrm != null && !qtaCompPrm.equals("")) {
								strComponente = art.getKey() + "-" + mag.getIdMagazzino() + "-" + qtaCompPrm;
								componenti.add(strComponente);
							}
						} else if (mag.getKey().equals(magPart)) {

							strComponente = art.getKey() + "-" + mag.getIdMagazzino() + "-"
									+ (componente.getCoeffImpiego().toBigInteger().multiply(qtaCoeff.toBigInteger()))
											.toString();
							componenti.add(strComponente);
						}

					}

				}
			}
		}

		return componenti;
	}

}
