package it.thera.thip.tessile.vendite.ordineVE;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.OneToMany;
import com.thera.thermfw.persist.PersistentCollection;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.type.DateType;
import com.thera.thermfw.type.DecimalType;
import com.thera.thermfw.web.WebForm;

import it.thera.thip.acquisti.ordineAC.OrdineAcquistoRigaLottoPrm;
import it.thera.thip.base.agentiProvv.Agente;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.articolo.ArticoloBase;
import it.thera.thip.base.articolo.ClasseMateriale;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.articolo.ArticoloCliente;
import it.thera.thip.base.articolo.ArticoloDataCollector;
import it.thera.thip.base.articolo.ArticoloDatiIdent;
import it.thera.thip.base.articolo.ArticoloTM;
import it.thera.thip.base.articolo.ArticoloVersione;
import it.thera.thip.base.cliente.ClienteVendita;
import it.thera.thip.base.cliente.ModalitaConsegna;
import it.thera.thip.base.cliente.ModalitaSpedizione;
import it.thera.thip.base.cliente.Spesa;
import it.thera.thip.base.comuniVenAcq.OrdineRigaLotto;
import it.thera.thip.base.comuniVenAcq.QuantitaInUMRif;
import it.thera.thip.base.comuniVenAcq.RifDataPrzScn;
import it.thera.thip.base.comuniVenAcq.StatoEvasione;
import it.thera.thip.base.comuniVenAcq.TipoRiga;
import it.thera.thip.base.comuniVenAcq.TipoRigaRicerca;
import it.thera.thip.base.documenti.DocumentoBaseRiga;
import it.thera.thip.base.fornitore.FornitoreAcquisto;
import it.thera.thip.base.generale.NumeratoreHandler;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.base.generale.UnitaMisura;
import it.thera.thip.base.listini.ListinoVendita;
import it.thera.thip.base.prezziExtra.CondizioniVEPrezziExtra;
import it.thera.thip.base.prezziExtra.DocOrdRigaPrezziExtra;
import it.thera.thip.base.prezziExtra.RicercaPrezziExtraVendita;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.cs.ThipException;
import it.thera.thip.magazzino.generalemag.Lotto;
import it.thera.thip.tessile.acquisti.ordineAC.VtOrdAcqRigAtv;
import it.thera.thip.tessile.articolo.VtArticoliTex;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.articolo.VtArticoloClienteRic;
import it.thera.thip.tessile.articolo.VtFibraArt;
import it.thera.thip.tessile.base.articolo.ChiaveProdotto;
import it.thera.thip.tessile.base.articolo.ChiaveProdottoTM;
import it.thera.thip.tessile.base.articolo.SchemaProdottoLotto;
import it.thera.thip.tessile.base.articolo.VtIntraRicerca;
import it.thera.thip.tessile.base.listini.VtMaggiorZonaValutaCV;
import it.thera.thip.tessile.cliente.VtClienteDivisione;
import it.thera.thip.tessile.tabelle.VtArticoloGetChiaveProdotto;
import it.thera.thip.tessile.tabelle.VtBuyer;
import it.thera.thip.tessile.tabelle.VtEtichetta;
import it.thera.thip.tessile.tabelle.VtGestioneRiserveLotti;
import it.thera.thip.tessile.tabelle.VtIndirizzo;
import it.thera.thip.tessile.tabelle.VtLavorazioni;
import it.thera.thip.tessile.tabelle.VtLavorazioniAtv;
import it.thera.thip.tessile.tabelle.VtNazione;
import it.thera.thip.tessile.tabelle.VtPezza;
import it.thera.thip.tessile.tabelle.VtPezzaTM;
import it.thera.thip.tessile.tabelle.VtSchemiAbbinati;
import it.thera.thip.tessile.tabelle.VtSettore;
import it.thera.thip.tessile.tabelle.VtStagione;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.vendite.generaleVE.VtCausaleOrdineVendita;
import it.thera.thip.tessile.vendite.ordineVE.servlet.VtDeleteLottoRow;
import it.thera.thip.tessile.vendite.ordineVE.servlet.VtFillOrdVenRigVar;
import it.thera.thip.tessile.vendite.ordineVE.servlet.VtOrdVenRigAtvSetPrezzo;
import it.thera.thip.tessile.vendite.ordineVE.servlet.VtOrdVenRigCaricaMaggiorazioni;
import it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineVenditaRigaPrmCompletaFormModifier;
import it.thera.thip.tessile.vendite.ordineVE.web.VtOrdineVenditaRigaPrmRidottaSave;
import it.thera.thip.vendite.documentoVE.DocumentoVenRigaLotto;
import it.thera.thip.vendite.generaleVE.CausaleOrdineVendita;
import it.thera.thip.vendite.generaleVE.CausaleRigaOrdVen;
import it.thera.thip.vendite.generaleVE.CausaleRigaVendita;
import it.thera.thip.vendite.generaleVE.CondizioniDiVendita;
import it.thera.thip.vendite.generaleVE.PersDatiVen;
import it.thera.thip.vendite.generaleVE.RicercaCondizioniDiVendita;
import it.thera.thip.vendite.ordineVE.OrdineVendita;
import it.thera.thip.vendite.ordineVE.OrdineVenditaPO;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaLotto;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaLottoPrm;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrm;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrmTM;
import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaTM;

/*
 * Revisions:
 * Number  	Date        Owner     	Description
 * 80279   	23/04/2015	ANDC		Prima versione 
 * 80364				ANDC		gestione maggiorazoni
 * 80444				ANDC		Nuovi campi aggiunti
 * 80484	05/02/2016	ANDC		Nuovi campi ID_PRODOTTO (parziale e completo)
 * 80498	18/02/2016	ANDC		Nuovi campi prodotto finito
 * 80540   	23/03/2016	ANDC		Aggiunti altri campi per gestione righe prodotto finito
 * 80563   	06/05/2016	ANDC		Aggiunti altri campi per riferimenti ordine a disporre associato (per save)
 * 80669   	04/07/2016	ANDC		Aggiunto campo Variante per parzializzare Articolo Cliente (facoltativo)
 * 80679   	11/07/2016	ANDC		Aggiunto campo Articolo per cliente
 * 80660    13/07/2016  CMB         Modificati i proxy riferiti alle tabelle rese interaziendali
 * 80691    19/07/2016	ANDC		Aggiunti campi subagenti 2, 3 e 4, con relative provvigioni 1 e 2 per i nuovi subagenti
 * 80722    30/08/2016	ANDC		Aggiunti attributi di servizio calcolati: TotalePezze e MaxStatoPezze, per utilizzo su descrittore e su viste righe ordini
 * 80723    05/09/2016	ANDC		Aggiunti attributi di servizio per forzatura esclusiva clienti (quando prevista)
 * 81347    16/05/2018  NCPR   	    Aggiungere controllo valuta cliente - listino - Maggiorazione Zona Valuta
 * 81366    21/06/2018  NCPR		Gestione INTRA
 * 81368    25/06/2018  NCPR	    Modifica controllo valuta: documento - listino
 * 81373    02/07/2018  NCPR        Aggiungere controllo causale testata per la ricerca INTRA + flag Ricerca INTRA
 */

public class VtOrdineVenditaRigaPrm extends OrdineVenditaRigaPrm {

	// Fix 80364 - Gestione griglia maggiorazioni
	// protected PersistentCollection iVtMaggiorOrdVenRig = new
	// PersistentCollection(VtMaggiorOrdVenRig.class, "", "", "retrieveList");
	// chave 31 (idAzienda-1, idAnnoOrd-3, idNumeroOrd-7, idRigaOrd-15,
	// idDetRigaOrd-31)
	protected OneToMany iVtMaggiorOrdVenRig = new OneToMany(
			it.thera.thip.tessile.vendite.ordineVE.VtMaggiorOrdVenRig.class, this, 31, true);

	protected static CachedStatement csRicDocMontSmontRig;

	protected char iDisporre = 0; // Fix 81753

	protected char iEliminata = 0; // Fix 81880

	// Fix 80364 - Variabili da visualizzare sotto la griglia
	protected BigDecimal iTotPrz;
	protected BigDecimal iTotMag;
	protected BigDecimal iTotale;
	protected BigDecimal iTotalePrz;
	// ----- Fix 80358 - Inizio
	// Estensione per aggiunta di campi
	protected BigDecimal iLungPezFin;
	protected String iRFornitore;
	protected String iRModSpedizione;
	protected String iRModConsegna;
	protected String iRDivisioneCliente;
	protected String iRSettore;
	// ----- Fix 80358 - Fine
	protected Integer iSettaggio; // fix 80444 - Campo da STRING a INTEGER
	protected String iRArtPrimario; // fix 80444 - Nuovo
	protected String iRArtOrdinato; // fix 80444 - Nuovo
	protected Integer iRArtCli; // fix 80444 - Nuovo
	protected BigDecimal iNumeroPezze; // fix 80444 - Nuovo
	protected String iRBuyer; // fix 80444 - Nuovo
	protected String iRStagione; // fix 80444 - Nuovo
	protected String iIdCliente; // fix 80444 - Nuovo
	// fix 80484 - inizio
	protected String iIdPrdFinoVar;
	protected String iIdProdotto;
	// fix 80484 - fine
	// fix 80498 - inizio
	protected String iRArtFinito;
	protected BigDecimal iQtaVenFinito;
	protected String iUmVenFinito;
	protected BigDecimal iCoeffTessuto;
	protected char iTipologiaOrdine = '-';
	protected Integer iRigaPreord;
	protected Integer iDetPreord;
	protected String iREtichetta;
	// fix 80498 - fine
	// fix 80540 - inizio
	protected BigDecimal iQtaDaProd;
	protected java.sql.Date iDataConsRich;
	protected java.sql.Date iDataConsRichTes;
	protected boolean iPrzManuale;
	// fix 80540 - fine

	// fix 80563 - inizio
	// riferimenti ad ordine a disporre (solo se per righe derivate da ordini a
	// disporre)
	protected String iAnnoAdisp;
	protected String iNumAdisp;
	protected Integer iRigAdisp;
	protected Integer iDetAdisp;
	// fix 80563 - fine

	// fix 80358 - inizio
	protected Proxy iFornitoreAcquisto = new Proxy(it.thera.thip.base.fornitore.FornitoreAcquisto.class);
	protected Proxy iModalitaSpedizione = new Proxy(it.thera.thip.base.cliente.ModalitaSpedizione.class);
	protected Proxy iModalitaConsegna = new Proxy(it.thera.thip.base.cliente.ModalitaConsegna.class);
	protected Proxy iVtClienteDivisione = new Proxy(it.thera.thip.tessile.cliente.VtClienteDivisione.class);
	protected Proxy iVtSettore = new Proxy(it.thera.thip.tessile.tabelle.VtSettore.class);
	// fix 80358 - fine
	// fix 80444 - inizio (nuovi proxy)
	protected Proxy iVtArticoloPrm = new Proxy(it.thera.thip.base.articolo.Articolo.class);
	protected Proxy iVtArticolo = new Proxy(it.thera.thip.base.articolo.Articolo.class);
	protected Proxy iArticoloCliente = new Proxy(it.thera.thip.base.articolo.ArticoloCliente.class);
	protected Proxy iVtBuyer = new Proxy(it.thera.thip.tessile.tabelle.VtBuyer.class);
	protected Proxy iVtStagione = new Proxy(it.thera.thip.tessile.tabelle.VtStagione.class);
	// fix 80444 - fine
	// fix 80498 - inizio (nuovi proxy)
	protected Proxy iVtArticoloFin = new Proxy(it.thera.thip.base.articolo.Articolo.class);
	protected Proxy iArticoloFin = new Proxy(it.thera.thip.base.articolo.Articolo.class);
	protected Proxy iUMFin = new Proxy(UnitaMisura.class);
	protected Proxy iRUMFin = new Proxy(UnitaMisura.class);
	protected Proxy iVtEtichetta = new Proxy(VtEtichetta.class);
	// fix 80498 - fine
	// fix 80503 - inizio
	protected String iRifCliente;
	protected String iDStagione;
	protected String iRCliente;
	protected String iDCliente;

	protected Integer iLivelli = new Integer(3);
	protected String iIdArtCorrente;
	protected BigDecimal iQtaRichiestaCorrente;
	protected BigDecimal iQtaTaglio;
	// fix 80503 - fine

	// fix 80503 - inizio
	protected String iRDivTex;
	protected String iDDivTex;
	// fix 80503 - fine

	// fix 80556 - inizio (aggiunte date consegna tessuto)
	protected String iSettConsRichTes;
	protected java.sql.Date iDataConsConfTes;
	protected String iSettConsConfTes;
	protected java.sql.Date iDataConsProdTes;
	protected String iSettConsProdTes;
	// fix 80556 - fine

	// fix 80563 - inizio (aggiunti riferimenti ordini a disporre)
	protected BigDecimal iQtaDisposta;
	protected Integer iNumRigaAdisp;
	protected Integer iDetRigaAdisp;
	protected BigDecimal iQtaResidua;
	protected BigDecimal iQtaIniziale;
	// fix 80563 - fine

	// fix 80669 - inizio (aggiunta variante per parzializzare articolo cliente)
	protected String iVarianteArtcli;
	// fix 80669 - fine

	// fix 80679 - inizio (aggiunta variante per parzializzare articolo cliente)
	protected String iArticoloCli;
	protected Proxy iVtArticoloClienteRic = new Proxy(it.thera.thip.tessile.articolo.VtArticoloClienteRic.class);
	// fix 80679 - fine

	// Fix 80720 - Inizio
	protected BigDecimal iMaggiorZonaValuta;
	protected BigDecimal iRisZonaValuta;
	protected BigDecimal iMaggiorCliente;
	protected BigDecimal iTotMagCliente;
	protected BigDecimal iMaggiorParametrica;
	// Fix 80720 - Fine

	// Fix 80725 - Inizio
	protected BigDecimal iPrezzoVal;
	protected BigDecimal iPrezzoVendita;
	// Fix 80725 - Fine

	// fix 80691 - inizio (aggiunta 3 subagenti con relative provvigioni 1 e 2)
	protected BigDecimal iProvvigione1Subagente2 = new BigDecimal("0");
	protected BigDecimal iProvvigione2Subagente2 = new BigDecimal("0");
	protected BigDecimal iProvvigione1Subagente3 = new BigDecimal("0");
	protected BigDecimal iProvvigione2Subagente3 = new BigDecimal("0");
	protected BigDecimal iProvvigione1Subagente4 = new BigDecimal("0");
	protected BigDecimal iProvvigione2Subagente4 = new BigDecimal("0");
	protected Proxy iSubagente2 = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iSubagente3 = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iSubagente4 = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected boolean iServeRicalcoloProvvSubag2 = false;
	protected boolean iServeRicalcoloProvvSubag3 = false;
	protected boolean iServeRicalcoloProvvSubag4 = false;
	private boolean iDifferenzaPrezzoSubag2 = false;
	private boolean iDifferenzaPrezzoSubag3 = false;
	private boolean iDifferenzaPrezzoSubag4 = false;
	// fix 80691 - fine

	// fix 80722 - inizio (campi di servizio per totale pezze e max stato pezze)
	protected BigDecimal iTotalePezze = new BigDecimal("0");
	private char iMaxStatoPezze = '0';
	// fix 80722 - fine

	// fix 80723 - inizio (campi di servizio per forzatura e controllo esclusiva
	// cliente)
	protected String iForzatura = null; // Forzatura esclusiva disegni. Valori:
	// null-nessun controllo, 0-non forzare,
	// 1-forzare
	protected String iOkEsclusiva = null; // valori: null-nessun controllo,
	// 0-controllo ok, 1-errore
	// controllo esclusiva
	protected String iForzaturaV = null; // forzatura esclusiva su varianti.
	// Valori: null-nessun controllo,
	// 0-non forzare, 1-forzare
	protected String iOkEsclusivaV = null; // valori: null-nessun controllo,
	// 0-controllo ok, 1-errore
	// controllo esclusiva (per
	// varianti)
	protected String iIdClienteEsclusivo = null;
	protected String iDesClienteEsclusivo = null;
	protected String iIdClienteEsclusivoV = null;
	protected String iDesClienteEsclusivoV = null;
	protected String iTassativo = null;
	protected String iTassativoV = null;
	// fix 80723 - fine
	
	protected Proxy iRCausaleSaldoManuale = new Proxy(VtCausaleSaldoManualeRiga.class);
	// Cristi - Campo per raggruppamento righe per taglia e colore
	protected Long iGruppoTC;

	// Fix 81091 - Inizio
	protected char iStatoCodTess = 'D';
	protected String iUmPrmFinito;
	protected String iUmSecFinito;
	protected BigDecimal iQtaPrmFinito;
	protected BigDecimal iQtaSecFinito;
	protected char iRlsOrdPrdTess = RILASCIO_ORD_PRD_NON_ESTRATTO;
	// Fix 81091 - Fine

	//
	protected BigDecimal iAltezzaTirella;
	protected BigDecimal iLarghezzaTirella;

	public BigDecimal getAltezzaTirella() {
		return iAltezzaTirella;
	}

	public void setAltezzaTirella(BigDecimal iAltezzaTirella) {
		this.iAltezzaTirella = iAltezzaTirella;
	}

	public BigDecimal getLarghezzaTirella() {
		return iLarghezzaTirella;
	}

	public void setLarghezzaTirella(BigDecimal iLarghezzaTirella) {
		this.iLarghezzaTirella = iLarghezzaTirella;
	}

	//
	// Figlia
	protected OneToMany iVtOrdVenRigAtv = new OneToMany(it.thera.thip.tessile.vendite.ordineVE.VtOrdVenRigAtv.class,
			this, 15, true);
	// Fix 80976 - Inizio
	protected Proxy iRNDoganale = new Proxy(it.thera.thip.base.articolo.ClasseMateriale.class);

