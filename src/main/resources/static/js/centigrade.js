var $header = $("#header");
var $nav = $("nav");
console.log($header.height());

$(window).scroll(function () {
    var scrolled = $(window).scrollTop();

    if (scrolled > ($header.height() - $nav.height())) {
        $nav.css("background", "rgba(0,0,0,0.75)");
    } else {
        $nav.css("background", "rgba(100, 100, 100, 0.4)");
    }
});

$('.carousel').carousel();
$('.carousel').carousel('pause');