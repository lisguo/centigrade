var $header = $("#header");
var $nav = $("nav");

var pathname = window.location.pathname;
console.log(pathname);

switch (pathname) {
    case "/": // home
        $nav.css("position", "fixed");
        break;
    case "/login":
    case "/register":
        break;
    default:
        $nav.css("position", "relative");
        $nav.css("background", "rgba(0,0,0, 0.75)");
        break;
}

$(window).scroll(function () {
    var scrolled = $(window).scrollTop();
    if (pathname === "/") {
        if (scrolled > ($header.height() - $nav.height())) {
            $nav.css("background", "rgba(0,0,0,0.75)");
        } else {
            $nav.css("background", "rgba(100, 100, 100, 0.4)");
        }
    }
});

$('.carousel').carousel();
$('.carousel').carousel('pause');

// Load Icon for Ajax Requests
$(document).ajaxStart(function() {
    $("#loading").show();
});

$(document).ajaxStop(function() {
    $("#loading").hide();
});

// scrolling function for clicking
$('a[href^="#"]').on('click', function(event) {
    var target = $(this.getAttribute('href'));
    //var difference = ($(window).outerWidth() < 900)? 30: 50;

    /*if (navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1 && $(this).attr('href') == '#about') {
        difference = 0;
    };*/

    if( target.length ) {
        event.preventDefault();
        $('html, body').stop().animate({
            scrollTop: target.offset().top
        }, 1000);
    }

});