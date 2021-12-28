package it.thera.thip.tessile.vendite.ordineVE;

import java.math.BigDecimal;

import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.Proxy;

import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.documenti.StatoAvanzamento;

public class VtOrdVenRigaPrmMontSmontK extends VtOrdineVenditaRigaPrm{
	public static final String ORD_VEN_MONT_SMONT = "VtOrdVenRigaPrmMontSmontK";
	
	private BigDecimal iQuantita;
	protected Proxy iMagazzino = new Proxy(it.thera.thip.base.azienda.Magazzino.class); 
	protected Proxy iMagazzinoPart = new Proxy(it.thera.thip.base.azienda.Magazzino.class); 
	protected char iTipoMontSmont =VT_TipoMontSmontKit.ESTERNO;
	
	  protected String getClassAdCollectionName() {
	    return ORD_VEN_MONT_SMONT;
	  }
	
	  
		
		public BigDecimal getQtaKit() {
			return iQuantita;
		}

		public void setQtaKit(BigDecimal iQuantita) {
			this.iQuantita = iQuantita;
		}
		
		 //MAGAZZINO 

		  /**
		   * Restituisce il Proxy Magazzino .
		   */
		  public Magazzino getMagazzinoKit() {
		    return (Magazzino)iMagazzino.getObject();
		  }


		  /**
		   * Valorizza il Proxy Magazzino.
		   */
		  public void setMagazzinoKit(Magazzino iMagazzino) {
		    this.iMagazzino.setObject(iMagazzino);
		    //setDirty();
		  }
		  
		  

		  /**
		   * Restituisce la chiave del Proxy Magazzino.
		   */
		  public String getMagazzinoKitKey() {
		    return iMagazzino.getKey();
		  }


		  /**
		   * Valorizza la chiave del Proxy Magazzino .
		   */
		  public void setMagazzinoKitKey(String key) {
			  iMagazzino.setKey(key);
		   // setDirty();
		  }


		  /**
		   * Restituisce l'attributo relativo al Proxy Magazzino .
		   */
		  public String getIdMagazzinoKit() {
		    String key = iMagazzino.getKey();
		    String rMagazzino = KeyHelper.getTokenObjectKey(key,2);
		    return rMagazzino;
		  }


		  /**
		   * Valorizza l'attributo relativo al Proxy Magazzino .
		   */
		  public void setIdMagazzinoKit(String rMagazzino) {
		    String key = iMagazzino.getKey();
		    iMagazzino.setKey(KeyHelper.replaceTokenObjectKey(key , 2, rMagazzino));
		    //setDirty();
		  }
		
		  

		  /**
		   * Restituisce l'attributo relativo al Proxy Magazzino .
		   */
		  public String getIdMagazzinoPart() {
		    String key = iMagazzinoPart.getKey();
		    String rMagazzino = KeyHelper.getTokenObjectKey(key,2);
		    return rMagazzino;
		  }


		  /**
		   * Valorizza l'attributo relativo al Proxy Magazzino .
		   */
		  public void setIdMagazzinoPart(String rMagazzino) {
		    String key = iMagazzinoPart.getKey();
		    iMagazzinoPart.setKey(KeyHelper.replaceTokenObjectKey(key , 2, rMagazzino));
		    //setDirty();
		  }
		
		  
		  //MAGAZZINO  KIT

		  /**
		   * Restituisce il Proxy Magazzino .
		   */
		  public Magazzino getRMagazzinoKit() {
		    return (Magazzino)iMagazzino.getObject();
		  }


		  /**
		   * Valorizza il Proxy Magazzino.
		   */
		  public void setRMagazzinoKit(Magazzino iMagazzino) {
		    this.iMagazzino.setObject(iMagazzino);
		    //setDirty();
		  }


		  /**
		   * Restituisce la chiave del Proxy Magazzino.
		   */
		  public String getRMagazzinoKitKey() {
		    return iMagazzino.getKey();
		  }


		  /**
		   * Valorizza la chiave del Proxy Magazzino .
		   */
		  public void setRMagazzinoKitKey(String key) {
			  iMagazzino.setKey(key);
		   // setDirty();
		  }


		  

		  
		  //MAGAZZINO  KIT

		  /**
		   * Restituisce il Proxy Magazzino .
		   */
		  public Magazzino getRMagazzinoPart() {
		    return (Magazzino)iMagazzinoPart.getObject();
		  }


		  /**
		   * Valorizza il Proxy Magazzino.
		   */
		  public void setRMagazzinoPart(Magazzino iMagazzino) {
		    this.iMagazzinoPart.setObject(iMagazzino);
		    //setDirty();
		  }


		  /**
		   * Restituisce la chiave del Proxy Magazzino.
		   */
		  public String getRMagazzinoPartKey() {
		    return iMagazzinoPart.getKey();
		  }


		  /**
		   * Valorizza la chiave del Proxy Magazzino .
		   */
		  public void setRMagazzinoPartKey(String key) {
			  iMagazzinoPart.setKey(key);
		   // setDirty();
		  }


		  
		  public char getTipoMontSmont() {
			    return iTipoMontSmont;
			  }


			  public void setTipoMontSmont(char tipoMontSmont) {
			    this.iTipoMontSmont = tipoMontSmont;
			   //setDirty();
			  }

	  
	  
}
