package it.thera.thip.tessile.vendite.ordineVE.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.base.Utils;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.type.DecimalType;
import com.thera.thermfw.web.ServletEnvironment;

import it.thera.thip.acquisti.documentoAC.DocumentoAcqRigaPrm;
import it.thera.thip.acquisti.documentoAC.DocumentoAcquisto;
import it.thera.thip.acquisti.generaleAC.CausaleDocumentoRigaAcq;
import it.thera.thip.acquisti.generaleAC.CausaleOrdineTestataAcq;
import it.thera.thip.acquisti.ordineAC.OrdineAcquisto;
import it.thera.thip.acquisti.trasporti.base.PersDatiTrasporti;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.cliente.Sconto;
import it.thera.thip.base.commessa.Commessa;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.base.documenti.web.DocumentoDatiSessione;
import it.thera.thip.base.fornitore.Fornitore;
import it.thera.thip.base.fornitore.FornitoreAcquisto;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.generale.UnitaMisura;
import it.thera.thip.logis.hst.RigaMovimentoWrapper;
import it.thera.thip.magazzino.documenti.CausaleDocumentoTrasf;
import it.thera.thip.magazzino.documenti.DocMagTrasferimento;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcqRigaPrm;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcquisto;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagTrasferimento;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagTrasferimentoRiga;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVendita;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrm;
import it.thera.thip.vendite.generaleVE.CausaleRigaVendita;

public class VtOrdVenRigaPrmRidMagFormActionAdapter extends VtOrdineVenditaRigaPrmRidottaFormActionAdapter {
	public static final String CLASS_NAME = "thClassName";
	public static final String SDOPPIORIGA = "SDOPPIORIGA";
	public static final String TRASFMAG = "TRASFMAG";
	public static final String ONLYTRASFMAG = "ONLYTRASFMAG";
	public static final String KEY_PAR1_SERIE_MAG = "pers.serie.docmag.pezze";
	public static final String KEY_PAR2_TRASF_SERIE_MAG = "Serie documento magazzino trasferimento";
	String causale = "";
	String causaleTraEst = "";
	String causaleTraInt = "";
	String fornitoreId = null;
	String serieDocAcq = "";
	String serieDocMag = "";

