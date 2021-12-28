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
  BODataCollector VtCausaleSaldoManualeRigaBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm VtCausaleSaldoManualeRigaForm =  
     new com.thera.thermfw.web.WebForm(request, response, "VtCausaleSaldoManualeRigaForm", "VtCausaleSaldoManualeRiga", null, "com.thera.thermfw.web.servlet.FormActionAdapter", false, false, true, true, true, true, null, 1, true, "it/thera/thip/tessile/vendite/ordineVE/VtCausaleSaldoManualeRiga.js"); 
  VtCausaleSaldoManualeRigaForm.setServletEnvironment(se); 
  VtCausaleSaldoManualeRigaForm.setJSTypeList(jsList); 
  VtCausaleSaldoManualeRigaForm.setHeader("it.thera.thip.cs.PantheraHeader.jsp"); 
  VtCausaleSaldoManualeRigaForm.setFooter("com.thera.thermfw.common.Footer.jsp"); 
  VtCausaleSaldoManualeRigaForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = VtCausaleSaldoManualeRigaForm.getMode(); 
  String key = VtCausaleSaldoManualeRigaForm.getKey(); 
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
        VtCausaleSaldoManualeRigaForm.outTraceInfo(getClass().getName()); 
        String collectorName = VtCausaleSaldoManualeRigaForm.findBODataCollectorName(); 
                VtCausaleSaldoManualeRigaBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (VtCausaleSaldoManualeRigaBODC instanceof WebDataCollector) 
            ((WebDataCollector)VtCausaleSaldoManualeRigaBODC).setServletEnvironment(se); 
        VtCausaleSaldoManualeRigaBODC.initialize("VtCausaleSaldoManualeRiga", true, 1); 
        VtCausaleSaldoManualeRigaForm.setBODataCollector(VtCausaleSaldoManualeRigaBODC); 
        int rcBODC = VtCausaleSaldoManualeRigaForm.initSecurityServices(); 
        mode = VtCausaleSaldoManualeRigaForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           VtCausaleSaldoManualeRigaForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = VtCausaleSaldoManualeRigaBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              VtCausaleSaldoManualeRigaForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(VtCausaleSaldoManualeRigaForm); 
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
  myToolBarTB.setParent(VtCausaleSaldoManualeRigaForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/com/thera/thermfw/common/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
  <body onbeforeunload="<%=VtCausaleSaldoManualeRigaForm.getBodyOnBeforeUnload()%>" onload="<%=VtCausaleSaldoManualeRigaForm.getBodyOnLoad()%>" onunload="<%=VtCausaleSaldoManualeRigaForm.getBodyOnUnload()%>" style="margin: 0px; overflow: hidden;"><%
   VtCausaleSaldoManualeRigaForm.writeBodyStartElements(out); 
%> 

    <table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = VtCausaleSaldoManualeRigaForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", VtCausaleSaldoManualeRigaBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=VtCausaleSaldoManualeRigaForm.getServlet()%>" method="post" name="VtCausaleSaldoManualeRigaForm" style="height:100%"><%
  VtCausaleSaldoManualeRigaForm.writeFormStartElements(out); 
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
  mytabbed.setParent(VtCausaleSaldoManualeRigaForm); 
 mytabbed.addTab("tab1", "it.thera.thip.tessile.vendite.ordineVE.resources.VtCausaleSaldoManualeRiga", "tab1", "VtCausaleSaldoManualeRiga", null, null, null, null); 
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
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtCausaleSaldoManualeRiga", "Idazienda", null); 
   label.setParent(VtCausaleSaldoManualeRigaForm); 
%><label class="<%=label.getClassType()%>" for="Idazienda"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebTextInput VtCausaleSaldoManualeRigaIdazienda =  
     new com.thera.thermfw.web.WebTextInput("VtCausaleSaldoManualeRiga", "Idazienda"); 
  VtCausaleSaldoManualeRigaIdazienda.setParent(VtCausaleSaldoManualeRigaForm); 
%>
<input class="<%=VtCausaleSaldoManualeRigaIdazienda.getClassType()%>" id="<%=VtCausaleSaldoManualeRigaIdazienda.getId()%>" maxlength="<%=VtCausaleSaldoManualeRigaIdazienda.getMaxLength()%>" name="<%=VtCausaleSaldoManualeRigaIdazienda.getName()%>" size="<%=VtCausaleSaldoManualeRigaIdazienda.getSize()%>"><% 
  VtCausaleSaldoManualeRigaIdazienda.write(out); 
%>

                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtCausaleSaldoManualeRiga", "Codicedescrizione", null); 
   label.setParent(VtCausaleSaldoManualeRigaForm); 
%><label class="<%=label.getClassType()%>" for="Codicedescrizione"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebTextInput VtCausaleSaldoManualeRigaCodicedescrizione =  
     new com.thera.thermfw.web.WebTextInput("VtCausaleSaldoManualeRiga", "Codicedescrizione"); 
  VtCausaleSaldoManualeRigaCodicedescrizione.setParent(VtCausaleSaldoManualeRigaForm); 
%>
<input class="<%=VtCausaleSaldoManualeRigaCodicedescrizione.getClassType()%>" id="<%=VtCausaleSaldoManualeRigaCodicedescrizione.getId()%>" maxlength="<%=VtCausaleSaldoManualeRigaCodicedescrizione.getMaxLength()%>" name="<%=VtCausaleSaldoManualeRigaCodicedescrizione.getName()%>" size="<%=VtCausaleSaldoManualeRigaCodicedescrizione.getSize()%>"><% 
  VtCausaleSaldoManualeRigaCodicedescrizione.write(out); 
%>

                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "VtCausaleSaldoManualeRiga", "IdCauSaldoManualeRiga", null); 
   label.setParent(VtCausaleSaldoManualeRigaForm); 
%><label class="<%=label.getClassType()%>" for="IdCauSaldoManualeRiga"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebTextInput VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga =  
     new com.thera.thermfw.web.WebTextInput("VtCausaleSaldoManualeRiga", "IdCauSaldoManualeRiga"); 
  VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.setParent(VtCausaleSaldoManualeRigaForm); 
%>
<input class="<%=VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.getClassType()%>" id="<%=VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.getId()%>" maxlength="<%=VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.getMaxLength()%>" name="<%=VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.getName()%>" size="<%=VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.getSize()%>"><% 
  VtCausaleSaldoManualeRigaIdCauSaldoManualeRiga.write(out); 
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
  errorList.setParent(VtCausaleSaldoManualeRigaForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>-->
          </td>
        </tr>
      </table>
    <%
  VtCausaleSaldoManualeRigaForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = VtCausaleSaldoManualeRigaForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", VtCausaleSaldoManualeRigaBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


  <%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              VtCausaleSaldoManualeRigaForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, VtCausaleSaldoManualeRigaBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, VtCausaleSaldoManualeRigaBODC.getErrorList().getErrors()); 
           if(VtCausaleSaldoManualeRigaBODC.getConflict() != null) 
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
     if(VtCausaleSaldoManualeRigaBODC != null && !VtCausaleSaldoManualeRigaBODC.close(false)) 
        errors.addAll(0, VtCausaleSaldoManualeRigaBODC.getErrorList().getErrors()); 
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
     String errorPage = VtCausaleSaldoManualeRigaForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", VtCausaleSaldoManualeRigaBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = VtCausaleSaldoManualeRigaForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
