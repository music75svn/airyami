function initNavigation(seq) {
	var nav = document.getElementById("topMenuList");
	nav.menu = new Array();
	nav.current = null;
	nav.menuseq = 0;
	navLen = nav.childNodes.length;
	
	allA = nav.getElementsByTagName("a");
	for(k = 0; k < allA.length; k++) {

		allA.item(k).onmouseover = allA.item(k).onfocus = function () {
			nav.isOver = true;
		}
		allA.item(k).onmouseout = allA.item(k).onblur = function () {
			nav.isOver = false;
			//alert(event.button);
			setTimeout(function () {
				if (nav.isOver == false) {
					if (nav.menu[seq]) {
						nav.menu[seq].onmouseover();	
					} else if(nav.current) {			
						if( seq != 0)
						this.current.parentNode.className = this.current.parentNode.className.replace(" over","");						
						if (nav.current.submenu)						
							nav.current.submenu.style.display = "none";
						nav.current = null;					
					}
				}
			}, 3000);
		}
	}

	for (i = 0; i < navLen; i++) {
		navItem = nav.childNodes.item(i);
		if (navItem.tagName != "LI")
			continue;

		navAnchor = navItem.getElementsByTagName("a").item(0);
		navAnchor.submenu = navItem.getElementsByTagName("ul").item(0);
		
		navAnchor.onmouseover = navAnchor.onfocus = function () {
			if (nav.current) {		
				if( seq !=0)
				nav.current.parentNode.className = nav.current.parentNode.className.replace(" over","");			
				if (nav.current.submenu)				
					nav.current.submenu.style.display = "none";
				nav.current = null;
			}
			
			if (nav.current != this) {
				if( seq !=0)
					this.submenu.parentNode.className +=" over";			
				if (this.submenu)		{						
					this.submenu.style.display = "block";
				}
				nav.current = this;
			}
			nav.isOver = true;
		}
		nav.menuseq++;
		nav.menu[nav.menuseq] = navAnchor;
	}
	if (nav.menu[seq])
		nav.menu[seq].onmouseover();

	
    
    
    /*
    alert("subMenuUlLeft:" + subMenuUlLeft +" \n"
    	+ "subMenuUlRight:" + subMenuUlRight +" \n"
    	+ "subMenuUlWidth:" + subMenuUlWidth +" \n"
    	+ "topMenuUlLeft:" + topMenuUlLeft +" \n"
    	+ "topMenuUlRight:" + topMenuUlRight +" \n"
    	+ "topMenuUlWidth:" + topMenuUlWidth +" \n"
    	+ "topMenuUlCenter:" + topMenuUlCenter +" \n"
    	+ "leftMargin:" + leftMargin +" \n"
    );
    */
	
}
