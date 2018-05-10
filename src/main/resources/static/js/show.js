function deleteShow(showId){
    $.ajax({
        type: 'POST',
        url: '/delete_show',
        data: {
            showId: showId
        }
    }).done(function(data) {
        $('.content-row-block').html(data);
    });
}