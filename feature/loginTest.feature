
Feature: login and logout

Scenario: Successful Login with Valid Credentials
	Given User is on EmailTypeList Page
	When User Navigate to 163 Mail Login Page
	And user enters UserName and Password
	Then Message displayed Login Successfully