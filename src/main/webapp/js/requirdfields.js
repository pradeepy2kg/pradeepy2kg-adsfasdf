/** @author Janith Widarshana
 * javascript to select all the required textboxes are filled */

function checkrequirdfields(id)
{
    var select=document.getElementById(id);
    var checked = false;
    
               if ( checked ) {
                  alert ( 'Do you want skip some fields' );
                  el.focus ( );
                  return false;
               }
}
function check(m){
    
     var array;
     var el = document.getElementById(m);

        if ( !el.value) {
           alert ( 'Please fill out the ' + el.name + ' field' );
           el.focus();

           return false;
        }
        else
        return true;
   
}


