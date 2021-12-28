
function VtOrdVenRigaPrmRidMagOL() {
	window.resizeTo(800, 400);
	window.moveTo(300, 100);  
}

function okButtonSdoppio()
{
	var qta=document.getElementById('QtaTrasferire').value;
	var magDest = document.getElementById('IdMagazzinoDest').value;
	
	if(  magDest != ""  && qta != "" && qta != "0,00")
	{
		var className = eval("document.forms[0].thClassName.value");
		runActionDirect('SDOPPIORIGA','action_submit', className ,null,'same','no');
	}
	else
		alert("Attenzione: 'Magazzino Destinazione' e 'Quantita' sono campi obbligatori!");
		
}

function addRowMatrice(tableID, valuesRow, colorName)
{
	
	  var tableRef = document.getElementById(tableID);

	  var newRow = tableRef.insertRow(-1);
	  
	  for(var i = 0; i < valuesRow.length; i++){
		 
		  // Insert a cell in the row at index 0
		  var newCell = newRow.insertCell(i);
		  newCell.className =colorName[i];
		  // Append a text node to the cell
		  var newText = document.createTextNode(valuesRow[i]);
		  newCell.appendChild(newText);

		}
	
	
}

function okButtonTrasferimento()
{
	var qta=document.getElementById('QtaTrasferire').value;
	var magPart = document.getElementById('IdMagazzinoPart').value;
	var magDest = document.getElementById('IdMagazzinoDest').value;
	
	if(  magPart != "" && magDest != ""  && qta != "" && qta != "0,00")
	{
		var className = eval("document.forms[0].thClassName.value");
		runActionDirect('TRASFMAG','action_submit', className ,null,'same','no');
	}
	else
		alert("Attenzione: 'Magazzino Partenza' 'Magazzino Destinazione' e 'Quantita' sono campi obbligatori!");
		
}

function okButtonOnlyTrasferimento()
{
	var qta=document.getElementById('QtaTrasferire').value;
	var magPart = document.getElementById('IdMagazzinoPart').value;
	var magDest = document.getElementById('IdMagazzinoDest').value;
	
	if(  magPart != "" && magDest != ""  && qta != "" && qta != "0,00")
	{
		var className = eval("document.forms[0].thClassName.value");
		runActionDirect('ONLYTRASFMAG','action_submit', className ,null,'same','no');
	}
	else
		alert("Attenzione: 'Magazzino Partenza' 'Magazzino Destinazione' e 'Quantita' sono campi obbligatori!");
		
}


