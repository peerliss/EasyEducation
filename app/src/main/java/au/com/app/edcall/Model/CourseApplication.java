package au.com.app.edcall.Model;

public class CourseApplication {
    private String name, surname, fullname, email, number, dob, gender, countryBirth, street, suburb, state, postCode, currentAddress;
    private String homeCountryStreet, homeCountrySuburb, homeCountryState, homeCountryPostCode, homeCountry, homeCountryAddress;
    private String usi;
    private String passportNumber, passportExpiryDate, countryCitizenship;
    private String visaType, visaSubclass, visaExpiryDate, visaNumber;
    private String englishLevel, englishTest, englishTestDate, englishTestResults, englishTestMainLanguageSpoken;
    private String furtherStudies, furtherStudiesCourse, furtherStudiesInstitution, furtherStudiesCommencementDate;
    private String highestQualification, highestQualificationName, highestQualificationInstitution, highestQualificationCountryName;
    private String highestQualification2Name, highestQualification2Institution, highestQualification2CountryName;
    private String highestQualification3Name, highestQualification3Institution, highestQualification3CountryName;
    private String issues, issuesMedical, issuesLegal, issuesVisa, issuesAAT, issuesOther;
    private String issuesMedicalDetails, issuesLegalDetails, issuesVisaDetails, issuesAATDetails, issuesOtherDetails;
    private String healthInsurance, healthInsuranceProviderName, healthInsuranceMembershipNumber,healthInsuranceExpiryDate;
    private String currentlyStudying, currentQualificationName, currentQualificationInstitution;
    private String applyingForRPL, nameChanged, previousName, employmentStatus;
    private String institutionName, institutionCricos, courseName, courseCode;
    private String applicationStatus, uid;

    public CourseApplication() {

    }

