var page = 1;

$(document).ready(function(){
    $('#sort-form').submit(function(event) {
        var sortBy = $('select[name=sortBy]').val();
        var sortDirection = $('select[name=sortDirection]').val();

        $.ajax({
            type : 'GET',
            url : '/movies_table?sortBy=' + sortBy + '&sortDirection=' + sortDirection,
            encode : true
        })

            .done(function(data) {
                console.log(data);
                $('#movie-results').html(data);
            });

        event.preventDefault();
    });

    $('#sort-form').submit();
});

function getPrev(){
    page--;

    $.ajax({
        type : 'GET',
        url : '/movies_table?page=' + page,
        encode : true
    })

        .done(function(data) {
            $('#movie-results').html(data);
        });
}

function getNext(){
    page++;

    $.ajax({
        type : 'GET',
        url : '/movies_table?page=' + page,
        encode : true
    })

        .done(function(data) {
            $('#movie-results').html(data);
        });
}