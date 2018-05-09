function deleteUser(id){
    $.ajax({
        type: 'POST',
        url: '/delete_user',
        data: {
            userId: id
        }
    }).done(function(data) {
        $('.content-row-block').html(data);
    });
}

function reportUser(id){
    var message = $('#report-reason-textarea').val();
    $.ajax({
        type: 'POST',
        url: '/report_user',
        data: {
            reportedId: id,
            message: message
        }
    }).done(function(data) {
        $('#notification').html(data);
    });
}