/** @author Indunil Moremda
 * javascript to select all the check boxes
 * when clicking on one check box  */


/**
 * fieldName is the name of the dynamicaly growing checkboxes
 */
var fieldName = 'index';
/**
 * @param frm   is the form name of the check box containing jsp
 */
function selectall(frm) {
    var frm1 = document.getElementById(frm);
    var i = frm1.elements.length;
    var e = document.frm.elements;
    var name = new Array();
    var value = new Array();
    var j = 0;
    for (var k = 0; k < i; k++)
    {
        if (frm1.elements[k].name == fieldName)
        {
            if (frm1.elements[k].checked == true) {
                value[j] = frm1.elements[k].value;
                j++;
            }
        }
    }
    checkSelect(frm);
}
function selectCheck(obj, frm)
{   var frm1=document.getElementById(frm);
    var i = frm1.elements.length;
    for (var k = 0; k < i; k++)
    {
        if (frm1.elements[k].name == fieldName)
        {
            frm1.elements[k].checked = obj;
        }
    }
    selectall(frm);
}

function selectallMe(frm)
{   frm1=document.getElementById(frm);
    if (frm1.allCheck.checked == true)
    {
        selectCheck(true,frm);
    }
    else
    {
        selectCheck(false,frm);
    }
}
function checkSelect(frm)
{   frm1=document.getElementById(frm);
    var i = frm1.elements.length;
    var berror = true;
    for (var k = 0; k < i; k++)
    {
        if (frm1.elements[k].name == fieldName)
        {
            if (frm1.elements[k].checked == false)
            {
                berror = false;
                break;
            }
        }
    }
    if (berror == false)
    {
        frm1.allCheck.checked = false;
    }
    else
    {
        frm1.allCheck.checked = true;
    }
}