	public String getNDoganale() {
		String key = iRNDoganale.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setNDoganale(String nDoganale) {
		String key = iRNDoganale.getKey();
		setRNDoganaleKey(KeyHelper.replaceTokenObjectKey(key, 2, nDoganale));
	}

	// iVtSettore
	public void setRNDoganale(ClasseMateriale rNDoganale) {
		this.iRNDoganale.setObject(rNDoganale);
		setDirty();
	}

	public ClasseMateriale getRNDoganale() {
		return (ClasseMateriale) iRNDoganale.getObject();
	}

	public void setRNDoganaleKey(String rNDoganaleKey) {
		iRNDoganale.setKey(rNDoganaleKey);
		setDirty();
	}

	public String getRNDoganaleKey() {
		return iRNDoganale.getKey();
	}

	// Fix 80976 - Fine

	// Fix 80982 - Inizio

	protected BigDecimal iQtaTessuto;

	public BigDecimal getQtaTessuto() {
		return iQtaTessuto;
	}

	public void setQtaTessuto(BigDecimal qtaTessuto) {
		this.iQtaTessuto = qtaTessuto;
	}
	// Fix 80982 - Fine
	//FIX 82138 FIRAS : 22/11/2021
		public static final char EVADIBILE_INTERO = '1';
		public static final char EVADIBILE_CON_MANUTENZIONE = '2';
		public static final char EVADIBILE_PARZIALE = '3';
		public static final char NON_EVADIBILE = '4';
		public static final char NON_EVADIBILE_FUTURO = '5';
		
		protected char iTipoEvasione;
		
		public char getTipoEvasione() {
			return iTipoEvasione;
		}

		public void setTipoEvasione(char iTipoEvasione) {
			this.iTipoEvasione = iTipoEvasione;
												 
		}
	//FIX 82138 FINE
	// BRY causale saldo manuale
	

	   public String getRCausaleSaldoManualeKey()
	   {
	      return iRCausaleSaldoManuale.getKey();
	   }

	   public void setRCausaleSaldoManualeKey(String key)
	   {
		   iRCausaleSaldoManuale.setKey(key);
	   }
	
	   
	   public String getIdCausaleSaldoManuale()
	   {
	     String key = iRCausaleSaldoManuale.getKey();
	     return key;
	   }

	    public void setIdCausaleSaldoManuale(String idCausale)
	   {
	    	iRCausaleSaldoManuale.setKey(idCausale);
	     setDirty();
	   }
	    
	   

	/**
	 * Costruttore
	 */
	public VtOrdineVenditaRigaPrm() {
		super();

	}

	public int save() throws SQLException {

		// Fix 80910 - Inizio
		Integer nuovoIdArtCli = new Integer(0);
		Integer numArtCli = getRArtCli();

		// se numero articolo cliente della riga è vuoto
		// Fix 80951 if(numArtCli == null || numArtCli.equals(new Integer(0))) {

		// verifico se esiste l'articolo cliente (per cliente/articolo/variante)
		boolean esiste = false;
		/*
		 * try { esiste =
		 * VtFillOrdVenRigVar.cercaSeEsisteArticoloCliente(getIdAzienda(),
		 * getArticoloCli(), getVarianteArtcli(), getIdArticolo(), getRCliente(),
		 * getRStagione()); } catch (SQLException e) { e.printStackTrace(); }
		 */

		// Fix 80951 - Inizio
		esiste = esisteArticoloCliente();
		// Fix 80951 - Fine

		// se non esiste l'articolo cliente lo creo
		// if(!esiste) { //Fix 81125 - non eseguo anche se tipologiaOrdine = C. Se
		// tipologia =C eseguo artCli solo dopo le operazioniPreOrdine
		if (!esiste && this.getTipologiaOrdine() != 'C' && this.getTipologiaOrdine() != 'P') {
			// Fix 80910 - Inizio
			if (getArticoloCli() != null) {
				try {
					nuovoIdArtCli = VtOrdineVenditaRigaPrmRidottaSave.creaArticoloCliente(getIdAzienda(),
							getArticoloCli(), getVarianteArtcli(), getIdArticolo(), getRCliente(), getRStagione(),
							getIdUMRif());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Fix 80957 - Inizio
			else {
				setRArtCli(null);
				disabilitaAggiornamentoParteIntestatario();
			}

			// se creato l'articolo cliente, aggiorno il riferimento sulla riga
			if (nuovoIdArtCli != null && !nuovoIdArtCli.equals(new Integer(0))) {
				setRArtCli(nuovoIdArtCli);
			}

		}
		// Fix 80957
		// else
		// setRArtCli(null);

		// }

		// Fix 80910 - Fine

		// Fix 80529 - Inizio
		reloadRiserveLotto();
		// Fix 80529 - Fine

		// ATTENZIONE!!! questa implementazione (aggiornare IVA su righe se
		// presente in nazioni dest.finale)
		// non viene eseguita pi� qui ma quando imposto l'articolo
		// (onSearchBack) tramite
		// le funzioni di RecuperaDatiArticoliRigaVendita che eseguono le stesse
		// cose
		// qui implementate impostando, se previsto, l'iva direttamente sul
		// pannello
		// Se lasciassi l'implementazione qui, l'iva viene sempre e comunque
		// forzata, anche
		// nel caso si desideri modificarla sul pannello.
		// beforeSave();

		// se indicato il totale prezzo (prezzo + maggiorazioni) e se diverso
		// dal prezzo (quindi se ci sono magg.) allora aggiorno prezzo
		/*
		 * BigDecimal total = getTotale(); if(total != null) {
		 * //if(!getTotale().equals(getTotPrz())) { BigDecimal totaMaggio = getTotMag();
		 * if(!totaMaggio.equals(new BigDecimal(0))) { setPrezzo(getTotale()); } }
		 */

		// this.setMaggiorParametrica(getTotale()); //80743

		// fix 80484 - Inizio
		// se non ho gi� valorizzato ID_PRODOTTO e ID_PRD_FINO_VARIANTE,
		// valorizzo il primo da ARTICOLO, il secondo lo lascio com'� perch�
		// serve solamente nelle gestioni di righe per variante, quindi nelle
		// altre casistiche � vuoto

		if (getIdProdotto() == null || getIdProdotto().equals("")) {
			setIdProdotto(getArticolo().getIdProdotto());
		}
		// fix 80484 - Fine
		if (getDettaglioRigaDocumento() == null) {
			setDettaglioRigaDocumento(new Integer(0));
		}

		boolean ob = isOnDB(); // fix 80627 - memorizzo prima della save
		// l'indicatore se � un record nuovo

		// Fix 80767 - Inizio
		if (!ob)
			calcolaMaggiorazioni();

		if (getSettaggio() == null)
			setSettaggio(new Integer(0));
		// Fix 80767 - Fine

		// inizio fix 80799 prova forzatura
		/*
		 * if (this.getTipologiaOrdine()=='P') {
		 * 
		 * BigDecimal a = this.getQtaInUMPrmMag(); BigDecimal b =
		 * this.getQtaVenFinito(); BigDecimal c = this.getQtaInUMRif(); BigDecimal e =
		 * this.getQuantitaOrdinata().getQuantitaInUMPrm(); BigDecimal d =
		 * this.getQuantitaOrdinata().getQuantitaInUMRif();
		 * 
		 * this.setQtaInUMPrmMag(this.getQtaVenFinito());
		 * 
		 * }
		 */
		// fix 80808 elimino forzature fix 80799

		BigDecimal c = this.getQtaInUMRif();
		BigDecimal a = this.getQtaInUMPrmMag();

		Articolo art = this.getArticolo();
		VtArticoliTex articTex = null;

		VtArticolo vtArt = (VtArticolo) this.getArticolo();

		if (art != null)
			articTex = (VtArticoliTex) PersistentObject.elementWithKey(VtArticoliTex.class, art.getKey(),
					VtArticoliTex.NO_LOCK);

		java.util.Date curDate = new java.util.Date();
		java.sql.Date dataBo = new java.sql.Date(curDate.getTime());

		QuantitaInUMRif qrif = null;

		if (art != null)
			if (art.getUMSecMag() != null) {
				qrif = art.calcolaQuantitaArrotondate(this.getQtaInUMPrmMag(), art.getUMRiferimento(),
						art.getUMPrmMag(), art.getUMSecMag(), art.getVersioneAtDate(dataBo), Articolo.UM_RIF);
				this.setQtaInUMSecMag(qrif.getQuantitaInUMSec());
			}

		// Fix 81676 > set descrizione articolo
		if (art != null && art.getDescrizioneArticoloNLS() != null) {
			this.setDescrizioneArticolo(art.getDescrizioneArticoloNLS().getDescrizione());
		}
		// Fix 81676 <

		// Fix 80866
		// Fatto questa porcata detta da roberto

		// settaAltezzaMultipla(art,articTex);
		settaAltezzaMultiplaV2(art, articTex);

		// Fix 80976 - Inizio
		if (this.getNDoganale() != null && !this.getNDoganale().equals(""))
			saveClasseMaterialeSuArticolo(vtArt, this.getNDoganale());
		// Fix 80976 - Fine

		if (this.getIdCartella() == null)
			this.setIdCartella("");

		// Fix 80910 - Inizio
		// Controllo se esiste un articolo cliente : se si non faccio niente se no lo
		// creo

		/*
		 * if (this.getArticoloCli()==null) { ArticoloCliente artCli = (ArticoloCliente)
		 * Factory.createObject(ArticoloCliente.class);
		 * artCli.setIdArticolo(idArticolo);
		 * 
		 * artCli.setIdUMPrmVendita(this.getIdUMPrm()); }
		 */
		// Fix 80910 - Fine

		// FIX 80877 - Max - Inizio Inserimento articolo ordinato

		if (this.getRArtOrdinato() == null)
			this.setRArtOrdinato(this.getIdArticolo());

		// FIX 80877 - Max - Fine Inserimento articolo ordinato

		// Fix 80893 -Inizio
		Integer zero = new Integer(0);
		if (this.getSettaggio().equals(zero)) {
			int settaggio = 0;
			// vado a settargli il set con il max
			String select = "SELECT MAX(SETTAGGIO) FROM THIP.VT_ORD_VEN_RIG " + "WHERE ID_AZIENDA='"
					+ this.getIdAzienda() + "' AND ID_ANNO_ORD='" + this.getAnnoDocumento() + "' "
					+ "AND ID_NUMERO_ORD='" + this.getNumeroDocumento() + "'";

			CachedStatement cInstanceOrdVenRig = new CachedStatement(select);
			try {

				ResultSet rs = cInstanceOrdVenRig.executeQuery();

				if (rs.next()) {
					settaggio = rs.getInt(1);
				}
				rs.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			this.setSettaggio(new Integer(settaggio + 1));
		}
		// Fix 80893 - Fine

		// Fine porcata detta da roberto

		// MAAM 19/11/2016
		/*
		 * if (this.getQtaVenFinito()!=null){ if (this.getTipologiaOrdine()!='P') //if
		 * (this.getQtaVenFinito().equals(new BigDecimal("0.00")))
		 * this.setQtaVenFinito(this.getQtaInUMRif()); } else
		 * this.setQtaVenFinito(this.getQtaInUMRif());
		 */
		// fine fix 80799
		// fix 80808 ritorna come prima

		// Fix 81091 - Inizio

		// Fix 81283 - inizio ho spostato questo codice prima perch� se ero in
		// inserimento e mettevo subito a definitivo non mi creava il PF
		// Se sono in inserimento di una nuova riga e la tipologia == 'P' (Prodotto
		// finito) allora modifico la tipologia in C
		if (!this.isOnDB() && this.getTipologiaOrdine() == 'P')
			this.setTipologiaOrdine('C');
		// Fix 81283 - Fine

		// Se ?tato salvato per la prima volta in con flag StatoAvanzamento = '2' allora
		// eseguo operazioni della conferma pre-ordine
		boolean wasDefinitivo = false;
		if (this.isOnDB()) {
			VtOrdineVenditaRigaPrm ord = (VtOrdineVenditaRigaPrm) PersistentObject
					.elementWithKey(VtOrdineVenditaRigaPrm.class, this.getKey(), VtOrdineVenditaRigaPrm.NO_LOCK);
			if (ord != null) {
				if (ord.getStatoAvanzamento() == '2')
					wasDefinitivo = true;
			}
		}
		if (!wasDefinitivo && this.getStatoAvanzamento() == '2' && this.getTipologiaOrdine() == 'C'
				&& this.getArticoloFin() != null
		/* && this.getOrdineAcquistoRigaPrmIC() == null */) { // Fix 81258
			// Eseguo le stesse operazioni per tutte le righe dello stesso set di questo
			// ordine
			// griglia varianti legate alla riga per anno/numero ordine/prodotto fino a
			// variante, e settaggio
			if (this.getSettaggio() != null) {
				List idRighe = new ArrayList();

				String selectRigheOrd = "SELECT " + VtOrdineVenditaRigaPrmTM.ID_RIGA_ORD + " FROM "
						+ VtOrdineVenditaRigaPrmTM.TABLE_NAME_EXT + " " + "WHERE " + VtOrdineVenditaRigaPrmTM.ID_AZIENDA
						+ "='" + this.getIdAzienda() + "' AND " + VtOrdineVenditaRigaPrmTM.ID_ANNO_ORD + "='"
						+ this.getAnnoDocumento().trim() + "' AND " + VtOrdineVenditaRigaPrmTM.ID_NUMERO_ORD + "='"
						+ this.getNumeroDocumento().trim() + "' AND " + VtOrdineVenditaRigaPrmTM.SETTAGGIO + "= "
						+ this.getSettaggio() + " AND " + VtOrdineVenditaRigaPrmTM.ID_RIGA_ORD + "<> "
						+ this.getNumeroRigaDocumento() + " ";

				CachedStatement sqlRigheOrd = new CachedStatement(selectRigheOrd);
				ResultSet rsrig = sqlRigheOrd.executeQuery();
				while (rsrig.next()) {
					idRighe.add(rsrig.getString(1));
				}
				rsrig.close();

				if (idRighe != null && !idRighe.isEmpty()) {
					int rc1 = eseguiOperazoniPreOrdine();

					Iterator iter = idRighe.iterator();
					while (iter.hasNext()) {
						String idRigaOrd = (String) iter.next();
						String keyRiga = KeyHelper.replaceTokenObjectKey(this.getKey(), 4, idRigaOrd);
						VtOrdineVenditaRigaPrm riga = (VtOrdineVenditaRigaPrm) PersistentObject
								.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRiga, VtOrdineVenditaRigaPrm.NO_LOCK);
						if (riga != null && riga.getStatoAvanzamento() != '2' && riga.getTipologiaOrdine() != 'C'
								&& riga.getArticoloFin() != null && riga.getOrdineAcquistoRigaPrmIC() == null) {
							riga.setStatoAvanzamento('2');
							// riga.setSettaggio(null); //Fix 81197 - commento riga
							// int rc = 0;
							// ConnectionManager.pushConnection();
							riga.eseguiOperazoniPreOrdine();

							// if(rc >= 0)
							// ConnectionManager.commit();
							// else
							// ConnectionManager.rollback();

							// ConnectionManager.popConnection();
						}
					}

					return rc1;
				}
			}
			return eseguiOperazoniPreOrdine();
		}

		// Se sono in inserimento di una nuova riga e la tipologia == 'P' (Prodotto
		// finito) allora modifico la tipologia in C
		/*
		 * Fix 81283 if(!this.isOnDB() && this.getTipologiaOrdine() == 'P')
		 * this.setTipologiaOrdine('C'); Fix 81283
		 */
		// Fix 81091 - Fine

		// 10-05-2018 - inizio
		// Set prezzo sommato con prezzo attivit�
		if (getPrezzo() == null || getPrezzo().compareTo(new BigDecimal(0)) <= 0) { // Fix 80759

			// Set prezzo con la somma dei prezzi delle attivit�
			List listaAtv = getVtOrdVenRigAtv();

			if (listaAtv != null) {
				BigDecimal prezzoTot = getPrezzoTotAttivita(listaAtv);

				if (prezzoTot != null)
					if (prezzoTot.compareTo(new BigDecimal(0)) <= 0)
						// Fix 81341 prezzoTot = null;
						prezzoTot = new BigDecimal(0);

				setPrezzo(prezzoTot);
				this.setPrzManuale(true);

			}
		}

		// 81373 INIZIO
		// 81366 inizio
		/*
		 * if (this.getArticolo().getClasseMateriale() == null) { String classeMateriale
		 * = VtIntraRicerca.ricercaIntra(this.getIdAzienda(), this.getIdArticolo()); if
		 * (classeMateriale != null) { String keyArt = KeyHelper.buildObjectKey(new
		 * String[] { this.getIdAzienda(), classeMateriale }); ClasseMateriale classeMat
		 * = (ClasseMateriale) PersistentObject.elementWithKey(ClasseMateriale.class,
		 * keyArt, PersistentObject.NO_LOCK);
		 * this.getArticolo().setClasseMateriale(classeMat); this.getArticolo().save();
		 * } }
		 */
		// 81366 fine
		String tesKey = this.getTestataKey();
		VtOrdineVendita ordVen = null;
		ordVen = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
				PersistentObject.NO_LOCK);
		String cauKey = ordVen.getCausaleKey();
		VtCausaleOrdineVendita cau = null;
		cau = (VtCausaleOrdineVendita) PersistentObject.elementWithKey(VtCausaleOrdineVendita.class, cauKey,
				PersistentObject.NO_LOCK);
		if (!cau.getContoTrasformazione()) {
			Object[] keyParts = { Azienda.getAziendaCorrente(), "" + SchemaProdottoLotto.PRODOTTO,
					this.getArticolo().getIdProdotto().substring(0, 2) };
			SchemaProdottoLotto schPrd = null;
			try {
				schPrd = SchemaProdottoLotto.elementWithKey(KeyHelper.buildObjectKey(keyParts),
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
			if (schPrd != null) {
				if (schPrd.getRicercaINTRA()) {
					if (this.getArticolo().getClasseMateriale() == null) {
						String classeMateriale = VtIntraRicerca.ricercaIntra(this.getIdAzienda(), this.getIdArticolo());
						if (classeMateriale != null) {
							String keyArt = KeyHelper
									.buildObjectKey(new String[] { this.getIdAzienda(), classeMateriale });
							ClasseMateriale classeMat = (ClasseMateriale) PersistentObject
									.elementWithKey(ClasseMateriale.class, keyArt, PersistentObject.NO_LOCK);
							this.getArticolo().setClasseMateriale(classeMat);

							// 03-09-2018
							// List lstUMSec =
							// this.getArticolo().getArticoloDatiVendita().getUMSecondarie();

							this.getArticolo().save();
						}
					}
				}
			}

		}
		// 81373 FINE

		// Fix 81753 >

		if (this.iEliminata == 0)
			aggiornaQtaRigaADisporre();

		// Fix 81753 <

		int ret = super.save();

		// fix 80627 - inizio
		// dopo il salvataggio scrivo le maggiorazioni (solo se nuova riga)
		if (ret > 0) {
			if (!ob) {
				scrivoMaggiorazioni();
				// Fix 81325 - Inizio solo in fase di inserimento eredito tutte le attivita
				// dalla lavorazione dell'articolo
				// scrivo attivita dalla tabella lavorazioni
				// fare questo solo se la causale e di tipo conto lavoro interno e tipo riga = 1
				String causaleKey = this.getTestata().getCausaleKey();
				CausaleOrdineVendita cauOrdVen = (CausaleOrdineVendita) PersistentObject
						.elementWithKey(CausaleOrdineVendita.class, causaleKey, PersistentObject.NO_LOCK);

				if (this.getCausaleRiga().getTipoRiga() == '1' && cauOrdVen.getContoTrasformazione()) {

					// Scrivere metodo che mi controlla se esiste a parita di chiavi
					// CLIENTE,ARTIC,LAV
					String chiaveOrdVen = checkOrdiniPrecedenti(vtArt);

					VtOrdineVenditaRigaPrm rigaPrm = (VtOrdineVenditaRigaPrm) PersistentObject
							.elementWithKey(VtOrdineVenditaRigaPrm.class, chiaveOrdVen, PersistentObject.NO_LOCK);

					// 11-07-2018
					boolean ordineCorrente = false;

					if (rigaPrm != null && rigaPrm.getKey().equals(this.getKey())) {
						ordineCorrente = true;
					}
					// 11-07-2018

					// 11-07-2018 if(rigaPrm==null)
					if (ordineCorrente) {
						// String posOld = VtTessileUtil.getPosizioneChiaveByArticolo(vtArt,
						// "VtLavorazioni");
						String pos = VtTessileUtil.getCampoChiaveByArticolo(vtArt, "VtLavorazioni");

						if (pos != null) {
							String chiave = VtTessileUtil.getValueKeyArtByPos(vtArt, pos);

							Object[] keyParts = { art.getIdAzienda(), art.getIdSchemaProdotto(), chiave };
							String keyLav = KeyHelper.buildObjectKey(keyParts);

							VtLavorazioni lav = (VtLavorazioni) PersistentObject.elementWithKey(VtLavorazioni.class,
									keyLav, PersistentObject.NO_LOCK);

							List lstAtvRigaOrd = new ArrayList();

							if (lav != null) {
								List atvLav = lav.getVtLavorazioniAtv();

								Iterator iterAtvLav = atvLav.iterator();
								while (iterAtvLav.hasNext()) {
									VtLavorazioniAtv atv = (VtLavorazioniAtv) iterAtvLav.next();
									// this.getVtOrdVenRigAtv()
									VtOrdVenRigAtv atvRigaOrd = creaVtOrdVenRigAtvByAtv(atv);

									// VtTessileUtil.genericSave(atvRigaOrd);

									lstAtvRigaOrd.add(atvRigaOrd);
								}
								if (!lstAtvRigaOrd.isEmpty()) {
									this.getVtOrdVenRigAtv().addAll(lstAtvRigaOrd);
									this.save();

									aggiornaPrezzoOrdVenRigAtv();

								}

							}
						}
					} else
					// fix 81361
					{
						// Fix 81530 - Inizio
						if (rigaPrm != null) {
							List lstLavorazioni = rigaPrm.getVtOrdVenRigAtv();
							Iterator iterLavorazioni = lstLavorazioni.iterator();

							List lstAtvRigaOrd = new ArrayList();

							while (iterLavorazioni.hasNext()) {
								VtOrdVenRigAtv atv = (VtOrdVenRigAtv) iterLavorazioni.next();

								VtOrdVenRigAtv atvRigaOrd = creaVtOrdVenRigAtvByAtv(atv);

								lstAtvRigaOrd.add(atvRigaOrd);

							}

							if (!lstAtvRigaOrd.isEmpty()) {
								this.getVtOrdVenRigAtv().addAll(lstAtvRigaOrd);
								this.save();
							}

						} // Fix 81530

					}
				}

				// Fix 81325 - Fine

			}
			// Fix 80678 - Inizio
			if (!getRigheLotto().isEmpty()) {
				VtTessileUtil.aggiornaRifOrdVenSuPezza(this, getRigheLotto());
			}
			// Fix 80678 - Fine
		}
		// fix 80627 - fine

		// Fix 80820 - Inizio prova
		/*
		 * if(azione.equals(SAVE_AND_CLOSE)) {
		 * //se.getRequest().setAttribute("thAction", "CLOSE"); //URL =
		 * "/it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigClose.jsp";
		 * //se.sendRequest(getServletContext(), URL, false); String urlr =
		 * "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigRidRefreshGridClose.jsp";
		 * se.sendRequest(getServletContext(), urlr, false); }
		 */
		// Fix 80820 - Fine prova

		/*
		 * fix 81341 non va messo qui perch� non settera mai il prezzo //Set prezzo
		 * sommato con prezzo attivit� if(getPrezzo() == null ||
		 * getPrezzo().compareTo(new BigDecimal(0)) <= 0){ //Fix 80759
		 * 
		 * //Set prezzo con la somma dei prezzi delle attivit� List listaAtv =
		 * getVtOrdVenRigAtv();
		 * 
		 * if(listaAtv != null){ BigDecimal prezzoTot = getPrezzoTotAttivita(listaAtv);
		 * 
		 * if(prezzoTot != null) if(prezzoTot.compareTo(new BigDecimal(0)) <= 0)
		 * prezzoTot = null;
		 * 
		 * setPrezzo(prezzoTot); this.setPrzManuale(true);
		 * 
		 * } }
		 */

		return ret;
	}

	/**
	 * BRY Ridefinizione metodo checkDelete() Se esiste almeno una riga documento
	 * collegata ad un documento di montaggio smontaggio o sdoppio, la riga ordine
	 * non pu� essere eliminata
	 */
	public ErrorMessage checkDelete() {
		ErrorMessage err = super.checkDelete();
		if (err == null)
			err = checkEsistenzaRigaDocMontSmontCollegata();
		return err;
	}

	public ErrorMessage checkEsistenzaRigaDocMontSmontCollegata() {
		ErrorMessage err = null;
		if (existRigaDocMontSmontCollegata())
			err = new ErrorMessage("VT_300113");
		return err;
	}

	public boolean existRigaDocMontSmontCollegata() {

		if (csRicDocMontSmontRig == null) {
			String SQL_RIC_DOC_COLL = "SELECT COUNT(*) FROM " + VtDocKitOrdVenRigaTM.TABLE_NAME + " WHERE "
					+ VtDocKitOrdVenRigaTM.ID_AZIENDA + "=? AND " + VtDocKitOrdVenRigaTM.ID_ANNO_ORD + "=? AND "
					+ VtDocKitOrdVenRigaTM.ID_NUMERO_ORD + "=? AND " + VtDocKitOrdVenRigaTM.ID_RIGA_ORD + "=? ";
			csRicDocMontSmontRig = new CachedStatement(SQL_RIC_DOC_COLL);
		}

		boolean exist = false;
		Database db = ConnectionManager.getCurrentDatabase();
		try {
			PreparedStatement ps = csRicDocMontSmontRig.getStatement();
			synchronized (ps) {
				db.setString(ps, 1, getIdAzienda());
				db.setString(ps, 2, getAnnoDocumento());
				db.setString(ps, 3, getNumeroDocumento());
				ps.setInt(4, getNumeroRigaDocumento().intValue());

			}
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				exist = (rs.getInt(1) > 0);
			rs.close();
		} catch (SQLException ex) {
			ex.printStackTrace(Trace.excStream);
			exist = false;
		}
		return exist;
	}

	// Fix 81753 >
	public void aggiornaQtaRigaADisporre() throws SQLException {
		// if(isOnDB())
		// {
		// sono sull'ordine corrente

		VtOrdineVendita ordVen = (VtOrdineVendita) this.getTestata();

		String serie = ParametroPsn.getValoreParametroPsn("tessile.ordini.a.disporre", "OD");

		String idSerie = ordVen.getNumeroDocumento().substring(0, 2);

		if (iDisporre != 1) {
			if (ordVen != null && idSerie.equals(serie)) {
				// aggiorno la sua quantit� disposta e residua
				// calcolo quantita disposta
				BigDecimal qtaDisposta = calcoloQtaDisposta(this.getAnnoDocumento(), this.getNumeroDocumento(),
						this.getNumeroRigaDocumento(), this.getDettaglioRigaDocumento());

				if (qtaDisposta == null)
					qtaDisposta = new BigDecimal(0);

				this.setQtaDisposta(qtaDisposta);
				this.setQtaResidua(this.getQtaInUMRif().subtract(this.getQtaDisposta()));

				// qtadisposta = qta spedita

				QuantitaInUMRif qtaInUMRif = this.getArticolo().calcolaQuantitaArrotondate(qtaDisposta, this.getUMRif(),
						this.getUMPrm(), this.getUMSec(), Articolo.UM_RIF);

				this.getQuantitaSpedita().setQuantitaInUMRif(qtaInUMRif.getQuantitaInUMRif());
				this.getQuantitaSpedita().setQuantitaInUMPrm(qtaInUMRif.getQuantitaInUMPrm());
				this.getQuantitaSpedita().setQuantitaInUMSec(qtaInUMRif.getQuantitaInUMSec());

				// 81880 >
				if (qtaDisposta.compareTo(this.getQtaInUMPrmMag()) >= 0) {
					// saldo riga
					this.setStatoEvasione(StatoEvasione.SALDATO);
				}

				else if (qtaDisposta.compareTo(new BigDecimal(0)) > 0) {
					this.setStatoEvasione(StatoEvasione.EVASO_PARZIALMENTE);
				} else if (qtaDisposta.compareTo(new BigDecimal(0)) == 0) {
					this.setStatoEvasione(StatoEvasione.INEVASO);
				}
				// 81880 <

			} else {
				// verifico se sto salvando una riga derivata
				Integer rifNumRigaOrdADisp = this.getNumRigaAdisp();
				Integer rifDetRigaOrdADisp = this.getDetRigaAdisp();
				String rifNumOrdADisp = ordVen.getNumeroOrdAdisp();
				String rifAnnoOrdADisp = ordVen.getAnnoOrdineAdisp();

				BigDecimal qtaDisposta = calcoloQtaDisposta(rifAnnoOrdADisp, rifNumOrdADisp, rifNumRigaOrdADisp,
						rifDetRigaOrdADisp);

				if (qtaDisposta == null)
					qtaDisposta = new BigDecimal(0);
				String keyRigaOrdVen = Azienda.getAziendaCorrente() + KeyHelper.KEY_SEPARATOR + rifAnnoOrdADisp
						+ KeyHelper.KEY_SEPARATOR + rifNumOrdADisp + KeyHelper.KEY_SEPARATOR + rifNumRigaOrdADisp;

				VtOrdineVenditaRigaPrm rigaPrm = (VtOrdineVenditaRigaPrm) PersistentObject
						.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRigaOrdVen, PersistentObject.NO_LOCK);
				if (rigaPrm != null) {
					// 81880 if (!this.isOnDB())
					if (!this.isOnDB()) {
						rigaPrm.iDisporre = 1;
						BigDecimal qta = this.getQtaInUMRif();
						rigaPrm.setQtaDisposta(qtaDisposta.add(this.getQtaInUMRif()));
						rigaPrm.setQtaResidua(rigaPrm.getQtaInUMRif().subtract(rigaPrm.getQtaDisposta()));

						// qtadisposta = qta spedita

						QuantitaInUMRif qtaInUMRif = this.getArticolo().calcolaQuantitaArrotondate(
								rigaPrm.getQtaDisposta(), this.getUMRif(), this.getUMPrm(), this.getUMSec(),
								Articolo.UM_RIF);

						rigaPrm.getQuantitaSpedita().setQuantitaInUMRif(qtaInUMRif.getQuantitaInUMRif());
						rigaPrm.getQuantitaSpedita().setQuantitaInUMPrm(qtaInUMRif.getQuantitaInUMPrm());
						rigaPrm.getQuantitaSpedita().setQuantitaInUMSec(qtaInUMRif.getQuantitaInUMSec());

						// 81880 >
						if (rigaPrm.getQtaDisposta().compareTo(rigaPrm.getQtaInUMPrmMag()) >= 0) {
							// saldo riga
							rigaPrm.setStatoEvasione(StatoEvasione.SALDATO);
						} else if (rigaPrm.getQtaDisposta().compareTo(new BigDecimal(0)) > 0) {
							rigaPrm.setStatoEvasione(StatoEvasione.EVASO_PARZIALMENTE);
						} else if (rigaPrm.getQtaDisposta().compareTo(new BigDecimal(0)) == 0) {
							rigaPrm.setStatoEvasione(StatoEvasione.INEVASO);
						}
						// 81880 <

						rigaPrm.save();
					} else {
						rigaPrm.iDisporre = 1;

						// calcolo il delta tra la qta di prima e la quantit� cambiata
						BigDecimal qta = getQtaInUMPrmMagPreSave();
						if (qta == null)
							qta = new BigDecimal(0);

						BigDecimal delta = this.getQtaInUMRif().subtract(qta);

						// rigaPrm.setQtaDisposta(qtaDisposta);
						rigaPrm.setQtaDisposta(qtaDisposta.add(delta));
						rigaPrm.setQtaResidua(rigaPrm.getQtaInUMRif().subtract(rigaPrm.getQtaDisposta()));

						// qtadisposta = qta spedita

						QuantitaInUMRif qtaInUMRif = this.getArticolo().calcolaQuantitaArrotondate(
								rigaPrm.getQtaDisposta(), this.getUMRif(), this.getUMPrm(), this.getUMSec(),
								Articolo.UM_RIF);

						rigaPrm.getQuantitaSpedita().setQuantitaInUMRif(qtaInUMRif.getQuantitaInUMRif());
						rigaPrm.getQuantitaSpedita().setQuantitaInUMPrm(qtaInUMRif.getQuantitaInUMPrm());
						rigaPrm.getQuantitaSpedita().setQuantitaInUMSec(qtaInUMRif.getQuantitaInUMSec());

						// 81880 >
						if (qtaDisposta.compareTo(rigaPrm.getQtaInUMPrmMag()) >= 0) {
							// saldo riga
							rigaPrm.setStatoEvasione(StatoEvasione.SALDATO);
						} else if (qtaDisposta.compareTo(new BigDecimal(0)) > 0) {
							rigaPrm.setStatoEvasione(StatoEvasione.EVASO_PARZIALMENTE);
						} else if (qtaDisposta.compareTo(new BigDecimal(0)) == 0) {
							rigaPrm.setStatoEvasione(StatoEvasione.INEVASO);
						}
						// 81880 <

						rigaPrm.save();
					}
				}
			}

		}
	}

	// Fix 81880 >
	public BigDecimal getQtaInUMPrmMagPreSave() throws SQLException {
		BigDecimal qta = new BigDecimal(0);

		String select = " SELECT QTA_ORD_UM_VEN " + " FROM THIP.ORD_VEN_RIG AS A " + " WHERE A.ID_AZIENDA='"
				+ Azienda.getAziendaCorrente() + "' AND A.ID_ANNO_ORD='" + this.getAnnoDocumento() + "' "
				+ " AND A.ID_NUMERO_ORD='" + this.getNumeroDocumento() + "' AND A.ID_RIGA_ORD='"
				+ this.getNumeroRigaDocumento() + "' " + " AND A.ID_DET_RIGA_ORD='" + this.getDettaglioRigaDocumento()
				+ "'";

		CachedStatement cInstanceOrdVenRig = new CachedStatement(select);
		try {

			ResultSet rs = cInstanceOrdVenRig.executeQuery();

			if (rs.next()) {
				qta = rs.getBigDecimal(1);
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return qta;

	}
	// Fix 81880 <

	public BigDecimal calcoloQtaDisposta(String rifAnnoOrdADisp, String rifNumOrdADisp, Integer rifNumRigaOrdADisp,
			Integer rifDetRigaOrdADisp) throws SQLException {
		BigDecimal qtaDisposta = new BigDecimal(0);

		/*
		 * String select = " SELECT SUM(QTA_ORD_UM_VEN) " +
		 * " FROM THIP.VT_ORD_VEN_TES AS A, THIP.ORD_VEN_RIG AS B, THIP.VT_ORD_VEN_RIG AS C "
		 * + " WHERE ANNO_ORDINE_ADISP='"+
		 * this.getAnnoDocumento()+"' AND NUMERO_ORD_ADISP='"+
		 * this.getNumeroDocumento()+"'" +
		 * " AND NUM_RIGA_ADISP='"+this.getNumeroRigaDocumento()
		 * +"' AND DET_RIGA_ADISP='"+this.getDettaglioRigaDocumento()+"'" +
		 * " AND A.ID_AZIENDA=B.ID_AZIENDA AND A.ID_ANNO_ORDINE=B.ID_ANNO_ORD AND A.ID_NUMERO_ORD=B.ID_NUMERO_ORD "
		 * +
		 * " AND B.ID_AZIENDA=C.ID_AZIENDA AND B.ID_ANNO_ORD=C.ID_ANNO_ORD AND B.ID_NUMERO_ORD=C.ID_NUMERO_ORD "
		 * +
		 * " AND B.ID_RIGA_ORD=C.ID_RIGA_ORD AND B.ID_DET_RIGA_ORD=C.ID_DET_RIGA_ORD ";
		 */

		// 14-06-2019
		if (rifNumRigaOrdADisp != null && rifDetRigaOrdADisp != null) {

			String select = " SELECT SUM(QTA_ORD_UM_VEN) "
					+ " FROM THIP.VT_ORD_VEN_TES AS A, THIP.ORD_VEN_RIG AS B, THIP.VT_ORD_VEN_RIG AS C "
					+ " WHERE A.ID_AZIENDA='" + Azienda.getAziendaCorrente() + "' AND ANNO_ORDINE_ADISP='"
					+ rifAnnoOrdADisp + "' AND NUMERO_ORD_ADISP='" + rifNumOrdADisp + "'" + " AND NUM_RIGA_ADISP='"
					+ rifNumRigaOrdADisp + "' AND DET_RIGA_ADISP='" + rifDetRigaOrdADisp + "'"
					+ " AND A.ID_AZIENDA=B.ID_AZIENDA AND A.ID_ANNO_ORDINE=B.ID_ANNO_ORD AND A.ID_NUMERO_ORD=B.ID_NUMERO_ORD "
					+ " AND B.ID_AZIENDA=C.ID_AZIENDA AND B.ID_ANNO_ORD=C.ID_ANNO_ORD AND B.ID_NUMERO_ORD=C.ID_NUMERO_ORD "
					+ " AND B.ID_RIGA_ORD=C.ID_RIGA_ORD AND B.ID_DET_RIGA_ORD=C.ID_DET_RIGA_ORD ";

			CachedStatement cInstanceOrdVenRig = new CachedStatement(select);
			try {

				ResultSet rs = cInstanceOrdVenRig.executeQuery();

				if (rs.next()) {
					qtaDisposta = rs.getBigDecimal(1);
				}
				rs.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return qtaDisposta;
	}

	// Fix 81753 <

	public void beforeSave() {
		OrdineVendita ove = (OrdineVendita) super.getTestata();
		char tipoInd = ove.getTipoDestinatario();
		// tipo destinatario 1=indirizzo 2-cliente 3-manuale
		if (tipoInd == '1') {
			// ricavo codice anagrafico per agganciare indirizzi
			ClienteVendita clVe = ove.getCliente();
			Integer codAnagrafico = clVe.getIdAnagrafico();
			BigDecimal indir = ove.getIdSequenzaInd();
			String keyIndir = KeyHelper
					.buildObjectKey(new String[] { String.valueOf(codAnagrafico), String.valueOf(indir) });
			VtIndirizzo ind = null;
			try {
				ind = (VtIndirizzo) PersistentObject.elementWithKey(VtIndirizzo.class, keyIndir,
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// se indicato indirizzo cerco nazione destinazione finale (se
			// indicata)
			if (ind != null) {
				String nazDest = ind.getNazDestin();
				if (nazDest != null || !nazDest.equals("")) {
					VtNazione naz = null;
					try {
						naz = (VtNazione) PersistentObject.elementWithKey(VtNazione.class, nazDest,
								PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					// se trovo nazione destinazione, ricavo gli assoggettamenti
					// iva da forzare su riga ordine
					if (naz != null) {
						String assMer = naz.getAssIvaMerce();
						String assSer = naz.getAssIvaServ();
						char tr = super.getTipoRiga();
						// forzatura assoggettamento IVA riga se presente
						// indirizzo/nazione (per merce o servizi)
						// TIPO RIGA:
						// 1-Merce, 2-Omaggio, 3-Servizio, 4-Spese/Mov.val.
						// 5-Servizio/Noleggio 6-Servizio/Assist./Manut.
						// 7-Servizio/Canone
						// iva merce per solo tipo riga 1, iva servizi per tutti
						// gli altri tipi riga
						if (tr == '1') {
							if (assMer != null && !assMer.equals("")) {
								super.setIdAssogIVA(assMer);
							}
						} else {
							if (assSer != null && !assSer.equals("")) {
								super.setIdAssogIVA(assSer);
							}
						}

					}
				}
			}
		}

	}

	public static BigDecimal getPrezzoTotAttivita(List listaAtv) {

		BigDecimal prezzoTot = new BigDecimal(0);

		if (!listaAtv.isEmpty()) {
			Iterator iteAtv = listaAtv.iterator();

			while (iteAtv.hasNext()) {
				VtOrdVenRigAtv atv = (VtOrdVenRigAtv) iteAtv.next();

				if (atv != null) {
					BigDecimal prezzo = atv.getPrezzo();

					if (prezzo != null)
						prezzoTot = prezzoTot.add(prezzo);

				}

			}

		}

		return prezzoTot;
	}

	/**
	 * fix 80627 - Nuova funzione per scrivere maggiorazioni (se non presenti) e
	 * solo in immissione riga (provvede il chiamante al test se NEW)
	 * 
	 * @throws SQLException
	 */
	public void scrivoMaggiorazioni() throws SQLException {
		String kiave = getKey();
		VtOrdineVenditaRigaPrm rrr = null;
		try {
			rrr = (VtOrdineVenditaRigaPrm) PersistentObject.elementWithKey(VtOrdineVenditaRigaPrm.class, kiave,
					PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// VtOrdineVenditaRigaPrmCompletaFormModifier.cancMaggiorazioni(rrr); //
		// cancellazione non necessaria
		// VtOrdineVenditaRigaPrmCompletaFormModifier.testMaggiorazioni(rrr);
		// scrivo maggiorazioni su riga ordine (secondo le regole stabilite)
		// ConnectionManager.pushConnection();
		int ret = VtOrdineVenditaRigaPrmCompletaFormModifier.testMaggiorazioni(rrr, false);
		// se diverso da 999 allora aggiorno con COMMIT/ROLBACK (999 indica
		// nessuna regola di maggiorazione trovata)
		if (ret != 999) {
			if (ret > 0) {
				ConnectionManager.commit();
				Trace.excStream.println("Salvataggio maggiorazioni per riga ordine (commit), cod.ritorno: " + ret);
			} else {
				ConnectionManager.rollback();
				Trace.excStream.println("Salvataggio maggiorazioni per riga ordine (rollback), cod.ritorno: " + ret);
			}
		}
		// ConnectionManager.popConnection();
	}

	// fix 80563 - inizio
	// iQtaDisposta
	public BigDecimal getQtaDisposta() {
		return iQtaDisposta;
	}

	public void setQtaDisposta(BigDecimal qtaDisposta) {
		this.iQtaDisposta = qtaDisposta;
	}

	// iQtaResidua
	public BigDecimal getQtaResidua() {
		if (getQtaDisposta() != null) {
			iQtaResidua = super.getQuantitaOrdinata().getQuantitaInUMRif().subtract(getQtaDisposta());
		} else {
			iQtaResidua = super.getQuantitaOrdinata().getQuantitaInUMRif();
		}
		return iQtaResidua;
	}

	public void setQtaResidua(BigDecimal qtaResidua) {
		this.iQtaResidua = qtaResidua;
	}

	// iQtaIniziale (prima della eventuale modifica)
	public BigDecimal getQtaIniziale() {

		return iQtaIniziale;
	}

	public void setQtaIniziale(BigDecimal val) {
		this.iQtaIniziale = val;
	}

	// iNumRigaAdisp
	public Integer getNumRigaAdisp() {
		return iNumRigaAdisp;
	}

	public void setNumRigaAdisp(Integer numRigaAdisp) {
		this.iNumRigaAdisp = numRigaAdisp;
	}

	// iDetRigaAdisp
	public Integer getDetRigaAdisp() {
		return iDetRigaAdisp;
	}

	public void setDetRigaAdisp(Integer detRigaAdisp) {
		this.iDetRigaAdisp = detRigaAdisp;
	}
	// fix 80563 - fine

	// iLungPezFin;
	public BigDecimal getLungPezFin() {
		if (iLungPezFin == null)
			return this.getLungPezzaArticolo();
		else
			return iLungPezFin;
	}

	public void setLungPezFin(BigDecimal lungPezFin) {
		this.iLungPezFin = lungPezFin;
	}

	// iRFornitore
	public String getRFornitore() {
		String key = iFornitoreAcquisto.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRFornitore(String rFornitore) {
		String key = iFornitoreAcquisto.getKey();
		setFornitoreAcquistoKey(KeyHelper.replaceTokenObjectKey(key, 2, rFornitore));
	}

	// iFornitoreAcquisto
	public void setFornitoreAcquisto(FornitoreAcquisto FornitoreAcquisto) {
		this.iFornitoreAcquisto.setObject(FornitoreAcquisto);
		setDirty();
	}

	public FornitoreAcquisto getFornitoreAcquisto() {
		return (FornitoreAcquisto) iFornitoreAcquisto.getObject();
	}

	public void setFornitoreAcquistoKey(String FornitoreAcquistoKey) {
		iFornitoreAcquisto.setKey(FornitoreAcquistoKey);
		setDirty();
	}

	public String getFornitoreAcquistoKey() {
		return iFornitoreAcquisto.getKey();
	}

	// iRModSpedizione
	public String getRModSpedizione() {
		String key = iModalitaSpedizione.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRModSpedizione(String rModSpedizione) {
		String key = iModalitaSpedizione.getKey();
		setModalitaSpedizioneKey(KeyHelper.replaceTokenObjectKey(key, 2, rModSpedizione));
	}

	// iModalitaSpedizione
	public void setModalitaSpedizione(ModalitaSpedizione ModalitaSpedizione) {
		this.iModalitaSpedizione.setObject(ModalitaSpedizione);
		setDirty();
	}

	public ModalitaSpedizione getModalitaSpedizione() {
		return (ModalitaSpedizione) iModalitaSpedizione.getObject();
	}

	public void setModalitaSpedizioneKey(String modalitaSpedizioneKey) {
		iModalitaSpedizione.setKey(modalitaSpedizioneKey);
		setDirty();
	}

	public String getModalitaSpedizioneKey() {
		return iModalitaSpedizione.getKey();
	}

	// iRModConsegna
	public String getRModConsegna() {
		String key = iModalitaConsegna.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRModConsegna(String rModConsegna) {
		String key = iModalitaConsegna.getKey();
		setModalitaConsegnaKey(KeyHelper.replaceTokenObjectKey(key, 2, rModConsegna));
	}

	// iModalitaConsegna
	public void setModalitaConsegna(ModalitaConsegna ModalitaConsegna) {
		this.iModalitaConsegna.setObject(ModalitaConsegna);
		setDirty();
	}

	public ModalitaConsegna getModalitaConsegna() {
		return (ModalitaConsegna) iModalitaConsegna.getObject();
	}

	public void setModalitaConsegnaKey(String modalitaConsegnaKey) {
		iModalitaConsegna.setKey(modalitaConsegnaKey);
		setDirty();
	}

	public String getModalitaConsegnaKey() {
		return iModalitaConsegna.getKey();
	}

	// iRDivisioneCliente
	public String getRDivisioneCliente() {
		String key = iVtClienteDivisione.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRDivisioneCliente(String rDivisioneCliente) {
		String key = iVtClienteDivisione.getKey();
		setVtClienteDivisioneKey(KeyHelper.replaceTokenObjectKey(key, 2, rDivisioneCliente));
	}

	// iVtClienteDivisione
	public void setVtClienteDivisione(VtClienteDivisione vtClienteDivisione) {
		this.iVtClienteDivisione.setObject(vtClienteDivisione);
		setDirty();
	}

	public VtClienteDivisione getVtClienteDivisione() {
		return (VtClienteDivisione) iVtClienteDivisione.getObject();
	}

	public void setVtClienteDivisioneKey(String vtClienteDivisioneKey) {
		iVtClienteDivisione.setKey(vtClienteDivisioneKey);
		setDirty();
	}

	public String getVtClienteDivisioneKey() {
		return iVtClienteDivisione.getKey();
	}

	// iRSettore
	public String getRSettore() {
		String key = iVtSettore.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRSettore(String rSettore) {
		String key = iVtSettore.getKey();
		setVtSettoreKey(KeyHelper.replaceTokenObjectKey(key, 2, rSettore));
	}

	// iVtSettore
	public void setVtSettore(VtSettore vtSettore) {
		this.iVtSettore.setObject(vtSettore);
		setDirty();
	}

	public VtSettore getVtSettore() {
		return (VtSettore) iVtSettore.getObject();
	}

	public void setVtSettoreKey(String vtSettoreKey) {
		iVtSettore.setKey(vtSettoreKey);
		setDirty();
	}

	public String getVtSettoreKey() {
		return iVtSettore.getKey();
	}

	// iSettaggio;
	// fix 80444 - cambiato tipo oggetto da String a Integer
	public Integer getSettaggio() {
		return iSettaggio;
	}

	public void setSettaggio(Integer settaggio) {
		this.iSettaggio = settaggio;
	}

	// Attributi aggiunti
	// iIdCliente
	public String getIdCliente() {
		String idCliente = null;
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			idCliente = testata.getCliente().getIdCliente();

		return idCliente;
	}

	public void setIdCliente(String idCliente) {
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			idCliente = testata.getCliente().getIdCliente();
		this.iIdCliente = idCliente;
	}

	// fix 80444 - inizio (definizione nuove proxy)

	// iRArtPrimario - Articolo ordinato
	public String getRArtPrimario() {
		String key = iVtArticoloPrm.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRArtPrimario(String rArtPrimario) {
		String key = iVtArticoloPrm.getKey();
		setVtArticoloPrmKey(KeyHelper.replaceTokenObjectKey(key, 2, rArtPrimario));
	}

	// iVtArticoloPrm - Proxy di Articolo Ordinato
	public void setVtArticoloPrm(Articolo vtArticoloPrm) {
		this.iVtArticoloPrm.setObject(vtArticoloPrm);
		setDirty();
	}

	public Articolo getVtArticoloPrm() {
		return (Articolo) iVtArticoloPrm.getObject();
	}

	public void setVtArticoloPrmKey(String vtArticoloPrmKey) {
		iVtArticoloPrm.setKey(vtArticoloPrmKey);
		setDirty();
	}

	public String getVtArticoloPrmKey() {
		return iVtArticoloPrm.getKey();
	}

	// iRArtOrdinato - Articolo ordinato
	public String getRArtOrdinato() {
		String key = iVtArticolo.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRArtOrdinato(String rArtOrdinato) {
		String key = iVtArticolo.getKey();
		setVtArticoloKey(KeyHelper.replaceTokenObjectKey(key, 2, rArtOrdinato));
	}

	// iVtArticolo - Proxy di Articolo Ordinato
	public void setVtArticolo(Articolo vtArticolo) {
		this.iVtArticolo.setObject(vtArticolo);
		setDirty();
	}

	public Articolo getVtArticolo() {
		return (Articolo) iVtArticolo.getObject();
	}

	public void setVtArticoloKey(String vtArticoloKey) {
		iVtArticolo.setKey(vtArticoloKey);
		setDirty();
	}

	public String getVtArticoloKey() {
		return iVtArticolo.getKey();
	}

	// //iRArtCli - Articolo Cliente
	// public Integer getRArtCli() {
	// String key = iArticoloCliente.getKey();
	// Integer var = new Integer(0);
	// if(KeyHelper.getTokenObjectKey(key, 2) != null &&
	// !KeyHelper.getTokenObjectKey(key, 2).equals("")) {
	// var = new Integer(KeyHelper.getTokenObjectKey(key, 2));
	// }
	// return var;
	// }
	//
	// public void setRArtCli(Integer rArtCli) {
	// String key = iArticoloCliente.getKey();
	// setArticoloClienteKey(KeyHelper.replaceTokenObjectKey(key, 2, rArtCli));
	// }
	//
	// // iVtArticolo - Proxy di Articolo Ordinato
	// public void setArticoloCliente(ArticoloCliente articoloCliente){
	// this.iArticoloCliente.setObject(articoloCliente);
	// setDirty();
	// }
	//
	// public ArticoloCliente getArticoloCliente(){
	// return (ArticoloCliente)iArticoloCliente.getObject();
	// }
	//
	// public void setArticoloClienteKey(String articoloClienteKey){
	// iArticoloCliente.setKey(articoloClienteKey);
	// setDirty();
	// }
	//
	// public String getArticoloClienteKey(){
	// return iArticoloCliente.getKey();
	// }

	// fix 80679 - inizio
	// iArticoloCli - Articolo Cliente
	// public String getArticoloCli() {
	// String key = iVtArticoloClienteRic.getKey();
	// String var = "";
	// if(KeyHelper.getTokenObjectKey(key, 3) != null &&
	// !KeyHelper.getTokenObjectKey(key, 3).equals("")) {
	// var = KeyHelper.getTokenObjectKey(key, 3);
	// }
	// return var;
	// }
	//
	// public void setArticoloCli(String var) {
	// String key = iArticoloCliente.getKey();
	// setVtArticoloClienteRicKey(KeyHelper.replaceTokenObjectKey(key, 2, var));
	// }

	// iVtArticolo - Proxy di Articolo Ordinato
	public void setVtArticoloClienteRic(VtArticoloClienteRic articoloCliente) {
		this.iVtArticoloClienteRic.setObject(articoloCliente);
		setDirty();
	}

	public VtArticoloClienteRic getVtArticoloClienteRic() {
		return (VtArticoloClienteRic) iVtArticoloClienteRic.getObject();
	}

	public void setVtArticoloClienteRicKey(String articoloClienteKey) {
		iVtArticoloClienteRic.setKey(articoloClienteKey);
		setDirty();
	}

	public String getVtArticoloClienteRicKey() {
		return iVtArticoloClienteRic.getKey();
	}

	// fix 80498 - inizio (definizione nuove proxy)

	// iRArtFinito
	public String getRArtFinito() {
		// String key = iVtArticoloFin.getKey();
		String key = iArticoloFin.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRArtFinito(String rArtFinito) {
		// String key = iVtArticoloFin.getKey();
		String key = iArticoloFin.getKey();
		// setVtArticoloFinKey(KeyHelper.replaceTokenObjectKey(key, 2,
		// rArtFinito));
		setArticoloFinKey(KeyHelper.replaceTokenObjectKey(key, 2, rArtFinito));
	}

	// iVtArticoloFin - Proxy di Articolo Finito
	// public void setVtArticoloFin(Articolo vtArticoloFin){
	// this.iVtArticoloFin.setObject(vtArticoloFin);
	public void setArticoloFin(Articolo vtArticoloFin) {
		this.iArticoloFin.setObject(vtArticoloFin);

		setDirty();
	}

	// public Articolo getVtArticoloFin(){
	// return (Articolo)iVtArticoloFin.getObject();
	// }
	public Articolo getArticoloFin() {
		return (Articolo) iArticoloFin.getObject();
	}

	// public void setVtArticoloFinKey(String vtArticoloFinKey){
	// iVtArticoloFin.setKey(vtArticoloFinKey);
	public void setArticoloFinKey(String vtArticoloFinKey) {
		iArticoloFin.setKey(vtArticoloFinKey);
		setDirty();
	}

	// public String getVtArticoloFinKey(){
	// return iVtArticoloFin.getKey();
	// }
	public String getArticoloFinKey() {
		return iArticoloFin.getKey();
	}

	// Proxy Etichetta
	public VtEtichetta getVtEtichetta() {
		return (VtEtichetta) iVtEtichetta.getObject();
	}

	public void setVtEtichetta(VtEtichetta iVtEtichetta) {
		this.iVtEtichetta.setObject(iVtEtichetta);
		setDirty();
	}

	public String getVtEtichettaKey() {
		return iVtEtichetta.getKey();
	}

	public void setVtEtichettaKey(String key) {
		iVtEtichetta.setKey(key);
		setDirty();
	}

	public String getREtichetta() {
		String key = iVtEtichetta.getKey();
		String rEtichetta = KeyHelper.getTokenObjectKey(key, 2);
		return rEtichetta;
	}

	public void setREtichetta(String rEtichetta) {
		String key = iVtEtichetta.getKey();
		iVtEtichetta.setKey(KeyHelper.replaceTokenObjectKey(key, 2, rEtichetta));
		setDirty();
	}

	// Proxy UM Finito
	public UnitaMisura getUMFin() {
		return (UnitaMisura) iUMFin.getObject();
	}

	public void setUMFin(UnitaMisura iUMFin) {
		this.iUMFin.setObject(iUMFin);
		setDirty();
	}

	public String getUMFinKey() {
		return iUMFin.getKey();
	}

	public void setUMFinKey(String key) {
		iUMFin.setKey(key);
		setDirty();
	}

	// secondo proxy um finito
	public UnitaMisura getRUMFin() {
		return (UnitaMisura) iRUMFin.getObject();
	}

	public void setRUMFin(UnitaMisura iUMFin) {
		this.iRUMFin.setObject(iUMFin);
		setDirty();
	}

	public String getRUMFinKey() {
		return iRUMFin.getKey();
	}

	public void setRUMFinKey(String key) {
		iRUMFin.setKey(key);
		setDirty();
	}

	public String getUmVenFinito() {
		String key = iRUMFin.getKey();
		String rUMFin = KeyHelper.getTokenObjectKey(key, 2);
		return rUMFin;
	}

	public void setUmVenFinito(String rUMFin) {
		String key = iRUMFin.getKey();
		iRUMFin.setKey(KeyHelper.replaceTokenObjectKey(key, 2, rUMFin));
		setDirty();
	}

	// fix 80498 - fine

	// Fix 81091 - Inizio
	public String getUmPrmFinito() {
		return this.iUmPrmFinito;
	}

	public void setUmPrmFinito(String UmPrmFinito) {
		this.iUmPrmFinito = UmPrmFinito;
		setDirty();
	}

	public String getUmSecFinito() {
		return this.iUmSecFinito;
	}

	public void setUmSecFinito(String UmSecFinito) {
		this.iUmSecFinito = UmSecFinito;
		setDirty();
	}

	public BigDecimal getQtaPrmFinito() {
		return this.iQtaPrmFinito;
	}

	public void setQtaPrmFinito(BigDecimal QtaPrmFinito) {
		this.iQtaPrmFinito = QtaPrmFinito;
		setDirty();
	}

	public BigDecimal getQtaSecFinito() {
		return this.iQtaSecFinito;
	}

	public void setQtaSecFinito(BigDecimal QtaSecFinito) {
		this.iQtaSecFinito = QtaSecFinito;
		setDirty();
	}

	public char getRlsOrdPrdTess() {
		return this.iRlsOrdPrdTess;
	}

	public void setRlsOrdPrdTess(char RlsOrdPrdTess) {
		this.iRlsOrdPrdTess = RlsOrdPrdTess;
		setDirty();
	}

	// Fix 81091 - Fine

	/**
	 * Buyer - Compratore
	 * 
	 */

	// Imposta buyer proxy
	public void setVtBuyer(VtBuyer vtBuyer) {
		String oldObjectKey = getKey();
		String idAzienda = null;
		if (vtBuyer != null) {
			idAzienda = KeyHelper.getTokenObjectKey(vtBuyer.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iVtBuyer.setObject(vtBuyer);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	// recupera um proxy
	public VtBuyer getVtBuyer() {
		return (VtBuyer) iVtBuyer.getObject();
	}

	// imposta chiave buyer
	public void setVtBuyerKey(String key) {
		String oldObjectKey = getKey();
		iVtBuyer.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	// recupera chiave buyer
	public String getVtBuyerKey() {
		return iVtBuyer.getKey();
	}

	// imposta buyer
	public void setRBuyer(String vtBuyer) {
		String key = iVtBuyer.getKey();
		iVtBuyer.setKey(KeyHelper.replaceTokenObjectKey(key, 2, vtBuyer));
		setDirty();
	}

	// Recupera buyer
	public String getRBuyer() {
		String key = iVtBuyer.getKey();
		String objRBuyer = KeyHelper.getTokenObjectKey(key, 2);
		return objRBuyer;
	}

	// iRStagione
	public String getRStagione() {
		String key = iVtStagione.getKey();
		// return KeyHelper.getTokenObjectKey(key, 2);
		return KeyHelper.getTokenObjectKey(key, 1); // Fix 80660
	}

	public void setRStagione(String rStagione) {
		String key = iVtStagione.getKey();
		// setVtStagioneKey(KeyHelper.replaceTokenObjectKey(key, 2, rStagione));
		setVtStagioneKey(KeyHelper.replaceTokenObjectKey(key, 1, rStagione)); // Fix
		// 80660
	}

	// iVtStagione
	public void setVtStagione(VtStagione vtStagione) {
		this.iVtStagione.setObject(vtStagione);
		setDirty();
	}

	public VtStagione getVtStagione() {
		return (VtStagione) iVtStagione.getObject();
	}

	public void setVtStagioneKey(String vtStagioneKey) {
		iVtStagione.setKey(vtStagioneKey);
		setDirty();
	}

	public String getVtStagioneKey() {
		return iVtStagione.getKey();
	}

	// fix 80444 - fine

	// fix 80484 - inizio
	public String getIdPrdFinoVar() {
		return iIdPrdFinoVar;
	}

	public void setIdPrdFinoVar(String idPrdFinoVar) {
		this.iIdPrdFinoVar = idPrdFinoVar;
	}

	public String getIdProdotto() {
		return iIdProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.iIdProdotto = idProdotto;
	}
	// fix 80484 - fine

	// fix 80669 - inizio
	// variante per parzializzare ricerca articolo cliente
	public String getVarianteArtcli() {
		return iVarianteArtcli;
	}

	public void setVarianteArtcli(String val) {
		this.iVarianteArtcli = val;
	}
	// fix 80669 - fine

	// fix 80679 - inizio
	// articolo del cliente
	public String getArticoloCli() {
		return iArticoloCli;
	}

	public void setArticoloCli(String val) {
		this.iArticoloCli = val;
	}

	public Integer getRArtCli() {
		return iRArtCli;
	}

	public void setRArtCli(Integer val) {
		this.iRArtCli = val;
	}
	// fix 80679 - fine

	// fix 80498 - inizio
	public BigDecimal getQtaVenFinito() {
		return iQtaVenFinito;
	}

	public void setQtaVenFinito(BigDecimal qtaVenFinito) {
		this.iQtaVenFinito = qtaVenFinito;
	}

	/*
	 * public String getUmVenFinito() { return iUmVenFinito; }
	 * 
	 * public void setUmVenFinito(String umVenFinito) { this.iUmVenFinito =
	 * umVenFinito; }
	 */

	public BigDecimal getCoeffTessuto() {
		return iCoeffTessuto;
	}

	public void setCoeffTessuto(BigDecimal coeffTessuto) {
		this.iCoeffTessuto = coeffTessuto;
	}

	// fix 80498 - fine

	public char getTipologiaOrdine() {
		return iTipologiaOrdine;
	}

	public void setTipologiaOrdine(char tipologiaOrdine) {
		this.iTipologiaOrdine = tipologiaOrdine;
	}

	// Riferimento a riga ordine tessuto origine (pre-ordine)
	public Integer getRigaPreord() {
		return iRigaPreord;
	}

	public void setRigaPreord(Integer rigaPreord) {
		this.iRigaPreord = rigaPreord;
	}

	// Riferimento a riga ordine tessuto origine (pre-ordine)
	public Integer getDetPreord() {
		return iDetPreord;
	}

	public void setDetPreord(Integer detPreord) {
		this.iDetPreord = detPreord;
	}

	// fix 80498 - fine

	// fix 80540 - inizio

	// Quantita da produrre
	public BigDecimal getQtaDaProd() {
		return iQtaDaProd;
	}

	public void setQtaDaProd(BigDecimal qtaDaProd) {
		this.iQtaDaProd = qtaDaProd;
	}

	// Data consegna richiesta
	public java.sql.Date getDataConsRich() {
		return iDataConsRich;
	}

	public void setDataConsRich(java.sql.Date dataConsRich) {
		this.iDataConsRich = dataConsRich;
	}

	// Data consegna richiesta tessuto
	public java.sql.Date getDataConsRichTes() {
		return iDataConsRichTes;
	}

	public void setDataConsRichTes(java.sql.Date dataConsRich) {
		this.iDataConsRichTes = dataConsRich;
	}

	// Data consegna confermata tessuto
	public java.sql.Date getDataConsConfTes() {
		return iDataConsConfTes;
	}

	public void setDataConsConfTes(java.sql.Date dataConsConf) {
		this.iDataConsConfTes = dataConsConf;
	}

	// Data consegna produzione tessuto
	public java.sql.Date getDataConsProdTes() {
		return iDataConsProdTes;
	}

	public void setDataConsProdTes(java.sql.Date dataProdRich) {
		this.iDataConsProdTes = dataProdRich;
	}

	// Settimana consegna richiesta tessuto
	public void setSettConsRichTes(String iSettConsRichTes) {
		this.iSettConsRichTes = iSettConsRichTes;
		setDirty();
	}

	public String getSettConsRichTes() {
		return iSettConsRichTes;
	}

	// Settimana consegna confermata tessuto
	public void setSettConsConfTes(String iSettConsConfTes) {
		this.iSettConsConfTes = iSettConsConfTes;
		setDirty();
	}

	public String getSettConsConfTes() {
		return iSettConsConfTes;
	}

	// Settimana consegna produzione tessuto
	public void setSettConsProdTes(String iSettConsProdTes) {
		this.iSettConsProdTes = iSettConsProdTes;
		setDirty();
	}

	public String getSettConsProdTes() {
		return iSettConsProdTes;
	}

	// Flag prezzo manuale
	public boolean getPrzManuale() {
		return iPrzManuale;
	}

	public void setPrzManuale(boolean przManuale) {
		this.iPrzManuale = przManuale;
	}

	// fix 80540 - fine

	// fix 80563 - inizio

	// riferimenti ordine a disporre se indicato (solo per ordini legati ad
	// ordine a disporre)
	public String getAnnoAdisp() {
		return iAnnoAdisp;
	}

	public void setAnnoAdisp(String val) {
		this.iAnnoAdisp = val;
	}

	public String getNumAdisp() {
		return iNumAdisp;
	}

	public void setNumAdisp(String val) {
		this.iNumAdisp = val;
	}

	// Riferimento a riga ordine a disporre
	public Integer getRigAdisp() {
		return iRigAdisp;
	}

	public void setRigAdisp(Integer val) {
		this.iRigAdisp = val;
	}

	// Riferimento a riga ordine a disporre
	public Integer getDetAdisp() {
		return iDetAdisp;
	}

	public void setDetAdisp(Integer val) {
		this.iDetAdisp = val;
	}

	// Cristi - TC
	public void setGruppoTC(Long gruppoTC) {
		this.iGruppoTC = gruppoTC;
		setDirty();
	}

	public Long getGruppoTC() {
		return iGruppoTC;
	}

	// 81091
	public char getStatoCodTess() {
		return iStatoCodTess;
	}

	public void setStatoCodTess(char statoCodTess) {
		this.iStatoCodTess = statoCodTess;
		setDirty();
	}
	// 81091 - Fine

	// Metodi di prelevamento dati

	// Prelevo lunghezza pezza da Articolo
	public BigDecimal getLungPezzaArticolo() {
		BigDecimal lungPezArt = null;

		VtArticolo art = null;
		String artKey = this.getArticoloKey();

		try {
			art = (VtArticolo) PersistentObject.elementWithKey(VtArticolo.class, artKey, PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (art != null)
			lungPezArt = art.getLungPezFin();

		return lungPezArt;
	}

	// fix 80498 - inizio
	/**
	 * Restituisce le unit� di misura di vendita associate all'articolo finito Serve
	 * per riempire a GUI la combo delle UM di vendita
	 */
	public List getListaUMFinito() {
		Articolo vtArticoloFin = getArticoloFin();
		if (vtArticoloFin != null) {
			List list = new ArrayList(vtArticoloFin.getArticoloDatiVendita().getForcedUMSecondarie());
			// List list2 = new
			// ArrayList(vtArticoloFin.getArticoloDatiVendita().getUMSecondarieAvailable());
			// if (!DocumentoBaseRiga.isUMNellaLista(this.getRUMFin(), list)) {
			// list.add(this.getUMFin());
			// }
			return list;
		} else {
			return new ArrayList();
		}
	}
	// fix 80498 - fine

	// Impostazione dei proxy
	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		VtOrdineVenditaRigaPrm vtOrdineVenditaRigaPrm = (VtOrdineVenditaRigaPrm) obj;
		iFornitoreAcquisto.setEqual(vtOrdineVenditaRigaPrm.iFornitoreAcquisto);
		iModalitaSpedizione.setEqual(vtOrdineVenditaRigaPrm.iModalitaSpedizione);
		iModalitaConsegna.setEqual(vtOrdineVenditaRigaPrm.iModalitaConsegna);
		iVtClienteDivisione.setEqual(vtOrdineVenditaRigaPrm.iVtClienteDivisione);
		iVtSettore.setEqual(vtOrdineVenditaRigaPrm.iVtSettore);
		// iVtMaggiorOrdVenRig.setEqual(vtOrdineVenditaRigaPrm.iVtMaggiorOrdVenRig);
		// fix 80444 - inizio
		iVtArticolo.setEqual(vtOrdineVenditaRigaPrm.iVtArticolo);
		iVtArticoloPrm.setEqual(vtOrdineVenditaRigaPrm.iVtArticoloPrm);
		iArticoloCliente.setEqual(vtOrdineVenditaRigaPrm.iArticoloCliente);
		iVtBuyer.setEqual(vtOrdineVenditaRigaPrm.iVtBuyer);
		iVtStagione.setEqual(vtOrdineVenditaRigaPrm.iVtStagione);
		// fix 80444 - fine

		iVtArticoloFin.setEqual(vtOrdineVenditaRigaPrm.iVtArticoloFin); // fix
		// 80498
		iArticoloFin.setEqual(vtOrdineVenditaRigaPrm.iArticoloFin); // fix 80498
		iUMFin.setEqual(vtOrdineVenditaRigaPrm.iUMFin); // fix 80498
		iRUMFin.setEqual(vtOrdineVenditaRigaPrm.iRUMFin); // fix 80498
		iVtEtichetta.setEqual(vtOrdineVenditaRigaPrm.iVtEtichetta); // fix 80498
		iSubagente2.setEqual(vtOrdineVenditaRigaPrm.iSubagente2); // fix 80691
		iSubagente3.setEqual(vtOrdineVenditaRigaPrm.iSubagente3); // fix 80691
		iSubagente4.setEqual(vtOrdineVenditaRigaPrm.iSubagente4); // fix 80691
	}

	// Impostazione idAzienda sui proxy
	// fix 80830 >> cambiato nome da "setCodiceAziendaInternal" a
	// "setIdAziendaInternal"
	protected void setIdAziendaInternal(String codiceAzienda) {
		super.setIdAziendaInternal(codiceAzienda);
		String key1 = iFornitoreAcquisto.getKey();
		String key2 = iModalitaSpedizione.getKey();
		String key3 = iModalitaConsegna.getKey();
		String key4 = iVtClienteDivisione.getKey();
		String key5 = iVtSettore.getKey();
		String key6 = iVtArticolo.getKey();
		String key8 = iVtArticoloPrm.getKey();
		String key7 = iArticoloCliente.getKey();
		String key9 = iVtBuyer.getKey();
		// String key10 = iVtStagione.getKey(); //Fix 80660
		String key11 = iVtArticoloFin.getKey();
		String key11b = iArticoloFin.getKey(); // Fix 80830
		String key12 = iUMFin.getKey();
		String key12b = iRUMFin.getKey();
		String key13 = iVtEtichetta.getKey();
		String key14 = iArticoloCliente.getKey();
		String keyS2 = iSubagente2.getKey(); // fix 80691
		String keyS3 = iSubagente3.getKey(); // fix 80691
		String keyS4 = iSubagente4.getKey(); // fix 80691

		iFornitoreAcquisto.setKey(KeyHelper.replaceTokenObjectKey(key1, 1, codiceAzienda));
		iModalitaSpedizione.setKey(KeyHelper.replaceTokenObjectKey(key2, 1, codiceAzienda));
		iModalitaConsegna.setKey(KeyHelper.replaceTokenObjectKey(key3, 1, codiceAzienda));
		iVtClienteDivisione.setKey(KeyHelper.replaceTokenObjectKey(key4, 1, codiceAzienda));
		iVtSettore.setKey(KeyHelper.replaceTokenObjectKey(key5, 1, codiceAzienda));
		// fix 80444 - inizio
		iVtArticolo.setKey(KeyHelper.replaceTokenObjectKey(key6, 1, codiceAzienda));
		iVtArticoloPrm.setKey(KeyHelper.replaceTokenObjectKey(key8, 1, codiceAzienda));
		iArticoloCliente.setKey(KeyHelper.replaceTokenObjectKey(key7, 1, codiceAzienda));
		iVtBuyer.setKey(KeyHelper.replaceTokenObjectKey(key9, 1, codiceAzienda));
		// iVtStagione.setKey(KeyHelper.replaceTokenObjectKey(key10, 1,
		// codiceAzienda));
		// fix 80444 - fine
		iVtArticoloFin.setKey(KeyHelper.replaceTokenObjectKey(key11, 1, codiceAzienda)); // fix
		// 80498
		iArticoloFin.setKey(KeyHelper.replaceTokenObjectKey(key11b, 1, codiceAzienda)); // fix 80830
		iUMFin.setKey(KeyHelper.replaceTokenObjectKey(key12, 1, codiceAzienda)); // fix
		// 80498
		iRUMFin.setKey(KeyHelper.replaceTokenObjectKey(key12b, 1, codiceAzienda)); // fix
		// 80498
		iVtEtichetta.setKey(KeyHelper.replaceTokenObjectKey(key13, 1, codiceAzienda)); // fix
		// 80498
		iArticoloCliente.setKey(KeyHelper.replaceTokenObjectKey(key14, 1, codiceAzienda));
		iSubagente2.setKey(KeyHelper.replaceTokenObjectKey(keyS2, 1, codiceAzienda)); // fix
		// 80691
		iSubagente3.setKey(KeyHelper.replaceTokenObjectKey(keyS3, 1, codiceAzienda)); // fix
		// 80691
		iSubagente4.setKey(KeyHelper.replaceTokenObjectKey(keyS4, 1, codiceAzienda)); // fix
		// 80691

	}

	// ----- Fix 80358 - Fine

	// fix 80691 - inizio (Gestione subagenti aggiuntivi e provvigioni)

	public void setServeRicalProvvSubag2(boolean b) {
		this.iServeRicalcoloProvvSubag2 = b;
	}

	public boolean isServeRicalProvvSubag2() {
		return iServeRicalcoloProvvSubag2;
	}

	public void setServeRicalProvvSubag3(boolean b) {
		this.iServeRicalcoloProvvSubag3 = b;
	}

	public boolean isServeRicalProvvSubag3() {
		return iServeRicalcoloProvvSubag3;
	}

	public void setServeRicalProvvSubag4(boolean b) {
		this.iServeRicalcoloProvvSubag4 = b;
	}

	public boolean isServeRicalProvvSubag4() {
		return iServeRicalcoloProvvSubag4;
	}

	public void setProvvigione1Subagente2(BigDecimal iProvvigione1Subagente2) {
		this.iProvvigione1Subagente2 = iProvvigione1Subagente2;
		setDirty();
	}

	public BigDecimal getProvvigione1Subagente2() {
		return iProvvigione1Subagente2;
	}

	public void setProvvigione2Subagente2(BigDecimal iProvvigione2Subagente2) {
		this.iProvvigione2Subagente2 = iProvvigione2Subagente2;
		setDirty();
	}

	public BigDecimal getProvvigione2Subagente2() {
		return iProvvigione2Subagente2;
	}

	public void setProvvigione1Subagente3(BigDecimal iProvvigione1Subagente3) {
		this.iProvvigione1Subagente3 = iProvvigione1Subagente3;
		setDirty();
	}

	public BigDecimal getProvvigione1Subagente3() {
		return iProvvigione1Subagente3;
	}

	public void setProvvigione2Subagente3(BigDecimal iProvvigione2Subagente3) {
		this.iProvvigione2Subagente3 = iProvvigione2Subagente3;
		setDirty();
	}

	public BigDecimal getProvvigione2Subagente3() {
		return iProvvigione2Subagente3;
	}

	public void setProvvigione1Subagente4(BigDecimal iProvvigione1Subagente4) {
		this.iProvvigione1Subagente4 = iProvvigione1Subagente4;
		setDirty();
	}

	public BigDecimal getProvvigione1Subagente4() {
		return iProvvigione1Subagente4;
	}

	public void setProvvigione2Subagente4(BigDecimal iProvvigione2Subagente4) {
		this.iProvvigione2Subagente4 = iProvvigione2Subagente4;
		setDirty();
	}

	public BigDecimal getProvvigione2Subagente4() {
		return iProvvigione2Subagente4;
	}

	public void setDifferenzaPrezzoSubag2(boolean iDifferenzaPrezzoSubag2) {
		this.iDifferenzaPrezzoSubag2 = iDifferenzaPrezzoSubag2;
		setDirty();
	}

	public boolean hasDifferenzaPrezzoSubag2() {
		return iDifferenzaPrezzoSubag2;
	}

	public void setDifferenzaPrezzoSubag3(boolean iDifferenzaPrezzoSubag3) {
		this.iDifferenzaPrezzoSubag3 = iDifferenzaPrezzoSubag3;
		setDirty();
	}

	public boolean hasDifferenzaPrezzoSubag3() {
		return iDifferenzaPrezzoSubag3;
	}

	public void setDifferenzaPrezzoSubag4(boolean iDifferenzaPrezzoSubag4) {
		this.iDifferenzaPrezzoSubag4 = iDifferenzaPrezzoSubag4;
		setDirty();
	}

	public boolean hasDifferenzaPrezzoSubag4() {
		return iDifferenzaPrezzoSubag4;
	}

	// proxy SUBAGENTE 2
	public void setSubagente2(Agente Subagente) {
		this.iSubagente2.setObject(Subagente);
		setDirty();
	}

	public Agente getSubagente2() {
		return (Agente) iSubagente2.getObject();
	}

	public void setSubagente2Key(String key) {
		iSubagente2.setKey(key);
		setDirty();
	}

	public String getSubagente2Key() {
		return iSubagente2.getKey();
	}

	public void setIdAgenteSub2(String rAgenteSub) {
		String key = iSubagente2.getKey();
		iSubagente2.setKey(KeyHelper.replaceTokenObjectKey(key, 2, rAgenteSub));
		setDirty();
	}

	public String getIdAgenteSub2() {
		String key = iSubagente2.getKey();
		String objRAgenteSub = KeyHelper.getTokenObjectKey(key, 2);
		return objRAgenteSub;
	}

	// proxy SUBAGENTE 3
	public void setSubagente3(Agente Subagente) {
		this.iSubagente3.setObject(Subagente);
		setDirty();
	}

	public Agente getSubagente3() {
		return (Agente) iSubagente3.getObject();
	}

	public void setSubagente3Key(String key) {
		iSubagente3.setKey(key);
		setDirty();
	}

	public String getSubagente3Key() {
		return iSubagente3.getKey();
	}

	public void setIdAgenteSub3(String rAgenteSub) {
		String key = iSubagente3.getKey();
		iSubagente3.setKey(KeyHelper.replaceTokenObjectKey(key, 2, rAgenteSub));
		setDirty();
	}

	public String getIdAgenteSub3() {
		String key = iSubagente3.getKey();
		String objRAgenteSub = KeyHelper.getTokenObjectKey(key, 2);
		return objRAgenteSub;
	}

	// proxy SUBAGENTE 4
	public void setSubagente4(Agente Subagente) {
		this.iSubagente4.setObject(Subagente);
		setDirty();
	}

	public Agente getSubagente4() {
		return (Agente) iSubagente4.getObject();
	}

	public void setSubagente4Key(String key) {
		iSubagente4.setKey(key);
		setDirty();
	}

	public String getSubagente4Key() {
		return iSubagente4.getKey();
	}

	public void setIdAgenteSub4(String rAgenteSub) {
		String key = iSubagente4.getKey();
		iSubagente4.setKey(KeyHelper.replaceTokenObjectKey(key, 2, rAgenteSub));
		setDirty();
	}

	public String getIdAgenteSub4() {
		String key = iSubagente4.getKey();
		String objRAgenteSub = KeyHelper.getTokenObjectKey(key, 2);
		return objRAgenteSub;
	}

	// fix 80691 - fine

	// fix 80364 - inizio (Gestione griglia delle maggiorazioni)
	/*
	 * public Vector getVtMaggiorOrdVenRig() { if (isOnDB()) {
	 * iVtMaggiorOrdVenRig.setWhereClause(VtMaggiorDettTM.ID_AZIENDA + " = '" +
	 * Azienda.getAziendaCorrente()+ "' AND " + VtMaggiorOrdVenRigTM.ID_ANNO_ORD +
	 * " = '" + getAnnoDocumento() + "' AND " + VtMaggiorOrdVenRigTM.ID_NUMERO_ORD +
	 * " = '" + getNumeroDocumento() + "' AND " + VtMaggiorOrdVenRigTM.ID_RIGA_ORD +
	 * " = " + getNumeroRigaDocumento() + "  AND " +
	 * VtMaggiorOrdVenRigTM.ID_DET_RIGA_ORD + " = " + getDettaglioRigaDocumento() +
	 * "  AND " + VtMaggiorOrdVenRigTM.ID_CLIENTE + " = '" + getIdCliente() +
	 * "' AND " + VtMaggiorOrdVenRigTM.ID_ARTICOLO + " = '" + getIdArticolo() + "'",
	 * true);
	 * 
	 * iVtMaggiorOrdVenRig.setOrderByClause(VtMaggiorOrdVenRigTM.ID_TIPO_MAGG + ", "
	 * + VtMaggiorOrdVenRigTM.ID_CODICE); return iVtMaggiorOrdVenRig.getElements();
	 * } else return new Vector(); }
	 */

	public List getVtMaggiorOrdVenRig() {
		return getVtMaggiorOrdVenRigInternal();
	}

	protected OneToMany getVtMaggiorOrdVenRigInternal() {
		if (iVtMaggiorOrdVenRig.isNew())
			iVtMaggiorOrdVenRig.retrieve();
		return iVtMaggiorOrdVenRig;
	}

	// definizione metodi per campi totali sotto la griglia maggiorazioni

	// prezzo listino
	public BigDecimal getTotPrz() {
		return iTotPrz;
	}

	public void setTotPrz(BigDecimal totPrezzo) {
		this.iTotPrz = totPrezzo;
	}

	// totale maggiorazioni
	public BigDecimal getTotMag() {
		return iTotMag;
	}

	public void setTotMag(BigDecimal TotaleMagg) {
		this.iTotMag = TotaleMagg;
	}

	// Totale nuovo prezzo riga ordine
	public BigDecimal getTotale() {
		return iTotale;
	}

	public void setTotale(BigDecimal totale) {
		this.iTotale = totale;
	}

	// prezzo totale pannello riga ridotta (prezzo - sconti + maggiorazione)
	public BigDecimal getTotalePrz() {
		return iTotalePrz;
	}

	public void setTotalePrz(BigDecimal totalePrezzo) {
		this.iTotalePrz = totalePrezzo;
	}

	// Numero pezze riga
	public BigDecimal getNumeroPezze() {
		return iNumeroPezze;
	}

	public void setNumeroPezze(BigDecimal numeroPezze) {
		this.iNumeroPezze = numeroPezze;
	}

	// implementazioni metodi chiavi
	public void setIdAzienda(String idAzienda) {
		super.setIdAzienda(idAzienda);
		iVtMaggiorOrdVenRig.setFatherKeyChanged();
		iVtOrdVenRigAtv.setFatherKeyChanged();
	}

	public void setAnnoDocumento(String idAnno) {
		super.setAnnoDocumento(idAnno);
		iVtMaggiorOrdVenRig.setFatherKeyChanged();
		iVtOrdVenRigAtv.setFatherKeyChanged();
	}

	public void setNumeroDocumento(String idNum) {
		super.setNumeroDocumento(idNum);
		iVtMaggiorOrdVenRig.setFatherKeyChanged();
		iVtOrdVenRigAtv.setFatherKeyChanged();
	}

	public void setNumeroRigaDocumento(Integer numeroRigaDocumento) {
		super.setNumeroRigaDocumento(numeroRigaDocumento);
		iVtMaggiorOrdVenRig.setFatherKeyChanged();
		iVtOrdVenRigAtv.setFatherKeyChanged();
	}

	/*
	 * public void setDettaglioRigaDocumento(Integer dettaglioRigaDocumento) {
	 * super.setDettaglioRigaDocumento(dettaglioRigaDocumento);
	 * iVtMaggiorOrdVenRig.setFatherKeyChanged(); }
	 */

	// Metodi Tabelle Figlie

	public List getVtOrdVenRigAtv() {
		return getVtOrdVenRigAtvInternal();
	}

	protected OneToMany getVtOrdVenRigAtvInternal() {
		if (iVtOrdVenRigAtv.isNew())
			iVtOrdVenRigAtv.retrieve();
		// Fix 81534 return iVtOrdVenRigAtv;
		iVtOrdVenRigAtv.setOrderBy(VtOrdVenRigAtvTM.SEQUENZA); // Fix 81534
		return iVtOrdVenRigAtv;
	}

	public int saveOwnedObjects(int rc) throws SQLException {
		rc = super.saveOwnedObjects(rc);
		rc = iVtMaggiorOrdVenRig.save(rc);
		rc = rc + iVtOrdVenRigAtv.save(rc);
		return rc;
	}

	public int deleteOwnedObjects() throws SQLException {

		// Fix 81880 >
		VtOrdineVendita ordVen = (VtOrdineVendita) this.getTestata();
		Integer rifNumRigaOrdADisp = this.getNumRigaAdisp();
		Integer rifDetRigaOrdADisp = this.getDetRigaAdisp();
		String rifNumOrdADisp = ordVen.getNumeroOrdAdisp();
		String rifAnnoOrdADisp = ordVen.getAnnoOrdineAdisp();

		// BigDecimal qtaDisposta =
		// calcoloQtaDisposta(rifAnnoOrdADisp,rifNumOrdADisp,rifNumRigaOrdADisp,rifDetRigaOrdADisp);
		String keyRigaOrdVen = Azienda.getAziendaCorrente() + KeyHelper.KEY_SEPARATOR + rifAnnoOrdADisp
				+ KeyHelper.KEY_SEPARATOR + rifNumOrdADisp + KeyHelper.KEY_SEPARATOR + rifNumRigaOrdADisp;

		VtOrdineVenditaRigaPrm rigaPrm = (VtOrdineVenditaRigaPrm) PersistentObject
				.elementWithKey(VtOrdineVenditaRigaPrm.class, keyRigaOrdVen, PersistentObject.NO_LOCK);
		;
		// rigaPrm.save();
		// Fix 81880 <

		int ret = super.deleteOwnedObjects();
		ret = getVtOrdVenRigAtvInternal().delete();

		if (rigaPrm != null) {
			rigaPrm.iEliminata = '1';
			aggiornaQtaDisposta(rigaPrm, this);
			rigaPrm.save(); // Fix 81880
		}
		return iVtMaggiorOrdVenRig.delete(ret);
	}

	public boolean initializeOwnedObjects(boolean result) {
		result = super.initializeOwnedObjects(result);
		result = iVtMaggiorOrdVenRig.initialize(result);
		result = iVtOrdVenRigAtv.initialize(result);
		return result;
	}

	// fix 80364 - fine

	// fix 80503 - inizio

	// iRCliente
	public String getRCliente() {
		String rCliente = iRCliente;
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			rCliente = testata.getCliente().getIdCliente();

		return rCliente;
	}

	public void setRCliente(String rCliente) {
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			rCliente = testata.getCliente().getIdCliente();
		this.iRCliente = rCliente;
	}

	// iDCliente
	public String getDCliente() {
		String dCliente = iDCliente;
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			dCliente = testata.getCliente().getRagioneSociale();

		return dCliente;
	}

	public void setDCliente(String dCliente) {
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			dCliente = testata.getCliente().getRagioneSociale();
		this.iDCliente = dCliente;
	}

	// iDStagione
	public String getDStagione() {
		String dStagione = iDStagione;
		VtOrdineVendita testata = (VtOrdineVendita) this.getTestata();
		if (testata != null)
			if (testata.getVtStagione() != null)
				dStagione = testata.getVtStagione().getDescrizione().getDescrizione();

		return dStagione;
	}

	public void setDStagione(String dStagione) {
		VtOrdineVendita testata = (VtOrdineVendita) this.getTestata();
		if (testata != null)
			if (testata.getVtStagione() != null)
				dStagione = testata.getVtStagione().getDescrizione().getDescrizione();
		this.iDStagione = dStagione;
	}

	// iRifCliente
	public String getRifCliente() {
		String rifCliente = null;
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			rifCliente = testata.getNumeroOrdineIntestatario();

		return rifCliente;
	}

	public void setRifCliente(String rifCliente) {
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			rifCliente = testata.getNumeroOrdineIntestatario();
		this.iRifCliente = rifCliente;
	}

	// Livelli
	public Integer getLivelli() {
		return iLivelli;
	}

	public void setLivelli(Integer livelli) {
		this.iLivelli = livelli;
	}

	// IdArtCorrente
	public String getIdArtCorrente() {
		return iIdArtCorrente;
	}

	public void setArtCorrente(String idArtCorrente) {
		this.iIdArtCorrente = idArtCorrente;
	}

	public BigDecimal getQtaRichiestaCorrente() {
		return iQtaRichiestaCorrente;
	}

	public void setQtaRichiestaCorrente(BigDecimal QtaRichiestaCorrente) {
		this.iQtaRichiestaCorrente = QtaRichiestaCorrente;
	}

	// 80971 inizio
	public BigDecimal getQtaTaglio() {
		return iQtaTaglio;
	}

	public void setQtaTaglio(BigDecimal QtaTaglio) {
		this.iQtaTaglio = QtaTaglio;
	}
	// 80971 fine

	// Gestione griglia non posseduta degli articoli impegnati
	protected PersistentCollection iVtOrdVenRigPrmImp = new PersistentCollection(VtOrdVenRigPrmImp.class, "", "",
			"retrieveList");

	public Vector getVtOrdVenRigPrmImp() {
		return new Vector();
	}

	// ------------------------

	// Gestione griglia non posseduta dei lotti materiali impegnati
	protected PersistentCollection iVtOrdVenRigPrmMat = new PersistentCollection(VtOrdVenRigPrmMat.class, "", "",
			"retrieveList");

	public Vector getVtOrdVenRigPrmMat() {
		return new Vector();
	}

	// fix 80503 - fine

	// Fix 80529 - Inizio
	public void reloadRiserveLotto() {
		List righeLotto = this.getRigheLotto();

		/**
		 * Fix 81042 - BP 14/06/2017 - INIZIO Cancellazione riserve....va sempre fatta
		 * anche se non ci sono lotti nuovi o non ce ne sono piu... altrimenti resta
		 * anche se lotto 衳tato eliminato
		 */
		// String deleteKey = VtGestioneRiserveLotti.creaChiaveRiservaLotto(null, null,
		// null, this.getKey(), 'V');
		// VtTessileUtil.svuotaVtRiserveLotto(deleteKey);
		/**
		 * Fix 81042 - BP 14/06/2017 - FINE
		 */

		/*
		 * if (isOnDB() && !righeLotto.isEmpty()) {
		 * 
		 * // Riempio VtRiserveLotto String idMagazzino = this.getIdMagazzino(); String
		 * key = this.getKey(); if (idMagazzino != null && key != null)
		 * VtGestioneRiserveLotti.generaRiservaLottoOrdVenRig(righeLotto, idMagazzino,
		 * key); }
		 */
	}

	// Risoluzione problema lotto

	public Vector checkAll(BaseComponentsCollection components) {

		checkLotti();

		// Fix 80951
		if (this.getAltezzaMultipla() != '0' && this.getAltezzaMultipla() != '1' && this.getAltezzaMultipla() != '2'
				&& this.getAltezzaMultipla() != '3' && this.getAltezzaMultipla() != '4') {
			this.setAltezzaMultipla('0');
		}

		Vector errors = super.checkAll(components);

		// Fix 81296 - Se sono in copia e ho cambiato l'id articolo devo togliere i
		// lotti con il vecchio id lotto e toglo il messaggio di errore
		// Cambio anche la chiave della riga sulle attivit�, se presenti
		// Annullo anche l'articolo ordinato per far si che la save lo setti
		// correttamente in base all'articolo selezionato
		if (this.isInCopiaRiga) {
			this.setRArtOrdinato(null);
			List righe = this.getRigheLotto();
			Iterator iter = righe.iterator();
			while (iter.hasNext()) {
				OrdineVenditaRigaLottoPrm lotto = (OrdineVenditaRigaLottoPrm) iter.next();
				if (!lotto.getIdArticolo().equals(this.getIdArticolo())) {
					Iterator errIter = errors.iterator();
					while (errIter.hasNext()) {
						ErrorMessage errore = (ErrorMessage) errIter.next();
						if (errore.getAttOrGroupName().equals("CodiceLotto")
								&& errore.getLongText().indexOf("/" + lotto.getIdLotto()) != -1) {
							errIter.remove();
							break;
						}
					}
					iter.remove();
				}
			}

			List attivitaList = this.getVtOrdVenRigAtv();
			Iterator iteratv = attivitaList.iterator();
			while (iteratv.hasNext()) {
				VtOrdVenRigAtv atv = (VtOrdVenRigAtv) iteratv.next();
				atv.setIdRigaOrd(this.getNumeroRigaDocumento());
			}
		}
		// FIX 81347 INIZIO
		// if (errors.isEmpty()) {
		// 81368 inizio
		// check : Valuta Cli == Valuta listino ord ven riga
		// if != --> err message
		/*
		 * Cliente c; try { c = Cliente.elementWithKey(((OrdineVendita)
		 * this.getTestata()).getClienteKey(), PersistentObject.NO_LOCK); } catch
		 * (SQLException e) { throw new RuntimeException(e); }
		 */

		if (!iPrzManuale) {
			if (this.getListino() != null && this.getServizioValutaTestata() != null) {
				if (!this.getServizioValutaTestata().equals(this.getListino().getValuta().getKey())) {
					String key = KeyHelper.buildObjectKey(new String[] { this.getIdAzienda(), this.getRStagione(),
							this.getRDivTex(), this.getServizioValutaTestata() });
					try {
						VtMaggiorZonaValutaCV checkMaggiorZonaVal = (VtMaggiorZonaValutaCV) PersistentObject
								.elementWithKey(VtMaggiorZonaValutaCV.class, key, lockType);
						if (checkMaggiorZonaVal == null) {
							ErrorMessage em = new ErrorMessage("VT_0000015", false);
							errors.add(em);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				/*
				 * if ( c.getValuta() == null || this.getListino().getValuta().getKey() == null
				 * || c.getValuta().getKey().equals(this.getListino().getValuta(). getKey())) {
				 * return errors; } else { if (c.getValuta() != null) { String key =
				 * KeyHelper.buildObjectKey(new String[] {this.getIdAzienda(),
				 * this.getRStagione(), this.getRDivTex(), c.getIdValuta()}); try {
				 * VtMaggiorZonaValutaCV checkMaggiorZonaVal = (VtMaggiorZonaValutaCV)
				 * PersistentObject.elementWithKey(VtMaggiorZonaValutaCV.class, key, lockType);
				 * if (checkMaggiorZonaVal == null) { ErrorMessage em = new
				 * ErrorMessage("VT_0000015", false); errors.add(em); } else { return errors; }
				 * } catch (SQLException e) { e.printStackTrace(); } }
				 * 
				 * 
				 * }
				 */
				// 81368 fine
			}
		}
		// }
		// FIX 81347 FINE
		// 81373 INIZIO

		String tesKey = this.getTestataKey();
		VtOrdineVendita ordVen = null;
		try {
			ordVen = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, tesKey,
					PersistentObject.NO_LOCK);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (ordVen != null) {
			String cauKey = ordVen.getCausaleKey();
			VtCausaleOrdineVendita cau = null;
			try {
				cau = (VtCausaleOrdineVendita) PersistentObject.elementWithKey(VtCausaleOrdineVendita.class, cauKey,
						PersistentObject.NO_LOCK);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (cau != null && !cau.getContoTrasformazione()) {
				Object[] keyParts = { Azienda.getAziendaCorrente(), "" + SchemaProdottoLotto.PRODOTTO,
						this.getArticolo().getIdProdotto().substring(0, 2) };
				SchemaProdottoLotto schPrd = null;
				try {
					schPrd = SchemaProdottoLotto.elementWithKey(KeyHelper.buildObjectKey(keyParts),
							PersistentObject.NO_LOCK);
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
				if (schPrd != null) {
					if (schPrd.getRicercaINTRA()) {
						if (this.getArticolo().getClasseMateriale() == null) {
							String classeMateriale = VtIntraRicerca.ricercaIntra(this.getIdAzienda(),
									this.getIdArticolo());
							if (classeMateriale == null) {
								ErrorMessage em = new ErrorMessage("VT_0000020", false);
								errors.add(em);
							}
						}
					}
				}
			}
		}
		// 81368 inizio
		/*
		 * if(this.getArticolo().getClasseMateriale() == null){ String classeMateriale =
		 * VtIntraRicerca.ricercaIntra(this.getIdAzienda(), this.getIdArticolo()); if
		 * (classeMateriale == null) { ErrorMessage em = new ErrorMessage("VT_0000020",
		 * false); errors.add(em); } }
		 */
		// 81373 FINE
		// 81366 inizio
		/*
		 * String classeMateriale = VtIntraRicerca.ricercaIntra(this.getIdAzienda(),
		 * this.getIdArticolo()); if (classeMateriale == null) { ErrorMessage em = new
		 * ErrorMessage("VT_0000020", false); errors.add(em); }
		 */
		// 81366 fine

		// 81368 fine
		return errors;
	}

	public void checkLotti() {

		/*
		 * Creata gestione personale per il controllo dei lotti aggiunti e da
		 * cancellare, in quanto per colpa di una personalizzazione i lotti aggiunti
		 * perdono l'idArticolo, e l'ordine vendita non riesce pi� ad eliminare i lotti
		 * che l'utente vuole eliminare
		 */
		List righeLotto = this.getRigheLotto();
		Iterator righeLottoIter = righeLotto.iterator();
		VtDeleteLottoRow classeDeleteRow = (VtDeleteLottoRow) Factory.createObject(VtDeleteLottoRow.class);
		List lottiToDelete = classeDeleteRow.getListaLottiDaCancellare();
		if (!lottiToDelete.isEmpty() && !righeLotto.isEmpty()) {
			Iterator lottiToDeleteIter = lottiToDelete.iterator();

			while (lottiToDeleteIter.hasNext()) {
				String keyLottoToDelete = (String) lottiToDeleteIter.next();
				String idArticolo = KeyHelper.getTokenObjectKey(keyLottoToDelete, 1);
				String idLotto = KeyHelper.getTokenObjectKey(keyLottoToDelete, 2);

				righeLottoIter = righeLotto.iterator();
				while (righeLottoIter.hasNext()) {
					OrdineVenditaRigaLottoPrm lotto = (OrdineVenditaRigaLottoPrm) righeLottoIter.next();
					if (lotto.getIdArticolo().equals(idArticolo) && lotto.getIdLotto().equals(idLotto)) {
						righeLottoIter.remove();
					}
				}
			}
			classeDeleteRow.clearListaLottiDaCancellare();
		}
		if (!righeLotto.isEmpty()) {
			righeLottoIter = righeLotto.iterator();
			List righeLottoPreControllo = this.getRigheLotto();
			List listIndex = new ArrayList();
			while (righeLottoIter.hasNext()) {
				OrdineVenditaRigaLottoPrm lotto = (OrdineVenditaRigaLottoPrm) righeLottoIter.next();
				if (!lotto.getIdArticolo().equals(this.getIdArticolo())/* && lotto.getIdLotto().equals("-") */) {
					int x = controlloSeLottoDuplicato(righeLottoPreControllo, this.getIdArticolo(), lotto.getIdLotto());
					if (x != -1)
						listIndex.add(new Integer(x));
					lotto.setIdArticolo(this.getIdArticolo());
				}
			}
			if (!listIndex.isEmpty()) {
				righeLottoIter = righeLotto.iterator();

				int index = 0;
				while (righeLottoIter.hasNext()) {
					OrdineVenditaRigaLottoPrm lotto = (OrdineVenditaRigaLottoPrm) righeLottoIter.next();

					Iterator iteListIndex = listIndex.iterator();
					while (iteListIndex.hasNext()) {
						Integer idx = (Integer) iteListIndex.next();
						if (idx != null) {
							if (idx.intValue() == index) {
								righeLottoIter.remove();
							}
						}
					}
					index++;
				}
			}
		}

	}

	public int controlloSeLottoDuplicato(List righeLottoPreControllo, String idArticolo, String idLotto) {
		int x = -1;
		int index = 0;
		Iterator iter = righeLottoPreControllo.iterator();
		while (iter.hasNext()) {
			OrdineVenditaRigaLottoPrm lotto = (OrdineVenditaRigaLottoPrm) iter.next();
			if (lotto.getIdArticolo().equals(idArticolo) && lotto.getIdLotto().equals(idLotto))
				return index;
			else
				index++;
		}
		return x;
	}

	// --------------------------

	// Fix 80529 - Fine

	// Fix 80549 - Inizio

	public String getRDivTex() {
		String rDivTex = iRDivTex;
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			rDivTex = testata.getIdDivisione();

		return rDivTex;
	}

	public void setRDivTex(String rDivTex) {
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			rDivTex = testata.getIdDivisione();
		this.iRDivTex = rDivTex;
	}

	public String getDDivTex() {
		String dDivTex = iDDivTex;
		OrdineVendita testata = (OrdineVendita) getTestata();
		if (testata != null) {
			if (testata.getDivisione() != null) {
				dDivTex = testata.getDivisione().getIdDescrizione();
			} else {
				if (dDivTex == null) {
					dDivTex = "";
				}
			}
		} else {
			if (dDivTex == null) {
				dDivTex = "";
			}
		}
		return dDivTex;
	}

	public void setDDivTex(String dDivTex) {
		OrdineVendita testata = (OrdineVendita) this.getTestata();
		if (testata != null)
			if (testata.getDivisione() != null) {
				dDivTex = testata.getDivisione().getIdDescrizione();
			}
		this.iDDivTex = dDivTex;
	}

	// Fix 80549 - Fine

	// Fix 80691 - Inizio - Acquisizione dei nuovi subagenti con relative
	// provvigioni da testata

	public void completaBO() {

		super.completaBO();

		if (getTestata() instanceof VtOrdineVendita) {

			VtOrdineVendita testata = (VtOrdineVendita) getTestata();

			// riporto i test come OrdineVenditaRiga per poi valorizzare nuovi 3
			// subagenti in modo corretto
			// Causale e tipo riga
			CausaleRigaVendita causale = getCausaleRiga();
			if (causale == null) {
				causale = testata.getCausale().getCausaleRigaOrdVen();
				// setCausaleRiga(causale);
			}
			Spesa spesa = causale.getSpesa();
			if (causale.getTipoRiga() == TipoRiga.SPESE_MOV_VALORE && spesa != null) {
				// non serve
			} else {
				if (getSpecializzazioneRiga() == RIGA_PRIMARIA
						|| getSpecializzazioneRiga() == this.RIGA_SECONDARIA_DA_FATTURARE) {
					setIdAgenteSub2(testata.getIdAgenteSub2());
					// setSubagente2(testata.getSubagente2());

					// Fix 80942 setProvvigione1Subagente2(testata.getProvvigioneSubagente2());
					// Fix 80942 setDifferenzaPrezzoSubag2(testata.hasDifferenzaPrezzoSubag2());

					setIdAgenteSub3(testata.getIdAgenteSub3());
					// setSubagente3(testata.getSubagente3());

					// Fix 80942 setProvvigione1Subagente3(testata.getProvvigioneSubagente3());
					// Fix 80942 setDifferenzaPrezzoSubag3(testata.hasDifferenzaPrezzoSubag3());

					setIdAgenteSub4(testata.getIdAgenteSub4());
					// setSubagente4(testata.getSubagente4());

					// Fix 80942 setProvvigione1Subagente4(testata.getProvvigioneSubagente4());
					// Fix 80942 setDifferenzaPrezzoSubag4(testata.hasDifferenzaPrezzoSubag4());
				}
			}

			// Fix 80866
			// Fatto questa porcata detta da roberto
			Articolo art = this.getArticolo();
			VtArticoliTex articTex = null;

			if (art != null)
				try {
					articTex = (VtArticoliTex) PersistentObject.elementWithKey(VtArticoliTex.class, art.getKey(),
							VtArticoliTex.NO_LOCK);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// settaAltezzaMultipla(art,articTex);
			settaAltezzaMultiplaV2(art, articTex);

			// Fix 80940 - Inizio
			if (this.getIdCartella() == null)
				this.setIdCartella("");
			// Fix 80940 - Fine

			/*
			 * if (this.getIdUMRif() != null) {
			 * 
			 * if(this.getIdUMRif().equals("M2") || this.getIdUMRif().equals("Y2") )
			 * //if(this.getUMRif().getIdUnitaMisura().equals("M2") ||
			 * this.getUMRif().getIdUnitaMisura().equals("Y2") ) {
			 * this.setAltezzaMultipla('1'); } else { this.setAltezzaMultipla('2'); }
			 * 
			 * }
			 */
		}
	}

	// Fix 80691 - Fine

	// Fix 80720 - Inizio

	// MaggiorZonaValuta
	public BigDecimal getMaggiorZonaValuta() {
		return iMaggiorZonaValuta;
	}

	public void setMaggiorZonaValuta(BigDecimal MaggiorZonaValuta) {
		this.iMaggiorZonaValuta = MaggiorZonaValuta;
	}

	// MaggiorCliente
	public BigDecimal getMaggiorCliente() {
		return iMaggiorCliente;
	}

	public void setMaggiorCliente(BigDecimal MaggiorCliente) {
		this.iMaggiorCliente = MaggiorCliente;
	}

	// MaggiorParametrica
	public BigDecimal getMaggiorParametrica() {
		return iMaggiorParametrica;
	}

	public void setMaggiorParametrica(BigDecimal MaggiorParametrica) {
		this.iMaggiorParametrica = MaggiorParametrica;
	}

	// RisZonaValuta
	public BigDecimal getRisZonaValuta() {
		return iRisZonaValuta;
	}

	public void setRisZonaValuta(BigDecimal RisZonaValuta) {
		this.iRisZonaValuta = RisZonaValuta;
	}

	// TotMagCliente
	public BigDecimal getTotMagCliente() {
		return iTotMagCliente;
	}

	public void setTotMagCliente(BigDecimal TotMagCliente) {
		this.iTotMagCliente = TotMagCliente;
	}

	// Fix 80720 - Fine

	// Fix 80725 - Inizio
	// PrezzoVal
	public BigDecimal getPrezzoVal() {
		return iPrezzoVal;
	}

	public void setPrezzoVal(BigDecimal PrezzoVal) {
		this.iPrezzoVal = PrezzoVal;
	}

	// PrezzoVendita
	public BigDecimal getPrezzoVendita() {
		return iPrezzoVendita;
	}

	public void setPrezzoVendita(BigDecimal PrezzoVendita) {
		this.iPrezzoVendita = PrezzoVendita;
	}
	// Fix 80725 - Fine

	// Fix 80722 - Inizio
	public BigDecimal getTotalePezze() {
		// BigDecimal totalePezze = new BigDecimal(0);
		//
		// String[] key = KeyHelper.unpackObjectKey(getKey());
		//
		// String idAzienda = key[0];
		// String annoOrdine = key[1];
		// String numeroOrdine = key[2];
		// String rigaOrdine = key[3];
		//
		// String detRigaOrdine = "0";
		// if(key.length == 5)
		// detRigaOrdine = key[4];
		//
		// String select =
		// " SELECT SUM(" + VtPezzaTM.QTA_UM1 + "), " +
		// " MAX(" + VtPezzaTM.STATO_PEZZA + "), " +
		// " FROM " + VtPezzaTM.TABLE_NAME +
		// " WHERE " + VtPezzaTM.ID_AZIENDA + " = '" + idAzienda + "' " +
		// " AND " + VtPezzaTM.R_ANNO_ORD_VEN + " = '" + annoOrdine + "' " +
		// " AND " + VtPezzaTM.R_NR_ORD_VEN + " = '" + numeroOrdine + "' " +
		// " AND " + VtPezzaTM.R_RIGA_ORD_VEN + " = " + rigaOrdine +
		// " AND " + VtPezzaTM.R_DET_RIGA_ORD_VEN + " = " + detRigaOrdine +
		// " GROUP BY " + VtPezzaTM.ID_AZIENDA +
		// " , " + VtPezzaTM.R_ANNO_ORD_VEN +
		// " , " + VtPezzaTM.R_NR_ORD_VEN +
		// " , " + VtPezzaTM.R_RIGA_ORD_VEN +
		// " , " + VtPezzaTM.R_DET_RIGA_ORD_VEN;
		//
		//
		//
		// CachedStatement cStatement = new CachedStatement(select);
		//
		// try {
		// ResultSet rs = cStatement.executeQuery();
		//
		// // il record letto e' uno solo perche' e' un totale raggruppato
		// //if(rs.next()){
		//
		// BigDecimal qtaPezze = rs.getBigDecimal(1); // sommatoria delle
		// quantita' in UM prm
		// char stsPezze = rs.getString(2).charAt(0); // valore massimo dello
		// stato pezze
		// if(qtaPezze == null){
		// qtaPezze = new BigDecimal(0);
		// }
		//
		// totalePezze = qtaPezze;
		// setMaxStatoPezze(stsPezze);
		// setTotalePezze(totalePezze);
		//
		// //}
		//
		// rs.close();
		// }
		// catch(Exception ex) {
		// ex.printStackTrace();
		// }
		//
		//
		// return totalePezze;
		return iTotalePezze;
	}

	public void setTotalePezze(BigDecimal tot) {
		this.iTotalePezze = tot;
	}

	public char getMaxStatoPezze() {
		return iMaxStatoPezze;
	}

	public void setMaxStatoPezze(char stato) {
		this.iMaxStatoPezze = stato;
	}
	// Fix 80722 - Fine

	// fix 80723 - Inizio (forzatura su errore esclusiva disegno)
	public String getForzatura() {
		return iForzatura;
	}

	public void setForzatura(String id) {
		this.iForzatura = id;
	}

	public String getOkEsclusiva() {
		return iOkEsclusiva;
	}

	public void setOkEsclusiva(String id) {
		this.iOkEsclusiva = id;
	}

	public String getForzaturaV() {
		return iForzaturaV;
	}

	public void setForzaturaV(String id) {
		this.iForzaturaV = id;
	}

	public String getOkEsclusivaV() {
		return iOkEsclusivaV;
	}

	public void setOkEsclusivaV(String id) {
		this.iOkEsclusivaV = id;
	}

	public String getIdClienteEsclusivo() {
		return iIdClienteEsclusivo;
	}

	public void setIdClienteEsclusivo(String id) {
		this.iIdClienteEsclusivo = id;
	}

	public String getDesClienteEsclusivo() {
		return iDesClienteEsclusivo;
	}

	public void setDesClienteEsclusivo(String id) {
		this.iDesClienteEsclusivo = id;
	}

	public String getIdClienteEsclusivoV() {
		return iIdClienteEsclusivoV;
	}

	public void setIdClienteEsclusivoV(String id) {
		this.iIdClienteEsclusivoV = id;
	}

	public String getDesClienteEsclusivoV() {
		return iDesClienteEsclusivoV;
	}

	public void setDesClienteEsclusivoV(String id) {
		this.iDesClienteEsclusivoV = id;
	}

	public String getTassativoV() {
		return iTassativoV;
	}

	public void setTassativoV(String id) {
		this.iTassativoV = id;
	}

	public String getTassativo() {
		return iTassativo;
	}

	public void setTassativo(String id) {
		this.iTassativo = id;
	}
	// fix 80723 - Fine

	// fix 80731 - Inizio (eliminato controllo su valute ordine/listino) >>>
	// inserita il 15/9/2016 (su indicazioni di Roberto)

	/**
	 * Elimino la verifica che la valuta del listino sia uguale alla valuta della
	 * testata.
	 */
	public ErrorMessage checkValute() {
		// ErrorMessage e = super.checkValute();

		// forzo return senza errori su controllo valuta ordine/listino
		return null;
	}

	// fix 80731 - Fine

	// MAAM 04/10/2016
	protected String iIdCartella;

	public void setIdCartella(String idCartella) {
		this.iIdCartella = idCartella;
		setDirty();
	}

	public String getIdCartella() {
		return iIdCartella;
	}

	// Fix 80767 - Inizio

	// fix 80779
	public char iAltezzaMultipla;

	public void setAltezzaMultipla(char altezzaMultipla) {
		this.iAltezzaMultipla = altezzaMultipla;
		setDirty();
	}

	public char getAltezzaMultipla() {
		return iAltezzaMultipla;
	}
	// fix fine 80779

	// Fix 80880 - Inizio
	public String iRStagioneCartCli;

	public void setRStagioneCartCli(String rStagioneCartCli) {
		this.iRStagioneCartCli = rStagioneCartCli;
		setDirty();
	}

	public String getRStagioneCartCli() {
		return iRStagioneCartCli;
	}
	// Fix 80880 - Fine

	// Fix 80907 - Inizio
	public String iNDoganale;

	/*
	 * public void setNDoganale(String nDoganale) { this.iNDoganale = nDoganale;
	 * setDirty(); }
	 * 
	 * public String getNDoganale() { return iNDoganale; }
	 */
	public String iRAttivita;

	public void setRAttivita(String rAttivita) {
		this.iRAttivita = rAttivita;
		setDirty();
	}

	public String getRAttivita() {
		return iRAttivita;
	}

	// Fix 80907 - Fine

	// Calcoli maggiorazioni

	public void calcolaMaggiorazioni() {

		String chiaveTestata = getTestata().getKey();
		String divisione = getRDivTex();
		VtMaggiorZonaValutaCV maggiorCV = null;

		VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal = null;
		VtOrdVenRigCaricaMaggiorazioni.TipoArrotondZonaVal = 'M';

		// Calcolo PrezzoVal
		BigDecimal prezzoVal = getPrezzoVal();
		if (prezzoVal == null || prezzoVal.compareTo(new BigDecimal(0)) <= 0) {

			prezzoVal = getPrezzoListino();

			if (chiaveTestata != null && divisione != null) {
				maggiorCV = VtOrdVenRigCaricaMaggiorazioni.getVtMaggiorZonaValutaCV(divisione, chiaveTestata);

				if (maggiorCV != null) {
					BigDecimal cambio = maggiorCV.getCambio();

					if (getPrezzoListino() != null && cambio != null)
						prezzoVal = getPrezzoListino().multiply(cambio);
				}

			}

			setPrezzoVal(prezzoVal);
		}

		BigDecimal maggiorZonaValuta = getMaggiorZonaValuta();
		if (maggiorZonaValuta == null || maggiorZonaValuta.compareTo(new BigDecimal(0)) <= 0) {

			BigDecimal percMaggior = VtOrdVenRigCaricaMaggiorazioni
					.getPercMaggiorZonaValuta((VtOrdineVendita) getTestata(), getRDivTex());

			if (percMaggior != null)
				setMaggiorZonaValuta(percMaggior);
			else
				setMaggiorZonaValuta(new BigDecimal(0));

		}

		// Maggior Cliente
		if (getMaggiorCliente() == null)
			setMaggiorCliente(new BigDecimal(0));

		// Maggior Parametrica
		if (getMaggiorParametrica() == null)
			setMaggiorParametrica(new BigDecimal(0));

		BigDecimal prz = getPrezzo();
		if (prz == null || prz.compareTo(new BigDecimal(0)) == 0) {

			// var prezzo = prezzoVal + risZonaValuta + totMagCliente +
			// maggiorParam;

			BigDecimal prezzo = this.getPrezzoVal();

			if (prezzo == null)
				prezzo = new BigDecimal(0);

			if (chiaveTestata != null && divisione != null) {

				BigDecimal risMaggiorZV = VtOrdVenRigCaricaMaggiorazioni.getPercMaggZonaValuta(prezzoVal, divisione,
						chiaveTestata, null);

				if (risMaggiorZV == null)
					risMaggiorZV = new BigDecimal(0);

				// Fix 80856 - Inizio
				BigDecimal totMagCliente = new BigDecimal(0);
				if (getMaggiorZonaValuta() != null) {
					// BigDecimal totMagCliente = getMaggiorZonaValuta().add(getPrezzoVal());
					if (getPrezzoVal() != null)
						totMagCliente = getMaggiorZonaValuta().add(getPrezzoVal());
					else
						totMagCliente = getMaggiorZonaValuta();
					if (totMagCliente.compareTo(new BigDecimal(0)) > 0)
						totMagCliente = VtTessileUtil.calcolaPercentuale(totMagCliente, getMaggiorCliente(), 2,
								BigDecimal.ROUND_HALF_UP);
				} else
					totMagCliente = getPrezzoVal();
				// Fix 80856 - Fine

				prezzo = prezzo.add(risMaggiorZV);
				prezzo = prezzo.add(totMagCliente);
				prezzo = prezzo.add(getMaggiorParametrica());

				if (prezzo.compareTo(new BigDecimal(0)) > 0) {
					BigDecimal cent = new BigDecimal(0);
					char tipoArrotond = 'M';

					if (maggiorCV != null) {
						if (maggiorCV.getArrotondamento() != null
								&& maggiorCV.getArrotondamento().compareTo(new BigDecimal(0)) > 0) {
							cent = maggiorCV.getArrotondamento();
							tipoArrotond = maggiorCV.getTipoArrotond();
						}
					}

					if (VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal != null
							&& VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal.compareTo(new BigDecimal(0)) > 0) {
						cent = VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal;
						tipoArrotond = VtOrdVenRigCaricaMaggiorazioni.TipoArrotondZonaVal;
					}

					if (cent.compareTo(new BigDecimal(0)) > 0) {
						switch (tipoArrotond) {
						case 'M': {
							prezzo = prezzo.divide(cent, 0, BigDecimal.ROUND_HALF_UP);
							break;
						}
						case 'E': {
							prezzo = prezzo.divide(cent, 0, BigDecimal.ROUND_UP);
							break;
						}
						}

						prezzo = prezzo.multiply(cent);
					}
				}
			}

			setPrezzo(prezzo);

		}

	}

	public static BigDecimal calcolaPrezzoConMaggiorEdit(VtOrdineVenditaRigaPrm ordVen, BigDecimal przListino) {

		BigDecimal prezzo = new BigDecimal(0);
		BigDecimal percMaggior = new BigDecimal(0);

		String chiaveTestata = ordVen.getTestata().getKey();
		String divisione = ordVen.getRDivTex();
		VtMaggiorZonaValutaCV maggiorCV = null;

		VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal = null;
		VtOrdVenRigCaricaMaggiorazioni.TipoArrotondZonaVal = 'M';

		// Calcolo PrezzoVal
		BigDecimal prezzoVal = przListino;

		if (chiaveTestata != null && divisione != null) {
			maggiorCV = VtOrdVenRigCaricaMaggiorazioni.getVtMaggiorZonaValutaCV(divisione, chiaveTestata);

			if (maggiorCV != null) {
				BigDecimal cambio = maggiorCV.getCambio();

				if (cambio != null)
					prezzoVal = przListino.multiply(cambio);
			}

		}

		// BigDecimal maggiorZonaValuta = ordVen.getMaggiorZonaValuta();
		/*
		 * if (maggiorZonaValuta == null || maggiorZonaValuta.compareTo(new
		 * BigDecimal(0)) <= 0) { percMaggior =
		 * VtOrdVenRigCaricaMaggiorazioni.getPercMaggiorZonaValuta((VtOrdineVendita)
		 * ordVen.getTestata(), divisione);
		 * 
		 * }
		 */

		// var prezzo = prezzoVal + risZonaValuta + totMagCliente +
		// maggiorParam;

		// prezzo = ordVen.getPrezzoVal();
		prezzo = prezzoVal; // Fix 80820

		if (chiaveTestata != null && divisione != null) {

			BigDecimal risMaggiorZV = VtOrdVenRigCaricaMaggiorazioni.getPercMaggZonaValuta(prezzoVal, divisione,
					chiaveTestata, null);

			if (risMaggiorZV == null)
				risMaggiorZV = new BigDecimal(0);

			/**
			 * Fix 81042 - BP 13/06/2017 - INIZIO Esistono casistiche in cui
			 * getMaggiorZonaValuta() torna null e andava in nullPointer (su decode della
			 * qta) condiziono con if
			 */

			// BigDecimal totMagCliente =
			// ordVen.getMaggiorZonaValuta().add(ordVen.getPrezzoVal());
			BigDecimal totMagCliente = ordVen.getPrezzoVal();
			if (ordVen.getMaggiorZonaValuta() != null) {
				totMagCliente = ordVen.getMaggiorZonaValuta().add(totMagCliente);
			}

			/**
			 * Fix 81042 - BP 13/06/2017 - FINE
			 */

			if (totMagCliente.compareTo(new BigDecimal(0)) > 0)
				totMagCliente = VtTessileUtil.calcolaPercentuale(totMagCliente, ordVen.getMaggiorCliente(), 2,
						BigDecimal.ROUND_HALF_UP);

			// Fix 80820 - Inizio
			BigDecimal maggiorParametrica = VtOrdineVenditaRigaPrmCompletaFormModifier.totaleMaggiorazioniRiga(ordVen)
					.setScale(2);
			// Fix 80820 - Fine

			prezzo = prezzo.add(risMaggiorZV);
			prezzo = prezzo.add(totMagCliente);
			// prezzo = prezzo.add(ordVen.getMaggiorParametrica()); // Fix 80820
			prezzo = prezzo.add(maggiorParametrica).setScale(2, BigDecimal.ROUND_HALF_UP);

			if (prezzo.compareTo(new BigDecimal(0)) > 0) {
				BigDecimal cent = new BigDecimal(0);
				char tipoArrotond = 'M';

				if (maggiorCV != null) {
					if (maggiorCV.getArrotondamento() != null
							&& maggiorCV.getArrotondamento().compareTo(new BigDecimal(0)) > 0) {
						cent = maggiorCV.getArrotondamento();
						tipoArrotond = maggiorCV.getTipoArrotond();
					}
				}

				if (VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal != null
						&& VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal.compareTo(new BigDecimal(0)) > 0) {
					cent = VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal;
					tipoArrotond = VtOrdVenRigCaricaMaggiorazioni.TipoArrotondZonaVal;
				}

				if (cent.compareTo(new BigDecimal(0)) > 0) {
					switch (tipoArrotond) {
					case 'M': {
						prezzo = prezzo.divide(cent, 0, BigDecimal.ROUND_HALF_UP);
						break;
					}
					case 'E': {
						prezzo = prezzo.divide(cent, 0, BigDecimal.ROUND_UP);
						break;
					}
					}

					prezzo = prezzo.multiply(cent);
				}
			}
		}

		return prezzo;

	}

	public static BigDecimal calcolaPrezzoConMaggiorNew(String chiaveTestata, String divisione, BigDecimal przListino) {

		BigDecimal prezzo = new BigDecimal(0);
		BigDecimal percMaggior = new BigDecimal(0);

		VtMaggiorZonaValutaCV maggiorCV = null;

		VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal = null;
		VtOrdVenRigCaricaMaggiorazioni.TipoArrotondZonaVal = 'M';

		// Calcolo PrezzoVal
		BigDecimal prezzoVal = przListino;

		if (chiaveTestata != null && divisione != null) {
			maggiorCV = VtOrdVenRigCaricaMaggiorazioni.getVtMaggiorZonaValutaCV(divisione, chiaveTestata);

			if (maggiorCV != null) {
				BigDecimal cambio = maggiorCV.getCambio();

				if (cambio != null)
					prezzoVal = przListino.multiply(cambio);
			}

		}

		// BigDecimal percMaggior =
		// VtOrdVenRigCaricaMaggiorazioni.getPercMaggiorZonaValuta((VtOrdineVendita)getTestata(),
		// divisione);

		BigDecimal maggiorCliente = new BigDecimal(0);
		BigDecimal maggiorParametrica = new BigDecimal(0);

		/*
		 * //Maggior Cliente if(ordVen.getMaggiorCliente() == null)
		 * ordVen.setMaggiorCliente(new BigDecimal(0));
		 * 
		 * //Maggior Parametrica if(ordVen.getMaggiorParametrica() == null)
		 * ordVen.setMaggiorParametrica(new BigDecimal(0));
		 */

		// var prezzo = prezzoVal + risZonaValuta + totMagCliente +
		// maggiorParam;

		prezzo = prezzoVal;

		if (chiaveTestata != null && divisione != null) {

			BigDecimal risMaggiorZV = VtOrdVenRigCaricaMaggiorazioni.getPercMaggZonaValuta(prezzoVal, divisione,
					chiaveTestata, null);

			if (risMaggiorZV == null)
				risMaggiorZV = new BigDecimal(0);

			BigDecimal totMagCliente = percMaggior.add(prezzoVal);
			if (totMagCliente.compareTo(new BigDecimal(0)) > 0)
				totMagCliente = VtTessileUtil.calcolaPercentuale(totMagCliente, maggiorCliente, 2,
						BigDecimal.ROUND_HALF_UP);

			prezzo = prezzo.add(risMaggiorZV);
			prezzo = prezzo.add(totMagCliente);
			prezzo = prezzo.add(maggiorParametrica);

			if (prezzo.compareTo(new BigDecimal(0)) > 0) {

				// BigDecimal cent = new BigDecimal(0);
				BigDecimal cent = new BigDecimal(1); // Fix 80808

				char tipoArrotond = 'M';

				if (maggiorCV != null) {
					if (maggiorCV.getArrotondamento() != null
							&& maggiorCV.getArrotondamento().compareTo(new BigDecimal(0)) > 0) {
						cent = maggiorCV.getArrotondamento();
						tipoArrotond = maggiorCV.getTipoArrotond();
					}
				}

				if (VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal != null
						&& VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal.compareTo(new BigDecimal(0)) > 0) {
					cent = VtOrdVenRigCaricaMaggiorazioni.ArrotondZonaVal;
					tipoArrotond = VtOrdVenRigCaricaMaggiorazioni.TipoArrotondZonaVal;
				}

				if (cent.compareTo(new BigDecimal(0)) > 0) { // Fix 80820
					switch (tipoArrotond) {
					case 'M': {
						prezzo = prezzo.divide(cent, 2, BigDecimal.ROUND_HALF_UP); // Fix
						// 80820
						break;
					}
					case 'E': {
						prezzo = prezzo.divide(cent, 2, BigDecimal.ROUND_UP); // Fix
						// 80820
						break;
					}
					}
				}
				prezzo = prezzo.multiply(cent);
			}
		}

		return prezzo;

	}

	protected void calcolaDatiVendita(OrdineVenditaPO testata) throws SQLException {
		// Fix 2333
		boolean ricercaListini = true;
		if (getProvenienzaPrezzo() != TipoRigaRicerca.MANUALE && isGestitoAPrezziExtra()) {
			Date dataValid = null;
			PersDatiVen pda = PersDatiVen.getCurrentPersDatiVen(); // Fix 3770

			char tipoDataPrezziSconti = testata.getCliente().getRifDataPerPrezzoSconti();
			if (tipoDataPrezziSconti == RifDataPrzScn.DA_CONDIZIONI_GENERALI) {
				tipoDataPrezziSconti = pda.getTipoDataPrezziSconti();
			}
			switch (tipoDataPrezziSconti) {
			case RifDataPrzScn.DATA_ORDINE:
				dataValid = TimeUtils.getDate(testata.getDataDocumento());
				break;
			case RifDataPrzScn.DATA_CONSEGNA:
				dataValid = TimeUtils.getDate(this.getDataConsegnaConfermata());
				break;
			}
			// Fix 5270 BP ini...
			// RicercaPrezziExtraVendita ricerca = new
			// RicercaPrezziExtraVendita();
			RicercaPrezziExtraVendita ricerca = (RicercaPrezziExtraVendita) Factory
					.createObject(RicercaPrezziExtraVendita.class);
			// Fix 5270 BP fine.

			CausaleOrdineVendita causale = testata.getCausale(); // ...FIX04356
			// - DZ
			// Fix 2821
			CondizioniVEPrezziExtra condAcq = null;
			try {
				// CondizioniVEPrezziExtra condAcq =
				// ricerca.ricercaPrezziExtra(this.getIdAzienda(),this.getIdCliente(),testata.getIdDivisione(),
				condAcq = ricerca.ricercaPrezziExtra(this.getIdAzienda(), this.getIdCliente(), testata.getIdDivisione(),
						causale.getContoTrasformazione(), // ...FIX04356
						// - DZ
						testata.getIdValuta(), this.getIdArticolo(), this.getIdConfigurazione(), this.getIdUMRif(),
						this.getIdUMPrm(), this.getQuantitaOrdinata().getQuantitaInUMRif(),
						this.getQuantitaOrdinata().getQuantitaInUMPrm(), TimeUtils.getDate(dataValid), null, null, null,
						false, this.getIdUMSec(), this.getQuantitaOrdinata().getQuantitaInUMSec());
			} catch (SQLException ex) {
				condAcq = null;
			}
			// fine fix 2821

			if (condAcq != null) {
				ricercaListini = false;
				DocOrdRigaPrezziExtra rigaPrezzi = this.getRigaPrezziExtra();
				if (rigaPrezzi == null) {
					this.istanziaRigaPrezziExtra();
				}
				rigaPrezzi = this.getRigaPrezziExtra();
				rigaPrezzi.aggiornaDatiDaCondVen(condAcq);
				// this.setPrezzo(condAcq.getPrezzoRiga()); Fix 08705
				this.setProvenienzaPrezzo(TipoRigaRicerca.CONTRATTO);
				// this.setRiferimentoUMPrezzo(condAcq.getRiferimentoUMPrezzo());
				// Fix 08705
				// Fix 08705 ini
				aggiornaDatiDaCondPrezziExtra(condAcq);
				// Fix 08705 fin

				if (this.getIdAgente() != null || this.getIdSubagente() != null) {
					CondizioniDiVendita cV = new CondizioniDiVendita();
					cV.setRArticolo(this.getIdArticolo());
					cV.setRSubAgente(this.getIdSubagente());
					cV.setRAgente(this.getIdAgente());
					cV.setRValuta(testata.getIdValuta());
					cV.setRUnitaMisura(this.getIdUMRif());
					cV.setRCliente(testata.getIdCliente());
					cV.setIdAzienda(this.getIdAzienda());
					cV.setRConfigurazione(this.getIdConfigurazione());
					cV.setDataValidita(dataValid);
					cV.setMaggiorazione(this.getMaggiorazione());
					cV.setSconto(this.getSconto());
					cV.setScontoArticolo1(this.getScontoArticolo1());
					cV.setScontoArticolo2(this.getScontoArticolo2());
					cV.setProvvigioneAgente1(this.getProvvigione1Agente());
					cV.setProvvigioneSubagente1(this.getProvvigione1Subagente());
					cV.setQuantita(this.getQtaInUMRif());
					cV.setPrezzo(condAcq.getPrezzoRiga());
					cV.setRModalitaPagamento(testata.getIdModPagamento());

					RicercaCondizioniDiVendita ric = new RicercaCondizioniDiVendita();
					ric.setCondizioniDiVendita(cV);
					ric.aggiornaProvvigioni();

					if (this.getIdAgente() != null) {
						this.setProvvigione2Agente(cV.getProvvigioneAgente2());
					}
					if (this.getIdSubagente() != null) {
						this.setProvvigione2Subagente(cV.getProvvigioneSubagente2());
					}

				}

			}
		}
		if (ricercaListini) {
			// Fine Fix 2333
			String idArticolo = getIdArticolo();
			// Integer idConfigurazione = getIdConfigurazione();
			BigDecimal quantVendita = getQtaInUMRif();
			BigDecimal quantMagazzino = getQtaInUMPrmMag();
			String idListino = getIdListino();
			char provenienzaPrezzo = getProvenienzaPrezzo();

			if (idArticolo != null && idArticolo.length() != 0 &&
			// idConfigurazione != null &&
			// quantVendita != null && quantMagazzino != null &&
			// idListino != null && idListino.length() != 0 &&
			// provenienzaPrezzo != TipoRigaRicerca.MANUALE) //Fix 20267
					quantVendita != null && quantMagazzino != null && provenienzaPrezzo != TipoRigaRicerca.MANUALE) // Fix
			// 20267
			{
				Trace.print("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§");
				Trace.print("§§§§§§§§§§§§§   CDV   §§§§§§§§§§§§§");
				Trace.print("§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§§");

				DecimalType decType = new DecimalType();
				DateType dateType = new DateType();

				recuperaCondizioniVendita(testata);

				if (condVen != null) {
					setListinoVendita(condVen.getListinoVendita());
					// Fix 18341 inizio
					if (condVen.getListinoVenditaScaglione() != null
							&& condVen.getListinoVenditaScaglione().getListinoVenditaRiga() != null
							&& condVen.getListinoVenditaScaglione().getListinoVenditaRiga()
									.getAssoggettamentoIVA() != null)
						this.setIdAssogIVA(condVen.getListinoVenditaScaglione().getListinoVenditaRiga()
								.getAssoggettamentoIVA().getIdAssoggettamentoIVA());
					// Fix 18341 fine
					setProvvigione2Agente(condVen.getProvvigioneAgente2());
					setProvvigione2Subagente(condVen.getProvvigioneSubagente2());
					// setScontoArticolo1(condVen.getScontoArticolo1());
					setScontoArticolo2(condVen.getScontoArticolo2());
					setMaggiorazione(condVen.getMaggiorazione());

					// Fix 80767 - Inizio

					// Evito che vengano settati i prezzi dalle condizioni di
					// vendita se non sono valorizzati
					if (condVen.getPrezzo() != null) {
						setPrezzo(condVen.getPrezzo());
						setPrezzoListino(condVen.getPrezzo());
					}

					if (condVen.getPrezzoExtra() != null)
						setPrezzoExtra(condVen.getPrezzoExtra());

					if (condVen.getPrezzoExtra() != null)
						setPrezzoExtraListino(condVen.getPrezzoExtra());

					// Fix 80767 - Fine

					// setSconto(condVen.getSconto());
					setRiferimentoUMPrezzo(condVen.getUMPrezzo());
					setProvenienzaPrezzo(condVen.getTipoTestata());

					// Fix 2456 - inizio
					setTipoRigaListino(condVen.getTipologiaRiga());
					// Fix 2456 - fine

					// ...FIX03085 - DZ
					if (condVen.getAzzeraScontiCliFor()) {
						setPrcScontoIntestatario(new BigDecimal("0"));
						setPrcScontoModalita(new BigDecimal("0"));
						setScontoModalita(null);
					}
					// ...fine FIX03085 - DZ

					// Fix 3910 - inizio
					setQuantitaMinListino(condVen.getQuantitaMin());
					// Fix 3910 - fine

					// fix 11156
					this.setNumeroImballo(condVen.getNumeroImballo());
					// fine fix 11156

					/*
					 * AssoggettamentoIVA assIVA = condVen.getListinoVenditaScaglione().
					 * getListinoVenditaRiga().getAssoggettamentoIVA(); if (assIVA != null) {
					 * setAssoggettamentoIVA(assIVA); }
					 */
				}

			}
			// Fix 2333
		}
		// Fine 2333
	}
	// Fix 80767 - Fine

	public void settaAltezzaMultiplaV2(Articolo art, VtArticoliTex articTex) {
		String umM2 = ParametroPsn.getValoreParametroPsn("tessile.umH70", "H70");
		String umY2 = ParametroPsn.getValoreParametroPsn("tessile.umH70Y", "H70Y");
		String umMT = ParametroPsn.getValoreParametroPsn("tessile.umH140", "H140");
		String umYD = ParametroPsn.getValoreParametroPsn("tessile.umH140Y", "H140Y");

		if (this.getIdUMRif() != null) {
			if (this.getIdUMRif().equals(umY2) || this.getIdUMRif().equals(umM2))
				this.setAltezzaMultipla('1');

			if (this.getIdUMRif().equals(umYD) || this.getIdUMRif().equals(umMT)) {
				if (articTex != null && articTex.getNumeroTeli() != null)
					this.setAltezzaMultipla(articTex.getNumeroTeli().toString().charAt(0));
				else
					this.setAltezzaMultipla('1'); // Fix 80970
				// this.setAltezzaMultipla('0');
			}

		}
		// Fix 80951
		else {
			this.setAltezzaMultipla('0');
		}
	}

	//
	public void settaAltezzaMultipla(Articolo art, VtArticoliTex articTex) {

		// if(!this.isOnDB()) //solo in insert
		// {
		if (articTex != null && articTex.getNumeroTeli().compareTo(new BigDecimal(2)) < 0) {
			this.setAltezzaMultipla('1');
		} else {
			if (/* this.getUMRif()!=null && */ this.getIdUMRif() != null && art.getUMDefaultVendita() != null) {
				if (this.getIdUMRif().equals(art.getUMDefaultVendita().getIdUnitaMisura())) {
					this.setAltezzaMultipla('1');
				} else {
					if (articTex != null && articTex.getNumeroTeli() != null)
						this.setAltezzaMultipla(articTex.getNumeroTeli().toString().charAt(0));
				}

			} else {
				if (articTex != null && articTex.getNumeroTeli() != null)
					this.setAltezzaMultipla(articTex.getNumeroTeli().toString().charAt(0));
			}
		}
		/*
		 * if (this.getIdUMRif() != null) {
		 * 
		 * if(this.getIdUMRif().equals("M2") || this.getIdUMRif().equals("Y2") )
		 * //if(this.getUMRif().getIdUnitaMisura().equals("M2") ||
		 * this.getUMRif().getIdUnitaMisura().equals("Y2") ) {
		 * this.setAltezzaMultipla('1'); } else { this.setAltezzaMultipla('2'); }
		 * 
		 * }
		 */
		// }
	}

	// Fix 80951 - Inizio
	public boolean esisteArticoloCliente() throws SQLException {

		boolean esiste;

		String selectArtCli = "SELECT A.ID_NUMERATORE FROM THIP.VT_ARTICOLI_CLI AS A, THIP.ARTICOLI_CLI AS B"
				+ " WHERE A.ID_AZIENDA=B.ID_AZIENDA AND A.ID_NUMERATORE=B.ID_NUMERATORE AND A.ID_AZIENDA='"
				+ this.getIdAzienda() + "' ";

		// Fix 81763
		String where = "";
		if (getArticoloCli() != null)
			where = where + " AND B.ARTICOLO_CLI='" + getArticoloCli() + "' ";
		else
			where = where + "AND B.ARTICOLO_CLI IS NULL ";

		if (getRStagione() != null)
			where = where + " AND A.STAGIONE='" + this.getRStagione() + "' ";
		else
			where = where + " AND A.STAGIONE IS NULL ";

		// 14-01-2020 selectArtCli = selectArtCli + where + " AND R_ARTICOLO='" +
		// this.getIdArticolo() + "' AND B.R_CLIENTE='" + this.getIdCliente() + "'";
		// 14-01-2020 >
		selectArtCli = selectArtCli + where /* + " AND R_ARTICOLO='" + this.getIdArticolo() */ + " AND B.R_CLIENTE='"
				+ this.getIdCliente() + "'";

		/*
		 * Fix 81763 String selectArtCli =
		 * "SELECT A.ID_NUMERATORE FROM THIP.VT_ARTICOLI_CLI AS A, THIP.ARTICOLI_CLI AS B"
		 * +
		 * " WHERE A.ID_AZIENDA=B.ID_AZIENDA AND A.ID_NUMERATORE=B.ID_NUMERATORE AND A.ID_AZIENDA='"
		 * + this.getIdAzienda() + "' AND B.ARTICOLO_CLI='" + getArticoloCli() + "'" +
		 * " AND A.STAGIONE='" + this.getRStagione() + "' AND A.VARIANTE='" +
		 * this.getVarianteArtcli() + "' AND R_ARTICOLO='" + this.getIdArticolo() +
		 * "' AND B.R_CLIENTE='" + this.getIdCliente() + "'";
		 */

		String idNumeratore = "";

		CachedStatement sqlArtCli = new CachedStatement(selectArtCli);
		ResultSet rsArtCli = sqlArtCli.executeQuery();
		while (rsArtCli.next()) {
			idNumeratore = rsArtCli.getString(1);
		}
		rsArtCli.close();

		/*
		 * if(idNumeratore!=null) { if(idNumeratore.equals("")) { esiste=false; } else
		 * esiste = true; } else esiste= true;
		 */

		if (idNumeratore.equals("")) {
			esiste = false;
		} else {
			esiste = true;
		}

		return esiste;
	}
	// Fix 80951 - Fine

	// Gestione griglia pezze figlie
	protected PersistentCollection iVtPezza = new PersistentCollection(VtPezza.class, "", "", "retrieveList");

	public Vector getVtPezza() {

		// Fix 80992 - Inizio
		/*
		 * if (isOnDB()) { iVtPezza.setWhereClause(VtPezzaTM.ID_AZIENDA + " = '" +
		 * getIdAzienda()+ "' " + " AND " + VtPezzaTM.R_ANNO_ORD_VEN + " = '" +
		 * this.getAnnoDocumento() + "' " + " AND " + VtPezzaTM.R_NR_ORD_VEN + " = '" +
		 * this.getNumeroDocumento() + "' " + " AND " + VtPezzaTM.R_RIGA_ORD_VEN + " = "
		 * + this.getNumeroRigaDocumento() + " AND " + VtPezzaTM.R_DET_RIGA_ORD_VEN +
		 * " = 0 ", true);
		 * 
		 * return iVtPezza.getElements(); } else return new Vector();
		 */

		return new Vector();
		// Fix 80992 - Fine
	}

	// Fix 80976 - Inizio
	public void saveClasseMaterialeSuArticolo(VtArticolo art, String nDoganale) {
		if (art != null) {
			art.setIdClasseMateriale(nDoganale);
			/*
			 * List lstAvail = art.getArticoloDatiVendita().getUMSecondarieAvailable(); List
			 * lstElem = art.getArticoloDatiVendita().getUMSecondarieElements(); List
			 * lstUMSec = art.getArticoloDatiVendita().getUMSecondarie(); List prova =
			 * art.getUMSecondarieVendita(); List lstSpec = art.getUMSpecifiche(); List
			 * lstUMList = art.getUnitaMisuraList(); System.out.println("a");
			 */
			// 09-02-2018
			List lstUMSec = art.getArticoloDatiVendita().getUMSecondarie();
			VtTessileUtil.saveVtArticolo(art);
		}
	}
	// Fix 80976 - Fine

	// Fix 80971 - Inizio
	public void componiChiave() {
		Integer numRiga = this.getNumeroRigaDocumento();
		Integer zero = new Integer(0);
		super.componiChiave();
		// if (numRiga!=null && numRiga.compareTo(zero)!=0)
		// Fix 81292 - Aggiungo controllo per la copia riga. Non devo settare il numero
		// riga se sono in copia per evitare errore
		if (numRiga != null && numRiga.compareTo(zero) != 0 && !this.isInCopiaRiga)
			this.setNumeroRigaDocumento(numRiga);
	}
	// Fix 80971 - Fine

	// Fix 81091 - Inizio

	public int eseguiOperazoniPreOrdine() throws SQLException {

		VtArticolo artFinito = creaArticoloFinito();
		if (artFinito != null) {
			// Adesso devo salvare l'articolo presente in RArticolo nel campo esteso del
			// modello
			// e l'articolo finito lo devo andare a salvare in nell'articolo standard
			this.setArticoloFin(this.getArticolo());

			this.setArticolo(artFinito);

			// Fix 81265 - Inizio
			this.setRArtOrdinato(artFinito.getIdArticolo());
			// Fix 81265 - Fine

			eseguoAggiornamentiOrdine();

			/*
			 * Salvo le quantit?el tessuto nei campi delle quantit?el finito in quanto il
			 * modello viene sostituito dal tessuto
			 */

			// Fix 81111
			// Commento in quanto vado a settarli nel metodo "eseguoAggiornamentiOrdine()"
			/*
			 * this.setUMFin(this.getUMRif()); this.setQtaVenFinito(this.getQtaInUMRif());
			 * 
			 * this.setUmPrmFinito(this.getIdUMPrm());
			 * this.setQtaPrmFinito(this.getQtaInUMPrmMag());
			 * 
			 * if(this.getIdUMSec() != null && this.getQtaInUMSecMag() != null){
			 * this.setUmSecFinito(this.getIdUMSec());
			 * this.setQtaSecFinito(this.getQtaInUMSecMag()); }
			 */

			this.setDescrizioneArticolo(artFinito.getDescrizioneArticoloNLS().getDescrizione());

			/*
			 * Fix 81125 - Creo adesso l'articolo cliente in quanto per il pre ordine
			 * modello qui viene creato il prodotto finito, quindi l'articolo cliente deve
			 * essere intestato al prodotto finito e non al tessuto iniziale
			 */
			operazioniArticoloCliente();
		}
		return super.save();
	}

	public VtArticolo creaArticoloFinito() {
		VtArticolo artFin = null;

		String idAzienda = this.getIdAzienda();
		String idAnno = this.getAnnoDocumento();
		String idNumOrd = this.getNumeroDocumento();// per prima cosa cancello eventuali etichette aperte per l'ordine
													// sul file di WORK
		try {
			cancellaPrimaEtichetteAperte(idAzienda, idAnno, idNumOrd);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		VtArticolo artT = (VtArticolo) this.getArticolo();

		VtArticolo artF = (VtArticolo) this.getArticoloFin();

		if (artT != null && artF != null) {
			// in base allo schema dei 2 articoli (tessuto e modello) ricavo lo schema e il
			// prodotto risultante

			try {
				artFin = elaboraSchemiArticoli(artT, artF, idAzienda);

				// Fix 81120 - Aggiungo al nuovo articolo anche le fibre se sono presenti sul
				// tessuto di partenza
				if (artFin != null) {
					VtArticoliTex artFinTex = (VtArticoliTex) PersistentObject.elementWithKey(VtArticoliTex.class,
							artFin.getKey(), VtArticoliTex.NO_LOCK);

					VtArticoliTex artTTex = (VtArticoliTex) PersistentObject.elementWithKey(VtArticoliTex.class,
							artT.getKey(), VtArticoliTex.NO_LOCK);

					if (artFinTex != null && artTTex != null && artTTex.getVtFibraArt() != null
							&& !artTTex.getVtFibraArt().isEmpty()) {
						artFinTex.getVtFibraArt().clear();
						Iterator iter = artTTex.getVtFibraArt().iterator();
						while (iter.hasNext()) {
							VtFibraArt fibra = (VtFibraArt) iter.next();

							// Cerco se esiste gi� una fibra con stesso id ma articolo = all'articolo finito
							// Se non esiste lo creo in copia
							String keyFibraNew = KeyHelper.replaceTokenObjectKey(fibra.getKey(), 2,
									artFinTex.getIdArticolo());
							VtFibraArt fibraNew = (VtFibraArt) PersistentObject.elementWithKey(VtFibraArt.class,
									keyFibraNew, VtFibraArt.NO_LOCK);
							if (fibraNew != null) {
								artFinTex.getVtFibraArt().add(fibraNew);
							} else {
								fibraNew = (VtFibraArt) Factory.createObject(VtFibraArt.class);
								fibraNew.setIdAzienda(fibra.getIdAzienda());
								fibraNew.setIdArticolo(artFinTex.getIdArticolo());
								fibraNew.setIdFibra(fibra.getIdFibra());
								fibraNew.setPercComponente(fibra.getPercComponente());

								/*
								 * ConnectionManager.pushConnection(); int rc = fibraNew.save(); if(rc >= 0)
								 * ConnectionManager.commit(); else ConnectionManager.rollback();
								 * ConnectionManager.popConnection();
								 * 
								 * if(rc >= 0){ fibraNew.retrieve();
								 */
								artFinTex.getVtFibraArt().add(fibraNew);
								// }

							}
						}
						ConnectionManager.pushConnection();
						int rc = artFinTex.save();
						if (rc >= 0)
							ConnectionManager.commit();
						else
							ConnectionManager.rollback();
						ConnectionManager.popConnection();
					}
				}
				// if(artT.getfib)
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// scrivo varianti righe ordini per il settaggio previsto (e lo stesso prodotto
			// fino alla variante, esclusa)
			try {
				creaEtichetteRighe(this);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return artFin;
	}

	/**
	 * Cancellazione iniziale delle eventuali etichette esistenti per l'ordine
	 * scelto
	 * 
	 * @param azi
	 * @param ann
	 * @param num
	 * @throws SQLException
	 */
	public void cancellaPrimaEtichetteAperte(String azi, String ann, String num) throws SQLException {

		String where = null;
		// griglia varianti legate alla riga per anno/numero ordine/prodotto fino a
		// variante, e settaggio

		where = VtOrdVenRigEtiTM.ID_AZIENDA + "='" + azi + "' AND " + VtOrdVenRigEtiTM.ID_ANNO_ORD + "='" + ann
				+ "' AND " + VtOrdVenRigEtiTM.ID_NUMERO_ORD + "='" + num + "' ";

		String orderBy = VtOrdVenRigEtiTM.ID_ETICHETTA;
		Vector etichette = null;

		try {
			etichette = VtOrdVenRigEti.retrieveList(where, orderBy, false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		// rileggo varianti e cancello quelle aperte relative all'ordine
		Iterator iter = etichette.iterator();
		while (iter.hasNext()) {
			VtOrdVenRigEti eti = (VtOrdVenRigEti) iter.next();

			ConnectionManager.pushConnection();

			// Aggiorno riferimento riga ordine generata su varianti

			// aggancio variante per cancellarla e poi ricrearla con nuova chiave con riga
			// ordine
			String keyVarAgg = eti.getKey();
			VtOrdVenRigEti etiAgg = null;
			try {
				etiAgg = (VtOrdVenRigEti) PersistentObject.elementWithKey(VtOrdVenRigEti.class, keyVarAgg,
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (etiAgg != null) {
				int rcs = etiAgg.delete();
				if (rcs > 0) {
					ConnectionManager.commit();
					Trace.excStream.println("Pulizia iniziale etichette (commit): " + ann + "/" + num + " eti: "
							+ etiAgg.getIdEtichetta() + " cod.ritorno: " + rcs);
				} else {
					ConnectionManager.rollback();
					Trace.excStream.println("Pulizia iniziale etichette (rollback): " + ann + "/" + num + " eti: "
							+ etiAgg.getIdEtichetta() + " cod.ritorno: " + rcs);
				}
			}
			ConnectionManager.popConnection();

		}

	}

	/**
	 * Eseguo scrittura o aggiornamento VARIANTE
	 * 
	 * @param rig
	 * @throws SQLException
	 */
	public VtArticolo elaboraSchemiArticoli(Articolo artTex, Articolo artFin, String idAzienda) throws SQLException {

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
			String schemaCf = sa.getIdScheCmb();
			String keyStc = KeyHelper.buildObjectKey(new String[] { idAzienda, String.valueOf(tipo), schemaCf });
			SchemaProdottoLotto scf = null;
			try {
				scf = SchemaProdottoLotto.elementWithKey(keyStc, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			List chiaF = new ArrayList(); // chiavi schema finito
			List chiaT = new ArrayList(); // chiavi schema tessuto
			List chiaC = new ArrayList(); // chiavi schema CONFEZIONE (schema abbinato)
			List chiaX = new ArrayList(); // chiavi finito + tessuto (per controllo)

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
				valoF.add(ricavaValoreChiave(chia, artFin));
				chiaX.add(chia);
				nomeX.add(chia.getIdChiaveProdotto());
				valoX.add(ricavaValoreChiave(chia, artFin));
			}
			// Ciclo caricamento chiavi del Tessuto (su list di confronto)
			Iterator iteT = chiaT.iterator();
			while (iteT.hasNext()) {
				ChiaveProdotto chia = (ChiaveProdotto) iteT.next();
				nomeT.add(chia.getIdChiaveProdotto());
				valoT.add(ricavaValoreChiave(chia, artTex));
				chiaX.add(chia);
				nomeX.add(chia.getIdChiaveProdotto());
				valoX.add(ricavaValoreChiave(chia, artTex));
			}

			// Ciclo caricamento chiavi ABBINATE (schema abbinato)
			Iterator iteC = chiaC.iterator();
			while (iteC.hasNext()) {
				ChiaveProdotto chia = (ChiaveProdotto) iteC.next();
				nomeC.add(chia.getIdChiaveProdotto());
				indcC.add(String.valueOf(chia.getCampoChiave()));
			}

			// -----------------------------------------------------------------------------------------------------------------------
			// Come creare il prodotto combinato
			//
			// esempio: schema MODELLO schema TESSUTO schema TOTALE schema abbinato
			// CONFEZIONE (valori tra parentesi)
			// (non reale) MODELLO ARTICOLO MODELLO (A) MODELLO (A)
			// ETICHETTA DISTES ETICHETTA (01) VARMOD (SS)
			// VARMOD VARTES VARMOD (SS) ETICHETTA (01)
			// LAV ARTICOLO (ABC) ARTICOLO (ABC)
			// PROVA DISTES (DIX) LAV (AAA)
			// VARTES (V01) DISTES (DIX)
			// LAV (AAA) VARTES (V01)
			// PROVA (XXX)
			//
			// - nell'esempio si sono volutamente modificate le sequenze campi degli schemi
			// modello/tessuto
			//
			//
			// - i valori dello schema totale vengono presi dallo schema TOTALE e messi
			// nei corrispondenti dello schema CONFEZIONE pur avendo sequenze diverse
			//
			// -----------------------------------------------------------------------------------------------------------------------

			// se almeno una chiave prodotto dello schema "Confezione" non esiste nel totale
			// chiavi schema Modello + Tessuto
			// allora segnalo incongruenza
			int i = 0;
			idProd = formattaValore(2, scf.getId()); // schema prodotto
			Iterator iteXX = nomeC.iterator();
			// Ciclo dei nomi chiavi della confezione (i = indice dello schema "Confezione",
			// ix = indice dello schema totale modello+tessuto)
			String n = null;
			while (iteXX.hasNext()) {
				// nome della chiave schema "Confezione"
				String nome = (String) iteXX.next();
				// cerco l'indice del nome sullo schema modello+tessuto (per poi reperire il
				// valore)
				if (nome != null && !nome.equals("")) {
					int ix = nomeX.indexOf(nome);
					if (ix >= 0) {
						// ricavo il valore della chiave sullo schema totale (modello+tessuto)
						String valoreX = (String) valoX.get(ix); // valore della chiave su schema totale
																	// (modello+tessuto)
						// carico array dei valori per lo schema CONFEZIONE (nella sequenza schema
						// CONFEZIONE)
						valoC.add(valoreX);
					} else {
						valoC.add(n);
					}
				}
			}

			Iterator iteVAL = valoC.iterator();
			i = 0;
			// Ciclo dei nomi chiavi della confezione (i = indice dello schema "Confezione",
			// ix = indice dello schema totale modello+tessuto)
			while (iteVAL.hasNext()) {
				String nome = (String) iteVAL.next();
				ChiaveProdotto ccc = (ChiaveProdotto) chiaC.get(i);

				idProd += formattaValore(ccc.getLunghezza().intValue(), nome);
				i++;
			}

			String iA = null;

			// 15-03-2018 devo intervenire qua: se sono un ordine intercompany devo
			// verificare se esiste quel prodotto in tutte le aziende

			// 15-03-2018 iA =
			// VtArticoloGetChiaveProdotto.getIdArticoloFromIdProdotto(idAzienda, idProd);
			String key = "";
			key = getIdArticoloFromIdProdotto(idAzienda, idProd);
			if (!key.equals(""))
				iA = key.split(KeyHelper.KEY_SEPARATOR)[0];

			CausaleRigaOrdVen cauRigaOrdVen = (CausaleRigaOrdVen) this.getCausaleRiga();

			if (iA != null && !iA.equals("") && cauRigaOrdVen != null && cauRigaOrdVen.isGestioneIntercompany()) {

				Articolo articoloAzDaCopia = (Articolo) PersistentObject.elementWithKey(Articolo.class,
						key.split(KeyHelper.KEY_SEPARATOR)[1] + KeyHelper.KEY_SEPARATOR + iA, Articolo.NO_LOCK);

				VtArticolo articoloP = null;
				String whereArticolo = ArticoloTM.ID_AZIENDA + "=" + Azienda.getAziendaCorrente() + " AND "
						+ ArticoloTM.ID_PRODOTTO + "=" + "'" + articoloAzDaCopia.getIdSchemaProdotto().trim() + "'";
				List articoliList = new ArrayList();

				try {
					articoliList = Articolo.retrieveList(whereArticolo, ArticoloTM.ID_ARTICOLO, true);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Iterator list = articoliList.iterator();
				while (list.hasNext()) {
					articoloP = (VtArticolo) list.next();
				}

				VtUtilVenditeNew.creaArticoloAzCorrente(articoloP, articoloAzDaCopia);

			}

			if (iA == null || iA.equals("")) {
				Trace.excStream.println("(getIdArticoloFromIdProdotto) non trovato articolo per il prodotto: " + idProd
						+ " viene creato nuovo articolo.");
			} else {
				Trace.excStream
						.println("(getIdArticoloFromIdProdotto) trovato articolo: " + iA + "  da prodotto: " + idProd);
			}

			// se articolo con la nuova variante non esiste, cerco di aprirlo partendo da
			// quello iniziale
			if (iA == null || iA.equals("")) {
				ConnectionManager.pushConnection();
				try {
					artNew = creaArticoloDaCopia(artFin, idProd, scf.getId(), scf.getTipo());
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (artNew != null) {
					Trace.excStream.println("(creaArticoloDaCopia) articolo partenza : " + artFin.getIdArticolo()
							+ " articolo nuovo: " + artNew.getIdArticolo() + " nuovo prodotto: " + idProd);
				} else {
					Trace.excStream.println("(creaArticoloDaCopia) NON RIUSCITO !  articolo partenza : "
							+ artFin.getIdArticolo() + " nuovo prodotto: " + idProd);
				}

				// se duplicato articolo: aggiorno il tipo in KIT e restituisco il valore
				// dell'articolo
				if (artNew != null) {
					// ConnectionManager.pushConnection();
					// riaggancio il nuovo articolo
					Articolo artNew2 = null;
					String keyAr22 = KeyHelper.buildObjectKey(new String[] { idAzienda, artNew.getIdArticolo() });
					try {
						artNew2 = Articolo.elementWithKey(keyAr22, PersistentObject.NO_LOCK);
					} catch (SQLException e) {
						e.printStackTrace();
					}
					if (artNew2 != null) {
						// artNew.retrieve();
						// >>> qui inserire la valorizzazione dei campi chiave articolo (getIndiceChiave
						// per recuperare l'indice indcC/posiC) poi usare valoX per i valori
						// artNew2.setTipoParte(ArticoloDatiIdent.KIT_NON_GEST);
						// Fix 80957
						artNew2.setTipoParte(ArticoloDatiIdent.PROD_FINITO);
						int rr = artNew2.save();
						if (rr > 0) {
							Trace.excStream
									.println("(creaArticoloDaCopia) commit : " + rr + " nuovo prodotto: " + idProd);
							ConnectionManager.commit();

						} else {
							Trace.excStream.println("(creaArticoloDaCopia) rollback : " + rr + " errore ");
							ConnectionManager.rollback();
						}
					}
					ConnectionManager.popConnection();

					return (VtArticolo) artNew;
				}

			} else {
				String[] keyArtF = { idAzienda, iA };
				String keyArF = KeyHelper.buildObjectKey(keyArtF);
				VtArticolo artF = null;
				try {
					artF = (VtArticolo) VtArticolo.elementWithKey(VtArticolo.class, keyArF, PersistentObject.NO_LOCK);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return (VtArticolo) artF;
			}

		}

		return null;
	}

	/**
	 * Ridefinita perch?ella CONFERMA pre-ordine non vanno caricate varianti, ma
	 * solo Etichette
	 */
	public void creaEtichetteRighe(VtOrdineVenditaRigaPrm rig)
			throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		// >>>> ATTENZIONE: le etichette su VtOrdVenRigEti vengono scritte in immissione
		// e continuano ad esistere fino al passaggio in produzione
		// questo perch?on riuscirei a rigenerarle partendo dalla riga tessuto dato che
		// non ho agganci tra la riga del tessuto e le
		// righe ordini di finito generate per il pre-ordine tessuto
		VtArticolo art = (VtArticolo) rig.getArticolo();

		// leggo righe ordini secondarie associate all'ordine primario di pre-ordine
		// (solo secondarie con dett > 0 e riga preordine = riga del preordine
		String where = OrdineVenditaRigaTM.ID_AZIENDA + "='" + rig.getIdAzienda() + "' AND "
				+ OrdineVenditaRigaTM.ID_ANNO_ORD + "='" + rig.getAnnoDocumento() + "' AND "
				+ OrdineVenditaRigaTM.ID_NUMERO_ORD + "='" + rig.getNumeroDocumento() + "' AND "
				+ OrdineVenditaRigaTM.ID_DET_RIGA_ORD + "> 0  ";
		// OrdineVenditaRigaTM.RIGA_PREORD + "= " + rig.getNumeroRigaDocumento() + " ";
		String orderBy = OrdineVenditaRigaPrmTM.ID_AZIENDA;
		List righe = new ArrayList();
		righe = VtOrdineVenditaRigaSec.retrieveList(VtOrdineVenditaRigaSec.class, where, orderBy, false);

		Iterator ite = righe.iterator();

		// leggo righe ordini dell'ordine in questione
		while (ite.hasNext()) {
			VtOrdineVenditaRigaSec rigord = (VtOrdineVenditaRigaSec) ite.next();
			// solo per le righe secondarie associate alla riga primaria pre-ordine
			if (rigord.getRigaPreord().equals(rig.getNumeroRigaDocumento())) {
				// scrivo etichetta passando riga secondaria e riga primaria del pre-ordine
				scrivoEtichetta(rigord, rig);
			}

		}

	}

	/**
	 * In base alla chiave prodotto e all'articolo, restituisco il valore relativo
	 * alla chiave preso dalla relativa posizione
	 * 
	 * @param chi
	 * @param art
	 * @return
	 */
	public static String ricavaValoreChiave(ChiaveProdotto chi, Articolo art) {
		char ccc = chi.getCampoChiave(); // carattere corrispondente al campo chiave (facoltativo) 1, 2, 3, 4, 5, 6, 7,
											// 8, 9, A, B, C
		int pos = chi.getPosizione().intValue(); // posizione del campo chiave (se non indicato campoChiave qui sopra)

		String valKey = null;
		int posizione = 0;
		// verifico posizione chiave da campo "Campo Chiave"
		if (ccc != '0') {
			switch (ccc) {
			case '1':
				posizione = 1;
				break;
			case '2':
				posizione = 2;
				break;
			case '3':
				posizione = 3;
				break;
			case '4':
				posizione = 4;
				break;
			case '5':
				posizione = 5;
				break;
			case '6':
				posizione = 6;
				break;
			case '7':
				posizione = 7;
				break;
			case '8':
				posizione = 8;
				break;
			case '9':
				posizione = 9;
				break;
			case 'A':
				posizione = 10;
				break;
			case 'B':
				posizione = 11;
				break;
			case 'C':
				posizione = 12;
				break;
			// Fix 80640 inizio
			case 'D':
				posizione = 13;
				break;
			case 'E':
				posizione = 14;
				break;
			case 'F':
				posizione = 15;
				break;
			case 'G':
				posizione = 16;
				break;
			case 'H':
				posizione = 17;
				break;
			case 'I':
				posizione = 18;
				break;
			case 'L':
				posizione = 19;
				break;
			case 'M':
				posizione = 20;
				break;
			case 'N':
				posizione = 21;
				break;
			case 'O':
				posizione = 22;
				break;
			case 'P':
				posizione = 23;
				break;
			case 'Q':
				posizione = 24;
				break;
			case 'R':
				posizione = 25;
				break;
			// Fix 80640 fine
			}
		}
		// se l'indice "Campo Chiave" ?ERO allora utilizzo il campo "Posizione" come
		// posizione della chiave
		if (posizione == 0) {
			posizione = pos;
		}

		switch (posizione) {
		case 1:
			valKey = art.getChiaveProdotto1();
			break;
		case 2:
			valKey = art.getChiaveProdotto2();
			break;
		case 3:
			valKey = art.getChiaveProdotto3();
			break;
		case 4:
			valKey = art.getChiaveProdotto4();
			break;
		case 5:
			valKey = art.getChiaveProdotto5();
			break;
		case 6:
			valKey = art.getChiaveProdotto6();
			break;
		case 7:
			valKey = art.getChiaveProdotto7();
			break;
		case 8:
			valKey = art.getChiaveProdotto8();
			break;
		case 9:
			valKey = art.getChiaveProdotto9();
			break;
		case 10:
			valKey = art.getChiaveProdotto10();
			break;
		case 11:
			valKey = art.getChiaveProdotto11();
			break;
		case 12:
			valKey = art.getChiaveProdotto12();
			break;
		// Fix 80640 inizio
		case 13:
			valKey = art.getChiaveProdotto13();
			break;
		case 14:
			valKey = art.getChiaveProdotto14();
			break;
		case 15:
			valKey = art.getChiaveProdotto15();
			break;
		case 16:
			valKey = art.getChiaveProdotto16();
			break;
		case 17:
			valKey = art.getChiaveProdotto17();
			break;
		case 18:
			valKey = art.getChiaveProdotto18();
			break;
		case 19:
			valKey = art.getChiaveProdotto19();
			break;
		case 20:
			valKey = art.getChiaveProdotto20();
			break;
		case 21:
			valKey = art.getChiaveProdotto21();
			break;
		case 22:
			valKey = art.getChiaveProdotto22();
			break;
		case 23:
			valKey = art.getChiaveProdotto23();
			break;
		case 24:
			valKey = art.getChiaveProdotto24();
			break;
		case 25:
			valKey = art.getChiaveProdotto25();
			break;
		// Fix 80640 fine
		}

		return valKey;
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
	 * Eseguo scrittura o aggiornamento VARIANTE
	 * 
	 * @param rig
	 * @throws SQLException
	 */
	public void scrivoEtichetta(VtOrdineVenditaRigaSec rig, VtOrdineVenditaRigaPrm rigPre) throws SQLException {
		VtArticolo art = (VtArticolo) rig.getArticolo(); // articolo finito della riga secondaria

		// preparazione scrittura Variante
		char tipologia = art.getTipologiaProdotto();
		String idAzienda = art.getIdAzienda();
		String schema = art.getIdSchemaProdotto();
		char tipo = 'P';

		// leggo chiavi schema prodotto per ricavare il codice ETICHETTA dal prodotto
		// finito della riga secondaria
		String where = ChiaveProdottoTM.R_SCHEMA_PRODOTTO + "='" + schema + "' AND " + ChiaveProdottoTM.ID_AZIENDA
				+ "='" + idAzienda + "' AND " + ChiaveProdottoTM.R_TIPO + "='" + tipo + "' ";
		String orderBy = ChiaveProdottoTM.POSIZIONE;
		List chiavi = new ArrayList();
		try {
			chiavi = ChiaveProdotto.retrieveList(where, orderBy, false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		Iterator ite = chiavi.iterator();
		String xEti = null;
		int xIEti = 0;

		while (ite.hasNext()) {
			ChiaveProdotto chia = (ChiaveProdotto) ite.next();
			String tab = chia.getIdTabella(); // tabella utilizzata (nome descrittore)
			char ccc = chia.getCampoChiave(); // carattere corrispondente al campo chiate (facoltativo) 1, 2, 3, 4, 5,
												// 6, 7, 8, 9, A, B, C
			int pos = chia.getPosizione().intValue(); // posizione del campo chiave (se non indicato campoChiave qui
														// sopra)
			String[] strX = null; // stringa array contenente: elemento 0 il valore chiave, elemento 1 l'indice
									// chiave (formato stringa)

			// ignoro chiavi senza tabella
			if (tab == null)
				continue;
			// disegno tessuto (per tipologie R/J/M)

			if (tab.equals("VtEtichetta")) {
				// in base al "CampoChiave" e alla "PosizioneChiave" calcola l'esatta posizione
				// della chiave prodotto e ne restituisce il valore
				strX = VtFillOrdVenRigVar.ricavaValoreChiave(pos, ccc, art);
				xEti = strX[0];
				if (strX[1] != null)
					xIEti = new Integer(strX[1]).intValue();
			}

		}

		// eseguo apertura etichette solo se trovata l'etihetta
		if (xEti != null) {

			// cerco se variante esiste (se sono in immissione riga ordine lascio a zero
			// RIGA e DETTAGLIO RIGA)
			Integer rigaOrd = new Integer(0);
			Integer dettRigaOrd = new Integer(0);

			if (rig != null) {
				// in modifica ho sempre la riga ordine sulle varianti per collegarla
				rigaOrd = rigPre.getNumeroRigaDocumento();
				dettRigaOrd = rigPre.getDettaglioRigaDocumento();
			}

			String vtVarKey = KeyHelper.buildObjectKey(new String[] { rig.getIdAzienda(), rig.getAnnoDocumento(),
					rig.getNumeroDocumento(), rigaOrd.toString(), dettRigaOrd.toString(), xEti });
			VtOrdVenRigEti rv = null;
			try {
				rv = VtOrdVenRigEti.elementWithKey(vtVarKey, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Nuova riga: SCRIVO ---------------------------------------------------
			if (rv == null) {
				VtOrdVenRigEti rigEtiNew = (VtOrdVenRigEti) Factory.createObject(VtOrdVenRigEti.class);
				rigEtiNew.setIdAzienda(idAzienda);
				rigEtiNew.setIdAnnoOrd(rigPre.getAnnoDocumento());
				rigEtiNew.setIdNumeroOrd(rigPre.getNumeroDocumento());
				rigEtiNew.setIdEtichetta(xEti);

				String etiKey = KeyHelper.buildObjectKey(new String[] { idAzienda, xEti });
				VtEtichetta ee = null;
				try {
					ee = VtEtichetta.elementWithKey(etiKey, PersistentObject.NO_LOCK);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				// riferimenti riga del pre-ordine (chiave principale per aggancio etichette)
				rigEtiNew.setIdRigaOrd(rigPre.getNumeroRigaDocumento());
				rigEtiNew.setIdDetRigaOrd(rigPre.getDettaglioRigaDocumento());

				// riferimenti su etichetta della riga ordine secondaria di appartenenza (se
				// ZERO sono righe nuove, non in questo caso)
				rigEtiNew.setRigaOrdRif(rig.getNumeroRigaDocumento());
				rigEtiNew.setDetRigaOrdRif(rig.getDettaglioRigaDocumento());

				rigEtiNew.setDescrizioneEti(ee.getDescrizione().getDescrizione());

				rigEtiNew.setRArticolo(art.getIdArticolo());
				rigEtiNew.setArticolo(art);

				rigEtiNew.setQuantitaVendita(rig.getQtaInUMRif());

				if (rig.getUmVenFinito() != null) {
					rigEtiNew.setUmVendita(rig.getUmVenFinito());
				}

				int rrr = rigEtiNew.save();
				if (rrr < 0) {
					ConnectionManager.rollback();
					Trace.excStream.println("(scrivoEtichetta - FormModifier) scrivo variante ROLLBACK: "
							+ rigEtiNew.getKey() + " cod.ritorno: " + rrr);
				} else {
					ConnectionManager.commit();
					Trace.excStream.println("(scrivoEtichetta - FormModifier) scrivo variante COMMIT: "
							+ rigEtiNew.getKey() + " cod.ritorno: " + rrr);
					// return true;
				}

			}
			// Riga esistente: AGGIORNO ---------------------------------------------
			else {
				// se gestito articolo finito lo valorizzo

				rv.setQuantitaVendita(rig.getQtaInUMRif());

				if (rig.getUmVenFinito() != null) {
					rv.setUmVendita(rig.getUmVenFinito());
				}

				int rrr = rv.save();
				if (rrr < 0) {
					ConnectionManager.rollback();
					Trace.excStream.println("(scrivoEtichetta - FormModifier) aggiornamento variante ROLLBACK: "
							+ rv.getKey() + " cod.ritorno: " + rrr);
				} else {
					ConnectionManager.commit();
					Trace.excStream.println("(scrivoEtichetta - FormModifier) aggiornamento variante COMMIT: "
							+ rv.getKey() + " cod.ritorno: " + rrr);
				}
			}
		}
	}

	/**
	 * Imposta descrizione articolo da ID_PRODOTTO
	 * 
	 * @param idProdotto
	 * @return
	 */
	public static String getDescrizione(String idProdotto) {
		if (idProdotto.length() > 35) {
			return idProdotto.substring(0, 35);
		}
		return idProdotto;
	}

	/**
	 * Imposta descrizione ridotta articolo da ID_PRODOTTO
	 * 
	 * @param idProdotto
	 * @return
	 */
	public static String getDescrizioneRidotta(String idProdotto) {
		if (idProdotto.length() > 15) {
			return idProdotto.substring(0, 15);
		}
		return idProdotto;
	}

	public void eseguoAggiornamentiOrdine() {

		String umV = this.getIdUMRif();
		String umM = this.getIdUMPrm();
		String umMSec = this.getIdUMSec(); // Fix 81111
		String umF = this.getUmVenFinito();
		BigDecimal qtaF = this.getQtaVenFinito();
		// BigDecimal qtaM = this.getQtaInUMPrmMag();
		// BigDecimal coef = this.getCoeffTessuto();
		// BigDecimal qtaV = this.getQtaVenFinito(); // fix 80808
		BigDecimal qtaD = this.getQtaDaProd();

		BigDecimal przX = this.getPrezzo();
		BigDecimal sc1X = this.getScontoArticolo1();
		// boolean przMan = thi;

		// versione richiesta articolo (da verificare se corretto il reperimento) (preso
		// da base\ecommerce\ImportatoreOrdini)

		Integer numVersione = null;
		ArticoloVersione versioneRcs = null;

		if (versioneRcs == null) {
			versioneRcs = this.getArticolo().getVersioneAtDate(new Date(System.currentTimeMillis()));
			if (versioneRcs != null) {
				numVersione = versioneRcs.getIdVersione();
				this.setIdVersioneRcs(numVersione);

			}
		}

		this.setAlfanumRiservatoUtente1(null);
		this.setAlfanumRiservatoUtente2(null);

		// this.setDescrizioneExtArticolo(desEstRig); // Descrizione estesa
		// 21-01-2019 LA NOTA NON DEVE ESSERE SETTATA A NULL this.setNota(null); // Note
		this.setIdFornitore(null); // Fornitore

		// String keyUmv = KeyHelper.buildObjectKey(new String[] { this.getIdAzienda(),
		// umV });
		/*
		 * UnitaMisura UMV = null; try { UMV =
		 * (UnitaMisura)PersistentObject.elementWithKey(UnitaMisura.class, keyUmv,
		 * PersistentObject.NO_LOCK); } catch (SQLException e) { e.printStackTrace(); }
		 */
		// String keyUmm = KeyHelper.buildObjectKey(new String[] { this.getIdAzienda(),
		// umM });
		// UnitaMisura UMM = null;
		// try {
		// UMM = (UnitaMisura) PersistentObject.elementWithKey(UnitaMisura.class,
		// keyUmm, PersistentObject.NO_LOCK);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		String keyUmf = KeyHelper.buildObjectKey(new String[] { this.getIdAzienda(), umF });
		UnitaMisura UMF = null;
		try {
			UMF = (UnitaMisura) PersistentObject.elementWithKey(UnitaMisura.class, keyUmf, PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// se trovata UM finito eseguo aggiornamenti riga
		if (UMF != null) {
			UnitaMisura unitaMisuraRif = UMF;
			UnitaMisura unitaMisuraPrm = UMF; // ----> devo valorizzare l'oggetto di var.getUmMagPrimaria() <<<<<
												// verificare
			// UnitaMisura unitaMisuraFin = UMF;

			if (unitaMisuraPrm != null) {
				this.setUMRif(unitaMisuraPrm);
			}
			if (unitaMisuraRif != null) {
				this.setIdUMPrm(unitaMisuraRif.getIdUnitaMisura());
			}
			/*
			 * if(unitaMisuraFin != null) { //Fix81111
			 * this.setUmVenFinito(unitaMisuraFin.getIdUnitaMisura()); }
			 */
		} else {
			this.setUmVenFinito("");
		}

		// Fix 81111
		this.setUmVenFinito(umV);
		this.setUmPrmFinito(umM);
		this.setUmSecFinito(umMSec);

		// Fix inizio 80808

		QuantitaInUMRif qrif = null;
		java.util.Date curDate = new java.util.Date();
		java.sql.Date dataBo = new java.sql.Date(curDate.getTime());

		if (this.getArticolo().getUMSecMag() != null) {
			qrif = this.getArticolo().calcolaQuantitaArrotondate(this.getQtaInUMPrmMag(),
					this.getArticolo().getUMRiferimento(), this.getArticolo().getUMPrmMag(),
					this.getArticolo().getUMSecMag(), this.getArticolo().getVersioneAtDate(dataBo), Articolo.UM_RIF);
			this.setQtaInUMSecMag(qrif.getQuantitaInUMSec());
		} else {
			this.setIdUMSec("");
			this.setQtaInUMSecMag(new BigDecimal("0"));
		}

		// elimino i lotti

		List righeL = this.getRigheLotto();

		List pezze = getPezze(this);

		if (pezze != null && !pezze.isEmpty()) {

			for (int i = 0; i < pezze.size(); i++) {

				VtPezza pezza = (VtPezza) pezze.get(i);

				// Fix 81761 >
				boolean isPezzaVirtuale = isPezzaVirtuale(pezza);
				if (!isPezzaVirtuale) {
					// Fix 81761 <

					VtOrdVenRigPrmMat ordVenRigPrmMat = (VtOrdVenRigPrmMat) Factory
							.createObject(VtOrdVenRigPrmMat.class);

					// ordVenRigPrmMat.getKey();
					ordVenRigPrmMat.setIdAzienda(pezza.getIdAzienda());
					ordVenRigPrmMat.setAnnoDocumento(pezza.getRAnnoOrdVen());
					ordVenRigPrmMat.setNumeroDocumento(pezza.getRNrOrdVen());

					if (pezza.getRRigaOrdVen() != null)
						ordVenRigPrmMat.setNumeroRigaDocumento(new Integer(pezza.getRRigaOrdVen().intValue()));

					ordVenRigPrmMat.setDettaglioRigaDocumento(new Integer(0));
					ordVenRigPrmMat.setIdArticolo(pezza.getRProdCorrente());
					ordVenRigPrmMat.setIdLotto(pezza.getRLotto());
					ordVenRigPrmMat.setIdMagazzino(pezza.getRMagazzino());

					VtArticolo artPezza = VtTessileUtil.getVtArticoloFromId(Azienda.getAziendaCorrente(),
							pezza.getRProdCorrente());
					if (artPezza != null)
						ordVenRigPrmMat.setUnitaMisura(artPezza.getIdUMPrmMag());

					ordVenRigPrmMat.setQtaImpegnata(pezza.getQtaUM1());
					ordVenRigPrmMat.setQtaSec(pezza.getQtaUM2());

					saveOrdVenRigPrmMat(ordVenRigPrmMat);

				}
			}
		}
		// Fix 80965 - Fine

		// fine creazione riga

		// inizio eliminazione lotti
		for (int i = 0; i < righeL.size(); i++) {
			OrdineVenditaRigaLottoPrm rLotto = (OrdineVenditaRigaLottoPrm) righeL.get(i);
			if (rLotto != null) {
				righeL.remove(rLotto);
			}
		}
		// fine eliminazione lotti

		this.setRilascioOrdineLavEsterna('0');

		if (przX == null)
			przX = new BigDecimal("0");
		if (sc1X == null)
			przX = new BigDecimal("0");

		// Fix 81111
		/*
		 * BigDecimal qtaPrm = qtaF; BigDecimal qtavvv = qtaF; BigDecimal qtafff = qtaF;
		 * this.setQtaInUMPrmMag(qtaD); this.setQtaInUMRif(qtaV);
		 * 
		 * this.setQtaVenFinito(qtafff); this.setQtaVenFinito(qtaV); //fix 80808
		 */
		// Fix 81111 setto le qta invertendo tra finito e standard
		this.setQtaVenFinito(this.getQtaInUMRif());
		this.setQtaPrmFinito(this.getQtaInUMPrmMag());
		if (this.getIdUMSec() != null && this.getQtaInUMSecMag() != null) {
			this.setQtaSecFinito(this.getQtaInUMSecMag());
		}
		this.setQtaDaProd(this.getQtaInUMPrmMag());
		this.setQtaInUMRif(qtaF);
		this.setQtaInUMPrmMag(qtaD);

		// Fix 81111 fine

		this.setRicalcoloQtaFattoreConv(true);
		// this.setDataConsegnaConfermata(dacX);

		VtOrdineVendita ordVen = null;
		if (this.getFatherKey() != null) {
			try {
				ordVen = (VtOrdineVendita) PersistentObject.elementWithKey(VtOrdineVendita.class, this.getFatherKey(),
						VtOrdineVendita.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (ordVen != null) {
			this.setDataConsegnaRichiesta(ordVen.getDataConsegnaRichiesta());
			this.setDataConsegnaProduzione(ordVen.getDataConsegnaProduzione());
		}

		if (this.getArticolo().getAssoggettamentoIVA() != null)
			this.setAssoggettamentoIVA(this.getArticolo().getAssoggettamentoIVA());

		this.setPesoNetto(new BigDecimal("0"));

		// (XXXX)
		// imposto il raggruppamento righe >>>>>>>> non uso settaggio della variante
		// (non pi??stito) ma quello sul pannello "Generale"
		// this.setSettaggio(var.getSettaggio());
		// this.setSettaggio(new Integer(0));

		// this.setNumeroPezze(new BigDecimal("0")); //Fix 81111

		// per non calcolare dati vendita che pulisce il prezzo
		this.setServizioCalcDatiVendita(false);
		this.getDatiComuniEstesi().setStato(DatiComuniEstesi.VALIDO);
		// imposto il flag di calcolo automatico peso e volume a FALSE (non ricalcola)
		this.setRicalcolaPesiEVolume(false);

		// recupero il Buyer dalla testata ordine
		this.setRBuyer(ordVen.getRBuyer());

		// aggiorno lunghezza pezza
		// this.setLungPezFin(new BigDecimal("0")); //Fix 81111

		// aggiorno coefficiente tessuto
		// Fix 81111 il coefficiente tessuto non va pi� settato a 0 ma deve rimanere
		// quello inserito
		// this.setCoeffTessuto(new BigDecimal("0"));

		// valorizzo IdProdotto da ARTICOLO
		this.setIdProdotto(this.getArticolo().getIdProdotto());

		// calcolo e valorizzo IdProdotto fino a variante (esclusa)

		this.setIdPrdFinoVar("");

		// tipologia ordine (usata solo per pre-ordini tessuto (P) e finito (C), gli
		// altri hanno sempre valore = '-')
		this.setTipologiaOrdine('C');

		// la riga dell'articolo KIT va impostato da non fatturare
		// this.setNonFatturare(true); // Fix 80820
		this.setNonFatturare(false);

	}

	// Fix 81761 >
	public boolean isPezzaVirtuale(VtPezza pezza) {
		boolean isPessaVirtuale = false;

		if (pezza.getRLotto() == null || pezza.getRMagazzino() == null)
			isPessaVirtuale = true;

		return isPessaVirtuale;
	}
	// Fix 81761 <

	public List getPezze(VtOrdineVenditaRigaPrm rigaOrd) {

		VtPezza cInstance = (VtPezza) Factory.createObject(VtPezza.class);

		String annoOrdine = rigaOrd.getAnnoDocumento();
		String numeroOrdine = rigaOrd.getNumeroDocumento();
		Integer rigaOrdine = rigaOrd.getNumeroRigaDocumento();

		// Fix 81122 - Inizio

		String whereCondition = VtPezzaTM.ID_AZIENDA + " = '" + rigaOrd.getIdAzienda() + "' " + " AND "
				+ VtPezzaTM.R_ANNO_ORD_VEN + " = '" + annoOrdine + "' " + " AND " + VtPezzaTM.R_NR_ORD_VEN + " = '"
				+ numeroOrdine + "' " + " AND " + VtPezzaTM.R_RIGA_ORD_VEN + " = " + rigaOrdine + " AND "
				+ VtPezzaTM.R_DET_RIGA_ORD_VEN + " = 0 " +
				// " AND " + VtPezzaTM.R_ARTICOLO + " = '" + rigaOrd.getIdArticolo() + "' ";

				" AND " + VtPezzaTM.R_ARTICOLO + " = '" + rigaOrd.getRArtFinito() + "' ";
		// Fix 81122 - Fine

		List pezze = new ArrayList();

		try {
			pezze = PersistentObject.retrieveList(cInstance, whereCondition, "", true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		return pezze;
	}

	public static boolean saveOrdVenRigPrmMat(VtOrdVenRigPrmMat ordVenRigPrmMat) {
		boolean success = false;
		if (ordVenRigPrmMat != null) {
			int rt = 0;
			try {
				ConnectionManager.pushConnection();
				rt = ordVenRigPrmMat.save();
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

	/**
	 * Esegue la creazione dell'articolo se non esiste (partendo come base
	 * l'articolo template)
	 * 
	 * @param articoloTemplate
	 * @param idProdotto
	 * @return
	 * @throws ThipException
	 * @throws IOException
	 * @throws ServletException
	 */
	public static Articolo creaArticoloDaCopia(Articolo articoloTemplate, String idProdotto, String idSchema, char tipo)
			throws ThipException, ServletException, IOException {

		if (articoloTemplate != null) {

			ArticoloDataCollector dataCollector = (ArticoloDataCollector) Factory
					.createObject(ArticoloDataCollector.class);
			dataCollector.initialize("Articolo", true, 1);
			dataCollector.initSecurityServices(WebForm.COPY, true, true, true);
			if (dataCollector.retrieve(articoloTemplate.getKey()) == BODataCollector.OK) {
				((ArticoloBase) dataCollector.getBo()).setInCopia(true);
				((ArticoloBase) dataCollector.getBo()).setIdArticoloInCopia(articoloTemplate.getIdArticolo());
				dataCollector.setAttribute("IdArticolo", null);
				dataCollector.setAttribute("IdProdotto", idProdotto);
				dataCollector.setAttribute("IdSchemaProdotto", idSchema);
				dataCollector.setAttribute("TipoSchemaProdotto", String.valueOf(tipo));
				dataCollector.setAttribute("TipoCodice", ArticoloBase.COD_NORMALE + "");
				// String fitti = ArticoloDatiIdent.FITTIZIO + "";
				// per descrizione normale riduco l'Id Prodotto
				// String idProdRid1 = null;
				/*
				 * if (idProdotto.length() > 35) { idProdRid1 = idProdotto.substring(0, 34); }
				 * else { idProdRid1 = idProdotto; }
				 */
				dataCollector.setAttribute("DescrizioneArticolo.Descrizione",
						getDescrizione(articoloTemplate.getDescrizioneArticoloNLS().getDescrizione()));
				dataCollector.setAttribute("DescrizioneArticolo.DescrizioneRidotta",
						getDescrizioneRidotta(articoloTemplate.getDescrizioneArticoloNLS().getDescrizioneRidotta()));

				// Prova 06/06/2017
				// Tolgo dal dataCollector la distinta base e il modello produttivo che dovranno
				// essere ricalcolati al salvataggio

				VtArticolo artic = (VtArticolo) dataCollector.getBo();
				artic.setDistintaBase(null);
				artic.setModelloProd(null);

				// Fine prova 06/06/2017

				// >>> il tipo parte che definisce l'articolo KIT non si riesce ad impostarlo
				// qui. Lo imposto dopo sull'oggetto ottenuto.
				// ((ArticoloDatiIdent) dataCollector.getBo()).setTipoParte('5'); // definito
				// TipoParte = 5 (Kit non gestito a magazzino)
				// dataCollector.setAttribute("TipoParte", ArticoloDatiIdent.KIT_NON_GEST);
				String messaggioErrore = null;
				int rrr = dataCollector.save();
				if (rrr == BODataCollector.OK) {

					// PROVA 06/06/2017 - Inizio

					try {
						ConnectionManager.pushConnection();
						VtArticoliTex articTex = (VtArticoliTex) PersistentObject.elementWithKey(VtArticoliTex.class,
								dataCollector.getBo().getKey(), VtArticoliTex.NO_LOCK);
						articTex.setDistintaBase(null);
						articTex.setModelloProd(null);
						int rc = articTex.save();
						if (rc >= 0)
							ConnectionManager.commit();
						else
							ConnectionManager.rollback();
						ConnectionManager.popConnection();
					} catch (SQLException e) {
						e.printStackTrace();
					}

					// PROVA 06/06/2017 - Fine

					return (Articolo) dataCollector.getBo();
				}
				/*
				 * else { // segnalo errore su finestra per impossibilit?d aprire un prodotto
				 * messaggioErrore =
				 * dataCollector.getErrorList().getFirstElement().getLongText(); String URL =
				 * "it/thera/thip/tessile/vendite/ordineVE/servlet/VtOrdVenRigErrPrd1.jsp?Testo="
				 * + messaggioErrore + "&Prod=" + idProdotto;
				 * se.sendRequest(getServletContext(), URL, false); return null; //throw new
				 * ThipException(dataCollector.getErrorList().getFirstElement()); }
				 */

			}
		}
		return null;
	}

	// Fix 81098 - Inizio
	public Object[] getRigaLottoOrig(OrdineVenditaRigaLotto rigaLottoOrig, List lottiOrd, char azione) { // 19139
		int totLottiOrig = 0;
		OrdineVenditaRigaLotto rigaLottoDummy = null;
		BigDecimal totaleQtaOrdLottiInUMPrm = new BigDecimal(0.00);
		BigDecimal totaleQtaOrdLottiInUMSec = new BigDecimal(0.00);
		BigDecimal totaleQtaOrdLottiInUMVen = new BigDecimal(0.00);
		if (rigaLottoOrig != null)
			// return rigaLottoOrig;
			// 19139
			return new Object[] { Boolean.FALSE, rigaLottoOrig };
		// 19139

		if (lottiOrd != null && !lottiOrd.isEmpty()) {
			for (int i = 0; i < lottiOrd.size(); i++) {
				OrdineVenditaRigaLotto currentRigaLotto = (OrdineVenditaRigaLotto) lottiOrd.get(i);
				totaleQtaOrdLottiInUMPrm = totaleQtaOrdLottiInUMPrm
						.add(currentRigaLotto.getQuantitaOrdinata().getQuantitaInUMPrm());
				totaleQtaOrdLottiInUMSec = totaleQtaOrdLottiInUMSec
						.add(currentRigaLotto.getQuantitaOrdinata().getQuantitaInUMSec());
				totaleQtaOrdLottiInUMVen = totaleQtaOrdLottiInUMVen
						.add(currentRigaLotto.getQuantitaOrdinata().getQuantitaInUMRif());

				if (currentRigaLotto.getIdLotto().equals(Lotto.LOTTO_DUMMY))
					rigaLottoDummy = currentRigaLotto;
				if (!currentRigaLotto.isDaEvasione()) {
					rigaLottoOrig = currentRigaLotto;
					totLottiOrig++;
				}
			}
			if (rigaLottoDummy != null)
				// return rigaLottoDummy;//19139
				return new Object[] { Boolean.FALSE, rigaLottoDummy };

			// else if(totLottiOrig != 0){ // totLottiOrig != 1 MAAM FIX 81098 MODIFICA PER
			// FAR SI CHE CREI IL LOTTO DUMMY ANCHE QUANDO I LOTTI DELL'ORDINE ?
			else if (totLottiOrig != 1) {
				// return creaLottiDummy(totaleQtaOrdLottiInUMPrm,
				// totaleQtaOrdLottiInUMSec,totaleQtaOrdLottiInUMVen);// 19139
				// 19139 inziio
				return new Object[] { Boolean.TRUE,
						creaLottiDummy(totaleQtaOrdLottiInUMPrm, totaleQtaOrdLottiInUMSec, totaleQtaOrdLottiInUMVen) };
				// 19139 fine
			}
		}
		// return rigaLottoOrig; //19139
		return new Object[] { Boolean.FALSE, rigaLottoOrig };
	}
	// Fix 81098 - Fine

	// Fix 81125 - Raggruppo passaggi perla creazione dell'articolo
	// cliente per non incasinare il codice nel caso in cui si tratti di un ordine
	// di tipo pre ordine modello
	public void operazioniArticoloCliente() {
		Integer nuovoIdArtCli = new Integer(0);
		if (getArticoloCli() != null) {
			try {
				nuovoIdArtCli = VtOrdineVenditaRigaPrmRidottaSave.creaArticoloCliente(getIdAzienda(), getArticoloCli(),
						getVarianteArtcli(), getIdArticolo(), getRCliente(), getRStagione(), getIdUMRif());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// Fix 80957 - Inizio
		else {
			setRArtCli(null);
		}

		// se creato l'articolo cliente, aggiorno il riferimento sulla riga
		if (nuovoIdArtCli != null && !nuovoIdArtCli.equals(new Integer(0))) {
			setRArtCli(nuovoIdArtCli);
		}
	}

	// Fix 81292 - Inzio
	public String getIdArticoloFromIdProdotto(String idAzienda, String idProdotto) {

		String SELECT_ID_ARTICOLO = "SELECT " + ArticoloTM.ID_ARTICOLO + "," + ArticoloTM.ID_AZIENDA + " FROM "
				+ ArticoloTM.TABLE_NAME + " WHERE " + ArticoloTM.ID_PRODOTTO + " = ?";

		String idAz = "";
		String idProd = "";
		String ret = "";

		CachedStatement cIdArt = new CachedStatement(SELECT_ID_ARTICOLO);

		try {
			Database db = ConnectionManager.getCurrentDatabase();
			PreparedStatement ps = cIdArt.getStatement();
			db.setString(cIdArt.getStatement(), 1, idProdotto);
			// ps.setString(2, idProdotto);
			ResultSet rs = cIdArt.executeQuery();
			// String ret = (rs.next()) ? rs.getString(1) : "";

			//
			while (rs.next()) {

				idProd = rs.getString(1);
				idAz = rs.getString(2);

				ret = idProd.trim() + KeyHelper.KEY_SEPARATOR + idAz.trim();
			}
			//

			rs.close();
			return ret.trim();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}

	}

	// Fix 81292 - Fine

	// Fix 81361 - Inizio

	public String recuperaPosizioniChiave(VtArticolo art) {
		String posizioni = null;

		String chiave1 = ParametroPsn.getParametroPsn("VtChiave1", "Chiave1").getValore();
		String chiave2 = ParametroPsn.getParametroPsn("VtChiave2", "Chiave2").getValore();
		String chiave3 = ParametroPsn.getParametroPsn("VtChiave3", "Chiave3").getValore();

		String selectPos = "SELECT " + ChiaveProdottoTM.CAMPO_CHIAVE + " FROM " + ChiaveProdottoTM.TABLE_NAME + " "
				+ "WHERE " + ChiaveProdottoTM.ID_AZIENDA + "='" + art.getIdAzienda() + "' AND "
				+ ChiaveProdottoTM.R_TIPO + "='P' AND " + ChiaveProdottoTM.R_SCHEMA_PRODOTTO + "='"
				+ art.getIdSchemaProdotto() + "' AND (" + ChiaveProdottoTM.ID_CHIAVE_PRODOTTO + "= '" + chiave1
				+ "' OR " + ChiaveProdottoTM.ID_CHIAVE_PRODOTTO + "= '" + chiave2 + "' OR "
				+ ChiaveProdottoTM.ID_CHIAVE_PRODOTTO + "= '" + chiave3 + "')";

		CachedStatement sqlPos = new CachedStatement(selectPos);
		ResultSet rsPos;
		try {
			rsPos = sqlPos.executeQuery();

			int i = 0;
			while (rsPos.next()) {
				if (i == 0)
					posizioni = rsPos.getString(1);
				else
					posizioni = posizioni + KeyHelper.KEY_SEPARATOR + rsPos.getString(1);
				i++;
			}

			rsPos.close();
			sqlPos.free();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return posizioni;
	}

	public static String convertiChiaveInNumero(char chiave) {

		String posizione = "";

		switch (chiave) {
		case '1':
			posizione = "1";
			break;
		case '2':
			posizione = "2";
			break;
		case '3':
			posizione = "3";
			break;
		case '4':
			posizione = "4";
			break;
		case '5':
			posizione = "5";
			break;
		case '6':
			posizione = "6";
			break;
		case '7':
			posizione = "7";
			break;
		case '8':
			posizione = "8";
			break;
		case '9':
			posizione = "9";
			break;
		case 'A':
			posizione = "10";
			break;
		case 'B':
			posizione = "11";
			break;
		case 'C':
			posizione = "12";
			break;
		// Fix 80640 inizio
		case 'D':
			posizione = "13";
			break;
		case 'E':
			posizione = "14";
			break;
		case 'F':
			posizione = "15";
			break;
		case 'G':
			posizione = "16";
			break;
		case 'H':
			posizione = "17";
			break;
		case 'I':
			posizione = "18";
			break;
		case 'L':
			posizione = "19";
			break;
		case 'M':
			posizione = "20";
			break;
		case 'N':
			posizione = "21";
			break;
		case 'O':
			posizione = "22";
			break;
		case 'P':
			posizione = "23";
			break;
		case 'Q':
			posizione = "24";
			break;
		case 'R':
			posizione = "25";
			break;
		// Fix 80640 fine
		}

		return posizione;
	}

	public String ricercaRigaOrdineVendita(String chiave1, String chiave2, String chiave3, VtArticolo vtArt) {
		String chiaveOrd = "";

		String cChiave1 = "";
		String cChiave2 = "";
		String cChiave3 = "";

		// 05-06-2018
		if (chiave1 != null && !chiave1.equals("null"))
			cChiave1 = convertiChiaveInNumero(chiave1.charAt(0));
		if (chiave2 != null && !chiave1.equals("null"))
			cChiave2 = convertiChiaveInNumero(chiave2.charAt(0));
		if (chiave3 != null && !chiave1.equals("null"))
			cChiave3 = convertiChiaveInNumero(chiave3.charAt(0));

		String valChiave1 = VtTessileUtil.getValueKeyArtByPos(vtArt, cChiave1);
		String valChiave2 = VtTessileUtil.getValueKeyArtByPos(vtArt, cChiave2);
		String valChiave3 = VtTessileUtil.getValueKeyArtByPos(vtArt, cChiave3);

		String select = " SELECT R.ID_AZIENDA,R.ID_ANNO_ORD,R.ID_NUMERO_ORD, R.ID_RIGA_ORD "
				+ " FROM THIP.ORD_VEN_RIG AS R INNER JOIN THIP.VT_ART_CHIAVI AS C "
				+ " ON R.ID_AZIENDA=C.ID_AZIENDA AND R.R_ARTICOLO=C.ID_ARTICOLO " + " WHERE R.ID_AZIENDA='"
				+ vtArt.getIdAzienda() + "' AND C.CHIAVE_" + cChiave1 + "='" + valChiave1 + "'" + " AND C.CHIAVE_"
				+ cChiave2 + "='" + valChiave2 + "' " + " AND C.CHIAVE_" + cChiave3 + "='" + valChiave3 + "' " +
				// " AND (R.ID_RIGA_ORD!='"+ this.getNumeroRigaDocumento() +"' AND
				// R.ID_NUMERO_ORD!='" + this.getNumeroDocumento() + "')" +
				" ORDER BY R.TIMESTAMP_AGG DESC ";

		CachedStatement sql = new CachedStatement(select);
		ResultSet rs;
		try {
			rs = sql.executeQuery();

			while (rs.next()) {

				chiaveOrd = rs.getString(1).trim() + KeyHelper.KEY_SEPARATOR + rs.getString(2).trim()
						+ KeyHelper.KEY_SEPARATOR + rs.getString(3).trim() + KeyHelper.KEY_SEPARATOR
						+ rs.getString(4).trim();
				if (!chiaveOrd.equals(this.getKey()))
					break;
			}

			rs.close();
			sql.free();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return chiaveOrd;
	}

	public String checkOrdiniPrecedenti(VtArticolo vtArt) {
		// boolean esiste = false;
		String chiaveRigaOrdVen = "";

		String posChiavi = recuperaPosizioniChiave(vtArt);

		String[] chiave = {};

		// String[] chiave = posChiavi.split(KeyHelper.KEY_SEPARATOR);
		// String pos = VtTessileUtil.getCampoChiaveByArticolo(vtArt, "VtLavorazioni");

		if (posChiavi != null) {
			chiave = posChiavi.split(KeyHelper.KEY_SEPARATOR);
			if (chiave.length == 3) {
				String chiave1 = chiave[0];
				String chiave2 = chiave[1];
				String chiave3 = chiave[2];

				chiaveRigaOrdVen = ricercaRigaOrdineVendita(chiave1, chiave2, chiave3, vtArt);

			}
		}

		// PRIMA FACCIO UNA SELECT PER LA POSIZIONE CON PARAMETRI ARTIC, CLIENTE,LAV
		// POI FACCIO UNA SELECT NEGLI ORDINI PER VERIFICARE SE ESISTE

		return chiaveRigaOrdVen;
	}

	public VtOrdVenRigAtv creaVtOrdVenRigAtvByAtv(VtOrdVenRigAtv atv) {
		// boolean ret = false;

		VtOrdVenRigAtv atvRigaOrd = (VtOrdVenRigAtv) Factory.createObject(VtOrdVenRigAtv.class);

		atvRigaOrd.setIdAzienda(this.getIdAzienda());
		atvRigaOrd.setIdAnnoOrd(this.getAnnoDocumento());
		atvRigaOrd.setIdNumeroOrd(this.getNumeroDocumento());
		atvRigaOrd.setIdRigaOrd(this.getNumeroRigaDocumento());
		atvRigaOrd.setIdDetRigaOrd(this.getDettaglioRigaDocumento());
		atvRigaOrd.setIdAttivita(atv.getIdAttivita());
		atvRigaOrd.setAttivita(atv.getAttivita());

		atvRigaOrd.setSequenza(atv.getSequenza());

		// Fix 81367
		atvRigaOrd.setPrezzo(atv.getPrezzo());

		return atvRigaOrd;

	}
	// Fix 81325 - Fine

	// Fix 81361 - Fine

	// Fix 81325 - Inizio
	public VtOrdVenRigAtv creaVtOrdVenRigAtvByAtv(VtLavorazioniAtv atvLav) {
		// boolean ret = false;

		VtOrdVenRigAtv atvRigaOrd = (VtOrdVenRigAtv) Factory.createObject(VtOrdVenRigAtv.class);

		atvRigaOrd.setIdAzienda(this.getIdAzienda());
		atvRigaOrd.setIdAnnoOrd(this.getAnnoDocumento());
		atvRigaOrd.setIdNumeroOrd(this.getNumeroDocumento());
		atvRigaOrd.setIdRigaOrd(this.getNumeroRigaDocumento());
		atvRigaOrd.setIdDetRigaOrd(this.getDettaglioRigaDocumento());
		atvRigaOrd.setIdAttivita(atvLav.getIdAttivita());
		atvRigaOrd.setAttivita(atvLav.getAttivita());

		atvRigaOrd.setSequenza(atvLav.getIdRigaAttivita());

		// Fix 81367 - Inizio

		QuantitaInUMRif qtaInUMRif = getQtaStessoSettore(this);

		// Fix 81367 BigDecimal prezzo = VtOrdVenRigAtvSetPrezzo.calcolaPrezzoAtv(this,
		// atvLav.getIdAttivita());
		BigDecimal prezzo = VtOrdVenRigAtvSetPrezzo.calcolaPrezzoAtv(this, atvLav.getIdAttivita(), qtaInUMRif);
		atvRigaOrd.setPrezzo(prezzo);

		// aggiornaPrezzoOrdVenRigAtv(prezzo);

		// Fix 81367 - Fine

		return atvRigaOrd;

	}
	// Fix 81325 - Fine

	// Fix 81367 - Inizio
	public void aggiornaPrezzoOrdVenRigAtv() {
		String whereOrdVenRig = VtOrdineVenditaRigaPrmTM.ID_AZIENDA + "='" + this.getIdAzienda() + "' AND "
				+ VtOrdineVenditaRigaPrmTM.ID_ANNO_ORD + "=" + "'" + this.getAnnoDocumento() + "' AND "
				+ VtOrdineVenditaRigaPrmTM.ID_NUMERO_ORD + "=" + "'" + this.getNumeroDocumento() + "'";// AND " +
		// VtOrdineVenditaRigaPrmTM.SETTAGGIO + "=" + "'" + this.getSettaggio() + "'" ;

		List lstRigheOrd = new ArrayList();
		try {
			lstRigheOrd = VtOrdineVenditaRigaPrm.retrieveList(VtOrdineVenditaRigaPrm.class, whereOrdVenRig,
					VtOrdineVenditaRigaPrmTM.ID_AZIENDA, true);

			Iterator iterlstRigheOrd = lstRigheOrd.iterator();
			while (iterlstRigheOrd.hasNext()) {
				VtOrdineVenditaRigaPrm rigaOrdPrm = (VtOrdineVenditaRigaPrm) iterlstRigheOrd.next();

				if (!rigaOrdPrm.getSettaggio().equals(this.getSettaggio()))
					continue;
				else {
					if (!rigaOrdPrm.getNumeroRigaDocumento().equals(this.getNumeroRigaDocumento())) {
						List lstAtv = rigaOrdPrm.getVtOrdVenRigAtv();
						Iterator iterLstAtv = lstAtv.iterator();
						while (iterLstAtv.hasNext()) {
							VtOrdVenRigAtv ordVenRigAtv = (VtOrdVenRigAtv) iterLstAtv.next();

							QuantitaInUMRif qtaInUMRif = getQtaStessoSettore(this);

							// Fix 81367 BigDecimal prezzo = VtOrdVenRigAtvSetPrezzo.calcolaPrezzoAtv(this,
							// atvLav.getIdAttivita());
							BigDecimal prezzo = VtOrdVenRigAtvSetPrezzo.calcolaPrezzoAtv(this,
									ordVenRigAtv.getIdAttivita(), qtaInUMRif);

							ordVenRigAtv.setPrezzo(prezzo);

						}

						rigaOrdPrm.setPrezzo(this.getPrezzo());
						rigaOrdPrm.save();
					}

				}

			}

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

	}

	public QuantitaInUMRif getQtaStessoSettore(VtOrdineVenditaRigaPrm ordVenrigPrm) {

		// BigDecimal qtaUmPrm = new BigDecimal(0);
		QuantitaInUMRif quantitaInUMRif = new QuantitaInUMRif();

		BigDecimal qtaInUMRif = new BigDecimal(0);
		BigDecimal qtaInUMPrm = new BigDecimal(0);
		BigDecimal qtaInUMSec = new BigDecimal(0);

		String select = "SELECT SUM(QTA_ORD_UM_VEN),SUM(QTA_ORD_UM_PRM), SUM(QTA_ORD_UM_SEC) "
				+ " FROM THIP.VT_ORD_VEN_RIG AS A, THIP.ORD_VEN_RIG B "
				+ " WHERE A.ID_AZIENDA=B.ID_AZIENDA AND A.ID_ANNO_ORD=B.ID_ANNO_ORD AND A.ID_NUMERO_ORD=B.ID_NUMERO_ORD AND A.ID_RIGA_ORD=B.ID_RIGA_ORD AND A.ID_DET_RIGA_ORD=B.ID_DET_RIGA_ORD "
				+ " AND A.ID_AZIENDA='" + this.getIdAzienda() + "' AND A.ID_ANNO_ORD='" + this.getAnnoDocumento() + "' "
				+ " AND A.ID_NUMERO_ORD='" + this.getNumeroDocumento() + "' AND A.SETTAGGIO='" + this.getSettaggio()
				+ "'";

		CachedStatement cInstanceOrdVenRig = new CachedStatement(select);
		try {

			ResultSet rs = cInstanceOrdVenRig.executeQuery();

			if (rs.next()) {
				qtaInUMRif = rs.getBigDecimal(1);
				qtaInUMPrm = rs.getBigDecimal(1);
				qtaInUMSec = rs.getBigDecimal(1);
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		quantitaInUMRif.setQuantitaInUMRif(qtaInUMRif);
		quantitaInUMRif.setQuantitaInUMPrm(qtaInUMPrm);
		quantitaInUMRif.setQuantitaInUMSec(qtaInUMSec);

		return quantitaInUMRif;
	}

	// Fix 81367 - Fine

	// davide fix 81487
	protected char iTipoColorante = '-';

	public char getTipoColorante() {
		String idArt = this.getRArtOrdinato();
		if (idArt != null && !idArt.trim().equals("null") && !idArt.trim().equals("")) {
			VtArticoliTex art = null;
			try {
				art = (VtArticoliTex) VtArticoliTex.elementWithKey(VtArticoliTex.class,
						Azienda.getAziendaCorrente() + KeyHelper.KEY_SEPARATOR + idArt.trim(),
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (art != null) {
				iTipoColorante = art.getTipoColorante();
			}
		}
		return iTipoColorante;
	}

	public void setTipoColorante(char tipocol) {
		String idArt = this.getRArtOrdinato();
		if (idArt != null && !idArt.trim().equals("null") && !idArt.trim().equals("")) {

			VtArticoliTex art = null;
			try {
				art = (VtArticoliTex) VtArticoliTex.elementWithKey(VtArticoliTex.class,
						Azienda.getAziendaCorrente() + KeyHelper.KEY_SEPARATOR + idArt.trim(),
						PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (art != null) {
				tipocol = art.getTipoColorante();
			}
		}
		this.iTipoColorante = tipocol;
	}

	// 81487

	// 01-02-2019 > Metodo ridefinito per non aggionare la quantita ordinata in
	// funzione della quantita dei lotti
	protected void aggiornaQuantitaPortafoglio() {
		// QuantitaInUMRif qtaOrdinata = getQuantitaOrdinata();
		BigDecimal qtaUMRif = getQuantitaOrdinata().getQuantitaInUMRif();
		BigDecimal qtaUMPrm = getQuantitaOrdinata().getQuantitaInUMPrm();
		BigDecimal qtaUMSec = getQuantitaOrdinata().getQuantitaInUMSec();

		super.aggiornaQuantitaPortafoglio();

		getQuantitaOrdinata().setQuantitaInUMRif(qtaUMRif);
		getQuantitaOrdinata().setQuantitaInUMPrm(qtaUMPrm);
		getQuantitaOrdinata().setQuantitaInUMSec(qtaUMSec);

		/*
		 * Iterator i = getRigheLotto().iterator(); azzeraQuantitaPortafoglio(); while
		 * (i.hasNext()) { OrdineVenditaRigaLotto ovrl = (OrdineVenditaRigaLotto)
		 * i.next(); sommaQuantita(getQuantitaOrdinata(), ovrl.getQuantitaOrdinata());
		 * sommaQuantita(getQtaPropostaEvasione(), ovrl.getQtaPropostaEvasione());
		 * sommaQuantita(getQtaAttesaEvasione(), ovrl.getQtaAttesaEvasione());
		 * sommaQuantita(getQuantitaSpedita(), ovrl.getQuantitaSpedita());
		 * sommaQuantita(getQuantitaAccantonata(), ovrl.getQuantitaAccantonata()); }
		 */
	}
	// 01-02-2019 <

	protected void aggiungiNuoviLotti(List nuoviLotti, List lottiOrdineCancellabili) throws SQLException {

		if (isCalledFromProposteDiEvasione()) {
			boolean isAllNuoviDummy = true;
			boolean isAllCancellabiliNonDummy = true;
			for (int i = 0; i < nuoviLotti.size(); ++i) {
				DocumentoVenRigaLotto drl = (DocumentoVenRigaLotto) nuoviLotti.get(i);
				if (!drl.getIdLotto().equals("-")) {
					isAllNuoviDummy = false;
				}
			}

			for (int i = 0; i < lottiOrdineCancellabili.size(); ++i) {
				OrdineRigaLotto drl = (OrdineRigaLotto) lottiOrdineCancellabili.get(i);
				if (drl.getIdLotto().equals("-")) {
					isAllCancellabiliNonDummy = false;
				}
			}

			if (isAllNuoviDummy && isAllCancellabiliNonDummy) {
				nuoviLotti = new Vector();
				lottiOrdineCancellabili = new Vector();
			}
		}

		super.aggiungiNuoviLotti(nuoviLotti, lottiOrdineCancellabili);

	}

	private boolean isCalledFromProposteDiEvasione() {

		/*
		 * 14-06-2019 StackTraceElement[] st = Thread.currentThread().getStackTrace();
		 * for (int i = 0; i < st.length; ++i) { if (st[i].toString().indexOf("LspGen")
		 * >= 0) { return true; } }
		 */

		return false;
	}

	// Fix 81880 >
	protected void aggiornaQtaDisposta(VtOrdineVenditaRigaPrm rigaOrdPrmDisp, VtOrdineVenditaRigaPrm rigaOrdPrmDeriv) {
		rigaOrdPrmDisp.setQtaDisposta(rigaOrdPrmDisp.getQtaDisposta().subtract(rigaOrdPrmDeriv.getQtaInUMRif()));
		rigaOrdPrmDisp.setQtaResidua(rigaOrdPrmDisp.getQtaResidua().add(rigaOrdPrmDeriv.getQtaInUMPrmMag()));

		QuantitaInUMRif qtaInUMRif = this.getArticolo().calcolaQuantitaArrotondate(rigaOrdPrmDisp.getQtaDisposta(),
				rigaOrdPrmDisp.getUMRif(), rigaOrdPrmDisp.getUMPrm(), rigaOrdPrmDisp.getUMSec(), Articolo.UM_RIF);

		rigaOrdPrmDisp.getQuantitaSpedita().setQuantitaInUMRif(qtaInUMRif.getQuantitaInUMRif());
		rigaOrdPrmDisp.getQuantitaSpedita().setQuantitaInUMPrm(qtaInUMRif.getQuantitaInUMPrm());
		rigaOrdPrmDisp.getQuantitaSpedita().setQuantitaInUMSec(qtaInUMRif.getQuantitaInUMSec());

		// 81880 >
		if (rigaOrdPrmDisp.getQtaDisposta().compareTo(this.getQtaInUMPrmMag()) >= 0) {
			// saldo riga
			this.setStatoEvasione(StatoEvasione.SALDATO);
		}

		else if (rigaOrdPrmDisp.getQtaDisposta().compareTo(new BigDecimal(0)) > 0) {
			this.setStatoEvasione(StatoEvasione.EVASO_PARZIALMENTE);
		}

		else if (rigaOrdPrmDisp.getQtaDisposta().compareTo(new BigDecimal(0)) == 0) {
			this.setStatoEvasione(StatoEvasione.INEVASO);
		}

		/*
		 * this.setQtaDisposta(qtaDisposta);
		 * this.setQtaResidua(this.getQtaInUMRif().subtract(this.getQtaDisposta()));
		 * 
		 * //qtadisposta = qta spedita
		 * 
		 * QuantitaInUMRif qtaInUMRif =
		 * this.getArticolo().calcolaQuantitaArrotondate(qtaDisposta,this.getUMRif(),
		 * this.getUMPrm(),this.getUMSec(), Articolo.UM_RIF);
		 * 
		 * this.getQuantitaSpedita().setQuantitaInUMRif(qtaInUMRif.getQuantitaInUMRif())
		 * ;
		 * this.getQuantitaSpedita().setQuantitaInUMPrm(qtaInUMRif.getQuantitaInUMPrm())
		 * ;
		 * this.getQuantitaSpedita().setQuantitaInUMSec(qtaInUMRif.getQuantitaInUMSec())
		 * ;
		 * 
		 * //81880 > if(qtaDisposta.compareTo(this.getQtaInUMPrmMag())>=0) { //saldo
		 * riga this.setStatoEvasione(StatoEvasione.SALDATO); }
		 * 
		 * else if (qtaDisposta.compareTo(new BigDecimal(0))>0) {
		 * this.setStatoEvasione(StatoEvasione.EVASO_PARZIALMENTE); }
		 * 
		 */

	}
	// Fix 81880 z

}
