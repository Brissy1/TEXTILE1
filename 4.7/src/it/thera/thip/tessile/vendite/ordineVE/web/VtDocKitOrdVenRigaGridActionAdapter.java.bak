package it.thera.thip.tessile.vendite.ordineVE.web;

import com.thera.thermfw.web.LicenceManager;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebMenuBar;
import com.thera.thermfw.web.WebMenuItem;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;
import com.thera.thermfw.web.WebToolBarException;
import com.thera.thermfw.web.servlet.GridActionAdapter;
import com.thera.thermfw.security.Security;

import it.thera.thip.acquisti.documentoAC.DocumentoAcquisto;
import it.thera.thip.acquisti.documentoAC.web.DocumentoAcquistoDataCollector;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.documenti.web.DocumentoGridActionAdapter;
import it.thera.thip.base.profilo.UtenteAzienda;
import it.thera.thip.datiTecnici.modpro.ModelloProduttivo;
import it.thera.thip.magazzino.documenti.web.DocMagGenDataCollector;
import it.thera.thip.tessile.acquisti.documentoAC.VtDocumentoAcquisto;
import it.thera.thip.tessile.articolo.VtArticolo;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagGenerico;
import it.thera.thip.tessile.magazzino.documenti.VtDocMagTrasferimento;
import it.thera.thip.tessile.tabelle.VtTessileUtil;
import it.thera.thip.tessile.vendite.ordineVE.VtDocKitOrdVenRiga;
import it.thera.thip.tessile.vendite.ordineVE.VtOrdineVenditaRigaPrm;
import com.thera.thermfw.persist.Database;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.ad.ClassADCollectionManager;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;

public class VtDocKitOrdVenRigaGridActionAdapter extends GridActionAdapter {

	public static final String FILE_RES = "it.thera.thip.tessile.vendite.ordineVE.resources.VtDocKitOrdVenRiga";
	public static final String ELIMINA_DOC = "ELIMINA_DOC";
	public static final String ELIMINA_COLL_DOC = "ELIMINA_COLL_DOC";
	public static final String VIEW_DOC = "VIEW_DOC";
	public static final String SERVLET_VIEWDOCMAG = "it.thera.thip.magazzino.documenti.web.DocMagGenGridActionAdapter";
	public static final String SERVLET_VIEWDOCMAGRIGHE = "it.thera.thip.magazzino.documenti.web.DocMagGenRigaPrmGridActionAdapter";
	public static final String SERVLET_VIEWDOCACQ = "it.thera.thip.acquisti.documentoAC.web.DocumentoAcquistoGridActionAdapter";
	public static final String SERVLET_VIEWDOCACQRIGHE = "it.thera.thip.acquisti.documentoAC.web.DocumentoAcquistoRigaPrmGridActionAdapter";
	public static final String SERVLET_VIEWMAGTRA = "it.thera.thip.magazzino.documenti.web.DocMagTrasfGridActionAdapter";

	public static final String JSP_VIEWDOCTRA = "it/thera/thip/magazzino/documenti/DocMagGenerico.jsp";
	List listaErrori = new ArrayList();

