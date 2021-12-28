/*
 * @(#)VtCausaleSaldoManualeRigaTM.java
 */

/**
 * VtCausaleSaldoManualeRigaTM
 *
 * <br></br><b>Copyright (C) : Thera SpA</b>
 * @author Wizard 03/12/2021 at 10:54:17
 */
/*
 * Revisions:
 * Date          Owner      Description
 * 03/12/2021    Wizard     Codice generato da Wizard
 *
 */
package it.thera.thip.tessile.vendite.ordineVE;
import com.thera.thermfw.persist.*;
import com.thera.thermfw.common.*;
import java.sql.*;
import com.thera.thermfw.base.*;

public class VtCausaleSaldoManualeRigaTM extends TableManager {

  
  /**
   * Attributo IDAZIENDA
   */
  public static final String IDAZIENDA = "IDAZIENDA";

  /**
   * Attributo CODICEDESCRIZIONE
   */
  public static final String CODICEDESCRIZIONE = "CODICEDESCRIZIONE";

  /**
   * Attributo ID_CAU_SALDO_MANUALE_RIGA
   */
  public static final String ID_CAU_SALDO_MANUALE_RIGA = "ID_CAU_SALDO_MANUALE_RIGA";

  /**
   * Attributo TIMESTAMP
   */
  public static final String TIMESTAMP = "TIMESTAMP";

  /**
   *  TABLE_NAME
   */
  public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "SI_CAU_SALDO_MAN_ORDVENRIGA";

  /**
   *  instance
   */
  private static TableManager cInstance;

  /**
   *  CLASS_NAME
   */
  private static final String CLASS_NAME = it.thera.thip.tessile.vendite.ordineVE.VtCausaleSaldoManualeRiga.class.getName();

  
  /**
   *  getInstance
   * @return TableManager
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/12/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public synchronized static TableManager getInstance() throws SQLException {
    if (cInstance == null) {
      cInstance = (TableManager)Factory.createObject(VtCausaleSaldoManualeRigaTM.class);
    }
    return cInstance;
  }

  /**
   *  VtCausaleSaldoManualeRigaTM
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/12/2021    CodeGen     Codice generato da CodeGenerator
   *
   */
  public VtCausaleSaldoManualeRigaTM() throws SQLException {
    super();
  }

  /**
   *  initialize
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/12/2021    CodeGen     Codice generato da CodeGenerator
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
   * 03/12/2021    Wizard     Codice generato da Wizard
   *
   */
  protected void initializeRelation() throws SQLException {
    super.initializeRelation();
    addAttribute("Idazienda", IDAZIENDA);
    addAttribute("Codicedescrizione", CODICEDESCRIZIONE);
    addAttribute("IdCauSaldoManualeRiga", ID_CAU_SALDO_MANUALE_RIGA);
    addTimestampAttribute("Timestamp" , TIMESTAMP);
    setKeys(IDAZIENDA + "," + ID_CAU_SALDO_MANUALE_RIGA);
  }

  /**
   *  init
   * @throws SQLException
   */
  /*
   * Revisions:
   * Date          Owner      Description
   * 03/12/2021    Wizard     Codice generato da Wizard
   *
   */
  private void init() throws SQLException {
    configure(IDAZIENDA + ", " + CODICEDESCRIZIONE + ", " + ID_CAU_SALDO_MANUALE_RIGA + ", " + TIMESTAMP
        );
  }

}

