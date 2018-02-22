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
    // 当数字是小于10的就要在前面加0.看起来规范
    m=checkTime(m)
    s=checkTime(s)
    document.getElementById('txt').innerHTML=h+":"+m+":"+s
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
    document.getElementById("info1").innerHTML=year+"-"+(month+1)+"-"+date+" ";
}
currDate();