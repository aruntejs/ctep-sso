function popup(mylink, windowname, type)
  {
    //if(type == "organization" || document.forms[0].organizationName.value == ''){
      if (! window.focus)return true;
      var href;
      if (typeof(mylink) == 'string')
         href=mylink;
      else
         href=mylink.href;
      window.open(href, windowname, 'width=790,height=500,resizable=yes,scrollbars=yes,status=yes');
      return false;
    /*}
    else{      
      alert("Cannot Update State/Province when the Organization is chosen from LOV. \n If the account request was initiated on your behalf, please use Institution LOV to select an organization and address.");    
    }*/
    
//}else{ QC
//      alert("Cannot Update State/Province")
//    } 
  }

function showHelpText(mylink,windowname)
{
  if (! window.focus)return true;
  var href;
  if (typeof(mylink) == 'string')
     href=mylink;
  else
     href=mylink.href;
  window.open(href, windowname, 'width=700,height=500,resizable=yes,scrollbars=yes,status=yes,left=50 , top=50');
  return false;      
}
    
function changeMenuStyle(obj, new_style) { 
  obj.className = new_style; 
}

function showCursor(){
	document.body.style.cursor='hand'
}

function hideCursor(){
	document.body.style.cursor='default'
}

function confirmDelete(){
  if (confirm('Are you sure you want to delete?')){
    return true;
    }else{
    return false;
  }
}

function toggleLayer(whichLayer)
{
      if (document.getElementById)
      {
            // this is the way the standards work            
            var style2 = (document.getElementById(whichLayer).className == "hideDiv") ? "showDiv" : "hideDiv" ;
            document.getElementById(whichLayer).className =style2;
            document.getElementById(whichLayer+"_").src = (style2=="hideDiv") ? "UserInterface/graphics/plus.gif" : "UserInterface/graphics/minus.gif";
            document.getElementById(whichLayer+"_").alt = (style2=="hideDiv") ? "Expand" : "Collapse";
      }
      else if (document.all)
      {
            // this is the way old msie versions work
            var style2 = (document.all[whichLayer].className == "hideDiv") ? "showDiv" : "hideDiv" ;
            document.all[whichLayer].className =style2;
            document.all[whichLayer+"_"].src = (style2=="hideDiv") ? "UserInterface/graphics/plus.gif" : "UserInterface/graphics/minus.gif";
            document.all[whichLayer+"_"].alt = (style2=="hideDiv") ? "Expand" : "Collapse";
      }
      else if (document.layers)
      {
            // this is the way nn4 works
            var style2 = (document.layers[whichLayer].className == "hideDiv") ? "showDiv" : "hideDiv" ;
            document.layers[whichLayer].className =style2;
            document.layers[whichLayer+"_"].src = (style2=="hideDiv") ? "UserInterface/graphics/plus.gif" : "UserInterface/graphics/minus.gif";
            document.layers[whichLayer+"_"].alt = (style2=="hideDiv") ? "Expand" : "Collapse";
      }
}

function deleteConfirm(formno){
  if(confirm("This Adverse Event record will be deleted. Click \"OK\" to continue.")){
       eval("document.form"+formno+".submit()");
  }
}

function showProfiles(pageSrc){      
   var oPopup = window.createPopup();
   var oPopupBody = oPopup.document.body;
   //oPopupBody.style.backgroundColor = "black";
   oPopupBody.style.border = "solid 3 #E0E0E0";
   oPopupBody.style.fontFamily="system";
   oPopupBody.style.fontSize="0.8em";
   oPopupBody.style.padding="10px";
   oPopupBody.innerHTML = document.getElementById(pageSrc).innerHTML;
     // The following popup object is used only to detect what height the 
    // displayed popup object should be using the scrollHeight property. 
    // This is important because the size of the popup object varies 
    // depending on the length of the definition text. This first 
    // popup object is not seen by the user.
   oPopup.show(0, 0, 300, 0);
   var realHeight = oPopupBody.scrollHeight;
   var realWidth = oPopupBody.scrollWidth;
   // Hides the dimension detector popup object.
   oPopup.hide();
   // Shows the actual popup object with correct height.
   //oPopup.show(250,450,realWidth, realHeight, document.body);
   oPopup.show(0,400,realWidth, realHeight, document.body);
   //oPopupBody.show(250,450,250,100, document.body);
   //location.href=url;
}

