package it.thera.thip.tessile.vendite.ordineVE;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.Proxy;

import it.thera.thip.base.agentiProvv.Agente;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.cliente.ClienteVendita;
import it.thera.thip.base.cliente.Zona;
import it.thera.thip.tessile.cliente.VtClienteDivisione;
import it.thera.thip.tessile.cliente.VtClienteVendita;
import it.thera.thip.tessile.tabelle.VtBuyer;
import it.thera.thip.tessile.tabelle.VtLineaVen;
import it.thera.thip.tessile.tabelle.VtOperatComm;
import it.thera.thip.tessile.tabelle.VtSettore;
import it.thera.thip.tessile.tabelle.VtStagione;
import it.thera.thip.vendite.ordineVE.OrdineVendita;
import it.thera.thip.vendite.prezziExtra.Cantiere;


/**
 * VtOrdineVendita.
 * <b>Copyright (C) : SIConsulting</b>
 * @author Andrea Calligaro
 */
/*
 * Revisions:
 * Number  Date         Owner   Description
 * 80279   28/04/2015	ANDC 	Prima versione
 * 80660   13/07/2016   CMB     Modificato riferimento alle tabelle rese interaziendali
 */

public class VtOrdineVendita extends OrdineVendita {

	protected String iRBuyer;
	protected String iROpeComm;
	protected Proxy iVtBuyer = new Proxy(it.thera.thip.tessile.tabelle.VtBuyer.class);
	protected Proxy iVtOpeComm = new Proxy(it.thera.thip.tessile.tabelle.VtOperatComm.class);

	//------- Fix - Inizio
	protected String iRDivisioneCliente;
	protected String iRLineaVen;
	protected String iRSettore;
	protected String iRStagione;

	protected Proxy iVtClienteDivisione = new Proxy(it.thera.thip.tessile.cliente.VtClienteDivisione.class);
	protected Proxy iVtLineaVen = new Proxy(it.thera.thip.tessile.tabelle.VtLineaVen.class);
	protected Proxy iVtSettore = new Proxy(it.thera.thip.tessile.tabelle.VtSettore.class);
	protected Proxy iVtStagione = new Proxy(it.thera.thip.tessile.tabelle.VtStagione.class);

	//------- Fix - Fine
	protected static CachedStatement csRicDocMontSmontRig;
	//Fix 80717 - Inizio
	protected String iRZonaVis;

	protected Proxy iZonaVis = new Proxy(Zona.class);
	//Fix 80717 - Fine

	protected String iAnnoOrdineAdisp;
	protected String iNumeroOrdAdisp;
	protected String iTipoOrdineNuovo;		// contiene D per ordini derivati da ordini a disporre (altri = VUOTO)
	protected String iIdSerie;				// di servizio per passare la serie dell'ordine al pannello testata nel caso di Nuovo ordine

	protected BigDecimal iProvvigioneSubagente2      = new BigDecimal("0");
	protected BigDecimal iProvvigioneSubagente3      = new BigDecimal("0");
	protected BigDecimal iProvvigioneSubagente4      = new BigDecimal("0");
	protected Proxy iSubagente2               = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iSubagente3               = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iSubagente4               = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iCausaleStatistica 		  = new Proxy(VtCausaleStatistica.class);
	
	private boolean iDifferenzaPrezzoSubag2    = false;
	private boolean iDifferenzaPrezzoSubag3    = false;
	private boolean iDifferenzaPrezzoSubag4    = false;


	//Fix 80965 - Inizio
	protected String iRifProduzione;
	//Fix 80965 - Fine

	//Fix XXXXX - Inizio
	protected String iRifCliente;
	//Fix XXXXX - Fine

	//Fix 81292 - Inizio
	protected String iTipologiaOrdine;
	//Fix 81292 - Fine

	// riferimenti ordine a disporre
	public String getAnnoOrdineAdisp() {
		return iAnnoOrdineAdisp;
	}

