/** @author Indunil Moremda
 * javascript to select all the check boxes
 * when clicking on one check box  */


var fieldName = 'index';

function selectall(form, allCheck) {
    var e = form.elements;
    var name = new Array();
    var value = new Array();
    var j = 0;
    for (var k = 0; k < i; k++)
    {
        if (form.elements[k].name == fieldName)
        {
            if (form.elements[k].checked == true) {
                value[j] = form.elements[k].value;
                j++;
            }
        }
    }
    checkSelect(form, allCheck);
}
function selectCheck(obj, form, allCheck)
{
    var i = form.elements.length;
    for (var k = 0; k < i; k++)
    {
        if (form.elements[k].name == fieldName)
        {
            form.elements[k].checked = obj;
        }
    }
    selectall(form, allCheck);
}

function selectallMe(form, allCheck)
{
    if (allCheck.checked == true)
    {
        selectCheck(true, form, allCheck);
    }
    else
    {
        selectCheck(false, form);
    }
}
function checkSelect(form, allCheck)
{
    var i = form.elements.length;
    var berror = true;
    for (var k = 0; k < i; k++)
    {
        if (form.elements[k].name == fieldName)
        {
            if (form.elements[k].checked == false)
            {
                berror = false;
                break;
            }
        }
    }
    if (berror == false)
    {
        alert("hello");
        document.birth_register_approval_body.allCheck.checked = false;//allCheck.checked = false;
    }
    else
    {
        alert("hello1");
        document.birth_register_approval_body.allCheck.checked = true;
    }
}