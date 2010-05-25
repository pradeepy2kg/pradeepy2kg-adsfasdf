/** @author Indunil Moremda
 * javascript to select all the check boxes
 * when clicking on one check box  */


var fieldName='index';

function selectall(){
  var i=document.birth_register_approval_body.elements.length;
  var e=document.birth_register_approval_body.elements;
  var name=new Array();
  var value=new Array();
  var j=0;
  for(var k=0;k<i;k++)
  {
    if(document.birth_register_approval_body.elements[k].name==fieldName)
    {
      if(document.birth_register_approval_body.elements[k].checked==true){
        value[j]=document.birth_register_approval_body.elements[k].value;
        j++;
      }
    }
  }
  checkSelect();
}
function selectCheck(obj)
{
 var i=document.birth_register_approval_body.elements.length;
  for(var k=0;k<i;k++)
  {
    if(document.birth_register_approval_body.elements[k].name==fieldName)
    {
      document.birth_register_approval_body.elements[k].checked=obj;
    }
  }
  selectall();
}

function selectallMe()
{
  if(document.birth_register_approval_body.allCheck.checked==true)
  {
   selectCheck(true);
  }
  else
  {
    selectCheck(false);
  }
}
function checkSelect()
{
 var i=document.birth_register_approval_body.elements.length;
 var berror=true;
  for(var k=0;k<i;k++)
  {
    if(document.birth_register_approval_body.elements[k].name==fieldName)
    {
      if(document.birth_register_approval_body.elements[k].checked==false)
      {
        berror=false;
        break;
      }
    }
  }
  if(berror==false)
  {
    document.birth_register_approval_body.allCheck.checked=false;
  }
  else
  {
    document.birth_register_approval_body.allCheck.checked=true;
  }
}