function getUserName(){
    if ((document.getElementById("firstName").value != '') && (document.getElementById("lastName").value != '')){
        var sublastName = document.getElementById("firstName").value.substring(0,1);
        var s1 = document.getElementById("lastName").value.substring(0,29)+sublastName;
        var s  = s1.toUpperCase();  

        //document.iForm.lovWinHandle_check.value='2';
        var r = /&/ig;
        var n = "";
        if (s.indexOf('&') != -1)
        {
         s = s.replace(r,n);
        }
        r = /%/ig;
        n = "";
        if (s.indexOf('%') != -1)
        {
          s = s.replace(r,n);
        }
//        r = /'/ig;
//        n = "";
//        if (s.indexOf("'") != -1)
//        {
//          s = s.replace(r,n);
//        }
        r = /`/ig;
        n = "";
        if (s.indexOf("`") != -1)
        {
          s = s.replace(r,n);
        }
        r = /-/ig;
        n = "";
        if (s.indexOf("-") != -1)
        {
          s = s.replace(r,n);
        }
        
        document.getElementById("accountName").value=stringFilterStr(s);        
        return false;
    }
       
}

function getAlternateUserName(){
  if ((document.getElementById("firstName").value == '') && (document.getElementById("lastName").value == '')){
    alert("Please enter an First Name and Last Name");
  }else{    
    var digits = '0123456789';
    if(document.getElementById("accountName").value == ''){
      getUserName();
    }else{
      var s = stringFilterStr(document.getElementById("accountName").value);
      if(digits.indexOf(s.charAt(0)) >= 0){
        alert("Account Name cannot start with a Number");  
      }else{
        var mywindow = window.open('validateUsername.do?method=validateUsername&ACCOUNT_NAME='+s,"myvalidate", "top=0,left=0,height=300,,width=400");     
      }
    }        
  }
} 

/*
Submit Once form validation- 
© Dynamic Drive (www.dynamicdrive.com)
For full source code, usage terms, and 100's more DHTML scripts, visit http://dynamicdrive.com
*/
function submitonce(theform){
  //if IE 4+ or NS 6+
  if (document.all||document.getElementById){
  //screen thru every element in the form, and hunt down "submit" and "reset"
  for (i=0;i<theform.length;i++){
    var tempobj=theform.elements[i]
    if(tempobj.type.toLowerCase()=="submit"||tempobj.type.toLowerCase()=="reset")
      //disable em
      tempobj.disabled=true;
    }
  }
}

/*
  This function is used to clear all institution information if the user
  keys in a new Institution name
*/
function clearInstitutionDetails(){
      if(document.forms[0].organizationName.value != ''){
        document.forms[0].organizationName.value='';      
        document.forms[0].internalOffice.value='';
        document.forms[0].street1.value='';
        document.forms[0].street2.value='';
        document.forms[0].city.value='';
        document.forms[0].stateProvince.value='';
        document.forms[0].zipPostalCode.value='';
        document.forms[0].countryName.value='';      
        document.forms[0].organizationId.value='';      
        document.forms[0].countryId.value='';
        
                  //if other institution is not null
          if(document.forms[0].otherInstitution != null){
              //to disable the approve button when institution is selected            
               var i=0;                
               var k = document.forms[0].choice;
               while (k!= null && i >= 0) {
                  var j = document.forms[0].choice[i]
                    if (j != null) {
                      if(document.forms[0].choice[i].value == "Approved") {
                              if(document.forms[0].choice[i].disabled==false) {
                                  document.forms[0].choice[i].disabled=true;
                                }
                        break;

                      }
                      i++;
                    } else {
                        i = -1;
                    }
                }
              document.forms[0].stateProvince.readOnly=false;
          }
       
        //changeToReadOnly();
     }
}

/*
  This function is used to clear all institution information if the user
  keys in a new Institution name
*/
function changeToReadOnly(){
      var value=false;      
      /*
      if(document.forms[0].organizationName.value !=''){      
        value=true;
      }*/
      document.forms[0].city.readOnly=value;
//      document.forms[0].stateProvince.readOnly=value;
      document.forms[0].countryName.readOnly=value;  
}

