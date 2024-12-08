package com.kshrd.kroya_api.exception.exceptionValidateInput;

import com.kshrd.kroya_api.exception.constand.FieldBlankExceptionHandler;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Validation {
    public void ValidationPhoneNumber(String phoneNumber) {
        String pNumber = "^0.*$";
        Pattern phoneNumberPattern = Pattern.compile(pNumber);
        Matcher phoneNumberMatcher = phoneNumberPattern.matcher(phoneNumber);
        if (!phoneNumberMatcher.matches()) {
            throw new FieldBlankExceptionHandler("Phone number must be start with 0 and not allow to input character, special character or more than 11 digits. ");
        }
    }

    public void ValidationDate(String inputDate) {
        Integer dayEarchMonth;
        String monthWithString = null;
        String test = "^\\d+\\-\\d+\\-\\d+$";
        Pattern pattern1 = Pattern.compile(test);
        Matcher matcher1 = pattern1.matcher(inputDate);
        if (!matcher1.matches()) {
            throw new FieldBlankExceptionHandler(("Date of birth is not correct.Example yyyy-mm-dd"));
        }
        String[] parts = inputDate.split("-");
        Integer day = Integer.parseInt(parts[2]);
        Integer month = Integer.parseInt(parts[1]);
        Integer year = Integer.parseInt(parts[0]);
        Integer lengthofyear = year.toString().length();

        if (month == 1) {
            monthWithString = "January";
        } else if (month == 2) {
            monthWithString = "February";
        } else if (month == 3) {
            monthWithString = "March";
        } else if (month == 4) {
            monthWithString = "April";
        } else if (month == 5) {
            monthWithString = "May";
        } else if (month == 6) {
            monthWithString = "June";
        } else if (month == 7) {
            monthWithString = "July";
        } else if (month == 8) {
            monthWithString = "August";
        } else if (month == 9) {
            monthWithString = "September";
        } else if (month == 10) {
            monthWithString = "October";
        } else if (month == 11) {
            monthWithString = "November";
        } else if (month == 12) {
            monthWithString = "December";
        }

        Integer verifydate = year % 4;
        if (verifydate == 0) {
            if (month == 2 && (day > 29 || day <= 0)) {
                throw new FieldBlankExceptionHandler(("Day is not correct, because February, " + year + ", has only 29 days.Example format yyyy-mm-dd"));
            }
        } else {
            if (month == 2 && (day > 28 || day <= 0)) {
                throw new FieldBlankExceptionHandler(("Day is not correct, because February, " + year + ", has only 28 days.Example format yyyy-mm-dd"));
            }
        }

        if (month <= 7 && month > 0) {
            dayEarchMonth = month % 2;
            if (dayEarchMonth == 0) {
                if (day <= 0 || day > 30) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", hss only 30 days.Example format yyyy-mm-dd"));
                }
            } else {
                if (day <= 0 || day > 31) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", has only 31 days.Example format yyyy-mm-dd"));
                }
            }
        }
        if (month > 7) {
            dayEarchMonth = month % 2;
            if (dayEarchMonth == 0) {
                if (day <= 0 || day > 31) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", has only 31 days.Example format yyyy-mm-dd"));
                }
            } else {
                if (day <= 0 || day > 30) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", has only 30 days.Example format yyyy-mm-dd"));
                }
            }
        }
        if (month >= 13 || month <= 0) {
            throw new FieldBlankExceptionHandler(("Month is not correct,because month of year have only 12 month so can input less then 13 and more then 0.Example format yyyy-mm-dd"));
        }
        if (lengthofyear >= 5) {
            throw new FieldBlankExceptionHandler(("Year is not correct,because allow input only less then 5 digit."));
        }
