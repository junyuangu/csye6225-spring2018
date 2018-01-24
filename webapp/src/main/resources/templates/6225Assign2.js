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
