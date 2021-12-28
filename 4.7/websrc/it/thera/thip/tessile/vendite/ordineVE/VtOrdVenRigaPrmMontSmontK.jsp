<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/4.7.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Form - multiBrowserGen = true -->
<%=WebGenerator.writeRuntimeInfo()%>
<head>
<%@ page contentType="text/html; charset=Cp1252"%>
<%@ page import= " 
  java.sql.*, 
  java.util.*, 
  java.lang.reflect.*, 
  javax.naming.*, 
  com.thera.thermfw.common.*, 
  com.thera.thermfw.type.*, 
  com.thera.thermfw.web.*, 
  com.thera.thermfw.security.*, 
  com.thera.thermfw.base.*, 
  com.thera.thermfw.ad.*, 
  com.thera.thermfw.persist.*, 
  com.thera.thermfw.gui.cnr.*, 
  com.thera.thermfw.setting.*, 
  com.thera.thermfw.collector.*, 
  com.thera.thermfw.batch.web.*, 
  com.thera.thermfw.batch.*, 
  com.thera.thermfw.pref.* 
"%> 
<%
  ServletEnvironment se = (ServletEnvironment)Factory.createObject("com.thera.thermfw.web.ServletEnvironment"); 
  BODataCollector VtOrdVenRigaPrmMontSmontKBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm VtOrdVenRigaPrmMontSmontKForm =  
     new com.thera.thermfw.web.WebForm(request, response, "VtOrdVenRigaPrmMontSmontKForm", "VtOrdVenRigaPrmMontSmontK", "Arial,10", "it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigaPrmMontSmontKFormActionAdapter", false, false, false, false, true, true, null, 1, true, "it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmMontSmontK.js"); 
  VtOrdVenRigaPrmMontSmontKForm.setServletEnvironment(se); 
  VtOrdVenRigaPrmMontSmontKForm.setJSTypeList(jsList); 
  VtOrdVenRigaPrmMontSmontKForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  VtOrdVenRigaPrmMontSmontKForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  VtOrdVenRigaPrmMontSmontKForm.setWebFormModifierClass("it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigaPrmMontSmontKFormModifier"); 
  VtOrdVenRigaPrmMontSmontKForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = VtOrdVenRigaPrmMontSmontKForm.getMode(); 
  String key = VtOrdVenRigaPrmMontSmontKForm.getKey(); 
  String errorMessage; 
  boolean requestIsValid = false; 
  boolean leftIsKey = false; 
  boolean conflitPresent = false; 
  String leftClass = ""; 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        VtOrdVenRigaPrmMontSmontKForm.outTraceInfo(getClass().getName()); 
        String collectorName = VtOrdVenRigaPrmMontSmontKForm.findBODataCollectorName(); 
                VtOrdVenRigaPrmMontSmontKBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (VtOrdVenRigaPrmMontSmontKBODC instanceof WebDataCollector) 
            ((WebDataCollector)VtOrdVenRigaPrmMontSmontKBODC).setServletEnvironment(se); 
        VtOrdVenRigaPrmMontSmontKBODC.initialize("VtOrdVenRigaPrmMontSmontK", true, 1); 
        VtOrdVenRigaPrmMontSmontKForm.setBODataCollector(VtOrdVenRigaPrmMontSmontKBODC); 
        int rcBODC = VtOrdVenRigaPrmMontSmontKForm.initSecurityServices(); 
        mode = VtOrdVenRigaPrmMontSmontKForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           VtOrdVenRigaPrmMontSmontKForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = VtOrdVenRigaPrmMontSmontKBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              VtOrdVenRigaPrmMontSmontKForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<style type="text/css">                                                                                               
			.cellStyleMagazzino{
    		border-width: 0px;
    		height:20px;
    		border-color:transparent;
     		background-color: #FFFFCC;
			}
			
			.cellStyleMagazzinoTD{
    		border-width: 0px;
    		height:20px;
    		width:15%;
    		border-color:transparent;
     		background-color: #FFFFCC;
			}
			
			.cellStyleComponente{
    		border-width: 0px;
    		font-size: 9px;
    		width:100%;
    		border-color:transparent;
     		background-color: #FFCC00;
			}
			
			.cellStyleComponenteTD{
    		border-width: 0px;
    		
    		width:15%;
    		border-color:transparent;
     		background-color: #FFCC00;
			}
			
			.cellStyleArticolo{
    		border-width: 0px;
    		font-size: 9px;
    		width:100%;
    		
    		border-color:transparent;
     		background-color: #CCCCCC;
			}
			
			
			.cellStyleArticoloTD{
    		border-width: 0px;
    		font-size: 10px;
    		width:50%;
    		
    		border-color:transparent;
     		background-color: #CCCCCC;
			}
			
			.cellStyleDati{
    		border-width: 0px;
    		width:100%;
    		border-color:transparent;
    		
     		background-color: #FFFFFF;
			}
				.cellStyleDatiTD{
    		border-width: 0px;
    		width:15%;
    		border-color:transparent;
    		
     		background-color: #FFFFFF;
			}
			
			
			table#matrice td{
			   
			   border-width: 0px;
    		width:10%;
    		border-color:transparent;
			    
			    
			}
					</style>