    public CourseApplication(String name, String surname, String fullname, String email, String number, String dob, String gender, String countryBirth, String street, String suburb, String state, String postCode, String currentAddress, String homeCountryStreet, String homeCountrySuburb, String homeCountryState, String homeCountryPostCode, String homeCountry, String homeCountryAddress, String usi, String passportNumber, String passportExpiryDate, String countryCitizenship, String visaType, String visaSubclass, String visaExpiryDate, String visaNumber, String englishLevel, String englishTest, String englishTestDate, String englishTestResults, String englishTestMainLanguageSpoken, String furtherStudies, String furtherStudiesCourse, String furtherStudiesInstitution, String furtherStudiesCommencementDate, String highestQualification, String highestQualificationName, String highestQualificationInstitution, String highestQualificationCountryName, String highestQualification2Name, String highestQualification2Institution, String highestQualification2CountryName, String highestQualification3Name, String highestQualification3Institution, String highestQualification3CountryName, String issues, String issuesMedical, String issuesLegal, String issuesVisa, String issuesAAT, String issuesOther, String issuesMedicalDetails, String issuesLegalDetails, String issuesVisaDetails, String issuesAATDetails, String issuesOtherDetails, String healthInsurance, String healthInsuranceProviderName, String healthInsuranceMembershipNumber, String healthInsuranceExpiryDate, String currentlyStudying, String currentQualificationName, String currentQualificationInstitution, String applyingForRPL, String nameChanged, String previousName, String employmentStatus, String institutionName, String institutionCricos, String courseName, String courseCode, String applicationStatus, String uid) {
        this.name = name;
        this.surname = surname;
        this.fullname = fullname;
        this.email = email;
        this.number = number;
        this.dob = dob;
        this.gender = gender;
        this.countryBirth = countryBirth;
        this.street = street;
        this.suburb = suburb;
        this.state = state;
        this.postCode = postCode;
        this.currentAddress = currentAddress;
        this.homeCountryStreet = homeCountryStreet;
        this.homeCountrySuburb = homeCountrySuburb;
        this.homeCountryState = homeCountryState;
        this.homeCountryPostCode = homeCountryPostCode;
        this.homeCountry = homeCountry;
        this.homeCountryAddress = homeCountryAddress;
        this.usi = usi;
        this.passportNumber = passportNumber;
        this.passportExpiryDate = passportExpiryDate;
        this.countryCitizenship = countryCitizenship;
        this.visaType = visaType;
        this.visaSubclass = visaSubclass;
        this.visaExpiryDate = visaExpiryDate;
        this.visaNumber = visaNumber;
        this.englishLevel = englishLevel;
        this.englishTest = englishTest;
        this.englishTestDate = englishTestDate;
        this.englishTestResults = englishTestResults;
        this.englishTestMainLanguageSpoken = englishTestMainLanguageSpoken;
        this.furtherStudies = furtherStudies;
        this.furtherStudiesCourse = furtherStudiesCourse;
        this.furtherStudiesInstitution = furtherStudiesInstitution;
        this.furtherStudiesCommencementDate = furtherStudiesCommencementDate;
        this.highestQualification = highestQualification;
        this.highestQualificationName = highestQualificationName;
        this.highestQualificationInstitution = highestQualificationInstitution;
        this.highestQualificationCountryName = highestQualificationCountryName;
        this.highestQualification2Name = highestQualification2Name;
        this.highestQualification2Institution = highestQualification2Institution;
        this.highestQualification2CountryName = highestQualification2CountryName;
        this.highestQualification3Name = highestQualification3Name;
        this.highestQualification3Institution = highestQualification3Institution;
        this.highestQualification3CountryName = highestQualification3CountryName;
        this.issues = issues;
        this.issuesMedical = issuesMedical;
        this.issuesLegal = issuesLegal;
        this.issuesVisa = issuesVisa;
        this.issuesAAT = issuesAAT;
        this.issuesOther = issuesOther;
        this.issuesMedicalDetails = issuesMedicalDetails;
        this.issuesLegalDetails = issuesLegalDetails;
        this.issuesVisaDetails = issuesVisaDetails;
        this.issuesAATDetails = issuesAATDetails;
        this.issuesOtherDetails = issuesOtherDetails;
        this.healthInsurance = healthInsurance;
        this.healthInsuranceProviderName = healthInsuranceProviderName;
        this.healthInsuranceMembershipNumber = healthInsuranceMembershipNumber;
        this.healthInsuranceExpiryDate = healthInsuranceExpiryDate;
        this.currentlyStudying = currentlyStudying;
        this.currentQualificationName = currentQualificationName;
        this.currentQualificationInstitution = currentQualificationInstitution;
        this.applyingForRPL = applyingForRPL;
        this.nameChanged = nameChanged;
        this.previousName = previousName;
        this.employmentStatus = employmentStatus;
        this.institutionName = institutionName;
        this.institutionCricos = institutionCricos;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.applicationStatus = applicationStatus;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getCountryBirth() {
        return countryBirth;
    }

    public String getStreet() {
        return street;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getState() {
        return state;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public String getHomeCountryStreet() {
        return homeCountryStreet;
    }

    public String getHomeCountrySuburb() {
        return homeCountrySuburb;
    }

    public String getHomeCountryState() {
        return homeCountryState;
    }

    public String getHomeCountryPostCode() {
        return homeCountryPostCode;
    }

    public String getHomeCountry() {
        return homeCountry;
    }

    public String getHomeCountryAddress() {
        return homeCountryAddress;
    }

    public String getUsi() {
        return usi;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getPassportExpiryDate() {
        return passportExpiryDate;
    }

    public String getCountryCitizenship() {
        return countryCitizenship;
    }

    public String getVisaType() {
        return visaType;
    }

    public String getVisaSubclass() {
        return visaSubclass;
    }

    public String getVisaExpiryDate() {
        return visaExpiryDate;
    }

    public String getVisaNumber() {
        return visaNumber;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public String getEnglishTest() {
        return englishTest;
    }

    public String getEnglishTestDate() {
        return englishTestDate;
    }

    public String getEnglishTestResults() {
        return englishTestResults;
    }

    public String getEnglishTestMainLanguageSpoken() {
        return englishTestMainLanguageSpoken;
    }

    public String getFurtherStudies() {
        return furtherStudies;
    }

    public String getFurtherStudiesCourse() {
        return furtherStudiesCourse;
    }

    public String getFurtherStudiesInstitution() {
        return furtherStudiesInstitution;
    }

    public String getFurtherStudiesCommencementDate() {
        return furtherStudiesCommencementDate;
    }

    public String getHighestQualification() {
        return highestQualification;
    }

    public String getHighestQualificationName() {
        return highestQualificationName;
    }

    public String getHighestQualificationInstitution() {
        return highestQualificationInstitution;
    }

    public String getHighestQualificationCountryName() {
        return highestQualificationCountryName;
    }

    public String getHighestQualification2Name() {
        return highestQualification2Name;
    }

    public String getHighestQualification2Institution() {
        return highestQualification2Institution;
    }

    public String getHighestQualification2CountryName() {
        return highestQualification2CountryName;
    }

    public String getHighestQualification3Name() {
        return highestQualification3Name;
    }

    public String getHighestQualification3Institution() {
        return highestQualification3Institution;
    }

    public String getHighestQualification3CountryName() {
        return highestQualification3CountryName;
    }

    public String getIssues() {
        return issues;
    }

    public String getIssuesMedical() {
        return issuesMedical;
    }

    public String getIssuesLegal() {
        return issuesLegal;
    }

    public String getIssuesVisa() {
        return issuesVisa;
    }

    public String getIssuesAAT() {
        return issuesAAT;
    }

    public String getIssuesOther() {
        return issuesOther;
    }

    public String getIssuesMedicalDetails() {
        return issuesMedicalDetails;
    }

    public String getIssuesLegalDetails() {
        return issuesLegalDetails;
    }

    public String getIssuesVisaDetails() {
        return issuesVisaDetails;
    }

    public String getIssuesAATDetails() {
        return issuesAATDetails;
    }

    public String getIssuesOtherDetails() {
        return issuesOtherDetails;
    }

    public String getHealthInsurance() {
        return healthInsurance;
    }

    public String getHealthInsuranceProviderName() {
        return healthInsuranceProviderName;
    }

    public String getHealthInsuranceMembershipNumber() {
        return healthInsuranceMembershipNumber;
    }

    public String getHealthInsuranceExpiryDate() {
        return healthInsuranceExpiryDate;
    }

    public String getCurrentlyStudying() {
        return currentlyStudying;
    }

    public String getCurrentQualificationName() {
        return currentQualificationName;
    }

    public String getCurrentQualificationInstitution() {
        return currentQualificationInstitution;
    }

    public String getApplyingForRPL() {
        return applyingForRPL;
    }

    public String getNameChanged() {
        return nameChanged;
    }

    public String getPreviousName() {
        return previousName;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public String getInstitutionCricos() {
        return institutionCricos;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getUid() {
        return uid;
    }
}
