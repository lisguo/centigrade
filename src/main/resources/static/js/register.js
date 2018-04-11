console.log("hello");

function validateForm(form) {
    var formName = document.forms[form]["displayname"].value;
    console.log(form);
    if (form == "signupform") {
        var alert = document.getElementsByClassName("alert");
        console.log(alert[0]);
        alert[0].style.display = "block";
    }
}