	public void setAnnoOrdineAdisp(String annoOrdineAdisp) {
		this.iAnnoOrdineAdisp = annoOrdineAdisp;
	}

	// riferimenti ordine a disporre
	public String getNumeroOrdAdisp() {
		return iNumeroOrdAdisp;
	}

	public void setNumeroOrdAdisp(String numeroOrdAdisp) {
		this.iNumeroOrdAdisp = numeroOrdAdisp;
	}

	// tipo nuovo ordine (di servizio)
	public String getTipoOrdineNuovo() {
		return iTipoOrdineNuovo;
	}

	public void setTipoOrdineNuovo(String tipoOrdineNuovo) {
		this.iTipoOrdineNuovo = tipoOrdineNuovo;
	}

	// tipo id Serie (di servizio)
	public String getIdSerie() {
		return iIdSerie;
	}

	public void setIdSerie(String idSerie) {
		this.iIdSerie = idSerie;
	}

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
		setCodiceAziendaInternal(idAzienda);
		this.iVtBuyer.setObject(vtBuyer);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	// recupera um proxy
	public VtBuyer getVtBuyer() {
		return (VtBuyer)iVtBuyer.getObject();
	}

	// imposta chiave buyer
	public void setVtBuyerKey(String key) {
		String oldObjectKey = getKey();
		iVtBuyer.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setCodiceAziendaInternal(idAzienda);
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
		iVtBuyer.setKey(KeyHelper.replaceTokenObjectKey(key , 2, vtBuyer));
		setDirty();
	}

	// Recupera buyer
	public String getRBuyer() {
		String key = iVtBuyer.getKey();
		String objRBuyer = KeyHelper.getTokenObjectKey(key,2);
		return objRBuyer;
	}

	/**
	 * OpeComm - Operatore commerciale
	 * 
	 */

