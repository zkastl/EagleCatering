function getCurrentUser() {
    	$.ajax({
    	    url: "/EagleEventPlanning/services/employees/current",
    	    type: 'GET',
    	    datatype: 'json',
    	    statusCode: {
    	    200: function( planner ) {
    	    	$('#menuBar').html('<td><a href="MainPage.html">Home</a></td>' +
									'<td><a href="contactinfo.html">Contact</a></td>' +
									'<td><a href="events.html">Events</a></td>' +
									'<td><a href="import.html">Import</a></td>' +
									'<td><a href="ClientManagement.html">Clients</a></td>' +
									'<td><a href="EmployeeManagement.html">Employees</a></td>' +
									'<td><a href="logout.html">Logout</a></td>' +
									'<td style="text-align: right; width: 100%"> Welcome, ' + planner.name + '</td>');
    	    },
    	    401:function( response ) {
    			$('#login-logout').html('<a href="Login.html">Login</a>');}
    	    },
    	    
    	    error: function(jqXHR, textStatus, errorThrown) {
      	        console.log("error " + textStatus);
      	        console.log("incoming Text " + jqXHR.responseText);},
    	    beforeSend: setHeader
    	  });
}
  
	function getCookie(cname) {
	    var name = cname + "=";
	    var decodedCookie = decodeURIComponent(document.cookie);
	    var ca = decodedCookie.split(';');
	    for(var i = 0; i <ca.length; i++) {
	        var c = ca[i];
	        while (c.charAt(0) == ' ') {
	            c = c.substring(1);
	        }
	        if (c.indexOf(name) == 0) {
	            return c.substring(name.length, c.length);
	        }
	    }
	    return "";
	}
	
	function setHeader(xhr) {
		  token = getCookie("token");
		  xhr.setRequestHeader('Authorization', 'Bearer '+token );		  
	}
$(document).ready(function() {
    getCurrentUser();
})