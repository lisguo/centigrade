$(document).ready(function(){
    $('#critic-application-form').submit(function(event) {
        var id = $('#critic-application-id').val();
        var sourceName = $('#critic-application-source-name').val();
        var sourceUrl = $('#critic-application-source-url').val();
        var resumeText = $('#critic-application-resume-text').val();

        $.ajax({
            type : 'POST',
            url : '/submit_critic_application',
            data : {
                id: id,
                sourceName: sourceName,
                sourceUrl: sourceUrl,
                resumeText: resumeText
            }
        }).done(function(data) {
            $('#notificationModal').html(data);
        });
        event.preventDefault();
    });
});