	// Imposta OpeComm proxy
	public void setVtOpeComm(VtOperatComm vtOpeComm) {
		String oldObjectKey = getKey();
		String idAzienda = null;
		if (vtOpeComm != null) {
			idAzienda = KeyHelper.getTokenObjectKey(vtOpeComm.getKey(), 1);
		}
		setCodiceAziendaInternal(idAzienda);
		this.iVtBuyer.setObject(vtOpeComm);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	// recupera proxy
	public VtOperatComm getVtOpeComm() {
		return (VtOperatComm)iVtOpeComm.getObject();
	}

	// imposta chiave buyer
	public void setVtOpeCommKey(String key) {
		String oldObjectKey = getKey();
		iVtOpeComm.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setCodiceAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	// recupera chiave buyer
	public String getVtOpeCommKey() {
		return iVtOpeComm.getKey();
	}

	// imposta operatore commerciale
	public void setROpeComm(String vtOpeComm) {
		String key = iVtOpeComm.getKey();
		iVtOpeComm.setKey(KeyHelper.replaceTokenObjectKey(key , 2, vtOpeComm));
		setDirty();
	}

	// Recupera operatore commerciale
	public String getROpeComm() {
		String key = iVtOpeComm.getKey();
		String objROpeComm = KeyHelper.getTokenObjectKey(key,2);
		// se vuoto lo ricavo dal cliente  (viene eseguito in FormModifier e nella JS)
		/*if(objROpeComm == null) {
			VtClienteVendita cliV = (VtClienteVendita) getCliente();
		    if (cliV != null) {
		      if(cliV.getIdOpeComm() != null) {
		    	  objROpeComm = cliV.getIdOpeComm();
		      }
		    }
		}*/
		return objROpeComm;
	}


	//------- Fix - Inizio

	//iRDivisioneCliente
	public String getRDivisioneCliente() {
		String key = iVtClienteDivisione.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRDivisioneCliente(String rDivisioneCliente) {
		String key = iVtClienteDivisione.getKey();
		setVtClienteDivisioneKey(KeyHelper.replaceTokenObjectKey(key, 2, rDivisioneCliente));
	}

	// iVtClienteDivisione
	public void setVtClienteDivisione(VtClienteDivisione vtClienteDivisione){
		this.iVtClienteDivisione.setObject(vtClienteDivisione);
		setDirty();
	}

	public VtClienteDivisione getVtClienteDivisione(){
		return (VtClienteDivisione)iVtClienteDivisione.getObject();
	}

	public void setVtClienteDivisioneKey(String vtClienteDivisioneKey){
		iVtClienteDivisione.setKey(vtClienteDivisioneKey);
		setDirty();
	}

	public String getVtClienteDivisioneKey(){
		return iVtClienteDivisione.getKey();
	}

	//iRLineaVen
	public String getRLineaVen() {
		String key = iVtLineaVen.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRLineaVen(String rLineaVen) {
		String key = iVtLineaVen.getKey();
		setVtLineaVenKey(KeyHelper.replaceTokenObjectKey(key, 2, rLineaVen));
	}

	// iVtLineaVen
	public void setVtLineaVen(VtLineaVen vtLineaVen){
		this.iVtLineaVen.setObject(vtLineaVen);
		setDirty();
	}

	public VtLineaVen getVtLineaVen(){
		return (VtLineaVen)iVtLineaVen.getObject();
	}

	public void setVtLineaVenKey(String vtLineaVenKey){
		iVtLineaVen.setKey(vtLineaVenKey);
		setDirty();
	}

	public String getVtLineaVenKey(){
		return iVtSettore.getKey();
	}

	//iRSettore
	public String getRSettore() {
		String key = iVtSettore.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);
	}

	public void setRSettore(String rSettore) {
		String key = iVtSettore.getKey();
		setVtSettoreKey(KeyHelper.replaceTokenObjectKey(key, 2, rSettore));
	}

	// iVtSettore
	public void setVtSettore(VtSettore vtSettore){
		this.iVtSettore.setObject(vtSettore);
		setDirty();
	}

	public VtSettore getVtSettore(){
		return (VtSettore)iVtSettore.getObject();
	}

	public void setVtSettoreKey(String vtSettoreKey){
		iVtSettore.setKey(vtSettoreKey);
		setDirty();
	}

	public String getVtSettoreKey(){
		return iVtSettore.getKey();
	}

	//iRStagione
	public String getRStagione() {
		String key = iVtStagione.getKey();
		//		return KeyHelper.getTokenObjectKey(key, 2);
		return KeyHelper.getTokenObjectKey(key, 1);    //Fix 80660
	}

	public void setRStagione(String rStagione) {
		String key = iVtStagione.getKey();
		//		setVtStagioneKey(KeyHelper.replaceTokenObjectKey(key, 2, rStagione));
		setVtStagioneKey(KeyHelper.replaceTokenObjectKey(key, 1, rStagione));     //Fix 80660
	}

	// iVtStagione
	public void setVtStagione(VtStagione vtStagione){
		this.iVtStagione.setObject(vtStagione);
		setDirty();
	}

	public VtStagione getVtStagione(){
		return (VtStagione)iVtStagione.getObject();
	}

	public void setVtStagioneKey(String vtStagioneKey){
		iVtStagione.setKey(vtStagioneKey);
		setDirty();
	}

	public String getVtStagioneKey(){
		return iVtStagione.getKey();
	}

	//------- Fix - Fine


	//Fix 80717 - Inizio

	//iRZonaVis
	public String getRZonaVis() {
		String key = iZonaVis.getKey();
		return KeyHelper.getTokenObjectKey(key, 2);

	}

	public void setProvvigioneSubagente2(BigDecimal ProvvigioneSubagente)
	{
		this.iProvvigioneSubagente2 = ProvvigioneSubagente;
		setDirty();
	}
	public BigDecimal getProvvigioneSubagente2()
	{
		return iProvvigioneSubagente2;
	}

	public void setProvvigioneSubagente3(BigDecimal ProvvigioneSubagente)
	{
		this.iProvvigioneSubagente3 = ProvvigioneSubagente;
		setDirty();
	}

	public BigDecimal getProvvigioneSubagente3()
	{
		return iProvvigioneSubagente3;
	}

	public void setProvvigioneSubagente4(BigDecimal ProvvigioneSubagente)
	{
		this.iProvvigioneSubagente4 = ProvvigioneSubagente;
		setDirty();
	}

	public BigDecimal getProvvigioneSubagente4()
	{
		return iProvvigioneSubagente4;
	}

	// proxy SUBAGENTE 2 
	public void setSubagente2(Agente Subagente)
	{
		this.iSubagente2.setObject(Subagente);
		setDirty();
	}

	public Agente getSubagente2()
	{
		return (Agente)iSubagente2.getObject();
	}

	public void setSubagente2Key(String key)
	{
		iSubagente2.setKey(key);
		setDirty();
	}

	public String getSubagente2Key()
	{
		return iSubagente2.getKey();
	}

	public void setIdAgenteSub2(String rAgenteSub)
	{
		String key = iSubagente2.getKey();
		iSubagente2.setKey(KeyHelper.replaceTokenObjectKey(key , 2, rAgenteSub));
		setDirty();
	}

	public String getIdAgenteSub2()
	{
		String key = iSubagente2.getKey();
		String objRAgenteSub = KeyHelper.getTokenObjectKey(key,2);
		return objRAgenteSub;
	}

	// proxy SUBAGENTE 3
	public void setSubagente3(Agente Subagente)
	{
		this.iSubagente3.setObject(Subagente);
		setDirty();
	}

	public Agente getSubagente3()
	{
		return (Agente)iSubagente3.getObject();
	}

	public void setSubagente3Key(String key)
	{
		iSubagente3.setKey(key);
		setDirty();
	}

	public String getSubagente3Key()
	{
		return iSubagente3.getKey();
	}

	public void setIdAgenteSub3(String rAgenteSub)
	{
		String key = iSubagente3.getKey();
		iSubagente3.setKey(KeyHelper.replaceTokenObjectKey(key , 2, rAgenteSub));
		setDirty();
	}

	public String getIdAgenteSub3()
	{
		String key = iSubagente3.getKey();
		String objRAgenteSub = KeyHelper.getTokenObjectKey(key,2);
		return objRAgenteSub;
	}

	// proxy SUBAGENTE 4
	public void setSubagente4(Agente Subagente)
	{
		this.iSubagente4.setObject(Subagente);
		setDirty();
	}

	public Agente getSubagente4()
	{
		return (Agente)iSubagente4.getObject();
	}

	public void setSubagente4Key(String key)
	{
		iSubagente4.setKey(key);
		setDirty();
	}

	public String getSubagente4Key()
	{
		return iSubagente4.getKey();
	}

	public void setIdAgenteSub4(String rAgenteSub)
	{
		String key = iSubagente4.getKey();
		iSubagente4.setKey(KeyHelper.replaceTokenObjectKey(key , 2, rAgenteSub));
		setDirty();
	}

	public String getIdAgenteSub4()
	{
		String key = iSubagente4.getKey();
		String objRAgenteSub = KeyHelper.getTokenObjectKey(key,2);
		return objRAgenteSub;
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

	public void setRZonaVis(String RZonaVis) {
		String key = iZonaVis.getKey();
		setZonaVisKey(KeyHelper.replaceTokenObjectKey(key, 2, RZonaVis));
	}

	// iZonaVis
	public void setZonaVis(Zona ZonaVis){
		this.iZonaVis.setObject(ZonaVis);
	}

	public Zona getZonaVis(){
		return (Zona)iZonaVis.getObject();
	}

	public void setZonaVisKey(String ZonaVisKey){
		iZonaVis.setKey(ZonaVisKey);
	}

	public String getZonaVisKey(){
		return iZonaVis.getKey();
	}

	//Fix 80717 - Fine

	// BRY Causale statistica
	

	   public String getCausaleStatisticaKey()
	   {
	      return iCausaleStatistica.getKey();
	   }

	   public void setCausaleStatisticaKey(String key)
	   {
		   iCausaleStatistica.setKey(key);
	   }
	
	   
	   public String getIdCausaleStatistica()
	   {
	     String key = iCausaleStatistica.getKey();
	     return key;
	   }

	    public void setIdCausaleStatistica(String idCausale)
	   {
	    	iCausaleStatistica.setKey(idCausale);
	     setDirty();
	   }
	    
	
	
	// Impostazione dei proxy
	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		VtOrdineVendita vtOrdineVendita = (VtOrdineVendita)obj;
		iVtBuyer.setEqual(vtOrdineVendita.iVtBuyer);
		iVtOpeComm.setEqual(vtOrdineVendita.iVtOpeComm);

		//------- Fix - Inizio
		iVtClienteDivisione.setEqual(vtOrdineVendita.iVtClienteDivisione);
		iVtLineaVen.setEqual(vtOrdineVendita.iVtLineaVen);
		iVtSettore.setEqual(vtOrdineVendita.iVtSettore);
		iVtStagione.setEqual(vtOrdineVendita.iVtStagione);
		//------- Fix - Fine
		iSubagente2.setEqual(vtOrdineVendita.iSubagente2);
		iSubagente3.setEqual(vtOrdineVendita.iSubagente3);
		iSubagente4.setEqual(vtOrdineVendita.iSubagente4);
		//Fix 80717 - Inizio
		iZonaVis.setEqual(vtOrdineVendita.iZonaVis);
		//Fix 80717 - Fine
	}


	// impostazione idAzienda sui proxy
	protected void setCodiceAziendaInternal(String codiceAzienda) {
		String key1 = iVtBuyer.getKey();
		String key2 = iVtOpeComm.getKey();
		iVtBuyer.setKey(KeyHelper.replaceTokenObjectKey(key1, 1, codiceAzienda));
		iVtOpeComm.setKey(KeyHelper.replaceTokenObjectKey(key2, 1, codiceAzienda));
		iSubagente2.setKey(KeyHelper.replaceTokenObjectKey(iSubagente2.getKey(), 1, codiceAzienda));
		iSubagente3.setKey(KeyHelper.replaceTokenObjectKey(iSubagente3.getKey(), 1, codiceAzienda));
		iSubagente4.setKey(KeyHelper.replaceTokenObjectKey(iSubagente4.getKey(), 1, codiceAzienda));

		//------- Fix - Inizio
		String key3 = iVtClienteDivisione.getKey();
		String key4 = iVtLineaVen.getKey();
		String key5 = iVtSettore.getKey();
		String key6 = iVtStagione.getKey();

		iVtClienteDivisione.setKey(KeyHelper.replaceTokenObjectKey(key3, 1, codiceAzienda));
		iVtLineaVen.setKey(KeyHelper.replaceTokenObjectKey(key4, 1, codiceAzienda));
		iVtSettore.setKey(KeyHelper.replaceTokenObjectKey(key5, 1, codiceAzienda));
		iVtStagione.setKey(KeyHelper.replaceTokenObjectKey(key6, 1, codiceAzienda));
		//------- Fix - Fine
		//Fix 80717 - Inizio
		String key7 = iZonaVis.getKey();   
		iZonaVis.setKey(KeyHelper.replaceTokenObjectKey(key7, 1, codiceAzienda));
		//Fix 80717 - Fine

	}


	//Fix 80717 - Inizio
		public int save() throws SQLException {
			ClienteVendita cliFat = this.getClienteFatturazione();
			ClienteVendita cliVen = this.getCliente();
			if(cliFat != null){
				Zona z = cliFat.getCliente().getZona();
				if(z != null){
					this.setZona(z);
				}
				if (cliFat.getCliente().getLingua()!=null && this.getLingua()==null)
					this.setLingua(cliFat.getCliente().getLingua());

			} else if(cliVen != null){
				if (cliVen.getCliente().getLingua()!=null && this.getLingua()==null)
					this.setLingua(cliVen.getCliente().getLingua());
			}
			return super.save();
		}
	//Fix 80717 - Fine
	/**
	 * Recupero le provvigioni subagenti 2, 3 e 4 dal cliente
	 */
	public void completaBO()
	{
		super.completaBO();

		/* Prova 06/03/2017
   		boolean dp = PersDatiVen.getCurrentPersDatiVen().hasDifferenzaPrezzo();

   		setDifferenzaPrezzoSubag2(dp);
   		setDifferenzaPrezzoSubag3(dp);
   		setDifferenzaPrezzoSubag4(dp);
		 */

		String keyCliDiv = KeyHelper.buildObjectKey(new String[]{Azienda.getAziendaCorrente(), getIdCliente(), getIdDivisione()});

		VtClienteVendita cv = (VtClienteVendita) getCliente();
		Cantiere cnt = this.getCantiereTestata();


		if (cnt!=null && cv!=null){
			// non serve qui
		}
		else {
			if (cv != null)
			{

				setIdAgenteSub2(cv.getIdSubAgente2());
				setIdAgenteSub3(cv.getIdSubAgente3());
				setIdAgenteSub4(cv.getIdSubAgente4());

				String aa = cv.getIdAgente();
				String a = cv.getIdSubAgente();

				String aaa = cv.getIdSubAgente2();
				// Fix 80942 this.setProvvigioneSubagente2(cv.getPvgSubAgente2());
				// Fix 80942 this.setProvvigioneSubagente3(cv.getPvgSubAgente3());
				// Fix 80942 this.setProvvigioneSubagente4(cv.getPvgSubAgente4());

				// Fix 80942 - Inizio
				if (cv.getAgente()!=null)
					this.setProvvigioneAgente(cv.getAgente().getPrcProvvigione());

				if (cv.getSubAgente()!=null)
					this.setProvvigioneSubagente(cv.getSubAgente().getPrcProvvigione());

				//if (cv.getIdSubAgente2()!=null)   //Fix 81286
				if (cv.getSubAgente2()!=null)
					this.setProvvigioneSubagente(cv.getSubAgente2().getPrcProvvigione());

				//if (cv.getIdSubAgente3()!=null)   //Fix 81286
				if (cv.getSubAgente3()!=null)
					this.setProvvigioneSubagente(cv.getSubAgente3().getPrcProvvigione());

				//if (cv.getIdSubAgente4()!=null)   //Fix 81286
				if (cv.getSubAgente4()!=null)
					this.setProvvigioneSubagente(cv.getSubAgente4().getPrcProvvigione());

				// Fix 80942 - Fine
			}

		}
	}


	/**
	 * Elimino la verifica che la valuta del listino sia uguale alla valuta della testata.
	 */
	public ErrorMessage checkValute()
	{
		ErrorMessage e = super.checkValute();

		// forzo return senza errori su controllo valuta ordine/listino 
		return null;
	}

	// fix 80731 - Fine
	
	//bry check se  esiste un documento montaggio / smontaggio collegato 
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
					+ VtDocKitOrdVenRigaTM.ID_NUMERO_ORD + "=? ";
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
				//ps.setInt(4, getNumeroRigaDocumento().intValue());

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



	//Fix 80965 - Inizio

	//iRifProduzione
	public  String getRifProduzione() {
		return iRifProduzione;
	}

	public void setRifProduzione(String rifProduzione) {
		this.iRifProduzione = rifProduzione;
		setDirty();
	}
	//Fix 80965 - Fine

	//Fix XXXXX - Inizio

	//iRifCliente
	public  String getRifCliente() {
		return iRifCliente;
	}

	public void setRifCliente(String rifCliente) {
		this.iRifCliente = rifCliente;
		setDirty();
	}
	//Fix XXXXX - Fine

	//Fix 81292 - Inizio
	public String getTipologiaOrdine() {
		return iTipologiaOrdine;
	}

	public void setTipologiaOrdine(String iTipologiaOrdine) {
		this.iTipologiaOrdine = iTipologiaOrdine;
	}
	//Fix 81292 - Fine
	

	/*
	 * FIX 82138 FIRAS : 22/11/2021 : Evadibilita ordine : nuova colonna sulle viste
	 */
		public static final char EVADIBILE_NULL = '0';
		public static final char EVADIBILE_INTERO = '1';
		public static final char EVADIBILE_CON_MANUTENZIONE = '2';
		public static final char EVADIBILE_PARZIALE = '3';
		public static final char NON_EVADIBILE = '4';
		public static final char NON_EVADIBILE_FUTURO = '5';
		
		public static final String TOT_QTY_RIG_PRM 				= "TotQtyRighePrm";
		public static final String TOT_QTY_RIG_PRM_SEC 			= "TotQtyRighePrmSec";
		public static final String TOT_QTY_RIG_PRM_SPED 		= "TotQtyRighePrmSped";
		public static final String TOT_QTY_RIG_PRM_SEC_SPED 	= "TotQtyRighePrmSecSped";
		
		protected char iTipoEvasione = EVADIBILE_NULL;
		
		public char getTipoEvasione() {
			return iTipoEvasione;
		}

		public void setTipoEvasione(char iTipoEvasione) {
			this.iTipoEvasione = iTipoEvasione;
			setDirty();
		}
		
		/*
		 * 01/12/2021 : 4 nuovi campi al JSP OrdVenTestataEstrattoTotale
		 */
		
		/**
		 * SOMMA Righe primarie
		 */
		protected BigDecimal iTotQtyRighePrm = new BigDecimal("0");
		public BigDecimal getTotQtyRighePrm() {
			return iTotQtyRighePrm;
		}

		public void setTotQtyRighePrm(BigDecimal iTotQtyRighePrm) {
			this.iTotQtyRighePrm= iTotQtyRighePrm;
		}
		
		/**
		 * SOMMA Righe primarie no kit + Righe secondarie dei kit (tot pezzi - tot pezzi sfusi)
		 */
		protected BigDecimal iTotQtyRighePrmSec = new BigDecimal("0");
		public BigDecimal getTotQtyRighePrmSec() {
			return iTotQtyRighePrmSec;
		}

		public void setTotQtyRighePrmSec(BigDecimal iTotQtyRighePrmSec) {
			this.iTotQtyRighePrmSec= iTotQtyRighePrmSec;
		}

		/**
		 * SOMMA Righe primarie spedite oppure in attesa spedizione
		 */
		protected BigDecimal iTotQtyRighePrmSped = new BigDecimal("0");
		public BigDecimal getTotQtyRighePrmSped() {
			return iTotQtyRighePrmSped;
		}

		public void setTotQtyRighePrmSped(BigDecimal iTotQtyRighePrmSped) {
			this.iTotQtyRighePrmSped= iTotQtyRighePrmSped;
		}
		
		/**
		 *  SOMMA Righe primarie no kit + Righe secondarie dei kit (tot pezzi - tot pezzi sfusi)
		 *  spedite oppure in attesa spedizione
		 */
		protected BigDecimal iTotQtyRighePrmSecSped = new BigDecimal("0");
		public BigDecimal getTotQtyRighePrmSecSped() {
			return iTotQtyRighePrmSecSped;
		}

		public void setTotQtyRighePrmSecSped(BigDecimal iTotQtyRighePrmSecSped) {
			this.iTotQtyRighePrmSecSped= iTotQtyRighePrmSecSped;
		}
		
	//FIX 82138 FINE	
}
