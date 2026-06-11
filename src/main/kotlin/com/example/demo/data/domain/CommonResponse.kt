package com.example.demo.data.domain

import org.springframework.http.ResponseEntity

data class CommonResponse<T>(
    val code: Int = 0,
    val msg: String = "",
) {
    companion object {
        fun <T> success(): CommonResponse<T> =
            CommonResponse(
                code = ResultCode.OK.code,
                msg = ResultCode.OK.msg,
            )

        fun <T> fail(result: ResultCode): CommonResponse<T> =
            CommonResponse(
                code = result.code,
                msg = result.msg,
            )

        fun toResponseEntity(commonResponse: CommonResponse<*>): ResponseEntity<*> = ResponseEntity.status(commonResponse.code).body(commonResponse.msg)

    }
}