function stringFilter(input) {
    s = input.value;
    filteredValues = " `~!@%^&$#*()+=/*+.[{]}_-|\;:',<>?/";     //List of Invalid Character
    var i;
    var returnString = "";
    
    for (i = 0; i < s.length; i++) {  
      var c = s.charAt(i);
      if (filteredValues.indexOf(c) == -1) 
        returnString += c;
    }
  
    input.value = returnString;
    s1 = input.value;
    filteredValues1 = '"';     //List of Invalid Character
    var i1;
    var returnString1 = "";
    for (i1 = 0; i1 < s1.length; i1++) {  
      var c1 = s1.charAt(i1);
      if (filteredValues1.indexOf(c1) == -1) 
      returnString1 += c1;
    }
    input.value = returnString1;
    if(returnString1 != ''){
      if(returnString1.length < 3){
        alert("Username has to be a Minimum of 3 characters");        
      }else if(returnString1.length > 30){
        alert("Username has to be a Maximum of 30 characters");
      }else{
        getAlternateUserName();
      }
    }
}

function stringFilterStr(inputStr) {
    s = inputStr;    
    filteredValues = " `~!@%^&$#*()+=/*+.[{]}_-|\;:',<>?/";     //List of Invalid Character
    var i;
    var returnString = "";
    
    for (i = 0; i < s.length; i++) {  
      var c = s.charAt(i);
      if (filteredValues.indexOf(c) == -1) 
        returnString += c;
    }    
    return returnString;
}

  function changeCase(frmObj){
    //Personal
     document.getElementById("firstName").value = convertToInitCap(document.getElementById("firstName").value);
     document.getElementById("middleName").value = convertToInitCap(document.getElementById("middleName").value);
     document.getElementById("lastName").value = convertToInitCap(document.getElementById("lastName").value);
    
    //Address
    document.getElementById("internalOffice").value = convertToInitCap(document.getElementById("internalOffice").value);
    document.getElementById("street1").value = convertToInitCap(document.getElementById("street1").value);
    document.getElementById("street2").value = convertToInitCap(document.getElementById("street2").value);
    document.getElementById("city").value = convertToInitCap(document.getElementById("city").value);
    
    if((document.getElementById("countryName").value).toUpperCase() != 'USA')
      document.getElementById("stateProvince").value = convertToInitCap(document.getElementById("stateProvince").value);
  }

  function convertToInitCap(srcString){
    var i;
    var tmpStr;
    var initChar;
    var initCapString="";
    var postString;
    var delimiter=" ";

    tmpStr = srcString.split(delimiter);
    for (i = 0; i < tmpStr.length; i++)  {
      initChar = tmpStr[i].substring(0,1).toUpperCase();
      postString = initChar + tmpStr[i].substring(1,tmpStr[i].length).toLowerCase();
      initCapString += postString;
      if(i != (tmpStr.length-1)){
        initCapString += delimiter;
      }
    }    
    return initCapString;
  }
      

 function resetSelection(){
    if(document.forms[0].applicationProfile != null){
      if(document.forms[0].applicationProfile.length > 0)
         {
               for (i=0;i<document.forms[0].applicationProfile.length;++ i)
                {
                    if (document.forms[0].applicationProfile[i].checked)
                    {
                        document.forms[0].applicationProfile[i].checked = false;
                    }
               }
         }
         else if(document.forms[0].applicationProfile.checked){
          document.forms[0].applicationProfile.checked = false;
      }
    }
 }
  
  /*
    This functio is used to check if the todate is earlier to from date
    on request query screens.
  */
  function checkDate(fromD, toD){
    if(fromD != "" && isDate(fromD) == false){
      return false;
    }else if(toD != "" && isDate(toD) == false){
      return false;
    }
    
    fromDate = new Date(fromD);
    toDate = new Date(toD);
    if((toDate - fromDate) < 0){
      alert("\'To Date\' cannot be earlier than \'From Date\'");
      return false;
    }else{
      return true;
    }
  }  


/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strMonth=dtStr.substring(0,pos1)
	var strDay=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("Format accepted is MM/DD/YYYY")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
}

 function ValidateForm(){
	var dt=document.frmSample.txtDate
	if (isDate(dt.value)==false){
		dt.focus()
		return false
	}
    return true
 }
 
 // modifications by JHORA on 05/15/08
  var previousRole = "";

var Browser = navigator.appName
	var Net = Browser.indexOf("Netscape")
	var Micro = Browser.indexOf("Microsoft")
	var Netscape = false
	var IE = false

	if(Net >= 0) {
		Netscape = true
    window.captureEvents(Event.MOUSEDOWN)
    window.onmousedown =   closeDiv
	}

	if(Micro >= 0) {
		IE = true
    document.onclick=closeDiv
  }
 	
