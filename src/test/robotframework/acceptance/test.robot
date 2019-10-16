*** Settings ***
Library  Telnet
Library  BuiltIn
Test Template  Letter Switching Test

*** Variables ***
${IP}  127.0.0.1
${PORT}  8100

*** Test Cases ***          MESSAGE                         EXPECTED
Regular Message             someinput           we were here again!

*** Keywords ***
Letter Switching Test
    [Arguments]     ${message}       ${expected}
    Open Connection  host=${IP}  port=${PORT}
    ${response} =  Write  ${message}
    Should Contain  ${response}  ${expected}
    Close Connection


