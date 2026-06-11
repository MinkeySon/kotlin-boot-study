package com.example.demo.data.domain

enum class ResultCode (
    val code: Int,
    val msg: String,
){
    OK(200, "ok"),
    BAD_REQUEST(400, "bad request"),
    NOT_FOUND(404, "not found"),
    FAIL(500, "fail"),
}