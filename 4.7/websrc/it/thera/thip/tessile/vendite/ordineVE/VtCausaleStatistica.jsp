<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///W:\PthDev\Projects\Panthera\TEXTILE\WebContent\dtd/xhtml1-transitional.dtd">
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
  BODataCollector VtCausaleStatisticaBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm VtCausaleStatisticaForm =  
     new com.thera.thermfw.web.WebForm(request, response, "VtCausaleStatisticaForm", "VtCausaleStatistica", null, "com.thera.thermfw.web.servlet.FormActionAdapter", false, false, true, true, true, true, null, 1, true, "it/thera/thip/tessile/vendite/ordineVE/VtCausaleStatistica.js"); 
  VtCausaleStatisticaForm.setServletEnvironment(se); 
  VtCausaleStatisticaForm.setJSTypeList(jsList); 
  VtCausaleStatisticaForm.setHeader("it.thera.thip.cs.PantheraHeader.jsp"); 
  VtCausaleStatisticaForm.setFooter("com.thera.thermfw.common.Footer.jsp"); 
  VtCausaleStatisticaForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = VtCausaleStatisticaForm.getMode(); 
  String key = VtCausaleStatisticaForm.getKey(); 
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
        VtCausaleStatisticaForm.outTraceInfo(getClass().getName()); 
        String collectorName = VtCausaleStatisticaForm.findBODataCollectorName(); 
                VtCausaleStatisticaBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (VtCausaleStatisticaBODC instanceof WebDataCollector) 
            ((WebDataCollector)VtCausaleStatisticaBODC).setServletEnvironment(se); 
        VtCausaleStatisticaBODC.initialize("VtCausaleStatistica", true, 1); 
        VtCausaleStatisticaForm.setBODataCollector(VtCausaleStatisticaBODC); 
        int rcBODC = VtCausaleStatisticaForm.initSecurityServices(); 
        mode = VtCausaleStatisticaForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           VtCausaleStatisticaForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = VtCausaleStatisticaBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              VtCausaleStatisticaForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(VtCausaleStatisticaForm); 
   request.setAttribute("menuBar", menuBar); 
%> 
<jsp:include page="/com/thera/thermfw/common/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="menuBar"/> 
</jsp:include> 
<% 
  menuBar.write(out); 
  menuBar.writeChildren(out); 
