package com.tiny.cash.loan.card.net.request.params;


import java.io.Serializable;

public class UserInfoParams implements Serializable {

    String accountId;
    String firstName;
    String middleName;
    String lastName;
    String bvn;
    String birthday;
    String gender;
    String email;
    String home_state;
    String home_area;
    String home_address;
    public static UserInfoParams createParams(String accountId, String firstName, String middleName, String lastName,
                                              String bvn, String birthday,String gender, String emailAddress
            , String home_state,String home_area, String home_address) {
        UserInfoParams params = new UserInfoParams();
        params.accountId = accountId;
        params.firstName = firstName;
        params.middleName = middleName;
        params.lastName = lastName;
        params.bvn = bvn;
        params.birthday = birthday;
        params.gender = gender;
        params.email = emailAddress;
        params.home_state = home_state;
        params.home_area = home_area;
        params.home_address = home_address;
        return params;
    }
}
