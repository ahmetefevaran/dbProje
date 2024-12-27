package org.example.db_project;

public class TestResults {
    private String testName;
    private String testDate;
    private String testResult;
    private String doctorName;
    private String patientName;

    // Constructor
    public TestResults(String testName, String testDate, String testResult, String doctorName, String patientName) {
        this.testName = testName;
        this.testDate = testDate;
        this.testResult = testResult;
        this.doctorName = doctorName;
        this.patientName = patientName;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
