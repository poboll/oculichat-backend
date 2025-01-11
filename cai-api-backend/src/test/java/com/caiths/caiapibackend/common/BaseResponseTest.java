//package com.caiths.caiapibackend.common;
//
//import com.caiths.caiapibackend.model.entity.User;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class BaseResponseTest {
//
//    @Test
//    void testSuccessResponseWithData() {
//        User user = new User("john_doe", "ADMIN");
//        BaseResponse<User> response = BaseResponse.success(user);
//
//        assertEquals(200, response.getCode());
//        assertEquals(user, response.getData());
//        assertEquals("请求成功", response.getMessage());
//    }
//
//    @Test
//    void testSuccessResponseWithoutData() {
//        BaseResponse<Void> response = BaseResponse.success();
//
//        assertEquals(200, response.getCode());
//        assertNull(response.getData());
//        assertEquals("请求成功", response.getMessage());
//    }
//
//    @Test
//    void testErrorResponse() {
//        ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;
//        BaseResponse<Void> response = BaseResponse.error(errorCode);
//
//        assertEquals(errorCode.getCode(), response.getCode());
//        assertNull(response.getData());
//        assertEquals(errorCode.getMessage(), response.getMessage());
//    }
//
//    @Test
//    void testCustomResponse() {
//        BaseResponse<String> response = BaseResponse.of(400, "参数错误");
//
//        assertEquals(400, response.getCode());
//        assertNull(response.getData());
//        assertEquals("参数错误", response.getMessage());
//    }
//
//    @Test
//    void testAllArgsConstructor() {
//        BaseResponse<String> response = new BaseResponse<>(201, "Data", "创建成功");
//
//        assertEquals(201, response.getCode());
//        assertEquals("Data", response.getData());
//        assertEquals("创建成功", response.getMessage());
//    }
//
//    @Test
//    void testTwoArgsConstructor() {
//        BaseResponse<String> response = new BaseResponse<>(200, "Data");
//
//        assertEquals(200, response.getCode());
//        assertEquals("Data", response.getData());
//        assertEquals("", response.getMessage());
//    }
//}
