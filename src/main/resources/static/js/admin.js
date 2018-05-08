$(document).ready(function(){
    $('#create-movie-form').submit(function(event) {
        title = $('#create-movie-title').val();
        year = parseInt($('#create-movie-year').val());
        rated = $('#create-movie-rated').val();
        released = $('#create-movie-released').val();
        runtime = $('#create-movie-runtime').val();
        genre = $('#create-movie-genre').val().join(", ");
        plot = $('#create-movie-plot').val();
        boxoffice = $('#create-movie-boxoffice').val();
        production = $('#create-movie-production').val();
        website = $('#create-movie-website').val();

        $.ajax({
            type : 'POST',
            url : '/create_movie',
            data : {
                title: title,
                year: year,
                rated: rated,
                released: released,
                runtime: runtime,
                genre: genre,
                plot: plot,
                boxoffice: boxoffice,
                production: production,
                website: website
            }
        }).done(function(data) {
                $('#notification').html(data);
            });
    });

    $('#create-movie-released').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd mmm yyyy'
    });

    $('#admin-applications-tab').click(function(){
        $.ajax({
            type : 'GET',
            url : '/get_critic_applications'
        }).done(function(data) {
            $('#admin-critic-applications').html(data);
        });
    });
});