$(document).ready(function(){
    var search = $('#search-terms').val();
    showMovieResults(search);
    showTVShowResults(search);
    showPeopleResults(search);
});

function showMovieResults(search){
    $.ajax({
        type : 'GET',
        url : '/search_movies',
        data: {
            search: search
        }
    }).done(function(data) {
        $('#movie-results-table').html(data);
    });
}

function showTVShowResults(search){
    $.ajax({
        type : 'GET',
        url : '/search_shows',
        data: {
            search: search
        }
    }).done(function(data) {
        $('#tv-results-table').html(data);
    });
}

function showPeopleResults(search){
    $.ajax({
        type : 'GET',
        url : '/search_people',
        data: {
            search: search
        }
    }).done(function(data) {
        $('#people-results-table').html(data);
    });
}