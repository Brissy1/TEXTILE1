/**
 * VtOrdineVenditaTM
 *
 * <br></br><b>Copyright (C) : SIConsulting</b>
 * @author Andrea Calligaro
 * 
 * Implementazione ID BUYER (compratore) su testate ordini vendita (tabella VT_BUYER)
 */
 /*
 * Revisions:
 * Number  Date         Owner   Description
 * 80279   28/04/2015	ANDC 	Prima versione
 * 80681   20/07/2016	ANDC 	aggiunti sub-agenti 2 3 e 4 con relative provvigioni
 */

package it.thera.thip.tessile.vendite.ordineVE;

import it.thera.thip.vendite.ordineVE.OrdineVenditaTM;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;

public class VtOrdineVenditaTM extends OrdineVenditaTM
{
	public static final String R_BUYER = "R_BUYER";
	public static final String R_OPE_COMM = "R_OPE_COMM";
	

	public static final String R_DIVISIONE_CLIENTE = "R_DIVISIONE_CLIENTE";
	public static final String R_LINEA_VEN = "R_LINEA_VEN";
	public static final String R_SETTORE = "R_SETTORE";
	public static final String R_STAGIONE = "R_STAGIONE";
	public static final String R_AGENTE_SUB2 = "R_AGENTE_SUB2";
	public static final String R_AGENTE_SUB3 = "R_AGENTE_SUB3";
	public static final String R_AGENTE_SUB4 = "R_AGENTE_SUB4";
	public static final String PVG_AGENTE_SUB2 = "PVG_AGENTE_SUB2";
	public static final String PVG_AGENTE_SUB3 = "PVG_AGENTE_SUB3";
	public static final String PVG_AGENTE_SUB4 = "PVG_AGENTE_SUB4";
	public static final String DIFF_PRZ_SUB_AGE2 = "DIFF_PRZ_SUB_AGE2";
	public static final String DIFF_PRZ_SUB_AGE3 = "DIFF_PRZ_SUB_AGE3";
	public static final String DIFF_PRZ_SUB_AGE4 = "DIFF_PRZ_SUB_AGE4";

	
	public static final String ANNO_ORDINE_ADISP = "ANNO_ORDINE_ADISP";
	public static final String NUMERO_ORD_ADISP = "NUMERO_ORD_ADISP";
	//BRY 
		public static final String ID_CAU_STATISTICA = "ID_CAU_STATISTICA";
	//Fix 80965 - Inizio
	public static final String RIF_PRODUZIONE = "RIF_PRODUZIONE";
	//Fix 80965 - Fine
	
	
	//Fix XXXXX Davide - inizio
	public static final String RIF_CLIENTE = "RIF_CLIENTE";
	//Fix XXXXX Davide - fine
	
	//FIX 82138 FIRAS : 22/11/2021
	public static final String TIPO_EVASIONE = "TIPO_EVASIONE";
	public static final String QTY_TOT_RIG_PRM = "QTY_TOT_RIG_PRM";
	public static final String QTY_TOT_RIG_PRM_SEC = "QTY_TOT_RIG_PRM_SEC";
	public static final String QTY_TOT_RIG_PRM_SPED = "QTY_TOT_RIG_PRM_SPED";
	public static final String QTY_TOT_RIG_PRM_SEC_SPED = "QTY_TOT_RIG_PRM_SEC_SPED";
	//FINE 82138					   
															
			 
 
	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIP") +
			"VT_ORD_VEN_TES";
	
	public VtOrdineVenditaTM() throws SQLException {
		super();
	}
	

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(VtOrdineVendita.class.getName());
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT);
		addAttributeOnTable("RBuyer", R_BUYER, TABLE_NAME_EXT);
		addAttributeOnTable("ROpeComm", R_OPE_COMM, TABLE_NAME_EXT);
		

		addAttributeOnTable("RDivisioneCliente", R_DIVISIONE_CLIENTE, TABLE_NAME_EXT);
		addAttributeOnTable("RLineaVen", R_LINEA_VEN, TABLE_NAME_EXT);
		addAttributeOnTable("RSettore", R_SETTORE, TABLE_NAME_EXT);
		addAttributeOnTable("RStagione", R_STAGIONE, TABLE_NAME_EXT);
		addAttributeOnTable("AnnoOrdineAdisp", ANNO_ORDINE_ADISP, TABLE_NAME_EXT);
		addAttributeOnTable("NumeroOrdAdisp", NUMERO_ORD_ADISP, TABLE_NAME_EXT);
		addAttributeOnTable("IdAgenteSub2", R_AGENTE_SUB2, TABLE_NAME_EXT);
		addAttributeOnTable("IdAgenteSub3", R_AGENTE_SUB3, TABLE_NAME_EXT);
		addAttributeOnTable("IdAgenteSub4", R_AGENTE_SUB4, TABLE_NAME_EXT);
		addAttributeOnTable("ProvvigioneSubagente2" , PVG_AGENTE_SUB2, TABLE_NAME_EXT);
		addAttributeOnTable("ProvvigioneSubagente3" , PVG_AGENTE_SUB3, TABLE_NAME_EXT);
		addAttributeOnTable("ProvvigioneSubagente4" , PVG_AGENTE_SUB4, TABLE_NAME_EXT);
	    addAttributeOnTable("DifferenzaPrezzoSubag2", DIFF_PRZ_SUB_AGE2, TABLE_NAME_EXT);
	    addAttributeOnTable("DifferenzaPrezzoSubag3", DIFF_PRZ_SUB_AGE3, TABLE_NAME_EXT);
	    addAttributeOnTable("DifferenzaPrezzoSubag4", DIFF_PRZ_SUB_AGE4, TABLE_NAME_EXT);
	    
	    //Fix 80965 - Inizio
	    addAttributeOnTable("RifProduzione" , RIF_PRODUZIONE, TABLE_NAME_EXT);
	    //Fix 80965 - Fine
	    
	    //Fix XXXXX - Inizio
	    addAttributeOnTable("RifCliente" , RIF_CLIENTE, TABLE_NAME_EXT);
	    //Fix XXXXX - Fine
		
		
	  //FIX 82138 FIRAS : 22/11/2021
	    addAttributeOnTable("TipoEvasione", TIPO_EVASIONE, TABLE_NAME_EXT);
	  //FIX 82138 FIRAS : 02/12/2021
	    addAttributeOnTable("TotQtyRighePrm", QTY_TOT_RIG_PRM, TABLE_NAME_EXT);
	    addAttributeOnTable("TotQtyRighePrmSec", QTY_TOT_RIG_PRM_SEC, TABLE_NAME_EXT);
	    addAttributeOnTable("TotQtyRighePrmSped", QTY_TOT_RIG_PRM_SPED, TABLE_NAME_EXT);
	    addAttributeOnTable("TotQtyRighePrmSecSped", QTY_TOT_RIG_PRM_SEC_SPED, TABLE_NAME_EXT);
	    //BRY Causale Statistica
	    addAttributeOnTable("IdCausaleStatistica",ID_CAU_STATISTICA, TABLE_NAME_EXT);
	
	}
}