	public void processAction(ServletEnvironment se) throws ServletException, IOException {

		super.processAction(se);
		String azione = getStringParameter(se.getRequest(), "thAction").toUpperCase();

		Trace.println("======================AZIONE=" + azione);
		if (azione.equals(SDOPPIORIGA)) {
			try {
				apriGestioneSdoppioRiga(se);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CopyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (azione.equals(TRASFMAG)) {
			try {
				apriGestioneTrasfMag(se);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CopyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (azione.equals(ONLYTRASFMAG)) {
			try {
				apriGestioneOnlyTrasfMag(se);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CopyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {

		super.otherActions(cadc, se);

	}

	public void apriGestioneSdoppioRiga(ServletEnvironment se)
			throws SQLException, ServletException, IOException, CopyException {

		String tesKey = null;
		String rigaKey = null;
		String qta = getStringParameter(se.getRequest(), "QtaTrasferire");
		String magDest = getStringParameter(se.getRequest(), "IdMagazzinoDest");

		if (qta == null || qta.equals("") || qta.equals("null"))
			qta = "0.00";

		BigDecimal qtariga = new BigDecimal(qta.replace(',', DecimalType.getThousandsSeparator()));
		String azienda = getStringParameter(se.getRequest(), "IdAzienda");

		String annoOrd = getStringParameter(se.getRequest(), "AnnoOrdine");
		String numOrd = getStringParameter(se.getRequest(), "NumeroOrdine");
		String rigaOrd = getStringParameter(se.getRequest(), "RigaOrdine");
		// String dettaRigaOrd = getStringParameter(se.getRequest(),
		// "DettaglioRigaOrdine");

		tesKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd });

		rigaKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd, rigaOrd });

		VtOrdineVendita tesO = null;
		VtOrdineVenditaRigaPrm rigaO = null;

		try {
			tesO = (VtOrdineVendita) VtOrdineVendita.elementWithKey(VtOrdineVendita.class, tesKey,
					VtOrdineVendita.NO_LOCK);
			rigaO = (VtOrdineVenditaRigaPrm) VtOrdineVenditaRigaPrm.elementWithKey(VtOrdineVenditaRigaPrm.class,
					rigaKey, VtOrdineVenditaRigaPrm.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (tesO != null && rigaO != null) {

			BigDecimal qtaO = rigaO.getQtaInUMRif();
			// TODO verificare che la quantità presa in considerazione sia corretta
			// rigaO.getQtaInUMPrmMag();

			if (qtaO.compareTo(qtariga) < 0)
				invokeAlertWindow(se,
						"Attenzione: la quantità indicata deve essere minore o uguale alla  quantità riga originale!",
						false);
			else if (rigaO.getQtaResidua().compareTo(qtariga) < 0)
				invokeAlertWindow(se, "Attenzione: quantità non trasferibile perchè maggiore della quantità residua!",
						false);
			else {
				VtArticolo art = VtTessileUtil.getVtArticoloFromId(azienda, rigaO.getIdArticolo());
				if (art != null) {
					if (sdoppiaOrdineVenditaRiga(azienda, tesO, rigaO, art, qtariga, magDest))
						invokeAlertWindow(se, "Sdoppio riga terminato con successo!", true);
					else
						invokeAlertWindow(se, "ATTENZIONE: Sdoppio riga NON eseguito - verificare i dati inseriti! ",
								true);
				} else
					invokeAlertWindow(se, "ATTENZIONE: Sdoppio riga NON eseguito - aticolo non valido ", true);

			}
		}

	}

	public void apriGestioneTrasfMag(ServletEnvironment se)
			throws ServletException, IOException, SQLException, CopyException {

		retrieveValues();
		String tesKey = null;
		String rigaKey = null;
		List lRigheDocAcq = new ArrayList();

		String qta = getStringParameter(se.getRequest(), "QtaTrasferire");
		String magPartId = getStringParameter(se.getRequest(), "IdMagazzinoPart");
		String magDestId = getStringParameter(se.getRequest(), "IdMagazzinoDest");

		String azienda = getStringParameter(se.getRequest(), "IdAzienda");
		String annoOrd = getStringParameter(se.getRequest(), "AnnoOrdine");
		String numOrd = getStringParameter(se.getRequest(), "NumeroOrdine");
		String rigaOrd = getStringParameter(se.getRequest(), "RigaOrdine");
		// String dettaRigaOrd = getStringParameter(se.getRequest(),
		// "DettaglioRigaOrdine");

		// get fornitore

		String fornitoreKey = KeyHelper.buildObjectKey(new String[] { azienda, fornitoreId });

		tesKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd });

		rigaKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd, rigaOrd });
		String cauKey = KeyHelper.buildObjectKey(new String[] { azienda, causale });
		String magDestKey = KeyHelper.buildObjectKey(new String[] { azienda, magDestId });
		String magPartKey = KeyHelper.buildObjectKey(new String[] { azienda, magPartId });
		VtOrdineVendita tesO = null;
		VtOrdineVenditaRigaPrm rigaO = null;
		Magazzino magPart = null;
		Magazzino magDest = null;

		try {
			tesO = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
					VtOrdineVendita.NO_LOCK);
			rigaO = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, rigaKey,
					VtOrdineVenditaRigaPrm.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (tesO != null && rigaO != null) {
			BigDecimal qtaO = rigaO.getQtaInUMRif();

			/*
			 * CausaleOrdineTestataAcq causVen = (CausaleOrdineTestataAcq) PersistentObject
			 * .elementWithKey(CausaleOrdineTestataAcq.class, cauKey,
			 * PersistentObject.NO_LOCK);
			 */
			if (qta == null || qta.equals("") || qta.equals("null"))
				qta = "0.00";

			BigDecimal qtariga = new BigDecimal(qta.replace(',', DecimalType.getThousandsSeparator()));

			if (qtaO.compareTo(qtariga) < 0)
				invokeAlertWindow(se,
						"Attenzione: la quantità da trasferire deve essere minore o uguale alla  quantità riga originale!",
						false);
			else if (rigaO.getQtaResidua().compareTo(qtariga) < 0)
				invokeAlertWindow(se, "Attenzione: quantità non trasferibile perchè maggiore della quantità residua!",
						false);
			else {
				VtArticolo art = VtTessileUtil.getVtArticoloFromId(azienda, rigaO.getIdArticolo());

				magPart = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magPartKey, Magazzino.NO_LOCK);
				magDest = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magDestKey, Magazzino.NO_LOCK);

				
				if (magPart != null && magDest != null) {
					String idRiga=String.valueOf(rigaO.getNumeroRigaDocumento()) ;
					String gruppoTC=null;
					if (rigaO.getGruppoTC() != null)
						gruppoTC=String.valueOf(rigaO.getGruppoTC().intValue());
					
					
					boolean isMagPartEst = (magPart.getIdFornitoreAzienda() != null);
					boolean isMagDestEst = (magDest.getIdFornitoreAzienda() != null);

					if (!isMagPartEst && !isMagDestEst) {
						// Documento magazzino trasferimento
						DocMagTrasferimento testataTrasf = null;
						
					
						testataTrasf = creaDocMagazTrasferimentoTestata(azienda, art, causale, magDestId, rigaO.getIdMagazzino(),rigaO.getStatoAvanzamento(),
								fornitoreId,gruppoTC,numOrd, annoOrd,idRiga);
						int res = testataTrasf.save();

						if (res >= 0) {
							// TRASFERIMENTO DAL MAGAZZINO AZ AL MAGAZZINO CL
							VtDocMagTrasferimentoRiga rigaDocTrasf = creaDocMagTrasferimentoRiga(testataTrasf, art,
									causale, magPartId, magDestId, qtariga, fornitoreKey);
							res = rigaDocTrasf.save();
						}

						if (res >= 0) {
							if (sdoppiaOrdineVenditaRiga(azienda, tesO, rigaO, art, qtariga, magDestId))
								invokeAlertWindow(se, "Trasferimento terminato con successo!", true);
							else
								invokeAlertWindow(se,
										"ATTENZIONE: trasferimento magazzino fallito! - Errore in fase di sdoppio riga! ",
										true);
						} else {
							ConnectionManager.rollback();
							invokeAlertWindow(se,
									"Attenzione: trasferimento magazzino fallito! Verificare i dati inseriti ", true);
						}

					} else if ((!isMagPartEst && isMagDestEst) || (isMagPartEst && !isMagDestEst)) {
						// Doc Acq trasf con causale TE1 (DDT) se(!isMagPartEst && isMagDestEst)
						// con causale TE2 (BEM) se(isMagPartEst && !isMagDestEst)

						String cauDoc = (!isMagPartEst && isMagDestEst) ? causaleTraEst : causaleTraInt;

						DocumentoAcquisto testata = creaDocumentoAcquistoTestata(fornitoreKey, art, cauDoc, magPartKey,
								magDestKey,gruppoTC,numOrd, annoOrd,idRiga);

						int res = testata.save();
						if (res >= 0 && art != null) {
							VtDocumentoAcqRigaPrm rigaDocAcq = creaDocumentoAcquistoRigaPrm(testata, causale,
									magPartKey, qtariga, art,rigaO.getCommessa(), rigaO.getSconto(),rigaO.getScontoArticolo1() );
							res = rigaDocAcq.save();

							if (res >= 0) {

								if (sdoppiaOrdineVenditaRiga(azienda, tesO, rigaO, art, qtariga, magDestId))
									invokeAlertWindow(se, "Trasferimento terminato con successo!", true);
								else
									invokeAlertWindow(se,
											"ATTENZIONE: trasferimento magazzino fallito! - verificare i dati inseriti! ",
											true);

								/*
								 * // TODO verificare : la quantità è quella totale delle riga?
								 * VtOrdineVenditaRigaPrm rigaOrdPrm = creaOrdineVenditaRigaPrm(azienda, tesO,
								 * rigaO, art, qtaO, magDest);
								 * 
								 * if (rigaOrdPrm.save() >= 0) { tesO.getRighe().add(rigaOrdPrm);
								 * tesO.getRighe().remove(rigaO);
								 * 
								 * int res = tesO.save();
								 * 
								 * if (res >= 0) { ConnectionManager.commit(); invokeAlertWindow(se,
								 * "Trasferimento terminato con successo!", true); } else {
								 * ConnectionManager.rollback(); invokeAlertWindow(se,
								 * "ATTENZIONE: trasferimento magazzino fallito! - verificare i dati inseriti! "
								 * , true); }
								 * 
								 * } else { ConnectionManager.rollback(); invokeAlertWindow(se,
								 * "ATTENZIONE: trasferimento magazzino fallito! - verificare i dati riga inseriti! "
								 * , true); }
								 */

							} else {
								ConnectionManager.rollback();
								invokeAlertWindow(se,
										"Attenzione: trasferimento magazzino fallito! Verificare i dati inseriti ",
										true);

							}

						} else if (isMagPartEst && isMagDestEst) {
							// Doc Acq trasf con causale TE2 (BEM)
							invokeAlertWindow(se,
									"Attenzione: trasferimento magazzino fallito - Trrasferimento tra magazzini esterni non gestito! ",
									true);

						}
					}

				} else {
					ConnectionManager.rollback();
					invokeAlertWindow(se, "Attenzione: trasferimento magazzino fallito! Magazzino non trovato! ", true);

				}

			}
		}
	}

	public void apriGestioneOnlyTrasfMag(ServletEnvironment se)
			throws ServletException, IOException, SQLException, CopyException {
		retrieveValues();
		String tesKey = null;
		String articoloKey = null;
		List lRigheDocAcq = new ArrayList();

		String qta = getStringParameter(se.getRequest(), "QtaTrasferire");
		String magPartId = getStringParameter(se.getRequest(), "IdMagazzinoPart");
		String magDestId = getStringParameter(se.getRequest(), "IdMagazzinoDest");
		String articoloId = getStringParameter(se.getRequest(), "IdArticolo");

		String azienda = getStringParameter(se.getRequest(), "IdAzienda");
		String annoOrd = getStringParameter(se.getRequest(), "AnnoOrdine");
		String numOrd = getStringParameter(se.getRequest(), "NumeroOrdine");
		String rigaOrd = getStringParameter(se.getRequest(), "RigaOrdine");
		String gruppoTC = getStringParameter(se.getRequest(), "GruppoTC");
		// String dettaRigaOrd = getStringParameter(se.getRequest(),
		// "DettaglioRigaOrdine");
		String rigaKey=null;
		// get fornitore

		String fornitoreKey = KeyHelper.buildObjectKey(new String[] { azienda, fornitoreId });

		tesKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd });
		rigaKey = KeyHelper.buildObjectKey(new String[] { azienda, annoOrd, numOrd, rigaOrd });
		
		articoloKey = KeyHelper.buildObjectKey(new String[] { azienda, articoloId});
		String cauKey = KeyHelper.buildObjectKey(new String[] { azienda, causale });
		String magDestKey = KeyHelper.buildObjectKey(new String[] { azienda, magDestId });
		String magPartKey = KeyHelper.buildObjectKey(new String[] { azienda, magPartId });
		VtOrdineVendita tesO = null;
		VtOrdineVenditaRigaPrm rigaO = null;
		Magazzino magPart = null;
		Magazzino magDest = null;
		String idRiga=null;
		
		try {
			tesO = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
					VtOrdineVendita.NO_LOCK);
			rigaO = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, rigaKey,
					VtOrdineVenditaRigaPrm.NO_LOCK);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//if (tesO != null) {

			if (qta == null || qta.equals("") || qta.equals("null"))
				qta = "0.00";

			BigDecimal qtariga = new BigDecimal(qta.replace(',', DecimalType.getThousandsSeparator()));

			
			VtArticolo art =(VtArticolo) PersistentObject.elementWithKey(VtArticolo.class, articoloKey,
					VtArticolo.NO_LOCK);

			magPart = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magPartKey, Magazzino.NO_LOCK);
			magDest = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magDestKey, Magazzino.NO_LOCK);

			if (magPart != null && magDest != null) {

				boolean isMagPartEst = (magPart.getIdFornitoreAzienda() != null);
				boolean isMagDestEst = (magDest.getIdFornitoreAzienda() != null);
				if(rigaO!=null)
				{
					idRiga=String.valueOf(rigaO.getNumeroRigaDocumento()) ;
					if (rigaO.getGruppoTC() != null)
						gruppoTC=String.valueOf(rigaO.getGruppoTC().intValue());
				}
				
				if (!isMagPartEst && !isMagDestEst) {
					// Documento magazzino trasferimento
					DocMagTrasferimento testataTrasf = null;
					
					testataTrasf = creaDocMagazTrasferimentoTestata(azienda, art, causale, magDestId, magPartId,StatoAvanzamento.DEFINITIVO,
							fornitoreId,gruppoTC,numOrd,annoOrd,idRiga);
					int res = testataTrasf.save();

					if (res >= 0) {
						// TRASFERIMENTO DAL MAGAZZINO AZ AL MAGAZZINO CL
						VtDocMagTrasferimentoRiga rigaDocTrasf = creaDocMagTrasferimentoRiga(testataTrasf, art, causale,
								magPartId, magDestId, qtariga, fornitoreKey);
						res = rigaDocTrasf.save();
					}
					if (res >= 0) {
						ConnectionManager.commit();
							invokeAlertWindow(se, "Trasferimento terminato con successo!", true);
						
					} else {
						ConnectionManager.rollback();
						invokeAlertWindow(se,
								"Attenzione: trasferimento magazzino fallito! Verificare i dati inseriti ", true);
					}

				} else if ((!isMagPartEst && isMagDestEst) || (isMagPartEst && !isMagDestEst)) {
					// Doc Acq trasf con causale TE1 (DDT) se(!isMagPartEst && isMagDestEst)
					// con causale TE2 (BEM) se(isMagPartEst && !isMagDestEst)

					String cauDoc = (!isMagPartEst && isMagDestEst) ? causaleTraEst : causaleTraInt;

					DocumentoAcquisto testata = creaDocumentoAcquistoTestata(fornitoreKey, art, cauDoc, magPartKey,
							magDestKey,gruppoTC,numOrd,annoOrd,idRiga);

					int res = testata.save();
					if (res >= 0 && art != null) {
						VtDocumentoAcqRigaPrm rigaDocAcq = creaDocumentoAcquistoRigaPrm(testata, causale,
								magPartKey, qtariga, art, null, null, null);
						res = rigaDocAcq.save();

						if (res >= 0) {
							ConnectionManager.commit();
								invokeAlertWindow(se, "Trasferimento terminato con successo!", true);
							
						} else {
							ConnectionManager.rollback();
							invokeAlertWindow(se,
									"Attenzione: trasferimento magazzino fallito! Verificare i dati inseriti ",
									true);

						}
						
					} else if (isMagPartEst && isMagDestEst) {
						// Doc Acq trasf con causale TE2 (BEM)
						invokeAlertWindow(se,
								"Attenzione: trasferimento magazzino fallito - Trrasferimento tra magazzini esterni non gestito! ",
								true);

					}
				}

			} else {
				ConnectionManager.rollback();
				invokeAlertWindow(se, "Attenzione: trasferimento magazzino fallito! Magazzino non trovato! ", true);

			}

		//}
			
		
	}

	public DocMagTrasferimento creaDocMagazTrasferimentoTestata(String azie, VtArticolo art, String idCauDoc,
			String magArrivo,String mag, char statoAvanzamento, String idFor,String gruppoTC, String numOrd,
			String annoOrd, String numRiga ) throws SQLException {
		VtDocMagTrasferimento docTestata = (VtDocMagTrasferimento) Factory.createObject(VtDocMagTrasferimento.class);

		String cauKey = KeyHelper.buildObjectKey(new String[] { azie, idCauDoc });
		CausaleDocumentoTrasf causVenTra = (CausaleDocumentoTrasf) PersistentObject
				.elementWithKey(CausaleDocumentoTrasf.class, cauKey, PersistentObject.NO_LOCK);

		Date dataFine = TimeUtils.getCurrentDate();

		docTestata.setIdAzienda(azie);

		docTestata.setIdCau(causVenTra.getIdCausale());
		docTestata.setIdMagazzino(mag);
		docTestata.setCodiceMagazzinoArrivo(magArrivo);

		docTestata.setStatoAvanzamento(statoAvanzamento);

		docTestata.getNumeratoreHandler().setDataDocumento(dataFine);

		//Trace Documento
		docTestata.setAnnoOrdineCli(annoOrd);
		docTestata.setNumeroOrdineCli(numOrd);
		if (gruppoTC!=null && !gruppoTC.equals(""))
			docTestata.setDettaglioRigaOrdineCli(Integer.parseInt(gruppoTC));
		if(numRiga!=null && !numRiga.equals(""))
			docTestata.setRigaOrdineCli(Integer.parseInt(numRiga));
		
		docTestata.getNumeratoreHandler().setIdSerie(serieDocMag);

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
		testata.getRighe().add(docGenRiga);

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

	public boolean sdoppiaOrdineVenditaRiga(String azienda, VtOrdineVendita testata,
			VtOrdineVenditaRigaPrm rigaOriginale, VtArticolo art, BigDecimal qtaRiga, String magDest)
			throws SQLException, CopyException {
		boolean result = false;
		VtOrdineVenditaRigaPrm rigaOrdPrm = null;
		String keyRiga = null;

		if (rigaOriginale.getGruppoTC() != null)
			keyRiga = checkExistRiga(testata, rigaOriginale.getGruppoTC(), art, magDest, qtaRiga);
		/*
		 * if (keyRiga != null) rigaOrdPrm = agggiornaOrdineVenditaRigaPrm(azienda,
		 * keyRiga, testata, qtaRiga, rigaOriginale, art, magDest); else rigaOrdPrm =
		 * creaOrdineVenditaRigaPrm(azienda, testata, qtaRiga, rigaOriginale, art,
		 * magDest);
		 */
		rigaOriginale.setQtaInUMRif(rigaOriginale.getQtaInUMRif().subtract(qtaRiga));
		QuantitaInUMRif qrif = null;
		// ricalcolo riga originale
		if (art != null)
			qrif = art.calcolaQuantitaArrotondate(rigaOriginale.getQtaInUMRif(), art.getUMRiferimento(),
					art.getUMPrmMag(), art.getUMSecMag(),
					art.getVersioneAtDate(testata.getNumeratoreHandler().getDataDocumento()), Articolo.UM_RIF);
		rigaOriginale.setQtaInUMPrmMag(qrif.getQuantitaInUMPrm());
		rigaOriginale.setQtaInUMSecMag(qrif.getQuantitaInUMSec());

		int res = rigaOriginale.save();

		if (keyRiga != null)
			rigaOrdPrm = agggiornaOrdineVenditaRigaPrm(azienda, keyRiga, testata, qtaRiga, rigaOriginale, art, magDest);
		else
			rigaOrdPrm = creaOrdineVenditaRigaPrm(azienda, testata, qtaRiga, rigaOriginale, art, magDest);

		if (res > 0) {
			// BRY bisogno settare la testata altrimenti la save di due righe
			// contemporaneamente
			// va in errore save -1 non aggiornando i saldi portafoglio

			// rigaOrdPrm.setTestata(rigaOriginale.getTestata());
			// rigaOrdPrm.retrieve();
			res = rigaOrdPrm.save();

			if (rigaOriginale.getQtaInUMRif().equals(new BigDecimal("0.00"))) {
				testata.getRighe().remove(rigaOriginale);
				rigaOriginale.delete();

			}

			if (res >= 0) {
				ConnectionManager.commit();
				result = true;

			} else
				ConnectionManager.rollback();

		} else
			ConnectionManager.rollback();

		return result;
	}

	public String checkExistRiga(VtOrdineVendita testata, long gruppoTC, VtArticolo art, String magDest,
			BigDecimal qtaRiga) throws SQLException, CopyException {
		String rigaKey = null;

		List righeDoc = testata.getRighe();
		Iterator righeIter = righeDoc.iterator();

		while (righeIter.hasNext()) {
			VtOrdineVenditaRigaPrm riga = (VtOrdineVenditaRigaPrm) righeIter.next();
			if (riga.getArticoloKey().equals(art.getKey()) && riga.getGruppoTC() != null
					&& gruppoTC == riga.getGruppoTC() && riga.getIdMagazzino().equals(magDest)) {
				return riga.getKey();

			}
		}

		return rigaKey;

	}

	public VtOrdineVenditaRigaPrm creaOrdineVenditaRigaPrm(String azienda, VtOrdineVendita testata, BigDecimal qtaRiga,
			VtOrdineVenditaRigaPrm rigaOriginale, VtArticolo art, String magDest) throws SQLException, CopyException {

		VtOrdineVenditaRigaPrm rigaOrdPrm = null;
		/*
		 * String keyRiga = null; if (rigaOriginale.getGruppoTC() != null) keyRiga =
		 * checkExistRiga(testata, rigaOriginale.getGruppoTC(), art, magDest); //
		 * rigaOrdPrm = (VtOrdineVenditaRigaPrm) //
		 * Factory.createObject(VtOrdineVenditaRigaPrm.class); if (keyRiga != null)
		 * rigaOrdPrm = (VtOrdineVenditaRigaPrm)
		 * VtOrdineVenditaRigaPrm.elementWithKey(VtOrdineVenditaRigaPrm.class,
		 * keyRiga,VtOrdineVenditaRigaPrm.OPTIMISTIC_LOCK);
		 * 
		 * if (rigaOrdPrm == null) { rigaOrdPrm = (VtOrdineVenditaRigaPrm)
		 * Factory.createObject(VtOrdineVenditaRigaPrm.class);
		 * 
		 * rigaOrdPrm.setQtaInUMRif(qtaRiga); } else
		 * rigaOrdPrm.setQtaInUMRif(rigaOrdPrm.getQtaInUMRif().add(qtaRiga));
		 */
		rigaOrdPrm = (VtOrdineVenditaRigaPrm) Factory.createObject(VtOrdineVenditaRigaPrm.class);
		// rigaOrdPrm.setEqual(rigaOriginale);
		rigaOrdPrm.setTestata(testata);
		rigaOrdPrm.setIdAzienda(azienda);

		// set campi obbligatori
		rigaOrdPrm.completaBO();
		rigaOrdPrm.setIdCauRig(testata.getCausale().getIdCausaleRigaOrdVen());

		rigaOrdPrm.setVtSettore(testata.getVtSettore());
		rigaOrdPrm.setRSettore(testata.getRSettore());
		rigaOrdPrm.setVtStagione(testata.getVtStagione());
		rigaOrdPrm.setRStagione(testata.getRStagione());

		String magKey = KeyHelper.buildObjectKey(new String[] { azienda, magDest });
		Magazzino magazzino = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magKey, Magazzino.NO_LOCK);

		if (magazzino != null)
			rigaOrdPrm.setMagazzino(magazzino);
		else
			rigaOrdPrm.setMagazzino(rigaOriginale.getMagazzino());

		rigaOrdPrm.setGruppoTC(rigaOriginale.getGruppoTC());
		rigaOrdPrm.setAlfanumRiservatoUtente2(rigaOriginale.getAlfanumRiservatoUtente2());
		rigaOrdPrm.setIdArticolo(rigaOriginale.getIdArticolo());
		if (art.getAssoggettamentoIVA() != null)
			rigaOrdPrm.setAssoggettamentoIVA(art.getAssoggettamentoIVA());
		else
			rigaOrdPrm.setAssoggettamentoIVA(testata.getAssoggettamentoIVA());

		rigaOrdPrm.setUMRif(art.getUMRiferimento());
		rigaOrdPrm.setUMPrm(art.getUMPrmMag());
		rigaOrdPrm.setUMSec(art.getUMSecMag());
		rigaOrdPrm.setRicalcoloQtaFattoreConv(true);

		rigaOrdPrm.setDataConsegnaRichiesta(rigaOriginale.getDataConsegnaRichiesta());
		rigaOrdPrm.setDataConsegnaConfermata(rigaOriginale.getDataConsegnaConfermata());
		rigaOrdPrm.setDataConsegnaProduzione(rigaOriginale.getDataConsegnaProduzione());

		rigaOrdPrm.setAltezzaMultipla('1');

		rigaOrdPrm.setIdAgente(rigaOriginale.getIdAgente());
		rigaOrdPrm.setProvvigione1Agente(rigaOriginale.getProvvigione1Agente());
		rigaOrdPrm.setSconto(rigaOriginale.getSconto());
		rigaOrdPrm.setScontoArticolo1(rigaOriginale.getScontoArticolo1());

		// Setto la quantità a 1, per evitare il caso in cui l'utente non abbia inserito
		// la quantità
		// decremento la quantità della riga sdoppiata della qta da caricare sulla nuova
		// riga

		rigaOrdPrm.setCommessa(rigaOriginale.getCommessa());
		rigaOrdPrm.setQtaInUMRif(qtaRiga);
		rigaOrdPrm.setStatoAvanzamento(rigaOriginale.getStatoAvanzamento());

		rigaOrdPrm.setListinoVendita(testata.getListinoPrezzi());

		QuantitaInUMRif qrif = null;
		// ricalcolo nuova riga
		if (art != null)
			qrif = art.calcolaQuantitaArrotondate(rigaOrdPrm.getQtaInUMRif(), art.getUMRiferimento(), art.getUMPrmMag(),
					art.getUMSecMag(), art.getVersioneAtDate(testata.getNumeratoreHandler().getDataDocumento()),
					Articolo.UM_RIF);
		rigaOrdPrm.setQtaInUMPrmMag(qrif.getQuantitaInUMPrm());
		rigaOrdPrm.setQtaInUMSecMag(qrif.getQuantitaInUMSec());

		// rigaOriginale.applicoIMovPortafoglioConGesSaldi();
		return rigaOrdPrm;

	}

	public VtOrdineVenditaRigaPrm agggiornaOrdineVenditaRigaPrm(String azienda, String keyRiga, VtOrdineVendita testata,
			BigDecimal qtaRiga, VtOrdineVenditaRigaPrm rigaOriginale, VtArticolo art, String magDest) {
		VtOrdineVenditaRigaPrm rigaOrdPrm = null;

		if (keyRiga != null)
			try {
				rigaOrdPrm = (VtOrdineVenditaRigaPrm) VtOrdineVenditaRigaPrm
						.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRiga, VtOrdineVenditaRigaPrm.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (rigaOrdPrm != null) {
			rigaOrdPrm.setQtaInUMRif(rigaOrdPrm.getQtaInUMRif().add(qtaRiga));

			QuantitaInUMRif qrif = null;
			// ricalcolo nuova riga
			if (art != null)
				qrif = art.calcolaQuantitaArrotondate(rigaOrdPrm.getQtaInUMRif(), art.getUMRiferimento(),
						art.getUMPrmMag(), art.getUMSecMag(),
						art.getVersioneAtDate(testata.getNumeratoreHandler().getDataDocumento()), Articolo.UM_RIF);
			rigaOrdPrm.setQtaInUMPrmMag(qrif.getQuantitaInUMPrm());
			rigaOrdPrm.setQtaInUMSecMag(qrif.getQuantitaInUMSec());

		}

		return rigaOrdPrm;
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

	public DocumentoAcquisto creaDocumentoAcquistoTestata(String fornAcqKey, VtArticolo art, String idCausale,
			String magP, String magD,  String groupTC,String numOrd, String annoOrd ,String idRiga) throws SQLException {

		FornitoreAcquisto fornAcq = (FornitoreAcquisto) PersistentObject.elementWithKey(FornitoreAcquisto.class,
				fornAcqKey, PersistentObject.NO_LOCK);

		Date dataFine = TimeUtils.getCurrentDate();
		VtDocumentoAcquisto nuovoDocAcq = (VtDocumentoAcquisto) Factory.createObject(VtDocumentoAcquisto.class);

		boolean salvato = false;
		nuovoDocAcq.setIdAzienda(Azienda.getAziendaCorrente());
		nuovoDocAcq.getNumeratoreHandler().setDataDocumento(dataFine);
		nuovoDocAcq.setMagazzinoKey(magD);
		nuovoDocAcq.setMagazzinoTrasferimentoKey(magP);
		nuovoDocAcq.getNumeratoreHandler().setIdSerie(serieDocAcq);

		nuovoDocAcq.setIdCau(idCausale);

		nuovoDocAcq.setIdFornitore(fornAcq.getIdFornitore());
		nuovoDocAcq.setIdAnagrafico(fornAcq.getIdAnagrafico());
		
		//trace doc
		nuovoDocAcq.setAnnoOrdineCli(annoOrd);
		nuovoDocAcq.setNumeroOrdineCli(numOrd);
		if (groupTC != null && !groupTC.equals(""))
			nuovoDocAcq.setDettaglioRigaOrdineCli(Integer.parseInt(groupTC));
		if (idRiga != null && !idRiga.equals(""))
			nuovoDocAcq.setRigaOrdineCli(Integer.valueOf(idRiga));
		
		
		// String assIva = recuperaIdAssogIva(fornAcq);
		// nuovoDocAcq.setIdAssogIva(assIva);

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
		nuovoDocAcq.setStatoAvanzamento(StatoAvanzamento.DEFINITIVO);
		// salvato = VtTessileUtil.genericSave(nuovoDocAcq);

		// if (salvato)
		return nuovoDocAcq;
		// else
		// return null;

	}

	public VtDocumentoAcqRigaPrm creaDocumentoAcquistoRigaPrm(DocumentoAcquisto testata,
			 String idCausaleRiga, String magKey, BigDecimal totQta, 
			VtArticolo art, Commessa comm, Sconto sconto, BigDecimal scontoArt1) {

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
		nuovaRigaDocAcq.setMagazzinoKey(magKey);
		nuovaRigaDocAcq.setStatoAvanzamento(testata.getStatoAvanzamento());
		nuovaRigaDocAcq.setCommessa(comm);
		nuovaRigaDocAcq.setSconto(sconto);
		nuovaRigaDocAcq.setScontoArticolo1(scontoArt1);

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

	private void invokeAlertWindow(ServletEnvironment se, String message, boolean close)
			throws ServletException, IOException {

		PrintWriter wr = se.getResponse().getWriter();
		wr.println("<script language='JavaScript1.2'>");
		wr.println("alert('" + message + "');");
		//wr.println("var radice= parent.window.opener.document.getElementById('header').value;");
		//wr.println("alert(radice);");
		//wr.println("parent.window.opener.runAction('REFRESH_GRID','none','infoArea','no');");
		//wr.println("window.opener.location.reload(true)");
		wr.println("top.window.close();");
		// else {
		// wr.println("top.window.location.reload(false);");

		// }

		//wr.println("window.top.opener.runAction('REFRESH_GRID','none','infoArea','no')");

		wr.println("</script>");
	}

	public void retrieveValues() {

		ParametroPsn fornitorePsn = ParametroPsn.getParametroPsn("VTOVFORN", "VT_TESSILE_OV_FORNITORE");
		ParametroPsn causalePsn = ParametroPsn.getParametroPsn("VTOVCAUTRA", "VT_TESSILE_OV_CAU_TRA");
		ParametroPsn causaleTraEstPsn = ParametroPsn.getParametroPsn("VTOVCAUTRA", "VT_TESSILE_OV_CAU_TRA_EST");
		ParametroPsn causaleTraIntPsn = ParametroPsn.getParametroPsn("VTOVCAUTRA", "VT_TESSILE_OV_CAU_TRA_INT");
		ParametroPsn serieDocAcqPsn = ParametroPsn.getParametroPsn("VTMONTSMONT",
				"Serie documento Acquisto trasferimento");

		ParametroPsn serieDocMagPsn = ParametroPsn.getParametroPsn(KEY_PAR1_SERIE_MAG, KEY_PAR2_TRASF_SERIE_MAG);
		if (serieDocMagPsn != null && serieDocMagPsn.getValore() != null)
			serieDocMag = serieDocMagPsn.getValore();

		if (fornitorePsn != null && fornitorePsn.getValore() != null)
			fornitoreId = fornitorePsn.getValore();

		if (causalePsn != null && causalePsn.getValore() != null)
			causale = causalePsn.getValore();

		if (causaleTraEstPsn != null && causaleTraEstPsn.getValore() != null)
			causaleTraEst = causaleTraEstPsn.getValore();

		if (causaleTraIntPsn != null && causaleTraIntPsn.getValore() != null)
			causaleTraInt = causaleTraIntPsn.getValore();

		if (serieDocAcqPsn != null && serieDocAcqPsn.getValore() != null)
			serieDocAcq = serieDocAcqPsn.getValore();

	}

}
