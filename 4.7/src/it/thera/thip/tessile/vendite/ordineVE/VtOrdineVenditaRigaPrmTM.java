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
 * 80444   14/12/2015	ANDC	Aggiunti Articolo Ordinato e Articolo Cliente
 * 								Modificato Settaggio da Stringa a Integer
 * 80498   29/02/2016	ANDC	Aggiunti campi per gestione righe prodotto finito
 * 80540   23/03/2016	ANDC	Aggiunti altri campi per gestione righe prodotto finito
 * 80556   07/04/2016	ANDC	Aggiunti campi date consegna tessuto (comprese settimane)
 * 80563   12/04/2016	ANDC	Aggiunti campi riferimento ordine a disporre e qta disposta
 * 80669   01/07/2016	ANDC	Aggiunto campo variante per articolo cliente
 * 80679   11/07/2016	ANDC	Aggiunto campo codice articolo per cliente 
 * 80691   19/07/2016	ANDC	Aggiunti campi subagenti 2, 3 e 4, con relative provvigioni 1 e 2 per i nuovi subagenti
 */

package it.thera.thip.tessile.vendite.ordineVE;

import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrmTM;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;

public class VtOrdineVenditaRigaPrmTM extends OrdineVenditaRigaPrmTM
{
	public static final String LUNG_PEZ_FIN = "LUNG_PEZ_FIN";
	public static final String R_FORNITORE = "R_FORNITORE";
	public static final String R_MOD_SPEDIZIONE = "R_MOD_SPEDIZIONE";
	public static final String R_MOD_CONSEGNA = "R_MOD_CONSEGNA";
	public static final String R_DIVISIONE_CLIENTE = "R_DIVISIONE_CLIENTE";
	public static final String R_SETTORE = "R_SETTORE";
	public static final String SETTAGGIO = "SETTAGGIO";
	public static final String R_ART_ORDINATO = "R_ART_ORDINATO";
	public static final String R_ART_CLI = "R_ART_CLI";
	public static final String R_BUYER = "R_BUYER";
	public static final String R_STAGIONE = "R_STAGIONE";
	public static final String NUMERO_PEZZE = "NUMERO_PEZZE";
	public static final String ID_PRD_FINO_VAR = "ID_PRD_FINO_VAR";
	public static final String ID_PRODOTTO = "ID_PRODOTTO";
	public static final String R_ART_FINITO = "R_ART_FINITO";
	public static final String QTA_VEN_FINITO = "QTA_VEN_FINITO";
	public static final String UM_VEN_FINITO = "UM_VEN_FINITO";
	public static final String COEFF_TESSUTO = "COEFF_TESSUTO";
	public static final String TIPOLOGIA_ORDINE = "TIPOLOGIA_ORDINE";
	public static final String RIGA_PREORD = "RIGA_PREORD";
	public static final String DET_PREORD = "DET_PREORD";
	public static final String R_ETICHETTA = "R_ETICHETTA";
	public static final String QTA_DA_PROD = "QTA_DA_PROD";
	public static final String DATA_CONS_RICH = "DATA_CONS_RICH";
	public static final String DATA_CONS_RICH_TES = "DATA_CONS_RICH_TES";
	public static final String PRZ_MANUALE = "PRZ_MANUALE";
	public static final String DATA_CONS_CONF_TES = "DATA_CONS_CONF_TES";
	public static final String DATA_CONS_PROD_TES = "DATA_CONS_PROD_TES";
	public static final String STT_CONS_RICH_TES = "STT_CONS_RICH_TES";
	public static final String STT_CONS_CONF_TES = "STT_CONS_CONF_TES";
	public static final String STT_CONS_PROD_TES = "STT_CONS_PROD_TES";
	public static final String QTA_DISPOSTA = "QTA_DISPOSTA";
	public static final String NUM_RIGA_ADISP = "NUM_RIGA_ADISP";
	public static final String DET_RIGA_ADISP = "DET_RIGA_ADISP";
	public static final String QTA_RESIDUA = "QTA_RESIDUA";
	public static final String VARIANTE_ARTCLI = "VARIANTE_ARTCLI";
	public static final String ARTICOLO_CLI = "ARTICOLO_CLI";
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
	
