package it.thera.thip.tessile.vendite.ordineVE.web;

import it.thera.thip.vendite.ordineVE.web.OrdVenTestataFormCustomizer;


/**
 * VtOrdVenTestataFormCustomizer.
 *
 * <br><br><b>Copyright (c): SIConsulting</b>
 *
 * @author Andrea Calligaro 28/04/2015
 */
/**
 * Revisions:
 * Number Date        Owner   Description
 * 80279  28/04/2015  AND1    Prima stesura
 */
public class VtOrdVenTestataFormCustomizer extends OrdVenTestataFormCustomizer {

  /**
   * Ridefinizione
   */
  public String getCustomizationFileName() {
    return "it/thera/thip/tessile/vendite/ordineVE/xml/VtOrdVenTestata.xml";
  }

}