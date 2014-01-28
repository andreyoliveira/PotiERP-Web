dpb={
	// parameters to change 
	navId:'nav',
	currentClass:'open',
	panelClass:'panel',
	triggerElements:'h6',

	init:function(){
		var tl,hs,i;
		if(!document.getElementById || !document.createTextNode){return;}
		dpb.ul=document.getElementById(dpb.navId);
		if(!dpb.ul){return false;}
		dpb.cssjs('add',dpb.ul,dpb.panelClass);
		hs=dpb.ul.getElementsByTagName(dpb.triggerElements);
		for (i=0;i<hs.length;i++){
			tl=document.createElement('a');
			tl.setAttribute('href','#');
			tl.appendChild(document.createTextNode(hs[i].firstChild.nodeValue));
			hs[i].replaceChild(tl,hs[i].firstChild);
			dpb.addEvent(tl,'click',dpb.expand,false);
			tl.onclick=function(){return false;} // safari fix
 			if(dpb.cssjs('check',hs[i],dpb.currentClass)){
				hs[i].parentNode.getElementsByTagName('ul')[0].style.display='block';
				dpb.curSec=hs[i].parentNode.getElementsByTagName('ul')[0];
				dpb.curHead=hs[i];
			} else {
				hs[i].parentNode.getElementsByTagName('ul')[0].style.display='none';
			}
		}
	},
	expand:function(e){
		var t,tcl
		t=dpb.getTarget(e);
		tc=t.parentNode.parentNode.getElementsByTagName('ul')[0];
		if(tc.style.display!='block'){
			if(dpb.curSec){
				dpb.curSec.style.display='none';
				dpb.cssjs('remove',dpb.curHead,dpb.currentClass)
			}
			tc.style.display='block';
			dpb.cssjs('add',t.parentNode,dpb.currentClass)
		} else {
			tc.style.display='none';
			dpb.cssjs('remove',t.parentNode,dpb.currentClass)
		}
		dpb.curSec=tc;
		dpb.curHead=t.parentNode;
		dpb.cancelClick(e);
	},
/* helper methods */
	getTarget:function(e){
		var target = window.event ? window.event.srcElement : e ? e.target : null;
		if (!target){return false;}
		if (target.nodeName.toLowerCase() != 'a' && 
		target.nodeName.toLowerCase() != dpb.triggerElements){
			target = target.parentNode;
		}
		return target;
	},
	cancelClick:function(e){
		if (window.event){
			window.event.cancelBubble = true;
			window.event.returnValue = false;
			return;
		}
		if (e){
			e.stopPropagation();
			e.preventDefault();
		}
	},
	addEvent: function(elm, evType, fn, useCapture){
		if (elm.addEventListener) 
		{
			elm.addEventListener(evType, fn, useCapture);
			return true;
		} else if (elm.attachEvent) {
			var r = elm.attachEvent('on' + evType, fn);
			return r;
		} else {
			elm['on' + evType] = fn;
		}
	},
	cssjs:function(a,o,c1,c2){
		switch (a){
			case 'swap':
				o.className=!dpb.cssjs('check',o,c1)?o.className.replace(c2,c1):o.className.replace(c1,c2);
			break;
			case 'add':
				if(!dpb.cssjs('check',o,c1)){o.className+=o.className?' '+c1:c1;}
			break;
			case 'remove':
				var rep=o.className.match(' '+c1)?' '+c1:c1;
				o.className=o.className.replace(rep,'');
			break;
			case 'check':
				return new RegExp("(^|\s)" + c1 + "(\s|$)").test(o.className)
			break;
		}
	}
}
dpb.addEvent(window, 'load', dpb.init, false);
