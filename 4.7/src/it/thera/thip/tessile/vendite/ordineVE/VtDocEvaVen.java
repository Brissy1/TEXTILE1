package it.thera.thip.tessile.vendite.ordineVE;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CopyException;
import com.thera.thermfw.persist.Copyable;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.Proxy;
import com.thera.thermfw.persist.TableManager;

import it.thera.thip.base.agentiProvv.Agente;
import it.thera.thip.base.cliente.ClienteVendita;
import it.thera.thip.base.partner.Indirizzo;
import it.thera.thip.cs.ThipException;
import it.thera.thip.tessile.cliente.VtClienteDivisione;
import it.thera.thip.tessile.tabelle.VtLineaVen;
import it.thera.thip.tessile.tabelle.VtSettore;
import it.thera.thip.tessile.tabelle.VtStagione;
import it.thera.thip.vendite.ordineVE.DocEvaVen;
import it.thera.thip.vendite.ordineVE.OrdineVendita;

/**
 * <p>Title: </p>
 * <p>Description: </p>	Estensione classe standard per aggiunta campi subagente e relative provvigioni
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: SI Consulting</p>
 * @author Andrea Calligaro
 * @version 1.0
 */
/*
 * Number  Date          Author     Description
 */

public class VtDocEvaVen extends DocEvaVen  { 

	protected BigDecimal iProvvigioneSubagente2      = new BigDecimal("0");
	protected BigDecimal iProvvigioneSubagente3      = new BigDecimal("0");
	protected BigDecimal iProvvigioneSubagente4      = new BigDecimal("0");
	protected Proxy iSubagente2               = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iSubagente3               = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);
	protected Proxy iSubagente4               = new Proxy(it.thera.thip.base.agentiProvv.Agente.class);

	private boolean iDifferenzaPrezzoSubag2    = false;
	private boolean iDifferenzaPrezzoSubag3    = false;
	private boolean iDifferenzaPrezzoSubag4    = false;

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

	//inizio fix 80795
	protected String iRDivisioneCliente;
	protected String iRLineaVen;
	protected String iRSettore;
	protected String iRStagione;

	protected Proxy iVtClienteDivisione = new Proxy(it.thera.thip.tessile.cliente.VtClienteDivisione.class);
	protected Proxy iVtLineaVen = new Proxy(it.thera.thip.tessile.tabelle.VtLineaVen.class);
	protected Proxy iVtSettore = new Proxy(it.thera.thip.tessile.tabelle.VtSettore.class);
	protected Proxy iVtStagione = new Proxy(it.thera.thip.tessile.tabelle.VtStagione.class);

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


	// fine fix 80795

// fix 80958  inizio
	  private boolean iMostraRigheQtaAppartate;

	  public boolean getMostraRigheQtaAppartate() {
	    return this.iMostraRigheQtaAppartate;
	  }

	  public void setMostraRigheQtaAppartate(boolean mostra) {
	    this.iMostraRigheQtaAppartate = mostra;
	  }
