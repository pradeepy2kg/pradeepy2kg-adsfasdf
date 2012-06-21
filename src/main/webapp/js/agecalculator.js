/**
 *  @author Duminda Dharmakeerthi
 *
 *  Calculate the age between the Birthday and the Given day.
 *  Returns an array containing the age from birth day to the given date.
 *  age[0] = Years
 *  age[1] = Months
 *  age[2] = Days
 *
 *  Consider a month has 30 Days.
 */


function calculateAge(birthDate, currentDate){
    // Get Birth Year, Month and Day
    var birthYear = birthDate.getYear();
    var birthMonth = birthDate.getMonth();
    var birthDay = birthDate.getDate();

    // Get Year, Month and Day to be calculate the age.
    var currentYear = currentDate.getYear();
    var currentMonth = currentDate.getMonth();
    var currentDay = currentDate.getDate();

    // Define age years, months and days to be zero.
    var age = [0, 0, 0];

    if(currentDate > birthDate){
        if(currentYear >= birthYear){
            age[0] = (currentYear - birthYear);
            if(currentMonth > birthMonth){
                age[1] = (currentMonth - birthMonth);
                if(currentDay >= birthDay){
                    age[2] = (currentDay - birthDay);
                }else if(currentDay < birthDay){
                    age[1] = (age[1] - 1);
                    age[2] = ((30 - birthDay) + currentDay);
                }
            }else if(currentMonth < birthMonth){
                age[0] = (age[0] > 0)?(age[0] - 1):0;
                age[1] = ((12 - birthMonth) + currentMonth);
                if(currentDay >= birthDay){
                    age[2] = (currentDay - birthDay);
                }else if(currentDay < birthDay){
                    age[1] = (age[1] - 1);
                    age[2] = ((30 - birthDay) + currentDay);
                }
            }else if(currentMonth == birthMonth){
                age[1] = 0;
                if(currentDay >= birthDay){
                    age[2] = (currentDay - birthDay);
                }else if(currentDay < birthDay){
                    age[0] = (age[0] > 0)?(age[0] - 1):0;
                    age[2] = ((30 - birthDay) + currentDay);
                }
            }
        }
    }
    return age;
}