function initial(){
    setTimeout("countdown()", 1000);
}
function countdown(){
    var second = parseInt($("#second").text());
    if (second > 0){
        $("#second").text(second - 1);
        setTimeout("countdown()", 1000);
    } else {
        window.location.replace("/");
    }
}
onload = initial;