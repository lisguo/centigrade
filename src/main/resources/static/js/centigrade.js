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