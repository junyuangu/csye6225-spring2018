/**
 * Created by callouswander on 2018/1/22.
 */

function validate_form(thisform) {
    with( thisform ) {
        if ( validate_email( email,"Not a valid e-mail address!")==false ) {
            email.focus();
            return false
        }
        if( confirm_password( newpw,newpw2, "Passwords are not the same!" )==false ) {
            message.focus();
            return false
        }
        return true
    }
}

function validate_email(field, alerttxt) {
    with( field ) {
        apos=value.indexOf("@")
        dotpos=value.lastIndexOf(".")
        if (apos<1||dotpos-apos<2) {
            alert(alerttxt);
            return false
        }
        else {
            return true
        }
    }
}

function confirm_password(field1, field2, alerttxt) {
    with( field1 ) {
        if( value!=field2.value() ) {
            alert(alerttxt);
            return false
        }
        else {
            return true
        }
    }
}

function currTime()
{
    var today=new Date()
    var h=today.getHours()
    var m=today.getMinutes()
    var s=today.getSeconds()
    // When time is less than 10, add 0 to keep 2-digit
    m=checkTime(m)
    s=checkTime(s)
    document.getElementById('time').innerHTML=h+":"+m+":"+s
    t=setTimeout('currTime()',1000)
}
function checkTime(i)
{
    if (i<10)
    {i="0" + i}
    return i
}

function currDate()
{
    var now= new Date();
    var year=now.getFullYear();
    var month=now.getMonth();
    var date=now.getDate();
    document.getElementById("date").innerHTML=year+"-"+(month+1)+"-"+date+" ";
}
currDate();