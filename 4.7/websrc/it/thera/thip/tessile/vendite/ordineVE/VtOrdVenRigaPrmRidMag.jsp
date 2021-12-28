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
  BODataCollector VtOrdVenRigaPrmRidMagBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm VtOrdVenRigaPrmRidMagForm =  
     new com.thera.thermfw.web.WebForm(request, response, "VtOrdVenRigaPrmRidMagForm", "VtOrdVenRigaPrmRidMag", "Arial,10", "it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigaPrmRidMagFormActionAdapter", false, false, false, false, true, true, null, 1, true, "it/thera/thip/tessile/vendite/ordineVE/VtOrdVenRigaPrmRidMag.js"); 
  VtOrdVenRigaPrmRidMagForm.setServletEnvironment(se); 
  VtOrdVenRigaPrmRidMagForm.setJSTypeList(jsList); 
  VtOrdVenRigaPrmRidMagForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  VtOrdVenRigaPrmRidMagForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  VtOrdVenRigaPrmRidMagForm.setWebFormModifierClass("it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigaPrmRidMagFormModifier"); 
  VtOrdVenRigaPrmRidMagForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = VtOrdVenRigaPrmRidMagForm.getMode(); 
  String key = VtOrdVenRigaPrmRidMagForm.getKey(); 
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
        VtOrdVenRigaPrmRidMagForm.outTraceInfo(getClass().getName()); 
        String collectorName = VtOrdVenRigaPrmRidMagForm.findBODataCollectorName(); 
                VtOrdVenRigaPrmRidMagBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (VtOrdVenRigaPrmRidMagBODC instanceof WebDataCollector) 
            ((WebDataCollector)VtOrdVenRigaPrmRidMagBODC).setServletEnvironment(se); 
        VtOrdVenRigaPrmRidMagBODC.initialize("VtOrdVenRigaPrmRidMag", true, 1); 
        VtOrdVenRigaPrmRidMagForm.setBODataCollector(VtOrdVenRigaPrmRidMagBODC); 
        int rcBODC = VtOrdVenRigaPrmRidMagForm.initSecurityServices(); 
        mode = VtOrdVenRigaPrmRidMagForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           VtOrdVenRigaPrmRidMagForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = VtOrdVenRigaPrmRidMagBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              VtOrdVenRigaPrmRidMagForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<style type="text/css">                                                                                               
			.cellStyleMagazzino{
    		 border: 1px  black;
     		background-color: #FFFFCC;
			}
			.cellStyleComponente{
    		 border: 1px  black;
     		background-color: #FFCC00;
			}
			.cellStyleArticolo{
    		 border: 1px  black;
     		background-color: #CCCCCC;
			}
			.cellStyleDati{
    		 border: 1px black;
     		background-color: #FFFFFF;
			}
					</style>
</head>
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=VtOrdVenRigaPrmRidMagForm.getBodyOnBeforeUnload()%>" onload="<%=VtOrdVenRigaPrmRidMagForm.getBodyOnLoad()%>" onunload="<%=VtOrdVenRigaPrmRidMagForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   VtOrdVenRigaPrmRidMagForm.writeBodyStartElements(out); 
%> 


	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = VtOrdVenRigaPrmRidMagForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", VtOrdVenRigaPrmRidMagBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=VtOrdVenRigaPrmRidMagForm.getServlet()%>" method="post" name="VtOrdVenRigaPrmRidMagForm" style="height:100%"><%
  VtOrdVenRigaPrmRidMagForm.writeFormStartElements(out); 
%>

		<table>
			<tr>
				<td><% 
  WebTextInput VtOrdVenRigaPrmRidMagIdAzienda =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "IdAzienda"); 
  VtOrdVenRigaPrmRidMagIdAzienda.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagIdAzienda.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagIdAzienda.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagIdAzienda.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagIdAzienda.getName()%>" size="<%=VtOrdVenRigaPrmRidMagIdAzienda.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmRidMagIdAzienda.write(out); 
%>
</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmRidMagAnnoOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "AnnoOrdine"); 
  VtOrdVenRigaPrmRidMagAnnoOrdine.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagAnnoOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagAnnoOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagAnnoOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagAnnoOrdine.getName()%>" size="<%=VtOrdVenRigaPrmRidMagAnnoOrdine.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmRidMagAnnoOrdine.write(out); 
%>
</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmRidMagNumeroOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "NumeroOrdine"); 
  VtOrdVenRigaPrmRidMagNumeroOrdine.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagNumeroOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagNumeroOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagNumeroOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagNumeroOrdine.getName()%>" size="<%=VtOrdVenRigaPrmRidMagNumeroOrdine.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmRidMagNumeroOrdine.write(out); 
%>
</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmRidMagRigaOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "RigaOrdine"); 
  VtOrdVenRigaPrmRidMagRigaOrdine.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagRigaOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagRigaOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagRigaOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagRigaOrdine.getName()%>" size="<%=VtOrdVenRigaPrmRidMagRigaOrdine.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmRidMagRigaOrdine.write(out); 
%>
</td>
				<td><% 
  WebTextInput VtOrdVenRigaPrmRidMagDettaglioRigaOrdine =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "DettaglioRigaOrdine"); 
  VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.getName()%>" size="<%=VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.getSize()%>" style="display: none;" value="0"><% 
  VtOrdVenRigaPrmRidMagDettaglioRigaOrdine.write(out); 
