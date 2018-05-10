var page, sortBy, sortDirection;

$(document).ready(function(){
    $('#sort-form').submit(function(event) {
        page = 1;

        sortBy = $('select[name=sortBy]').val();
        sortDirection = $('select[name=sortDirection]').val();

        $.ajax({
            type : 'GET',
            url : '/shows_table?sortBy=' + sortBy + '&sortDirection=' + sortDirection,
            encode : true
        })

            .done(function(data) {
                console.log(data);
                $('#show-results').html(data);
            });

        event.preventDefault();
    });

    $('#sort-form').submit();
});

function getPrev(){
    page--;

    $.ajax({
        type : 'GET',
        url : '/shows_table?sortBy=' + sortBy + '&sortDirection=' + sortDirection + '&page=' + page,
        encode : true
    })

        .done(function(data) {
            $('#show-results').html(data);
        });
}

function getNext(){
    page++;

    $.ajax({
        type : 'GET',
        url : '/shows_table?sortBy=' + sortBy + '&sortDirection=' + sortDirection + '&page=' + page,
        encode : true
    })

        .done(function(data) {
            $('#show-results').html(data);
        });
}