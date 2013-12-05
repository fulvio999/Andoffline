<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<html>

  <head>

	<sj:head/>
    
    <!-- styles for displaytag library -->
	<link href="css/displaytag/maven-theme.css" rel="stylesheet" type="text/css" media="all"/>
	<link href="css/displaytag/site.css" rel="stylesheet" type="text/css" media="all"/>
	<link href="css/displaytag/screen.css" rel="stylesheet" type="text/css" media="all"/>
	<link href="css/displaytag/print.css" rel="stylesheet" type="text/css" media="print" />	
	
	<link href="css/custom.css" rel="stylesheet" type="text/css" media="all"/>
	
	<script type="text/javascript" src="js/custom.js" ></script>
	
  </head>
 

  <body> 
 
		<table style="width: 100%">
		 
			  <tbody>
			  
			    <tr>
			        <td>             
			          	<tiles:insertAttribute name="header" />         
			        </td>      
			    </tr>
			    
			    <tr>
			        <td style="text-align:left;font-size:110%;vertical-align:text-middle;">             
			             <tiles:insertAttribute name="subheader" />   
			        </td>      
			    </tr>
			    
			    <tr>
			        <td style="text-align:center;font-size:150%">             
			          	<tiles:insertAttribute name="title" />       
			        </td>      
			    </tr>
			    
			    <tr>
			        <td style="height: 35em">
				    	<tiles:insertAttribute name="contentPage" />
				    </td>
			    </tr>
			    
			    <tr>
			        <td>
					  	<tiles:insertAttribute name="footer" />
				    </td>
			    </tr>
			    
			  </tbody>
		  
		</table>

    </body>


</html>