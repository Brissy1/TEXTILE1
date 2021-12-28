function VtOrdVenRigaPrmMontSmontKOL() {
	window.resizeTo(850, 500);
	window.moveTo(300, 100);

}

function okButtonMontaggio() {
	var qta = document.getElementById('QtaKit').value;
	var magPart = document.getElementById('IdMagazzinoPart').value;
	var magKit = document.getElementById('IdMagazzinoKit').value;
	
	if (magKit != "" && qta != "" && qta != "0,00" ) {
		var className = eval("document.forms[0].thClassName.value");
		if(checkQtaMag())
			runActionDirect('MONT', 'action_submit', className, null, 'same', 'no');
	} else
		alert("Attenzione: 'Magazzino partenza','Magazzino' e 'Quantita' sono campi obbligatori!");

}

function addRowMatrice(tableID, indiciRow, valuesRow, colorName) {

	var tableRef = document.getElementById(tableID);

	var newRow = tableRef.insertRow(-1);

	for (var i = 0; i < indiciRow.length; i++) {

		// Insert a cell in the row at index 0
		var newCell = newRow.insertCell(i);
		// newCell.className =colorName[i];

		if (indiciRow[i] == "")
			newCell.innerHTML = "<input type=\"text\"  class=\"" + colorName[i]
					+ "\" value=\"" + valuesRow[i] + "\"/>";
		else
			newCell.innerHTML = "<input type=\"text\" id=\"" + indiciRow[i]
					+ "\" name=\"" + indiciRow[i] + "\"  class=\""
					+ colorName[i] + "\" value=\"" + valuesRow[i] + "\"/>";

		newCell.className = colorName[i] + "TD";

		// Append a text node to the cell
		// var newText = document.createTextNode(valuesRow[i]);
		// newCell.appendChild(newText);

	}

}

function ricalcolaQtaMag() {
	var qta = document.getElementById('QtaKit').value;
	var magPart = document.getElementById('IdMagazzinoPart').value;
	var azienda = document.getElementById('IdAzienda').value;
	var keyMagazzino = azienda + '' + magPart

	var elDati = document.getElementsByClassName("cellStyleDati");
	for (var i = 0; i < elDati.length; i++) {

		var idElement = elDati[i].id
		if (idElement.indexOf(keyMagazzino) > -1) {

			var arrayKey = idElement.split("-");

			if (arrayKey[1] == "KIT")
				document.getElementById(idElement).value = parseInt(qta);
			else
				document.getElementById(idElement).value = parseInt(qta)
						* parseInt(arrayKey[2]);
		}
	}

}

function checkQtaMag() {
	var qta = document.getElementById('QtaKit').value;
	var magPart = document.getElementById('IdMagazzinoPart').value;
	var azienda = document.getElementById('IdAzienda').value;
	var keyMagazzino = azienda + '' + magPart
	var keyColorOld = "";
	var elDati = document.getElementsByClassName("cellStyleDati");
	var qtaColor = 0;
	var coeff=0;
    var esito=true;
    
	for (var i = 0; i < elDati.length; i++) {

		var idElement = elDati[i].id

		var arrayKey = idElement.split("-");
		if (arrayKey[1] != "KIT") {
			var qtaComp=0;
			
			if(document.getElementById(idElement).value !=null && document.getElementById(idElement).value!="")
				qtaComp=parseInt(document.getElementById(idElement).value);
			
			var keyColorNew = arrayKey[0] + "-" + arrayKey[1];

			if (keyColorOld != keyColorNew) {
				if (keyColorOld != "" && coeff != qtaColor)
					{
					alert("Attenzione la somma del componente " + keyColorOld
							+ " deve essere uguale a: " + coeff);
					esito=false;
					}
				qtaColor = qtaComp;
				coeff=parseInt(qta)* parseInt(arrayKey[2]);
				keyColorOld = keyColorNew;
							
			} else if(i==(elDati.length-1) && coeff != qtaColor){
				//ultimo elemento
				
					alert("Attenzione la somma del componente " + keyColorNew
							+ " deve essere uguale a: " + coeff);
					esito=false;
				
			}
			else
				qtaColor = parseInt(qtaColor)+ qtaComp;

		}
	}
	return esito;
}

function okButtonSmontaggio() {
	var qta = document.getElementById('QtaKit').value;
	var magKit = document.getElementById('IdMagazzinoKit').value;

	if (magKit != "" && qta != "" && qta != "0,00") {
		var className = eval("document.forms[0].thClassName.value");
		if(checkQtaMag())
		runActionDirect('SMONT', 'action_submit', className, null, 'same', 'no');
	} else
		alert("Attenzione: 'Magazzino'  e 'Quantita' sono campi obbligatori!");

}

function setTipo() {
	var f = document.forms[0];
	var url = "";
	var key;

	url = "/" + webAppPath + "/" + servletPath + nomeServletRecTipo();

	url = url + "?IdAzienda="
			+ eval("document.forms[0]." + idFromName['IdAzienda']).value;
	url = url + "&thClassName=" + f.thClassName.value;
	url = url + "&IdMagazzino="
			+ eval("document.forms[0]." + idFromName['IdMagazzinoPart']).value;
	url = url + "&Tipo="
			+ eval("document.forms[0]." + idFromName['Tipo']).value;

	if (url != "") {
		var fError = document.getElementById('errorsFrameName');// document.getElementById('errorsFrameName').contentWindow
		// ;
		// //eval("document."
		// +
		// errorsFrameName);
		// 12091

		setLocationOnWindow(fError.contentWindow, url);

	}
}

function nomeServletRecTipo() {

	return "/it.thera.thip.tessile.vendite.ordineVE.web.VtOrdVenRigaPrmMontSmontImpostaTipo";
}
