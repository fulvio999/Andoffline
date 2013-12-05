<%@ taglib prefix="s" uri="/struts-tags"%>


<!-- show an error message set in the action -->
<div style="border-style: solid; background-color:#FA0723; text-align:center">

     <s:label value="%{errorMessage}" />
	
</div>

  <!-- Add a "back" button to go to the page where the user can change the content (currently only jobs) to display  -->
  
      <div style="text-align:left;font-size:110%;vertical-align:text-middle;">             
	       <a href="loginAction.action">  <img alt="Change content" src="images/left-arrow.png" height="30" width="30"> Back to Home </a> 
	  </div>      
  
