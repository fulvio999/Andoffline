 
  //------------------- Utility scripts used in the application --------------

  //  Check if a SMS item is selected and set the value of a hidden field before submit the form
  function getSmsSelected() 
  {			 
	 // get the id of the selected item
	 var selItem = $('input[name=smsId]:checked', '#detailSmsForm').val();	 
	 $('#selectedSms').val(selItem); 
	 
	 /*
	 if(!selItem || 0 === selItem.length){	
	 	alert('Please, select an Item !');	 	
	 }else{
		 $('#selectedSms').val(selItem); 
	 }	
	 */		 
  }
  
  //Check if a JOB item is selected and set the value of a hidden field before submit the form
  function getJobSelected() 
  { 
	  // get the id of the selected item
	  var selItem = $('input[name=jobId]:checked', '#detailJobForm').val();	 
	  $('#selectedJob').val(selItem); 
	
	 /*
	 if(!selItem || 0 === selItem.length){	
	 	alert('Please, select an Item !');	 	
	 }else{
		 $('#selectedSms').val(selItem); 
	 }	
	 */		 
  }
 
  //hide/ show the block with the 'id' in argument
  function hideDetailsBlock(obj) 
  {	
	 try{
		var el = document.getElementById(obj);		
		el.style.display = 'none'; //hide
	 }
	 catch(e){	
		//no editing block is shown currently
	 }	
   }
 
 