</head>
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=VtOrdVenRigaPrmMontSmontKForm.getBodyOnBeforeUnload()%>" onload="<%=VtOrdVenRigaPrmMontSmontKForm.getBodyOnLoad()%>" onunload="<%=VtOrdVenRigaPrmMontSmontKForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   VtOrdVenRigaPrmMontSmontKForm.writeBodyStartElements(out); 
%> 


	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = VtOrdVenRigaPrmMontSmontKForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", VtOrdVenRigaPrmMontSmontKBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=VtOrdVenRigaPrmMontSmontKForm.getServlet()%>" method="post" name="VtOrdVenRigaPrmMontSmontKForm" style="height:100%"><%
  VtOrdVenRigaPrmMontSmontKForm.writeFormStartElements(out); 
%>

		<table width="100%">
			<tr>
				<td><% 
  WebTextInput VtOrdVenRigaPrmMontSmontKIdAzienda =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "IdAzienda"); 
  VtOrdVenRigaPrmMontSmontKIdAzienda.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKIdAzienda.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKIdAzienda.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKIdAzienda.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKIdAzienda.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKIdAzienda.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmMontSmontKIdAzienda.write(out); 
%>

				</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmMontSmontKAnnoOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "AnnoOrdine"); 
  VtOrdVenRigaPrmMontSmontKAnnoOrdine.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKAnnoOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKAnnoOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKAnnoOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKAnnoOrdine.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKAnnoOrdine.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmMontSmontKAnnoOrdine.write(out); 
%>

					<% 
  WebTextInput VtOrdVenRigaPrmMontSmontKNumeroOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "NumeroOrdine"); 
  VtOrdVenRigaPrmMontSmontKNumeroOrdine.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKNumeroOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKNumeroOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKNumeroOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKNumeroOrdine.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKNumeroOrdine.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmMontSmontKNumeroOrdine.write(out); 
%>
</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmMontSmontKIdArticolo =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "IdArticolo"); 
  VtOrdVenRigaPrmMontSmontKIdArticolo.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKIdArticolo.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKIdArticolo.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKIdArticolo.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKIdArticolo.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKIdArticolo.getSize()%>" style="display: none;" value="0"><% 
  VtOrdVenRigaPrmMontSmontKIdArticolo.write(out); 
%>
</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmMontSmontKRigaOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "RigaOrdine"); 
  VtOrdVenRigaPrmMontSmontKRigaOrdine.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKRigaOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKRigaOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKRigaOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKRigaOrdine.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKRigaOrdine.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmMontSmontKRigaOrdine.write(out); 
