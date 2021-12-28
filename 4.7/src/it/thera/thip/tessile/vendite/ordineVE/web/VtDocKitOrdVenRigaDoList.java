package it.thera.thip.tessile.vendite.ordineVE.web;

import com.thera.thermfw.ad.ClassAD;
import com.thera.thermfw.gui.cnr.DOList;
import com.thera.thermfw.persist.KeyHelper;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.MagazzinoTM;
import it.thera.thip.tessile.vendite.ordineVE.VtDocKitOrdVenRigaTM;

public class VtDocKitOrdVenRigaDoList extends DOList {

	public void setRestrictCondition(ClassAD[] attributes, String[] values) {

		if (values != null && values.length > 0) {
			String aziendaID = values[0];
			String ordineKey = values[1];
			String annoOrd = KeyHelper.getTokenObjectKey(ordineKey, 2);
			String numOrd = KeyHelper.getTokenObjectKey(ordineKey, 3);
			String idRiga = values[2];

			String strWhere = VtDocKitOrdVenRigaTM.TABLE_NAME + "." + VtDocKitOrdVenRigaTM.ID_AZIENDA + "='" + aziendaID
					+ "'";
			strWhere = strWhere + " AND " + VtDocKitOrdVenRigaTM.TABLE_NAME + "." + VtDocKitOrdVenRigaTM.ID_ANNO_ORD
					+ "='" + annoOrd + "'";
			strWhere = strWhere + " AND " + VtDocKitOrdVenRigaTM.TABLE_NAME + "." + VtDocKitOrdVenRigaTM.ID_NUMERO_ORD
					+ "='" + numOrd + "'";
			if (idRiga != null && !idRiga.equals("null") && !idRiga.equals(""))
				strWhere = strWhere + " AND (" + VtDocKitOrdVenRigaTM.TABLE_NAME + "." + VtDocKitOrdVenRigaTM.ID_RIGA_ORD
						+ "='" + idRiga + "' OR " + VtDocKitOrdVenRigaTM.TABLE_NAME + "." + VtDocKitOrdVenRigaTM.ID_RIGA_ORD
						+ " is null ) OR " + VtDocKitOrdVenRigaTM.TABLE_NAME + "." + VtDocKitOrdVenRigaTM.ID_RIGA_ORD +"='0'";

			specificWhereClause = strWhere;
		}
	}

}
