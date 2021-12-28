/*
 * @(#)VtCausaleStatisticaPO.java
 */

/**
 * null
 *
 * <br></br><b>Copyright (C) : Thera SpA</b>
 * @author Wizard 07/12/2021 at 10:46:29
 */
/*
 * Revisions:
 * Date          Owner      Description
 * 07/12/2021    Wizard     Codice generato da Wizard
 *
 */
package it.thera.thip.tessile.vendite.ordineVE;
import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import com.thera.thermfw.common.*;
import it.thera.thip.base.azienda.Azienda;
import com.thera.thermfw.security.*;

public abstract class VtCausaleStatisticaPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, Conflictable {

  
  /**
   *  instance
   */
  private static VtCausaleStatistica cInstance;

  /**
   * Attributo iIdazienda
   */
  protected String iIdazienda;

  /**
   * Attributo iCodicedescrizione
   */
  protected String iCodicedescrizione;

  /**
   * Attributo iIdCauStatistica
   */
  protected String iIdCauStatistica;

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
   * 07/12/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public static Vector retrieveList(String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    if (cInstance == null)
      cInstance = (VtCausaleStatistica)Factory.createObject(VtCausaleStatistica.class);
    return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
  }

  /**
   *  elementWithKey
   * @param key
   * @param lockType
   * @return VtCausaleStatistica
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public static VtCausaleStatistica elementWithKey(String key, int lockType) throws SQLException {
    return (VtCausaleStatistica)PersistentObject.elementWithKey(VtCausaleStatistica.class, key, lockType);
  }

  /**
   * VtCausaleStatisticaPO
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public VtCausaleStatisticaPO() {
    setIdazienda(Azienda.getAziendaCorrente());
  }

  /**
   * Valorizza l'attributo. 
   * @param idazienda
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdazienda(String idazienda) {
    this.iIdazienda = idazienda;
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
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdazienda() {
    return iIdazienda;
  }

  /**
   * Valorizza l'attributo. 
   * @param codicedescrizione
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setCodicedescrizione(String codicedescrizione) {
    this.iCodicedescrizione = codicedescrizione;
    setDirty();
  }

  /**
   * Restituisce l'attributo. 
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getCodicedescrizione() {
    return iCodicedescrizione;
  }

  /**
   * Valorizza l'attributo. 
   * @param idCauStatistica
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setIdCauStatistica(String idCauStatistica) {
    this.iIdCauStatistica = idCauStatistica;
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
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getIdCauStatistica() {
    return iIdCauStatistica;
  }

  /**
   * Valorizza l'attributo. 
   * @param timestamp
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
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
   * 07/12/2021    Wizard     Codice generato da Wizard
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
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setEqual(Copyable obj) throws CopyException {
    super.setEqual(obj);
    VtCausaleStatisticaPO vtCausaleStatisticaPO = (VtCausaleStatisticaPO)obj;
    if (vtCausaleStatisticaPO.iTimestamp != null)
        iTimestamp = (java.sql.Timestamp)vtCausaleStatisticaPO.iTimestamp.clone();
  }

  /**
   * checkAll
   * @param components
   * @return Vector
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
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
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public void setKey(String key) {
    setIdazienda(KeyHelper.getTokenObjectKey(key, 1));
    setIdCauStatistica(KeyHelper.getTokenObjectKey(key, 2));
  }

  /**
   *  getKey
   * @return String
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
   *
   */
  public String getKey() {
    String idazienda = getIdazienda();
    String idCauStatistica = getIdCauStatistica();
    Object[] keyParts = {idazienda, idCauStatistica};
    return KeyHelper.buildObjectKey(keyParts);
  }

  /**
   * isDeletable
   * @return boolean
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 07/12/2021    Wizard     Codice generato da Wizard
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
   * 07/12/2021    Wizard     Codice generato da Wizard
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
   * 07/12/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  protected TableManager getTableManager() throws SQLException {
    return VtCausaleStatisticaTM.getInstance();
  }

}