%>

					<% 
  WebTextInput VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "DettaglioRigaOrdine"); 
  VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.getSize()%>" style="display: none;" value="0"><% 
  VtOrdVenRigaPrmMontSmontKDettaglioRigaOrdine.write(out); 
%>
</td>
				<td>
					<% 
  WebTextInput VtOrdVenRigaPrmMontSmontKGruppoTC =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "GruppoTC"); 
  VtOrdVenRigaPrmMontSmontKGruppoTC.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKGruppoTC.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKGruppoTC.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKGruppoTC.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKGruppoTC.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKGruppoTC.getSize()%>" style="display: none;" value="0"><% 
  VtOrdVenRigaPrmMontSmontKGruppoTC.write(out); 
%>

					</td>

			</tr>
		
		
			<tr>
				<td colspan="2"><label>Magazzino di partenza</label></td>
				<td colspan="3"><% 
  WebMultiSearchForm VtOrdVenRigaPrmMontSmontKRMagazzinoPart =  
     new com.thera.thermfw.web.WebMultiSearchForm("VtOrdVenRigaPrmMontSmontK", "RMagazzinoPart", false, false, true, 1, null, null); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoPart.setParent(VtOrdVenRigaPrmMontSmontKForm); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoPart.setOnKeyBlur("setTipo()"); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoPart.setOnKeyChange("setTipo()"); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoPart.setAdditionalRestrictConditions("IdAzienda, IdAzienda;AnnoOrdine,AnnoOrdine;NumeroOrdine,NumeroOrdine;RigaOrdine,RigaOrdine"); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoPart.setSpecificDOList("it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigaPrmMontSmontKDoList"); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoPart.write(out); 
%>
<!--<span class="multisearchform" id="RMagazzinoPart" name="RMagazzinoPart"></span>--></td>
			</tr>
		
			<tr>
				<td colspan="2"><label>Magazzino </label></td>
				<td colspan="3"><% 
  WebMultiSearchForm VtOrdVenRigaPrmMontSmontKRMagazzinoKit =  
     new com.thera.thermfw.web.WebMultiSearchForm("VtOrdVenRigaPrmMontSmontK", "RMagazzinoKit", false, false, true, 1, null, null); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoKit.setParent(VtOrdVenRigaPrmMontSmontKForm); 
  VtOrdVenRigaPrmMontSmontKRMagazzinoKit.write(out); 
%>
<!--<span class="multisearchform" id="RMagazzinoKit" name="RMagazzinoKit"></span>--></td>
			</tr>
			
			
				 <tr>
    		<td colspan="2"><label> Quantita </label></td>
    		<td colspan="3"><% 
  WebTextInput VtOrdVenRigaPrmMontSmontKQtaKit =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmMontSmontK", "QtaKit"); 
  VtOrdVenRigaPrmMontSmontKQtaKit.setOnChange("ricalcolaQtaMag()"); 
  VtOrdVenRigaPrmMontSmontKQtaKit.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%>
<input class="<%=VtOrdVenRigaPrmMontSmontKQtaKit.getClassType()%>" id="<%=VtOrdVenRigaPrmMontSmontKQtaKit.getId()%>" maxlength="<%=VtOrdVenRigaPrmMontSmontKQtaKit.getMaxLength()%>" name="<%=VtOrdVenRigaPrmMontSmontKQtaKit.getName()%>" size="<%=VtOrdVenRigaPrmMontSmontKQtaKit.getSize()%>"><% 
  VtOrdVenRigaPrmMontSmontKQtaKit.write(out); 
%>
 </td>
    	</tr> 
    	<tr>
    	<!-- Combo Tipo interno/esterno -->
				<td colspan="2" nowrap>
					<%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtOrdVenRigaPrmMontSmontK", "TipoMontSmont", null); 
   label.setParent(VtOrdVenRigaPrmMontSmontKForm); 
