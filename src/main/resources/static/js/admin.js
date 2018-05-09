$(document).ready(function(){
    $('#create-movie-released').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd mmm yyyy'
    });

    $('#create-movie-form').submit(function(event) {
        submitMovieForm();
    });

    $('#admin-reported-users-tab').click(function(){
        showAllReportedUsers();
    });

    $('#admin-reported-reviews-tab').click(function(){
        showAllReportedReviews();
    });

    $('#admin-applications-tab').click(function(){
        showAllApplications();
    });
});
/* CREATE CONTENT */
function submitMovieForm(){
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
}

/* REPORTED USERS */
function showAllReportedUsers(){
    $.ajax({
        type : 'GET',
        url : '/get_reported_users'
    }).done(function(data) {
        $('#admin-reported-users-content').html(data);
    });
}
function allowReportedUser(id){
    $.ajax({
        type : 'POST',
        url : '/remove_reported_user',
        data: {
            id: id
        }
    }).done(function(data) {
        $('#notification').html(data);
        showAllReportedUsers();
    });
}
function deleteReportedUser(id){
    $.ajax({
        type: 'POST',
        url: '/delete_user',
        data: {
            id: id
        }
    }).done(function(data) {
        showAllReportedUsers();
    });
}

/* REPORTED REVIEWS */
function showAllReportedReviews(){
    $.ajax({
        type : 'GET',
        url : '/get_reported_reviews'
    }).done(function(data) {
        $('#admin-reported-reviews-content').html(data);
    });
}
function allowReportedReview(id){
    $.ajax({
        type : 'POST',
        url : '/remove_reported_review',
        data: {
            id: id
        }
    }).done(function(data) {
        $('#notification').html(data);
        showAllReportedReviews();
    });
}
function deleteReportedReview(id){
    $.ajax({
        type: 'POST',
        url: '/delete_review',
        data: {
            id: id,
            fromAdmin: true
        }
    }).done(function(data) {
        showAllReportedReviews();
    });
}

/* CRITIC APPLICATIONS */
function showAllApplications(){
    $.ajax({
        type : 'GET',
        url : '/get_critic_applications'
    }).done(function(data) {
        $('#admin-critic-applications-content').html(data);
    });
}
function showAppDetail(id){
    $.ajax({
        type : 'GET',
        url : '/critic_application',
        data: {
            id: id
        }
    }).done(function(data) {
        $('#admin-critic-applications-content').html(data);
    });
}
function acceptApplication(id){
    $.ajax({
        type : 'POST',
        url : '/accept_critic_application',
        data: {
            id: id
        }
    }).done(function(data) {
        $('#notification').html(data);
        showAllApplications();
    });
}
function rejectApplication(id){
    $.ajax({
        type : 'POST',
        url : '/reject_critic_application',
        data: {
            id: id
        }
    }).done(function(data) {
        $('#notification').html(data);
        showAllApplications();
    });
}