%>
<% 
  WebTextInput VtOrdVenRigaPrmRidMagIdArticolo =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "IdArticolo"); 
  VtOrdVenRigaPrmRidMagIdArticolo.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagIdArticolo.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagIdArticolo.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagIdArticolo.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagIdArticolo.getName()%>" size="<%=VtOrdVenRigaPrmRidMagIdArticolo.getSize()%>" style="display: none;"><% 
  VtOrdVenRigaPrmRidMagIdArticolo.write(out); 
%>
<% 
  WebTextInput VtOrdVenRigaPrmRidMagGruppoTC =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "GruppoTC"); 
  VtOrdVenRigaPrmRidMagGruppoTC.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagGruppoTC.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagGruppoTC.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagGruppoTC.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagGruppoTC.getName()%>" size="<%=VtOrdVenRigaPrmRidMagGruppoTC.getSize()%>" style="display: none;" value="0"><% 
  VtOrdVenRigaPrmRidMagGruppoTC.write(out); 
%>
</td>

			</tr>
		</table>

		<table>
		
			<tr>
				<td><label>Magazzino di partenza</label></td>
				<td><% 
  WebMultiSearchForm VtOrdVenRigaPrmRidMagRMagazzinoPart =  
     new com.thera.thermfw.web.WebMultiSearchForm("VtOrdVenRigaPrmRidMag", "RMagazzinoPart", false, false, true, 1, null, null); 
  VtOrdVenRigaPrmRidMagRMagazzinoPart.setParent(VtOrdVenRigaPrmRidMagForm); 
  VtOrdVenRigaPrmRidMagRMagazzinoPart.write(out); 
%>
<!--<span class="multisearchform" id="RMagazzinoPart" name="RMagazzinoPart"></span>--></td>
			</tr>
			
			<tr>
				<td><label>Magazzino di destinazione</label></td>
				<td><% 
  WebMultiSearchForm VtOrdVenRigaPrmRidMagRMagazzinoDest =  
     new com.thera.thermfw.web.WebMultiSearchForm("VtOrdVenRigaPrmRidMag", "RMagazzinoDest", false, false, true, 1, null, null); 
  VtOrdVenRigaPrmRidMagRMagazzinoDest.setParent(VtOrdVenRigaPrmRidMagForm); 
  VtOrdVenRigaPrmRidMagRMagazzinoDest.write(out); 
%>
<!--<span class="multisearchform" id="RMagazzinoDest" name="RMagazzinoDest"></span>--></td>
			</tr>
			
				 <tr>
    		<td><label> Quantita da trasferire</label></td>
    		<td><% 
  WebTextInput VtOrdVenRigaPrmRidMagQtaTrasferire =  
     new com.thera.thermfw.web.WebTextInput("VtOrdVenRigaPrmRidMag", "QtaTrasferire"); 
  VtOrdVenRigaPrmRidMagQtaTrasferire.setParent(VtOrdVenRigaPrmRidMagForm); 
%>
<input class="<%=VtOrdVenRigaPrmRidMagQtaTrasferire.getClassType()%>" id="<%=VtOrdVenRigaPrmRidMagQtaTrasferire.getId()%>" maxlength="<%=VtOrdVenRigaPrmRidMagQtaTrasferire.getMaxLength()%>" name="<%=VtOrdVenRigaPrmRidMagQtaTrasferire.getName()%>" size="<%=VtOrdVenRigaPrmRidMagQtaTrasferire.getSize()%>"><% 
  VtOrdVenRigaPrmRidMagQtaTrasferire.write(out); 
%>
 </td>
    	</tr> 
    	<tr>
		<td colspan="2">
		
		</td>
		</tr>
    		<!--  <tr>   <td colspan="2">   <table id="Matrice" cellpadding="2" name="Matrice" align="center" style="width:90%;border: 1px solid #000">         </table>    </td>        </tr>-->
			
    	
		<tr>
		<td colspan="2" style="width:180px;">
		<table>
			<tr>
			<td><button id="OK" name="OK" onclick="okButtonSdoppio();matrixReolad();" style=" width:180px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmRidMag", "OK")%></button></td>
			
			<td><button id="OKTRASF" name="OKTRASF" onclick="okButtonTrasferimento();matrixReolad();" style=" width:180px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmRidMag", "OKTRASF")%></button></td>
			
			<td><button id="OKONLYTRASF" name="OKONLYTRASF" onclick="okButtonOnlyTrasferimento();matrixReolad();" style=" width:180px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmRidMag", "OKONLYTRASF")%></button></td>
			
			<td><button id="CHIUDI" name="CHIUDI" onclick="window.close();" style="width:80px;background: #A5B6CE;border-color: #A5B6CE;" type="button"><%= ResourceLoader.getString("it.thera.thip.tessile.vendite.ordineVE.resources.VtOrdVenRigaPrmRidMag", "CHIUDI")%></button></td>
			</tr>
		
		
		</table>
			</td>
			
			</tr>
			
			
		</table>




	<%
  VtOrdVenRigaPrmRidMagForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = VtOrdVenRigaPrmRidMagForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", VtOrdVenRigaPrmRidMagBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>



<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              VtOrdVenRigaPrmRidMagForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, VtOrdVenRigaPrmRidMagBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, VtOrdVenRigaPrmRidMagBODC.getErrorList().getErrors()); 
           if(VtOrdVenRigaPrmRidMagBODC.getConflict() != null) 
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
     if(VtOrdVenRigaPrmRidMagBODC != null && !VtOrdVenRigaPrmRidMagBODC.close(false)) 
        errors.addAll(0, VtOrdVenRigaPrmRidMagBODC.getErrorList().getErrors()); 
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
     String errorPage = VtOrdVenRigaPrmRidMagForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", VtOrdVenRigaPrmRidMagBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = VtOrdVenRigaPrmRidMagForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