	public void modifyToolBar(WebToolBar toolBar) {

		super.modifyToolBar(toolBar);

		String[] key = new String[] { Azienda.getAziendaCorrente(), Security.getCurrentUser().getId().split("_")[0] };

		WebToolBarButton eliminaDocKit = new WebToolBarButton("DocKitElimina", "action_submit", "infoArea", "no",
				FILE_RES, "DocKitElimina", "thermweb/image/gui/cnr/Delete.gif", ELIMINA_DOC, "mulitple", false);
		toolBar.addButton(eliminaDocKit);

		WebToolBarButton eliminaCollDocKit = new WebToolBarButton("CollDocKitElimina", "action_submit", "infoArea",
				"no", FILE_RES, "CollDocKitElimina", "it/thera/thip/tessile/vendite/ordineVE/images/delete-link.gif",
				ELIMINA_COLL_DOC, "mulitple", false);
		toolBar.addButton(eliminaCollDocKit);

		/*
		 * WebToolBarButton viewDocKit = new WebToolBarButton("DocKitVisualizza",
		 * "action_submit", "new", "no", FILE_RES, "DocKitView",
		 * "thermweb/image/gui/cnr/View.gif", VIEW_DOC, "single", false);
		 * toolBar.addButton(viewDocKit);
		 */
		toolBar.removeButton("New");
		toolBar.removeButton("Open");
		// toolBar.removeButton("View");
		toolBar.removeButton("Copy");
		toolBar.removeButton("GeoLocalizazzione");
		toolBar.removeButton("Delete");
		toolBar.removeButton("Export");
		// toolBar.removeButton("Refresh");
		toolBar.removeButton("WfAdvancement");
		toolBar.removeButton("Print");
		toolBar.removeButton("PrintList");
		toolBar.removeButton("HelpTopics");
		toolBar.removeButton("Tickler");
		toolBar.removeButton("NavDocDgt");
		try {
			toolBar.removeSeparatorBefore("DocKitElimina");
		} catch (WebToolBarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void modifyMenuBar(WebMenuBar menuBar) {
		super.modifyMenuBar(menuBar);
		menuBar.removeMenu("SelectedMenu.Copy");
		menuBar.removeMenu("SelectedMenu.New");
		menuBar.removeMenu("SelectedMenu.Open");
		menuBar.removeMenu("SelectedMenu.View");

		menuBar.removeMenu("SelectedMenu.GeoLocalizazzione");
		menuBar.removeMenu("SelectedMenu.Tickler");
		menuBar.removeMenu("SelectedMenu.NavDocDgt");
		menuBar.removeMenu("SelectedMenu.WfAdvancement");
		menuBar.removeMenu("SelectedMenu.Delete");
		menuBar.removeMenu("SelectedMenu.COPY_CLIPBOARD");
		menuBar.removeMenu("ListMenu.New");
		menuBar.removeMenu("ListMenu.NewTemplate");
		menuBar.removeMenu("ListMenu.New");

		WebMenuItem eliminaDocKit = new WebMenuItem("EliminaDocKit", "action_submit", "infoArea", "no", FILE_RES,
				"DocKitElimina", "ELIMINA_DOC", "multiple", false);
		menuBar.addMenu("SelectedMenu.+1", eliminaDocKit);

		WebMenuItem collDocKitElimina = new WebMenuItem("CollDocKitElimina", "action_submit", "infoArea", "no",
				FILE_RES, "CollDocKitElimina", "ELIMINA_COLL_DOC", "multiple", false);
		menuBar.addMenu("SelectedMenu.+1", collDocKitElimina);

	}

	@Override
	public void processAction(ServletEnvironment se) throws ServletException, IOException {

		String kitKey = null;
		String azione = getStringParameter(se.getRequest(), "thAction").toUpperCase();

		String objectKeys[] = se.getRequest().getParameterValues("ObjectKey");
		if (objectKeys != null)
			kitKey = objectKeys[0];

		VtDocKitOrdVenRiga myDocKit = null;
		try {
			myDocKit = (VtDocKitOrdVenRiga) VtDocKitOrdVenRiga.elementWithKey(kitKey, PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (myDocKit != null && azione.equals("UPDATE"))
			viewDoc(se);
		else if (myDocKit != null && azione.equals("DELETE"))
			eliminaDoc(se);
		else
			super.processAction(se);

		if (listaErrori != null && listaErrori.size() > 0) {
			Iterator iter = listaErrori.iterator();
			String strErroti = "";
			while (iter.hasNext()) {
				String em = (String) iter.next();
				strErroti = strErroti + "\\r  - " + em;
			}
			invokeAlertWindow(se, "ATTENZIONE: impossibile caricare il documento : \\r " + strErroti);
		}

	}

	@Override
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		super.otherActions(cadc, se);

		listaErrori.clear();

		String azione = getStringParameter(se.getRequest(), "thAction").toUpperCase();
		if (azione.equals(ELIMINA_DOC)) {

			eliminaDoc(se);

		} else if (azione.equals(ELIMINA_COLL_DOC)) {

			eliminaCollDoc(se);

		}

	}

	protected void eliminaDoc(ServletEnvironment se) throws ServletException, IOException {
		String[] keyRigheOrd = se.getRequest().getParameterValues(OBJECT_KEY);
		if (keyRigheOrd != null)
			for (int i = 0; i < keyRigheOrd.length; i++) {
				try {
					String key = keyRigheOrd[i];

					VtDocKitOrdVenRiga myDocKit = (VtDocKitOrdVenRiga) VtDocKitOrdVenRiga.elementWithKey(key,
							PersistentObject.NO_LOCK);

					if (myDocKit != null) {
						String keyDoc = KeyHelper.buildObjectKey(new String[] { myDocKit.getIdAzienda(),
								myDocKit.getIdAnnoDoc(), myDocKit.getIdNumeroDoc() });

						if (myDocKit.getTipo().equals("DA")) {

							// Documento acquisto

							VtDocumentoAcquisto docAcq = (VtDocumentoAcquisto) VtDocumentoAcquisto
									.elementWithKey(keyDoc, PersistentObject.NO_LOCK);
							int res = docAcq.delete();
							if (res >= 0)
								ConnectionManager.commit();
							else
								listaErrori.add("Impossibile eliminare il documento di acquisto (" + keyDoc + ")!");

						}
						if (myDocKit.getTipo().equals("MG")) {
							// Doc Magazzino generico

							VtDocMagGenerico docMagGen = (VtDocMagGenerico) VtDocMagGenerico.elementWithKey(keyDoc,
									PersistentObject.NO_LOCK);
							int res = docMagGen.delete();
							if (res >= 0)
								ConnectionManager.commit();
							else
								listaErrori.add("Impossibile eliminare il doc magazzino generico (" + keyDoc + ")!");

						}
						if (myDocKit.getTipo().equals("MT")) {
							// Doc Magazzino trasferimento

							VtDocMagTrasferimento docMagTra = (VtDocMagTrasferimento) VtDocMagTrasferimento
									.elementWithKey(keyDoc, PersistentObject.NO_LOCK);
							int res = docMagTra.delete();
							if (res >= 0)
								ConnectionManager.commit();
							else
								listaErrori
										.add("Impossibile eliminare il doc magazzino trasferimento (" + keyDoc + ")!");
						}
					}
				} catch (SQLException e) {
					listaErrori.add("exception - " + e.getMessage());
					e.printStackTrace();
				}

			}

		if (listaErrori != null && listaErrori.size() > 0) {
			Iterator iter = listaErrori.iterator();
			String strErroti = "";
			while (iter.hasNext()) {
				String em = (String) iter.next();
				strErroti = strErroti + "\\r  - " + em;
			}
			invokeAlertWindow(se, "ATTENZIONE! Si sono verificati i seguenti errori: " + strErroti);
		} else
			invokeAlertWindow(se, "N." + String.valueOf(keyRigheOrd.length) + " documenti eliminati con successo!");
	}

	protected void eliminaCollDoc(ServletEnvironment se) throws ServletException, IOException {
		String[] keyRigheOrd = se.getRequest().getParameterValues(OBJECT_KEY);
		if (keyRigheOrd != null)
			for (int i = 0; i < keyRigheOrd.length; i++) {
				try {
					String key = keyRigheOrd[i];
					String deleteStr = "DELETE FROM THIPPERS.";
					VtDocKitOrdVenRiga myDocKit = (VtDocKitOrdVenRiga) VtDocKitOrdVenRiga.elementWithKey(key,
							PersistentObject.NO_LOCK);

					if (myDocKit != null) {
						if (myDocKit.getTipo().equals("DA")) {

							// Documento acquisto
							deleteStr += "VT_DOC_ACQ_TES ";
						} else if (myDocKit.getTipo().equals("MG")) {
							// Doc Magazzino generico
							deleteStr += "VT_DOC_GNR_TES ";
						} else if (myDocKit.getTipo().equals("MT")) {
							// Doc Magazzino trasferimento

							deleteStr += "VT_DOC_TRA_TES ";
						}

						deleteStr += " WHERE ID_AZIENDA = ?" + " AND ID_ANNO_DOC = ?" + " AND ID_NUMERO_DOC = ?";
						CachedStatement cDeleteCollegamentoDoc = new CachedStatement(deleteStr);

						PreparedStatement ps = cDeleteCollegamentoDoc.getStatement();
						Database db = ConnectionManager.getCurrentDatabase();
						db.setString(ps, 1, myDocKit.getIdAzienda());
						db.setString(ps, 2, myDocKit.getIdAnnoDoc());
						db.setString(ps, 3, myDocKit.getIdNumeroDoc());

						int rs = ps.executeUpdate();
						if (rs < 0)
							listaErrori.add("Attenzione: impossibile eliminare il documento tipo " + myDocKit.getTipo()
									+ " num. " + myDocKit.getIdNumeroDoc());
						else
							ConnectionManager.commit();
					}
				} catch (SQLException e) {
					listaErrori.add("exception - " + e.getMessage());
					e.printStackTrace();
				}

			}

		if (listaErrori != null && listaErrori.size() > 0) {
			Iterator iter = listaErrori.iterator();
			String strErroti = "";
			while (iter.hasNext()) {
				String em = (String) iter.next();
				strErroti = strErroti + "\\r  - " + em;
			}
			invokeAlertWindow(se, "ATTENZIONE! Si sono verificati i seguenti errori: " + strErroti);
		} else
			invokeAlertWindow(se, "N." + String.valueOf(keyRigheOrd.length) + " collegamenti eliminati con successo!");
	}

	private void invokeAlertWindow(ServletEnvironment se, String message) throws ServletException, IOException {

		PrintWriter wr = se.getResponse().getWriter();
		wr.println("<script language='JavaScript1.2'>");
		wr.println("alert('" + message + "');");

		// wr.println("top.window.close();");
		wr.println("parent.runAction('REFRESH_GRID','none','infoArea','no')");
		// wr.println("window.top.opener.runAction('REFRESH_GRID','none','infoArea','no')");

		wr.println("</script>");
	}

	protected void viewDoc(ServletEnvironment se) throws ServletException, IOException {
		String[] objectKeys = se.getRequest().getParameterValues(OBJECT_KEY);
		String url = null;
		try {
			String key = objectKeys[0];

			VtDocKitOrdVenRiga myDocKit = (VtDocKitOrdVenRiga) VtDocKitOrdVenRiga.elementWithKey(key,
					PersistentObject.NO_LOCK);

			if (myDocKit != null) {
				String keyDoc = KeyHelper.buildObjectKey(
						new String[] { myDocKit.getIdAzienda(), myDocKit.getIdAnnoDoc(), myDocKit.getIdNumeroDoc() });

				if (myDocKit.getTipo().equals("DA")) {

					// Documento acquisto

					VtDocumentoAcquisto docAcq = (VtDocumentoAcquisto) VtDocumentoAcquisto.elementWithKey(keyDoc,
							PersistentObject.NO_LOCK);
					// show testata
					String param = "?thAction=UPDATE" + "&thMode=SHOW" + "&thModoForm="
							+ DocumentoGridActionAdapter.cercaModoForm(se, "VtDocumentoAcquisto")
							+ "&thClassName=VtDocumentoAcquisto" + "&ObjectKey=" + docAcq.getKey() + "&thCollectorName="
							+ DocumentoAcquistoDataCollector.class.getName();
					url = SERVLET_VIEWDOCACQ + param;

					/*
					 * //show Righe String paramRighe = "?thAction=SHOW_RIGHE" + "&thTarget=NEW" +
					 * "&thModoForm=" +
					 * DocumentoGridActionAdapter.cercaModoForm(se,"DocumentoAcquisto") +
					 * "&thClassName=DocumentoAcquistoRigaPrm" + "&ObjectKey=" + docAcq.getKey()+
					 * "&thCollectorName=" + DocumentoAcquistoDataCollector.class.getName();
					 * url=SERVLET_VIEWDOCACQRIGHE + paramRighe;
					 */
				}
				if (myDocKit.getTipo().equals("MG")) {
					// Doc Magazzino generico

					VtDocMagGenerico docMagGen = (VtDocMagGenerico) VtDocMagGenerico.elementWithKey(keyDoc,
							PersistentObject.NO_LOCK);

					// show testata
					String param = "?thAction=UPDATE" + "&thMode=SHOW" + "&thClassName=VtDocMagGenerico"
							+ "&thModoForm=" + DocumentoGridActionAdapter.cercaModoForm(se, "VtDocMagGenerico")
							+ "&ObjectKey=" + docMagGen.getKey() + "&thCollectorName="
							+ DocMagGenDataCollector.class.getName();
					url = SERVLET_VIEWDOCMAG + param;

				}
				if (myDocKit.getTipo().equals("MT")) {
					// Doc Magazzino trasferimento

					VtDocMagTrasferimento docMagTra = (VtDocMagTrasferimento) VtDocMagTrasferimento
							.elementWithKey(keyDoc, PersistentObject.NO_LOCK);

					// show testata
					String param = "?thAction=UPDATE" + "&thMode=SHOW" + "&thClassName=VtDocMagTrasferimento"
							+ "&thModoForm=" + DocumentoGridActionAdapter.cercaModoForm(se, "VtDocMagTrasferimento")
							+ "&ObjectKey=" + docMagTra.getKey() + "&thCollectorName="
							+ DocMagGenDataCollector.class.getName();
					url = SERVLET_VIEWMAGTRA + param;

				}
			} else
				listaErrori.add("Documento kit non trovato (key:" + key + ").");

		} catch (SQLException e) {
			listaErrori.add("exception - " + e.getMessage());
			e.printStackTrace();
		}

		if (url != null)
			se.sendRequest(getServletContext(), se.getServletPath() + url, false);

	}

	protected void showObject(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		viewDoc(se);
	}

}
