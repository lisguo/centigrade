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
        $nav.css("position", "-webkit-sticky");
        $nav.css("position", "sticky");
        $nav.css("background", "black");
        $nav.css("top", "0px");
        break;
}

$(window).scroll(function () {
    var scrolled = $(window).scrollTop();

    if (scrolled > ($header.height() - $nav.height()) && pathname === "/") {
        $nav.css("background", "rgba(0,0,0,0.75)");
    } else {
        $nav.css("background", "rgba(100, 100, 100, 0.4)");
    }
});

$('.carousel').carousel();
$('.carousel').carousel('pause');