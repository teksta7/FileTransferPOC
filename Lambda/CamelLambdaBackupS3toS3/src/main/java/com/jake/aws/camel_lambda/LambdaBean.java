package com.jake.aws.camel_lambda;

public class LambdaBean {

	public String log(String msg) {
        String data = "Message printed from route: " + msg;
        System.out.println("Triggered bean ok with "+ data);
        return data;
    }
}