	//Fix 80720 - Inizio
	public static final String MAGGIOR_ZONA_VALUTA = "MAGGIOR_ZONA_VALUTA";
	public static final String MAGGIOR_CLIENTE = "MAGGIOR_CLIENTE";
	public static final String MAGGIOR_PARAMETRICA = "MAGGIOR_PARAMETRICA";
	//Fix 80720 - Fine
	
	//Fix 80725 - Inizio
	public static final String PREZZO_VAL = "PREZZO_VAL";
	//Fix 80725 - Fine
	
	//MAAM 04/10/2016
	public static final String ID_CARTELLA = "ID_CARTELLA";
	
	//fix 80779
	public static final String ALTEZZA_MULTIPLA = "ALTEZZA_MULTIPLA";
	// fix 80779
	
	//Fix 80880 - Inizio
	public static final String R_STAGIONE_CART_CLI = "R_STAGIONE_CART_CLI";
	//Fix 80880 - Fine
	
	//Fix 80907 - Inizio
	public static final String R_ATTIVITA = "R_ATTIVITA";
	public static final String N_DOGANALE = "N_DOGANALE";
	//Fix 80907 - Fine
	
	//Fix 80982 - Inizio
	public static final String QTA_TESSUTO = "QTA_TESSUTO";
	//Fix 80982 - Fine
	
	//Fix 81091 - Inizio
	public static final String STATO_COD_TESS = "STATO_COD_TESS";
	public static final String UM_PRM_FINITO = "UM_PRM_FINITO";
	public static final String UM_SEC_FINITO = "UM_SEC_FINITO";
	public static final String QTA_PRM_FINITO = "QTA_PRM_FINITO";
	public static final String QTA_SEC_FINITO = "QTA_SEC_FINITO";
	public static final String RLS_ORD_PRD_TESS = "RLS_ORD_PRD_TESS";
	//Fix 81091 - Fine
	
	
	
	//
	public static final String ALTEZZA_TIRELLA = "ALTEZZA_TIRELLA";
	public static final String LARGHEZZA_TIRELLA = "LARGHEZZA_TIRELLA";
	//

	//BRY 
	public static final String ID_CAU_SALDO_MANUALE = "ID_CAU_SALDO_MANUALE";
	
	//Cristi - Campo raggruppamento righe per taglia e colore
	public static final String GRUPPO_TC = "GRUPPO_TC";
	//Fine
	
	 //FIX 82138 FIRAS 
	  public static final String TIPO_EVASIONE = "TIPO_EVASIONE";
	
	public static final String TABLE_NAME_EXT = SystemParam.getSchema("THIP") + "VT_ORDVEN_RIG_PRM_V01";
	
	public VtOrdineVenditaRigaPrmTM() throws SQLException {
		super();
	}
	

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(VtOrdineVenditaRigaPrm.class.getName());
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		//linkTable(TABLE_NAME_EXT);
		String colList = "ID_AZIENDA,ID_ANNO_ORD, ID_NUMERO_ORD, ID_RIGA_ORD,LUNG_PEZ_FIN,R_FORNITORE,R_MOD_SPEDIZIONE,"
				+ "R_MOD_CONSEGNA,R_DIVISIONE_CLIENTE,R_SETTORE,SETTAGGIO,R_ART_ORDINATO,R_ART_CLI,R_BUYER,R_STAGIONE,"
				+ "NUMERO_PEZZE,ID_PRD_FINO_VAR,ID_PRODOTTO,R_ART_FINITO,QTA_VEN_FINITO,"
				+ "UM_VEN_FINITO,COEFF_TESSUTO,TIPOLOGIA_ORDINE,RIGA_PREORD,DET_PREORD, R_ETICHETTA,"
				+ "QTA_DA_PROD,DATA_CONS_RICH,DATA_CONS_RICH_TES,PRZ_MANUALE,"
				+ "DATA_CONS_CONF_TES,DATA_CONS_PROD_TES,STT_CONS_RICH_TES,STT_CONS_CONF_TES,STT_CONS_PROD_TES,"
				+ "QTA_DISPOSTA,NUM_RIGA_ADISP,DET_RIGA_ADISP,QTA_RESIDUA,VARIANTE_ARTCLI, ARTICOLO_CLI,"
				+ "R_AGENTE_SUB2,PVG_1_SUB_AGENTE2,PVG_2_SUB_AGENTE2,"
				+ "R_AGENTE_SUB3,PVG_1_SUB_AGENTE3,PVG_2_SUB_AGENTE3,"
				+ "R_AGENTE_SUB4,PVG_1_SUB_AGENTE4,PVG_2_SUB_AGENTE4,"
				+ "DIFF_PRZ_SUB_AGE2,DIFF_PRZ_SUB_AGE3,DIFF_PRZ_SUB_AGE4"
				+ ",MAGGIOR_ZONA_VALUTA,MAGGIOR_CLIENTE,MAGGIOR_PARAMETRICA"  //80720
				+ ",PREZZO_VAL" + ",ID_CARTELLA"+ ",ALTEZZA_MULTIPLA,R_STAGIONE_CART_CLI" //80725
				+ ",R_ATTIVITA,N_DOGANALE,QTA_TESSUTO,GRUPPO_TC" //80907
				+ ",STATO_COD_TESS,UM_PRM_FINITO,UM_SEC_FINITO,QTA_PRM_FINITO,QTA_SEC_FINITO,RLS_ORD_PRD_TESS"
				+ ",ALTEZZA_TIRELLA,LARGHEZZA_TIRELLA,ID_CAU_SALDO_MANUALE"
				+ ",TIPO_EVASIONE"; //81091
		
		
		
