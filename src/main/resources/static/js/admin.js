$(document).ready(function(){
    initDatePickers();

    $('#create-movie-form').submit(function(event) {
        event.preventDefault();

        var formData = new FormData(this);

        $.ajax({
            url : '/create_movie',
            data : formData,
            type : 'POST',
            method: 'POST',
            cache: false,
            contentType: false,
            processData: false
        }).done(function(data) {
            $('#notification').html(data);
            document.getElementById('create-movie-form').reset();
        });
    });
    $('#create-show-form').submit(function(event) {
        event.preventDefault();

        var formData = new FormData(this);

        $.ajax({
            url : '/create_show',
            data : formData,
            type : 'POST',
            method: 'POST',
            cache: false,
            contentType: false,
            processData: false
        }).done(function(data) {
            $('#notification').html(data);
            document.getElementById('create-show-form').reset();
        });
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
            userId: id,
            deleteReport: true
        }
    }).done(function(data) {
        $('#notification').html(data);
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

function initDatePickers(){
    $('#create-movie-released').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd mmm yyyy'
    });
    $('#create-show-first-aired').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd mmm yyyy'
    });
}