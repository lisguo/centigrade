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
            showResult(data, 0);
        });
});

function createPagination(){
    $('.pagination').empty();

    var numPages = allMovies.length / numResults;
    for(var i = 0; i < numPages; i++){
        $('.pagination').append('<li class=\"page-item\">' +
            '<a class=\"page-link movie-search-page-link\">' + i + '</a>' +
            '</li>');
    }

    $('.movie-search-page-link').click(function (event) {
        var page = event.target.innerHTML;
        showResult(allMovies, page);
    });
}

function showResult(data, page){
    $('#search-results').empty();

    var start = page * numResults;
    var end = start + numResults;
    for(var i = start; i < data.length && i < end; i++){
        var movie = data[i];
        var title = '<td><a href="/movie?id=' + movie.id + '">' + movie.title + '</a></td>';
        $('#search-results').append('<tr>' + title + '</tr>');
    }
}