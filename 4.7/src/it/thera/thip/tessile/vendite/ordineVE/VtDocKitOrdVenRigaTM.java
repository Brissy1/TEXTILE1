/*
 * @(#)VtDocKitOrdVenRigaTM.java
 */

/**
 * VtDocKitOrdVenRigaTM
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
import com.thera.thermfw.common.*;
import java.sql.*;
import com.thera.thermfw.base.*;

public class VtDocKitOrdVenRigaTM extends TableManager {

  
  /**
   * Attributo ID_ANNO_ORD
   */
  public static final String ID_ANNO_ORD = "ID_ANNO_ORD";

  /**
   * Attributo ID_NUMERO_ORD
   */
  public static final String ID_NUMERO_ORD = "ID_NUMERO_ORD";

  /**
   * Attributo ID_RIGA_ORD
   */
  public static final String ID_RIGA_ORD = "ID_RIGA_ORD";

  /**
   * Attributo ID_ANNO_DOC
   */
  public static final String ID_ANNO_DOC = "ID_ANNO_DOC";

  /**
   * Attributo ID_NUMERO_DOC
   */
  public static final String ID_NUMERO_DOC = "ID_NUMERO_DOC";

  /**
   * Attributo TIPO
   */
  public static final String TIPO = "TIPO";

  /**
   * Attributo GRUPPO_TC
   */
  public static final String GRUPPO_TC = "GRUPPO_TC";

  /**
   * Attributo ID_AZIENDA
   */
  public static final String ID_AZIENDA = "ID_AZIENDA";

  /**
   * Attributo TIMESTAMP
   */
  public static final String TIMESTAMP = "TIMESTAMP";

  /**
   *  TABLE_NAME
   */
  public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "SI_DOC_KIT_ORD_VEN_RIGA_V01";

  /**
   *  instance
   */
  private static TableManager cInstance;

  /**
   *  CLASS_NAME
   */
  private static final String CLASS_NAME = it.thera.thip.tessile.vendite.ordineVE.VtDocKitOrdVenRiga.class.getName();

  
  /**
   *  getInstance
   * @return TableManager
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public synchronized static TableManager getInstance() throws SQLException {
    if (cInstance == null) {
      cInstance = (TableManager)Factory.createObject(VtDocKitOrdVenRigaTM.class);
    }
    return cInstance;
  }

  /**
   *  VtDocKitOrdVenRigaTM
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public VtDocKitOrdVenRigaTM() throws SQLException {
    super();
  }

  /**
   *  initialize
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  protected void initialize() throws SQLException {
    setTableName(TABLE_NAME);
    setObjClassName(CLASS_NAME);
    init();
  }

  /**
   *  initializeRelation
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  protected void initializeRelation() throws SQLException {
    super.initializeRelation();
    addAttribute("IdAnnoOrd", ID_ANNO_ORD);
    addAttribute("IdNumeroOrd", ID_NUMERO_ORD);
    addAttribute("IdRigaOrd", ID_RIGA_ORD, "getIntegerObject");
    addAttribute("IdAnnoDoc", ID_ANNO_DOC);
    addAttribute("IdNumeroDoc", ID_NUMERO_DOC);
    addAttribute("Tipo", TIPO);
    addAttribute("GruppoTc", GRUPPO_TC, "getIntegerObject");
    addAttribute("IdAzienda", ID_AZIENDA);
    addTimestampAttribute("Timestamp" , TIMESTAMP);
    setKeys(ID_AZIENDA + "," + ID_ANNO_ORD + "," + ID_NUMERO_ORD + "," + ID_ANNO_DOC + "," + ID_NUMERO_DOC + "," + TIPO);
  }

  /**
   *  init
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/11/2021    Wizard     Codice generato da Wizard
   *
   */
  private void init() throws SQLException {
    configure(ID_ANNO_ORD + ", " + ID_NUMERO_ORD + ", " + ID_RIGA_ORD + ", " + ID_ANNO_DOC
         + ", " + ID_NUMERO_DOC + ", " + TIPO + ", " + GRUPPO_TC + ", " + ID_AZIENDA
         + ", " + TIMESTAMP);
  }

}

