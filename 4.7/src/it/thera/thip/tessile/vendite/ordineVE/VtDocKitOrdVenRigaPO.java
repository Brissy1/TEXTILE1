/*
 * @(#)VtDocKitOrdVenRigaPO.java
 */

/**
 * null
 *
 * <br></br><b>Copyright (C) : Thera SpA</b>
 * @author Wizard 03/11/2021 at 10:03:09
 */
/*
 * Revisions:
 * Date          Owner      Description
 * 03/11/2021    Wizard     Codice generato da Wizard
 *
 */
package it.thera.thip.tessile.vendite.ordineVE;
import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import com.thera.thermfw.common.*;
import it.thera.thip.base.azienda.Azienda;
import com.thera.thermfw.security.*;

public abstract class VtDocKitOrdVenRigaPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, Conflictable {

  
  /**
   *  instance
   */
  private static VtDocKitOrdVenRiga cInstance;

  /**
   * Attributo iIdAnnoOrd
   */
  protected String iIdAnnoOrd;

  /**
   * Attributo iIdNumeroOrd
   */
  protected String iIdNumeroOrd;

  /**
   * Attributo iIdRigaOrd
   */
  protected Integer iIdRigaOrd;

  /**
   * Attributo iIdAnnoDoc
   */
  protected String iIdAnnoDoc;

  /**
   * Attributo iIdNumeroDoc
   */
  protected String iIdNumeroDoc;

  /**
   * Attributo iTipo
   */
  protected String iTipo = "DA";

  /**
   * Attributo iGruppoTc
   */
  protected Integer iGruppoTc;

  /**
   * Attributo iIdAzienda
   */
  protected String iIdAzienda;

  /**
   * Attributo iTimestamp
   */
  protected java.sql.Timestamp iTimestamp;

  
  /**
   *  retrieveList
   * @param where
   * @param orderBy
   * @param optimistic
   * @return Vector
   * @throws SQLException
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public static Vector retrieveList(String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    if (cInstance == null)
      cInstance = (VtDocKitOrdVenRiga)Factory.createObject(VtDocKitOrdVenRiga.class);
    return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
  }

  /**
   *  elementWithKey
   * @param key
   * @param lockType
   * @return VtDocKitOrdVenRiga
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public static VtDocKitOrdVenRiga elementWithKey(String key, int lockType) throws SQLException {
    return (VtDocKitOrdVenRiga)PersistentObject.elementWithKey(VtDocKitOrdVenRiga.class, key, lockType);
  }

  /**
   * VtDocKitOrdVenRigaPO
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public VtDocKitOrdVenRigaPO() {
    setTipo("DA");
    setIdAzienda(Azienda.getAziendaCorrente());
  }

  /**
   * Valorizza l'attributo. 
   * @param idAnnoOrd
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdAnnoOrd(String idAnnoOrd) {
    this.iIdAnnoOrd = idAnnoOrd;
    setDirty();
    setOnDB(false);
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdAnnoOrd() {
    return iIdAnnoOrd;
  }

  /**
   * Valorizza l'attributo. 
   * @param idNumeroOrd
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdNumeroOrd(String idNumeroOrd) {
    this.iIdNumeroOrd = idNumeroOrd;
    setDirty();
    setOnDB(false);
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdNumeroOrd() {
    return iIdNumeroOrd;
  }

  /**
   * Valorizza l'attributo. 
   * @param idRigaOrd
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdRigaOrd(Integer idRigaOrd) {
    this.iIdRigaOrd = idRigaOrd;
    setDirty();
  }

  /**
   * Restituisce l'attributo. 
   * @return Integer
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public Integer getIdRigaOrd() {
    return iIdRigaOrd;
  }

  /**
   * Valorizza l'attributo. 
   * @param idAnnoDoc
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdAnnoDoc(String idAnnoDoc) {
    this.iIdAnnoDoc = idAnnoDoc;
    setDirty();
    setOnDB(false);
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdAnnoDoc() {
    return iIdAnnoDoc;
  }

  /**
   * Valorizza l'attributo. 
   * @param idNumeroDoc
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdNumeroDoc(String idNumeroDoc) {
    this.iIdNumeroDoc = idNumeroDoc;
    setDirty();
    setOnDB(false);
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdNumeroDoc() {
    return iIdNumeroDoc;
  }

  /**
   * Valorizza l'attributo. 
   * @param tipo
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setTipo(String tipo) {
    this.iTipo = tipo;
    setDirty();
    setOnDB(false);
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getTipo() {
    return iTipo;
  }

  /**
   * Valorizza l'attributo. 
   * @param gruppoTc
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setGruppoTc(Integer gruppoTc) {
    this.iGruppoTc = gruppoTc;
    setDirty();
  }

  /**
   * Restituisce l'attributo. 
   * @return Integer
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public Integer getGruppoTc() {
    return iGruppoTc;
  }

  /**
   * Valorizza l'attributo. 
   * @param idAzienda
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdAzienda(String idAzienda) {
    this.iIdAzienda = idAzienda;
    setDirty();
    setOnDB(false);
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdAzienda() {
    return iIdAzienda;
  }

  /**
   * Valorizza l'attributo. 
   * @param timestamp
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setTimestamp(java.sql.Timestamp timestamp) {
    this.iTimestamp = timestamp;
    
  }

  /**
   * Restituisce l'attributo. 
   * @return java.sql.Timestamp
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public java.sql.Timestamp getTimestamp() {
    return iTimestamp;
  }

  /**
   * setEqual
   * @param obj
   * @throws CopyException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setEqual(Copyable obj) throws CopyException {
    super.setEqual(obj);
    VtDocKitOrdVenRigaPO vtDocKitOrdVenRigaPO = (VtDocKitOrdVenRigaPO)obj;
    if (vtDocKitOrdVenRigaPO.iTimestamp != null)
        iTimestamp = (java.sql.Timestamp)vtDocKitOrdVenRigaPO.iTimestamp.clone();
  }

  /**
   * checkAll
   * @param components
   * @return Vector
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public Vector checkAll(BaseComponentsCollection components) {
    Vector errors = new Vector();
    components.runAllChecks(errors);
    return errors;
  }

  /**
   *  setKey
   * @param key
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setKey(String key) {
    setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
    setIdAnnoOrd(KeyHelper.getTokenObjectKey(key, 2));
    setIdNumeroOrd(KeyHelper.getTokenObjectKey(key, 3));
    setIdAnnoDoc(KeyHelper.getTokenObjectKey(key, 4));
    setIdNumeroDoc(KeyHelper.getTokenObjectKey(key, 5));
    setTipo(KeyHelper.getTokenObjectKey(key, 6));
  }

  /**
   *  getKey
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getKey() {
    String idAzienda = getIdAzienda();
    String idAnnoOrd = getIdAnnoOrd();
    String idNumeroOrd = getIdNumeroOrd();
    String idAnnoDoc = getIdAnnoDoc();
    String idNumeroDoc = getIdNumeroDoc();
    String tipo = getTipo();
    Object[] keyParts = {idAzienda, idAnnoOrd, idNumeroOrd, idAnnoDoc, idNumeroDoc, tipo};
    return KeyHelper.buildObjectKey(keyParts);
  }

  /**
   * isDeletable
   * @return boolean
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public boolean isDeletable() {
    return checkDelete() == null;
  }

  /**
   * toString
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  public String toString() {
    return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
  }

  /**
   *  getTableManager
   * @return TableManager
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  protected TableManager getTableManager() throws SQLException {
    return VtDocKitOrdVenRigaTM.getInstance();
  }

}