%> 
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(VtCausaleStatisticaForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/com/thera/thermfw/common/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
  <body onbeforeunload="<%=VtCausaleStatisticaForm.getBodyOnBeforeUnload()%>" onload="<%=VtCausaleStatisticaForm.getBodyOnLoad()%>" onunload="<%=VtCausaleStatisticaForm.getBodyOnUnload()%>" style="margin: 0px; overflow: hidden;"><%
   VtCausaleStatisticaForm.writeBodyStartElements(out); 
%> 

    <table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = VtCausaleStatisticaForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", VtCausaleStatisticaBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=VtCausaleStatisticaForm.getServlet()%>" method="post" name="VtCausaleStatisticaForm" style="height:100%"><%
  VtCausaleStatisticaForm.writeFormStartElements(out); 
%>

      <table cellpadding="0" cellspacing="0" height="100%" id="emptyborder" width="100%">
        <tr>
          <td style="height:0">
            <% menuBar.writeElements(out); %> 

          </td>
        </tr>
        <tr>
          <td style="height:0">
            <% myToolBarTB.writeChildren(out); %> 

          </td>
        </tr>
        <tr>
          <td height="100%">
            <!--<span class="tabbed" id="mytabbed">-->
<table width="100%" height="100%" cellpadding="0" cellspacing="0" style="padding-right:1px">
   <tr valign="top">
     <td><% 
  WebTabbed mytabbed = new com.thera.thermfw.web.WebTabbed("mytabbed", "100%", "100%"); 
  mytabbed.setParent(VtCausaleStatisticaForm); 
 mytabbed.addTab("tab1", "it.thera.thip.tessile.vendite.ordineVE.resources.VtCausaleStatistica", "tab1", "VtCausaleStatistica", null, null, null, null); 
  mytabbed.write(out); 
%>

     </td>
   </tr>
   <tr>
     <td height="100%"><div class="tabbed_pagine" id="tabbedPagine" style="position: relative; width: 100%; height: 100%;">
              <div class="tabbed_page" id="<%=mytabbed.getTabPageId("tab1")%>" style="width:100%;height:100%;overflow:auto;"><% mytabbed.startTab("tab1"); %>
                <table style="width: 100%;">
                  <tr>
                    <td valign="top">
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtCausaleStatistica", "Idazienda", null); 
   label.setParent(VtCausaleStatisticaForm); 
%><label class="<%=label.getClassType()%>" for="Idazienda"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebTextInput VtCausaleStatisticaIdazienda =  
     new com.thera.thermfw.web.WebTextInput("VtCausaleStatistica", "Idazienda"); 
  VtCausaleStatisticaIdazienda.setParent(VtCausaleStatisticaForm); 
%>
<input class="<%=VtCausaleStatisticaIdazienda.getClassType()%>" id="<%=VtCausaleStatisticaIdazienda.getId()%>" maxlength="<%=VtCausaleStatisticaIdazienda.getMaxLength()%>" name="<%=VtCausaleStatisticaIdazienda.getName()%>" size="<%=VtCausaleStatisticaIdazienda.getSize()%>"><% 
  VtCausaleStatisticaIdazienda.write(out); 
%>

                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtCausaleStatistica", "IdCauStatistica", null); 
   label.setParent(VtCausaleStatisticaForm); 
%><label class="<%=label.getClassType()%>" for="IdCauStatistica"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebTextInput VtCausaleStatisticaIdCauStatistica =  
     new com.thera.thermfw.web.WebTextInput("VtCausaleStatistica", "IdCauStatistica"); 
  VtCausaleStatisticaIdCauStatistica.setParent(VtCausaleStatisticaForm); 
%>
<input class="<%=VtCausaleStatisticaIdCauStatistica.getClassType()%>" id="<%=VtCausaleStatisticaIdCauStatistica.getId()%>" maxlength="<%=VtCausaleStatisticaIdCauStatistica.getMaxLength()%>" name="<%=VtCausaleStatisticaIdCauStatistica.getName()%>" size="<%=VtCausaleStatisticaIdCauStatistica.getSize()%>"><% 
  VtCausaleStatisticaIdCauStatistica.write(out); 
%>

                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtCausaleStatistica", "Codicedescrizione", null); 
   label.setParent(VtCausaleStatisticaForm); 
%><label class="<%=label.getClassType()%>" for="Codicedescrizione"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebTextInput VtCausaleStatisticaCodicedescrizione =  
     new com.thera.thermfw.web.WebTextInput("VtCausaleStatistica", "Codicedescrizione"); 
  VtCausaleStatisticaCodicedescrizione.setParent(VtCausaleStatisticaForm); 
%>
<input class="<%=VtCausaleStatisticaCodicedescrizione.getClassType()%>" id="<%=VtCausaleStatisticaCodicedescrizione.getId()%>" maxlength="<%=VtCausaleStatisticaCodicedescrizione.getMaxLength()%>" name="<%=VtCausaleStatisticaCodicedescrizione.getName()%>" size="<%=VtCausaleStatisticaCodicedescrizione.getSize()%>"><% 
  VtCausaleStatisticaCodicedescrizione.write(out); 
%>

                    </td>
                  </tr>
                </table>
              <% mytabbed.endTab(); %> 
</div>
            </div><% mytabbed.endTabbed();%> 

     </td>
   </tr>
</table><!--</span>-->
          </td>
        </tr>
        <tr>
          <td style="height:0">
            <% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(VtCausaleStatisticaForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>-->
          </td>
        </tr>
      </table>
    <%
  VtCausaleStatisticaForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = VtCausaleStatisticaForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", VtCausaleStatisticaBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


  <%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              VtCausaleStatisticaForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, VtCausaleStatisticaBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, VtCausaleStatisticaBODC.getErrorList().getErrors()); 
           if(VtCausaleStatisticaBODC.getConflict() != null) 
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
     if(VtCausaleStatisticaBODC != null && !VtCausaleStatisticaBODC.close(false)) 
        errors.addAll(0, VtCausaleStatisticaBODC.getErrorList().getErrors()); 
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
     String errorPage = VtCausaleStatisticaForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", VtCausaleStatisticaBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = VtCausaleStatisticaForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
