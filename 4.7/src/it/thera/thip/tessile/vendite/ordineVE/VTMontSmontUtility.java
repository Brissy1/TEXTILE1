package it.thera.thip.tessile.vendite.ordineVE;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;

import it.thera.thip.base.articolo.VistaDisponibilita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.fornitore.FornitoreAcquisto;
import it.thera.thip.datiTecnici.modpro.AttivitaProdMateriale;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivo;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivoTM;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcqRigaSec;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.base.fornitore.VtFornitore;
import it.thera.thip.tessile.datiTecnici.modpro.VtAttivitaProduttiva;
import it.thera.thip.tessile.tabelle.VtTessileUtil;

public class VTMontSmontUtility {

	public static ModelloProduttivo getModProdFromArticoloPatron(String idAzienda, String idArticolo) {

		String where = ModelloProduttivoTM.ID_AZIENDA + "='" + idAzienda + "' AND " + ModelloProduttivoTM.R_ARTICOLO
				+ "= '" + idArticolo + "'";

		Vector modelli = null;
		try {
			modelli = ModelloProduttivo.retrieveList(ModelloProduttivo.class, where, "", true);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (modelli != null && !modelli.isEmpty()) {
			ModelloProduttivo modello = (ModelloProduttivo) modelli.get(0);
			if (modello != null) {
				return modello;
			}
		}

		return null;
	}

	public static String makeComponenKey(VtArticolo articolo, AttivitaProdMateriale comp, String magKey) {
		String key = "";
		String colore = articolo.getColoreVariante();
		key = articolo.getChiaveProdotto16().trim() + "-" + colore.trim() + "-"
				+ comp.getCoeffImpiego().toBigInteger().toString() + "-" + magKey;
		return key;
	}

	public static String makeComponenValue(VtArticolo articolo, AttivitaProdMateriale comp) {
		String value = "";
		String colore = articolo.getColoreVariante();
		value = articolo.getChiaveProdotto1().trim() + "-" + articolo.getChiaveProdotto16().trim() + "-" + colore.trim()
				+ "-" + articolo.getChiaveProdotto17().trim() + "(" + comp.getCoeffImpiego().toBigInteger().toString()
				+ ")";
		// value =articolo.getChiaveProdotto16().trim()+"-" + colore.trim();// +"-PZ " +
		// comp.getCoeffImpiego().toBigInteger().toString();

		return value;
	}

	public static VtFornitore getFornitoreFromMag(String azienda, String idmagazzino) {
		VtFornitore forn = null;
		Magazzino mag = null;

		String magKey = KeyHelper.buildObjectKey(new String[] { azienda, idmagazzino });
		try {
			mag = (Magazzino) PersistentObject.elementWithKey(Magazzino.class, magKey, Magazzino.NO_LOCK);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		if (mag != null) {
			try {
				String fornKey = KeyHelper.buildObjectKey(new String[] { azienda, mag.getIdFornitoreAzienda() });
				forn = (VtFornitore) PersistentObject.elementWithKey(VtFornitore.class, fornKey, VtFornitore.NO_LOCK);
			} catch (SQLException e) {

				e.printStackTrace();
			}

		}

		return forn;

	}

	
}