%><label class="<%=label.getClassType()%>" for="TipoMontSmont"><%label.write(out);%></label><%}%>
				</td>
				<td colspan="3">
					<% 
  WebComboBox VtOrdVenRigaPrmMontSmontKTipoMontSmont =  
     new com.thera.thermfw.web.WebComboBox("VtOrdVenRigaPrmMontSmontK", "TipoMontSmont", null); 
  VtOrdVenRigaPrmMontSmontKTipoMontSmont.setParent(VtOrdVenRigaPrmMontSmontKForm); 
  VtOrdVenRigaPrmMontSmontKTipoMontSmont.setOnChange(""); 
%>
<select id="<%=VtOrdVenRigaPrmMontSmontKTipoMontSmont.getId()%>" name="<%=VtOrdVenRigaPrmMontSmontKTipoMontSmont.getName()%>" readonly="true"><% 
  VtOrdVenRigaPrmMontSmontKTipoMontSmont.write(out); 
%> 

						
					</select>
				</td>
    	</tr>
    	<tr>
		<td colspan="5">
		&nbsp;
		&nbsp;
		</td>
		</tr>
    		<tr>
		<td colspan="5">
		
		<fieldset>
		<legend align="top"> 
						
			<label>
			Componenti
			</label>
			</legend>
			
		<table border="0" id="Matrice" name="Matrice" width="100%">
		</table>
		
		</fieldset>
		
		</td>
		
		
			
		
			</tr>
			
    	
		<tr>
		<td colspan="5">
		<table style="width:180px;">
			<tr>
			<td><button id="OKMONT" name="OKMONT" onclick="okButtonMontaggio();matrixReolad();" style=" width:180px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmMontSmontK", "OKMONT")%></button></td>
			
			<td><button id="OKSMONT" name="OKSMONT" onclick="okButtonSmontaggio();matrixReolad();" style=" width:180px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmMontSmontK", "OKSMONT")%></button></td>
			
			<td><button id="CHIUDI" name="CHIUDI" onclick="window.close();" style="width:80px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmMontSmontK", "CHIUDI")%></button></td>
			</tr>
		
		
		</table>
			</td>
			
			</tr>
			
			<iframe frameborder="0" height="0" id="errorsFrameName" name="errorsFrameName" style="visibility:hidden;" value="errorsFrameName" width="0"></iframe>
		</table>



	<%
  VtOrdVenRigaPrmMontSmontKForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = VtOrdVenRigaPrmMontSmontKForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", VtOrdVenRigaPrmMontSmontKBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>



<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              VtOrdVenRigaPrmMontSmontKForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, VtOrdVenRigaPrmMontSmontKBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, VtOrdVenRigaPrmMontSmontKBODC.getErrorList().getErrors()); 
           if(VtOrdVenRigaPrmMontSmontKBODC.getConflict() != null) 
                conflitPresent = true; 
     } 
     else 
        errors.add(new ErrorMessage("BAS0000010")); 
  } 
  catch(NamingException e) { 
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("CBS000025", errorMessage));  } 
  catch(SQLException e) {
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("BAS0000071", errorMessage));  } 
  catch(Throwable e) {
     e.printStackTrace(Trace.excStream);
  }
  finally 
  {
     if(VtOrdVenRigaPrmMontSmontKBODC != null && !VtOrdVenRigaPrmMontSmontKBODC.close(false)) 
        errors.addAll(0, VtOrdVenRigaPrmMontSmontKBODC.getErrorList().getErrors()); 
     try 
     { 
        se.end(); 
     }
     catch(IllegalArgumentException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
  } 
  if(!errors.isEmpty())
  { 
      if(!conflitPresent)
  { 
     request.setAttribute("ErrorMessages", errors); 
     String errorPage = VtOrdVenRigaPrmMontSmontKForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", VtOrdVenRigaPrmMontSmontKBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = VtOrdVenRigaPrmMontSmontKForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 

</body>
</html>
