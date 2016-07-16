function signIn(token)
{
    document.cookie = 'logger_token' + "=" + token + "; path=/";
	window.location.assign("http://jaredeverett.ca/darmok/map.php")
}

function loginUser() {
	var username_input = $('#username_input').val(),
		password_input = $('#password_input').val();
		
		$('#username_login').val('');
		$('#password_login').val('');
	
	$.ajax({
      type: 'POST',
      url: 'http://jaredeverett.ca/darmok/web_api/login.php',
      data: { 
        'username': username_input, 
        'password': password_input
      },
      async: false,
      dataType: 'json',
      success: function (data) {
         
        var token = data['token'];
        if (token.length > 0) {
            signIn(token);
        } else {
            alert('Username and password combination not found.');
        }    
    
      }
    });
}

$(document).ready(function() {
    
	$('.login_btn').click(function(e) {
		loginUser();
		e.preventDefault();
	});
    
});