//            String date = "(^0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4}$)";
        String date = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(date);
        Matcher matcher = pattern.matcher(inputDate);
        if (!matcher.matches()) {
            throw new FieldBlankExceptionHandler(("Date of birth is not correct.Example yyy-mm-dd"));
        }

        LocalDate dateFromInput = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();
        if (dateFromInput.compareTo(currentDate) >= 0) {
            throw new FieldBlankExceptionHandler(("Not allow to input the current date or future date."));
        }
    }

    public void ValidationDatePost(String inputDate) {
        String monthWithString = null;
        Integer dayEarchMonth;
        String test = "^\\d+\\-\\d+\\-\\d+$";
        Pattern pattern1 = Pattern.compile(test);
        Matcher matcher1 = pattern1.matcher(inputDate);
        if (!matcher1.matches()) {
            throw new FieldBlankExceptionHandler(("Date should be yyyy-mm-dd. Example: 2023-06-24"));
        }
        String[] parts = inputDate.split("-");
        Integer day = Integer.parseInt(parts[2]);
        Integer month = Integer.parseInt(parts[1]);
        Integer year = Integer.parseInt(parts[0]);
        Integer lengthofyear = year.toString().length();

        if (month == 1) {
            monthWithString = "January";
        } else if (month == 2) {
            monthWithString = "February";
        } else if (month == 3) {
            monthWithString = "March";
        } else if (month == 4) {
            monthWithString = "April";
        } else if (month == 5) {
            monthWithString = "May";
        } else if (month == 6) {
            monthWithString = "June";
        } else if (month == 7) {
            monthWithString = "July";
        } else if (month == 8) {
            monthWithString = "August";
        } else if (month == 9) {
            monthWithString = "September";
        } else if (month == 10) {
            monthWithString = "October";
        } else if (month == 11) {
            monthWithString = "November";
        } else if (month == 12) {
            monthWithString = "December";
        }

        if (parts[2].length() != 2 || parts[1].length() != 2) {
            throw new FieldBlankExceptionHandler(("Date is not correct.Example yyyy-mm-dd"));
        }

        Integer verifydate = year % 4;
        if (verifydate == 0) {
            if (month == 2 && (day > 29 || day <= 0)) {
                throw new FieldBlankExceptionHandler(("Day is not correct, because February, " + year + ", has only 29 days."));
            }
        } else {
            if (month == 2 && (day > 28 || day <= 0)) {
                throw new FieldBlankExceptionHandler(("Day is not correct, because February, " + year + ", has only 28 days."));
            }
        }
        if (month <= 7 && month > 0) {
            dayEarchMonth = month % 2;
            if (dayEarchMonth == 0) {
                if (day <= 0 || day > 30) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", hss only 30 days"));
                }
            } else {
                if (day <= 0 || day > 31) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", has only 31 days"));
                }
            }
        }
        if (month > 7) {
            dayEarchMonth = month % 2;
            if (dayEarchMonth == 0) {
                if (day <= 0 || day > 31) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", has only 31 days"));
                }
            } else {
                if (day <= 0 || day > 30) {
                    throw new FieldBlankExceptionHandler(("Day is not correct, because " + monthWithString + ", " + year + ", has only 30 days"));
                }
            }
        }
        if (month >= 13 || month <= 0) {
            throw new FieldBlankExceptionHandler(("Month is not correct,because month of year have only 12 month so can input less then 13 and more then 0"));
        }
        if (lengthofyear >= 5) {
            throw new FieldBlankExceptionHandler(("Year is not correct,because allow input only less then 5 digit."));
        }
        String date = "^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(date);
        Matcher matcher = pattern.matcher(inputDate);
        if (!matcher.matches()) {
            throw new FieldBlankExceptionHandler(("Date is not correct.Example yyyy-mm-dd"));
        }
    }

    public void ValidationEmail(String inputEmail) {
        String email = "^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$";
        Pattern pattern1 = Pattern.compile(email);
        Matcher matcher1 = pattern1.matcher(inputEmail);
        if (!matcher1.matches()) {
            throw new FieldBlankExceptionHandler("🚫 Oops! Your email seems incorrect. Please use the format: example@gmail.com 🌈");
        }
    }

    public void ValidationPassword(String inputPassword) {
        String password = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
        Pattern pattern2 = Pattern.compile(password);
        Matcher matcher2 = pattern2.matcher(inputPassword);
        if (!matcher2.matches()) {
            throw new FieldBlankExceptionHandler(("Password must be more then 8 digit with characters, number and special characters!"));
        }
    }

    public void ValidationInputOnlyText(String inputText, String feil) {
        String text = "^[a-zA-Z]*$";
        Pattern pattern2 = Pattern.compile(text);
        Matcher matcher2 = pattern2.matcher(inputText);
        if (!matcher2.matches() || !(inputText.equals("Male") || inputText.equals("Female"))) {
            throw new FieldBlankExceptionHandler((feil + "is can not allow input with special character or number, Allow input only Female and Male"));
        }
    }

    public void ValidationInputUserName(String inputUserName) {
        String username1 = "^[a-zA-Z_\\s\\W]+$";
        Pattern pattern3 = Pattern.compile(username1);
        Matcher matcher3 = pattern3.matcher(inputUserName);
        if (!matcher3.matches()) {
            throw new FieldBlankExceptionHandler(("Username allow input only text."));
        }
    }

    public void ValidationInputConfirmPincode(String Inputpincode) {
        String pincode = "^[0-9]{6}$";
        Pattern pattern2 = Pattern.compile(pincode);
        Matcher matcher2 = pattern2.matcher(Inputpincode);
        if (!matcher2.matches()) {
            throw new FieldBlankExceptionHandler(("Pincode must be input only number 6 digit."));
        }
    }

    public void ValidateInputNumber(String InputNumber) {
        String number = "^\\d+$";
        Pattern pattern2 = Pattern.compile(number);
        Matcher matcher2 = pattern2.matcher(InputNumber);
        if (!matcher2.matches()) {
            throw new FieldBlankExceptionHandler(("Category_id allow input with number only!"));
        }
    }

    // General validation to check if input is not blank or null
    public void validateNotBlank(String field, String fieldName) {
        if (field == null || field.trim().isEmpty()) {
            throw new FieldBlankExceptionHandler(fieldName + " cannot be blank.");
        }
    }

    // Example of validating phone number with custom validation
    public void validatePhoneNumber(String phoneNumber) {
        String pNumber = "^0.*$";
        Pattern phoneNumberPattern = Pattern.compile(pNumber);
        Matcher phoneNumberMatcher = phoneNumberPattern.matcher(phoneNumber);
        if (!phoneNumberMatcher.matches()) {
            throw new FieldBlankExceptionHandler("Phone number must start with 0 and cannot contain special characters or more than 11 digits.");
        }
    }

    // Example of a validation for username
    public void validateUserName(String inputUserName) {
        if (inputUserName == null || inputUserName.trim().isEmpty()) {
            throw new FieldBlankExceptionHandler("Username cannot be blank.");
        }
        String usernamePattern = "^[a-zA-Z_\\s\\W]+$";
        Pattern pattern = Pattern.compile(usernamePattern);
        Matcher matcher = pattern.matcher(inputUserName);
        if (!matcher.matches()) {
            throw new FieldBlankExceptionHandler("Username must only contain text characters.");
        }
    }

    // Method to validate LocalDateTime and convert it to ZonedDateTime for Phnom Penh time zone
    public void validationDateWithLocalDateTime(LocalDateTime inputDateTime) {
        try {
            // Convert LocalDateTime to ZonedDateTime in Phnom Penh time zone
            ZonedDateTime inputZonedDateTime = inputDateTime.atZone(ZoneId.of("Asia/Phnom_Penh"));

            // Get the current time in Phnom Penh (UTC+7)
            ZonedDateTime currentDateTimeInPhnomPenh = ZonedDateTime.now(ZoneId.of("Asia/Phnom_Penh"));

            // Check if the input date-time is in the future compared to Phnom Penh current time
            if (!inputZonedDateTime.isAfter(currentDateTimeInPhnomPenh)) {
                throw new FieldBlankExceptionHandler("Date and time should be in the future.");
            }

        } catch (DateTimeParseException e) {
            // Handle parsing error and provide a meaningful message
            throw new FieldBlankExceptionHandler("Invalid LocalDateTime format.");
        }
    }

}
