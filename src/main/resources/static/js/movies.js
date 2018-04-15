var allMovies;
var numResults = 10;
$(document).ready(function() {
    $.ajax({
        type: 'GET',
        url: '/api_movies',
        encode: true
    })

        .done(function (data) {
            allMovies = data;
            createPagination();
        });

    // Pagination event handler
    $('.movie-search-page-link').click(function (event) {
        var page = event.target.innerHTML;

        $.ajax({
            type: 'GET',
            url: '/search_items?search=' + search + "&page=" + page,
            encode: true
        })

            .done(function (data) {
                showResult(data)
            });
    });
});

function createPagination(){
    $('.pagination').empty();

    var numPages = data.length / numResults;
    for(var i = 0; i < numPages; i++){
        $('.pagination').append('<li class=\"page-item\">' +
            '<a class=\"page-link movie-search-page-link\">' + i + '</a>' +
            '</li>');
    }
}

function showResult(data, page){
    $('#search-results').empty();

    var start = page * numResults;
    var end = start + numResults;
    for(var i = start; i < data.length && i < end; i++){
        $('#search-results').append("<tr><td>" + data[i].title + "</td></tr>");
    }
}