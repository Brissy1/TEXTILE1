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
  BODataCollector VtDocKitOrdVenRigaBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm VtDocKitOrdVenRigaForm =  
     new com.thera.thermfw.web.WebForm(request, response, "VtDocKitOrdVenRigaForm", "VtDocKitOrdVenRiga", null, "com.thera.thermfw.web.servlet.FormActionAdapter", false, false, true, true, true, true, null, 1, true, "it/thera/thip/tessile/vendite/ordineVE/VtDocKitOrdVenRiga.js"); 
  VtDocKitOrdVenRigaForm.setServletEnvironment(se); 
  VtDocKitOrdVenRigaForm.setJSTypeList(jsList); 
  VtDocKitOrdVenRigaForm.setHeader("it.thera.thip.cs.PantheraHeader.jsp"); 
  VtDocKitOrdVenRigaForm.setFooter("com.thera.thermfw.common.Footer.jsp"); 
  VtDocKitOrdVenRigaForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = VtDocKitOrdVenRigaForm.getMode(); 
  String key = VtDocKitOrdVenRigaForm.getKey(); 
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
        VtDocKitOrdVenRigaForm.outTraceInfo(getClass().getName()); 
        String collectorName = VtDocKitOrdVenRigaForm.findBODataCollectorName(); 
                VtDocKitOrdVenRigaBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (VtDocKitOrdVenRigaBODC instanceof WebDataCollector) 
            ((WebDataCollector)VtDocKitOrdVenRigaBODC).setServletEnvironment(se); 
        VtDocKitOrdVenRigaBODC.initialize("VtDocKitOrdVenRiga", true, 1); 
        VtDocKitOrdVenRigaForm.setBODataCollector(VtDocKitOrdVenRigaBODC); 
        int rcBODC = VtDocKitOrdVenRigaForm.initSecurityServices(); 
        mode = VtDocKitOrdVenRigaForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           VtDocKitOrdVenRigaForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = VtDocKitOrdVenRigaBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              VtDocKitOrdVenRigaForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(VtDocKitOrdVenRigaForm); 
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
  myToolBarTB.setParent(VtDocKitOrdVenRigaForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/com/thera/thermfw/common/defObjMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
  <body onbeforeunload="<%=VtDocKitOrdVenRigaForm.getBodyOnBeforeUnload()%>" onload="<%=VtDocKitOrdVenRigaForm.getBodyOnLoad()%>" onunload="<%=VtDocKitOrdVenRigaForm.getBodyOnUnload()%>" style="margin: 0px; overflow: hidden;"><%
   VtDocKitOrdVenRigaForm.writeBodyStartElements(out); 
%> 

    <table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = VtDocKitOrdVenRigaForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", VtDocKitOrdVenRigaBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=VtDocKitOrdVenRigaForm.getServlet()%>" method="post" name="VtDocKitOrdVenRigaForm" style="height:100%"><%
  VtDocKitOrdVenRigaForm.writeFormStartElements(out); 
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
          <td>
            <% 
  WebTextInput VtDocKitOrdVenRigaIdAzienda =  
     new com.thera.thermfw.web.WebTextInput("VtDocKitOrdVenRiga", "IdAzienda"); 
  VtDocKitOrdVenRigaIdAzienda.setParent(VtDocKitOrdVenRigaForm); 
%>
<input class="<%=VtDocKitOrdVenRigaIdAzienda.getClassType()%>" id="<%=VtDocKitOrdVenRigaIdAzienda.getId()%>" maxlength="<%=VtDocKitOrdVenRigaIdAzienda.getMaxLength()%>" name="<%=VtDocKitOrdVenRigaIdAzienda.getName()%>" size="<%=VtDocKitOrdVenRigaIdAzienda.getSize()%>" type="hidden"><% 
  VtDocKitOrdVenRigaIdAzienda.write(out); 
%>

          </td>
        </tr>
        <tr>
          <td>
            <% 
  WebTextInput VtDocKitOrdVenRigaIdAnnoOrd =  
     new com.thera.thermfw.web.WebTextInput("VtDocKitOrdVenRiga", "IdAnnoOrd"); 
  VtDocKitOrdVenRigaIdAnnoOrd.setParent(VtDocKitOrdVenRigaForm); 
%>
<input class="<%=VtDocKitOrdVenRigaIdAnnoOrd.getClassType()%>" id="<%=VtDocKitOrdVenRigaIdAnnoOrd.getId()%>" maxlength="<%=VtDocKitOrdVenRigaIdAnnoOrd.getMaxLength()%>" name="<%=VtDocKitOrdVenRigaIdAnnoOrd.getName()%>" size="<%=VtDocKitOrdVenRigaIdAnnoOrd.getSize()%>" type="hidden"><% 
  VtDocKitOrdVenRigaIdAnnoOrd.write(out); 
%>

          </td>
        </tr>
        <tr>
          <td>
            <% 
  WebTextInput VtDocKitOrdVenRigaIdNumeroOrd =  
     new com.thera.thermfw.web.WebTextInput("VtDocKitOrdVenRiga", "IdNumeroOrd"); 
  VtDocKitOrdVenRigaIdNumeroOrd.setParent(VtDocKitOrdVenRigaForm); 
%>
<input class="<%=VtDocKitOrdVenRigaIdNumeroOrd.getClassType()%>" id="<%=VtDocKitOrdVenRigaIdNumeroOrd.getId()%>" maxlength="<%=VtDocKitOrdVenRigaIdNumeroOrd.getMaxLength()%>" name="<%=VtDocKitOrdVenRigaIdNumeroOrd.getName()%>" size="<%=VtDocKitOrdVenRigaIdNumeroOrd.getSize()%>" type="hidden"><% 
  VtDocKitOrdVenRigaIdNumeroOrd.write(out); 
%>

          </td>
        </tr>
        <tr>
          <td>
            <% 
  WebTextInput VtDocKitOrdVenRigaIdAnnoDoc =  
     new com.thera.thermfw.web.WebTextInput("VtDocKitOrdVenRiga", "IdAnnoDoc"); 
  VtDocKitOrdVenRigaIdAnnoDoc.setParent(VtDocKitOrdVenRigaForm); 
%>
<input class="<%=VtDocKitOrdVenRigaIdAnnoDoc.getClassType()%>" id="<%=VtDocKitOrdVenRigaIdAnnoDoc.getId()%>" maxlength="<%=VtDocKitOrdVenRigaIdAnnoDoc.getMaxLength()%>" name="<%=VtDocKitOrdVenRigaIdAnnoDoc.getName()%>" size="<%=VtDocKitOrdVenRigaIdAnnoDoc.getSize()%>" type="hidden"><% 
  VtDocKitOrdVenRigaIdAnnoDoc.write(out); 
%>

          </td>
        </tr>
        <tr>
          <td>
            <% 
  WebTextInput VtDocKitOrdVenRigaIdNumeroDoc =  
     new com.thera.thermfw.web.WebTextInput("VtDocKitOrdVenRiga", "IdNumeroDoc"); 
  VtDocKitOrdVenRigaIdNumeroDoc.setParent(VtDocKitOrdVenRigaForm); 
%>
<input class="<%=VtDocKitOrdVenRigaIdNumeroDoc.getClassType()%>" id="<%=VtDocKitOrdVenRigaIdNumeroDoc.getId()%>" maxlength="<%=VtDocKitOrdVenRigaIdNumeroDoc.getMaxLength()%>" name="<%=VtDocKitOrdVenRigaIdNumeroDoc.getName()%>" size="<%=VtDocKitOrdVenRigaIdNumeroDoc.getSize()%>" type="hidden"><% 
  VtDocKitOrdVenRigaIdNumeroDoc.write(out); 
%>

          </td>
        </tr>
        <tr>
          <td>
            <% 
  WebTextInput VtDocKitOrdVenRigaTipo =  
     new com.thera.thermfw.web.WebTextInput("VtDocKitOrdVenRiga", "Tipo"); 
  VtDocKitOrdVenRigaTipo.setParent(VtDocKitOrdVenRigaForm); 
%>
<input class="<%=VtDocKitOrdVenRigaTipo.getClassType()%>" id="<%=VtDocKitOrdVenRigaTipo.getId()%>" maxlength="<%=VtDocKitOrdVenRigaTipo.getMaxLength()%>" name="<%=VtDocKitOrdVenRigaTipo.getName()%>" size="<%=VtDocKitOrdVenRigaTipo.getSize()%>" type="hidden"><% 
  VtDocKitOrdVenRigaTipo.write(out); 
%>

          </td>
        </tr>
        <tr>
          <td height="100%">
            <!--<span class="tabbed" id="mytabbed">-->
<table width="100%" height="100%" cellpadding="0" cellspacing="0" style="padding-right:1px">
   <tr valign="top">
     <td><% 
  WebTabbed mytabbed = new com.thera.thermfw.web.WebTabbed("mytabbed", "100%", "100%"); 
  mytabbed.setParent(VtDocKitOrdVenRigaForm); 
 mytabbed.addTab("tab1", "it.thera.thip.tessile.vendite.ordineVE.resources.VtDocKitOrdVenRiga", "tab1", "VtDocKitOrdVenRiga", null, null, null, null); 
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
                    </td>
                    <td valign="top">
                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
                    </td>
                  </tr>
                  <tr>
                    <td valign="top">
                    </td>
                    <td valign="top">
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
  errorList.setParent(VtDocKitOrdVenRigaForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>-->
          </td>
        </tr>
      </table>
    <%
  VtDocKitOrdVenRigaForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = VtDocKitOrdVenRigaForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", VtDocKitOrdVenRigaBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


  <%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              VtDocKitOrdVenRigaForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, VtDocKitOrdVenRigaBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, VtDocKitOrdVenRigaBODC.getErrorList().getErrors()); 
           if(VtDocKitOrdVenRigaBODC.getConflict() != null) 
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
     if(VtDocKitOrdVenRigaBODC != null && !VtDocKitOrdVenRigaBODC.close(false)) 
        errors.addAll(0, VtDocKitOrdVenRigaBODC.getErrorList().getErrors()); 
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
     String errorPage = VtDocKitOrdVenRigaForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", VtDocKitOrdVenRigaBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = VtDocKitOrdVenRigaForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
