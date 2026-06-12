package com.example.demo.data.domain

import org.springframework.http.ResponseEntity

data class CommonResponse<T>(
    val code: Int = 0,
    val msg: String = "",
    val data: T? = null,
) {
    companion object {

        fun <T> success(): CommonResponse<T> = success(null)

        fun <T> success(data: T?): CommonResponse<T> =    // ② 추가
            CommonResponse(
                code = ResultCode.OK.code,
                msg = ResultCode.OK.msg,
                data = data,
            )

        fun <T> fail(result: ResultCode): CommonResponse<T> =
            CommonResponse(
                code = result.code,
                msg = result.msg,
            )

        fun <T> unauthorized(): CommonResponse<T> = CommonResponse(
            code = ResultCode.UNAUTHORIZED.code,
            msg = ResultCode.UNAUTHORIZED.msg,
        )

        fun toResponseEntity(commonResponse: CommonResponse<*>): ResponseEntity<*> =
            ResponseEntity.status(commonResponse.code).body(commonResponse)

    }
}