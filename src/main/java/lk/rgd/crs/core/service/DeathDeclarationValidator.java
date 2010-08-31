package lk.rgd.crs.core.service;

import lk.rgd.AppConstants;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.domain.DeathRegister;
import lk.rgd.crs.api.bean.UserWarning;
import lk.rgd.crs.api.dao.DeathRegisterDAO;

import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.text.MessageFormat;

/**
 * a class to do validations in death declarations
 */

public class DeathDeclarationValidator {

    private static final ResourceBundle rb_si =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_EN);

    /**
     * validate minimul requirment against the filled date in the death declaration form
     *
     * @param deathRegister the death declaration form
     */
    public void validateMinimalRequirments(DeathRegister deathRegister) {

    }

    /**
     * validate standeard requrimnets such as
     * inserting same death persons PIN or NIC in two unique entries,
     *
     * @param deathRegisterDAO the DeathRegisterDAO
     * @param deathRegister    the death deaclaration form to validate
     * @param user             the user initiating the validation.
     * @return a list of warnings issued against the death declaration
     */
    public List<UserWarning> validateStandardRequirements(
            DeathRegisterDAO deathRegisterDAO, DeathRegister deathRegister, User user) {
        // create a holder to capture any warnings
        List<UserWarning> warnings = new ArrayList<UserWarning>();

        // select locale for user
        ResourceBundle rb = rb_en;
        if (AppConstants.SINHALA.equals(user.getPrefLanguage())) {
            rb = rb_si;
        } else if (AppConstants.TAMIL.equals(user.getPrefLanguage())) {
            rb = rb_ta;
        }

        //check is there any duplications in PIN
        String deathPersonPinOrNIC = deathRegister.getDeathPerson().getDeathPersonPINorNIC();
        List deathsWithSamePin = deathRegisterDAO.getDeathRegisterByDeathPersenPINorNIC(deathPersonPinOrNIC);
        if (deathsWithSamePin.size() > 0) {
            UserWarning w = new UserWarning(MessageFormat.format(rb.getString("same_death_person_pin_found_in_previous_recode"), deathPersonPinOrNIC));
            w.setSeverity(UserWarning.Severity.WARN);
            warnings.add(w);
        }

        return warnings;
    }

}
