package com.edsoft.LM.pojo;

import lombok.Data;

@Data
public class UserPasswordChangePojo {
    public String name;
    public String oldPassword;
    public String newPassword;
    public String newPasswordConfirm;
}