var date1 , date2;

function showProfiles1(source, id, data) 
  {
    date1= new Date();
	  if(previousRole == "" || previousRole != id) 
	  {
     	document.getElementById(source).innerHTML= document.getElementById(data).innerHTML;
		 	previousRole = id ; 
      isDivVisible = true;
      var height ;
      height = window.frameElement.offsetHeight ;
      if(data.indexOf("appProfilePending") == -1) 
      {
        height = height + 500;
      }
      document.getElementById(source).style.top = height - 200;
      document.getElementById(source).style.display="block";
    }
	  else 
	  {
			document.getElementById(source).style.display="none";
			previousRole = "" ;  
    }
  }
  
   function closeDiv(e) 
   {
        date2 = new Date(); 
        if(date1 != undefined) 
       {
           if((date2.getTime() - date1.getTime()) > 100) 
           {   
             if(document.getElementById(div).style.display="block"  ) 
             {
               document.getElementById(div).style.display="none";
                previousRole="";
             }
          }
      }
  }
 // modifications ends 
 
  function showProfiles2(divToShow)
  {
     date1 = new Date(); 
     div = divToShow;
    document.getElementById(divToShow).style.display="block";
  }
  
   // changes related to javascript for date format
 var p1;
 
 function getIt(e)
  {        
    var keyId = window.event ? event.keyCode : e.keyCode ; 
    var targetSrc = window.event ? event.srcElement : e.target ; 
    if(targetSrc.id == "personPhone" || targetSrc.id == "personFax") 
    {      
      p1 = targetSrc;
      if(keyId != 37 && keyId != 39) 
      {
        var country = "";
        if(document.getElementById("countryName") != null){        
          country = document.getElementById("countryName").value.toUpperCase();      
        }
        
        if(country != null && (country == 'USA' || country == 'CANADA')){           
          formatPhoneForDeletion(targetSrc);
        }else if(country == ""){
          targetSrc.value="";
          alert("Please complete Organization and Address");
          return false;
        }
      }	
    }
    
}
 
 
 function formatPhoneForDeletion(targetSrc)
{
		//var val = document.frmPhone.txtphone.value;
		var val = targetSrc.value;
		var tmp ="";
		for(var i=0;i<val.length;i++)
		{
			if(isDigit(val.charAt(i)))
			{
				tmp = tmp + val.charAt(i);
			}
		}
		reformatPhoneAfterDeletion(tmp,targetSrc);
		//document.getElementById('txtphone').focus();
		targetSrc.focus();
}

function reformatPhoneAfterDeletion(tmp,targetSrc)
{
	var formatNo ="" ;
	for(i = 0;i<tmp.length;i++) 
	{
		if(i==3) 
		{
			formatNo = "("+ formatNo + ") ";
			formatNo = formatNo + tmp.charAt(i);
		}
		else if(i == 6 ) 
		{
			formatNo =  formatNo +"-" + tmp.charAt(i);
		}else {
			formatNo = formatNo + tmp.charAt(i);
		}
	}
	//document.frmPhone.txtphone.value = formatNo;
	//document.getElementById('txtphone').value = formatNo;
	targetSrc.value = formatNo;
}

function isDigit(num) 
{
		if (num.length>1)
		{	
			return false;
		}
		var string="1234567890";
		if (string.indexOf(num)!=-1)
		{
			return true;
		}
		return false;
}

  document.onkeyup = getIt;  // added for key press event handling
  
/*
  This function will be attached to countryName of 
  request New account, QC Screens.
*/
function resetPhoneFormat(){
  var phoneField = document.getElementById("personPhone");
  var faxField = document.getElementById("personFax");
  
  var country = document.getElementById("countryName").value.toUpperCase();
  if(country != null && (country == 'USA' || country == 'CANADA')){ 
    formatPhoneForDeletion(phoneField);
    formatPhoneForDeletion(faxField);
  }else if(country != null && (country != 'USA' || country != 'CANADA')){
    reFormatPhoneFaxValue(phoneField);  
    reFormatPhoneFaxValue(faxField); 
  }
}

function clearCountryId(){  
    document.forms[0].countryId.value='';    
    resetPhoneFormat();
}

function reFormatPhoneFaxValue(ele){
	var phoneVal = ele.value;
	var temp = phoneVal.replace("(","");
	temp=temp.replace(") ","");	
	temp=temp.replace("-","");
	temp=temp.replace("-","");
	ele.value=temp;
}
