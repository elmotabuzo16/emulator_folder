package com.vitalityactive.va.login;

public class CreateUserInstructionServiceRequest {
    public UserInstruction instructions[];
    public String userId;

    public CreateUserInstructionServiceRequest(String userId, String type, String effectiveFrom) {
        instructions = new UserInstruction[]{
                new UserInstruction(type, effectiveFrom)
        };
        this.userId = userId;
    }

    public static class UserInstruction {
        public String type;
        public String effectiveFrom;

        public UserInstruction(String type, String effectiveFrom) {
            this.type = type;
            this.effectiveFrom = effectiveFrom;
        }
    }
}
