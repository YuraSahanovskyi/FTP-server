package org.example.authentication;

import lombok.Data;

@Data
public class Permission {
    private Integer id;
    private String path;
    private boolean read;
    private boolean write;
    private boolean execute;
    private User user;
}
