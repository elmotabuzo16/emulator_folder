package com.vitalityactive.va;

import com.vitalityactive.va.utilities.ValidationPatterns;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class RegexTest {

    @SuppressWarnings("unused")
    private Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, true, "james@smith.com"},
                {2, true, "james01@smith.com"},
                {3, true, "John@smith.com"},
                {4, true, "JSCAPS@smith.com"},
                {5, true, "1234567890@test.com"},
                {6, true, "JOHN01@smith.com"},
                {7, true, "John01@smith.com"},
                {8, true, "john+01@smith.com"},
                {9, true, "john#01@smth.com"},
                {10, true, "john#01@smth.com"},
                {11, true, "john$100@smith.com"},
                {12, true, "john%01@smith.com"},
                {13, true, "john&lt01@smith.com"},
                {14, true, "johnlt;01@smith.com"},
                {15, true, "john*01@smith.com"},
                {16, true, "john-01@smith.com"},
                {17, true, "john/01@smith.com"},
                {18, true, "john=01@smith.com"},
                {19, true, "john?01@smith.com"},
                {20, true, "john^01@smith.com"},
                {21, true, "john`01@smith.com"},
                {22, true, "john{01}@smith.com"},
                {23, true, "john|01@smith.com"},
                {24, true, "john~01@smith.com"},
                {25, true, "js@smith.com"},
                {26, false, ".john@smith.com"},
                {27, false, "john.@smith.com"},
                {28, false, "john..jones@smith.com"},
                {29, true, "\".john\"@smith.com"},
                {30, true, "\"john.\"@smith.com"},
                {31, true, "\"john..jones\"@smith.com"},
                {32, true, "\"abcdefg\"@testing.com"},
                {33, true, "abc.\"def\".ghi@testing.com"},
                {34, false, "\"abc\"\"def\"ghi@testing.com"},
                {35, true, "bugs.\"space jam\".bunny@wb.com"},
                {36, true, "ed4DZ3rS3Zul0KfnJFsLh9uyg5BhaoLxxEUzXuXNejL4pGnyvHbYqvKngh4mQSnT@test.com"},
                {37, false, "OuLIqoiXMxuOuMyyRMhTia3icDVJ3QZgb3h5nHpdxIzkhQfdGRwP8VHxPsvWDaZud@test.com"},
                {38, true, "john@smith.com"},
                {39, true, "john@SMITH.com"},
                {40, true, "john@12345.net"},
                {41, true, "john@Smith21.org"},
//                {42, true, ""}, // Edge
//                {43, true, ""}, // Edge
                {44, false, "xy@pxIxStlDKhjZiPulrHtAsyHWhrHtfjCpgNeUgAdMTkBLqSzkiRVwrldyEpETIhAYfBXiJXyUfXXzVNfeJsQDbrZtGdXQIQTfcajnJFwHtCeQmfCAxbKUQHcqvNgoPqvu.com"},
                {45, true, "user@mail.box.com"},
                {46, true, "user@domain.mail.box.com"},
                {47, true, "user@sub.domain.mail.box.com"},
                {48, true, "user@buried.sub.domain.mail.com"},
                {49, true, "ax@aQEnDJDSoIowLwSoDEjCNcmhJJvlgLSZHcmTPLAzJDuXPudvUjsjoZGiryRaKvh.tdyAnxLzIfbvQYxrfRsOwCyLVyoIyTaoarTZtmQQalYHoNcvIUphMETQunehtjZ.YIifpMmxhCItrVfvOUPokpHKMgKwimUsLpMhiajDEkNiHcbXVdwEzWfRUWNEfGS.hZKHoDQxUljYNcBEfEakGReRZeSPkCgIrEFozGmXOehfkuDbVQsMcnz.com"},
                {50, true, "user@mail-box.com"},
                {51, false, "user@-mailbox.com"},
                {52, false, "user@mailbox-.com"},
                {53, true, "jsmith@[192.168.2.1]"},
                {54, true, "jsmith@[IPv6:2001:db8::1]"},
                {55, true, "abc(comment)@abc.com"},
                {56, true, "abc@(comment)abc.com"},
                {57, true, "abc@abc(comment).com"},
                {58, true, "abc@testing.COM"},
                {59, true, "abc@testing.za"},
                {60, true, "user@test.XN--VERMGENSBERATUNG-PWB"},
                {61, true, "abc@testing.(comment)com"},
                {62, true, "abc@testing.com(comment)"},
                {63, false, "lohn..lones@smith.com"},
                {64, true, "\".lohn\"@smith.com"},
                {65, true, "\"lohn.\"@smith.com"},
                {66, true, "\"lohn..lones\"@smith.com"},
                {67, true, "\"obcdofg\"@testing.com"},
                {68, true, "obo.\"def\".ghi@testing.com"},
                {69, true, "baks.\"space jam\".bunny@wb.com"},
                {70, true, "jsmolh@[192.168.2.1]"},
                {71, true, "jsmolh@[IPv6:2001:db8::1]"},
                {72, true, "olc(comment)@abc.com"},
                {73, true, "anb@(comment)abc.com"},
                {74, true, "okc@abc(comment).com"},
                {75, true, "qpc@testing.(comment)com"},
                {76, true, "acq@testing.com(comment)"},
//                {77, true, "test@example.c(with comment)om"},
//                {78, true, "methis is a comment@exam(with comment)ple.com"},
//                {79, true, "methis is a comment@exam(withcomment)ple.com"},
//                {80, true, "me(this is a comment)@example.com"},
//                {81, true, "!#$%&‘*+-/=.?^_`{|}~@[IPv6:0123:4567:89AB:CDEF:0123:4567:89AB:CDEF]"},
//                {82, true, "!#$%&‘*+-/=.?^_`{|}~@[1.0.0.127]"},
//                {83, true, "me.example@com"},
//                {84, true, "simon@QyysWFJhcRuCQQmfiUFK6VJUIvCQwTMhGAuH2cD9xbBaL2KqDJbTdKJOYEJPqH9l4O7SbEH5SJVL3ctYxQlG5eUQTfzFK1HnEbSrJvyyhjXKNgtkdDc1OYQ0orkVTTTpLLUL5FFNqKJLwXjlXU1YvpginmDm8r6bkR1OvbcTzmtPULKeQ9qRue9vAvpVTsXk5WSW5YxvVqkFl4mtud5KGgIsfjRMRph1lW8OoK5fTYdyyZ5MjV1JCeBreGR.com"},
//                {85, true, "simon@VAO2UvjUszgQivcJykdNcuHFfKIMRdpU5EoQyxIyF1quRr7gJfyc758moqXQmnONJvmS6pRMWMlwMxIYLCoqJ6Lvc8NAXGAuqDwBdjsXeksS69t2wcEtBthUEuyT4cv1GXLo2loP7k7b1CJpDWpJNC6zf2X9ad8AZYM3qT8SeIjOyA9vi3YDtz0Umeo0jVkdsM4p7EnSYsTAWCnCkEx6sxJyyhSEaxDBJRud0nEe147Ap5TehWKN.com"},
//                {86, true, "a@b.c"},
//                {87, true, "a@b.cc"},
//                {88, true, "l@gmail.com"}
        });
    }

    @Test
    @Parameters(method="data")
    @TestCaseName("{0} = {1}")
    public void regex_validates_email_address_correctly(@SuppressWarnings("unused") int testCaseNumber, boolean expectedResult, String emailAddress) {
        assertEquals(expectedResult, ValidationPatterns.EMAIL_ADDRESS.matcher(emailAddress).matches());
    }
}
