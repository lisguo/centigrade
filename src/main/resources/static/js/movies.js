$(document).ready(function(){
    $('#login-form').submit(function(event) {
        var formData = {
            'username' : $('input[name=login-username]').val(),
            'password' : $('input[name=login-password]').val()
        };

        $.ajax({
            type : 'POST',
            url : '/login',
            data : JSON.stringify(formData),
            dataType : 'json',
            contentType: "application/json; charset=utf-8",
            encode : true
        })

            .done(function(data) {
                if(data['status'] == 'OK'){
                    $('#login-card').fadeOut();
                    location.reload();
                }
                else{
                    $('.alert').show();
                    $('.alert').html(data['error']);
                }
            });

        event.preventDefault();
    });
});