/**
 * Arquivo de Javascript - PotiERP.
 */

jQuery.noConflict();

function highlightMessage(){
	var msgs = jQuery('#msgsErroModal');
	for(var x = 0; x < msgs.length; x++){
		if(msgs[x].style.display != 'none'){
			new Effect.Highlight(msgs[x].id, { duration: 3 , delay: 0, endcolor:'#F2F2F2'});
		}
	}
}

function windowclose(nomeForm, panel){
	var msgs = jQuery('.rich-message', '#'+nomeForm);
	for(var i = 0; i < msgs.length; i++){
		if(msgs[i].children[0].innerHTML != ''){
			return;
		}
	}
	if(!ajaxRequestContainsErrors()){
		Richfaces.hideModalPanel(panel);
		return;
	}else{
		return;
	}
	Richfaces.hideModalPanel(panel);
}

function ajaxRequestContainsErrors(){
	return document.getElementById("contemErro").value == "1" || document.getElementById("contemField").value == "1";
}

function applyModalPanelEffect(panelId, effectFunc, params, hide) {
	if (panelId && effectFunc) {  
		var modalPanel = $(panelId);
		if (modalPanel && modalPanel.component) {  
			var component = modalPanel.component;
			var div = component.getSizedElement();
			if (hide) {
				Element.hide(div);
			}
			effectFunc.call(this, Object.extend( {targetId : div.id}, params || {}));
		}
	}  
}

function showModalPanelWithEffect(panelId, showEffect, params) {  
	applyModalPanelEffect(panelId, showEffect, params, true);  
}

function hideModalPanelWithEffect(panelId, hideEffect, params) {  
	var _params = params;  
	_params['afterFinish'] = function(){Richfaces.hideModalPanel(panelId)};  
	applyModalPanelEffect(panelId, hideEffect, params, false);  
}

function limpaForm(nomeForm){
	jQuery('#'+nomeForm).clearForm();
	jQuery('option', '#'+nomeForm).each(function(){
	if(this.text == 'Selecione')
		this.selected = true;
	});
}

function jqCheckOnlyOne(checkbox, nomeDataTable)
{
	var dataTableComponent = document.getElementById(nomeDataTable);
	jQuery("input[@type=checkbox]", dataTableComponent).each(function(){
		if(this.id != checkbox.id)
			this.checked = false;
	});
}


function jqCheckAll(checkboxPai, nomeDataTable){
	var checked = checkboxPai.checked;
	var dataTableComponent = document.getElementById(nomeDataTable);
	jQuery("input[@type=checkbox]", dataTableComponent).each(function(){
		this.checked = checked;
	});
}

function somenteNumeros (num) {
	var er = /[^0-9]/;
	er.lastIndex = 0;
	var campo = num;
	if (er.test(campo.value)) {
	campo.value = "";
	}
}

function somenteNumerosPontosVirgulas (num) {
	var er = /[^0-9.,]/;
	er.lastIndex = 0;
	var campo = num;
	if (er.test(campo.value)) {
	campo.value = "";
	}
}

function interceptarEvento(e) {
	if (!e) var e = window.event;
	e.cancelBubble = true;
	if (e.stopPropagation) e.stopPropagation();
}

function setCaretToEnd (e) {
    var control = $((e.target ? e.target : e.srcElement).id);
    if (control.createTextRange) {
        var range = control.createTextRange();
        range.collapse(false);
        range.select();
    }
    else if (control.setSelectionRange) {
        control.focus();
        var length = control.value.length;
        control.setSelectionRange(length, length);
    }
    control.selectionStart = control.selectionEnd = control.value.length;
}