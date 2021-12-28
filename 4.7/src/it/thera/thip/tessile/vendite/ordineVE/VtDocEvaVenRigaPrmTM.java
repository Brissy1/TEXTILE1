package it.thera.thip.tessile.vendite.ordineVE;

import it.thera.thip.vendite.ordineVE.DocEvaVenRigaPrmTM;

import com.thera.thermfw.base.SystemParam;
import com.thera.thermfw.persist.*;
import java.sql.*;

/**
 * Title:
 * Description:	Implementazione DocEvaVenTM per nuovi subagenti e relative descrizioni
 * Copyright:    Copyright (c) 2016
 * Company:		SI Consulting
 * @author		Andrea Calligaro
 * @version 1.0
 */
/**
 * Revisions:
 * Number  Date         Owner  Description
 * 
 */

public class VtDocEvaVenRigaPrmTM extends DocEvaVenRigaPrmTM {

	private static final String CLASS_NAME = VtDocEvaVenRigaPrm.class.getName();

	private static TableManagerPool cInstance;

	public static final String R_AGENTE_SUB2 = "R_AGENTE_SUB2";
	public static final String R_AGENTE_SUB3 = "R_AGENTE_SUB3";
	public static final String R_AGENTE_SUB4 = "R_AGENTE_SUB4";
	public static final String PVG_1_SUB_AGENTE2 = "PVG_1_SUB_AGENTE2";
	public static final String PVG_2_SUB_AGENTE2 = "PVG_2_SUB_AGENTE2";
	public static final String PVG_1_SUB_AGENTE3 = "PVG_1_SUB_AGENTE3";
	public static final String PVG_2_SUB_AGENTE3 = "PVG_2_SUB_AGENTE3";
	public static final String PVG_1_SUB_AGENTE4 = "PVG_1_SUB_AGENTE4";
	public static final String PVG_2_SUB_AGENTE4 = "PVG_2_SUB_AGENTE4";
	public static final String DIFF_PRZ_SUB_AGE2 = "DIFF_PRZ_SUB_AGE2";
	public static final String DIFF_PRZ_SUB_AGE3 = "DIFF_PRZ_SUB_AGE3";
	public static final String DIFF_PRZ_SUB_AGE4 = "DIFF_PRZ_SUB_AGE4";

	//fix 80795
	public static final String R_DIVISIONE_CLIENTE = "R_DIVISIONE_CLIENTE";
	public static final String R_SETTORE = "R_SETTORE";
	public static final String R_STAGIONE = "R_STAGIONE";

	//Fix 81250
	public static final String GRUPPO_TC = "GRUPPO_TC";


	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIP") + "VT_DOCVEN_RIG_PRM_V01";


	/**
	 * Costruttore
	 */
	public VtDocEvaVenRigaPrmTM() throws SQLException {
		super();
	}

	public synchronized static TableManager getInstance() throws SQLException {

		if (cInstance == null)
			cInstance = new TableManagerPool(VtDocEvaVenRigaPrmTM.class);
		return (VtDocEvaVenRigaPrmTM)cInstance.getTableManager();

	}


	public void free() {
		if (cInstance != null)
			cInstance.putTableManager(this);
	}

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(VtDocEvaVenRigaPrm.class.getName());
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		String colList = "ID_AZIENDA,ID_ANNO_DOC, ID_NUMERO_DOC, ID_RIGA_DOC,"
				+ "R_AGENTE_SUB2,PVG_1_SUB_AGENTE2,PVG_2_SUB_AGENTE2,DIFF_PRZ_SUB_AGE2,"
				+ "R_AGENTE_SUB3,PVG_1_SUB_AGENTE3,PVG_2_SUB_AGENTE3,DIFF_PRZ_SUB_AGE3,"
				+ "R_AGENTE_SUB4,PVG_1_SUB_AGENTE4,PVG_2_SUB_AGENTE4,DIFF_PRZ_SUB_AGE4,"
				+ "R_DIVISIONE_CLIENTE,R_SETTORE,R_STAGIONE,"
				+ "GRUPPO_TC";   //Fix 81250

		linkTable(TABLE_NAME_EXT, colList);

		addAttributeOnTable("IdAgenteSub2", R_AGENTE_SUB2, TABLE_NAME_EXT);
		addAttributeOnTable("Provvigione1Subagente2", PVG_1_SUB_AGENTE2, TABLE_NAME_EXT);
		addAttributeOnTable("Provvigione2Subagente2", PVG_2_SUB_AGENTE2, TABLE_NAME_EXT);
		addAttributeOnTable("IdAgenteSub3", R_AGENTE_SUB3, TABLE_NAME_EXT);
		addAttributeOnTable("Provvigione1Subagente3", PVG_1_SUB_AGENTE3, TABLE_NAME_EXT);
		addAttributeOnTable("Provvigione2Subagente3", PVG_2_SUB_AGENTE3, TABLE_NAME_EXT);
		addAttributeOnTable("IdAgenteSub4", R_AGENTE_SUB4, TABLE_NAME_EXT);
		addAttributeOnTable("Provvigione1Subagente4", PVG_1_SUB_AGENTE4, TABLE_NAME_EXT);
		addAttributeOnTable("Provvigione2Subagente4", PVG_2_SUB_AGENTE4, TABLE_NAME_EXT);
		addAttributeOnTable("DifferenzaPrezzoSubag2", DIFF_PRZ_SUB_AGE2, TABLE_NAME_EXT);
		addAttributeOnTable("DifferenzaPrezzoSubag3", DIFF_PRZ_SUB_AGE3, TABLE_NAME_EXT);
		addAttributeOnTable("DifferenzaPrezzoSubag4", DIFF_PRZ_SUB_AGE4, TABLE_NAME_EXT);

		//fix 80795
		addAttributeOnTable("RDivisioneCliente", R_DIVISIONE_CLIENTE, TABLE_NAME_EXT);
		addAttributeOnTable("RSettore", R_SETTORE, TABLE_NAME_EXT);
		addAttributeOnTable("RStagione", R_STAGIONE, TABLE_NAME_EXT);

		//fix 81250
		addAttributeOnTable("GruppoTC", GRUPPO_TC, TABLE_NAME_EXT);

	}

}