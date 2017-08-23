var start = 0;
var page = 15;

	
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
	
function getPrevious()
{
	start = start-page;
	if (start <1)
	   start = 0;
	createEventTable(start);
}
	
function getNext()
{
	if ($("#events") && $("#events").prop('rows').length-1 == page)
	   start=start+page;
	createEventTable(start);
}

function selectChange() {
    id = getId();
    $("#editId").val(id);
    $("#deleteId").val(id); 
    $("#guestId").val(id);
}
    
function getId() {
    return jQuery( 'input[name=selectedRow]:checked' ).val();
}
    
function getCurrentUser() {
    $.ajax({
        url: "/EagleEventPlanning/services/employees/current",
    	type: 'GET',
    	datatype: 'json',
    	statusCode: {
    	   200: function( planner ) { 
    	       $('#userWelcome').html("Welcome, "+planner.name);
    	   },
    	   401:function( response ) {
    	       $('#eventPlanners').html("You must Login first");
    	   }
    },
    	    
        error: function(jqXHR, textStatus, errorThrown) {
            console.log("error " + textStatus);
      	    console.log("incoming Text " + jqXHR.responseText);
        },
        beforeSend: setHeader
    });   	
}
  
function createEventTable(start) {
    $.ajax({
    url: "/EagleEventPlanning/services/events?page="+start+"&per_page="+page,
    type: 'GET',
    datatype: 'json',
    statusCode: {
        200:function( response ) {
            var tableHTML = '<tr><th></th><th width= 50px>ID</th><th width= 100px>Name</th></tr>';
            $.each(response, function (i, event) {
    	       tableHTML += '<tr><td><input type="radio" onchange= "selectChange()" name="selectedRow" value='+event.eventId+'><td>' + event.eventId + '</td><td>' + event.name + '</td></tr>';
    	    });
            tableHTML+='</table>';
  	        $('#events').html(tableHTML);
 	    },
        401:function( response ) {
  	        $('#events').html("You must Login first");
  	    },
        403:function( response ) {
  	        $('#events').html("Access to the page forbidden");
  	    }
    },
  	error: function(jqXHR, textStatus, errorThrown) {
        console.log("error " + textStatus);
        console.log("incoming Text " + jqXHR.responseText);
    },
    beforeSend: setHeader       
    });
}
  	  
function setHeader(xhr) {
    token = getCookie("token");
	xhr.setRequestHeader('Authorization', 'Bearer '+token );		  
}

function getEventByEventId() {
    
}

$(document).ready(function() {
    $("#previous").click(function() {
        getPrevious()
    });
    $("#next").click(function() {
        getNext()
    });
    getCurrentUser();
    createEventTable(start);
});