	    linkTable(TABLE_NAME_EXT, colList);
		
		addAttributeOnTable("LungPezFin", LUNG_PEZ_FIN, TABLE_NAME_EXT);
		addAttributeOnTable("RFornitore", R_FORNITORE, TABLE_NAME_EXT);
		addAttributeOnTable("RModSpedizione", R_MOD_SPEDIZIONE, TABLE_NAME_EXT);
		addAttributeOnTable("RModConsegna", R_MOD_CONSEGNA, TABLE_NAME_EXT);
		addAttributeOnTable("RDivisioneCliente", R_DIVISIONE_CLIENTE, TABLE_NAME_EXT);
		addAttributeOnTable("RSettore", R_SETTORE, TABLE_NAME_EXT);
		addAttributeOnTable("Settaggio", SETTAGGIO, TABLE_NAME_EXT);
		addAttributeOnTable("RArtOrdinato", R_ART_ORDINATO, TABLE_NAME_EXT);
		addAttributeOnTable("RArtCli", R_ART_CLI, TABLE_NAME_EXT);
		addAttributeOnTable("RBuyer", R_BUYER, TABLE_NAME_EXT);
		addAttributeOnTable("RStagione", R_STAGIONE, TABLE_NAME_EXT);
		addAttributeOnTable("NumeroPezze", NUMERO_PEZZE, TABLE_NAME_EXT);
		addAttributeOnTable("IdPrdFinoVar", ID_PRD_FINO_VAR, TABLE_NAME_EXT);
		addAttributeOnTable("IdProdotto", ID_PRODOTTO, TABLE_NAME_EXT);
		addAttributeOnTable("RArtFinito", R_ART_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("QtaVenFinito", QTA_VEN_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("UmVenFinito", UM_VEN_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("CoeffTessuto", COEFF_TESSUTO, TABLE_NAME_EXT);
		addAttributeOnTable("TipologiaOrdine", TIPOLOGIA_ORDINE, TABLE_NAME_EXT);
		addAttributeOnTable("RigaPreord", RIGA_PREORD, TABLE_NAME_EXT);
		addAttributeOnTable("DetPreord", DET_PREORD, TABLE_NAME_EXT);
		addAttributeOnTable("REtichetta", R_ETICHETTA, TABLE_NAME_EXT);
		addAttributeOnTable("QtaDaProd", QTA_DA_PROD, TABLE_NAME_EXT);
		addAttributeOnTable("DataConsRich", DATA_CONS_RICH, TABLE_NAME_EXT);
		addAttributeOnTable("DataConsRichTes", DATA_CONS_RICH_TES, TABLE_NAME_EXT);
		addAttributeOnTable("PrzManuale", PRZ_MANUALE, TABLE_NAME_EXT);
		addAttributeOnTable("DataConsConfTes", DATA_CONS_CONF_TES, TABLE_NAME_EXT);
		addAttributeOnTable("DataConsProdTes", DATA_CONS_PROD_TES, TABLE_NAME_EXT);
		addAttributeOnTable("SettConsRichTes", STT_CONS_RICH_TES, TABLE_NAME_EXT);
		addAttributeOnTable("SettConsConfTes", STT_CONS_CONF_TES, TABLE_NAME_EXT);
		addAttributeOnTable("SettConsProdTes", STT_CONS_PROD_TES, TABLE_NAME_EXT);
		addAttributeOnTable("QtaDisposta", QTA_DISPOSTA, TABLE_NAME_EXT);
		addAttributeOnTable("NumRigaAdisp", NUM_RIGA_ADISP, TABLE_NAME_EXT);
		addAttributeOnTable("DetRigaAdisp", DET_RIGA_ADISP, TABLE_NAME_EXT);
		addAttributeOnTable("QtaResidua", QTA_RESIDUA, TABLE_NAME_EXT);
		addAttributeOnTable("VarianteArtcli", VARIANTE_ARTCLI, TABLE_NAME_EXT);
		addAttributeOnTable("ArticoloCli", ARTICOLO_CLI, TABLE_NAME_EXT);
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
		


		//Fix 80720 - Inizio
		addAttributeOnTable("MaggiorZonaValuta", MAGGIOR_ZONA_VALUTA, TABLE_NAME_EXT);
		addAttributeOnTable("MaggiorCliente", MAGGIOR_CLIENTE, TABLE_NAME_EXT);
		addAttributeOnTable("MaggiorParametrica", MAGGIOR_PARAMETRICA, TABLE_NAME_EXT);
		//Fix 80720 - Fine
		
		//Fix 80725 - Inizio
		addAttributeOnTable("PrezzoVal", PREZZO_VAL, TABLE_NAME_EXT);
		//Fix 80725 - Fine
		
		//MAAM 04/10/2016
		addAttributeOnTable("IdCartella", ID_CARTELLA, TABLE_NAME_EXT);
		
		//fix 80779
		addAttributeOnTable("AltezzaMultipla", ALTEZZA_MULTIPLA, TABLE_NAME_EXT);
		
		//Fix 80880
		addAttributeOnTable("RStagioneCartCli", R_STAGIONE_CART_CLI, TABLE_NAME_EXT);
		
		//Fix 80907
		addAttributeOnTable("RAttivita", R_ATTIVITA, TABLE_NAME_EXT);
		addAttributeOnTable("NDoganale", N_DOGANALE, TABLE_NAME_EXT);
		
		addAttributeOnTable("QtaTessuto",QTA_TESSUTO, TABLE_NAME_EXT);
		
		//Fix 81091 - Inizio
		addAttributeOnTable("StatoCodTess",STATO_COD_TESS, TABLE_NAME_EXT);
		addAttributeOnTable("UmPrmFinito",UM_PRM_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("UmSecFinito",UM_SEC_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("QtaPrmFinito",QTA_PRM_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("QtaSecFinito",QTA_SEC_FINITO, TABLE_NAME_EXT);
		addAttributeOnTable("RlsOrdPrdTess",RLS_ORD_PRD_TESS, TABLE_NAME_EXT);
		//Fix 81091 - Fine

		//Cristi
		addAttributeOnTable("GruppoTC",GRUPPO_TC, TABLE_NAME_EXT);
		
		
		//
		addAttributeOnTable("AltezzaTirella",ALTEZZA_TIRELLA, TABLE_NAME_EXT);
		addAttributeOnTable("LarghezzaTirella",LARGHEZZA_TIRELLA, TABLE_NAME_EXT);
		//
		addAttributeOnTable("IdCausaleSaldoManuale",ID_CAU_SALDO_MANUALE, TABLE_NAME_EXT);
//		setKeys(
//			      new String[] {
//			        ID_AZIENDA, ID_ANNO_ORD, ID_NUMERO_ORD, ID_RIGA_ORD, ID_DET_RIGA_ORD
//			      }
//			    );
		
		//FIX 82138 FIRAS : 22/11/2021
				addAttributeOnTable("TipoEvasione", TIPO_EVASIONE, TABLE_NAME_EXT);
	}
}

