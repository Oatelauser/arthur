package com.broadtech.arthur.admin.common.execption;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Machenike
 * @version 1.0.0
 * @date 2022/9/9
 */


public class BaseException extends RuntimeException {
    @Getter
    public String message;

    private BaseException(String message) {
        super(message);
        this.message = message;
    }

    public static class NotFindTokenException extends BaseException {
        private static final String NOT_TOKEN = "The token cannot be obtained, or the obtained token is empty";

        private NotFindTokenException() {
            super(NOT_TOKEN);
        }

        private NotFindTokenException(String message) {
            super(message == null ? NOT_TOKEN : message);
        }
        public static NotFindTokenException of(){
            return new NotFindTokenException();
        }
        public static NotFindTokenException of(String message){
            return new NotFindTokenException(message);
        }
    }


    public static class NotGetUserException extends BaseException {

        private static final String NOT_SUPPORTED_GET_USER = "Getting logged in users from session is not supported";

        private NotGetUserException() {
            super(NOT_SUPPORTED_GET_USER);
        }

        private NotGetUserException(String message) {
            super(message == null ? NOT_SUPPORTED_GET_USER : message);
        }

        public static NotGetUserException of(){
            return new NotGetUserException();
        }
        public static NotGetUserException of(String message){
            return new NotGetUserException(message);
        }

    }

    public static class UserInsertFailException extends BaseException {

        private static final String USER_INSERT_FALL = "User insert failed";

        private UserInsertFailException() {
            super(USER_INSERT_FALL);
        }

        private UserInsertFailException(String message) {
            super(message == null ? USER_INSERT_FALL : message);
        }

        public static UserInsertFailException of(){
            return new UserInsertFailException();
        }
        public static UserInsertFailException of(String message){
            return new UserInsertFailException(message);
        }
    }


    public static class UserUpdateFailException extends BaseException {

        private static final String USER_UPDATE_FALL = "User update failed";

        private UserUpdateFailException() {
            super(USER_UPDATE_FALL);
        }

        private UserUpdateFailException(String message) {
            super(message == null ? USER_UPDATE_FALL : message);
        }

        public static UserUpdateFailException of(){
            return new UserUpdateFailException();
        }
        public static UserUpdateFailException of(String message){
            return new UserUpdateFailException(message);
        }
    }


    public static class UserDeleteFailException extends BaseException {

        private static final String USER_DELETE_FALL = "User delete failed";

        private UserDeleteFailException() {
            super(USER_DELETE_FALL);
        }

        private UserDeleteFailException(String message) {
            super(message == null ? USER_DELETE_FALL : message);
        }

        public static UserDeleteFailException of(){
            return new UserDeleteFailException();
        }
        public static UserDeleteFailException of(String message){
            return new UserDeleteFailException(message);
        }

    }


    public static class RepeatedSubmitException extends BaseException {

        private static final String REPEATED_SUBMIT = "Repeated Submit";

        private RepeatedSubmitException() {
            super(REPEATED_SUBMIT);
        }

        private RepeatedSubmitException(String message) {
            super(message == null ? REPEATED_SUBMIT : message);
        }

        public static RepeatedSubmitException of(){
            return new RepeatedSubmitException();
        }
        public static RepeatedSubmitException of(String message){
            return new RepeatedSubmitException(StrUtil.format("{} : {}",REPEATED_SUBMIT,message));
        }
    }


}