//fix 80958   fine

	public void setEqual(Copyable obj) throws CopyException
	{
		super.setEqual(obj);
		VtDocEvaVen docEvaVen = (VtDocEvaVen)obj;
		iSubagente2.setEqual(docEvaVen.iSubagente2);
		iSubagente3.setEqual(docEvaVen.iSubagente3);
		iSubagente4.setEqual(docEvaVen.iSubagente4);


	}


	/**
	 * setIdAziendaInternal
	 * @param idAzienda
	 */
	protected void setIdAziendaInternal(String idAzienda)
	{
		super.setIdAziendaInternal(idAzienda);
		iSubagente2.setKey(KeyHelper.replaceTokenObjectKey(iSubagente2.getKey(), 1, idAzienda));
		iSubagente3.setKey(KeyHelper.replaceTokenObjectKey(iSubagente3.getKey(), 1, idAzienda));
		iSubagente4.setKey(KeyHelper.replaceTokenObjectKey(iSubagente4.getKey(), 1, idAzienda));
	}

	protected void aggiornaAttributiDaOrdine(OrdineVendita ordine) {
		super.aggiornaAttributiDaOrdine(ordine);
		if (!isOnDB()) {

			this.setIdAgenteSub2(((VtOrdineVendita) ordine).getIdAgenteSub2());
			this.setIdAgenteSub3(((VtOrdineVendita) ordine).getIdAgenteSub3());
			this.setIdAgenteSub4(((VtOrdineVendita) ordine).getIdAgenteSub4());

			this.setProvvigioneSubagente2(((VtOrdineVendita) ordine).getProvvigioneSubagente2());
			this.setProvvigioneSubagente3(((VtOrdineVendita) ordine).getProvvigioneSubagente3());
			this.setProvvigioneSubagente4(((VtOrdineVendita) ordine).getProvvigioneSubagente4());

			this.setDifferenzaPrezzoSubag2(((VtOrdineVendita) ordine).hasDifferenzaPrezzoSubag2());
			this.setDifferenzaPrezzoSubag3(((VtOrdineVendita) ordine).hasDifferenzaPrezzoSubag3());
			this.setDifferenzaPrezzoSubag4(((VtOrdineVendita) ordine).hasDifferenzaPrezzoSubag4());

			// inizio fix 80795
			this.setRDivisioneCliente(((VtOrdineVendita) ordine).getRDivisioneCliente());
			this.setRSettore(((VtOrdineVendita) ordine).getRSettore());
			this.setRStagione(((VtOrdineVendita) ordine).getRStagione());
			this.setRLineaVen(((VtOrdineVendita) ordine).getRLineaVen());
			// fine fix 80795
			
			 // BRY Get Nota by Indirizzo.getNota altrimenti cliente.GetNota
	        this.setNota(getNotaFromOrdine((VtOrdineVendita) ordine));
		}

	}

	protected TableManager getTableManager() throws java.sql.SQLException {
		return VtDocEvaVenTM.getInstance();
	}
	
	
	// Fix 80893 - Inizio
	/**
	   * Esegue un controllo generale per definire se il documento è pronto per essere
	   * evaso o salvato.
	   * @return una lista piena di ErrorMessage, altrimenti lista vuota
	   */
	  public List checkGeneraleDocumento() throws ThipException {

	    Vector errors = new Vector();
	    ErrorMessage em = null;
	    em = this.checkRigheDocumento();
	    if (em != null) {
	      errors.add(em);
	    }
	    em = this.checkStatoAvanzamento(true);
	    if (this.isConfermaEvasione() && em != null) {
	      errors.add(em);
	    }
	    if (errors.isEmpty()) {
	      // fix 11120 >
	      //errors.addAll(this.ckeckControlloDisp());
	      errors.addAll(this.ckeckControlloDisp(errors));
	      // fix 11120 <
	    }
	    if (errors.isEmpty()) {
	      errors.addAll(this.checkCondCompEvas());
	    }
	    if (errors.isEmpty()) {
	      errors.addAll(this.checkGruppoConsegna());
	    }
	    if (errors.isEmpty()) {
	      errors.addAll(this.checkGruppoOrdineIntero());
	    }
	    if (errors.isEmpty()) {
	      errors.addAll(this.checkRaggruppamentoBolle());
	    }
	    //Fix11178 inizio
	    /*  // Fix 80893 - Inizio
	    if(errors.isEmpty()){
	      setCheckValutaAbilitato(true);
	      em = checkValute();
	      if (em != null)
	    	  errors.add(em);
	    }
	    */ // Fix 80893 - Fine
	    //Fix11178 fine
	    return errors;
	  }
	  //Fix 80893 - Fine

	  public String getNotaFromOrdine(VtOrdineVendita ordInUso) {
			
			String nota = ordInUso.getNota();
			
			ClienteVendita cliente = ordInUso.getCliente();

			Indirizzo indirizzoOrd =ordInUso.getIndirizzo();
			
			if (indirizzoOrd != null && indirizzoOrd.getNote() != null && !indirizzoOrd.getNote().trim().equals(""))
				nota = indirizzoOrd.getNote();
			else if(cliente.getNota()!=null && !cliente.getNota().trim().equals(""))
				nota = cliente.getNota();

			
			return nota;
		}
}
