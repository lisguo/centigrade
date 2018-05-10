function deleteMovie(movieId){
    $.ajax({
        type: 'POST',
        url: '/delete_movie',
        data: {
            movieId: movieId
        }
    }).done(function(data) {
        $('.content-row-block').html(data);
    });
}