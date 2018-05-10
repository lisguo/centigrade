$(document).ready(function(){
    loadlatestCriticReviews();
    loadMostPopularMovies();
    loadOpeningSoon();
    loadTopBoxOffice();

    console.log($(document).height());
});

function loadTopBoxOffice(){
    $.ajax({
        type : 'GET',
        url : '/index_top_box_office'
    }).done(function(data) {
        $('#index-top-box-office').html(data);
    });
}

function loadOpeningSoon(){
    $.ajax({
        type : 'GET',
        url : '/index_opening_soon'
    }).done(function(data) {
        $('#index-opening-soon').html(data);
    });
}

function loadMostPopularMovies(){
    $.ajax({
        type : 'GET',
        url : '/index_most_popular_movies'
    }).done(function(data) {
        $('#index-most-popular-movies').html(data);
    });
}

function loadlatestCriticReviews(){
    $.ajax({
        type : 'GET',
        url : '/index_latest_critic_reviews'
    }).done(function(data) {
        $('#index-latest-critic-reviews').html(data);
    });
}