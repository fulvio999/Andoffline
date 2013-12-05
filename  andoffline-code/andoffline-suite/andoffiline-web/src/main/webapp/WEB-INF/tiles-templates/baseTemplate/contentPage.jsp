<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<br/>
<br/>
<br/>
<br/>
<br/>

<div align="center"> 

<table>
   
   <tr>
      <td>
   		Welcome to Andoffline Web Console <br/> <br/>
      </td> 
   </tr>

   <s:form id="formSms" action="getStoredContentAction" theme="simple" cssClass="yform">
      
        <tr>     
	       <td>   
	           <s:textfield cssStyle="width: 15em;" label=" Type" name="contentType" value="Job" readonly="true" required="true" />
	       </td> 
       </tr>
      
       <tr>     
	       <td>   
	   	   	   <s:select cssStyle="width: 15em;" label="Database Type" name="dbmsType" headerKey="-1" headerValue="Database Type" list="#{'Mysql':'Mysql', 'Sqlite':'Sqlite'}" required="true"/>
	       </td> 
       </tr>
       
       <tr>
           <td>      
		       <sj:submit formIds="formSms"	effect="pulsate" value="Load" indicator="indicator"	button="true" cssClass="customButton" />  
           </td> 
       </tr>  
         
   </s:form>   

</table> 
   
   <br/>
   
   <img id="indicator" src="images/loading.gif" alt="Loading..." style="display:none"/>



</div>

<br/>
<br/>
<br/>
<br/>
<br/>
<br/>