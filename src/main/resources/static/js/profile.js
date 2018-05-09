function deleteUser(id){
    $.ajax({
        type: 'POST',
        url: '/delete_user',
        data: {
            id: id
        }
    }).done(function(data) {
        $('.content-row-block').html(data);
    });
}

function reportUser(id){
    $.ajax({
        type: 'POST',
        url: '/report_user',
        data: {
            reportedId: id,
            message: "EXAMPLE MESSAGE"
        }
    }).done(function(data) {
        $('#notification').html(data);
    });
}