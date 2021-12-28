package it.thera.thip.tessile.vendite.ordineVE.web;

import java.sql.SQLException;
import java.util.Iterator;

import com.thera.thermfw.ad.ClassAD;
import com.thera.thermfw.gui.cnr.DOList;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.setting.Setting;

import it.thera.thip.base.articolo.VistaDisponibilita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.azienda.Magazzino;
import it.thera.thip.base.azienda.MagazzinoTM;
import it.thera.thip.base.fornitore.FornitoreAcquistoTM;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.tessile.tabelle.VtColoreFondoStampaTM;

public class VtOrdVenRigaPrmMontSmontKDoList extends DOList{


	 public static final String FUNZIONE_DISP_MAG 	= "SiVistaDispMag";
	 public static final String PARAM_DISP_MAG 	= "CodiceVista";
	
	
	public void setRestrictCondition(ClassAD[] attributes, String[] values) {
		
		specificWhereClause = getMagazzinoAziendaCondition() + " AND " + getMagazzinoCondition();
	}
	
	public String getMagazzinoAziendaCondition() {
		return MagazzinoTM.TABLE_NAME +"."+ MagazzinoTM.ID_AZIENDA+"='"+Azienda.getAziendaCorrente()+"'";
	}
	

	public String getMagazzinoCondition() {
		String magCondition="";
		
		String vistaDisponKey = ParametroPsn.getValoreParametroPsn(FUNZIONE_DISP_MAG, PARAM_DISP_MAG);
		
		
		VistaDisponibilita vistaDisponibilita = null;
		
		if (!vistaDisponKey.equals("")) {
			String vistaKey = KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), vistaDisponKey });
			try {
				vistaDisponibilita = (VistaDisponibilita) PersistentObject.elementWithKey(VistaDisponibilita.class,
						vistaKey, PersistentObject.NO_LOCK);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String magazzini="";
		if ( vistaDisponibilita != null) {
			
			Iterator magIter = vistaDisponibilita.getMagazzini().iterator();
			 while (magIter.hasNext()) {
				 Magazzino mag=(Magazzino) magIter.next();
				 magazzini +="'" + mag.getIdMagazzino() +"',";
			 }
		}
		
		if( !magazzini.equals(""))
		{ 
			if(magazzini.length()>0)
				magazzini=magazzini.substring(0, magazzini.length()-1);
			
			magCondition=MagazzinoTM.TABLE_NAME +"."+ MagazzinoTM.ID_MAGAZZINO+" in ("+ magazzini+")";
		}
		return magCondition;
	}
}
